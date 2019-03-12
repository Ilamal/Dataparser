package uef.proj.dataparserUI;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class LoadAndParse {

    // Create a DataFormatter to format and get each cell's value as String
    private final DataFormatter dataFormatter = new DataFormatter();
    private final String AnimalId = "animalid";
    
   // String filepathProbe = LoadAndParse.class.getClassLoader().getResource("Statistics-Geronimo-pup-Probe.xlsx").getFile();
   // String filepathTrialList = LoadAndParse.class.getClassLoader().getResource("Trial_list_Heikille.xlsx").getFile();
    Workbook probeWb;
    Workbook trialWb;
    LoadAndParse(File file) {
        try {
         probeWb = WorkbookFactory.create(file);
         trialWb = WorkbookFactory.create(file);
        } catch (IOException ex) {
            System.out.println(Arrays.toString(ex.getStackTrace()));            
        }
    }
    ArrayList getAllHeaders() {
        ArrayList<String> headers = new ArrayList();
        int a = 2;
        Cell tieto = null;
         do {
            String heading = "";

            for (int i = 0; i < 4; i++) {

                Row row = probeWb.getSheetAt(0).getRow(i);
                if (dataFormatter.formatCellValue(row.getCell(a)).equals("") && i == 0) {
                    tieto = null;
                    break;
                } else {
                    tieto = row.getCell(a);
                    heading += dataFormatter.formatCellValue(tieto);
                    headers.add(heading);
                }
            }

            a++;
        } while (tieto != null);
         return headers;
    }
    /*public static void main(String[] args) throws IOException {
        //Get the file from resources
        

        //Create the Workbook to work with and print the number of sheets
        
        System.out.println("Workbook has " + probeWb.getNumberOfSheets() + " Sheets : ");

        // Getting the Sheets at index zero
        Sheet probeSheet = probeWb.getSheetAt(0);
        Sheet trialSheet = trialWb.getSheetAt(0);

        System.out.println("\n\nIterating over probe file using for-each loop\n");
        for (Row row : probeSheet) {
            for (Cell cell : row) {
                String cellValue = dataFormatter.formatCellValue(cell);
                System.out.print(cellValue + "\t");
            }
            System.out.println();
        }

        System.out.println("\n\nIterating over trial-lists file using for-each loop\n");
        for (Row row : trialSheet) {
            for (Cell cell : row) {
                String cellValue = dataFormatter.formatCellValue(cell);
                System.out.print(cellValue + "\t");
            }
            System.out.println();
        }
        System.out.println("\n\n--------- END OF TEST EXAMPLES");
        //List for testing (in future needs to be somekind of standard from user choices averages etc..)
        ArrayList<String> list = new ArrayList();
        list.add("Distance to point");
        addData(list, readData(probeWb));

        // Close the workbooks
        probeWb.close();
        trialWb.close();
    }
*/
    public HashMap<Integer, HashMap<String, Double>> readData(Workbook wb) {
        System.out.println("Aloitus");
        // Read the workbook and add necessary data to HashMap according to the headings list
        HashMap<Integer, HashMap<String, Double>> hashmap = new HashMap<>();
        // Read the headings
        ArrayList<String> apu = new ArrayList();
        int a = 2;
        Cell tieto = null;
        System.out.println("do while alku");
        do {
            String heading = "";

            for (int i = 0; i < 4; i++) {

                Row row = wb.getSheetAt(0).getRow(i);
                if (dataFormatter.formatCellValue(row.getCell(a)).equals("") && i == 0) {
                    tieto = null;
                    break;
                } else {
                    tieto = row.getCell(a);
                    heading += dataFormatter.formatCellValue(tieto);
                    apu.add(heading);
                }
            }

            a++;
        } while (tieto != null);
        System.out.println("do while loppu");
        // Add other data
        for (Row row : wb.getSheetAt(0)) {

            HashMap<String, Double> temp = new HashMap<>();
            System.out.println("sattaa kusta... 1");
            if (dataFormatter.formatCellValue(row.getCell(0)).replace("Trial ", "").equals("")) {
                continue;
            }
            int trialn = Integer.valueOf(dataFormatter.formatCellValue(row.getCell(0)).replace("Trial   ", ""));

            temp.put(AnimalId, Double.valueOf(dataFormatter.formatCellValue(row.getCell(1))));

            hashmap.put(trialn, temp);

            //     apu.forEach(e-> System.out.println(e));
            //saattaa kusta
            for (int i = 2; i < row.getLastCellNum(); i++) {
                System.out.println("loopissa");
                Cell cell = row.getCell(i);
                String cellValue = dataFormatter.formatCellValue(cell);
                //temp.put(heading, cellValue)
                System.out.println(cellValue);
                try {
                    temp.put(apu.get(i - 2), Double.valueOf(cellValue));
                } catch (NumberFormatException ex) {
                    temp.put(apu.get(i - 2), null);
                }

            }
        }
        // TODO
        // Heading stuff needs to be figured and commented stuff coded and trial-list readed for the correct date value to the temp map
        System.out.println("lopetus");
        return hashmap;
    }

    public void addData(List headings, HashMap<Integer, HashMap<String, Double>> data) {
        Workbook workbook = new XSSFWorkbook(); // new HSSFWorkbook() for generating `.xls` file
        // Create a Sheet
        Sheet sheet = workbook.createSheet("Sheet1");
        int j = 1;
        // Create cells
        for (Map.Entry<Integer, HashMap<String, Double>> trialEntry : data.entrySet()) {

            // Create a Row
            int i = 0;
            Row row = sheet.createRow(j);
            j++;
            // Fill the row
            for (Map.Entry<String, Double> dataEntry : trialEntry.getValue().entrySet()) {
                Cell cell = row.createCell(i);
                if (dataEntry.getValue() == null) {
                    cell.setCellValue("-");
                } else {
                    cell.setCellValue(dataEntry.getValue());
                }
                i++;
            }
        }
        // Add Headings
        Row row = sheet.createRow(0);
        for (int i = 0; i < headings.size(); i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(headings.get(i).toString());

        }

        //    TODO     keskiarvot ja lyhennyksien/otsikoiden mukaan laitto, kaiken sijasta
        createXlsx(workbook);
        //TODO      
    }

    private void createXlsx(Workbook wb) {
        // Write the output to a file
        FileOutputStream fileOut = null;
        try {
            fileOut = new FileOutputStream("generated-statistics-file.xlsx");
            wb.write(fileOut);
        } catch (IOException ex) {
            Logger.getLogger(LoadAndParse.class
                    .getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                // Closing the stream and workbook
                wb.close();
                fileOut.close();
            } catch (IOException | NullPointerException ex) {
                Logger.getLogger(LoadAndParse.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
