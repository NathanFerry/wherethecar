package groupe1.il3.app.gui.header;

import groupe1.il3.app.gui.style.StyleApplier;
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

    public HeaderViewBuilder(HeaderModel model, Runnable onEditAction) {
        this.model = model;
        this.onEditAction = onEditAction;
    }

    @Override
    public Region build() {
        BorderPane header = new BorderPane();
        header.setPadding(new Insets(10, 20, 10, 20));
        header.getStyleClass().add("header-container");

        Label appNameLabel = new Label();
        appNameLabel.textProperty().bind(model.appNameProperty());
        appNameLabel.getStyleClass().add("header-app-name");

        HBox rightBox = new HBox(10);
        rightBox.setAlignment(Pos.CENTER_RIGHT);

        Label userNameLabel = new Label();
        userNameLabel.textProperty().bind(
            model.userFirstnameProperty().concat(" ").concat(model.userLastnameProperty())
        );
        userNameLabel.getStyleClass().add("header-username");

        Button editButton = new Button("Modifier");
        editButton.setOnAction(e -> onEditAction.run());
        editButton.getStyleClass().add("header-edit-button");

        rightBox.getChildren().addAll(userNameLabel, editButton);

        header.setLeft(appNameLabel);
        header.setRight(rightBox);

        return header;
    }

    public Dialog<ButtonType> buildEditDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Modifier les informations");
        dialog.setHeaderText("Modifier votre email et/ou mot de passe");

        StyleApplier.applyStylesheets(dialog);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        Label emailLabel = new Label("Email:");
        TextField emailField = new TextField();
        emailField.setPromptText("Nouvel email (laisser vide pour ne pas changer)");
        emailField.textProperty().bindBidirectional(model.editEmailProperty());

        Label passwordLabel = new Label("Mot de passe:");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Nouveau mot de passe (laisser vide pour ne pas changer)");
        passwordField.textProperty().bindBidirectional(model.editPasswordProperty());

        Label passwordConfirmLabel = new Label("Confirmer:");
        PasswordField passwordConfirmField = new PasswordField();
        passwordConfirmField.setPromptText("Confirmer le nouveau mot de passe");
        passwordConfirmField.textProperty().bindBidirectional(model.editPasswordConfirmProperty());

        Label errorLabel = new Label();
        errorLabel.textProperty().bind(model.errorMessageProperty());
        errorLabel.getStyleClass().add("error-message");
        errorLabel.managedProperty().bind(errorLabel.textProperty().isNotEmpty());
        errorLabel.visibleProperty().bind(errorLabel.textProperty().isNotEmpty());

        Label successLabel = new Label();
        successLabel.textProperty().bind(model.successMessageProperty());
        successLabel.getStyleClass().add("success-message");
        successLabel.managedProperty().bind(successLabel.textProperty().isNotEmpty());
        successLabel.visibleProperty().bind(successLabel.textProperty().isNotEmpty());

        grid.add(emailLabel, 0, 0);
        grid.add(emailField, 1, 0);
        grid.add(passwordLabel, 0, 1);
        grid.add(passwordField, 1, 1);
        grid.add(passwordConfirmLabel, 0, 2);
        grid.add(passwordConfirmField, 1, 2);
        grid.add(errorLabel, 0, 3, 2, 1);
        grid.add(successLabel, 0, 4, 2, 1);

        dialog.getDialogPane().setContent(grid);

        ButtonType saveButtonType = new ButtonType("Enregistrer", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("Annuler", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, cancelButtonType);

        Button saveButton = (Button) dialog.getDialogPane().lookupButton(saveButtonType);
        saveButton.disableProperty().bind(model.updateInProgressProperty());

        return dialog;
    }
}
