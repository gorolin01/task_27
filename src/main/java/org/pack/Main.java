package org.pack;

import java.util.ArrayList;

public class Main {

    private static String URL = "C:\\Users\\Cashless\\Desktop\\инф25\\Доп.файлы_2025\\Задание 27\\demo_2025_27_Б.xlsx";

    //характеристики кластера
    private static double WIDTH = 3;
    private static double HEIGHT = 3;

    public static void main(String[] args) {

        LoaderDate loaderDate = new LoaderDate(URL);
        Clusterization clusterization = new Clusterization(loaderDate.getArr_points(), WIDTH, HEIGHT);
        System.out.println("PX : " + (int) (10000 * PX(clusterization.getArr_clusters())));
        System.out.println("PY : " + (int) (10000 * PY(clusterization.getArr_clusters())));

    }

    private static double PX (ArrayList<Cluster> arr_clusters) {

        double sum = 0;
        for (int num_clusters = 0; num_clusters < arr_clusters.size(); num_clusters++) {
            sum += arr_clusters.get(num_clusters).getCenter().getCenter().getX();
        }

        return sum / arr_clusters.size();

    }

    private static double PY (ArrayList<Cluster> arr_clusters) {

        double sum = 0;
        for (int num_clusters = 0; num_clusters < arr_clusters.size(); num_clusters++) {
            sum += arr_clusters.get(num_clusters).getCenter().getCenter().getY();
        }

        return sum / arr_clusters.size();

    }

}