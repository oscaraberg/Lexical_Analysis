
/**
 * Class that represents the up and down tokens
 */
public class PenToggler implements Expression{

    private final boolean toggle;

    public PenToggler(boolean toggle) {
        this.toggle = toggle;
    }

    @Override
    public DrawingBoard evaluate(DrawingBoard state) {
        state.setPenToggle(toggle);
        return state;
    }
}
