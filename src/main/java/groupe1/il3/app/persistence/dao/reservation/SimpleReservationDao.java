package groupe1.il3.app.persistence.dao.reservation;

import groupe1.il3.app.persistence.DatabaseConnectionManager;
import groupe1.il3.app.persistence.DatabaseException;
import groupe1.il3.app.persistence.dto.reservation.ReservationDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SimpleReservationDao implements ReservationDao {

    private final DatabaseConnectionManager connectionManager;

    public SimpleReservationDao() {
        this.connectionManager = DatabaseConnectionManager.getInstance();
    }

    @Override
    public ReservationDto getReservationById(UUID uuid) {
        String query = "SELECT uuid, agent_uuid, vehicle_uuid, start_date, end_date, status " +
                       "FROM reservation WHERE uuid = ?";

        try (Connection conn = connectionManager.getNewConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setObject(1, uuid);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToReservationDto(rs);
                }
                return null;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to retrieve reservation with UUID: " + uuid, e);
        } catch (DatabaseException e) {
            throw new RuntimeException("Database connection error while retrieving reservation", e);
        }
    }

    @Override
    public List<ReservationDto> getAllReservations() {
        String query = "SELECT uuid, agent_uuid, vehicle_uuid, start_date, end_date, status " +
                       "FROM reservation";

        List<ReservationDto> reservations = new ArrayList<>();

        try (Connection conn = connectionManager.getNewConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                reservations.add(mapResultSetToReservationDto(rs));
            }

            return reservations;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to retrieve all reservations", e);
        } catch (DatabaseException e) {
            throw new RuntimeException("Database connection error while retrieving reservations", e);
        }
    }

    private ReservationDto mapResultSetToReservationDto(ResultSet rs) throws SQLException {
        UUID uuid = (UUID) rs.getObject("uuid");
        UUID agentUuid = (UUID) rs.getObject("agent_uuid");
        UUID vehicleUuid = (UUID) rs.getObject("vehicle_uuid");
        Timestamp startTimestamp = rs.getTimestamp("start_date");
        LocalDateTime start = startTimestamp != null ? startTimestamp.toLocalDateTime() : null;
        Timestamp endTimestamp = rs.getTimestamp("end_date");
        LocalDateTime end = endTimestamp != null ? endTimestamp.toLocalDateTime() : null;
        String status = rs.getString("status");

        return new ReservationDto(
            uuid,
            agentUuid,
            vehicleUuid,
            start,
            end,
            status
        );
    }
}
