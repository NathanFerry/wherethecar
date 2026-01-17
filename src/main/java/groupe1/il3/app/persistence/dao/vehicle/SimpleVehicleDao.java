package groupe1.il3.app.persistence.dao.vehicle;

import groupe1.il3.app.persistence.DatabaseConnectionManager;
import groupe1.il3.app.persistence.DatabaseException;
import groupe1.il3.app.persistence.dto.vehicle.VehicleDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SimpleVehicleDao implements VehicleDao {

    private final DatabaseConnectionManager connectionManager;

    public SimpleVehicleDao() {
        this.connectionManager = DatabaseConnectionManager.getInstance();
    }

    @Override
    public VehicleDto getVehicleById(UUID uuid) {
        String query = "SELECT uuid, license_plate, manufacturer, model, energy, power, " +
                       "seats, capacity, utility_weight, color, kilometers, acquisition_date, status " +
                       "FROM vehicle WHERE uuid = ?";

        try (Connection conn = connectionManager.getNewConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setObject(1, uuid);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToVehicleDto(rs);
                }
                return null;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to retrieve vehicle with UUID: " + uuid, e);
        } catch (DatabaseException e) {
            throw new RuntimeException("Database connection error while retrieving vehicle", e);
        }
    }

    @Override
    public List<VehicleDto> getAllVehicles() {
        String query = "SELECT uuid, license_plate, manufacturer, model, energy, power, " +
                       "seats, capacity, utility_weight, color, kilometers, acquisition_date, status " +
                       "FROM vehicle";

        List<VehicleDto> vehicles = new ArrayList<>();

        try (Connection conn = connectionManager.getNewConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                vehicles.add(mapResultSetToVehicleDto(rs));
            }

            return vehicles;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to retrieve all vehicles", e);
        } catch (DatabaseException e) {
            throw new RuntimeException("Database connection error while retrieving vehicles", e);
        }
    }

    private VehicleDto mapResultSetToVehicleDto(ResultSet rs) throws SQLException {
        UUID uuid = (UUID) rs.getObject("uuid");
        String licensePlate = rs.getString("license_plate");
        String manufacturer = rs.getString("manufacturer");
        String model = rs.getString("model");
        String energy = rs.getString("energy");
        Integer power = rs.getInt("power");
        Integer seats = rs.getInt("seats");
        Integer capacity = rs.getInt("capacity");
        Integer utilityWeight = rs.getInt("utility_weight");
        String color = rs.getString("color");
        Integer kilometers = rs.getInt("kilometers");
        Timestamp acquisitionDateTimestamp = rs.getTimestamp("acquisition_date");
        LocalDateTime acquisitionDate = acquisitionDateTimestamp != null ? acquisitionDateTimestamp.toLocalDateTime() : null;
        String status = rs.getString("status");

        return new VehicleDto(
            uuid,
            licensePlate,
            manufacturer,
            model,
            energy,
            power,
            seats,
            capacity,
            utilityWeight,
            color,
            kilometers,
            acquisitionDate,
            status
        );
    }
}
