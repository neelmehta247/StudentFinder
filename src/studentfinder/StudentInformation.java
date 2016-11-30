/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package studentfinder;

import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.util.StringTokenizer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 *
 * @author neel
 */
public class StudentInformation extends javax.swing.JFrame {

    private static final long serialVersionUID = 1L;

    private final Student[] returnIfNeeded;
    private final CallFrom toReturnIfNeeded;
    private boolean mDataAvailable = false;
    private Student student;
    private ArrayList<TimeSlot> timeSlots = new ArrayList<>();

    /**
     * Creates new form StudentInformation
     *
     * @param student
     * @param returnIfNeeded
     * @param toReturnIfNeeded
     * @param point
     */
    @SuppressWarnings("fallthrough")
    public StudentInformation(Student student, Student[] returnIfNeeded, CallFrom toReturnIfNeeded, Point point) {
        initComponents();

        setLocation(point);

        this.student = student;

        studentName.setText(student.firstname.trim() + " " + student.lastname.trim());
        studentGender.setText(student.gen);
        teacherTOK.setText(student.TeacherTOK);

        if (student.SL.size() > 3) {
            int slSize = student.SL.size();
            switch (slSize) {
                case 5:
                    textHL1.setText("SL4:");
                    HL1.setText(student.SL.get(3));
                    teacherHL1.setText(student.TeacherSL.get(3));
                    textHL2.setText("SL5:");
                    HL2.setText(student.SL.get(4));
                    teacherHL2.setText(student.TeacherSL.get(4));
                    break;
                case 4:
                    HL1.setText(student.HL.get(0));
                    teacherHL1.setText(student.TeacherHL.get(0));
                    textHL2.setText("SL4:");
                    HL2.setText(student.SL.get(3));
                    teacherHL2.setText(student.TeacherSL.get(3));
            }
            textHL3.setText("");
            HL3.setText("");
            teacherHL3.setText("");
            jLabel28.setText("");
        } else {
            HL1.setText(student.HL.get(0));
            teacherHL1.setText(student.TeacherHL.get(0));
            HL2.setText(student.HL.get(1));
            teacherHL2.setText(student.TeacherHL.get(1));
            HL3.setText(student.HL.get(2));
            teacherHL3.setText(student.TeacherHL.get(2));
        }
        SL1.setText(student.SL.get(0));
        teacherSL1.setText(student.TeacherSL.get(0));
        SL2.setText(student.SL.get(1));
        teacherSL2.setText(student.TeacherSL.get(1));
        SL3.setText(student.SL.get(2));
        teacherSL3.setText(student.TeacherSL.get(2));

        Date today = new Date(System.currentTimeMillis());

        String hour = today.toString().substring(11, 13);
        String minute = today.toString().substring(14, 16);

        textHour.setText(hour);
        textMinute.setText(minute);

        switch (today.toString().substring(0, 3)) {
            case "Mon":
                daySelector.setSelectedIndex(0);
                break;
            case "Tue":
                daySelector.setSelectedIndex(1);
                break;
            case "Wed":
                daySelector.setSelectedIndex(2);
                break;
            case "Thu":
                daySelector.setSelectedIndex(3);
                break;
            case "Fri":
                daySelector.setSelectedIndex(4);
                break;
        }

        textHour.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                if (!textMinute.getText().isEmpty() && !textHour.getText().isEmpty()) {
                    updateLocationInformation();
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                if (!textMinute.getText().isEmpty() && !textHour.getText().isEmpty()) {
                    updateLocationInformation();
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                if (!textMinute.getText().isEmpty() && !textHour.getText().isEmpty()) {
                    updateLocationInformation();
                }
            }
        });

        textMinute.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                if (!textMinute.getText().isEmpty() && !textHour.getText().isEmpty()) {
                    updateLocationInformation();
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                if (!textMinute.getText().isEmpty() && !textHour.getText().isEmpty()) {
                    updateLocationInformation();
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                if (!textMinute.getText().isEmpty() && !textHour.getText().isEmpty()) {
                    updateLocationInformation();
                }
            }
        });

        updateLocationInformation();

        this.returnIfNeeded = returnIfNeeded;
        this.toReturnIfNeeded = toReturnIfNeeded;
    }

    private void updateLocationInformation() {
        if (!mDataAvailable) {
            getData();
        }
        try {
            String day = (String) daySelector.getSelectedItem();
            int hour = Integer.parseInt(textHour.getText().trim());
            int minute = Integer.parseInt(textMinute.getText().trim());

            if (hour < 4 && hour != 0) {
                hour += 12;
            }

            if (hour == 14 && minute > 15) {
                hour = 13;
                minute = 45;
            }

            TimeSlot currentTimeSlot = new TimeSlot();
            boolean timeFound = false;

            for (TimeSlot timeSlot : timeSlots) {
                if (day.toUpperCase().equals(timeSlot.day)) {
                    if (hour == timeSlot.times.get("startHour")) {
                        if (minute >= timeSlot.times.get("startMinute")) {
                            if (hour < timeSlot.times.get("endHour")) {
                                currentTimeSlot = timeSlot;
                                timeFound = true;
                                break;
                            } else {
                                if (minute < timeSlot.times.get("endMinute")) {
                                    currentTimeSlot = timeSlot;
                                    timeFound = true;
                                    break;
                                }
                            }
                        }
                    } else if (hour > timeSlot.times.get("startHour")) {
                        if (hour == timeSlot.times.get("endHour")) {
                            if (minute < timeSlot.times.get("endMinute")) {
                                currentTimeSlot = timeSlot;
                                timeFound = true;
                                break;
                            }
                        } else if (hour < timeSlot.times.get("endHour")) {
                            currentTimeSlot = timeSlot;
                            timeFound = true;
                            break;
                        }
                    }
                }
            }

            if (!timeFound) {
                displayText("their home", "their laptop", "nothing");
            } else {
                boolean classFound = false;

                for (int i = 2; i < ParseClasses.roomMaster.length && !classFound; i++) {
                    if (currentTimeSlot.classes.get(ParseClasses.roomMaster[i]).contains("YEAR 12")) {
                        StringTokenizer tokenizer = new StringTokenizer(currentTimeSlot.classes.get(ParseClasses.roomMaster[i]), "|", false);
                        String subject = tokenizer.nextToken();
                        subject = tokenizer.nextToken();
                        String teacher = tokenizer.nextToken();

                        for (int j = 0; j < student.HL.size(); j++) {
                            String teach = student.TeacherHL.get(j);
                            if (subject.contains("HL") && subject.contains("B" + student.BlockHL.get(j).substring(0, 1))
                                    && (teacher.contains(teach.substring(0, 2))//Checking front and beginning because of multiple teachers
                                    || teacher.contains(teach.substring(teach.length() - 2)))) {
                                classFound = true;
                                String room = ParseClasses.roomMaster[i];
                                if ((room.contains("8") || room.contains("9") || room.contains("10")) && !room.equals("IBD8")) {
                                    displayText(room.substring(1) + room.substring(0, 1), teach, subject.substring(0, subject.length() - 3));
                                } else {
                                    displayText(room, teach, subject.substring(0, subject.length() - 3));
                                }
                                break;
                            }
                        }

                        for (int k = 0; k < student.SL.size(); k++) {
                            String teach = student.TeacherSL.get(k);
                            if ((subject.contains("SL") || subject.contains("AB") || subject.contains("ST")) //French and Spanish AB don't have SL written
                                    && subject.contains("B" + student.BlockSL.get(k).substring(0, 1))
                                    && (teacher.contains(teach.substring(0, 2))
                                    || teacher.contains(teach.substring(teach.length() - 2)))) {
                                classFound = true;
                                String room = ParseClasses.roomMaster[i];
                                if (room.contains("8") || room.contains("9") || room.contains("10")) {
                                    displayText(room.substring(1) + room.substring(0, 1), teach, subject.substring(0, subject.length() - 3));
                                } else {
                                    displayText(room, teach, subject.substring(0, subject.length() - 3));
                                }
                                break;
                            }
                        }

                        String teach = student.TeacherTOK;
                        if (subject.contains("TOK") && (teacher.contains(teach.substring(0, 2))
                                || teacher.contains(teach.substring(teach.length() - 2)))) {
                            classFound = true;
                            String room = ParseClasses.roomMaster[i];
                            if (room.contains("8") || room.contains("9") || room.contains("10")) {
                                displayText(room.substring(1) + room.substring(0, 1), teach, subject.substring(0, subject.length() - 3));
                            } else {
                                displayText(room, teach, subject.substring(0, subject.length() - 3));
                            }
                        }
                    }
                }
                if (!classFound) {
                    displayText("-", "-", "-");
                }
            }
        } catch (NullPointerException e) {
        }
    }

    private void getData() {
        File roomMaster = LocalDB.getFile(LocalDB.FILE_ROOM_MASTER);
        Scanner s = null;
        try {
            s = new Scanner(roomMaster);
        } catch (FileNotFoundException e) {
        }

        String fLine = s.nextLine();

        while (s.hasNext()) {
            StringTokenizer fLineToken = new StringTokenizer(fLine, ",", false);

            String data = s.nextLine();
            StringTokenizer dataToken = new StringTokenizer(data, ",", false);

            TimeSlot timeSlot = new TimeSlot();

            while (dataToken.hasMoreTokens()) {

                String column = fLineToken.nextToken().trim();
                String token = dataToken.nextToken();

                token = token.substring(0, token.length() - 1).trim();

                switch (column) {
                    case "Time":
                        token = token.substring(2);
                        String build = "";
                        int count = 0;
                        for (int i = 0; i < token.length(); i++) {
                            char x = token.charAt(i);
                            if (x == ':') {
                                int time = Integer.parseInt(build.trim());
                                if (time < 4) {
                                    time += 12;
                                }
                                build = "";
                                if (count == 0) {
                                    count++;
                                    timeSlot.times.put("startHour", time);
                                } else {
                                    count = 0;
                                    timeSlot.times.put("endHour", time);
                                }
                            } else if (x == '-') {
                                timeSlot.times.put("startMinute", Integer.parseInt(build.trim()));
                                build = "";
                            } else {
                                build += x;
                            }
                        }
                        timeSlot.times.put("endMinute", Integer.parseInt(build.trim()));
                        break;
                    case "Days":
                        timeSlot.day = token;
                        break;
                    default:
                        timeSlot.classes.put(column, token);
                        break;
                }
            }
            timeSlots.add(timeSlot);
            mDataAvailable = true;
        }
    }

    private void displayText(String className, String teacherName, String subjectName) {
        informationText.setText(student.firstname.trim() + " " + student.lastname.trim()
                + " should be in " + className + " with " + teacherName + " studying " + subjectName + ".");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        studentName = new javax.swing.JLabel();
        studentGender = new javax.swing.JLabel();
        textHL1 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        textHL2 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        textHL3 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        HL3 = new javax.swing.JLabel();
        teacherHL3 = new javax.swing.JLabel();
        SL3 = new javax.swing.JLabel();
        teacherSL3 = new javax.swing.JLabel();
        HL2 = new javax.swing.JLabel();
        teacherHL2 = new javax.swing.JLabel();
        SL2 = new javax.swing.JLabel();
        teacherSL2 = new javax.swing.JLabel();
        HL1 = new javax.swing.JLabel();
        teacherHL1 = new javax.swing.JLabel();
        SL1 = new javax.swing.JLabel();
        teacherSL1 = new javax.swing.JLabel();
        daySelector = new javax.swing.JComboBox<String>();
        jLabel34 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        textHour = new javax.swing.JTextField();
        textMinute = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        teacherTOK = new javax.swing.JLabel();
        backButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        informationText = new javax.swing.JTextArea();

        jLabel9.setText("jLabel5");

        jLabel10.setText("jLabel5");

        jLabel15.setText("jLabel5");

        jLabel16.setText("jLabel5");

        jLabel17.setText("jLabel11");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("Name:");

        jLabel2.setText("Gender:");

        studentName.setText("jLabel3");

        studentGender.setText("jLabel4");

        textHL1.setText("HL1:");

        jLabel6.setText("Tchr:");

        jLabel7.setText("SL1:");

        jLabel8.setText("Tchr:");

        jLabel21.setText("SL2:");

        jLabel22.setText("Tchr:");

        textHL2.setText("HL2:");

        jLabel24.setText("Tchr:");

        jLabel25.setText("SL3:");

        jLabel26.setText("Tchr:");

        textHL3.setText("HL3:");

        jLabel28.setText("Tchr:");

        HL3.setText("jLabel11");

        teacherHL3.setText("jLabel11");

        SL3.setText("jLabel11");

        teacherSL3.setText("jLabel11");

        HL2.setText("jLabel11");

        teacherHL2.setText("jLabel11");

        SL2.setText("jLabel11");

        teacherSL2.setText("jLabel11");

        HL1.setText("jLabel11");

        teacherHL1.setText("jLabel11");

        SL1.setText("jLabel11");

        teacherSL1.setText("jLabel11");

        daySelector.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Monday", "Tuesday", "Wednesday", "Thursday", "Friday" }));
        daySelector.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                daySelectorActionPerformed(evt);
            }
        });

        jLabel34.setText(":");

        jLabel35.setText("(HH:MM)");

        textHour.setText("jTextField1");

        textMinute.setText("jTextField2");

        jLabel3.setText("TOK Tchr:");

        teacherTOK.setText("jLabel4");

        backButton.setText("Back");
        backButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backButtonActionPerformed(evt);
            }
        });

        informationText.setEditable(false);
        informationText.setBackground(new java.awt.Color(238, 238, 238));
        informationText.setColumns(20);
        informationText.setLineWrap(true);
        informationText.setRows(2);
        informationText.setWrapStyleWord(true);
        informationText.setBorder(null);
        informationText.setFocusable(false);
        informationText.setOpaque(false);
        jScrollPane1.setViewportView(informationText);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(backButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 117, Short.MAX_VALUE)
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(teacherTOK, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(106, 106, 106))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(13, 13, 13)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(textHL1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(HL1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(teacherHL1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(SL1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(teacherSL1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(textHL2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(HL2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(teacherHL2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(SL2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(teacherSL2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(textHL3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel28, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(HL3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(teacherHL3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(SL3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(teacherSL3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(studentGender, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(studentName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(daySelector, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(textHour, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel34)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(textMinute, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel35))
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 441, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(studentName))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(studentGender))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(daySelector, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel34)
                    .addComponent(jLabel35)
                    .addComponent(textHour, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(textMinute, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(textHL1)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel6)
                                .addComponent(teacherHL1))
                            .addGap(18, 18, 18)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel7)
                                .addComponent(SL1))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel8)
                                .addComponent(teacherSL1)))
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(textHL2)
                                .addComponent(HL1))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jLabel24)
                            .addGap(18, 18, 18)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel21)
                                .addComponent(SL2))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jLabel22)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(HL2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(teacherHL2)
                        .addGap(40, 40, 40)
                        .addComponent(teacherSL2))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(textHL3)
                            .addComponent(HL3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel28)
                            .addComponent(teacherHL3))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel25)
                            .addComponent(SL3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel26)
                            .addComponent(teacherSL3))))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(teacherTOK)
                    .addComponent(backButton))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void backButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backButtonActionPerformed
        // TODO add your handling code here:
        new ListFrame(SortBy.STUDENT, toReturnIfNeeded, returnIfNeeded, getLocation()).setVisible(true);
        setVisible(false);
    }//GEN-LAST:event_backButtonActionPerformed

    private void daySelectorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_daySelectorActionPerformed
        // TODO add your handling code here:
        if (!textHour.getText().isEmpty() && !textMinute.getText().isEmpty()) {
            updateLocationInformation();
        }
    }//GEN-LAST:event_daySelectorActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel HL1;
    private javax.swing.JLabel HL2;
    private javax.swing.JLabel HL3;
    private javax.swing.JLabel SL1;
    private javax.swing.JLabel SL2;
    private javax.swing.JLabel SL3;
    private javax.swing.JButton backButton;
    private javax.swing.JComboBox<String> daySelector;
    private javax.swing.JTextArea informationText;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel studentGender;
    private javax.swing.JLabel studentName;
    private javax.swing.JLabel teacherHL1;
    private javax.swing.JLabel teacherHL2;
    private javax.swing.JLabel teacherHL3;
    private javax.swing.JLabel teacherSL1;
    private javax.swing.JLabel teacherSL2;
    private javax.swing.JLabel teacherSL3;
    private javax.swing.JLabel teacherTOK;
    private javax.swing.JLabel textHL1;
    private javax.swing.JLabel textHL2;
    private javax.swing.JLabel textHL3;
    private javax.swing.JTextField textHour;
    private javax.swing.JTextField textMinute;
    // End of variables declaration//GEN-END:variables
}
