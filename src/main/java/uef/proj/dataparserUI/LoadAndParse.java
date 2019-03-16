package uef.proj.dataparserUI;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class LoadAndParse {

    // Create a DataFormatter to format and get each cell's value as String
    private final DataFormatter dataFormatter = new DataFormatter();
    private final String AnimalId = "animalid";

    private Workbook probeWb;
    private Workbook trialWb;

    LoadAndParse(File file) {
        try {
            System.out.println("load&parse tiedosto " + file);
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
                    heading += dataFormatter.formatCellValue(tieto) + "\n";
                }
            }
            headers.add(heading);

            a++;
        } while (tieto != null);
        return headers;
    }

    public HashMap<Integer, HashMap<String, Double>> readData(ArrayList<HeaderInfo> headingsInfo) {
        System.out.println("Aloitus");
        // Read the workbook and add necessary data to HashMap according to the headings list
        HashMap<Integer, HashMap<String, Double>> hashmap = new HashMap<>();
        // Read the headings
        ArrayList<String> headings = new ArrayList();
        headingsInfo.forEach(e -> headings.add(e.getHeading()));

        // Add other data
        for (Row row : probeWb.getSheetAt(0)) {

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
                    temp.put(headings.get(i - 2), Double.valueOf(cellValue));
                } catch (NumberFormatException ex) {
                    temp.put(headings.get(i - 2), null);
                }

            }
        }
        // TODO
        // Heading stuff needs to be figured and commented stuff coded and trial-list readed for the correct date value to the temp map
        System.out.println("lopetus");
        return hashmap;
    }

    public void addData(ArrayList<HeaderInfo> headingsInfo, HashMap<Integer, HashMap<String, Double>> data) {
        Workbook workbook = new XSSFWorkbook(); // new HSSFWorkbook() for generating `.xls` file
        ArrayList<String> headings = new ArrayList();
        headingsInfo.forEach(e -> headings.add(e.getHeading()));
        // Create a Sheet
        Sheet sheet = workbook.createSheet("Sheet1");
        int j = 0;
        // Create cells
        for (String head : headings) {

            for (Map.Entry<Integer, HashMap<String, Double>> trialEntry : data.entrySet()) {
                j++;
                if (headingsInfo.get(j).isNormal()) {
                    // Create a Row
                    int i = 1;
                    Row row = sheet.createRow(j);

                    // Fill the row
                    // for (Map.Entry<String, Double> dataEntry : trialEntry.getValue().entrySet()) {
                    Double dataEntry = trialEntry.getValue().get(head);
                    Cell cell = row.createCell(i);
                    if (dataEntry == null) {
                        cell.setCellValue("-");
                    } else {
                        cell.setCellValue(dataEntry);
                    }
                    i++;
                } else if (headingsInfo.get(j - 1).isAvg()) {
                    j++;

                    int i = 1;
                    Row row = sheet.createRow(j);

                    // Fill the row
                    // for (Map.Entry<String, Double> dataEntry : trialEntry.getValue().entrySet()) {
                    Double dataEntry = getAverage(data, trialEntry.getValue().get(AnimalId), head);
                    Cell cell = row.createCell(i);
                    if (dataEntry == null) {
                        cell.setCellValue("-");
                    } else {
                        cell.setCellValue(dataEntry);
                    }
                    i++;
                }
            }

        }

        // Add Headings
        Row row = sheet.createRow(0);
        Cell aniCell = row.createCell(0);
        aniCell.setCellValue("AnID_1");
        for (int i = 1; i < headings.size(); i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(headings.get(i));
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

    private double getAverage(HashMap<Integer, HashMap<String, Double>> data, double id, String head) {
        ArrayList<Double> values = new ArrayList<>();
        for (Map.Entry<Integer, HashMap<String, Double>> trialEntry : data.entrySet()) {
            if (trialEntry.getValue().get(AnimalId).equals(id)) {
                values.add(trialEntry.getValue().get(head));
            }
        }
        return calculateAverage(values);
    }

    private static double calculateAverage(List<Double> marks) {
        Double sum = 0.0;
        if (!marks.isEmpty()) {
            sum = marks.stream().map((mark) -> mark).reduce(sum, (accumulator, _item) -> accumulator + _item);
            return sum / marks.size();
        }
        return sum;
    }
}
