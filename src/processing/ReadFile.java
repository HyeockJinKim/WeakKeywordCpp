package processing;

import java.io.*;
import java.util.Objects;
import java.util.Optional;

public class ReadFile {
    private static int count = 1;

    public static void readCcFile(String filePath) throws IOException {
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

    public static void recursiveReadDirectory(String dirPath) throws IOException {
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