package bearmaps;

import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;

public class KDTreeTest {
    @Test
    public void testConstructor() {
        List<Point> points = new LinkedList<>();
        points.add(new Point(3, 5));
        points.add(new Point(1, 7));
        points.add(new Point(4, 4));
        points.add(new Point(4, 5));
        points.add(new Point(5, 6));
        points.add(new Point(7, 8));
        points.add(new Point(1, 9));
        points.add(new Point(0, 3));
        points.add(new Point(0, 1));
        points.add(new Point(1, 1));
        points.add(new Point(3, 4));
        points.add(new Point(3, 5));
        points.add(new Point(3, 5));
        KDTree kdTree = new KDTree(points);
        kdTree.printKDTree();
    }

    @Test
    public void testConstructor2() {
        List<Point> points = new LinkedList<>();
        points.add(new Point(2, 3));
        points.add(new Point(4, 2));
        points.add(new Point(4, 5));
        points.add(new Point(3, 3));
        points.add(new Point(1, 5));
        points.add(new Point(4, 4));
        KDTree kdTree = new KDTree(points);
        kdTree.printKDTree();
    }

    @Test
    public void testNearest() {
        int iterateTime = 1000;
        Random randomGenerator = new Random(0);
        for (int i = 0; i < iterateTime; i++) {
            List<Point> points = new LinkedList<>();
//            points.add(new Point(3, 5));
//            points.add(new Point(1, 7));
//            points.add(new Point(4, 4));
//            points.add(new Point(4, 5));
//            points.add(new Point(5, 6));
//            points.add(new Point(7, 8));
//            points.add(new Point(1, 9));
//            points.add(new Point(0, 3));
//            points.add(new Point(0, 1));
//            points.add(new Point(1, 1));
//            points.add(new Point(3, 4));
//            points.add(new Point(3, 5));
//            points.add(new Point(3, 5));
//            points.add(new Point(10 * randomGenerator.nextDouble(), 10 * randomGenerator.nextDouble()));
            points.add(new Point(2, 3));
            points.add(new Point(4, 2));
            points.add(new Point(4, 5));
            points.add(new Point(3, 3));
            points.add(new Point(1, 5));
            points.add(new Point(4, 4));
            KDTree kdTree = new KDTree(points);
            NaivePointSet naivePointSet = new NaivePointSet(points);
            Point objPoint = new Point(10 * randomGenerator.nextDouble(), 10 * randomGenerator.nextDouble());
            Point nearestPoint = kdTree.nearest(objPoint.getX(), objPoint.getY());
            Point naivePointSetNearest = naivePointSet.nearest(objPoint.getX(), objPoint.getY());
            System.out.println(objPoint.getX());
            System.out.println(objPoint.getY());
            assertEquals(nearestPoint.getX(), naivePointSetNearest.getX(), 1e-12);
            assertEquals(nearestPoint.getY(), naivePointSetNearest.getY(), 1e-12);
        }

    }

    //    @Test
//    public void testRandomNearest() {
//        int pointNum = 10000;
//        List<Point> pointList = new ArrayList<>();
//        Random randomGenerator = new Random();
//        for(int i = 0; i < pointNum; i++) {
//            pointList.add(new Point(100 * randomGenerator.nextDouble(), 100 * randomGenerator.nextDouble()));
//        }
//        KDTree kdTree = new KDTree(pointList);
//        NaivePointSet naivePointSet = new NaivePointSet(pointList);
//        Point objPoint = new Point(100 * randomGenerator.nextDouble(), 100 * randomGenerator.nextDouble());
//        Point kdTreeNearest = kdTree.nearest(objPoint.getX(), objPoint.getY());
//        Point naivePointSetNearest = naivePointSet.nearest(objPoint.getX(), objPoint.getY());
//        double kdTreeDistance = Point.distance(kdTreeNearest, objPoint);
//        double naivePointSetDistance = Point.distance(naivePointSetNearest, objPoint);
//        System.out.println(kdTreeDistance);
//        System.out.println(naivePointSetDistance);
//        assertEquals(kdTreeNearest.getX(), naivePointSetNearest.getX(), 1e-12);
//        assertEquals(kdTreeNearest.getY(), naivePointSetNearest.getY(), 1e-12);
//    }
    @Test
    public void testNearestSimple() {
        List<Point> points = new LinkedList<>();
        points.add(new Point(2, 3));
        points.add(new Point(4, 2));
        points.add(new Point(4, 5));
        points.add(new Point(3, 3));
        points.add(new Point(1, 5));
        points.add(new Point(4, 4));
        KDTree kdTree = new KDTree(points);
        NaivePointSet naivePointSet = new NaivePointSet(points);
//        Point objPoint = new Point(2.17570412209686, 8.544871670422907);
        Point objPoint = new Point(4, 10);
        Point nearestPoint = kdTree.nearest(objPoint.getX(), objPoint.getY());
        Point naivePointSetNearest = naivePointSet.nearest(objPoint.getX(), objPoint.getY());
        double kdTreeDistance = Point.distance(nearestPoint, objPoint);
        double naivePointSetDistance = Point.distance(naivePointSetNearest, objPoint);
        System.out.println(kdTreeDistance);
        System.out.println(naivePointSetDistance);
        assertEquals(nearestPoint.getX(), naivePointSetNearest.getX(), 1e-12);
        assertEquals(nearestPoint.getY(), naivePointSetNearest.getY(), 1e-12);
    }

    @Test
    public void testRandomNearest() {
        int pointNum = 10000;
        List<Point> pointList = new ArrayList<>();
        Random randomGenerator = new Random(3);
        for (int i = 0; i < pointNum; i++) {
            pointList.add(new Point(randomGenerator.nextInt(1000), randomGenerator.nextInt(1000)));
        }
        KDTree kdTree = new KDTree(pointList);
        NaivePointSet naivePointSet = new NaivePointSet(pointList);
        Point objPoint = new Point(100 * randomGenerator.nextDouble(), 100 * randomGenerator.nextDouble());
        Point kdTreeNearest = kdTree.nearest(objPoint.getX(), objPoint.getY());
        Point naivePointSetNearest = naivePointSet.nearest(objPoint.getX(), objPoint.getY());
        double kdTreeDistance = Point.distance(kdTreeNearest, objPoint);
        double naivePointSetDistance = Point.distance(naivePointSetNearest, objPoint);
        System.out.println(kdTreeDistance);
        System.out.println(naivePointSetDistance);
        assertEquals(kdTreeNearest.getX(), naivePointSetNearest.getX(), 1e-12);
        assertEquals(kdTreeNearest.getY(), naivePointSetNearest.getY(), 1e-12);
    }
}

