package groupe1.il3.app.persistence.broker.reservation;

import groupe1.il3.app.domain.agent.Agent;
import groupe1.il3.app.domain.reservation.Reservation;
import groupe1.il3.app.domain.reservation.ReservationStatus;
import groupe1.il3.app.domain.vehicle.Status;
import groupe1.il3.app.domain.vehicle.Vehicle;
import groupe1.il3.app.persistence.broker.agent.AgentBroker;
import groupe1.il3.app.persistence.broker.vehicle.VehicleBroker;
import groupe1.il3.app.persistence.dao.reservation.ReservationDao;
import groupe1.il3.app.persistence.dao.reservation.SimpleReservationDao;
import groupe1.il3.app.persistence.dto.reservation.ReservationDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ReservationBroker {
    private final ReservationDao reservationDao;
    private final AgentBroker agentBroker;
    private final VehicleBroker vehicleBroker;

    public ReservationBroker() {
        this.reservationDao = new SimpleReservationDao();
        this.agentBroker = new AgentBroker();
        this.vehicleBroker = new VehicleBroker();
    }

    public Reservation getReservationById(UUID uuid) {
        ReservationDto dto = reservationDao.getReservationById(uuid);
        return dto != null ? convertToReservation(dto) : null;
    }

    public List<Reservation> getAllReservations() {
        return reservationDao.getAllReservations().stream()
                .map(this::convertToReservation)
                .collect(Collectors.toList());
    }

    public List<Reservation> getReservationsByAgentUuid(UUID agentUuid) {
        return reservationDao.getActiveReservationsByAgentUuid(agentUuid).stream()
                .map(this::convertToReservation)
                .collect(Collectors.toList());
    }

    public List<Reservation> getHistoricalReservationsByAgentUuid(UUID agentUuid) {
        return reservationDao.getHistoricalReservationsByAgentUuid(agentUuid).stream()
                .map(this::convertToReservation)
                .collect(Collectors.toList());
    }

    public void createReservation(UUID agentUuid, UUID vehicleUuid, LocalDateTime startDate, LocalDateTime endDate) {
        UUID reservationUuid = UUID.randomUUID();
        reservationDao.createReservation(reservationUuid, agentUuid, vehicleUuid, startDate, endDate, "pending");
    }

    public void returnVehicle(UUID reservationUuid, UUID vehicleUuid, int newKilometers) {
        reservationDao.updateReservationStatus(reservationUuid, "completed");

        Vehicle vehicle = vehicleBroker.getVehicleById(vehicleUuid);
        if (vehicle != null) {
            Vehicle updatedVehicle = new Vehicle(
                vehicle.uuid(),
                vehicle.licencePlate(),
                vehicle.manufacturer(),
                vehicle.model(),
                vehicle.energy(),
                vehicle.power(),
                vehicle.seats(),
                vehicle.capacity(),
                vehicle.utilityWeight(),
                vehicle.color(),
                newKilometers,
                vehicle.acquisitionDate(),
                Status.AVAILABLE
            );
            vehicleBroker.updateVehicle(updatedVehicle);
        }
    }

    public List<Reservation> getPendingReservations() {
        return reservationDao.getAllReservations().stream()
                .filter(dto -> "pending".equals(dto.getStatus()))
                .map(this::convertToReservation)
                .collect(Collectors.toList());
    }

    public void approveReservation(UUID reservationUuid, UUID vehicleUuid) {
        reservationDao.updateReservationStatus(reservationUuid, "confirmed");
        vehicleBroker.updateVehicleStatus(vehicleUuid, Status.RESERVED);
    }

    public void cancelReservation(UUID reservationUuid) {
        reservationDao.updateReservationStatus(reservationUuid, "cancelled");
    }

    private Reservation convertToReservation(ReservationDto dto) {
        Agent agent = agentBroker.getAgentById(dto.getAgentUuid());
        Vehicle vehicle = vehicleBroker.getVehicleById(dto.getVehicleUuid());

        return new Reservation(
                dto.getUuid(),
                agent,
                vehicle,
                dto.getStart(),
                dto.getEnd(),
                mapStringToReservationStatus(dto.getStatus())
        );
    }

    private ReservationStatus mapStringToReservationStatus(String status) {
        if (status == null) return ReservationStatus.PENDING;
        return switch (status.toLowerCase()) {
            case "pending" -> ReservationStatus.PENDING;
            case "confirmed" -> ReservationStatus.CONFIRMED;
            case "cancelled" -> ReservationStatus.CANCELLED;
            case "completed" -> ReservationStatus.COMPLETED;
            default -> ReservationStatus.PENDING;
        };
    }
}
