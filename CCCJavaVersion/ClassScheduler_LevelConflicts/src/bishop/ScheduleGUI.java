package bishop;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.basic.BasicBorders;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

/**
 * The graphical display of schedules using the conflict data from all TimeSegments
 */
public class ScheduleGUI extends JFrame{

    private JPanel[][] grid;
    private int[][][][] statusMap;
    private final int EMPTY = 0;
    private final int CLASSES = 1;
    private final int CONFLICTS = 2;
    private final int HIDE = 10;
    private final int SHOW = 11;

    public ScheduleGUI(ArrayList<TimeSegment> timeSegments){
        //For background color of buttons on Mac OS and universal platform usage
        try {
            UIManager.setLookAndFeel( UIManager.getCrossPlatformLookAndFeelClassName() );
        } catch (Exception e) {
            e.printStackTrace();
        }

        setSize(1200, 800);
        setLayout(new BorderLayout(5, 5));
        setBackground(Color.white);

        JPanel gridPanel = new JPanel(new GridLayout(16, 6));
        gridPanel.setMinimumSize(new Dimension(600,700));
        gridPanel.setBorder(new LineBorder(Color.black, 1));
        gridPanel.setBackground(Color.white);

        JPanel infoPanel = new JPanel(new GridLayout(2, 1));
        infoPanel.setMaximumSize(new Dimension(100, 700));
        infoPanel.setBackground(Color.white);

//        infoPanel.setMinimumSize(new Dimension(600, 600));
        JTextArea infoTextArea = new JTextArea();
        infoTextArea.setSize(400,400);
        infoTextArea.setFont(new Font("Arial", 1, 14));
        infoTextArea.setLineWrap(true);
        infoTextArea.setWrapStyleWord(true);

        grid = new JPanel[16][6];   //TimeSegments x 5 days + 1 row and column for labels

        //int array to show status of each conflictButton
        //  0: no classes, 1: classes but no conflict, 2: conflicts
        statusMap = new int[16][6][2][5];

        //Add all JPanels from grid to the gridPanel
        for (int i = 0; i < grid.length; i++){
            for (int j = 0; j < grid[0].length; j++){
                grid[i][j] = new JPanel();
                grid[i][j].setSize(new Dimension(100, 1));
                grid[i][j].setBorder(new LineBorder(Color.black, 1));
                grid[i][j].setBackground(Color.white);
                grid[i][j].setVisible(true);
                gridPanel.add(grid[i][j]);
            }
        }

        //Strings for day and time labels
        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
        String[] times = {"8:00AM", "9:00AM", "10:00AM", "11:00AM", "12:00PM", "1:00PM", "2:00PM",
                "3:00PM", "4:00PM", "5:00PM", "6:00PM", "7:00PM", "8:00PM", "9:00PM", "10:00PM", "11:00PM"};

        //Add all time labels
        for (int i = 1; i < grid.length; i++){
            JLabel timeLabel = new JLabel(times[i - 1]);
            grid[i][0].add(timeLabel);
        }

        //Add all day labels
        for (int j = 1; j < grid[0].length; j++){
            JLabel dayLabel = new JLabel(days[j - 1]);
            grid[0][j].add(dayLabel);
        }

        //File array for the button ImageIcons
        File conflictIconFiles[] = new File("ButtonImageIcons").listFiles();

        int segmentIndex = 0;

        for (int j = 1; j < grid[0].length; j++){
            for (int i = 1; i < grid.length; i++){

                grid[i][j].setLayout(new GridLayout(1, 5));

                //Add each JButton for each class level and set pattern or color depending on conflicts or
                // non conflict classes scheduled.
                for (int twoButtonsPerSegment = 0; twoButtonsPerSegment < 2; twoButtonsPerSegment++){

                    grid[i][j].setLayout(new GridLayout(2,1));

                    TimeSegment timeSegment = timeSegments.get(segmentIndex);

                    JPanel courseLevelPanel = new JPanel(new GridLayout(1,6));
                    courseLevelPanel.setBackground(Color.white);

                    for (int courseLevel = 1; courseLevel < 6; courseLevel++){

                        final String level = courseLevel + "00";

                        //Set grid panel layout based on number of conflicts in the TimeSegment
                        ArrayList<Conflict> conflicts = timeSegment.getConflicts(courseLevel);

                        //Create a button to show status of that course level in the TimeSegment
                        JButton conflictButton = new JButton();
                        conflictButton.setOpaque(true);
                        conflictButton.setForeground(Color.white);

                        //When there are conflicts
                        if (conflicts.size() != 0) {
                            //Set ConflictIcon and enable button
                            conflictButton.setIcon(new ImageIcon(new ImageIcon(conflictIconFiles[courseLevel - 1].getAbsolutePath())
                                    .getImage().getScaledInstance(50, 80, java.awt.Image.SCALE_SMOOTH)));
//                        conflictButton.setBorder(new LineBorder(Color.black, 1, true));

                            conflictButton.setEnabled(true);

                            statusMap[i][j][twoButtonsPerSegment][courseLevel - 1] = 2;

                            //Display conflict info upon clicking on the button
                            conflictButton.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    String conflictString = level + " Level Course Conflicts:\n\n";

                                    for (Conflict conflict : conflicts) {
                                        conflictString += conflict.toString() + "\n\n";
                                    }

                                    infoTextArea.setText(conflictString);
                                }
                            });
                        } else {

                            ArrayList<Course> nonConflictingCourses = timeSegment.getNonConflictCourses(courseLevel);

                            //When there are nonconflicting scheduled courses
                            if (nonConflictingCourses.size() != 0) {

                                //Enable button and set its plain color
                                conflictButton.setEnabled(true);

                                statusMap[i][j][twoButtonsPerSegment][courseLevel - 1] = 1;

                                //Change button background color based on conflict course level
                                switch (courseLevel) {
                                    case 1:
                                        conflictButton.setBackground(new Color(149, 213, 149));
                                        break;
                                    case 2:
                                        conflictButton.setBackground(new Color(0, 0, 130));
                                        break;
                                    case 3:
                                        conflictButton.setBackground(new Color(252, 185, 48));
                                        break;
                                    case 4:
                                        conflictButton.setBackground(new Color(229, 0, 0));
                                        break;
                                    case 5:
                                        conflictButton.setBackground(new Color(245, 246, 17));
                                        break;
//                              case 6:
//                                  conflictButton.setBackground(Color.cyan);
//                                  break;
                                }

                                //Add ActionListener to click on button and display conflict information
                                conflictButton.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        String conflictString = "Scheduled " + level + " Level Courses:\n\n";

                                        for (Course nonConflictingCourse : nonConflictingCourses) {
                                            conflictString += nonConflictingCourse.toButtonString() + "\n\n";
                                        }

                                        infoTextArea.setText(conflictString);
                                    }
                                });


                            } else {
                                //When there are no classes scheduled
                                conflictButton.setBackground(Color.white);
                                conflictButton.setEnabled(false);
                                conflictButton.setVisible(false);

                                statusMap[i][j][twoButtonsPerSegment][courseLevel - 1] = 0;
                            }
                        }
                        //Add button to course level panel
                        courseLevelPanel.add(conflictButton);
                    }
                    grid[i][j].add(courseLevelPanel);
                    segmentIndex++;
                }

            }
        }

        //Create radio button section for controlling display
        JPanel displayControlPanel = new JPanel(new GridLayout(1,3));
        displayControlPanel.setBackground(Color.white);
        displayControlPanel.setBorder(new TitledBorder("Display Controls"));

        JCheckBox emptyCheckBox = new JCheckBox("Empty", false);
        emptyCheckBox.setBackground(Color.white);

        emptyCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Alternate hiding or showing buttons
                if (!emptyCheckBox.isSelected()){
                    emptyCheckBox.setSelected(false);
                    hideOrShowButtons(EMPTY, HIDE);
                }
                else{
                    emptyCheckBox.setSelected(true);
                    hideOrShowButtons(EMPTY, SHOW);
                }
            }
        });

        JCheckBox nonConflictClassesCheckBox = new JCheckBox("Non Conflicts", true);
        nonConflictClassesCheckBox.setBackground(Color.white);

        nonConflictClassesCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Alternate hiding or showing buttons
                if (!nonConflictClassesCheckBox.isSelected()){
                    nonConflictClassesCheckBox.setSelected(false);
                    hideOrShowButtons(CLASSES, HIDE);
                }
                else{
                    nonConflictClassesCheckBox.setSelected(true);
                    hideOrShowButtons(CLASSES, SHOW);
                }
            }
        });

        JCheckBox conflictCheckBox = new JCheckBox("Conflicts", true);
        conflictCheckBox.setBackground(Color.white);

        conflictCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Alternate hiding or showing buttons
                if (!conflictCheckBox.isSelected()){
                    conflictCheckBox.setSelected(false);
                    hideOrShowButtons(CONFLICTS, HIDE);
                }
                else {
                    conflictCheckBox.setSelected(true);
                    hideOrShowButtons(CONFLICTS, SHOW);
                }
            }
        });

        displayControlPanel.add(emptyCheckBox);
        displayControlPanel.add(nonConflictClassesCheckBox);
        displayControlPanel.add(conflictCheckBox);

        //Add right side information section

        JLabel infoTitle = new JLabel("<html><U>Conflict Information</U></html>");
        infoTitle.setFont(new Font("Arial", Font.BOLD, 16));

        JPanel courseDisplayPanel = new JPanel(new BorderLayout());
        courseDisplayPanel.setSize(700, 400);
        courseDisplayPanel.add(infoTextArea, BorderLayout.CENTER);
        courseDisplayPanel.add(infoTitle, BorderLayout.NORTH);

        JPanel controlAndCourseDisplayPanel = new JPanel(new BorderLayout(5, 5));
        controlAndCourseDisplayPanel.add(courseDisplayPanel, BorderLayout.CENTER);
        controlAndCourseDisplayPanel.add(displayControlPanel, BorderLayout.NORTH);

        infoPanel.add(controlAndCourseDisplayPanel);
//        infoPanel.add(displayControlPanel);
//        infoPanel.add(courseDisplayPanel);

        JTextArea tutorialTextArea = new JTextArea();
        tutorialTextArea.setFont(new Font("Arial", 1, 13));
        tutorialTextArea.setLineWrap(true);
        tutorialTextArea.setWrapStyleWord(true);
        tutorialTextArea.setText("\nClick buttons for more conflict information.\n\n" +
                "Pattern Key:\nStriped - Course conflict\nSolid - Courses scheduled with no conflict\nBlank - No courses scheduled\n\n" +
                "Course Level Key:\n100 - Green, 200 - Blue, 300 - Orange,\n400 - Red, 500 - Yellow"); //, 600 - Cyan
        tutorialTextArea.setBorder(new TitledBorder("Instructions"));

        infoPanel.add(tutorialTextArea);

        //Add panels and display all components
        gridPanel.setVisible(true);
        infoPanel.setVisible(true);
        add(gridPanel, BorderLayout.CENTER);
        add(infoPanel, BorderLayout.EAST);

        //Add title
        JLabel titleLabel = new JLabel("<html><u>IPFW Scheduling Display</u></html>");
        titleLabel.setFont(new Font("Arial", 1, 20));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBackground(Color.white);
        add(titleLabel, BorderLayout.NORTH);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        //pack();
    }

    //Using the statusMap, hide or show buttons on the grid based on status
    private void hideOrShowButtons(int status, int hideOrShow){
        for (int i = 1; i < statusMap.length; i++){
            for (int j = 1; j < statusMap[0].length; j++){
                for (int k = 0; k < statusMap[0][0].length; k++){
                    for (int m = 0; m < statusMap[0][0][0].length; m++) {
                        if (statusMap[i][j][k][m] == status) {
                            JPanel gridPanel = (JPanel)grid[i][j].getComponent(k);
                            if (hideOrShow == HIDE) {
                                gridPanel.getComponent(m).setVisible(false);
                            } else {
                                gridPanel.getComponent(m).setVisible(true);
                            }
                        }
                    }
                }
            }
        }
    }
}
