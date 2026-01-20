package groupe1.il3.app.gui.admin;

import javafx.beans.property.*;

public class AdminPanelModel {

    private final StringProperty errorMessage = new SimpleStringProperty("");
    private final StringProperty successMessage = new SimpleStringProperty("");


    public StringProperty errorMessageProperty() {
        return errorMessage;
    }

    public void setErrorMessage(String message) {
        errorMessage.set(message);
    }

    public StringProperty successMessageProperty() {
        return successMessage;
    }

    public void setSuccessMessage(String message) {
        successMessage.set(message);
    }
}
