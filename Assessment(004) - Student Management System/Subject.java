//class subject represents a single subject that only knows its name and grade
public class Subject {
    String name;
    int grade;

    Subject(String name, int grade) {
        this.name = name;
        this.grade = grade;
    }


    String getStatusPerEachSubjectGrade(){
        if (grade >= 90 ) {return("Excellent");}
        else if (grade >= 75 ) {return ("Very Good");}
        else if( grade >= 60)  {return ("Pass");}
        else{ return ("Fail");}

    }
}
