package groupe1.il3.app.gui.admin.agentmanagement;

import groupe1.il3.app.domain.agent.Agent;
import javafx.concurrent.Task;
import javafx.scene.layout.Region;
import javafx.util.Builder;

import java.util.List;
import java.util.UUID;
import java.util.function.BiConsumer;

public class AgentManagementController {

    private final AgentManagementModel model;
    private final AgentManagementInteractor interactor;
    private final Builder<Region> viewBuilder;
    private final BiConsumer<String, String> messageHandler;

    public AgentManagementController(BiConsumer<String, String> messageHandler) {
        this.model = new AgentManagementModel();
        this.interactor = new AgentManagementInteractor();
        this.messageHandler = messageHandler;
        this.viewBuilder = new AgentManagementViewBuilder(
            model,
            this::loadAgents,
            this::addAgent,
            this::editAgent,
            this::deleteAgent
        );
    }

    public Region getView() {
        return viewBuilder.build();
    }

    public void loadAgents() {
        messageHandler.accept("", "");

        Task<List<Agent>> task = interactor.createLoadAgentsTask();

        task.setOnSucceeded(event -> {
            model.agentsProperty().clear();
            model.agentsProperty().addAll(task.getValue());
        });

        task.setOnFailed(event -> {
            messageHandler.accept("Erreur lors du chargement des agents: " + task.getException().getMessage(), "");
            task.getException().printStackTrace();
        });

        new Thread(task).start();
    }

    private void addAgent(Agent agent) {
        messageHandler.accept("", "");

        Task<Void> task = interactor.createAgentTask(agent);

        task.setOnSucceeded(event -> {
            messageHandler.accept("", "Agent ajouté avec succès");
            loadAgents();
        });

        task.setOnFailed(event -> {
            messageHandler.accept("Erreur lors de l'ajout de l'agent: " + task.getException().getMessage(), "");
            task.getException().printStackTrace();
        });

        new Thread(task).start();
    }

    private void editAgent(Agent agent) {
        messageHandler.accept("", "");

        Task<Void> task = interactor.updateAgentTask(agent);

        task.setOnSucceeded(event -> {
            messageHandler.accept("", "Agent modifié avec succès");
            loadAgents();
        });

        task.setOnFailed(event -> {
            messageHandler.accept("Erreur lors de la modification de l'agent: " + task.getException().getMessage(), "");
            task.getException().printStackTrace();
        });

        new Thread(task).start();
    }

    private void deleteAgent(UUID agentUuid) {
        messageHandler.accept("", "");

        Task<Void> task = interactor.deleteAgentTask(agentUuid);

        task.setOnSucceeded(event -> {
            messageHandler.accept("", "Agent supprimé avec succès");
            loadAgents();
        });

        task.setOnFailed(event -> {
            messageHandler.accept("Erreur lors de la suppression de l'agent: " + task.getException().getMessage(), "");
            task.getException().printStackTrace();
        });

        new Thread(task).start();
    }
}

