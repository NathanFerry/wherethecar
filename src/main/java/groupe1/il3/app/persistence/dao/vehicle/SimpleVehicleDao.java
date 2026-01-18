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

        try {

            Connection conn = connectionManager.getNewConnection();
            PreparedStatement stmt = conn.prepareStatement(query);

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

        try {

            Connection conn = connectionManager.getNewConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

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

    @Override
    public void updateVehicleStatus(UUID vehicleUuid, String status) {
        String query = "UPDATE vehicle SET status = ?::vehicle_status WHERE uuid = ?";

        try {
            Connection conn = connectionManager.getNewConnection();
            PreparedStatement stmt = conn.prepareStatement(query);

            stmt.setString(1, status);
            stmt.setObject(2, vehicleUuid);

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to update vehicle status for UUID: " + vehicleUuid, e);
        } catch (DatabaseException e) {
            throw new RuntimeException("Database connection error while updating vehicle status", e);
        }
    }

    @Override
    public void createVehicle(VehicleDto vehicleDto) {
        String query = "INSERT INTO vehicle (uuid, license_plate, manufacturer, model, energy, power, " +
                       "seats, capacity, utility_weight, color, kilometers, acquisition_date, status) " +
                       "VALUES (?, ?, ?, ?, ?::energy, ?, ?, ?, ?, ?, ?, ?, ?::vehicle_status)";

        try {

            Connection conn = connectionManager.getNewConnection();
            PreparedStatement stmt = conn.prepareStatement(query);

            stmt.setObject(1, vehicleDto.getUuid());
            stmt.setString(2, vehicleDto.getLicensePlate());
            stmt.setString(3, vehicleDto.getManufacturer());
            stmt.setString(4, vehicleDto.getModel());
            stmt.setString(5, vehicleDto.getEnergy());
            stmt.setInt(6, vehicleDto.getPower());
            stmt.setInt(7, vehicleDto.getSeats());
            stmt.setInt(8, vehicleDto.getCapacity());
            stmt.setInt(9, vehicleDto.getUtilityWeight());
            stmt.setString(10, vehicleDto.getColor());
            stmt.setInt(11, vehicleDto.getKilometers());
            stmt.setTimestamp(12, vehicleDto.getAcquisitionDate() != null ?
                Timestamp.valueOf(vehicleDto.getAcquisitionDate()) : null);
            stmt.setString(13, vehicleDto.getStatus());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to create vehicle", e);
        } catch (DatabaseException e) {
            throw new RuntimeException("Database connection error while creating vehicle", e);
        }
    }

    @Override
    public void updateVehicle(VehicleDto vehicleDto) {
        String query = "UPDATE vehicle SET license_plate = ?, manufacturer = ?, model = ?, " +
                       "energy = ?::energy, power = ?, seats = ?, capacity = ?, utility_weight = ?, " +
                       "color = ?, kilometers = ?, acquisition_date = ?, status = ?::vehicle_status " +
                       "WHERE uuid = ?";

        try {

            Connection conn = connectionManager.getNewConnection();
            PreparedStatement stmt = conn.prepareStatement(query);

            stmt.setString(1, vehicleDto.getLicensePlate());
            stmt.setString(2, vehicleDto.getManufacturer());
            stmt.setString(3, vehicleDto.getModel());
            stmt.setString(4, vehicleDto.getEnergy());
            stmt.setInt(5, vehicleDto.getPower());
            stmt.setInt(6, vehicleDto.getSeats());
            stmt.setInt(7, vehicleDto.getCapacity());
            stmt.setInt(8, vehicleDto.getUtilityWeight());
            stmt.setString(9, vehicleDto.getColor());
            stmt.setInt(10, vehicleDto.getKilometers());
            stmt.setTimestamp(11, vehicleDto.getAcquisitionDate() != null ?
                Timestamp.valueOf(vehicleDto.getAcquisitionDate()) : null);
            stmt.setString(12, vehicleDto.getStatus());
            stmt.setObject(13, vehicleDto.getUuid());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to update vehicle with UUID: " + vehicleDto.getUuid(), e);
        } catch (DatabaseException e) {
            throw new RuntimeException("Database connection error while updating vehicle", e);
        }
    }

    @Override
    public void deleteVehicle(UUID vehicleUuid) {
        String query = "DELETE FROM vehicle WHERE uuid = ?";

        try {

            Connection conn = connectionManager.getNewConnection();
            PreparedStatement stmt = conn.prepareStatement(query);

            stmt.setObject(1, vehicleUuid);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete vehicle with UUID: " + vehicleUuid, e);
        } catch (DatabaseException e) {
            throw new RuntimeException("Database connection error while deleting vehicle", e);
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
