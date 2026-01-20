package groupe1.il3.app.domain.reservation;

import groupe1.il3.app.domain.agent.Agent;
import groupe1.il3.app.domain.vehicle.Vehicle;

import java.time.LocalDateTime;
import java.util.UUID;

public record Reservation(
        UUID uuid,
        Agent agent,
        Vehicle vehicle,
        LocalDateTime startDate,
        LocalDateTime endDate,
        ReservationStatus status
) {}
