package groupe1.il3.app.persistence.dto.reservation;

import groupe1.il3.app.domain.agent.Agent;
import groupe1.il3.app.domain.reservation.Reservation;
import groupe1.il3.app.domain.reservation.ReservationStatus;
import groupe1.il3.app.domain.vehicle.Vehicle;

import java.time.LocalDateTime;
import java.util.UUID;

public record ReservationDto(
        UUID uuid,
        UUID agentUuid,
        UUID vehicleUuid,
        LocalDateTime start,
        LocalDateTime end,
        String status
) {
    public static ReservationDto fromDomainObject(Reservation reservation) {
        return new ReservationDto(
                reservation.uuid(),
                reservation.agent().uuid(),
                reservation.vehicle().uuid(),
                reservation.startDate(),
                reservation.endDate(),
                reservation.status().toString().toLowerCase()
        );
    }

    public Reservation toDomainObject(Agent agent, Vehicle vehicle) {
        return new Reservation(
                this.uuid,
                agent,
                vehicle,
                this.start,
                this.end,
                ReservationStatus.valueOf(this.status.toUpperCase())
        );
    }
}
