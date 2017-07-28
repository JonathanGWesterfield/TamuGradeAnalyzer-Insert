/**
 * Created by JonathanWesterfield on 12/20/16.
 */
package com.company;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.util.*;
import org.apache.pdfbox.*;
import java.io.*;
import java.util.*;

/** This class reads from the given pdf's and strips the text from them. it then
 * puts the text into a .dat file for further manipulation
 */
public class PDFReader extends DuplicateVerification
{
    //Main method for testing
    public static void main(String[] args)
    {
        try
        {
            String strippedTextFilename = loadPDF();
        }
        catch (IOException e)
        {
            System.err.println("Could not access PDF's");
            e.printStackTrace();
        }
    }

    // ultimately unnecessary default/only constructor
    public PDFReader()
    {
        super();
    }

    // loads all pdfs in resources folder and puts them into an array to be parsed through
    public static String loadPDF() throws IOException
    {
        // DuplicateVerification ver = new DuplicateVerification();
        File folder = new File("resources/GradePDFs/");
        File[] oldListOfFiles = folder.listFiles();
        ArrayList<File> listOfFiles = addToArrayList(oldListOfFiles);

        // makes or appends to a match verification file to make sure the same pdf is not
        // read multiple times depending on whether or not the PDFMatchVerification file exists

        if(determineMakeMatchFile()) // this means there is no verification file so make a new empty one
        {
            System.out.println("Should Make New Ver File? " + determineMakeMatchFile());

            // creates an empty file and checks against the empty file
            makeEmptyMatchVerFile(listOfFiles);

            // parses through the resources folder and adds the filenames to the verification file
            for (int i = 0; i < listOfFiles.size(); i++)
            {
                if (listOfFiles.get(i).isFile())
                {
                    //System.out.println("File " + listOfFiles.get(i).getName());
                    // Modifies list so that duplicate pdfs are analyzed
                    makeMatchVerFile(listOfFiles.get(i).getName());
                }
                else if (listOfFiles.get(i).isDirectory())
                {
                    System.out.println("Directory " + listOfFiles.get(i).getName());
                }
            }
        }
        else // if the verification file already exists, then modify the list of files
        // to be analyzed, then add the new filenames to the verification file
        {
            System.out.println("Only Appending to Match Ver File: " + determineMakeMatchFile());
            // makes or appends to a match verification file to make sure the same pdf is not read multiple times
            modifyListOfFiles(listOfFiles);
            for (int i = 0; i < listOfFiles.size(); i++)
            {
                if (listOfFiles.get(i).isFile())
                {
                    makeMatchVerFile(listOfFiles.get(i).getName());
                }
                else if (listOfFiles.get(i).isDirectory())
                {
                    System.out.println("Directory " + listOfFiles.get(i).getName());
                }
            }
        }
        return textStripper(listOfFiles);
    }

    // checks to see the GradeData.dat file has been previously made
    private static boolean determineMakeGradeDataFile()
    {
        boolean makeGradeFile = true;

        File gradeFile = new File("resources/GradeData.dat");

        if(gradeFile.exists())
        {
            makeGradeFile = false;
        }
        return makeGradeFile;
    }

    // goes through all pdfs in folder and strips the text from them
    private static String textStripper(ArrayList<File> listOfFiles)
    {
        String filename = "resources/GradeData.dat";

        try
        {
            PrintWriter output = new PrintWriter(filename);

            // parses through all pdf documents in the resources folder
            for(int i = 0; i < listOfFiles.size(); i++)
            {
                try
                {
                    PDDocument pddDocument = PDDocument.load(
                            new File("resources/GradePDFs/" + listOfFiles.get(i).getName()));
                    // Strips the text off of the PDF's
                    PDFTextStripper txtStrip = new PDFTextStripper();
                    String pdfContent = txtStrip.getText(pddDocument);

                    // outputs to the GradeData.dat file and overwrites everytime
                    output.println(pdfContent);
                    pddDocument.close();

                }
                catch(IOException e)
                {
                    System.err.println(listOfFiles.get(i) + " could not be loaded");
                    e.printStackTrace();
                    System.exit(0);
                }
            }
            output.close();
        }
        catch (FileNotFoundException e)
        {
            System.err.println("GradeData.dat file could not be opened");
            e.printStackTrace();
            System.exit(0);
        }

        return filename;
    }
}

        /* This block of code used to belong to pdfTextFileWriter but became obsolete
           It is here in case I need to revert and copy paste with this code

        if(determineMakeGradeDataFile()) // checks if there is a grade data file
        {
            try
            {
                PrintWriter output = new PrintWriter(filename);
                output.println(pdfStrippedText);
                output.close();
            }
            catch(FileNotFoundException e)
            {
                System.err.println("File 'resources/GradeData.data' could not be opened");
                e.printStackTrace();
                System.exit(0);
            }
        }
        else
        {
            try
            {
                FileWriter fileAppend = new FileWriter(filename, true);
                PrintWriter output = new PrintWriter(fileAppend);
                output.println();
                output.println(pdfStrippedText);
                fileAppend.close();
            }
            catch(FileNotFoundException e)
            {
                System.err.println("File could not be opened to be appended to");
                e.printStackTrace();
                System.exit(0);
            }
            catch (IOException e)
            {
                System.err.println("File could not be opened to be appended to");
                e.printStackTrace();
                System.exit(0);
            }
        }

        return; */
