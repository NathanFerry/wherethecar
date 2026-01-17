package groupe1.il3.app.persistence.dao.agent;

import groupe1.il3.app.persistence.DatabaseConnectionManager;
import groupe1.il3.app.persistence.DatabaseException;
import groupe1.il3.app.persistence.dto.agent.AgentDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SimpleAgentDao implements AgentDao {

    private final DatabaseConnectionManager connectionManager;

    public SimpleAgentDao() {
        this.connectionManager = DatabaseConnectionManager.getInstance();
    }

    @Override
    public AgentDto getAgentById(UUID uuid) {
        String query = "SELECT uuid, firstname, lastname, email, password_hash, is_admin " +
                       "FROM agent WHERE uuid = ?";

        try (Connection conn = connectionManager.getNewConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setObject(1, uuid);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToAgentDto(rs);
                }
                return null;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to retrieve agent with UUID: " + uuid, e);
        } catch (DatabaseException e) {
            throw new RuntimeException("Database connection error while retrieving agent", e);
        }
    }

    @Override
    public List<AgentDto> getAllAgents() {
        String query = "SELECT uuid, firstname, lastname, email, password_hash, is_admin " +
                       "FROM agent";

        List<AgentDto> agents = new ArrayList<>();

        try (Connection conn = connectionManager.getNewConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                agents.add(mapResultSetToAgentDto(rs));
            }

            return agents;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to retrieve all agents", e);
        } catch (DatabaseException e) {
            throw new RuntimeException("Database connection error while retrieving agents", e);
        }
    }

    private AgentDto mapResultSetToAgentDto(ResultSet rs) throws SQLException {
        UUID uuid = (UUID) rs.getObject("uuid");
        String firstName = rs.getString("firstname");
        String lastName = rs.getString("lastname");
        String email = rs.getString("email");
        String passwordHash = rs.getString("password_hash");
        Boolean isAdmin = rs.getBoolean("is_admin");

        return new AgentDto(
            uuid,
            firstName,
            lastName,
            email,
            passwordHash,
            isAdmin
        );
    }
}
