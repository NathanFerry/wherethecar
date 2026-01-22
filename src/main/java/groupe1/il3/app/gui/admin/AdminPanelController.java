package groupe1.il3.app.gui.admin;

import groupe1.il3.app.gui.admin.agentmanagement.AgentManagementController;
import groupe1.il3.app.gui.admin.reservationmanagement.ReservationManagementController;
import groupe1.il3.app.gui.admin.vehiclemanagement.VehicleManagementController;
import javafx.scene.layout.Region;
import javafx.util.Builder;

public class AdminPanelController {

    private final AdminPanelInteractor interactor;
    private final Builder<Region> viewBuilder;

    public AdminPanelController() {
        AdminPanelModel model = new AdminPanelModel();
        this.interactor = new AdminPanelInteractor(model);

        VehicleManagementController vehicleManagementController = new VehicleManagementController(this::handleMessages);
        AgentManagementController agentManagementController = new AgentManagementController(this::handleMessages);
        ReservationManagementController reservationManagementController = new ReservationManagementController(this::handleMessages);

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
        interactor.updateMessages(errorMessage, successMessage);
    }
}
