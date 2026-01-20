package groupe1.il3.app.persistence.dto.agent;

import java.util.UUID;

public record AgentDto(
        UUID uuid,
        String firstName,
        String lastName,
        String email,
        String passwordHash,
        Boolean isAdmin
) {}
