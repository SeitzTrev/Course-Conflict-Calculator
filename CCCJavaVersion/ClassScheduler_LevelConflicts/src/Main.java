import bishop.*;

import javax.swing.*;
import java.util.ArrayList;
import java.io.File;

public class Main {

    public static void main(String[] args) throws Exception {

        try {
            //Prompt user to select schedule file
            JFileChooser fileChooser = new JFileChooser("Schedule Reports/");
            fileChooser.showOpenDialog(new JPanel());

            //Create file, print out file name and year
            File scheduleFile = fileChooser.getSelectedFile();
            if (scheduleFile == null){
                System.out.println("Invalid or no file select. Exiting program.");
                System.exit(0);
            }
            System.out.println(scheduleFile.getName());
            String yearTerm = scheduleFile.getName();
            yearTerm = yearTerm.substring(yearTerm.indexOf('2'), yearTerm.indexOf('2') + 4);
            System.out.println(yearTerm);


            //Create ScheduleCompiler
            ScheduleCompiler scheduleCompiler = new ScheduleCompiler(scheduleFile);

            //Trim courses by removing duplicate courses and courses with no start or end time
            ArrayList<Course> courses = scheduleCompiler.GetCourses();
            System.out.println(courses.size());
            courses = scheduleCompiler.RemoveDuplicates(courses);
            courses = scheduleCompiler.RemoveTimeless(courses);
            courses = scheduleCompiler.RemoveAllTypesButLECLAB(courses);
            System.out.println(courses.size());

            //Get courses for each day and create TimeSegments for that day.
            ArrayList<Course> coursesM = scheduleCompiler.GetDayCourses(courses, "M");
            System.out.println("Monday classes:");
            for (Course course: coursesM)
                System.out.println(course.ToString());
            ArrayList<TimeSegment> timeSegmentsM = scheduleCompiler.CreateTimeSegments(coursesM);

            ArrayList<Course> coursesT = scheduleCompiler.GetDayCourses(courses, "T");
            System.out.println("Tuesday classes:");
            for (Course course: coursesT)
                System.out.println(course.ToString());
            ArrayList<TimeSegment> timeSegmentsT = scheduleCompiler.CreateTimeSegments(coursesT);


            ArrayList<Course> coursesW = scheduleCompiler.GetDayCourses(courses, "W");
            System.out.println("Wednesday classes:");
            for (Course course: coursesW)
                System.out.println(course.ToString());
            ArrayList<TimeSegment> timeSegmentsW = scheduleCompiler.CreateTimeSegments(coursesW);


            ArrayList<Course> coursesR = scheduleCompiler.GetDayCourses(courses, "R");
            System.out.println("Thursday classes:");
            for (Course course: coursesR)
                System.out.println(course.ToString());
            ArrayList<TimeSegment> timeSegmentsR = scheduleCompiler.CreateTimeSegments(coursesR);


            ArrayList<Course> coursesF = scheduleCompiler.GetDayCourses(courses, "F");
            System.out.println("Friday classes:");
            for (Course course: coursesF)
                System.out.println(course.ToString());
            ArrayList<TimeSegment> timeSegmentsF = scheduleCompiler.CreateTimeSegments(coursesF);

            System.out.println("\nAll courses:");
            for (Course course: courses)
                System.out.println(course.ToString());

            //Add all TimeSegments together to send to ScheduleGUI
            ArrayList<TimeSegment> timeSegments = new ArrayList<>();
            timeSegments.addAll(timeSegmentsM);
            timeSegments.addAll(timeSegmentsT);
            timeSegments.addAll(timeSegmentsW);
            timeSegments.addAll(timeSegmentsR);
            timeSegments.addAll(timeSegmentsF);

            for (TimeSegment timeSegment: timeSegments){
                System.out.println(timeSegment.toString());
            }

            new ScheduleGUI(timeSegments);

        } catch (Exception e){
            System.out.println("Exception: " + e.toString());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
