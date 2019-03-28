package processing;

import processing.util.IO;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Objects;

public class ReadFile {
    private static ArrayList<String> passList;

    /**
     * Read Args for setting
     * @param args Args for setting path, baseDir, LogDir
     * @throws IOException Args's length less than 1 or No path input
     */
    public static void read(String[] args) throws IOException {
        if (args.length > 0) {
            String path = readArgs(args);
            if (path == null)
                throw new IOException("NO File Input");

            setArgsDefault(path);
            passList = IO.readLog();
            readDirectoryRecursively(path);
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
    private static String readArgs(String[] args) throws IOException, IndexOutOfBoundsException {
        String path = null;
        for (int i = 0; i < args.length; ++i) {
            if (args[i].startsWith("-")) {
                setOption(args[i], args.length != i+1 ? args[i+1] : args[i]);
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
        IO.setDefaultDir(path);

        if (new File(path).isDirectory()) {
            IO.setLogDir(Paths.get(path, "writeLog").toString());
        } else {
            IO.setLogDir(Paths.get(Paths.get(path).getParent().toString(), "writeLog").toString());
        }
    }


    /**
     * Read Args and set options
     *
     * basedir: module's base directory
     * log: log directory
     *
     * @param arg1 Option's type
     * @param arg2 Option's value
     */
    private static void setOption(String arg1, String arg2) {
        switch (arg1.substring(1)) {
            case "-basedir":
            case "b":
                IO.setBaseDir(arg2);
                break;
            case "-debug":
            case "d":
                IO.setDebug();
                break;
            case "-info":
            case "i":
                IO.setInfoDir(arg2);
                break;
            case "-log":
            case "l":
                IO.setLogDir(arg2);
                break;
            case "-out":
            case "o":
                IO.setOutDir(arg2);
                break;
            default:
                break;
        }
    }

    /**
     * Convert .cc file to make safe in virtual table
     * @param filePath File path for converting
     */
    private static void convertCcFile(String filePath) {
        if (passList.contains(filePath))
            return ;

        try {
            Converter converter = new Converter(filePath);
            if (converter.convert())
                IO.writeLog(filePath);
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
    private static void readDirectoryRecursively(String dirPath) throws IOException {
        File dir = new File(dirPath);
        if (!dir.isDirectory()) {
            checkCCFile(dir);
            return ;
        }
        for (File file: Objects.requireNonNull(dir.listFiles())) {
            if (file.isDirectory())
                readDirectoryRecursively(file.getPath());
            else
                checkCCFile(file);
        }
    }

    private static boolean isDebugCheck(String fileName) {
        return IO.isDebug &&
                (fileName.endsWith("-expected.cpp")
                        || fileName.endsWith("-expected.c++")
                        || fileName.endsWith("-expected.cc"));
    }

    /**
     * Check this file is .cc file and convert it
     * @param file File for checking
     * @throws IOException No file input
     */
    private static void checkCCFile(File file) throws IOException {
        if (file.getPath().endsWith(".cc")
                || file.getPath().endsWith(".cpp")
                || file.getPath().endsWith(".c++")
                || file.getPath().endsWith(".h")) {
            if (isDebugCheck(file.getName()))
                return ;
            System.out.println(file.getPath());
            convertCcFile(file.getPath());
        } else {
            IO.copyFile(file.getPath());
        }
    }
}
