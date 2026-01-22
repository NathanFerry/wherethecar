package groupe1.il3.app.gui.login;

import groupe1.il3.app.domain.agent.Agent;
import groupe1.il3.app.domain.authentication.SessionManager;
import javafx.concurrent.Task;
import javafx.scene.layout.Region;
import javafx.util.Builder;

import java.util.function.Consumer;

/**
 * Controller for the login view.
 * Coordinates between the Model, View, and Interactor.
 * Handles user actions, manages task orchestration, and controls the authentication flow.
 */
public class LoginController {

    private final LoginInteractor interactor;
    private final Builder<Region> viewBuilder;
    private final Consumer<Agent> onLoginSuccess;

    /**
     * Creates a new LoginController.
     *
     * @param onLoginSuccess callback to execute when login is successful, receives the authenticated Agent
     */
    public LoginController(Consumer<Agent> onLoginSuccess) {
        // Create the model
        LoginModel model = new LoginModel();

        // Pass the model to interactor and viewbuilder, then forget about it
        this.interactor = new LoginInteractor(model);
        this.viewBuilder = new LoginViewBuilder(model, this::handleLogin);
        this.onLoginSuccess = onLoginSuccess;
    }

    /**
     * Returns the view for this controller.
     *
     * @return the JavaFX Region containing the login UI
     */
    public Region getView() {
        return viewBuilder.build();
    }

    /**
     * Handles the login button action.
     * Creates and manages an authentication task.
     */
    private void handleLogin() {
        // Create authentication task that calls the interactor
        Task<Agent> authTask = new Task<>() {
            @Override
            protected Agent call() {
                // Interactor handles authentication and model updates
                return interactor.authenticate();
            }
        };

        // Handle task completion
        authTask.setOnSucceeded(event -> {
            Agent authenticatedAgent = authTask.getValue();

            if (authenticatedAgent != null) {
                // Store the authenticated agent in the session
                SessionManager.getInstance().setCurrentAgent(authenticatedAgent);

                // Execute the success callback
                if (onLoginSuccess != null) {
                    onLoginSuccess.accept(authenticatedAgent);
                }
            }
            // If authentication failed, interactor has already updated the model with error message
        });

        // Handle task failure (exceptions)
        authTask.setOnFailed(event -> {
            Throwable exception = authTask.getException();
            exception.printStackTrace();
        });

        // Start the authentication task in a background thread
        new Thread(authTask).start();
    }
}
