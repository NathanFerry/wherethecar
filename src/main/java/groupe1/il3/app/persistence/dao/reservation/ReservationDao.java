package groupe1.il3.app.persistence.dao.reservation;

import groupe1.il3.app.persistence.dto.reservation.ReservationDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface ReservationDao {
    ReservationDto getReservationById(UUID uuid);
    List<ReservationDto> getAllReservations();
    List<ReservationDto> getActiveReservationsByAgentUuid(UUID agentUuid);
    List<ReservationDto> getHistoricalReservationsByAgentUuid(UUID agentUuid);
    List<ReservationDto> getReservationsByVehicleUuid(UUID vehicleUuid);
    boolean hasOverlappingReservation(UUID vehicleUuid, LocalDateTime startDate, LocalDateTime endDate);
    void createReservation(UUID uuid, UUID agentUuid, UUID vehicleUuid, LocalDateTime startDate, LocalDateTime endDate, String status);
    void updateReservationStatus(UUID reservationUuid, String status);
}
