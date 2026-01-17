package groupe1.il3.app.persistence.dao.agent;

import groupe1.il3.app.persistence.dto.agent.AgentDto;

import java.util.List;
import java.util.UUID;

public interface AgentDao {
    public AgentDto getAgentById(UUID uuid);
    public AgentDto getAgentByEmail(String email);
    public List<AgentDto> getAllAgents();
}
