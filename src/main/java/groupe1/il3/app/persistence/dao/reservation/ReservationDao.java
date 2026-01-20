package groupe1.il3.app.persistence.dao.reservation;

import groupe1.il3.app.persistence.dto.reservation.ReservationDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface ReservationDao {
    public ReservationDto getReservationById(UUID uuid);
    public List<ReservationDto> getAllReservations();
    public List<ReservationDto> getActiveReservationsByAgentUuid(UUID agentUuid);
    public List<ReservationDto> getHistoricalReservationsByAgentUuid(UUID agentUuid);
    public List<ReservationDto> getReservationsByVehicleUuid(UUID vehicleUuid);
    public boolean hasOverlappingReservation(UUID vehicleUuid, LocalDateTime startDate, LocalDateTime endDate);
    public void createReservation(UUID uuid, UUID agentUuid, UUID vehicleUuid, LocalDateTime startDate, LocalDateTime endDate, String status);
    public void updateReservationStatus(UUID reservationUuid, String status);
}
