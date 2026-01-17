package groupe1.il3.app.persistence.dto.reservation;

import java.time.LocalDateTime;
import java.util.UUID;

public class ReservationDto {
    private final UUID uuid;
    private final UUID agentUuid;
    private final UUID vehicleUuid;
    private final LocalDateTime start;
    private final LocalDateTime end;
    private final String status;

    public ReservationDto(
        UUID uuid,
        UUID agentUuid,
        UUID vehicleUuid,
        LocalDateTime start,
        LocalDateTime end,
        String status
    ) {
        this.uuid = uuid;
        this.agentUuid = agentUuid;
        this.vehicleUuid = vehicleUuid;
        this.start = start;
        this.end = end;
        this.status = status;
    }

    public UUID getUuid() {
        return uuid;
    }

    public UUID getAgentUuid() {
        return agentUuid;
    }

    public UUID getVehicleUuid() {
        return vehicleUuid;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public String getStatus() {
        return status;
    }
}
