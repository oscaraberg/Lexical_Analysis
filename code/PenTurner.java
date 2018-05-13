/**
 * Created by kimy on 22/04/17.
 */
public class PenTurner implements Expression {

    private int degrees;
    private boolean isLeft;


    public PenTurner(int degrees, boolean isLeft) {
        this.degrees = degrees;
        this.isLeft = isLeft;
    }

    @Override
    public DrawingBoard evaluate(DrawingBoard state) {
        if (isLeft) {
            state.turnPen(degrees);
        }
        else {
            state.turnPen(-degrees);
        }
        return state;
    }
}
