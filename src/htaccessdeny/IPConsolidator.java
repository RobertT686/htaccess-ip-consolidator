package htaccessdeny;

/*
 * Robert Tweedy
 * IPConsolidator.java
 * Creates a consolidated list of IP addresses for the purpose of denying 
 *  access in a .htaccess file from one or more sources.
 * First Published: May 30, 2013
 * Last Modified: March 10, 2016
 * -Updated it to use Apache 2.4's "Require IP" syntax instead of the old
 * 	"deny from" syntax.
 * -Added in IPv6 support.
 * 
 * Version 2.0
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.TreeSet;

public class IPConsolidator
{
	//Original regexes found on regexlib.com
	//TODO Adjust these so that they're better.
	private static final String IPv4_REGEX = "^(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[0-9]{1,2})(\\.(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[0-9]{1,2})){0,3}/?\\d?\\d?$";
	private static final String IPv6_REGEX = "^(::|(([a-fA-F0-9]{1,4}):){7}(([a-fA-F0-9]{1,4}))|(:(:([a-fA-F0-9]{1,4})){1,6})|((([a-fA-F0-9]{1,4}):){1,6}:)|((([a-fA-F0-9]{1,4}):)(:([a-fA-F0-9]{1,4})){1,6})|((([a-fA-F0-9]{1,4}):){2}(:([a-fA-F0-9]{1,4})){1,5})|((([a-fA-F0-9]{1,4}):){3}(:([a-fA-F0-9]{1,4})){1,4})|((([a-fA-F0-9]{1,4}):){4}(:([a-fA-F0-9]{1,4})){1,3})|((([a-fA-F0-9]{1,4}):){5}(:([a-fA-F0-9]{1,4})){1,2}))/?\\d?\\d?\\d?$";
	
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
		String fullLine;
		String[] stringToken;
		
		try
		{
			BufferedReader file = new BufferedReader (new FileReader(filePath));
			
			while (file.ready())
			{
				fullLine = file.readLine();
				stringToken = fullLine.split("\\s");
				
				for(String subString : stringToken)
				{
					//TODO Currently ALL input data is assumed to be good;
					//garbage in=garbage out!
					if(subString.matches(IPv4_REGEX) || subString.matches(IPv6_REGEX))
						consolidatedIPList.add(subString);
										
				}//foreach subString : stringToken
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
		//TODO Fix sorting so that it sorts correctly.
		//TODO This includes separating IPv4 from IPv6 in the results.
		boolean result = false;
		boolean isIPv6 = false;
		String currentIPAddress, currentSubRange;
		String workingSubRange = "null";
		
		String[] subIP;
		
		try
		{
			PrintWriter file = new PrintWriter(new FileWriter(fileName));
			
			for(String ipAddr : consolidatedIPList)
			{
				//TODO See if I can just use ipAddr directly; I forget why I did it this way
				//TODO before.
				currentIPAddress = ipAddr;
				if (currentIPAddress.matches(IPv6_REGEX))
					isIPv6 = true;
				else
					isIPv6 = false;
				
				subIP = currentIPAddress.split("[.]|:");
				//TODO Improve this significantly.
				if(!isIPv6 && subIP.length >= 2)
					currentSubRange = subIP[0] + "." + subIP[1];
				else if(isIPv6 && subIP.length >= 3)
					currentSubRange = subIP[0] + ":" + subIP[1] + ":" + subIP[2];
				else
					currentSubRange = Arrays.toString(subIP);
				
				if(!workingSubRange.equals(currentSubRange))
				{
					//On to a new subrange of IP addresses.
					if(!workingSubRange.equals("null"))
					{
						file.println();
					}//if
					
					file.print("Require ip ");
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
