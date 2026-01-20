package groupe1.il3.app.domain.maintenance;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

//TODO: Change VehicleUuid to Vehicle object
public record MaintenanceOperation(
        UUID uuid,
        UUID vehicleUuid,
        String name,
        String description,
        LocalDateTime operationDate,
        BigDecimal cost
) {}
