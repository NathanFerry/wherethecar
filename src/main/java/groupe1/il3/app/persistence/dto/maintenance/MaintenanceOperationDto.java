package groupe1.il3.app.persistence.dto.maintenance;

import groupe1.il3.app.domain.maintenance.MaintenanceOperation;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record MaintenanceOperationDto(
        UUID uuid,
        UUID vehicleUuid,
        String name,
        String description,
        LocalDateTime operationDate,
        BigDecimal cost
) {
    public static MaintenanceOperationDto fromDomainObject(MaintenanceOperation operation) {
        return new MaintenanceOperationDto(
                operation.uuid(),
                operation.vehicleUuid(),
                operation.name(),
                operation.description(),
                operation.operationDate(),
                operation.cost()
        );
    }

    public MaintenanceOperation toDomainObject() {
        return new MaintenanceOperation(
                uuid,
                vehicleUuid,
                name,
                description,
                operationDate,
                cost
        );
    }
}
