package com.example.csit228f2_2;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RecordPlayer {
    private Stage stage = new Stage();

    public void showRecord(Stage stage) throws Exception {
        AnchorPane pnMain = new AnchorPane();
        GridPane grid = new GridPane();
        pnMain.getChildren().add(grid);
        grid.setAlignment(Pos.CENTER);
//        grid.setGridLinesVisible(true);

        grid.getColumnConstraints().add(new ColumnConstraints(45));
        grid.getColumnConstraints().add(new ColumnConstraints(85));
        grid.getColumnConstraints().add(new ColumnConstraints(180));
        grid.getColumnConstraints().add(new ColumnConstraints(155));
        grid.getColumnConstraints().add(new ColumnConstraints(80));

        grid.getRowConstraints().add(new RowConstraints(10));
        grid.getRowConstraints().add(new RowConstraints(30));
        grid.getRowConstraints().add(new RowConstraints(25));
        grid.getRowConstraints().add(new RowConstraints(15));
        grid.getRowConstraints().add(new RowConstraints(20));
        grid.getRowConstraints().add(new RowConstraints(20));
        grid.getRowConstraints().add(new RowConstraints(20));
        grid.getRowConstraints().add(new RowConstraints(24));
        grid.getRowConstraints().add(new RowConstraints(24));
        grid.getRowConstraints().add(new RowConstraints(24));
        grid.getRowConstraints().add(new RowConstraints(24));

        Image image = new Image(getClass().getResourceAsStream("logout-icon.png"));
        ImageView imageView = new ImageView(image);
        Button btnLogOut = new Button("", imageView);
        btnLogOut.setPrefWidth(20);
        grid.add(btnLogOut, 0, 1);

        btnLogOut.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                HelloApplication mainApp = new HelloApplication();
                Stage stage = (Stage) btnLogOut.getScene().getWindow();
                stage.close();
                try {
                    mainApp.showLoginPage(stage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        Image image1 = new Image(getClass().getResourceAsStream("day-night-mode.png"));
        ImageView imageView1 = new ImageView(image1);
        imageView1.setFitWidth(20);
        imageView1.setFitHeight(20);
        ToggleButton tbNightMode = new ToggleButton("", imageView1);
        tbNightMode.setPrefWidth(20);
        grid.add(tbNightMode, 0, 2);
        
        tbNightMode.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (tbNightMode.isSelected()) {
                    // night mode
                    tbNightMode.getScene().getStylesheets().add(
                            getClass().getResource("styles.css").toExternalForm());
                } else {
                    tbNightMode.getScene().getStylesheets().clear();
                }
            }
        });

        TableView<Song> tableView = new TableView<>();
        TableColumn<Song, String> titleColumn = new TableColumn<>("Sound Record Title");
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        titleColumn.setPrefWidth(130);
        TableColumn<Song, String> descColumn = new TableColumn<>("Description");
        descColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        descColumn.setPrefWidth(160);
        TableColumn<Song, String> albumColumn = new TableColumn<>("Album/Topic");
        albumColumn.setCellValueFactory(new PropertyValueFactory<>("album"));
        albumColumn.setPrefWidth(120);

        TableColumn<Song, Void> update_delete = new TableColumn<>("Update/Delete");
        update_delete.setCellValueFactory(new PropertyValueFactory<>(""));
        update_delete.setPrefWidth(100);
        update_delete.setCellFactory(param -> new TableCell<Song, Void>() {
            private final Hyperlink editLink = new Hyperlink("Edit");
            private final Hyperlink deleteLink = new Hyperlink("Delete");
            private boolean isEditMode = false;
            {
                editLink.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        if (!isEditMode) {
                            tableView.setEditable(true);
                            titleColumn.setCellFactory(TextFieldTableCell.forTableColumn());
                            descColumn.setCellFactory(TextFieldTableCell.forTableColumn());
                            albumColumn.setCellFactory(TextFieldTableCell.forTableColumn());

                            tableView.edit(getIndex(), titleColumn);

                            editLink.setText("Update");

                            titleColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Song, String>>() {
                                @Override
                                public void handle(TableColumn.CellEditEvent<Song, String> event) {
                                    Song song = event.getRowValue();
                                    song.setTitle(event.getNewValue());
                                    if (!isEditMode) {
                                        updateSongInDatabase(song);
                                    }
                                }
                            });

                            descColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Song, String>>() {
                                @Override
                                public void handle(TableColumn.CellEditEvent<Song, String> event) {
                                    Song song = event.getRowValue();
                                    song.setDescription(event.getNewValue());
                                    if (!isEditMode) {
                                        updateSongInDatabase(song);
                                    }
                                }
                            });

                            albumColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Song, String>>() {
                                @Override
                                public void handle(TableColumn.CellEditEvent<Song, String> event) {
                                    Song song = event.getRowValue();
                                    song.setAlbum(event.getNewValue());
                                    if (!isEditMode) {
                                        updateSongInDatabase(song);
                                    }
                                }
                            });
                        } else {
                            tableView.setEditable(false);
                            editLink.setText("Edit");
                            tableView.getItems().forEach(song -> updateSongInDatabase(song));
                        }

                        isEditMode = !isEditMode;
                    }
                });



                deleteLink.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        Song song = getTableView().getItems().get(getIndex());
                        try (Connection c = SQLConnection.getConnection();
                             PreparedStatement statement = c.prepareStatement("DELETE FROM tblrecord WHERE title = ?")) {
                             statement.setString(1, song.getTitle());
                             int updateCount = statement.executeUpdate();
                             if (updateCount > 0) {
                                 getTableView().getItems().remove(song);
                                 System.out.println("Record deleted successfully.");
                             }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox hbox = new HBox(5);
                    hbox.getChildren().addAll(editLink, deleteLink);
                    setGraphic(hbox);
                }
            }
        });

        tableView.getColumns().addAll(titleColumn, descColumn, albumColumn, update_delete);

        ObservableList<Song> songList = FXCollections.observableArrayList();
        List<Song> songsFromDatabase = retrieveSongsFromDatabase();
        songList.addAll(songsFromDatabase);
        tableView.setItems(songList);

        ScrollPane scrollPane = new ScrollPane(tableView);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        grid.add(scrollPane, 1, 1, 4, 5);

        Text sceneTitle = new Text("Add:");
//        sceneTitle.setTextAlignment(TextAlignment.CENTER);
        sceneTitle.setFill(Paint.valueOf("#000000"));
        sceneTitle.setFont(Font.font("Arial", 14));
//        grid.setHalignment(sceneTitle, HPos.CENTER);
//        grid.setColumnSpan(sceneTitle, 5);
//        grid.setRowSpan(sceneTitle, 1);

        grid.add(sceneTitle, 1, 7);

        Label lblTitle = new Label("Title: ");
        lblTitle.setTextFill(Paint.valueOf("#000000"));
        lblTitle.setFont(Font.font("Tahoma",12));
        lblTitle.setPadding(new Insets(0, 0, 0, 15));
        grid.add(lblTitle, 1, 8);

        TextField tfTitle = new TextField();
        tfTitle.setFont(Font.font(12));
        tfTitle.setPrefWidth(300);
        grid.add(tfTitle, 2, 8);

        Label lblDesc = new Label("Description: ");
        lblDesc.setTextFill(Paint.valueOf("#000000"));
        lblDesc.setFont(Font.font("Tahoma",12));
        lblDesc.setPadding(new Insets(0, 0, 0, 15));
        grid.add(lblDesc, 1, 9);

        TextArea tfDesc = new TextArea();
        tfDesc.setFont(Font.font(12));
        tfDesc.setPrefWidth(300);
        grid.add(tfDesc, 2, 9, 1, 2);

        Label lblAlbum = new Label("Album: ");
        lblAlbum.setTextFill(Paint.valueOf("#000000"));
        lblAlbum.setFont(Font.font("Tahoma",12));
        lblAlbum.setPadding(new Insets(0, 0, 0, 15));
        grid.add(lblAlbum, 1, 11);

        TextField tfAlbum = new TextField();
        tfAlbum.setFont(Font.font(12));
        tfAlbum.setPrefWidth(300);
        grid.add(tfAlbum, 2, 11);

        TextField tfFile = new TextField();
        tfFile.setFont(Font.font(12));
        tfFile.setPrefWidth(300);
        grid.add(tfFile, 2, 12);

        Button btnChooseFile = new Button("Choose File");
        btnChooseFile.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Resource File");
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("MP3 files (*.mp3)", "*.mp3");
            fileChooser.getExtensionFilters().add(extFilter);
            File selectedFile = fileChooser.showOpenDialog(stage);
            if (selectedFile != null) {
                String filePath = selectedFile.getPath();
                tfFile.setText(selectedFile.getName());
                //filePath = selectedFile.getPath();
            }
        });
        grid.add(btnChooseFile, 1, 12);

        Button submit = new Button("Add recording");
        submit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try (Connection c = SQLConnection.getConnection();
                     PreparedStatement statement = c.prepareStatement(
                             "INSERT INTO tblrecord (title,description,album, file_path) Values (?,?,?,?)")) {
                    String title = tfTitle.getText();
                    String desc = tfDesc.getText();
                    String album = tfAlbum.getText();
                    String filename = tfFile.getText();
                    statement.setString(1, title);
                    statement.setString(2, desc);
                    statement.setString(3, album);
                    statement.setString(4, filename);
                    int updateCount = statement.executeUpdate();
                    if(updateCount > 0) {
                        System.out.println("Data inserted successfully to tblrecord.");
                        tfTitle.setText("");
                        tfDesc.setText("");
                        tfAlbum.setText("");
                        tfFile.setText("");

                        ObservableList<Song> updatedSongList = FXCollections.observableArrayList();
                        List<Song> updatedSongsFromDatabase = retrieveSongsFromDatabase();
                        updatedSongList.addAll(updatedSongsFromDatabase);
                        tableView.setItems(updatedSongList);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });

        grid.add(submit, 2, 13);

        Scene scene = new Scene(pnMain, 565, 330);
        stage.setScene(scene);
        stage.show();
    }

    private void updateSongInDatabase(Song song) {
        try (Connection c = SQLConnection.getConnection();
             PreparedStatement statement = c.prepareStatement("UPDATE tblrecord SET title = ?, description = ?, album = ? WHERE title = ?")) {
            statement.setString(1, song.getTitle());
            statement.setString(2, song.getDescription());
            statement.setString(3, song.getAlbum());
            statement.setString(4, song.getTitle());
            int updateCount = statement.executeUpdate();
            if (updateCount > 0) {
                System.out.println("Record updated successfully.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Song> retrieveSongsFromDatabase() {
        List<Song> songList = new ArrayList<>();
        try (Connection c = SQLConnection.getConnection();
             Statement statement = c.createStatement()) {
            String sqlquery = "SELECT title, description, album FROM tblrecord";
            ResultSet resultSet = statement.executeQuery(sqlquery);
            while(resultSet.next()) {
                String title = resultSet.getString("title");
                String description = resultSet.getString("description");
                String album = resultSet.getString("album");

                Song song = new Song(title, description, album);
                songList.add(song);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return songList;
    }

//    private void refreshTableView() {
//        ObservableList<Song> updatedSongList = FXCollections.observableArrayList();
//        List<Song> updatedSongsFromDatabase = retrieveSongsFromDatabase();
//        updatedSongList.addAll(updatedSongsFromDatabase);
//    }

    public static class Song {
        private String title;
        private String description;
        private String album;
        private String filePath;

        public Song(String title, String description, String album) {
            this.title = title;
            this.description = description;
            this.album = album;
//            this.filePath = filePath;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getAlbum() {
            return album;
        }

        public void setAlbum(String album) {
            this.album = album;
        }

        public String getFilePath() {
            return filePath;
        }

        public void setFilePath(String filePath) {
            this.filePath = filePath;
        }
    }
}
