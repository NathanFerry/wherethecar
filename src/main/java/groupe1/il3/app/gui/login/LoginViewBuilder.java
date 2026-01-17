package groupe1.il3.app.gui.login;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.Builder;

/**
 * ViewBuilder for the login screen.
 * Constructs the JavaFX UI components for the login form.
 */
public class LoginViewBuilder implements Builder<Region> {

    private final LoginModel model;
    private final Runnable loginAction;

    public LoginViewBuilder(LoginModel model, Runnable loginAction) {
        this.model = model;
        this.loginAction = loginAction;
    }

    @Override
    public Region build() {
        BorderPane root = new BorderPane();
        root.setPrefSize(400, 300);

        // Center the login form
        VBox loginForm = createLoginForm();
        root.setCenter(loginForm);

        return root;
    }

    private VBox createLoginForm() {
        VBox formBox = new VBox(15);
        formBox.setAlignment(Pos.CENTER);
        formBox.setPadding(new Insets(20));
        formBox.setMaxWidth(350);

        // Title
        Label titleLabel = new Label("Connexion");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        // Email field
        Label emailLabel = new Label("Email:");
        TextField emailField = new TextField();
        emailField.setPromptText("Entrez votre email");
        emailField.textProperty().bindBidirectional(model.emailProperty());

        // Password field
        Label passwordLabel = new Label("Mot de passe:");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Entrez votre mot de passe");
        passwordField.textProperty().bindBidirectional(model.passwordProperty());

        // Login button
        Button loginButton = new Button("Se connecter");
        loginButton.setMaxWidth(Double.MAX_VALUE);
        loginButton.setDefaultButton(true);
        loginButton.setOnAction(e -> loginAction.run());

        // Bind button disable property to login in progress
        loginButton.disableProperty().bind(model.loginInProgressProperty());

        // Error message label
        Label errorLabel = new Label();
        errorLabel.textProperty().bind(model.errorMessageProperty());
        errorLabel.setStyle("-fx-text-fill: red;");
        errorLabel.setWrapText(true);
        errorLabel.setMaxWidth(Double.MAX_VALUE);

        // Only show error label when there's an error message
        errorLabel.managedProperty().bind(errorLabel.textProperty().isNotEmpty());
        errorLabel.visibleProperty().bind(errorLabel.textProperty().isNotEmpty());

        // Add all components to form
        formBox.getChildren().addAll(
            titleLabel,
            emailLabel,
            emailField,
            passwordLabel,
            passwordField,
            loginButton,
            errorLabel
        );

        return formBox;
    }
}
