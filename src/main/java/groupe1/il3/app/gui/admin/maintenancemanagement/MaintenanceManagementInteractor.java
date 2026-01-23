package groupe1.il3.app.gui.admin.maintenancemanagement;

import groupe1.il3.app.domain.maintenance.MaintenanceOperation;
import groupe1.il3.app.persistence.broker.maintenance.MaintenanceOperationBroker;

import java.util.List;
import java.util.UUID;

public class MaintenanceManagementInteractor {

    private final MaintenanceManagementModel model;
    private final MaintenanceOperationBroker maintenanceOperationBroker;

    public MaintenanceManagementInteractor(MaintenanceManagementModel model) {
        this.model = model;
        this.maintenanceOperationBroker = new MaintenanceOperationBroker();
    }

    public List<MaintenanceOperation> fetchMaintenanceOperations(UUID vehicleUuid) {
        return maintenanceOperationBroker.getMaintenanceOperationsByVehicleUuid(vehicleUuid);
    }

    public void createMaintenanceOperation(MaintenanceOperation maintenanceOperation) {
        maintenanceOperationBroker.createMaintenanceOperation(maintenanceOperation);
    }

    public void updateMaintenanceOperation(MaintenanceOperation maintenanceOperation) {
        maintenanceOperationBroker.updateMaintenanceOperation(maintenanceOperation);
    }

    public void deleteMaintenanceOperation(UUID operationUuid) {
        maintenanceOperationBroker.deleteMaintenanceOperation(operationUuid);
    }

    public void updateMaintenanceOperationsList(List<MaintenanceOperation> operations) {
        model.maintenanceOperationsProperty().clear();
        model.maintenanceOperationsProperty().addAll(operations);
    }
}
