package com.example.springboot.helper;

import java.util.*;

public class ExtractTextUtil {

	public static String processExtractedText(String text) {

		String processedText = text;

		if (!processedText.equals("") && !processedText.equals(null) && processedText.length() > 0) {

			// Removing Empty Lines Spaces from Extracted Text
			processedText = processedText.replaceAll("\r\n", " ").replaceAll("\n", " ");

			// Removing Other Characters from Extracted Text
			processedText = processedText.replaceAll("[^a-zA-Z]", " ").toLowerCase();

			// Removing Less than 4 character Words from Extracted Text
			processedText = processedText.replaceAll("\\b\\w{1,3}\\b", "");

			// Getting Stopwords
			HashSet<String> stopwords_HashSet = PropertyFileUtil.getStopWords();

			// Removing Stopwords from Extracted Text
			Iterator<String> Itr_1 = stopwords_HashSet.iterator();
			while (Itr_1.hasNext()) {
				String textToReplace = Itr_1.next().toString();
				processedText = processedText.replaceAll("(?i)\\b" + textToReplace + "\\b", "").replaceAll("\\s+", " ");
			}
		}

		return processedText;
	}

	public static HashSet<String> convertStringToHashSet(String text) {
		HashSet<String> textHashSet = new HashSet<String>();
		if (text.contains(",")) {
			String[] TextArray = text.split(",");
			List<String> TextList = Arrays.asList(TextArray);
			textHashSet.addAll(TextList);
		}
		return textHashSet;
	}

	public static Hashtable<String, Integer> countWords(String input) {
		Hashtable<String, Integer> map = new Hashtable<String, Integer>();
		if (input != null) {
			String[] separatedWords = input.split(" ");
			for (String str : separatedWords) {
				if (map.containsKey(str)) {
					int count = map.get(str);
					if (!str.equals("")) {
						map.put(str, count + 1);
					}
				} else {
					map.put(str, 1);
				}
			}
		}
		return map;
	}

}
