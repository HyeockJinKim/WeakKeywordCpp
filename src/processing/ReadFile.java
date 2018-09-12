package processing;

import java.io.*;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Optional;

public class ReadFile {
    private static int count = 1;
    private static String baseDir;
    private static String log;
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

            ReadFile.recursiveReadDirectory(path);
        } else {
            throw new IOException("No Args Input");
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

    private static void readCcFile(String filePath) throws IOException {
        try {
            System.out.println(filePath +" : " + count);
            Optional<String> text = Converter.parse(filePath);
            text.ifPresent(x -> {
                ++count;
                System.out.println(x);
            });
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("No input path.");
        } catch (NullPointerException e) {
            System.out.println(filePath);
        }
        // TODO : Out of memory problem !
    }

    private static void recursiveReadDirectory(String dirPath) throws IOException {
        File dir = new File(dirPath);
        if (!dir.isDirectory()) {
            if (dir.isFile()) {
                readCcFile(dirPath);
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
                readCcFile(file.getPath());
            }
        }
    }
}
