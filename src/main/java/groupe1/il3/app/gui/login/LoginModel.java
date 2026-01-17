package groupe1.il3.app.gui.login;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Model for the login view.
 * Holds the state of the login form including credentials and UI state.
 */
public class LoginModel {

    private final StringProperty email = new SimpleStringProperty("");
    private final StringProperty password = new SimpleStringProperty("");
    private final StringProperty errorMessage = new SimpleStringProperty("");
    private final BooleanProperty loginInProgress = new SimpleBooleanProperty(false);

    // Email property
    public StringProperty emailProperty() {
        return email;
    }

    public String getEmail() {
        return email.get();
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    // Password property
    public StringProperty passwordProperty() {
        return password;
    }

    public String getPassword() {
        return password.get();
    }

    public void setPassword(String password) {
        this.password.set(password);
    }

    // Error message property
    public StringProperty errorMessageProperty() {
        return errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage.get();
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage.set(errorMessage);
    }

    // Login in progress property
    public BooleanProperty loginInProgressProperty() {
        return loginInProgress;
    }

    public boolean isLoginInProgress() {
        return loginInProgress.get();
    }

    public void setLoginInProgress(boolean loginInProgress) {
        this.loginInProgress.set(loginInProgress);
    }
}
