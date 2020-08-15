package utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * File utility class.
 *
 * Taken from Sean Grimes, sean@seanpgrimes.com
 */
public class FileUtils {
    public static List<String> parseAllFilenames(List<String> allFilePaths) {
        List<String> allFilenames = new ArrayList<>();

        // Add each filename to list.
        for (String s : allFilePaths)
            allFilenames.add(parseFilename(s));

        // Return all filenames.
        return allFilenames;
    }

    public static String parseFilename(String filePath) {
        // Get indices of filename substring.
        int beginIndex = filePath.lastIndexOf(File.separator) + 1;
        int endIndex = filePath.length();

        // Return filename.
        return filePath.substring(beginIndex, endIndex);
    }

    /**
     * Create a directory if one does not exist
     *
     * @param directoryPath The path to the directory to be created
     * @return True if success, else false
     */
    public static boolean createDirectory(String directoryPath) {
        File dir = new File(directoryPath);

        // Nothing exists here, create the directory and all parent directories
        if (!dir.exists())
            return dir.mkdirs();

        // Something exists at the supplied path, see if it's a directory. If it is,
        // return true. If it's not, it's something else so return false.
        return dir.isDirectory();
    }

    /**
     * Get a sorted list of all files in a directory
     *
     * @param path The path to the directory
     * @return The sorted list of paths
     */
    public static List<String> getAllFilePathsInDir(String path) {
        List<String> paths = new ArrayList<>();
        File[] files = new File(path).listFiles();

        if (files == null || files.length == 0)
            throw new IllegalStateException(path + " is empty");

        for (File f : files) {
            if (f.isFile())
                paths.add(f.getAbsolutePath());
        }

        return sortPaths(paths);
    }

    /**
     * Get a sorted list of all files in a directory
     *
     * @param path The path to the directory
     * @return The sorted list of paths
     */
    public static List<String> getAllFilenamesInDir(String path) {
        List<String> result = new ArrayList<>();
        File[] files = new File(path).listFiles();

        if (files == null || files.length == 0)
            throw new IllegalStateException(path + " is empty");

        for (File f : files) {
            if (f.isFile())
                result.add(f.getName());
        }

        return sortPaths(result);
    }

    /**
     * Sort strings *in place* using a custom comparator
     *
     * @param paths The list of strings to be sorted
     * @return The sorted list of strings
     */
    private static List<String> sortPaths(List<String> paths) {
        if (paths.isEmpty()) return Collections.emptyList();
        if (paths.size() == 1) return paths;

        Comparator<String> comp = Comparator.comparing((String x) -> x);
        paths.sort(comp);

        return paths;
    }

    public static boolean deleteFile(String path) throws IllegalArgumentException, SecurityException {
        // Test to make sure the path exists and is a file.
        File tst = new File(path);
        if (!tst.exists() || !tst.isFile())
            throw new IllegalArgumentException(path + " is invalid");

        return tst.delete();
    }
}
