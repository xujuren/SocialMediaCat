package anu.softwaredev.socialmediacat.Search;

import java.util.Scanner;

/**
 * Welcome to Task 1.
 * In this task your job is to implement the next() method. Just some rules:
 * 1. You may NOT modify any other classes in this task 1 package.
 * 2. You may not modify any of the methods or fields (that already exist) within this class.
 * 3. You may create additional fields or methods to finish you implementation within this class.
 * <p>
 * Tokenization, within the context of this lab, is the process of splitting a string into
 * small units called, 'Tokens', to be passed onto the Parser.
 */
public class Tokenizer {
    private String buffer;          // String to be transformed into tokens each time next() is called.
    private Token currentToken;     // The current token. The next token is extracted when next() is called.

    /**
     * To help you both test and understand what this tokenizer is doing, we have included a main method
     * which you can run. Once running, provide a mathematical string to the terminal and you will
     * receive back the result of your tokenization.
     */
    public static void main(String[] args) {
        // Create a scanner to get the user's input.
        Scanner scanner = new Scanner(System.in);

        /*
         Continue to get the user's input until they exit.
         To exit press: Control + D or providing the string 'q'
         Example input you can try: ((1 + 2) * 5)/2
         */
        System.out.println("Provide a mathematical string to be tokenized:");
        while (scanner.hasNext()) {
            String input = scanner.nextLine();

            // Check if 'quit' is provided.
            if (input.equals("q"))
                break;

            // Create an instance of the tokenizer.
            Tokenizer tokenizer = new Tokenizer(input);

            // Print all the tokens.
            while (tokenizer.hasNext()) {
                System.out.print(tokenizer.current() + " ");
                tokenizer.next();
            }
            System.out.println();
        }
    }

    /**
     * Tokenizer class constructor
     * The constructor extracts the first token and save it to currentToken
     * **** please do not modify this part ****
     */
    public Tokenizer(String text) {
        buffer = text;          // save input text (string)
        next();                 // extracts the first token.
    }

    /**
     * This function will find and extract a next token from {@code _buffer} and
     * save the token to {@code currentToken}.
     */
    public void next() throws Token.IllegalTokenException {
        buffer = buffer.trim();     // remove whitespace

        if (buffer.isEmpty()) {
            currentToken = null;    // if there's no string left, set currentToken null and return
            return;
        }

        /*
        To help you, we have already written the first few steps in the tokenization process.
        The rest will follow a similar format.
         */
        char firstChar = buffer.charAt(0);



        //tag
        if (firstChar == '#'){
            StringBuilder stringBuilder =  new StringBuilder();
            int pos = 1 ;
            //current char (digit) , pointer
            char current = buffer.charAt(pos);
            while(current!=';'){
                stringBuilder.append(current);
                pos++;
                // if else to handle the out of range bug
                //make sure the current pointer is not out of bounds
                if(pos<buffer.length()){
                    current = buffer.charAt(pos);
                } else {
                    break;
                }
            }
            currentToken = new Token(stringBuilder.toString(), Token.Type.TAG);

        }

        //post ID
        boolean existInvalidToken = false;
        if (firstChar == '@'){
            StringBuilder stringBuilder =  new StringBuilder();
            int pos = 1 ;
            //current char (digit) , pointer
            char current = buffer.charAt(pos);
            while(current!=';'){
                //invalid token
                if (current=='@' || current == '#'){
                    existInvalidToken = true;
//                    break;
                }

                stringBuilder.append(current);
                pos++;
                // if else to handle the out of range bug
                //make sure the current pointer is not out of bounds
                if(pos<buffer.length()){
                    current = buffer.charAt(pos);
                } else {
                    break;
                }
            }
            if (existInvalidToken){
                currentToken = new Token(stringBuilder.toString(), Token.Type.INVALID);

            } else {
                currentToken = new Token(stringBuilder.toString(), Token.Type.POSTID);

            }

        }
        if (firstChar == ';'){
            currentToken = new Token(";", Token.Type.AND);
        }


        // Remove the extracted token from buffer
        if (currentToken==null){
//            throw new Token.IllegalTokenException("no valid query string");
            System.out.println("no valid query string");
        } else {
            int tokenLen = currentToken.getToken().length();
            if(currentToken.getType().equals(Token.Type.TAG)){
                tokenLen++;
            }
            if(currentToken.getType().equals(Token.Type.POSTID)){
                tokenLen++;
            }
            if(currentToken.getType().equals(Token.Type.INVALID)){
                tokenLen++;
            }
            buffer = buffer.substring(tokenLen);
        }


    }


    /**
     * Returns the current token extracted by {@code next()}
     * **** please do not modify this part ****
     *
     * @return type: Token
     */
    public Token current() {
        return currentToken;
    }

    /**
     * Check whether there still exists another tokens in the buffer or not
     * **** please do not modify this part ****
     *
     * @return type: boolean
     */
    public boolean hasNext() {
        return currentToken != null;
    }
}