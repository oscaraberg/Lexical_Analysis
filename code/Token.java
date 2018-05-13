// Författare: Per Austrin

// De olika token-typer vi har i grammatiken
enum TokenType {
	FORW, BACK, LEFT, RIGHT, DOWN, UP, COLOR, REP, NUMBER, END, NEWLINE, INDENT, HEX, COMMENT, WRAP, Invalid
}

// Klass för att representera en token
// I praktiken vill man nog även spara info om vilken rad/position i
// indata som varje token kommer ifrån, för att kunna ge bättre
// felmeddelanden
class Token {
	private TokenType type;
	private Object data;
	
	public Token(TokenType type) {
		this.type = type;
		this.data = null;
	}

	public Token(TokenType type, Object data) {
		this.type = type;
		this.data = data;
	}

	public TokenType getType() { return type; }
	public Object getData() { return data; }

}
