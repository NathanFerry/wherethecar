package groupe1.il3.app.gui.admin;

public class AdminPanelInteractor {

    private final AdminPanelModel model;

    public AdminPanelInteractor(AdminPanelModel model) {
        this.model = model;
    }

    public void updateMessages(String errorMessage, String successMessage) {
        model.setErrorMessage(errorMessage);
        model.setSuccessMessage(successMessage);
    }
}
