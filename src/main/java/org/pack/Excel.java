package org.pack;

import com.sun.media.sound.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Excel {

    private String filename;
    private Integer Sheet;
    private Integer Row;
    private Integer Column;
    private XSSFWorkbook WorkbookExcel;
    private XSSFSheet SheetExcel;
    private XSSFRow RowExcel;
    private XSSFCell CellExcel;

    public void createExcel(){

        WorkbookExcel = new XSSFWorkbook();
        SheetExcel = WorkbookExcel.createSheet("1");    //пока только умеет работать с одним листом

    }

    public void createExcel(String filename, Integer Sheet){    //работа с уже созданной книгой

        this.filename = filename;
        this.Sheet = Sheet;
        WorkbookExcel = openBookDirectly(filename);
        SheetExcel = WorkbookExcel.getSheetAt(Sheet);

    }

    // создает файл без указания листа (по умолчанию лист №1)
    public void createExcel(String filename){    //работа с уже созданной книгой

        this.filename = filename;
        WorkbookExcel = openBookDirectly(filename);
        SheetExcel = WorkbookExcel.getSheetAt(0);

    }

    // получить ячейку на указанном листе
    public XSSFCell getCell(Integer Row, Integer Column, Integer Sheet){

        SheetExcel = WorkbookExcel.getSheetAt(Sheet);
        this.Row = Row;
        this.Column = Column;

        RowExcel = SheetExcel.getRow(Row);
        if(RowExcel == null) { RowExcel = SheetExcel.createRow(Row); }

        CellExcel = RowExcel.getCell(Column);
        if(CellExcel == null) { CellExcel = RowExcel.createCell(Column); }

        return CellExcel;
    }

    public XSSFCell getCell(Integer Row, Integer Column){

        this.Row = Row;
        this.Column = Column;

        RowExcel = SheetExcel.getRow(Row);
        if(RowExcel == null) { RowExcel = SheetExcel.createRow(Row); }

        CellExcel = RowExcel.getCell(Column);
        if(CellExcel == null) { CellExcel = RowExcel.createCell(Column); }

        return CellExcel;
    }

    public void setCell(Integer Row, Integer Column, String data){

        getCell(Row, Column);

        CellExcel.setCellValue(data);

    }

    public void setCell(Integer Row, Integer Column, Integer data){

        getCell(Row, Column);

        CellExcel.setCellValue(data);

    }

    //получить номер последней заполненной строки
    public int getLastRowNum(){

        return SheetExcel.getLastRowNum();

    }

    public int getLastRowNum(Integer Sheet){

        //SheetExcel = WorkbookExcel.getSheetAt(Sheet);
        return WorkbookExcel.getSheetAt(Sheet).getLastRowNum();

    }

    public void Build(String outFilename){

        createBookDirectly(WorkbookExcel, outFilename);

    }

    public static void autoSizeColumn(XSSFWorkbook book) {
        // Применяем автоподгон размера ячеек для всех листов и колонок
        for (int i = 0; i < book.getNumberOfSheets(); i++) {
            XSSFSheet sheet = book.getSheetAt(i);
            for (int col = 0; col < sheet.getRow(0).getLastCellNum(); col++) {
                sheet.autoSizeColumn(col);
            }
        }
    }

    private static void createBookDirectly(XSSFWorkbook book, String NEW_FILE_NAME) {

        autoSizeColumn(book);   // автовыравнивание всех столбцов

        FileOutputStream fileOut = null;
        try {
            fileOut = new FileOutputStream(new File(NEW_FILE_NAME));
            book.write(fileOut);
            fileOut.flush();
            fileOut.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static XSSFWorkbook openBookDirectly(String filename) {

        XSSFWorkbook book = null;
        try {
            //book = (XSSFWorkbook) WorkbookFactory.create(new File(filename)); //эта ебаторика не работает в собранном файле!!!
            book = new XSSFWorkbook(new File(filename));
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        } catch (IOException | org.apache.poi.openxml4j.exceptions.InvalidFormatException e) {
            e.printStackTrace();
        }
        return book;
    }

    public String mergeExcelFiles(String inputDir, String outputDir) {
        Map<String, Integer> combinedData = new HashMap<>();
        String outputFilePath = Paths.get(outputDir, getCurrentDateTime() + ".xlsx").toString();

        try {
            Files.list(Paths.get(inputDir))
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".xlsx"))
                    .forEach(filePath -> {
                        try (FileInputStream fis = new FileInputStream(filePath.toFile());
                             XSSFWorkbook workbook = new XSSFWorkbook(fis)) {

                            XSSFSheet sheet = workbook.getSheetAt(0); // Предполагаем, что данные на первом листе
                            for (int i = 0; i <= sheet.getLastRowNum(); i++) {  // если добавим шапку в обычный отчет, то i = 1
                                XSSFRow row = sheet.getRow(i);
                                if (row != null) {
                                    String article = row.getCell(0) != null ? row.getCell(0).getStringCellValue() : "";
                                    String name = row.getCell(1).getStringCellValue();
                                    int quantity = (int) row.getCell(2).getNumericCellValue();
                                    String key = article + "|" + name; // Составной ключ для уникальности

                                    combinedData.put(key, combinedData.getOrDefault(key, 0) + quantity);
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });

            // Создаем новый файл
            XSSFWorkbook newWorkbook = new XSSFWorkbook();
            XSSFSheet newSheet = newWorkbook.createSheet("Combined Data");

            // Добавляем заголовок
            XSSFRow headerRow = newSheet.createRow(0);
            headerRow.createCell(0).setCellValue("Артикул");
            headerRow.createCell(1).setCellValue("Наименование");
            headerRow.createCell(2).setCellValue("Количество");

            // Добавляем данные
            int rowNum = 1;
            for (Map.Entry<String, Integer> entry : combinedData.entrySet()) {
                XSSFRow row = newSheet.createRow(rowNum++);
                String[] keyParts = entry.getKey().split("\\|");
                row.createCell(0).setCellValue(keyParts[0]);
                row.createCell(1).setCellValue(keyParts[1]);
                row.createCell(2).setCellValue(entry.getValue());
            }

            autoSizeColumn(newWorkbook);

            // Записываем новый файл
            try (FileOutputStream fos = new FileOutputStream(new File(outputFilePath))) {
                newWorkbook.write(fos);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return outputFilePath;
    }

    // удалить все excel файлы из директории
    public void removeAllExcelFileFromDirectory(String pathDir) {
        try {
            Files.list(Paths.get(pathDir))
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".xlsx"))
                    .forEach(filePath -> {
                        try {
                            Files.delete(filePath);
                            System.out.println("Deleted: " + filePath);
                        } catch (IOException e) {
                            System.out.println("Failed to delete: " + filePath);
                            e.printStackTrace();
                        }
                    });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // возвращает отформатированную дату и время
    public static String getCurrentDateTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        String DateTime = formatter.format(date);
        DateTime = DateTime.replaceAll("/", "-").replaceAll(":", ";");
        return DateTime;
    }

}
