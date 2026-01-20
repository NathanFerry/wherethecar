package groupe1.il3.app.persistence.broker.maintenance;

import groupe1.il3.app.domain.maintenance.MaintenanceOperation;
import groupe1.il3.app.persistence.dao.maintenance.MaintenanceOperationDao;
import groupe1.il3.app.persistence.dao.maintenance.SimpleMaintenanceOperationDao;
import groupe1.il3.app.persistence.dto.maintenance.MaintenanceOperationDto;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class MaintenanceOperationBroker {

    private final MaintenanceOperationDao maintenanceOperationDao;

    public MaintenanceOperationBroker() {
        this.maintenanceOperationDao = new SimpleMaintenanceOperationDao();
    }

    public MaintenanceOperation getMaintenanceOperationById(UUID uuid) {
        MaintenanceOperationDto dto = maintenanceOperationDao.getMaintenanceOperationById(uuid);
        return dto != null ? convertToMaintenanceOperation(dto) : null;
    }

    public List<MaintenanceOperation> getMaintenanceOperationsByVehicleUuid(UUID vehicleUuid) {
        return maintenanceOperationDao.getMaintenanceOperationsByVehicleUuid(vehicleUuid).stream()
                .map(this::convertToMaintenanceOperation)
                .collect(Collectors.toList());
    }

    public List<MaintenanceOperation> getAllMaintenanceOperations() {
        return maintenanceOperationDao.getAllMaintenanceOperations().stream()
                .map(this::convertToMaintenanceOperation)
                .collect(Collectors.toList());
    }

    public void createMaintenanceOperation(MaintenanceOperation maintenanceOperation) {
        MaintenanceOperationDto dto = new MaintenanceOperationDto(
            maintenanceOperation.uuid(),
            maintenanceOperation.vehicleUuid(),
            maintenanceOperation.name(),
            maintenanceOperation.description(),
            maintenanceOperation.operationDate(),
            maintenanceOperation.cost()
        );
        maintenanceOperationDao.createMaintenanceOperation(dto);
    }

    public void updateMaintenanceOperation(MaintenanceOperation maintenanceOperation) {
        MaintenanceOperationDto dto = new MaintenanceOperationDto(
            maintenanceOperation.uuid(),
            maintenanceOperation.vehicleUuid(),
            maintenanceOperation.name(),
            maintenanceOperation.description(),
            maintenanceOperation.operationDate(),
            maintenanceOperation.cost()
        );
        maintenanceOperationDao.updateMaintenanceOperation(dto);
    }

    public void deleteMaintenanceOperation(UUID uuid) {
        maintenanceOperationDao.deleteMaintenanceOperation(uuid);
    }

    private MaintenanceOperation convertToMaintenanceOperation(MaintenanceOperationDto dto) {
        return new MaintenanceOperation(
            dto.uuid(),
            dto.vehicleUuid(),
            dto.name(),
            dto.description(),
            dto.operationDate(),
            dto.cost()
        );
    }
}
