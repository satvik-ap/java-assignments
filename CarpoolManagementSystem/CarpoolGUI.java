import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

public class CarpoolGUI extends JFrame {

    ArrayList<Commuter> commuters = new ArrayList<>();
    ArrayList<Ride> rides = new ArrayList<>();
    ArrayList<RideRequest> requests = new ArrayList<>();

    LinkedList<Ride> tripHistory = new LinkedList<>();

    HashMap<Integer, Ride> rideMap = new HashMap<>();
    TreeMap<String, Ride> timeMap = new TreeMap<>();

    int nextCommuterId = 1;
    int nextRideId = 101;
    int nextRequestId = 1;

    JTextArea output;

    public CarpoolGUI() {

        setTitle("Carpool Management System");
        setSize(750, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));

        JLabel title = new JLabel(
                "CARPOOL MANAGEMENT SYSTEM",
                SwingConstants.CENTER
        );

        title.setFont(new Font("Arial", Font.BOLD, 22));

        mainPanel.add(title, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(
                new GridLayout(4, 2, 10, 10)
        );

        JButton commuterButton =
                new JButton("Commuter Registration");

        JButton rideButton =
                new JButton("Ride Offer / Request");

        JButton matchButton =
                new JButton("Route Matching");

        JButton costButton =
                new JButton("Cost Splitting");

        JButton trackingButton =
                new JButton("Trip Tracking");

        JButton searchButton =
                new JButton("Ride Search");

        JButton reportButton =
                new JButton("Carpool Reports");

        JButton exitButton =
                new JButton("Exit");

        buttonPanel.add(commuterButton);
        buttonPanel.add(rideButton);
        buttonPanel.add(matchButton);
        buttonPanel.add(costButton);
        buttonPanel.add(trackingButton);
        buttonPanel.add(searchButton);
        buttonPanel.add(reportButton);
        buttonPanel.add(exitButton);

        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        output = new JTextArea();
        output.setEditable(false);
        output.setFont(new Font("Monospaced", Font.PLAIN, 13));

        JScrollPane scrollPane =
                new JScrollPane(output);

        mainPanel.add(scrollPane, BorderLayout.SOUTH);

        commuterButton.addActionListener(e ->
                registerCommuter()
        );

        rideButton.addActionListener(e ->
                rideOfferRequest()
        );

        matchButton.addActionListener(e ->
                matchRide()
        );

        costButton.addActionListener(e ->
                splitCost()
        );

        trackingButton.addActionListener(e ->
                tripTracking()
        );

        searchButton.addActionListener(e ->
                searchRide()
        );

        reportButton.addActionListener(e ->
                showReports()
        );

        exitButton.addActionListener(e ->
                System.exit(0)
        );

        add(mainPanel);
        setVisible(true);
    }

    // 1. Commuter Registration

    void registerCommuter() {

        String name = JOptionPane.showInputDialog(
                this,
                "Enter name:"
        );

        String source = JOptionPane.showInputDialog(
                this,
                "Enter source:"
        );

        String destination = JOptionPane.showInputDialog(
                this,
                "Enter destination:"
        );

        if (name == null ||
                source == null ||
                destination == null) {
            return;
        }

        if (name.isEmpty() ||
                source.isEmpty() ||
                destination.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter all details."
            );

            return;
        }

        Commuter commuter = new Commuter(
                nextCommuterId,
                name,
                source,
                destination
        );

        commuters.add(commuter);

        output.append(
                "Commuter registered: "
                        + name
                        + " | ID: "
                        + nextCommuterId
                        + "\n"
        );

        nextCommuterId++;
    }

    // 2. Ride Offer / Request

    void rideOfferRequest() {

        String[] options = {
                "Offer Ride",
                "Request Ride",
                "Update Ride",
                "Delete Ride"
        };

        int choice = JOptionPane.showOptionDialog(
                this,
                "Select an option",
                "Ride Offer / Request",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]
        );

        if (choice == 0) {
            offerRide();
        } else if (choice == 1) {
            requestRide();
        } else if (choice == 2) {
            updateRide();
        } else if (choice == 3) {
            deleteRide();
        }
    }

    void offerRide() {

        if (commuters.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Register a commuter first."
            );

            return;
        }

        String idText = JOptionPane.showInputDialog(
                this,
                "Enter commuter ID:"
        );

        try {

            int commuterId =
                    Integer.parseInt(idText);

            Commuter driver = null;

            for (Commuter commuter : commuters) {

                if (commuter.id == commuterId) {
                    driver = commuter;
                    break;
                }
            }

            if (driver == null) {

                JOptionPane.showMessageDialog(
                        this,
                        "Commuter not found."
                );

                return;
            }

            String time = JOptionPane.showInputDialog(
                    this,
                    "Enter departure time (HH:MM):"
            );

            int seats = Integer.parseInt(
                    JOptionPane.showInputDialog(
                            this,
                            "Enter available seats:"
                    )
            );

            double cost = Double.parseDouble(
                    JOptionPane.showInputDialog(
                            this,
                            "Enter total cost:"
                    )
            );

            if (seats <= 0 || cost <= 0) {
                throw new IllegalArgumentException();
            }

            Ride ride = new Ride(
                    nextRideId,
                    driver,
                    time,
                    seats,
                    cost
            );

            rides.add(ride);
            rideMap.put(nextRideId, ride);

            timeMap.put(
                    time + "-" + nextRideId,
                    ride
            );

            ride.createRide();

            output.append(
                    "Ride offered: "
                            + nextRideId
                            + " | "
                            + driver.source
                            + " -> "
                            + driver.destination
                            + "\n"
            );

            nextRideId++;

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter valid ride details."
            );
        }
    }

    void requestRide() {

        if (commuters.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Register a commuter first."
            );

            return;
        }

        try {

            int commuterId =
                    Integer.parseInt(
                            JOptionPane.showInputDialog(
                                    this,
                                    "Enter commuter ID:"
                            )
                    );

            Commuter commuter = null;

            for (Commuter c : commuters) {

                if (c.id == commuterId) {
                    commuter = c;
                    break;
                }
            }

            if (commuter == null) {

                JOptionPane.showMessageDialog(
                        this,
                        "Commuter not found."
                );

                return;
            }

            RideRequest request =
                    new RideRequest(
                            nextRequestId,
                            commuter,
                            commuter.source,
                            commuter.destination
                    );

            requests.add(request);

            output.append(
                    "Ride request created: "
                            + nextRequestId
                            + " | "
                            + commuter.source
                            + " -> "
                            + commuter.destination
                            + "\n"
            );

            nextRequestId++;

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Invalid commuter ID."
            );
        }
    }

    void updateRide() {

        try {

            int id = Integer.parseInt(
                    JOptionPane.showInputDialog(
                            this,
                            "Enter Ride ID:"
                    )
            );

            Ride ride = rideMap.get(id);

            if (ride == null) {

                JOptionPane.showMessageDialog(
                        this,
                        "Ride not found."
                );

                return;
            }

            String newTime =
                    JOptionPane.showInputDialog(
                            this,
                            "Enter new departure time:"
                    );

            int newSeats =
                    Integer.parseInt(
                            JOptionPane.showInputDialog(
                                    this,
                                    "Enter new seats:"
                            )
                    );

            double newCost =
                    Double.parseDouble(
                            JOptionPane.showInputDialog(
                                    this,
                                    "Enter new cost:"
                            )
                    );

            if (newSeats <= 0 || newCost <= 0) {
                throw new IllegalArgumentException();
            }

            ride.departureTime = newTime;
            ride.seats = newSeats;
            ride.cost = newCost;

            timeMap.put(
                    newTime + "-" + id,
                    ride
            );

            output.append(
                    "Ride updated: "
                            + id
                            + "\n"
            );

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Invalid ride details."
            );
        }
    }

    void deleteRide() {

        try {

            int id = Integer.parseInt(
                    JOptionPane.showInputDialog(
                            this,
                            "Enter Ride ID:"
                    )
            );

            Ride ride = rideMap.get(id);

            if (ride == null) {

                JOptionPane.showMessageDialog(
                        this,
                        "Ride not found."
                );

                return;
            }

            rides.remove(ride);
            rideMap.remove(id);

            output.append(
                    "Ride deleted: "
                            + id
                            + "\n"
            );

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Invalid Ride ID."
            );
        }
    }

    // 3. Route Matching

    void matchRide() {

        if (rides.isEmpty() ||
                requests.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Ride offers or requests are not available."
            );

            return;
        }

        for (RideRequest request : requests) {

            Thread thread = new Thread(
                    new RideTask(
                            request.source,
                            request.destination
                    )
            );

            thread.start();

            for (Ride ride : rides) {

                if (RouteMatch.matchRide(
                        ride,
                        request.source,
                        request.destination
                )) {

                    ride.seats--;

                    output.append(
                            "Ride matched for "
                                    + request.commuter.name
                                    + " | Ride ID: "
                                    + ride.rideId
                                    + "\n"
                    );

                    requests.remove(request);

                    return;
                }
            }
        }

        output.append(
                "No suitable ride found.\n"
        );
    }

    // 4. Cost Splitting

    void splitCost() {

        try {

            int id = Integer.parseInt(
                    JOptionPane.showInputDialog(
                            this,
                            "Enter Ride ID:"
                    )
            );

            int passengers =
                    Integer.parseInt(
                            JOptionPane.showInputDialog(
                                    this,
                                    "Enter number of passengers:"
                            )
                    );

            Ride ride = rideMap.get(id);

            if (ride == null ||
                    passengers <= 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "Invalid details."
                );

                return;
            }

            double cost =
                    CostSplit.splitCost(
                            ride.cost,
                            passengers
                    );

            output.append(
                    "Cost Split | Ride: "
                            + id
                            + " | Total: Rs. "
                            + ride.cost
                            + " | Per Person: Rs. "
                            + cost
                            + "\n"
            );

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter valid values."
            );
        }
    }

    // 5. Trip Tracking

    void tripTracking() {

        String[] options = {
                "Complete Ride",
                "View Trip History"
        };

        int choice = JOptionPane.showOptionDialog(
                this,
                "Trip Tracking",
                "Trip Tracking",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]
        );

        if (choice == 0) {

            try {

                int id = Integer.parseInt(
                        JOptionPane.showInputDialog(
                                this,
                                "Enter Ride ID:"
                        )
                );

                Ride ride = rideMap.get(id);

                if (ride == null) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Ride not found."
                    );

                    return;
                }

                tripHistory.add(ride);
                rides.remove(ride);
                rideMap.remove(id);

                output.append(
                        "Trip completed: "
                                + id
                                + "\n"
                );

            } catch (Exception e) {

                JOptionPane.showMessageDialog(
                        this,
                        "Invalid Ride ID."
                );
            }

        } else if (choice == 1) {

            if (tripHistory.isEmpty()) {

                output.append(
                        "No completed trips.\n"
                );

            } else {

                output.append(
                        "\nTRIP HISTORY\n"
                );

                for (Ride ride :
                        tripHistory) {

                    output.append(
                            "Ride "
                                    + ride.rideId
                                    + " | "
                                    + ride.driver.name
                                    + " | "
                                    + ride.driver.source
                                    + " -> "
                                    + ride.driver.destination
                                    + "\n"
                    );
                }
            }
        }
    }

    // 6. Ride Search

    void searchRide() {

        String source =
                JOptionPane.showInputDialog(
                        this,
                        "Enter source:"
                );

        String destination =
                JOptionPane.showInputDialog(
                        this,
                        "Enter destination:"
                );

        boolean found = false;

        for (Ride ride : rides) {

            if (ride.driver.source
                    .equalsIgnoreCase(source)
                    && ride.driver.destination
                    .equalsIgnoreCase(destination)) {

                output.append(
                        "Ride found: "
                                + ride.rideId
                                + " | "
                                + ride.driver.name
                                + " | "
                                + ride.departureTime
                                + " | Rs. "
                                + ride.cost
                                + "\n"
                );

                found = true;
            }
        }

        if (!found) {

            output.append(
                    "No matching ride found.\n"
            );
        }
    }

    // 7. Carpool Reports

    void showReports() {

        output.append(
                "\nCARPOOL REPORT\n"
        );

        output.append(
                "Registered Commuters: "
                        + commuters.size()
                        + "\n"
        );

        output.append(
                "Active Rides: "
                        + rides.size()
                        + "\n"
        );

        output.append(
                "Ride Requests: "
                        + requests.size()
                        + "\n"
        );

        output.append(
                "Completed Trips: "
                        + tripHistory.size()
                        + "\n"
        );

        if (!rides.isEmpty()) {

            Collections.sort(
                    rides,
                    Comparator.comparingDouble(
                            ride -> ride.cost
                    )
            );

            output.append(
                    "\nRides sorted by cost:\n"
            );

            for (Ride ride : rides) {

                output.append(
                        "Ride "
                                + ride.rideId
                                + " - Rs. "
                                + ride.cost
                                + "\n"
                );
            }
        }

        output.append(
                "\nRides by departure time:\n"
        );

        for (Map.Entry<String, Ride> entry :
                timeMap.entrySet()) {

            output.append(
                    "Ride "
                            + entry.getValue().rideId
                            + " - "
                            + entry.getValue().departureTime
                            + "\n"
            );
        }
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(
                CarpoolGUI::new
        );
    }
}