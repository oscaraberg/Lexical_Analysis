import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;


public class Executor {


    private Parser parser;
    /**
     * Returns the main Body (root node) of the built tree.
     * @param lexer
     * @return
     * @throws IOException
     * @throws SyntaxError
     */
    public Body buildExecutableTree(Lexer lexer) throws IOException, SyntaxError {
        return parser.buildTheTree(lexer);
    }

    //Evaluerar den korrekta och parsade syntaxen
    public void executeTree(Body mainBody) {
        DrawingBoard state = new DrawingBoard();
        state = mainBody.evaluate(state);
    }

    public void throwError() throws IOException, SyntaxError {
        throw new SyntaxError(String.valueOf(parser.lastCodeLine));
    }
}
