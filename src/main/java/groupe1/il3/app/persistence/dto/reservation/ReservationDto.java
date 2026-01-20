package groupe1.il3.app.persistence.dto.reservation;

import java.time.LocalDateTime;
import java.util.UUID;

public record ReservationDto(
        UUID uuid,
        UUID agentUuid,
        UUID vehicleUuid,
        LocalDateTime start,
        LocalDateTime end,
        String status
) {}
