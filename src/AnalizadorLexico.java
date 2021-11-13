

import java.util.ArrayList;
import java.util.HashMap;

public class AnalizadorLexico {

    public static int ERROR = 256; //estado error
    public static int FINAL = 100; //estado final
    public static int FINALARCHIVO = 1000; //estado final de archivo

    //para controlar rangos en constantes
    public static double MIN_SINGLE_POSITIVO = 1.17549435*Math.pow(10,-38);
    public static double MAX_SINGLE_POSITIVO = 3.40282347*Math.pow(10,38);
    public static int MAX_INT_ABSOLUTO = (int) Math.pow(2,15);
    public static int MAX_INT = (int) Math.pow(2,15) - 1;
    public static int MIN_INT = - (int) Math.pow(2,15);

    //tabla de simbolos
    private HashMap<String, EntradaTablaSimbolos> tablaDeSimbolos = new HashMap<>();
    
    //--------//

    private CambioEstado cambiadorEstado = new CambioEstado();
    private Reader reader;
    private String buffer = "";
    private int tokenActual = -1;
    private char c;
    private int estadoActual = 0;
    //gernero las matrices
    private int[][] mTE = LectorMatrizTE.getMatriz();
    private AccionSemantica[][] mAS = LectorMatrizAS.getMatriz();

    private String entrada;
    private ArrayList<String> listaPalabrasReservadas = new ArrayList<>();
    private ArrayList<String> listaDeErroresLexicos = new ArrayList<>();
    private ArrayList<String> listaDeTokens = new ArrayList<>();

    public AnalizadorLexico(Reader reader) {
        this.reader = reader;
        cargarListaPR();
    }

    //verifica si el input es una palabra reservada
    public boolean esPalabraReservada(String pr) {
        return listaPalabrasReservadas.contains(pr);
    }

    //retorna la lista de errores lexicos
    public ArrayList<String> getListaDeErroresLexicos() {
        return listaDeErroresLexicos;
    }

    //retorna la lista de tokens
    public ArrayList<String> getListaDeTokens() {
        return listaDeTokens;
    }

    //agrega un error lexico
    public void addListaDeErroresLexicos(String error) {
        this.listaDeErroresLexicos.add(error);
    }

    //agrega un token
    public void addListaDeTokens(String token) {
        this.listaDeTokens.add(token);
    }

    //agrega una entrada a la tabla de simbolos
    public void agregarATablaSimbolos(EntradaTablaSimbolos entrada) {
        tablaDeSimbolos.put(entrada.getLexema(), entrada);
    }

    //para saber si un lexema está en la tabla de simbolos
    public boolean estaEnTabla(String lexema) {
        return !(tablaDeSimbolos.get(lexema) == null);
    }

    public EntradaTablaSimbolos estaEnTablaSimbolos(String lexema)
    {
        int primerPunto = lexema.indexOf(".");
        String nombreVariable = lexema.substring(0, primerPunto);
        String ambito = lexema.substring(primerPunto);
        int primerPuntoTabla = 0;
        EntradaTablaSimbolos atributosTablaDeSimbolos = null;
        for(HashMap.Entry<String, EntradaTablaSimbolos> entry : this.tablaDeSimbolos.entrySet())
        {
            primerPuntoTabla = entry.getKey().indexOf(".");
            if(primerPuntoTabla >= 0)
            {
                String nombreVariableTabla = entry.getKey().substring(0, primerPuntoTabla);
                String ambitoTabla = entry.getKey().substring(primerPuntoTabla);
                if(nombreVariable.equals(nombreVariableTabla) && ambito.contains(ambitoTabla))
                {
                    atributosTablaDeSimbolos = entry.getValue();
                    atributosTablaDeSimbolos.setLexema(entry.getKey());
                    if(nombreVariable.equals(nombreVariableTabla) && ambito.equals(ambitoTabla))
                    {
                        return atributosTablaDeSimbolos;
                    }
                }

            }
        }

        return atributosTablaDeSimbolos;
    }

    //se utiliza para inicializar la lista de palabras reservadas
    private void cargarListaPR() { // IF, THEN, ELSE, ENDIF, PRINT, FUNC, RETURN, BEGIN, END, BREAK, INT, SINGLE, WHILE, DO, POST
        listaPalabrasReservadas.add("IF");
        listaPalabrasReservadas.add("THEN");
        listaPalabrasReservadas.add("ELSE");
        listaPalabrasReservadas.add("ENDIF");
        listaPalabrasReservadas.add("PRINT");
        listaPalabrasReservadas.add("FUNC");
        listaPalabrasReservadas.add("RETURN");
        listaPalabrasReservadas.add("BEGIN");
        listaPalabrasReservadas.add("END");
        listaPalabrasReservadas.add("BREAK");
        listaPalabrasReservadas.add("INT");
        listaPalabrasReservadas.add("SINGLE");
        listaPalabrasReservadas.add("WHILE");
        listaPalabrasReservadas.add("DO");
        listaPalabrasReservadas.add("POST");
    }

    //retorna la línea que se está leyendo del programa fuente
    public int getLinea() {
        return reader.getActualLine();
    }

    //retorna el programa fuente
    private Reader getReader() {
        return reader;
    }

    //retorna el buffer en el estado actual
    public String getBuffer() {
        return buffer.toString();
    }

    //setea un valor al buffer
    public void setBuffer(String buffer) {
        this.buffer = buffer;
    }

    //setea el token actual leido
    public void setTokenActual(int tokenActual) {
        this.tokenActual = tokenActual;
    }

    //retorna el ultimo caracter leido
    public char getChar() {
        return c;
    }

    //retorna el siguiente token según la especificación del yacc
    //si se encuentra el fin de archivo retona -1
    public int getToken() {
        entrada = null;
        tokenActual = ERROR;
        buffer = "";
        estadoActual = 0; //Estado inicial.
        while ((estadoActual != ERROR) && (estadoActual != FINAL) && (estadoActual != FINALARCHIVO)) {
            c = (char) reader.getCaracter();

            //encuentra la accion semantica según la el caracter leido
            AccionSemantica aS = mAS[cambiadorEstado.getColumnaSimbolo(c)][estadoActual];
            //ejecuta dicha accion semantica
            aS.ejecutar(this);
            //encuentra el siguiente estado segun el caracter leido
            estadoActual = mTE[cambiadorEstado.getColumnaSimbolo(c)][estadoActual];
        }
        //si estoy en el fin del archivo retorno -1
        return estadoActual == FINALARCHIVO ? -1 : tokenActual;
    }

    //incremento la posicion para leer el siguiente caracter del fuente
    public void incPosition() {
        this.getReader().incPosition();
    }


    public int getIDforPalabra(String Token) {
        //vinculado a las variables estaticas publicas de YACC
        //se usa para retornar a YACC los tokens que reconoce
        switch (Token) {
            case "YYERRCODE":
                return Parser.YYERRCODE;
            case "ID":
                return Parser.ID;
            case ":=":
                return Parser.ASIGNACION;
            case "==":
                return Parser.IGUAL;
            case ">=":
                return Parser.MAYORIGUAL;
            case "<=":
                return Parser.MENORIGUAL;
            case "<>":
                return Parser.DISTINTO;
            case "IF":
                return Parser.IF;
            case "THEN":
                return Parser.THEN;
            case "ELSE":
                return Parser.ELSE;
            case "ENDIF":
                return Parser.ENDIF;
            case "PRINT":
                return Parser.PRINT;
            case "FUNC":
                return Parser.FUNC;
            case "RETURN":
                return Parser.RETURN;
            case "BEGIN":
                return Parser.BEGIN;
            case "END":
                return Parser.END;
            case "BREAK":
                return Parser.BREAK;
            case "WHILE":
                return Parser.WHILE;
            case "DO":
                return Parser.DO;
            case "POST":
                return Parser.POST;
            case "INT":
                return Parser.INT;
            case "SINGLE":
                return Parser.SINGLE;
            case "CTE":
                return Parser.CTE;
            case "CADENA":
                return Parser.CADENA;
            case "&&" :
                return Parser.AND;
            case "||":
                return Parser.OR;

        //para simbolos simples devuelvo el ascii
            case "_":
                return (int) '_';
            case ";":
                return (int) ';';
            case "+":
                return (int) '+';
            case "-":
                return (int) '-';
            case "<":
                return (int) '<';
            case ">":
                return (int) '>';
            case ",":
                return (int) ',';
            case "(":
                return (int) '(';
            case ")":
                return (int) ')';
            case "*":
                return (int) '*';
            case "/":
                return (int) '/';
            case ":":
                return (int) ':';
        }
        return -1;//palabra u operador reservado no valido

 
    }

    //setea la ultima entrada a la tabla de simbolos
    public void setEntrada(String elementoTS) {
        entrada = elementoTS;
    }

    //retorna una entrada a la tabla de simbolos
    public EntradaTablaSimbolos getEntrada(String id) {
        return tablaDeSimbolos.get(id);
    }

    //retorna la ultima entrada a la tabla de simbolos
    public String getEntradaTablaSimbolo() {
        return entrada;
    }

    //retotna la tabla de simbolos
    public HashMap<String, EntradaTablaSimbolos> getTablaDeSimbolos() {
        return tablaDeSimbolos;
    }

    public EntradaTablaSimbolos esRedeclarada(String lexema){
        int primerPunto = lexema.indexOf(".");
        String nombreVariable = lexema.substring(0, primerPunto);
        String ambito = lexema.substring(primerPunto);
        int primerPuntoTabla = 0;
        for(HashMap.Entry<String, EntradaTablaSimbolos> entry : this.tablaDeSimbolos.entrySet())
        {
            primerPuntoTabla = entry.getKey().indexOf(".");
            if(primerPuntoTabla >= 0)
            {
                String nombreVariableTabla = entry.getKey().substring(0, primerPuntoTabla);
                String ambitoTabla = entry.getKey().substring(primerPuntoTabla);
                if(nombreVariable.equals(nombreVariableTabla) && ambito.equals(ambitoTabla))
                {
                    return entry.getValue();
                }
            }
        }
        return null;
    }

    public void bajaTablaDeSimbolos(String lexema){
        this.tablaDeSimbolos.remove(lexema);
    }

    public void altaTablaDeSimbolos(String lexema, EntradaTablaSimbolos valor){
        this.tablaDeSimbolos.put(lexema, valor);
    }

    public void cambiarClave(String claveAntigua, String claveNueva){
        EntradaTablaSimbolos valor = this.tablaDeSimbolos.remove(claveAntigua);
        this.tablaDeSimbolos.put(claveNueva, valor);
    }

}