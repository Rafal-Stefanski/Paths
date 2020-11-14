package pl.rafalstefanski;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class Main {

    public static void main(String[] args) {

//        DirectoryStream.Filter<Path> filter =
//                new DirectoryStream.Filter<Path>() {
//                    public boolean accept(Path path) throws IOException {
//                        return (Files.isRegularFile(path));
//                    }
//                };

        // Lambda equivalent. Lambdas in section 16
        DirectoryStream.Filter<Path> filter = p -> Files.isRegularFile(p);

        Path directory = FileSystems.getDefault().getPath("FileTree" + File.separator + "Dir2");    // With File.separator
//        Path directory = FileSystems.getDefault().getPath("FileTree\\Dir2");                          // With hardcoded separator.
        try (DirectoryStream<Path> contents = Files.newDirectoryStream(directory, filter)) {
            for (Path file : contents) {
                System.out.println(file.getFileName());
            }
        } catch (IOException | DirectoryIteratorException e) {
            System.out.println(e.getMessage());
        }

        String separator = File.separator;
        System.out.println(separator);
        separator = FileSystems.getDefault().getSeparator();
        System.out.println(separator);

        try {
            Path tempFile = Files.createTempFile("mvapp", ".appext");
            System.out.println("Temporary files path = " + tempFile.toAbsolutePath());

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        Iterable<FileStore> stores = FileSystems.getDefault().getFileStores();
        for (FileStore store : stores) {
            System.out.println("Volume name/Drive letter = " + store);
            System.out.println("file store = " + store.name());
        }

        System.out.println("*******************");
        Iterable<Path> rootPaths = FileSystems.getDefault().getRootDirectories();
        for (Path path : rootPaths) {
            System.out.println(path);
        }

        System.out.println("--- Walking Tree for Dir2 ---");
        Path dir2Path = FileSystems.getDefault().getPath("FileTree" + File.separator + "Dir2");
        try {
            Files.walkFileTree(dir2Path, new PrintNames());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("--- Copy Dir2 to Dir4/Dir2Copy ---");
        Path copyPath = FileSystems.getDefault().getPath("FileTree" + File.separator + "Dir4" + File.separator + "Dir2Copy");
        // FileTree/Dir4/Dir2Copy
        try {
            Files.walkFileTree(dir2Path, new CopyFiles(dir2Path, copyPath));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        File file = new File("C:\\Users\\RS\\IdeaProjects\\Paths\\Examples\\file.txt");
        Path convertedPath = file.toPath();
        System.out.println("convertedPath = " + convertedPath);

        File parent = new File("C:\\Examples");
        File resolvedFile = new File(parent, "dir\\file.txt");
        System.out.println(resolvedFile.toPath());

        resolvedFile = new File("C:\\Examples", "dir\\file.txt");
        System.out.println(resolvedFile.toPath());

        Path parentPath = Paths.get("C:\\Examples");
        Path childRelativePath = Paths.get("dir\\file.txt");
        System.out.println(parentPath.resolve(childRelativePath));

        File workingDirectory = new File("").getAbsoluteFile();
        System.out.println("Working directory = " + workingDirectory.getAbsolutePath());

        System.out.println("--- print Dir2 contents using list() ---");
        File dir2File = new File(workingDirectory, "\\FileTree\\Dir2");
        String[] dir2Contents = dir2File.list();
        for (int i = 0; i < dir2Contents.length; i++) {
            System.out.println("i = " + i + ": " + dir2Contents[i]);
        }

        System.out.println("--- print Dir2 contents using listFiles() ---");
        File[] dir2Files = dir2File.listFiles();
        for (int i = 0; i < dir2Files.length; i++) {
            System.out.println("i = " + i + ": " + dir2Files[i].getName());
        }
    }
}


//    public static void main(String[] args) {
//        try {
//
////            Path fileToCreate = FileSystems.getDefault().getPath("Examples", "file2.txt");
////            Files.createFile(fileToCreate);
////            Path dirToCreate = FileSystems.getDefault().getPath("Examples", "Dir4");
////            Files.createDirectory(dirToCreate);
////            Path dirToCreate = FileSystems.getDefault().getPath("Examples", "Dir2\\Dir3\\Dir4\\Dir5\\Dir6");
//
////            Path dirToCreate = FileSystems.getDefault().getPath("Examples\\Dir2\\Dir3\\Dir4\\Dir5\\Dir6\\Dir7");
////            Files.createDirectories(dirToCreate);
//
//            Path filePath = FileSystems.getDefault().getPath("Examples", "Dir1\\file1.txt");
//            long size = Files.size(filePath);
//            System.out.println("Size = " + size);
//            System.out.println("Last modified = " + Files.getLastModifiedTime(filePath));
//
//
//            BasicFileAttributes attrs = Files.readAttributes(filePath, BasicFileAttributes.class);
//            System.out.println("Size = " + attrs.size());
//            System.out.println("Last modified = " + attrs.lastModifiedTime());
//            System.out.println("Created = " + attrs.creationTime());
//            System.out.println("Is directory = " + attrs.isDirectory());
//            System.out.println("Is regular file = " + attrs.isRegularFile());
//
//        //            Path fileToDelete = FileSystems.getDefault().getPath("Examples", "Dir1", "file1copy.txt");
//        //            Files.deleteIfExists(fileToDelete);
//
//        } catch (IOException e) {
//            System.out.println(e.getMessage());
//        }
//
//    }
//
////        Path path = FileSystems.getDefault().getPath("WorkingDirectoryFile.txt");
////        printFile(path);
////
//////        Path filePath = FileSystems.getDefault().getPath("files", "SubdirectoryFile.txt");
////        Path filePath = Paths.get(".", "files", "SubdirectoryFile.txt");
////        printFile(filePath);
////
////        filePath = Paths.get("C:\\Users\\RS\\IdeaProjects\\OutThere.txt");
////        printFile(filePath);
////
////        filePath = Paths.get(".");
////        System.out.println(filePath.toAbsolutePath());
////
////        // "C:\Users\.\RS\..\IdeaProjects
////        // "C:\Users\RS\IdeaProjects
////
////        Path path2 = FileSystems.getDefault().getPath(".", "files", "..", "files", "SubdirectoryFile.txt");
////        System.out.println(path2.normalize().toAbsolutePath());
////        printFile(path2.normalize());
////
////        Path path3 = FileSystems.getDefault().getPath("thisFileDoesntExists.txt");
////        System.out.println(path3.toAbsolutePath());
////
////        Path path4 = Paths.get("Z:\\", "VolumeDoesNotExists", "whatever.txt");
////        System.out.println("Exists = " + Files.exists(filePath));
////
////        System.out.println("Exists = " + Files.exists(path4));
////
////    }
////
////    private static void printFile(Path path) {
////        try (BufferedReader fileReader = Files.newBufferedReader(path)) {
////            String line;
////            while ((line = fileReader.readLine()) != null) {
////                System.out.println(line);
////            }
////        } catch (IOException e) {
////            System.out.println(e.getMessage());
////            e.printStackTrace();
////        }

