package groupe1.il3.app.persistence.dto.maintenance;

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
) {}
