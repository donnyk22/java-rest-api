package com.github.donnyk22.services.excel;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.github.donnyk22.exceptions.BadRequestException;
import com.github.donnyk22.models.dtos.FindResponse;
import com.github.donnyk22.models.dtos.MstStudentsDto;
import com.github.donnyk22.models.enums.TimeFormat;
import com.github.donnyk22.models.enums.UserGender;
import com.github.donnyk22.models.forms.students.StudentsFindForm;
import com.github.donnyk22.services.school.SchoolService;
import com.github.donnyk22.utils.ConverterUtil;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@Service
@RequiredArgsConstructor
public class ExcelServiceImpl implements ExcelService {

    private final SchoolService schoolService;
    private final ConverterUtil converterUtil;

    @Override
    @Transactional(readOnly = true)
    @SneakyThrows
    public byte[] exportToNewExcel(StudentsFindForm form) {
        StudentsFindForm param = new StudentsFindForm()
                .setAcademicYear(form.getAcademicYear());
        param.setPage(0)
                .setSize(Integer.MAX_VALUE)
                .setKeyword(form.getKeyword());

        FindResponse<MstStudentsDto> response = schoolService.findStudents(param);

        // Create Excel workbook
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Students");

            // Create header style
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setFontHeightInPoints((short) 12);
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            headerStyle.setBorderBottom(BorderStyle.THIN);
            headerStyle.setBorderTop(BorderStyle.THIN);
            headerStyle.setBorderLeft(BorderStyle.THIN);
            headerStyle.setBorderRight(BorderStyle.THIN);

            // Create header row
            Row headerRow = sheet.createRow(0);
            String[] headers = { "ID", "Full Name", "Gender", "Address", "Phone", "Created At", "Updated At" };
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // Create data style
            CellStyle dataStyle = workbook.createCellStyle();
            dataStyle.setBorderBottom(BorderStyle.THIN);
            dataStyle.setBorderTop(BorderStyle.THIN);
            dataStyle.setBorderLeft(BorderStyle.THIN);
            dataStyle.setBorderRight(BorderStyle.THIN);

            // Fill data rows
            int rowNum = 1;
            for (MstStudentsDto student : response.getRecords()) {
                Row row = sheet.createRow(rowNum++);

                createCell(row, 0, student.getId() != null ? student.getId().toString() : "", dataStyle);
                createCell(row, 1, student.getFullName() != null ? student.getFullName() : "", dataStyle);
                createCell(row, 2, student.getGender() != null ? UserGender.getVal(student.getGender()) : "",
                        dataStyle);
                createCell(row, 3, student.getAddress() != null ? student.getAddress() : "", dataStyle);
                createCell(row, 4, student.getPhone() != null ? student.getPhone() : "", dataStyle);
                createCell(row, 5,
                        student.getCreatedAt() != null
                                ? converterUtil.offsetDateTimeToString(student.getCreatedAt(),
                                        TimeFormat.DD_MM_YYYY_HH_MM_SS)
                                : "",
                        dataStyle);
                createCell(row, 6,
                        student.getUpdatedAt() != null
                                ? converterUtil.offsetDateTimeToString(student.getUpdatedAt(),
                                        TimeFormat.DD_MM_YYYY_HH_MM_SS)
                                : "",
                        dataStyle);
            }

            // Auto-size columns
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // Write to byte array
            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                workbook.write(outputStream);
                return outputStream.toByteArray();
            }
        }
    }

    private void createCell(Row row, int column, String value, CellStyle style) {
        Cell cell = row.createCell(column);
        cell.setCellValue(value);
        cell.setCellStyle(style);
    }

    @Override
    @Transactional(readOnly = true)
    @SneakyThrows
    public byte[] exportToExistingExcelTemplate(StudentsFindForm form) {
        StudentsFindForm param = new StudentsFindForm()
                .setAcademicYear(form.getAcademicYear());
        param.setPage(0)
                .setSize(Integer.MAX_VALUE)
                .setKeyword(form.getKeyword());

        FindResponse<MstStudentsDto> response = schoolService.findStudents(param);

        ClassPathResource resource = new ClassPathResource("templates/students-export-template.xlsx");

        try (InputStream inputStream = resource.getInputStream();
                Workbook workbook = new XSSFWorkbook(inputStream)) {

            Sheet sheet = workbook.getSheetAt(0);

            CellStyle dataStyle = workbook.createCellStyle();
            dataStyle.setBorderBottom(BorderStyle.THIN);
            dataStyle.setBorderTop(BorderStyle.THIN);
            dataStyle.setBorderLeft(BorderStyle.THIN);
            dataStyle.setBorderRight(BorderStyle.THIN);

            int rowNum = 1;
            for (MstStudentsDto student : response.getRecords()) {
                Row row = sheet.getRow(rowNum);
                if (row == null) {
                    row = sheet.createRow(rowNum);
                }
                rowNum++;

                createCell(row, 0, student.getId() != null ? student.getId().toString() : "", dataStyle);
                createCell(row, 1, student.getFullName() != null ? student.getFullName() : "", dataStyle);
                createCell(row, 2, student.getGender() != null ? UserGender.getVal(student.getGender()) : "",
                        dataStyle);
                createCell(row, 3, student.getAddress() != null ? student.getAddress() : "", dataStyle);
                createCell(row, 4, student.getPhone() != null ? student.getPhone() : "", dataStyle);
                createCell(row, 5,
                        student.getCreatedAt() != null
                                ? converterUtil.offsetDateTimeToString(student.getCreatedAt(),
                                        TimeFormat.DD_MM_YYYY_HH_MM_SS)
                                : "",
                        dataStyle);
                createCell(row, 6,
                        student.getUpdatedAt() != null
                                ? converterUtil.offsetDateTimeToString(student.getUpdatedAt(),
                                        TimeFormat.DD_MM_YYYY_HH_MM_SS)
                                : "",
                        dataStyle);
            }

            for (int i = 0; i < 7; i++) {
                sheet.autoSizeColumn(i);
            }
            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                workbook.write(outputStream);
                return outputStream.toByteArray();
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    @SneakyThrows
    public List<Map<String, Object>> readImportedExcelFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BadRequestException("File is required");
        }

        List<Map<String, Object>> excelDataList = new ArrayList<>();
        DataFormatter dataFormatter = new DataFormatter();

        try (InputStream inputStream = file.getInputStream();
                Workbook workbook = new XSSFWorkbook(inputStream)) {

            for (int s = 0; s < workbook.getNumberOfSheets(); s++) {
                Sheet sheet = workbook.getSheetAt(s);

                Map<String, Object> sheetMap = new LinkedHashMap<>();
                sheetMap.put("sheet", sheet.getSheetName());

                List<Map<String, String>> rowsList = new ArrayList<>();

                int lastRowNum = sheet.getLastRowNum();

                int maxCols = 0;
                for (int r = 0; r <= Math.min(lastRowNum, 10); r++) {
                    Row row = sheet.getRow(r);
                    if (row != null && row.getLastCellNum() > maxCols) {
                        maxCols = row.getLastCellNum();
                    }
                }

                if (lastRowNum < 0 || maxCols == 0) {
                    sheetMap.put("rows", rowsList);
                    excelDataList.add(sheetMap);
                    continue;
                }

                for (int r = 0; r <= lastRowNum; r++) {
                    Row row = sheet.getRow(r);

                    Map<String, String> rowData = new LinkedHashMap<>();
                    boolean hasData = false;

                    for (int c = 0; c < maxCols; c++) {
                        String cellValue = "";

                        if (row != null) {
                            Cell cell = row.getCell(c);
                            if (cell != null) {
                                cellValue = dataFormatter.formatCellValue(cell).trim();
                            }
                        }

                        if (!cellValue.isEmpty()) {
                            hasData = true;
                        }

                        rowData.put("columns " + (c + 1), cellValue);
                    }

                    if (hasData) {
                        rowsList.add(rowData);
                    }
                }

                sheetMap.put("rows", rowsList);
                excelDataList.add(sheetMap);
            }
        }

        return excelDataList;
    }
}