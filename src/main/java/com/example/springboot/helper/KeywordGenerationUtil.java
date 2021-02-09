package com.example.springboot.helper;

import java.util.*;

public class KeywordGenerationUtil {
	@SuppressWarnings("rawtypes")
	public static List<String> generateKeywordsFromImage(String fileType, String file, String bias) {
		List<String> keywordsArray = new ArrayList<String>();
		//Extracting Text from Image
		String extractedText = OcrUtil.extractTextFromImage(file);

		//Process Extracted Text
		String processedText = ExtractTextUtil.processExtractedText(extractedText);

		if (!processedText.equals("") && !processedText.equals(null) && processedText.length() > 0) {

			// Count the frequency of words and add it to Keywords_HashSet set
			HashSet<String> keywords_HashSet = new HashSet<>();
			Hashtable<String, Integer> words = ExtractTextUtil.countWords(processedText);
			for (Map.Entry singleWord : words.entrySet()) {
				if ((Integer) singleWord.getValue() > 2) {
					keywords_HashSet.add(singleWord.getKey().toString());
				}
			}			
			
			//Add bias to keywords
			if(bias != "" && bias != null && bias != "\\s+") {
				if (bias.contains(",")) {
					bias = bias.toLowerCase();
					String[] TextArray = bias.split(",");
					List<String> TextList = Arrays.asList(TextArray);
					keywords_HashSet.addAll(TextList);
				}
				else {
					keywords_HashSet.add(bias);
				}
			}			
			
			String keywords = keywords_HashSet.toString().replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\\s+","");
			
			if (!keywords.equals("") && !keywords.equals(null) && keywords.length() > 0) {

				// Converting File Type to Lower Case
				fileType = fileType.toLowerCase();

				// Update Property File
				Properties prop = PropertyFileUtil.loadKeywordsPropertFile();
				if (prop.containsKey(fileType)) {
					String keywords_String = PropertyFileUtil.readPropertyFromKeywordsFile(fileType.toLowerCase());
					HashSet<String> keywords_HashSet_New = ExtractTextUtil.convertStringToHashSet(keywords_String);
					String[] newKeywords_StringArray = keywords.split(",");
					List<String> newKeywords_List = Arrays.asList(newKeywords_StringArray);
					keywords_HashSet_New.addAll(newKeywords_List);
					String newKeywords = keywords_HashSet_New.toString().replaceAll("\\[", "").replaceAll("\\]", "");
					PropertyFileUtil.updateKeywords(fileType, newKeywords);
				} else {
					PropertyFileUtil.updateKeywords(fileType, keywords);
				}
				keywordsArray = Arrays.asList(keywords.split(","));
			}
		}
		return keywordsArray;
	}

}