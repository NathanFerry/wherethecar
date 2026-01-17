package groupe1.il3.app.persistence.dto.agent;

import java.util.UUID;

public class AgentDto {
    private final UUID uuid;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final Boolean isAdmin;

    public AgentDto(
        UUID uuid,
        String firstName,
        String lastName,
        String email,
        Boolean isAdmin
    ) {
        this.uuid = uuid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.isAdmin = isAdmin;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public Boolean getIsAdmin() {
        return isAdmin;
    }
}
