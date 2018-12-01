package processing;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Objects;

public class ReadFile {
    private static String baseDir;
    private static String log;
    private static ArrayList<String> logList;

    /**
     * Read Args for setting
     * @param args Args for setting path, baseDir, LogDir
     * @throws IOException Args's length less than 1 or No path input
     */
    public static void readArgs(String[] args) throws IOException {
        if (args.length > 0) {
            String path = readPath(args);

            if (path == null)
                throw new IOException("NO File Input");
            setArgsDefault(path);
            readLog();
            ReadFile.ReadDirectoryRecursively(path);
        } else {
            throw new IOException("No Args Input");
        }
    }

    /**
     * Read Path for setting path
     * @param args Args for setting path, option
     * @return File's path
     * @throws IOException No file path
     * @throws IndexOutOfBoundsException Last args is '-'
     */
    private static String readPath(String[] args) throws IOException, IndexOutOfBoundsException {
        String path = null;
        for (int i = 0; i < args.length; ++i) {
            if (args[i].startsWith("-")) {
                setOption(args[i], args[i+1]);
                ++i;
            } else {
                if (path == null)
                    path = Paths.get(args[i]).toString();
                else
                    throw new IOException("Path must be entered only once !!");
            }
        }
        return path;
    }

    /**
     * If option is null, set default value
     * @param path Default value for baseDir, writeLog
     */
    private static void setArgsDefault(String path) {
        if (baseDir == null) {
            baseDir = path;
        }

        if (log == null) {
            if (new File(baseDir).isDirectory()) {
                log = Paths.get(baseDir, "writeLog").toString();
            } else {
                log = Paths.get(Paths.get(baseDir).getParent().toString(), "writeLog").toString();
            }
        }
    }

    /**
     * Read writeLog and add it to pass list
     */
    private static void readLog() {
        logList = new ArrayList<>();
        if (!new File(log).exists()) {
            return ;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(log))) {
            String path;
            while ((path = br.readLine()) != null) {
                logList.add(path);
            }
            System.out.println(logList.size() + " File Pass");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Read Args and set options
     * @param arg1 Option's type
     * @param arg2 Option's value
     */
    private static void setOption(String arg1, String arg2) {
        switch (arg1.substring(1)) {
            case "-basedir":
            case "b":
                baseDir = arg2;
                break;
            case "-writeLog":
            case "l":
                log = arg2;
                break;
            default:
                break;
        }
    }

    /**
     * FIXME: Make directory and logging in the directory
     * Write Log
     * @param fileName File name for Logging
     */
    private static void writeLog(String fileName) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(log, true))) {
            bw.write(fileName);
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Convert .cc file to make safe in virtual table
     * @param filePath File path for converting
     */
    private static void convertCcFile(String filePath) {
        if (logList.contains(filePath)) {
            return ;
        }
        try {
            Converter converter = new Converter(filePath);
            converter.convert();
            writeLog(filePath);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("No input path.");
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        // TODO : Out of memory problem !
    }

    /**
     * Read directory recursively
     * @param dirPath Directory path for Reading
     * @throws IOException No file input
     */
    private static void ReadDirectoryRecursively(String dirPath) throws IOException {
        File dir = new File(dirPath);
        if (!dir.isDirectory()) {
            checkCCFile(dir);
            return ;
        }
        for (File file: Objects.requireNonNull(dir.listFiles())) {
            if (file.isDirectory())
                ReadDirectoryRecursively(file.getPath());
            else
                checkCCFile(file);
        }
    }

    /**
     * Check this file is .cc file
     * @param file File for checking
     * @throws IOException No file input
     */
    private static void checkCCFile(File file) throws IOException {
        if (file.getPath().endsWith(".cc")
                || file.getPath().endsWith(".cpp")
                || file.getPath().endsWith(".c++")
                || file.getPath().endsWith(".h")) {
            convertCcFile(file.getPath());
        }
    }

    /**
     * Get base directory
     * @return Base directory
     */
    public static String getBaseDir() {
        return baseDir;
    }
}
