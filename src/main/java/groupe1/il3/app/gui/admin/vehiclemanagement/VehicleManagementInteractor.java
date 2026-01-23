package groupe1.il3.app.gui.admin.vehiclemanagement;

import groupe1.il3.app.domain.vehicle.Vehicle;
import groupe1.il3.app.persistence.broker.vehicle.VehicleBroker;

import java.util.List;
import java.util.UUID;

public class VehicleManagementInteractor {

    private final VehicleManagementModel model;
    private final VehicleBroker vehicleBroker;

    public VehicleManagementInteractor(VehicleManagementModel model) {
        this.model = model;
        this.vehicleBroker = new VehicleBroker();
    }

    public List<Vehicle> fetchAllVehicles() {
        return vehicleBroker.getAllVehicles();
    }

    public void createVehicle(Vehicle vehicle) {
        vehicleBroker.createVehicle(vehicle);
    }

    public void updateVehicle(Vehicle vehicle) {
        vehicleBroker.updateVehicle(vehicle);
    }

    public void deleteVehicle(UUID vehicleUuid) {
        vehicleBroker.deleteVehicle(vehicleUuid);
    }

    public void updateVehiclesList(List<Vehicle> vehicles) {
        model.vehiclesProperty().clear();
        model.vehiclesProperty().addAll(vehicles);
    }
}

