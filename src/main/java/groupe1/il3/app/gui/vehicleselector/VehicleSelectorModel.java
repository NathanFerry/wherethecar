package groupe1.il3.app.gui.vehicleselector;

import groupe1.il3.app.domain.vehicle.Vehicle;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class VehicleSelectorModel {
    private final ObservableList<Vehicle> vehicles = FXCollections.observableArrayList();
    private final ObjectProperty<Vehicle> selectedVehicle = new SimpleObjectProperty<>();

    public ObservableList<Vehicle> getVehicles() {
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
