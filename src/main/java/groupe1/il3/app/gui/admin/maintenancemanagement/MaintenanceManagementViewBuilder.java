package groupe1.il3.app.gui.admin.maintenancemanagement;

import groupe1.il3.app.domain.maintenance.MaintenanceOperation;
import groupe1.il3.app.gui.style.StyleApplier;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class MaintenanceManagementViewBuilder implements Builder<Region> {

    private final MaintenanceManagementModel model;
    private final Runnable loadOperationsAction;
    private final Consumer<MaintenanceOperation> addOperationAction;
    private final Consumer<MaintenanceOperation> editOperationAction;
    private final Consumer<UUID> deleteOperationAction;
    private final Supplier<UUID> vehicleUuidSupplier;

    public MaintenanceManagementViewBuilder(
        MaintenanceManagementModel model,
        Runnable loadOperationsAction,
        Consumer<MaintenanceOperation> addOperationAction,
        Consumer<MaintenanceOperation> editOperationAction,
        Consumer<UUID> deleteOperationAction,
        Supplier<UUID> vehicleUuidSupplier
    ) {
        this.model = model;
        this.loadOperationsAction = loadOperationsAction;
        this.addOperationAction = addOperationAction;
        this.editOperationAction = editOperationAction;
        this.deleteOperationAction = deleteOperationAction;
        this.vehicleUuidSupplier = vehicleUuidSupplier;
    }

    @Override
    public Region build() {
        VBox mainPane = new VBox(10);
        mainPane.setPadding(new Insets(10));

        Label title = new Label("Opérations de Maintenance");
        title.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        ListView<MaintenanceOperation> operationListView = new ListView<>();
        operationListView.setItems(model.maintenanceOperationsProperty());
        operationListView.setPrefHeight(200);
        operationListView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(MaintenanceOperation operation, boolean empty) {
                super.updateItem(operation, empty);
                if (empty || operation == null) {
                    setText(null);
                } else {
                    String dateStr = operation.operationDate() != null ?
                        operation.operationDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "N/A";
                    setText(dateStr + " - " + operation.name() + " (" +
                           operation.cost() + " €)");
                }
            }
        });

        operationListView.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldVal, newVal) -> model.setSelectedOperation(newVal)
        );

        HBox buttonBox = new HBox(5);
        buttonBox.setAlignment(Pos.CENTER_LEFT);
        buttonBox.getStyleClass().add("admin-button-box");

        Button addBtn = new Button("Ajouter");
        addBtn.getStyleClass().add("admin-add-button");
        addBtn.setOnAction(e -> showAddOperationDialog());

        Button editBtn = new Button("Modifier");
        editBtn.getStyleClass().add("admin-edit-button");
        editBtn.setOnAction(e -> showEditOperationDialog());
        editBtn.disableProperty().bind(model.selectedOperationProperty().isNull());

        Button deleteBtn = new Button("Supprimer");
        deleteBtn.getStyleClass().add("admin-delete-button");
        deleteBtn.setOnAction(e -> confirmDeleteOperation());
        deleteBtn.disableProperty().bind(model.selectedOperationProperty().isNull());

        Button refreshBtn = new Button("Actualiser");
        refreshBtn.getStyleClass().add("admin-refresh-button");
        refreshBtn.setOnAction(e -> loadOperationsAction.run());

        buttonBox.getChildren().addAll(addBtn, editBtn, deleteBtn, refreshBtn);

        VBox detailsPane = createDetailsPane();

        VBox.setVgrow(operationListView, Priority.ALWAYS);
        VBox.setVgrow(detailsPane, Priority.ALWAYS);

        mainPane.getChildren().addAll(title, operationListView, buttonBox, detailsPane);

        return mainPane;
    }

    private VBox createDetailsPane() {
        VBox detailsPane = new VBox(10);
        detailsPane.setPadding(new Insets(10));
        detailsPane.setStyle("-fx-border-color: #CCCCCC; -fx-border-width: 1; -fx-border-radius: 5; -fx-background-radius: 5;");

        Label noSelectionLabel = new Label("Sélectionnez une opération pour voir ses détails");

        VBox detailsBox = new VBox(10);
        Label nameLabel = new Label();
        nameLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        GridPane detailsGrid = new GridPane();
        detailsGrid.setHgap(10);
        detailsGrid.setVgap(8);

        model.selectedOperationProperty().addListener((obs, oldVal, newVal) -> {
            detailsGrid.getChildren().clear();
            if (newVal != null) {
                nameLabel.setText(newVal.name());

                addDetailRow(detailsGrid, 0, "Date:",
                    newVal.operationDate() != null ?
                    newVal.operationDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) : "N/A");
                addDetailRow(detailsGrid, 1, "Coût:", newVal.cost() + " €");

                Label descLabel = new Label("Description:");
                descLabel.getStyleClass().add("admin-detail-label");
                TextArea descText = new TextArea(newVal.description());
                descText.setWrapText(true);
                descText.setEditable(false);
                descText.setPrefRowCount(4);
                descText.getStyleClass().add("admin-detail-value");

                detailsGrid.add(descLabel, 0, 2);
                detailsGrid.add(descText, 1, 2);
            }
        });

        detailsBox.getChildren().addAll(nameLabel, new Separator(), detailsGrid);
        detailsBox.visibleProperty().bind(model.selectedOperationProperty().isNotNull());

        noSelectionLabel.visibleProperty().bind(model.selectedOperationProperty().isNull());

        detailsPane.getChildren().addAll(noSelectionLabel, detailsBox);

        return detailsPane;
    }

    private void addDetailRow(GridPane grid, int row, String label, String value) {
        Label labelNode = new Label(label);
        labelNode.getStyleClass().add("admin-detail-label");
        Label valueNode = new Label(value);
        valueNode.getStyleClass().add("admin-detail-value");

        grid.add(labelNode, 0, row);
        grid.add(valueNode, 1, row);
    }

    private void showAddOperationDialog() {
        Dialog<MaintenanceOperation> dialog = new Dialog<>();
        dialog.setTitle("Ajouter une opération de maintenance");
        dialog.setHeaderText("Saisir les informations de l'opération");

        StyleApplier.applyStylesheets(dialog);

        ButtonType addButtonType = new ButtonType("Ajouter", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        GridPane grid = createOperationFormGrid(null);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                return extractOperationFromForm(grid, UUID.randomUUID());
            }
            return null;
        });

        Optional<MaintenanceOperation> result = dialog.showAndWait();
        result.ifPresent(addOperationAction);
    }

    private void showEditOperationDialog() {
        MaintenanceOperation selected = model.getSelectedOperation();
        if (selected == null) return;

        Dialog<MaintenanceOperation> dialog = new Dialog<>();
        dialog.setTitle("Modifier une opération de maintenance");
        dialog.setHeaderText("Modifier les informations de l'opération");

        StyleApplier.applyStylesheets(dialog);

        ButtonType saveButtonType = new ButtonType("Enregistrer", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        GridPane grid = createOperationFormGrid(selected);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                return extractOperationFromForm(grid, selected.uuid());
            }
            return null;
        });

        Optional<MaintenanceOperation> result = dialog.showAndWait();
        result.ifPresent(editOperationAction);
    }

    private void confirmDeleteOperation() {
        MaintenanceOperation selected = model.getSelectedOperation();
        if (selected == null) return;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Supprimer l'opération de maintenance");
        alert.setContentText("Êtes-vous sûr de vouloir supprimer cette opération ?");

        StyleApplier.applyStylesheets(alert);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            deleteOperationAction.accept(selected.uuid());
        }
    }

    private GridPane createOperationFormGrid(MaintenanceOperation operation) {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        int row = 0;

        TextField nameField = new TextField(operation != null ? operation.name() : "");
        grid.add(new Label("Nom:"), 0, row);
        grid.add(nameField, 1, row++);
        nameField.setUserData("name");

        TextArea descriptionArea = new TextArea(operation != null ? operation.description() : "");
        descriptionArea.setPrefRowCount(4);
        descriptionArea.setWrapText(true);
        grid.add(new Label("Description:"), 0, row);
        grid.add(descriptionArea, 1, row++);
        descriptionArea.setUserData("description");

        DatePicker datePicker = new DatePicker(
            operation != null && operation.operationDate() != null ?
            operation.operationDate().toLocalDate() : LocalDate.now()
        );
        grid.add(new Label("Date:"), 0, row);
        grid.add(datePicker, 1, row++);
        datePicker.setUserData("date");

        TextField costField = new TextField(
            operation != null ? operation.cost().toString() : "0.00"
        );
        grid.add(new Label("Coût (€):"), 0, row);
        grid.add(costField, 1, row++);
        costField.setUserData("cost");

        return grid;
    }

    private MaintenanceOperation extractOperationFromForm(GridPane grid, UUID uuid) {
        String name = "";
        String description = "";
        LocalDateTime operationDate = LocalDateTime.now();
        BigDecimal cost = BigDecimal.ZERO;

        for (Node node : grid.getChildren()) {
            Object userData = node.getUserData();
            if (userData == null) continue;

            switch (userData.toString()) {
                case "name":
                    name = ((TextField) node).getText();
                    break;
                case "description":
                    description = ((TextArea) node).getText();
                    break;
                case "date":
                    LocalDate date = ((DatePicker) node).getValue();
                    operationDate = date != null ? date.atTime(LocalTime.MIDNIGHT) : LocalDateTime.now();
                    break;
                case "cost":
                    try {
                        cost = new BigDecimal(((TextField) node).getText());
                    } catch (NumberFormatException e) {
                        cost = BigDecimal.ZERO;
                    }
                    break;
            }
        }

        UUID vehicleUuid = vehicleUuidSupplier.get();
        return new MaintenanceOperation(uuid, vehicleUuid, name, description, operationDate, cost);
    }
}
