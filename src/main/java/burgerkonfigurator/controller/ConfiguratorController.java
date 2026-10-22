package burgerkonfigurator.controller;

import burgerkonfigurator.model.Bun;
import burgerkonfigurator.model.Burger;
import burgerkonfigurator.model.Cheese;
import burgerkonfigurator.model.Extra;
import burgerkonfigurator.model.Patty;
import burgerkonfigurator.model.Sauce;
import burgerkonfigurator.model.Vegetable;
import burgerkonfigurator.repository.RepositoryException;
import burgerkonfigurator.service.BurgerService;
import burgerkonfigurator.service.ValidationException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ConfiguratorController {

    private final BurgerService service;
    private Consumer<Burger> onCreated = b -> { };
    private Burger editSource;

    private final List<CheckBox> vegetableChecks = new ArrayList<>();
    private final List<CheckBox> extraChecks = new ArrayList<>();

    @FXML private Label titleLabel;
    @FXML private TextField nameField;
    @FXML private ComboBox<Bun> bunCombo;
    @FXML private ComboBox<Patty> pattyCombo;
    @FXML private ComboBox<Cheese> cheeseCombo;
    @FXML private ComboBox<Sauce> sauceCombo;
    @FXML private VBox vegetableBox;
    @FXML private VBox extraBox;
    @FXML private Label priceLabel;
    @FXML private Button createButton;

    public ConfiguratorController(BurgerService service) {
        this.service = service;
    }

    @FXML
    private void initialize() {
        bunCombo.getItems().setAll(Bun.values());
        pattyCombo.getItems().setAll(Patty.values());
        cheeseCombo.getItems().setAll(Cheese.values());
        sauceCombo.getItems().setAll(Sauce.values());
        for (Vegetable v : Vegetable.values()) {
            addCheckBox(vegetableBox, vegetableChecks, v);
        }
        for (Extra e : Extra.values()) {
            addCheckBox(extraBox, extraChecks, e);
        }
        bunCombo.valueProperty().addListener((o, a, b) -> updatePrice());
        pattyCombo.valueProperty().addListener((o, a, b) -> updatePrice());
        cheeseCombo.valueProperty().addListener((o, a, b) -> updatePrice());
        sauceCombo.valueProperty().addListener((o, a, b) -> updatePrice());
        onReset();
    }

    public void setOnCreated(Consumer<Burger> onCreated) {
        this.onCreated = onCreated;
    }

    /** Pre-fills the form from an existing burger; saving creates a new burger. */
    public void startEditing(Burger burger) {
        editSource = burger;
        titleLabel.setText("Burger bearbeiten (erzeugt einen neuen Burger)");
        createButton.setText("Als neuen Burger speichern");
        nameField.setText(burger.getName());
        bunCombo.setValue(burger.getBun());
        pattyCombo.setValue(burger.getPatty());
        cheeseCombo.setValue(burger.getCheese());
        sauceCombo.setValue(burger.getSauce());
        vegetableChecks.forEach(c -> c.setSelected(burger.getVegetables().contains(c.getUserData())));
        extraChecks.forEach(c -> c.setSelected(burger.getExtras().contains(c.getUserData())));
        updatePrice();
    }

    @FXML
    private void onCreate() {
        try {
            Burger burger = editSource == null
                    ? service.createBurger(nameField.getText(), bunCombo.getValue(), pattyCombo.getValue(),
                            cheeseCombo.getValue(), sauceCombo.getValue(), selectedVegetables(), selectedExtras())
                    : service.createVariant(editSource, nameField.getText(), bunCombo.getValue(),
                            pattyCombo.getValue(), cheeseCombo.getValue(), sauceCombo.getValue(),
                            selectedVegetables(), selectedExtras());
            onReset();
            onCreated.accept(burger);
        } catch (ValidationException e) {
            showAlert(Alert.AlertType.WARNING, "Ungültige Eingabe", e.getMessage());
        } catch (RepositoryException e) {
            showAlert(Alert.AlertType.ERROR, "Speichern fehlgeschlagen", e.getMessage());
        }
    }

    @FXML
    private void onReset() {
        editSource = null;
        titleLabel.setText("Neuen Burger zusammenstellen");
        createButton.setText("Burger erstellen");
        nameField.clear();
        bunCombo.setValue(null);
        pattyCombo.setValue(null);
        cheeseCombo.setValue(Cheese.NONE);
        sauceCombo.setValue(Sauce.NONE);
        vegetableChecks.forEach(c -> c.setSelected(false));
        extraChecks.forEach(c -> c.setSelected(false));
        updatePrice();
    }

    private <T> void addCheckBox(VBox box, List<CheckBox> checks, T value) {
        CheckBox check = new CheckBox(value.toString());
        check.setUserData(value);
        check.selectedProperty().addListener((o, a, b) -> updatePrice());
        checks.add(check);
        box.getChildren().add(check);
    }

    private List<Vegetable> selectedVegetables() {
        return vegetableChecks.stream().filter(CheckBox::isSelected)
                .map(c -> (Vegetable) c.getUserData()).toList();
    }

    private List<Extra> selectedExtras() {
        return extraChecks.stream().filter(CheckBox::isSelected)
                .map(c -> (Extra) c.getUserData()).toList();
    }

    private void updatePrice() {
        double price = service.previewPrice(bunCombo.getValue(), pattyCombo.getValue(), cheeseCombo.getValue(),
                sauceCombo.getValue(), selectedVegetables(), selectedExtras());
        priceLabel.setText(String.format("Preis: %.2f €", price));
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type, message);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
