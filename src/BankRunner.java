import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * This is the runner class for the running the program
 * This class includes all the logic required for creating an account, transferring money, to see the account information and to remove all the records
 *
 * @author Nilay Das
 */


public class BankRunner {
    public static void main(String[] args) throws IOException, ClassNotFoundException{

        Scanner in = new Scanner(System.in);

//        ArrayList to hold the accounts
        ArrayList<BankAccount> listOfAccounts = new ArrayList<BankAccount>();

        //        Storing the data inside the accountList.dat into the arraylist
        try{
            FileInputStream fis = new FileInputStream("accountList.dat");
            ObjectInputStream ois = new ObjectInputStream(fis);

            BankAccount account = null;
            boolean flag = true;

            while(flag){
                if(fis.available() != 0){
                    account = (BankAccount) ois.readObject();
                    listOfAccounts.add(account);
                }
                else{
                    flag = false;
                }
            }
        }
        catch (EOFException | InvalidClassException e) {

        }

        //      Calling the mainMenu method to display the main menu
        mainMenu(in, listOfAccounts);

    }

    /**
     * This method is responsible for viewing the main menu of the program to the console
     * @param in is the scanner object for taking input from the user
     * @param listOfAccounts is the arraylist that stores the accounts as an object
     * @throws IOException
     */
    public static void mainMenu(Scanner in, ArrayList<BankAccount> listOfAccounts) throws IOException {
        System.out.println("Welcome to N.D Bank");
        System.out.println("Please select an option:");
        System.out.println("1. Create an account");
        System.out.println("2. Deposit money");
        System.out.println("3. Withdraw money");
        System.out.println("4. Transfer money");
        System.out.println("5. See account information");
        System.out.println("6. Remove all records (Administrator Only)");
        System.out.println("7. Exit");
        System.out.print("Enter option: ");

        int input = in.nextInt();

        if (input == 1) {
            createAccount(in, listOfAccounts);
        } else if (input == 2) {
            depositMoney(in, listOfAccounts);
        } else if (input == 3) {
            withdrawMoney(in, listOfAccounts);
        } else if (input == 4) {
            transferMoney(in, listOfAccounts);
        } else if (input == 5) {
            accountInformation(in, listOfAccounts);
        } else if (input == 6) {
            removeRecords(in, listOfAccounts);
        }
        else if(input == 7){
            storeIntoFile(listOfAccounts);
            System.exit(0);
        }
        else {
            System.out.println("Wrong option. Please select the correct option.");
            mainMenu(in, listOfAccounts);
        }
    }

    /**
     * This method is responsible for creating a bank account in the program
     * @param in is the scanner object for taking input from the user
     * @param listOfAccounts is the arraylist that stores the accounts as an object
     * @throws IOException
     */
    public static void createAccount(Scanner in, ArrayList<BankAccount> listOfAccounts) throws IOException {
        System.out.println("Select the type of account:");
        System.out.println("1. Chequing Account");
        System.out.println("2. Savings Account");
        System.out.println("3. Back to Main Menu");
        System.out.print("Enter option: ");

        int input = in.nextInt();

        if (input == 1) {
            while (true) {
                System.out.print("Enter Account Number (10 numerical digits): ");
//                Implementing a try-catch to catch exception and show an error message
                try {
                    int accountNumber = in.nextInt();

//                    Checking if the account with the account number already exists
                    for (BankAccount account : listOfAccounts) {
                        if (account.getAccountNumber() == accountNumber) {
                            System.out.println("An account already exists with this account number. Please enter a different account number.");
                            createAccount(in, listOfAccounts);
                        }
                    }

//            Checking if the account number is 10 digits in length
                    String accountNumberString = String.valueOf(accountNumber);
                    if (accountNumberString.length() != 10) {
                        System.out.println("Wrong input. Please enter the account number having 10 numerical digits.");
                        continue;
                    }
                    while (true) {
                        System.out.println("Enter Account Password (10 characters): ");
                        String accountPassword = in.next();
//                    Checking if the account password is 10 digits or letters
                        if (accountPassword.length() != 10) {
                            System.out.println("Wrong input. Please enter the account password having 10 characters.");
                            continue;
                        }
                        ChequingAccount account = new ChequingAccount(accountNumber, accountPassword);
                        listOfAccounts.add(account);
                        System.out.println("Account successfully created. Returning back to Main Menu.");
                        System.out.println();
                        mainMenu(in, listOfAccounts);
                    }

                } catch (Exception InputMismatchException) {
                    System.out.println("Wrong input. Please enter the account number having 10 numerical digits.");
                    in.next();
                }
            }

        } else if (input == 2) {
            while (true) {
//              Implementing a try-catch to catch exception and show an error message
                try {
                    System.out.print("Enter Account Number (10 numerical digits): ");
                    int accountNumber = in.nextInt();

//                  Checking if the account with the account number already exists
                    for (BankAccount account : listOfAccounts) {
                        if (account.getAccountNumber() == accountNumber) {
                            System.out.println("An account already exists with this account number. Please enter a different account number.");
                            createAccount(in, listOfAccounts);
                        }
                    }

//                  Checking if the account number is 10 digits in length
                    String accountNumberString = String.valueOf(accountNumber);
                    if (accountNumberString.length() != 10) {
                        System.out.println("Wrong input. Please enter the account number having 10 numerical digits.");
                        continue;
                    }
                    while (true) {
                        System.out.println("Enter Account Password (10 characters): ");
                        String accountPassword = in.next();
//                    Checking if the account password is 10 digits or letters
                        if (accountPassword.length() != 10) {
                            System.out.println("Wrong input. Please enter the account password having 10 characters.");
                            continue;
                        }
                        SavingsAccount account = new SavingsAccount(accountNumber, accountPassword);
                        listOfAccounts.add(account);
                        System.out.println("Account successfully created. Returning back to Main Menu.");
                        System.out.println();
                        mainMenu(in, listOfAccounts);
                    }
                } catch (Exception InputMismatchException) {
                    System.out.println("Wrong input. Please enter the account number having 10 numerical digits.");
                    in.next();
                }
            }
        } else if (input == 3) {
            mainMenu(in, listOfAccounts);
        } else {
            System.out.println("Wrong option. Please select the correct option.");
            createAccount(in, listOfAccounts);
        }
    }

    public static void depositMoney(Scanner in, ArrayList<BankAccount> listOfAccounts) throws IOException {

        //      Checking if there is any available account in the arraylist or not
        if (listOfAccounts.isEmpty()) {
            System.out.println("Sorry. There is no available account to deposit money.");
            System.out.println("Press 1 to return to Main Menu and Press 2 to Exit.");

            int input = in.nextInt();

            if (input == 1) {
                mainMenu(in, listOfAccounts);
            } else if (input == 2) {
                storeIntoFile(listOfAccounts);
                System.exit(0);
            } else {
                System.out.println("Wrong option. Returning back to Main Menu.");
                System.out.println();
                mainMenu(in, listOfAccounts);
            }
        } else {
            System.out.println("Enter Account Number:");
            int accountNumber = in.nextInt();
            System.out.println("Enter Account Password:");
            String accountPassword = in.next();


//        Searching for the account number and password within the listOfAccounts
            boolean flag = true;
            for (int i = 0; i < listOfAccounts.size(); i++) {
                if (listOfAccounts.get(i).getAccountNumber() == accountNumber && listOfAccounts.get(i).getAccountPassword().equals(accountPassword)) {
                    String accountType = listOfAccounts.get(i).toString();
                    System.out.println("Your account is a: " + accountType);

                    System.out.println("Please enter the amount to deposit: ");
                    double amount = in.nextDouble();

//                    Checking if the amount is a negative number
                    if(amount < 0){
                        System.out.println("Negative amount cannot be deposited. Returning back to Main Menu.");
                        System.out.println();
                        mainMenu(in, listOfAccounts);
                    }

//                Depositing the money into the account
                    listOfAccounts.get(i).deposit(amount);

                    String accountNumberString = String.valueOf(listOfAccounts.get(i).getAccountNumber());
                    System.out.println("$" + amount + " has been deposited into the " + listOfAccounts.get(i).toString() + " (" + accountNumberString + ")");
                    if(listOfAccounts.get(i) instanceof SavingsAccount){
                        System.out.println("A 2% interest has been applied.");
                    }
                    if(listOfAccounts.get(i) instanceof ChequingAccount){
                        System.out.println("A $1 transaction fee has been applied.");
                    }
                    System.out.println("Your current balance is: " + "$" + listOfAccounts.get(i).getBalance());
                    flag = false;
                    break;
                }
            }

//          If the account name or password does not match
            if (flag) {
                System.out.println("Wrong account number or password!");
                System.out.println("Press 1 to try again and Press 2 to return to Main Menu");
                int option = in.nextInt();

                if (option == 1) {
                    depositMoney(in, listOfAccounts);
                } else if (option == 2) {
                    mainMenu(in, listOfAccounts);
                } else {
                    System.out.println("Wrong option. Returning back to Main Menu.");
                    System.out.println();
                    mainMenu(in, listOfAccounts);
                }
            }

            System.out.println("Press 1 to return to Main Menu and Press 2 to Exit.");
            int input = in.nextInt();
            if (input == 1) {
                mainMenu(in, listOfAccounts);
            } else if (input == 2) {
                storeIntoFile(listOfAccounts);
                System.exit(0);
            } else {
                System.out.println("Wrong option. Please select the correct option.");
            }
        }
    }

    /**
     * This method is responsible for withdrawing money from a bank account
     * @param in is the scanner object for taking input from the user
     * @param listOfAccounts is the arraylist that stores the accounts as an object
     * @throws IOException
     */
    public static void withdrawMoney(Scanner in, ArrayList<BankAccount> listOfAccounts) throws IOException {

        //      Checking if there is any available account in the arraylist or not
        if (listOfAccounts.isEmpty()) {
            System.out.println("Sorry. There is no available account to withdraw money.");
            System.out.println("Press 1 to return to Main Menu and Press 2 to Exit.");

            int input = in.nextInt();

            if (input == 1) {
                mainMenu(in, listOfAccounts);
            } else if (input == 2) {
                storeIntoFile(listOfAccounts);
                System.exit(0);
            } else {
                System.out.println("Wrong option. Returning back to Main Menu.");
                System.out.println();
                mainMenu(in, listOfAccounts);
            }
        } else {
            System.out.println("Enter Account Number:");
            int accountNumber = in.nextInt();
            System.out.println("Enter Account Password:");
            String accountPassword = in.next();


//        Searching for the account number and password within the listOfAccounts
            boolean flag = true;
            for (int i = 0; i < listOfAccounts.size(); i++) {
                if (listOfAccounts.get(i).getAccountNumber() == accountNumber && listOfAccounts.get(i).getAccountPassword().equals(accountPassword)) {
                    String accountType = listOfAccounts.get(i).toString();
                    System.out.println("Your account is a: " + accountType);

                    System.out.println("Please enter the amount to withdraw: ");
                    double amount = in.nextDouble();

//                    Checking if the amount is a negative number
                    if(amount < 0){
                        System.out.println("Negative amount cannot be withdrawn. Returning back to Main Menu.");
                        System.out.println();
                        mainMenu(in, listOfAccounts);
                    }

                    // Checking if the amount to withdraw is greater than the current balance
                    if (listOfAccounts.get(i).getBalance() < amount) {
                        System.out.println("Not sufficient balance in the account.");
                        System.out.println("Press 1 to try again and Press 2 to return to Main Menu.");
                        int option = in.nextInt();
                        if (option == 1) {
                            withdrawMoney(in, listOfAccounts);
                        } else if (option == 2) {
                            mainMenu(in, listOfAccounts);
                        } else {
                            System.out.println("Wrong option. Returning back to Main Menu.");
                            System.out.println();
                            mainMenu(in, listOfAccounts);
                        }

                    } else {
                        //Withdrawing the money from the account
                        listOfAccounts.get(i).withdraw(amount);
                        String accountNumberString = String.valueOf(listOfAccounts.get(i).getAccountNumber());
                        System.out.println("$" + amount + " has been withdrawn from the " + listOfAccounts.get(i).toString() + " (" + accountNumberString + ")");
                        if(listOfAccounts.get(i) instanceof ChequingAccount){
                            System.out.println("A $1 transaction fee has been applied.");
                        }
                        System.out.println("Your current balance is: " + "$" + listOfAccounts.get(i).getBalance());
                        flag = false;
                        break;
                    }
                }
            }

//              If the account name or password does not match
            if (flag) {
                System.out.println("Wrong account number or password!");
                System.out.println("Press 1 to try again and Press 2 to return to Main Menu");
                int option = in.nextInt();

                if (option == 1) {
                    withdrawMoney(in, listOfAccounts);
                } else if (option == 2) {
                    mainMenu(in, listOfAccounts);
                } else {
                    System.out.println("Wrong option. Returning back to Main Menu.");
                    System.out.println();
                    mainMenu(in, listOfAccounts);
                }
            }


            System.out.println("Press 1 to return to Main Menu and Press 2 to Exit.");
            int input = in.nextInt();
            if (input == 1) {
                mainMenu(in, listOfAccounts);
            } else if (input == 2) {
                storeIntoFile(listOfAccounts);
                System.exit(0);
            } else {
                System.out.println("Wrong option. Please select the correct option.");
            }
        }

    }

    /**
     * This method is responsible for viewing the account information in the console
     * @param in is the scanner object for taking input from the user
     * @param listOfAccounts is the arraylist that stores the accounts as an object
     * @throws IOException
     */
    public static void accountInformation(Scanner in, ArrayList<BankAccount> listOfAccounts) throws IOException {

//        Option for viewing individual account or all account
        System.out.println("Select an option:");
        System.out.println("1. View individual account information");
        System.out.println("2. View all account information (Administrator Only)");
        System.out.println("3. Back to Main Menu");
        System.out.println("Enter Option: ");

        int input = in.nextInt();

        if (input == 1) {

            //      Checking if there is any available account in the arraylist or not
            if (listOfAccounts.isEmpty()) {
                System.out.println("Sorry. There is no available account to show information.");
                System.out.println("Press 1 to return to Main Menu and Press 2 to Exit.");

                int option = in.nextInt();

                if (option == 1) {
                    mainMenu(in, listOfAccounts);
                } else if (option == 2) {
                    storeIntoFile(listOfAccounts);
                    System.exit(0);
                } else {
                    System.out.println("Wrong option. Returning back to Main Menu.");
                    System.out.println();
                    mainMenu(in, listOfAccounts);
                }
            } else {
                System.out.println("Enter Account Number:");
                int accountNumber = in.nextInt();
                System.out.println("Enter Account Password:");
                String accountPassword = in.next();

                //        Searching for the account number and password within the listOfAccounts
                boolean flag = true;
                for (int i = 0; i < listOfAccounts.size(); i++) {
                    if (listOfAccounts.get(i).getAccountNumber() == accountNumber && listOfAccounts.get(i).getAccountPassword().equals(accountPassword)) {
                        String accountType = listOfAccounts.get(i).toString();
                        System.out.println("Your account is a: " + accountType);
                        System.out.println("Current Balance: " + "$" + listOfAccounts.get(i).getBalance());
                        flag = false;
                        break;
                    }

                }

//          If the account name or password does not match
                if (flag) {
                    System.out.println("Wrong account number or password!");
                    System.out.println("Press 1 to try again and Press 2 to return to Main Menu");

                    int option = in.nextInt();

                    if (option == 1) {
                        accountInformation(in, listOfAccounts);
                    } else if (option == 2) {
                        mainMenu(in, listOfAccounts);
                    } else {
                        System.out.println("Wrong option. Please select the correct option.");
                    }

                }

                System.out.println("Press 1 to return to Main Menu and Press 2 to Exit.");
                int option = in.nextInt();
                if (option == 1) {
                    mainMenu(in, listOfAccounts);
                } else if (option == 2) {
                    storeIntoFile(listOfAccounts);
                    System.exit(0);
                } else {
                    System.out.println("Wrong option. Please select the correct option.");
                }
            }

        } else if (input == 2) {
            System.out.println("Enter Administrator Password:");
            String password = in.next();

            if (password.equals("2022")) {
                //Checking if the arraylist is empty
                if(listOfAccounts.isEmpty()){
                    System.out.println("Sorry. There is no available account to show information. Returning back to Main Menu.");
                    System.out.println();
                    mainMenu(in, listOfAccounts);
                }
                System.out.println();
                System.out.println("All the account information are as follows:");
                System.out.println();
                for (int i = 0; i<listOfAccounts.size(); i++) {
                    System.out.println(i+1 + "." + " Account Type: " + listOfAccounts.get(i).toString() + " | " + " Account Number: " + listOfAccounts.get(i).getAccountNumber() + " | " + " Current Balance: " + "$" + listOfAccounts.get(i).getBalance());
                    System.out.println();
                }
                System.out.println("Returning back to Main Menu.");
                System.out.println();
                mainMenu(in, listOfAccounts);
            } else {
                System.out.println("Wrong Password! Returning to the Account Information Menu.");
                accountInformation(in, listOfAccounts);
            }
        } else if (input == 3) {
            mainMenu(in, listOfAccounts);
        } else {
            System.out.println("Wrong option. Returning back to Main Menu.");
            System.out.println();
            mainMenu(in, listOfAccounts);
        }
    }

    /**
     * This method is responsible for transferring money between bank accounts
     * @param in is the scanner object for taking input from the user
     * @param listOfAccounts is the arraylist that stores the accounts as an object
     * @throws IOException
     */
    public static void transferMoney(Scanner in, ArrayList<BankAccount> listOfAccounts) throws IOException {

//      Checking if there is any available account in the arraylist or not
        if (listOfAccounts.isEmpty()) {
            System.out.println("Sorry. There is no available account to transfer money.");
            System.out.println("Press 1 to return to Main Menu and Press 2 to Exit.");

            int input = in.nextInt();

            if (input == 1) {
                mainMenu(in, listOfAccounts);
            } else {
                storeIntoFile(listOfAccounts);
                System.exit(0);
            }
        } else {
            System.out.println("Select the transfer option:");
            System.out.println("1. From Chequing Account to Savings Account");
            System.out.println("2. From Savings Account to Chequing Account");
            System.out.println("3. From Chequing Account to Chequing Account");
            System.out.println("4. From Savings Account to Savings Account");
            System.out.println("5. Back to Main Menu");
            System.out.println("Enter option: ");

            int input = in.nextInt();

            if (input == 1) {

//              Instance of the accounts
                ChequingAccount chequingAccount = null;
                SavingsAccount savingsAccount = null;

                System.out.println("Enter Chequing Account Number:");
                int chequingAccountNumber = in.nextInt();
                System.out.println("Enter Chequing Account Password:");
                String chequingAccountPassword = in.next();

//              Checking if the chequing account information is correct or not
                boolean flag1 = true;
                for (int i = 0; i < listOfAccounts.size(); i++) {
                    if (listOfAccounts.get(i).getAccountNumber() == chequingAccountNumber && listOfAccounts.get(i).getAccountPassword().equals(chequingAccountPassword) && listOfAccounts.get(i) instanceof ChequingAccount) {
                        System.out.println("Account information is correct.");
                        chequingAccount = (ChequingAccount) listOfAccounts.get(i);
                        flag1 = false;
                        break;
                    }
                }
                if (flag1) {
                    System.out.println("There is no account with this account name or password. Please try again.");
                    transferMoney(in, listOfAccounts);
                }
                System.out.println("Enter Savings Account Number: ");
                int savingsAccountNumber = in.nextInt();
                System.out.println("Enter Savings Account Password: ");
                String savingsAccountPassword = in.next();

//              Checking if the savings account information is correct or not
                boolean flag2 = true;
                for (int i = 0; i < listOfAccounts.size(); i++) {
                    if (listOfAccounts.get(i).getAccountNumber() == savingsAccountNumber && listOfAccounts.get(i).getAccountPassword().equals(savingsAccountPassword) && listOfAccounts.get(i) instanceof SavingsAccount) {
                        System.out.println("Account information is correct.");
                        savingsAccount = (SavingsAccount) listOfAccounts.get(i);
                        flag2 = false;
                        break;
                    }
                }
                if (flag2) {
                    System.out.println("There is no account with this account name or password. Please try again.");
                    transferMoney(in, listOfAccounts);
                }

                System.out.println("Enter the amount to transfer: ");
                double amount = in.nextDouble();

//                Checking if the amount is a negative number
                if(amount < 0){
                    System.out.println("Negative amount cannot be transferred. Returning to the Transfer Menu.");
                    transferMoney(in, listOfAccounts);
                }

                double balance = chequingAccount.getBalance();

//                Withdrawing money from the chequing account and depositing money to the savings account
                if (balance >= amount) {
                    chequingAccount.withdraw(amount);
                    savingsAccount.deposit(amount);
                    System.out.println("$" + amount + " has been successfully transferred from Chequing Account" + " (" + chequingAccount.getAccountNumber() + ") to Savings Account" + " (" + savingsAccount.getAccountNumber() + ")");
                    System.out.println("Current balance of the Chequing Account" + " (" + chequingAccount.getAccountNumber() + "): " + "$" + chequingAccount.getBalance());
                    System.out.println("Press 1 to return to Main Menu and Press 2 to Exit.");
                    int option = in.nextInt();
                    if (option == 1) {
                        mainMenu(in, listOfAccounts);
                    } else if (option == 2) {
                        storeIntoFile(listOfAccounts);
                        System.exit(0);
                    } else {
                        System.out.println("Wrong option. Returning back to Main Menu.");
                        System.out.println();
                        mainMenu(in, listOfAccounts);
                    }
                } else {
                    System.out.println("There is not sufficient balance in the Chequing Account. Transaction cancelled.");
                    System.out.println("Returning to the Transfer Menu.");
                    transferMoney(in, listOfAccounts);
                }
            } else if (input == 2) {
                //              Instance of the accounts
                ChequingAccount chequingAccount = null;
                SavingsAccount savingsAccount = null;

                System.out.println("Enter Savings Account Number:");
                int savingsAccountNumber = in.nextInt();
                System.out.println("Enter Savings Account Password:");
                String savingsAccountPassword = in.next();

//              Checking if the savings account information is correct or not
                boolean flag1 = true;
                for (int i = 0; i < listOfAccounts.size(); i++) {
                    if (listOfAccounts.get(i).getAccountNumber() == savingsAccountNumber && listOfAccounts.get(i).getAccountPassword().equals(savingsAccountPassword) && listOfAccounts.get(i) instanceof SavingsAccount) {
                        System.out.println("Account information is correct.");
                        savingsAccount = (SavingsAccount) listOfAccounts.get(i);
                        flag1 = false;
                        break;
                    }
                }
                if (flag1) {
                    System.out.println("There is no account with this account name or password. Please try again.");
                    transferMoney(in, listOfAccounts);
                }
                System.out.println("Enter Chequing Account Number: ");
                int chequingAccountNumber = in.nextInt();
                System.out.println("Enter Chequing Account Password: ");
                String chequingAccountPassword = in.next();

//              Checking if the chequing account information is correct or not
                boolean flag2 = true;
                for (int i = 0; i < listOfAccounts.size(); i++) {
                    if (listOfAccounts.get(i).getAccountNumber() == chequingAccountNumber && listOfAccounts.get(i).getAccountPassword().equals(chequingAccountPassword) && listOfAccounts.get(i) instanceof ChequingAccount) {
                        System.out.println("Account information is correct.");
                        chequingAccount = (ChequingAccount) listOfAccounts.get(i);
                        flag2 = false;
                        break;
                    }
                }
                if (flag2) {
                    System.out.println("There is no account with this account name or password. Please try again.");
                    transferMoney(in, listOfAccounts);
                }

                System.out.println("Enter the amount to transfer: ");
                double amount = in.nextDouble();

//              Checking if the amount is a negative number
                if(amount < 0){
                    System.out.println("Negative amount cannot be transferred. Returning to the Transfer Menu.");
                    transferMoney(in, listOfAccounts);
                }

                double balance = savingsAccount.getBalance();

//                Withdrawing money from the chequing account and depositing money to the savings account
                if (balance >= amount) {
                    savingsAccount.withdraw(amount);
                    chequingAccount.deposit(amount);
                    System.out.println("$" + amount + " has been successfully transferred from Savings Account" + " (" + savingsAccount.getAccountNumber() + ") to Chequing Account" + " (" + chequingAccount.getAccountNumber() + ")");
                    System.out.println("Current balance of the Savings Account" + " (" + savingsAccount.getAccountNumber() + "): " + "$" + savingsAccount.getBalance());
                    System.out.println("Press 1 to return to Main Menu and Press 2 to Exit.");
                    int option = in.nextInt();
                    if (option == 1) {
                        mainMenu(in, listOfAccounts);
                    } else if (option == 2) {
                        storeIntoFile(listOfAccounts);
                        System.exit(0);
                    } else {
                        System.out.println("Wrong option. Returning back to Main Menu.");
                        System.out.println();
                        mainMenu(in, listOfAccounts);
                    }
                } else {
                    System.out.println("There is not sufficient balance in the Savings Account. Transaction cancelled.");
                    System.out.println("Returning to the Transfer Menu.");
                    transferMoney(in, listOfAccounts);
                }
            }
            else if(input == 3){

                //              Instance of the accounts
                ChequingAccount chequingAccount1 = null;
                ChequingAccount chequingAccount2 = null;

                System.out.println("Enter Chequing Account Number to transfer money from: ");
                int chequingAccountNumber1 = in.nextInt();
                System.out.println("Enter Chequing Account Password to transfer money from: ");
                String chequingAccountPassword1 = in.next();

//              Checking if the chequing account information is correct or not
                boolean flag1 = true;
                for (int i = 0; i < listOfAccounts.size(); i++) {
                    if (listOfAccounts.get(i).getAccountNumber() == chequingAccountNumber1 && listOfAccounts.get(i).getAccountPassword().equals(chequingAccountPassword1) && listOfAccounts.get(i) instanceof ChequingAccount) {
                        System.out.println("Account information is correct.");
                        chequingAccount1 = (ChequingAccount) listOfAccounts.get(i);
                        flag1 = false;
                        break;
                    }
                }
                if (flag1) {
                    System.out.println("There is no with this account name or password. Please try again.");
                    transferMoney(in, listOfAccounts);
                }
                System.out.println("Enter Chequing Account Number to transfer money to: ");
                int chequingAccountNumber2 = in.nextInt();
                System.out.println("Enter Chequing Account Password to transfer money to: ");
                String chequingAccountPassword2 = in.next();

//              Checking if the chequing account information is correct or not
                boolean flag2 = true;
                for (int i = 0; i < listOfAccounts.size(); i++) {
                    if (listOfAccounts.get(i).getAccountNumber() == chequingAccountNumber2 && listOfAccounts.get(i).getAccountPassword().equals(chequingAccountPassword2) && listOfAccounts.get(i) instanceof ChequingAccount) {
                        System.out.println("Account information is correct.");
                        chequingAccount2 = (ChequingAccount) listOfAccounts.get(i);
                        flag2 = false;
                        break;
                    }
                }
                if (flag2) {
                    System.out.println("There is no account with this account name or password. Please try again.");
                    transferMoney(in, listOfAccounts);
                }

                System.out.println("Enter the amount to transfer: ");
                double amount = in.nextDouble();

//                Checking if the amount is a negative number
                if(amount < 0){
                    System.out.println("Negative amount cannot be transferred. Returning to the Transfer Menu.");
                    transferMoney(in, listOfAccounts);
                }

                double balance = chequingAccount1.getBalance();

//                Withdrawing money from the chequing account and depositing money to the other chequing account
                if (balance >= amount) {
                    chequingAccount1.withdraw(amount);
                    chequingAccount2.deposit(amount);
                    System.out.println("$" + amount + " has been successfully transferred from Chequing Account" + " (" + chequingAccount1.getAccountNumber() + ") to Chequing Account" + " (" + chequingAccount2.getAccountNumber() + ")");
                    System.out.println("Current balance of the Chequing Account" + " (" + chequingAccount1.getAccountNumber() + "): " + "$" + chequingAccount1.getBalance());
                    System.out.println("Press 1 to return to Main Menu and Press 2 to Exit.");
                    int option = in.nextInt();
                    if (option == 1) {
                        mainMenu(in, listOfAccounts);
                    } else if (option == 2) {
                        storeIntoFile(listOfAccounts);
                        System.exit(0);
                    } else {
                        System.out.println("Wrong option. Returning back to Main Menu.");
                        System.out.println();
                        mainMenu(in, listOfAccounts);
                    }
                } else {
                    System.out.println("There is not sufficient balance in the Chequing Account. Transaction cancelled.");
                    System.out.println("Returning to the Transfer Menu.");
                    transferMoney(in, listOfAccounts);
                }
            }
            else if (input == 4){

                //              Instance of the accounts
                SavingsAccount savingsAccount1 = null;
                SavingsAccount savingsAccount2 = null;

                System.out.println("Enter Savings Account Number to transfer money from: ");
                int savingsAccountNumber1 = in.nextInt();
                System.out.println("Enter Savings Account Password to transfer money from: ");
                String savingsAccountPassword1 = in.next();

//              Checking if the savings account information is correct or not
                boolean flag1 = true;
                for (int i = 0; i < listOfAccounts.size(); i++) {
                    if (listOfAccounts.get(i).getAccountNumber() == savingsAccountNumber1 && listOfAccounts.get(i).getAccountPassword().equals(savingsAccountPassword1) && listOfAccounts.get(i) instanceof SavingsAccount) {
                        System.out.println("Account information is correct.");
                        savingsAccount1 = (SavingsAccount) listOfAccounts.get(i);
                        flag1 = false;
                        break;
                    }
                }
                if (flag1) {
                    System.out.println("There is no account with this account name or password. Please try again.");
                    transferMoney(in, listOfAccounts);
                }
                System.out.println("Enter Savings Account Number to transfer money to: ");
                int savingsAccountNumber2 = in.nextInt();
                System.out.println("Enter Savings Account Password to transfer money to: ");
                String savingsAccountPassword2 = in.next();

//              Checking if the savings account information is correct or not
                boolean flag2 = true;
                for (int i = 0; i < listOfAccounts.size(); i++) {
                    if (listOfAccounts.get(i).getAccountNumber() == savingsAccountNumber2 && listOfAccounts.get(i).getAccountPassword().equals(savingsAccountPassword2) && listOfAccounts.get(i) instanceof SavingsAccount) {
                        System.out.println("Account information is correct.");
                        savingsAccount2 = (SavingsAccount) listOfAccounts.get(i);
                        flag2 = false;
                        break;
                    }
                }
                if (flag2) {
                    System.out.println("There is no with this account name or password. Please try again.");
                    transferMoney(in, listOfAccounts);
                }

                System.out.println("Enter the amount to transfer: ");
                double amount = in.nextDouble();

//                Checking if the amount is a negative number
                if(amount < 0){
                    System.out.println("Negative amount cannot be transferred. Returning to the Transfer Menu.");
                    transferMoney(in, listOfAccounts);
                }

                double balance = savingsAccount1.getBalance();

//                Withdrawing money from the chequing account and depositing money to the other chequing account
                if (balance >= amount) {
                    savingsAccount1.withdraw(amount);
                    savingsAccount2.deposit(amount);
                    System.out.println("$" + amount + " has been successfully transferred from Savings Account" + " (" + savingsAccount1.getAccountNumber() + ") to Savings Account" + " (" + savingsAccount2.getAccountNumber() + ")");
                    System.out.println("Current balance of the Savings Account" + " (" + savingsAccount1.getAccountNumber() + "): " + "$" + savingsAccount1.getBalance());
                    System.out.println("Press 1 to return to Main Menu and Press 2 to Exit.");
                    int option = in.nextInt();
                    if (option == 1) {
                        mainMenu(in, listOfAccounts);
                    } else if (option == 2) {
                        storeIntoFile(listOfAccounts);
                        System.exit(0);
                    } else {
                        System.out.println("Wrong option. Returning back to Main Menu.");
                        System.out.println();
                        mainMenu(in, listOfAccounts);
                    }
                } else {
                    System.out.println("There is not sufficient balance in the Savings Account. Transaction cancelled.");
                    System.out.println("Returning to the Transfer Menu.");
                    transferMoney(in, listOfAccounts);
                }
            }
            else if (input == 5){
                mainMenu(in, listOfAccounts);
            }
            else{
                System.out.println("Wrong option. Returning to the Transfer Menu.");
                transferMoney(in, listOfAccounts);
            }
        }
    }

    /**
     * This method is responsible for storing the data inside the arraylist into the file (accountList.dat)
     * @param listOfAccounts is the arraylist that stores the accounts as an object
     * @throws IOException
     */
    public static void storeIntoFile(ArrayList<BankAccount> listOfAccounts) throws IOException {
        ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream("accountList.dat"));
        for(BankAccount accountReference: listOfAccounts){
            output.writeObject(accountReference);
        }
        output.close();
    }

    /**
     * This method is responsible for removing the all the data that is stored inside the file (accountList.dat)
     * @param in is the scanner object for taking input from the user
     * @param listOfAccounts is the arraylist that stores the accounts as an object
     * @throws IOException
     */
    public static void removeRecords(Scanner in, ArrayList<BankAccount> listOfAccounts) throws IOException {
        System.out.println("Enter Administrator Password:");
        String password = in.next();

        if(password.equals("2022")){
            OutputStream os = null;
            try {
                os = new FileOutputStream("accountList.dat");
            } finally {
                if (os != null) {
                    os.close();
                    System.out.println("All account records have been successfully removed. Exiting the program to apply the changes.");
                    System.exit(0);
                }
            }
        }
        else{
            System.out.println("Wrong Password! Returning back to Main Menu.");
            System.out.println();
            mainMenu(in, listOfAccounts);
        }
    }
}
