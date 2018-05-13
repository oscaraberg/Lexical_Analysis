import java.util.ArrayList;
import java.util.List;

/**
 * En rekursiv medåknings-parser för binära träd
 * Författare: Kim Ytterberg, Oscar Åberg
 */
	public class Parser {
	private Lexer lexer;
	public static int newlineAmount = 1;
	public  static int curentComandLine;
	public static int lastCodeLine = 0;
	private static  boolean emtyNEWLINE;

	public Parser(Lexer lexer) {
		this.lexer = lexer;
	}


    /**
     * Kollar så att indata är syntaxmässigt korrekt och parsar den till motsvarande object
     */
	public static Body buildTheTree(Lexer lexer) throws SyntaxError {
		int depth = 0;
		depth++;
		Body body = new Body();
		checkForOptionalEndIndents(lexer);
		if (!lexer.hasMoreTokens())
		    return body;
		while (lexer.hasMoreTokens()) {
			body.addExpression(handleNextExpression(lexer));
		}
		return body;
	}

	/**
     * behandlar tokans. ser till att de är giltiga kommandom.
	 * Någonting fram till "." t.ex. : UP. LEFT 5. REP 5 RIGHT 2. REP 3 "UP. DOWN. LEFT 2.".
	 */
	private static Expression handleNextExpression(Lexer lexer) throws SyntaxError {
		Expression toReturn = null;
		checkForOptionalIndents(lexer);
		Token token = lexer.nextToken();
		if (token.getType().equals(TokenType.UP)) {
            lastCodeLine = newlineAmount;
            checkForOptionalIndents(lexer);
            assureLastTokenIsPeriod(lexer.nextToken());
            checkForOptionalEndIndents(lexer);
			toReturn = new PenToggler(false);
		}
		else if (token.getType().equals(TokenType.DOWN)) {
            lastCodeLine = newlineAmount;
            checkForOptionalIndents(lexer);
		    assureLastTokenIsPeriod(lexer.nextToken());
            checkForOptionalEndIndents(lexer);
		    toReturn = new PenToggler(true);
		}
		else if (token.getType().equals(TokenType.LEFT)) {
            lastCodeLine = newlineAmount;
            assureNextTokenIsIndent(lexer);
            checkForOptionalIndents(lexer);
            int modifier = assureNextTokenIsNumber(lexer.nextToken());
            checkForOptionalIndents(lexer);
            assureLastTokenIsPeriod(lexer.nextToken());
            checkForOptionalEndIndents(lexer);
            toReturn = new PenTurner(modifier, true);
        }
        else if (token.getType().equals(TokenType.RIGHT)) {
            lastCodeLine = newlineAmount;
            assureNextTokenIsIndent(lexer);
            int modifier = assureNextTokenIsNumber(lexer.nextToken());
            checkForOptionalIndents(lexer);
            assureLastTokenIsPeriod(lexer.nextToken());
            checkForOptionalEndIndents(lexer);
            toReturn = new PenTurner(modifier, false);
        }
        else if (token.getType().equals(TokenType.FORW)) {
            lastCodeLine = newlineAmount;
            assureNextTokenIsIndent(lexer);
            checkForOptionalIndents(lexer);
            int modifier = assureNextTokenIsNumber(lexer.nextToken());
            checkForOptionalIndents(lexer);
            assureLastTokenIsPeriod(lexer.nextToken());
            checkForOptionalEndIndents(lexer);
            toReturn = new PenMover(modifier);
        }
        else if (token.getType().equals(TokenType.BACK)) {
            lastCodeLine = newlineAmount;
            assureNextTokenIsIndent(lexer);
            int modifier = assureNextTokenIsNumber(lexer.nextToken());
            checkForOptionalIndents(lexer);
            assureLastTokenIsPeriod(lexer.nextToken());
            checkForOptionalEndIndents(lexer);
            toReturn = new PenMover(-modifier);
        }
        else if (token.getType().equals(TokenType.COLOR)) {
            lastCodeLine = newlineAmount;
		    assureNextTokenIsIndent(lexer);
            String hexa = assureNextTokenIsHexa(lexer.nextToken());
            checkForOptionalIndents(lexer);
            assureLastTokenIsPeriod(lexer.nextToken());
            checkForOptionalEndIndents(lexer);
            toReturn = new ColorSetter(hexa);
		}
		else if (token.getType().equals(TokenType.REP)) {
            lastCodeLine = newlineAmount;
			assureNextTokenIsIndent(lexer);
            int repAmount = assureNextTokenIsNumber(lexer.nextToken());
            assureNextTokenIsIndent(lexer);
            Body body = buildLoop(lexer);
            body.setReps(repAmount);
            checkForOptionalEndIndents(lexer);
            toReturn = body;
		}
		else if (token.getType().equals(TokenType.WRAP)) {
            throw new SyntaxError(String.valueOf(newlineAmount));
        }
        else if (token.getType().equals(TokenType.END)) {
            throw new SyntaxError(String.valueOf(newlineAmount));
        }
        else if (token.getType().equals(TokenType.NUMBER)) {
            throw new SyntaxError(String.valueOf(newlineAmount));
        }
        else if (token.getType().equals(TokenType.HEX)) {
            throw new SyntaxError(String.valueOf(newlineAmount));
        }
		else if (token.getType().equals(TokenType.Invalid))
            throw new SyntaxError(String.valueOf(newlineAmount));

        if (toReturn == null) {
		    throw new RuntimeException("not implemented execution node for token type: " +  token.getType());
        }
		return toReturn;
	}

    /**
     * Checks there's at least one indent between argument and next token
     * @param lexer
     * @return true if there's at least one indentation
     * @throws SyntaxError if not
     */
	private static boolean assureNextTokenIsIndent(Lexer lexer) throws SyntaxError {
	    checkForEOL(lexer.peekToken());
	    Token nextToken = lexer.nextToken();
		if ((!nextToken.getType().equals(TokenType.INDENT) && !nextToken.getType().equals(TokenType.NEWLINE)) && !nextToken.getType().equals(TokenType.COMMENT)) {
            throw new SyntaxError(String.valueOf(newlineAmount));
		}

		if (nextToken.getType().equals(TokenType.NEWLINE)) {
            newlineAmount++;
        }

        checkForOptionalIndents(lexer);
//		nextToken = lexer.peekToken();
//        while (nextToken.getType().equals(TokenType.INDENT) || nextToken.getType().equals(TokenType.NEWLINE)) {
//		    if (nextToken.getType().equals(TokenType.NEWLINE)) {
//		        newlineAmount++;
//            }
//			lexer.nextToken();
//		    nextToken = lexer.peekToken();
//            if (nextToken == null && newlineAmount != lastCodeLine){
//                throw new SyntaxError(String.valueOf(lastCodeLine));
//            }
//		}
		return true;
	}

    private static void checkForEOL(Token token) throws SyntaxError {
        if (token.getType().equals(TokenType.Invalid)) {
            if (newlineAmount != lastCodeLine) {
                throw new SyntaxError(String.valueOf(newlineAmount));
            }
            throw new SyntaxError(String.valueOf(newlineAmount));
        }
    }


    /**
     * Checks for optional indentations or newlines or comments
     * @param lexer
     * @throws SyntaxError
     */
    private static void checkForOptionalIndents(Lexer lexer) throws SyntaxError {
        while (lexer.peekToken().getType().equals(TokenType.INDENT) || lexer.peekToken().getType().equals(TokenType.NEWLINE)||lexer.peekToken().getType().equals(TokenType.COMMENT)) {
            if (lexer.peekToken().getType().equals(TokenType.NEWLINE)) {
                newlineAmount++;
            }
            lexer.nextToken();
            Token nextToken = lexer.peekToken();
            checkForEOL(nextToken);
        }
    }

    /**
     * Checks for optional indentations or newlines comments att de end off the program
     * @param lexer
     * @throws SyntaxError
     */
    private static void checkForOptionalEndIndents(Lexer lexer) throws SyntaxError {
        if(lexer.seeNewline() != null) {
            while (lexer.seeNewline().getType().equals(TokenType.INDENT) || lexer.seeNewline().getType().equals(TokenType.NEWLINE)||lexer.seeNewline().getType().equals(TokenType.COMMENT)) {

                if (lexer.seeNewline().getType().equals(TokenType.NEWLINE)) {
                    newlineAmount++;
                }
                lexer.nextTokenNoCheck();
                if(lexer.seeNewline() == null)
                    break;
                checkForEOL(lexer.seeNewline());
            }
        }
    }

    /**
     * Checks if next token is a number
     * @param token
     * @return true if next token is a number
     * @throws SyntaxError if not
     */
	private static int assureNextTokenIsNumber(Token token) throws SyntaxError {
		if (token.getType().equals(TokenType.NUMBER)) {
            if ( Integer.valueOf((String) token.getData()) < 1)
                throw new SyntaxError(String.valueOf(lastCodeLine));
            lastCodeLine = newlineAmount;
            return Integer.parseInt(token.getData().toString());
        }
		else throw new SyntaxError(String.valueOf(newlineAmount));
	}

    /**
     * Makes sure last token is a period
     * @param token
     * @return true if it is a period
     * @throws SyntaxError if not
     */
	private static boolean assureLastTokenIsPeriod(Token token) throws SyntaxError {
		if (token.getType().equals(TokenType.END)) {
            lastCodeLine = newlineAmount;
            return true;
        }
		else throw new SyntaxError(String.valueOf(newlineAmount));
	}

    /**
     * Checks that next token is a hexa-value
     * @param token
     * @return
     * @throws SyntaxError
     */
    private static String assureNextTokenIsHexa(Token token) throws SyntaxError {
        if (token.getType().equals(TokenType.HEX)) {
            lastCodeLine = newlineAmount;
            return token.getData().toString();
        }
        else throw new SyntaxError(String.valueOf(lastCodeLine));
    }

    /**
     * Builds the rep body
     * @param lexer
     * @return
     * @throws SyntaxError
     */
	private static Body buildLoop(Lexer lexer) throws SyntaxError {
		Body body = new Body();
		if (lexer.peekToken().getType().equals(TokenType.WRAP)) {
		    lexer.nextToken();
		    boolean emtyWrap = true;
		    //TODO: need to stop if reach end, ie no more tokens
			while (!lexer.peekToken().getType().equals(TokenType.WRAP)) {
				body.addExpression(handleNextExpression(lexer));
				emtyWrap = false;
			}
			if (emtyWrap)
                throw new SyntaxError(String.valueOf(lastCodeLine));

            lastCodeLine = newlineAmount;
			lexer.nextToken();
		} else {
			body.addExpression(handleNextExpression(lexer));
		}
		return body;
	}

}
