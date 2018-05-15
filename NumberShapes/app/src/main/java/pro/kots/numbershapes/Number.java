package pro.kots.numbershapes;

public class Number {

    int number;

    public Number(int number) {
        this.number = number;
    }

    protected boolean isTriangular() {
        int x = 1;

        int triangularNumber = 1;

        while (triangularNumber < number) {
            triangularNumber += ++x;
        }

        return triangularNumber == number;
    }

    protected boolean isSquare() {
        double squareRoot = Math.sqrt(this.number);
        return squareRoot == Math.floor(squareRoot);
    }
}
