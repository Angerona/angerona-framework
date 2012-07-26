package angerona.fw.parser;

import angerona.fw.logic.Secret;

/**
 * A functional test class for the Secret parser.
 * The secret parser delegates to the FolParserB to read the secret
 * information.
 * @author Tim Janus
 */
public class SecretParserFunctionalTest {
	public static void main(String args []) throws ParseException
	  {
	   	String expr = "(Boss, java.class.irgendwas{d=0.25}, -who_argued(husband_of(mary)))";
	    System.out.println("Using expresion :" + expr);

		SecretParser parser = new SecretParser(expr);
	    try
	    {
	   	  Secret lst = parser.secret();
	   	  System.out.println("Parsing done...");
	   	  System.out.println(lst.toString());
	    }
	    catch (Exception e)
	    {
	      System.out.println("NOK.");
	      System.out.println(e.getMessage());
	      e.printStackTrace();
	    }
	    catch (Error e)
	    {
	      System.out.println("Oops.");
	      System.out.println(e.getMessage());
	      e.printStackTrace();
	    }
	  }
}
