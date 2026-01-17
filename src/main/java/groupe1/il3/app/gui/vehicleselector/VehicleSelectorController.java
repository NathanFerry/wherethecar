package groupe1.il3.app.gui.vehicleselector;

import groupe1.il3.app.domain.vehicle.Vehicle;
import javafx.concurrent.Task;
import javafx.scene.layout.Region;
import javafx.util.Builder;

import java.util.List;

public class VehicleSelectorController {
    private final VehicleSelectorModel model;
    private final VehicleSelectorInteractor interactor;
    private final Builder<Region> viewBuilder;

    public VehicleSelectorController() {
        this.model = new VehicleSelectorModel();
        this.interactor = new VehicleSelectorInteractor();
        this.viewBuilder = new VehicleSelectorViewBuilder(model, this::loadVehicles);
    }

    public Region getView() {
        return viewBuilder.build();
    }

    private void loadVehicles() {
        Task<List<Vehicle>> task = interactor.createLoadVehiclesTask();

        task.setOnSucceeded(event -> {
            model.getVehicles().clear();
            model.getVehicles().addAll(task.getValue());
        });

        task.setOnFailed(event -> {
            System.err.println("Failed to load vehicles: " + task.getException().getMessage());
            task.getException().printStackTrace();
        });

        new Thread(task).start();
    }
}
