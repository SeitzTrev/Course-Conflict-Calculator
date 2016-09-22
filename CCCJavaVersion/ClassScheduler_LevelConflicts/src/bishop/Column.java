package bishop;


import java.util.ArrayList;

public class Column {
    private ArrayList<Course> courses;
    private String day;


    public Column(String day) {
        courses = new ArrayList<>();
        this.day = day;
    }


    public void AddColumnEntry(Course course) {
        courses.add(course);
    }

    public ArrayList<Course> GetColumnCourses() {
        return courses;
    }

    public String GetDay(){
        return day;
    }


    public boolean DoesCourseFitColumn(Course course) {
        for (Course c : courses) {
            if (!(c.GetStartTime() >= course.GetEndTime() || c.GetEndTime() <= course.GetStartTime())) {
                return false;
            }
        }
        return true;
    }
}
