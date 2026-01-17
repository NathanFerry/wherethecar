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
 * Handles user actions and manages the authentication flow.
 */
public class LoginController {

    private final LoginModel model;
    private final LoginInteractor interactor;
    private final Builder<Region> viewBuilder;
    private final Consumer<Agent> onLoginSuccess;

    /**
     * Creates a new LoginController.
     *
     * @param onLoginSuccess callback to execute when login is successful, receives the authenticated Agent
     */
    public LoginController(Consumer<Agent> onLoginSuccess) {
        this.model = new LoginModel();
        this.interactor = new LoginInteractor();
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
     * Creates an authentication task and processes the result.
     */
    private void handleLogin() {
        // Clear any previous error messages
        model.setErrorMessage("");
        model.setLoginInProgress(true);

        // Get credentials from model
        String email = model.getEmail();
        String password = model.getPassword();

        // Create authentication task
        Task<Agent> authTask = interactor.createAuthenticationTask(email, password);

        // Handle successful authentication
        authTask.setOnSucceeded(event -> {
            model.setLoginInProgress(false);
            Agent authenticatedAgent = authTask.getValue();

            if (authenticatedAgent != null) {
                // Store the authenticated agent in the session
                SessionManager.getInstance().setCurrentAgent(authenticatedAgent);

                // Execute the success callback
                if (onLoginSuccess != null) {
                    onLoginSuccess.accept(authenticatedAgent);
                }
            } else {
                // Authentication failed
                model.setErrorMessage("Email ou mot de passe incorrect.");
            }
        });

        // Handle authentication failure
        authTask.setOnFailed(event -> {
            model.setLoginInProgress(false);
            Throwable exception = authTask.getException();
            model.setErrorMessage("Erreur lors de la connexion: " + exception.getMessage());
            exception.printStackTrace();
        });

        // Start the authentication task in a background thread
        new Thread(authTask).start();
    }
}
