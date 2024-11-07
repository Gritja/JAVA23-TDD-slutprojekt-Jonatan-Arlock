import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Bank implements BankInterface {
    private Map<String, User> users = new HashMap<>();

    public Bank() {
        users.put("01", new User ("01", "1234", 1234));
    }
    public User getUserById(String id) {
        return users.get(id);
    }

    public boolean isCardLocked(String userId) {
        User user = users.get(userId);
        System.out.println("Checking if card is locked for user: " + userId + ", result: " + (user != null && user.isLocked()));
        return user != null && user.isLocked();
    }
    public boolean pinValidate(String userId, String pinEntered) {
        User user = users.get(userId);
        System.out.println("Validating PIN for user: " + userId + ", entered PIN: " + pinEntered);
        if (Objects.equals(user.getPin(), pinEntered)) {
            System.out.println("PIN validated successfully");
            return true;
        }
        System.out.println("Invalid PIN");
        return false;
    }
    public void incrementFailedAttempts(String userId) {
        User user = users.get(userId);
        user.incrementFailedAttempts();
    }

    public void depositToAccount(String userId, double amount) {
        User user = users.get(userId);
        user.deposit(amount);
    }

    public void withdrawFromAccount(String userId, double amount) {
        User user = users.get(userId);
        user.withdraw(amount);
    }
    /*public void addUser(User user) {
        users.put(user.getId(), user);
    }
*/
    public static String getBankName() {
        return "MockBank";
    }
    public double currentBalance(String userId) {
        User user = users.get(userId);
        return user.getBalance();
    }
}
