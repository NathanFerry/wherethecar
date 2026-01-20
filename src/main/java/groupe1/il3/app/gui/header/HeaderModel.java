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

    private final BooleanProperty editDialogVisible = new SimpleBooleanProperty(false);
    private final BooleanProperty updateInProgress = new SimpleBooleanProperty(false);

    public StringProperty appNameProperty() {
        return appName;
    }

    public String getAppName() {
        return appName.get();
    }

    public void setAppName(String appName) {
        this.appName.set(appName);
    }

    public StringProperty userFirstnameProperty() {
        return userFirstname;
    }

    public String getUserFirstname() {
        return userFirstname.get();
    }

    public void setUserFirstname(String firstname) {
        this.userFirstname.set(firstname);
    }

    public StringProperty userLastnameProperty() {
        return userLastname;
    }

    public String getUserLastname() {
        return userLastname.get();
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

    public void setEditPassword(String password) {
        this.editPassword.set(password);
    }

    public StringProperty editPasswordConfirmProperty() {
        return editPasswordConfirm;
    }

    public String getEditPasswordConfirm() {
        return editPasswordConfirm.get();
    }

    public void setEditPasswordConfirm(String passwordConfirm) {
        this.editPasswordConfirm.set(passwordConfirm);
    }

    public StringProperty errorMessageProperty() {
        return errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage.get();
    }

    public void setErrorMessage(String message) {
        this.errorMessage.set(message);
    }

    public StringProperty successMessageProperty() {
        return successMessage;
    }

    public String getSuccessMessage() {
        return successMessage.get();
    }

    public void setSuccessMessage(String message) {
        this.successMessage.set(message);
    }

    public BooleanProperty editDialogVisibleProperty() {
        return editDialogVisible;
    }

    public boolean isEditDialogVisible() {
        return editDialogVisible.get();
    }

    public void setEditDialogVisible(boolean visible) {
        this.editDialogVisible.set(visible);
    }

    public BooleanProperty updateInProgressProperty() {
        return updateInProgress;
    }

    public boolean isUpdateInProgress() {
        return updateInProgress.get();
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
