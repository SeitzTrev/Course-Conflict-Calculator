package bishop;


import org.apache.poi.hssf.usermodel.examples.Alignment;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;


public class ScheduleMaker {
    /*
    private Segment monday[];
    private Segment tuesday[];
    private Segment wednesday[];
    private Segment thursday[];
    private Segment friday[];
    */

    private ArrayList<Column> days;

//    private Segment monday;
//    private Segment tuesday;
//    private Segment wednesday;
//    private Segment thursday;
//    private Segment friday;

    private XSSFCellStyle levelStyle[];
    private XSSFCellStyle headerStyle;

    private final String[] dayList = {"M", "T", "W", "R", "F"};


    public ScheduleMaker(ArrayList<ArrayList<Course>> coursesDays) {
        /*
        monday = new Segment[5];
        tuesday = new Segment[5];
        wednesday = new Segment[5];
        thursday = new Segment[5];
        friday = new Segment[5];
        */

        days = new ArrayList<>();

        CreateSegments(coursesDays);
    }


    public void CreateSegments(ArrayList<ArrayList<Course>> coursesDays) {
//        Collections.sort(courses);

//        // Compact View
//        monday = new Segment("Monday");
//        tuesday = new Segment("Tuesday");
//        wednesday = new Segment("Wednesday");
//        thursday = new Segment("Thursday");
//        friday = new Segment("Friday");
//
//        for (Course c : courses) {
//            if (c.IsOnMonday()) {
//                monday.AddCourse(c);
//            }
//            if (c.IsOnTuesday()) {
//                tuesday.AddCourse(c);
//            }
//            if (c.IsOnWednesday()) {
//                wednesday.AddCourse(c);
//            }
//            if (c.IsOnThursday()) {
//                thursday.AddCourse(c);
//            }
//            if (c.IsOnFriday()) {
//                friday.AddCourse(c);
//            }
//        }

        //Create all room segments and add all courses to each room
//        Column currentRoom = new Column("*****"); //tempColumn

        int dayIndex = 0;

        //Add all courses for each day to a separate Column
        for (ArrayList<Course> coursesDay: coursesDays) {
            Column currentDay = new Column(dayList[dayIndex]);


            for (Course course: coursesDay){
                currentDay.AddColumnEntry(course);
            }

            days.add(currentDay);
        }

        // Spaced out view
        /*
        for (int i = 0; i < 5; ++i) {
            int level = i + 1;

            monday[i] = new Segment("Monday");
            tuesday[i] = new Segment("Tuesday");
            wednesday[i] = new Segment("Wednesday");
            thursday[i] = new Segment("Thursday");
            friday[i] = new Segment("Friday");

            for (Course c : courses) {
                if (c.GetLevel() == level) {
                    if (c.IsOnMonday()) {
                        monday[i].AddCourse(c);
                    }
                    if (c.IsOnTuesday()) {
                        tuesday[i].AddCourse(c);
                    }
                    if (c.IsOnWednesday()) {
                        wednesday[i].AddCourse(c);
                    }
                    if (c.IsOnThursday()) {
                        thursday[i].AddCourse(c);
                    }
                    if (c.IsOnFriday()) {
                        friday[i].AddCourse(c);
                    }
                }
            }
        }
        */
    }


    public void SaveSchedule(String excelFilename) throws Exception {
        File excelFile = CopyTemplateToNewFile(excelFilename);
        InputStream inStream = new FileInputStream(excelFile);

        XSSFWorkbook workbook = new XSSFWorkbook(inStream);
        CreateStyles(workbook);

        XSSFSheet sheet = workbook.getSheetAt(0);

        int columnCount = 1;

        for (Column day: days){

            System.out.println("Column " + columnCount);
            ArrayList<Course> columnCourses = day.GetColumnCourses();
            for (Course course: columnCourses){
                System.out.println(course.ToString());
            }

            for (Course course: day.GetColumnCourses()){
                CreateCourseListing(workbook, sheet, course, columnCount);
            }

            Cell dayCell = sheet.getRow(0).createCell(columnCount);
            dayCell.setCellValue(day.GetDay());
            dayCell.setCellStyle(headerStyle);
            dayCell.setCellValue(day.GetDay());
//            CellRangeAddress roomRange = new CellRangeAddress(0, 0, roomColumn, roomColumn);
//            RegionUtil.setBorderTop(BorderFormatting.BORDER_THICK, roomRange, sheet, workbook);
//            RegionUtil.setBorderRight(BorderFormatting.BORDER_THIN, roomRange, sheet, workbook);
//            RegionUtil.setBorderLeft(BorderFormatting.BORDER_THIN, roomRange, sheet, workbook);
//            RegionUtil.setBorderBottom(BorderFormatting.BORDER_THICK, roomRange, sheet, workbook);
//            sheet.addMergedRegion(roomRange);
//            roomColumn = columnCount;
            columnCount++;
        }

//        // Monday
//        CellRangeAddress monLeftRange = new CellRangeAddress(1, 180, dayStartColumn, dayStartColumn);
//        RegionUtil.setBorderLeft(BorderFormatting.BORDER_THICK, monLeftRange, sheet, workbook);
//        //for (Segment segment : monday) {
//            for (Column column : monday.GetColumns()) {
//                for (Course course : column.GetColumnCourses()) {
//                    CreateCourseListing(workbook, sheet, course, columnCount);
//                }
//                ++columnCount;
//            }
//        //}
//        Cell mon = sheet.getRow(0).createCell(dayStartColumn);
//        mon.setCellStyle(headerStyle);
//        mon.setCellValue("Monday");
//        CellRangeAddress monRange = new CellRangeAddress(0, 0, dayStartColumn, columnCount - 1);
//        RegionUtil.setBorderTop(BorderFormatting.BORDER_THICK, monRange, sheet, workbook);
//        RegionUtil.setBorderRight(BorderFormatting.BORDER_THIN, monRange, sheet, workbook);
//        RegionUtil.setBorderLeft(BorderFormatting.BORDER_THIN, monRange, sheet, workbook);
//        RegionUtil.setBorderBottom(BorderFormatting.BORDER_THICK, monRange, sheet, workbook);
//        sheet.addMergedRegion(monRange);
//        dayStartColumn = columnCount;
//
//        // Tuesday
//        CellRangeAddress tueLeftRange = new CellRangeAddress(1, 180, dayStartColumn, dayStartColumn);
//        RegionUtil.setBorderLeft(BorderFormatting.BORDER_THIN, tueLeftRange, sheet, workbook);
//        //for (Segment segment : tuesday) {
//            for (Column column : tuesday.GetColumns()) {
//                for (Course course : column.GetColumnCourses()) {
//                    CreateCourseListing(workbook, sheet, course, columnCount);
//                }
//                ++columnCount;
//            }
//        //}
//        Cell tue = sheet.getRow(0).createCell(dayStartColumn);
//        tue.setCellStyle(headerStyle);
//        tue.setCellValue("Tuesday");
//        CellRangeAddress tueRange = new CellRangeAddress(0, 0, dayStartColumn, columnCount - 1);
//        RegionUtil.setBorderTop(BorderFormatting.BORDER_THICK, tueRange, sheet, workbook);
//        RegionUtil.setBorderRight(BorderFormatting.BORDER_THIN, tueRange, sheet, workbook);
//        RegionUtil.setBorderLeft(BorderFormatting.BORDER_THIN, tueRange, sheet, workbook);
//        RegionUtil.setBorderBottom(BorderFormatting.BORDER_THICK, tueRange, sheet, workbook);
//        sheet.addMergedRegion(tueRange);
//        dayStartColumn = columnCount;
//
//        // Wednesday
//        CellRangeAddress wedLeftRange = new CellRangeAddress(1, 180, dayStartColumn, dayStartColumn);
//        RegionUtil.setBorderLeft(BorderFormatting.BORDER_THIN, wedLeftRange, sheet, workbook);
//        //for (Segment segment : wednesday) {
//            for (Column column : wednesday.GetColumns()) {
//                for (Course course : column.GetColumnCourses()) {
//                    CreateCourseListing(workbook, sheet, course, columnCount);
//                }
//                ++columnCount;
//            }
//        //}
//        Cell wed = sheet.getRow(0).createCell(dayStartColumn);
//        wed.setCellStyle(headerStyle);
//        wed.setCellValue("Wednesday");
//        CellRangeAddress wedRange = new CellRangeAddress(0, 0, dayStartColumn, columnCount - 1);
//        RegionUtil.setBorderTop(BorderFormatting.BORDER_THICK, wedRange, sheet, workbook);
//        RegionUtil.setBorderRight(BorderFormatting.BORDER_THIN, wedRange, sheet, workbook);
//        RegionUtil.setBorderLeft(BorderFormatting.BORDER_THIN, wedRange, sheet, workbook);
//        RegionUtil.setBorderBottom(BorderFormatting.BORDER_THICK, wedRange, sheet, workbook);
//        sheet.addMergedRegion(wedRange);
//        dayStartColumn = columnCount;
//
//        // Thursday
//        CellRangeAddress thuLeftRange = new CellRangeAddress(1, 180, dayStartColumn, dayStartColumn);
//        RegionUtil.setBorderLeft(BorderFormatting.BORDER_THIN, thuLeftRange, sheet, workbook);
//        //for (Segment segment : thursday) {
//            for (Column column : thursday.GetColumns()) {
//                for (Course course : column.GetColumnCourses()) {
//                    CreateCourseListing(workbook, sheet, course, columnCount);
//                }
//                ++columnCount;
//            }
//        //}
//        Cell thu = sheet.getRow(0).createCell(dayStartColumn);
//        thu.setCellStyle(headerStyle);
//        thu.setCellValue("Thursday");
//        CellRangeAddress thuRange = new CellRangeAddress(0, 0, dayStartColumn, columnCount - 1);
//        RegionUtil.setBorderTop(BorderFormatting.BORDER_THICK, thuRange, sheet, workbook);
//        RegionUtil.setBorderRight(BorderFormatting.BORDER_THIN, thuRange, sheet, workbook);
//        RegionUtil.setBorderLeft(BorderFormatting.BORDER_THIN, thuRange, sheet, workbook);
//        RegionUtil.setBorderBottom(BorderFormatting.BORDER_THICK, thuRange, sheet, workbook);
//        sheet.addMergedRegion(thuRange);
//        dayStartColumn = columnCount;
//
//        // Friday
//        CellRangeAddress friLeftRange = new CellRangeAddress(1, 180, dayStartColumn, dayStartColumn);
//        RegionUtil.setBorderLeft(BorderFormatting.BORDER_THIN, friLeftRange, sheet, workbook);
//        //for (Segment segment : friday) {
//            for (Column column : friday.GetColumns()) {
//                for (Course course : column.GetColumnCourses()) {
//                    CreateCourseListing(workbook, sheet, course, columnCount);
//                }
//                ++columnCount;
//            }
//        //}
//        Cell fri = sheet.getRow(0).createCell(dayStartColumn);
//        fri.setCellStyle(headerStyle);
//        fri.setCellValue("Friday");
//        CellRangeAddress friRange = new CellRangeAddress(0, 0, dayStartColumn, columnCount - 1);
//        RegionUtil.setBorderTop(BorderFormatting.BORDER_THICK, friRange, sheet, workbook);
//        RegionUtil.setBorderRight(BorderFormatting.BORDER_THIN, friRange, sheet, workbook);
//        RegionUtil.setBorderLeft(BorderFormatting.BORDER_THIN, friRange, sheet, workbook);
//        RegionUtil.setBorderBottom(BorderFormatting.BORDER_THICK, friRange, sheet, workbook);
//        sheet.addMergedRegion(friRange);

        FileOutputStream outFile = new FileOutputStream(excelFile);
        workbook.write(outFile);

        workbook.close();
    }


    private void CreateCourseListing(XSSFWorkbook workbook, XSSFSheet sheet, Course c, int column) {
        int startRowNum = (int)Math.round(c.GetStartTime() * 12 - 96) + 1;
        int endRowNum = (int)Math.round(c.GetEndTime() * 12 - 96);

        Cell cell = sheet.getRow(startRowNum).createCell(column);
        cell.setCellValue(c.GetName() + "\n" + c.GetRoom() + "\n" + c.GetInstructor() + "\n" + c.GetDays() + "\n" + c.GetTime());

        CellRangeAddress rangeAddress = new CellRangeAddress(startRowNum, endRowNum, column, column);
        RegionUtil.setBorderTop(BorderFormatting.BORDER_THIN, rangeAddress, sheet, workbook);
        RegionUtil.setBorderBottom(BorderFormatting.BORDER_THIN, rangeAddress, sheet, workbook);
        RegionUtil.setBorderLeft(BorderFormatting.BORDER_THIN, rangeAddress, sheet, workbook);
        RegionUtil.setBorderRight(BorderFormatting.BORDER_THIN, rangeAddress, sheet, workbook);

        cell.setCellStyle(/*levelStyle[c.GetLevel() - 1]*/ levelStyle[0]);

        sheet.addMergedRegion(rangeAddress);
    }


    public void CreateStyles(XSSFWorkbook workbook) {
        XSSFFont font = workbook.createFont();
        font.setFontName("Calibri");
        font.setFontHeightInPoints((short)8);

        CellStyle baseStyle = workbook.createCellStyle();

        baseStyle.setBorderTop(CellStyle.BORDER_THIN);
        baseStyle.setBorderRight(CellStyle.BORDER_THIN);
        baseStyle.setBorderLeft(CellStyle.BORDER_THIN);
        baseStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        baseStyle.setWrapText(true);
        baseStyle.setVerticalAlignment(CellStyle.ALIGN_CENTER);
        baseStyle.setFont(font);

        levelStyle = new XSSFCellStyle[5];
        levelStyle[0] = workbook.createCellStyle();
        levelStyle[0].cloneStyleFrom(baseStyle);
        levelStyle[0].setFillForegroundColor(new XSSFColor(new java.awt.Color(0, 176, 240)));

        levelStyle[1] = workbook.createCellStyle();
        levelStyle[1].cloneStyleFrom(baseStyle);
        levelStyle[1].setFillForegroundColor(new XSSFColor(new java.awt.Color(146, 208, 80)));

        levelStyle[2] = workbook.createCellStyle();
        levelStyle[2].cloneStyleFrom(baseStyle);
        levelStyle[2].setFillForegroundColor(new XSSFColor(new java.awt.Color(255, 255, 0)));

        levelStyle[3] = workbook.createCellStyle();
        levelStyle[3].cloneStyleFrom(baseStyle);
        levelStyle[3].setFillForegroundColor(new XSSFColor(new java.awt.Color(255, 192, 0)));

        levelStyle[4] = workbook.createCellStyle();
        levelStyle[4].cloneStyleFrom(baseStyle);
        levelStyle[4].setFillForegroundColor(new XSSFColor(new java.awt.Color(255, 64, 64)));

        XSSFFont boldFont = workbook.createFont();
        boldFont.setBold(true);
        headerStyle = workbook.createCellStyle();
        headerStyle.setFont(boldFont);
    }


    private File CopyTemplateToNewFile(String excelFilename) throws Exception {
        File file = new File(excelFilename);
        FileInputStream fis = new FileInputStream("template.xlsx");
        FileOutputStream fos = new FileOutputStream(file);
        byte[] buffer = new byte[1024];
        int noOfBytes = 0;

        while ((noOfBytes = fis.read(buffer)) != -1) {
            fos.write(buffer, 0, noOfBytes);
        }

        fis.close();
        fos.close();

        return file;
    }
}
