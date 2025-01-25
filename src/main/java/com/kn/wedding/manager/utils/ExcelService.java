package com.kn.wedding.manager.utils;

import lombok.SneakyThrows;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

@Service
public class ExcelService {

    @SneakyThrows
    public <T> ResponseEntity<byte[]> generateExcel(List<T> dataset, Class<T> type, String fileName){
        if (CollectionUtils.isEmpty(dataset)) throw new IllegalArgumentException("The list cannot be null or empty");

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("responses");

        Field[] fields = type.getDeclaredFields();

        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < fields.length; i++) {
            fields[i].setAccessible(true);
            headerRow.createCell(i).setCellValue(fields[i].getName());
        }

        int rowIndex = 1;
        for (T obj : dataset) {
            Row row = sheet.createRow(rowIndex++);
            for (int i = 0; i < fields.length; i++) {
                try {
                    Object value = fields[i].get(obj);
                    row.createCell(i).setCellValue(!ObjectUtils.isEmpty(value) ? value.toString() : "");
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Failed to access field value: " + fields[i].getName(), e);
                }
            }
        }

        for (int i = 0; i < fields.length; i++) {
            sheet.autoSizeColumn(i);
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();

        byte[] excelData = outputStream.toByteArray();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=%s.xlsx".formatted(fileName));
        headers.add("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        return new ResponseEntity<>(excelData, headers, HttpStatus.OK);
    }
}