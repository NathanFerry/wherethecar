package groupe1.il3.app.gui.history;

import groupe1.il3.app.domain.reservation.Reservation;
import groupe1.il3.app.domain.reservation.ReservationStatus;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.Builder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HistoryViewBuilder implements Builder<Region> {
    private final HistoryModel model;
    private final Runnable loadHistoryAction;

    public HistoryViewBuilder(HistoryModel model, Runnable loadHistoryAction) {
        this.model = model;
        this.loadHistoryAction = loadHistoryAction;
    }

    @Override
    public Region build() {
        BorderPane mainPane = new BorderPane();
        mainPane.setPadding(new Insets(10));

        mainPane.setLeft(createHistoryListPane());
        mainPane.setCenter(createReservationDetailsPane());

        loadHistoryAction.run();

        return mainPane;
    }

    private Region createHistoryListPane() {
        VBox listPane = new VBox(10);
        listPane.setPadding(new Insets(10));
        listPane.setPrefWidth(350);

        Label title = new Label("Historique des Réservations");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        ListView<Reservation> reservationListView = new ListView<>();
        reservationListView.setItems(model.reservationsProperty());
        reservationListView.setCellFactory(lv -> new ReservationListCell());

        reservationListView.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldVal, newVal) -> model.setSelectedReservation(newVal)
        );

        VBox.setVgrow(reservationListView, Priority.ALWAYS);

        Button refreshButton = new Button("Actualiser");
        refreshButton.setOnAction(e -> loadHistoryAction.run());
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
                addDetailRow(detailsGrid, row++, "Statut:", formatReservationStatus(newVal.status()));
                addDetailRow(detailsGrid, row++, "Date de début:",
                    formatDateTime(newVal.startDate()));
                addDetailRow(detailsGrid, row++, "Date de fin:",
                    formatDateTime(newVal.endDate()));

                row++;
                addSectionHeader(detailsGrid, row++, "Véhicule");
                if (newVal.vehicle() != null) {
                    addDetailRow(detailsGrid, row++, "Modèle:",
                        newVal.vehicle().manufacturer() + " " + newVal.vehicle().model());
                    addDetailRow(detailsGrid, row++, "Plaque:",
                        newVal.vehicle().licencePlate());
                    addDetailRow(detailsGrid, row++, "Énergie:",
                        formatEnergy(newVal.vehicle().energy().toString()));
                    addDetailRow(detailsGrid, row++, "Sièges:",
                        String.valueOf(newVal.vehicle().seats()));
                    addDetailRow(detailsGrid, row++, "Couleur:",
                        newVal.vehicle().color());
                } else {
                    addDetailRow(detailsGrid, row++, "Véhicule:", "Non disponible");
                }

                row++;
                addSectionHeader(detailsGrid, row++, "Agent");
                if (newVal.agent() != null) {
                    addDetailRow(detailsGrid, row++, "Nom:",
                        newVal.agent().firstname() + " " + newVal.agent().lastname());
                    addDetailRow(detailsGrid, row++, "Email:",
                        newVal.agent().email());
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
        headerLabel.getStyleClass().add("reservation-section-header");
        grid.add(headerLabel, 0, row, 2, 1);
    }

    private void addDetailRow(GridPane grid, int row, String label, String value) {
        Label labelNode = new Label(label);
        labelNode.getStyleClass().add("reservation-detail-label");
        Label valueNode = new Label(value);
        valueNode.getStyleClass().add("reservation-detail-value");

        grid.add(labelNode, 0, row);
        grid.add(valueNode, 1, row);
    }

    private String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "N/A";
        }
        return dateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }

    private String formatReservationStatus(ReservationStatus status) {
        if (status == null) return "N/A";
        return switch (status) {
            case PENDING -> "En attente d'approbation";
            case CONFIRMED -> "Confirmée";
            case CANCELLED -> "Annulée";
            case COMPLETED -> "Terminée";
        };
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

                Label vehicleLabel = getVehicleTypeLabel(reservation);
                Label statusLabel = getStatusLabel(reservation);
                Label dateLabel = getReservationStartDateLabel(reservation);
                Label endDateLabel = getReservationEndDateLabel(reservation);

                cellContent.getChildren().addAll(vehicleLabel, statusLabel, dateLabel, endDateLabel);
                setGraphic(cellContent);
            }
        }

        private static Label getStatusLabel(Reservation reservation) {
            Label statusLabel = new Label();
            ReservationStatus status = reservation.status();

            if (status != null) {
                String statusText = switch (status) {
                    case PENDING -> "⏳ En attente";
                    case CONFIRMED -> "✓ Confirmée";
                    case CANCELLED -> "✗ Annulée";
                    case COMPLETED -> "✓ Terminée";
                };

                String statusClass = switch (status) {
                    case PENDING -> "reservation-status-pending";
                    case CONFIRMED -> "reservation-status-confirmed";
                    case CANCELLED -> "reservation-status-cancelled";
                    case COMPLETED -> "reservation-status-completed";
                };

                statusLabel.setText(statusText);
                statusLabel.getStyleClass().add(statusClass);
            } else {
                statusLabel.setText("Statut inconnu");
                statusLabel.getStyleClass().add("text-muted");
            }

            return statusLabel;
        }

        private static Label getVehicleTypeLabel(Reservation reservation) {
            Label vehicleLabel = new Label();

            if (reservation.vehicle() != null) {
                vehicleLabel.setText(reservation.vehicle().manufacturer() + " " +
                        reservation.vehicle().model());
                vehicleLabel.getStyleClass().add("label-bold");
            } else {
                vehicleLabel.setText("Véhicule inconnu");
                vehicleLabel.getStyleClass().addAll("label-bold", "text-muted");
            }

            return vehicleLabel;
        }

        private static Label getReservationStartDateLabel(Reservation reservation) {
            Label dateLabel = new Label();

            if (reservation.startDate() != null) {
                dateLabel.setText("Du " +
                        reservation.startDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
            } else {
                dateLabel.setText("Date non spécifiée");
            }

            return dateLabel;
        }

        private static Label getReservationEndDateLabel(Reservation reservation) {
            Label endDateLabel = new Label();
            if (reservation.endDate() != null) {
                endDateLabel.setText("Au " +
                        reservation.endDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
            } else {
                endDateLabel.setText("");
            }
            return endDateLabel;
        }
    }
}

