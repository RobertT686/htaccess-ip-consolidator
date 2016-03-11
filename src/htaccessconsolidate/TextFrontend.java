package htaccessconsolidate;

/*
 * Robert Tweedy
 * TextFrontend.java
 * Text-based frontend to .htaccess IP consolidator.
 * First Published: May 30, 2013
 * Last Modified: March 10, 2016
 * - Changed the text to be more generic since it's no longer explicitly for denied IPs.
 * 
 * Version 1.1
 */

public class TextFrontend
{

	/**
	 * @param args
	 * -The file name(s) with IP addresses to check and consolidate.
	 */
	public static void main(String[] args)
	{
		String outFilename = "consolidatedIPs.txt";
		
		System.out.println();
		System.out.println(".htaccess IP list consolidator.");
		System.out.println("©2013-2016 Robert Tweedy");
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
				System.out.println("Writing consolidated IP list file...");
				
				if(ipListGenerator.writeFile(outFilename))
				{
					System.out.println("File successfully written as \"" + outFilename + "\"");
				}//if
				else
				{
					System.out.println("Unable to write a file. Please make sure that you have write\npermissions for the current directory.");
				}//else
				
			}//if
			else
			{
				System.out.println("Unable to generate a list of IP addresses.");
				System.out.println("Check the specified files to make sure they exist and that you have\nread permissions on them.");
			}//else
		}//else
		
		System.out.println();
	}//main

}//TextFrontend
