
public class PenMover implements Expression {

    private int numberOfSteps;

    public PenMover(int numberOfSteps) {
        this.numberOfSteps = numberOfSteps;
    }

    @Override
    public DrawingBoard evaluate(DrawingBoard state) {
        state.movePen(numberOfSteps);
        return state;
    }
}
