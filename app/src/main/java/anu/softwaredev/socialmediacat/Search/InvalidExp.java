package anu.softwaredev.socialmediacat.Search;


public class InvalidExp extends Exp{
    private String invalidString;

    public InvalidExp(String postId){
        this.invalidString = invalidString;
    }

    public Token getToken(){
        return new Token(invalidString, Token.Type.INVALID);
    }

    public String getValue(){
        return invalidString;
    }

    @Override
    public String show() {
        if (invalidString.equals(null)){
            return "invalid query";
        }


        return invalidString;
    }

    @Override
    public int evaluate() {
        return 0;
    }
}
