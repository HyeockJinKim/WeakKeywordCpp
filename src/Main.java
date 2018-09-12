import processing.ReadFile;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            ReadFile.readArgs(args);
        } catch (IOException | IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }
}
