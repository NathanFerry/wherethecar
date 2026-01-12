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
- `status`: 
  - available
  - reserved
  - maintenance


## Relationships