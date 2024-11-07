public class ATM {
    private final Bank bank;
    private User currentUser;

    public ATM(Bank bank) {
        this.bank = bank;
        this.currentUser = null;
    }
    public boolean checkCard() {
        if (currentUser == null) {
            System.out.println("Card is no longer inserted");
            return false;
        } else if (bank.isCardLocked(currentUser.getId())) {
            System.out.println("Card has been locked");
            return false;
        }
        return true;
    }
    public boolean insertCard(User currentUser) {
        if (currentUser != null) {
            if (bank.isCardLocked(currentUser.getId())) {
                System.out.println("This card has been locked.");
                return false;
            }
            System.out.println("Customer number " + currentUser.getId() + " please enter your PIN.");
            this.currentUser = currentUser;
            return true;

        } else {
            System.out.println("Please insert credit card.");
            return false;
        }
    }
    public boolean enterPin(String pinEntered) {
        if (checkCard()) {
            if (bank.pinValidate(this.currentUser.getId(), pinEntered)) {
                currentUser.resetFailedAttempts();
                System.out.println("PIN entered correctly!");
                return true;
            } else {
                currentUser.incrementFailedAttempts();
                if (currentUser.getFailedAttempts() <= 0) {
                    currentUser.lockCard();
                    System.out.println("Your card has been locked. Please contact customer support.");
                } else {
                    currentUser.incrementFailedAttempts();
                    System.out.println("Wrong PIN entered. You have tried " +  currentUser.getFailedAttempts() + " times.");
                }
                return false;
            }
        }

        return false;
    }

    public Double checkBalance() {
        if (!checkCard()) return null;
        System.out.println("Current balance is " + currentUser.getBalance());
        return currentUser.getBalance();
    }

    public boolean deposit(double amount) {
        if (!checkCard()) {
            return false;
        }
        currentUser.deposit(amount);
        System.out.println(amount + " was deposited successfully. New balance: " + currentUser.getBalance());
        return true;
    }

    public boolean withdraw(double amount) {
        if (!checkCard()) {
            return false;
        }

        if (amount > currentUser.getBalance()) {
            System.out.println("Insufficient funds");
            return false;
        }
        currentUser.withdraw(amount);
        System.out.println("Withdrawal successful. New balance: " + currentUser.getBalance());
        return true;
    }

    public void removeCard() {
        currentUser = null;
        System.out.println("Session terminated.");
    }
}
