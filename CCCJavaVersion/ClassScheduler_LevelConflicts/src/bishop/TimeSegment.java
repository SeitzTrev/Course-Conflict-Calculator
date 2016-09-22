package bishop;

import java.lang.reflect.Array;
import java.util.ArrayList;

// A 1.5 hour portion of time that contains all conflicts primarily located in that time block.
// Used for graphical display of time conflicts

public class TimeSegment {
    private double startTime;
    private double endTime;

    private ArrayList<Conflict>[] conflictsLevel;
    private ArrayList<Course>[] nonConflictCoursesLevel;

    public TimeSegment(double startTime, double endTime) {
        this.startTime = startTime;
        this.endTime = endTime;

        conflictsLevel = new ArrayList[5];
        conflictsLevel[0] = new ArrayList<>();
        conflictsLevel[1] = new ArrayList<>();
        conflictsLevel[2] = new ArrayList<>();
        conflictsLevel[3] = new ArrayList<>();
        conflictsLevel[4] = new ArrayList<>();

        nonConflictCoursesLevel = new ArrayList[5];
        nonConflictCoursesLevel[0] = new ArrayList<>();
        nonConflictCoursesLevel[1] = new ArrayList<>();
        nonConflictCoursesLevel[2] = new ArrayList<>();
        nonConflictCoursesLevel[3] = new ArrayList<>();
        nonConflictCoursesLevel[4] = new ArrayList<>();
    }

    //Find all scheduling conflicts for courses that start in this TimeSegment's time boundaries.
    //Also determine if other classes are listed in the same TimeSegment
    public void findConflictsAndCourses(ArrayList<Course> courses, ArrayList<Conflict> accumulativeConflicts){
        for (Course course1: courses){

            //Add course to nonConflictCourses if any part of the course's time overlaps with this TimeSegment
            if ((course1.GetStartTime() < endTime && course1.GetStartTime() >= startTime) ||
                    (course1.GetEndTime() < endTime && course1.GetEndTime() >= startTime) ||
                    (course1.GetStartTime() <= startTime && course1.GetEndTime() >= endTime)){

                boolean courseIsCrossListed = false;

                //Add course to corresponding level of nonConflictCourses ArrayList, as long as course is not crosslisted
                for (Course crossListedCourse: course1.GetCrossListedCourses()) {
                    if (!courseIsCrossListed &&
                            nonConflictCoursesLevel[course1.GetLevel() - 1].contains(crossListedCourse)){
                        courseIsCrossListed = true;
                    }
                }

                if (!courseIsCrossListed){
                    nonConflictCoursesLevel[course1.GetLevel() - 1].add(course1);
                }
            }

            for (Course course2 : courses) {
                //Get later starting course to examine for conflicts
                Course laterStartingCourse = getLaterStartingCourse(course1, course2);

                //Check for later starting course being in the time boundaries
                double course1EndTime = course1.GetEndTime();
                double course1StartTime = course1.GetStartTime();
                double course2StartTime = course2.GetStartTime();
                double course2EndTime = course2.GetEndTime();
                if (((course1StartTime < endTime && course1StartTime >= startTime) ||
                        (course1EndTime < endTime && course1EndTime >= startTime) ||
                        (course1StartTime <= startTime && course1EndTime >= endTime))
                        && ((course2StartTime < endTime && course2StartTime >= startTime) ||
                        (course2EndTime < endTime && course2EndTime >= startTime) ||
                        (course2StartTime <= startTime && course2EndTime >= endTime))) {
                    //Check for courses having same course level and overlapping times
                    if (!course1.equals(course2) && course1.GetLevel() == course2.GetLevel()
                            && course1.GetDepartment().equals(course2.GetDepartment())
                            && ((course2.GetStartTime() >= course1.GetStartTime() && course2.GetStartTime() <= course1.GetEndTime())
                            || (course1.GetStartTime() >= course2.GetStartTime() && course1.GetStartTime() <= course2.GetEndTime()))) {

                        //Create conflict with earlier starting course listed first
                        Conflict conflict;
                        if (course1.GetStartTime() < course2.GetStartTime()) {
                            conflict = new Conflict(course1, course2, course1.GetLevel(), course1.GetDepartment(), startTime, endTime);
                        }
                        else {
                            conflict = new Conflict(course2, course1, course1.GetLevel(), course1.GetDepartment(), startTime, endTime);
                        }

                        //Check if the found conflict is a duplicate with swapped course orders
                        boolean duplicate = false;
                        for (Conflict checkConflict: accumulativeConflicts) {
                            if (conflict.equals(checkConflict))
                                duplicate = true;
                        }
                        if (!duplicate){
                            System.out.println("Found conflict:\n" + course1.ToString() + "\n&\n" + course2.ToString() + "\n");
                            conflictsLevel[course1.GetLevel() - 1].add(conflict);
                            accumulativeConflicts.add(conflict);
                        }
                    }
                }
            }
        }

        //Remove all conflicted courses from nonConflictCourses
        for (ArrayList<Conflict> conflictLevel: conflictsLevel){
            for (Conflict conflict: conflictLevel) {
                Course course1 = conflict.getCourse1();
                if (conflictLevel.contains(course1)) {
                    nonConflictCoursesLevel[course1.GetLevel() - 1].remove(course1);
                }
                //Handle cross listed courses as well
                for (Course course : conflict.getCourse1().GetCrossListedCourses()) {
                    if (nonConflictCoursesLevel[course1.GetLevel() - 1].contains(course)) {
                        nonConflictCoursesLevel[course1.GetLevel() - 1].remove(course1);
                    }
                }
            }
            //Same process for course2 as with course1
            for (Conflict conflict: conflictLevel) {
                Course course2 = conflict.getCourse2();
                if (conflictLevel.contains(course2)) {
                    nonConflictCoursesLevel[course2.GetLevel() - 1].remove(course2);
                }
                //Handle cross listed courses as well
                for (Course course : conflict.getCourse2().GetCrossListedCourses()) {
                    if (nonConflictCoursesLevel[course2.GetLevel() - 1].contains(course)) {
                        nonConflictCoursesLevel[course2.GetLevel() - 1].remove(course2);
                    }
                }
            }
        }
    }

    public ArrayList<Conflict> getConflicts(int courseLevel){
        return conflictsLevel[courseLevel - 1];
    }

    public ArrayList<Course> getNonConflictCourses(int courseLevel) {
        return nonConflictCoursesLevel[courseLevel - 1];
    }

    public String toString(){
        String toString = "Time Segment: " + startTime + " + " + endTime + "\n";

        for (ArrayList<Conflict> conflicts: conflictsLevel) {
            for (Conflict conflict : conflicts) {
                toString += conflict.toString() + "\n";
            }
        }

        for (ArrayList<Course> nonConflictCourses: nonConflictCoursesLevel) {
            for (Course course : nonConflictCourses) {
                toString += "non-conflict course: " + course.GetName() + " " + course.GetTime() + "\n";
            }
        }

        return toString;
    }

    private Course getLaterStartingCourse(Course course1, Course course2){
        if (course1.GetStartTime() <= course2.GetStartTime())
            return course2;
        return course1;
    }
}
