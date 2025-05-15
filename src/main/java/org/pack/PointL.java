package org.pack;

// точка + суммарная длина от этой точки ко всем точкам кластера
public class PointL {

    Point center;
    double length;

    PointL (Point center, double length) {

        this.center = center;
        this.length = length;

    }

    public Point getCenter() {
        return center;
    }

    public double getLength() {
        return length;
    }

}
