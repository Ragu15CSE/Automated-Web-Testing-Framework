package com.automation.framework.library;

import com.automation.framework.constants.FrameworkConstants;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

/**
 * Robust Apache POI Excel Data Reader.
 * Reads test data dynamically into Key-Value Map structures, DataProviders, or individual cells.
 */
public final class ExcelDataReader {

    private static final DataFormatter FORMATTER = new DataFormatter();

    private ExcelDataReader() {}

    /**
     * Reads all test data from a given sheet and returns a list of row maps.
     * The first row is treated as the column headers (keys).
     *
     * @param excelFilePath Absolute or relative path to the Excel file (.xlsx or .xls)
     * @param sheetName     Name of the sheet to read
     * @return List of Maps containing column header to cell value mappings
     */
    public static List<Map<String, String>> getData(String excelFilePath, String sheetName) {
        List<Map<String, String>> dataList = new ArrayList<>();
        File file = new File(excelFilePath);

        if (!file.exists()) {
            LoggerUtil.error("Excel file does not exist at: " + excelFilePath);
            return dataList;
        }

        try (FileInputStream fis = new FileInputStream(file);
             Workbook workbook = WorkbookFactory.create(fis)) {

            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                LoggerUtil.warn("Sheet '" + sheetName + "' was not found in " + excelFilePath);
                return dataList;
            }

            int rowCount = sheet.getLastRowNum();
            if (rowCount < 1) {
                LoggerUtil.warn("Sheet '" + sheetName + "' contains no data rows.");
                return dataList;
            }

            Row headerRow = sheet.getRow(0);
            int columnCount = headerRow.getLastCellNum();

            // Extract Header Names
            List<String> headers = new ArrayList<>();
            for (int col = 0; col < columnCount; col++) {
                Cell cell = headerRow.getCell(col, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                headers.add(FORMATTER.formatCellValue(cell).trim());
            }

            // Iterate Rows
            for (int r = 1; r <= rowCount; r++) {
                Row currentRow = sheet.getRow(r);
                if (currentRow == null || isRowEmpty(currentRow, columnCount)) {
                    continue; // Skip empty rows
                }

                Map<String, String> rowMap = new LinkedHashMap<>();
                for (int c = 0; c < columnCount; c++) {
                    String header = headers.get(c);
                    Cell cell = currentRow.getCell(c, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    String cellValue = FORMATTER.formatCellValue(cell).trim();
                    rowMap.put(header, cellValue);
                }
                dataList.add(rowMap);
            }

            LoggerUtil.info("Successfully loaded " + dataList.size() + " test data rows from sheet: " + sheetName);

        } catch (IOException e) {
            LoggerUtil.error("Error reading Excel data from: " + excelFilePath, e);
            throw new RuntimeException("Failed to read Excel data", e);
        }

        return dataList;
    }

    /**
     * Reads all test data from the default framework test data sheet.
     *
     * @param sheetName Sheet name to read from default TestData.xlsx
     * @return List of Maps containing key-value row data
     */
    public static List<Map<String, String>> getDefaultData(String sheetName) {
        return getData(FrameworkConstants.TEST_DATA_EXCEL_PATH, sheetName);
    }

    /**
     * Gets a single test data row mapped to a test case identifier (e.g. TestID / TestCaseName).
     *
     * @param sheetName   Sheet name
     * @param keyColumn   Header name of key column (e.g. "TestCaseId")
     * @param keyValue    Value to match (e.g. "TC_001")
     * @return Map containing test data for the matched row, or empty map
     */
    public static Map<String, String> getTestDataByCaseId(String sheetName, String keyColumn, String keyValue) {
        List<Map<String, String>> allData = getDefaultData(sheetName);
        for (Map<String, String> row : allData) {
            if (row.containsKey(keyColumn) && row.get(keyColumn).equalsIgnoreCase(keyValue)) {
                return row;
            }
        }
        LoggerUtil.warn("No test data found for Key [" + keyColumn + " = " + keyValue + "] in sheet: " + sheetName);
        return Collections.emptyMap();
    }

    /**
     * Reads a specific cell value by row index (1-based) and column header name.
     *
     * @param sheetName   Sheet name
     * @param rowIndex    Row index (1-based, where 1 is the first data row)
     * @param columnName  Column header name
     * @return Cell value as String
     */
    public static String getCellValue(String sheetName, int rowIndex, String columnName) {
        List<Map<String, String>> data = getDefaultData(sheetName);
        if (rowIndex > 0 && rowIndex <= data.size()) {
            return data.get(rowIndex - 1).getOrDefault(columnName, "");
        }
        LoggerUtil.warn("Row index out of bounds: " + rowIndex + " for sheet: " + sheetName);
        return "";
    }

    /**
     * Check if an Excel row is completely empty.
     */
    private static boolean isRowEmpty(Row row, int columnCount) {
        for (int c = 0; c < columnCount; c++) {
            Cell cell = row.getCell(c);
            if (cell != null && cell.getCellType() != CellType.BLANK && !FORMATTER.formatCellValue(cell).trim().isEmpty()) {
                return false;
            }
        }
        return true;
    }
}
