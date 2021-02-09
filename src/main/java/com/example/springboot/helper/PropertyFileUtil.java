package com.example.springboot.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.springframework.util.ResourceUtils;

public class PropertyFileUtil {
	
	static String keywordsPropertyFile = "src/main/java/com/example/springboot/resources/propertyFiles/keywords.properties";
	static String stopwordsPropertyFile = "src/main/java/com/example/springboot/resources/propertyFiles/stopwords.properties";
	static String stopwordsTextFile = "src/main/java/com/example/springboot/resources/propertyFiles/stopwords.txt";
	static String stopwordsKey = "stopwords";

	public static Properties loadPropertyFile(String File) {
		Properties prop = new Properties();
		try {
			FileInputStream fis = new FileInputStream(File);
			prop.load(fis);
		} catch (Exception e) {
			System.out.println("Exception - " + e);
		}
		return prop;
	}

	public static String readProperty(String File, String Key) {
		String value = null;
		try {
			FileInputStream fis = new FileInputStream(File);
			Properties prop = new Properties();
			prop.load(fis);
			value = prop.getProperty(Key).replaceAll("\\s", "");
			fis.close();
		} catch (Exception e) {
			System.out.println("Exception - " + e);
		}
		return value;
	}

	public static void updatePropertyFile(String File, String Key, String Value) {
		try {
			PropertiesConfiguration property = new PropertiesConfiguration(File);
			property.setProperty(Key, Value);
			property.save();
		} catch (Exception e) {
			System.out.println("Exception - " + e);
		}
	}
	
	
	public static String readStopWords() {
		String stopWords =  PropertyFileUtil.readProperty(stopwordsPropertyFile, stopwordsKey);
		return stopWords;
	}
	
	public static Properties loadKeywordsPropertFile() {
		Properties prop = PropertyFileUtil.loadPropertyFile(keywordsPropertyFile);
		return prop;
	}
	
	public static String readPropertyFromKeywordsFile(String property) {
		String keywords = readProperty(keywordsPropertyFile, property);
		return keywords;
	}
	
	public static void updateKeywords(String key, String value) {
		updatePropertyFile(keywordsPropertyFile, key, value);
	}
		
	public static Hashtable<String, List<String>> readAllKeywords() {
		Hashtable<String, List<String>> keywords = new Hashtable<String, List<String>>();
		try {
			FileReader reader = new FileReader(keywordsPropertyFile);
			Properties p = new Properties();
			p.load(reader);
			Enumeration<?> keys = p.propertyNames();
			while (keys.hasMoreElements()) {
				String key = (String) keys.nextElement();
				String[] valuesArray = p.getProperty(key).split(",");
				List<String>valuesList = Arrays.asList(valuesArray);
				keywords.put(key, valuesList);
			}
		}
		catch(Exception e) {
			System.out.println("Exception - " + e);
		}
		return keywords;
	}
	
	public static String getKeysFromKeywordsFile() {
		List<String> keyList = new ArrayList<String>();
		try {
			FileReader reader = new FileReader(keywordsPropertyFile);
			Properties p = new Properties();
			p.load(reader);

			Enumeration<?> keysEnumeration = p.propertyNames();
			while (keysEnumeration.hasMoreElements()) {
				String key = (String) keysEnumeration.nextElement();
				keyList.add(key);
			}
		}
		catch(Exception e) {
			System.out.println("Exception - " + e);
		}
		
		String keys = keyList.toString().replaceAll("\\[", "").replaceAll("\\]", "");
		System.out.println(keys);
		return keys;
	}
	
	
	public static String getValuesForKeyFromKeywordsFile(String key) {
		String values = null;
		try {
			Properties prop = PropertyFileUtil.loadKeywordsPropertFile();
			if (prop.containsKey(key.toLowerCase())) {
				values = PropertyFileUtil.readPropertyFromKeywordsFile(key.toLowerCase());
			}
			else {
				throw new Exception("Invalid Key");
			}
		}
		catch(Exception e) {
			System.out.println("Exception - " + e);
		}
		System.out.println(values);
		return values;
	}
	
	public static HashSet<String> getStopWords(){
		HashSet<String> stopwords_HashSet = new HashSet<String>();
        try {
        	File file = ResourceUtils.getFile(stopwordsTextFile);
        	List<String> lines = Files.readAllLines(file.toPath());
        	lines = lines.stream().map(line -> line.toLowerCase()).collect(Collectors.toList());
        	stopwords_HashSet = new HashSet<String>(lines);
        }
		catch(Exception e) {
			System.out.println("Exception - " + e);
		}
        return stopwords_HashSet;
    }
	
	
	
	
	
	
	

}
