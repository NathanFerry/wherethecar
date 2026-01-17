package groupe1.il3.app.gui.mainframe;

import groupe1.il3.app.gui.vehicleselector.VehicleSelectorController;
import javafx.scene.layout.Region;
import javafx.util.Builder;

public class MainFrameController {

    private final Builder<Region> viewBuilder;
    private final MainFrameModel model;

    public MainFrameController() {
        this.model = new MainFrameModel();
        this.viewBuilder = new MainFrameViewBuilder(this.model, this::showVehicleList, this::showReservations);
    }

    public Region getView() {
        return this.viewBuilder.build();
    }

    public void showVehicleList() {
        VehicleSelectorController vehicleSelectorController = new VehicleSelectorController();
        model.setCenterContent(vehicleSelectorController.getView());
    }

    public void showReservations() {
        // TODO: Implement reservations view
        javafx.scene.control.Label placeholder = new javafx.scene.control.Label("Mes réservations - À implémenter");
        model.setCenterContent(placeholder);
    }
}

