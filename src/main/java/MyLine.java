public class MyLine {
    private MyPoint a, b;

    public MyLine(MyPoint a, MyPoint b) {
        this.a = a;
        this.b = b;
    }

    public MyPoint getA() {
        return a;
    }

    public MyPoint getB() {
        return b;
    }

    public void setB(MyPoint b) { this.b = b; }
}
