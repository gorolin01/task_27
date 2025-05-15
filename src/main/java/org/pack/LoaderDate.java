package org.pack;

import java.util.ArrayList;

public class LoaderDate {

    private ArrayList<Point> arr_points;

    public ArrayList<Point> getArr_points() {
        return arr_points;
    }

    LoaderDate (String url) {

        arr_points = new ArrayList<>();
        Excel file_excel = new Excel();
        file_excel.createExcel(url);

        for (int row = 0; row <= file_excel.getLastRowNum(); row++) {
            double x = Double.parseDouble(file_excel.getCell(row, 0).toString());
            double y = Double.parseDouble(file_excel.getCell(row, 1).toString());
            Point point = new Point(x, y);
            arr_points.add(point);
        }

    }

}
