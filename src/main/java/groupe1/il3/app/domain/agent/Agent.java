package groupe1.il3.app.domain.agent;

import java.util.UUID;

public record Agent(
        UUID uuid,
        String firstname,
        String lastname,
        String email,
        String passwordHash,
        boolean isAdmin
) {}
