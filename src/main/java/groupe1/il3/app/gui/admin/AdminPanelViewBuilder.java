package groupe1.il3.app.gui.admin;

import groupe1.il3.app.gui.admin.agentmanagement.AgentManagementController;
import groupe1.il3.app.gui.admin.reservationmanagement.ReservationManagementController;
import groupe1.il3.app.gui.admin.vehiclemanagement.VehicleManagementController;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.Builder;

public class AdminPanelViewBuilder implements Builder<Region> {

    private final AdminPanelModel model;
    private final VehicleManagementController vehicleManagementController;
    private final AgentManagementController agentManagementController;
    private final ReservationManagementController reservationManagementController;

    public AdminPanelViewBuilder(
        AdminPanelModel model,
        VehicleManagementController vehicleManagementController,
        AgentManagementController agentManagementController,
        ReservationManagementController reservationManagementController
    ) {
        this.model = model;
        this.vehicleManagementController = vehicleManagementController;
        this.agentManagementController = agentManagementController;
        this.reservationManagementController = reservationManagementController;
    }

    @Override
    public Region build() {
        BorderPane mainPane = new BorderPane();
        mainPane.setPadding(new Insets(10));

        mainPane.setTop(createTitleLabel());
        mainPane.setCenter(createTabPane());
        mainPane.setBottom(createMessageBox());

        loadAllData();

        return mainPane;
    }

    private Label createTitleLabel() {
        Label title = new Label("Panneau d'Administration");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        BorderPane.setMargin(title, new Insets(10));
        return title;
    }

    private TabPane createTabPane() {
        TabPane tabPane = new TabPane();
        tabPane.getTabs().addAll(
            createVehiclesTab(),
            createAgentsTab(),
            createReservationsTab()
        );
        return tabPane;
    }

    private Tab createVehiclesTab() {
        Tab vehiclesTab = new Tab("Gestion des Véhicules");
        vehiclesTab.setClosable(false);
        vehiclesTab.setContent(vehicleManagementController.getView());
        return vehiclesTab;
    }

    private Tab createAgentsTab() {
        Tab agentsTab = new Tab("Gestion des Utilisateurs");
        agentsTab.setClosable(false);
        agentsTab.setContent(agentManagementController.getView());
        return agentsTab;
    }

    private Tab createReservationsTab() {
        Tab reservationsTab = new Tab("Réservations en Attente");
        reservationsTab.setClosable(false);
        reservationsTab.setContent(reservationManagementController.getView());
        return reservationsTab;
    }

    private void loadAllData() {
        vehicleManagementController.loadVehicles();
        agentManagementController.loadAgents();
        reservationManagementController.loadPendingReservations();
    }

    private VBox createMessageBox() {
        VBox messageBox = new VBox(5);
        messageBox.setPadding(new Insets(10));
        messageBox.getStyleClass().add("admin-message-box");

        messageBox.getChildren().addAll(
            createErrorLabel(),
            createSuccessLabel()
        );

        return messageBox;
    }

    private Label createErrorLabel() {
        Label errorLabel = new Label();
        errorLabel.textProperty().bind(model.errorMessageProperty());
        errorLabel.getStyleClass().add("admin-error-label");
        errorLabel.setWrapText(true);
        errorLabel.managedProperty().bind(errorLabel.textProperty().isNotEmpty());
        errorLabel.visibleProperty().bind(errorLabel.textProperty().isNotEmpty());
        return errorLabel;
    }

    private Label createSuccessLabel() {
        Label successLabel = new Label();
        successLabel.textProperty().bind(model.successMessageProperty());
        successLabel.getStyleClass().add("admin-success-label");
        successLabel.setWrapText(true);
        successLabel.managedProperty().bind(successLabel.textProperty().isNotEmpty());
        successLabel.visibleProperty().bind(successLabel.textProperty().isNotEmpty());
        return successLabel;
    }
}
