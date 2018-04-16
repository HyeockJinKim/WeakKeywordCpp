
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

public class Converter {
    private String filePath;
    private String folderPath;
    private String fileName;
    Converter(String[] paths) throws FileNotFoundException {
        setPath(paths[0]);
    }

    private void setPath(String paths) throws FileNotFoundException {
        Path path = Paths.get(paths);
        if (paths.contains("/")) {
            folderPath = path.getParent().toString();
        } else {
            folderPath = Paths.get(".").toString();
        }
        fileName = path.getFileName().toString();
        if (!"cpp".equals(fileName.substring(fileName.length()-3, fileName.length()))) {
            System.out.println("File format is not cpp file");
            throw new FileNotFoundException();
        }
        filePath = path.toString();
    }

    public Optional<String> parse() {

        return Optional.empty();
    }
// web kit
    public void writeCppFile(String text) {
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(Paths.get(folderPath, "_"+fileName).toString()));
            bufferedWriter.write(text);
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
