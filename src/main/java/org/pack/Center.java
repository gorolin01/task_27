package org.pack;

// точка(центр) + количество вхождений точек в кластер образованный вокруг этой точки
public class Center {

    Point center;
    int count;

    Center (Point center, int count) {

        this.center = center;
        this.count = count;

    }

    public Point getCenter() {
        return center;
    }

    public int getCount() {
        return count;
    }
}
