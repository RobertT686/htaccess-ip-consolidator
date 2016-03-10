package htaccessdeny;

/*
 * Robert Tweedy
 * IPConsolidator.java
 * Creates a consolidated list of IP addresses for the purpose of denying 
 *  access in a .htaccess file from one or more sources.
 * First Published: May 30, 2013
 * Last Modified: May 30, 2013
 * 
 * Version 1.0
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.StringTokenizer;
import java.util.TreeSet;

public class IPConsolidator
{
	private TreeSet<String> consolidatedIPList = new TreeSet<String>();
	private boolean successfulLoad = true; //Did all files load successfully?
	
	public IPConsolidator(String[] ipFiles)
	{
		for(String filename : ipFiles)
		{
			successfulLoad = (readFile(filename) && successfulLoad);
		}//foreach
	}//Constructor
	
	//Private methods
	/**
	 * Reads the list of IP addresses from the file and adds it to the ipLists array.
	 * @param filePath - The path of the file to read.
	 * @return True if success, false otherwise.
	 */
	private boolean readFile(String filePath)
	{
		boolean result = false;
		String fullLine, subString;
		StringTokenizer stringToken, subToken;
		
		try
		{
			BufferedReader file = new BufferedReader (new FileReader(filePath));
			
			while (file.ready())
			{
				fullLine = file.readLine();
				stringToken = new StringTokenizer(fullLine, " ");
				
				while(stringToken.hasMoreTokens())
				{
					subString = stringToken.nextToken();
					subToken = new StringTokenizer(subString, ".");
					
					if(subToken.countTokens() == 4)
					{
						//An IPv4 address is in the form a.b.c.d/e, so 
						//if there are 4 tokens in the subtoken, and the
						//input data is assumed to be good, then the subString
						//must be an IP address.
						consolidatedIPList.add(subString);
					}//if
					
				}//while stringToken.hasMoreTokens()
			}//while file.ready()
			
			file.close();
			
			result = true;
		}//try
		catch(IOException e)
		{
			System.err.println("There was an error reading the file \"" + filePath + "\".");
		}//catch
		
		return result;
	}//readFile
	
	//Public methods
	/**
	 * Returns if all the files passed to the object were successfully read.
	 * @return True if success, false otherwise.
	 */
	public boolean loadedSuccessfully()
	{
		return successfulLoad;
	}//loadedSuccessfully
	
	/**
	 * Write all the IP addresses into a file formatted to be inserted into a
	 * .htaccess file.
	 * @param fileName The filename of the output file.
	 * @return True if success, false otherwise.
	 */
	public boolean writeFile(String fileName)
	{
		//This method can be updated to write a file for allowed IP addresses instead of denied ones.
		boolean result = false;
		String currentIPAddress, currentSubRange;
		String workingSubRange = "null";
		
		StringTokenizer subIP;
		
		try
		{
			PrintWriter file = new PrintWriter(new FileWriter(fileName));
			
			for(String ipAddr : consolidatedIPList)
			{
				currentIPAddress = ipAddr;
				subIP = new StringTokenizer(currentIPAddress, ".");
				currentSubRange = subIP.nextToken() + "." + subIP.nextToken();
				
				if(!workingSubRange.equals(currentSubRange))
				{
					//On to a new subrange of IP addresses.
					if(!workingSubRange.equals("null"))
					{
						file.println();
					}//if
					
					file.print("deny from ");
					workingSubRange = currentSubRange;
				}//if
				else
				{
					file.print(" "); //Space is to separate each IP address on a line.
				}
				
				file.print(currentIPAddress);
			}//foreach
			
			file.close();
			result = true;
		}//try
		catch(IOException e)
		{
			System.err.println("There was an error writing the file \"" + fileName + "\".");
		}//catch
		
		return result;
	}//writeFile
}//IPConsolidator
