package groupe1.il3.app.gui.vehicleselector;

import groupe1.il3.app.domain.vehicle.Vehicle;
import groupe1.il3.app.persistence.broker.reservation.ReservationBroker;
import groupe1.il3.app.persistence.broker.vehicle.VehicleBroker;
import javafx.concurrent.Task;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class VehicleSelectorInteractor {
    private final VehicleBroker vehicleBroker;
    private final ReservationBroker reservationBroker;

    public VehicleSelectorInteractor() {
        this.vehicleBroker = new VehicleBroker();
        this.reservationBroker = new ReservationBroker();
    }

    public Task<List<Vehicle>> createLoadVehiclesTask() {
        return new Task<>() {
            @Override
            protected List<Vehicle> call() {
                return vehicleBroker.getAllVehicles();
            }
        };
    }

    public Task<Boolean> createReservationTask(UUID agentUuid, UUID vehicleUuid, LocalDateTime startDate, LocalDateTime endDate) {
        return new Task<>() {
            @Override
            protected Boolean call() {
                reservationBroker.createReservation(agentUuid, vehicleUuid, startDate, endDate);
                return true;
            }
        };
    }
}
