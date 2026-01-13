package groupe1.il3.app.domain.agent;

import java.util.UUID;

public final class Agent {
    private final UUID uuid;
    private final String firstname;
    private final String lastname;
    private final String email;
    private final boolean isAdmin;

    public Agent(
            UUID uuid,
            String firstname,
            String lastname,
            String email,
            boolean isAdmin
    ) {
        this.uuid = uuid;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.isAdmin = isAdmin;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getEmail() {
        return email;
    }

    public boolean isAdmin() {
        return isAdmin;
    }
}
