public class TercetoPosicion implements Terceto {

    protected int numero;

    public TercetoPosicion(int numero)
    {
        this.numero = numero;
    }

    public Integer getNumero()
    {
        return this.numero;
    }

    public void setNumero(int numero)
    {
        this.numero = numero;
    }

    public boolean esVariableOConstante()
    {
        return false;
    }

    public String getResultado()
    {
        return String.valueOf(this.getNumero());
    }

    public String getTextoOperando()
    {
        return "[" +  this.numero + "]";
    }

    public String toString()
    {
        return this.getTextoOperando();
    }
}
