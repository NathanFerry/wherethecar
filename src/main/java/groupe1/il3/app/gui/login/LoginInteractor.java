package groupe1.il3.app.gui.login;

import groupe1.il3.app.domain.agent.Agent;
import groupe1.il3.app.domain.authentication.AuthResult;
import groupe1.il3.app.domain.authentication.PasswordHasher;
import groupe1.il3.app.persistence.broker.agent.AgentBroker;

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
     * Fetches and validates agent credentials.
     * This method does NOT update the model and is safe to call from a background thread.
     *
     * @return AuthResult containing the authenticated Agent or error message
     */
    public AuthResult fetchAndValidateCredentials() {
        // Get credentials from model
        String email = model.getEmail();
        String password = model.getPassword();

        // Validate input
        if (email == null || email.isBlank() || password == null || password.isBlank()) {
            return new AuthResult(null, "Email et mot de passe requis.");
        }

        // Get agent from database
        Agent agent = agentBroker.getAgentByEmail(email);

        if (agent == null) {
            return new AuthResult(null, "Email ou mot de passe incorrect.");
        }

        // Hash the provided password and compare with stored hash
        String hashedPassword = PasswordHasher.hashPassword(password);

        if (!hashedPassword.equals(agent.passwordHash())) {
            return new AuthResult(null, "Email ou mot de passe incorrect.");
        }

        return new AuthResult(agent, "");
    }

    /**
     * Updates the model with authentication results.
     * This method MUST be called from the JavaFX application thread.
     *
     * @param result the authentication result to apply to the model
     */
    public void updateModelWithResult(AuthResult result) {
        model.setErrorMessage(result.errorMessage());
        model.setLoginInProgress(false);
    }

    /**
     * Prepares the model for authentication attempt.
     * This method MUST be called from the JavaFX application thread.
     */
    public void prepareAuthentication() {
        model.setErrorMessage("");
        model.setLoginInProgress(true);
    }
}
