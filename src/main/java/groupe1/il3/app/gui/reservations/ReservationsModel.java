package groupe1.il3.app.gui.reservations;

import groupe1.il3.app.domain.reservation.Reservation;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ReservationsModel {
    private final ObservableList<Reservation> reservations = FXCollections.observableArrayList();
    private final ObjectProperty<Reservation> selectedReservation = new SimpleObjectProperty<>();
    private final BooleanProperty loading = new SimpleBooleanProperty(false);

    public ObservableList<Reservation> getReservations() {
        return reservations;
    }

    public ObjectProperty<Reservation> selectedReservationProperty() {
        return selectedReservation;
    }

    public Reservation getSelectedReservation() {
        return selectedReservation.get();
    }

    public void setSelectedReservation(Reservation reservation) {
        selectedReservation.set(reservation);
    }

    public BooleanProperty loadingProperty() {
        return loading;
    }

    public boolean isLoading() {
        return loading.get();
    }

    public void setLoading(boolean loading) {
        this.loading.set(loading);
    }
}
