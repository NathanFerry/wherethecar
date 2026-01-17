package groupe1.il3.app.persistence.dto.vehicle;

import java.time.LocalDateTime;
import java.util.UUID;

public class VehicleDto {
    private final UUID uuid;
    private final String licensePlate;
    private final String manufacturer;
    private final String model;
    private final String energy;
    private final Integer power;
    private final Integer seats;
    private final Integer capacity;
    private final Integer utilityWeight;
    private final String color;
    private final Integer kilometers;
    private final LocalDateTime acquisitionDate;
    private final String status;

    public VehicleDto(
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
        this.uuid = uuid;
        this.licensePlate = licensePlate;
        this.manufacturer = manufacturer;
        this.model = model;
        this.energy = energy;
        this.power = power;
        this.seats = seats;
        this.capacity = capacity;
        this.utilityWeight = utilityWeight;
        this.color = color;
        this.kilometers = kilometers;
        this.acquisitionDate = acquisitionDate;
        this.status = status;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public String getModel() {
        return model;
    }

    public String getEnergy() {
        return energy;
    }

    public Integer getPower() {
        return power;
    }

    public Integer getSeats() {
        return seats;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public Integer getUtilityWeight() {
        return utilityWeight;
    }

    public String getColor() {
        return color;
    }

    public Integer getKilometers() {
        return kilometers;
    }

    public LocalDateTime getAcquisitionDate() {
        return acquisitionDate;
    }

    public String getStatus() {
        return status;
    }
}
