//this class represents a single student that knows his name,id,grades only but not other students
public class Student {
    String id;
    String name;
    Subject[] subjects;


    Student(String id, String name, Subject[] subjects) {
        this.id = id;
        this.name = name;
        this.subjects = subjects;
    }


    double getAvgGradePerStudent() {
        double sum = 0;
        for (Subject s : subjects) {
            sum += s.grade;
        }

        return sum / subjects.length;
    }


    String getStatusPerEachStudentAvgGrade(){
        double avg = getAvgGradePerStudent();

        if (avg >= 90 ) {return("Excellent");}
        else if (avg >= 75 ) {return ("Very Good");}
        else if(avg >= 60)  {return ("Pass");}
        else{ return ("Fail");}



    }
    void displayStudent(){
        System.out.println("----------------------------");

        System.out.println("Student Name: " + name);
        System.out.println("Student ID: " + id);

        for(int i = 0; i < subjects.length; i++){
            System.out.println(subjects[i].name + ": " + subjects[i].grade + " | " + subjects[i].getStatusPerEachSubjectGrade() );
        }

        System.out.println("avg of this student: " + getAvgGradePerStudent() + " | " + getStatusPerEachStudentAvgGrade());


    }

}
