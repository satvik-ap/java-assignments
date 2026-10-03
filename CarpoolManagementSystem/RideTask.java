public class RideTask implements Runnable {

    private String source;
    private String destination;

    public RideTask(String source, String destination) {
        this.source = source;
        this.destination = destination;
    }

    @Override
    public void run() {

        System.out.println(
                "Searching for ride: "
                        + source + " -> " + destination
        );

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            System.out.println("Ride search interrupted.");
        }

        System.out.println("Ride search completed.");
    }
}