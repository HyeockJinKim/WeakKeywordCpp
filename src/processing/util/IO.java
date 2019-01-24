package processing.util;

import processing.ReadFile;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;

public class IO {
    private static IO ourInstance = new IO();
    private static String baseDir = null;
    private static String log = null;
    private static String out = null;
    public static IO getInstance() {
        return ourInstance;
    }

    private IO() {
    }

    public static void setLogDir(String log) {
        if (IO.log == null)
            IO.log = log;
    }

    public static void setBaseDir(String baseDir) {
        if (IO.baseDir == null) {
            IO.baseDir = baseDir;
            IO.out = Paths.get("out_folder").toString();
        }
    }

    /**
     * Get class's information file's path
     * @param filePath File path to get class information
     * @return Class information file's path
     */
    public static String getClassInfoFilePath(String filePath) {
        return Paths.get(baseDir, log, filePath+".info").toString();
    }

    public static String getFullPath(String filePath) {
        return Paths.get(baseDir, filePath).toString();
    }

    public static String getOutPath(String filePath) {
        return Paths.get(out, filePath).toString();
    }

    /**
     * Read written Log and add it to pass list
     */
    public static ArrayList<String> readLog() {
        ArrayList<String> passList = new ArrayList<>();
        if (!new File(log).exists())
            return passList;

        try (BufferedReader br = new BufferedReader(new FileReader(log))) {
            String path;
            while ((path = br.readLine()) != null) {
                passList.add(path);
            }
            System.out.println(passList.size() + " File Pass");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return passList;
    }

    /**
     * Rewrite Cpp file to make safe in v-table
     * @param file .cc file
     */
    public static void rewriteCppFile(String filePath, String file) {
        new File(Paths.get(out, filePath).toString()).getParentFile().mkdirs();
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(Paths.get(out, filePath).toString()))) {
            bw.write(file);
            System.out.println(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Write class's information as JSON
     * @param filePath FilePath for logging
     * @param classInfo Class information stored in JSON
     */
    public static void writeClassInfo(String filePath, String classInfo) {
        new File(getClassInfoFilePath(filePath)).getParentFile().mkdirs();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(getClassInfoFilePath(filePath)))) {
            bw.write(classInfo);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * FIXME: Make log directory and logging in the directory
     * Write Log
     * @param fileName File name for Logging
     */
    public static void writeLog(String fileName) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(log, true))) {
            bw.write(fileName);
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
