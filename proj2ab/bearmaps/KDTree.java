package bearmaps;

import java.util.List;

public class KDTree {
    private Node root;

    private class Node {
        public Point point;
        public Node leftNode;
        public Node rightNode;
        public boolean lookupX = true;

        public Node(Point p, boolean lookupX) {
            this.point = p;
            this.leftNode = null;
            this.rightNode = null;
            this.lookupX = lookupX;
        }
    }

    public KDTree() {
        this.root = null;
    }

    public KDTree(List<Point> points) {
        this();
        for (Point p : points) {
            insert(p);
        }
    }

    public void printKDTree() {
        printTreeHelper(root);
    }

    private void printTreeHelper(Node n) {
        if (n == null) {
            return;
        }
        printTreeHelper(n.leftNode);
        System.out.println(String.format("x: %f, y: %f", n.point.getX(), n.point.getY()));
        printTreeHelper(n.rightNode);
    }

    public Point nearest(double x, double y) {
        Point objPoint = new Point(x, y);
        return nearestHelper(this.root, objPoint, this.root).point;
    }

    private Node nearestHelper(Node n, Point obj, Node bestNode) {
        if (n == null) {
            return bestNode;
        }
        double currentDist = Point.distance(n.point, obj);
        double bestDist = Point.distance(bestNode.point, obj);
        if (currentDist < bestDist) {
            bestDist = currentDist;
            bestNode = n;
        }

        Node goodSide;
        Node badSide;
        if (n.lookupX) {
            if (obj.getX() < n.point.getX()) {
                goodSide = n.leftNode;
                badSide = n.rightNode;
            }
            else {
                goodSide = n.rightNode;
                badSide = n.leftNode;
            }
        }
        else {
            if (obj.getY() < n.point.getY()) {
                goodSide = n.leftNode;
                badSide = n.rightNode;
            }
            else {
                goodSide = n.rightNode;
                badSide = n.leftNode;
            }
        }
        bestNode = nearestHelper(goodSide, obj, bestNode);
        Point bestPossibleBadSidePoint;
        if (n.lookupX) {
            bestPossibleBadSidePoint = new Point(n.point.getX(), obj.getY());
        }
        else {
            bestPossibleBadSidePoint = new Point(obj.getX(), n.point.getY());
        }
        if (bestDist > Point.distance(bestPossibleBadSidePoint, obj)) {
            bestNode = nearestHelper(badSide, obj, bestNode);
        }
        return bestNode;
    }


    private void insert(Point p) {
        if (this.root == null) {
            this.root = new Node(p, true);
        } else {
            this.root = insertHelper(this.root, p, this.root.lookupX);
        }
    }

    private Node insertHelper(Node n, Point p, boolean parentLookupX) {
        if (n == null) {
            return new Node(p, !parentLookupX);
        }
        if (n.lookupX) {
            if (p.getX() < n.point.getX()) {
                n.leftNode = insertHelper(n.leftNode, p, n.lookupX);
            } else {
                n.rightNode = insertHelper(n.rightNode, p, n.lookupX);
            }
        } else {
            if (p.getY() < n.point.getY()) {
                n.leftNode = insertHelper(n.leftNode, p, n.lookupX);
            } else {
                n.rightNode = insertHelper(n.rightNode, p, n.lookupX);
            }
        }
        return n;
    }
}
