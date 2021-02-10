package com.example.springboot.helper;

import com.example.springboot.model.Report;
import net.lingala.zip4j.ZipFile;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.*;
import java.util.Map.Entry;


public class FileClassifierUtil {
	
	public static HashMap<String, Report> classifyForms(String directory) {
		
		//Create Hashmap to store the filenames and type
		HashMap<String, Report> fileType = new HashMap<String, Report>();
		
		//Iterate the Files in directory
		try {
			List<File> fileList = iterateOverFiles(directory);
			Iterator<File> itr = fileList.iterator();
			while(itr.hasNext()) {
				File file = itr.next();			
				String filepath = file.toString();
				String fileName = file.getName().toString();
				HashMap<String, Object> complexityMap = ImageComplexityUtil.getImageComplexity(filepath);
				int noOfWords = (int) complexityMap.get("noOfWords");
				String complexity = complexityMap.get("complexity").toString();
				Report report = new Report(classifyForm(filepath), complexity, noOfWords);
				fileType.put(fileName, report);
			}
		}
		catch(Exception e) {
			System.out.println("Exception - " + e);
		}
			
		return fileType;
	}
	

	@SuppressWarnings("rawtypes")
	public static String classifyForm(String file) {

		// Read All Keywords from Keywords File
		Hashtable<String, List<String>> keywords = PropertyFileUtil.readAllKeywords();

		// Extracting Text from Image
		String extractedText = OcrUtil.extractTextFromImage(file);

		// Process Extracted Text
		String processedText = ExtractTextUtil.processExtractedText(extractedText);
		
		// Initailize File Type Variable
		String fileType = null;
		
		if (!processedText.equals("") && !processedText.equals(null) && processedText.length() > 0) {

			// Convert the Processed Text to List
			List<String> processedTextList =  new ArrayList<String>();
			Hashtable<String, Integer> words = ExtractTextUtil.countWords(processedText);
			for (Map.Entry singleWord : words.entrySet()) {
				int value = (Integer) singleWord.getValue();
				if (value > 1) {
					String word = singleWord.getKey().toString();
					for(int i=0; i<value; i++) {
						processedTextList.add(word);
					}
				}
			}

			// Comparing the Processed Text with Keywords
			Hashtable<String, Integer> matchingKeyWords = new Hashtable<>();
			Set<Map.Entry<String, List<String>>> entries = keywords.entrySet();
			Iterator<Map.Entry<String, List<String>>> itr = entries.iterator();
			while (itr.hasNext()) {
				Map.Entry<String, List<String>> entry = itr.next();
				String key = entry.getKey();
				List<String> value = new ArrayList<String>(entry.getValue());
				List<String> extractedTextList = new ArrayList<String>(processedTextList);
				//extractedTextList.retainAll(value);
				//matchingKeyWords.put(key, extractedTextList.size());
				int count = 0;
				for(int i=0; i<value.size(); i++) {
					for(int j=0; j<extractedTextList.size(); j++) {
						String keywordsValue = value.get(i).toString();
						String extractedTextValue = extractedTextList.get(j).toString();
						if (!extractedTextValue.equals("") && !extractedTextValue.equals(null) && extractedTextValue.length() > 0) {
							if(keywordsValue.matches( "(.*)" + extractedTextValue + "(.*)") || extractedTextValue.matches("(.*)" + keywordsValue + "(.*)")) {
								count = count+1;
							}
						}
					}					
				}
				matchingKeyWords.put(key, count);
			}

			// Finding Greater Number of Matching Keyword
			List<Integer> numberOfMatchingWords = new ArrayList<Integer>();
			Set<Entry<String, Integer>> matchingText = matchingKeyWords.entrySet();
			Iterator<Entry<String, Integer>> itr1 = matchingText.iterator();
			while (itr1.hasNext()) {
				Entry<String, Integer> entry = itr1.next();
				int value = entry.getValue();
				numberOfMatchingWords.add(value);
			}
			Collections.sort(numberOfMatchingWords, Collections.reverseOrder());
			int greatestValue = numberOfMatchingWords.get(0);

			// Get the File Type for Value
			if (greatestValue > 0) {
				if(numberOfMatchingWords.get(0) == numberOfMatchingWords.get(1)) {
					fileType = "!!! File Not Classified !!!";
				}else {
					fileType = getKeyFromValue(matchingKeyWords, greatestValue).toString();
				}
			} else {
				fileType = "!!! File Not Classified !!!";
			}

		}
		return fileType;
	}

	public static Object getKeyFromValue(Map<String, Integer> hm, Object value) {
		for (Object o : hm.keySet()) {
			if (hm.get(o).equals(value)) {
				return o;
			}
		}
		return null;
	}
	
	
	public static List<File> iterateOverFiles(String directory) {
		File folder = new File(directory);
		File[] listOfFiles = folder.listFiles();
		List<File> FileList = Arrays.asList(listOfFiles);		
		return FileList;
	}

	public static String storeAndReturnOutputFileName(String inputFormsDirectory, HashMap<String, Report> report, String uid, String classifyBy) {
		String outputFileName = "";
		try {
			String downloadsDirectory = "src/main/java/com/example/springboot/resources/downloads/";
			String unCompressedDirectory = "uncompressed/" + uid;
			String compressedDirectory = "compresssed/";
			report.keySet().stream().forEach(elem -> {
				try {
					String unCompressedDirectoryPath = downloadsDirectory + unCompressedDirectory + "/" ;
					if(classifyBy.equalsIgnoreCase("complexity")) {
						unCompressedDirectoryPath += report.get(elem).getComplexity() + "/";
					}
					unCompressedDirectoryPath += report.get(elem).getType();
					new File(unCompressedDirectoryPath).mkdirs();
					FileUtils.copyFileToDirectory(
							new File(inputFormsDirectory + "/" + elem),
							new File(unCompressedDirectoryPath)
					);
				}
				catch (Exception ex) {
					System.out.println("Exception in storeAndReturnDownloadUrl inside preprocessor path creation"  + elem + ex.toString());
				}
			});

			String compressedDirectoryPath = downloadsDirectory + compressedDirectory;
			new File(compressedDirectoryPath).mkdirs();
			new ZipFile(
				new File(downloadsDirectory + compressedDirectory + uid + ".zip")
			).addFolder(new File(downloadsDirectory + unCompressedDirectory));
			outputFileName = uid + ".zip";
		}
		catch (Exception ex) {
			System.out.println("Exception in storeAndReturnDownloadUrl" + ex.toString());
		}
		return outputFileName;
	}
}
