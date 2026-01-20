package groupe1.il3.app.gui.history;

import groupe1.il3.app.domain.reservation.Reservation;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;

public class HistoryModel {

    private final ListProperty<Reservation> reservations = new SimpleListProperty<>(FXCollections.observableArrayList());
    private final ObjectProperty<Reservation> selectedReservation = new SimpleObjectProperty<>();
    private final BooleanProperty loading = new SimpleBooleanProperty(false);

    public ListProperty<Reservation> reservationsProperty() {
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

