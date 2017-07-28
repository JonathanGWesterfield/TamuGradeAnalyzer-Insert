package com.company;
import java.io.*;
import java.util.*;

/**
 * Created by JonathanWesterfield on 12/24/16.
 *
 * This class provides methods for the PDFReader class that allows it to make sure the same pdf is not read and
 * analyzed twice.
 *
 * Creates a matchlist file that is compared with the files that are to be analyzed. If any match, the file that matched
 * is not to be anaylzed by the program and gets removed from the stream
 */
public class DuplicateVerification
{
    public DuplicateVerification() {}

    /* modifies the list of files to be analyzed by comparing the filenames to the file names already
logged in the PDFMatchVerification.dat file. If the filenames are the same, the file is a duplicate and
needs to be skipped over or removed from the list of files to be analyzed
 */
    protected static void modifyListOfFiles(ArrayList<File> listOfFiles)
    {
        try(Scanner matchlistreader = new Scanner(new File("resources/PDFMatchVerification.dat")))
        {
            ArrayList <String> matchList = new ArrayList<String>();
            while(matchlistreader.hasNext())
            {
                matchList.add(matchlistreader.nextLine());
            }

            // checks to verify the files were loaded correctly to be read and compared
            for(int i = 0; i < matchList.size(); i++)
            {
                System.out.println("Match List" + matchList.get(i) + " Before mod");
            }

            for(int i = 0; i < listOfFiles.size(); i++)
            {
                System.out.println("List of Files: " + listOfFiles.get(i) + " Before mod");
            }

            for(int i = 0; i < matchList.size(); i++)
            {
                for(int j = listOfFiles.size() - 1; j >= 0; j--)
                {
                    if(listOfFiles.get(j).getName().equals(matchList.get(i)))
                    {
                        listOfFiles.remove(j);
                    }
                }
            }

            System.out.println("Number of PDF's to be analyzed: " + listOfFiles.size());
            for(int i = 0; i < listOfFiles.size(); i++)
            {
                System.out.println("List of Files: " + listOfFiles.get(i) + " After mod");
            }

            if(listOfFiles.size() == 0)
            {
                System.out.println("All of the files are duplicates");
            }
            else
            {
                for(int i = 0; i < listOfFiles.size(); i++)
                {
                    System.out.println("Files to be analyzed: " + listOfFiles.get(i).getName());
                }
            }
        }
        catch(IOException e)
        {
            System.err.println("The match list verification file could not be opened");
            e.printStackTrace();
            System.exit(0);
        }
    }

    protected static void makeEmptyMatchVerFile(ArrayList<File> listOfFiles)
    {
        String matchFilename = "resources/PDFMatchVerification.dat";

        try(PrintWriter printMatchFile = new PrintWriter(matchFilename))
        {
            modifyListOfFiles(listOfFiles);
        }
        catch(FileNotFoundException e)
        {
            System.err.println("Match file could not be opened");
            e.printStackTrace();
            System.exit(0);
        }
    }


    //FIXME: FIX SO THAT SAME FILENAMES AREN'T REPEATED TO THE MATCH FILE TO INCREASE PERFORMANCE OF SEQUENTIAL SEARCH
    // possibly sort and then binary search?
    protected static void makeMatchVerFile(String filename)
    {
        String matchFilename = "resources/PDFMatchVerification.dat";

        // adds all file names to a file to make sure they aren't repeated
        // if a new verification file must be made

        if(determineMakeMatchFile()) // means a new verification file must be made
        {
            try(PrintWriter printMatchFile = new PrintWriter(matchFilename))
            {
                printMatchFile.println(filename);
            }
            catch(FileNotFoundException e)
            {
                System.err.println("Match file could not be opened");
                e.printStackTrace();
                System.exit(0);
            }
            catch(IOException e)
            {
                System.err.println("Match file could not be opened");
                e.printStackTrace();
                System.exit(0);
            }
        }
        else // if the file exists, then just add the new filenames to it
        {
            try(FileWriter matchAppend = new FileWriter(matchFilename, true);
                PrintWriter printMatchFile = new PrintWriter(matchAppend))
            {
                printMatchFile.println(filename);
            }
            catch(FileNotFoundException e)
            {
                System.err.println("Match file could not be opened");
                e.printStackTrace();
                System.exit(0);
            }
            catch(IOException e)
            {
                System.err.println("Match file could not be opened");
                e.printStackTrace();
                System.exit(0);
            }
        }
    }

    // determines whether or not to add a match verification file for the pdfs
    // true means you need to make a new match file
    // false means you shouldn't make new file
    protected static boolean determineMakeMatchFile()
    {
        boolean makeFile = true;

        File matchfile = new File("resources/PDFMatchVerification.dat");
        if(matchfile.exists())
        {
            // System.out.println("The thing thats ruining everything " +  matchfile); // listOfFiles.get(i).getName());
            makeFile = false;
        }
        return makeFile;
    }


    // takes file array and turns it into an arraylist for dynamic resizing
    protected static ArrayList<File> addToArrayList(File[] oldListOfFiles)
    {
        ArrayList<File> newArrayList = new ArrayList<File>();
        for(int i = 0; i < oldListOfFiles.length; i++)
        {
            newArrayList.add(oldListOfFiles[i]);
        }
        return newArrayList;
    }
}
