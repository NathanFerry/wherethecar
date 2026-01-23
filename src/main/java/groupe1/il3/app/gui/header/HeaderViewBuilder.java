package groupe1.il3.app.gui.header;

import groupe1.il3.app.gui.style.StyleApplier;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.util.Builder;

public class HeaderViewBuilder implements Builder<Region> {

    private final HeaderModel model;
    private final Runnable onEditAction;
    private final Runnable onLogoutAction;

    public HeaderViewBuilder(HeaderModel model, Runnable onEditAction, Runnable onLogoutAction) {
        this.model = model;
        this.onEditAction = onEditAction;
        this.onLogoutAction = onLogoutAction;
    }

    @Override
    public Region build() {
        BorderPane header = new BorderPane();
        header.setPadding(new Insets(10, 20, 10, 20));
        header.getStyleClass().add("header-container");

        Label appNameLabel = createAppNameLabel();
        HBox rightBox = createRightBox();

        header.setLeft(appNameLabel);
        header.setRight(rightBox);

        return header;
    }

    private Label createAppNameLabel() {
        Label appNameLabel = new Label();
        appNameLabel.textProperty().bind(model.appNameProperty());
        appNameLabel.getStyleClass().add("header-app-name");
        return appNameLabel;
    }

    private HBox createRightBox() {
        HBox rightBox = new HBox(10);
        rightBox.setAlignment(Pos.CENTER_RIGHT);

        Label userNameLabel = createUserNameLabel();
        Button editButton = createEditButton();
        Button logoutButton = createLogoutButton();

        rightBox.getChildren().addAll(userNameLabel, editButton, logoutButton);
        return rightBox;
    }

    private Label createUserNameLabel() {
        Label userNameLabel = new Label();
        userNameLabel.textProperty().bind(
            model.userFirstnameProperty().concat(" ").concat(model.userLastnameProperty())
        );
        userNameLabel.getStyleClass().add("header-username");
        return userNameLabel;
    }

    private Button createEditButton() {
        Button editButton = new Button("Modifier");
        editButton.setOnAction(_ -> onEditAction.run());
        editButton.getStyleClass().add("header-edit-button");
        return editButton;
    }

    private Button createLogoutButton() {
        Button logoutButton = new Button("Déconnexion");
        logoutButton.setOnAction(_ -> onLogoutAction.run());
        logoutButton.getStyleClass().add("header-logout-button");
        return logoutButton;
    }

    public Dialog<ButtonType> buildEditDialog(Runnable onSave) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Modifier les informations");
        dialog.setHeaderText("Modifier votre email et/ou mot de passe");

        StyleApplier.applyStylesheets(dialog);

        GridPane grid = createEditDialogGrid();
        dialog.getDialogPane().setContent(grid);

        setupDialogButtons(dialog, onSave);

        return dialog;
    }

    private GridPane createEditDialogGrid() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        TextField emailField = createEmailField();
        PasswordField passwordField = createPasswordField();
        PasswordField passwordConfirmField = createPasswordConfirmField();
        Label errorLabel = createErrorLabel();
        Label successLabel = createSuccessLabel();

        grid.add(new Label("Email:"), 0, 0);
        grid.add(emailField, 1, 0);
        grid.add(new Label("Mot de passe:"), 0, 1);
        grid.add(passwordField, 1, 1);
        grid.add(new Label("Confirmer:"), 0, 2);
        grid.add(passwordConfirmField, 1, 2);
        grid.add(errorLabel, 0, 3, 2, 1);
        grid.add(successLabel, 0, 4, 2, 1);

        return grid;
    }

    private TextField createEmailField() {
        TextField emailField = new TextField();
        emailField.setPromptText("Nouvel email (laisser vide pour ne pas changer)");
        emailField.textProperty().bindBidirectional(model.editEmailProperty());
        return emailField;
    }

    private PasswordField createPasswordField() {
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Nouveau mot de passe (laisser vide pour ne pas changer)");
        passwordField.textProperty().bindBidirectional(model.editPasswordProperty());
        return passwordField;
    }

    private PasswordField createPasswordConfirmField() {
        PasswordField passwordConfirmField = new PasswordField();
        passwordConfirmField.setPromptText("Confirmer le nouveau mot de passe");
        passwordConfirmField.textProperty().bindBidirectional(model.editPasswordConfirmProperty());
        return passwordConfirmField;
    }

    private Label createErrorLabel() {
        Label errorLabel = new Label();
        errorLabel.textProperty().bind(model.errorMessageProperty());
        errorLabel.getStyleClass().add("error-message");
        errorLabel.managedProperty().bind(errorLabel.textProperty().isNotEmpty());
        errorLabel.visibleProperty().bind(errorLabel.textProperty().isNotEmpty());
        return errorLabel;
    }

    private Label createSuccessLabel() {
        Label successLabel = new Label();
        successLabel.textProperty().bind(model.successMessageProperty());
        successLabel.getStyleClass().add("success-message");
        successLabel.managedProperty().bind(successLabel.textProperty().isNotEmpty());
        successLabel.visibleProperty().bind(successLabel.textProperty().isNotEmpty());
        return successLabel;
    }

    private void setupDialogButtons(Dialog<ButtonType> dialog, Runnable onSave) {
        ButtonType saveButtonType = new ButtonType("Enregistrer", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("Annuler", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, cancelButtonType);

        Button saveButton = (Button) dialog.getDialogPane().lookupButton(saveButtonType);
        saveButton.disableProperty().bind(model.updateInProgressProperty());

        if (onSave != null) {
            saveButton.addEventFilter(ActionEvent.ACTION, event -> {
                onSave.run();
                event.consume();
            });
        }
    }
}
