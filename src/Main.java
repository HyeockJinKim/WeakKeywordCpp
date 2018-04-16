import java.io.FileNotFoundException;
import java.util.Optional;

public class Main {
    public static void main(String[] args) {
        try {
            Converter converter = new Converter(args);
            Optional<String> text = converter.parse();
            text.ifPresent(converter::writeCppFile);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("No input path.");
        } catch (FileNotFoundException e) {
            System.out.println("File format is not .cpp");
        }
    }
}
