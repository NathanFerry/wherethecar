package groupe1.il3.app.persistence.dto.vehicle;

import groupe1.il3.app.domain.vehicle.Energy;
import groupe1.il3.app.domain.vehicle.Status;
import groupe1.il3.app.domain.vehicle.Vehicle;

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
) {
    public static VehicleDto fromDomainObject(Vehicle vehicle) {
        return new VehicleDto(
                vehicle.uuid(),
                vehicle.licencePlate(),
                vehicle.manufacturer(),
                vehicle.model(),
                vehicle.energy().toString().toLowerCase(),
                vehicle.power(),
                vehicle.seats(),
                vehicle.capacity(),
                vehicle.utilityWeight(),
                vehicle.color(),
                vehicle.kilometers(),
                vehicle.acquisitionDate(),
                vehicle.status().toString().toLowerCase()
        );
    }

    public Vehicle toDomainObject() {
        return new Vehicle(
                this.uuid,
                this.licensePlate,
                this.manufacturer,
                this.model,
                Energy.valueOf(this.energy.toUpperCase()),
                this.power,
                this.seats,
                this.capacity,
                this.utilityWeight,
                this.color,
                this.kilometers,
                this.acquisitionDate,
                Status.valueOf(this.status.toUpperCase())
        );
    }
}
