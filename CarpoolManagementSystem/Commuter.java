public class Commuter {

    int id;
    String name;
    String source;
    String destination;

    Commuter(int id, String name, String source, String destination) {
        this.id = id;
        this.name = name;
        this.source = source;
        this.destination = destination;
    }

    void showCommuter() {
        System.out.println("ID: " + id);
        System.out.println("Name: " + name);
        System.out.println("From: " + source);
        System.out.println("To: " + destination);
    }
}