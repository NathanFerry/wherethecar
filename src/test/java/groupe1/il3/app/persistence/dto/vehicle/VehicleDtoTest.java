package groupe1.il3.app.persistence.dto.vehicle;

import groupe1.il3.app.domain.vehicle.Energy;
import groupe1.il3.app.domain.vehicle.Status;
import groupe1.il3.app.domain.vehicle.Vehicle;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("VehicleDto Conversion Tests")
class VehicleDtoTest {

    @Test
    @DisplayName("fromDomainObject should correctly convert Vehicle to VehicleDto")
    void fromDomainObjectShouldConvertVehicleToDto() {
        // Arrange
        UUID uuid = UUID.randomUUID();
        LocalDateTime acquisitionDate = LocalDateTime.of(2024, 1, 15, 10, 0);
        Vehicle vehicle = new Vehicle(
                uuid,
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
                acquisitionDate,
                Status.AVAILABLE
        );

        // Act
        VehicleDto dto = VehicleDto.fromDomainObject(vehicle);

        // Assert
        assertNotNull(dto);
        assertEquals(uuid, dto.uuid());
        assertEquals("AB-123-CD", dto.licensePlate());
        assertEquals("Toyota", dto.manufacturer());
        assertEquals("Corolla", dto.model());
        assertEquals("gasoline", dto.energy());
        assertEquals(150, dto.power());
        assertEquals(5, dto.seats());
        assertEquals(450, dto.capacity());
        assertEquals(0, dto.utilityWeight());
        assertEquals("Blue", dto.color());
        assertEquals(50000, dto.kilometers());
        assertEquals(acquisitionDate, dto.acquisitionDate());
        assertEquals("available", dto.status());
    }

    @Test
    @DisplayName("toDomainObject should correctly convert VehicleDto to Vehicle")
    void toDomainObjectShouldConvertDtoToVehicle() {
        // Arrange
        UUID uuid = UUID.randomUUID();
        LocalDateTime acquisitionDate = LocalDateTime.of(2023, 5, 20, 14, 30);
        VehicleDto dto = new VehicleDto(
                uuid,
                "EF-456-GH",
                "Renault",
                "Clio",
                "diesel",
                100,
                5,
                300,
                0,
                "Red",
                75000,
                acquisitionDate,
                "reserved"
        );

        // Act
        Vehicle vehicle = dto.toDomainObject();

        // Assert
        assertNotNull(vehicle);
        assertEquals(uuid, vehicle.uuid());
        assertEquals("EF-456-GH", vehicle.licencePlate());
        assertEquals("Renault", vehicle.manufacturer());
        assertEquals("Clio", vehicle.model());
        assertEquals(Energy.DIESEL, vehicle.energy());
        assertEquals(100, vehicle.power());
        assertEquals(5, vehicle.seats());
        assertEquals(300, vehicle.capacity());
        assertEquals(0, vehicle.utilityWeight());
        assertEquals("Red", vehicle.color());
        assertEquals(75000, vehicle.kilometers());
        assertEquals(acquisitionDate, vehicle.acquisitionDate());
        assertEquals(Status.RESERVED, vehicle.status());
    }

    @Test
    @DisplayName("Round-trip conversion should preserve all data")
    void roundTripConversionShouldPreserveData() {
        // Arrange
        UUID uuid = UUID.randomUUID();
        LocalDateTime acquisitionDate = LocalDateTime.of(2022, 3, 10, 9, 0);
        Vehicle originalVehicle = new Vehicle(
                uuid,
                "IJ-789-KL",
                "Tesla",
                "Model 3",
                Energy.ELECTRIC,
                283,
                5,
                425,
                0,
                "White",
                25000,
                acquisitionDate,
                Status.MAINTENANCE
        );

        // Act
        VehicleDto dto = VehicleDto.fromDomainObject(originalVehicle);
        Vehicle convertedVehicle = dto.toDomainObject();

        // Assert
        assertEquals(originalVehicle, convertedVehicle);
    }

    @ParameterizedTest
    @EnumSource(Energy.class)
    @DisplayName("fromDomainObject should correctly convert all Energy types to lowercase")
    void fromDomainObjectShouldConvertEnergyToLowercase(Energy energy) {
        // Arrange
        Vehicle vehicle = new Vehicle(
                UUID.randomUUID(),
                "TEST-123",
                "Test",
                "Model",
                energy,
                100,
                5,
                300,
                0,
                "Black",
                10000,
                LocalDateTime.now(),
                Status.AVAILABLE
        );

        // Act
        VehicleDto dto = VehicleDto.fromDomainObject(vehicle);

        // Assert
        assertEquals(energy.toString().toLowerCase(), dto.energy());
    }

    @ParameterizedTest
    @EnumSource(Status.class)
    @DisplayName("fromDomainObject should correctly convert all Status types to lowercase")
    void fromDomainObjectShouldConvertStatusToLowercase(Status status) {
        // Arrange
        Vehicle vehicle = new Vehicle(
                UUID.randomUUID(),
                "TEST-456",
                "Test",
                "Model",
                Energy.GASOLINE,
                100,
                5,
                300,
                0,
                "Black",
                10000,
                LocalDateTime.now(),
                status
        );

        // Act
        VehicleDto dto = VehicleDto.fromDomainObject(vehicle);

        // Assert
        assertEquals(status.toString().toLowerCase(), dto.status());
    }

    @ParameterizedTest
    @EnumSource(Energy.class)
    @DisplayName("toDomainObject should correctly convert all lowercase energy strings to Energy enum")
    void toDomainObjectShouldConvertLowercaseEnergyToEnum(Energy energy) {
        // Arrange
        VehicleDto dto = new VehicleDto(
                UUID.randomUUID(),
                "TEST-789",
                "Test",
                "Model",
                energy.toString().toLowerCase(),
                100,
                5,
                300,
                0,
                "Black",
                10000,
                LocalDateTime.now(),
                "available"
        );

        // Act
        Vehicle vehicle = dto.toDomainObject();

        // Assert
        assertEquals(energy, vehicle.energy());
    }

    @ParameterizedTest
    @EnumSource(Status.class)
    @DisplayName("toDomainObject should correctly convert all lowercase status strings to Status enum")
    void toDomainObjectShouldConvertLowercaseStatusToEnum(Status status) {
        // Arrange
        VehicleDto dto = new VehicleDto(
                UUID.randomUUID(),
                "TEST-ABC",
                "Test",
                "Model",
                "gasoline",
                100,
                5,
                300,
                0,
                "Black",
                10000,
                LocalDateTime.now(),
                status.toString().toLowerCase()
        );

        // Act
        Vehicle vehicle = dto.toDomainObject();

        // Assert
        assertEquals(status, vehicle.status());
    }

    @Test
    @DisplayName("fromDomainObject should handle vehicle with utility weight")
    void fromDomainObjectShouldHandleUtilityVehicle() {
        // Arrange
        Vehicle vehicle = new Vehicle(
                UUID.randomUUID(),
                "UT-111-IL",
                "Ford",
                "Transit",
                Energy.DIESEL,
                170,
                3,
                1000,
                750,
                "White",
                100000,
                LocalDateTime.now(),
                Status.AVAILABLE
        );

        // Act
        VehicleDto dto = VehicleDto.fromDomainObject(vehicle);

        // Assert
        assertEquals(750, dto.utilityWeight());
    }

    @Test
    @DisplayName("toDomainObject should handle hybrid vehicle")
    void toDomainObjectShouldHandleHybridVehicle() {
        // Arrange
        VehicleDto dto = new VehicleDto(
                UUID.randomUUID(),
                "HY-222-BR",
                "Toyota",
                "Prius",
                "hybrid",
                122,
                5,
                450,
                0,
                "Silver",
                30000,
                LocalDateTime.now(),
                "available"
        );

        // Act
        Vehicle vehicle = dto.toDomainObject();

        // Assert
        assertEquals(Energy.HYBRID, vehicle.energy());
    }
}
