package groupe1.il3.app.gui.login;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.Builder;

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
        root.setPrefSize(400, 400);
        root.getStyleClass().add("login-root");

        VBox loginForm = createLoginForm();
        root.setCenter(loginForm);

        return root;
    }

    private VBox createLoginForm() {
        VBox formBox = new VBox(15);
        formBox.setAlignment(Pos.CENTER);
        formBox.setPadding(new Insets(20));
        formBox.setMaxWidth(350);
        formBox.getStyleClass().add("login-form-container");

        formBox.getChildren().addAll(
            createTitleLabel(),
            createEmailLabel(),
            createEmailField(),
            createPasswordLabel(),
            createPasswordField(),
            createLoginButton(),
            createErrorLabel()
        );

        return formBox;
    }

    private Label createTitleLabel() {
        Label titleLabel = new Label("Connexion");
        titleLabel.getStyleClass().add("login-title");
        return titleLabel;
    }

    private Label createEmailLabel() {
        Label emailLabel = new Label("Email:");
        emailLabel.getStyleClass().add("login-label");
        return emailLabel;
    }

    private TextField createEmailField() {
        TextField emailField = new TextField();
        emailField.setPromptText("Entrez votre email");
        emailField.textProperty().bindBidirectional(model.emailProperty());
        emailField.getStyleClass().add("login-text-field");
        return emailField;
    }

    private Label createPasswordLabel() {
        Label passwordLabel = new Label("Mot de passe:");
        passwordLabel.getStyleClass().add("login-label");
        return passwordLabel;
    }

    private PasswordField createPasswordField() {
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Entrez votre mot de passe");
        passwordField.textProperty().bindBidirectional(model.passwordProperty());
        passwordField.getStyleClass().add("login-text-field");
        return passwordField;
    }

    private Button createLoginButton() {
        Button loginButton = new Button("Se connecter");
        loginButton.setMaxWidth(Double.MAX_VALUE);
        loginButton.setDefaultButton(true);
        loginButton.setOnAction(e -> loginAction.run());
        loginButton.getStyleClass().add("login-button");
        loginButton.disableProperty().bind(model.loginInProgressProperty());
        return loginButton;
    }

    private Label createErrorLabel() {
        Label errorLabel = new Label();
        errorLabel.textProperty().bind(model.errorMessageProperty());
        errorLabel.getStyleClass().add("login-error-label");
        errorLabel.setWrapText(true);
        errorLabel.setMaxWidth(Double.MAX_VALUE);
        errorLabel.managedProperty().bind(errorLabel.textProperty().isNotEmpty());
        errorLabel.visibleProperty().bind(errorLabel.textProperty().isNotEmpty());
        return errorLabel;
    }
}
