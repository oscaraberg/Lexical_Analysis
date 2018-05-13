import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * En klass för att göra lexikal analys, konvertera indataströmmen
 * till en sekvens av tokens.  Den här klassen läser in hela
 * indatasträngen och konverterar den på en gång i konstruktorn. Man
 * kan tänka sig en variant som läser indataströmmen allt eftersom
 * tokens efterfrågas av parsern, men det blir lite mer komplicerat.
 *
 * Författare: Oscar Åberg, Kim Ytterberg
 */
public class Lexer {
	private String input;
	private List<Token> tokens;
	private int currentToken;

	// Hjälpmetod som läser in innehållet i en inputstream till en
	// sträng
	private static String readInput(InputStream f) throws java.io.IOException {
		Reader stdin = new InputStreamReader(f);
		StringBuilder buf = new StringBuilder();
		char input[] = new char[1024];
		int read = 0;
		while ((read = stdin.read(input)) != -1) { //-1 if end of stream reached read = -1, otherwise add input to buf of char array length 
			buf.append(input, 0, read);
		}
		return buf.toString();
	}


	public Lexer(InputStream in) throws java.io.IOException {
		String input = Lexer.readInput(in);
		// Ett regex som beskriver hur ett token kan se ut, plus whitespace (som vi här vill ignorera helt)
		Pattern tokenPattern = Pattern.compile("(?i)FORW|(?i)BACK|(?i)LEFT|(?i)RIGHT|(?i)DOWN|(?i)UP|(?i)COLOR|(?i)REP|\\d+|\\.|\\n|\\s|\\t|#([A-Fa-f]|\\d){6}|%.*|(\\\")");
		Matcher m = tokenPattern.matcher(input);
		int inputPos = 0;
		tokens = new ArrayList<Token>();
		currentToken = 0;
		// Hitta förekomster av tokens/whitespace i indata
		while (m.find()) {
			// Om matchningen inte börjar där den borde har vi hoppat
			// över något skräp i indata, markera detta som ett
			// "Invalid"-token
			if (m.start() != inputPos) {
				tokens.add(new Token(TokenType.Invalid));
			}
			// Kolla vad det var som matchade
			String str = m.group().toLowerCase();
			if 	(str.equals("forw"))
				tokens.add(new Token(TokenType.FORW));
			else if (str.equals("back"))
				tokens.add(new Token(TokenType.BACK));
			else if (str.equals("left"))
				tokens.add(new Token(TokenType.LEFT));
			else if (str.equals("right"))
				tokens.add(new Token(TokenType.RIGHT));
			else if (str.equals("down"))
				tokens.add(new Token(TokenType.DOWN));
			else if (str.equals("up"))
				tokens.add(new Token(TokenType.UP));
			else if (str.equals("color"))
				tokens.add(new Token(TokenType.COLOR));
			else if (str.equals("rep"))
				tokens.add(new Token(TokenType.REP));
			//TODO: Vill bara matcha på heltal
			else if (isDigit(m.group()))
				tokens.add(new Token(TokenType.NUMBER, m.group()));
			else if (m.group().equals("."))
				tokens.add(new Token(TokenType.END));
			else if (m.group().equals("\n"))
				tokens.add(new Token(TokenType.NEWLINE));
			else if (m.group().equals(" ") || m.group().equals("\t"))
				tokens.add(new Token(TokenType.INDENT));
			//Ta bort alla argument från % fram till newline och spara inte som token
			else if (isHexa(m.group()))
				tokens.add(new Token(TokenType.HEX, m.group()));
			else if (m.group().startsWith("%"))
				tokens.add(new Token(TokenType.COMMENT));
			else if (m.group().equals("\""))
				tokens.add(new Token(TokenType.WRAP));
			else tokens.add(new Token(TokenType.Invalid));
			//else if (Character.isDigit(m.group().charAt(0)))
			//	tokens.add(new Token(TokenType.Number, Integer.parseInt(m.group())));
			inputPos = m.end();
		}
		// Kolla om det fanns något kvar av indata som inte var ett token
		if (inputPos != input.length()) {
			tokens.add(new Token(TokenType.Invalid));
		}
		// Debug-kod för att skriva ut token-sekvensen
	//	for (Token token: tokens)
	//	   System.out.println(token.getType());
	}

	public Token seeNewline() throws SyntaxError {
		// Slut på indataströmmen
		if(hasMoreTokens())
			return tokens.get(currentToken);
		return null;
	}
	public Token nextTokenNoCheck() throws SyntaxError {
		Token res = seeNewline();
		if(hasMoreTokens())
			++currentToken;
		return res;
	}

	//Kika på nästa token i indata, utan att gå vidare
	public Token peekToken() throws SyntaxError {
		// Slut på indataströmmen
		if (!hasMoreTokens())
			throw new SyntaxError(String.valueOf(Parser.lastCodeLine));
		return tokens.get(currentToken);
	}
	//Kika på nästa token i indata, utan att gå vidare
	public Token peekWrapToken() throws SyntaxError {
		// Slut på indataströmmen
		if (!hasMoreTokens())
			throw new SyntaxError(String.valueOf(Parser.lastCodeLine));
		return tokens.get(currentToken);
	}

	//Hämta nästa token i indata och gå framåt i indata
	public Token nextToken() throws SyntaxError {
		Token res = peekToken();
		++currentToken;
		return res;
	}

	public boolean hasMoreTokens() {
		return currentToken < tokens.size();
	}


	private boolean isDigit(String str) {
		for (Character c : str.toCharArray()) {
			if (!Character.isDigit(c)) {
				return false;
			}
		}
		return true;
	}

	private boolean isHexa(String str) {
		if (str.charAt(0) != '#') {
			return false;
		}
		return str.length() == 7 && str.matches("#([A-Fa-f]|\\d){6}");
	}

	public boolean notEmty() {
		return tokens.size() > 0;
	}

}
