package groupe1.il3.app.gui.reservations;

import groupe1.il3.app.domain.reservation.Reservation;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.Builder;

import java.time.format.DateTimeFormatter;

public class ReservationsViewBuilder implements Builder<Region> {
    private final ReservationsModel model;
    private final Runnable loadReservationsAction;

    public ReservationsViewBuilder(ReservationsModel model, Runnable loadReservationsAction) {
        this.model = model;
        this.loadReservationsAction = loadReservationsAction;
    }

    @Override
    public Region build() {
        BorderPane mainPane = new BorderPane();
        mainPane.setPadding(new Insets(10));

        mainPane.setLeft(createReservationsListPane());
        mainPane.setCenter(createReservationDetailsPane());

        loadReservationsAction.run();

        return mainPane;
    }

    private Region createReservationsListPane() {
        VBox listPane = new VBox(10);
        listPane.setPadding(new Insets(10));
        listPane.setPrefWidth(350);

        Label title = new Label("Mes Réservations");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        ListView<Reservation> reservationListView = new ListView<>();
        reservationListView.setItems(model.getReservations());
        reservationListView.setCellFactory(lv -> new ReservationListCell());

        reservationListView.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldVal, newVal) -> model.setSelectedReservation(newVal)
        );

        VBox.setVgrow(reservationListView, Priority.ALWAYS);

        Button refreshButton = new Button("Actualiser");
        refreshButton.setOnAction(e -> loadReservationsAction.run());
        refreshButton.setMaxWidth(Double.MAX_VALUE);
        refreshButton.disableProperty().bind(model.loadingProperty());

        Label loadingLabel = new Label("Chargement...");
        loadingLabel.visibleProperty().bind(model.loadingProperty());

        listPane.getChildren().addAll(title, reservationListView, refreshButton, loadingLabel);

        return listPane;
    }

    private Region createReservationDetailsPane() {
        StackPane detailsPane = new StackPane();
        detailsPane.setPadding(new Insets(20));

        Label noSelectionLabel = new Label("Sélectionnez une réservation pour voir ses détails");
        noSelectionLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: gray;");

        VBox detailsBox = createDetailsBox();

        detailsBox.visibleProperty().bind(model.selectedReservationProperty().isNotNull());
        noSelectionLabel.visibleProperty().bind(model.selectedReservationProperty().isNull());

        detailsPane.getChildren().addAll(noSelectionLabel, detailsBox);

        return detailsPane;
    }

    private VBox createDetailsBox() {
        VBox detailsBox = new VBox(15);
        detailsBox.setAlignment(Pos.TOP_LEFT);

        Label titleLabel = new Label();
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        GridPane detailsGrid = new GridPane();
        detailsGrid.setHgap(15);
        detailsGrid.setVgap(10);
        detailsGrid.setPadding(new Insets(10));

        model.selectedReservationProperty().addListener((obs, oldVal, newVal) -> {
            detailsGrid.getChildren().clear();
            if (newVal != null) {
                titleLabel.setText("Détails de la réservation");

                int row = 0;

                addSectionHeader(detailsGrid, row++, "Période de réservation");
                addDetailRow(detailsGrid, row++, "Date de début:",
                    formatDateTime(newVal.getStartDate()));
                addDetailRow(detailsGrid, row++, "Date de fin:",
                    formatDateTime(newVal.getEndDate()));

                row++;
                addSectionHeader(detailsGrid, row++, "Véhicule");
                if (newVal.getVehicle() != null) {
                    addDetailRow(detailsGrid, row++, "Modèle:",
                        newVal.getVehicle().getManufacturer() + " " + newVal.getVehicle().getModel());
                    addDetailRow(detailsGrid, row++, "Plaque:",
                        newVal.getVehicle().getLicencePlate());
                    addDetailRow(detailsGrid, row++, "Énergie:",
                        formatEnergy(newVal.getVehicle().getEnergy().toString()));
                    addDetailRow(detailsGrid, row++, "Sièges:",
                        String.valueOf(newVal.getVehicle().getSeats()));
                    addDetailRow(detailsGrid, row++, "Couleur:",
                        newVal.getVehicle().getColor());
                } else {
                    addDetailRow(detailsGrid, row++, "Véhicule:", "Non disponible");
                }

                row++;
                addSectionHeader(detailsGrid, row++, "Agent");
                if (newVal.getAgent() != null) {
                    addDetailRow(detailsGrid, row++, "Nom:",
                        newVal.getAgent().getFirstname() + " " + newVal.getAgent().getLastname());
                    addDetailRow(detailsGrid, row++, "Email:",
                        newVal.getAgent().getEmail());
                } else {
                    addDetailRow(detailsGrid, row++, "Agent:", "Non disponible");
                }
            }
        });

        detailsBox.getChildren().addAll(titleLabel, new Separator(), detailsGrid);

        return detailsBox;
    }

    private void addSectionHeader(GridPane grid, int row, String title) {
        Label headerLabel = new Label(title);
        headerLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        grid.add(headerLabel, 0, row, 2, 1);
    }

    private void addDetailRow(GridPane grid, int row, String label, String value) {
        Label labelNode = new Label(label);
        labelNode.setStyle("-fx-font-weight: bold;");
        Label valueNode = new Label(value);

        grid.add(labelNode, 0, row);
        grid.add(valueNode, 1, row);
    }

    private String formatDateTime(java.time.LocalDateTime dateTime) {
        if (dateTime == null) {
            return "N/A";
        }
        return dateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
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

    private static class ReservationListCell extends ListCell<Reservation> {
        @Override
        protected void updateItem(Reservation reservation, boolean empty) {
            super.updateItem(reservation, empty);

            if (empty || reservation == null) {
                setText(null);
                setGraphic(null);
            } else {
                VBox cellContent = new VBox(5);
                cellContent.setPadding(new Insets(5));

                Label vehicleLabel = new Label();
                if (reservation.getVehicle() != null) {
                    vehicleLabel.setText(reservation.getVehicle().getManufacturer() + " " +
                                        reservation.getVehicle().getModel());
                    vehicleLabel.setStyle("-fx-font-weight: bold;");
                } else {
                    vehicleLabel.setText("Véhicule inconnu");
                    vehicleLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: gray;");
                }

                Label dateLabel = new Label();
                if (reservation.getStartDate() != null) {
                    dateLabel.setText("Du " +
                        reservation.getStartDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
                } else {
                    dateLabel.setText("Date non spécifiée");
                }

                Label endDateLabel = new Label();
                if (reservation.getEndDate() != null) {
                    endDateLabel.setText("Au " +
                        reservation.getEndDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
                } else {
                    endDateLabel.setText("");
                }

                cellContent.getChildren().addAll(vehicleLabel, dateLabel, endDateLabel);
                setGraphic(cellContent);
            }
        }
    }
}
