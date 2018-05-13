import java.util.ArrayList;
import java.util.List;

/*
    Går igenom den syntaktiskt korrekta och parsade token-strängen och exekverar den i rätt ordning
    Eg. DOWN. FORW 2. REP 2 "UP. LEFT 2. DOWN."
    Har givit oss indata:
    Body reps = 1 (PenToggler: true, PenMover: x = 2) Body reps = 2 (PenToggler = false, PenMover: x = 2, Pentoggler = true)
    som evaluate går igenom, och evaluerar varje objects metoder -> execute.
*/
public class Body implements Expression {

    private List<Expression> expressions = new ArrayList<>();
    private int reps = 1;

    public void setReps(int reps) {
        this.reps = reps;
    }

    public DrawingBoard evaluate(DrawingBoard state) {
        for (int ii = 1; ii <= reps; ii++) {
            for (Expression o : expressions) state = o.evaluate(state);
        }
        return state;
    }

    public void addExpression(Expression exp) {
        expressions.add(exp);
    }

    public boolean notEmty() {
        return expressions.size() > 0;
    }
}
