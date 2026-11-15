public class FeeService {
    public double calculateFee(String feeType, double amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Amount cannot be negative.");
        }
        switch (feeType) {
            case "Tuition":
                return amount;
            case "Laboratory":
                return amount * 0.10;
            case "Library":
                return amount * 0.05;
            case "Sports":
                return amount * 0.03;
            default:
                throw new IllegalArgumentException("Unknown fee type: " + feeType);
        }
    }
}
