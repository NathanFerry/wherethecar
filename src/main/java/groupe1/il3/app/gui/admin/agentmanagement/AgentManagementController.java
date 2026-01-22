package groupe1.il3.app.gui.admin.agentmanagement;

import groupe1.il3.app.domain.agent.Agent;
import javafx.concurrent.Task;
import javafx.scene.layout.Region;
import javafx.util.Builder;

import java.util.List;
import java.util.UUID;
import java.util.function.BiConsumer;

public class AgentManagementController {

    private final AgentManagementInteractor interactor;
    private final Builder<Region> viewBuilder;
    private final BiConsumer<String, String> messageHandler;

    public AgentManagementController(BiConsumer<String, String> messageHandler) {
        AgentManagementModel model = new AgentManagementModel();
        this.interactor = new AgentManagementInteractor(model);
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

        Task<List<Agent>> task = new Task<>() {
            @Override
            protected List<Agent> call() {
                return interactor.fetchAllAgents();
            }
        };

        task.setOnSucceeded(event -> {
            interactor.updateAgentsList(task.getValue());
        });

        task.setOnFailed(event -> {
            messageHandler.accept("Erreur lors du chargement des agents: " + task.getException().getMessage(), "");
            task.getException().printStackTrace();
        });

        new Thread(task).start();
    }

    private void addAgent(Agent agent) {
        messageHandler.accept("", "");

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                interactor.createAgent(agent);
                return null;
            }
        };

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

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                interactor.updateAgent(agent);
                return null;
            }
        };

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

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                interactor.deleteAgent(agentUuid);
                return null;
            }
        };

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

