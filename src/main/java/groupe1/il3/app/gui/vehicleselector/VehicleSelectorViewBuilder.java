package groupe1.il3.app.gui.vehicleselector;

import groupe1.il3.app.domain.vehicle.Vehicle;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class VehicleSelectorViewBuilder implements Builder<Region> {
    private final VehicleSelectorModel model;
    private final Runnable loadVehiclesAction;
    private final Runnable reserveVehicleAction;

    public VehicleSelectorViewBuilder(VehicleSelectorModel model, Runnable loadVehiclesAction, Runnable reserveVehicleAction) {
        this.model = model;
        this.loadVehiclesAction = loadVehiclesAction;
        this.reserveVehicleAction = reserveVehicleAction;
    }

    @Override
    public Region build() {
        BorderPane mainPane = new BorderPane();
        mainPane.setPadding(new Insets(10));

        mainPane.setLeft(createVehicleListPane());

        mainPane.setCenter(createVehicleDetailsPane());

        loadVehiclesAction.run();

        return mainPane;
    }

    private Region createVehicleListPane() {
        VBox listPane = new VBox(10);
        listPane.setPadding(new Insets(10));
        listPane.setPrefWidth(350);

        Label title = new Label("Liste des Véhicules");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

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

        Button reserveButton = new Button("Réserver ce véhicule");
        reserveButton.setOnAction(e -> showReservationDialog());
        reserveButton.setMaxWidth(Double.MAX_VALUE);

        // Only enable reserve button if vehicle is available
        reserveButton.disableProperty().bind(
            model.selectedVehicleProperty().isNull().or(
                model.selectedVehicleProperty().isNotNull().and(
                    javafx.beans.binding.Bindings.createBooleanBinding(
                        () -> {
                            Vehicle v = model.getSelectedVehicle();
                            return v != null && !"AVAILABLE".equals(v.getStatus().toString());
                        },
                        model.selectedVehicleProperty()
                    )
                )
            )
        );

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

        detailsBox.getChildren().addAll(titleLabel, new Separator(), detailsGrid, reserveButton);

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

    private void showReservationDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Réserver un véhicule");
        dialog.setHeaderText("Réservation de " + model.getSelectedVehicle().getManufacturer() + " " + model.getSelectedVehicle().getModel());

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        Label startDateLabel = new Label("Date de début:");
        DatePicker startDatePicker = new DatePicker(LocalDate.now());

        Label startTimeLabel = new Label("Heure de début:");
        TextField startTimeField = new TextField("09:00");

        Label endDateLabel = new Label("Date de fin:");
        DatePicker endDatePicker = new DatePicker(LocalDate.now().plusDays(1));

        Label endTimeLabel = new Label("Heure de fin:");
        TextField endTimeField = new TextField("17:00");

        Label errorLabel = new Label();
        errorLabel.textProperty().bind(model.reservationErrorMessageProperty());
        errorLabel.setWrapText(true);
        errorLabel.setMaxWidth(300);

        grid.add(startDateLabel, 0, 0);
        grid.add(startDatePicker, 1, 0);
        grid.add(startTimeLabel, 0, 1);
        grid.add(startTimeField, 1, 1);

        grid.add(new Label(), 0, 2); // Spacer

        grid.add(endDateLabel, 0, 3);
        grid.add(endDatePicker, 1, 3);
        grid.add(endTimeLabel, 0, 4);
        grid.add(endTimeField, 1, 4);

        grid.add(errorLabel, 0, 5, 2, 1);

        dialog.getDialogPane().setContent(grid);

        ButtonType reserveButtonType = new ButtonType("Réserver", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(reserveButtonType, ButtonType.CANCEL);

        dialog.showAndWait().ifPresent(response -> {
            if (response == reserveButtonType) {
                try {
                    LocalDate startDate = startDatePicker.getValue();
                    LocalTime startTime = LocalTime.parse(startTimeField.getText());
                    LocalDateTime startDateTime = LocalDateTime.of(startDate, startTime);

                    LocalDate endDate = endDatePicker.getValue();
                    LocalTime endTime = LocalTime.parse(endTimeField.getText());
                    LocalDateTime endDateTime = LocalDateTime.of(endDate, endTime);

                    model.setReservationStartDate(startDateTime);
                    model.setReservationEndDate(endDateTime);

                    reserveVehicleAction.run();

                    if (model.getReservationErrorMessage().isEmpty()) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Réservation confirmée");
                        alert.setHeaderText(null);
                        alert.setContentText("Votre réservation a été créée avec succès!");
                        alert.showAndWait();
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Erreur de réservation");
                        alert.setHeaderText(null);
                        alert.setContentText(model.getReservationErrorMessage());
                        alert.showAndWait();
                    }
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Erreur");
                    alert.setHeaderText(null);
                    alert.setContentText("Format de date/heure invalide. Utilisez HH:mm pour l'heure (ex: 09:00)");
                    alert.showAndWait();
                }
            }
        });
    }

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
