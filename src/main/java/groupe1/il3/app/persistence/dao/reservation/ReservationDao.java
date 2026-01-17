package groupe1.il3.app.persistence.dao.reservation;

import groupe1.il3.app.persistence.dto.reservation.ReservationDto;

import java.util.List;
import java.util.UUID;

public interface ReservationDao {
    public ReservationDto getReservationById(UUID uuid);
    public List<ReservationDto> getAllReservations();
}
