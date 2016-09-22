package bishop;


import java.util.ArrayList;


//Contains all information regarding a single course

public class Course implements Comparable {
    private String courseName;
    private String department;
    private String instructor;
    private String building;
    private String room;
    private String type;
    private String time;
    private double startTime;
    private double endTime;
    private String courseDays;
    private int courseLevel;
    private String crossListCode;
    private ArrayList<Course> crossListedCourses;

    public Course(String courseName, String instructor, String building, String room, String type,
                  String time, String courseDays, String crossListCode) {
        this.courseName = courseName;

        String[] departmentNumber = courseName.split("-");
        this.department = departmentNumber[0];

        String level = departmentNumber[1];
        this.courseLevel = Character.getNumericValue(level.charAt(0));

        this.instructor = instructor;
        this.building = building;
        this.room = room;
        this.type = type;
        this.time = time;
        this.courseDays = courseDays;

        // Parse start end times into doubles and convert to 24 hour clock
        if (time.contains("TBA") || type.contains("DIS") || type.contains("HSC") || type.contains("SILEC")
                || type.contains("LECLEC") || type.contains("SD") || time.contains("NA") || time.isEmpty()) {
            this.time = "NA";
            this.startTime = 0;
            this.endTime = 0;
        }
        else {
            String timeParts[] = time.split("-");
            String startTimeStr[] = timeParts[0].split(":");
            startTime = Double.parseDouble(startTimeStr[0]);
            startTime += Double.parseDouble(startTimeStr[1].substring(0, startTimeStr[1].length() - 2)) / 60.0d;
            String startPeriod = startTimeStr[1].substring(startTimeStr[1].length() - 2);

            if (startPeriod.equals("PM"))
                if (startTime < 12)
                    startTime += 12.0d;

            String endTimeStr[] = timeParts[1].split(":");
            endTime = Double.parseDouble(endTimeStr[0]);
            endTime += Double.parseDouble(endTimeStr[1].substring(0, endTimeStr[1].length() - 2)) / 60.0d;
            String endPeriod = endTimeStr[1].substring(endTimeStr[1].length() - 2);

            if (endPeriod.equals("PM"))
                if (endTime < 12)
                    endTime += 12.0d;

            if (startTime < 8 || endTime < 8)
                time = "NA";
        }

        this.crossListCode = crossListCode;
        crossListedCourses = new ArrayList<>();
    }


    public String GetName() {
        return courseName;
    }

    public String GetInstructor() {
        return instructor;
    }

    public String GetDepartment() {
        return department;
    }

    public String GetBuilding() {
        return building;
    }

    public String GetRoom() {
        return room;
    }

    public int GetRoomAsInt(){
        //Bypass the possible room number formats that have characters as well as "NA" room numbers
        if (room.equalsIgnoreCase("NA")){
            return -1;
        }
        else if (room.charAt(0) == 'G' || room.charAt(0) == 'B'){
            return Integer.parseInt(room.substring(1));
        }
        else if (room.charAt(room.length() - 1) == 'A'|| room.charAt(room.length() - 1) == 'B' || room.charAt(room.length() - 1) == 'C'
                || room.charAt(room.length() - 1) == 'N'){
            return Integer.parseInt(room.substring(0, room.length() - 1));
        }

        return Integer.parseInt(room);
    }

    public void addCrossListing(Course crossListedCourse){
        crossListedCourses.add(crossListedCourse);
    }

    public ArrayList<Course> GetCrossListedCourses (){
        return crossListedCourses;
    }

    public String GetType() {
        return type;
    }

    public double GetStartTime() {
        return startTime;
    }


    public double GetEndTime() {
        return endTime;
    }


    public int GetLevel() {
        return courseLevel;
    }


    public String GetTime() {
        return time;
    }

    public String GetTimeToDisplay (){
        return "";
    }

    public String GetDays() {
        return courseDays;
    }

    public String GetCrossListCode(){
        return crossListCode;
    }

    public boolean IsOnMonday() {
        return courseDays.contains("M");
    }


    public boolean IsOnTuesday() {
        return courseDays.contains("T");
    }


    public boolean IsOnWednesday() {
        return courseDays.contains("W");
    }


    public boolean IsOnThursday() {
        return courseDays.contains("R");
    }


    public boolean IsOnFriday() {
        return courseDays.contains("F");
    }


    public String ToString() {
        return "Name: " + courseName + ", Department: " + department + ", Level: " + courseLevel + ", Type: " + type
                + ", Building: " + building + ", Room: " + room + ", " + instructor + ", Start: "
                + startTime + ", End: " + endTime + ", Days:" + courseDays;
    }

    public String toString() {
        return "Name: " + courseName + ", Department: " + department + ", Level: " + courseLevel + ", Type: " + type
                + ", Building: " + building + ", Room: " + room + ", " + instructor + ", Start: "
                + startTime + ", End: " + endTime + ", Days:" + courseDays;
    }

    public String toButtonString(){
        String string = courseName;

        for (Course course: crossListedCourses){
            string += " / " + course.GetName();
        }

        string += " " + time;

        return string;
    }

    public static String BuildingName(String buildingInitials){
        switch (buildingInitials) {
            case "KT":
                return "Kettler Hall";
            case "NF":
                return "Neff Hall";
            case "SB":
                return "Science Building";
            case "ET":
                return "Engineering/Technology Building";
            case "LA":
                return "Liberal Arts Building";
            case "RC":
                return "Rhinehart Music Center";
            default:
                return "INVALID NAME";

            //Taken from https://www.ipfw.edu/offices/registrar/registration/abbreviations.html
        }
    }


//    @Override
//    public int compareTo(Object obj) {
//        if (!(obj instanceof Course)) {
//            return 0;
//        }
//
//        Course compCourse = (Course)obj;
//
//        int levelDiff = courseLevel - compCourse.GetLevel();
//        if (levelDiff != 0) {
//            return levelDiff;
//        }
//
//        int nameDiff = courseName.compareTo(compCourse.GetName());
//        return nameDiff;
//    }

    //Compare two courses for equality
    public boolean equals(Course course){
        if (this.courseName.equals(course.courseName) && this.instructor.equals(course.instructor) && this.building.equals(course.building)
                && this.room.equals(course.room) && this.startTime == course.startTime && this.endTime == course.endTime)
            return true;
        return false;
    }

    @Override
    public int compareTo(Object obj) {
        if (!(obj instanceof Course)) {
            return 0;
        }

        Course compCourse = (Course)obj;

        int roomDiff = GetRoomAsInt() - compCourse.GetRoomAsInt();
        return roomDiff ;
    }
}
