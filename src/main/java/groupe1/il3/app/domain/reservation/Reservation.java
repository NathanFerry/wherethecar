package groupe1.il3.app.domain.reservation;

import groupe1.il3.app.domain.agent.Agent;
import groupe1.il3.app.domain.vehicle.Vehicle;

import java.time.LocalDateTime;
import java.util.UUID;

public class Reservation {
    private final UUID uuid;
    private final Agent agent;
    private final Vehicle vehicle;
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;
    private final ReservationStatus status;

    public Reservation(
        UUID uuid,
        Agent agent,
        Vehicle vehicle,
        LocalDateTime startDate,
        LocalDateTime endDate,
        ReservationStatus status
    ) {
        this.uuid = uuid;
        this.agent = agent;
        this.vehicle = vehicle;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }

    public UUID getUuid() {
        return uuid;
    }

    public Agent getAgent() {
        return agent;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public ReservationStatus getStatus() {
        return status;
    }
}
