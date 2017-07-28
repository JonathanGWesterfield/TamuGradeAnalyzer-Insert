/**
 * Created by JonathanW on 12/26/16.
 */
package com.company;
import java.io.*;
import java.util.*;

/* condenses the information in the GradeData.dat file in order to make updating
the database possible

   After information has been condensed, it is put into another*/
public class InfoCondenser
{
    private int numIterations = 0;
    private int courseNum, sectionNum, year, numA, numB, numC, numD, numF, numQdrop;
    private double avgGPA, avgCourseGPA;
    private String subject, professor, semester;
    private boolean honors;
    private String outputFilename = "resources/CondensedData.dat";

    // Main method for testing
    /* public static void main(String[] args)
    {
        InfoCondenser info = new InfoCondenser();
        info.condenser();
    } */

    public InfoCondenser() {} // default constructor

    // removes excess data from the GradeData.dat file
    public String condenser(String inputFileName)
    {
        //String inputFileName = "resources/GradeData.dat";
        try(FileInputStream gradeData = new FileInputStream(new File(inputFileName));
            //"resources/GradeData.dat"));
            Scanner gradeDataReader = new Scanner(gradeData);
            PrintWriter condensedFile = new PrintWriter(outputFilename);)
        {

            while(gradeDataReader.hasNextLine())
            {
                String data = gradeDataReader.nextLine();
                StringTokenizer tokenizer = new StringTokenizer(data, " -");
                if(tokenizer.countTokens() == 8 && data.length() > 40) // for term datas
                {
                    // gets when the class was held
                    termInfoExtractor(data, tokenizer, condensedFile);
                }
                // the 3 else if statements below are for longer hyphenated names
                else if((tokenizer.countTokens() == 6 && data.length() > 17))
                {
                    // gets info for the class such as professor and such
                    classInfoExtractor(data, tokenizer, false, condensedFile);
                }
                else if((tokenizer.countTokens() == 7 && data.length() > 17))
                {
                    classInfoExtractor(data, tokenizer, true, condensedFile);
                }
                else if((tokenizer.countTokens() == 8 && data.length() < 36))
                {
                    classInfoExtractor(data, tokenizer, true, condensedFile);
                }
                else if(tokenizer.countTokens() == 12)
                {
                    // gets number of A's, B's, C's, D's etc
                    gradeInfoExtractor(data, tokenizer, condensedFile);
                }
            }
            condensedFile.close();
        }
        catch(IOException e)
        {
            System.err.println("The GradeData.dat file could not be opened to be condensed");
            e.printStackTrace();
            System.exit(0);
        }
        return outputFilename;
    }

    // gets number of A's, B's, C's, D's, F's, and Q drops
    private void gradeInfoExtractor(String data, StringTokenizer tokenizer,
                                    PrintWriter condensedFile)
    {
        int[] intArray = new int[12];
        for(int i = 0; i < 12; i++)
        {
            intArray[i] = Integer.parseInt(tokenizer.nextToken());
        }
        numA = intArray[0];
        numB = intArray[1];
        numC = intArray[2];
        numD = intArray[3];
        numF = intArray[4];
        numQdrop = intArray[9];

        /* System.out.printf("NumA: \t%d\n", numA);
        System.out.printf("NumB: \t%d\n", numB);
        System.out.printf("NumC: \t%d\n", numC);
        System.out.printf("NumD: \t%d\n", numD);
        System.out.printf("NumF: \t%d\n", numF);
        System.out.printf("NumQ: \t%d\n", numQdrop);
        System.out.println();*/

        condensedFile.println(numA + ";" + numB + ";" + numC + ";" + numD + ";" + numF + ";" +
                numQdrop + ";" + semester + ";" + year);
        System.out.println(numA + ";" + numB + ";" + numC + ";" + numD + ";" + numF + ";" +
                numQdrop + ";" + semester + ";" + year);

    }

    private void termInfoExtractor(String data, StringTokenizer tokenizer, PrintWriter condensedFile)
    {
        String[] strArray = new String[8];
        for(int i = 0; i < 8; i++)
        {
            strArray[i] = tokenizer.nextToken();
        }

        /* for(int i = 0; i < 8; i++)
        {
            System.out.println(strArray[i]);
        }
        System.out.println(); */

        semester = strArray[4];
        year = Integer.parseInt(strArray[5]);
        // System.out.println("Semester: " + semester + "\nYear: " + year);
        // System.out.println();
        numIterations++;
    }
    // extracts course information (not grades) to be put into condensed .dat file
    private void classInfoExtractor(String data, StringTokenizer tokenizer,
                                    boolean weirdName, PrintWriter condensedFile)
    {
        ArrayList<String> contents = new ArrayList<String>();
        while(tokenizer.hasMoreTokens())
        {
            contents.add(tokenizer.nextToken());
        }

        subject = contents.get(0);
        courseNum = Integer.parseInt(contents.get(1));
        sectionNum = Integer.parseInt(contents.get(2));
        avgGPA = Double.parseDouble(contents.get(3));
        if(contents.size() == 7)
        {
            professor = contents.get(4);
            professor += "-" + contents.get(5);
            // professor += " " + contents.get(6);
        }
        else if(contents.size() == 8)
        {
            professor = contents.get(4);
            professor += "-" + contents.get(5);
            professor += "-" + contents.get(6);
            // professor += " " + contents.get(7);
        }
        else
        {
            professor = contents.get(4);
            //  professor += " " + contents.get(5);
        }

        /* System.out.println("subject: " + subject);
        System.out.println("Course Num: " + courseNum);
        System.out.println("Section Num: " + sectionNum);
        System.out.println("Average GPA: " + avgGPA);
        System.out.println("Professor: " + professor);
        System.out.println(); */

        condensedFile.print(subject + ";" + courseNum + ";" + sectionNum + ";" + avgGPA
                + ";" + professor + ";");
        System.out.print(subject + ";" + courseNum + ";" + sectionNum + ";" + avgGPA
                + ";" + professor + ";");
    }

    // this function was in the main function. Unpacks the condensed info and then uses
    // the databaseAPI insert function to put the info into the database table
    public void unpackAndInsert(String condensedFileName, DatabaseAPI db)
            throws FileNotFoundException
    {
        System.out.println("\n");
        StringTokenizer tokenizer;
        int courseNum, sectionNum, year, numA, numB, numC, numD, numF, numQdrop;
        double avgGPA;
        String subject, professor, semester;
        boolean honors;

        String infoLine = "";
        String[] elemArray = new String[14]; // array for the elements in the condensed file


        Scanner reader = new Scanner(new File(condensedFileName));
        reader.useDelimiter(";\n");

        while(reader.hasNextLine())
        {
            infoLine = reader.nextLine();
            System.out.println("\nBefore Unpack: " + infoLine);
            tokenizer = new StringTokenizer(infoLine, ";");

            /* There are some dirty inputs that don't have enough information in the line
             * This verifies that there is enough information to be input into the Database */
            try
            {
                for(int i = 0; i < 13; i++)
                    elemArray[i] = tokenizer.nextToken();

                subject = elemArray[0];
                courseNum = Integer.parseInt(elemArray[1]);
                sectionNum = Integer.parseInt(elemArray[2]);
                avgGPA = Double.parseDouble(elemArray[3]);
                professor = elemArray[4];
                numA = Integer.parseInt(elemArray[5]);
                numB = Integer.parseInt(elemArray[6]);
                numC = Integer.parseInt(elemArray[7]);
                numD = Integer.parseInt(elemArray[8]);
                numF = Integer.parseInt(elemArray[9]);
                numQdrop = Integer.parseInt(elemArray[10]);
                semester = elemArray[11];
                year = Integer.parseInt(elemArray[12]);

                // if sectionNum is in the 200 range, it is an honors section
                if(sectionNum >= 200 && sectionNum <= 215)
                    honors = true;
                else
                    honors = false;

                System.out.println("After Unpack " + subject + "-" + courseNum + "-" + sectionNum
                        + "-" + avgGPA + "-" + professor + "-" + numA + "-" + numB  + "-" + numC
                        + "-" + numD + "-" + numF + "-" + numQdrop + "-" + semester + "-" + year);
                db.insert(subject, courseNum, sectionNum, avgGPA, professor, numA, numB, numC,
                        numD, numF, numQdrop, semester, year, honors);
            }
            catch(java.util.NoSuchElementException e)
            {
                continue;
            }

        }
    }
}
//DON'T INCLUDE SECTION NUMBERS 200 (HONORS SECTIONS) IN THE TABLE