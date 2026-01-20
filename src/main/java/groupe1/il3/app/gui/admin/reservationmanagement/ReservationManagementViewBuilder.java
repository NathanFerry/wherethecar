package groupe1.il3.app.gui.admin.reservationmanagement;

import groupe1.il3.app.domain.reservation.Reservation;
import groupe1.il3.app.domain.reservation.ReservationStatus;
import groupe1.il3.app.gui.style.StyleApplier;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.Builder;

import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ReservationManagementViewBuilder implements Builder<Region> {

    private final ReservationManagementModel model;
    private final Runnable loadPendingReservationsAction;
    private final BiConsumer<UUID, UUID> approveReservationAction;
    private final Consumer<UUID> cancelReservationAction;

    public ReservationManagementViewBuilder(
        ReservationManagementModel model,
        Runnable loadPendingReservationsAction,
        BiConsumer<UUID, UUID> approveReservationAction,
        Consumer<UUID> cancelReservationAction
    ) {
        this.model = model;
        this.loadPendingReservationsAction = loadPendingReservationsAction;
        this.approveReservationAction = approveReservationAction;
        this.cancelReservationAction = cancelReservationAction;
    }

    @Override
    public Region build() {
        BorderPane pane = new BorderPane();
        pane.setPadding(new Insets(10));

        VBox listPane = new VBox(10);
        listPane.setPrefWidth(400);

        Label listTitle = new Label("Réservations en Attente d'Approbation");
        listTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        ListView<Reservation> reservationListView = new ListView<>();
        reservationListView.setItems(model.pendingReservationsProperty());
        reservationListView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Reservation reservation, boolean empty) {
                super.updateItem(reservation, empty);
                if (empty || reservation == null) {
                    setText(null);
                } else {
                    String agentName = reservation.agent() != null ?
                        reservation.agent().firstname() + " " + reservation.agent().lastname() : "Unknown";
                    String vehicleInfo = reservation.vehicle() != null ?
                        reservation.vehicle().manufacturer() + " " + reservation.vehicle().model() : "Unknown";
                    setText(agentName + " - " + vehicleInfo);
                }
            }
        });

        reservationListView.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldVal, newVal) -> model.setSelectedReservation(newVal)
        );

        VBox.setVgrow(reservationListView, Priority.ALWAYS);

        HBox buttonBox = new HBox(10);
        Button approveBtn = new Button("Approuver");
        approveBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        approveBtn.setOnAction(e -> confirmApproveReservation());
        approveBtn.disableProperty().bind(model.selectedReservationProperty().isNull());

        Button cancelBtn = new Button("Refuser");
        cancelBtn.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
        cancelBtn.setOnAction(e -> confirmCancelReservation());
        cancelBtn.disableProperty().bind(model.selectedReservationProperty().isNull());

        Button refreshBtn = new Button("Actualiser");
        refreshBtn.setOnAction(e -> loadPendingReservationsAction.run());

        buttonBox.getChildren().addAll(approveBtn, cancelBtn, refreshBtn);

        listPane.getChildren().addAll(listTitle, reservationListView, buttonBox);

        pane.setLeft(listPane);

        VBox detailsPane = createReservationDetailsPane();
        pane.setCenter(detailsPane);

        return pane;
    }

    private VBox createReservationDetailsPane() {
        VBox detailsPane = new VBox(10);
        detailsPane.setPadding(new Insets(20));
        detailsPane.setAlignment(Pos.TOP_LEFT);

        Label noSelectionLabel = new Label("Sélectionnez une réservation pour voir ses détails");

        VBox detailsBox = new VBox(15);
        Label titleLabel = new Label();
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        GridPane detailsGrid = new GridPane();
        detailsGrid.setHgap(15);
        detailsGrid.setVgap(10);

        model.selectedReservationProperty().addListener((obs, oldVal, newVal) -> {
            detailsGrid.getChildren().clear();
            if (newVal != null) {
                titleLabel.setText("Détails de la Réservation");

                addDetailRow(detailsGrid, 0, "UUID:", newVal.uuid().toString());

                if (newVal.agent() != null) {
                    addDetailRow(detailsGrid, 1, "Agent:",
                        newVal.agent().firstname() + " " + newVal.agent().lastname());
                    addDetailRow(detailsGrid, 2, "Email:", newVal.agent().email());
                }

                if (newVal.vehicle() != null) {
                    addDetailRow(detailsGrid, 3, "Véhicule:",
                        newVal.vehicle().manufacturer() + " " + newVal.vehicle().model());
                    addDetailRow(detailsGrid, 4, "Plaque:", newVal.vehicle().licencePlate());
                }

                addDetailRow(detailsGrid, 5, "Date de début:",
                    newVal.startDate() != null ?
                    newVal.startDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) : "N/A");
                addDetailRow(detailsGrid, 6, "Date de fin:",
                    newVal.endDate() != null ?
                    newVal.endDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) : "N/A");
                addDetailRow(detailsGrid, 7, "Statut:", formatReservationStatus(newVal.status()));
            }
        });

        detailsBox.getChildren().addAll(titleLabel, new Separator(), detailsGrid);
        detailsBox.visibleProperty().bind(model.selectedReservationProperty().isNotNull());

        noSelectionLabel.visibleProperty().bind(model.selectedReservationProperty().isNull());

        detailsPane.getChildren().addAll(noSelectionLabel, detailsBox);

        return detailsPane;
    }

    private void addDetailRow(GridPane grid, int row, String label, String value) {
        Label labelNode = new Label(label);
        labelNode.setStyle("-fx-font-weight: bold;");
        Label valueNode = new Label(value);

        grid.add(labelNode, 0, row);
        grid.add(valueNode, 1, row);
    }

    private String formatReservationStatus(ReservationStatus status) {
        if (status == null) return "N/A";
        switch (status) {
            case PENDING: return "En Attente";
            case CONFIRMED: return "Confirmée";
            case CANCELLED: return "Annulée";
            case COMPLETED: return "Terminée";
            default: return "Inconnu";
        }
    }

    private void confirmApproveReservation() {
        Reservation selected = model.getSelectedReservation();
        if (selected == null) return;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        StyleApplier.applyStylesheets(alert);
        alert.setTitle("Confirmation d'approbation");
        alert.setHeaderText("Approuver la réservation");

        String agentName = selected.agent() != null ?
            selected.agent().firstname() + " " + selected.agent().lastname() : "Unknown";
        String vehicleInfo = selected.vehicle() != null ?
            selected.vehicle().manufacturer() + " " + selected.vehicle().model() : "Unknown";

        alert.setContentText("Êtes-vous sûr de vouloir approuver cette réservation ?\n\n" +
                           "Agent: " + agentName + "\n" +
                           "Véhicule: " + vehicleInfo + "\n" +
                           "Du: " + selected.startDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) + "\n" +
                           "Au: " + selected.endDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            approveReservationAction.accept(selected.uuid(), selected.vehicle().uuid());
        }
    }

    private void confirmCancelReservation() {
        Reservation selected = model.getSelectedReservation();
        if (selected == null) return;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        StyleApplier.applyStylesheets(alert);
        alert.setTitle("Confirmer le refus");
        alert.setHeaderText("Refuser la réservation");
        alert.setContentText("Voulez-vous vraiment refuser cette réservation ?");
        String agentName = selected.agent() != null ?
            selected.agent().firstname() + " " + selected.agent().lastname() : "Unknown";
        String vehicleInfo = selected.vehicle() != null ?
            selected.vehicle().manufacturer() + " " + selected.vehicle().model() : "Unknown";

        alert.setContentText("Êtes-vous sûr de vouloir refuser cette réservation ?\n\n" +
                           "Agent: " + agentName + "\n" +
                           "Véhicule: " + vehicleInfo + "\n" +
                           "Du: " + selected.startDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) + "\n" +
                           "Au: " + selected.endDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) + "\n\n" +
                           "Cette action est irréversible.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            cancelReservationAction.accept(selected.uuid());
        }
    }
}

