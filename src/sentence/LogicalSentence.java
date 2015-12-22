package sentence;

import java.util.ArrayList;

public class LogicalSentence implements LogicalExpression{

	//The sentence
	private String expression;
	
	//The number of variables in the sentence
	public int numVars;
	
	//The array that holds the outputs
	public boolean[] truthTable;
	
	//The array that holds the variable/variable names
	//Names must be a character
	private char[] vars;
	
	//The converted variables - either T or F.
	private char[] input;
	
	/**
	 * Creates a new logical sentence
	 * 
	 * @param input the input string
	 * @param numVars the number of variables in the string
	 */
	public LogicalSentence(String input, int numVars) {
		//Sets the expression of this logical sentence to the input
		expression = input;
		
		//Sets the number of variables
		this.numVars = numVars;
		
		//Creates the truthtable
		truthTable = new boolean[(int) (Math.pow(2, numVars))];
		
		//Creates the input array
		this.input = new char[(int) numVars];
		
		//Creates the variable array
		vars = new char[numVars];
		
		//removes any spaces from the expression
		expression = expression.replaceAll("\\s","");
		
		//Gets the variable names
		getVariableNames();
	}

	/**
	 * Gets the names of the variables used in the sentences.
	 */
	public void getVariableNames() {
		//A list of non-variable-characters
		char[] operators = {'~', '=', '<', '>', '^', 'v', '(', ')'};
		
		//Where in the list of names the new name should go
		int positionIndex = 0;
		
		//Search through the expression to look for variables.
		for(int i = 0; i < expression.length(); i++) {
			boolean matched = false;
			
			//Check if the character is an operator or parentheses
			for(char character : operators) {
				if(expression.charAt(i) == character) {
					matched = true;
				}
			}

			//If it is not an operator or parentheses, it is a variable.
			//We store its name in an array.
			if(!matched) {
				vars[positionIndex] = expression.charAt(i);
				positionIndex++;
			}
		}
	}

	
	String assign(String exp, int tvals){
		
		ArrayList<Character> hi = new ArrayList<Character>(); 
	    String number = Integer.toBinaryString(tvals);
	    
	    int number2 = Integer.valueOf(number);
	    
	    int length = number.length();
	    char[] input = new char[length];
	    char[] vars = new char[length];
	    
	    exp.su
	    //A list of non-variable-characters
			char[] operators = {'~', '=', '<', '>', '^', 'v', '(', ')'};
			
			//Where in the list of names the new name should go
			int positionIndex = 0;
			
			//Search through the expression to look for variables.
			for(int i = 0; i < exp.length(); i++) {
				boolean matched = false;
				
				//Check if the character is an operator or parentheses
				for(char character : operators) {
					if(exp.charAt(i) == character) {
						matched = true;
					}
				}

				//If it is not an operator or parentheses, it is a variable.
				//We store its name in an array.
				if(!matched) {
					vars[positionIndex] = exp.charAt(i);
					positionIndex++;
				}
				
				//Fills out the input array according to the binary number 
				for(int k = 0; k < length; k++) {
					if(number.charAt(k) == '1') {
						input[k] = 'T';
					}
					else input[k] = 'F';
				}

				//Replaces the variables in the sentence with the input. 
				//Input is T/F
				for(int a = 0; a < length; a++) {
					exp = exp.replace(vars[a], input[a]);
				}
			}return exp; 
	}
	
	@Override
	/**
	 * Gets the truth table of this logical sentence.
	 * First one is all true inputs, second is all true and one false...etc
	 * Ex: TTT, then TTF, then TFT, then TFF, then FTT, then FTF, then FFT, then FFF
	 */
	public boolean[] getTruthTable() {
		//Initializes the variables used in truth table input generation
		String aString = Integer.toBinaryString((int) Math.pow(2, numVars));
		
		//This will be all ones, with the number of ones being the number of variables.
		//EX: 3 vars, 111
		int aNumber = Integer.valueOf(aString, 2) - 1;

		//Generates all possible cases of input using binary numbers
		for(int i = 0; i < Math.pow(2, numVars); i++) {
			//Sets the string having its vars replaced and evaluated to the expression.
			//This is done each time so it starts over from the original expression each time
			String string = expression;
			
			//Subtracts one from the binary number to find all possible cases.
			//Goes 111, then 110, and so on and so forth. 
			aNumber = Integer.valueOf(aString, 2) - 1;
			aString = Integer.toBinaryString(aNumber);
			
			//Fills in zeros to keep the length of the number of variables
			if(aString.length() < numVars) {
				int length = aString.length();
				String zeros = "";
				for(int j = 0; j < numVars - length; j ++) {
					zeros = zeros + "0";
				}
				aString = zeros + aString;
			}

			//Fills out the input array according to the binary number 
			for(int k = 0; k < numVars; k++) {
				if(aString.charAt(k) == '1') {
					input[k] = 'T';
				}
				else input[k] = 'F';
			}

			//Replaces the variables in the sentence with the input. 
			//Input is T/F
			for(int a = 0; a < numVars; a++) {
				string = string.replace(vars[a], input[a]);
			}
			
			//Evaluates this particular permutation of inputs and stores it in the truth table
			truthTable[i] = evaluate(string);
		}
		return truthTable;
	}
	
	/**
	 * Takes an expression with T/F in place of variables and evaluates it.
	 * 
	 * @param expression the expression to be evaluated
	 * @return whether or not the expression is true or false
	 */
	public boolean evaluate(String expression) {

		while(expression.length() != 1) {
			//simplify parentheses
			expression = simplifyParentheses(expression);
			
			//simplify not
			expression = simplifyNot(expression);
			
			//simplify and
			expression = simplifyAnd(expression);
			
			//simplify or
			expression = simplifyOr(expression);
			
			//simplify implies
			expression = simplifyImplies(expression);
			
			//evaluate biconditional
			expression = simplifyBiconditional(expression);
		}
		
		//Checks to see if the end result is T or F and returns true or false accordingly.
		if(expression.equals("T")) {
			return true;
		}
		
		else return false;
	}
	
	/**
	 * Removes simple parentheses
	 * 
	 * @param expression the expression to have its parentheses removed(only at a basic level)
	 * @return the simplified expression
	 */
	public String simplifyParentheses(String expression) {
		if(expression.contains("(T)")) {
			expression = expression.replace("(T)", "T");
		}
		if(expression.contains("(F)")) {
			expression = expression.replace("(F)", "F");
		}
		return expression;
	}
	
	/**
	 * Removes simple not expressions
	 * 
	 * @param expression the expression to be simplified(changing not true to false, and vice versa)
	 * @return the simplified expression
	 */
	public String simplifyNot(String expression) {
		if(expression.contains("~T")) {
			expression = expression.replace("~T", "F");
		}
		if(expression.contains("~F")) {
			expression = expression.replace("~F", "T");
		}
		
		return expression;
	}
	
	/**
	 * Removes simple and expressions
	 * 
	 * @param expression the expression to be simplified(by evaluating the and expression and replacing it)
	 * @return the simplified expression
	 */
	public String simplifyAnd(String expression) {
		if(expression.contains("T&T")) {
			expression = expression.replace("T&T", "T");
		}
		if(expression.contains("T&F")) {
			expression = expression.replace("T&F", "F");
		}
		if(expression.contains("F&T")) {
			expression = expression.replace("F&T", "F");
		}
		if(expression.contains("F&F")) {
			expression = expression.replace("F&F", "F");
		}
		return expression;
	}
	
	/**
	 * Removes simple or expressions
	 * 
	 * @param expression the expression to be simplified (by evaluating the or and replacing it)
	 * @return the simplified expression
	 */
	public String simplifyOr(String expression) {
		if(expression.contains("T|T")) {
			expression = expression.replace("T|T", "T");
		}
		if(expression.contains("T|F")) {
			expression = expression.replace("T|F", "T");
		}
		if(expression.contains("F|T")) {
			expression = expression.replace("F|T", "T");
		}
		if(expression.contains("F|F")) {
			expression = expression.replace("F|F", "F");
		}
		return expression;
	}
	
	
	/**
	 * Removes simple implications
	 * 
	 * @param expression the expression to have implications removed(by evaluating and replacing)
	 * @return the simplified expression
	 */
	public String simplifyImplies(String expression) {
		if(expression.contains("T=>F")) {
			expression = expression.replace("T=>F", "F");
		}
		if(expression.contains("T=>T")) {
			expression = expression.replace("T=>T", "T");
		}
		if(expression.contains("F=>F")) {
			expression = expression.replace("F=>F", "T");
		}
		if(expression.contains("F=>T")) {
			expression = expression.replace("F=>T", "T");
		}
		return expression;
	}
	
	/**
	 * Removes simple biconditionals
	 * 
	 * @param expression the expression to have biconditionals removed(by evaluating and replacing)
	 * @return the simplified expression
	 */
	public String simplifyBiconditional(String expression) {
		if(expression.contains("T<=>T")) {
			expression = expression.replace("T<=>T", "T");
		}
		if(expression.contains("T<=>F")) {
			expression = expression.replace("T<=>F", "F");
		}
		if(expression.contains("F<=>T")) {
			expression = expression.replace("F<=>T", "F");
		}
		if(expression.contains("F<=>F")) {
			expression = expression.replace("F<=>F", "T");
		}
		return expression;
	}
	
	@Override
	/**
	 * Tests to see if this logical sentence is valid
	 * It is only valid if all values in the truth table are true.
	 * (If all truth values satisfy it)
	 */
	public boolean valid() {
		for(boolean bool : getTruthTable()) {
			if(!bool) {
				return false;
			}
		}
		return true;
	}

	@Override
	/**
	 * Tests to see if this logical sentence is satisfiable.
	 * It is satisfiable if it is valid or contingent.
	 * (If there is some truth values that satisfy it)
	 */
	public boolean satisfiable() {
		if(valid() || contingent()) {
			return true;
		}
		return false;
	}

	@Override
	/**
	 * Tests to see if this logical sentence is contingent
	 * It is contingent if the truth table has both true's and false's
	 * (If there are some truth values that satisfy it and some that don't)
	 */
	public boolean contingent() {
		boolean falseMet = false;
		boolean trueMet = false;
		for(boolean bool : getTruthTable()) {
			if(!bool) {
				falseMet = true;
			}
			else trueMet = true;
		}
		return falseMet&&trueMet;
	}

	@Override
	/**
	 * Tests to see if this logical sentence is equivalent to another
	 * They are equivalent if all truth values in one that result in true also result in true for the other expression.
	 * (One set of values returns true for one of the two, it must return true for the other one as well.)
	 * (If one is true, the other must be true. Otherwise not equivalent.)
	 * 
	 * @param expression the expression to be compared
	 * @return 1 if equivalent, 0 if unable to determine, -1 if not equivalent
	 */
	public int equivalent(LogicalExpression expression) {
		//If they have the same number of variables
		if(numVars == expression.getNumVars()) {
			for(int i = 0; i < getTruthTable().length; i++) {
				//If one is true but the other is not
				if((getTruthTable()[i] && !expression.getTruthTable()[i]) || (!getTruthTable()[i] && expression.getTruthTable()[i])) {
					return -1;
				}
			}
			//If there are no mismatches(all trues were true for the other one as well)
			return 1;
		}
		//If the number of variables are different
		return 0;
	}

	String removeParens(String str){
		while(str.contains("(") || str.contains(")")) {
		    if(str.contains("(")) {
		        str = str.replace("(","");
		    }
		    
		    if(str.contains(")")) {
		        str = str.replace(")","");
		    }
		}
		return str;  
		}
	
	@Override
	/**
	 * Tests to see if this logical sentence entails another
	 * This entails the other if all truth values resulting in true for the first also result in true for the second.
	 * (This differs from equivalence in that it is one way, while equivalence is two way)
	 * (If truth values for the first result in true, they must also result in true for the second as well)
	 * (Truth values that result in true for the second sentence do not have to result in true for the first as well.)
	 * 
	 * @param expression the expression to check entailment
	 * @return 1 if this entails the other, -1 is it doesn't, 0 if can't tell
	 */
	public int entails(LogicalExpression expression) {
		//If they have the same number of variables
		if(numVars == expression.getNumVars()) {
			for(int i = 0; i < getTruthTable().length; i++) {
				//If the first was true but the second was false
				if(getTruthTable()[i] && !expression.getTruthTable()[i]) {
					return -1;
				}
			}
			//If all instances of true for the first also resulted in true for the second
			return 1;
		}
		
		//If the number of variables were different
		else return 0;
	}

	@Override
	/**
	 * Gets the number of variables that this sentence has
	 * 
	 * @return the number of variables of this logical sentence
	 */
	public int getNumVars() {
		return numVars;
	}

	@Override
	/**
	 * Gets the expression of this logical sentence
	 * 
	 * @return the expression of this sentence
	 */
	public String getExpression() {
		return expression;
	}

}
