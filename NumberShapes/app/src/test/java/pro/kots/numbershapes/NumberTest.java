package pro.kots.numbershapes;

import org.junit.Test;

import static org.junit.Assert.*;


public class NumberTest {
    @Test
    public void detectsTriangularNumber() {
        Number triangularNumber = new Number(6);
        assertTrue(triangularNumber.isTriangular());
    }

    @Test
    public void detectsNonTriangularNumber() {
        Number nonTriangularNumber = new Number(5);
        assertFalse(nonTriangularNumber.isTriangular());
    }

    @Test
    public void detectsSquareNumber() {
        Number squareNumber = new Number(9);
        assertTrue(squareNumber.isSquare());
    }

    @Test
    public void detectsNonSquareNumber() {
        Number nonSquareNumber = new Number(7);
        assertFalse(nonSquareNumber.isSquare());
    }
}