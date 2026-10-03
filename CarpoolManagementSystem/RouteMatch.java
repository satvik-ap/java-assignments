public class RouteMatch {

    public static boolean matchRide(Ride ride, String source, String destination) {

        if (ride.seats <= 0) {
            return false;
        }

        if (ride.driver.source.equalsIgnoreCase(source)
                && ride.driver.destination.equalsIgnoreCase(destination)) {

            return true;
        }

        return false;
    }
}