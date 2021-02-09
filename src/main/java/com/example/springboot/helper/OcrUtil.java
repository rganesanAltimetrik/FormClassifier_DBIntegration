package com.example.springboot.helper;

import net.sourceforge.tess4j.Tesseract;
import java.io.File;

public class OcrUtil {
	
    public static String extractTextFromImage(String File){
        String extractedText = null;
        try {
            File image = new File(File);
            Tesseract tesseract = new Tesseract();
            tesseract.setDatapath("src/main/java/com/example/springboot/resources/trainedData");
            tesseract.setLanguage("eng");
            tesseract.setPageSegMode(1);
            tesseract.setOcrEngineMode(1);
            extractedText = tesseract.doOCR(image);
        }
        catch (Exception e){
            System.out.println("Exception - " + e);
        }
        return extractedText;
    }

}