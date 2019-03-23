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
        // Read the workbook and add necessary data to HashMap according to the headings list
        HashMap<Integer, HashMap<String, Double>> hashmap = new HashMap<>();
        // Read the headings
        ArrayList<String> headings = new ArrayList();
        headingsInfo.forEach(e -> headings.add(e.getHeading()));

        // Add other data
        for (Row row : probeWb.getSheetAt(0)) {

            HashMap<String, Double> temp = new HashMap<>();
            if (dataFormatter.formatCellValue(row.getCell(0)).replace("Trial ", "").equals("")) {
                continue;
            }
            int trialn = Integer.valueOf(dataFormatter.formatCellValue(row.getCell(0)).replace("Trial", "").replace(" ", ""));

            temp.put(AnimalId, Double.valueOf(dataFormatter.formatCellValue(row.getCell(1))));

            hashmap.put(trialn, temp);

            //TODO: HeadingsInfon käyttäminen 
            for (int i = 2; i < row.getLastCellNum(); i++) {

                Cell cell = row.getCell(i);
                String cellValue = dataFormatter.formatCellValue(cell);
                //temp.put(heading, cellValue)
                // System.out.println(cellValue);
                try {
                    temp.put(headings.get(i - 2), Double.valueOf(cellValue.replace(",", ".")));
                } catch (NumberFormatException ex) {
                    temp.put(headings.get(i - 2), null);
                }

            }
        }

        System.out.println("lopetus");
        return hashmap;
    }

    private ArrayList<Row> getRows(Sheet sheet, int amount) {
        ArrayList<Row> rows = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            rows.add(sheet.createRow(i));
        }
        return rows;
    }

    public void addData(ArrayList<HeaderInfo> headingsInfo, HashMap<Integer, HashMap<String, Double>> data) {
        Workbook workbook = new XSSFWorkbook(); // new HSSFWorkbook() for generating `.xls` file
        ArrayList<String> headings = new ArrayList();
        headingsInfo.forEach(e -> headings.add(e.getHeading()));
        // Create a Sheet
        Sheet sheet = workbook.createSheet("Sheet1");

        ArrayList<Row> rows = getRows(sheet, 50);

        // Add Headings
        Row topRow = sheet.createRow(0);
        Cell aniCell = topRow.createCell(0);
        aniCell.setCellValue("AnID_1");
        for (int i = 1; i < headings.size(); i++) {
            Cell cell = topRow.createCell(i);
            cell.setCellValue(headings.get(i - 1));
        }

        int colIdx = 1;
        // Create cells
        for (String head : headings) {

            int rowIdx = 1;
            if (headingsInfo.get(colIdx - 1).isNormal()) {

                for (Map.Entry<Integer, HashMap<String, Double>> trialEntry : data.entrySet()) {

                    Row row = rows.get(rowIdx);

                    // Fill the row
                    // for (Map.Entry<String, Double> dataEntry : trialEntry.getValue().entrySet()) {
                    Double dataEntry = trialEntry.getValue().get(head);
                    Cell cell = row.createCell(colIdx);
                    if (dataEntry == null) {
                        cell.setCellValue("-");
                    } else {
                        cell.setCellValue(dataEntry.toString());
                    }
                    row.createCell(0).setCellValue(trialEntry.getValue().get(AnimalId));
                    rowIdx++;
                }
                colIdx++;

            } else if (headingsInfo.get(colIdx - 1).isAvg()) {
                //Tässä oli j++ outoilu
                for (Map.Entry<Integer, HashMap<String, Double>> trialEntry : data.entrySet()) {

                    Row row = rows.get(rowIdx);
                    //TODO: AnimalID 
                    Double dataEntry = getAverage(data, trialEntry.getValue().get(AnimalId), head);
                    Cell cell = row.createCell(colIdx);
                    if (dataEntry == null) {
                        cell.setCellValue("-");
                    } else {
                        cell.setCellValue(dataEntry.toString());
                    }
                    row.createCell(0).setCellValue(trialEntry.getValue().get(AnimalId));
                    rowIdx++;

                }
                colIdx++;

            }

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
            if (trialEntry.getValue().get(AnimalId).equals(id) && trialEntry.getValue().get(head) != null) {
                values.add(trialEntry.getValue().get(head));
            }
        }

        return calculateAverage(values);
    }

    private static double calculateAverage(List<Double> marks) {
        Double sum = 0.0;

        if (marks != null && !marks.isEmpty()) {
            sum = marks.stream().map((mark) -> mark).reduce(sum, (accumulator, _item) -> accumulator + _item);
            return sum / marks.size();
        }
        return sum;
    }
}
