package processing;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

public class ReadFile {
    private static String baseDir;
    private static String log;
    private static ArrayList<String> logList;

    public static void readArgs(String[] args) throws IOException, IndexOutOfBoundsException {
        if (args.length > 0) {
            String path = null;
            for (int i = 0; i < args.length; ++i) {
                if (args[i].startsWith("-")) {
                    readOption(args[i], args[i+1]);
                    ++i;
                } else {
                    if (path == null)
                        path = Paths.get(args[i]).toString();
                    else
                        throw new IOException("Path must be entered only once !!");
                }
            }
            if (path == null)
                throw new IOException("NO File Input");
            if (baseDir == null) {
                baseDir = path;
            }

            if (log == null) {
                if (new File(baseDir).isDirectory()) {
                    log = Paths.get(baseDir, "log").toString();
                } else {
                    log = Paths.get(Paths.get(baseDir).getParent().toString(), "log").toString();
                }
            }
            readLog();
            ReadFile.recursiveReadDirectory(path);
        } else {
            throw new IOException("No Args Input");
        }


    }

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

    private static void readOption(String arg1, String arg2) {
        switch (arg1.substring(1)) {
            case "-basedir":
            case "b":
                baseDir = arg2;
                break;
            case "-log":
            case "l":
                log = arg2;
                break;
            default:
                break;
        }
    }

    private static void log(String fileName) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(log, true))) {
            bw.write(fileName);
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void convertCcFile(String filePath) {
//        if (logList.contains(filePath)) {
//            return ;
//        }
        try {
            Converter converter = new Converter(filePath);
            converter.convert();
            log(filePath);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("No input path.");
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        // TODO : Out of memory problem !
    }

    private static void recursiveReadDirectory(String dirPath) throws IOException {
        File dir = new File(dirPath);
        if (!dir.isDirectory()) {
            if (dir.isFile()) {
                convertCcFile(dirPath);
                return ;
            }
            System.out.println("No Directory: " + dir.getPath());
            return ;
        }
        for (File file: Objects.requireNonNull(dir.listFiles())) {
            if (file.isDirectory()) {
                recursiveReadDirectory(file.getPath());
            } else if (file.getPath().endsWith(".cc")
                    || file.getPath().endsWith(".cpp")
                    || file.getPath().endsWith(".c++")
                    || file.getPath().endsWith(".h")) {
                convertCcFile(file.getPath());
            }
        }
    }

    public static String getBaseDir() {
        return baseDir;
    }
}
