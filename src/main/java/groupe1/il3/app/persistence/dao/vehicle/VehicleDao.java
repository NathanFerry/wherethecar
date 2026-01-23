package groupe1.il3.app.persistence.dao.vehicle;

import groupe1.il3.app.persistence.dto.vehicle.VehicleDto;

import java.util.List;
import java.util.UUID;

public interface VehicleDao {
    VehicleDto getVehicleById(UUID uuid);
    List<VehicleDto> getAllVehicles();
    void updateVehicleStatus(UUID vehicleUuid, String status);
    void createVehicle(VehicleDto vehicleDto);
    void updateVehicle(VehicleDto vehicleDto);
    void deleteVehicle(UUID vehicleUuid);
}
