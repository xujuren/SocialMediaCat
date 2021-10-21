package anu.softwaredev.socialmediacat.Search;

//import Task1_Factory.Task1.Token;
//import Task1_Factory.Task1.Tokenizer;

import java.util.ArrayList;
import java.util.Scanner;

import Tree.Global_Data;
import anu.softwaredev.socialmediacat.Classes.Post;

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


//            if (parser.getTag()==null){
//                System.out.println("no Tag");
//            }else {
//                System.out.println(parser.getTag().show());
//            }
//
//            if (parser.getPostId()==null){
//                System.out.println("no postId");
//            }else {
//                System.out.println(parser.getPostId().show());
//
//            }


            ArrayList<Post> postsToShow = new ArrayList<>();

            String tagToSearch = "";
            String postIDToSearch = "";
            if (parser.getTag()==null) {
                System.out.println("no Tag");               //show purpose
            } else {
                System.out.println(parser.getTag().show());
                tagToSearch = parser.getTag().show();
            }
            if (parser.getPostId()==null){
                System.out.println("no postId");            //show purpose
            } else {
                System.out.println(parser.getPostId().show());
                postIDToSearch = parser.getPostId().show();
            }
            if (tagToSearch.equals("") && !postIDToSearch.equals("")){
                // only postid to search
                Post result = Global_Data.instance.searchById(postIDToSearch);
                if (result != null)
                    postsToShow.add(result);
            } else if (postIDToSearch.equals("") && !tagToSearch.equals("")){
                //only tag to search
                postsToShow.addAll(Global_Data.getInstance().searchByTag(tagToSearch)) ;
            } else if (tagToSearch.equals("") && postIDToSearch.equals("")){
                //empty, nothing to search
                System.out.println("nothing , Toaster throws reminder");
            } else {
                Post result = Global_Data.instance.search(tagToSearch,postIDToSearch);
                if (result != null)
                    postsToShow.add(result);
//            postsToShow.add() ;
            }

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




    }

