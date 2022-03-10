import java.util.ArrayList;
import java.util.Scanner;

public class ATM {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Bank bank = new Bank("Hapoalim");
        User user = bank.addUser("Alex", "Batura", "1234");
        Account newAccount = new Account("Checking", user, bank);
        user.addAccount(newAccount);
        bank.addAccount(newAccount);
        User curUser;
        while (true) {
            curUser = ATM.mainMenu(bank, scanner);
            ATM.printUserMenu(curUser, scanner);
        }
    }

    public static User mainMenu(Bank bank, Scanner sc) {
        String userID;
        String pin;
        User authUser;

        do {
            System.out.printf("\n\nWelcome to %s\n\n", bank.getName());
            System.out.printf("Enter userI ID: ");
            userID = sc.nextLine();
            System.out.printf("Enter pin");
            pin = sc.nextLine();
            authUser = bank.userLogin(userID, pin);
            if (authUser == null) {
                System.out.println("Incorrect user/id/pin combination " +
                                   "Please try again");
            }

        } while (authUser == null);
        return authUser;
    }

    public static void printUserMenu(User user, Scanner scanner) {
        user.printAccountsSummary();
        int choice;
        do {
            System.out.printf("Welcome %s, what would you like to do?\n ", user.getFirstName());
            System.out.println(" 1) Show account transaction history\n" +
                               "  2) Withdrawal\n " +
                               " 3) Deposit\n " +
                               " 4) Transfer\n " +
                               " 5) Exit\n");
            System.out.print("Enter choice: ");
            choice = scanner.nextInt();
            if (choice < 1 || choice > 5) {
                System.out.println("Invalid choice, please enter 1 - 5");
            }
        } while (choice < 1 || choice > 5);
        switch (choice) {
            case 1:
                ATM.showTransactionHistory(user, scanner);
                break;
            case 2:
                ATM.withdrawalFunds(user, scanner);
                break;
            case 3:
                ATM.depositFunds(user, scanner);
                break;
            case 4:
                ATM.transferFunds(user, scanner);
                break;
            case 5:{
                scanner.nextLine();
                break;
            }
        }
        if (choice != 5) {
            ATM.printUserMenu(user, scanner);
        }
    }

    private static void depositFunds(User user, Scanner scanner) {
        int toAcct;
        double amount;
        double acctBal;
        String memo;

        do {
            System.out.printf("Enter the number (1 - %d) of the account\n" +
                              "to deposit in: ",user.numAccounts());
            toAcct = scanner.nextInt() - 1;
            if (toAcct < 0 || toAcct >= user.numAccounts()) {
                System.out.println("Invalid account, please try again");
            }
        } while (toAcct < 0 || toAcct >= user.numAccounts());
        acctBal = user.getAcctBalance(toAcct);

        do {
            System.out.printf("Enter the amount to transfer (max $%.02f) : $ ", acctBal);
            amount = scanner.nextDouble();
            if (amount < 0) {
                System.out.println("amount must be greater than zero");
            }
        } while (amount < 0 );
        scanner.nextLine();
        System.out.println("Enter a memo: ");
        memo = scanner.nextLine();
        user.addAcctTransaction(toAcct, amount, memo);

    }

    private static void transferFunds(User user, Scanner scanner) {
        int fromAcct;
        int toAcct;
        double amount;
        double acctBal;

        do {
            System.out.printf("Enter the number (1 - %d) of the account\n" +
                              "to transfer from: ",user.numAccounts());
            fromAcct = scanner.nextInt() - 1;
            if (fromAcct < 0 || fromAcct >= user.numAccounts()) {
                System.out.println("Invalid account, please try again");
            }
        } while (fromAcct < 0 || fromAcct >= user.numAccounts());
        acctBal = user.getAcctBalance(fromAcct);

        do {
            System.out.printf("Enter the number (1 - %d) of the account\n" +
                              "to transfer to: ",user.numAccounts());
            toAcct = scanner.nextInt() - 1;
            if (toAcct < 0 || toAcct >= user.numAccounts()) {
                System.out.println("Invalid account, please try again");
            }
        } while (toAcct < 0 || toAcct >= user.numAccounts());
        do {
            System.out.printf("Enter the amount to transfer (max $%.02f) : $ ", acctBal);
            amount = scanner.nextDouble();
            if (amount < 0) {
                System.out.println("amount must be greater than zero");
            } else if (acctBal > acctBal) {
                System.out.printf("amount must be lesser than \n" +
                                  "balance of $%.02f.\n", acctBal);
            }
        } while (amount < 0 || amount > acctBal);
        user.addAcctTransaction(fromAcct, -1*amount, String.format("transfer to account %s",
                user.getAcctUUID(toAcct)));
        user.addAcctTransaction(toAcct, amount, String.format("transfer to account %s",
                user.getAcctUUID(fromAcct)));
    }

    private static void withdrawalFunds(User user, Scanner scanner) {
        int fromAcct;
        double amount;
        double acctBal;
        String memo;

        do {
            System.out.printf("Enter the number (1 - %d) of the account\n" +
                              "to withdrew from: ",user.numAccounts());
            fromAcct = scanner.nextInt() - 1;
            if (fromAcct < 0 || fromAcct >= user.numAccounts()) {
                System.out.println("Invalid account, please try again");
            }
        } while (fromAcct < 0 || fromAcct >= user.numAccounts());
        acctBal = user.getAcctBalance(fromAcct);

        do {
            System.out.printf("Enter the amount to withdraw (max $%.02f) : $ ", acctBal);
            amount = scanner.nextDouble();
            if (amount < 0) {
                System.out.println("amount must be greater than zero");
            } else if (amount > acctBal) {
                System.out.printf("amount must be lesser than \n" +
                                  "balance of $%.02f.\n", acctBal);
            }
        } while (amount < 0 || amount > acctBal);
        scanner.nextLine();
        System.out.println("Enter a memo: ");
        memo = scanner.nextLine();
        user.addAcctTransaction(fromAcct, -1 * amount, memo);

    }

    private static void showTransactionHistory(User user, Scanner scanner) {
        int theAcct;
        do {
            System.out.printf("Enter the number (1 - %d) of accounts " +
                              " whose transaction you want to see: ", user.numAccounts());
            theAcct = scanner.nextInt() - 1;
            if (theAcct < 0 || theAcct >= user.numAccounts()) {
                System.out.printf("Invalid account. Please try again");
            }
        } while (theAcct < 0 || theAcct >= user.numAccounts());
        user.printAcctTransHistory(theAcct);
    }
}
