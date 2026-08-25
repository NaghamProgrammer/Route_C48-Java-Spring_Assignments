import java.util.Scanner;

public class Main {

    static Scanner sc = new Scanner(System.in);

    static String[] studentNames;
    static int[][] studentGrades;


    public static void main(String[] args) {

        dataTaker();

        boolean running = true;
        while(running){
            int menuOption = menu();

            switch(menuOption){
                case 1:
                    printAllNames();
                    break;
                case 2:
                    printAllGradesInEachSubject();
                    break;
                case 3:
                    searchStudentByName();
                    break;
                case 4:
                    countPassedStudents();
                    break;
                case 5:
                    System.out.println("Thank you for using our program.");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option");

            }

        }
    }



    static public int menu(){
        System.out.println("1. Show All Students names.");
        System.out.println("2. Show all Students grades in each subject. ");
        System.out.println("3. Search Student by name.");
        System.out.println("4. Count Passed Students");
        System.out.println("5. Exit");
        System.out.print("please select an option: ");

        int menuOption = sc.nextInt();

        return menuOption;

    }


    static public void dataTaker(){

        //data like number of subjects is usually fixed in the program
        int numOfSubjects = 3;
        int numOfStudents = 0;

        //take number of students
        do {
            System.out.print("Please enter the number of students: ");
            numOfStudents = sc.nextInt();
            if(numOfStudents <= 0){System.out.println("Please enter a positive integer");}
        } while (numOfStudents <= 0);

        studentNames = new String[numOfStudents];
        studentGrades = new int[numOfStudents][numOfSubjects];



        //take each student name and his 3 subject grades
        for(int i = 0; i<numOfStudents ; i++){

            do {
                System.out.print("Enter Student name " + (i + 1) + ": ");
                studentNames[i] = sc.next();

                if (nameExists(studentNames[i], i)) {
                    System.out.println("This name already exists. Please enter another name.");
                }

            } while (nameExists(studentNames[i], i));



            for(int j = 0; j<numOfSubjects; j++){
                int grade;

                do{
                 System.out.print("Enter grade for Subject " + (j + 1) + ": ");
                 grade = sc.nextInt();
                 if(grade < 0 || grade > 100){System.out.println("Invalid grade");}
                }
                while(!gradeIsValid(grade));


                studentGrades[i][j] = grade;

            }
        }
    }


    static public void printAllNames(){
        for(int i = 0; i<studentNames.length; i++){
            System.out.println(studentNames[i]);
        }
    }


    static public void printAllGradesInEachSubject(){
        for (int i = 0; i < studentNames.length; i++) {

            System.out.print(studentNames[i] + ": ");

            for (int j = 0; j < studentGrades[0].length; j++) {
                System.out.print(studentGrades[i][j] + "||" +grader(studentGrades[i][j]) + "  ");
            }

            System.out.println();
        }


        avgPerSubject();
        System.out.println();
        highestGradePerSubject();
        System.out.println();

    }


    static public void searchStudentByName() {
        System.out.print("Enter the student name you want to search: ");
        String studentName = sc.next();

        boolean found = false;

        for (int i = 0; i < studentNames.length; i++) {

            if (studentNames[i].equalsIgnoreCase(studentName)) {

                found = true;

                System.out.println("Student: " + studentNames[i]);
                System.out.print("Grades: ");

                for (int j = 0; j < studentGrades[0].length; j++) {
                    System.out.print(studentGrades[i][j] + "|" + grader(studentGrades[i][j]) + " ");
                }

                System.out.println();
                break;
            }
        }

        if (!found) {
            System.out.println("Student not found.");
        }
    }


    static public void countPassedStudents(){
        int counter = 0;

        for (int i = 0; i < studentGrades.length; i++) {
            boolean passed = true;

            for (int j = 0; j < studentGrades[0].length; j++) {
                if (studentGrades[i][j] < 50) {
                    passed = false;
                    break;
                }
            }

            if (passed) { counter++; }
        }

        System.out.println("Passed students: " + counter);

    }


    static public void avgPerSubject(){
        //average of each subject column
        int dividor = studentNames.length;


        System.out.print("Avg of subject: ");
        for (int i = 0; i < studentGrades[0].length; i++) {
            int sum = 0;
            for(int j = 0; j<studentGrades.length; j++){
                sum += studentGrades[j][i];
            }
            System.out.print((double) sum / dividor + " ");
        }

    }


    static public void highestGradePerSubject(){
        //max of each subject column

        System.out.print("Top of subject: ");
        for (int i = 0; i < studentGrades[0].length; i++) {
            int max = 0;
            for(int j = 0; j<studentGrades.length; j++){
                if(studentGrades[j][i] > max){max = studentGrades[j][i] ;}
            }
            System.out.print(max + " ");
        }


    }


    static public char grader(int grade){
        if(grade >= 85 ){return 'A' ;}
        else if(grade >= 75 ){return 'B';}
        else if(grade >= 65){return 'C';}
        else if(grade >= 50){return 'D';}
        else{return 'F';}
    }


    //HELPER FUNCTIONS
    static public boolean gradeIsValid(int grade){
        return grade >= 0 && grade <= 100;
    }

    static public boolean nameExists(String name, int currentStudents) {
        for (int i = 0; i < currentStudents; i++) {
            if (studentNames[i].equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

}
