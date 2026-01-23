package groupe1.il3.app.gui.login;

import groupe1.il3.app.domain.agent.Agent;
import groupe1.il3.app.domain.authentication.AuthResult;
import groupe1.il3.app.domain.authentication.PasswordHasher;
import groupe1.il3.app.persistence.broker.agent.AgentBroker;

public class LoginInteractor {

    private final LoginModel model;
    private final AgentBroker agentBroker;

    public LoginInteractor(LoginModel model) {
        this.model = model;
        this.agentBroker = new AgentBroker();
    }

    public AuthResult fetchAndValidateCredentials() {
        String email = model.getEmail();
        String password = model.getPassword();

        if (email == null || email.isBlank() || password == null || password.isBlank()) {
            return new AuthResult(null, "Email et mot de passe requis.");
        }

        Agent agent = agentBroker.getAgentByEmail(email);

        if (agent == null) {
            return new AuthResult(null, "Email ou mot de passe incorrect.");
        }

        String hashedPassword = PasswordHasher.hashPassword(password);

        if (!hashedPassword.equals(agent.passwordHash())) {
            return new AuthResult(null, "Email ou mot de passe incorrect.");
        }

        return new AuthResult(agent, "");
    }

    public void updateModelWithResult(AuthResult result) {
        model.setErrorMessage(result.errorMessage());
        model.setLoginInProgress(false);
    }

    public void prepareAuthentication() {
        model.setErrorMessage("");
        model.setLoginInProgress(true);
    }
}
