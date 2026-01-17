package groupe1.il3.app.gui.vehicleselector;

import groupe1.il3.app.domain.vehicle.Vehicle;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.Builder;

import java.time.format.DateTimeFormatter;

public class VehicleSelectorViewBuilder implements Builder<Region> {
    private final VehicleSelectorModel model;
    private final Runnable loadVehiclesAction;

    public VehicleSelectorViewBuilder(VehicleSelectorModel model, Runnable loadVehiclesAction) {
        this.model = model;
        this.loadVehiclesAction = loadVehiclesAction;
    }

    @Override
    public Region build() {
        BorderPane mainPane = new BorderPane();
        mainPane.setPadding(new Insets(10));

        // Left side: List of vehicles
        mainPane.setLeft(createVehicleListPane());

        // Right side: Vehicle details
        mainPane.setCenter(createVehicleDetailsPane());

        // Load vehicles on initialization
        loadVehiclesAction.run();

        return mainPane;
    }

    private Region createVehicleListPane() {
        VBox listPane = new VBox();

        Label title = new Label("Liste des Véhicules");

        ListView<Vehicle> vehicleListView = new ListView<>();
        vehicleListView.setItems(model.getVehicles());
        vehicleListView.setCellFactory(lv -> new VehicleListCell());

        // Bind selection to model
        vehicleListView.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldVal, newVal) -> model.setSelectedVehicle(newVal)
        );

        VBox.setVgrow(vehicleListView, Priority.ALWAYS);

        Button refreshButton = new Button("Actualiser");
        refreshButton.setOnAction(e -> loadVehiclesAction.run());
        refreshButton.setMaxWidth(Double.MAX_VALUE);

        listPane.getChildren().addAll(title, vehicleListView, refreshButton);

        return listPane;
    }

    private Region createVehicleDetailsPane() {
        StackPane detailsPane = new StackPane();
        detailsPane.setPadding(new Insets(20));

        Label noSelectionLabel = new Label("Sélectionnez un véhicule pour voir ses détails");

        VBox detailsBox = createDetailsBox();

        // Show details only when a vehicle is selected
        detailsBox.visibleProperty().bind(model.selectedVehicleProperty().isNotNull());
        noSelectionLabel.visibleProperty().bind(model.selectedVehicleProperty().isNull());

        detailsPane.getChildren().addAll(noSelectionLabel, detailsBox);

        return detailsPane;
    }

    private VBox createDetailsBox() {
        VBox detailsBox = new VBox(15);
        detailsBox.setAlignment(Pos.TOP_LEFT);

        Label titleLabel = new Label();

        GridPane detailsGrid = new GridPane();
        detailsGrid.setHgap(15);
        detailsGrid.setVgap(10);
        detailsGrid.setPadding(new Insets(10));

        // Create labels that update with selected vehicle
        model.selectedVehicleProperty().addListener((obs, oldVal, newVal) -> {
            detailsGrid.getChildren().clear();
            if (newVal != null) {
                titleLabel.setText(newVal.getManufacturer() + " " + newVal.getModel());

                addDetailRow(detailsGrid, 0, "Plaque d'immatriculation:", newVal.getLicencePlate());
                addDetailRow(detailsGrid, 1, "Constructeur:", newVal.getManufacturer());
                addDetailRow(detailsGrid, 2, "Modèle:", newVal.getModel());
                addDetailRow(detailsGrid, 3, "Énergie:", formatEnergy(newVal.getEnergy().toString()));
                addDetailRow(detailsGrid, 4, "Puissance:", newVal.getPower() + " CV");
                addDetailRow(detailsGrid, 5, "Sièges:", String.valueOf(newVal.getSeats()));
                addDetailRow(detailsGrid, 6, "Capacité:", newVal.getCapacity() + " L");
                addDetailRow(detailsGrid, 7, "Poids utilitaire:", newVal.getUtilityWeight() + " kg");
                addDetailRow(detailsGrid, 8, "Couleur:", newVal.getColor());
                addDetailRow(detailsGrid, 9, "Kilométrage:", newVal.getKilometers() + " km");
                addDetailRow(detailsGrid, 10, "Date d'acquisition:",
                    newVal.getAcquisitionDate() != null ?
                    newVal.getAcquisitionDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "N/A");
                addDetailRow(detailsGrid, 11, "Statut:", formatStatus(newVal.getStatus().toString()));
            }
        });

        detailsBox.getChildren().addAll(titleLabel, new Separator(), detailsGrid);

        return detailsBox;
    }

    private void addDetailRow(GridPane grid, int row, String label, String value) {
        Label labelNode = new Label(label);
        Label valueNode = new Label(value);

        grid.add(labelNode, 0, row);
        grid.add(valueNode, 1, row);
    }

    private String formatEnergy(String energy) {
        return switch (energy) {
            case "GASOLINE" -> "Essence";
            case "DIESEL" -> "Diesel";
            case "ELECTRIC" -> "Électrique";
            case "HYBRID" -> "Hybride";
            case "NATURAL_GAS" -> "Gaz naturel";
            case "NONE" -> "Aucune";
            default -> "Non spécifié";
        };
    }

    private String formatStatus(String status) {
        return switch (status) {
            case "AVAILABLE" -> "Disponible";
            case "RESERVED" -> "Réservé";
            case "MAINTENANCE" -> "En maintenance";
            default -> "Inconnu";
        };
    }

    // Custom ListCell for displaying vehicles
    private static class VehicleListCell extends ListCell<Vehicle> {
        @Override
        protected void updateItem(Vehicle vehicle, boolean empty) {
            super.updateItem(vehicle, empty);

            if (empty || vehicle == null) {
                setText(null);
                setGraphic(null);
            } else {
                VBox content = new VBox(3);

                Label titleLabel = new Label(vehicle.getManufacturer() + " " + vehicle.getModel());
                Label plateLabel = new Label(vehicle.getLicencePlate());
                Label statusLabel = new Label(formatStatusForCell(vehicle.getStatus().toString()));

                content.getChildren().addAll(titleLabel, plateLabel, statusLabel);
                setGraphic(content);
                setText(null);
            }
        }

        private static String formatStatusForCell(String status) {
            return switch (status) {
                case "AVAILABLE" -> "Disponible";
                case "RESERVED" -> "Réservé";
                case "MAINTENANCE" -> "Maintenance";
                default -> "Inconnu";
            };
        }
    }
}
