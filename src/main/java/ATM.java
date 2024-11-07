public class ATM {
    private Bank bank;
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
                //currentUser.incrementFailedAttempts();
                bank.incrementFailedAttempts(currentUser.getId());
                int attemptsLeft = 3 - currentUser.getFailedAttempts();
                if (attemptsLeft <= 0) {
                    currentUser.lockCard();
                    System.out.println("Your card has been locked. Please contact customer support.");
                } else {
                    bank.incrementFailedAttempts(currentUser.getId());
                    //currentUser.incrementFailedAttempts();
                    System.out.println("Wrong PIN entered. " + attemptsLeft + " attempts remaining.");
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

    public void deposit(double amount) {
    }

    public boolean withdraw(double amount) {
        if (!checkCard()) {
            return false;
        }

        if (amount > currentUser.getBalance()) {
            System.out.println("Insufficient funds");
            return false;
        }
        bank.withdrawFromAccount(currentUser.getId(), amount);

        System.out.println("Withdrawal successful. New balance: " + currentUser.getBalance());
        return true;
    }

}
