package bishop;

/**
 * Store each record of two courses having conflicting times
 */
public class Conflict {

    private Course course1;
    private Course course2;
    private int courseLevel;
    private String department;
    private double startTime;
    private double endTime;

    public Conflict(Course course1, Course course2, int courseLevel, String department, double startTime, double endTime){
        this.course1 = course1;
        this.course2 = course2;
        this.courseLevel = courseLevel;
        this.department = department;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    //Compare two Conflicts for equality
    public boolean equals(Conflict conflict){
        if (startTime == conflict.startTime && endTime == conflict.endTime
                && ((course1.equals(conflict.course1) && course2.equals(conflict.course2))
                || (course1.equals(conflict.course2) && course2.equals(conflict.course1))))
            return true;
        return false;
    }

    public int getCourseLevel(){
        return courseLevel;
    }

    public String getDepartment() {
        return department;
    }

    public double getStartTime(){
        return startTime;
    }

    public double getEndTime(){
        return endTime;
    }

    public Course getCourse1(){
        return course1;
    }

    public Course getCourse2(){
        return course2;
    }

    public String toString(){
         return course1.toButtonString() + " \n\t&\n" + course2.toButtonString();
    }
}
