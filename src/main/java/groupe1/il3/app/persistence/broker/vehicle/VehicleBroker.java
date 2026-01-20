package groupe1.il3.app.persistence.broker.vehicle;

import groupe1.il3.app.domain.vehicle.Energy;
import groupe1.il3.app.domain.vehicle.Status;
import groupe1.il3.app.domain.vehicle.Vehicle;
import groupe1.il3.app.persistence.dao.vehicle.SimpleVehicleDao;
import groupe1.il3.app.persistence.dao.vehicle.VehicleDao;
import groupe1.il3.app.persistence.dto.vehicle.VehicleDto;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class VehicleBroker {
    private final VehicleDao vehicleDao;

    public VehicleBroker() {
        this.vehicleDao = new SimpleVehicleDao();
    }

    public Vehicle getVehicleById(UUID uuid) {
        VehicleDto dto = vehicleDao.getVehicleById(uuid);
        return dto != null ? convertToVehicle(dto) : null;
    }

    public List<Vehicle> getAllVehicles() {
        return vehicleDao.getAllVehicles().stream()
                .map(this::convertToVehicle)
                .collect(Collectors.toList());
    }

    public void updateVehicleStatus(UUID vehicleUuid, Status status) {
        vehicleDao.updateVehicleStatus(vehicleUuid, status.toString().toLowerCase());
    }

    public void createVehicle(Vehicle vehicle) {
        VehicleDto dto = new VehicleDto(
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
        vehicleDao.createVehicle(dto);
    }

    public void updateVehicle(Vehicle vehicle) {
        VehicleDto dto = new VehicleDto(
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
        vehicleDao.updateVehicle(dto);
    }

    public void deleteVehicle(UUID vehicleUuid) {
        vehicleDao.deleteVehicle(vehicleUuid);
    }

    private Vehicle convertToVehicle(VehicleDto dto) {
        return new Vehicle(
                dto.getUuid(),
                dto.getLicensePlate(),
                dto.getManufacturer(),
                dto.getModel(),
                parseEnergy(dto.getEnergy()),
                dto.getPower(),
                dto.getSeats(),
                dto.getCapacity(),
                dto.getUtilityWeight(),
                dto.getColor(),
                dto.getKilometers(),
                dto.getAcquisitionDate(),
                parseStatus(dto.getStatus())
        );
    }

    private Energy parseEnergy(String energyStr) {
        if (energyStr == null || energyStr.isEmpty()) {
            return Energy.NOT_SPECIFIED;
        }
        try {
            return Energy.valueOf(energyStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            return Energy.NOT_SPECIFIED;
        }
    }

    private Status parseStatus(String statusStr) {
        if (statusStr == null || statusStr.isEmpty()) {
            return Status.MAINTENANCE;
        }
        try {
            return Status.valueOf(statusStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            return Status.MAINTENANCE;
        }
    }
}
