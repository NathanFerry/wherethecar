package groupe1.il3.app.gui.vehicleselector;

import groupe1.il3.app.domain.reservation.Reservation;
import groupe1.il3.app.domain.vehicle.Vehicle;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;

import java.time.LocalDateTime;

public class VehicleSelectorModel {
    private final ListProperty<Vehicle> vehicles = new SimpleListProperty<>(FXCollections.observableArrayList());
    private final FilteredList<Vehicle> filteredVehicles;
    private final StringProperty searchText = new SimpleStringProperty("");
    private final ObjectProperty<Vehicle> selectedVehicle = new SimpleObjectProperty<>();
    private final ListProperty<Reservation> selectedVehicleReservations = new SimpleListProperty<>(FXCollections.observableArrayList());
    private final ObjectProperty<LocalDateTime> reservationStartDate = new SimpleObjectProperty<>();
    private final ObjectProperty<LocalDateTime> reservationEndDate = new SimpleObjectProperty<>();
    private final StringProperty reservationErrorMessage = new SimpleStringProperty("");
    private final BooleanProperty reservationSuccessful = new SimpleBooleanProperty(false);

    public VehicleSelectorModel() {
        filteredVehicles = new FilteredList<>(vehicles, p -> true);

        searchText.addListener((observable, oldValue, newValue) -> {
            filteredVehicles.setPredicate(vehicle -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();
                String vehicleName = (vehicle.manufacturer() + " " + vehicle.model()).toLowerCase();

                return vehicleName.contains(lowerCaseFilter);
            });
        });
    }

    public ListProperty<Vehicle> vehiclesProperty() {
        return vehicles;
    }

    public FilteredList<Vehicle> getFilteredVehicles() {
        return filteredVehicles;
    }

    public StringProperty searchTextProperty() {
        return searchText;
    }

    public String getSearchText() {
        return searchText.get();
    }

    public void setSearchText(String text) {
        searchText.set(text);
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

    public ListProperty<Reservation> selectedVehicleReservationsProperty() {
        return selectedVehicleReservations;
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

    public BooleanProperty reservationSuccessfulProperty() {
        return reservationSuccessful;
    }

    public boolean isReservationSuccessful() {
        return reservationSuccessful.get();
    }

    public void setReservationSuccessful(boolean successful) {
        reservationSuccessful.set(successful);
    }
}
