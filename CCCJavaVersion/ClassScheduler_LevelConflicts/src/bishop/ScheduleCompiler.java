package bishop;

/*
Given a folder of excel files, clean and compile all files into one mail file of classes
 */
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.util.ArrayList;
import java.io.File;
import java.util.Collections;

public class ScheduleCompiler {
    ArrayList<Course> courses;


    public ScheduleCompiler(File scheduleFile) throws IOException {
        courses = new ArrayList<>();


        Workbook workbook = new XSSFWorkbook(scheduleFile.getPath());
        Sheet sheet = workbook.getSheetAt(0);

        //First row is always column names, ignore
        int rowStart = 1;
        int rowEnd = sheet.getLastRowNum();
        for (int rowNum = rowStart; rowNum <= rowEnd; ++rowNum) {
            Row row = sheet.getRow(rowNum);
            if (row == null) {
                continue;
            }

            //Only read rows with class listings, exclude extra info

            //Read name
            if (row.getCell(1) != null) {
                String name = row.getCell(1).getStringCellValue();
                if (name.matches(".*\\d.*")) {   //Check if name contains a number, otherwise not a course listing
                    //Read type
                    String type;
                    if (row.getCell(11) != null)
                        type = row.getCell(11).getStringCellValue();
                    else
                        type = "NA";

                    String days;
                    if (row.getCell(12) != null) {
                        days = row.getCell(12).getStringCellValue();
                    } else
                        days = "NA";

                    String time;
                    Cell cell13 = row.getCell(13);
                    Cell cell14 = row.getCell(14);
                    if (row.getCell(13) != null && row.getCell(14) != null && !row.getCell(13).getStringCellValue().isEmpty() && !row.getCell(14).getStringCellValue().isEmpty())
                        time = row.getCell(13).getStringCellValue() + "-" + row.getCell(14).getStringCellValue();
                    else
                        time = "NA";


                    //Read building and room
                    String building;
                    String room;
                    if (row.getCell(15) != null && !row.getCell(15).getStringCellValue().equals(" ")) {
                        String buildingRoom[] = row.getCell(15).getStringCellValue().split(" ");
                        if (buildingRoom.length > 1) {
                            building = buildingRoom[0];
                            room = buildingRoom[1];
                        } else {
                            building = row.getCell(15).getStringCellValue();
                            room = "NA";
                        }
                    } else {
                        building = "NA";
                        room = "NA";
                    }

                    String instructor;
                    if (row.getCell(16) != null)
                        instructor = row.getCell(16).getStringCellValue();
                    else
                        instructor = "NA";

                    String crossListCode;
                    if (row.getCell(3) != null) {
                        crossListCode = row.getCell(3).getStringCellValue();
                    } else
                        crossListCode = "NA";


                    courses.add(new Course(name, instructor, building, room, type, time, days, crossListCode));
                }
            }
        }

        //Find all cross listings and add them to corresponding courses
        if (courses.size() != 0){
            for (Course course1: courses){
                for (Course course2 : courses){
                    if (course1 != course2 &&
                            !(course1.GetCrossListCode().equals("NA")) &&
                            !(course1.GetCrossListCode().isEmpty()) &&
                            course1.GetCrossListCode().equals(course2.GetCrossListCode())){

                        System.out.println("Crosslisted courses " + course1.toButtonString() + " code: " +
                                        course1.GetCrossListCode() + " and " + course2.toButtonString() + " code: " + course2.GetCrossListCode());
                        course1.addCrossListing(course2);
                    }
                }
            }
        }

    }

    public ArrayList<Course> GetCourses() {
        return courses;
    }

    //Create all TimeSegments and find conflicts of courses within each TimeSegment
    public ArrayList<TimeSegment> CreateTimeSegments(ArrayList<Course> courses){
        ArrayList<TimeSegment> timeSegments = new ArrayList<>();
        ArrayList<Conflict> accumulativeConflicts = new ArrayList<>();
        double time = 8;

        //Make 1.5 hour TimeSegments and find conflicts, then add each TimeSegment to the ArrayList
        while (time < 23){
            TimeSegment timeSegment = new TimeSegment(time, time + .5);
            timeSegment.findConflictsAndCourses(courses, accumulativeConflicts);
            timeSegments.add(timeSegment);
            time += .5;
        }

        System.out.println("Segment count: " + timeSegments.size());
        return timeSegments;
    }

    //The following methods are used to slim down the class list into desired course type, course building, and course days
    public static ArrayList<Course> GetTypeCourses(ArrayList<Course> courses, String type) {
        ArrayList<Course> coursesType = new ArrayList<>();
        for (Course course: courses){
            if (course.GetType().equalsIgnoreCase(type))
                coursesType.add(course);
        }
        return coursesType;
    }

    public static ArrayList<Course> GetDepartmentCourses(ArrayList<Course> courses, String department){
        ArrayList<Course> coursesDepartment = new ArrayList<>();
        for (Course course: courses){
            if (course.GetType().equalsIgnoreCase(department))
                coursesDepartment.add(course);
        }
        return coursesDepartment;
    }

    public static ArrayList<Course> GetLevelCourses(ArrayList<Course> courses, int level) {
        ArrayList<Course> coursesLevel = new ArrayList<>();
        for (Course course : courses) {
            if (course.GetLevel() == level)
                coursesLevel.add(course);
        }
        return coursesLevel;
    }

    public static ArrayList<Course> GetBuildingCourses(ArrayList<Course> courses, String building) {
        ArrayList<Course> coursesBuilding = new ArrayList<>();
        for (Course course: courses){
            if (course.GetBuilding().equalsIgnoreCase(building)) {
                coursesBuilding.add(course);
            }
        }
        Collections.sort(coursesBuilding);
        return coursesBuilding;
    }

    public static ArrayList<Course> GetDayCourses(ArrayList<Course> courses, String days){
        ArrayList<Course> coursesDay = new ArrayList<>();
        for (Course course: courses){
            if (course.GetDays().contains(days))
                coursesDay.add(course);
        }
        return coursesDay;
    }

    public static ArrayList<Course> GetExactDayCourses(ArrayList<Course> courses, String days){
        ArrayList<Course> coursesDay = new ArrayList<>();
        for (Course course: courses){
            if (course.GetDays().equalsIgnoreCase(days))
                coursesDay.add(course);
        }
        return coursesDay;
    }

    public static ArrayList<Course> RemoveDuplicates(ArrayList<Course> courses){
        //There are some duplicates in the schedule listings, as well as several courses that have different CRN but are the same course
        //at the same location and same time... very inconsistent scheduling rules on IPFW's part. Does not handle cross listed classes, use method below
        for (int i = 0; i < courses.size(); i++){
            for (int j = 0; j < courses.size(); j++){
                Course course1 = courses.get(i);
                Course course2 = courses.get(j);
                if (i != j &&
                        (course1.GetRoom() + course1.GetTime() + course1.GetDays() + course1.GetLevel() + course1.GetDepartment()).equalsIgnoreCase(
                        (course2.GetRoom() + course2.GetTime() + course2.GetDays()+ course2.GetLevel() + course2.GetDepartment()))) {
                    System.out.println("Removed course: " + courses.get(j).ToString() + ",\nmatched: " + courses.get(i).ToString() + "\n");
                    courses.remove(j);
                    j--;
                    if (courses.size() - 1 <= i){
                        i--;
                    }
                }
            }
        }

        return courses;
    }

    //Removes cross listed courses
    public static ArrayList<Course> RemoveCrossListings(ArrayList<Course> courses){
        for (int i = 0; i < courses.size(); i++){
            for (int j = 0; j < courses.size(); j++){
                Course course1 = courses.get(i);
                Course course2 = courses.get(j);
                if (i != j && (course1.GetRoom() + course1.GetTime() + course1.GetDays() + course1.GetLevel()).equalsIgnoreCase(
                        (course2.GetRoom() + course2.GetTime() + course2.GetDays()+ course2.GetLevel()))
                        && !course1.GetDepartment().equals(course2.GetDepartment())) {
                    System.out.println("Removed course: " + courses.get(j).ToString() + ",\ncrosslisted with: " + courses.get(i).ToString() + "\n");
                    courses.remove(j);
                    j--;
                    if (courses.size() - 1 <= i){
                        i--;
                    }
                }
            }
        }

        return courses;
    }

    //Remove courses with no start or end time
    public static ArrayList<Course> RemoveTimeless(ArrayList<Course> courses) {
        for (int i = 0; i < courses.size(); i++) {
            Course course = courses.get(i);
            if (course.GetStartTime() == 0.0 && course.GetEndTime() == 0.0) {
                System.out.println("Removed Course: " + course.toString() + ", because timeless");
                courses.remove(i);
                i--;
            }
        }
        return courses;
    }

    //Remove courses that aren't of type LEC or LAB
    public static ArrayList<Course> RemoveAllTypesButLECLAB(ArrayList<Course> courses){
        for (Course course: courses){
            if (!course.GetType().equals("LEC") && !course.GetType().equals("LAB")){
                System.out.println("Removed course: " + course.toString() + " because type is " + course.GetType());
                courses.remove(course);
            }
        }

        return courses;
    }

//    public static boolean isCrossListed(){
//
//    }
}
