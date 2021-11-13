public class Tripla {
    private String conversionPrimerOperando;
    private String tipo;
    private String conversionSegundoOperando;

    public Tripla(String conversionPrimerOperando, String tipo, String conversionSegundoOperando)
    {
        this.conversionPrimerOperando = conversionPrimerOperando;
        this.tipo = tipo;
        this.conversionSegundoOperando = conversionSegundoOperando;
    }

    public String getConversionPrimerOperando()
    {
        return this.conversionPrimerOperando;
    }

    public void setConversionPrimerOperando(String conversionPrimerOperando)
    {
        this.conversionPrimerOperando = conversionPrimerOperando;
    }

    public String getTipo()
    {
        return this.tipo;
    }

    public void setTipo(String tipo)
    {
        this.tipo = tipo;
    }

    public String getConversionSegundoOperando()
    {
        return this.conversionSegundoOperando;
    }

    public void setConversionSegundoOperando(String conversionSegundoOperando)
    {
        this.conversionSegundoOperando = conversionSegundoOperando;
    }
}
