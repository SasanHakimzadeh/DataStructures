package poly;

import java.io.IOException;
import java.util.Scanner;

/**
 * This class implements evaluate, add and multiply for polynomials.
 * 
 * @author runb-cs112
 *
 */
public class Polynomial {
	
	/**
	 * Reads a polynomial from an input stream (file or keyboard). The storage format
	 * of the polynomial is:
	 * <pre>
	 *     <coeff> <degree>
	 *     <coeff> <degree>
	 *     ...
	 *     <coeff> <degree>
	 * </pre>
	 * with the guarantee that degrees will be in descending order. For example:
	 * <pre>
	 *      4 5
	 *     -2 3
	 *      2 1
	 *      3 0
	 * </pre>
	 * which represents the polynomial:
	 * <pre>
	 *      4*x^5 - 2*x^3 + 2*x + 3 
	 * </pre>
	 * 
	 * @param sc Scanner from which a polynomial is to be read
	 * @throws IOException If there is any input error in reading the polynomial
	 * @return The polynomial linked list (front node) constructed from coefficients and
	 *         degrees read from scanner
	 */
	public static Node read(Scanner sc) 
	throws IOException {
		Node poly = null;
		while (sc.hasNextLine()) {
			Scanner scLine = new Scanner(sc.nextLine());
			poly = new Node(scLine.nextFloat(), scLine.nextInt(), poly);
			scLine.close();
		}
		return poly;
	}
	
	/**
	 * Returns the sum of two polynomials - DOES NOT change either of the input polynomials.
	 * The returned polynomial MUST have all new nodes. In other words, none of the nodes
	 * of the input polynomials can be in the result.
	 * 
	 * @param poly1 First input polynomial (front of polynomial linked list)
	 * @param poly2 Second input polynomial (front of polynomial linked list
	 * @return A new polynomial which is the sum of the input polynomials - the returned node
	 *         is the front of the result polynomial
	 */
	public static Node add(Node poly1, Node poly2) {
		Node sumfront = null;
		Node ptr1 = poly1;
		Node ptr2 = poly2; 
		System.out.println(toString(poly1));
		System.out.println(toString(poly2));
		while(ptr1 != null || ptr2 !=null) {
			
			if(ptr1 == null) {
				Node a = new Node(ptr2.term.coeff, ptr2.term.degree, sumfront); 
				sumfront = a;
				ptr2 = ptr2.next;
			}
			else if(ptr2 == null) {
				Node a = new Node(ptr1.term.coeff, ptr1.term.degree, sumfront); 
				sumfront = a;
				ptr1 = ptr1.next;
			}
			
			
			else	 if(ptr1.term.degree < ptr2.term.degree) {
					
					Node a = new Node(ptr1.term.coeff, ptr1.term.degree, sumfront); 
					sumfront = a;
					ptr1 = ptr1.next;
				}
				
			else if(ptr2.term.degree < ptr1.term.degree) {
					
					Node a = new Node(ptr2.term.coeff, ptr2.term.degree, sumfront); 
					sumfront = a;
					ptr2 = ptr2.next;
				
			}
			else if(ptr1.term.degree == ptr2.term.degree) {
					if(ptr1.term.coeff + ptr2.term.coeff != 0) {
						
					Node a = new Node(ptr1.term.coeff + ptr2.term.coeff, ptr1.term.degree, sumfront); 
					sumfront = a;
					}
					
					ptr1 = ptr1.next;
					ptr2 = ptr2.next;
				}
					
		}
		
		Node currNode = sumfront;
		Node nextNode = null;
		Node prevNode = null;
		while(currNode!=null){
		     nextNode = currNode.next;
		     currNode.next = prevNode;
		     prevNode = currNode;
		     currNode = nextNode;
		}
		return prevNode;
	}
	
	/**
	 * Returns the product of two polynomials - DOES NOT change either of the input polynomials.
	 * The returned polynomial MUST have all new nodes. In other words, none of the nodes
	 * of the input polynomials can be in the result.
	 * 
	 * @param poly1 First input polynomial (front of polynomial linked list)
	 * @param poly2 Second input polynomial (front of polynomial linked list)
	 * @return A new polynomial which is the product of the input polynomials - the returned node
	 *         is the front of the result polynomial
	 */
	private static Node multiplyHelper(float coeff,int degree, Node poly) {
		if (poly == null)
			return null;
		else 
		return new Node(coeff * poly.term.coeff, degree + poly.term.degree, multiplyHelper(coeff, degree, poly.next));	
	}
	
	public static Node multiply(Node poly1, Node poly2) {
		if (poly1 == null || poly2 == null) {
			return null;
		}
		
		Node product = null;
		float coeff; int degree;
		Node ptr = poly1;
		while(ptr != null)
		{
			coeff = ptr.term.coeff;
			degree = ptr.term.degree;
			product = add(product, multiplyHelper(coeff, degree, poly2));
			//System.out.println(add(product, multiplyTerm(coeff, degree, poly2)));
			ptr = ptr.next;
		}
		return product;
	}
	
		
	/**
	 * Evaluates a polynomial at a given value.
	 * 
	 * @param poly Polynomial (front of linked list) to be evaluated
	 * @param x Value at which evaluation is to be done
	 * @return Value of polynomial p at x
	 */
	public static float evaluate(Node poly, float x) {
		float answer = 0;Node ptr = poly;
		while(ptr != null) {
			answer += (Math.pow(x, ptr.term.degree) * ptr.term.coeff);
			ptr = ptr.next;
		}
		System.out.println(answer); return answer;
		
	}   //hhh
	
	/**
	 * Returns string representation of a polynomial
	 * 
	 * @param poly Polynomial (front of linked list)
	 * @return String representation, in descending order of degrees
	 */
	public static String toString(Node poly) {
		if (poly == null) {
			return "0";
		} 
		
		String retval = poly.term.toString();
		for (Node current = poly.next ; current != null ;
		current = current.next) {
			retval = current.term.toString() + " + " + retval;
		}
		return retval;
	}	
}