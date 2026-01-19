package groupe1.il3.app.gui.admin.agentmanagement;
import groupe1.il3.app.domain.agent.Agent;
import javafx.beans.property.*;
import javafx.collections.FXCollections;

public class AgentManagementModel {

    private final ListProperty<Agent> agents = new SimpleListProperty<>(FXCollections.observableArrayList());
    private final ObjectProperty<Agent> selectedAgent = new SimpleObjectProperty<>();

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
}
