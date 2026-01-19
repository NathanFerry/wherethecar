package groupe1.il3.app.gui.vehicleselector;

import groupe1.il3.app.domain.vehicle.Vehicle;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;

import java.time.LocalDateTime;

public class VehicleSelectorModel {
    private final ListProperty<Vehicle> vehicles = new SimpleListProperty<>(FXCollections.observableArrayList());
    private final ObjectProperty<Vehicle> selectedVehicle = new SimpleObjectProperty<>();
    private final ObjectProperty<LocalDateTime> reservationStartDate = new SimpleObjectProperty<>();
    private final ObjectProperty<LocalDateTime> reservationEndDate = new SimpleObjectProperty<>();
    private final StringProperty reservationErrorMessage = new SimpleStringProperty("");

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

    public ObjectProperty<LocalDateTime> reservationStartDateProperty() {
        return reservationStartDate;
    }

    public LocalDateTime getReservationStartDate() {
        return reservationStartDate.get();
    }

    public void setReservationStartDate(LocalDateTime startDate) {
        reservationStartDate.set(startDate);
    }

    public ObjectProperty<LocalDateTime> reservationEndDateProperty() {
        return reservationEndDate;
    }

    public LocalDateTime getReservationEndDate() {
        return reservationEndDate.get();
    }

    public void setReservationEndDate(LocalDateTime endDate) {
        reservationEndDate.set(endDate);
    }

    public StringProperty reservationErrorMessageProperty() {
        return reservationErrorMessage;
    }

    public String getReservationErrorMessage() {
        return reservationErrorMessage.get();
    }

    public void setReservationErrorMessage(String message) {
        reservationErrorMessage.set(message);
    }
}
