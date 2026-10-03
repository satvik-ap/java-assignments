public class Ride implements Rideable {

    int rideId;
    Commuter driver;
    String departureTime;
    int seats;
    double cost;

    Ride(int rideId, Commuter driver, String departureTime, int seats, double cost) {
        this.rideId = rideId;
        this.driver = driver;
        this.departureTime = departureTime;
        this.seats = seats;
        this.cost = cost;
    }

    void showRide() {
        System.out.println("Ride ID: " + rideId);
        System.out.println("Driver: " + driver.name);
        System.out.println("Route: " + driver.source + " -> " + driver.destination);
        System.out.println("Departure: " + departureTime);
        System.out.println("Available Seats: " + seats);
        System.out.println("Cost: Rs. " + cost);
    }

    @Override
    public void createRide() {
        System.out.println("Ride created successfully.");
    }

    @Override
    public void cancelRide() {
        System.out.println("Ride cancelled.");
    }
}