# Domain documentation
Here you can find all the information about the domain layer of the WhereTheCar project.

## Entities
**Vehicle:**
- `id`: Unique identifier for the vehicle.
- `licensePlate`: License plate number.
- `manufacturer`: Manufacturer of the vehicle.
- `model`: Model of the vehicle.
- `energy`: 
  - gasoline
  - diesel
  - electric
  - hybrid
  - natural gas
  - none
  - not specified
- `power`: Power of the vehicle in horsepower (HP).
- `seats`: Number of seats in the vehicle.
- `capacity`: Cargo capacity of the vehicle in liters.
- `utilityWeight`: Maximum load weight of the vehicle in kilograms.
- `color`: Color of the vehicle.
- `kilometers`: Current distance traveled by the vehicle.
- `acquisitionDate`: Date of the vehicle acquisition.
- `maintenanceOperations`: List of maintenance operations performed on the vehicle.
- `pictures`: List of pictures of the vehicle.
- `status`: 
  - available
  - reserved
  - maintenance

**Picture:**
- `id`: Unique identifier for the picture.
- `vehicleId`: Identifier of the vehicle associated with the picture.
- `name`: Name of the picture.

**MaintenanceOperation:**
- `id`: Unique identifier for the maintenance operation.
- `vehicleId`: Identifier of the vehicle associated with the maintenance operation.
- `name`: Name of the maintenance operation.
- `description`: Description of the maintenance operation.
- `date`: Date when the maintenance operation was performed.
- `price`: Cost of the maintenance operation.
- `documents`: List of documents related to the maintenance operation.

**MaintenanceDocument:**
- `id`: Unique identifier for the maintenance document.
- `operationId`: Identifier of the maintenance operation associated with the document.
- `name`: Name of the document.

**Agent:**
- `id`: Unique identifier for the user.
- `firstName`: First name of the user.
- `lastName`: Last name of the user.
- `email`: Email address of the user.
- `isAdmin`: Boolean indicating if the user has administrative privileges.

**Reservation:**
- `id`: Unique identifier for the reservation.
- `agentId`: Identifier of the agent making the reservation.
- `vehicleId`: Identifier of the vehicle being reserved.
- `startDate`: Start date of the reservation.
- `endDate`: End date of the reservation.
- `status`:
  - pending
  - confirmed
  - cancelled
  - completed

## Relationships

**Vehicle and maintenance operations**
- A `Vehicle` can have multiple `MaintenanceOperation` records associated with it.
- Each `MaintenanceOperation` is linked to a single `Vehicle`.

**Maintenance operation and documents**
- A `MaintenanceOperation` can have multiple `MaintenanceDocument` records associated with it.
- Each `MaintenanceDocument` is linked to a single `MaintenanceOperation`.

**Reservation**
- An `Agent` can make multiple `Reservation` records.
- Each `Reservation` is linked to a single `Agent` and a single `Vehicle`.
- A `Vehicle` can have multiple `Reservation` records associated with it, but not with overlapping periods.