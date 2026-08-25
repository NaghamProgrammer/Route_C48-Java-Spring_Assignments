import java.util.Scanner;

public class Main {
    static Scanner sc = new Scanner(System.in);

    //hard-coded user data
    static int userPin = 1234;
    static double balance = 2500.75 ;

    static int depositCounter = 0;
    static int withdrawCounter = 0;


    public static void main(String[] args) {
        checkPin();

        boolean running = true;
        while(running){
            printMenu();

            int choice = sc.nextInt();
            switch (choice){
                case 1:
                    checkBalance();
                    break;
                case 2:
                    deposit();
                    break;
                case 3:
                    withdraw();
                    break;
                case 4:
                    showAccountStatus();
                    break;
                case 5:
                    System.out.println("Thank you for using our ATM.");
                    System.out.println("You did " + depositCounter + " successful deposits");
                    System.out.println("You did " + withdrawCounter + " successful withdrawals");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice.");
            }


        }
    }



    public static void checkPin(){
        int attempts = 0;
        int pin;
        do{
            System.out.print("Enter Your PIN: ");
            pin = sc.nextInt();

            if (pin == userPin) {
                return;    //correct PIN continue to the ATM menu
            }

            attempts++;
            System.out.println("Incorrect PIN.");


        }while(attempts < 3); //if you want it to run forever without limit write pin != userPin instead of attempts < 3

        System.out.println("Your account has been locked.");
        System.exit(0);
    }


    public static void printMenu(){
        System.out.println("========= ATM =========");
        System.out.println("1. Check Balance");
        System.out.println("2. Deposit");
        System.out.println("3. Withdraw");
        System.out.println("4. Show Account Status");
        System.out.println("5. Exit");
        System.out.println("=======================");
        System.out.print("Please enter your choice: ");
    }


    public static void checkBalance(){
        System.out.println("Your Current Balance is: " + balance);
    }


    public static void deposit(){
        System.out.print("Enter deposit amount: ");
        double depositAmount = sc.nextDouble();
        if(!checkTransactionValidity(depositAmount)) return;
        balance += depositAmount;
        System.out.println("Deposited: " + depositAmount);
        System.out.println("Your current balance is: " + balance);
        depositCounter++;
    }


    public static void withdraw(){
        System.out.print("Enter withdraw amount: ");
        double withdrawAmount = sc.nextDouble();
        if(!checkTransactionValidity(withdrawAmount)) return;
        if(withdrawAmount <= balance){
            balance -= withdrawAmount;
            balanceWarning();
            System.out.println("Withdrawn: " + withdrawAmount);
            System.out.println("Your current balance is: " + balance);
            withdrawCounter++;

        }else{System.out.println("Insufficient balance.");}

    }


    public static void showAccountStatus(){
      if (balance >= 5000) {System.out.println("VIP Customer");}
      else if (balance >= 1000 && balance < 5000) {System.out.println("Regular Customer");}
      else {System. out.println("Low Balance");}

    }


    public static boolean checkTransactionValidity(double amount){
        //the withdrawal and deposit amount must be > 0 otherwise print "Transaction cancelled."
        if(amount <= 0){
            System.out.println("Transaction invalid");
            return false;
        }
        return true;
    }


    public static void balanceWarning(){
        if (balance == 0){System.out.println("Warning: Your account is empty.");}
    }



}

/*Test Scenarios
* 3 failed attempts of entering pin (PASSED)
* 2 failed attempts of entering pin then a correct attempt (PASSED)
* invalid menu choice user entered 6 (PASSED)
* -ve deposit (PASSED)
* 0 deposit (PASSED)
* withdraw greater than balance (PASSED)
* -ve withdraw (PASSED)
* 0 withdraw (PASSED)
* valid deposit user entered 2000 (PASSED)
* valid withdraw user withdrew 100 (PASSED)
* show account status regular user (PASSED)
* show account status VIP user (PASSED)
* show account status low balance (PASSED)
* floating point deposit (PASSED)
* floating point withdrawal (PASSED)
* balance 0 warning (PASSED)
* exit (PASSED)
* successful deposit counter (PASSED)
* successful withdraw counter (PASSED)
* */
