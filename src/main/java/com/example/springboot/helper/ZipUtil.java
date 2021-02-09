package com.example.springboot.helper;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.apache.commons.io.FileUtils;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;

public class ZipUtil {
        public static String moveFilesToInputFolder(MultipartFile multipartFile, String inputPath)
        {
            String folderName = "";
            String fName = multipartFile.getOriginalFilename();
            String directoryName = "src/main/java/com/example/springboot/resources/temp/" + System.currentTimeMillis();
            try{
                File directory = new File(directoryName);
                if (! directory.exists()){
                    directory.mkdir();
                }
                String pathName = directoryName + "/" + fName;
                InputStream initialStream = multipartFile.getInputStream();
                byte[] buffer = new byte[initialStream.available()];
                initialStream.read(buffer);
                File file = new File(pathName);
                file.createNewFile();
                try (OutputStream outStream = new FileOutputStream(file)) {
                    outStream.write(buffer);
                }
                if(fName.toLowerCase().contains(".zip")) {
                    unZipFolderAndMoveToDestination(file,inputPath);
                    folderName = fName.replace(".zip", "");
                }
                else { copyFiles(directory,inputPath); }
            }
            catch (Exception e){e.printStackTrace();}
            return folderName;
        }
        public static void unZipFolderAndMoveToDestination(File file, String destination){
            try {
                ZipFile zipFile = new ZipFile(file);
                zipFile.extractAll(destination);
            } catch (ZipException e) {
                e.printStackTrace();
            }
        }
        public static void copyFiles(File file, String destination)
        {
            File dest = new File(destination);
            try{
                FileUtils.copyDirectory(file,dest);
            }
            catch (Exception e){e.printStackTrace();}
        }
    }

