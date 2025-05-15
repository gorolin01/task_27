package org.pack;

import java.util.ArrayList;

public class Cluster {

    private ArrayList<Point> arr_points;
    private Center center;

    Cluster (ArrayList<Point> arr_points, Center center) {

        this.arr_points = new ArrayList<>(arr_points);
        this.center = center;

    }

    public ArrayList<Point> getArr_points() {
        return arr_points;
    }

    public Center getCenter() {
        return center;
    }

    public void setCenter(Center center) {
        this.center = center;
    }
}
