package groupe1.il3.app.domain.vehicle;

import java.time.LocalDateTime;
import java.util.UUID;

public class Vehicle {
    private UUID uuid;
    private String licencePlate;
    private String manufacturer;
    private String model;
    private Energy energy;
    private int power;
    private int seats;
    private int capacity;
    private int utilityWeight;
    private String color;
    private int kilometers;
    private LocalDateTime acquisitionDate;
    private Status status;

    public Vehicle(
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
    ) {
        this.uuid = uuid;
        this.licencePlate = licencePlate;
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

    public String getLicencePlate() {
        return licencePlate;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public String getModel() {
        return model;
    }

    public Energy getEnergy() {
        return energy;
    }

    public int getPower() {
        return power;
    }

    public int getSeats() {
        return seats;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getUtilityWeight() {
        return utilityWeight;
    }

    public String getColor() {
        return color;
    }

    public int getKilometers() {
        return kilometers;
    }

    public LocalDateTime getAcquisitionDate() {
        return acquisitionDate;
    }

    public Status getStatus() {
        return status;
    }
}


