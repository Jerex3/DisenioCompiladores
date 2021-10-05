//Clase que se utiliza para almacenar la información en la tabla de simbolos
public class EntradaTablaSimbolos {
    public static final String IDENTIFICADOR = "Identificador";
    public static final String CADENA = "Cadena";
    public static final String INT = "Int";
    public static final String SINGLE = "Single";
    private String lexema;
    private String tipo;

    public EntradaTablaSimbolos(String lexema, String tipo) {
        this.lexema = lexema;
        this.tipo = tipo;
    }

    public String getLexema() {
        return lexema;
    }

    public void setLexema(String lexema) {
        this.lexema = lexema;
    }

    public String getTipo() {
        return tipo;
    }

}