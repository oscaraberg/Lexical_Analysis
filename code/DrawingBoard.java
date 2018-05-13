import java.math.*;
import java.text.DecimalFormat;

/**
 * Sample output
 * c x1, y1, x2, x2
 */

public class DrawingBoard {
    //TODO: if penToggle is false, pen will move, however no line will be drawn.
    private boolean penToggle;
    private String color = "#0000FF";
    private double xPosition;
    private double yPosition;
    private int currentDirection;

    public DrawingBoard() {
        this.penToggle = false;
        currentDirection = 0;
        xPosition = 0;
        yPosition = 0;
    }

    public void setColour(String color) {
        this.color = color;
    }

    public void setPenToggle(boolean penToggle) {
        this.penToggle = penToggle;
    }

    public void setPosition(int x, int y) {
        this.xPosition += x;
        this.yPosition += y;
    }

    //TODO: if UP is first argument, printout is wrong. See Sample input 11
    public void movePen(int noOfSteps) {
        double previousX = xPosition;
        double previousY = yPosition;
        //TODO: String.format("%.5f"
        evaulateNewPosition(xPosition, yPosition, currentDirection, noOfSteps);
        //if (penToggle) {
        //System.out.print(this.color.toUpperCase() + " " + previousX + " " + previousY);
            if (penToggle) {
                System.out.print(this.color.toUpperCase() + " " + previousX + " " + previousY);
                System.out.println(" " + this.xPosition + " " + this.yPosition);

            } else {
                //System.out.println(" " + previousX + " " + previousY);
            }
                    //+ " " + this.xPosition + " " + this.yPosition)

        //} else {
        //    System.out.println(this.color.toUpperCase() + " " + previousX + " " + previousY
        //            + " " + previousX + " " + previousY);
        //}
    }

    public boolean turnPen(int degrees) {
        this.currentDirection += degrees % 360;
        return true;
    }

    public void evaulateNewPosition(double xPosition, double yPosition,
                                    int currentDirection, int noOfSteps) {
            this.xPosition = xPosition + noOfSteps * Math.cos(Math.PI * currentDirection / 180);
            this.yPosition = yPosition + noOfSteps * Math.sin(Math.PI * currentDirection / 180);
    }
}
