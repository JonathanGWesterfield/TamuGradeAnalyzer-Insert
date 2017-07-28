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
import sun.rmi.runtime.Log;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Scanner;

/**
 * Created by jonathanw on 7/27/17.
 */
public class LoginScreen extends Application
{
    private GridPane grid;
    private Scene scene;
    Button btn;
    TextField userTextField;
    PasswordField pwBox;
    private String username;
    private String password;

    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage)
    {
        LoginScreen login = new LoginScreen();
        primaryStage.setTitle("Insert Into TamuData Database");
        primaryStage.setScene(login.getScene());
        primaryStage.show();
    }

    public LoginScreen()
    {
        setGrid();
    }

    public Scene getScene()
    {
        return scene;
    }

    private void setGrid()
    {
        grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        // Add in the text, labels and text Fields
        Text sceneTitle = new Text("Welcome");
        sceneTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(sceneTitle, 0, 0, 2, 1);

        Label userName = new Label("User Name:");
        grid.add(userName, 0, 1);

        userTextField = new TextField(); // Text field for the username
        grid.add(userTextField, 1, 1);

        Label pw = new Label("Password:");
        grid.add(pw, 0, 2);

        pwBox = new PasswordField(); // text field for the password
        grid.add(pwBox, 1, 2);

        btn = new Button("Sign in");
        btn.setOnAction(e -> setUsernamePassword());
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btn);
        grid.add(hbBtn, 1, 4);

        final Text actiontarget = new Text();
        grid.add(actiontarget, 1, 6);

        scene = new Scene(grid, 300, 275);
    }

    private void setUsernamePassword()
    {
        btn.setTextFill(Color.FIREBRICK);
        username = userTextField.getText();
        password = pwBox.getText();

        // TODO: comment this out after testing
        System.out.println("Username: " + username);
        System.out.println("Password: " + password);
    }
}
