package groupe1.il3.app.gui.admin.reservationmanagement;

import groupe1.il3.app.domain.reservation.Reservation;
import groupe1.il3.app.persistence.broker.reservation.ReservationBroker;
import javafx.concurrent.Task;

import java.util.List;
import java.util.UUID;

public class ReservationManagementInteractor {

    private final ReservationBroker reservationBroker;

    public ReservationManagementInteractor() {
        this.reservationBroker = new ReservationBroker();
    }

    public Task<List<Reservation>> createLoadPendingReservationsTask() {
        return new Task<>() {
            @Override
            protected List<Reservation> call() {
                return reservationBroker.getPendingReservations();
            }
        };
    }

    public Task<Void> approveReservationTask(UUID reservationUuid, UUID vehicleUuid) {
        return new Task<>() {
            @Override
            protected Void call() {
                reservationBroker.approveReservation(reservationUuid, vehicleUuid);
                return null;
            }
        };
    }

    public Task<Void> cancelReservationTask(UUID reservationUuid) {
        return new Task<>() {
            @Override
            protected Void call() {
                reservationBroker.cancelReservation(reservationUuid);
                return null;
            }
        };
    }
}

