package groupe1.il3.app.persistence.dao.vehicle;

import groupe1.il3.app.persistence.dto.vehicle.VehicleDto;

import java.util.List;
import java.util.UUID;

public interface VehicleDao {
    public VehicleDto getVehicleById(UUID uuid);
    public List<VehicleDto> getAllVehicles();
}
