package org.pack;

import java.util.ArrayList;

public class Clusterization {

    private ArrayList<Point> arr_points;
    private ArrayList<Point> arr_points_no_clustering;  // хранит еще не кластерезированные точки
    private double width, height;
    private ArrayList<Cluster> arr_clusters;    // массив кластеров

    Clusterization (ArrayList<Point> arr_points, double width, double height) {

        this.arr_points = arr_points;
        this.arr_points_no_clustering = arr_points;
        this.width = width;
        this.height = height;
        arr_clusters = new ArrayList<>();

        while (!arr_points_no_clustering.isEmpty()) {
            ArrayList<Center> mass = calcAllPont();
            Center max_center = mass.get(0);
            for (int num = 1; num < mass.size(); num++) {
                if (max_center.getCount() < mass.get(num).getCount()) {
                    max_center = mass.get(num);
                }
            }
            ArrayList<Point> new_arr_cluster = calcEnter(max_center);
            arr_clusters.add(minLengthCenter(new Cluster(new_arr_cluster, max_center)));   // добавляем новый кластер, и сразу находим минимальный центр (minLengthCenter)
            arr_points_no_clustering = subtractClusters(arr_points_no_clustering, new_arr_cluster); // получаем массив точек не вошедших в новый кластер

        }

    }

    // находит и заменяет центр кластера на центр с минимальным суммарным расстоянием до каждой точки кластера
    private Cluster minLengthCenter (Cluster cluster) {

        ArrayList<PointL> arr_pointL = new ArrayList<>();

        for (int num_f = 0; num_f < cluster.getArr_points().size(); num_f++) {
            double length = 0;
            for (int num_s = 0; num_s < cluster.getArr_points().size(); num_s++) {
                length += Math.sqrt(Math.pow((cluster.getArr_points().get(num_f).getX() - cluster.getArr_points().get(num_s).getX()), 2)
                        + Math.pow((cluster.getArr_points().get(num_f).getY() - cluster.getArr_points().get(num_s).getY()), 2));
            }
            arr_pointL.add(new PointL(cluster.getArr_points().get(num_f), length));
        }

        PointL max_center = arr_pointL.get(0);
        for (int num = 0; num < arr_pointL.size(); num++) {
            if (max_center.getLength() > arr_pointL.get(num).getLength()) {
                max_center = arr_pointL.get(num);
            }
        }

        cluster.setCenter(new Center(max_center.getCenter(), cluster.getCenter().getCount()));

        return cluster;

    }

    // вычитает из массива точек A массив точек B
    private ArrayList<Point> subtractClusters (ArrayList<Point> A, ArrayList<Point> B) {

        for (int num_a = 0; num_a < A.size(); num_a++) {
            for (int num_b = 0; num_b < B.size(); num_b++) {
                if ((A.get(num_a).getX() == B.get(num_b).getX()) & (A.get(num_a).getY() == B.get(num_b).getY())) {
                    A.remove(num_a);
                    B.remove(num_b);
                    num_a--;
                    break;
                }
            }
        }

        return A;
    }

    // считает все точки как центры
    private ArrayList<Center> calcAllPont () {

        ArrayList<Center> arr_center = new ArrayList<>();

        for (int num = 0; num < arr_points_no_clustering.size(); num++) {
            arr_center.add(calcEnter(arr_points_no_clustering.get(num)));
        }

        return arr_center;

    }

    // считает число вхождений в кластер точек, относительно опорной (центральной) точки. Каждая точка становится опорной и для нее рассчитываем число вхождений.
    private Center calcEnter (Point center) {

        int count = 0;
        double x1, x2, y1, y2;

        x1 = center.getX() - (width / 2);   //left
        x2 = center.getX() + (width / 2);   //right
        y1 = center.getY() - (height / 2);  //down
        y2 = center.getY() + (height / 2);  //up

        for (int num = 0; num < arr_points_no_clustering.size(); num++) {
            if ((arr_points_no_clustering.get(num).getX() > x1) & (arr_points_no_clustering.get(num).getX() < x2)
                    & (arr_points_no_clustering.get(num).getY() > y1) & (arr_points_no_clustering.get(num).getY() < y2)) {
                count++;
            }
        }

        return new Center(center, count);

    }

    // возвращает кластер точек привязанных к центру
    private ArrayList<Point> calcEnter (Center center) {

        double x1, x2, y1, y2;
        ArrayList<Point> arr_points_in_cluster = new ArrayList<>();

        x1 = center.getCenter().getX() - (width / 2);   //left
        x2 = center.getCenter().getX() + (width / 2);   //right
        y1 = center.getCenter().getY() - (height / 2);  //down
        y2 = center.getCenter().getY() + (height / 2);  //up

        for (int num = 0; num < arr_points_no_clustering.size(); num++) {
            if ((arr_points_no_clustering.get(num).getX() > x1) & (arr_points_no_clustering.get(num).getX() < x2)
                    & (arr_points_no_clustering.get(num).getY() > y1) & (arr_points_no_clustering.get(num).getY() < y2)) {
                arr_points_in_cluster.add(arr_points_no_clustering.get(num));
            }
        }

        return arr_points_in_cluster;

    }

    public ArrayList<Cluster> getArr_clusters() {
        return arr_clusters;
    }
}
