package burgerkonfigurator.controller;

import burgerkonfigurator.model.Burger;
import burgerkonfigurator.repository.RepositoryException;
import burgerkonfigurator.service.BurgerService;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class OverviewController {

    private final BurgerService service;
    private Consumer<Burger> onEdit = b -> { };

    @FXML private TableView<Burger> burgerTable;
    @FXML private TableColumn<Burger, String> nameColumn;
    @FXML private TableColumn<Burger, String> bunColumn;
    @FXML private TableColumn<Burger, String> pattyColumn;
    @FXML private TableColumn<Burger, String> cheeseColumn;
    @FXML private TableColumn<Burger, String> sauceColumn;
    @FXML private TableColumn<Burger, String> toppingsColumn;
    @FXML private TableColumn<Burger, String> priceColumn;
    @FXML private Button editButton;
    @FXML private Button deleteButton;

    public OverviewController(BurgerService service) {
        this.service = service;
    }

    @FXML
    private void initialize() {
        nameColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getName()));
        bunColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getBun().toString()));
        pattyColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getPatty().toString()));
        cheeseColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getCheese().toString()));
        sauceColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getSauce().toString()));
        toppingsColumn.setCellValueFactory(c -> new SimpleStringProperty(toppings(c.getValue())));
        priceColumn.setCellValueFactory(c -> new SimpleStringProperty(String.format("%.2f €", c.getValue().getPrice())));

        editButton.disableProperty().bind(burgerTable.getSelectionModel().selectedItemProperty().isNull());
        deleteButton.disableProperty().bind(burgerTable.getSelectionModel().selectedItemProperty().isNull());
        refresh();
    }

    public void setOnEdit(Consumer<Burger> onEdit) {
        this.onEdit = onEdit;
    }

    public void refresh() {
        burgerTable.getItems().setAll(service.getAllBurgers());
    }

    @FXML
    private void onEdit() {
        Burger selected = burgerTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            onEdit.accept(selected);
        }
    }

    @FXML
    private void onDelete() {
        Burger selected = burgerTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            return;
        }
        try {
            service.deleteBurger(selected);
            refresh();
        } catch (RepositoryException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
            alert.setHeaderText(null);
            alert.showAndWait();
        }
    }

    private String toppings(Burger burger) {
        List<String> parts = new ArrayList<>();
        burger.getVegetables().forEach(v -> parts.add(v.toString()));
        burger.getExtras().forEach(e -> parts.add(e.toString()));
        return String.join(", ", parts);
    }
}
