package groupe1.il3.app.gui.login;

import groupe1.il3.app.domain.agent.Agent;
import groupe1.il3.app.domain.authentication.AuthResult;
import groupe1.il3.app.domain.authentication.SessionManager;
import javafx.concurrent.Task;
import javafx.scene.layout.Region;
import javafx.util.Builder;

import java.util.function.Consumer;

public class LoginController {

    private final LoginInteractor interactor;
    private final Builder<Region> viewBuilder;
    private final Consumer<Agent> onLoginSuccess;

    public LoginController(Consumer<Agent> onLoginSuccess) {
        LoginModel model = new LoginModel();

        this.interactor = new LoginInteractor(model);
        this.viewBuilder = new LoginViewBuilder(model, this::handleLogin);
        this.onLoginSuccess = onLoginSuccess;
    }

    public Region getView() {
        return viewBuilder.build();
    }

    private void handleLogin() {
        interactor.prepareAuthentication();

        Task<AuthResult> authTask = new Task<>() {
            @Override
            protected AuthResult call() {
                return interactor.fetchAndValidateCredentials();
            }
        };

        authTask.setOnSucceeded(event -> {
            AuthResult result = authTask.getValue();

            interactor.updateModelWithResult(result);

            Agent authenticatedAgent = result.agent();
            if (authenticatedAgent != null) {
                SessionManager.getInstance().setCurrentAgent(authenticatedAgent);

                if (onLoginSuccess != null) {
                    onLoginSuccess.accept(authenticatedAgent);
                }
            }
        });

        authTask.setOnFailed(event -> {
            Throwable exception = authTask.getException();
            exception.printStackTrace();
            interactor.updateModelWithResult(new AuthResult(null, "Une erreur est survenue."));
        });

        new Thread(authTask).start();
    }
}
