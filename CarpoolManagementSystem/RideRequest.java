public class RideRequest {

    int requestId;
    Commuter commuter;
    String source;
    String destination;

    RideRequest(int requestId, Commuter commuter,
                String source, String destination) {

        this.requestId = requestId;
        this.commuter = commuter;
        this.source = source;
        this.destination = destination;
    }

    void showRequest() {

        System.out.println("Request ID: " + requestId);
        System.out.println("Commuter: " + commuter.name);
        System.out.println("Route: " + source + " -> " + destination);
    }
}