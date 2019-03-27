package processing.util;

import weakclass.CppClass;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;

public class IO {
    public static boolean isDebug = false;
    private static String baseDir = null;
    private static String log = null;
    private static String out = null;
    private static String info = null;

    private IO() {
    }


    static String addBasePath(String filePath) {
        return Paths.get(baseDir, filePath).toString();
    }

    public static void copyFile(String filePath) throws IOException {
        String outPath = getOutPath(filePath);
        if (new File(outPath).exists())
            return ;

        makeDictionaries(outPath);
        Files.copy(new File(addBasePath(filePath)).toPath(), new File(outPath).toPath());
    }

    public static void setDebug() {
        IO.isDebug = true;
    }

    public static void setLogDir(String log) {
        if (IO.log == null)
            IO.log = Paths.get(log).toAbsolutePath().toString().replace("/./", "/");
    }

    public static void setInfoDir(String info) {
        if (IO.info == null)
            IO.info = Paths.get(info).toAbsolutePath().toString().replace("/./", "/");
    }

    public static void setOutDir(String out) {
        if (IO.out == null)
            IO.out = Paths.get(out).toAbsolutePath().toString().replace("/./", "/");
    }

    public static void setBaseDir(String baseDir) {
        if (IO.baseDir == null)
            IO.baseDir = Paths.get(baseDir).toAbsolutePath().toString().replace("/./", "/");
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
        filePath = filePath.replace(IO.baseDir, "");
        return Paths.get(info, filePath).toString()
                .replace(".cc", ".info")
                .replace(".c++", ".info")
                .replace(".cpp", ".info")
                .replace(".h", ".hinfo")
                .replace(".h++", ".hinfo")
                .replace(".hh", ".hinfo")
                .replace(".hpp", ".hinfo");
    }

    public static String getRelativePath(String filePath) {
        return new File(filePath).getAbsolutePath()
                .replace(IO.baseDir, "")
                .replace("/./", "/");
    }

    static String getOutPath(String filePath) {
        filePath = filePath.replace(IO.baseDir, "");
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
        String out = getOutPath(filePath);
        makeDictionaries(out);
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(out))) {
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
        filePath = getClassInfoFilePath(filePath);
        makeDictionaries(filePath);
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filePath))) {
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
