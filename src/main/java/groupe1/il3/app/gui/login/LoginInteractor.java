package groupe1.il3.app.gui.login;

import groupe1.il3.app.domain.agent.Agent;
import groupe1.il3.app.persistence.broker.agent.AgentBroker;
import javafx.concurrent.Task;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

/**
 * Interactor for login operations.
 * Handles the business logic for authentication using background tasks.
 */
public class LoginInteractor {

    private final AgentBroker agentBroker;

    public LoginInteractor() {
        this.agentBroker = new AgentBroker();
    }

    /**
     * Creates a task to authenticate an agent with email and password.
     *
     * @param email the agent's email
     * @param password the agent's plain text password
     * @return a Task that returns the authenticated Agent or null if authentication fails
     */
    public Task<Agent> createAuthenticationTask(String email, String password) {
        return new Task<>() {
            @Override
            protected Agent call() {
                // Validate input
                if (email == null || email.isBlank() || password == null || password.isBlank()) {
                    return null;
                }

                // Get agent from database
                Agent agent = agentBroker.getAgentByEmail(email);

                if (agent == null) {
                    return null;
                }

                // Hash the provided password and compare with stored hash
                String hashedPassword = hashPassword(password);

                if (!hashedPassword.equals(agent.passwordHash())) {
                    return null;
                }

                return agent;
            }
        };
    }

    /**
     * Hashes a password using SHA-256.
     *
     * @param password the plain text password
     * @return the hashed password in hexadecimal format
     */
    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not available", e);
        }
    }
}
