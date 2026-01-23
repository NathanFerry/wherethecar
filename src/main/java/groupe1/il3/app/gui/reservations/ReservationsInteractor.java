package groupe1.il3.app.gui.reservations;

import groupe1.il3.app.domain.reservation.Reservation;
import groupe1.il3.app.persistence.broker.reservation.ReservationBroker;

import java.util.List;
import java.util.UUID;

public class ReservationsInteractor {
    private final ReservationsModel model;
    private final ReservationBroker reservationBroker;

    public ReservationsInteractor(ReservationsModel model) {
        this.model = model;
        this.reservationBroker = new ReservationBroker();
    }

    public List<Reservation> loadReservations(UUID agentUuid) {
        return reservationBroker.getReservationsByAgentUuid(agentUuid);
    }

    public void returnVehicle(UUID reservationUuid, UUID vehicleUuid, int newKilometers) {
        reservationBroker.returnVehicle(reservationUuid, vehicleUuid, newKilometers);
    }

    public void setLoading(boolean loading) {
        model.setLoading(loading);
    }

    public void updateModelWithReservations(List<Reservation> reservations) {
        model.setLoading(false);
        model.reservationsProperty().clear();
        model.reservationsProperty().addAll(reservations);
    }

    public boolean validateReturn() {
        model.setReturnErrorMessage("");

        Reservation selectedReservation = model.getSelectedReservation();
        if (selectedReservation == null) {
            model.setReturnErrorMessage("Aucune réservation sélectionnée");
            return false;
        }

        if (selectedReservation.vehicle() == null) {
            model.setReturnErrorMessage("Véhicule non disponible");
            return false;
        }

        int currentKilometers = selectedReservation.vehicle().kilometers();
        int newKilometers = model.getNewKilometers();

        if (newKilometers < currentKilometers) {
            model.setReturnErrorMessage("Le nouveau kilométrage doit être supérieur ou égal au kilométrage actuel (" + currentKilometers + " km)");
            return false;
        }

        return true;
    }

    public Reservation getSelectedReservation() {
        return model.getSelectedReservation();
    }

    public int getNewKilometers() {
        return model.getNewKilometers();
    }

    public void clearReturnError() {
        model.setReturnErrorMessage("");
    }

    public void setReturnError(String message) {
        model.setReturnErrorMessage(message);
    }
}
