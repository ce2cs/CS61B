package bearmaps;

import java.util.List;

public class NaivePointSet implements PointSet {
    private List<Point> points;
    public NaivePointSet(List<Point> points) {
        this.points = points;
    }

    @Override
    public Point nearest(double x, double y) {
        Point objectPoint = new Point(x, y);
        Point nearestPoint = this.points.get(0);
        double bestDistance = Point.distance(nearestPoint, objectPoint);
        for(Point p: this.points) {
            double distance = Point.distance(p, objectPoint);
            if (distance < bestDistance) {
                bestDistance = distance;
                nearestPoint = p;
            }
        }
        return nearestPoint;
    }

    public static void main(String[] args) {
        Point p1 = new Point(1.1, 2.2); // constructs a Point with x = 1.1, y = 2.2
        Point p2 = new Point(3.3, 4.4);
        Point p3 = new Point(-2.9, 4.2);

        NaivePointSet nn = new NaivePointSet(List.of(p1, p2, p3));
        Point ret = nn.nearest(3.0, 4.0); // returns p2
        ret.getX(); // evaluates to 3.3
        ret.getY(); // evaluates to 4.4
    }
}


