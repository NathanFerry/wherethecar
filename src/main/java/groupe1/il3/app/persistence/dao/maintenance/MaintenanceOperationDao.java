package groupe1.il3.app.persistence.dao.maintenance;

import groupe1.il3.app.persistence.dto.maintenance.MaintenanceOperationDto;

import java.util.List;
import java.util.UUID;

public interface MaintenanceOperationDao {
    MaintenanceOperationDto getMaintenanceOperationById(UUID uuid);
    List<MaintenanceOperationDto> getMaintenanceOperationsByVehicleUuid(UUID vehicleUuid);
    List<MaintenanceOperationDto> getAllMaintenanceOperations();
    void createMaintenanceOperation(MaintenanceOperationDto maintenanceOperationDto);
    void updateMaintenanceOperation(MaintenanceOperationDto maintenanceOperationDto);
    void deleteMaintenanceOperation(UUID uuid);
}
