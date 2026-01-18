package groupe1.il3.app.gui.admin;

import groupe1.il3.app.domain.agent.Agent;
import groupe1.il3.app.domain.vehicle.Vehicle;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class AdminPanelModel {

    private final ObservableList<Vehicle> vehicles = FXCollections.observableArrayList();
    private final ObjectProperty<Vehicle> selectedVehicle = new SimpleObjectProperty<>();

    private final ObservableList<Agent> agents = FXCollections.observableArrayList();
    private final ObjectProperty<Agent> selectedAgent = new SimpleObjectProperty<>();

    private final StringProperty errorMessage = new SimpleStringProperty("");
    private final StringProperty successMessage = new SimpleStringProperty("");

    public ObservableList<Vehicle> vehiclesProperty() {
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

    public ObservableList<Agent> getAgents() {
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
