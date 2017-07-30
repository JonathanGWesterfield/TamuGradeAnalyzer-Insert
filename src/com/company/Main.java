package com.company;

import javafx.application.Application;
import java.io.*;
import java.sql.*;
import javafx.scene.control.*;
import javafx.animation.*;
import javafx.geometry.Insets;
import javafx.scene.paint.Color;
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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;


public class Main extends Application
{

    DatabaseAPI db;
    LoginScreen loginScreen;
    Scene insertScreen;
    Scene finished;
    Stage stage;

    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage)
    {
        stage = primaryStage;
        loginScreen = new LoginScreen();
        loginScreen.getCancel().setOnAction(e ->
        {
            e.consume();
            exit();
        }); // sets the exit button
        loginScreen.getAuth().setOnAction(e ->
        {
            setUsernamePassword();
            if(db!=null)
                setInsertPane(); // only changes screen if there is a connection
        });


        stage.setTitle("Insert Into TamuData Database");
        stage.setScene(loginScreen.getScene());
        stage.setOnCloseRequest(e -> // close for the exit button in the window
        {
            e.consume();
            exit();
        });
        stage.show();
    }

    public void setFinished()
    {
        Label finishLabel = new Label("Insert Operation is complete");
        finishLabel.setFont(new Font("Futura", 30));

        Button close = new Button("Close");
        close.setOnAction(e -> exit());

        BorderPane pane = new BorderPane();

        BorderPane.setAlignment(finishLabel, Pos.CENTER);
        BorderPane.setMargin(finishLabel, new Insets(8, 8, 8, 8));
        pane.setTop(finishLabel);

        BorderPane.setAlignment(close, Pos.CENTER);
        BorderPane.setMargin(close, new Insets(8, 8, 8, 8));
        pane.setCenter(close);

        finished = new Scene(pane, 450, 150);
        stage.setScene(finished);
        stage.show();
    }

    public void setInsertPane()
    {
        BorderPane layout = new BorderPane();
        BorderPane.setAlignment(layout, Pos.CENTER);
        BorderPane.setMargin(layout, new Insets(8, 8, 8, 8));

        Label doInsert = new Label();
        doInsert.setText("Insert into the Database?");
        doInsert.setFont(new Font("Futura", 30));

        BorderPane.setAlignment(doInsert, Pos.CENTER);
        BorderPane.setMargin(doInsert, new Insets(8, 8, 8, 8));
        layout.setTop(doInsert);
        BorderPane.setAlignment(doInsert, Pos.CENTER);

        Button insert = new Button("Insert");
        insert.setOnAction(e ->
        {
            processAndInsert();
            setFinished();
        });

        HBox buttons = new HBox(8);
        buttons.setAlignment(Pos.CENTER);
        buttons.getChildren().addAll(insert, loginScreen.getCancel());

        BorderPane.setAlignment(buttons, Pos.CENTER);
        BorderPane.setMargin(buttons, new Insets(8, 8, 8, 8));
        layout.setCenter(buttons);

        insertScreen = new Scene(layout, 420, 150);

        stage.setScene(insertScreen);
    }

    public void exit() // will close the application
    {
        try
        {
            if(db != null) // if there is a connection open
                db.closeDBConn(); // closes connection if open
            stage.close(); // closes application window
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            AlertError.showSQLException();
        }
    }

    public void processAndInsert()
    {
        try
        {
            String strippedTextFilename = PDFReader.loadPDF();
            System.out.println("Output file from main: " + strippedTextFilename + "\n\n");

            InfoCondenser condense = new InfoCondenser();
            String condensedFilename = condense.condenser("resources/GradeData.dat");
            db.unpackAndInsert(condensedFilename);
            db.closeDBConn();
        }
        catch (FileNotFoundException e)
        {
            System.err.println(e);
            e.printStackTrace();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        catch (NumberFormatException | IOException e)
        {
            System.err.println("Could not access PDF's");
            e.printStackTrace();
        }
    }

    private void setUsernamePassword()
    {
        loginScreen.getAuth().setTextFill(Color.FIREBRICK);
        String username = loginScreen.getUserTextField().getText();
        String password = loginScreen.getPwBox().getText();

        setDatabaseAccess(username, password);

        // TODO: comment this out after testing
        System.out.println("Username: " + username);
        System.out.println("Password: " + password);
    }

    private void setDatabaseAccess(String username, String password)
    {
        try
        {
            this.db = new DatabaseAPI(username, password);
            System.out.println("Admin Database connection established");
            return;
        }
        catch (SQLException e)
        {
            AlertError.showSQLException();
        }
        catch (ClassNotFoundException e)
        {
            AlertError.showClassNotFoundException();
        }

        System.err.println("The database connection could not be made");
        AlertError.showSQLException();
    }

}
