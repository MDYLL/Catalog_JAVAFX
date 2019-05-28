package sample;

import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.TileBuilder;
import eu.hansolo.tilesfx.skins.BarChartItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.json.*;


import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
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

    @FXML
    private Pane pBar;

    private Tile barTile;
    private BarChartItem bc1;
    private BarChartItem bc2;
    private BarChartItem bc3;
    private BarChartItem bc4;

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
        showTile();
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
        showTile();
    }

    public void showTile() {
        if (bookData.size() > 0) {
            ArrayList<Float> century = new ArrayList<>(Arrays.asList(0f, 0f, 0f, 0f));
            bookData.forEach(book -> {
                if (book.getYear() < 1700) {
                    century.set(0, century.get(0) + 1);
                } else if (book.getYear() < 1800) {
                    century.set(1, century.get(1) + 1);
                } else if (book.getYear() < 1900) {
                    century.set(2, century.get(2) + 1);
                } else {
                    century.set(3, century.get(3) + 1);
                }
            });
            bc1 = new BarChartItem("till 17 century", 100 * century.get(0) / bookData.size(), Tile.BLUE);
            bc2 = new BarChartItem("17-18 centuries", 100 * century.get(1) / bookData.size(), Tile.RED);
            bc3 = new BarChartItem("18-19 centuries", 100 * century.get(2) / bookData.size(), Tile.GREEN);
            bc4 = new BarChartItem("after 19 century", 100 * century.get(3) / bookData.size(), Tile.ORANGE);

            barTile = TileBuilder.create()
                    .skinType(Tile.SkinType.BAR_CHART)
                    .prefSize(300, 160)
                    .title("BarChart Tile")
                    .text("Whatever text")
                    .barChartItems(bc1, bc2, bc3, bc4)
                    .decimals(0)
                    .build();
            pBar.getChildren().add(barTile);
        }

    }

    @FXML
    public void filterBooksByDate() {
        filteredData = bookData.filtered(book -> book.getYear() > Integer.parseInt(fromYear.getText()));
        tvTable.setItems(filteredData);
    }

}
