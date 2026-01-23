package groupe1.il3.app.gui.admin.agentmanagement;

import groupe1.il3.app.domain.agent.Agent;
import groupe1.il3.app.persistence.broker.agent.AgentBroker;

import java.util.List;
import java.util.UUID;

public class AgentManagementInteractor {

    private final AgentManagementModel model;
    private final AgentBroker agentBroker;

    public AgentManagementInteractor(AgentManagementModel model) {
        this.model = model;
        this.agentBroker = new AgentBroker();
    }

    public List<Agent> fetchAllAgents() {
        return agentBroker.getAllAgents();
    }

    public void createAgent(Agent agent) {
        agentBroker.createAgent(agent);
    }

    public void updateAgent(Agent agent) {
        agentBroker.updateAgent(agent);
    }

    public void deleteAgent(UUID agentUuid) {
        agentBroker.deleteAgent(agentUuid);
    }

    public void updateAgentsList(List<Agent> agents) {
        model.agentsProperty().clear();
        model.agentsProperty().addAll(agents);
    }
}

