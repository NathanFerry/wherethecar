package groupe1.il3.app.gui.admin.reservationmanagement;
import groupe1.il3.app.domain.reservation.Reservation;
import javafx.beans.property.*;
import javafx.collections.FXCollections;

public class ReservationManagementModel {

    private final ListProperty<Reservation> pendingReservations = new SimpleListProperty<>(FXCollections.observableArrayList());
    private final ObjectProperty<Reservation> selectedReservation = new SimpleObjectProperty<>();

    public ListProperty<Reservation> pendingReservationsProperty() {
        return pendingReservations;
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
}
