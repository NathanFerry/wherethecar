package groupe1.il3.app.gui.header;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class HeaderModel {
    private final StringProperty appName = new SimpleStringProperty("WhereTheCar");
    private final StringProperty userFirstname = new SimpleStringProperty("");
    private final StringProperty userLastname = new SimpleStringProperty("");

    private final StringProperty editEmail = new SimpleStringProperty("");
    private final StringProperty editPassword = new SimpleStringProperty("");
    private final StringProperty editPasswordConfirm = new SimpleStringProperty("");
    private final StringProperty errorMessage = new SimpleStringProperty("");
    private final StringProperty successMessage = new SimpleStringProperty("");

    private final BooleanProperty updateInProgress = new SimpleBooleanProperty(false);

    public StringProperty appNameProperty() {
        return appName;
    }

    public StringProperty userFirstnameProperty() {
        return userFirstname;
    }

    public void setUserFirstname(String firstname) {
        this.userFirstname.set(firstname);
    }

    public StringProperty userLastnameProperty() {
        return userLastname;
    }

    public void setUserLastname(String lastname) {
        this.userLastname.set(lastname);
    }

    public StringProperty editEmailProperty() {
        return editEmail;
    }

    public String getEditEmail() {
        return editEmail.get();
    }

    public void setEditEmail(String email) {
        this.editEmail.set(email);
    }

    public StringProperty editPasswordProperty() {
        return editPassword;
    }

    public String getEditPassword() {
        return editPassword.get();
    }

    public StringProperty editPasswordConfirmProperty() {
        return editPasswordConfirm;
    }

    public String getEditPasswordConfirm() {
        return editPasswordConfirm.get();
    }

    public StringProperty errorMessageProperty() {
        return errorMessage;
    }

    public void setErrorMessage(String message) {
        this.errorMessage.set(message);
    }

    public StringProperty successMessageProperty() {
        return successMessage;
    }

    public BooleanProperty updateInProgressProperty() {
        return updateInProgress;
    }

    public void setUpdateInProgress(boolean inProgress) {
        this.updateInProgress.set(inProgress);
    }

    public void clearMessages() {
        errorMessage.set("");
        successMessage.set("");
    }

    public void clearEditFields() {
        editEmail.set("");
        editPassword.set("");
        editPasswordConfirm.set("");
    }
}
