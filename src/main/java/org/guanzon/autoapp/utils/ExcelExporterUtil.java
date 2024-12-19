package org.guanzon.autoapp.utils;

import java.io.File;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import javafx.stage.FileChooser;
import org.guanzon.appdriver.agent.ShowMessageFX;

/**
 * A utility class for exporting data to an Excel file.
 *
 * <p>
 * This class provides methods to export data to an Excel file with specified
 * headers and data rows. It uses the Apache POI library to create and write the
 * Excel file.</p>
 *
 * @version 1.0, 05/14/2024
 */
public class ExcelExporterUtil {

    /**
     * Exports the given data to an Excel file at the specified file path.
     *
     * @param filePath the path where the Excel file will be saved
     * @param headers a list of headers for the columns in the Excel file
     * @param data a list of maps, where each map represents a row of data with
     * the keys corresponding to the headers
     * @throws IOException if an I/O error occurs while writing the Excel file
     */
    public void exportData(String filePath, List<String> headers, List<Map<String, Object>> data) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet();
            // Create header row
            createHeaderRow(sheet, headers, workbook);
            // Fill data rows
            fillDataRows(sheet, headers, data);
            // Adjust column width based on content
            for (int i = 0; i < headers.size(); i++) {
                sheet.autoSizeColumn(i);  // Auto-size each column

            }
            try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                workbook.write(fileOut);
            }
        }
    }

    /**
     * Creates the header row in the given sheet using the provided headers.
     *
     * @param sheet the sheet in which the header row will be created
     * @param headers a list of headers for the columns in the Excel file
     */
    private void createHeaderRow(Sheet foSheet, List<String> fsHeaders, Workbook foWorkBook) {
        Row headerRow = foSheet.createRow(0);

        // Create a bold font for the header
        Font headerFont = foWorkBook.createFont();
        headerFont.setBold(true);
        headerFont.setColor(IndexedColors.WHITE.getIndex());
        // Create a cell style for the header
        CellStyle headerCellStyle = foWorkBook.createCellStyle();
        headerCellStyle.setFillForegroundColor(IndexedColors.OLIVE_GREEN.getIndex());
        headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerCellStyle.setFont(headerFont);
        headerCellStyle.setAlignment(HorizontalAlignment.CENTER); // Center text horizontally
        headerCellStyle.setVerticalAlignment(VerticalAlignment.CENTER); // Center text vertically

        // Set header row
        for (int i = 0; i < fsHeaders.size(); i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(fsHeaders.get(i));
            cell.setCellStyle(headerCellStyle); // Apply the style to the header cells
        }
    }

    /**
     * Fills the data rows in the given sheet using the provided headers and
     * data.
     *
     * @param sheet the sheet in which the data rows will be filled
     * @param headers a list of headers for the columns in the Excel file
     * @param data a list of maps, where each map represents a row of data with
     * the keys corresponding to the headers
     */
    private void fillDataRows(Sheet sheet, List<String> headers, List<Map<String, Object>> data) {
        int rowNum = 1;
        for (Map<String, Object> rowData : data) {
            Row row = sheet.createRow(rowNum++);
            for (int i = 0; i < headers.size(); i++) {
                Cell cell = row.createCell(i);
                Object value = rowData.get(headers.get(i));
                cell.setCellValue(value != null ? value.toString() : "");
            }
        }
    }

    /**
     * Exports the given data to an Excel file using a FileChooser dialog for
     * saving the file. The file is saved with the provided title as part of the
     * filename and formatted with headers.
     *
     * @param fsTitleHeaders the list of headers for the columns in the Excel
     * file
     * @param foData a list of maps, where each map represents a row of data
     * with the keys corresponding to the headers
     * @param fsTitleName the title used for the default filename and dialog box
     */
    public static void exportDataToExcel(List<String> fsTitleHeaders, List<Map<String, Object>> foData, String fsTitleName) {
        ExcelExporterUtil exporter = new ExcelExporterUtil();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Excel File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));

        String defaultFileName = fsTitleName.replace(" ", "_") + "_Data" + ".xlsx";
        fileChooser.setInitialFileName(defaultFileName);

        // Show the Save dialog
        File file = fileChooser.showSaveDialog(null);

        if (file != null) {
            try {
                exporter.exportData(file.getAbsolutePath(), fsTitleHeaders, foData);
                System.out.println("Data exported to " + file.getAbsolutePath());
                ShowMessageFX.Information(null, fsTitleName, "Exported in Excel Successfully!");
            } catch (IOException e) {
            }
        } else {
            ShowMessageFX.Warning(null, fsTitleName, "Exporting in Excel Failed!");
        }
    }

}
