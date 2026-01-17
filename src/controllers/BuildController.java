package controllers;

import components.*;
import db.*;
import db.products.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
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
    @FXML private Label coolerName, coolerPrice;
    @FXML private Label caseName, casePrice;
    @FXML private Label psuName, psuPrice;

    @FXML private Label totalPriceLabel, wattageLabel, errorLabel;
    @FXML private Button saveButton;

    private CompatibilityChecker checker = new CompatibilityChecker();

    private CPUDAO cpuDAO = new CPUDAO();
    private MoboDAO moboDAO = new MoboDAO();
    private CaseDAO caseDAO = new CaseDAO();
    private CoolerDAO coolerDAO = new CoolerDAO();
    private PSUDAO psuDAO = new PSUDAO();

    @FXML
    public void handleSelectCpu() {
        List<CPU> allCpus = cpuDAO.getAllCpus();

        openSelectionPopup("Select CPU", allCpus, (selectedProduct) -> {
            CPU selectedCpu = (CPU) selectedProduct;
            currentBuild.setCpu(selectedCpu);
            cpuName.setText(selectedCpu.getName());
            cpuPrice.setText("$" + selectedCpu.getPrice());
            updateSummary();
        });
    }

    @FXML
    public void handleSelectMobo() {
        List<Motherboard> allMobos = moboDAO.getAllMobos();

        openSelectionPopup("Select Mobo", allMobos, (selectedProduct) -> {
            Motherboard selectedMobo = (Motherboard) selectedProduct;
            currentBuild.setMobo(selectedMobo);
            moboName.setText(selectedMobo.getName());
            moboPrice.setText("$" + selectedMobo.getPrice());
            updateSummary();
        });
    }

    @FXML
    public void handleSelectCooler() {
        List<Cooler> allCoolers = coolerDAO.getAllCoolers();

        openSelectionPopup("Select Cooler", allCoolers, (selectedProduct) -> {
            Cooler selectedCooler = (Cooler) selectedProduct;
            currentBuild.setCooler(selectedCooler);
            coolerName.setText(selectedCooler.getName());
            coolerPrice.setText("$" + selectedCooler.getPrice());
            updateSummary();
        });
    }

    @FXML
    public void handleSelectPsu() {
        List<PSU> allPsus = psuDAO.getAllPsus();

        openSelectionPopup("Select PSU", allPsus, (selectedProduct) -> {
            PSU selectedPsu = (PSU) selectedProduct;
            currentBuild.setPsu(selectedPsu);
            psuName.setText(selectedPsu.getName());
            psuPrice.setText("$" + selectedPsu.getPrice());
            updateSummary();
        });
    }

    @FXML
    public void handleSelectCase() {
        List<Case> allCases = caseDAO.getAllCases();

        openSelectionPopup("Select Case", allCases, (selectedProduct) -> {
            Case selectedCase = (Case) selectedProduct;
            currentBuild.setPcCase(selectedCase);
            caseName.setText(selectedCase.getName());
            casePrice.setText("$" + selectedCase.getPrice());
            updateSummary();
        });
    }

    private HBox createProductRow(Product product, Runnable onRemoveDataModel) {
        HBox row = new HBox(10);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(5, 10, 5, 10));
        row.setStyle("-fx-background-color: #f0f0f0; -fx-background-radius: 5;");

        Label nameLabel = new Label(product.getName());
        nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        nameLabel.setPrefWidth(250);

        Label priceLabel = new Label(String.format("$%.2f", product.getPrice()));
        priceLabel.setStyle("-fx-text-fill: green;");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button removeBtn = new Button("X");
        removeBtn.setOnAction(e -> {
            onRemoveDataModel.run();

            if (row.getParent() != null) {
                ((Pane) row.getParent()).getChildren().remove(row);
            }
        });

        row.getChildren().addAll(nameLabel, spacer, priceLabel, removeBtn);
        return row;
    }

    @FXML
    public void handleAddGpu() {
        List<GPU> allGpus = new GPUDAO().getAllGpus();

        openSelectionPopup("Select a Graphics Card", allGpus, (selectedProduct) -> {
            GPU pickedGpu = (GPU) selectedProduct;
            addGpuToUI(pickedGpu);
            updateSummary();
        });
    }
    @FXML
    public void handleAddRam() {
        List<RAM> allRams = new RAMDAO().getAllRam();
        openSelectionPopup("Select a RAM Kit", allRams, (selectedProduct) -> {
            RAM pickedRam = (RAM) selectedProduct;
            addRamToUI(pickedRam);
            updateSummary();
        });
    }
    @FXML
    public void handleAddStorage() {
        List<Storage> allStorages = new StorageDAO().getAllStorages();
        openSelectionPopup("Select a Storage", allStorages, (selectedProduct) -> {
            Storage pickedStorage = (Storage) selectedProduct;
            addStorageToUI(pickedStorage);
            updateSummary();
        });
    }
    @FXML
    public void handleAddAccessory() {
        List<Accessory> allAccessories = new AccessoryDAO().getAllaccs();
        openSelectionPopup("Select an Accessory", allAccessories, (selectedProduct) -> {
            Accessory pickedAccessory = (Accessory) selectedProduct;
            addAccessoryToUI(pickedAccessory);
            updateSummary();
        });
    }


    public void addGpuToUI(GPU gpu) {
        if (!currentBuild.getGpus().contains(gpu)) {
            currentBuild.getGpus().add(gpu);
        }
        HBox row = createProductRow(gpu, () -> {
            currentBuild.getGpus().remove(gpu);
            updateSummary();
        });
        gpuContainer.getChildren().add(row);
    }
    public void addRamToUI(RAM ram) {
        if (!currentBuild.getRams().contains(ram)) {
            currentBuild.getRams().add(ram);
        }
        HBox row = createProductRow(ram, () -> {
            currentBuild.getRams().remove(ram);
            updateSummary();
        });
        ramContainer.getChildren().add(row);
    }
    public void addStorageToUI(Storage storage) {
        if (!currentBuild.getStorages().contains(storage)) {
            currentBuild.getStorages().add(storage);
        }
        HBox row = createProductRow(storage, () -> {
            currentBuild.getStorages().remove(storage);
            updateSummary();
        });
        storageContainer.getChildren().add(row);
    }
    public void addAccessoryToUI(Accessory accessory){
        if (!currentBuild.getAccessories().contains(accessory)) {
            currentBuild.getAccessories().add(accessory);
        }
        HBox row = createProductRow(accessory, () -> {
            currentBuild.getAccessories().remove(accessory);
            updateSummary();
        });
        accessoryContainer.getChildren().add(row);
    }

    public void loadExistingBuild(Build build) {
        this.currentBuild = build;

        if (build.getCpu() != null) {
            cpuName.setText(build.getCpu().getName());
            cpuPrice.setText("$" + build.getCpu().getPrice());
        }

        if (build.getMobo() != null) {
            moboName.setText(build.getMobo().getName());
            moboPrice.setText("$" + build.getMobo().getPrice());
        }

        if(build.getPcCase() != null){
            caseName.setText(build.getPcCase().getName());
            casePrice.setText("$" + build.getPcCase().getPrice());

        }

        if(build.getPsu() != null){
            psuName.setText(build.getPsu().getName());
            psuPrice.setText("$" + build.getPsu().getPrice());
        }

        if(build.getCooler() != null){
            coolerName.setText(build.getCooler().getName());
            coolerPrice.setText("$" + build.getCooler().getPrice());
        }

        gpuContainer.getChildren().clear();
        ramContainer.getChildren().clear();
        storageContainer.getChildren().clear();

        for (GPU gpu : build.getGpus()) {
            addGpuToUI(gpu);
            updateSummary();
        }

        for (RAM ram : build.getRams()) {
            addRamToUI(ram);
            updateSummary();
        }

        for (Storage storage : build.getStorages()) {
            addStorageToUI(storage);
            updateSummary();
        }

        for (Accessory accessory : build.getAccessories()){
            addAccessoryToUI(accessory);
            updateSummary();
        }

        updateSummary();
    }

    private void updateSummary() {
        double total = 0;
        int watts = 0;

        total = currentBuild.calculateTotal();
        watts = currentBuild.calculateWattage();

        totalPriceLabel.setText(String.format("$%.2f", total));
        wattageLabel.setText((watts + 150) + "W");

        List<String> errors = checker.checkBuild(currentBuild.getCpu(),
                currentBuild.getMobo(),
                currentBuild.getGpus(),
                currentBuild.getPsu(),
                currentBuild.getRams(),
                currentBuild.getPcCase(),
                currentBuild.getStorages(),
                currentBuild.getCooler());

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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmls/selection-view.fxml"));
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
    private Build currentBuild = new Build();

    @FXML
    public void handleSave() {
        User currentUser = UserSession.getInstance().getCurrentUser();

        TextInputDialog dialog = new TextInputDialog("My Build");
        dialog.setHeaderText("Name your build:");
        dialog.showAndWait().ifPresent(name -> {
            currentBuild.setBuildName(name);
            currentBuild.setUser_id(currentUser.getId());
            boolean success = BuildDAO.saveBuild(currentBuild);
        });
    }

    public void handleExit(javafx.event.ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmls/user-builds-view.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(loader.load()));
        stage.show();
    }
}