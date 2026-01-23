package groupe1.il3.app.gui.header;

import groupe1.il3.app.domain.agent.Agent;
import groupe1.il3.app.domain.authentication.SessionManager;
import javafx.concurrent.Task;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.layout.Region;
import javafx.util.Builder;

public class HeaderController {

    private final HeaderInteractor interactor;
    private final Builder<Region> viewBuilder;
    private final Runnable onLogout;

    public HeaderController(Runnable onLogout) {
        this.onLogout = onLogout;
        HeaderModel model = new HeaderModel();
        this.interactor = new HeaderInteractor(model);
        this.viewBuilder = new HeaderViewBuilder(model, this::showEditDialog, this::handleLogout);

        initializeUserInfo();
    }

    public Region getView() {
        return viewBuilder.build();
    }

    private void initializeUserInfo() {
        Agent currentAgent = SessionManager.getInstance().getCurrentAgent();
        interactor.initializeUserInfo(currentAgent);
    }

    private void showEditDialog() {
        Agent currentAgent = SessionManager.getInstance().getCurrentAgent();
        if (currentAgent == null) {
            return;
        }

        interactor.prepareEditDialog(currentAgent);

        HeaderViewBuilder headerViewBuilder = (HeaderViewBuilder) viewBuilder;
        Dialog<ButtonType>[] dialogHolder = new Dialog[1];
        dialogHolder[0] = headerViewBuilder.buildEditDialog(() -> handleUpdate(currentAgent, dialogHolder[0]));

        dialogHolder[0].showAndWait();
    }

    private void handleUpdate(Agent currentAgent, Dialog<ButtonType> dialog) {
        String email = interactor.getEditEmail();
        String password = interactor.getEditPassword();
        String passwordConfirm = interactor.getEditPasswordConfirm();

        if (!interactor.validateUpdate(email, password, passwordConfirm)) {
            return;
        }

        Task<Agent> updateTask = new Task<>() {
            @Override
            protected Agent call() {
                return interactor.updateUser(currentAgent, email, password);
            }
        };

        updateTask.setOnSucceeded(event -> {
            Agent updatedAgent = updateTask.getValue();
            SessionManager.getInstance().setCurrentAgent(updatedAgent);
            interactor.initializeUserInfo(updatedAgent);
            dialog.close();
        });

        updateTask.setOnFailed(event -> {
            Throwable exception = updateTask.getException();
            interactor.handleUpdateError(exception);
        });

        new Thread(updateTask).start();
    }

    private void handleLogout() {
        if (onLogout != null) {
            onLogout.run();
        }
    }
}
