package groupe1.il3.app.gui.vehicleselector;

import groupe1.il3.app.domain.reservation.Reservation;
import groupe1.il3.app.domain.vehicle.Vehicle;
import groupe1.il3.app.persistence.broker.reservation.ReservationBroker;
import groupe1.il3.app.persistence.broker.vehicle.VehicleBroker;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class VehicleSelectorInteractor {
    private final VehicleSelectorModel model;
    private final VehicleBroker vehicleBroker;
    private final ReservationBroker reservationBroker;

    public VehicleSelectorInteractor(VehicleSelectorModel model) {
        this.model = model;
        this.vehicleBroker = new VehicleBroker();
        this.reservationBroker = new ReservationBroker();
    }

    public List<Vehicle> loadVehicles() {
        return vehicleBroker.getAllVehicles();
    }

    public List<Reservation> loadVehicleReservations(UUID vehicleUuid) {
        return reservationBroker.getReservationsByVehicleUuid(vehicleUuid);
    }

    public boolean createReservation(UUID agentUuid, UUID vehicleUuid, LocalDateTime startDate, LocalDateTime endDate) {
        if (reservationBroker.hasOverlappingReservation(vehicleUuid, startDate, endDate)) {
            return false;
        }
        reservationBroker.createReservation(agentUuid, vehicleUuid, startDate, endDate);
        return true;
    }

    public void updateModelWithVehicles(List<Vehicle> vehicles) {
        model.vehiclesProperty().clear();
        model.vehiclesProperty().addAll(vehicles);
    }

    public void updateModelWithReservations(List<Reservation> reservations) {
        model.selectedVehicleReservationsProperty().clear();
        model.selectedVehicleReservationsProperty().addAll(reservations);
    }

    public void clearReservations() {
        model.selectedVehicleReservationsProperty().clear();
    }

    public boolean validateReservation() {
        model.setReservationErrorMessage("");
        model.setReservationSuccessful(false);

        if (model.getSelectedVehicle() == null) {
            model.setReservationErrorMessage("Veuillez sélectionner un véhicule");
            return false;
        }

        if (model.getReservationStartDate() == null || model.getReservationEndDate() == null) {
            model.setReservationErrorMessage("Veuillez sélectionner les dates de début et de fin");
            return false;
        }

        if (model.getReservationEndDate().isBefore(model.getReservationStartDate())) {
            model.setReservationErrorMessage("La date de fin doit être après la date de début");
            return false;
        }

        return true;
    }

    public void setReservationSuccess() {
        model.setReservationErrorMessage("");
        model.setReservationSuccessful(true);
    }

    public void setReservationError(String message) {
        model.setReservationErrorMessage(message);
        model.setReservationSuccessful(false);
    }

    public boolean hasSelectedVehicle() {
        return model.getSelectedVehicle() != null;
    }

    public UUID getSelectedVehicleUuid() {
        return model.getSelectedVehicle().uuid();
    }

    public LocalDateTime getReservationStartDate() {
        return model.getReservationStartDate();
    }

    public LocalDateTime getReservationEndDate() {
        return model.getReservationEndDate();
    }
}
