https://powcoder.com
代写代考加微信 powcoder
Assignment Project Exam Help
Add WeChat powcoder
https://powcoder.com
代写代考加微信 powcoder
Assignment Project Exam Help
Add WeChat powcoder
package cop5556sp18; /**
* Initial code for the cop5556sp18.Scanner for the class project in COP5556 Programming Language Principles
* at the University of Florida, Spring 2018.
* 
* This software is solely for the educational benefit of students 
* enrolled in the course during the Spring 2018 semester.  
* 
* This software, and any software derived from it,  may not be shared with others or posted to public web sites,
* either during the course or afterwards.
* 
*  @Beverly A. Sanders, 2018
*/

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Scanner {

	@SuppressWarnings("serial")
	public static class LexicalException extends Exception {

		int pos;

		public LexicalException(String message, int pos) {
			super(message);
			this.pos = pos;
		}

		public int getPos() {
			return pos;
		}
	}

	public static enum Kind {
		IDENTIFIER, INTEGER_LITERAL, BOOLEAN_LITERAL, FLOAT_LITERAL,
		KW_Z/* Z */, KW_default_width/* default_width */, KW_default_height/* default_height */, 
		KW_width /* width */, KW_height /* height*/, KW_show/*show*/, KW_write /* write */, KW_to /* to */,
		KW_input /* input */, KW_from /* from */, KW_cart_x/* cart_x*/, KW_cart_y/* cart_y */, 
		KW_polar_a/* polar_a*/, KW_polar_r/* polar_r*/, KW_abs/* abs */, KW_sin/* sin*/, KW_cos/* cos */, 
		KW_atan/* atan */, KW_log/* log */, KW_image/* image */, KW_int/* int */, KW_float /* float */, 
		KW_boolean/* boolean */, KW_filename/* filename */, KW_red /* red */, KW_blue /* blue */, KW_sleep /* sleep */,
		KW_green /* green */, KW_alpha /* alpha*/, KW_while /* while */, KW_if /* if */, OP_ASSIGN/* := */, 
		OP_EXCLAMATION/* ! */, OP_QUESTION/* ? */, OP_COLON/* : */, OP_EQ/* == */, OP_NEQ/* != */, 
		OP_GE/* >= */, OP_LE/* <= */, OP_GT/* > */, OP_LT/* < */, OP_AND/* & */, OP_OR/* | */, 
		OP_PLUS/* +*/, OP_MINUS/* - */, OP_TIMES/* * */, OP_DIV/* / */, OP_MOD/* % */, OP_POWER/* ** */, 
		OP_AT/* @ */, LPAREN/*( */, RPAREN/* ) */, LSQUARE/* [ */, RSQUARE/* ] */, LBRACE /*{ */, 
		RBRACE /* } */, LPIXEL /* << */, RPIXEL /* >> */, SEMI/* ; */, COMMA/* , */, DOT /* . */, EOF;
	}

	/**
	 * Class to represent Tokens.
	 * 
	 * This is defined as a (non-static) inner class which means that each Token
	 * instance is associated with a specific cop5556sp18.Scanner instance. We use this when
	 * some token methods access the chars array in the associated cop5556sp18.Scanner.
	 * 
	 * @author Beverly Sanders
	 *
	 */
	public class Token {
		public final Kind kind;
		public final int pos; // position of first character of this token in the input. Counting starts at 0
								// and is incremented for every character.
		public final int length; // number of characters in this token

		public Token(Kind kind, int pos, int length) {
			super();
			this.kind = kind;
			this.pos = pos;
			this.length = length;
		}

		public String getText() {
			return String.copyValueOf(chars, pos, length);
		}

		/**
		 * precondition: This Token's kind is INTEGER_LITERAL
		 * 
		 * @returns the integer value represented by the token
		 */
		public int intVal() {
			assert kind == Kind.INTEGER_LITERAL;
			return Integer.valueOf(String.copyValueOf(chars, pos, length));
		}

		/**
		 * precondition: This Token's kind is FLOAT_LITERAL]
		 * 
		 * @returns the float value represented by the token
		 */
		public float floatVal() {
			assert kind == Kind.FLOAT_LITERAL;
			return Float.valueOf(String.copyValueOf(chars, pos, length));
		}

		/**
		 * precondition: This Token's kind is BOOLEAN_LITERAL
		 * 
		 * @returns the boolean value represented by the token
		 */
		public boolean booleanVal() {
			assert kind == Kind.BOOLEAN_LITERAL;
			return getText().equals("true");
		}

		/**
		 * Calculates and returns the line on which this token resides. The first line
		 * in the source code is line 1.
		 * 
		 * @return line number of this Token in the input.
		 */
		public int line() {
			return Scanner.this.line(pos) + 1;
		}

		/**
		 * Returns position in line of this token.
		 * 
		 * @param line.
		 *            The line number (starting at 1) for this token, i.e. the value
		 *            returned from Token.line()
		 * @return
		 */
		public int posInLine(int line) {
			return Scanner.this.posInLine(pos, line - 1) + 1;
		}

		/**
		 * Returns the position in the line of this Token in the input. Characters start
		 * counting at 1. Line termination characters belong to the preceding line.
		 * 
		 * @return
		 */
		public int posInLine() {
			return Scanner.this.posInLine(pos) + 1;
		}

		public String toString() {
			int line = line();
			return "[" + kind + "," + String.copyValueOf(chars, pos, length) + "," + pos + "," + length + "," + line
					+ "," + posInLine(line) + "]";
		}

		/**
		 * Since we override equals, we need to override hashCode, too.
		 * 
		 * See
		 * https://docs.oracle.com/javase/9/docs/api/java/lang/Object.html#hashCode--
		 * where it says, "If two objects are equal according to the equals(Object)
		 * method, then calling the hashCode method on each of the two objects must
		 * produce the same integer result."
		 * 
		 * This method, along with equals, was generated by eclipse
		 * 
		 */
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + ((kind == null) ? 0 : kind.hashCode());
			result = prime * result + length;
			result = prime * result + pos;
			return result;
		}

		/**
		 * Override equals so that two Tokens are equal if they have the same Kind, pos,
		 * and length.
		 * 
		 * This method, along with hashcode, was generated by eclipse.
		 */
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Token other = (Token) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (kind != other.kind)
				return false;
			if (length != other.length)
				return false;
			if (pos != other.pos)
				return false;
			return true;
		}

		/**
		 * used in equals to get the cop5556sp18.Scanner object this Token is associated with.
		 * 
		 * @return
		 */
		private Scanner getOuterType() {
			return Scanner.this;
		}

	}// Token

	/**
	 * Array of positions of beginning of lines. lineStarts[k] is the pos of the
	 * first character in line k (starting at 0).
	 * 
	 * If the input is empty, the chars array will have one element, the synthetic
	 * EOFChar token and lineStarts will have size 1 with lineStarts[0] = 0;
	 */
	int[] lineStarts;

	int[] initLineStarts() {
		ArrayList<Integer> lineStarts = new ArrayList<Integer>();
		int pos = 0;

		for (pos = 0; pos < chars.length; pos++) {
			lineStarts.add(pos);
			char ch = chars[pos];
			while (ch != EOFChar && ch != '\n' && ch != '\r') {
				pos++;
				ch = chars[pos];
			}
			if (ch == '\r' && chars[pos + 1] == '\n') {
				pos++;
			}
		}
		// convert arrayList<Integer> to int[]
		return lineStarts.stream().mapToInt(Integer::valueOf).toArray();
	}

	int line(int pos) {
		int line = Arrays.binarySearch(lineStarts, pos);
		if (line < 0) {
			line = -line - 2;
		}
		return line;
	}

	public int posInLine(int pos, int line) {
		return pos - lineStarts[line];
	}

	public int posInLine(int pos) {
		int line = line(pos);
		return posInLine(pos, line);
	}

	/**
	 * Sentinal character added to the end of the input characters.
	 */
	static final char EOFChar = 128;

	/**
	 * The list of tokens created by the scan method.
	 */
	final ArrayList<Token> tokens;

	/**
	 * An array of characters representing the input. These are the characters from
	 * the input string plus an additional EOFchar at the end.
	 */
	final char[] chars;

	/**
	 * position of the next token to be returned by a call to nextToken
	 */
	private int nextTokenPos = 0;

	Scanner(String inputString) {
		int numChars = inputString.length();
		this.chars = Arrays.copyOf(inputString.toCharArray(), numChars + 1); // input string terminated with null char
		chars[numChars] = EOFChar;
		tokens = new ArrayList<Token>();
		lineStarts = initLineStarts();
	}

	private enum State {
		START,
		IN_OP_NEQ_OR_OP_ASSIGN_OR_OP_LE_OR_OP_GE,
		FlOATINGPOINTLITERAL_BEHIND_DOT,
		STATE3,
		IN_DIGIT,
		IN_OP_EQ,
		IN_IDENT,
		IN_ZERO
	};

	public Scanner scan() throws LexicalException {
		HashMap<String, Kind> keywordKind = new HashMap<>();
		keywordKind.put("Z", Kind.KW_Z);keywordKind.put("default_width", Kind.KW_default_width);
		keywordKind.put("default_height", Kind.KW_default_height);keywordKind.put("show", Kind.KW_show);
		keywordKind.put("write", Kind.KW_write);keywordKind.put("to", Kind.KW_to);
		keywordKind.put("input", Kind.KW_input);keywordKind.put("from", Kind.KW_from);
		keywordKind.put("cart_x", Kind.KW_cart_x);keywordKind.put("cart_y", Kind.KW_cart_y);
		keywordKind.put("polar_a", Kind.KW_polar_a);keywordKind.put("polar_r", Kind.KW_polar_r);
		keywordKind.put("abs", Kind.KW_abs);keywordKind.put("sin", Kind.KW_sin);
		keywordKind.put("cos", Kind.KW_cos);keywordKind.put("atan", Kind.KW_atan);
		keywordKind.put("log", Kind.KW_log);keywordKind.put("image", Kind.KW_image);
		keywordKind.put("int", Kind.KW_int);keywordKind.put("float", Kind.KW_float);
		keywordKind.put("filename", Kind.KW_filename);keywordKind.put("boolean", Kind.KW_boolean);
		keywordKind.put("red", Kind.KW_red);keywordKind.put("blue", Kind.KW_blue);
		keywordKind.put("green", Kind.KW_green);keywordKind.put("alpha", Kind.KW_alpha);
		keywordKind.put("while", Kind.KW_while);keywordKind.put("if", Kind.KW_if);
		keywordKind.put("width", Kind.KW_width);keywordKind.put("height", Kind.KW_height);
		keywordKind.put("sleep", Kind.KW_sleep);
		boolean FloatTokenJustEnd = false;
		int pos = 0;
		State state = State.START;
		int startPos = 0;
		while (pos < chars.length) {
			char ch = chars[pos];
			switch(state) {
			
				//<State id="0">
				case START: {
					if (chars[pos]!='.')
						startPos = pos;
					else {
						if (pos>0) {
							if (Character.isDigit(chars[pos-1])) {
								if ((tokens.size())>0) {
									if ((tokens.get(tokens.size()-1).kind)==Kind.FLOAT_LITERAL) {
										if (FloatTokenJustEnd) {
											startPos = pos;									
											FloatTokenJustEnd = false;
										}
									} else {
										FloatTokenJustEnd = false;
									}
								} else {
								}
							} else {
								startPos = pos;
							}
						} else {
						}
					}
					switch (ch) {
						//<Transition char="32" stateId="0" />
						case ' ':
					    //<Transition char="10" stateId="0" />
						case '\n':
						//<Transition char="13" stateId="0" />
						case '\r':
						// <Transition char="9" stateId="0" />
						case '\t':
						//<Transition char="12" stateId="0" />
						case '\f': {
							FloatTokenJustEnd = false;
							state = State.START;// START <State id="0">
							pos++;
						}
						break;
						//<Transition char="33" stateId="1" />
						case '!': {
							state = State.IN_OP_NEQ_OR_OP_ASSIGN_OR_OP_LE_OR_OP_GE;
						}
						break;
				        //<Transition char="37" stateId="0" />
						case '%': {
							tokens.add(new Token(Kind.OP_MOD, startPos, pos - startPos + 1));
							state = State.START;// START <State id="0">
							pos++;
						}
						break;
				        //<Transition char="38" stateId="0" />
						case '&': {
							tokens.add(new Token(Kind.OP_AND, startPos, pos - startPos + 1));
							state = State.START;// START <State id="0">
							pos++;
						}
						break;
				        //<Transition char="40" stateId="0" />
						case '(': {
							tokens.add(new Token(Kind.LPAREN, startPos, pos - startPos + 1));
							state = State.START;// START <State id="0">
							pos++;
						}
						break;
				        //<Transition char="41" stateId="0" />
						case ')': {
							tokens.add(new Token(Kind.RPAREN, startPos, pos - startPos + 1));
							state = State.START;// START <State id="0">
							pos++;
						}
						break;
						//<Transition char="42" stateId="0" />
						case '*': {
							if (chars[pos+1]!='*') {
								tokens.add(new Token(Kind.OP_TIMES, startPos, pos - startPos + 1));
								pos++;
							} else {
								tokens.add(new Token(Kind.OP_POWER, startPos, 2));
								pos+=2;								
							}
							state = State.START;// START <State id="0">
						}
						break;
						//<Transition char="43" stateId="0" />
						case '+': {
							tokens.add(new Token(Kind.OP_PLUS, startPos, pos - startPos + 1));
							state = State.START;// START <State id="0">
							pos++;
						}
						break;
						//<Transition char="44" stateId="0" />
						case ',': {
							tokens.add(new Token(Kind.COMMA, startPos, pos - startPos + 1));
							state = State.START;// START <State id="0">
							pos++;
						}
						break;
						//<Transition char="45" stateId="0" />
						case '-': {
							tokens.add(new Token(Kind.OP_MINUS, startPos, pos - startPos + 1));
							state = State.START;// START <State id="0">
							pos++;
						}
						break;
				        //<Transition char="46" stateId="2" />
						case '.': {
							state = State.FlOATINGPOINTLITERAL_BEHIND_DOT;
							pos++;
						}
						break;
				        //<Transition char="47" stateId="3" />
						case '/': {
							state = State.STATE3;
						}
						break;
				        //<Transition char="58" stateId="1" />
						case ':': {
							state = State.IN_OP_NEQ_OR_OP_ASSIGN_OR_OP_LE_OR_OP_GE;
						}
						break;
						//<Transition char="59" stateId="0" />
						case ';': {
							tokens.add(new Token(Kind.SEMI, startPos, pos - startPos + 1));
							state = State.START;// START <State id="0">
							pos++;
						}
						break;
						//<Transition char="60" stateId="1" />
						case '<': {
							state = State.IN_OP_NEQ_OR_OP_ASSIGN_OR_OP_LE_OR_OP_GE;
						}
						break;
				        //<Transition char="61" stateId="5" />
						case '=': {
							state = State.IN_OP_EQ;
							pos++;
						}
						break;
				        //<Transition char="62" stateId="1" />
						case '>': {
							state = State.IN_OP_NEQ_OR_OP_ASSIGN_OR_OP_LE_OR_OP_GE;
						}
						break;
						//<Transition char="63" stateId="0" />
						case '?': {
							tokens.add(new Token(Kind.OP_QUESTION, startPos, pos - startPos + 1));
							state = State.START;// START <State id="0">
							pos++;
						}
						break;
						//<Transition char="64" stateId="0" />
						case '@': {
							tokens.add(new Token(Kind.OP_AT, startPos, pos - startPos + 1));
							state = State.START;// START <State id="0">
							pos++;
						}
						break;
						//<Transition char="91" stateId="0" />
						case '[': {
							tokens.add(new Token(Kind.LSQUARE, startPos, pos - startPos + 1));
							state = State.START;// START <State id="0">
							pos++;
						}
						break;
						//<Transition char="93" stateId="0" />
						case ']': {
							tokens.add(new Token(Kind.RSQUARE, startPos, pos - startPos + 1));
							state = State.START;// START <State id="0">
							pos++;
						}
						break;
						//<Transition char="123" stateId="0" />
						case '{': {
							tokens.add(new Token(Kind.LBRACE, startPos, pos - startPos + 1));
							state = State.START;// START <State id="0">
							pos++;
						}
						break;
						//<Transition char="124" stateId="0" />
						case '|': {
							tokens.add(new Token(Kind.OP_OR, startPos, pos - startPos + 1));
							state = State.START;// START <State id="0">
							pos++;
						}
						break;
						//<Transition char="125" stateId="0" />
						case '}': {
							tokens.add(new Token(Kind.RBRACE, startPos, pos - startPos + 1));
							state = State.START;// START <State id="0">
							pos++;
						}
						break;
						case EOFChar: {
							tokens.add(new Token(Kind.EOF, startPos, 0));
							pos++; // next iteration will terminate loop
						}
						break;
						default: {							
							if (Character.isDigit(ch)) {
								if (ch=='0') {
									state = State.IN_ZERO;
								} else {
									// <Transition char="48-57" stateId="4" />
									state = State.IN_DIGIT;
									pos++;
								}
							} else if (Character.isUpperCase(ch) || Character.isLowerCase(ch)) {
								/*
								// A-Z
						        <Transition char="65-90" stateId="6" />
						        // a-z
						        <Transition char="97-122" stateId="6" />
								*/
				            	state = State.IN_IDENT;
				            	startPos = pos;
				            } else 
				            	error(pos, line(pos), posInLine(pos), "illegal char");
						}
					}//switch ch
				}//case START <State id="0">
				break;
				
				//<State id="1">
				case IN_OP_NEQ_OR_OP_ASSIGN_OR_OP_LE_OR_OP_GE: {
					startPos = pos;
					switch (ch) {
						//<Transition char="32" stateId="0" />
						case ' ':
					    //<Transition char="10" stateId="0" />
						case '\n':
						//<Transition char="13" stateId="0" />
						case '\r':
						// <Transition char="9" stateId="0" />
						case '\t':
						//<Transition char="12" stateId="0" />
						case '\f': {
							FloatTokenJustEnd = false;
							state = State.START;// START <State id="0">
							pos++;
						}
						break;
						//<Transition char="33" stateId="1" />
						case '!': {
							if (chars[pos+1]!='=')
								tokens.add(new Token(Kind.OP_EXCLAMATION, startPos, pos - startPos + 1));
							state = State.IN_OP_NEQ_OR_OP_ASSIGN_OR_OP_LE_OR_OP_GE;
							pos++;
						}
						break;
				        //<Transition char="37" stateId="0" />
						case '%':
				        //<Transition char="38" stateId="0" />
						case '&':
				        //<Transition char="40" stateId="0" />
						case '(':
				        //<Transition char="41" stateId="0" />
						case ')':
						//<Transition char="42" stateId="0" />
						case '*':
						//<Transition char="43" stateId="0" />
						case '+':
						//<Transition char="44" stateId="0" />
						case ',':
						//<Transition char="45" stateId="0" />
						case '-': {
							state = State.START;// START <State id="0">
						}
						break;
				        //<Transition char="46" stateId="2" />
						case '.': {
							state = State.FlOATINGPOINTLITERAL_BEHIND_DOT;
							pos++;
						}
						break;
				        //<Transition char="47" stateId="3" />
						case '/': {
							state = State.STATE3;
						}
						break;
				        //<Transition char="58" stateId="1" />
						case ':': {
							if (chars[pos+1]!='=')
								tokens.add(new Token(Kind.OP_COLON, startPos, pos - startPos + 1));
							state = State.IN_OP_NEQ_OR_OP_ASSIGN_OR_OP_LE_OR_OP_GE;
							pos++;
						}
						break;
						//<Transition char="59" stateId="0" />
						case ';': {
							state = State.START;// START <State id="0">
						}
						break;
						//<Transition char="60" stateId="1" />
						case '<': {							
							if (chars[pos+1]=='<') {
								tokens.add(new Token(Kind.LPIXEL, pos, 2));
								pos+=2;
							} else if (chars[pos+1]=='=') {
								pos++;
							} else {
								tokens.add(new Token(Kind.OP_LT, startPos, pos - startPos + 1));
								pos++;
							}							
							state = State.IN_OP_NEQ_OR_OP_ASSIGN_OR_OP_LE_OR_OP_GE;
						}
						break;
				        //<Transition char="61" stateId="0" />
						case '=': { // (!= or := or <= or >=)'s second =
							if (chars[pos-1]=='!') {
								tokens.add(new Token(Kind.OP_NEQ, pos-1, 2));
								state = State.START;
							} else if (chars[pos-1]==':') {
								tokens.add(new Token(Kind.OP_ASSIGN, pos-1, 2));
								state = State.START;
							} else if (chars[pos-1]=='<') {
								Kind lastTokenKind = null;
								if (tokens.size()>0)
									lastTokenKind = tokens.get(tokens.size()-1).kind;
								if (lastTokenKind!=Kind.LPIXEL) {
									// this '<' on chars[pos-1] is not the second '<' of token "<<" last got
									tokens.add(new Token(Kind.OP_LE, pos-1, 2));
								} else {
									error(pos, line(pos), posInLine(pos), "illegal char: =");
								}
								state = State.START;
							} else if (chars[pos-1]=='>') {
								Kind lastTokenKind = null;
								if (tokens.size()>0)
									lastTokenKind = tokens.get(tokens.size()-1).kind;
								if (lastTokenKind!=Kind.RPIXEL) {
									// this '>' on chars[pos-1] is not the second '>' of token ">>" last got
									tokens.add(new Token(Kind.OP_GE, pos-1, 2));
								} else {
									error(pos, line(pos), posInLine(pos), "illegal char: =");
								}
								state = State.START;
							} else
								error(pos, line(pos), posInLine(pos), "illegal char");
							pos++;
						}
						break;
				        //<Transition char="62" stateId="1" />
						case '>': {
							if (chars[pos+1]=='>') {
								tokens.add(new Token(Kind.RPIXEL, pos, 2));
								pos+=2;
							} else if (chars[pos+1]=='=') {
								pos++;
							} else {
								tokens.add(new Token(Kind.OP_GT, startPos, pos - startPos + 1));
								pos++;
							}							
							state = State.IN_OP_NEQ_OR_OP_ASSIGN_OR_OP_LE_OR_OP_GE;
						}
						break;
						//<Transition char="63" stateId="0" />
						case '?':
						//<Transition char="64" stateId="0" />
						case '@':
						//<Transition char="91" stateId="0" />
						case '[':
						//<Transition char="93" stateId="0" />
						case ']':
						//<Transition char="123" stateId="0" />
						case '{':
						//<Transition char="124" stateId="0" />
						case '|':
						//<Transition char="125" stateId="0" />
						case '}': {
							state = State.START;// START <State id="0">
						}
						break;
						case EOFChar: {
							tokens.add(new Token(Kind.EOF, startPos, 0));
							pos++; // next iteration will terminate loop
						}
						break;
						default: {							
							if (Character.isDigit(ch)) {
								if (ch=='0') {
									state = State.IN_ZERO;
								} else {
									// <Transition char="48-57" stateId="4" />
									state = State.IN_DIGIT;
									pos++;
								}
							} else if (Character.isUpperCase(ch) || Character.isLowerCase(ch)) {
								/*
								// A-Z
						        <Transition char="65-90" stateId="6" />
						        // a-z
						        <Transition char="97-122" stateId="6" />
								*/
				            	state = State.IN_IDENT;
				            	startPos = pos;
				            } else 
				            	error(pos, line(pos), posInLine(pos), "illegal char");
						}
					}//switch ch
				}//case IN_OP_NEQ_OR_OP_ASSIGN_OR_OP_LE_OR_OP_GE <State id="1">
				break;
				
				//<State id="2">
				case FlOATINGPOINTLITERAL_BEHIND_DOT: {
					switch (ch) {
						//<Transition char="32" stateId="0" />
						case ' ':
					    //<Transition char="10" stateId="0" />
						case '\n':
						//<Transition char="13" stateId="0" />
						case '\r':
						// <Transition char="9" stateId="0" />
						case '\t':
						//<Transition char="12" stateId="0" />
						case '\f': {								
							try {
								if (Float.isFinite(Float.valueOf(String.copyValueOf(chars, startPos, pos-startPos))))
									tokens.add(new Token(Kind.FLOAT_LITERAL, startPos, pos-startPos));
								else
									error(startPos, line(startPos), posInLine(startPos), "Numeric literal is out of the range of the Java equivalent.");
							} catch (Exception e) {
								tokens.add(new Token(Kind.DOT, startPos, 1));
							}
							FloatTokenJustEnd = false;
							state = State.START;
							pos++;
						}
						break;
						case '.': {
							boolean isADigitPreThisDOT, isADigitAfterThisDot;
							if (pos>=1) {
								isADigitPreThisDOT = Character.isDigit(chars[pos-1]);
							} else {
								isADigitPreThisDOT = false;
							}
							if ((pos+1)<(chars.length-1)) {
								isADigitAfterThisDot = Character.isDigit(chars[pos+1]);
							} else {
								isADigitAfterThisDot = false;
							}
							if (!isADigitPreThisDOT && !isADigitAfterThisDot) {
								try {
									if (Float.isFinite(Float.valueOf(String.copyValueOf(chars, startPos, pos-startPos))))
										tokens.add(new Token(Kind.FLOAT_LITERAL, startPos, pos-startPos));
									else
										error(startPos, line(startPos), posInLine(startPos), "Numeric literal is out of the range of the Java equivalent.");
								} catch (Exception e) {
									tokens.add(new Token(Kind.DOT, startPos, 1));
								}
								tokens.add(new Token(Kind.DOT, pos, 1));
								state = State.START;
								pos++;
							} else {
								if (Float.isFinite(Float.valueOf(String.copyValueOf(chars, startPos, pos-startPos))))
									tokens.add(new Token(Kind.FLOAT_LITERAL, startPos, pos-startPos));
								else
									error(startPos, line(startPos), posInLine(startPos), "Numeric literal is out of the range of the Java equivalent.");
								state = State.START;
							}
						}
						break;
						case EOFChar: {
							if (pos==1) {
								tokens.add(new Token(Kind.DOT, 0, 1));
								tokens.add(new Token(Kind.EOF, 1, 0));
								pos++; // next iteration will terminate loop
							} else {
								try {
									if (Float.isFinite(Float.valueOf(String.copyValueOf(chars, startPos, pos-startPos))))
										tokens.add(new Token(Kind.FLOAT_LITERAL, startPos, pos-startPos));
									else
										error(startPos, line(startPos), posInLine(startPos), "Numeric literal is out of the range of the Java equivalent.");
								} catch (Exception e) {
									tokens.add(new Token(Kind.DOT, startPos, 1));
								}								
								state = State.START;								
							}
						}
						break;
						default: {
							if (Character.isDigit(ch)) {
						        // <Transition char="48-57" stateId="0" />
								state = State.START;								
								int i=pos+1;
								for (; i<chars.length; i++) {
									if (!(Character.isDigit(chars[i]))) {
										break;
									}
								}
								pos=i;
								if (Float.isFinite(Float.valueOf(String.copyValueOf(chars, startPos, pos-startPos))))
									tokens.add(new Token(Kind.FLOAT_LITERAL, startPos, pos-startPos));
								else
									error(startPos, line(startPos), posInLine(startPos), "Numeric literal is out of the range of the Java equivalent.");
								FloatTokenJustEnd = true;
							} else if (Character.isUpperCase(ch) || Character.isLowerCase(ch)) {
								String tokenStr = String.valueOf(Arrays.copyOfRange(chars, startPos, pos));
								if (tokenStr.equals(".")) {
									tokens.add(new Token(Kind.DOT, pos-1, 1));								
								} else {
									if (Float.isFinite(Float.valueOf(String.copyValueOf(chars, startPos, pos-startPos))))
										tokens.add(new Token(Kind.FLOAT_LITERAL, startPos, pos-startPos));
									else
										error(startPos, line(startPos), posInLine(startPos), "Numeric literal is out of the range of the Java equivalent.");
								}
								/*
								// A-Z
						        <Transition char="65-90" stateId="6" />
						        // a-z
						        <Transition char="97-122" stateId="6" />
								*/
								state = State.IN_IDENT;
								startPos = pos;
							} else {
								try {
									if (Float.isFinite(Float.valueOf(String.copyValueOf(chars, startPos, pos-startPos))))
										tokens.add(new Token(Kind.FLOAT_LITERAL, startPos, pos-startPos));
									else
										error(startPos, line(startPos), posInLine(startPos), "Numeric literal is out of the range of the Java equivalent.");
								} catch (Exception e) {
									tokens.add(new Token(Kind.DOT, startPos, 1));
								}								
								state = State.START;
							}
						}
					}//switch ch
				}//case FlOATINGPOINTLITERAL_BEHIND_DOT <State id="2">
				break;
				
				//<State id="3">
				case STATE3: {
					startPos = pos;
					switch (ch) {
					    //<Transition char="10" stateId="0" />
						case '\n':
						//<Transition char="13" stateId="0" />
						case '\r':
						// <Transition char="9" stateId="0" />
						case '\t':
						//<Transition char="12" stateId="0" />
						case '\f': {
							FloatTokenJustEnd = false;
							state = State.START;// START <State id="0">
							pos++;
						}
						break;
						//<Transition char="32" stateId="7" />
						case ' ': {
							state = State.START;
							pos++;
						}
						break;
						//<Transition char="33" stateId="1" />
						case '!': {
							state = State.IN_OP_NEQ_OR_OP_ASSIGN_OR_OP_LE_OR_OP_GE;
						}
						break;
				        //<Transition char="37" stateId="0" />
						case '%':
				        //<Transition char="38" stateId="0" />
						case '&':
				        //<Transition char="40" stateId="0" />
						case '(':
				        //<Transition char="41" stateId="0" />
						case ')':
						//<Transition char="42" stateId="0" />
						case '*':
						//<Transition char="43" stateId="0" />
						case '+':
						//<Transition char="44" stateId="0" />
						case ',':
						//<Transition char="45" stateId="0" />
						case '-': {
							state = State.START;// START <State id="0">
						}
						break;
				        //<Transition char="46" stateId="2" />
						case '.': {
							state = State.FlOATINGPOINTLITERAL_BEHIND_DOT;
							pos++;
						}
						break;
				        //<Transition char="47" stateId="3" />
						case '/': {
							if (chars[pos+1]=='*') {// Comment begin
								boolean found = false; // find Comment end: "*/"
								for (int i=pos+2;i<((chars.length)-2);i++)
									if (chars[i]=='*' && chars[i+1]=='/') {
										found = true;
										pos=i+2;
										break;
									}
								if (!found)
						            error(startPos, line(startPos), posInLine(startPos), "illegal Comment");
							} else {
								tokens.add(new Token(Kind.OP_DIV, startPos, pos - startPos + 1));
								pos++;
							}							
							state = State.STATE3;
						}
						break;
				        //<Transition char="58" stateId="1" />
						case ':': {
							state = State.IN_OP_NEQ_OR_OP_ASSIGN_OR_OP_LE_OR_OP_GE;
						}
						break;
						//<Transition char="59" stateId="0" />
						case ';': {
							state = State.START;// START <State id="0">
						}
						break;
						//<Transition char="60" stateId="1" />
						case '<': {
							state = State.IN_OP_NEQ_OR_OP_ASSIGN_OR_OP_LE_OR_OP_GE;
						}
						break;
				        //<Transition char="61" stateId="5" />
						case '=': {
							state = State.IN_OP_EQ;
							pos++;
						}
						break;
				        //<Transition char="62" stateId="1" />
						case '>': {
							state = State.IN_OP_NEQ_OR_OP_ASSIGN_OR_OP_LE_OR_OP_GE;
						}
						break;
						//<Transition char="63" stateId="0" />
						case '?':
						//<Transition char="64" stateId="0" />
						case '@':
						//<Transition char="91" stateId="0" />
						case '[':
						//<Transition char="93" stateId="0" />
						case ']':
						//<Transition char="123" stateId="0" />
						case '{':
						//<Transition char="124" stateId="0" />
						case '|':
						//<Transition char="125" stateId="0" />
						case '}': {
							state = State.START;// START <State id="0">
						}
						break;
						case EOFChar: {
							tokens.add(new Token(Kind.EOF, startPos, 0));
							pos++; // next iteration will terminate loop
						}
						break;
						default: {							
							if (Character.isDigit(ch)) {
								if (ch=='0') {
									state = State.IN_ZERO;
								} else {
									// <Transition char="48-57" stateId="4" />
									state = State.IN_DIGIT;
									pos++;
								}
							} else if (Character.isUpperCase(ch) || Character.isLowerCase(ch)) {
								/*
								// A-Z
						        <Transition char="65-90" stateId="6" />
						        // a-z
						        <Transition char="97-122" stateId="6" />
								*/
				            	state = State.IN_IDENT;
				            	startPos = pos;
				            } else 
				            	error(pos, line(pos), posInLine(pos), "illegal char");
						}
					}//switch ch
				}//case STATE3 <State id="3">
				break;
				
				//<State id="4">
				case IN_DIGIT: {
					switch (ch) {						
						//<Transition char="32" stateId="0" />
						case ' ':
					    //<Transition char="10" stateId="0" />
						case '\n':
						//<Transition char="13" stateId="0" />
						case '\r':
						// <Transition char="9" stateId="0" />
						case '\t':
						//<Transition char="12" stateId="0" />
						case '\f': {
							if (pos<=(1+startPos) || chars[startPos]!='0') {
								try {
									@SuppressWarnings("unused")
									Integer tempInt = (Integer.valueOf(String.copyValueOf(chars, startPos, pos-startPos)));
									tokens.add(new Token(Kind.INTEGER_LITERAL, startPos, pos-startPos));
								} catch (Exception e) {
									error(startPos, line(startPos), posInLine(startPos), "Numeric literal is out of the range of the Java equivalent.");
								}
							} else
								error(startPos, line(startPos), posInLine(startPos), "Non-zero integers can not start with zero");
							FloatTokenJustEnd = false;
							state = State.START;// START <State id="0">
							pos++;
						}
						break;
						//<Transition char="33" stateId="1" />
						case '!': {
							if (pos<=(1+startPos) || chars[startPos]!='0') {
								try {
									@SuppressWarnings("unused")
									Integer tempInt = (Integer.valueOf(String.copyValueOf(chars, startPos, pos-startPos)));
									tokens.add(new Token(Kind.INTEGER_LITERAL, startPos, pos-startPos));
								} catch (Exception e) {
									error(startPos, line(startPos), posInLine(startPos), "Numeric literal is out of the range of the Java equivalent.");
								}
							} else
								error(startPos, line(startPos), posInLine(startPos), "Non-zero integers can not start with zero");
							state = State.IN_OP_NEQ_OR_OP_ASSIGN_OR_OP_LE_OR_OP_GE;
						}
						break;
				        //<Transition char="37" stateId="0" />
						case '%':
				        //<Transition char="38" stateId="0" />
						case '&':
				        //<Transition char="40" stateId="0" />
						case '(':
				        //<Transition char="41" stateId="0" />
						case ')':
						//<Transition char="42" stateId="0" />
						case '*':
						//<Transition char="43" stateId="0" />
						case '+':
						//<Transition char="44" stateId="0" />
						case ',':
						//<Transition char="45" stateId="0" />
						case '-':
						//<Transition char="59" stateId="0" />
						case ';':
						//<Transition char="63" stateId="0" />
						case '?':
						//<Transition char="64" stateId="0" />
						case '@':
						//<Transition char="91" stateId="0" />
						case '[':
						//<Transition char="93" stateId="0" />
						case ']':
						//<Transition char="123" stateId="0" />
						case '{':
						//<Transition char="124" stateId="0" />
						case '|':
					    //<Transition char="125" stateId="0" />
						case '}': {
							if (pos<=(1+startPos) || chars[startPos]!='0') {
								try {
									@SuppressWarnings("unused")
									Integer tempInt = (Integer.valueOf(String.copyValueOf(chars, startPos, pos-startPos)));
									tokens.add(new Token(Kind.INTEGER_LITERAL, startPos, pos-startPos));
								} catch (Exception e) {
									error(startPos, line(startPos), posInLine(startPos), "Numeric literal is out of the range of the Java equivalent.");
								}
							} else
								error(startPos, line(startPos), posInLine(startPos), "Non-zero integers can not start with zero");
							state = State.START;
						}
						break;
				        //<Transition char="46" stateId="0" />
						case '.': {
							state = State.START;
						}
						break;
				        //<Transition char="47" stateId="3" />
						case '/': {
							if (pos<=(1+startPos) || chars[startPos]!='0') {
								try {
									@SuppressWarnings("unused")
									Integer tempInt = (Integer.valueOf(String.copyValueOf(chars, startPos, pos-startPos)));
									tokens.add(new Token(Kind.INTEGER_LITERAL, startPos, pos-startPos));
								} catch (Exception e) {
									error(startPos, line(startPos), posInLine(startPos), "Numeric literal is out of the range of the Java equivalent.");
								}
							} else
								error(startPos, line(startPos), posInLine(startPos), "Non-zero integers can not start with zero");
							state = State.STATE3;
						}
						break;
				        //<Transition char="61" stateId="5" />
						case '=': {
							if (pos<=(1+startPos) || chars[startPos]!='0') {
								try {
									@SuppressWarnings("unused")
									Integer tempInt = (Integer.valueOf(String.copyValueOf(chars, startPos, pos-startPos)));
									tokens.add(new Token(Kind.INTEGER_LITERAL, startPos, pos-startPos));
								} catch (Exception e) {
									error(startPos, line(startPos), posInLine(startPos), "Numeric literal is out of the range of the Java equivalent.");
								}
							} else
								error(startPos, line(startPos), posInLine(startPos), "Non-zero integers can not start with zero");
							state = State.IN_OP_EQ;
							pos++;
						}
						break;
				        //<Transition char="58" stateId="1" />
						case ':':
						//<Transition char="60" stateId="1" />
						case '<':
						//<Transition char="62" stateId="1" />
						case '>': {
							if (pos<=(1+startPos) || chars[startPos]!='0') {
								try {
									@SuppressWarnings("unused")
									Integer tempInt = (Integer.valueOf(String.copyValueOf(chars, startPos, pos-startPos)));
									tokens.add(new Token(Kind.INTEGER_LITERAL, startPos, pos-startPos));
								} catch (Exception e) {
									error(startPos, line(startPos), posInLine(startPos), "Numeric literal is out of the range of the Java equivalent.");
								}
							} else
								error(startPos, line(startPos), posInLine(startPos), "Non-zero integers can not start with zero");
							state = State.IN_OP_NEQ_OR_OP_ASSIGN_OR_OP_LE_OR_OP_GE;
						}
						break;
						case EOFChar: {
							if (pos<=(1+startPos) || chars[startPos]!='0') {
								try {
									@SuppressWarnings("unused")
									Integer tempInt = (Integer.valueOf(String.copyValueOf(chars, startPos, pos-startPos)));
									tokens.add(new Token(Kind.INTEGER_LITERAL, startPos, pos-startPos));
								} catch (Exception e) {
									error(startPos, line(startPos), posInLine(startPos), "Numeric literal is out of the range of the Java equivalent.");
								}
							} else
								error(startPos, line(startPos), posInLine(startPos), "Non-zero integers can not start with zero");
							tokens.add(new Token(Kind.EOF, pos, 0));						
							pos++; // next iteration will terminate loop
						}
						break;
						default: {
							if (Character.isDigit(ch)) {
								// <Transition char="48-57" stateId="4" />
								state = State.IN_DIGIT;
								pos++;
							} else if (Character.isUpperCase(ch) || Character.isLowerCase(ch)) {
								if (pos<=(1+startPos) || chars[startPos]!='0') {
									try {
										@SuppressWarnings("unused")
										Integer tempInt = (Integer.valueOf(String.copyValueOf(chars, startPos, pos-startPos)));
										tokens.add(new Token(Kind.INTEGER_LITERAL, startPos, pos-startPos));
									} catch (Exception e) {
										error(startPos, line(startPos), posInLine(startPos), "Numeric literal is out of the range of the Java equivalent.");
									}
								} else
									error(startPos, line(startPos), posInLine(startPos), "Non-zero integers can not start with zero");
								/*
								// A-Z
						        <Transition char="65-90" stateId="6" />
						        // a-z
						        <Transition char="97-122" stateId="6" />
								*/
				            	state = State.IN_IDENT;
				            	startPos = pos;
				            } else 
				            	error(pos, line(pos), posInLine(pos), "illegal char");
						}
					}//switch ch
				}//case IN_DIGIT <State id="4">
				break;
				
				//<State id="5">
				case IN_OP_EQ: {
					startPos = pos;
					switch (ch) {
						//<Transition char="61" stateId="0" />
						case '=': { // =='s second =
							tokens.add(new Token(Kind.OP_EQ, pos-1, 2));
							state = State.START;// START <State id="0">
							pos++;
						}
						break;
						default: {	
							error(pos-1, line(pos-1), posInLine(pos-1), "illegal char: =");
						}
					}//switch ch
				}//case IN_OP_EQ <State id="5">
				break;
				
				//<State id="6">
				case IN_IDENT: {
					switch (ch) {
					    //<Transition char="32" stateId="0" />
						case ' ':
					    //<Transition char="10" stateId="0" />
						case '\n':
						//<Transition char="13" stateId="0" />
						case '\r':
						// <Transition char="9" stateId="0" />
						case '\t':
						//<Transition char="12" stateId="0" />
						case '\f':		        
				        //<Transition char="33" stateId="0" />
						case '!':
					    //<Transition char="58" stateId="0" />
						case ':':
					    //<Transition char="60" stateId="0" />
						case '<':
					    //<Transition char="62" stateId="0" />
						case '>':
				        //<Transition char="37" stateId="0" />
						case '%':
				        //<Transition char="38" stateId="0" />
						case '&':
				        //<Transition char="40" stateId="0" />
						case '(':
						//<Transition char="41" stateId="0" />
						case ')':
						//<Transition char="42" stateId="0" />
						case '*':
						//<Transition char="43" stateId="0" />
						case '+':
				        //<Transition char="44" stateId="0" />
						case ',':
				        //<Transition char="45" stateId="0" />
						case '-':					        
						//<Transition char="46" stateId="0" />
						case '.':					        
						//<Transition char="47" stateId="0" />
						case '/':
				        //<Transition char="59" stateId="0" />
						case ';':
						//<Transition char="61" stateId="0" />
						case '=':
				        //<Transition char="63" stateId="0" />
						case '?':
				        //<Transition char="64" stateId="0" />
						case '@':
				        //<Transition char="91" stateId="0" />
						case '[':
				        //<Transition char="93" stateId="0" />
						case ']':
				        //<Transition char="123" stateId="0" />
						case '{':
				        //<Transition char="124" stateId="0" />
						case '|':
				        //<Transition char="125" stateId="0" />
						case '}': 
						case EOFChar: {
							String Identifier = String.valueOf(Arrays.copyOfRange(chars, startPos, pos));
							if (Identifier.equals("true") || Identifier.equals("false"))
								tokens.add(new Token(Kind.BOOLEAN_LITERAL, startPos, pos - startPos));
							else {
								Kind value = keywordKind.get(Identifier);
								if (value!=null) // Keyword
									tokens.add(new Token(value, startPos, pos - startPos));
								else
									tokens.add(new Token(Kind.IDENTIFIER, startPos, pos - startPos));
							}
							if (ch!=EOFChar) {
								FloatTokenJustEnd = false;
								state = State.START; // START <State id="0">
								if (ch==' ' || ch=='\n' || ch=='\r' || ch=='\t' || ch=='\f')
									pos++;
							} else {
								tokens.add(new Token(Kind.EOF, startPos, 0));
								pos++; // next iteration will terminate loop
							}
						}
						break;
						default: {
							if (Character.isDigit(ch) || Character.isUpperCase(ch) || Character.isLowerCase(ch) || 
								ch=='_' || ch=='$') {
						        // <Transition char="36" stateId="6" />
								// <Transition char="95" stateId="6" />
								// <Transition char="48-57" stateId="6" />
								/*
								// A-Z
						        <Transition char="65-90" stateId="6" />
						        // a-z
						        <Transition char="97-122" stateId="6" />
								*/
				            	state = State.IN_IDENT;
				            	pos++;
				            } else 
				            	error(pos, line(pos), posInLine(pos), "illegal char");
						}
					}//switch ch
				}//case IN_IDENT <State id="6">
				break;
				
				//<State id="7">
				case IN_ZERO: {
					switch (ch) {
						case '0': {
							int i=pos+1; // from behind this '0'
							while (i<(chars.length)) {
								if (chars[i]=='0') {
									i++;
								} else {
									if (chars[i]=='.') {
										// output single 0 before chars[i-1] as INTEGER_LITERAL, from index startPos to i-2
										for (int j=startPos; j<(i-1); j++)
											tokens.add(new Token(Kind.INTEGER_LITERAL, j, 1)); // "0"
										//<Transition char="46" stateId="2" />
										startPos = i-1;
										state = State.FlOATINGPOINTLITERAL_BEHIND_DOT;
										pos = i+1;										
									} else {
										for (int j=pos; j<i; j++)
											tokens.add(new Token(Kind.INTEGER_LITERAL, j, 1)); // "0"
										state = State.START;// START <State id="0">
										pos = i;
									}
									break;
								}
							}
						}
						break;
						default: {							
						}
					}					
				}//case IN_ZERO <State id="7">
				break;
				
				default: {
					error(pos, 0, 0, "undefined state");
				}
			}// switch state
		} // while
		return this;
	}

	private void error(int pos, int line, int posInLine, String message) throws LexicalException {
		String m = (line + 1) + ":" + (posInLine + 1) + " " + message;
		throw new LexicalException(m, pos);
	}

	/**
	 * Returns true if the internal iterator has more Tokens
	 * 
	 * @return
	 */
	public boolean hasTokens() {
		return nextTokenPos < tokens.size();
	}

	/**
	 * Returns the next Token and updates the internal iterator so that the next
	 * call to nextToken will return the next token in the list.
	 * 
	 * It is the callers responsibility to ensure that there is another Token.
	 * 
	 * Precondition: hasTokens()
	 * 
	 * @return
	 */
	public Token nextToken() {
		return tokens.get(nextTokenPos++);
	}

	/**
	 * Returns the next Token, but does not update the internal iterator. This means
	 * that the next call to nextToken or peek will return the same Token as
	 * returned by this methods.
	 * 
	 * It is the callers responsibility to ensure that there is another Token.
	 * 
	 * Precondition: hasTokens()
	 * 
	 * @return next Token.
	 */
	public Token peek() {
		return tokens.get(nextTokenPos);
	}

	/**
	 * Resets the internal iterator so that the next call to peek or nextToken will
	 * return the first Token.
	 */
	public void reset() {
		nextTokenPos = 0;
	}

	/**
	 * Returns a String representation of the list of Tokens and line starts
	 */
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("Tokens:\n");
		for (int i = 0; i < tokens.size(); i++) {
			sb.append(tokens.get(i)).append('\n');
		}
		sb.append("Line starts:\n");
		for (int i = 0; i < lineStarts.length; i++) {
			sb.append(i).append(' ').append(lineStarts[i]).append('\n');
		}
		return sb.toString();
	}

}
