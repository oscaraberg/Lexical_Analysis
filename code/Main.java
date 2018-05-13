import java.io.*;
import java.sql.SQLSyntaxErrorException;
import java.util.Scanner;


/**
 * Exempel på rekursiv medåkning: en parser för binära träd enligt grammatiken
 *
 * BinTree --> leaf ( Number ) | branch ( BinTree , BinTree )
 *
 * Parsar trädet och skriver sedan ut det i ett lite annorlunda format
 * samt byter plats på vänster och höger i alla noder (i brist på
 * något roligare att göra)
 *
 * Provkörning från terminal på fil "test.in"
 *
 * javac *.java
 * java Main < test.in
 *
 *
 * (Det här exempelprogrammet skrevs av en person som normalt inte
 * använder Java, så ha överseende om delar av koden inte är så
 * vacker.)
 *
 * Författare: Oscar Åberg, Kim Ytterberg
 */
public class Main {
	public static void main(String args[]) throws java.io.IOException, SyntaxError {
		//System.out.println("STARTING...");

		//temporary change: Input input as string here:
//		String myInput= "% Nästlad loop 1\n" +
//				"REP 2 \"UP. FORW 10. DOWN. REP 3 \"LEFT 120. FORW 1.\"\n" +
//				"% Nästlad loop 2\n" +
//				"REP 3 \"REP 2 \"RIGHT 2. FORW 1.\"\n" +
//				"COLOR #FF0000. FORW 10. COLOR #0000FF.\"\n" +
//				"% COLOR #000000. % Bortkommenterat färgbyte\n" +
//				"BACK 10.\n" +
//				"% Upper/lower case ignoreras\n" +
//				"% Detta gäller även hex-tecknen A-F i färgerna i utdata,\n" +
//				"% det spelar ingen roll om du använder stora eller små\n" +
//				"% bokstäver eller en blandning.\n" +
//				"color #AbcdEF. left 70. foRW 10.";
		//String myInput = "";
		//String myInput = new Scanner(new File("src/test/test.in")).useDelimiter("\\Z").next();
		//InputStream input = new ByteArrayInputStream(myInput.getBytes());
		//Lexer lexer = new Lexer(input);

		//System.in changed to input for testing
		Lexer lexer = new Lexer(System.in);
		Executor executor = new Executor();
		try {
			Body mainBody = executor.buildExecutableTree(lexer);
			executor.executeTree(mainBody);
		} catch (SyntaxError e) {
			System.out.println(e.getMessage());
		}

		//Lexer lexer = new Lexer(System.in);
		//Parser parser = new Parser(lexer);
		//ParseTree result = parser.parse();
		// Parsning klar, gör vad vi nu vill göra med syntaxträdet
		//System.out.println(result.process());
	}
}
