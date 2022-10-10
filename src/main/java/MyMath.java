public class MyMath {
    static final double EPSILON = 0.000001;

    public static double dist(MyPoint a, MyPoint b) {
        return Math.sqrt((a.getX() - b.getX()) * (a.getX() - b.getX()) + (a.getY() - b.getY()) * (a.getY() - b.getY()));
    }

    public static double length(MyLine line) {
        return dist(line.getA(), line.getB());
    }

    public static boolean pointInLine(MyPoint point, MyLine line) {
        return point.getX() <= Math.max(line.getA().getX(), line.getB().getX()) + EPSILON && point.getX() >= Math.min(line.getA().getX(), line.getB().getX()) - EPSILON &&
                point.getY() <= Math.max(line.getA().getY(), line.getB().getY()) + EPSILON && point.getY() >= Math.min(line.getA().getY(), line.getB().getY()) - EPSILON;
    }

    public static boolean pointInCircle(MyPoint point, MyCircle circle) {
        return dist(point, circle.getCenter()) <= circle.getRadius() + 0.1;
    }

    public static MyLine lineByStartAndAngle(MyPoint start, double alpha, double length) {
        return new MyLine(start, new MyPoint(start.getX() + length * Math.sin(Math.PI / 2 - alpha), start.getY() + length * Math.cos(Math.PI / 2 - alpha)));
    }

    public static MyPoint equationAndEquation(LineEquation first, LineEquation second) {
        double a1 = first.a, a2 = second.a, b1 = first.b, b2 = second.b, c1 = first.c, c2 = second.c;
        if (a1 * b2 == b1 * a2) {
            //линии паралельны
            return null;
        } else {
            return new MyPoint((a1 * c2 - a2 * c1) / (b1 * a2 - a1 * b2), (-b1 * c2 + b2 * c1) / (b1 * a2 - a1 * b2));
        }
    }

    public static MyPoint lineAndLine(MyLine first, MyLine second) {
        MyPoint ans = equationAndEquation(new LineEquation(first), new LineEquation(second));
        if (ans == null) {
            return null;
        }
        if (pointInLine(ans, first) && pointInLine(ans, second)) {
            return ans;
        }
        return null;
    }


    public static MyPoint lineAndCircle(MyLine line, MyCircle circle) {
        LineEquation equation1 = new LineEquation(line);
        double x0 = circle.getCenter().getX(), y0 = circle.getCenter().getY(), r = circle.getRadius(), a = equation1.a, b = equation1.b, c = equation1.c;
        MyPoint first, second;
        if (a == 0) {
            //для вертикальных прямых отдельно, т.к. в общем случае произойдет деление на 0
            double firstCoef = 1, secondCoef = -2 * y0, thirdCoef = y0 * y0 + (x0 + c) * (x0 + c) - r * r;
            double discriminant = secondCoef * secondCoef - 4 * thirdCoef * firstCoef;
            if (discriminant < 0) {
                return null;
            } else {
                first = new MyPoint(-c, (-secondCoef + Math.sqrt(discriminant)) / (2 * firstCoef));
                second = new MyPoint(-c, (-secondCoef - Math.sqrt(discriminant)) / (2 * firstCoef));
            }
        } else {
            double firstCoef = 1 + (b * b) / (a * a), secondCoef = (2 * c * b) / (a * a) + (2 * y0 * b) / a - 2 * x0,
                    thirdCoef = x0 * x0 - r * r + y0 * y0 + (c / a) * (c / a) + (2 * y0 * c) / a;
            double discriminant = secondCoef * secondCoef - 4 * firstCoef * thirdCoef;
            if (discriminant < 0) {
                return null;
            } else {
                first = new MyPoint((-secondCoef + Math.sqrt(discriminant)) / (2 * firstCoef), (-c - b * ((-secondCoef + Math.sqrt(discriminant)) / (2 * firstCoef))) / a);
                second = new MyPoint((-secondCoef - Math.sqrt(discriminant)) / (2 * firstCoef), (-c - b * ((-secondCoef - Math.sqrt(discriminant)) / (2 * firstCoef))) / a);
            }
        }
        int countOfPoints = 0;
        if (pointInLine(first, line) && pointInCircle(first, circle)) {
            countOfPoints++;
        }
        if (pointInLine(second, line) && pointInCircle(second, circle)) {
            countOfPoints += 2;
        }
        switch (countOfPoints) {
            case 1:
                return first;
            case 2:
                return second;
            case 3: {
                if (dist(line.getA(), first) < dist(line.getA(), second)) {
                    return first;
                } else {
                    return second;
                }
            }
        }
        return null;
    }

    public static double getAngle(MyLine line) {
        double angle;
        if (new LineEquation(line).a == 0) {
            if (line.getB().getY() > line.getA().getY()) {
                angle = Math.PI / 2;
            } else {
                angle = 3 * Math.PI / 2;
            }
        } else {
            angle = Math.atan((line.getB().getY() - line.getA().getY()) / (line.getB().getX() - line.getA().getX()));
            if (line.getB().getX() - line.getA().getX() < 0) {
                angle += Math.PI;
            }
        }
        return angle;
    }

    public static MyLine perpendicular(MyPoint point, MyLine line) {
        LineEquation equation = new LineEquation(line);
        double angle = getAngle(line);
        LineEquation vert = new LineEquation(lineByStartAndAngle(point, Math.PI / 2 + angle, 100));
        MyPoint intersection = equationAndEquation(equation, vert);
        return new MyLine(point, intersection);
    }

    public static MyLine proection(MyLine first, MyLine second) {
        LineEquation equation1 = new LineEquation(first), equation2 = new LineEquation(second);
        double a1 = equation1.a, a2 = equation2.a, b1 = equation1.b, b2 = equation2.b;
        if (a1 * b2 == b1 * a2) {
            //линии паралельны
            return first;
        } else {
            MyLine perpendicular1 = perpendicular(first.getA(), second), perpendicular2 = perpendicular(first.getB(), second);
            MyPoint intersection1, intersection2;
            if (pointInLine(first.getA(), second)) {
                intersection1 = first.getA();
            } else {
                intersection1 = equationAndEquation(new LineEquation(perpendicular1), equation2);
            }
            if (pointInLine(first.getB(), second)) {
                intersection2 = first.getB();
            } else {
                intersection2 = equationAndEquation(new LineEquation(perpendicular2), equation2);
            }
            return new MyLine(intersection1, intersection2);
        }
    }
}
