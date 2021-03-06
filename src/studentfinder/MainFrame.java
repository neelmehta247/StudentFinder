/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package studentfinder;

import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author neel
 */
public class MainFrame extends javax.swing.JFrame implements SwingWorkerCallbacks {

    /**
     * Creates new form MainFrame
     *
     * @param point
     */
    public MainFrame(Point point) {
        initComponents();

        setLocation(point);

        new GetRequestSwingWorker(MainFrame.this, ParseRequestTypes.UPDATED, null).execute();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonStudent = new javax.swing.JButton();
        buttonSubject = new javax.swing.JButton();
        buttonTeacher = new javax.swing.JButton();
        buttonAdmin = new javax.swing.JButton();
        infoLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        buttonStudent.setText("Students");
        buttonStudent.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonStudentActionPerformed(evt);
            }
        });

        buttonSubject.setText("Subjects");
        buttonSubject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonSubjectActionPerformed(evt);
            }
        });

        buttonTeacher.setText("Teachers");
        buttonTeacher.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonTeacherActionPerformed(evt);
            }
        });

        buttonAdmin.setText("Admin");
        buttonAdmin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                buttonAdminMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(90, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(buttonTeacher, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(buttonAdmin, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(buttonStudent, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(buttonSubject, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(78, 78, 78))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(infoLabel)
                        .addGap(157, 157, 157))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(62, 62, 62)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(buttonStudent, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(buttonSubject, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonTeacher, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(buttonAdmin, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addComponent(infoLabel)
                .addContainerGap(105, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void buttonAdminMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_buttonAdminMouseClicked
        // TODO add your handling code here:
        setVisible(false);

        new AdminLoginFrame(getLocation()).setVisible(true);
    }//GEN-LAST:event_buttonAdminMouseClicked

    private void buttonStudentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonStudentActionPerformed
        // TODO add your handling code here:
        new ListFrame(SortBy.STUDENT, CallFrom.MAIN_FRAME, null, getLocation()).setVisible(true);
        setVisible(false);
    }//GEN-LAST:event_buttonStudentActionPerformed

    private void buttonTeacherActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonTeacherActionPerformed
        // TODO add your handling code here:
        new ListFrame(SortBy.TEACHER, CallFrom.MAIN_FRAME, null, getLocation()).setVisible(true);
        setVisible(false);
    }//GEN-LAST:event_buttonTeacherActionPerformed

    private void buttonSubjectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonSubjectActionPerformed
        // TODO add your handling code here:
        new ListFrame(SortBy.SUBJECT, CallFrom.MAIN_FRAME, null, getLocation()).setVisible(true);
        setVisible(false);
    }//GEN-LAST:event_buttonSubjectActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainFrame(new Point(0, 0)).setVisible(true);
            }
        });
    }

    @Override
    public void onDoInBckgroundStarted() {

    }

    @Override
    public void onDone(Object returnObject, Object otherParam) {
        if ((ParseRequestTypes) otherParam == ParseRequestTypes.UPDATED) {
            try {
                File lastUpdate = LocalDB.getFile(LocalDB.FILE_LAST_UPDATE);
                long updateTime = new Scanner(lastUpdate).nextLong();

                JSONObject json = (JSONObject) returnObject;

                JSONArray results = json.getJSONArray("results");
                for (int i = 0; i < results.length(); i++) {
                    JSONObject object = results.getJSONObject(i);
                    if (object.getLong("updateTime") >= updateTime) {
                        new GetRequestSwingWorker(this, ParseRequestTypes.REROOM, null).execute();
                        new GetRequestSwingWorker(this, ParseRequestTypes.ROOM_MASTER, null).execute();
                        new GetRequestSwingWorker(this, ParseRequestTypes.BLOCKWISE_DATA, null).execute();

                        LocalDB.writeFile(LocalDB.FILE_LAST_UPDATE, String.valueOf(System.currentTimeMillis() / 1000));
                        break;
                    }
                }
            } catch (JSONException | FileNotFoundException e) {
                e.printStackTrace();

                new GetRequestSwingWorker(this, ParseRequestTypes.REROOM, null).execute();
                new GetRequestSwingWorker(this, ParseRequestTypes.ROOM_MASTER, null).execute();
                new GetRequestSwingWorker(this, ParseRequestTypes.BLOCKWISE_DATA, null).execute();

                LocalDB.writeFile(LocalDB.FILE_LAST_UPDATE, String.valueOf(System.currentTimeMillis() / 1000));
            }
        } else if ((ParseRequestTypes) otherParam == ParseRequestTypes.REROOM) {
            LocalDB.makeCSV(LocalDB.FILE_REROOM, (JSONObject) returnObject, ParseClasses.reroom);
        } else if ((ParseRequestTypes) otherParam == ParseRequestTypes.ROOM_MASTER) {
            LocalDB.makeCSV(LocalDB.FILE_ROOM_MASTER, (JSONObject) returnObject, ParseClasses.roomMaster);
        } else if ((ParseRequestTypes) otherParam == ParseRequestTypes.BLOCKWISE_DATA) {
            LocalDB.makeCSV(LocalDB.FILE_BLOCKWISE_DATA, (JSONObject) returnObject, ParseClasses.blockwiseDataStudents);
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonAdmin;
    private javax.swing.JButton buttonStudent;
    private javax.swing.JButton buttonSubject;
    private javax.swing.JButton buttonTeacher;
    private javax.swing.JLabel infoLabel;
    // End of variables declaration//GEN-END:variables
}
