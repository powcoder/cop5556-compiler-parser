https://powcoder.com
代写代考加微信 powcoder
Assignment Project Exam Help
Add WeChat powcoder
https://powcoder.com
代写代考加微信 powcoder
Assignment Project Exam Help
Add WeChat powcoder
 /**
 * JUunit tests for the cop5556sp18.Scanner for the class project in COP5556 Programming Language Principles
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

package cop5556sp18;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import cop5556sp18.Scanner.LexicalException;
import cop5556sp18.Scanner.Token;
import static cop5556sp18.Scanner.Kind.*;

public class ScannerTest {

	//set Junit to be able to catch exceptions
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	//To make it easy to print objects and turn this output on and off
	static boolean doPrint = true;
	private void show(Object input) {
		if (doPrint) {
			System.out.println(input.toString());
		}
	}

	/**
	 *Retrieves the next token and checks that it is an EOF token. 
	 *Also checks that this was the last token.
	 *
	 * @param scanner
	 * @return the Token that was retrieved
	 */	
	Token checkNextIsEOF(Scanner scanner) {
		Scanner.Token token = scanner.nextToken();
		assertEquals(Scanner.Kind.EOF, token.kind);
		assertFalse(scanner.hasTokens());
		return token;
	}

	/**
	 * Retrieves the next token and checks that its kind, position, length, line, and position in line
	 * match the given parameters.
	 * 
	 * @param scanner
	 * @param kind
	 * @param pos
	 * @param length
	 * @param line
	 * @param pos_in_line
	 * @return  the Token that was retrieved
	 */
	Token checkNext(Scanner scanner, Scanner.Kind kind, int pos, int length, int line, int pos_in_line) {
		Token t = scanner.nextToken();
		assertEquals(kind, t.kind);
		assertEquals(pos, t.pos);
		assertEquals(length, t.length);
		assertEquals(line, t.line());
		assertEquals(pos_in_line, t.posInLine());
		return t;
	}

	/**
	 * Retrieves the next token and checks that its kind and length match the given
	 * parameters.  The position, line, and position in line are ignored.
	 * 
	 * @param scanner
	 * @param kind
	 * @param length
	 * @return  the Token that was retrieved
	 */
	Token checkNext(Scanner scanner, Scanner.Kind kind, int length) {
		Token t = scanner.nextToken();
		assertEquals(kind, t.kind);
		assertEquals(length, t.length);
		return t;
	}
	
	/**
	 * Simple test case with an empty program.  The only Token will be the EOF Token.
	 *   
	 * @throws LexicalException
	 */
	@Test
	public void testEmpty() throws LexicalException {
		String input = "";  //The input is the empty string.  This is legal
		show(input);        //Display the input 
		Scanner scanner = new Scanner(input).scan();  //Create a cop5556sp18.Scanner and initialize it
		show(scanner);   //Display the cop5556sp18.Scanner
		checkNextIsEOF(scanner);  //Check that the only token is the EOF token.
	}
	
	/**
	 * Test illustrating how to put a new line in the input program and how to
	 * check content of tokens.
	 * 
	 * Because we are using a Java String literal for input, we use \n for the
	 * end of line character. (We should also be able to handle \n, \r, and \r\n
	 * properly.)
	 * 
	 * Note that if we were reading the input from a file, the end of line 
	 * character would be inserted by the text editor.
	 * Showing the input will let you check your input is 
	 * what you think it is.
	 * 
	 * @throws LexicalException
	 */
	@Test
	public void testSemi() throws LexicalException {
		String input = ";;\n;;";
		Scanner scanner = new Scanner(input).scan();
		show(input);
		show(scanner);
		checkNext(scanner, SEMI, 0, 1, 1, 1);
		checkNext(scanner, SEMI, 1, 1, 1, 2);
		checkNext(scanner, SEMI, 3, 1, 2, 1);
		checkNext(scanner, SEMI, 4, 1, 2, 2);
		checkNextIsEOF(scanner);
	}
	
	/**
	 * This example shows how to test that your scanner is behaving when the
	 * input is illegal.  In this case, we are giving it an illegal character '~' in position 2
	 * 
	 * The example shows catching the exception that is thrown by the scanner,
	 * looking at it, and checking its contents before rethrowing it.  If caught
	 * but not rethrown, then JUnit won't get the exception and the test will fail.  
	 * 
	 * The test will work without putting the try-catch block around 
	 * new cop5556sp18.Scanner(input).scan(); but then you won't be able to check
	 * or display the thrown exception.
	 * 
	 * @throws LexicalException
	 */
	@Test
	public void failIllegalChar() throws LexicalException {
		String input = ";;~";
		show(input);
		thrown.expect(LexicalException.class);  //Tell JUnit to expect a LexicalException
		try {
			new Scanner(input).scan();
		} catch (LexicalException e) {  //Catch the exception
			show(e);                    //Display it
			assertEquals(2,e.getPos()); //Check that it occurred in the expected position
			throw e;                    //Rethrow exception so JUnit will see it
		}
	}

	@Test
	public void testParens() throws LexicalException {
		String input = "()";
		Scanner scanner = new Scanner(input).scan();
		show(input);
		show(scanner);
		checkNext(scanner, LPAREN, 0, 1, 1, 1);
		checkNext(scanner, RPAREN, 1, 1, 1, 2);
		checkNextIsEOF(scanner);
	}

	@Test
	public void test_OP_LE() throws LexicalException {
		String input = "<=";
		Scanner scanner = new Scanner(input).scan();
		show(input);
		show(scanner);
		checkNext(scanner, OP_LE, 0, 2, 1, 1);
		checkNextIsEOF(scanner);
	}
	
	@Test
	public void test_OP_GEAndSemi() throws LexicalException {
		String input = "\t>=\r\n ;";
		Scanner scanner = new Scanner(input).scan();
		show(input);
		show(scanner);
		checkNext(scanner, OP_GE, 1, 2, 1, 2);
		checkNext(scanner, SEMI, 6, 1, 2, 2);
		checkNextIsEOF(scanner);
	}
	
	@Test
	public void testDOT() throws LexicalException {
		String input = ".";
		Scanner scanner = new Scanner(input).scan();
		show(input);
		show(scanner);
		checkNext(scanner, DOT, 0, 1, 1, 1);
		checkNextIsEOF(scanner);
	}
	
	@Test
	public void testDOT2() throws LexicalException {
		String input = "..";
		Scanner scanner = new Scanner(input).scan();
		show(input);
		show(scanner);
		checkNext(scanner, DOT, 0, 1, 1, 1);
		checkNext(scanner, DOT, 1, 1, 1, 2);
		checkNextIsEOF(scanner);
	}
	
	@Test
	public void testDOT3() throws LexicalException {
		String input = "...";
		Scanner scanner = new Scanner(input).scan();
		show(input);
		show(scanner);
		checkNext(scanner, DOT, 0, 1, 1, 1);
		checkNext(scanner, DOT, 1, 1, 1, 2);
		checkNext(scanner, DOT, 2, 1, 1, 3);
		checkNextIsEOF(scanner);
	}
	
	@Test
	public void testDOT4() throws LexicalException {
		String input = "....";
		Scanner scanner = new Scanner(input).scan();
		show(input);
		show(scanner);
		checkNext(scanner, DOT, 0, 1, 1, 1);
		checkNext(scanner, DOT, 1, 1, 1, 2);
		checkNext(scanner, DOT, 2, 1, 1, 3);
		checkNext(scanner, DOT, 3, 1, 1, 4);
		checkNextIsEOF(scanner);
	}
	
	@Test
	public void testDOT5() throws LexicalException {
		String input = ". .";
		Scanner scanner = new Scanner(input).scan();
		show(input);
		show(scanner);
		checkNext(scanner, DOT, 0, 1, 1, 1);
		checkNext(scanner, DOT, 2, 1, 1, 3);
		checkNextIsEOF(scanner);
	}
	
	@Test
	public void testDOT6() throws LexicalException {
		String input = ". X";
		Scanner scanner = new Scanner(input).scan();
		show(input);
		show(scanner);
		checkNext(scanner, DOT, 0, 1, 1, 1);
		checkNext(scanner, IDENTIFIER, 2, 1, 1, 3);
		checkNextIsEOF(scanner);
	}
	
	@Test
	public void testDOT7() throws LexicalException {
		String input = "Y\n.";
		Scanner scanner = new Scanner(input).scan();
		show(input);
		show(scanner);
		checkNext(scanner, IDENTIFIER, 0, 1, 1, 1);
		checkNext(scanner, DOT, 2, 1, 2, 1);
		checkNextIsEOF(scanner);
	}
	
	@Test
	public void testDOT8() throws LexicalException {
		String input = ".!.";
		Scanner scanner = new Scanner(input).scan();
		show(input);
		show(scanner);
		checkNext(scanner, DOT, 0, 1, 1, 1);
		checkNext(scanner, OP_EXCLAMATION, 1, 1, 1, 2);
		checkNext(scanner, DOT, 2, 1, 1, 3);
		checkNextIsEOF(scanner);
	}
	
	@Test
	public void test_DOT_And_IDENTIFIER() throws LexicalException {
		String input = ".a";
		Scanner scanner = new Scanner(input).scan();
		show(input);
		show(scanner);
		checkNext(scanner, DOT, 0, 1, 1, 1);
		checkNext(scanner, IDENTIFIER, 1, 1, 1, 2);
		checkNextIsEOF(scanner);
	}
	
	@Test
	public void testDOT9() throws LexicalException {
		String input = ".o.";
		Scanner scanner = new Scanner(input).scan();
		show(input);
		show(scanner);
		checkNext(scanner, DOT, 0, 1, 1, 1);
		checkNext(scanner, IDENTIFIER, 1, 1, 1, 2);
		checkNext(scanner, DOT, 2, 1, 1, 3);
		checkNextIsEOF(scanner);
	}
	
	@Test
	public void test_OP_GT() throws LexicalException {
		String input = ">";
		Scanner scanner = new Scanner(input).scan();
		show(input);
		show(scanner);
		checkNext(scanner, OP_GT, 0, 1, 1, 1);
		checkNextIsEOF(scanner);
	}
	
	@Test
	public void testRPIXEL() throws LexicalException {
		String input = ">>";
		Scanner scanner = new Scanner(input).scan();
		show(input);
		show(scanner);
		checkNext(scanner, RPIXEL, 0, 2, 1, 1);
		checkNextIsEOF(scanner);
	}
	
	@Test
	public void testRPIXEL2() throws LexicalException {
		String input = ">>>>";
		Scanner scanner = new Scanner(input).scan();
		show(input);
		show(scanner);
		checkNext(scanner, RPIXEL, 0, 2, 1, 1);
		checkNext(scanner, RPIXEL, 2, 2, 1, 3);
		checkNextIsEOF(scanner);
	}
	
	@Test
	public void testRPIXEL3() throws LexicalException {
		String input = ">>>";
		Scanner scanner = new Scanner(input).scan();
		show(input);
		show(scanner);
		checkNext(scanner, RPIXEL, 0, 2, 1, 1);
		checkNext(scanner, OP_GT, 2, 1, 1, 3);
		checkNextIsEOF(scanner);
	}
		
	@Test
	public void test_OP_LT() throws LexicalException {
		String input = "<";
		Scanner scanner = new Scanner(input).scan();
		show(input);
		show(scanner);
		checkNext(scanner, OP_LT, 0, 1, 1, 1);
		checkNextIsEOF(scanner);
	}
	
	@Test
	public void testLPIXEL() throws LexicalException {
		String input = "<<";
		Scanner scanner = new Scanner(input).scan();
		show(input);
		show(scanner);
		checkNext(scanner, LPIXEL, 0, 2, 1, 1);
		checkNextIsEOF(scanner);
	}
	
	@Test
	public void testLPIXEL2() throws LexicalException {
		String input = "<<<<";
		Scanner scanner = new Scanner(input).scan();
		show(input);
		show(scanner);
		checkNext(scanner, LPIXEL, 0, 2, 1, 1);
		checkNext(scanner, LPIXEL, 2, 2, 1, 3);
		checkNextIsEOF(scanner);
	}
	
	@Test
	public void test_OP_LT_And_OP_GT() throws LexicalException {
		String input = "<>";
		Scanner scanner = new Scanner(input).scan();
		show(input);
		show(scanner);
		checkNext(scanner, OP_LT, 0, 1, 1, 1);
		checkNext(scanner, OP_GT, 1, 1, 1, 2);
		checkNextIsEOF(scanner);
	}
	
	@Test
	public void test_OP_GT_And_OP_LT() throws LexicalException {
		String input = "> \n<";
		Scanner scanner = new Scanner(input).scan();
		show(input);
		show(scanner);
		checkNext(scanner, OP_GT, 0, 1, 1, 1);
		checkNext(scanner, OP_LT, 3, 1, 2, 1);
		checkNextIsEOF(scanner);
	}
	
	@Test
	public void failIllegalChar4() throws LexicalException {
		String input = "!==";
		show(input);
		thrown.expect(LexicalException.class);  //Tell JUnit to expect a LexicalException
		try {
			new Scanner(input).scan();
		} catch (LexicalException e) {  //Catch the exception
			show(e);                    //Display it
			assertEquals(2,e.getPos()); //Check that it occurred in the expected position
			throw e;                    //Rethrow exception so JUnit will see it
		}
	}
	
	@Test
	public void test_OP_NEQ_And_OP_LT() throws LexicalException {
		String input = "!=<";
		Scanner scanner = new Scanner(input).scan();
		show(input);
		show(scanner);
		checkNext(scanner, OP_NEQ, 0, 2, 1, 1);
		checkNext(scanner, OP_LT, 2, 1, 1, 3);
		checkNextIsEOF(scanner);
	}
	
	@Test
	public void test_OP_POWER() throws LexicalException {
		String input = "**";
		Scanner scanner = new Scanner(input).scan();
		show(input);
		show(scanner);
		checkNext(scanner, OP_POWER, 0, 2, 1, 1);
		checkNextIsEOF(scanner);
	}
	
	@Test
	public void test_OP_POWER_And_OP_TIMES() throws LexicalException {
		String input = "***";
		Scanner scanner = new Scanner(input).scan();
		show(input);
		show(scanner);
		checkNext(scanner, OP_POWER, 0, 2, 1, 1);
		checkNext(scanner, OP_TIMES, 2, 1, 1, 3);
		checkNextIsEOF(scanner);
	}
	
	@Test
	public void test_OP_POWER_And_OP_POWER() throws LexicalException {
		String input = "****";
		Scanner scanner = new Scanner(input).scan();
		show(input);
		show(scanner);
		checkNext(scanner, OP_POWER, 0, 2, 1, 1);
		checkNext(scanner, OP_POWER, 2, 2, 1, 3);
		checkNextIsEOF(scanner);
	}
	
	@Test
	public void test_OP_POWER_And_OP_POWER_And_OP_TIMES() throws LexicalException {
		String input = "*****";
		Scanner scanner = new Scanner(input).scan();
		show(input);
		show(scanner);
		checkNext(scanner, OP_POWER, 0, 2, 1, 1);
		checkNext(scanner, OP_POWER, 2, 2, 1, 3);
		checkNext(scanner, OP_TIMES, 4, 1, 1, 5);
		checkNextIsEOF(scanner);
	}
	
	@Test
	public void test_OP_TIMES() throws LexicalException {
		String input = "*";
		Scanner scanner = new Scanner(input).scan();
		show(input);
		show(scanner);
		checkNext(scanner, OP_TIMES, 0, 1, 1, 1);
		checkNextIsEOF(scanner);
	}
	
	@Test
	public void test_OP_EXCLAMATION_And_OP_TIMES() throws LexicalException {
		String input = "!*";
		Scanner scanner = new Scanner(input).scan();
		show(input);
		show(scanner);
		checkNext(scanner, OP_EXCLAMATION, 0, 1, 1, 1);
		checkNext(scanner, OP_TIMES, 1, 1, 1, 2);
		checkNextIsEOF(scanner);
	}
	
	@Test
	public void test_OP_EXCLAMATION_And_OP_POWER() throws LexicalException {
		String input = "!**";
		Scanner scanner = new Scanner(input).scan();
		show(input);
		show(scanner);
		checkNext(scanner, OP_EXCLAMATION, 0, 1, 1, 1);
		checkNext(scanner, OP_POWER, 1, 2, 1, 2);
		checkNextIsEOF(scanner);
	}
	
	@Test
	public void test_OP_EQ() throws LexicalException {
		String input = "==";
		Scanner scanner = new Scanner(input).scan();
		show(input);
		show(scanner);
		checkNext(scanner, OP_EQ, 0, 2, 1, 1);
		checkNextIsEOF(scanner);
	}
	
	@Test
	public void test_OP_EQ2() throws LexicalException {
		String input = "====";
		Scanner scanner = new Scanner(input).scan();
		show(input);
		show(scanner);
		checkNext(scanner, OP_EQ, 0, 2, 1, 1);
		checkNext(scanner, OP_EQ, 2, 2, 1, 3);
		checkNextIsEOF(scanner);
	}
	
	@Test
	public void failIllegalChar5() throws LexicalException {
		String input = "=";
		show(input);
		thrown.expect(LexicalException.class);  //Tell JUnit to expect a LexicalException
		try {
			new Scanner(input).scan();
		} catch (LexicalException e) {  //Catch the exception
			show(e);                    //Display it
			assertEquals(0,e.getPos()); //Check that it occurred in the expected position
			throw e;                    //Rethrow exception so JUnit will see it
		}
	}
	
	@Test
	public void failIllegalChar6() throws LexicalException {
		String input = "===";
		show(input);
		thrown.expect(LexicalException.class);  //Tell JUnit to expect a LexicalException
		try {
			new Scanner(input).scan();
		} catch (LexicalException e) {  //Catch the exception
			show(e);                    //Display it
			assertEquals(2,e.getPos()); //Check that it occurred in the expected position
			throw e;                    //Rethrow exception so JUnit will see it
		}
	}
	
	@Test
	public void failIllegalChar_Comment() throws LexicalException {
		String input = "/*s1fsfs";
		show(input);
		thrown.expect(LexicalException.class);  //Tell JUnit to expect a LexicalException
		try {
			new Scanner(input).scan();
		} catch (LexicalException e) {  //Catch the exception
			show(e);                    //Display it
			assertEquals(0,e.getPos()); //Check that it occurred in the expected position
			throw e;                    //Rethrow exception so JUnit will see it
		}
	}
	
	@Test
	public void test_Comment() throws LexicalException {
		String input = "== /**/ !";
		Scanner scanner = new Scanner(input).scan();
		show(input);
		show(scanner);
		checkNext(scanner, OP_EQ, 0, 2, 1, 1);
		checkNext(scanner, OP_EXCLAMATION, 8, 1, 1, 9);
		checkNextIsEOF(scanner);
	}
	
	@Test
	public void test_Comment2() throws LexicalException {
		String input = "== /* a\nb */ !";
		Scanner scanner = new Scanner(input).scan();
		show(input);
		show(scanner);
		checkNext(scanner, OP_EQ, 0, 2, 1, 1);
		checkNext(scanner, OP_EXCLAMATION, 13, 1, 2, 6);
		checkNextIsEOF(scanner);
	}
	
	@Test
	public void test_Comment3() throws LexicalException {
		String input = "/**/";
		Scanner scanner = new Scanner(input).scan();
		show(input);
		show(scanner);
		checkNextIsEOF(scanner);
	}
	
	@Test
	public void test_Comment4() throws LexicalException {
		String input = "/* &#!/ */";
		Scanner scanner = new Scanner(input).scan();
		show(input);
		show(scanner);
		checkNextIsEOF(scanner);
	}
	
	@Test
	public void test_Comment5() throws LexicalException {
		String input = "/* *asd/ */";
		Scanner scanner = new Scanner(input).scan();
		show(input);
		show(scanner);
		checkNextIsEOF(scanner);
	}
	
	@Test
	public void test_Comment6() throws LexicalException {
		String input = "/***********************/";
		Scanner scanner = new Scanner(input).scan();
		show(input);
		show(scanner);
		checkNextIsEOF(scanner);
	}
	
	@Test
	public void test_Comment7() throws LexicalException {
		String input = "/* abcd xyz */";
		Scanner scanner = new Scanner(input).scan();
		show(input);
		show(scanner);
		checkNextIsEOF(scanner);
	}

	@Test
	public void test_OP_DIV() throws LexicalException {
		String input = "/";
		Scanner scanner = new Scanner(input).scan();
		show(input);
		show(scanner);
		checkNext(scanner, OP_DIV, 0, 1, 1, 1);
		checkNextIsEOF(scanner);
	}
	
	@Test
	public void test_OP_DIV2() throws LexicalException {
		String input = "//";
		Scanner scanner = new Scanner(input).scan();
		show(input);
		show(scanner);
		checkNext(scanner, OP_DIV, 0, 1, 1, 1);
		checkNext(scanner, OP_DIV, 1, 1, 1, 2);
		checkNextIsEOF(scanner);
	}
	
	@Test
	public void test_OP_DIV4() throws LexicalException {
		String input = "///";
		Scanner scanner = new Scanner(input).scan();
		show(input);
		show(scanner);
		checkNext(scanner, OP_DIV, 0, 1, 1, 1);
		checkNext(scanner, OP_DIV, 1, 1, 1, 2);
		checkNext(scanner, OP_DIV, 2, 1, 1, 3);
		checkNextIsEOF(scanner);
	}
	
	@Test
	public void test_OP_DIV3() throws LexicalException {
		String input = "a/b";
		Scanner scanner = new Scanner(input).scan();
		show(input);
		show(scanner);
		checkNext(scanner, IDENTIFIER, 0, 1, 1, 1);
		checkNext(scanner, OP_DIV, 1, 1, 1, 2);
		checkNext(scanner, IDENTIFIER, 2, 1, 1, 3);
		checkNextIsEOF(scanner);
	}
	
	@Test
	public void testSQUAREs() throws LexicalException {
		String input = "][";
		Scanner scanner = new Scanner(input).scan();
		show(input);
		show(scanner);
		checkNext(scanner, RSQUARE, 0, 1, 1, 1);
		checkNext(scanner, LSQUARE, 1, 1, 1, 2);
		checkNextIsEOF(scanner);
	}
	
	@Test
	public void testINTEGER_LITERAL1() throws LexicalException {
		String input = "0";
		Scanner scanner = new Scanner(input).scan();
		show(input);
		show(scanner);
		checkNext(scanner, INTEGER_LITERAL, 0, 1, 1, 1);
		checkNextIsEOF(scanner);
	}
	
	@Test
	public void testINTEGER_LITERAL2() throws LexicalException {
		String input = "2";
		Scanner scanner = new Scanner(input).scan();
		show(input);
		show(scanner);
		checkNext(scanner, INTEGER_LITERAL, 0, 1, 1, 1);
		checkNextIsEOF(scanner);
	}
	
	@Test
	public void testINTEGER_LITERAL3() throws LexicalException {
		String input = "203 310";
		Scanner scanner = new Scanner(input).scan();
		show(input);
		show(scanner);
		checkNext(scanner, INTEGER_LITERAL, 0, 3, 1, 1);
		checkNext(scanner, INTEGER_LITERAL, 4, 3, 1, 5);
		checkNextIsEOF(scanner);
	}
	
	@Test
	public void testINTEGER_LITERAL4() throws LexicalException {
		String input = "213-314";
		Scanner scanner = new Scanner(input).scan();
		show(input);
		show(scanner);
		checkNext(scanner, INTEGER_LITERAL, 0, 3, 1, 1);
		checkNext(scanner, OP_MINUS, 3, 1, 1, 4);
		checkNext(scanner, INTEGER_LITERAL, 4, 3, 1, 5);
		checkNextIsEOF(scanner);
	}
	
	@Test
	public void testINTEGER_LITERAL7() throws LexicalException {
		String input = "012";
		Scanner scanner = new Scanner(input).scan();
		show(input);
		show(scanner);
		checkNext(scanner, INTEGER_LITERAL, 0, 1, 1, 1);
		checkNext(scanner, INTEGER_LITERAL, 1, 2, 1, 2);
		checkNextIsEOF(scanner);
	}
	
	@Test
	public void testINTEGER_LITERAL8() throws LexicalException {
		String input = "56\r\n0340";
		Scanner scanner = new Scanner(input).scan();
		show(input);
		show(scanner);
		checkNext(scanner, INTEGER_LITERAL, 0, 2, 1, 1);
		checkNext(scanner, INTEGER_LITERAL, 4, 1, 2, 1);
		checkNext(scanner, INTEGER_LITERAL, 5, 3, 2, 2);
		checkNextIsEOF(scanner);
	}
	
	@Test
	public void testINTEGER_LITERAL9() throws LexicalException {
		String input = "999999999";
		Scanner scanner = new Scanner(input).scan();
		show(input);
		show(scanner);
		checkNext(scanner, INTEGER_LITERAL, 0, 9, 1, 1);
		checkNextIsEOF(scanner);
	}
	
	@Test
	public void testINTEGER_LITERAL10() throws LexicalException {
		String input = "9999999999";
		show(input);
		thrown.expect(LexicalException.class);  //Tell JUnit to expect a LexicalException
		try {
			new Scanner(input).scan();
		} catch (LexicalException e) {  //Catch the exception
			show(e);                    //Display it
			assertEquals(0,e.getPos()); //Check that it occurred in the expected position
			throw e;                    //Rethrow exception so JUnit will see it
		}
	}
	
	@Test
	public void failIllegalChar9() throws LexicalException {
		String input = ">>=< >";
		show(input);
		thrown.expect(LexicalException.class);  //Tell JUnit to expect a LexicalException
		try {
			new Scanner(input).scan();
		} catch (LexicalException e) {  //Catch the exception
			show(e);                    //Display it
			assertEquals(2,e.getPos()); //Check that it occurred in the expected position
			throw e;                    //Rethrow exception so JUnit will see it
		}
	}
	
	@Test
	public void failIllegalChar10() throws LexicalException {
		String input = ">>=";
		show(input);
		thrown.expect(LexicalException.class);  //Tell JUnit to expect a LexicalException
		try {
			new Scanner(input).scan();
		} catch (LexicalException e) {  //Catch the exception
			show(e);                    //Display it
			assertEquals(2,e.getPos()); //Check that it occurred in the expected position
			throw e;                    //Rethrow exception so JUnit will see it
		}
	}
	
	@Test
	public void failIllegalChar11() throws LexicalException {
		String input = ">==";
		show(input);
		thrown.expect(LexicalException.class);  //Tell JUnit to expect a LexicalException
		try {
			new Scanner(input).scan();
		} catch (LexicalException e) {  //Catch the exception
			show(e);                    //Display it
			assertEquals(2,e.getPos()); //Check that it occurred in the expected position
			throw e;                    //Rethrow exception so JUnit will see it
		}
	}
	
	@Test
	public void failIllegalChar12() throws LexicalException {
		String input = ":==";
		show(input);
		thrown.expect(LexicalException.class);  //Tell JUnit to expect a LexicalException
		try {
			new Scanner(input).scan();
		} catch (LexicalException e) {  //Catch the exception
			show(e);                    //Display it
			assertEquals(2,e.getPos()); //Check that it occurred in the expected position
			throw e;                    //Rethrow exception so JUnit will see it
		}
	}
	
	@Test
	public void failIllegalChar14() throws LexicalException {
		String input = "a=b";
		show(input);
		thrown.expect(LexicalException.class);  //Tell JUnit to expect a LexicalException
		try {
			new Scanner(input).scan();
		} catch (LexicalException e) {  //Catch the exception
			show(e);                    //Display it
			assertEquals(1,e.getPos()); //Check that it occurred in the expected position
			throw e;                    //Rethrow exception so JUnit will see it
		}
	}
	
	@Test
	public void failIllegalChar15() throws LexicalException {
		String input = "$abc";
		show(input);
		thrown.expect(LexicalException.class);  //Tell JUnit to expect a LexicalException
		try {
			new Scanner(input).scan();
		} catch (LexicalException e) {  //Catch the exception
			show(e);                    //Display it
			assertEquals(0,e.getPos()); //Check that it occurred in the expected position
			throw e;                    //Rethrow exception so JUnit will see it
		}
	}
	
	@Test
	public void failIllegalChar16() throws LexicalException {
		String input = "999999999999999999999999999999999999999999.123";
		show(input);
		thrown.expect(LexicalException.class);  //Tell JUnit to expect a LexicalException
		try {
			new Scanner(input).scan();
		} catch (LexicalException e) {  //Catch the exception
			show(e);                    //Display it
			assertEquals(0,e.getPos()); //Check that it occurred in the expected position
			throw e;                    //Rethrow exception so JUnit will see it
		}
	}
		
	@Test
	public void testINTEGER_LITERAL5() throws LexicalException {
		String input = "567\n809";
		Scanner scanner = new Scanner(input).scan();
		show(input);
		show(scanner);
		checkNext(scanner, INTEGER_LITERAL, 0, 3, 1, 1);
		checkNext(scanner, INTEGER_LITERAL, 4, 3, 2, 1);
		checkNextIsEOF(scanner);
	}
	
	@Test
	public void testINTEGER_LITERAL6() throws LexicalException {
		String input = "00010";
		Scanner scanner = new Scanner(input).scan();
		show(input);
		show(scanner);
		checkNext(scanner, INTEGER_LITERAL, 0, 1, 1, 1);
		checkNext(scanner, INTEGER_LITERAL, 1, 1, 1, 2);
		checkNext(scanner, INTEGER_LITERAL, 2, 1, 1, 3);
		checkNext(scanner, INTEGER_LITERAL, 3, 2, 1, 4);
		checkNextIsEOF(scanner);
	}
	
	@Test
	public void testFlOATINGPOINTLITERAL() throws LexicalException {
		String input = "1.2";
		Scanner scanner = new Scanner(input).scan();
		show(input);
		show(scanner);
		checkNext(scanner, FLOAT_LITERAL, 0, 3, 1, 1);
		checkNextIsEOF(scanner);
	}
	
	@Test
	public void testFlOATINGPOINTLITERAL2() throws LexicalException {
		String input = "1.02";
		Scanner scanner = new Scanner(input).scan();
		show(input);
		show(scanner);
		checkNext(scanner, FLOAT_LITERAL, 0, 4, 1, 1);
		checkNextIsEOF(scanner);
	}
	
	@Test
	public void testFlOATINGPOINTLITERAL3() throws LexicalException {
		String input = "0.";
		Scanner scanner = new Scanner(input).scan();
		show(input);
		show(scanner);
		checkNext(scanner, FLOAT_LITERAL, 0, 2, 1, 1);
		checkNextIsEOF(scanner);
	}
	
	@Test
	public void testFlOATINGPOINTLITERAL4() throws LexicalException {
		String input = ".0 .202 .03";
		Scanner scanner = new Scanner(input).scan();
		show(input);
		show(scanner);
		checkNext(scanner, FLOAT_LITERAL, 0, 2, 1, 1);
		checkNext(scanner, FLOAT_LITERAL, 3, 4, 1, 4);
		checkNext(scanner, FLOAT_LITERAL, 8, 3, 1, 9);
		checkNextIsEOF(scanner);
	}
	
	@Test
	public void testFlOATINGPOINTLITERAL5() throws LexicalException {
		String input = "0.0\n05.020\n10.03\n2.\n20.\n34.00";
		Scanner scanner = new Scanner(input).scan();
		show(input);
		show(scanner);
		checkNext(scanner, FLOAT_LITERAL, 0, 3, 1, 1);
		checkNext(scanner, INTEGER_LITERAL, 4, 1, 2, 1);
		checkNext(scanner, FLOAT_LITERAL, 5, 5, 2, 2);
		checkNext(scanner, FLOAT_LITERAL, 11, 5, 3, 1);
		checkNext(scanner, FLOAT_LITERAL, 17, 2, 4, 1);
		checkNext(scanner, FLOAT_LITERAL, 20, 3, 5, 1);
		checkNext(scanner, FLOAT_LITERAL, 24, 5, 6, 1);
		checkNextIsEOF(scanner);
	}
	
	@Test
	public void testFlOATINGPOINTLITERAL6() throws LexicalException {
		String input = "2.\n20. 34.00";
		Scanner scanner = new Scanner(input).scan();
		show(input);
		show(scanner);
		checkNext(scanner, FLOAT_LITERAL, 0, 2, 1, 1);
		checkNext(scanner, FLOAT_LITERAL, 3, 3, 2, 1);
		checkNext(scanner, FLOAT_LITERAL, 7, 5, 2, 5);
		checkNextIsEOF(scanner);
	}
	
	@Test
	public void testFlOATINGPOINTLITERAL7() throws LexicalException {
		String input = ".0\t.1 .010\r\n.000\f.1230";
		Scanner scanner = new Scanner(input).scan();
		show(input);
		show(scanner);
		checkNext(scanner, FLOAT_LITERAL, 0, 2, 1, 1);
		checkNext(scanner, FLOAT_LITERAL, 3, 2, 1, 4);
		checkNext(scanner, FLOAT_LITERAL, 6, 4, 1, 7);
		checkNext(scanner, FLOAT_LITERAL, 12, 4, 2, 1);
		checkNext(scanner, FLOAT_LITERAL, 17, 5, 2, 6);
		checkNextIsEOF(scanner);
	}
	
	@Test
	public void testFlOATINGPOINTLITERAL8() throws LexicalException {
		String input = "1.0.1230";
		Scanner scanner = new Scanner(input).scan();
		show(input);
		show(scanner);
		checkNext(scanner, FLOAT_LITERAL, 0, 3, 1, 1);
		checkNext(scanner, FLOAT_LITERAL, 3, 5, 1, 4);
		checkNextIsEOF(scanner);
	}
	
	@Test
	public void testFlOATINGPOINTLITERAL9() throws LexicalException {
		String input = "1..1230";
		Scanner scanner = new Scanner(input).scan();
		show(input);
		show(scanner);
		checkNext(scanner, FLOAT_LITERAL, 0, 2, 1, 1);
		checkNext(scanner, FLOAT_LITERAL, 2, 5, 1, 3);
		checkNextIsEOF(scanner);
	}
	
	@Test
	public void testINTEGER_LITERAL11() throws LexicalException {
		String input = "0012";
		Scanner scanner = new Scanner(input).scan();
		show(input);
		show(scanner);
		checkNext(scanner, INTEGER_LITERAL, 0, 1, 1, 1);
		checkNext(scanner, INTEGER_LITERAL, 1, 1, 1, 2);
		checkNext(scanner, INTEGER_LITERAL, 2, 2, 1, 3);
		checkNextIsEOF(scanner);
	}
	
	@Test
	public void testINTEGER_LITERAL12() throws LexicalException {
		String input = "000.";
		Scanner scanner = new Scanner(input).scan();
		show(input);
		show(scanner);
		checkNext(scanner, INTEGER_LITERAL, 0, 1, 1, 1);
		checkNext(scanner, INTEGER_LITERAL, 1, 1, 1, 2);
		checkNext(scanner, FLOAT_LITERAL, 2, 2, 1, 3);
		checkNextIsEOF(scanner);
	}
	
	@Test
	public void testFLOAT_LITERAL13() throws LexicalException {
		String input = "35.";
		Scanner scanner = new Scanner(input).scan();
		show(input);
		show(scanner);
		checkNext(scanner, FLOAT_LITERAL, 0, 3, 1, 1);
		checkNextIsEOF(scanner);
	}
	
	@Test
	public void testINTEGER_LITERAL13() throws LexicalException {
		String input = "-25";
		Scanner scanner = new Scanner(input).scan();
		show(input);
		show(scanner);
		checkNext(scanner, OP_MINUS, 0, 1, 1, 1);
		checkNext(scanner, INTEGER_LITERAL, 1, 2, 1, 2);
		checkNextIsEOF(scanner);
	}
	
	@Test
	public void failIllegalChar17() throws LexicalException {
		String input = "123\n=\n456";
		show(input);
		thrown.expect(LexicalException.class);  //Tell JUnit to expect a LexicalException
		try {
			new Scanner(input).scan();
		} catch (LexicalException e) {  //Catch the exception
			show(e);                    //Display it
			assertEquals(4,e.getPos()); //Check that it occurred in the expected position
			throw e;                    //Rethrow exception so JUnit will see it
		}
	}
	
	@Test
	public void failIllegalChar18() throws LexicalException {
		String input = "/*";
		show(input);
		thrown.expect(LexicalException.class);  //Tell JUnit to expect a LexicalException
		try {
			new Scanner(input).scan();
		} catch (LexicalException e) {  //Catch the exception
			show(e);                    //Display it
			assertEquals(0,e.getPos()); //Check that it occurred in the expected position
			throw e;                    //Rethrow exception so JUnit will see it
		}
	}
	
	@Test
	public void failIllegalChar19() throws LexicalException {
		String input = "/**";
		show(input);
		thrown.expect(LexicalException.class);  //Tell JUnit to expect a LexicalException
		try {
			new Scanner(input).scan();
		} catch (LexicalException e) {  //Catch the exception
			show(e);                    //Display it
			assertEquals(0,e.getPos()); //Check that it occurred in the expected position
			throw e;                    //Rethrow exception so JUnit will see it
		}
	}
	
	@Test
	public void failIllegalChar20() throws LexicalException {
		String input = "/* abcd xyz";
		show(input);
		thrown.expect(LexicalException.class);  //Tell JUnit to expect a LexicalException
		try {
			new Scanner(input).scan();
		} catch (LexicalException e) {  //Catch the exception
			show(e);                    //Display it
			assertEquals(0,e.getPos()); //Check that it occurred in the expected position
			throw e;                    //Rethrow exception so JUnit will see it
		}
	}
	
	@Test
	public void testFlOATINGPOINTLITERAL10() throws LexicalException {
		String input = "0.0000";
		Scanner scanner = new Scanner(input).scan();
		show(input);
		show(scanner);
		checkNext(scanner, FLOAT_LITERAL, 0, 6, 1, 1);
		checkNextIsEOF(scanner);
	}
	
	@Test
	public void testFlOATINGPOINTLITERAL11() throws LexicalException {
		String input = "0000.01";
		Scanner scanner = new Scanner(input).scan();
		show(input);
		show(scanner);
		checkNext(scanner, INTEGER_LITERAL, 0, 1, 1, 1);
		checkNext(scanner, INTEGER_LITERAL, 1, 1, 1, 2);
		checkNext(scanner, INTEGER_LITERAL, 2, 1, 1, 3);
		checkNext(scanner, FLOAT_LITERAL, 3, 4, 1, 4);
		checkNextIsEOF(scanner);
	}
	
	@Test
	public void testFlOATINGPOINTLITERAL12() throws LexicalException {
		String input = ".0";
		Scanner scanner = new Scanner(input).scan();
		show(input);
		show(scanner);
		checkNext(scanner, FLOAT_LITERAL, 0, 2, 1, 1);
		checkNextIsEOF(scanner);
	}
	
	@Test
	public void testFlOATINGPOINTLITERAL13() throws LexicalException {
		String input = "9.";
		Scanner scanner = new Scanner(input).scan();
		show(input);
		show(scanner);
		checkNext(scanner, FLOAT_LITERAL, 0, 2, 1, 1);
		checkNextIsEOF(scanner);
	}
	
	@Test
	public void testFlOATINGPOINTLITERAL14() throws LexicalException {
		String input = "1.2.3";
		Scanner scanner = new Scanner(input).scan();
		show(input);
		show(scanner);
		checkNext(scanner, FLOAT_LITERAL, 0, 3, 1, 1);
		checkNext(scanner, FLOAT_LITERAL, 3, 2, 1, 4);
		checkNextIsEOF(scanner);
	}
	
	@Test
	public void testFlOATINGPOINTLITERAL15() throws LexicalException {
		String input = "0..";
		Scanner scanner = new Scanner(input).scan();
		show(input);
		show(scanner);
		checkNext(scanner, FLOAT_LITERAL, 0, 2, 1, 1);
		checkNext(scanner, DOT, 2, 1, 1, 3);
		checkNextIsEOF(scanner);
	}
	
	@Test
	public void testFlOATINGPOINTLITERAL16() throws LexicalException {
		String input = ".1.1";
		Scanner scanner = new Scanner(input).scan();
		show(input);
		show(scanner);
		checkNext(scanner, FLOAT_LITERAL, 0, 2, 1, 1);
		checkNext(scanner, FLOAT_LITERAL, 2, 2, 1, 3);
		checkNextIsEOF(scanner);
	}
	
	@Test
	public void testFlOATINGPOINTLITERAL17() throws LexicalException {
		String input = "00000.12302";
		Scanner scanner = new Scanner(input).scan();
		show(input);
		show(scanner);
		checkNext(scanner, INTEGER_LITERAL, 0, 1, 1, 1);
		checkNext(scanner, INTEGER_LITERAL, 1, 1, 1, 2);
		checkNext(scanner, INTEGER_LITERAL, 2, 1, 1, 3);
		checkNext(scanner, INTEGER_LITERAL, 3, 1, 1, 4);
		checkNext(scanner, FLOAT_LITERAL, 4, 7, 1, 5);
		checkNextIsEOF(scanner);
	}
	
	@Test
	public void testFlOATINGPOINTLITERAL18() throws LexicalException {
		String input = "0012.1";
		Scanner scanner = new Scanner(input).scan();
		show(input);
		show(scanner);
		checkNext(scanner, INTEGER_LITERAL, 0, 1, 1, 1);
		checkNext(scanner, INTEGER_LITERAL, 1, 1, 1, 2);
		checkNext(scanner, FLOAT_LITERAL, 2, 4, 1, 3);
		checkNextIsEOF(scanner);
	}
	
	@Test
	public void testFlOATINGPOINTLITERAL19() throws LexicalException {
		String input = "00124.555";
		Scanner scanner = new Scanner(input).scan();
		show(input);
		show(scanner);
		checkNext(scanner, INTEGER_LITERAL, 0, 1, 1, 1);
		checkNext(scanner, INTEGER_LITERAL, 1, 1, 1, 2);
		checkNext(scanner, FLOAT_LITERAL, 2, 7, 1, 3);
		checkNextIsEOF(scanner);
	}
	
	@Test
	public void test_IDENTIFIER_And_FlOATINGPOINTLITERAL() throws LexicalException {
		String input = "abc.0000";
		Scanner scanner = new Scanner(input).scan();
		show(input);
		show(scanner);
		checkNext(scanner, IDENTIFIER, 0, 3, 1, 1);
		checkNext(scanner, FLOAT_LITERAL, 3, 5, 1, 4);
		checkNextIsEOF(scanner);
	}
	
	@Test
	public void test_IDENTIFIER2() throws LexicalException {
		String input = "A.B";
		Scanner scanner = new Scanner(input).scan();
		show(input);
		show(scanner);
		checkNext(scanner, IDENTIFIER, 0, 1, 1, 1);
		checkNext(scanner, DOT, 1, 1, 1, 2);
		checkNext(scanner, IDENTIFIER, 2, 1, 1, 3);
		checkNextIsEOF(scanner);
	}
	
	@Test
	public void testEXP() throws LexicalException {
		String input = ".0+1230.";
		Scanner scanner = new Scanner(input).scan();
		show(input);
		show(scanner);
		checkNext(scanner, FLOAT_LITERAL, 0, 2, 1, 1);
		checkNext(scanner, OP_PLUS, 2, 1, 1, 3);
		checkNext(scanner, FLOAT_LITERAL, 3, 5, 1, 4);
		checkNextIsEOF(scanner);
	}
	
	@Test
	public void testEXP8() throws LexicalException {
		String input = "(a+b)/c;";
		Scanner scanner = new Scanner(input).scan();
		show(input);
		show(scanner);
		checkNext(scanner, LPAREN, 0, 1, 1, 1);
		checkNext(scanner, IDENTIFIER, 1, 1, 1, 2);
		checkNext(scanner, OP_PLUS, 2, 1, 1, 3);
		checkNext(scanner, IDENTIFIER, 3, 1, 1, 4);
		checkNext(scanner, RPAREN, 4, 1, 1, 5);
		checkNext(scanner, OP_DIV, 5, 1, 1, 6);
		checkNext(scanner, IDENTIFIER, 6, 1, 1, 7);
		checkNext(scanner, SEMI, 7, 1, 1, 8);
		checkNextIsEOF(scanner);
	}
	
	@Test
	public void testEXP2() throws LexicalException {
		String input = ".0!===1230.";
		Scanner scanner = new Scanner(input).scan();
		show(input);
		show(scanner);
		checkNext(scanner, FLOAT_LITERAL, 0, 2, 1, 1);
		checkNext(scanner, OP_NEQ, 2, 2, 1, 3);
		checkNext(scanner, OP_EQ, 4, 2, 1, 5);
		checkNext(scanner, FLOAT_LITERAL, 6, 5, 1, 7);
		checkNextIsEOF(scanner);
	}
	
	@Test
	public void testEXP3() throws LexicalException {
		String input = "2!=20.";
		Scanner scanner = new Scanner(input).scan();
		show(input);
		show(scanner);
		checkNext(scanner, INTEGER_LITERAL, 0, 1, 1, 1);
		checkNext(scanner, OP_NEQ, 1, 2, 1, 2);
		checkNext(scanner, FLOAT_LITERAL, 3, 3, 1, 4);
		checkNextIsEOF(scanner);
	}
	
	@Test
	public void testEXP4() throws LexicalException {
		String input = "!===";
		Scanner scanner = new Scanner(input).scan();
		show(input);
		show(scanner);
		checkNext(scanner, OP_NEQ, 0, 2, 1, 1);
		checkNext(scanner, OP_EQ, 2, 2, 1, 3);
		checkNextIsEOF(scanner);
	}
	
	@Test
	public void testEXP5() throws LexicalException {
		String input = "==!=";
		Scanner scanner = new Scanner(input).scan();
		show(input);
		show(scanner);
		checkNext(scanner, OP_EQ, 0, 2, 1, 1);
		checkNext(scanner, OP_NEQ, 2, 2, 1, 3);
		checkNextIsEOF(scanner);
	}
	
	@Test
	public void testEXP6() throws LexicalException {
		String input = "!0";
		Scanner scanner = new Scanner(input).scan();
		show(input);
		show(scanner);
		checkNext(scanner, OP_EXCLAMATION, 0, 1, 1, 1);
		checkNext(scanner, INTEGER_LITERAL, 1, 1, 1, 2);
		checkNextIsEOF(scanner);
	}
		
	@Test
	public void testEXP7() throws LexicalException {
		String input = "1.<<.3>>5";
		Scanner scanner = new Scanner(input).scan();
		show(input);
		show(scanner);
		checkNext(scanner, FLOAT_LITERAL, 0, 2, 1, 1);
		checkNext(scanner, LPIXEL, 2, 2, 1, 3);
		checkNext(scanner, FLOAT_LITERAL, 4, 2, 1, 5);
		checkNext(scanner, RPIXEL, 6, 2, 1, 7);
		checkNext(scanner, INTEGER_LITERAL, 8, 1, 1, 9);
		checkNextIsEOF(scanner);
	}
	
	@Test
	public void test_FLOAT_LITERAL_And_IDENTIFIER() throws LexicalException {
		String input = "0.A";
		Scanner scanner = new Scanner(input).scan();
		show(input);
		show(scanner);
		checkNext(scanner, FLOAT_LITERAL, 0, 2, 1, 1);
		checkNext(scanner, IDENTIFIER, 2, 1, 1, 3);
		checkNextIsEOF(scanner);
	}
	
	@Test
	public void testIDENTIFIER() throws LexicalException {
		String input = "A1_$B";
		Scanner scanner = new Scanner(input).scan();
		show(input);
		show(scanner);
		checkNext(scanner, IDENTIFIER, 0, 5, 1, 1);
		checkNextIsEOF(scanner);
	}
	
	@Test
	public void testIDENTIFIER2() throws LexicalException {
		String input = "1ABC";
		Scanner scanner = new Scanner(input).scan();
		show(input);
		show(scanner);
		checkNext(scanner, INTEGER_LITERAL, 0, 1, 1, 1);
		checkNext(scanner, IDENTIFIER, 1, 3, 1, 2);
		checkNextIsEOF(scanner);
	}
		
	@Test
	public void test_BOOLEAN_LITERAL() throws LexicalException {
		String input = "false";
		Scanner scanner = new Scanner(input).scan();
		show(input);
		show(scanner);
		checkNext(scanner, BOOLEAN_LITERAL, 0, 5, 1, 1);
		checkNextIsEOF(scanner);
	}
	
	@Test
	public void testKEYWORD() throws LexicalException {
		String input = "show filename";
		Scanner scanner = new Scanner(input).scan();
		show(input);
		show(scanner);
		checkNext(scanner, KW_show, 0, 4, 1, 1);
		checkNext(scanner, KW_filename, 5, 8, 1, 6);
		checkNextIsEOF(scanner);
	}
	
	@Test
	public void testKEYWORD2() throws LexicalException {
		String input = "if (a<b) {\n\tc:=sin(a);\n}";
		Scanner scanner = new Scanner(input).scan();
		show(input);
		show(scanner);
		checkNext(scanner, KW_if, 0, 2, 1, 1);
		checkNext(scanner, LPAREN, 3, 1, 1, 4);
		checkNext(scanner, IDENTIFIER, 4, 1, 1, 5);
		checkNext(scanner, OP_LT, 5, 1, 1, 6);
		checkNext(scanner, IDENTIFIER, 6, 1, 1, 7);
		checkNext(scanner, RPAREN, 7, 1, 1, 8);
		checkNext(scanner, LBRACE, 9, 1, 1, 10);
		checkNext(scanner, IDENTIFIER, 12, 1, 2, 2); // c
		checkNext(scanner, OP_ASSIGN, 13, 2, 2, 3);
		checkNext(scanner, KW_sin, 15, 3, 2, 5);
		checkNext(scanner, LPAREN, 18, 1, 2, 8);
		checkNext(scanner, IDENTIFIER, 19, 1, 2, 9);
		checkNext(scanner, RPAREN, 20, 1, 2, 10);
		checkNext(scanner, SEMI, 21, 1, 2, 11);
		checkNext(scanner, RBRACE, 23, 1, 3, 1);
		checkNextIsEOF(scanner);
	}
	
	@Test
	public void testKEYWORD3() throws LexicalException {
		String input = "sleep\nshow";
		Scanner scanner = new Scanner(input).scan();
		show(input);
		show(scanner);
		checkNext(scanner, KW_sleep, 0, 5, 1, 1);
		checkNext(scanner, KW_show, 6, 4, 2, 1);
		checkNextIsEOF(scanner);
	}
	
}
