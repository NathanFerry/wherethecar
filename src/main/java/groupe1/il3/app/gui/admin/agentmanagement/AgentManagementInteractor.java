package groupe1.il3.app.gui.admin.agentmanagement;

import groupe1.il3.app.domain.agent.Agent;
import groupe1.il3.app.persistence.broker.agent.AgentBroker;
import javafx.concurrent.Task;

import java.util.List;
import java.util.UUID;

public class AgentManagementInteractor {

    private final AgentBroker agentBroker;

    public AgentManagementInteractor() {
        this.agentBroker = new AgentBroker();
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

