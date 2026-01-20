package groupe1.il3.app.gui.vehicleselector;

import groupe1.il3.app.domain.reservation.Reservation;
import groupe1.il3.app.domain.vehicle.Vehicle;
import groupe1.il3.app.gui.style.StyleApplier;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;

public class VehicleSelectorViewBuilder implements Builder<Region> {
    private final VehicleSelectorModel model;
    private final Runnable loadVehiclesAction;
    private final Runnable reserveVehicleAction;
    private final Runnable loadVehicleReservationsAction;

    public VehicleSelectorViewBuilder(VehicleSelectorModel model, Runnable loadVehiclesAction, Runnable reserveVehicleAction, Runnable loadVehicleReservationsAction) {
        this.model = model;
        this.loadVehiclesAction = loadVehiclesAction;
        this.reserveVehicleAction = reserveVehicleAction;
        this.loadVehicleReservationsAction = loadVehicleReservationsAction;
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
        listPane.getStyleClass().add("vehicle-list-container");

        Label title = new Label("Liste des Véhicules");
        title.getStyleClass().add("vehicle-list-title");

        ListView<Vehicle> vehicleListView = new ListView<>();
        vehicleListView.setItems(model.vehiclesProperty());
        vehicleListView.setCellFactory(lv -> new VehicleListCell());

        vehicleListView.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldVal, newVal) -> {
                model.setSelectedVehicle(newVal);
                if (newVal != null) {
                    loadVehicleReservationsAction.run();
                }
            }
        );

        VBox.setVgrow(vehicleListView, Priority.ALWAYS);

        Button refreshButton = new Button("Actualiser");
        refreshButton.setOnAction(e -> loadVehiclesAction.run());
        refreshButton.setMaxWidth(Double.MAX_VALUE);
        refreshButton.getStyleClass().add("vehicle-refresh-button");

        listPane.getChildren().addAll(title, vehicleListView, refreshButton);

        return listPane;
    }

    private Region createVehicleDetailsPane() {
        StackPane detailsPane = new StackPane();
        detailsPane.setPadding(new Insets(20));
        detailsPane.getStyleClass().add("vehicle-details-pane");

        Label noSelectionLabel = new Label("Sélectionnez un véhicule pour voir ses détails");
        noSelectionLabel.getStyleClass().add("no-selection-label");

        HBox contentBox = new HBox(20);
        VBox detailsBox = createDetailsBox();
        VBox calendarBox = createCalendarBox();

        contentBox.getChildren().addAll(detailsBox, calendarBox);
        HBox.setHgrow(detailsBox, Priority.SOMETIMES);
        HBox.setHgrow(calendarBox, Priority.SOMETIMES);

        // Show details only when a vehicle is selected
        contentBox.visibleProperty().bind(model.selectedVehicleProperty().isNotNull());
        noSelectionLabel.visibleProperty().bind(model.selectedVehicleProperty().isNull());

        detailsPane.getChildren().addAll(noSelectionLabel, contentBox);

        return detailsPane;
    }

    private VBox createDetailsBox() {
        VBox detailsBox = new VBox(15);
        detailsBox.setAlignment(Pos.TOP_LEFT);

        Label titleLabel = new Label();
        titleLabel.getStyleClass().add("vehicle-details-title");

        GridPane detailsGrid = new GridPane();
        detailsGrid.setHgap(15);
        detailsGrid.setVgap(10);
        detailsGrid.setPadding(new Insets(10));

        Button reserveButton = new Button("Réserver ce véhicule");
        reserveButton.setOnAction(e -> showReservationDialog());
        reserveButton.setMaxWidth(Double.MAX_VALUE);
        reserveButton.getStyleClass().add("vehicle-reserve-button");

        reserveButton.disableProperty().bind(model.selectedVehicleProperty().isNull());

        model.selectedVehicleProperty().addListener((obs, oldVal, newVal) -> {
            detailsGrid.getChildren().clear();
            if (newVal != null) {
                titleLabel.setText(newVal.manufacturer() + " " + newVal.model());

                addDetailRow(detailsGrid, 0, "Plaque d'immatriculation:", newVal.licencePlate());
                addDetailRow(detailsGrid, 1, "Constructeur:", newVal.manufacturer());
                addDetailRow(detailsGrid, 2, "Modèle:", newVal.model());
                addDetailRow(detailsGrid, 3, "Énergie:", formatEnergy(newVal.energy().toString()));
                addDetailRow(detailsGrid, 4, "Puissance:", newVal.power() + " CV");
                addDetailRow(detailsGrid, 5, "Sièges:", String.valueOf(newVal.seats()));
                addDetailRow(detailsGrid, 6, "Capacité:", newVal.capacity() + " L");
                addDetailRow(detailsGrid, 7, "Poids utilitaire:", newVal.utilityWeight() + " kg");
                addDetailRow(detailsGrid, 8, "Couleur:", newVal.color());
                addDetailRow(detailsGrid, 9, "Kilométrage:", newVal.kilometers() + " km");
                addDetailRow(detailsGrid, 10, "Date d'acquisition:",
                    newVal.acquisitionDate() != null ?
                    newVal.acquisitionDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "N/A");
                addDetailRow(detailsGrid, 11, "Statut:", formatStatus(newVal.status().toString()));
            }
        });

        detailsBox.getChildren().addAll(titleLabel, new Separator(), detailsGrid, reserveButton);

        return detailsBox;
    }

    private void addDetailRow(GridPane grid, int row, String label, String value) {
        Label labelNode = new Label(label);
        labelNode.getStyleClass().add("vehicle-detail-label");
        Label valueNode = new Label(value);
        valueNode.getStyleClass().add("vehicle-detail-value");

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

    private VBox createCalendarBox() {
        VBox calendarBox = new VBox(10);
        calendarBox.setPrefWidth(450);
        calendarBox.getStyleClass().add("calendar-container");

        Label calendarTitle = new Label("Disponibilité du véhicule");
        calendarTitle.getStyleClass().add("calendar-title");

        // Calendar navigation
        HBox navigationBox = new HBox(10);
        navigationBox.getStyleClass().add("calendar-month-navigation");
        navigationBox.setAlignment(Pos.CENTER);

        Button prevMonthButton = new Button("◀");
        prevMonthButton.getStyleClass().add("calendar-nav-button");

        Label monthLabel = new Label();
        monthLabel.getStyleClass().add("calendar-month-label");

        Button nextMonthButton = new Button("▶");
        nextMonthButton.getStyleClass().add("calendar-nav-button");

        navigationBox.getChildren().addAll(prevMonthButton, monthLabel, nextMonthButton);

        // Calendar grid
        GridPane calendarGrid = new GridPane();
        calendarGrid.getStyleClass().add("calendar-grid");

        // Legend
        HBox legendBox = new HBox(15);
        legendBox.getStyleClass().add("calendar-legend");
        legendBox.setAlignment(Pos.CENTER);

        HBox availableItem = new HBox(5);
        availableItem.getStyleClass().add("calendar-legend-item");
        Region availableBox = new Region();
        availableBox.getStyleClass().addAll("calendar-legend-box", "calendar-legend-box-available");
        Label availableLabel = new Label("Disponible");
        availableLabel.getStyleClass().add("calendar-legend-label");
        availableItem.getChildren().addAll(availableBox, availableLabel);

        HBox reservedItem = new HBox(5);
        reservedItem.getStyleClass().add("calendar-legend-item");
        Region reservedBox = new Region();
        reservedBox.getStyleClass().addAll("calendar-legend-box", "calendar-legend-box-reserved");
        Label reservedLabel = new Label("Réservé");
        reservedLabel.getStyleClass().add("calendar-legend-label");
        reservedItem.getChildren().addAll(reservedBox, reservedLabel);

        legendBox.getChildren().addAll(availableItem, reservedItem);

        // Reservation list
        VBox reservationListBox = new VBox(5);
        reservationListBox.getStyleClass().add("reservation-list-container");

        Label reservationListTitle = new Label("Réservations confirmées");
        reservationListTitle.getStyleClass().add("reservation-list-title");

        ScrollPane reservationScrollPane = new ScrollPane();
        reservationScrollPane.setFitToWidth(true);
        reservationScrollPane.setStyle("-fx-background-color: transparent;");

        VBox reservationItemsBox = new VBox(5);
        reservationScrollPane.setContent(reservationItemsBox);

        VBox.setVgrow(reservationScrollPane, Priority.ALWAYS);
        reservationListBox.getChildren().addAll(reservationListTitle, reservationScrollPane);

        calendarBox.getChildren().addAll(calendarTitle, navigationBox, calendarGrid, legendBox, reservationListBox);

        // Calendar state
        YearMonth[] currentMonth = {YearMonth.now()};

        // Update calendar display
        Runnable updateCalendar = () -> {
            calendarGrid.getChildren().clear();

            monthLabel.setText(currentMonth[0].getMonth().getDisplayName(TextStyle.FULL, Locale.FRENCH) + " " + currentMonth[0].getYear());

            // Day headers
            String[] dayHeaders = {"Lun", "Mar", "Mer", "Jeu", "Ven", "Sam", "Dim"};
            for (int i = 0; i < 7; i++) {
                Label dayHeader = new Label(dayHeaders[i]);
                dayHeader.getStyleClass().add("calendar-day-header");
                calendarGrid.add(dayHeader, i, 0);
            }

            // Get first day of month and number of days
            LocalDate firstOfMonth = currentMonth[0].atDay(1);
            int daysInMonth = currentMonth[0].lengthOfMonth();
            int dayOfWeek = firstOfMonth.getDayOfWeek().getValue(); // 1=Monday, 7=Sunday

            // Fill in days
            int row = 1;
            int col = dayOfWeek - 1; // Start at the correct day

            for (int day = 1; day <= daysInMonth; day++) {
                LocalDate date = currentMonth[0].atDay(day);
                Label dayLabel = new Label(String.valueOf(day));
                dayLabel.getStyleClass().add("calendar-day-cell");

                // Check if this day is reserved
                boolean isReserved = model.selectedVehicleReservationsProperty().stream()
                    .anyMatch(reservation -> {
                        LocalDate resStart = reservation.startDate().toLocalDate();
                        LocalDate resEnd = reservation.endDate().toLocalDate();
                        return !date.isBefore(resStart) && !date.isAfter(resEnd);
                    });

                if (isReserved) {
                    dayLabel.getStyleClass().add("calendar-day-reserved");
                } else {
                    dayLabel.getStyleClass().add("calendar-day-available");
                }

                // Highlight today
                if (date.equals(LocalDate.now())) {
                    dayLabel.getStyleClass().add("calendar-day-today");
                }

                calendarGrid.add(dayLabel, col, row);

                col++;
                if (col > 6) {
                    col = 0;
                    row++;
                }
            }

            // Update reservation list
            reservationItemsBox.getChildren().clear();
            if (model.selectedVehicleReservationsProperty().isEmpty()) {
                Label noReservations = new Label("Aucune réservation");
                noReservations.setStyle("-fx-text-fill: -fx-text-secondary; -fx-font-style: italic;");
                reservationItemsBox.getChildren().add(noReservations);
            } else {
                for (Reservation reservation : model.selectedVehicleReservationsProperty()) {
                    VBox reservationItem = new VBox(3);
                    reservationItem.getStyleClass().add("reservation-item");

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                    Label dateLabel = new Label("Du " + reservation.startDate().format(formatter) + " au " + reservation.endDate().format(formatter));
                    dateLabel.getStyleClass().add("reservation-item-date");

                    Label statusLabel = new Label("Statut: " + formatReservationStatus(reservation.status().toString()));
                    statusLabel.getStyleClass().add("reservation-item-status");
                    statusLabel.getStyleClass().add("reservation-status-" + reservation.status().toString().toLowerCase());

                    reservationItem.getChildren().addAll(dateLabel, statusLabel);
                    reservationItemsBox.getChildren().add(reservationItem);
                }
            }
        };

        // Navigation buttons
        prevMonthButton.setOnAction(e -> {
            currentMonth[0] = currentMonth[0].minusMonths(1);
            updateCalendar.run();
        });

        nextMonthButton.setOnAction(e -> {
            currentMonth[0] = currentMonth[0].plusMonths(1);
            updateCalendar.run();
        });

        // Update calendar when reservations change
        model.selectedVehicleReservationsProperty().addListener((obs, oldVal, newVal) -> updateCalendar.run());

        // Initial update
        updateCalendar.run();

        return calendarBox;
    }

    private String formatReservationStatus(String status) {
        return switch (status) {
            case "PENDING" -> "En attente";
            case "CONFIRMED" -> "Confirmée";
            case "CANCELLED" -> "Annulée";
            case "COMPLETED" -> "Terminée";
            default -> status;
        };
    }

    private void showReservationDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Réserver un véhicule");
        dialog.setHeaderText("Réservation de " + model.getSelectedVehicle().manufacturer() + " " + model.getSelectedVehicle().model());

        StyleApplier.applyStylesheets(dialog);

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
                        StyleApplier.applyStylesheets(alert);
                        alert.setTitle("Réservation confirmée");
                        alert.setHeaderText(null);
                        alert.setContentText("Votre réservation a été créée avec succès!");
                        alert.showAndWait();
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        StyleApplier.applyStylesheets(alert);
                        alert.setTitle("Erreur de réservation");
                        alert.setHeaderText(null);
                        alert.setContentText(model.getReservationErrorMessage());
                        alert.showAndWait();
                    }
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    StyleApplier.applyStylesheets(alert);
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
                content.getStyleClass().add("vehicle-list-cell");

                Label titleLabel = new Label(vehicle.manufacturer() + " " + vehicle.model());
                titleLabel.getStyleClass().add("vehicle-manufacturer");

                Label plateLabel = new Label(vehicle.licencePlate());
                plateLabel.getStyleClass().add("vehicle-license-plate");

                Label statusLabel = new Label(formatStatusForCell(vehicle.status().toString()));
                String statusClass = switch (vehicle.status().toString()) {
                    case "AVAILABLE" -> "vehicle-status-available";
                    case "RESERVED" -> "vehicle-status-reserved";
                    case "MAINTENANCE" -> "vehicle-status-maintenance";
                    default -> "";
                };
                if (!statusClass.isEmpty()) {
                    statusLabel.getStyleClass().add(statusClass);
                }

                content.getChildren().addAll(titleLabel, plateLabel, statusLabel);
                setGraphic(content);
                setText(null);
            }
        }

        private static String formatStatusForCell(String status) {
            return switch (status) {
                case "AVAILABLE" -> "✓ Disponible";
                case "RESERVED" -> "⏳ Réservé";
                case "MAINTENANCE" -> "⚠️ Maintenance";
                default -> "Inconnu";
            };
        }
    }
}
