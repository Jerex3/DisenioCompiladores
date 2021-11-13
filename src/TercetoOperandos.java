public class TercetoOperandos extends TercetoPosicion{

    private String operando;
    private Terceto operador1;
    private Terceto operador2;
    private String tipo;
    private String resultado;

    public TercetoOperandos(String operando, Terceto operador1, Terceto operador2)
    {
        super(0);
        this.operando = operando;
        this.operador1 = operador1;
        this.operador2 = operador2;
        this.tipo = "";
        this.resultado = "";
    }

    public TercetoOperandos(String operando, Terceto operador1)
    {
        super(0);
        this.operando = operando;
        this.operador1 = operador1;
        this.operador2 = null;
        this.tipo = "";
        this.resultado = "";
    }

    public TercetoOperandos(String etiqueta)
    {
        super(0);
        this.operando = etiqueta;
        this.operador1 = null;
        this.operador2 = null;
        this.tipo = "";
        this.resultado = "";
    }

    public String getOperando()
    {
        return this.operando;
    }

    public void setOperando(String operando)
    {
        this.operando = operando;
    }

    public Terceto getOperador1()
    {
        return this.operador1;
    }

    public void setOperador1(Terceto operador1)
    {
        this.operador1 = operador1;
    }

    public Terceto getOperador2()
    {
        return this.operador2;
    }

    public void setOperador2(Terceto operador2)
    {
        this.operador2 = operador2;
    }

    public String getTipo()
    {
        return this.tipo;
    }

    public void setTipo(String tipo)
    {
        this.tipo = tipo;
    }

    public boolean esVariableOConstante()
    {
        return false;
    }

    public String getResultado()
    {
        if(this.resultado.contains("@"))
        {
            return "_" + this.resultado;
        }
        return this.resultado;
    }

    public void setResultado(String resultado)
    {
        this.resultado = resultado;
    }

    public String toString()
    {
        StringBuilder textoADevolver = new StringBuilder();
        textoADevolver.append(this.getNumero());
        textoADevolver.append(". ( ");
        textoADevolver.append(this.getOperando());
        textoADevolver.append(" , ");
        if(this.getOperador1() != null) {
            textoADevolver.append(this.getOperador1().getTextoOperando());
        }
        else
        {
            textoADevolver.append("--");
        }
        textoADevolver.append(" , ");
        if(this.getOperador2() != null)
        {
            textoADevolver.append(this.getOperador2().getTextoOperando());
        }
        else
        {
            textoADevolver.append("--");
        }
        textoADevolver.append(" )");
        return textoADevolver.toString();
    }
}
