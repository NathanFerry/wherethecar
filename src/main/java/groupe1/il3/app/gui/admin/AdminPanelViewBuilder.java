package groupe1.il3.app.gui.admin;

import groupe1.il3.app.domain.agent.Agent;
import groupe1.il3.app.domain.reservation.Reservation;
import groupe1.il3.app.domain.reservation.ReservationStatus;
import groupe1.il3.app.domain.vehicle.Energy;
import groupe1.il3.app.domain.vehicle.Status;
import groupe1.il3.app.domain.vehicle.Vehicle;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class AdminPanelViewBuilder implements Builder<Region> {

    private final AdminPanelModel model;
    private final Runnable loadVehiclesAction;
    private final Runnable loadAgentsAction;
    private final Consumer<Vehicle> addVehicleAction;
    private final Consumer<Vehicle> editVehicleAction;
    private final Consumer<UUID> deleteVehicleAction;
    private final Consumer<Agent> addAgentAction;
    private final Consumer<Agent> editAgentAction;
    private final Consumer<UUID> deleteAgentAction;
    private final Runnable loadPendingReservationsAction;
    private final BiConsumer<UUID, UUID> approveReservationAction;
    private final Consumer<UUID> cancelReservationAction;

    public AdminPanelViewBuilder(
        AdminPanelModel model,
        Runnable loadVehiclesAction,
        Runnable loadAgentsAction,
        Consumer<Vehicle> addVehicleAction,
        Consumer<Vehicle> editVehicleAction,
        Consumer<UUID> deleteVehicleAction,
        Consumer<Agent> addAgentAction,
        Consumer<Agent> editAgentAction,
        Consumer<UUID> deleteAgentAction,
        Runnable loadPendingReservationsAction,
        BiConsumer<UUID, UUID> approveReservationAction,
        Consumer<UUID> cancelReservationAction
    ) {
        this.model = model;
        this.loadVehiclesAction = loadVehiclesAction;
        this.loadAgentsAction = loadAgentsAction;
        this.addVehicleAction = addVehicleAction;
        this.editVehicleAction = editVehicleAction;
        this.deleteVehicleAction = deleteVehicleAction;
        this.addAgentAction = addAgentAction;
        this.editAgentAction = editAgentAction;
        this.deleteAgentAction = deleteAgentAction;
        this.loadPendingReservationsAction = loadPendingReservationsAction;
        this.approveReservationAction = approveReservationAction;
        this.cancelReservationAction = cancelReservationAction;
    }

    @Override
    public Region build() {
        BorderPane mainPane = new BorderPane();
        mainPane.setPadding(new Insets(10));

        Label title = new Label("Panneau d'Administration");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        BorderPane.setMargin(title, new Insets(10));
        mainPane.setTop(title);

        TabPane tabPane = new TabPane();

        Tab vehiclesTab = new Tab("Gestion des Véhicules");
        vehiclesTab.setClosable(false);
        vehiclesTab.setContent(createVehiclesManagementPane());

        Tab agentsTab = new Tab("Gestion des Utilisateurs");
        agentsTab.setClosable(false);
        agentsTab.setContent(createAgentsManagementPane());

        Tab reservationsTab = new Tab("Réservations en Attente");
        reservationsTab.setClosable(false);
        reservationsTab.setContent(createPendingReservationsPane());

        tabPane.getTabs().addAll(vehiclesTab, agentsTab, reservationsTab);

        mainPane.setCenter(tabPane);

        VBox messageBox = createMessageBox();
        mainPane.setBottom(messageBox);

        loadVehiclesAction.run();
        loadAgentsAction.run();
        loadPendingReservationsAction.run();

        return mainPane;
    }

    private Region createVehiclesManagementPane() {
        BorderPane pane = new BorderPane();
        pane.setPadding(new Insets(10));

        VBox listPane = new VBox(10);
        listPane.setPrefWidth(350);

        Label listTitle = new Label("Liste des Véhicules");
        listTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        ListView<Vehicle> vehicleListView = new ListView<>();
        vehicleListView.setItems(model.vehiclesProperty());
        vehicleListView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Vehicle vehicle, boolean empty) {
                super.updateItem(vehicle, empty);
                if (empty || vehicle == null) {
                    setText(null);
                } else {
                    setText(vehicle.getManufacturer() + " " + vehicle.getModel() +
                           " (" + vehicle.getLicencePlate() + ")");
                }
            }
        });

        vehicleListView.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldVal, newVal) -> model.setSelectedVehicle(newVal)
        );

        VBox.setVgrow(vehicleListView, Priority.ALWAYS);

        HBox buttonBox = new HBox(10);
        Button addBtn = new Button("Ajouter");
        addBtn.setOnAction(e -> showAddVehicleDialog());
        Button editBtn = new Button("Modifier");
        editBtn.setOnAction(e -> showEditVehicleDialog());
        editBtn.disableProperty().bind(model.selectedVehicleProperty().isNull());
        Button deleteBtn = new Button("Supprimer");
        deleteBtn.setOnAction(e -> confirmDeleteVehicle());
        deleteBtn.disableProperty().bind(model.selectedVehicleProperty().isNull());
        Button refreshBtn = new Button("Actualiser");
        refreshBtn.setOnAction(e -> loadVehiclesAction.run());

        buttonBox.getChildren().addAll(addBtn, editBtn, deleteBtn, refreshBtn);

        listPane.getChildren().addAll(listTitle, vehicleListView, buttonBox);

        pane.setLeft(listPane);

        VBox detailsPane = createVehicleDetailsPane();
        pane.setCenter(detailsPane);

        return pane;
    }

    private VBox createVehicleDetailsPane() {
        VBox detailsPane = new VBox(10);
        detailsPane.setPadding(new Insets(20));
        detailsPane.setAlignment(Pos.TOP_LEFT);

        Label noSelectionLabel = new Label("Sélectionnez un véhicule pour voir ses détails");

        VBox detailsBox = new VBox(15);
        Label titleLabel = new Label();
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        GridPane detailsGrid = new GridPane();
        detailsGrid.setHgap(15);
        detailsGrid.setVgap(10);

        model.selectedVehicleProperty().addListener((obs, oldVal, newVal) -> {
            detailsGrid.getChildren().clear();
            if (newVal != null) {
                titleLabel.setText(newVal.getManufacturer() + " " + newVal.getModel());

                addDetailRow(detailsGrid, 0, "UUID:", newVal.getUuid().toString());
                addDetailRow(detailsGrid, 1, "Plaque:", newVal.getLicencePlate());
                addDetailRow(detailsGrid, 2, "Constructeur:", newVal.getManufacturer());
                addDetailRow(detailsGrid, 3, "Modèle:", newVal.getModel());
                addDetailRow(detailsGrid, 4, "Énergie:", formatEnergy(newVal.getEnergy()));
                addDetailRow(detailsGrid, 5, "Puissance:", newVal.getPower() + " CV");
                addDetailRow(detailsGrid, 6, "Sièges:", String.valueOf(newVal.getSeats()));
                addDetailRow(detailsGrid, 7, "Capacité:", newVal.getCapacity() + " L");
                addDetailRow(detailsGrid, 8, "Poids utilitaire:", newVal.getUtilityWeight() + " kg");
                addDetailRow(detailsGrid, 9, "Couleur:", newVal.getColor());
                addDetailRow(detailsGrid, 10, "Kilométrage:", newVal.getKilometers() + " km");
                addDetailRow(detailsGrid, 11, "Date d'acquisition:",
                    newVal.getAcquisitionDate() != null ?
                    newVal.getAcquisitionDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "N/A");
                addDetailRow(detailsGrid, 12, "Statut:", formatStatus(newVal.getStatus()));
            }
        });

        detailsBox.getChildren().addAll(titleLabel, new Separator(), detailsGrid);
        detailsBox.visibleProperty().bind(model.selectedVehicleProperty().isNotNull());

        noSelectionLabel.visibleProperty().bind(model.selectedVehicleProperty().isNull());

        detailsPane.getChildren().addAll(noSelectionLabel, detailsBox);

        return detailsPane;
    }

    private Region createAgentsManagementPane() {
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

    private VBox createMessageBox() {
        VBox messageBox = new VBox(5);
        messageBox.setPadding(new Insets(10));

        Label errorLabel = new Label();
        errorLabel.textProperty().bind(model.errorMessageProperty());
        errorLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
        errorLabel.setWrapText(true);
        errorLabel.managedProperty().bind(errorLabel.textProperty().isNotEmpty());
        errorLabel.visibleProperty().bind(errorLabel.textProperty().isNotEmpty());

        Label successLabel = new Label();
        successLabel.textProperty().bind(model.successMessageProperty());
        successLabel.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
        successLabel.setWrapText(true);
        successLabel.managedProperty().bind(successLabel.textProperty().isNotEmpty());
        successLabel.visibleProperty().bind(successLabel.textProperty().isNotEmpty());

        messageBox.getChildren().addAll(errorLabel, successLabel);

        return messageBox;
    }

    private void addDetailRow(GridPane grid, int row, String label, String value) {
        Label labelNode = new Label(label);
        labelNode.setStyle("-fx-font-weight: bold;");
        Label valueNode = new Label(value);

        grid.add(labelNode, 0, row);
        grid.add(valueNode, 1, row);
    }

    private String formatEnergy(Energy energy) {
        if (energy == null) return "N/A";
        switch (energy) {
            case GASOLINE: return "Essence";
            case DIESEL: return "Diesel";
            case ELECTRIC: return "Électrique";
            case HYBRID: return "Hybride";
            case NATURAL_GAS: return "Gaz naturel";
            case NONE: return "Aucun";
            default: return "Non spécifié";
        }
    }

    private String formatStatus(Status status) {
        if (status == null) return "N/A";
        switch (status) {
            case AVAILABLE: return "Disponible";
            case RESERVED: return "Réservé";
            case MAINTENANCE: return "En maintenance";
            default: return "Inconnu";
        }
    }

    private void showAddVehicleDialog() {
        Dialog<Vehicle> dialog = new Dialog<>();
        dialog.setTitle("Ajouter un véhicule");
        dialog.setHeaderText("Saisir les informations du véhicule");

        ButtonType addButtonType = new ButtonType("Ajouter", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        GridPane grid = createVehicleFormGrid(null);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                return extractVehicleFromForm(grid, UUID.randomUUID());
            }
            return null;
        });

        Optional<Vehicle> result = dialog.showAndWait();
        result.ifPresent(addVehicleAction);
    }

    private void showEditVehicleDialog() {
        Vehicle selected = model.getSelectedVehicle();
        if (selected == null) return;

        Dialog<Vehicle> dialog = new Dialog<>();
        dialog.setTitle("Modifier un véhicule");
        dialog.setHeaderText("Modifier les informations du véhicule");

        ButtonType saveButtonType = new ButtonType("Enregistrer", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        GridPane grid = createVehicleFormGrid(selected);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                return extractVehicleFromForm(grid, selected.getUuid());
            }
            return null;
        });

        Optional<Vehicle> result = dialog.showAndWait();
        result.ifPresent(editVehicleAction);
    }

    private GridPane createVehicleFormGrid(Vehicle vehicle) {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        int row = 0;

        TextField licensePlateField = new TextField(vehicle != null ? vehicle.getLicencePlate() : "");
        grid.add(new Label("Plaque d'immatriculation:"), 0, row);
        grid.add(licensePlateField, 1, row++);
        licensePlateField.setUserData("licensePlate");

        TextField manufacturerField = new TextField(vehicle != null ? vehicle.getManufacturer() : "");
        grid.add(new Label("Constructeur:"), 0, row);
        grid.add(manufacturerField, 1, row++);
        manufacturerField.setUserData("manufacturer");

        TextField modelField = new TextField(vehicle != null ? vehicle.getModel() : "");
        grid.add(new Label("Modèle:"), 0, row);
        grid.add(modelField, 1, row++);
        modelField.setUserData("model");

        ComboBox<Energy> energyCombo = new ComboBox<>();
        energyCombo.getItems().addAll(Energy.values());
        energyCombo.setValue(vehicle != null ? vehicle.getEnergy() : Energy.NOT_SPECIFIED);
        grid.add(new Label("Énergie:"), 0, row);
        grid.add(energyCombo, 1, row++);
        energyCombo.setUserData("energy");

        TextField powerField = new TextField(vehicle != null ? String.valueOf(vehicle.getPower()) : "");
        grid.add(new Label("Puissance (CV):"), 0, row);
        grid.add(powerField, 1, row++);
        powerField.setUserData("power");

        TextField seatsField = new TextField(vehicle != null ? String.valueOf(vehicle.getSeats()) : "");
        grid.add(new Label("Nombre de sièges:"), 0, row);
        grid.add(seatsField, 1, row++);
        seatsField.setUserData("seats");

        TextField capacityField = new TextField(vehicle != null ? String.valueOf(vehicle.getCapacity()) : "");
        grid.add(new Label("Capacité (L):"), 0, row);
        grid.add(capacityField, 1, row++);
        capacityField.setUserData("capacity");

        TextField utilityWeightField = new TextField(vehicle != null ? String.valueOf(vehicle.getUtilityWeight()) : "");
        grid.add(new Label("Poids utilitaire (kg):"), 0, row);
        grid.add(utilityWeightField, 1, row++);
        utilityWeightField.setUserData("utilityWeight");

        TextField colorField = new TextField(vehicle != null ? vehicle.getColor() : "");
        grid.add(new Label("Couleur:"), 0, row);
        grid.add(colorField, 1, row++);
        colorField.setUserData("color");

        TextField kilometersField = new TextField(vehicle != null ? String.valueOf(vehicle.getKilometers()) : "");
        grid.add(new Label("Kilométrage:"), 0, row);
        grid.add(kilometersField, 1, row++);
        kilometersField.setUserData("kilometers");

        DatePicker acquisitionDatePicker = new DatePicker(
            vehicle != null && vehicle.getAcquisitionDate() != null ?
            vehicle.getAcquisitionDate().toLocalDate() : LocalDate.now()
        );
        grid.add(new Label("Date d'acquisition:"), 0, row);
        grid.add(acquisitionDatePicker, 1, row++);
        acquisitionDatePicker.setUserData("acquisitionDate");

        ComboBox<Status> statusCombo = new ComboBox<>();
        statusCombo.getItems().addAll(Status.values());
        statusCombo.setValue(vehicle != null ? vehicle.getStatus() : Status.AVAILABLE);
        grid.add(new Label("Statut:"), 0, row);
        grid.add(statusCombo, 1, row++);
        statusCombo.setUserData("status");

        return grid;
    }

    private Vehicle extractVehicleFromForm(GridPane grid, UUID uuid) {
        String licensePlate = "";
        String manufacturer = "";
        String model = "";
        Energy energy = Energy.NOT_SPECIFIED;
        int power = 0;
        int seats = 0;
        int capacity = 0;
        int utilityWeight = 0;
        String color = "";
        int kilometers = 0;
        LocalDateTime acquisitionDate = LocalDateTime.now();
        Status status = Status.AVAILABLE;

        for (javafx.scene.Node node : grid.getChildren()) {
            Object userData = node.getUserData();
            if (userData == null) continue;

            switch (userData.toString()) {
                case "licensePlate":
                    licensePlate = ((TextField) node).getText();
                    break;
                case "manufacturer":
                    manufacturer = ((TextField) node).getText();
                    break;
                case "model":
                    model = ((TextField) node).getText();
                    break;
                case "energy":
                    energy = ((ComboBox<Energy>) node).getValue();
                    break;
                case "power":
                    try {
                        power = Integer.parseInt(((TextField) node).getText());
                    } catch (NumberFormatException e) {
                        power = 0;
                    }
                    break;
                case "seats":
                    try {
                        seats = Integer.parseInt(((TextField) node).getText());
                    } catch (NumberFormatException e) {
                        seats = 0;
                    }
                    break;
                case "capacity":
                    try {
                        capacity = Integer.parseInt(((TextField) node).getText());
                    } catch (NumberFormatException e) {
                        capacity = 0;
                    }
                    break;
                case "utilityWeight":
                    try {
                        utilityWeight = Integer.parseInt(((TextField) node).getText());
                    } catch (NumberFormatException e) {
                        utilityWeight = 0;
                    }
                    break;
                case "color":
                    color = ((TextField) node).getText();
                    break;
                case "kilometers":
                    try {
                        kilometers = Integer.parseInt(((TextField) node).getText());
                    } catch (NumberFormatException e) {
                        kilometers = 0;
                    }
                    break;
                case "acquisitionDate":
                    LocalDate date = ((DatePicker) node).getValue();
                    acquisitionDate = date != null ? date.atTime(LocalTime.MIDNIGHT) : LocalDateTime.now();
                    break;
                case "status":
                    status = ((ComboBox<Status>) node).getValue();
                    break;
            }
        }

        return new Vehicle(uuid, licensePlate, manufacturer, model, energy, power, seats,
                          capacity, utilityWeight, color, kilometers, acquisitionDate, status);
    }

    private void confirmDeleteVehicle() {
        Vehicle selected = model.getSelectedVehicle();
        if (selected == null) return;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Supprimer le véhicule");
        alert.setContentText("Êtes-vous sûr de vouloir supprimer ce véhicule ?\n\n" +
                           selected.getManufacturer() + " " + selected.getModel() +
                           " (" + selected.getLicencePlate() + ")\n\n" +
                           "Cette action est irréversible.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            deleteVehicleAction.accept(selected.getUuid());
        }
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

        for (javafx.scene.Node node : grid.getChildren()) {
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
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            return java.util.HexFormat.of().formatHex(hash);
        } catch (java.security.NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to hash password", e);
        }
    }

    private Region createPendingReservationsPane() {
        BorderPane pane = new BorderPane();
        pane.setPadding(new Insets(10));

        VBox listPane = new VBox(10);
        listPane.setPrefWidth(400);

        Label listTitle = new Label("Réservations en Attente d'Approbation");
        listTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        ListView<Reservation> reservationListView = new ListView<>();
        reservationListView.setItems(model.pendingReservationsProperty());
        reservationListView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Reservation reservation, boolean empty) {
                super.updateItem(reservation, empty);
                if (empty || reservation == null) {
                    setText(null);
                } else {
                    String agentName = reservation.getAgent() != null ?
                        reservation.getAgent().getFirstname() + " " + reservation.getAgent().getLastname() : "Unknown";
                    String vehicleInfo = reservation.getVehicle() != null ?
                        reservation.getVehicle().getManufacturer() + " " + reservation.getVehicle().getModel() : "Unknown";
                    setText(agentName + " - " + vehicleInfo);
                }
            }
        });

        reservationListView.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldVal, newVal) -> model.setSelectedReservation(newVal)
        );

        VBox.setVgrow(reservationListView, Priority.ALWAYS);

        HBox buttonBox = new HBox(10);
        Button approveBtn = new Button("Approuver");
        approveBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        approveBtn.setOnAction(e -> confirmApproveReservation());
        approveBtn.disableProperty().bind(model.selectedReservationProperty().isNull());

        Button cancelBtn = new Button("Refuser");
        cancelBtn.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
        cancelBtn.setOnAction(e -> confirmCancelReservation());
        cancelBtn.disableProperty().bind(model.selectedReservationProperty().isNull());

        Button refreshBtn = new Button("Actualiser");
        refreshBtn.setOnAction(e -> loadPendingReservationsAction.run());

        buttonBox.getChildren().addAll(approveBtn, cancelBtn, refreshBtn);

        listPane.getChildren().addAll(listTitle, reservationListView, buttonBox);

        pane.setLeft(listPane);

        VBox detailsPane = createReservationDetailsPane();
        pane.setCenter(detailsPane);

        return pane;
    }

    private VBox createReservationDetailsPane() {
        VBox detailsPane = new VBox(10);
        detailsPane.setPadding(new Insets(20));
        detailsPane.setAlignment(Pos.TOP_LEFT);

        Label noSelectionLabel = new Label("Sélectionnez une réservation pour voir ses détails");

        VBox detailsBox = new VBox(15);
        Label titleLabel = new Label();
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        GridPane detailsGrid = new GridPane();
        detailsGrid.setHgap(15);
        detailsGrid.setVgap(10);

        model.selectedReservationProperty().addListener((obs, oldVal, newVal) -> {
            detailsGrid.getChildren().clear();
            if (newVal != null) {
                titleLabel.setText("Détails de la Réservation");

                addDetailRow(detailsGrid, 0, "UUID:", newVal.getUuid().toString());

                if (newVal.getAgent() != null) {
                    addDetailRow(detailsGrid, 1, "Agent:",
                        newVal.getAgent().getFirstname() + " " + newVal.getAgent().getLastname());
                    addDetailRow(detailsGrid, 2, "Email:", newVal.getAgent().getEmail());
                }

                if (newVal.getVehicle() != null) {
                    addDetailRow(detailsGrid, 3, "Véhicule:",
                        newVal.getVehicle().getManufacturer() + " " + newVal.getVehicle().getModel());
                    addDetailRow(detailsGrid, 4, "Plaque:", newVal.getVehicle().getLicencePlate());
                }

                addDetailRow(detailsGrid, 5, "Date de début:",
                    newVal.getStartDate() != null ?
                    newVal.getStartDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) : "N/A");
                addDetailRow(detailsGrid, 6, "Date de fin:",
                    newVal.getEndDate() != null ?
                    newVal.getEndDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) : "N/A");
                addDetailRow(detailsGrid, 7, "Statut:", formatReservationStatus(newVal.getStatus()));
            }
        });

        detailsBox.getChildren().addAll(titleLabel, new Separator(), detailsGrid);
        detailsBox.visibleProperty().bind(model.selectedReservationProperty().isNotNull());

        noSelectionLabel.visibleProperty().bind(model.selectedReservationProperty().isNull());

        detailsPane.getChildren().addAll(noSelectionLabel, detailsBox);

        return detailsPane;
    }

    private String formatReservationStatus(ReservationStatus status) {
        if (status == null) return "N/A";
        switch (status) {
            case PENDING: return "En Attente";
            case CONFIRMED: return "Confirmée";
            case CANCELLED: return "Annulée";
            case COMPLETED: return "Terminée";
            default: return "Inconnu";
        }
    }

    private void confirmApproveReservation() {
        Reservation selected = model.getSelectedReservation();
        if (selected == null) return;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation d'approbation");
        alert.setHeaderText("Approuver la réservation");

        String agentName = selected.getAgent() != null ?
            selected.getAgent().getFirstname() + " " + selected.getAgent().getLastname() : "Unknown";
        String vehicleInfo = selected.getVehicle() != null ?
            selected.getVehicle().getManufacturer() + " " + selected.getVehicle().getModel() : "Unknown";

        alert.setContentText("Êtes-vous sûr de vouloir approuver cette réservation ?\n\n" +
                           "Agent: " + agentName + "\n" +
                           "Véhicule: " + vehicleInfo + "\n" +
                           "Du: " + selected.getStartDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) + "\n" +
                           "Au: " + selected.getEndDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            approveReservationAction.accept(selected.getUuid(), selected.getVehicle().getUuid());
        }
    }

    private void confirmCancelReservation() {
        Reservation selected = model.getSelectedReservation();
        if (selected == null) return;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de refus");
        alert.setHeaderText("Refuser la réservation");

        String agentName = selected.getAgent() != null ?
            selected.getAgent().getFirstname() + " " + selected.getAgent().getLastname() : "Unknown";
        String vehicleInfo = selected.getVehicle() != null ?
            selected.getVehicle().getManufacturer() + " " + selected.getVehicle().getModel() : "Unknown";

        alert.setContentText("Êtes-vous sûr de vouloir refuser cette réservation ?\n\n" +
                           "Agent: " + agentName + "\n" +
                           "Véhicule: " + vehicleInfo + "\n" +
                           "Du: " + selected.getStartDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) + "\n" +
                           "Au: " + selected.getEndDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) + "\n\n" +
                           "Cette action est irréversible.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            cancelReservationAction.accept(selected.getUuid());
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
