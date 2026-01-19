package groupe1.il3.app.gui.history;

import groupe1.il3.app.domain.reservation.Reservation;
import groupe1.il3.app.persistence.broker.reservation.ReservationBroker;
import javafx.concurrent.Task;

import java.util.List;
import java.util.UUID;

public class HistoryInteractor {
    private final ReservationBroker reservationBroker;

    public HistoryInteractor() {
        this.reservationBroker = new ReservationBroker();
    }

    public Task<List<Reservation>> createLoadHistoryTask(UUID agentUuid) {
        return new Task<>() {
            @Override
            protected List<Reservation> call() {
                return reservationBroker.getHistoricalReservationsByAgentUuid(agentUuid);
            }
        };
    }
}

