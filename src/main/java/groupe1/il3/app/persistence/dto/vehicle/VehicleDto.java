package groupe1.il3.app.persistence.dto.vehicle;

import java.time.LocalDateTime;
import java.util.UUID;

public record VehicleDto(
        UUID uuid,
        String licensePlate,
        String manufacturer,
        String model,
        String energy,
        Integer power,
        Integer seats,
        Integer capacity,
        Integer utilityWeight,
        String color,
        Integer kilometers,
        LocalDateTime acquisitionDate,
        String status
) {}
