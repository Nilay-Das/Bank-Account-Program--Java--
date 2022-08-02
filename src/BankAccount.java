import java.io.Serializable;

/**
 * This class represents the base characteristics of the bank accounts
 *
 * @author Nilay Das
 */

public abstract class BankAccount implements Serializable {

    // Declaring the instance variables
    private int accountNumber;
    private String accountPassword;
    private double balance;

    // Constructor
    public BankAccount(int accountNumber, String accountPassword) {
        this.accountNumber = accountNumber;
        this.accountPassword = accountPassword;
        this.balance = 0;
    }

    // Getters
    public int getAccountNumber() {
        return accountNumber;
    }

    public String getAccountPassword() {
        return accountPassword;
    }

    public double getBalance() {
        return balance;
    }

    // Abstract methods
    public abstract void deposit(double amount);

    public abstract void withdraw(double amount);

}
