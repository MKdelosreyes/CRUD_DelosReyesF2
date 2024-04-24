package com.example.csit228f2_2;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HelloApplication extends Application {
    private Stage stage = new Stage();
//    public static List<User> users;
    public static void main(String[] args) {
        try (Connection c = SQLConnection.getConnection();
             Statement statement = c.createStatement()) {
            String sqlquery = "CREATE TABLE IF NOT EXISTS tbluser (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "username VARCHAR(50) NOT NULL, " +
                    "email VARCHAR(100) NOT NULL, " +
                    "password VARCHAR(100) NOT NULL)";
            statement.execute(sqlquery);
            System.out.println("Table created successfully");
            System.out.println(HelloApplication.class.getResource("hello-view.fxml"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        showLoginPage(this.stage);
    }

    public Stage getStage() {
        return stage;
    }

    public void showLoginPage(Stage stage) throws Exception {
//        users = new ArrayList<>();
        AnchorPane pnMain = new AnchorPane();
        GridPane grid = new GridPane();
        pnMain.getChildren().add(grid);
        grid.setAlignment(Pos.CENTER);
//        grid.setGridLinesVisible(true);

        grid.getColumnConstraints().add(new ColumnConstraints(170)); // column 0 is 100 wide
        grid.getColumnConstraints().add(new ColumnConstraints(300)); // column 1 is 200 wide
        grid.getColumnConstraints().add(new ColumnConstraints());
        grid.getColumnConstraints().add(new ColumnConstraints(45));

        grid.getRowConstraints().add(new RowConstraints(20));
        grid.getRowConstraints().add(new RowConstraints(50));
        grid.getRowConstraints().add(new RowConstraints(25));
        grid.getRowConstraints().add(new RowConstraints());
        grid.getRowConstraints().add(new RowConstraints(8));
        grid.getRowConstraints().add(new RowConstraints());
        grid.getRowConstraints().add(new RowConstraints(15));
        grid.getRowConstraints().add(new RowConstraints());
        grid.getRowConstraints().add(new RowConstraints(15));


        Text sceneTitle = new Text("Record Player tani");
        sceneTitle.setTextAlignment(TextAlignment.CENTER);
        sceneTitle.setStrokeType(StrokeType.CENTERED);
        sceneTitle.setStrokeWidth(50);
        sceneTitle.setFill(Paint.valueOf("#325622"));
        sceneTitle.setFont(Font.font("Arial", FontWeight.BOLD, 40));
        grid.setHalignment(sceneTitle, HPos.CENTER);
        grid.setColumnSpan(sceneTitle, 5);
        grid.setRowSpan(sceneTitle, 1);

        grid.add(sceneTitle, 0, 1);
        grid.requestLayout();

        Group gpSignIn = new Group();

        Label lblUsername = new Label("Username: ");
        lblUsername.setTextFill(Paint.valueOf("#000000"));
        lblUsername.setFont(Font.font("Tahoma",13));
        lblUsername.setPadding(new Insets(0, 0, 0, 85));
        grid.add(lblUsername, 0, 3);

        TextField tfUsername = new TextField();
        tfUsername.setFont(Font.font(14));
        tfUsername.setPrefWidth(300);
        grid.add(tfUsername, 1, 3);

        Label lblEmail = new Label("Email: ");
        lblEmail.setTextFill(Paint.valueOf("#000000"));
        lblEmail.setFont(Font.font("Tahoma",13));
        lblEmail.setPadding(new Insets(0, 0, 0, 85));
        grid.add(lblEmail, 0, 5);

        TextField tfEmail = new TextField();
        tfEmail.setFont(Font.font(14));
        tfEmail.setPrefWidth(300);
        grid.add(tfEmail, 1, 5);

        lblEmail.setVisible(false);
        tfEmail.setVisible(false);

        Label lblPassword = new Label("Password: ");
        lblPassword.setTextFill(Paint.valueOf("#000000"));
        lblPassword.setFont(Font.font("Tahoma",13));
        lblPassword.setPadding(new Insets(0, 0, 0, 85));
        grid.add(lblPassword, 0, 5);

        TextField pfPassword = new PasswordField();
        pfPassword.setFont(Font.font(14));
        pfPassword.setPrefWidth(300);
        grid.add(pfPassword, 1, 5);

        Button btnShow = new Button("<*>");
        btnShow.setFont(Font.font("System", 13));

        HBox hbShow = new HBox();
        hbShow.getChildren().add(btnShow);
        hbShow.setAlignment(Pos.CENTER);
        hbShow.setMaxWidth(400);

        TextField tfPassword = new TextField();
        tfPassword.setFont(Font.font(15));
        hbShow.getChildren().add(tfPassword);
        grid.add(tfPassword, 1, 5);
        grid.add(hbShow, 2, 5);
        tfPassword.setVisible(false);
//        grid.add(gpSignIn, 0, 1);

        btnShow.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent actionEvent) {
                tfPassword.setText(pfPassword.getText());
                tfPassword.setVisible(true);
                pfPassword.setVisible(false);
//                grid.add(new Button("Hello"), 4,4);
            }
        });

        btnShow.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                tfPassword.setVisible(false);
                pfPassword.setVisible(true);
            }
        });

        Button btnSignIn = new Button("Sign In");
        btnSignIn.setFont(Font.font(16));
        Button btnSignUp = new Button("Sign Up");
        btnSignUp.setFont(Font.font(16));
        btnSignUp.setVisible(false);

        HBox hbSignIn = new HBox();
        hbSignIn.getChildren().add(btnSignIn);
        hbSignIn.setMargin(btnSignIn, new Insets(0, 0, 0, 110));
        hbSignIn.setAlignment(Pos.CENTER);
        grid.add(hbSignIn, 0, 7, 2, 1);

        HBox hbSignUp = new HBox();
        hbSignUp.getChildren().add(btnSignUp);
        hbSignUp.setPadding(new Insets(0, 0, 0, 110));
        hbSignUp.setAlignment(Pos.CENTER);
        grid.add(hbSignUp, 0, 9, 2, 1);


        final Text actionTarget = new Text("");
        actionTarget.setFont(Font.font(10));
        grid.add(actionTarget, 1, 6);

        final Hyperlink registerText = new Hyperlink("Don't have an account? Sign Up!");
        registerText.setFont(Font.font(10));
        registerText.setTranslateX(60);
        registerText.setUnderline(true);
        grid.add(registerText, 1, 9);

        TextField password = new PasswordField();
        password.setFont(Font.font(14));
        password.setPrefWidth(300);

        registerText.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                GridPane.setConstraints(lblPassword, 0, 7);
                pfPassword.setVisible(false);
                grid.add(password, 1, 7);
                // update constraints for tfPassword and hbShow
                GridPane.setConstraints(tfPassword, 1, 7);
                GridPane.setConstraints(hbShow, 2, 7);
                pfPassword.isFocusTraversable();
                grid.getRowConstraints().get(6).setPrefHeight(5);
                hbShow.setVisible(false);
                registerText.setVisible(false);
                btnSignIn.setVisible(false);
                actionTarget.setVisible(false);
                tfUsername.setText("");
                pfPassword.setText("");
                lblEmail.setVisible(true);
                tfEmail.setVisible(true);
                btnSignUp.setVisible(true);

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        grid.requestLayout();
                    }
                });
            }
        });

        btnSignUp.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                /*-------------Create database table for tblAudioFiles---------------*/
                try (Connection c = SQLConnection.getConnection();
                     Statement statement = c.createStatement()) {
                    String sqlquery = "CREATE TABLE IF NOT EXISTS tblrecord (" +
                            "id INT AUTO_INCREMENT PRIMARY KEY," +
                            "title VARCHAR(255) NOT NULL, " +
                            "description VARCHAR(255) NOT NULL, " +
                            "album VARCHAR(255) NOT NULL, " +
                            "file_path VARCHAR(255) NOT NULL)";
                    statement.execute(sqlquery);
                    System.out.println("tblsong created successfully");
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }


                registerText.setVisible(false);
                registerText.setFocusTraversable(false);
                try (Connection c = SQLConnection.getConnection();
                     PreparedStatement statement = c.prepareStatement(
                             "INSERT INTO tbluser (username,email,password) Values (?,?,?)")) {
                    String name = tfUsername.getText();
                    String email = tfEmail.getText();
                    String pw = password.getText();
                    statement.setString(1, name);
                    statement.setString(2, email);
                    statement.setString(3, pw);
                    int updateCount = statement.executeUpdate();
                    if(updateCount > 0) {
                        System.out.println("Data inserted successfully.");
                        RecordPlayer record = new RecordPlayer();
                        Stage stage = (Stage) btnSignUp.getScene().getWindow();
                        stage.close();
                        record.showRecord(stage);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });

        btnSignIn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                int flag = 0;
                String username = tfUsername.getText();
                String password = pfPassword.getText();
//                ArrayList list = new ArrayList();
                try (Connection c = SQLConnection.getConnection();
                     Statement statement = c.createStatement()) {
                    String sqlquery = "SELECT * FROM tbluser";
                    ResultSet resultSet = statement.executeQuery(sqlquery);
                    while(resultSet.next() && flag == 0) {
                        String name = resultSet.getString("username");
                        String pw = resultSet.getString("password");
                        if(name.equals(username) && pw.equals(password)) {
                            RecordPlayer record = new RecordPlayer();
                            Stage stage = (Stage) btnSignUp.getScene().getWindow();
                            stage.close();
                            record.showRecord(stage);
                            flag = 1;
                        }
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                actionTarget.setText("Invalid username/password. Try again or go to sign up.");
                actionTarget.setOpacity(1);
            }
        });

        EventHandler<KeyEvent> fieldChange = new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent actionEvent) {
                actionTarget.setOpacity(0);
            }
        };
        tfUsername.setOnKeyTyped(fieldChange);
        pfPassword.setOnKeyTyped(fieldChange);


        Scene scene = new Scene(pnMain, javafx.scene.layout.Region.USE_PREF_SIZE, 300);
        stage.setScene(scene);
        stage.show();
    }
}