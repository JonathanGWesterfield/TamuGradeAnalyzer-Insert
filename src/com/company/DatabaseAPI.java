/**
 * Created by JonathanWesterfield on 2/10/17.
 * This class is nothing but functions that allow the user to access the database
 * through the application. As such it is very long and has very many functions
 */

/* Contains the functions: getAllSubjectDistinct, getAllCourseNumDistinct, getCourseProfessors,
 * getNumASem, getNumBSem, getNumCSem, getNumDSem, getNumFSem, getNumA, getNumB, getNumC, getNumD, getNumF,
 * getNumQDrop, getAvgGPA, getAvgGPASem, getTotalNumStudentsTaught, getPercentA, getPercentB,
  * getPercentC, getPercentD, getPercentF, getPercentQDrops,  getNumSemestersTaught,
  * getProfRawData and the insert into table */

package com.company;
import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;

import java.io.IOException;
import java.sql.*;
import java.util.*;
import java.lang.*;
import java.lang.String;
import java.io.*;

public class DatabaseAPI
{
    private String connectionString = "jdbc:mysql://tamudata.cgmm1m5yk0wt.us-east-2." +
            "rds.amazonaws.com:3306/TamuData";
    private Connection conn;

    // class constructor that throws an exception to the function caller
    DatabaseAPI() throws java.sql.SQLException, java.lang.ClassNotFoundException
    {
        System.out.println("Default constructor called");
        /*String password = "TamuDefaultUserHullabaloo2019WHO0P!"; // password for a default user account
        String username = "DefaultUser"; // default username
        Class.forName("com.mysql.jdbc.Driver");
        this.conn = DriverManager.getConnection(connectionString, username, password);
        System.out.println("Database connection established");*/
    }

    DatabaseAPI(String username, String password) throws java.sql.SQLException, java.lang.ClassNotFoundException
    {
        Class.forName("com.mysql.jdbc.Driver");
        this.conn = DriverManager.getConnection(connectionString, username, password);
        System.out.println("Database connection established");
    }

    // unpacks the condensed data and inserts into the database using the DB API
    public void unpackAndInsert(String condensedFileName)
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
                insert(subject, courseNum, sectionNum, avgGPA, professor, numA, numB, numC,
                        numD, numF, numQdrop, semester, year, honors);
            }
            catch(java.util.NoSuchElementException e)
            {
                continue;
            }

        }
    }

    //inserts all of the information given into the database table
    public void insert(String Subject, int courseNum, int sectionNum, Double avgGPA,
                       String professor, int numA, int numB, int numC, int numD, int numF, int numQdrop,
                       String termSemester, int termYear, boolean honors) // throws java.sql.SQLException
    {
        try
        {
            //FIXME: CHANGE THIS BACK TO TAMURAWDATA FOR USE ON LAPTOP
            String query = "INSERT INTO TamuGrades " /*TamuRawData*/ + "VALUES (\"" + Subject + "\", " +
                    courseNum + ", " + sectionNum + ", " + avgGPA + ", \"" + professor + "\", "
                    + numA + ", " + numB + ", " + numC + ", " + numD + ", " + numF
                    + ", " + numQdrop + ", \"" + termSemester + "\", " + termYear + ", " + honors + ") ";
            System.out.println("Inserting query\n" + query);
            PreparedStatement insertStatement = this.conn.prepareStatement(query);
            insertStatement.execute();
            System.out.println("Query Inserted");
        }
        catch(SQLException e)
        {
            // this small block turns the exception into a string to be compared
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            String exceptionMessage = sw.toString();

            if(exceptionMessage.contains("com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrity" +
                    "ConstraintViolationException"))
            {
                System.out.println("\n" + e);
                System.out.println("\nThis entry is already in the table\nIGNORING");
            }
            else
            {
                System.out.println("\n" + e);
                System.out.println("Could not create insert statement");
                e.printStackTrace();
            }
        }
        catch(Exception e)
        {
            // System.out.println("\n" + e);
            System.err.println("Could not Insert into Database because something strange happened");
            e.printStackTrace();
        }
    }

    public void closeDBConn() throws SQLException
    {
        System.out.println("\nClosing Database connection");
        this.conn.close();
        System.out.println("\nDatabase connection closed");
    }

}
/*What is needed make the database on the laptop CONSTRAINT so that duplicate rows aren't added is:
<BEGIN;

ALTER IGNORE TABLE TamuGrades ADD CONSTRAINT TamuGrades_unique
UNIQUE (CourseSubject, CourseNum, SectionNum, Avg_GPA, Professor,
NumA, Numb, NumC, NumD, NumF, Num_QDrop, Semester_Term, Semester_Year, Honors);>

then use the command <COMMIT>
 */


// is the code for listing all of the raw data in case I want to use it in a different file
/* ArrayList<String> rawData = db.getProfRawData("CSCE", 121, "MOORE");
            for(int i = 0; i < rawData.size(); i++)
            {
                if(i % 10 == 0)
                {
                    //System.out.println();
                    System.out.print("\n" + rawData.get(i) + " ");
                }
                else
                    System.out.print(rawData.get(i) + " ");
            } */