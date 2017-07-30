package com.company;

import java.io.*;
import java.sql.*;
import javafx.scene.control.*;
import javafx.animation.*;
import javafx.geometry.Insets;
import javafx.stage.*;
import javafx.collections.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.image.*;
import javafx.scene.control.*;
import javafx.event.*;
import javafx.scene.text.*;
import javafx.application.*;
import javafx.beans.value.*;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Scanner;

public class Main  {

    public static void main(String[] args)
    {
        // TODO: figure out how to use the login screen I made
        try
        {
            String strippedTextFilename = PDFReader.loadPDF();
            System.out.println("Output file from main: " + strippedTextFilename + "\n\n");

            InfoCondenser condense = new InfoCondenser();
            String condensedFilename = condense.condenser("resources/GradeData.dat");
            DatabaseAPI db = new DatabaseAPI();
            db.unpackAndInsert(condensedFilename);
            db.closeDBConn();
        }
        catch (FileNotFoundException e)
        {
            System.err.println(e);
            e.printStackTrace();
        }
        catch(SQLException | ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (NumberFormatException | IOException e)
        {
            System.err.println("Could not access PDF's");
            e.printStackTrace();
        }
    }
}
