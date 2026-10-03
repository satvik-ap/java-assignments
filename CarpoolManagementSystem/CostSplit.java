public class CostSplit {

    public static double splitCost(double totalCost, int passengers) {

        if (passengers <= 0) {
            return 0;
        }

        return totalCost / passengers;
    }
}