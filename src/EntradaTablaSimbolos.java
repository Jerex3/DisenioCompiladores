import java.util.ArrayList;

//Clase que se utiliza para almacenar la información en la tabla de simbolos
public class EntradaTablaSimbolos {
    public static final String IDENTIFICADOR = "Identificador";
    public static final String CADENA = "Cadena";
    public static final String INT = "Int";
    public static final String SINGLE = "Single";

    private String lexema;
    private String tipo;

    private String uso; //nombre de variable, nombre de procedimimento, nombre de parámetro
    private Integer naContenidos;
    private int lineaDeclaracion;
    private ArrayList<Integer> listaReferencias;


    public EntradaTablaSimbolos(String tipo, int lineaDeclaracion)
    {
        this.lexema = "";
        this.tipo = tipo;
        this.uso = new String();
        this.naContenidos = null;
        this.lineaDeclaracion = lineaDeclaracion;
        this.listaReferencias = new ArrayList<Integer>();
    }

   public EntradaTablaSimbolos(String lexema, String tipo, int lineaDeclaracion){
        this.lexema = lexema;
        this.tipo = tipo;
        this.uso = new String();
        this.naContenidos = null;
        this.lineaDeclaracion = lineaDeclaracion;
        this.listaReferencias = new ArrayList<Integer>();
    }

    //***********Métodos de la clase AtributosTablaDeSimbolos***********
    public String getLexema()
    {
        return this.lexema;
    }

    public void setLexema(String lexema)
    {
        this.lexema = lexema;
    }

    public String getTipo()
    {
        return this.tipo;
    }

    public void setTipo(String tipo)
    {
        this.tipo = tipo;
    }

    public String getUso()
    {
        return this.uso;
    }

    public void setUso(String uso)
    {
        this.uso = uso;
    }

    public Integer getNaContenidos()
    {
        return this.naContenidos;
    }

    public void setNaContenidos(Integer naContenidos)
    {
        this.naContenidos = naContenidos;
    }

    public void aumentarNaContenidos()
    {
        this.naContenidos++;
    }

    public Integer getLineaDeclaracion()
    {
        return this.lineaDeclaracion;
    }

    public boolean esReferenciado()
    {
        return this.listaReferencias.size() > 0;
    }

    public void agregarReferencia(int referencia)
    {
        this.listaReferencias.add(referencia);
    }

    public void borrarUltimaReferencia()
    {
        this.listaReferencias.remove(this.listaReferencias.size() - 1);
    }

    public ArrayList<Integer> getListaReferencias()
    {
        ArrayList<Integer> copiaLista = new ArrayList<Integer>();
        for(int i = 0; i < this.listaReferencias.size(); i++)
        {
            copiaLista.add(this.listaReferencias.get(i));
        }
        return copiaLista;
    }

}