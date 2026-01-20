package groupe1.il3.app.persistence.dto.agent;

import groupe1.il3.app.domain.agent.Agent;

import java.util.UUID;

public record AgentDto(
        UUID uuid,
        String firstName,
        String lastName,
        String email,
        String passwordHash,
        Boolean isAdmin
) {
    public static AgentDto fromDomainObject(Agent agent) {
        return new AgentDto(
                agent.uuid(),
                agent.firstname(),
                agent.lastname(),
                agent.email(),
                agent.passwordHash(),
                agent.isAdmin()
        );
    }

    public Agent toDomainObject() {
        return new Agent(
                this.uuid,
                this.firstName,
                this.lastName,
                this.email,
                this.passwordHash,
                this.isAdmin
        );
    }
}
