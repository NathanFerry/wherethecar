package groupe1.il3.app.gui.admin.vehiclemanagement;
import groupe1.il3.app.domain.vehicle.Vehicle;
import javafx.beans.property.*;
import javafx.collections.FXCollections;

public class VehicleManagementModel {

    private final ListProperty<Vehicle> vehicles = new SimpleListProperty<>(FXCollections.observableArrayList());
    private final ObjectProperty<Vehicle> selectedVehicle = new SimpleObjectProperty<>();

    public ListProperty<Vehicle> vehiclesProperty() {
        return vehicles;
    }

    public ObjectProperty<Vehicle> selectedVehicleProperty() {
        return selectedVehicle;
    }

    public Vehicle getSelectedVehicle() {
        return selectedVehicle.get();
    }

    public void setSelectedVehicle(Vehicle vehicle) {
        selectedVehicle.set(vehicle);
    }
}
