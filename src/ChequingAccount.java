import java.io.Serializable;

/**
 * This class represents the characteristics of the chequing accounts
 *
 * @author Nilay Das
 */

public class ChequingAccount extends BankAccount implements Serializable {

    //    Declaring the instance variables
    private double balance;
    private final double fee = 1.00;

    //    Constructor
    public ChequingAccount(int accountNumber, String accountPassword) {
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
            balance = balance + amount;
            balance = balance - this.fee;
        }
    }

    //    Withdraw method
    public void withdraw(double amount) {
        if (balance >= amount && amount > 0){
            balance = balance - amount;
            balance = balance - this.fee;
        }
    }

//    toString method
    public String toString(){
        return "Chequing Account";
    }

}
