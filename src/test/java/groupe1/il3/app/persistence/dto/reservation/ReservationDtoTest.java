package groupe1.il3.app.persistence.dto.reservation;

import groupe1.il3.app.domain.agent.Agent;
import groupe1.il3.app.domain.reservation.Reservation;
import groupe1.il3.app.domain.reservation.ReservationStatus;
import groupe1.il3.app.domain.vehicle.Energy;
import groupe1.il3.app.domain.vehicle.Status;
import groupe1.il3.app.domain.vehicle.Vehicle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ReservationDto Conversion Tests")
class ReservationDtoTest {

    private Agent testAgent;
    private Vehicle testVehicle;

    @BeforeEach
    void setUp() {
        testAgent = new Agent(
                UUID.randomUUID(),
                "John",
                "Doe",
                "john.doe@example.com",
                "$2a$10$hashedpassword",
                false
        );

        testVehicle = new Vehicle(
                UUID.randomUUID(),
                "AB-123-CD",
                "Toyota",
                "Corolla",
                Energy.GASOLINE,
                150,
                5,
                450,
                0,
                "Blue",
                50000,
                LocalDateTime.now(),
                Status.AVAILABLE
        );
    }

    @Test
    @DisplayName("fromDomainObject should correctly convert Reservation to ReservationDto")
    void fromDomainObjectShouldConvertReservationToDto() {
        // Arrange
        UUID reservationUuid = UUID.randomUUID();
        LocalDateTime start = LocalDateTime.of(2024, 6, 1, 10, 0);
        LocalDateTime end = LocalDateTime.of(2024, 6, 5, 18, 0);
        Reservation reservation = new Reservation(
                reservationUuid,
                testAgent,
                testVehicle,
                start,
                end,
                ReservationStatus.CONFIRMED
        );

        // Act
        ReservationDto dto = ReservationDto.fromDomainObject(reservation);

        // Assert
        assertNotNull(dto);
        assertEquals(reservationUuid, dto.uuid());
        assertEquals(testAgent.uuid(), dto.agentUuid());
        assertEquals(testVehicle.uuid(), dto.vehicleUuid());
        assertEquals(start, dto.start());
        assertEquals(end, dto.end());
        assertEquals("confirmed", dto.status());
    }

    @Test
    @DisplayName("toDomainObject should correctly convert ReservationDto to Reservation")
    void toDomainObjectShouldConvertDtoToReservation() {
        // Arrange
        UUID reservationUuid = UUID.randomUUID();
        LocalDateTime start = LocalDateTime.of(2024, 7, 10, 9, 0);
        LocalDateTime end = LocalDateTime.of(2024, 7, 15, 17, 0);
        ReservationDto dto = new ReservationDto(
                reservationUuid,
                testAgent.uuid(),
                testVehicle.uuid(),
                start,
                end,
                "pending"
        );

        // Act
        Reservation reservation = dto.toDomainObject(testAgent, testVehicle);

        // Assert
        assertNotNull(reservation);
        assertEquals(reservationUuid, reservation.uuid());
        assertEquals(testAgent, reservation.agent());
        assertEquals(testVehicle, reservation.vehicle());
        assertEquals(start, reservation.startDate());
        assertEquals(end, reservation.endDate());
        assertEquals(ReservationStatus.PENDING, reservation.status());
    }

    @Test
    @DisplayName("Round-trip conversion should preserve all data")
    void roundTripConversionShouldPreserveData() {
        // Arrange
        UUID reservationUuid = UUID.randomUUID();
        LocalDateTime start = LocalDateTime.of(2024, 8, 20, 8, 0);
        LocalDateTime end = LocalDateTime.of(2024, 8, 25, 20, 0);
        Reservation originalReservation = new Reservation(
                reservationUuid,
                testAgent,
                testVehicle,
                start,
                end,
                ReservationStatus.COMPLETED
        );

        // Act
        ReservationDto dto = ReservationDto.fromDomainObject(originalReservation);
        Reservation convertedReservation = dto.toDomainObject(testAgent, testVehicle);

        // Assert
        assertEquals(originalReservation, convertedReservation);
    }

    @ParameterizedTest
    @EnumSource(ReservationStatus.class)
    @DisplayName("fromDomainObject should correctly convert all ReservationStatus types to lowercase")
    void fromDomainObjectShouldConvertStatusToLowercase(ReservationStatus status) {
        // Arrange
        Reservation reservation = new Reservation(
                UUID.randomUUID(),
                testAgent,
                testVehicle,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(1),
                status
        );

        // Act
        ReservationDto dto = ReservationDto.fromDomainObject(reservation);

        // Assert
        assertEquals(status.toString().toLowerCase(), dto.status());
    }

    @ParameterizedTest
    @EnumSource(ReservationStatus.class)
    @DisplayName("toDomainObject should correctly convert all lowercase status strings to ReservationStatus enum")
    void toDomainObjectShouldConvertLowercaseStatusToEnum(ReservationStatus status) {
        // Arrange
        ReservationDto dto = new ReservationDto(
                UUID.randomUUID(),
                testAgent.uuid(),
                testVehicle.uuid(),
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(1),
                status.toString().toLowerCase()
        );

        // Act
        Reservation reservation = dto.toDomainObject(testAgent, testVehicle);

        // Assert
        assertEquals(status, reservation.status());
    }

    @Test
    @DisplayName("fromDomainObject should extract correct agent UUID from reservation")
    void fromDomainObjectShouldExtractAgentUuid() {
        // Arrange
        UUID expectedAgentUuid = UUID.randomUUID();
        Agent specificAgent = new Agent(
                expectedAgentUuid,
                "Jane",
                "Smith",
                "jane.smith@example.com",
                "hash",
                true
        );
        Reservation reservation = new Reservation(
                UUID.randomUUID(),
                specificAgent,
                testVehicle,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(2),
                ReservationStatus.PENDING
        );

        // Act
        ReservationDto dto = ReservationDto.fromDomainObject(reservation);

        // Assert
        assertEquals(expectedAgentUuid, dto.agentUuid());
    }

    @Test
    @DisplayName("fromDomainObject should extract correct vehicle UUID from reservation")
    void fromDomainObjectShouldExtractVehicleUuid() {
        // Arrange
        UUID expectedVehicleUuid = UUID.randomUUID();
        Vehicle specificVehicle = new Vehicle(
                expectedVehicleUuid,
                "EF-456-GH",
                "Renault",
                "Clio",
                Energy.DIESEL,
                100,
                5,
                300,
                0,
                "Red",
                75000,
                LocalDateTime.now(),
                Status.RESERVED
        );
        Reservation reservation = new Reservation(
                UUID.randomUUID(),
                testAgent,
                specificVehicle,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(3),
                ReservationStatus.CONFIRMED
        );

        // Act
        ReservationDto dto = ReservationDto.fromDomainObject(reservation);

        // Assert
        assertEquals(expectedVehicleUuid, dto.vehicleUuid());
    }

    @Test
    @DisplayName("toDomainObject should use provided agent and vehicle objects")
    void toDomainObjectShouldUseProvidedObjects() {
        // Arrange
        ReservationDto dto = new ReservationDto(
                UUID.randomUUID(),
                testAgent.uuid(),
                testVehicle.uuid(),
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(1),
                "confirmed"
        );

        // Act
        Reservation reservation = dto.toDomainObject(testAgent, testVehicle);

        // Assert
        assertSame(testAgent, reservation.agent());
        assertSame(testVehicle, reservation.vehicle());
    }

    @Test
    @DisplayName("fromDomainObject should handle cancelled reservation")
    void fromDomainObjectShouldHandleCancelledReservation() {
        // Arrange
        Reservation reservation = new Reservation(
                UUID.randomUUID(),
                testAgent,
                testVehicle,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(1),
                ReservationStatus.CANCELLED
        );

        // Act
        ReservationDto dto = ReservationDto.fromDomainObject(reservation);

        // Assert
        assertEquals("cancelled", dto.status());
    }

    @Test
    @DisplayName("toDomainObject should preserve date and time precision")
    void toDomainObjectShouldPreserveDateTimePrecision() {
        // Arrange
        LocalDateTime preciseStart = LocalDateTime.of(2024, 12, 25, 14, 30, 45, 123456789);
        LocalDateTime preciseEnd = LocalDateTime.of(2024, 12, 26, 18, 45, 30, 987654321);
        ReservationDto dto = new ReservationDto(
                UUID.randomUUID(),
                testAgent.uuid(),
                testVehicle.uuid(),
                preciseStart,
                preciseEnd,
                "pending"
        );

        // Act
        Reservation reservation = dto.toDomainObject(testAgent, testVehicle);

        // Assert
        assertEquals(preciseStart, reservation.startDate());
        assertEquals(preciseEnd, reservation.endDate());
    }
}
