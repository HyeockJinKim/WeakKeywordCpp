import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Utils {

    private Utils() {
    }

    static String execCommand(String cmd) {
        try {
            Process process = Runtime.getRuntime().exec(cmd);
            BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;

            StringBuffer bf = new StringBuffer();
            while ((line = br.readLine()) != null) {
                bf.append(line);
                bf.append("\n");
            }
            return bf.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}