public interface BankInterface {
    public User getUserById(String id);
    public boolean isCardLocked(String userId);
    public boolean pinValidate(String userId, String pinEntered);
    public void incrementFailedAttempts(String cardId);
    public void depositToAccount(String userId, double amount);
    public void withdrawFromAccount(String userId, double amount);
    public double currentBalance(String userId, double amount);
}
