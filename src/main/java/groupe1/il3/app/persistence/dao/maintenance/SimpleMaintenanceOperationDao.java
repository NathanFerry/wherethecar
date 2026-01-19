package groupe1.il3.app.persistence.dao.maintenance;

import groupe1.il3.app.persistence.DatabaseConnectionManager;
import groupe1.il3.app.persistence.DatabaseException;
import groupe1.il3.app.persistence.dto.maintenance.MaintenanceOperationDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SimpleMaintenanceOperationDao implements MaintenanceOperationDao {

    private final DatabaseConnectionManager connectionManager;

    public SimpleMaintenanceOperationDao() {
        this.connectionManager = DatabaseConnectionManager.getInstance();
    }

    @Override
    public MaintenanceOperationDto getMaintenanceOperationById(UUID uuid) {
        String query = "SELECT uuid, vehicle_uuid, name, description, operation_date, cost " +
                       "FROM maintenance_operation WHERE uuid = ?";

        try {

            Connection conn = connectionManager.getNewConnection();
            PreparedStatement stmt = conn.prepareStatement(query);

            stmt.setObject(1, uuid);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToDto(rs);
            }

            return null;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to retrieve maintenance operation with UUID: " + uuid, e);
        } catch (DatabaseException e) {
            throw new RuntimeException("Database connection error while retrieving maintenance operation", e);
        }
    }

    @Override
    public List<MaintenanceOperationDto> getMaintenanceOperationsByVehicleUuid(UUID vehicleUuid) {
        String query = "SELECT uuid, vehicle_uuid, name, description, operation_date, cost " +
                       "FROM maintenance_operation WHERE vehicle_uuid = ? ORDER BY operation_date DESC";

        List<MaintenanceOperationDto> operations = new ArrayList<>();

        try {

            Connection conn = connectionManager.getNewConnection();
            PreparedStatement stmt = conn.prepareStatement(query);

            stmt.setObject(1, vehicleUuid);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                operations.add(mapResultSetToDto(rs));
            }

            return operations;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to retrieve maintenance operations for vehicle UUID: " + vehicleUuid, e);
        } catch (DatabaseException e) {
            throw new RuntimeException("Database connection error while retrieving maintenance operations", e);
        }
    }

    @Override
    public List<MaintenanceOperationDto> getAllMaintenanceOperations() {
        String query = "SELECT uuid, vehicle_uuid, name, description, operation_date, cost " +
                       "FROM maintenance_operation ORDER BY operation_date DESC";

        List<MaintenanceOperationDto> operations = new ArrayList<>();

        try {

            Connection conn = connectionManager.getNewConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                operations.add(mapResultSetToDto(rs));
            }

            return operations;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to retrieve all maintenance operations", e);
        } catch (DatabaseException e) {
            throw new RuntimeException("Database connection error while retrieving maintenance operations", e);
        }
    }

    @Override
    public void createMaintenanceOperation(MaintenanceOperationDto maintenanceOperationDto) {
        String query = "INSERT INTO maintenance_operation (uuid, vehicle_uuid, name, description, operation_date, cost) " +
                       "VALUES (?, ?, ?, ?, ?, ?)";

        try {

            Connection conn = connectionManager.getNewConnection();
            PreparedStatement stmt = conn.prepareStatement(query);

            stmt.setObject(1, maintenanceOperationDto.uuid());
            stmt.setObject(2, maintenanceOperationDto.vehicleUuid());
            stmt.setString(3, maintenanceOperationDto.name());
            stmt.setString(4, maintenanceOperationDto.description());
            stmt.setTimestamp(5, maintenanceOperationDto.operationDate() != null ?
                Timestamp.valueOf(maintenanceOperationDto.operationDate()) : null);
            stmt.setBigDecimal(6, maintenanceOperationDto.cost());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to create maintenance operation", e);
        } catch (DatabaseException e) {
            throw new RuntimeException("Database connection error while creating maintenance operation", e);
        }
    }

    @Override
    public void updateMaintenanceOperation(MaintenanceOperationDto maintenanceOperationDto) {
        String query = "UPDATE maintenance_operation SET vehicle_uuid = ?, name = ?, description = ?, " +
                       "operation_date = ?, cost = ? WHERE uuid = ?";

        try {

            Connection conn = connectionManager.getNewConnection();
            PreparedStatement stmt = conn.prepareStatement(query);

            stmt.setObject(1, maintenanceOperationDto.vehicleUuid());
            stmt.setString(2, maintenanceOperationDto.name());
            stmt.setString(3, maintenanceOperationDto.description());
            stmt.setTimestamp(4, maintenanceOperationDto.operationDate() != null ?
                Timestamp.valueOf(maintenanceOperationDto.operationDate()) : null);
            stmt.setBigDecimal(5, maintenanceOperationDto.cost());
            stmt.setObject(6, maintenanceOperationDto.uuid());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to update maintenance operation", e);
        } catch (DatabaseException e) {
            throw new RuntimeException("Database connection error while updating maintenance operation", e);
        }
    }

    @Override
    public void deleteMaintenanceOperation(UUID uuid) {
        String query = "DELETE FROM maintenance_operation WHERE uuid = ?";

        try {

            Connection conn = connectionManager.getNewConnection();
            PreparedStatement stmt = conn.prepareStatement(query);

            stmt.setObject(1, uuid);

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete maintenance operation with UUID: " + uuid, e);
        } catch (DatabaseException e) {
            throw new RuntimeException("Database connection error while deleting maintenance operation", e);
        }
    }

    private MaintenanceOperationDto mapResultSetToDto(ResultSet rs) throws SQLException {
        return new MaintenanceOperationDto(
            (UUID) rs.getObject("uuid"),
            (UUID) rs.getObject("vehicle_uuid"),
            rs.getString("name"),
            rs.getString("description"),
            rs.getTimestamp("operation_date") != null ?
                rs.getTimestamp("operation_date").toLocalDateTime() : null,
            rs.getBigDecimal("cost")
        );
    }
}
