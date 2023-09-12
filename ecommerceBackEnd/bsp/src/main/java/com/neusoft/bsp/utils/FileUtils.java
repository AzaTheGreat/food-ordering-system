package com.neusoft.bsp.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;


public class FileUtils {

    /**
     *
     * @param file
     * @param path
     * @param fileName
     * @return
     */
    public static boolean upload(MultipartFile file, String path, String fileName){


//        String realPath = path + "/" + FileNameUtils.getFileName(fileName);


        String realPath = path + "/" + fileName;

        File dest = new File(realPath);


        if(!dest.getParentFile().exists()){
            dest.getParentFile().mkdir();
        }

        try {

            file.transferTo(dest);
            return true;
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }

    }
}