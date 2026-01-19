package groupe1.il3.app.gui.admin.maintenancemanagement;

import groupe1.il3.app.domain.maintenance.MaintenanceOperation;
import groupe1.il3.app.persistence.broker.maintenance.MaintenanceOperationBroker;
import javafx.concurrent.Task;

import java.util.List;
import java.util.UUID;

public class MaintenanceManagementInteractor {

    private final MaintenanceOperationBroker maintenanceOperationBroker;

    public MaintenanceManagementInteractor() {
        this.maintenanceOperationBroker = new MaintenanceOperationBroker();
    }

    public Task<List<MaintenanceOperation>> createLoadMaintenanceOperationsTask(UUID vehicleUuid) {
        return new Task<>() {
            @Override
            protected List<MaintenanceOperation> call() {
                return maintenanceOperationBroker.getMaintenanceOperationsByVehicleUuid(vehicleUuid);
            }
        };
    }

    public Task<Void> createMaintenanceOperationTask(MaintenanceOperation maintenanceOperation) {
        return new Task<>() {
            @Override
            protected Void call() {
                maintenanceOperationBroker.createMaintenanceOperation(maintenanceOperation);
                return null;
            }
        };
    }

    public Task<Void> updateMaintenanceOperationTask(MaintenanceOperation maintenanceOperation) {
        return new Task<>() {
            @Override
            protected Void call() {
                maintenanceOperationBroker.updateMaintenanceOperation(maintenanceOperation);
                return null;
            }
        };
    }

    public Task<Void> deleteMaintenanceOperationTask(UUID operationUuid) {
        return new Task<>() {
            @Override
            protected Void call() {
                maintenanceOperationBroker.deleteMaintenanceOperation(operationUuid);
                return null;
            }
        };
    }
}
