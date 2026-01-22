package groupe1.il3.app.gui.login;

import groupe1.il3.app.domain.agent.Agent;
import groupe1.il3.app.domain.authentication.PasswordHasher;
import groupe1.il3.app.persistence.broker.agent.AgentBroker;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

/**
 * Interactor for login operations.
 * Handles the business logic for authentication.
 * Updates the model based on data from the broker.
 */
public class LoginInteractor {

    private final LoginModel model;
    private final AgentBroker agentBroker;

    public LoginInteractor(LoginModel model) {
        this.model = model;
        this.agentBroker = new AgentBroker();
    }

    /**
     * Authenticates an agent using credentials from the model.
     * Updates the model with error messages and login state.
     *
     * @return the authenticated Agent or null if authentication fails
     */
    public Agent authenticate() {
        // Clear previous errors and set login in progress
        model.setErrorMessage("");
        model.setLoginInProgress(true);

        try {
            // Get credentials from model
            String email = model.getEmail();
            String password = model.getPassword();

            // Validate input
            if (email == null || email.isBlank() || password == null || password.isBlank()) {
                model.setErrorMessage("Email et mot de passe requis.");
                return null;
            }

            // Get agent from database
            Agent agent = agentBroker.getAgentByEmail(email);

            if (agent == null) {
                model.setErrorMessage("Email ou mot de passe incorrect.");
                return null;
            }

            // Hash the provided password and compare with stored hash
            String hashedPassword = PasswordHasher.hashPassword(password);

            if (!hashedPassword.equals(agent.passwordHash())) {
                model.setErrorMessage("Email ou mot de passe incorrect.");
                return null;
            }

            return agent;
        } finally {
            // Always reset login in progress state
            model.setLoginInProgress(false);
        }
    }
}
