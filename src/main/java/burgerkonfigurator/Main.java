package burgerkonfigurator;

import burgerkonfigurator.controller.ConfiguratorController;
import burgerkonfigurator.controller.OverviewController;
import burgerkonfigurator.repository.JsonBurgerRepository;
import burgerkonfigurator.service.BurgerService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Path;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        BurgerService service = new BurgerService(new JsonBurgerRepository(Path.of("burgers.json")));
        ConfiguratorController configurator = new ConfiguratorController(service);
        OverviewController overview = new OverviewController(service);

        Parent configuratorRoot = load("configurator-view.fxml", configurator);
        Parent overviewRoot = load("overview-view.fxml", overview);

        Tab configuratorTab = new Tab("Konfigurator", configuratorRoot);
        Tab overviewTab = new Tab("Test 2", overviewRoot);
        configuratorTab.setClosable(false);
        overviewTab.setClosable(false);
        TabPane tabs = new TabPane(configuratorTab, overviewTab);

        configurator.setOnCreated(burger -> {
            overview.refresh();
            tabs.getSelectionModel().select(overviewTab);
        });
        overview.setOnEdit(burger -> {
            configurator.startEditing(burger);
            tabs.getSelectionModel().select(configuratorTab);
        });

        stage.setTitle("Burger-Konfigurator");
        stage.setScene(new Scene(tabs, 760, 640));
        stage.show();
    }

    private Parent load(String fxml, Object controller) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource(fxml));
        loader.setControllerFactory(type -> controller);
        return loader.load();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
