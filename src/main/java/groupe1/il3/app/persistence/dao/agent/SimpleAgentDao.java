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

        try {

            Connection conn = connectionManager.getNewConnection();
            PreparedStatement stmt = conn.prepareStatement(query);

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
    public AgentDto getAgentByEmail(String email) {
        String query = "SELECT uuid, firstname, lastname, email, password_hash, is_admin " +
                       "FROM agent WHERE email = ?";

        try {

            Connection conn = connectionManager.getNewConnection();
            PreparedStatement stmt = conn.prepareStatement(query);

            stmt.setString(1, email);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToAgentDto(rs);
                }
                return null;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to retrieve agent with email: " + email, e);
        } catch (DatabaseException e) {
            throw new RuntimeException("Database connection error while retrieving agent", e);
        }
    }

    @Override
    public List<AgentDto> getAllAgents() {
        String query = "SELECT uuid, firstname, lastname, email, password_hash, is_admin " +
                       "FROM agent";

        List<AgentDto> agents = new ArrayList<>();

        try {

            Connection conn = connectionManager.getNewConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

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

    @Override
    public void createAgent(AgentDto agentDto) {
        String query = "INSERT INTO agent (uuid, firstname, lastname, email, password_hash, is_admin) " +
                       "VALUES (?, ?, ?, ?, ?, ?)";

        try {

            Connection conn = connectionManager.getNewConnection();
            PreparedStatement stmt = conn.prepareStatement(query);

            stmt.setObject(1, agentDto.getUuid());
            stmt.setString(2, agentDto.getFirstName());
            stmt.setString(3, agentDto.getLastName());
            stmt.setString(4, agentDto.getEmail());
            stmt.setString(5, agentDto.getPasswordHash());
            stmt.setBoolean(6, agentDto.getIsAdmin());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to create agent", e);
        } catch (DatabaseException e) {
            throw new RuntimeException("Database connection error while creating agent", e);
        }
    }

    @Override
    public void updateAgent(AgentDto agentDto) {
        String query = "UPDATE agent SET firstname = ?, lastname = ?, email = ?, " +
                       "password_hash = ?, is_admin = ? WHERE uuid = ?";

        try {

            Connection conn = connectionManager.getNewConnection();
            PreparedStatement stmt = conn.prepareStatement(query);

            stmt.setString(1, agentDto.getFirstName());
            stmt.setString(2, agentDto.getLastName());
            stmt.setString(3, agentDto.getEmail());
            stmt.setString(4, agentDto.getPasswordHash());
            stmt.setBoolean(5, agentDto.getIsAdmin());
            stmt.setObject(6, agentDto.getUuid());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to update agent with UUID: " + agentDto.getUuid(), e);
        } catch (DatabaseException e) {
            throw new RuntimeException("Database connection error while updating agent", e);
        }
    }

    @Override
    public void deleteAgent(UUID agentUuid) {
        String query = "DELETE FROM agent WHERE uuid = ?";

        try {

            Connection conn = connectionManager.getNewConnection();
            PreparedStatement stmt = conn.prepareStatement(query);

            stmt.setObject(1, agentUuid);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete agent with UUID: " + agentUuid, e);
        } catch (DatabaseException e) {
            throw new RuntimeException("Database connection error while deleting agent", e);
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
