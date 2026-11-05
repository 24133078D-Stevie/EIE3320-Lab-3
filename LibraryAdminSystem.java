/**
 * LAM King Shan (24131593D)
 * LAU Ching Hoi (24133078D)
 */

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LibraryAdminSystem extends Application{
    public void start(Stage primaryStage){
        primaryStage.setTitle("Library Admin System");
        
        //java.time.LocalDateTime; java.time.format.DateTimeFormatter;
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String startTime = currentDateTime.format(formatter);
        
        // top pane - Name: and Program Stary
        VBox headerPanel = new VBox(3);
        headerPanel.setPadding(new Insets(10));
        headerPanel.getChildren().addAll(
            new Label("Name: LAM King Shan  Student ID: 24131593D"),
            new Label("Name: LAU Ching Hoi  Student ID: 24133078D"),
            new Label("Program Started: " + startTime)
        );
        
        
        // Book Grid
        // Create the column
        TableColumn<Book, String> isbnColumn = new TableColumn<>("ISBN");
        // Tell the column to look for getIsbn() in the Book class
        isbnColumn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        isbnColumn.setPrefWidth(150);
        
        
        // Add column to table and set data
        /*
        bookTable.getColumns().add(isbnColumn);
        bookTable.setItems(tableData); // tableData is your ObservableList
        
        */
       
       
           
        // button
        Button btnAdd = new Button("Add");
        Button btnEdit = new Button("Edit");
        Button btnSave = new Button("Save");
        Button btnDelete = new Button("Delete");
        Button btnSearch = new Button("Search");
        Button btnDisplayAll = new Button("Display All");
        Button btnLoadTestData = new Button("Load Test Data");
        Button btnBorrow = new Button("Borrow");
        Button btnReturn = new Button("Return");
        Button btnExit = new Button("Exit");
        Button btnClear = new Button("Clear");
        
        // Testing top pane
        BorderPane mainApp = new BorderPane();
        mainApp.setTop(headerPanel);
        
        Scene scene = new Scene(mainApp, 700, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}