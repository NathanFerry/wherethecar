package groupe1.il3.app.persistence.dto.maintenance;

import groupe1.il3.app.domain.maintenance.MaintenanceOperation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("MaintenanceOperationDto Conversion Tests")
class MaintenanceOperationDtoTest {

    @Test
    @DisplayName("fromDomainObject should correctly convert MaintenanceOperation to MaintenanceOperationDto")
    void fromDomainObjectShouldConvertMaintenanceOperationToDto() {
        // Arrange
        UUID operationUuid = UUID.randomUUID();
        UUID vehicleUuid = UUID.randomUUID();
        LocalDateTime operationDate = LocalDateTime.of(2024, 3, 15, 10, 30);
        BigDecimal cost = new BigDecimal("250.50");
        MaintenanceOperation operation = new MaintenanceOperation(
                operationUuid,
                vehicleUuid,
                "Oil Change",
                "Regular oil change and filter replacement",
                operationDate,
                cost
        );

        // Act
        MaintenanceOperationDto dto = MaintenanceOperationDto.fromDomainObject(operation);

        // Assert
        assertNotNull(dto);
        assertEquals(operationUuid, dto.uuid());
        assertEquals(vehicleUuid, dto.vehicleUuid());
        assertEquals("Oil Change", dto.name());
        assertEquals("Regular oil change and filter replacement", dto.description());
        assertEquals(operationDate, dto.operationDate());
        assertEquals(cost, dto.cost());
    }

    @Test
    @DisplayName("toDomainObject should correctly convert MaintenanceOperationDto to MaintenanceOperation")
    void toDomainObjectShouldConvertDtoToMaintenanceOperation() {
        // Arrange
        UUID operationUuid = UUID.randomUUID();
        UUID vehicleUuid = UUID.randomUUID();
        LocalDateTime operationDate = LocalDateTime.of(2024, 5, 20, 14, 0);
        BigDecimal cost = new BigDecimal("1500.75");
        MaintenanceOperationDto dto = new MaintenanceOperationDto(
                operationUuid,
                vehicleUuid,
                "Brake Replacement",
                "Front and rear brake pads replaced",
                operationDate,
                cost
        );

        // Act
        MaintenanceOperation operation = dto.toDomainObject();

        // Assert
        assertNotNull(operation);
        assertEquals(operationUuid, operation.uuid());
        assertEquals(vehicleUuid, operation.vehicleUuid());
        assertEquals("Brake Replacement", operation.name());
        assertEquals("Front and rear brake pads replaced", operation.description());
        assertEquals(operationDate, operation.operationDate());
        assertEquals(cost, operation.cost());
    }

    @Test
    @DisplayName("Round-trip conversion should preserve all data")
    void roundTripConversionShouldPreserveData() {
        // Arrange
        UUID operationUuid = UUID.randomUUID();
        UUID vehicleUuid = UUID.randomUUID();
        LocalDateTime operationDate = LocalDateTime.of(2024, 7, 10, 9, 15);
        BigDecimal cost = new BigDecimal("750.00");
        MaintenanceOperation originalOperation = new MaintenanceOperation(
                operationUuid,
                vehicleUuid,
                "Tire Replacement",
                "All four tires replaced with new ones",
                operationDate,
                cost
        );

        // Act
        MaintenanceOperationDto dto = MaintenanceOperationDto.fromDomainObject(originalOperation);
        MaintenanceOperation convertedOperation = dto.toDomainObject();

        // Assert
        assertEquals(originalOperation, convertedOperation);
    }

    @Test
    @DisplayName("fromDomainObject should preserve UUID values")
    void fromDomainObjectShouldPreserveUuidValues() {
        // Arrange
        UUID operationUuid = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        UUID vehicleUuid = UUID.fromString("987e6543-e21b-12d3-a456-426614174999");
        MaintenanceOperation operation = new MaintenanceOperation(
                operationUuid,
                vehicleUuid,
                "Inspection",
                "Annual inspection",
                LocalDateTime.now(),
                new BigDecimal("100.00")
        );

        // Act
        MaintenanceOperationDto dto = MaintenanceOperationDto.fromDomainObject(operation);

        // Assert
        assertEquals(operationUuid, dto.uuid());
        assertEquals(vehicleUuid, dto.vehicleUuid());
    }

    @Test
    @DisplayName("fromDomainObject should handle zero cost")
    void fromDomainObjectShouldHandleZeroCost() {
        // Arrange
        MaintenanceOperation operation = new MaintenanceOperation(
                UUID.randomUUID(),
                UUID.randomUUID(),
                "Warranty Repair",
                "Covered under warranty",
                LocalDateTime.now(),
                BigDecimal.ZERO
        );

        // Act
        MaintenanceOperationDto dto = MaintenanceOperationDto.fromDomainObject(operation);

        // Assert
        assertEquals(BigDecimal.ZERO, dto.cost());
    }

    @Test
    @DisplayName("toDomainObject should handle large cost values")
    void toDomainObjectShouldHandleLargeCostValues() {
        // Arrange
        BigDecimal largeCost = new BigDecimal("99999.99");
        MaintenanceOperationDto dto = new MaintenanceOperationDto(
                UUID.randomUUID(),
                UUID.randomUUID(),
                "Engine Rebuild",
                "Complete engine overhaul",
                LocalDateTime.now(),
                largeCost
        );

        // Act
        MaintenanceOperation operation = dto.toDomainObject();

        // Assert
        assertEquals(largeCost, operation.cost());
    }

    @Test
    @DisplayName("fromDomainObject should preserve operation date precision")
    void fromDomainObjectShouldPreserveDateTimePrecision() {
        // Arrange
        LocalDateTime preciseDate = LocalDateTime.of(2024, 11, 22, 13, 45, 30, 123456789);
        MaintenanceOperation operation = new MaintenanceOperation(
                UUID.randomUUID(),
                UUID.randomUUID(),
                "Battery Check",
                "Battery voltage test",
                preciseDate,
                new BigDecimal("50.00")
        );

        // Act
        MaintenanceOperationDto dto = MaintenanceOperationDto.fromDomainObject(operation);

        // Assert
        assertEquals(preciseDate, dto.operationDate());
    }

    @Test
    @DisplayName("toDomainObject should preserve operation date precision")
    void toDomainObjectShouldPreserveDateTimePrecision() {
        // Arrange
        LocalDateTime preciseDate = LocalDateTime.of(2024, 12, 1, 16, 30, 15, 987654321);
        MaintenanceOperationDto dto = new MaintenanceOperationDto(
                UUID.randomUUID(),
                UUID.randomUUID(),
                "Alignment",
                "Wheel alignment service",
                preciseDate,
                new BigDecimal("75.00")
        );

        // Act
        MaintenanceOperation operation = dto.toDomainObject();

        // Assert
        assertEquals(preciseDate, operation.operationDate());
    }

    @Test
    @DisplayName("fromDomainObject should handle empty description")
    void fromDomainObjectShouldHandleEmptyDescription() {
        // Arrange
        MaintenanceOperation operation = new MaintenanceOperation(
                UUID.randomUUID(),
                UUID.randomUUID(),
                "Quick Check",
                "",
                LocalDateTime.now(),
                new BigDecimal("25.00")
        );

        // Act
        MaintenanceOperationDto dto = MaintenanceOperationDto.fromDomainObject(operation);

        // Assert
        assertEquals("", dto.description());
    }

    @Test
    @DisplayName("toDomainObject should handle long description")
    void toDomainObjectShouldHandleLongDescription() {
        // Arrange
        String longDescription = "This is a very detailed description of the maintenance operation " +
                "that includes multiple steps and observations about the vehicle's condition. " +
                "The technician noted several items that need attention in the future.";
        MaintenanceOperationDto dto = new MaintenanceOperationDto(
                UUID.randomUUID(),
                UUID.randomUUID(),
                "Comprehensive Service",
                longDescription,
                LocalDateTime.now(),
                new BigDecimal("500.00")
        );

        // Act
        MaintenanceOperation operation = dto.toDomainObject();

        // Assert
        assertEquals(longDescription, operation.description());
    }

    @Test
    @DisplayName("fromDomainObject should handle decimal cost precision")
    void fromDomainObjectShouldHandleDecimalPrecision() {
        // Arrange
        BigDecimal preciseCost = new BigDecimal("123.456");
        MaintenanceOperation operation = new MaintenanceOperation(
                UUID.randomUUID(),
                UUID.randomUUID(),
                "Precision Service",
                "Test precision",
                LocalDateTime.now(),
                preciseCost
        );

        // Act
        MaintenanceOperationDto dto = MaintenanceOperationDto.fromDomainObject(operation);

        // Assert
        assertEquals(preciseCost, dto.cost());
    }
}
