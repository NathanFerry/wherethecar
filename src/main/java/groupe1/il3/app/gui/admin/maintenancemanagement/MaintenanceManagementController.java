package groupe1.il3.app.gui.admin.maintenancemanagement;

import groupe1.il3.app.domain.maintenance.MaintenanceOperation;
import javafx.concurrent.Task;
import javafx.scene.layout.Region;
import javafx.util.Builder;

import java.util.List;
import java.util.UUID;
import java.util.function.BiConsumer;

public class MaintenanceManagementController {

    private final MaintenanceManagementModel model;
    private final MaintenanceManagementInteractor interactor;
    private final Builder<Region> viewBuilder;
    private final BiConsumer<String, String> messageHandler;
    private UUID currentVehicleUuid;

    public MaintenanceManagementController(
        UUID vehicleUuid,
        BiConsumer<String, String> messageHandler
    ) {
        this.model = new MaintenanceManagementModel();
        this.interactor = new MaintenanceManagementInteractor();
        this.messageHandler = messageHandler;
        this.currentVehicleUuid = vehicleUuid;
        this.viewBuilder = new MaintenanceManagementViewBuilder(
            model,
            this::loadMaintenanceOperations,
            this::addMaintenanceOperation,
            this::editMaintenanceOperation,
            this::deleteMaintenanceOperation,
            () -> this.currentVehicleUuid
        );
    }

    public Region getView() {
        return viewBuilder.build();
    }

    public void loadMaintenanceOperations() {
        if (currentVehicleUuid == null) {
            return;
        }

        messageHandler.accept("", "");

        Task<List<MaintenanceOperation>> task = interactor.createLoadMaintenanceOperationsTask(currentVehicleUuid);

        task.setOnSucceeded(event -> {
            model.maintenanceOperationsProperty().clear();
            model.maintenanceOperationsProperty().addAll(task.getValue());
        });

        task.setOnFailed(event -> {
            messageHandler.accept("Erreur lors du chargement des opérations: " + task.getException().getMessage(), "");
            task.getException().printStackTrace();
        });

        new Thread(task).start();
    }

    private void addMaintenanceOperation(MaintenanceOperation operation) {
        messageHandler.accept("", "");

        Task<Void> task = interactor.createMaintenanceOperationTask(operation);

        task.setOnSucceeded(event -> {
            messageHandler.accept("", "Opération de maintenance ajoutée avec succès");
            loadMaintenanceOperations();
        });

        task.setOnFailed(event -> {
            messageHandler.accept("Erreur lors de l'ajout de l'opération: " + task.getException().getMessage(), "");
            task.getException().printStackTrace();
        });

        new Thread(task).start();
    }

    private void editMaintenanceOperation(MaintenanceOperation operation) {
        messageHandler.accept("", "");

        Task<Void> task = interactor.updateMaintenanceOperationTask(operation);

        task.setOnSucceeded(event -> {
            messageHandler.accept("", "Opération de maintenance modifiée avec succès");
            loadMaintenanceOperations();
        });

        task.setOnFailed(event -> {
            messageHandler.accept("Erreur lors de la modification de l'opération: " + task.getException().getMessage(), "");
            task.getException().printStackTrace();
        });

        new Thread(task).start();
    }

    private void deleteMaintenanceOperation(UUID operationUuid) {
        messageHandler.accept("", "");

        Task<Void> task = interactor.deleteMaintenanceOperationTask(operationUuid);

        task.setOnSucceeded(event -> {
            messageHandler.accept("", "Opération de maintenance supprimée avec succès");
            loadMaintenanceOperations();
        });

        task.setOnFailed(event -> {
            messageHandler.accept("Erreur lors de la suppression de l'opération: " + task.getException().getMessage(), "");
            task.getException().printStackTrace();
        });

        new Thread(task).start();
    }

    public void setVehicleUuid(UUID vehicleUuid) {
        this.currentVehicleUuid = vehicleUuid;
        loadMaintenanceOperations();
    }
}
