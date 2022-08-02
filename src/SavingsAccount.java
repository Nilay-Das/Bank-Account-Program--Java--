import java.io.Serializable;

/**
 * This class represents the characteristics of the savings accounts
 *
 * @author Nilay Das
 */

public class SavingsAccount extends BankAccount implements Serializable {

    //    Declaring the instance variables
    private double balance;
    private final double interest = 2.00/100;

    //    Constructor
    public SavingsAccount(int accountNumber, String accountPassword) {
        super(accountNumber, accountPassword);
        this.balance = 0;
    }

    //    Getters
    public double getBalance() {
        return balance;
    }

    //    Deposit method
    public void deposit(double amount) {
        if(amount >0){
            double interestToAdd = amount * this.interest;
            balance = balance + amount + interestToAdd;
        }
    }

    //    Withdraw method
    public void withdraw(double amount) {
        if (balance > 0 && amount > 0){
            balance = balance - amount;
        }
        else{
            System.out.println("Not sufficient balance in the account");
        }
    }

//    toString method
    public String toString(){
        return "Savings Account";
    }
}
