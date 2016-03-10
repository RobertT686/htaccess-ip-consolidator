package htaccessdeny;

/*
 * Robert Tweedy
 * TextFrontend.java
 * Text-based frontend to .htaccess Deny consolidator.
 * First Published: May 30, 2013
 * Last Modified: May 30, 2013
 * 
 * Version 1.0
 */

public class TextFrontend
{

	/**
	 * @param args
	 * -The file name(s) with IPv4 addresses to check and consolidate.
	 */
	public static void main(String[] args)
	{
		System.out.println("Text based .htaccess deny from IP generator.");
		System.out.println("©2013 Robert Tweedy");
		System.out.println();

		if(args.length == 0)
		{
			System.out.println("To use, specify 1 or more files on the command line to consolidate\ninto a file.");
		}//if
		else
		{
			System.out.println("Loading files, please wait...");

			IPConsolidator ipListGenerator = new IPConsolidator(args);

			if(ipListGenerator.loadedSuccessfully())
			{
				System.out.println("Writing consolidated deny file...");
				
				if(ipListGenerator.writeFile("deniedIPs.txt"))
				{
					System.out.println("File successfully written as \"deniedIPs.txt\"");
				}//if
				else
				{
					System.out.println("Unable to write a file. Please make sure that you have write\npermissions for the current directory.");
				}//else
				
			}//if
			else
			{
				System.out.println("Unable to generate a list of denied IPs.");
				System.out.println("Check the specified files to make sure they exist.");
			}//else
		}//else
		
		System.out.println();
	}//main

}//TextFrontend
