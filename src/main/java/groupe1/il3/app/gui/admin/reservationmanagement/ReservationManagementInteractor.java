package groupe1.il3.app.gui.admin.reservationmanagement;

import groupe1.il3.app.domain.reservation.Reservation;
import groupe1.il3.app.persistence.broker.reservation.ReservationBroker;

import java.util.List;
import java.util.UUID;

public class ReservationManagementInteractor {

    private final ReservationManagementModel model;
    private final ReservationBroker reservationBroker;

    public ReservationManagementInteractor(ReservationManagementModel model) {
        this.model = model;
        this.reservationBroker = new ReservationBroker();
    }

    public List<Reservation> fetchPendingReservations() {
        return reservationBroker.getPendingReservations();
    }

    public void approveReservation(UUID reservationUuid, UUID vehicleUuid) {
        reservationBroker.approveReservation(reservationUuid, vehicleUuid);
    }

    public void cancelReservation(UUID reservationUuid) {
        reservationBroker.cancelReservation(reservationUuid);
    }

    public void updatePendingReservationsList(List<Reservation> reservations) {
        model.pendingReservationsProperty().clear();
        model.pendingReservationsProperty().addAll(reservations);
    }
}

