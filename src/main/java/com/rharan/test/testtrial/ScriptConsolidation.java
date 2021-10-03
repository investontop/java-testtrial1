package com.rharan.test.testtrial;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

//Customerized utility
import Util.utility;

public class ScriptConsolidation {
	
	static utility localUtil = new utility();
	static String ConfigFilePath = localUtil.InitiateEnvVariable("ConfigFilePath");
	static String ConfigFile = localUtil.InitiateEnvVariable("ConfigFile");
	static String SelectiveScriptConfigFile = localUtil.InitiateEnvVariable("SelectiveScripts");
	
	static FileWriter writer;
	
	
//	static String NoOfDays = localUtil.GetValueFromConfig("dataPoint.SelectiveScriptConsolidation", "NoOfDays", ConfigFilePath, ConfigFile);

	@SuppressWarnings("null")
	public static void main(String[] args) throws IOException, FileNotFoundException {
		
		System.out.println("[" + localUtil.TodaysDate("yyyy/MM/dd HH:mm:ss") + "]" + " SelectiveScriptConsolidation Started - Considering config: [" + ConfigFile +"]");
		
		String NoOfDays = localUtil.GetValueFromConfig("dataPoint.SelectiveScriptConsolidation", "NoOfDays", ConfigFilePath, ConfigFile);
		String sourcepath = localUtil.GetValueFromConfig("dataPoint.ArchivePath", "ArchivePath", ConfigFilePath, ConfigFile);
		String SourceFile = localUtil.GetValueFromConfig("dataPoint.FinalFileCreation", "OutPutFile", ConfigFilePath, ConfigFile);
		String SelectiveScriptOutputPath = localUtil.GetValueFromConfig("dataPoint.SelectiveScriptPath", "SelectiveScriptOutputPath", ConfigFilePath, ConfigFile);
		String SelectiveScriptOPFilePrefix = localUtil.GetValueFromConfig("dataPoint.SelectiveScriptConsolidation", "SelectiveScriptOPFilePrefix", ConfigFilePath, ConfigFile);
		String MergeFileName = localUtil.GetValueFromConfig("dataPoint.SelectiveScriptConsolidation", "MergeFileName", ConfigFilePath, ConfigFile);
		
		String OutputFile = SelectiveScriptOPFilePrefix+"_"+localUtil.TodaysDate("yyyyMMdd")+".csv";
		
		Consolidation(NoOfDays, sourcepath, SourceFile, SelectiveScriptOutputPath, SelectiveScriptOPFilePrefix, MergeFileName, OutputFile);
		
		System.out.println("[" + localUtil.TodaysDate("yyyy/MM/dd HH:mm:ss") + "]" + " SelectiveScriptConsolidation Completed");
		System.out.println("  ");
	}
	
	static void Consolidation (String NoOfDays, String sourcepath, String SourceFile, String SelectiveScriptOutputPath, String SelectiveScriptOPFilePrefix, String MergeFileName, String OutputFile) 
			
			throws IOException 	{
		
		//Get the list of Source Files from the Path
		//Creating a file object for a Directory
		File SourceDirectory = new File(sourcepath);
		//List of all files
		File Sourcefilelist[] = SourceDirectory.listFiles();
		
		String [] FileName = new String[200];
		int i =0;
		for (File file : Sourcefilelist) {
			if(file.getName().length() > 20 && SourceFile.substring(0, SourceFile.length()-4).equals(file.getName().substring(0, file.getName().length()-13))) 
			{
				FileName [i]= file.getName();
				i=i+1;
			}
		}
		
//Delete the Merge file if already exist
		localUtil.DeleteAFileFromPath(SelectiveScriptOutputPath, MergeFileName);
		
//Merge the Above List of Files
		writer = new FileWriter(SelectiveScriptOutputPath+"\\"+MergeFileName);
		List<String> Datarows = null;
		
		int l = 0;
		for (int j=i-1; j>=0; j--) {
			
			if (j==i-1) {
				Datarows = localUtil.readCsvListString(sourcepath, FileName[j], "No");
			} else {
				Datarows = localUtil.readCsvListString(sourcepath, FileName[j], "Yes");	
			}
			
			for (int m=0; m < Datarows.size(); m++) {
				String Date = FileName[j].substring( (SourceFile.substring(0, SourceFile.length()-4)).length()+1 , 26);
				
				if (j==i-1 && m==0) {
					writer.append("DATE" +","+Datarows.get(m)+"\n");
				} else {
					writer.append(Date +","+Datarows.get(m)+"\n");
				}
			}
			
			l = l+1;
			if(l == Integer.parseInt(NoOfDays)) {
				break;
			}
		}
		
		writer.flush();
		writer.close();
		

//Read the file which have the selective stock names
		List<String> SelectiveScriptsRow = localUtil.readCsvListString(ConfigFilePath, SelectiveScriptConfigFile, "Yes");


//Delete the Final Selective data file if any
		localUtil.DeleteAFileFromPath(SelectiveScriptOutputPath, OutputFile );
		

//Read the Merged file
		List<String> MergeScriptsRow = localUtil.readCsvListString(SelectiveScriptOutputPath, MergeFileName, "NO");


//Create new Final Selective data file
		writer = new FileWriter(SelectiveScriptOutputPath+"\\"+OutputFile);
//		String data = "Date1, Script, Sector, PrevClose, TodayClose, PriceMov, %PriceChange, 1DayCandle, BulkTrade, BlockTrade, 1DaysDel%, 3DaysDel%, 5DaysDel%,	10DaysDel%,	15DaysDel%,	Delivery%Trend,	Contracts, VAL_INLAKH, OPEN_INT, CHG_IN_OI, %CHG_IN_OI, Price_OI_Indicator, HistoricalPriceOI \n";
//		localUtil.DataAppend (data, writer);
	
		
		int rowCount = 0;
		
		for (int k=0;k<SelectiveScriptsRow.size();k++) {
			String SelectScriptRow = SelectiveScriptsRow.get(k);
			
			for (String Mergerow : MergeScriptsRow) {
				
				String [] MergeEntries = Mergerow.split(",");
				
				if(MergeEntries[0].equals("DATE") && k==0) {
				localUtil.DataAppend (Mergerow + "\n", writer);
				}
				
				if (MergeEntries[1].equals(SelectScriptRow)) {
					rowCount = rowCount + 1;
					localUtil.DataAppend (Mergerow + "\n", writer);
				}
				
				if(rowCount == Integer.parseInt(NoOfDays) ) {
					rowCount = 0;
					break;
				}
			}
		}

		writer.flush();
		writer.close();
		
	}
	
}



