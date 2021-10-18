package anu.softwaredev.socialmediacat.Search;

//import Task1_Factory.Task1.Token;
//import Task1_Factory.Task1.Tokenizer;

import java.util.Scanner;

/**
 * Note: You will need to have completed task 1 to complete this task.
 * <p>
 * Welcome to task 2. In this task your job is to implement a simple parser.
 * It should be able to parser the following grammar rule:
 * <exp>    ::=  <term> | <term> + <exp> | <term> - <exp>
 * <term>   ::=  <factor> | <factor> * <term> | <factor> / <term>
 * <factor> ::=  <unsigned integer> | ( <exp> )
 * <p>
 * Here are some rules you must abide by for this task:
 * 1. You may NOT modify any other classes in this task 2 package.
 * 2. You may create additional fields or methods to finish you implementation within this class.
 * <p>
 * Parsing, within the context of this lab, is the process of taking a bunch of tokens and
 * evaluating them. You will not need to 'evaluate' them within this class, instead, just
 * return an expression which can be evaluated.
 */
public class Parser {
    /**
     * The following exception should be thrown if the parse is faced with series of tokens that do not
     * correlate with any possible production rule.
     */
    public static class IllegalProductionException extends IllegalArgumentException {
        public IllegalProductionException(String errorMessage) {
            super(errorMessage);
        }
    }

    // The tokenizer (class field) this parser will use.
    Tokenizer tokenizer;
    boolean illegal = false;

    /**
     * Parser class constructor
     * Simply sets the tokenizer field.
     * **** please do not modify this part ****
     */
    public Parser(Tokenizer tokenizer) {
        this.tokenizer = tokenizer;
    }

    /**
     * To help you both test and understand what this parser is doing, we have included a main method
     * which you can run. Once running, provide a mathematical string to the terminal and you will
     * receive back the result of your parsing.
     */
    public static void main(String[] args) {
        // Create a scanner to get the user's input.
        Scanner scanner = new Scanner(System.in);

        /*
         Continue to get the user's input until they exit.
         To exit press: Control + D or providing the string 'q'
         Example input you can try: ((1 + 2) * 5)/2
         Note that evaluations will round down to negative infinity (because they are integers).
         */
        System.out.println("Provide a mathematical string to be parsed:");
        while (scanner.hasNext()) {
            String input = scanner.nextLine();

            // Check if 'quit' is provided.
            if (input.equals("q"))
                break;

            // Create an instance of the tokenizer.
            Tokenizer tokenizer = new Tokenizer(input);

            // Print out the expression from the parser.
            Parser parser = new Parser(tokenizer);
//            System.out.println(parser.getTag().show());
//
//            System.out.println(parser.getPostId().show());
            if (parser.getTag()==null){
                System.out.println("no Tag");
            }else {
                System.out.println(parser.getTag().show());
            }

            if (parser.getPostId()==null){
                System.out.println("no postId");
            }else {
                System.out.println(parser.getPostId().show());

            }

//            Exp expression = parser.parseExp();
//            System.out.println("Parsing: " + expression.show());
//            System.out.println("Evaluation: " + expression.evaluate());
        }
    }


    public Exp getTag(){
        Tokenizer tok = tokenizer;
        //handle if no valid token at all
        if (tok.current()==null){
            return null;
        }
        if (tok.current().getType() == Token.Type.TAG){
            String current = tok.current().getToken();
            return new TagExp(current);
        } else {
            return null;
        }
    }

    public Exp getPostId(){
        Tokenizer tok = tokenizer;
        //handle if no valid token at all
        if (tok.current()==null){
            return null;
        }
        if (tok.current().getType() == Token.Type.POSTID){
            String current = tok.current().getToken();
            return new PostIdExp(current);
        } else {
            tok.next();
            tok.next();
            if (tok.current() == null){
                return null;
                // input(nothing) handle
            }
            String current = tok.current().getToken();
            return new PostIdExp(current);

        }
    }





    /**
     * Adheres to the grammar rule:
     * <exp>    ::= <term> | <term> + <exp> | <term> - <exp>
     *
     * @return type: Exp.
     */
    public Exp parseExp() throws IllegalProductionException {
        /*
         TODO: Implement parse function for <exp>.
         TODO: Throw an IllegalProductionException if provided with tokens not conforming to the grammar.
         Hint 1: you know that the first item will always be a term (according to the grammar).
         Hint 2: the possible grammar return '<term> + <exp>' correlates with the class (AddExp(term, exp)).
         */
        // ########## YOUR CODE STARTS HERE ##########
        Tokenizer tok = tokenizer;
//        Exp exp =

//        Exp term = parseTerm();
//
//
//        if(tok.hasNext()&&tok.current().getType()== Token.Type.ADD){
//            tok.next();
//            Exp exp = parseExp();
//            return new AndExp(term,exp);
//        }
//        if(tok.hasNext() && tok.current().getType()== Token.Type.SUB){
//            tok.next();
//            Exp exp = parseExp();
//            return new AndExp(term,exp);
//        } else {
//            if (illegal){
//                throw new IllegalProductionException("aaa");
//
//            }
//            return term;
//        }


        return null; // Change this return (if you want). It is simply a placeholder to prevent an error.
        // ########## YOUR CODE ENDS HERE ##########
    }

    /**
     * Adheres to the grammar rule:
     * <term>   ::=  <factor> | <factor> * <term> | <factor> / <term>
     *
     * @return type: Exp.
     */
    public Exp parseTerm() throws IllegalProductionException {
        /*
         TODO: Implement parse function for <term>.
         TODO: Throw an IllegalProductionException if provided with tokens not conforming to the grammar.
         Hint: you know that the first item will always be a factor (according to the grammar).
         */
        // ########## YOUR CODE STARTS HERE ##########
        Tokenizer tok = tokenizer;

        Exp factor = parseFactor();


        if(tok.hasNext() && tok.current().getType()== Token.Type.MUL){
            tok.next();
            Exp term = parseTerm();
            return new AndExp(factor,term);
        }
        if(tok.hasNext() && tok.current().getType()== Token.Type.DIV){
            tok.next();
            Exp term = parseTerm();
            return new AndExp(factor,term);
        } else {
            if (illegal){
                throw new IllegalProductionException("aaa");

            }
            return factor;
        }


//        return null; // Change this return (if you want). It is simply a placeholder to prevent an error.
        // ########## YOUR CODE ENDS HERE ##########
    }

    /**
     * Adheres to the grammar rule:
     * <factor> ::= <unsigned integer> | ( <exp> )
     *
     * @return type: Exp.
     */
    public Exp parseFactor() throws IllegalProductionException{
        /*
         TODO: Implement parse function for <factor>.
         TODO: Throw an IllegalProductionException if provided with tokens not conforming to the grammar.
         Hint: you can use Integer.parseInt() to convert a string into an integer.
         Fun fact: Integer.parseInt() is using a parser!
         */
        // ########## YOUR CODE STARTS HERE ##########
        Tokenizer tok = tokenizer;
        if (tok.current().getType() == Token.Type.LBRA){
            tok.next();
            Exp exp = parseExp();
            tok.next();
            return exp;
        } else {
            try {
                Exp i = new TagExp((tok.current().getToken()));
                tok.next();

                return i;
            } catch (NumberFormatException e){
                System.out.println("catch the error");
                illegal = true;
                System.out.println(illegal);
                throw new IllegalProductionException("yes");
            }
        }


//        public Exp parseExp2() throws IllegalProductionException {
//        /*
//         TODO: Implement parse function for <exp>.
//         TODO: Throw an IllegalProductionException if provided with tokens not conforming to the grammar.
//         Hint 1: you know that the first item will always be a term (according to the grammar).
//         Hint 2: the possible grammar return '<term> + <exp>' correlates with the class (AddExp(term, exp)).
//         */
//            // ########## YOUR CODE STARTS HERE ##########
//            Tokenizer tok = tokenizer;
//            if (illegal){
//                throw new IllegalProductionException("aaa");
//
//            }
//            Exp term = parseTerm();
//
//
//            if(tok.hasNext()&&tok.current().getType()== Token.Type.ADD){
//                tok.next();
//                Exp exp = parseExp2();
//                return new AddExp(term,exp);
//            }
//            if(tok.hasNext() && tok.current().getType()== Token.Type.SUB){
//                tok.next();
//                Exp exp = parseExp2();
//                return new SubExp(term,exp);
//            } else {
//                if (illegal){
//                    throw new IllegalProductionException("aaa");
//
//                }
//                return term;
//            }


//        return null; // Change this return (if you want). It is simply a placeholder to prevent an error.
        // ########## YOUR CODE ENDS HERE ##########
    }
}
