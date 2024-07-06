package org.guanzon.autoapp.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

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
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet();

        createHeaderRow(sheet, headers);
        fillDataRows(sheet, headers, data);

        try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
            workbook.write(fileOut);
        }

        workbook.close();
    }

    /**
     * Creates the header row in the given sheet using the provided headers.
     *
     * @param sheet the sheet in which the header row will be created
     * @param headers a list of headers for the columns in the Excel file
     */
    private void createHeaderRow(Sheet sheet, List<String> headers) {
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.size(); i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers.get(i));
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
}
