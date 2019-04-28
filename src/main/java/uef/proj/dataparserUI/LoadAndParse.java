package uef.proj.dataparserUI;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;



public class LoadAndParse {

    // Create a DataFormatter to format and get each cell's value as String
    private final DataFormatter dataFormatter = new DataFormatter();
    private final String AnimalId = "animalid";

    private Workbook statisticsWb; // DATA
    private Workbook trialListWb; // DATES

    LoadAndParse(File probeFile, File trialListFile) {
        try {
            System.out.println("load&parse tiedosto " + probeFile + " " + trialListFile);
            statisticsWb = WorkbookFactory.create(probeFile);
            trialListWb = WorkbookFactory.create(trialListFile);

        } catch (IOException | NullPointerException ex) {
            System.out.println(Arrays.toString(ex.getStackTrace()));
            alertError(ex, "Problem with given files...");
        }
    }

    ArrayList getAllHeaders() {

        ArrayList<String> headers = new ArrayList();
        int a = 2;
        Cell tieto = null;
        do {
            String heading = "";

            for (int i = 0; i < 4; i++) {

                Row row = statisticsWb.getSheetAt(0).getRow(i);
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

    ArrayList shortHeadings(ArrayList<HeaderInfo> headingsInfo) {
        /* Returns the shortened versions of the headings. The shortened heading includes (HEAD(AVG)_XY:)
        1. HEAD - shortens the text part of the heading to 4-6 characters.
        2. (AVG) - included if a user selects the average option to be shown.
        3. X - Day.
        4. Y - Swim number.
         */
        ArrayList<String> shortHeadings = new ArrayList();
        //Creation of the HEAD part.
        headingsInfo.forEach(e -> shortHeadings.add(e.getHeading()));
        for (String s : shortHeadings) {
            String firstWord = s.substring(0, 5);

            //Check if average selected -> Add AVG if needed:
            if (true) {
                //shortHeadings.get(e) += "AVG"; //korjaa e
            }

            //Add char _ between the HEAD(AVG) part and the XY part:
            //shortHeadings.get(e) += "_"; //korjaa e
            //Add trial day:
            //Miten tähän päivät? Jokin tarkistus että mitä päiviä/monta trialia per päivä millekkin otsikolle löytyy? Kuulostaa aika raskaalta.
            //shortHeadings.get(?) += Integer.toString(?);
            //Add swim number:
            //shortHeadings.get(e) += Integer.toString(?);
        }

        return shortHeadings;

    }

    public HashMap<Integer, HashMap<String, Double>> readData(ArrayList<HeaderInfo> headingsInfo) {
        // Read the workbook and add necessary data to HashMap according to the headings list
        HashMap<Integer, HashMap<String, Double>> hashmap = new HashMap<>();
        // Read the headings
        ArrayList<String> headings = new ArrayList();
        headingsInfo.forEach(e -> headings.add(e.getHeading()));

        // Add other data
        for (Row row : statisticsWb.getSheetAt(0)) {

            HashMap<String, Double> temp = new HashMap<>();
            if (dataFormatter.formatCellValue(row.getCell(0)).replace("Trial ", "").equals("")) {
                continue;
            }
            int trialn = Integer.valueOf(dataFormatter.formatCellValue(row.getCell(0)).replace("Trial", "").replace(" ", ""));

            temp.put(AnimalId, Double.valueOf(dataFormatter.formatCellValue(row.getCell(1))));

            hashmap.put(trialn, temp);
            // DATES FROM TRIALLISTS
            Double day = new Double((Integer) readDateFromTrialList().get(trialn));
            temp.put("Date", day);
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

    HashMap readDateFromTrialList() {
        // Luetaan trialList tiedosto
        //for(???)
        HashMap<Integer, Integer> dates = new HashMap<>();

        /*
        Row row = trialListWb.getSheetAt(0).getRow(3); 
        dates.put(Integer.valueOf(dataFormatter.formatCellValue(row.getCell(0)).replace("Trial", "").replace(" ", "")), Integer.valueOf(dataFormatter.formatCellValue(row.getCell(5))));
         System.out.println("Moro, päästiin metodiin!!!");
         System.out.println(dates.keySet());
         System.out.println(dates.values());
         */
        // poikki jos solu tyhjä
        int idx = 1;
        Row row = trialListWb.getSheetAt(0).getRow(idx);
        while (row != null) {
            //!(dataFormatter.formatCellValue(row.getCell(0))).equals("")
            // System.out.println("meneekö" + idx);

            //System.out.println("");
            //Laitetaan dates hashmapiin tiedot päivistä <TrialNumber, Date>
            int trialNumber = Integer.valueOf(dataFormatter.formatCellValue(row.getCell(0)).replaceAll("\\D", ""));
            int trialDay = Integer.valueOf(dataFormatter.formatCellValue(row.getCell(5)));
            dates.put(trialNumber, trialDay);
            row = trialListWb.getSheetAt(0).getRow(++idx);
        }

        // TESTIT - VOIKO POISTAA?
        //System.out.println("Moro, päästiin metodiin!!!");
        //System.out.println(dates.get(301));
        //System.out.println(dates.get(300));
        //Palautetaan
        return dates;
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
        ArrayList temp = new ArrayList();
        headingsInfo.forEach(e -> {
            if (e.normal || e.avg) {
                temp.add(e);
                headings.add(e.heading);
            }
        });
        headingsInfo = temp;
        // Create a Sheet
        Sheet sheet = workbook.createSheet("Sheet1");

        Set getAnims = getAllAnimals(data);
        Object[] allAnims = getAnims.toArray();
        ArrayList<Row> rows = getRows(sheet, 50);

        // Add Headings
        Row topRow = sheet.createRow(0);
        Cell aniCell = topRow.createCell(0);
        aniCell.setCellValue("AnID_1");
        for (int i = 1; i <= headings.size(); i++) {
            Cell cell = topRow.createCell(i);
            cell.setCellValue(headings.get(i - 1));
        }
        int it = 1;
        // Add animalnumbers
        for (Object ani : allAnims) {
            rows.get(it).createCell(0).setCellValue((Double) ani);
            it++;
        }
        int colIdx = 1;
        // Create cells
        for (String head : headings) {
            HeaderInfo headInfo = headingsInfo.get(colIdx - 1);
            int rowIdx = 1;
            if (headInfo.isNormal()) {
                topRow.createCell(colIdx).setCellValue(headInfo.getHeading());
                for (Map.Entry<Integer, HashMap<String, Double>> trialEntry : data.entrySet()) {

                    Row row = rows.get(rowIdx);

                    Double dataEntry = trialEntry.getValue().get(head);
                    Cell cell = row.createCell(colIdx);
                    if (dataEntry == null) {
                        cell.setCellValue("-");
                    } else {
                        cell.setCellValue(dataEntry.toString());
                    }
                    rowIdx++;
                }

                colIdx++;
                if (headInfo.isAvg()) {
                    rowIdx = 1;
                }
            }
            if (headInfo.isAvg()) {
                topRow.createCell(colIdx).setCellValue(headInfo.getHeading());
                ArrayList<Double> dones = new ArrayList<>();
                for (Map.Entry<Integer, HashMap<String, Double>> trialEntry : data.entrySet()) {
                    if (dones.contains(trialEntry.getValue().get(AnimalId))) {
                        break;
                    } else {
                        dones.add(trialEntry.getValue().get(AnimalId));
                    }
                    Row row = rows.get(rowIdx);
                    //TODO: AnimalID 
                    Double dataEntry = getAverage(data, trialEntry.getValue().get(AnimalId), trialEntry.getValue().get("Date"), head);
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

    private Set getAllAnimals(HashMap<Integer, HashMap<String, Double>> data) {
        Set ret = new LinkedHashSet();

        for (Map.Entry<Integer, HashMap<String, Double>> trialEntry : data.entrySet()) {
            ret.add(trialEntry.getValue().get(AnimalId));
        }
        return ret;
    }

    private Double findValue(HashMap<Integer, HashMap<String, Double>> data, String head, double animal, double day) {

        for (Map.Entry<Integer, HashMap<String, Double>> trialEntry : data.entrySet()) {
            if (trialEntry.getValue().get(AnimalId).equals(animal) && trialEntry.getValue().get("Date").equals(day)) {
                return trialEntry.getValue().get(head);
            }
        }
        return null;
    }

    private void createXlsx(Workbook wb) {
        // Write the output to a file
        FileOutputStream fileOut = null;

        try {

            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XLSX files (.xlsx)", ".xlsx");
            fileChooser.getExtensionFilters().add(extFilter);

            fileOut = new FileOutputStream(fileChooser.showSaveDialog(null).getAbsolutePath());
            wb.write(fileOut);
        } catch (IOException ex) {
            Logger.getLogger(LoadAndParse.class
                    .getName()).log(Level.SEVERE, null, ex);
            alertError(ex, "Error creating/editing the file! Is your computer using the file?");
        } catch (NullPointerException ex) {
            alertError(ex, "Did you close without saving template?");
        } finally {
            try {
                // Closing the stream and workbook                
                fileOut.close();
                wb.close();
            } catch (IOException | NullPointerException ex) {
                Logger.getLogger(LoadAndParse.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private double getAverage(HashMap<Integer, HashMap<String, Double>> data, double id, double day, String head) {
        ArrayList<Double> values = new ArrayList<>();
        for (Map.Entry<Integer, HashMap<String, Double>> trialEntry : data.entrySet()) {
            if (trialEntry.getValue().get(AnimalId).equals(id) && trialEntry.getValue().get("Date").equals(day) && trialEntry.getValue().get(head) != null) {
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

    public static void alertError(Exception err, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message + " (Full stack below...) \n\n" + err.getMessage());
        alert.show();
    }
}
