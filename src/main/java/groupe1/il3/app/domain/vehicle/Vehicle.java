package groupe1.il3.app.domain.vehicle;

import java.time.LocalDateTime;
import java.util.UUID;

public record Vehicle(
        UUID uuid,
        String licencePlate,
        String manufacturer,
        String model,
        Energy energy,
        int power,
        int seats,
        int capacity,
        int utilityWeight,
        String color,
        int kilometers,
        LocalDateTime acquisitionDate,
        Status status
) {}


