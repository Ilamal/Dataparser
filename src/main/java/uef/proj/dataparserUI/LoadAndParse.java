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
 import org.apache.poi.ss.usermodel.DataFormat;

/**
 *
 * @author UEF Projektityö 2019 - Tony Heikkilä, Ilari Malinen, Mikko Nygård, Toni Takkinen
 * Ilamal, mikkonyg, Tonhei, Tontak
 * @version 1.0
 */
public class LoadAndParse {

    // Create a DataFormatter to format and get each cell's value as String
    private final DataFormatter dataFormatter = new DataFormatter();

    private final String AnimalId = "animalid";

    private Workbook statisticsWb; // DATA

    private Workbook trialListWb; // DATES

    /**
     *
     * @param probeFile Data file from user
     * @param trialListFile Data file from user
     */
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

    /**
     * Returns an ArrayList that contains all headings from the source file.
     *
     * @return {@literal ArrayList<String> } headers
     */
    ArrayList getAllHeaders() {

        ArrayList<String> headers = new ArrayList();
        int a = 2;
        Cell cellData = null;
        do {
            String heading = "";

            for (int i = 0; i < 4; i++) {

                Row row = statisticsWb.getSheetAt(0).getRow(i);
                if (dataFormatter.formatCellValue(row.getCell(a)).equals("") && i == 0) {
                    cellData = null;
                    break;
                } else {
                    cellData = row.getCell(a);
                    heading += dataFormatter.formatCellValue(cellData) + "\n";
                }
            }
            if (heading != "") {
                headers.add(heading);
            }

            a++;
        } while (cellData != null);

        return headers;
    }

    /**
     * Reads source file's data and adds that data to {@literal HashMap<animalID,
     * HashMap<heading, value>}.
     *
     * @param headingsInfo {@literal ArrayList<HeaderInfo>} containing the data
     * from headingsInfo.
     *
     * @return {@literal HashMap<animalID, HashMap<heading, value>}
     */
    public HashMap<Integer, HashMap<String, Double>> readData(ArrayList<HeaderInfo> headingsInfo) {
        // Read the workbook and add necessary data to HashMap according to the headings list
        HashMap<Integer, HashMap<String, Double>> hashmap = new HashMap<>();
        // Read the headings
        ArrayList<String> headings = new ArrayList();
        headingsInfo.forEach(e -> headings.add(e.getHeading()));
        HashMap<Integer, Integer> dates = readDateFromTrialList();
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
            Double day = new Double(dates.get(trialn));
            temp.put("Date", day);

            for (int i = 2; i < row.getLastCellNum(); i++) {

                Cell cell = row.getCell(i);
                String cellValue = dataFormatter.formatCellValue(cell);
                try {
                    temp.put(headings.get(i - 2), Double.valueOf(cellValue.replace(",", ".")));
                } catch (NumberFormatException ex) {
                    temp.put(headings.get(i - 2), null);
                }

            }
        }
        return hashmap;
    }

    /**
     * Read trialList file containing trials and which day trial was done.
     *
     * @return {@literal HashMap<Integer, Integer> } where the first Integer is
     * trialNumber and the second Integer is trialDay
     */
    HashMap readDateFromTrialList() {

        HashMap<Integer, Integer> dates = new HashMap<>();

        int idx = 1;
        Row row = trialListWb.getSheetAt(0).getRow(idx);
        while (row != null) {
            int trialNumber = Integer.valueOf(dataFormatter.formatCellValue(row.getCell(0)).replaceAll("\\D", ""));
            int trialDay = Integer.valueOf(dataFormatter.formatCellValue(row.getCell(5)));
            dates.put(trialNumber, trialDay);
            row = trialListWb.getSheetAt(0).getRow(++idx);
        }

        return dates;
    }

    /**
     * Creates rows to Excel workbook sheet.
     *
     * @param sheet Excel workbook sheet
     * @param amount The amount of rows generated
     * @return {@literal ArrayList<Row> } rows
     */
    private ArrayList<Row> getRows(Sheet sheet, int amount) {
        ArrayList<Row> rows = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            rows.add(sheet.createRow(i));
        }
        return rows;
    }

    /**
     * Adds data to the new workbook from the source file and calls createXlsx
     * method.
     *
     * @param headingsInfo Contains the headings
     * @param data Contains the numeric values
     */
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
        
        Set<Double> getAnims = getAllAnimals(data);
        Double[] allAnims = getAnims.toArray(new Double[0]);
        ArrayList<Row> rows = getRows(sheet, allAnims.length + 5);

        // Add Headings
        Row topRow = sheet.createRow(0);
        Cell aniCell = topRow.createCell(0);
        aniCell.setCellValue("AnID_1");
        int it = 1;
        // Add animalnumbers
        for (Object ani : allAnims) {
            rows.get(it).createCell(0).setCellValue((Double) ani);
            it++;
        }
        int colIdx = 1;
        // Create cells
        for (String head : headings) {
            HeaderInfo headInfo = headingsInfo.get(headings.indexOf(head));
            int rowIdx = 1;
            if (headInfo.isNormal()) {
                Double day = 0.0;
                int swim = 1;
                HashMap<Double, List<Integer>> dones = getAnimDones(allAnims);
                int donesLength = calculateAllArrs(dones);
                while (true) { // same heading different days

                    if (donesLength == calculateAllArrs(dones)) {
                        swim = 1;
                        day = findNextDay(data, day, head);
                        rowIdx = 1;
                        if (day == null) // All end
                        {
                            // Clear last row
                            for(int i = 0; i <= allAnims.length; i++) {
                                rows.get(i).createCell(colIdx);
                            }
                            topRow.createCell(colIdx);
                            break;
                        }
                    } else {
                        swim++;
                        colIdx++;
                        rowIdx = 1;
                    }

                    donesLength = calculateAllArrs(dones);
                    for (Double anim : allAnims) {
                        // Write heading
                        topRow.createCell(colIdx).setCellValue(headInfo.getAlias() + "_" + day.intValue() + "_" + swim);
                        // Find animal + date + heading and insert there
                        Row row = rows.get(rowIdx);
                        Map.Entry<Integer, HashMap<String, Double>> trial = findValue(data, head, anim, day, dones.get(anim));

                        if (trial != null) {
                            Double dataEntry = trial.getValue().get(head);
                            dones.get(anim).add(trial.getKey());
                            Cell cell = row.createCell(colIdx);
                            if (dataEntry != null) {                                
                                writeNumCell(dataEntry, cell, workbook);
                            } else {
                                cell.setCellValue("-");
                            }
                        } else {
                            Cell cell = row.createCell(colIdx);
                            cell.setCellValue("-");
                        }
                        rowIdx++;
                    }
                }
            }
            if (headInfo.isAvg()) {
                Double day = findNextDay(data, 0.0, head);
                while (day != null) {
                    topRow.createCell(colIdx).setCellValue(headInfo.getAlias() + "_" + day.intValue());
                    for (Double anim : allAnims) {
                        // Loop the anim averages for the day
                        Row row = rows.get(rowIdx);
                        Double dataEntry = getAverage(data, anim, day, head);
                        Cell cell = row.createCell(colIdx);
                        if (dataEntry == null) {
                            cell.setCellValue("-");
                        } else {                            
                            writeNumCell(dataEntry, cell, workbook);
                        }
                        rowIdx++;
                    }
                    // Change the day
                    day = findNextDay(data, day, head);
                    colIdx++;
                    rowIdx = 1;
                }
            }

        }
        // Auto size columns
        for (int i = 0; i < sheet.getRow(0).getPhysicalNumberOfCells(); i++) {
            sheet.autoSizeColumn(i);
        }
        createXlsx(workbook);
    }
    /**
     * Writes the given value to the cell in a decimal format
     * 
     * @Param value Double to be inserted into the cell
     * @Param cell the cell to insert the value in
     * @Param workbook workbook containing the cell to generate styles
     */
    private void writeNumCell(Double value, Cell cell, Workbook workbook) {
        DataFormat format = workbook.createDataFormat();
        CellStyle style = workbook.createCellStyle();
        style.setDataFormat(format.getFormat("0.00############"));
        cell.setCellStyle(style);
        cell.setCellType(CellType.NUMERIC);
        cell.setCellValue(value);
    }
    /**
     * Returns the next day
     *
     * @param data whole data from statistics file
     * @param prevDay the previous day
     * @param head
     * @return
     */
    private Double findNextDay(HashMap<Integer, HashMap<String, Double>> data, Double prevDay, String head) {
        Double large = 999999.9;
        Double nextDay = large;
        for (Map.Entry<Integer, HashMap<String, Double>> trialEntry : data.entrySet()) {
            if (trialEntry.getValue().containsKey(head)) {
                Double day = trialEntry.getValue().get("Date");
                if (day > prevDay && day < nextDay) {
                    nextDay = day;
                }
            }
        }
        if (nextDay < large) {
            return nextDay;
        } else {
            return null;
        }
    }

    /**
     *
     *
     * @param data HashMap that contains key (animalID) and value (heading,
     * value)
     * @return LinkedHashSet
     */
    private Set getAllAnimals(HashMap<Integer, HashMap<String, Double>> data) {
        Set uniqueAnimals = new LinkedHashSet();

        for (Map.Entry<Integer, HashMap<String, Double>> trialEntry : data.entrySet()) {
            uniqueAnimals.add(trialEntry.getValue().get(AnimalId));
        }
        return uniqueAnimals;
    }

    /**
     * Finds value from data where header, animal id and date match the
     * parameters and is not contained in the dones list. If non found returns
     * null.
     *
     * @param data HashMap that contains key (animalID) and value (heading,
     * value)
     * @param head String value of heading
     * @param animal Numeric value of animalID
     * @param day Numeric value of trial day
     * @param dones Values not to include in the search
     * @return Heading or null
     */
    private Map.Entry<Integer, HashMap<String, Double>> findValue(HashMap<Integer, HashMap<String, Double>> data, String head, double animal, double day, List dones) {

        for (Map.Entry<Integer, HashMap<String, Double>> trialEntry : data.entrySet()) {
            if (trialEntry.getValue().get(AnimalId).equals(animal) && trialEntry.getValue().get("Date").equals(day) && trialEntry.getValue().containsKey(head)) {
                if (!dones.contains(trialEntry.getKey())) {
                    return trialEntry;
                }
            }
        }
        return null;
    }

    /**
     * Generate fitting hashmap to use with animal ids to track the done animal
     * trials.
     *
     * @param anims
     * @return {@literal HashMap<Double, List<Integer>> } containing tests per
     * unique animal.
     */
    private HashMap<Double, List<Integer>> getAnimDones(Double[] anims) {
        HashMap<Double, List<Integer>> dones = new HashMap();
        for (Double anim : anims) {
            dones.put(anim, new ArrayList<>());
        }
        return dones;
    }

    /**
     * Calculates the amount of elements in hashmap of lists.
     *
     * @param arrs
     * @return unique animalIDs
     */
    private int calculateAllArrs(HashMap<Double, List<Integer>> arrs) {
        int uniqueAnimals = 0;
        uniqueAnimals = arrs.entrySet().stream().map((trialEntry) -> trialEntry.getValue().size()).reduce(uniqueAnimals, Integer::sum);
        return uniqueAnimals;
    }

    /**
     * Creates Excel file from workbook and let's the user select saving
     * location.
     *
     * @param wb Workbook containing the data
     */
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
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "File succesfully created on your chosen location!");
                alert.setTitle("Success");
                alert.show();
            } catch (IOException | NullPointerException ex) {
                Logger.getLogger(LoadAndParse.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Returns Average value of one animals one day tests of a certain heading.
     *
     * @param data
     * @param id A number value of animalID
     * @param day A day of trial
     * @param head Heading information in String
     * @return Calls the method that calculates the average of values
     */
    private Double getAverage(HashMap<Integer, HashMap<String, Double>> data, double id, double day, String head) {
        ArrayList<Double> values = new ArrayList<>();
        for (Map.Entry<Integer, HashMap<String, Double>> trialEntry : data.entrySet()) {
            if (trialEntry.getValue().get(AnimalId).equals(id) && trialEntry.getValue().get("Date").equals(day) && trialEntry.getValue().get(head) != null) {
                values.add(trialEntry.getValue().get(head));
            }
        }
        if (values.size() > 0) {
            return calculateAverage(values);
        } else {
            return null;
        }
    }

    /**
     *
     * @param marks
     * @return Average of given values
     */
    private static double calculateAverage(List<Double> marks) {
        Double sum = 0.0;

        if (marks != null && !marks.isEmpty()) {
            sum = marks.stream().map((mark) -> mark).reduce(sum, (accumulator, _item) -> accumulator + _item);
            return sum / marks.size();
        }
        return sum;
    }

    /**
     * Alerts the user with a message box.
     *
     * @param err Exception type
     * @param message Error Message String
     */
    public static void alertError(Exception err, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message + " (Full stack below...) \n\n" + err.getMessage());
        alert.show();
    }
}
