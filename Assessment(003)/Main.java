import java.util.Scanner;

public class Main {


    static Scanner sc = new Scanner(System.in);


    static boolean[][] seats;
    static String[] movieNames;


    public static void main(String[] args) {
        dataTaker();

        boolean running = true;
        while(running){
            byte choice = menu();

            switch (choice){
                case 1:
                    displaySeats(seats);
                    break;
                case 2:
                    bookSeat(seats);
                    break;
                case 3:
                    cancelBooking(seats);
                    break;
                case 4:
                    showAllMovies(movieNames);
                    break;
                case 5:
                    showAvailableAndBookedSeats(seats);
                    break;

                case 6:
                    System.out.println("Thanks for using our program!");
                    running = false;
                    break;

                default:
                    System.out.println("Invalid choice. Try again.");


            }
        }



    }

    static void dataTaker() {

        int rows = getPositiveInt("Enter number of rows: ");
        int columns = getPositiveInt("Enter number of columns: ");

        seats = new boolean[rows][columns];

        int numberOfMovies = getPositiveInt("Enter number of movies: ");

        movieNames = new String[numberOfMovies];

        for (int i = 0; i < movieNames.length; i++) {
            System.out.print("Enter movie " + (i + 1) + ": ");
            movieNames[i] = sc.next();
        }
    }


    static byte menu(){
        byte choice = 0;
        do {
            System.out.println("________Menu Choices_______");
            System.out.println("1. Display Seats");
            System.out.println("2. Book Seat");
            System.out.println("3. Cancel Booking");
            System.out.println("4. Show all movies.");
            System.out.println("5. Show number of available and booked seats");
            System.out.println("6. Exit");
            System.out.print("Please enter your choice: ");

            if (!sc.hasNextByte()) {
                System.out.println("Please enter a valid number.");
                sc.next();
                continue;
            }

            choice = sc.nextByte();

            if (choice < 1 || choice > 6) {
                System.out.println("Please enter a number between 1 and 6");
            }

        } while (choice < 1 || choice > 6);

        return choice;
    }


    static void displaySeats(boolean[][] seats){
        for(int i = 0; i < seats.length; i++){
            for(int j = 0; j < seats[0].length; j++){

                if(seats[i][j]){System.out.print("X");}
                else{System.out.print("O");}
            }
            System.out.println();
        }
    }


    static void bookSeat(boolean[][] seats) {

        int[] seat = getSeat(seats);

        int row = seat[0];
        int column = seat[1];

        if (seats[row][column]) {
            System.out.println("Seat " + (row+1) + (column+1) + " already booked");
        } else {
            seats[row][column] = true;
            System.out.println("Seat " + (row+1) + (column+1) + " booked successfully");
        }
    }


    static void cancelBooking(boolean[][] seats) {

        int[] seat = getSeat(seats);

        int row = seat[0];
        int column = seat[1];

        if (!seats[row][column]) {
            System.out.println("Seat " + (row+1) + (column+1) + " is already not booked therfeore can't cancel booking");
        } else {
            seats[row][column] = false;
            System.out.println("seat " + (row+1) + (column+1) + " booking cancelled");
        }
    }


    static void showAllMovies(String[] movieNames){
        System.out.println("Available movies: ");
        for(int i = 0; i < movieNames.length; i++){
            System.out.println(movieNames[i]);
        }
    }


    static void showAvailableAndBookedSeats(boolean[][] seats){
        int counterBooked = 0;
        int counterAvailable = 0;


        for(int i = 0; i < seats.length; i++){
            for(int j = 0; j < seats[0].length; j++){
                if(seats[i][j]){counterBooked++;}
                else{counterAvailable++;}
            }
        }

        System.out.println("we have " + counterAvailable + " available seats");
        System.out.println("Available seat numbers are: " );
        for (int i = 0; i < seats.length; i++) {
            for (int j = 0; j < seats[0].length; j++) {
                if (!seats[i][j]) {
                    System.out.print("  " + (i + 1) + (j + 1) + "  ");
                }
            }
        }

        System.out.println();


        System.out.println("we have " + counterBooked + " booked seats");
        System.out.println("booked seat numbers are: " );
        for (int i = 0; i < seats.length; i++) {
            for (int j = 0; j < seats[0].length; j++) {
                if (seats[i][j]) {
                    System.out.print("  " + (i + 1) + (j + 1) + "  ");
                }
            }
        }

        System.out.println();


        if( (double) counterBooked / (double) (seats.length * seats[0].length) > 0.80){
            System.out.println("Almost Full");
        }

    }


    static int getPositiveInt(String message) {
        int number;

        while (true) {
            System.out.print(message);

            if (!sc.hasNextInt()) {
                System.out.println("please enter a number");
                sc.next();
                continue;
            }

            number = sc.nextInt();

            if (number > 0) {
                return number;
            }

            System.out.println("Please enter a positive number");
        }
    }


    static int[] getSeat(boolean[][] seats) {

        int row;
        int column;

        while (true) {

            row = getPositiveInt("Enter row: ");
            column = getPositiveInt("Enter column: ");

            if (row > seats.length || column > seats[0].length) {
                System.out.println("Invalid seat number");
                continue;
            }

            return new int[] {row - 1, column - 1};
        }
    }


}
/*
* Mandatory Requirements:
* Display Seats (DONE)
* Book Seat (DONE)
* Cancel Booking (DONE)
* Show all movies (DONE)
* Show number of available and booked seats (DONE)
* Exit (DONE)
* validating row and seat numbers when booking (DONE)
* Display booked seats, available seats (DONE)
*
* Bonus Requirements:
* Take number of rows & columns (DONE)
* Take number of movies and their names (DONE)
* Display 'Almost Full' when occupancy exceeds 80% (DONE)
*
*
*
* */
