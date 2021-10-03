package Util;

//Test Trial in pushing the code in to Git


//The common utils
//1. InitiateEnvVariable - Initiate the Env variables e.g. configfiles.
//2. GetValueFromConfig - To get the values from config file
//3. TodaysDate - to get todays date in desired format by passing the Parameter
//4. readCsvListString - YOu can read the CSV and the values can be returned in a ListString type
//5. FindColIndex - Find a colIndex from a row by passing the row and Col name
//5. DeleteAFileFromPath - Delete a specific file from a path
//6. removeDuplicates - To remove the Duplicates in a CSV file. OutPut will be written in a different filename.


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

public class utility {
	
	public static void main(String[] args) throws Exception {
		System.out.println(TodaysDate("yyyy/MM/dd HH:mm:ss"));
		
		int Check = FindColIndex("RAM,SELVI,HARI,HARAN", "RAMA");
		System.out.println(Check);
	}
	
	public static String InitiateEnvVariable (String WhatToReturn) {
		
		String ConfigFilePath = "C:\\Users\\Dell\\eclipse-workspace\\dataPoint\\src";
//		String ConfigFile = "config_trial_1.csv";
		String ConfigFile = "config.csv";
		
		String SelectiveScripts = "selectiveScripts.csv";
		
		if(WhatToReturn.equals("ConfigFilePath")) {
			return ConfigFilePath;			
		} else if (WhatToReturn.equals("ConfigFile")) {
			return ConfigFile;
		} else {
			return SelectiveScripts;
		}

	}
	
	@SuppressWarnings("resource")
	public static String GetValueFromConfig(String PackClass, String Variable, String ConfigFilePath, String ConfigFile) throws IOException {
		
		String AssignValue = null;
		
        // read Filetoupdate csv file and create a Array List
		BufferedReader bufferedReaderOut = null;
        List<String> input = new ArrayList<String>();
        File inputFile = new File(ConfigFilePath+"\\"+ConfigFile);
        bufferedReaderOut = new BufferedReader(new FileReader(inputFile));
        String readLine = null;
        while ((readLine = bufferedReaderOut.readLine()) != null) {
        input.add(readLine);
        }
        
        int numOfRecordsOut = input.size();

        for(int i=0; i < numOfRecordsOut; i++) {
            String row = input.get(i);
            String [] entries = row.split(",");
            
            String Concat = entries[0]+entries[1];
            
            if (Concat.equals(PackClass+Variable)) {
            	AssignValue = entries[3];
            	break;
            } else {
            }
        }
		
		return AssignValue;
	}

	public static String TodaysDate(String Format) {
		
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern(Format);
		LocalDateTime now = LocalDateTime.now(); 
		String Date = dtf.format(now);
		return Date;
		
	}
	
	@SuppressWarnings("resource")
	public static List<String> readCsvListString (String DataFile1path, String DataFile, String SkipHeader) throws IOException 
	{
		
		BufferedReader bufferedReaderOut = null;
        List<String> input = new ArrayList<String>();
        File inputFile = new File(DataFile1path+"\\"+DataFile);
        bufferedReaderOut = new BufferedReader(new FileReader(inputFile));
        String readLine = null;
        
        if (SkipHeader.toUpperCase().equals("YES")) {
        	readLine = bufferedReaderOut.readLine();            	
        }
        
        while ((readLine = bufferedReaderOut.readLine()) != null) {
        input.add(readLine);
        }
        
        return input;
	}
	
	public static Integer FindColIndex(String Header, String FindCol) {
		
		String [] entries = Header.split(",");
		int length = entries.length;
		
		int ColIndex = 150;
		
		for (int i=0; i <length; i++ ) {
			if (entries[i].equals(FindCol)) {
				ColIndex = i;
				break;
			}
		}
		
		return ColIndex;
			
	}
	
	public static void DeleteAFileFromPath(String Path, String FileToDelete) {
		
		File Directory = new File(Path);
		File FileList[] = Directory.listFiles();
		
		for (File file : FileList) {
			String FileName = new String(file.getName());
					if (FileName.equals(FileToDelete))
					{	        
						if(file.delete()) 
				        { 
				        	System.out.println("	Deleted the existing file ["+ FileToDelete + "]" ); 
				        } 
				        else
				        { 
				            System.out.println("	Failed to delete the file"); 
				        } 
					} 
			}
		}
	
	public static void DataAppend (String data, FileWriter writer) throws IOException {
		writer.append(data);
		writer.flush();
		}
	
	public static void removeDuplicates(String SourcePath, String FiletoRemoveDup, String DestPath, String FinalFile ) throws IOException {
		
		String csvFile = SourcePath+"\\"+FiletoRemoveDup;
		String OutputFile = DestPath+"\\"+FinalFile;
	    BufferedReader br = null;
	    String line = "";
	    HashSet<String> lines = new HashSet<>();
	    
	    FileWriter writer = new FileWriter(OutputFile);
	    
	    try {
	        br = new BufferedReader(new FileReader(csvFile));
	        while ((line = br.readLine()) != null) {
	            if (lines.add(line)) {
//	                System.out.println(line);
	            	writer.append(line + "\n");
	            }
	        }

	    } catch (FileNotFoundException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    } finally {
	        if (br != null) {
	            try {
	                br.close();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
	    }
	    
	    writer.flush();
	    writer.close();
		
	}
	
}