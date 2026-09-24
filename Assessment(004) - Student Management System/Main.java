import java.util.Scanner;

public class Main {

    static Scanner sc = new Scanner(System.in);



    static int numOfStudents;
    static int numOfSubjects;

    static String[] subjectNames;
    static Student[] students;




    public static void main(String[] args) {

        numOfStudents = getPositiveInt("Please enter the number of students: ");
        numOfSubjects = getPositiveInt("Please enter the number of subjects: ");

        subjectNames = getSubjectNames(numOfSubjects);
        students = getStudents(numOfStudents, subjectNames);

        sortStudentsByAverageGrade();


        boolean running  = true;
        while (running) {

            byte choice = menu();
            switch (choice) {
                case 1:
                    dashboard();
                    break;
                case 2:
                    displayStudents();
                    break;
                case 3:
                    displayAvgGradePerSubject();
                    break;
                case 4:
                    displayHighestGradePerSubject();
                    break;
                case 5:
                    searchStudentByID();
                    break;
                case 6:
                    System.out.println("Thanks for using our program!");
                    running = false;

                    break;

            }
        }

    }


    static byte menu(){
        byte choice = 0;
        do {
            System.out.println("________Menu Choices_______");
            System.out.println("1. Dashboard");
            System.out.println("2. Display Students");
            System.out.println("3. Calculate Average Grade");
            System.out.println("4. Find Highest Grade");
            System.out.println("5. Search Student by ID");
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



    static String[] getSubjectNames(int numOfSubjects){
        sc.nextLine();
        String[] subjectNames = new String[numOfSubjects];

        for (int i = 0; i < numOfSubjects; i++) {
            System.out.print("Enter name of subject " + (i + 1) + ": ");
            subjectNames[i] = sc.nextLine();
        }

        return subjectNames;
    }


    static Student[] getStudents(int numOfStudents , String[] subjectNames){

        Student[] students = new Student[numOfStudents];
        for (int i = 0; i < numOfStudents; i++) {

            System.out.println();

            System.out.println("Student " + (i + 1));

            System.out.print("Enter student ID: ");
            String id = sc.nextLine();

            System.out.print("Enter student name: ");
            String name = sc.nextLine();

            Subject[] subjects = new Subject[numOfSubjects];

            for (int j = 0; j < numOfSubjects; j++) {

                int grade;

                do {
                    String message  = "Enter " + subjectNames[j] + " grade: ";
                    grade = getPositiveInt(message);

                    if (grade > 100) {
                        System.out.println("Grade must be between 0 and 100.");
                    }

                } while (grade > 100);

                subjects[j] = new Subject(subjectNames[j], grade);
            }

            students[i] = new Student(id, name, subjects);
            if (i < numOfStudents - 1) {
                sc.nextLine(); // consume newline left by the last nextInt()
            }
        }

        return students;
    }


    static void dashboard() {

        System.out.println("\n================ DASHBOARD ================\n");

        System.out.printf("%-20s", "Name(ID)");

        for (String subject : subjectNames)
            System.out.printf("%-20s", subject);

        System.out.printf("%-15s%-15s%n", "Average", "Status");



        for (Student student : students) {

            System.out.printf("%-20s", student.name + "(" + student.id + ")");

            for (Subject subject : student.subjects) {
                System.out.printf("%-20s", subject.grade + "|" + subject.getStatusPerEachSubjectGrade());
            }

            System.out.printf("%-15f%-15s%n", student.getAvgGradePerStudent(), student.getStatusPerEachStudentAvgGrade());

        }



        System.out.println("\n----------------------------------------------");

        System.out.printf("%-20s", "");

        for (String subject : subjectNames) {System.out.printf("%-20s", subject);}

        System.out.println();



        System.out.printf("%-20s", "Average");

        for (int i = 0; i < subjectNames.length; i++) {

            double sum = 0;

            for (Student student : students) {sum += student.subjects[i].grade;}

            System.out.printf("%-20f", sum / students.length);

        }

        System.out.println();



        System.out.printf("%-20s", "Highest");

        for (int i = 0; i < subjectNames.length; i++) {

            int highest = students[0].subjects[i].grade;

            for (Student student : students) {

                if (student.subjects[i].grade > highest) {
                    highest = student.subjects[i].grade;
                }

            }

            System.out.printf("%-20d", highest);

        }

        System.out.println();



        int passed = 0;
        int failed = 0;

        for (Student student : students) {

            if (student.getAvgGradePerStudent() >= 60) {passed++;}
            else {failed++;}

        }

        System.out.println("\n----------------------------------------------");
        System.out.printf("%-20s", "");

        System.out.println();

        System.out.println("Passed Students: " + passed);
        System.out.println("Failed Students: " + failed);

    }

    static void displayStudents(){
        for (int i = 0 ; i < students.length ; i++){
            students[i].displayStudent();
        }
    }



    static void displayAvgGradePerSubject(){

        double sum = 0;
        sc.nextLine();

        System.out.print("Enter subject name: ");
        String subject = sc.nextLine();

        int index = findSubjectIndex(subject);

        if (index == -1){
            System.out.println("subject doesn't exist");
        return;
        }

        for (Student student : students) {
            sum += student.subjects[index].grade;
        }
        System.out.println("avg grade: " + (sum / students.length) );
    }


    static void displayHighestGradePerSubject() {
        sc.nextLine();

        System.out.print("Enter subject name: ");
        String subject = sc.nextLine();

        int index = findSubjectIndex(subject);

        if (index == -1) {
            System.out.println("Subject doesn't exist");
            return;
        }

        int max = students[0].subjects[index].grade;
        Student topStudent = students[0];

        for (Student student : students) {
            if (student.subjects[index].grade > max) {
                max = student.subjects[index].grade;
                topStudent = student;
            }
        }

        System.out.println("Highest grade: " + max);
        System.out.println("Student: " + topStudent.name + " (" + topStudent.id + ")");
    }
    static void searchStudentByID(){

        sc.nextLine(); //to consume the leftover newline


        System.out.print("Enter Student ID: ");
        String studentID = sc.nextLine();
        for (int i = 0 ; i < students.length ; i++){
            if(students[i].id.equals(studentID)){
                System.out.println("Student found!");
                students[i].displayStudent();
                return;
            }

        }

        System.out.println("Student not found!");

    }




//HELPER FUNCTIONS
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


    static int findSubjectIndex(String subjectName){
        for (int i = 0; i < subjectNames.length; i++) {
            if (subjectNames[i].equalsIgnoreCase(subjectName)) {
                return i;
            }
        }
        return -1;
    }


    static void sortStudentsByAverageGrade() {

        for (int i = 0; i < students.length - 1; i++) {

            for (int j = 0; j < students.length - 1 - i; j++) {

                if (students[j].getAvgGradePerStudent() < students[j + 1].getAvgGradePerStudent()) {

                    Student temp = students[j];
                    students[j] = students[j + 1];
                    students[j + 1] = temp;

                }
            }
        }
    }


}


/* MANDATORY REQUIREMENTS
* Display all students with their grade status (DONE)
* Search for a student using ID (DONE)
* Calculate the average grade (DONE PER STUDENT AND PER SUBJECT)
* Find the student with the highest grade.(DONE)
* Validate that grades are between 0 and 100 (DONE)
* If an ID is not found, display "Student not found." (DONE)
*
*
* BONUS REQUIREMENTS
* Count passed students and failed students (DONE IN DASHBOARD)
*  Sort students by grade. (DONE IN DASHBOARD AND DISPLAY STUDENTS)
*  Each student has two subjects instead of one. (DONE MADE IT USER INPUT)
*
*
* */
