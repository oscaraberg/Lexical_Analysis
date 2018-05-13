/**
 * Created by kimy on 23/04/17.
 */
public class ColorSetter implements Expression {

    private String color;

    public ColorSetter(String color) {
        this.color = color;
    }

    @Override
    public DrawingBoard evaluate(DrawingBoard state) {
        state.setColour(color);
        return state;
    }
}
