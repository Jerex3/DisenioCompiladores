public class TercetoLexema implements Terceto{
    private String lexema;

    public TercetoLexema(String lexema)
    {
        this.lexema = lexema;
    }

    public String getLexema()
    {
        return this.lexema;
    }

    public void setLexema(String lexema)
    {
        this.lexema = lexema;
    }

    public boolean esVariableOConstante()
    {
        return true;
    }

    public String getResultado()
    {
        if(this.lexema.contains("@"))
        {
            return "_" + this.lexema;
        }
        return this.getTextoOperando();
    }

    public String getTextoOperando()
    {
        return this.lexema;
    }

    public String toString()
    {
        return this.getTextoOperando();
    }
}
