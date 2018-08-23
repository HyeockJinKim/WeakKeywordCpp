import java.io.IOException;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) throws IOException {
        if (args.length > 0) {
            String path = Paths.get(args[0]).toString();
            ReadFile.recursiveReadDirectory(path);
        } else {
            System.out.println("No File Input ");
        }
    }
}
