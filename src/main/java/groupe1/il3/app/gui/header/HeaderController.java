package groupe1.il3.app.gui.header;

import groupe1.il3.app.domain.agent.Agent;
import groupe1.il3.app.domain.authentication.SessionManager;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.layout.Region;
import javafx.util.Builder;

public class HeaderController {

    private final HeaderModel model;
    private final HeaderInteractor interactor;
    private final Builder<Region> viewBuilder;
    private final Runnable onLogout;
    private Dialog<ButtonType> currentDialog;

    public HeaderController(Runnable onLogout) {
        this.model = new HeaderModel();
        this.interactor = new HeaderInteractor();
        this.onLogout = onLogout;
        this.viewBuilder = new HeaderViewBuilder(model, this::showEditDialog, this::handleLogout);

        initializeUserInfo();
    }

    public Region getView() {
        return viewBuilder.build();
    }

    private void initializeUserInfo() {
        Agent currentAgent = SessionManager.getInstance().getCurrentAgent();
        if (currentAgent != null) {
            model.setUserFirstname(currentAgent.getFirstname());
            model.setUserLastname(currentAgent.getLastname());
        }
    }

    private void showEditDialog() {
        Agent currentAgent = SessionManager.getInstance().getCurrentAgent();
        if (currentAgent == null) {
            return;
        }

        model.clearMessages();
        model.clearEditFields();
        model.setEditEmail(currentAgent.getEmail());

        HeaderViewBuilder headerViewBuilder = (HeaderViewBuilder) viewBuilder;
        currentDialog = headerViewBuilder.buildEditDialog();

        currentDialog.setResultConverter(buttonType -> {
            if (buttonType.getButtonData() == ButtonType.OK.getButtonData()) {
                handleUpdate(currentAgent);
                return null;
            }
            return buttonType;
        });

        currentDialog.showAndWait();
    }

    private void handleUpdate(Agent currentAgent) {
        model.clearMessages();

        String email = model.getEditEmail();
        String password = model.getEditPassword();
        String passwordConfirm = model.getEditPasswordConfirm();

        if (email.trim().isEmpty() && password.trim().isEmpty()) {
            model.setErrorMessage("Veuillez remplir au moins un champ.");
            Platform.runLater(this::showEditDialog);
            return;
        }

        if (!password.isEmpty() && !password.equals(passwordConfirm)) {
            model.setErrorMessage("Les mots de passe ne correspondent pas.");
            Platform.runLater(this::showEditDialog);
            return;
        }

        model.setUpdateInProgress(true);

        String newEmail = email.trim().isEmpty() ? null : email.trim();
        String newPassword = password.trim().isEmpty() ? null : password.trim();

        Task<Agent> updateTask = interactor.createUpdateUserTask(currentAgent, newEmail, newPassword);

        updateTask.setOnSucceeded(event -> {
            model.setUpdateInProgress(false);
            Agent updatedAgent = updateTask.getValue();
            SessionManager.getInstance().setCurrentAgent(updatedAgent);
            initializeUserInfo();
        });

        updateTask.setOnFailed(event -> {
            model.setUpdateInProgress(false);
            Throwable exception = updateTask.getException();
            model.setErrorMessage("Erreur lors de la mise à jour: " +
                (exception != null ? exception.getMessage() : "Erreur inconnue"));
            Platform.runLater(this::showEditDialog);
        });

        new Thread(updateTask).start();
    }

    private void handleLogout() {
        if (onLogout != null) {
            onLogout.run();
        }
    }
}
