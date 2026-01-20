package groupe1.il3.app.gui.header;

import groupe1.il3.app.domain.agent.Agent;
import groupe1.il3.app.domain.authentication.PasswordHasher;
import groupe1.il3.app.persistence.broker.agent.AgentBroker;
import javafx.concurrent.Task;

public class HeaderInteractor {

    private final AgentBroker agentBroker;

    public HeaderInteractor() {
        this.agentBroker = new AgentBroker();
    }

    public Task<Agent> createUpdateUserTask(Agent currentAgent, String newEmail, String newPassword) {
        return new Task<>() {
            @Override
            protected Agent call() {
                String email = newEmail != null && !newEmail.trim().isEmpty()
                    ? newEmail.trim()
                    : currentAgent.email();

                String passwordHash = newPassword != null && !newPassword.trim().isEmpty()
                    ? PasswordHasher.hashPassword(newPassword)
                    : currentAgent.passwordHash();

                Agent updatedAgent = new Agent(
                    currentAgent.uuid(),
                    currentAgent.firstname(),
                    currentAgent.lastname(),
                    email,
                    passwordHash,
                    currentAgent.isAdmin()
                );

                agentBroker.updateAgent(updatedAgent);

                return updatedAgent;
            }
        };
    }
}
