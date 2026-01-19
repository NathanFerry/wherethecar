package groupe1.il3.app.gui.admin;

import groupe1.il3.app.gui.admin.agentmanagement.AgentManagementController;
import groupe1.il3.app.gui.admin.reservationmanagement.ReservationManagementController;
import groupe1.il3.app.gui.admin.vehiclemanagement.VehicleManagementController;
import javafx.scene.layout.Region;
import javafx.util.Builder;

public class AdminPanelController {

    private final AdminPanelModel model;
    private final AdminPanelInteractor interactor;
    private final VehicleManagementController vehicleManagementController;
    private final AgentManagementController agentManagementController;
    private final ReservationManagementController reservationManagementController;
    private final Builder<Region> viewBuilder;

    public AdminPanelController() {
        this.model = new AdminPanelModel();
        this.interactor = new AdminPanelInteractor();

        this.vehicleManagementController = new VehicleManagementController(this::handleMessages);
        this.agentManagementController = new AgentManagementController(this::handleMessages);
        this.reservationManagementController = new ReservationManagementController(this::handleMessages);

        this.viewBuilder = new AdminPanelViewBuilder(
            model,
            vehicleManagementController,
            agentManagementController,
            reservationManagementController
        );
    }

    public Region getView() {
        return viewBuilder.build();
    }

    private void handleMessages(String errorMessage, String successMessage) {
        model.setErrorMessage(errorMessage);
        model.setSuccessMessage(successMessage);
    }
}
