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

    private TextField isbnField;
    private TextField titleField;

    private ArrayList<Book> books = new ArrayList<>();

    private ObservableList<Book> tableData = FXCollections.observableArrayList();

    private TableView<Book> bookTable;

    private Book selectedBook = null;
    
    private Label messageLabel;

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
            new Label("Name: LAM King Shan\t\t Student ID: 24131593D"),
            new Label("Name: LAU Ching Hoi\t\t  Student ID: 24133078D"),
            new Label("Program Started: " + startTime)
        );

        //Centre Panel:  book records displayed in a table,  ISBN and Title text fields

        // Book Grid
        bookTable = new TableView<Book>();

        // Create the column
        TableColumn<Book, String> isbnColumn = new TableColumn<>("ISBN");
        // Tell the column to look for getIsbn() in the Book class
        isbnColumn.setCellValueFactory(new PropertyValueFactory<>("ISBN"));
        isbnColumn.setPrefWidth(150);

        TableColumn<Book, String> titleColumn = new TableColumn<>("Title");
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        titleColumn.setPrefWidth(150);

        TableColumn<Book, String> availabilityColumn = new TableColumn<>("Availability");
        availabilityColumn.setCellValueFactory(new PropertyValueFactory<>("availability"));
        availabilityColumn.setPrefWidth(150);

        bookTable.getColumns().addAll(isbnColumn, titleColumn, availabilityColumn);
        bookTable.setItems(tableData);

        bookTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);//Remove the empty space

        // Listen for clicks on the header
        /*
        isbnColumn.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, e -> {
        // Sort the main ArrayList
        books.sort(Comparator.comparing(Book::getIsbn));

        // If it's currently descending, reverse the list!
        if (!sortByIsbnAscending) {
        java.util.Collections.reverse(books);
        }

        // Flip the boolean for next time
        sortByIsbnAscending = !sortByIsbnAscending;

        // Refresh the table with the newly sorted list
        displayAllBooks(); 
        });
         */

        // Below the table: ISBN and Title fields
        GridPane formGrid = new GridPane();

        formGrid.setAlignment(Pos.CENTER);

        formGrid.setPadding(new Insets(15));

        formGrid.setHgap(8);

        formGrid.setVgap(10);

        // Create text fields
        isbnField = new TextField();
        titleField = new TextField();
        isbnField.setPrefWidth(260);
        titleField.setPrefWidth(260);

        bookTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                isbnField.setText(newValue.getISBN());
                titleField.setText(newValue.getTitle());
                selectedBook = newValue;
                }
            }
        );
        
        // Create Clear button
        Button btnClear = new Button("Clear");

        // Add labels and text fields
        formGrid.add(new Label("ISBN:"), 0, 0);
        formGrid.add(isbnField, 1, 0);
        formGrid.add(new Label("Title:"), 0, 1);
        formGrid.add(titleField, 1, 1);
        formGrid.add(btnClear, 2, 1);

        VBox centrePanel = new VBox(10);
        centrePanel.setPadding(new Insets(10));
        centrePanel.getChildren().addAll(bookTable,formGrid);

        //bottom panel
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

        FlowPane firstButtonRow = new FlowPane(10, 10);

        firstButtonRow.setAlignment(Pos.CENTER);

        firstButtonRow.getChildren().addAll(btnAdd, btnEdit, btnSave,btnDelete,btnSearch);

        // Second button row
        FlowPane secondButtonRow = new FlowPane(10, 10);

        secondButtonRow.setAlignment(Pos.CENTER);

        secondButtonRow.getChildren().addAll(btnDisplayAll, btnLoadTestData, btnBorrow, btnReturn, btnExit);

        // System message
        messageLabel = new Label("Displaying all book records in their current order.");

        // Bottom Panel
        VBox bottomPanel = new VBox(10);

        bottomPanel.setAlignment(Pos.CENTER);

        bottomPanel.setPadding(new Insets(15));

        bottomPanel.getChildren().addAll(firstButtonRow, secondButtonRow, messageLabel);

        // Button Event Handler
        //btnLoadTestData.setOnAction(e -> loadTestData());
        
        btnClear.setOnAction(e -> clear());
        btnAdd.setOnAction(e -> add());
        btnLoadTestData.setOnAction(e -> loadTestData());
        btnSearch.setOnAction(e -> search());
        btnDelete.setOnAction(e -> delete());
        btnBorrow.setOnAction(e -> borrow());
        btnReturn.setOnAction(e -> returnBk());
        
        /*
        btnEdit.setOnAction(e -> edit());
        btnSave.setOnAction(e -> save());
        btnDisplayAll.setOnAction(e -> displayAll());
        btnExit.setOnAction(e -> primaryStage.close());
         */

        // Testing top pane
        BorderPane mainApp = new BorderPane();
        mainApp.setTop(headerPanel);
        mainApp.setCenter(centrePanel);
        mainApp.setBottom(bottomPanel);

        Scene scene = new Scene(mainApp, 700, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    private void add() {
        // Get ISBN and Title from TextFields
        String isbn = isbnField.getText().trim();
        String title = titleField.getText().trim();
    
        // Check empty ISBN or Title
        if (isbn.isEmpty() || title.isEmpty()) {
            showError("Please enter both ISBN and Title.");
            return;
        }
    
        // Check duplicate ISBN
        for (Book book : books) {
    
            if (book.getISBN().equalsIgnoreCase(isbn)) {
    
                showError("Duplicate ISBN: " + isbn + " already exists.");
                return;
            }
        }
    
        // Create a new Book object
        Book newBook = new Book(isbn, title, true);
    
        // Add book to ArrayList
        books.add(newBook);
    
        // Update TableView
        tableData.setAll(books);
    
        // Clear text fields
        isbnField.clear();
        titleField.clear();
    
        // Remove selected row
        bookTable.getSelectionModel().clearSelection();
    
        // Reset selected book
        selectedBook = null;
    
    }   
    
    //Clear not done
    private void clear(){
        isbnField.clear();
        titleField.clear();
    
        // Remove selected row
        bookTable.getSelectionModel().clearSelection();
    
        // Reset selected book
        selectedBook = null;
        
        messageLabel.setText("Text fields cleared.");
    }
    
    // handle deplicate?
    private void loadTestData(){
        books.add(new Book("0131450913", "HTML How to Program", true));
        books.add(new Book("0132222205", "Java How to Program", true));
        books.add(new Book("0131857576", "C++ How to Program", true));

        // Display the books in TableView
        tableData.setAll(books); 
        
    }
    
    private void search(){
        String isbn = isbnField.getText().trim().toLowerCase();
        String title = titleField.getText().trim().toLowerCase();
        
        if(isbn.isEmpty() && title.isEmpty()){
            showError("Please enter a partial ISBN and/or partial Title for searching.");
        }
        
        ArrayList<Book> searchBooks = new ArrayList<>();
        
        for (Book book : books){
            boolean match = (isbn.isEmpty() &&
            book.getISBN().toLowerCase().contains(isbn)) || (!title.isEmpty() &&
            book.getTitle().toLowerCase().contains(title));
            
            if(match){
                searchBooks.add(book);
            }
             
        }
        tableData.setAll(searchBooks);
        

        messageLabel.setText(searchBooks.size() + " book record(s) found. Search does not change or delete book records");
    }

    private void delete(){
        String isbn = isbnField.getText().trim();
        
        if (selectedBook == null && isbn.isEmpty()) {
            showError("No book record is available for deletion.");
            return;
        }
        
        if (isbn.isEmpty()) {
            isbn = selectedBook.getISBN();
        }
        
        Book bookDelete = null;
        
        for(Book book: books){
            if(book.getISBN().equalsIgnoreCase(isbn)){
                bookDelete = book;
                break;
            }
        }
        
        if (bookDelete == null) {
            showError("Book with ISBN " + isbn + " cannot be found.");
            return;
        }
            books.remove(bookDelete);
        tableData.setAll(books);
        
        isbnField.clear();
        titleField.clear();
        bookTable.getSelectionModel().clearSelection();
        
        selectedBook = null;
        
    }
    
    private void borrow(){
        if(selectedBook == null){
            showError("Please select a book first.");
            return;
        }
        
        if(!selectedBook.getAvailable()){
            showError("The selected book is not available.");
            return;
        }
        
        selectedBook.setAvailable(false);
        
        bookTable.refresh();
        messageLabel.setText("The book is borrowed and books.txt has been updated.");
    }
    
    private void returnBk(){
        if(selectedBook == null){
            showError("Please select a book first.");
            return;
        }
        
        if (selectedBook.getAvailable()) {
            showError("The selected book is already available.");
            return;
        }
        
        selectedBook.setAvailable(true);
        
        bookTable.refresh();
        
        messageLabel.setText("The book is returned and books.txt has been updated.");
    }
    
    private void showError(String message) { 
        Alert alert = new Alert(Alert.AlertType.ERROR); 
        alert.setTitle("Library Admin System"); 
        alert.setHeaderText("Error"); 
        alert.setContentText(message); 
        alert.showAndWait(); 
        messageLabel.setText(message); 
    }     

    public static void main(String[] args) {
        launch(args);
    }
}