package groupe1.il3.app.persistence.dao.agent;

import groupe1.il3.app.persistence.dto.agent.AgentDto;

import java.util.List;
import java.util.UUID;

public interface AgentDao {
    AgentDto getAgentById(UUID uuid);
    AgentDto getAgentByEmail(String email);
    List<AgentDto> getAllAgents();
    void createAgent(AgentDto agentDto);
    void updateAgent(AgentDto agentDto);
    void deleteAgent(UUID agentUuid);
}
