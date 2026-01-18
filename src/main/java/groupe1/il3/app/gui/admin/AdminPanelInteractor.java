package groupe1.il3.app.gui.admin;

import groupe1.il3.app.domain.agent.Agent;
import groupe1.il3.app.domain.vehicle.Vehicle;
import groupe1.il3.app.persistence.broker.agent.AgentBroker;
import groupe1.il3.app.persistence.broker.vehicle.VehicleBroker;
import javafx.concurrent.Task;

import java.util.List;
import java.util.UUID;

public class AdminPanelInteractor {

    private final VehicleBroker vehicleBroker;
    private final AgentBroker agentBroker;

    public AdminPanelInteractor() {
        this.vehicleBroker = new VehicleBroker();
        this.agentBroker = new AgentBroker();
    }

    public Task<List<Vehicle>> createLoadVehiclesTask() {
        return new Task<>() {
            @Override
            protected List<Vehicle> call() {
                return vehicleBroker.getAllVehicles();
            }
        };
    }

    public Task<Void> createVehicleTask(Vehicle vehicle) {
        return new Task<>() {
            @Override
            protected Void call() {
                vehicleBroker.createVehicle(vehicle);
                return null;
            }
        };
    }

    public Task<Void> updateVehicleTask(Vehicle vehicle) {
        return new Task<>() {
            @Override
            protected Void call() {
                vehicleBroker.updateVehicle(vehicle);
                return null;
            }
        };
    }

    public Task<Void> deleteVehicleTask(UUID vehicleUuid) {
        return new Task<>() {
            @Override
            protected Void call() {
                vehicleBroker.deleteVehicle(vehicleUuid);
                return null;
            }
        };
    }

    public Task<List<Agent>> createLoadAgentsTask() {
        return new Task<>() {
            @Override
            protected List<Agent> call() {
                return agentBroker.getAllAgents();
            }
        };
    }

    public Task<Void> createAgentTask(Agent agent) {
        return new Task<>() {
            @Override
            protected Void call() {
                agentBroker.createAgent(agent);
                return null;
            }
        };
    }

    public Task<Void> updateAgentTask(Agent agent) {
        return new Task<>() {
            @Override
            protected Void call() {
                agentBroker.updateAgent(agent);
                return null;
            }
        };
    }

    public Task<Void> deleteAgentTask(UUID agentUuid) {
        return new Task<>() {
            @Override
            protected Void call() {
                agentBroker.deleteAgent(agentUuid);
                return null;
            }
        };
    }
}
