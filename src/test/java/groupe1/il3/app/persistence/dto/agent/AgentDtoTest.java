package groupe1.il3.app.persistence.dto.agent;

import groupe1.il3.app.domain.agent.Agent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("AgentDto Conversion Tests")
class AgentDtoTest {

    @ParameterizedTest
    @MethodSource("dtoSource")
    void dtoToDomainConversionTest(AgentDto dto) {
        Agent agent = dto.toDomainObject();

        assertEquals(dto.uuid(), agent.uuid());
        assertEquals(dto.firstName(), agent.firstname());
        assertEquals(dto.lastName(), agent.lastname());
        assertEquals(dto.email(), agent.email());
        assertEquals(dto.passwordHash(), agent.passwordHash());
        assertEquals(dto.isAdmin(), agent.isAdmin());
    }

    @ParameterizedTest
    @MethodSource("domainObjectSource")
    void domainToDtoConversionTest(Agent agent) {
        AgentDto dto = AgentDto.fromDomainObject(agent);

        assertEquals(agent.uuid(), dto.uuid());
        assertEquals(agent.firstname(), dto.firstName());
        assertEquals(agent.lastname(), dto.lastName());
        assertEquals(agent.email(), dto.email());
        assertEquals(agent.passwordHash(), dto.passwordHash());
        assertEquals(agent.isAdmin(), dto.isAdmin());
    }

    static Stream<Arguments> dtoSource() {
        return Stream.of(
                Arguments.of(new AgentDto(
                        UUID.randomUUID(),
                        "Romain",
                        "Gourad",
                        "romain.gouraud@wtc.com",
                        "password123",
                        false
                        )
                ),
                Arguments.of(new AgentDto(
                        UUID.randomUUID(),
                        "Ferry",
                        "Nathan",
                        "nathan.ferry@wtc.com",
                        "adminPass456",
                        true
                        )
                )
        );
    }

    static Stream<Arguments> domainObjectSource() {
        return Stream.of(
                Arguments.of(new Agent(
                        UUID.randomUUID(),
                        "Romain",
                        "Gourad",
                        "romain.gouraud@wtc.com",
                        "password123",
                        false
                        )
                ),
                Arguments.of(new Agent(
                        UUID.randomUUID(),
                        "Ferry",
                        "Nathan",
                        "nathan.ferry@wtc.com",
                        "adminPass456",
                        true
                        )
                )
        );
    }
}
