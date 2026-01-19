package groupe1.il3.app.gui.admin;

import groupe1.il3.app.domain.agent.Agent;
import groupe1.il3.app.domain.reservation.Reservation;
import groupe1.il3.app.domain.vehicle.Vehicle;
import javafx.beans.property.*;
import javafx.collections.FXCollections;

public class AdminPanelModel {

    private final ListProperty<Vehicle> vehicles = new SimpleListProperty<>(FXCollections.observableArrayList());
    private final ObjectProperty<Vehicle> selectedVehicle = new SimpleObjectProperty<>();

    private final ListProperty<Agent> agents = new SimpleListProperty<>(FXCollections.observableArrayList());
    private final ObjectProperty<Agent> selectedAgent = new SimpleObjectProperty<>();

    private final ListProperty<Reservation> pendingReservations = new SimpleListProperty<>(FXCollections.observableArrayList());
    private final ObjectProperty<Reservation> selectedReservation = new SimpleObjectProperty<>();

    private final StringProperty errorMessage = new SimpleStringProperty("");
    private final StringProperty successMessage = new SimpleStringProperty("");

    public ListProperty<Vehicle> vehiclesProperty() {
        return vehicles;
    }

    public ObjectProperty<Vehicle> selectedVehicleProperty() {
        return selectedVehicle;
    }

    public Vehicle getSelectedVehicle() {
        return selectedVehicle.get();
    }

    public void setSelectedVehicle(Vehicle vehicle) {
        selectedVehicle.set(vehicle);
    }

    public ListProperty<Agent> agentsProperty() {
        return agents;
    }

    public ObjectProperty<Agent> selectedAgentProperty() {
        return selectedAgent;
    }

    public Agent getSelectedAgent() {
        return selectedAgent.get();
    }

    public void setSelectedAgent(Agent agent) {
        selectedAgent.set(agent);
    }

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

    public StringProperty errorMessageProperty() {
        return errorMessage;
    }

    public void setErrorMessage(String message) {
        errorMessage.set(message);
    }

    public StringProperty successMessageProperty() {
        return successMessage;
    }

    public void setSuccessMessage(String message) {
        successMessage.set(message);
    }
}
