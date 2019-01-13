package app;

import java.io.*;
import java.util.*;
import java.util.regex.*;

import structures.Stack;

public class Expression {

	public static String delims = " \t*+-/()[]";
			
	public static void makeVariableLists(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays) {
		String arrayName = "";
		String variableName = "";
		String name = "";
		for(int i = 0; i<expr.length(); i++) 
		{
			if(Character.isLetter(expr.charAt(i)) == true) 
			{
				name += expr.charAt(i);
				if ( ((i+1) == expr.length()) || (!Character.isLetter((expr.charAt(i+1))) && expr.charAt(i+1) != '[') ) 
				{
					boolean contained = false;
					variableName = name;
					for(int k = 0; k<vars.size(); k++) 
					{
						if(vars.get(k).name.equals(variableName))
							contained = true;
					}
					if(!contained)
						vars.add(new Variable(variableName));
					name="";
				}	
				else if(expr.charAt(i+1) == '[') 
				{
					arrayName = name;
					boolean contained = false;
					for(int j = 0; j<arrays.size(); j++) 
					{
						if(arrays.get(j).name.equals(arrayName))
							contained = true;
					}
					if(!contained)
						arrays.add(new Array(arrayName));
					name="";
				}
			}
		}
	}
	
    public static void 
    loadVariableValues(Scanner sc, ArrayList<Variable> vars, ArrayList<Array> arrays) 
    throws IOException {
        while (sc.hasNextLine()) {
            StringTokenizer st = new StringTokenizer(sc.nextLine().trim());
            int numTokens = st.countTokens();
            String tok = st.nextToken();
            Variable var = new Variable(tok);
            Array arr = new Array(tok);
            int vari = vars.indexOf(var);
            int arri = arrays.indexOf(arr);
            if (vari == -1 && arri == -1) {
            	continue;
            }
            int num = Integer.parseInt(st.nextToken());
            if (numTokens == 2) { 
                vars.get(vari).value = num;
            } else { 
            	arr = arrays.get(arri);
            	arr.values = new int[num];
             
                while (st.hasMoreTokens()) {
                    tok = st.nextToken();
                    StringTokenizer stt = new StringTokenizer(tok," (,)");
                    int index = Integer.parseInt(stt.nextToken());
                    int val = Integer.parseInt(stt.nextToken());
                    arr.values[index] = val;              
                }
            }
        }

    }

	private static String evaluateP(String expr) {
		int startIndex = -1;
		int endInd = -1;
		int paranthesesCount = 1;
		if(expr.equals(""))
			return "";
		if(expr.indexOf('(') == -1) 
			return evaluateAdditionSubtraction(evaluateMultiplyDivide(expr));
		int index = 0;
		while(index < expr.length())
		{
			if(expr.indexOf('(') == -1)
				break;
			if(expr.charAt(index) == '(')
			{
				if(startIndex == -1)
					startIndex = index;
				while(startIndex == index || paranthesesCount != 0) 
				{
					index++;
					if(expr.charAt(index) == ')') 
						paranthesesCount = paranthesesCount - 1;
					else if(expr.charAt(index) == '(')
						paranthesesCount = paranthesesCount + 1;
				}
				endInd = index;
			}
			if(endInd != -1 && startIndex != -1)
			{
				String ans = evaluateP(expr.substring(startIndex + 1, endInd));
				expr = expr.substring(0, startIndex) + ans + expr.substring(endInd + 1);
				index = -1;
				startIndex = -1;
				endInd = -1;
				paranthesesCount = 1;
			}
			index++;
		}
		return evaluateP(expr);
	}

	private static String evaluateMultiplyDivide(String expr) {
		if(expr.equals(""))
			return "";
		if(expr.indexOf('/') == -1 && expr.indexOf('*') == -1)
			return expr;
		float ans = 0;
		int startIndex = -1;
		int endIndex = -1;
		String firstNum = "";
		String secondNum = "";
		int index =0;
		while(index < expr.length())
		{
			if((firstNum == "" && expr.charAt(index) == '-') || Character.isDigit(expr.charAt(index)) || expr.charAt(index) == '.') 
			{
				firstNum = firstNum + expr.charAt(index);
				if(startIndex == -1)
					startIndex = index;
			}
			else if(firstNum != "")
			{
				if(!(expr.charAt(index) == '+' || expr.charAt(index) == '-'))
				{
					String action = expr.charAt(index) + "";
					index++;
					while(index < expr.length() && (Character.isDigit(expr.charAt(index)) || expr.charAt(index) == '.' || expr.charAt(index) == '-'))
					{
						secondNum += expr.charAt(index);
						index++;
					}
					endIndex = index;
					if(action.equals("/"))
						ans = Float.parseFloat(firstNum) / Float.parseFloat(secondNum);
					else
						ans = Float.parseFloat(firstNum) * Float.parseFloat(secondNum);
					return evaluateMultiplyDivide(expr.substring(0,startIndex) + ans + expr.substring(endIndex));
				}
				else 
				{
					startIndex = -1;
					firstNum = "";
					index++;
					continue;
				}
			}
			index++;
		}
		return expr;
	}

	private static String evaluateAdditionSubtraction(String expr) {
		if(expr.equals(""))
			return "";
		if(expr.indexOf('-') == -1 && expr.indexOf('+') == -1)
			return expr;
		float ans = 0;
		int startIndex = -1;
		int endIndex = -1;
		String firstNum = "";
		String secondNum = "";
		int index = 0;
		while(index < expr.length())
		{
			if((firstNum == "" && expr.charAt(index) == '-') || Character.isDigit(expr.charAt(index)) || expr.charAt(index) == '.' ) 
			{
				firstNum += expr.charAt(index);
				if(startIndex == -1)
					startIndex = index;
			}
			else if(firstNum != "")
			{
					String action = expr.charAt(index) + "";
					index++;
					if(expr.charAt(index) == '-')
					{
						if(action.equals("-"))
							action = "+";
						else
							action = "-";
						expr = expr.substring(0,index) + expr.substring(index + 1);
					}
					while(index < expr.length() && (Character.isDigit(expr.charAt(index)) || expr.charAt(index) == '.'))
					{
						secondNum += expr.charAt(index);
						index++;
					}
					if(action.equals("-"))
						ans = Float.parseFloat(firstNum) - Float.parseFloat(secondNum);
					else
						ans = Float.parseFloat(firstNum) + Float.parseFloat(secondNum);
					endIndex = index;
					return evaluateAdditionSubtraction(expr.substring(0,startIndex) + ans + expr.substring(endIndex));
			}
			index++;
		}
		return expr;
	}
	
	private static String helperReplaceV(String expr, ArrayList<Variable> vars) {
		expr = expr.replace(" ", "");
		String nameOfVariable = "";
		int index = 0;
		while(index < expr.length())
		{
			if(expr.charAt(index) == '[') 
				nameOfVariable = "";
			else if (Character.isLetter(expr.charAt(index)) == true)
				nameOfVariable += expr.charAt(index);
			else if(nameOfVariable != "")
			{
				float varVal = 0;
				int variableIndex = 0;
				while(variableIndex < vars.size())
				{
					if(vars.get(variableIndex).name.equals(nameOfVariable))
						varVal = vars.get(variableIndex).value;
					variableIndex++;
				}
				int digitCount = (varVal + "").length();
				expr = expr.substring(0,index - nameOfVariable.length()) + varVal + expr.substring(index);
				index -= nameOfVariable.length() - digitCount + 1;
				nameOfVariable = "";
			}
			if(index == expr.length() -1 && nameOfVariable != "") 
			{
				float varVal = 0;
				int variableIndex = 0;
				while(variableIndex < vars.size())
				{
					if(vars.get(variableIndex).name.equals(nameOfVariable))
						varVal = vars.get(variableIndex).value;
					variableIndex++;
				}
				expr = expr.substring(0, index - nameOfVariable.length() + 1) + varVal;
				return expr;
			}	
			index++;
		}
		return expr;
	}
	
	private static String helperReplaceA(String expr, ArrayList<Array> arrays) {
		int endInd = -1;
		int startIndex = -1;
		int bracketCounter = 1;
		int startbIndex = -1;
		String nameOfVariable = "";
		if(expr == "" || expr.indexOf('[') == -1) 
		{
			expr = evaluateP(expr);
			return expr;
		}
		int index = 0;
		while(index < expr.length())
		{
			if(expr.indexOf('[') == -1)
				break;
			if(expr.charAt(index) == '[')
			{
				if(startbIndex == -1)
					startbIndex = index;
				while(startbIndex == index || bracketCounter != 0) 
				{
					index++;
					if(expr.charAt(index) == ']') 
						bracketCounter = bracketCounter - 1;
					else if(expr.charAt(index) == '[')
						bracketCounter = bracketCounter + 1;
				}
				endInd = index;
			}
			else if(Character.isLetter(expr.charAt(index)))
			{
				if(startIndex == -1)
					startIndex = index;
				nameOfVariable = nameOfVariable + expr.charAt(index);
			}
			if(endInd != -1 && startbIndex != -1)
			{
				String inner = helperReplaceA(expr.substring(startbIndex + 1, endInd), arrays);
				int[] array = new int[1];
				int arrayIndex = 0;
				while(arrayIndex < arrays.size())
				{
					if(arrays.get(arrayIndex).name.equals(nameOfVariable))
						array = arrays.get(arrayIndex).values;
					arrayIndex++;
				}
				expr = expr.substring(0, startIndex) + array[Math.round(Float.parseFloat(inner))] + expr.substring(endInd + 1);
				nameOfVariable = "";
				index = -1;
				startbIndex = -1;
				startIndex = -1;
				endInd = -1;
				bracketCounter = 1;
			}
			index++;
		}
		return helperReplaceA(expr, arrays);
	}
	
	public static float evaluate(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays) {
		return Float.parseFloat(evaluateP(helperReplaceA(helperReplaceV(expr, vars), arrays)));
	}
}