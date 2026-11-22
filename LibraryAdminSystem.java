/**
 * LAM King Shan (24131593D)
 * LAU Ching Hoi (24133078D)
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LibraryAdminSystem extends Application {
    private final ArrayList<Book> books = new ArrayList<>();
    private final ObservableList<Book> tableData = FXCollections.observableArrayList();

    private TableView<Book> bookTable;
    private TextField isbnField;
    private TextField titleField;
    private Label messageLabel;

    // Action Buttons
    private Button btnAdd;
    private Button btnEdit;
    private Button btnSave;
    private Button btnDelete;
    private Button btnSearch;
    private Button btnDisplayAll;
    private Button btnLoadTestData;
    private Button btnBorrow;
    private Button btnReturn;
    private Button btnClear;
    private Button btnExit;

    // State Tracking
    private Book selectedBook = null;
    private Book editingBook = null;
    private boolean isEditMode = false;

    private static final String FILE_NAME = "books.txt";

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Library Admin System");

        // Top: Names and Program Start Time
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String startTime = currentDateTime.format(formatter);

        VBox headerPanel = new VBox(3);
        headerPanel.setPadding(new Insets(10));
        headerPanel.getChildren().addAll(
            new Label("Name: LAM King Shan\t\tStudent ID: 24131593D"),
            new Label("Name: LAU Ching Hoi\t\tStudent ID: 24133078D"),
            new Label("Program Started: " + startTime)
        );

        // Center: Book TableView
        bookTable = new TableView<>();
        bookTable.setPlaceholder(new Label("No book records"));

        TableColumn<Book, String> isbnColumn = new TableColumn<>("ISBN");
        isbnColumn.setCellValueFactory(new PropertyValueFactory<>("ISBN"));
        isbnColumn.setPrefWidth(150);

        TableColumn<Book, String> titleColumn = new TableColumn<>("Title");
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        titleColumn.setPrefWidth(220);

        TableColumn<Book, String> availabilityColumn = new TableColumn<>("Availability");
        availabilityColumn.setCellValueFactory(new PropertyValueFactory<>("availability"));
        availabilityColumn.setPrefWidth(130);
        availabilityColumn.setSortable(false); // Only ISBN and Title are sortable

        bookTable.getColumns().addAll(isbnColumn, titleColumn, availabilityColumn);
        bookTable.setItems(tableData);
        bookTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Custom sort to sort ArrayList<Book>
        bookTable.setSortPolicy(tv -> {
            ObservableList<TableColumn<Book, ?>> sortOrder = tv.getSortOrder();
            if (!sortOrder.isEmpty()) {
                TableColumn<Book, ?> col = sortOrder.get(0);
                if (col == isbnColumn) {
                    if (isbnColumn.getSortType() == TableColumn.SortType.ASCENDING) {
                        books.sort(Comparator.comparing(Book::getISBN, String.CASE_INSENSITIVE_ORDER));
                        messageLabel.setText("Sorted by ISBN in ascending order.");
                    } else {
                        books.sort(Comparator.comparing(Book::getISBN, String.CASE_INSENSITIVE_ORDER).reversed());
                        messageLabel.setText("Sorted by ISBN in descending order.");
                    }
                } else if (col == titleColumn) {
                    if (titleColumn.getSortType() == TableColumn.SortType.ASCENDING) {
                        books.sort(Comparator.comparing(Book::getTitle, String.CASE_INSENSITIVE_ORDER));
                        messageLabel.setText("Sorted by Title in ascending order.");
                    } else {
                        books.sort(Comparator.comparing(Book::getTitle, String.CASE_INSENSITIVE_ORDER).reversed());
                        messageLabel.setText("Sorted by Title in descending order.");
                    }
                }
                tableData.setAll(books);
                return true;
            }
            return true;
        });

        // Row selection listener
        bookTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (!isEditMode) {
                selectedBook = newVal;
                if (newVal != null) {
                    isbnField.setText(newVal.getISBN());
                    titleField.setText(newVal.getTitle());
                }
                updateButtonStates();
            }
        });

        // Form fields
        GridPane formGrid = new GridPane();
        formGrid.setAlignment(Pos.CENTER);
        formGrid.setPadding(new Insets(10));
        formGrid.setHgap(10);
        formGrid.setVgap(10);

        isbnField = new TextField();
        isbnField.setPrefWidth(260);
        titleField = new TextField();
        titleField.setPrefWidth(260);
        btnClear = new Button("Clear");

        formGrid.add(new Label("ISBN:"), 0, 0);
        formGrid.add(isbnField, 1, 0);
        formGrid.add(new Label("Title:"), 0, 1);
        formGrid.add(titleField, 1, 1);
        formGrid.add(btnClear, 2, 1);

        VBox centrePanel = new VBox(10);
        centrePanel.setPadding(new Insets(10));
        centrePanel.getChildren().addAll(bookTable, formGrid);

        // Bottom: Buttons and system message
        btnAdd = new Button("Add");
        btnEdit = new Button("Edit");
        btnSave = new Button("Save");
        btnDelete = new Button("Delete");
        btnSearch = new Button("Search");

        HBox firstButtonRow = new HBox(10);
        firstButtonRow.setAlignment(Pos.CENTER);
        firstButtonRow.getChildren().addAll(btnAdd, btnEdit, btnSave, btnDelete, btnSearch);

        btnDisplayAll = new Button("Display All");
        btnLoadTestData = new Button("Load Test Data");
        btnBorrow = new Button("Borrow");
        btnReturn = new Button("Return");
        btnExit = new Button("Exit");

        HBox secondButtonRow = new HBox(10);
        secondButtonRow.setAlignment(Pos.CENTER);
        secondButtonRow.getChildren().addAll(btnDisplayAll, btnLoadTestData, btnBorrow, btnReturn, btnExit);

        messageLabel = new Label("Displaying all book records in their current order.");

        VBox bottomPanel = new VBox(10);
        bottomPanel.setAlignment(Pos.CENTER);
        bottomPanel.setPadding(new Insets(15));
        bottomPanel.getChildren().addAll(firstButtonRow, secondButtonRow, messageLabel);

        // Attach event handlers
        btnAdd.setOnAction(e -> add());
        btnLoadTestData.setOnAction(e -> loadTestData());
        btnEdit.setOnAction(e -> edit());
        btnSave.setOnAction(e -> save());
        btnDelete.setOnAction(e -> delete());
        btnSearch.setOnAction(e -> search());
        btnDisplayAll.setOnAction(e -> displayAll());
        btnBorrow.setOnAction(e -> borrow());
        btnReturn.setOnAction(e -> returnBk());
        btnClear.setOnAction(e -> clear());
        btnExit.setOnAction(e -> primaryStage.close());

        // Load books.txt on startup
        loadFromFile();
        displayAll();
        updateButtonStates();

        // Build scene and show
        BorderPane mainApp = new BorderPane();
        mainApp.setTop(headerPanel);
        mainApp.setCenter(centrePanel);
        mainApp.setBottom(bottomPanel);

        Scene scene = new Scene(mainApp, 720, 620);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Action methods

    private void add() {
        String isbn = isbnField.getText().trim();
        String title = titleField.getText().trim();

        if (isbn.isEmpty() || title.isEmpty()) {
            showError("Please enter both ISBN and Title.");
            return;
        }

        if (findBookByIsbn(isbn) != null) {
            showError("Duplicate ISBN: " + isbn + " already exists.");
            return;
        }

        Book newBook = new Book(isbn, title, true);
        books.add(newBook);
        saveToFile();

        tableData.setAll(books);
        clearFieldsAndSelection();
        messageLabel.setText("The book is added and books.txt has been updated.");
    }

    private void loadTestData() {
        String[][] testData = {
            {"0131450913", "HTML How to Program"},
            {"0131857576", "C++ How to Program"},
            {"0132222205", "Java How to Program"}
        };

        boolean hasDuplicate = false;
        int added = 0;

        for (String[] data : testData) {
            if (findBookByIsbn(data[0]) == null) {
                books.add(new Book(data[0], data[1], true));
                added++;
            } else {
                hasDuplicate = true;
            }
        }

        if (added > 0) {
            saveToFile();
            tableData.setAll(books);
        }

        if (hasDuplicate) {
            showError("Duplicate ISBN: One or more test books already exist and were skipped.");
        } else {
            messageLabel.setText("Test data loaded and books.txt has been updated.");
        }
        updateButtonStates();
    }

    private void edit() {
        if (books.isEmpty()) {
            showError("No book record is available for editing.");
            return;
        }

        String inputIsbn = isbnField.getText().trim();
        String inputTitle = titleField.getText().trim();

        // Edit target can be selected row or typed ISBN
        if (selectedBook != null) {
            editingBook = selectedBook;
        } else if (!inputIsbn.isEmpty()) {
            editingBook = findBookByIsbn(inputIsbn);
            if (editingBook == null) {
                showError("Book with ISBN " + inputIsbn + " cannot be found.");
                return;
            }
        } else {
            showError("Please enter or select an ISBN.");
            return;
        }

        isbnField.setText(editingBook.getISBN());
        titleField.setText(editingBook.getTitle());

        isEditMode = true;
        updateButtonStates();
        messageLabel.setText("Editing book. Change the information and click Save.");
    }

    private void save() {
        if (!isEditMode || editingBook == null) {
            showError("No book is selected for editing.");
            return;
        }

        String newIsbn = isbnField.getText().trim();
        String newTitle = titleField.getText().trim();

        if (newIsbn.isEmpty() || newTitle.isEmpty()) {
            showError("Please enter both ISBN and Title.");
            return;
        }

        // If ISBN changed, check duplicate on other books
        if (!newIsbn.equalsIgnoreCase(editingBook.getISBN())) {
            Book duplicate = findBookByIsbn(newIsbn);
            if (duplicate != null) {
                showError("Duplicate ISBN: " + newIsbn + " already exists.");
                return; // Remain in edit mode per Acceptance Test T8
            }
        }

        editingBook.setISBN(newIsbn);
        editingBook.setTitle(newTitle);

        saveToFile();
        bookTable.refresh();

        isEditMode = false;
        editingBook = null;
        clearFieldsAndSelection();
        messageLabel.setText("Book information saved and written to books.txt.");
    }

    private void delete() {
        if (books.isEmpty()) {
            showError("No book record is available for deletion.");
            return;
        }

        String isbn = isbnField.getText().trim();
        Book bookToDelete = null;

        if (selectedBook != null) {
            bookToDelete = selectedBook;
        } else if (!isbn.isEmpty()) {
            bookToDelete = findBookByIsbn(isbn);
            if (bookToDelete == null) {
                showError("Book with ISBN " + isbn + " cannot be found.");
                return;
            }
        } else {
            showError("No book record is available for deletion.");
            return;
        }

        books.remove(bookToDelete);
        saveToFile();

        tableData.setAll(books);
        clearFieldsAndSelection();
        messageLabel.setText("The book is deleted and books.txt has been updated.");
    }

    private void search() {
        String iq = isbnField.getText().trim().toLowerCase();
        String tq = titleField.getText().trim().toLowerCase();

        if (iq.isEmpty() && tq.isEmpty()) {
            showError("Please enter a partial ISBN and/or partial Title for searching.");
            return;
        }

        ArrayList<Book> searchResults = new ArrayList<>();
        for (Book book : books) {
            boolean match = (!iq.isEmpty() && book.getISBN().toLowerCase().contains(iq))
                         || (!tq.isEmpty() && book.getTitle().toLowerCase().contains(tq));
            if (match) {
                searchResults.add(book);
            }
        }

        tableData.setAll(searchResults);
        messageLabel.setText(searchResults.size() + " book record(s) found. Search does not change or delete book records.");
    }

    private void displayAll() {
        tableData.setAll(books);
        messageLabel.setText("Displaying all book records in their current order.");
    }

    private void borrow() {
        if (selectedBook == null) {
            showError("Please select a book first.");
            return;
        }

        if (!selectedBook.getAvailable()) {
            showError("The selected book is not available.");
            return;
        }

        selectedBook.setAvailable(false);
        saveToFile();
        bookTable.refresh();
        updateButtonStates();
        messageLabel.setText("The book is borrowed and books.txt has been updated.");
    }

    private void returnBk() {
        if (selectedBook == null) {
            showError("Please select a book first.");
            return;
        }

        if (selectedBook.getAvailable()) {
            showError("The selected book is already available.");
            return;
        }

        selectedBook.setAvailable(true);
        saveToFile();
        bookTable.refresh();
        updateButtonStates();
        messageLabel.setText("The book is returned and books.txt has been updated.");
    }

    private void clear() {
        isEditMode = false;
        editingBook = null;
        clearFieldsAndSelection();
        messageLabel.setText("Text fields cleared.");
    }

    // States and GUI

    private void updateButtonStates() {
        if (isEditMode) {
            // After clicking Edit: only Save and Exit are enabled
            btnAdd.setDisable(true);
            btnEdit.setDisable(true);
            btnSave.setDisable(false);
            btnDelete.setDisable(true);
            btnSearch.setDisable(true);
            btnDisplayAll.setDisable(true);
            btnLoadTestData.setDisable(true);
            btnBorrow.setDisable(true);
            btnReturn.setDisable(true);
            btnClear.setDisable(true);
            btnExit.setDisable(false);
        } else {
            // Normal mode
            btnAdd.setDisable(false);
            btnEdit.setDisable(false);
            btnSave.setDisable(true);
            btnDelete.setDisable(false);
            btnSearch.setDisable(false);
            btnDisplayAll.setDisable(false);
            btnLoadTestData.setDisable(false);
            btnClear.setDisable(false);
            btnExit.setDisable(false);

            if (selectedBook == null) {
                btnBorrow.setDisable(true);
                btnReturn.setDisable(true);
            } else {
                btnBorrow.setDisable(!selectedBook.getAvailable());
                btnReturn.setDisable(selectedBook.getAvailable());
            }
        }
    }

    private void clearFieldsAndSelection() {
        isbnField.clear();
        titleField.clear();
        bookTable.getSelectionModel().clearSelection();
        selectedBook = null;
        updateButtonStates();
    }

    private Book findBookByIsbn(String isbn) {
        for (Book b : books) {
            if (b.getISBN().equalsIgnoreCase(isbn)) {
                return b;
            }
        }
        return null;
    }

    // File storage handlers

    private void loadFromFile() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            books.clear();
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split(",", 3);
                if (parts.length == 3) {
                    boolean isAvail = Boolean.parseBoolean(parts[2].trim());
                    books.add(new Book(parts[0].trim(), parts[1].trim(), isAvail));
                }
            }
        } catch (IOException ex) {
            showError("Unable to load book records from books.txt.");
        }
    }

    private void saveToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Book b : books) {
                writer.write(b.getISBN() + "," + b.getTitle() + "," + b.getAvailable());
                writer.newLine();
            }
        } catch (IOException ex) {
            showError("Unable to save book records to books.txt.");
        }
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