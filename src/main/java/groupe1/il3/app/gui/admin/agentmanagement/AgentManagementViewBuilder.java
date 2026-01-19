package groupe1.il3.app.gui.admin.agentmanagement;

import groupe1.il3.app.domain.agent.Agent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.Builder;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

public class AgentManagementViewBuilder implements Builder<Region> {

    private final AgentManagementModel model;
    private final Runnable loadAgentsAction;
    private final Consumer<Agent> addAgentAction;
    private final Consumer<Agent> editAgentAction;
    private final Consumer<UUID> deleteAgentAction;

    public AgentManagementViewBuilder(
        AgentManagementModel model,
        Runnable loadAgentsAction,
        Consumer<Agent> addAgentAction,
        Consumer<Agent> editAgentAction,
        Consumer<UUID> deleteAgentAction
    ) {
        this.model = model;
        this.loadAgentsAction = loadAgentsAction;
        this.addAgentAction = addAgentAction;
        this.editAgentAction = editAgentAction;
        this.deleteAgentAction = deleteAgentAction;
    }

    @Override
    public Region build() {
        BorderPane pane = new BorderPane();
        pane.setPadding(new Insets(10));

        VBox listPane = new VBox(10);
        listPane.setPrefWidth(350);

        Label listTitle = new Label("Liste des Utilisateurs");
        listTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        ListView<Agent> agentListView = new ListView<>();
        agentListView.setItems(model.agentsProperty());
        agentListView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Agent agent, boolean empty) {
                super.updateItem(agent, empty);
                if (empty || agent == null) {
                    setText(null);
                } else {
                    String adminLabel = agent.isAdmin() ? " [ADMIN]" : "";
                    setText(agent.getFirstname() + " " + agent.getLastname() +
                           " (" + agent.getEmail() + ")" + adminLabel);
                }
            }
        });

        agentListView.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldVal, newVal) -> model.setSelectedAgent(newVal)
        );

        VBox.setVgrow(agentListView, Priority.ALWAYS);

        HBox buttonBox = new HBox(10);
        Button addBtn = new Button("Ajouter");
        addBtn.setOnAction(e -> showAddAgentDialog());
        Button editBtn = new Button("Modifier");
        editBtn.setOnAction(e -> showEditAgentDialog());
        editBtn.disableProperty().bind(model.selectedAgentProperty().isNull());
        Button deleteBtn = new Button("Supprimer");
        deleteBtn.setOnAction(e -> confirmDeleteAgent());
        deleteBtn.disableProperty().bind(model.selectedAgentProperty().isNull());
        Button refreshBtn = new Button("Actualiser");
        refreshBtn.setOnAction(e -> loadAgentsAction.run());

        buttonBox.getChildren().addAll(addBtn, editBtn, deleteBtn, refreshBtn);

        listPane.getChildren().addAll(listTitle, agentListView, buttonBox);

        pane.setLeft(listPane);

        VBox detailsPane = createAgentDetailsPane();
        pane.setCenter(detailsPane);

        return pane;
    }

    private VBox createAgentDetailsPane() {
        VBox detailsPane = new VBox(10);
        detailsPane.setPadding(new Insets(20));
        detailsPane.setAlignment(Pos.TOP_LEFT);

        Label noSelectionLabel = new Label("Sélectionnez un utilisateur pour voir ses détails");

        VBox detailsBox = new VBox(15);
        Label titleLabel = new Label();
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        GridPane detailsGrid = new GridPane();
        detailsGrid.setHgap(15);
        detailsGrid.setVgap(10);

        model.selectedAgentProperty().addListener((obs, oldVal, newVal) -> {
            detailsGrid.getChildren().clear();
            if (newVal != null) {
                titleLabel.setText(newVal.getFirstname() + " " + newVal.getLastname());

                addDetailRow(detailsGrid, 0, "UUID:", newVal.getUuid().toString());
                addDetailRow(detailsGrid, 1, "Prénom:", newVal.getFirstname());
                addDetailRow(detailsGrid, 2, "Nom:", newVal.getLastname());
                addDetailRow(detailsGrid, 3, "Email:", newVal.getEmail());
                addDetailRow(detailsGrid, 4, "Administrateur:", newVal.isAdmin() ? "Oui" : "Non");
            }
        });

        detailsBox.getChildren().addAll(titleLabel, new Separator(), detailsGrid);
        detailsBox.visibleProperty().bind(model.selectedAgentProperty().isNotNull());

        noSelectionLabel.visibleProperty().bind(model.selectedAgentProperty().isNull());

        detailsPane.getChildren().addAll(noSelectionLabel, detailsBox);

        return detailsPane;
    }

    private void addDetailRow(GridPane grid, int row, String label, String value) {
        Label labelNode = new Label(label);
        labelNode.setStyle("-fx-font-weight: bold;");
        Label valueNode = new Label(value);

        grid.add(labelNode, 0, row);
        grid.add(valueNode, 1, row);
    }

    private void showAddAgentDialog() {
        Dialog<Agent> dialog = new Dialog<>();
        dialog.setTitle("Ajouter un utilisateur");
        dialog.setHeaderText("Saisir les informations de l'utilisateur");

        ButtonType addButtonType = new ButtonType("Ajouter", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        GridPane grid = createAgentFormGrid(null);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                return extractAgentFromForm(grid, UUID.randomUUID());
            }
            return null;
        });

        Optional<Agent> result = dialog.showAndWait();
        result.ifPresent(addAgentAction);
    }

    private void showEditAgentDialog() {
        Agent selected = model.getSelectedAgent();
        if (selected == null) return;

        Dialog<Agent> dialog = new Dialog<>();
        dialog.setTitle("Modifier un utilisateur");
        dialog.setHeaderText("Modifier les informations de l'utilisateur");

        ButtonType saveButtonType = new ButtonType("Enregistrer", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        GridPane grid = createAgentFormGrid(selected);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                return extractAgentFromForm(grid, selected.getUuid());
            }
            return null;
        });

        Optional<Agent> result = dialog.showAndWait();
        result.ifPresent(editAgentAction);
    }

    private GridPane createAgentFormGrid(Agent agent) {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        int row = 0;

        TextField firstNameField = new TextField(agent != null ? agent.getFirstname() : "");
        grid.add(new Label("Prénom:"), 0, row);
        grid.add(firstNameField, 1, row++);
        firstNameField.setUserData("firstName");

        TextField lastNameField = new TextField(agent != null ? agent.getLastname() : "");
        grid.add(new Label("Nom:"), 0, row);
        grid.add(lastNameField, 1, row++);
        lastNameField.setUserData("lastName");

        TextField emailField = new TextField(agent != null ? agent.getEmail() : "");
        grid.add(new Label("Email:"), 0, row);
        grid.add(emailField, 1, row++);
        emailField.setUserData("email");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText(agent != null ? "Laisser vide pour ne pas changer" : "Mot de passe");
        grid.add(new Label("Mot de passe:"), 0, row);
        grid.add(passwordField, 1, row++);
        passwordField.setUserData("password");

        CheckBox isAdminCheckBox = new CheckBox();
        isAdminCheckBox.setSelected(agent != null && agent.isAdmin());
        grid.add(new Label("Administrateur:"), 0, row);
        grid.add(isAdminCheckBox, 1, row++);
        isAdminCheckBox.setUserData("isAdmin");

        return grid;
    }

    private Agent extractAgentFromForm(GridPane grid, UUID uuid) {
        String firstName = "";
        String lastName = "";
        String email = "";
        String password = "";
        boolean isAdmin = false;

        for (Node node : grid.getChildren()) {
            Object userData = node.getUserData();
            if (userData == null) continue;

            switch (userData.toString()) {
                case "firstName":
                    firstName = ((TextField) node).getText();
                    break;
                case "lastName":
                    lastName = ((TextField) node).getText();
                    break;
                case "email":
                    email = ((TextField) node).getText();
                    break;
                case "password":
                    password = ((PasswordField) node).getText();
                    break;
                case "isAdmin":
                    isAdmin = ((CheckBox) node).isSelected();
                    break;
            }
        }

        String passwordHash;
        if (password.isEmpty() && model.getSelectedAgent() != null) {
            passwordHash = model.getSelectedAgent().getPasswordHash();
        } else {
            passwordHash = hashPassword(password);
        }

        return new Agent(uuid, firstName, lastName, email, passwordHash, isAdmin);
    }

    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes(StandardCharsets.UTF_8));
            return java.util.HexFormat.of().formatHex(hash);
        } catch (java.security.NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to hash password", e);
        }
    }

    private void confirmDeleteAgent() {
        Agent selected = model.getSelectedAgent();
        if (selected == null) return;

        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Supprimer l'utilisateur");
        alert.setContentText("⚠️ ATTENTION ⚠️\n\n" +
                           "Vous êtes sur le point de supprimer l'utilisateur :\n" +
                           selected.getFirstname() + " " + selected.getLastname() +
                           " (" + selected.getEmail() + ")\n\n" +
                           "TOUTES les données associées à cet utilisateur seront perdues " +
                           "(réservations, etc.).\n\n" +
                           "Cette action est IRRÉVERSIBLE.\n\n" +
                           "Êtes-vous absolument sûr de vouloir continuer ?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            deleteAgentAction.accept(selected.getUuid());
        }
    }
}

