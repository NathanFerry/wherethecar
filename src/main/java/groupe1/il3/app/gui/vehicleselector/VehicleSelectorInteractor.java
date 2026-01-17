package groupe1.il3.app.gui.vehicleselector;

import groupe1.il3.app.domain.vehicle.Vehicle;
import groupe1.il3.app.persistence.broker.vehicle.VehicleBroker;
import javafx.concurrent.Task;

import java.util.List;

public class VehicleSelectorInteractor {
    private final VehicleBroker vehicleBroker;

    public VehicleSelectorInteractor() {
        this.vehicleBroker = new VehicleBroker();
    }

    public Task<List<Vehicle>> createLoadVehiclesTask() {
        return new Task<>() {
            @Override
            protected List<Vehicle> call() {
                return vehicleBroker.getAllVehicles();
            }
        };
    }
}
