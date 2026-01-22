package groupe1.il3.app.gui.header;

import groupe1.il3.app.domain.agent.Agent;
import groupe1.il3.app.domain.authentication.PasswordHasher;
import groupe1.il3.app.persistence.broker.agent.AgentBroker;

public class HeaderInteractor {

    private final HeaderModel model;
    private final AgentBroker agentBroker;

    public HeaderInteractor(HeaderModel model) {
        this.model = model;
        this.agentBroker = new AgentBroker();
    }

    public void initializeUserInfo(Agent currentAgent) {
        if (currentAgent != null) {
            model.setUserFirstname(currentAgent.firstname());
            model.setUserLastname(currentAgent.lastname());
        }
    }

    public void prepareEditDialog(Agent currentAgent) {
        model.clearMessages();
        model.clearEditFields();
        if (currentAgent != null) {
            model.setEditEmail(currentAgent.email());
        }
    }

    public boolean validateUpdate(String email, String password, String passwordConfirm) {
        model.clearMessages();

        if (email.trim().isEmpty() && password.trim().isEmpty()) {
            model.setErrorMessage("Veuillez remplir au moins un champ.");
            return false;
        }

        if (!password.isEmpty() && !password.equals(passwordConfirm)) {
            model.setErrorMessage("Les mots de passe ne correspondent pas.");
            return false;
        }

        return true;
    }

    public Agent updateUser(Agent currentAgent, String email, String password) {
        model.setUpdateInProgress(true);

        try {
            String newEmail = email.trim().isEmpty() ? currentAgent.email() : email.trim();
            String passwordHash = password.trim().isEmpty()
                ? currentAgent.passwordHash()
                : PasswordHasher.hashPassword(password);

            Agent updatedAgent = new Agent(
                currentAgent.uuid(),
                currentAgent.firstname(),
                currentAgent.lastname(),
                newEmail,
                passwordHash,
                currentAgent.isAdmin()
            );

            agentBroker.updateAgent(updatedAgent);

            return updatedAgent;
        } finally {
            model.setUpdateInProgress(false);
        }
    }

    public void setErrorMessage(String message) {
        model.setErrorMessage(message);
    }

    public void handleUpdateError(Throwable exception) {
        model.setErrorMessage("Erreur lors de la mise à jour: " +
            (exception != null ? exception.getMessage() : "Erreur inconnue"));
    }

    public String getEditEmail() {
        return model.getEditEmail();
    }

    public String getEditPassword() {
        return model.getEditPassword();
    }

    public String getEditPasswordConfirm() {
        return model.getEditPasswordConfirm();
    }
}
