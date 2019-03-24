package processing.util;

import classinfo.Serializer;
import processing.ReadFile;
import weakclass.CppClass;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class IO {
    private static IO ourInstance = new IO();
    public static boolean isDebug = false;
    private static String baseDir = null;
    private static String log = null;
    private static String out = null;
    private static String info = null;
    public static IO getInstance() {
        return ourInstance;
    }

    private IO() {
    }

    public static void setDebug() {
        IO.isDebug = true;
    }

    public static void setLogDir(String log) {
        if (IO.log == null)
            IO.log = log;
    }

    public static void setInfoDir(String info) {
        if (IO.info == null)
            IO.info = info;
    }

    public static void setOutDir(String out) {
        if (IO.out == null)
            IO.out = out;
    }

    public static void setBaseDir(String baseDir) {
        if (IO.baseDir == null) {
            IO.baseDir = baseDir;
        }
    }

    public static void setDefaultDir(String baseDir) {
        if (IO.baseDir == null)
            IO.baseDir = baseDir;
        setInfoDir(Paths.get(baseDir,"info").toString());
        setOutDir(Paths.get(baseDir, "out_folder").toString());
    }

    /**
     * Get class's information file's path
     * @param filePath File path to get class information
     * @return Class information file's path
     */
    public static String getClassInfoFilePath(String filePath) {
        String base = IO.baseDir.replace("./", "");
        return Paths.get(info, base, filePath).toString()
                .replace(".cc", ".info")
                .replace(".c++", ".info")
                .replace(".cpp", ".info");
    }

    static String getFullPath(String filePath) {
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
        if (!new File(log).exists() || isDebug)
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

    public static HashSet<CppClass> readClassInfo(String filePath) {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filePath))) {
            return (HashSet<CppClass>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Rewrite Cpp file to make safe in v-table
     * @param file .cc file
     */
    public static void rewriteCppFile(String filePath, String file) {
        makeDictionaries(getOutPath(filePath));
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(getOutPath(filePath)))) {
            bw.write(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void makeDictionaries(String filePath) {
        new File(filePath).getParentFile().mkdirs();
    }

    /**
     * Write class's information
     * @param filePath FilePath for logging
     * @param classSet Class Set used in cpp file
     */
    public static void writeClassInfo(String filePath, HashSet<CppClass> classSet) {
        makeDictionaries(getClassInfoFilePath(filePath));
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(getClassInfoFilePath(filePath)))) {
            out.writeObject(classSet);
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
        if (isDebug)
            return ;
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(log, true))) {
            bw.write(fileName);
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
