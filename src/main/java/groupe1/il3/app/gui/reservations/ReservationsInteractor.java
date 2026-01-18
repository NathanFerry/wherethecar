package groupe1.il3.app.gui.reservations;

import groupe1.il3.app.domain.reservation.Reservation;
import groupe1.il3.app.persistence.broker.reservation.ReservationBroker;
import javafx.concurrent.Task;

import java.util.List;
import java.util.UUID;

public class ReservationsInteractor {
    private final ReservationBroker reservationBroker;

    public ReservationsInteractor() {
        this.reservationBroker = new ReservationBroker();
    }

    public Task<List<Reservation>> createLoadReservationsTask(UUID agentUuid) {
        return new Task<>() {
            @Override
            protected List<Reservation> call() {
                return reservationBroker.getReservationsByAgentUuid(agentUuid);
            }
        };
    }

    public Task<Void> createReturnVehicleTask(UUID reservationUuid, UUID vehicleUuid, int newKilometers) {
        return new Task<>() {
            @Override
            protected Void call() {
                reservationBroker.returnVehicle(reservationUuid, vehicleUuid, newKilometers);
                return null;
            }
        };
    }
}
