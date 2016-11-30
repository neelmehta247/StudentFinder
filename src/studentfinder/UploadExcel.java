package studentfinder;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author neel
 */
public class UploadExcel {
    
    public static boolean uploadBlockwiseData(File inputFile) {
        try {
            // Get the workbook object for XLSX file
            Workbook workBook = WorkbookFactory.create(new FileInputStream(inputFile)); //Workbook and not XSSFWorkbook to allow for .xls and .xlsx files both
            // Get first sheet from the workbook
            Sheet firstSheet = workBook.getSheetAt(0);
            Row row;
            Cell cell;
            // Iterate through each rows from first sheet
            Iterator<Row> iterator = firstSheet.iterator();
            row = iterator.next();
            row = iterator.next();//Have to remove 2 rows

            boolean firstRow = true;
            ArrayList<String> columns = new ArrayList<>();
            
            JSONArray requestArray = new JSONArray();
            
            while (iterator.hasNext()) {
                row = iterator.next();
                
                JSONObject requestObject = new JSONObject();
                requestObject.put("method", "POST");
                requestObject.put("path", "/1/classes/BlockwiseDataStudents");
                
                JSONObject bodyObject = new JSONObject();

                // For each row, iterate through each columns
                Iterator<Cell> cellIterator = row.cellIterator();
                
                String currentSubject = "";
                int i = 0;
                int countForBlockwise = 0;
                
                while (cellIterator.hasNext()) {
                    
                    cell = cellIterator.next();
                    String cellValue;
                    try {
                        cellValue = cell.getStringCellValue();
                    } catch (IllegalStateException e) {
                        cellValue = String.valueOf((int) cell.getNumericCellValue());
                    }
                    
                    if (firstRow) {
                        if (cellValue.contains("Sr.")) {
                            columns.add("srno");
                            countForBlockwise++;
                        } else if (cellValue.contains("Middle")) {
                            columns.add("middlename");
                            countForBlockwise++;
                        } else if (cellValue.contains("First")) {
                            columns.add("firstname");
                            countForBlockwise++;
                        } else if (cellValue.contains("Last")) {
                            columns.add("lastname");
                            countForBlockwise++;
                        } else if (cellValue.contains("HL") || cellValue.contains("SL") || cellValue.contains("TOK")) {
                            currentSubject = cellValue.trim();
                            columns.add(currentSubject);
                        } else if (cellValue.contains("lock") || cellValue.contains("chr")) {
                            columns.add(cellValue.trim() + currentSubject);
                        } else if (!cellValue.trim().isEmpty()) {
                            columns.add(cellValue.trim());
                        }
                    } else {
                        if (!(i >= columns.size())) {
                            if (columns.get(i).equals("srno")) {
                                bodyObject.put(columns.get(i), Integer.parseInt(cellValue.trim()));
                            } else if (!cellValue.isEmpty()) {
                                bodyObject.put(columns.get(i), LocalDB.optimiseText(cellValue.trim()).trim());
                            } else {
                                bodyObject.put(columns.get(i), "");
                            }
                        }
                    }
                    i++;
                }
                if (firstRow) {
                    firstRow = false;
                    if (!(countForBlockwise >= 3)) {
                        return false;
                    }
                } else {
                    requestObject.put("body", bodyObject);
                    requestArray.put(requestObject);
                    
                    if (requestArray.length() == 50) {
                        JSONObject send = new JSONObject();
                        send.put("requests", requestArray);
                        
                        new PostRequestSwingWorker("batch", send).execute();
                        
                        requestArray = new JSONArray();
                    }
                }
            }
            
            JSONObject send = new JSONObject();
            send.put("requests", requestArray);
            
            new PostRequestSwingWorker("batch", send).execute();
            return true;
        } catch (Exception ioe) {
            ioe.printStackTrace();
            return false;
        }
    }
    
    public static void convert(File inputFile, File outputFile) {
        // For storing data into CSV files
        StringBuilder info = new StringBuilder();
        
        try {
            FileOutputStream outputStream = new FileOutputStream(outputFile);
            // Get the workbook object for XLSX file
            Workbook workBook = WorkbookFactory.create(new FileInputStream(inputFile)); //Workbook and not XSSFWorkbook to allow for .xls and .xlsx files both
            // Get first sheet from the workbook
            Sheet firstSheet = workBook.getSheetAt(0);
            Row row;
            Cell cell;
            // Iterate through each rows from first sheet
            Iterator<Row> iterator = firstSheet.iterator();
            row = iterator.next();
            row = iterator.next();//Have to remove 2 rows
            boolean firstRow = true;
            ArrayList<String> columns = new ArrayList<>();
            
            while (iterator.hasNext()) {
                row = iterator.next();

                // For each row, iterate through each columns
                Iterator<Cell> cellIterator = row.cellIterator();
                
                String currentSubject = "";
                int i = 0;
                
                while (cellIterator.hasNext()) {
                    
                    cell = cellIterator.next();
                    String cellValue;
                    try {
                        cellValue = cell.getStringCellValue();
                    } catch (IllegalStateException e) {
                        cellValue = String.valueOf(cell.getNumericCellValue());
                    }
                    
                    if (firstRow) {
                        if (cellValue.contains("Sr.")) {
                            info.append("srno ,");
                            columns.add("srno");
                        } else if (cellValue.contains("Middle")) {
                            info.append("middlename ,");
                            columns.add("middlename");
                        } else if (cellValue.contains("First")) {
                            info.append("firstname ,");
                            columns.add("firstname");
                        } else if (cellValue.contains("Last")) {
                            info.append("lastname ,");
                            columns.add("lastname");
                        } else if (cellValue.contains("HL") || cellValue.contains("SL") || cellValue.contains("TOK")) {
                            info.append(cellValue).append(" ,");
                            currentSubject = cellValue.trim();
                            columns.add(currentSubject);
                        } else if (cellValue.contains("lock") || cellValue.contains("chr")) {
                            info.append(cellValue.trim()).append(currentSubject).append(" ,");
                            columns.add(cellValue.trim() + currentSubject);
                        } else if (!cellValue.trim().isEmpty()) {
                            info.append(cellValue.trim()).append(" ,");
                            columns.add(cellValue.trim());
                        }
                    } else {
                        if (!(i >= columns.size())) {
                            if (!cellValue.isEmpty()) {
                                info.append(LocalDB.optimiseText(cellValue.trim())).append(",");
                            } else {
                                info.append(" ,");
                            }
                        }
                    }
                    i++;
                }
                info.append("\r\n");
                if (firstRow) {
                    firstRow = false;
                }
            }
            
            outputStream.write(info.toString().getBytes());
            outputStream.close();
        } catch (Exception ioe) {
            ioe.printStackTrace();
        }
    }
    
    public static boolean uploadRoomMaster(File inputFile) {
        try {
            // Get the workbook object for XLSX file
            Workbook workBook = WorkbookFactory.create(new FileInputStream(inputFile));
            // Get first sheet from the workbook
            Sheet firstSheet = workBook.getSheetAt(0);
            Row row;
            Cell cell;
            // Iterate through each rows from first sheet
            Iterator<Row> iterator = firstSheet.iterator();
            
            row = iterator.next();
            boolean firstRow = true;
            String currentDay = "";
            ArrayList<String> columns = new ArrayList<>();
            
            JSONArray requestArray = new JSONArray();
            
            while (iterator.hasNext()) {
                row = iterator.next();

                // For each row, iterate through each columns
                Iterator<Cell> cellIterator = row.cellIterator();
                
                boolean firstColumn = true;
                boolean secondColumn = false;
                
                JSONObject requestObject = new JSONObject();
                requestObject.put("method", "POST");
                requestObject.put("path", "/1/classes/RoomMaster");
                
                JSONObject bodyObject = new JSONObject();
                
                int i = 0;
                int emptyClassCounter = 0;
                while (cellIterator.hasNext()) {
                    
                    cell = cellIterator.next();
                    String cellValue;
                    try {
                        cellValue = cell.getStringCellValue();
                    } catch (IllegalStateException e) {
                        cellValue = String.valueOf(cell.getNumericCellValue());
                    }
                    
                    if (firstRow && firstColumn) {
                        columns.add("Days");
                        firstColumn = false;
                        secondColumn = true;
                    } else if (firstRow && secondColumn) {
                        columns.add("Time");
                        secondColumn = false;
                    } else if (firstRow && cellValue.startsWith("8")) {
                        columns.add(cellValue.substring(1).trim() + "8");
                    } else if (firstRow && cellValue.startsWith("9")) {
                        columns.add(cellValue.substring(1).trim() + "9");
                    } else if (firstRow && cellValue.startsWith("10")) {
                        columns.add(cellValue.substring(2).trim() + "10");
                    } else if (firstRow) {
                        columns.add(cellValue.trim());
                    } else {
                        if (firstColumn) {
                            if (!cellValue.trim().isEmpty()) {
                                currentDay = cellValue.trim();
                            }
                            bodyObject.put(columns.get(i), LocalDB.optimiseText(currentDay).trim());
                            firstColumn = false;
                            secondColumn = true;
                        } else if (secondColumn) {
                            bodyObject.put(columns.get(i), LocalDB.optimiseText(cellValue).trim());
                            secondColumn = false;
                        } else {
                            if (cellValue.toUpperCase().trim().startsWith("YEAR")) {
                                bodyObject.put(columns.get(i), LocalDB.optimiseText(cellValue).trim());
                            } else {
                                if (i < columns.size()) {
                                    bodyObject.put(columns.get(i), "");
                                    emptyClassCounter++;
                                }
                            }
                        }
                    }
                    i++;
                }
                if (firstRow || (emptyClassCounter >= (columns.size() - 2))) {
                    firstRow = false;
                } else {
                    requestObject.put("body", bodyObject);
                    requestArray.put(requestObject);
                }
            }
            
            JSONObject send = new JSONObject();
            send.put("requests", requestArray);
            
            new PostRequestSwingWorker("batch", send).execute();
            return true;
        } catch (IOException | InvalidFormatException | EncryptedDocumentException | JSONException ioe) {
            return false;
        }
    }
    
    public static void main(String[] args) {
        //reading file from desktop
        File inputFile = new File("/Users/neel/Desktop/Yr 12-2015-16-Blockwise data (7th-Aug2015).xlsx");
        //writing excel data to csv 
        File outputFile = new File("/Users/neel/Desktop/test.csv");
        convert(inputFile, outputFile);
    }
}
