import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Optional;

public class Main {
    public static void main(String[] args) throws IOException {
        String path = Paths.get(args[0]).toString();
        ReadFile.checkDirectory(path);
    }
}
