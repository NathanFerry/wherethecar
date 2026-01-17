package groupe1.il3.app.persistence.broker.agent;

import groupe1.il3.app.domain.agent.Agent;
import groupe1.il3.app.persistence.dao.agent.AgentDao;
import groupe1.il3.app.persistence.dao.agent.SimpleAgentDao;
import groupe1.il3.app.persistence.dto.agent.AgentDto;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class AgentBroker {
    private final AgentDao agentDao;

    public AgentBroker() {
        this.agentDao = new SimpleAgentDao();
    }

    public Agent getAgentById(UUID uuid) {
        AgentDto dto = agentDao.getAgentById(uuid);
        return dto != null ? convertToAgent(dto) : null;
    }

    public Agent getAgentByEmail(String email) {
        AgentDto dto = agentDao.getAgentByEmail(email);
        return dto != null ? convertToAgent(dto) : null;
    }

    public List<Agent> getAllAgents() {
        return agentDao.getAllAgents().stream()
                .map(this::convertToAgent)
                .collect(Collectors.toList());
    }

    private Agent convertToAgent(AgentDto dto) {
        return new Agent(
                dto.getUuid(),
                dto.getFirstName(),
                dto.getLastName(),
                dto.getEmail(),
                dto.getPasswordHash(),
                dto.getIsAdmin()
        );
    }
}
