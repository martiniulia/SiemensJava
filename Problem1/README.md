# Train Ticketing Application

A console-based Java application for managing train routes and booking tickets.

---

## Getting Started

### Configuration
Before running the application, you **must** update the configuration file to enable email notifications:
* Open `config.properties` in the root folder.
* Update the following fields with your SMTP email credentials:
  * `mail.username`: Your email address.
  * `mail.password`: Your email app password.
  * `mail.from`: The email address for the sender.

---

## Architecture & Class Roles

The project is structured using a standard Layered Architecture pattern, ensuring that each class has a single responsibility.

### 1. config (Configuration Layer)
- **AppConfig.java**: Responsible for loading properties from `config.properties` (like email credentials) securely. Also holds the hardcoded ADMIN_PASSWORD (`123`).

### 2. model (Data Entities)
- **Station.java**: Represents a physical location. Contains an `id` and a `name`.
- **StopEntry.java**: Represents a stop on a route. Links a `Station` to a specific `arrivalTime` and `departureTime`.
- **Route.java**: Represents a path. Contains an `id`, a `name`, and an ordered list of `StopEntry` objects.
- **Train.java**: Represents a physical train. Has a total seat capacity and is assigned to a specific `routeId`.
- **Booking.java**: Stores ticket data. Links a customer (name/email) to a `trainId`, origin/destination stations, and assigned seat numbers.

### 3. repository (Data Access Layer)
- **JsonRepository<T>.java**: A generic file I/O utility using the Jackson library. It reads and writes JSON arrays to disk securely, formatting the output beautifully.
- **StationRepository, RouteRepository, TrainRepository, BookingRepository**: Static wrappers around `JsonRepository` that bind specific files to their respective models.

### 4. service (Business Logic Layer)
- **RouteSearchService.java**: Contains the complex algorithms for pathfinding. It checks for Direct Connections and 1-Changeover Connections.
- **BookingService.java**: Handles seat capacity logic. Validates that the requested stations actually belong to the chosen train's route, and assigns seats sequentially without overbooking.
- **RouteService, StationService, TrainService.java**: Handle basic operations like adding and removing data. `TrainService` also handles the Delay Registration logic.
- **EmailService.java**: Connects to SMTP via `jakarta.mail`. If the host is invalid or missing, it safely degrades to a console mockup.

### 5. ui (Presentation Layer)
- **CustomerMenu.java**: Presents the interactive console menu for passengers to search and book.
- **AdminMenu.java**: Securely gated menu for administrators to manage the application data.

### 6. util
- **InputHelper.java**: Centralizes all `Scanner` logic to prevent crashes caused by user typos.

---

## Testing & Scenarios

The application can be tested in three ways:
1. **Automated TestRunner**: Run `com.trainticket.TestRunner` to execute all scenarios programmatically without console interaction.
2. **Manual Customer Mode**: Run `Main.java` and select option 1 to manually test the customer booking flow.
3. **Manual Admin Mode**: Run `Main.java` and select option 2 (Password: `123`) to manually test administrative operations.

Below are the explicit test scenarios (inputs and expected outputs) for every functionality in Customer and Admin mode.

### Customer Mode Functionalities

#### 1. Search Connections
* **Valid Direct Connection:**
    * **Input:** Origin `S2`, Destination `S5`
    * **Output:** Displays "Direct Connections", listing Train 1, departure and arrival times, and available seats.
* **Valid 1-Changeover Connection:**
    * **Input:** Origin `S4`, Destination `S5`
    * **Output:** Displays "1-Changeover Connections", listing the path (e.g., Train 2 -> Station 1 -> Train 1), times, and seats for both legs.
* **Invalid Connection:**
    * **Input:** Origin `S5`, Destination `S2`
    * **Output:** `No connection found between Station 5 and Station 2`

#### 2. Book a Ticket
* **Valid Booking:**
    * **Input:** Train ID `T1`, Origin `S2`, Destination `S5`, Seats `2`, Name `John`, Email `john@test.com`
    * **Output:** `Booking successful! Your booking ID is [uuid]` followed by a mock/real email confirmation.
* **Invalid Train ID:**
    * **Input:** Train ID `INVALID_TRAIN`
    * **Output:** `Error: Train not found.`
* **Invalid Station ID:**
    * **Input:** Train ID `T1`, Origin `INVALID_STATION`, Destination `S5`
    * **Output:** `Error: The selected stations do not belong to this train's route.`
* **Wrong Direction (Origin after Destination):**
    * **Input:** Train ID `T1`, Origin `S5`, Destination `S2`
    * **Output:** `Error: The origin station must appear before the destination station on this route.`
* **Overbooking:**
    * **Input:** Train ID `T1`, Origin `S2`, Destination `S5`, Seats `9999`
    * **Output:** `Error: Not enough available seats. Overbooking prevented.`

### Admin Mode Functionalities (Password: 123)

#### 1. Add Route
* **Valid Route Creation:**
    * **Input:** Route ID `R99`, Name `New Route`, Stops `2`, Stop 1 ID `S1` (Arr `08:00`, Dep `08:15`), Stop 2 ID `S2` (Arr `09:00`, Dep `09:15`)
    * **Output:** `Route added`
* **Duplicate Route ID:**
    * **Input:** Route ID `R1` (assuming R1 already exists)
    * **Output:** `Error: A Route with this ID already exists.`
* **Invalid Time Format:**
    * **Input:** Route ID `R99`, Name `New Route`, Stops `1`, Station ID `S1`, Arr Time `99:99`
    * **Output:** `Invalid time format. Please use HH:mm` (re-prompts for input).

#### 2. Remove Route
* **Valid Removal:**
    * **Input:** Route ID to remove `R3`
    * **Output:** `Route removed`

#### 3. Add Train
* **Valid Train Creation:**
    * **Input:** Assigned Route ID `R1`, Train ID `T99`, Name `New Train`, Seats `100`
    * **Output:** `Train added`
* **Duplicate Train ID:**
    * **Input:** Assigned Route ID `R1`, Train ID `T1` (assuming T1 already exists)
    * **Output:** `Error: A Train with this ID already exists.`
* **Invalid Route ID:**
    * **Input:** Assigned Route ID `INVALID_ROUTE`
    * **Output:** `Error: Route ID does not exist. Please create the route first.`

#### 4. Remove Train
* **Valid Removal:**
    * **Input:** Train ID to remove `T3`
    * **Output:** `Train removed`

#### 5. View Bookings for Train
* **Train With Bookings:**
    * **Input:** Train ID `T1`
    * **Output:** Lists booking IDs, customer names, emails, and allocated seat numbers.
* **Train Without Bookings:**
    * **Input:** Train ID `T2` (assuming no bookings)
    * **Output:** `No bookings found for this train`

#### 6. Mark Train as Delayed
* **Valid Delay Registration:**
    * **Input:** Train ID `T1`, Delay `30`
    * **Output:** `Train Train 1 marked as delayed by 30 minutes` followed by mock/real delay notification emails for every customer with a booking on that train.

#### 7. Add Station
* **Valid Station Creation:**
    * **Input:** Station ID `S99`, Name `New Station`
    * **Output:** `Station added successfully.`
* **Duplicate Station ID:**
    * **Input:** Station ID `S1` (assuming S1 already exists)
    * **Output:** `Error: A Station with this ID already exists.`
