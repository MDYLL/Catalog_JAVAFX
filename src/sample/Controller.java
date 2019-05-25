package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.json.*;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

public class Controller {

    @FXML
    private TableView<Book> tvTable;

    @FXML
    private TableColumn<Book, Integer> tcId;

    @FXML
    private TableColumn<Book, String> tcTitle;

    @FXML
    private TableColumn<Book, String> tcAuthor;

    @FXML
    private TableColumn<Book, Integer> tcYear;

    @FXML
    private TextField tfId;

    @FXML
    private TextField tfTitle;

    @FXML
    private TextField tfAuthor;

    @FXML
    private TextField tfYear;

    @FXML
    private TextField fromYear;

    private ObservableList<Book> bookData = FXCollections.observableArrayList();
    private ObservableList<Book> filteredData;

    @FXML
    private void initialize() {
        tcId.setCellValueFactory(new PropertyValueFactory<Book, Integer>("id"));
        tcAuthor.setCellValueFactory(new PropertyValueFactory<Book, String>("author"));
        tcTitle.setCellValueFactory(new PropertyValueFactory<Book, String>("title"));
        tcYear.setCellValueFactory(new PropertyValueFactory<Book, Integer>("year"));
        tvTable.setItems(bookData);
    }

    @FXML
    public void addBook() {
        bookData.add(new Book(Integer.parseInt(tfId.getText()),
                tfTitle.getText(),
                tfAuthor.getText(),
                Integer.parseInt(tfYear.getText())));
    }

    @FXML
    public void setListFromFile() throws FileNotFoundException, JSONException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select JSON file");
        fileChooser.setInitialDirectory(new File("D:\\"));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JSON", "*.json"));
        File selectedFile = fileChooser.showOpenDialog(new Stage());
        Scanner scanner = new Scanner(selectedFile);
        String text = scanner.useDelimiter("\\A").next();
        scanner.close();
        JSONObject obj = new JSONObject(text);
        JSONArray items = (JSONArray) obj.get("books");
        for (int i = 0; i < items.length(); i++) {
            JSONObject obj1 = items.getJSONObject(i);
            int id = i;
            String title = obj1.getString("title");
            String author = obj1.getString("author");
            int year = obj1.getInt("year");
            bookData.add(new Book(id, title, author, year));
        }
        tvTable.setItems(bookData);
    }

    @FXML
    public void filterBooksByDate() {
        filteredData=bookData.filtered(book -> book.getYear()>Integer.parseInt(fromYear.getText()));
        tvTable.setItems(filteredData);
    }

}
