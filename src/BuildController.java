import components.*;
import db.products.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.function.Consumer;

public class BuildController {

    public VBox gpuContainer;
    public VBox ramContainer;
    public VBox storageContainer;
    public VBox accessoryContainer;
    @FXML
    private Label cpuName, cpuPrice;
    @FXML private Label moboName, moboPrice;
    @FXML private Label gpuName, gpuPrice;
    @FXML private Label coolerName, coolerPrice;
    @FXML private Label caseName, casePrice;
    @FXML private Label psuName, psuPrice;

    @FXML private Label totalPriceLabel, wattageLabel, errorLabel;
    @FXML private Button saveButton;

    private CPU selectedCPU;
    private Motherboard selectedMobo;
    private Case selectedCase;
    private PSU selectedPSU;
    private Cooler selectedCooler;
    private List<RAM> ramKits;
    private List<GPU> gpus;
    private List <Storage> storageList;
    private List <Accessory> accessoryList;

    private CompatibilityChecker checker = new CompatibilityChecker();

    private GPUDAO gpuDAO = new GPUDAO();
    private CPUDAO cpuDAO = new CPUDAO();
    private MoboDAO moboDAO = new MoboDAO();
    private CaseDAO caseDAO = new CaseDAO();
    private AccessoryDAO accessoryDAO = new AccessoryDAO();
    private RAMDAO ramDAO = new RAMDAO();
    private CoolerDAO coolerDAO = new CoolerDAO();
    private PSUDAO psuDAO = new PSUDAO();
    private StorageDAO storageDAO = new StorageDAO();


    @FXML
    public void handleSelectCpu() {
        List<CPU> allCpus = cpuDAO.getAllCpus(); // Get data from DB

        openSelectionPopup("Select CPU", allCpus, (selectedProduct) -> {
            CPU selectedCpu = (CPU) selectedProduct;

            this.selectedCPU = selectedCpu;
            cpuName.setText(selectedCpu.getName());
            updateSummary();
        });
    }

    @FXML
    public void handleSelectMobo() {
        List<Motherboard> allMobos = moboDAO.getAllMobos();

        openSelectionPopup("Select Mobo", allMobos, (selectedProduct) -> {
            Motherboard selectedMobo = (Motherboard) selectedProduct;
            this.selectedMobo = selectedMobo;
            moboName.setText(selectedMobo.getName());
            updateSummary();
        });
    }

    @FXML
    public void handleSelectCooler() {
        List<Cooler> allCoolers = coolerDAO.getAllCoolers();

        openSelectionPopup("Select Cooler", allCoolers, (selectedProduct) -> {
            Cooler selectedCooler = (Cooler) selectedProduct;

            this.selectedCooler = selectedCooler;
            coolerName.setText(selectedCooler.getName());
            updateSummary();
        });
    }

    @FXML
    public void handleSelectPsu() {
        List<PSU> allPsus = psuDAO.getAllPsus();

        openSelectionPopup("Select PSU", allPsus, (selectedProduct) -> {
            PSU selectedPsu = (PSU) selectedProduct;

            this.selectedPSU = selectedPsu;
            cpuName.setText(selectedPsu.getName());
            updateSummary();
        });
    }

    @FXML
    public void handleSelectCase() {
        List<Case> allCases = caseDAO.getAllCases(); // Get data from DB

        openSelectionPopup("Select Case", allCases, (selectedProduct) -> {
            Case selectedCase = (Case) selectedProduct;

            this.selectedCase = selectedCase;
            caseName.setText(selectedCase.getName());
            updateSummary();
        });
    }

    @FXML
    public void handleAddGpu() throws SQLException {
        openSelectionPopup("Select GPU", gpuDAO.getAllGpus(), (product) -> {
            GPU newGpu = (GPU) product;

            gpus.add(newGpu);

            HBox row = createProductRow(newGpu, () -> gpus.remove(newGpu));
            gpuContainer.getChildren().add(row);

            updateSummary();
        });
    }

    @FXML
    public void handleAddRam() {
        openSelectionPopup("Select RAM", ramDAO.getAllRam(), (product) -> {
            RAM newRam = (RAM) product;
            ramKits.add(newRam);

            HBox row = createProductRow(newRam, () -> ramKits.remove(newRam));
            ramContainer.getChildren().add(row);

            updateSummary();
        });
    }

    @FXML
    public void handleAddStorage() {
        openSelectionPopup("Select Storage", storageDAO.getAllStorages(), (product) -> {
            Storage newStorage = (Storage) product;
            storageList.add(newStorage);

            HBox row = createProductRow(newStorage, () -> storageList.remove(newStorage));
            storageContainer.getChildren().add(row);

            updateSummary();
        });
    }

    @FXML
    public void handleAddAccessory() {
        openSelectionPopup("Select Accessory", accessoryDAO.getAllaccs(), (product) -> {
            Accessory newAccessory = (Accessory) product;
            accessoryList.add(newAccessory);

            HBox row = createProductRow(newAccessory, () -> accessoryList.remove(newAccessory));
            accessoryContainer.getChildren().add(row);

            updateSummary();
        });
    }

    private HBox createProductRow(Product product, Runnable onRemove) {
        HBox row = new HBox(10);
        row.setStyle("-fx-padding: 5; -fx-background-color: #f0f0f0; -fx-background-radius: 5;");
        row.setAlignment(Pos.CENTER_LEFT);

        Label nameLbl = new Label(product.getName());
        nameLbl.setStyle("-fx-font-weight: bold;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label priceLbl = new Label("$" + product.getPrice());

        Button removeBtn = new Button("✖");
        removeBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: red; -fx-font-weight: bold;");

        removeBtn.setOnAction(e -> {
            onRemove.run();
            ((VBox) row.getParent()).getChildren().remove(row);
            updateSummary();
        });

        row.getChildren().addAll(nameLbl, spacer, priceLbl, removeBtn);
        return row;
    }


    private void updateSummary() {
        double total = 0;
        int watts = 0;

        if (selectedCPU != null) {
            total += selectedCPU.getPrice();
            watts += selectedCPU.getTdp();
        }
        if (selectedMobo != null) {
            total += selectedMobo.getPrice();
        }

        totalPriceLabel.setText(String.format("$%.2f", total));
        wattageLabel.setText(watts + "W");

        List<String> errors = checker.checkBuild(selectedCPU, selectedMobo, gpus, selectedPSU, ramKits, selectedCase, storageList, selectedCooler);

        if (errors.isEmpty()) {
            errorLabel.setText("No issues found.");
            errorLabel.setStyle("-fx-text-fill: green;");
            saveButton.setDisable(false);
        } else {
            errorLabel.setText(String.join("\n", errors));
            errorLabel.setStyle("-fx-text-fill: red;");
        }
    }
    private void openSelectionPopup(String title, List<? extends Product> items, Consumer<Product> callback) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("selection-view.fxml"));
            Scene scene = new Scene(loader.load());

            SelectionController controller = loader.getController();

            controller.initData(items, callback);

            Stage popupStage = new Stage();
            popupStage.setTitle(title);
            popupStage.setScene(scene);

            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}