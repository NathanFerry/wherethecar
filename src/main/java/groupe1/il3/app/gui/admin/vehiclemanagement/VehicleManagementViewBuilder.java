package groupe1.il3.app.gui.admin.vehiclemanagement;

import groupe1.il3.app.domain.vehicle.Energy;
import groupe1.il3.app.domain.vehicle.Status;
import groupe1.il3.app.domain.vehicle.Vehicle;
import groupe1.il3.app.gui.style.StyleApplier;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

public class VehicleManagementViewBuilder implements Builder<Region> {

    private final VehicleManagementModel model;
    private final Runnable loadVehiclesAction;
    private final Consumer<Vehicle> addVehicleAction;
    private final Consumer<Vehicle> editVehicleAction;
    private final Consumer<UUID> deleteVehicleAction;

    public VehicleManagementViewBuilder(
        VehicleManagementModel model,
        Runnable loadVehiclesAction,
        Consumer<Vehicle> addVehicleAction,
        Consumer<Vehicle> editVehicleAction,
        Consumer<UUID> deleteVehicleAction
    ) {
        this.model = model;
        this.loadVehiclesAction = loadVehiclesAction;
        this.addVehicleAction = addVehicleAction;
        this.editVehicleAction = editVehicleAction;
        this.deleteVehicleAction = deleteVehicleAction;
    }

    @Override
    public Region build() {
        BorderPane pane = new BorderPane();
        pane.setPadding(new Insets(10));

        VBox listPane = new VBox(10);
        listPane.setPrefWidth(350);

        Label listTitle = new Label("Liste des Véhicules");
        listTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        ListView<Vehicle> vehicleListView = new ListView<>();
        vehicleListView.setItems(model.vehiclesProperty());
        vehicleListView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Vehicle vehicle, boolean empty) {
                super.updateItem(vehicle, empty);
                if (empty || vehicle == null) {
                    setText(null);
                } else {
                    setText(vehicle.getManufacturer() + " " + vehicle.getModel() +
                           " (" + vehicle.getLicencePlate() + ")");
                }
            }
        });

        vehicleListView.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldVal, newVal) -> model.setSelectedVehicle(newVal)
        );

        VBox.setVgrow(vehicleListView, Priority.ALWAYS);

        VBox buttonBox = new VBox(5);
        buttonBox.getStyleClass().add("admin-button-box");

        Button addBtn = new Button("Ajouter");
        addBtn.getStyleClass().add("admin-add-button");
        addBtn.setOnAction(e -> showAddVehicleDialog());
        addBtn.setMaxWidth(Double.MAX_VALUE);

        Button editBtn = new Button("Modifier");
        editBtn.getStyleClass().add("admin-edit-button");
        editBtn.setOnAction(e -> showEditVehicleDialog());
        editBtn.disableProperty().bind(model.selectedVehicleProperty().isNull());
        editBtn.setMaxWidth(Double.MAX_VALUE);

        Button deleteBtn = new Button("Supprimer");
        deleteBtn.getStyleClass().add("admin-delete-button");
        deleteBtn.setOnAction(e -> confirmDeleteVehicle());
        deleteBtn.disableProperty().bind(model.selectedVehicleProperty().isNull());
        deleteBtn.setMaxWidth(Double.MAX_VALUE);

        Button refreshBtn = new Button("Actualiser");
        refreshBtn.getStyleClass().add("admin-refresh-button");
        refreshBtn.setOnAction(e -> loadVehiclesAction.run());
        refreshBtn.setMaxWidth(Double.MAX_VALUE);

        buttonBox.getChildren().addAll(addBtn, editBtn, deleteBtn, refreshBtn);

        listPane.getChildren().addAll(listTitle, vehicleListView, buttonBox);

        pane.setLeft(listPane);

        VBox detailsPane = createVehicleDetailsPane();
        pane.setCenter(detailsPane);

        return pane;
    }

    private VBox createVehicleDetailsPane() {
        VBox detailsPane = new VBox(10);
        detailsPane.setPadding(new Insets(20));
        detailsPane.setAlignment(Pos.TOP_LEFT);

        Label noSelectionLabel = new Label("Sélectionnez un véhicule pour voir ses détails");

        VBox detailsBox = new VBox(15);
        Label titleLabel = new Label();
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        GridPane detailsGrid = new GridPane();
        detailsGrid.setHgap(15);
        detailsGrid.setVgap(10);

        model.selectedVehicleProperty().addListener((obs, oldVal, newVal) -> {
            detailsGrid.getChildren().clear();
            if (newVal != null) {
                titleLabel.setText(newVal.getManufacturer() + " " + newVal.getModel());

                addDetailRow(detailsGrid, 0, "UUID:", newVal.getUuid().toString());
                addDetailRow(detailsGrid, 1, "Plaque:", newVal.getLicencePlate());
                addDetailRow(detailsGrid, 2, "Constructeur:", newVal.getManufacturer());
                addDetailRow(detailsGrid, 3, "Modèle:", newVal.getModel());
                addDetailRow(detailsGrid, 4, "Énergie:", formatEnergy(newVal.getEnergy()));
                addDetailRow(detailsGrid, 5, "Puissance:", newVal.getPower() + " CV");
                addDetailRow(detailsGrid, 6, "Sièges:", String.valueOf(newVal.getSeats()));
                addDetailRow(detailsGrid, 7, "Capacité:", newVal.getCapacity() + " L");
                addDetailRow(detailsGrid, 8, "Poids utilitaire:", newVal.getUtilityWeight() + " kg");
                addDetailRow(detailsGrid, 9, "Couleur:", newVal.getColor());
                addDetailRow(detailsGrid, 10, "Kilométrage:", newVal.getKilometers() + " km");
                addDetailRow(detailsGrid, 11, "Date d'acquisition:",
                    newVal.getAcquisitionDate() != null ?
                    newVal.getAcquisitionDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "N/A");
                addDetailRow(detailsGrid, 12, "Statut:", formatStatus(newVal.getStatus()));
            }
        });

        detailsBox.getChildren().addAll(titleLabel, new Separator(), detailsGrid);
        detailsBox.visibleProperty().bind(model.selectedVehicleProperty().isNotNull());

        noSelectionLabel.visibleProperty().bind(model.selectedVehicleProperty().isNull());

        detailsPane.getChildren().addAll(noSelectionLabel, detailsBox);

        return detailsPane;
    }

    private void addDetailRow(GridPane grid, int row, String label, String value) {
        Label labelNode = new Label(label);
        labelNode.getStyleClass().add("admin-detail-label");
        Label valueNode = new Label(value);
        valueNode.getStyleClass().add("admin-detail-value");

        grid.add(labelNode, 0, row);
        grid.add(valueNode, 1, row);
    }

    private String formatEnergy(Energy energy) {
        if (energy == null) return "N/A";
        return switch (energy) {
            case GASOLINE -> "Essence";
            case DIESEL -> "Diesel";
            case ELECTRIC -> "Électrique";
            case HYBRID -> "Hybride";
            case NATURAL_GAS -> "Gaz naturel";
            case NONE -> "Aucun";
            default -> "Non spécifié";
        };
    }

    private String formatStatus(Status status) {
        if (status == null) return "Inconnu";
        return switch (status) {
            case AVAILABLE -> "Disponible";
            case RESERVED -> "Réservé";
            case MAINTENANCE -> "En maintenance";
        };
    }

    private void showAddVehicleDialog() {
        Dialog<Vehicle> dialog = new Dialog<>();
        dialog.setTitle("Ajouter un véhicule");
        dialog.setHeaderText("Saisir les informations du véhicule");

        StyleApplier.applyStylesheets(dialog);

        ButtonType addButtonType = new ButtonType("Ajouter", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        GridPane grid = createVehicleFormGrid(null);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                return extractVehicleFromForm(grid, UUID.randomUUID());
            }
            return null;
        });

        Optional<Vehicle> result = dialog.showAndWait();
        result.ifPresent(addVehicleAction);
    }

    private void showEditVehicleDialog() {
        Vehicle selected = model.getSelectedVehicle();
        if (selected == null) return;

        Dialog<Vehicle> dialog = new Dialog<>();
        dialog.setTitle("Modifier un véhicule");
        dialog.setHeaderText("Modifier les informations du véhicule");

        StyleApplier.applyStylesheets(dialog);

        ButtonType saveButtonType = new ButtonType("Enregistrer", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        GridPane grid = createVehicleFormGrid(selected);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                return extractVehicleFromForm(grid, selected.getUuid());
            }
            return null;
        });

        Optional<Vehicle> result = dialog.showAndWait();
        result.ifPresent(editVehicleAction);
    }

    private GridPane createVehicleFormGrid(Vehicle vehicle) {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        int row = 0;

        TextField licensePlateField = new TextField(vehicle != null ? vehicle.getLicencePlate() : "");
        grid.add(new Label("Plaque d'immatriculation:"), 0, row);
        grid.add(licensePlateField, 1, row++);
        licensePlateField.setUserData("licensePlate");

        TextField manufacturerField = new TextField(vehicle != null ? vehicle.getManufacturer() : "");
        grid.add(new Label("Constructeur:"), 0, row);
        grid.add(manufacturerField, 1, row++);
        manufacturerField.setUserData("manufacturer");

        TextField modelField = new TextField(vehicle != null ? vehicle.getModel() : "");
        grid.add(new Label("Modèle:"), 0, row);
        grid.add(modelField, 1, row++);
        modelField.setUserData("model");

        ComboBox<Energy> energyCombo = new ComboBox<>();
        energyCombo.getItems().addAll(Energy.values());
        energyCombo.setValue(vehicle != null ? vehicle.getEnergy() : Energy.NOT_SPECIFIED);
        grid.add(new Label("Énergie:"), 0, row);
        grid.add(energyCombo, 1, row++);
        energyCombo.setUserData("energy");

        TextField powerField = new TextField(vehicle != null ? String.valueOf(vehicle.getPower()) : "");
        grid.add(new Label("Puissance (CV):"), 0, row);
        grid.add(powerField, 1, row++);
        powerField.setUserData("power");

        TextField seatsField = new TextField(vehicle != null ? String.valueOf(vehicle.getSeats()) : "");
        grid.add(new Label("Nombre de sièges:"), 0, row);
        grid.add(seatsField, 1, row++);
        seatsField.setUserData("seats");

        TextField capacityField = new TextField(vehicle != null ? String.valueOf(vehicle.getCapacity()) : "");
        grid.add(new Label("Capacité (L):"), 0, row);
        grid.add(capacityField, 1, row++);
        capacityField.setUserData("capacity");

        TextField utilityWeightField = new TextField(vehicle != null ? String.valueOf(vehicle.getUtilityWeight()) : "");
        grid.add(new Label("Poids utilitaire (kg):"), 0, row);
        grid.add(utilityWeightField, 1, row++);
        utilityWeightField.setUserData("utilityWeight");

        TextField colorField = new TextField(vehicle != null ? vehicle.getColor() : "");
        grid.add(new Label("Couleur:"), 0, row);
        grid.add(colorField, 1, row++);
        colorField.setUserData("color");

        TextField kilometersField = new TextField(vehicle != null ? String.valueOf(vehicle.getKilometers()) : "");
        grid.add(new Label("Kilométrage:"), 0, row);
        grid.add(kilometersField, 1, row++);
        kilometersField.setUserData("kilometers");

        DatePicker acquisitionDatePicker = new DatePicker(
            vehicle != null && vehicle.getAcquisitionDate() != null ?
            vehicle.getAcquisitionDate().toLocalDate() : LocalDate.now()
        );
        grid.add(new Label("Date d'acquisition:"), 0, row);
        grid.add(acquisitionDatePicker, 1, row++);
        acquisitionDatePicker.setUserData("acquisitionDate");

        ComboBox<Status> statusCombo = new ComboBox<>();
        statusCombo.getItems().addAll(Status.values());
        statusCombo.setValue(vehicle != null ? vehicle.getStatus() : Status.AVAILABLE);
        grid.add(new Label("Statut:"), 0, row);
        grid.add(statusCombo, 1, row++);
        statusCombo.setUserData("status");

        return grid;
    }

    private Vehicle extractVehicleFromForm(GridPane grid, UUID uuid) {
        String licensePlate = "";
        String manufacturer = "";
        String model = "";
        Energy energy = Energy.NOT_SPECIFIED;
        int power = 0;
        int seats = 0;
        int capacity = 0;
        int utilityWeight = 0;
        String color = "";
        int kilometers = 0;
        LocalDateTime acquisitionDate = LocalDateTime.now();
        Status status = Status.AVAILABLE;

        for (Node node : grid.getChildren()) {
            Object userData = node.getUserData();
            if (userData == null) continue;

            switch (userData.toString()) {
                case "licensePlate":
                    licensePlate = ((TextField) node).getText();
                    break;
                case "manufacturer":
                    manufacturer = ((TextField) node).getText();
                    break;
                case "model":
                    model = ((TextField) node).getText();
                    break;
                case "energy":
                    energy = ((ComboBox<Energy>) node).getValue();
                    break;
                case "power":
                    try {
                        power = Integer.parseInt(((TextField) node).getText());
                    } catch (NumberFormatException e) {
                        power = 0;
                    }
                    break;
                case "seats":
                    try {
                        seats = Integer.parseInt(((TextField) node).getText());
                    } catch (NumberFormatException e) {
                        seats = 0;
                    }
                    break;
                case "capacity":
                    try {
                        capacity = Integer.parseInt(((TextField) node).getText());
                    } catch (NumberFormatException e) {
                        capacity = 0;
                    }
                    break;
                case "utilityWeight":
                    try {
                        utilityWeight = Integer.parseInt(((TextField) node).getText());
                    } catch (NumberFormatException e) {
                        utilityWeight = 0;
                    }
                    break;
                case "color":
                    color = ((TextField) node).getText();
                    break;
                case "kilometers":
                    try {
                        kilometers = Integer.parseInt(((TextField) node).getText());
                    } catch (NumberFormatException e) {
                        kilometers = 0;
                    }
                    break;
                case "acquisitionDate":
                    LocalDate date = ((DatePicker) node).getValue();
                    acquisitionDate = date != null ? date.atTime(LocalTime.MIDNIGHT) : LocalDateTime.now();
                    break;
                case "status":
                    status = ((ComboBox<Status>) node).getValue();
                    break;
            }
        }

        return new Vehicle(uuid, licensePlate, manufacturer, model, energy, power, seats,
                          capacity, utilityWeight, color, kilometers, acquisitionDate, status);
    }

    private void confirmDeleteVehicle() {
        Vehicle selected = model.getSelectedVehicle();
        if (selected == null) return;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        StyleApplier.applyStylesheets(alert);
        alert.setTitle("Confirmer la suppression");
        alert.setHeaderText("Supprimer le véhicule");
        alert.setContentText("Êtes-vous sûr de vouloir supprimer ce véhicule ?\n\n" +
                           selected.getManufacturer() + " " + selected.getModel() +
                           " (" + selected.getLicencePlate() + ")\n\n" +
                           "Cette action est irréversible.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            deleteVehicleAction.accept(selected.getUuid());
        }
    }
}

