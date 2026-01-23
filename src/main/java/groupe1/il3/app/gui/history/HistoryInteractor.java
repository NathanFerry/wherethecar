package groupe1.il3.app.gui.history;

import groupe1.il3.app.domain.reservation.Reservation;
import groupe1.il3.app.persistence.broker.reservation.ReservationBroker;

import java.util.List;
import java.util.UUID;

public class HistoryInteractor {
    private final HistoryModel model;
    private final ReservationBroker reservationBroker;

    public HistoryInteractor(HistoryModel model) {
        this.model = model;
        this.reservationBroker = new ReservationBroker();
    }

    public List<Reservation> loadHistory(UUID agentUuid) {
        model.setLoading(true);

        try {
            return reservationBroker.getHistoricalReservationsByAgentUuid(agentUuid);
        } finally {
            model.setLoading(false);
        }
    }

    public void updateModelWithReservations(List<Reservation> reservations) {
        model.reservationsProperty().clear();
        model.reservationsProperty().addAll(reservations);
    }
}

