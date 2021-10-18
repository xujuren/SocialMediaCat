package anu.softwaredev.socialmediacat.Search;

public class AndExp  extends  Exp{
    private Exp term;
    private Exp exp;

    public AndExp(Exp term, Exp exp) {
        this.term = term;
        this.exp = exp;
    }

    public Token getToken(){
        return null;
    }

    public String getValue(){
        return "(" + term.show() + " + " + exp.show() + ")";
    }

    @Override
    public String show() {
        return "(" + term.show() + " + " + exp.show() + ")";
    }

    @Override
    public int evaluate() {
        return 0;
    }
}
