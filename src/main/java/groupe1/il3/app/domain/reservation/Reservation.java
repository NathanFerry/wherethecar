package groupe1.il3.app.domain.reservation;

import groupe1.il3.app.domain.agent.Agent;
import groupe1.il3.app.domain.vehicle.Vehicle;

import java.time.LocalDateTime;
import java.util.UUID;

public class Reservation {
    private UUID uuid;
    private Agent agent;
    private Vehicle vehicle;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public Reservation(
        UUID uuid,
        Agent agent,
        Vehicle vehicle,
        LocalDateTime startDate,
        LocalDateTime endDate
    ) {
        this.uuid = uuid;
        this.agent = agent;
        this.vehicle = vehicle;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
