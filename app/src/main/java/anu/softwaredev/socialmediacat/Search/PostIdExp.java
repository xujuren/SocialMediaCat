package anu.softwaredev.socialmediacat.Search;

public class PostIdExp extends Exp{
    private String postId;

    public PostIdExp(String postId){
        this.postId = postId;
    }

    public Token getToken(){
        return new Token(postId, Token.Type.POSTID);
    }

    public String getValue(){
        return postId;
    }

    @Override
    public String show() {
        if (postId.equals(null)){
            return "invalid query";
        }


        return postId;
    }

    @Override
    public int evaluate() {
        return 0;
    }
}
