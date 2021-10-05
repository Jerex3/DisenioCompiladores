%{
import java.util.ArrayList;
import java.util.HashMap;

%}

%token IF THEN ELSE ENDIF PRINT FUNC RETURN BEGIN END BREAK WHILE DO POST INT SINGLE ID CADENA OR AND MAYORIGUAL MENORIGUAL IGUAL DISTINTO ASIGNACION CTE
%left '+' '-'
%left '*' '/'
%start programa

%%
    programa:
        contenidoPrograma                       {addReglaSintacticaReconocida(String.format("Programa reconocido en linea %1$d",al.getLinea()));}
    ;

	contenidoPrograma :
		sentenciaDeclarativa BEGIN conjuntoSentenciasEjecutables END
		|
		sentenciaDeclarativa error conjuntoSentenciasEjecutables END          {addErrorSintactico(String.format("Falta BEGIN en linea %1$d",al.getLinea()));}
		|
		sentenciaDeclarativa BEGIN conjuntoSentenciasEjecutables error          {addErrorSintactico(String.format("Falta END en linea %1$d",al.getLinea()));}
	;


	sentenciaDeclarativa :
        declaracionSimple
        |
        declaracionEncabezadoFuncion
        |
        declaracionFuncion
     	|
        sentenciaDeclarativa declaracionSimple
		|
     	sentenciaDeclarativa declaracionEncabezadoFuncion
     	|
     	sentenciaDeclarativa declaracionFuncion
	;


    declaracionSimple :
        tipo listaVariables ';'                     {addReglaSintacticaReconocida(String.format("Declaracion simple reconocida en linea %1$d",al.getLinea()));}
        |
        tipo listaVariables error                   {addErrorSintactico(String.format("Falta un ';' en linea %1$d",al.getLinea()));}
    ;


    tipo :
        INT
        |
        SINGLE
    ;


    listaVariables :
        identificador
        |
        listaVariables ',' identificador
        |
        listaVariables error identificador        {addErrorSintactico(String.format("Falta una ',' en linea %1$d",al.getLinea()));}
    ;


    identificador :
        ID
    ;


    declaracionEncabezadoFuncion :
        tipo FUNC '(' tipo ')' listaVariables ';'       {addReglaSintacticaReconocida(String.format("Declaracion encabezado funcion reconocida en linea %1$d",al.getLinea()));}
        |
        tipo FUNC error tipo ')' listaVariables ';'     {addErrorSintactico(String.format("Falta un '(' en la declaracion del encabezado funcion en linea %1$d",al.getLinea()));}
        |
        tipo FUNC '(' error ')' listaVariables ';'      {addErrorSintactico(String.format("Falta un tipo entre los parentesis en la declaracion del encabezado funcion en linea %1$d",al.getLinea()));}
        |
        tipo FUNC '(' tipo error listaVariables ';'     {addErrorSintactico(String.format("Falta un ')' en la declaracion del encabezado funcion en linea %1$d",al.getLinea()));}
        |
        tipo FUNC '(' tipo ')' error ';'                {addErrorSintactico(String.format("Faltan los nombre de las variables en la declaracion del encabezado funcion en linea %1$d",al.getLinea()));}
        |
        tipo FUNC '(' tipo ')' listaVariables error     {addErrorSintactico(String.format("Falta un ';' en la declaracion del encabezado funcion en linea %1$d",al.getLinea()));}

    ;


    declaracionFuncion :
        encabezadoFuncion sentenciasDeclarativasFuncion BEGIN conjuntoSentenciasEjecutables finFuncion END ';'          {addReglaSintacticaReconocida(String.format("Declaracion de funcion reconocida en linea %1$d",al.getLinea()));}
        |
        encabezadoFuncion sentenciasDeclarativasFuncion error conjuntoSentenciasEjecutables finFuncion END ';'          {addErrorSintactico(String.format("Falta un BEGIN en la declaracion de la funcion en linea %1$d",al.getLinea()));}
        |
        encabezadoFuncion sentenciasDeclarativasFuncion BEGIN conjuntoSentenciasEjecutables finFuncion error ';'        {addErrorSintactico(String.format("Falta un END en la declaracion de la funcion en linea %1$d",al.getLinea()));}
        |
        encabezadoFuncion sentenciasDeclarativasFuncion BEGIN conjuntoSentenciasEjecutables finFuncion END error        {addErrorSintactico(String.format("Falta un ';' en la declaracion de la funcion en linea %1$d",al.getLinea()));}
    ;


    finFuncion :
        retorno ';' postcondicion ';'
        |
        retorno ';'
        |
        retorno error postcondicion ';'         {addErrorSintactico(String.format("Falta un ';' en el fin de la funcion en linea %1$d",al.getLinea()));}
        |
        retorno ';' postcondicion error         {addErrorSintactico(String.format("Falta un ';' en el fin de la funcion en linea %1$d",al.getLinea()));}
        |
        retorno error                           {addErrorSintactico(String.format("Falta un ';' en el fin de la funcion en linea %1$d",al.getLinea()));}
    ;


    encabezadoFuncion :
        tipo FUNC ID '(' parametro ')'
        |
        tipo FUNC ID error parametro ')'        {addErrorSintactico(String.format("Falta un '(' en el encabezado de la funcion en linea %1$d",al.getLinea()));}
        |
        tipo FUNC ID '(' error ')'              {addErrorSintactico(String.format("Falta el parametro en el encabezado de la funcion en linea %1$d",al.getLinea()));}
        |
        tipo FUNC ID '(' parametro error        {addErrorSintactico(String.format("Falta un ')' en el encabezado de la funcion en linea %1$d",al.getLinea()));}
    ;


    parametro :
        tipo ID
    ;


    sentenciasDeclarativasFuncion :
        declaracionSimple
        |
        sentenciasDeclarativasFuncion declaracionSimple
    ;


    retorno :
        RETURN '(' expresionSimple ')'      {addReglaSintacticaReconocida(String.format("Retorno reconocido en linea %1$d",al.getLinea()));}
        |
        RETURN error expresionSimple ')'    {addErrorSintactico(String.format("Falta un '(' en el retorno de la funcion en linea %1$d",al.getLinea()));}
        |
        RETURN '(' error ')'                {addErrorSintactico(String.format("Falta una expresion en el retorno de la funcion en linea %1$d",al.getLinea()));}
        |
        RETURN '(' expresionSimple error    {addErrorSintactico(String.format("Falta un ')' en el retorno de la funcion en linea %1$d",al.getLinea()));}
    ;


    postcondicion :
        POST ':' '(' condicion ')' ',' CADENA           {addReglaSintacticaReconocida(String.format("Postcondicion reconocida en linea %1$d",al.getLinea()));}
        |
        POST error '(' condicion ')' ',' CADENA       {addErrorSintactico(String.format("Falta un ':' en la postcondicion de la funcion en linea %1$d",al.getLinea()));}
        |
        POST ':' error condicion ')' ',' CADENA       {addErrorSintactico(String.format("Falta un '(' en la postcondicion de la funcion en linea %1$d",al.getLinea()));}
        |
        POST ':' '(' error ')' ',' CADENA              {addErrorSintactico(String.format("Falta la condicion en la postcondicion de la funcion en linea %1$d",al.getLinea()));}
        |
        POST ':' '(' condicion error ',' CADENA             {addErrorSintactico(String.format("Falta un ')' en la postcondicion de la funcion en linea %1$d",al.getLinea()));}
        |
        POST ':' '(' error ')' error CADENA                 {addErrorSintactico(String.format("Falta una ',' en la postcondicion de la funcion en linea %1$d",al.getLinea()));}
        |
        POST ':' '(' condicion ')' ',' error            {addErrorSintactico(String.format("Falta la cadena en la postcondicion de la funcion en linea %1$d",al.getLinea()));}
    ;


    conjuntoSentenciasEjecutables :
        sentenciaEjecutable
        |
        conjuntoSentenciasEjecutables sentenciaEjecutable
    ;


    sentenciaEjecutable :
        asignacion
        |
        sentenciaIf
        |
        sentenciaPrint
        |
        sentenciaWhile
    ;


    asignacion :
        identificador ASIGNACION expresionCompleta ';'          {addReglaSintacticaReconocida(String.format("Asignacion reconocida en linea %1$d",al.getLinea()));}
        |
        identificador error expresionCompleta ';'               {addErrorSintactico(String.format("Falta la asignacion en linea %1$d",al.getLinea()));}
        |
        identificador ASIGNACION expresionCompleta error        {addErrorSintactico(String.format("Falta un ';' en linea %1$d",al.getLinea()));}
    ;


    sentenciaIf :
        encabezadoIf cuerpoIf ';'                           {addReglaSintacticaReconocida(String.format("Sentencia if reconocida en linea %1$d",al.getLinea()));}
        |
        encabezadoIf cuerpoIf error                           {addErrorSintactico(String.format("Falta un ';' en linea %1$d",al.getLinea()));}
    ;


    encabezadoIf :
        IF '(' condicion ')'
        |
        IF error condicion ')'                      {addErrorSintactico(String.format("Falta un '(' en el encabezado del if en linea %1$d",al.getLinea()));}
        |
        IF '(' error ')'                            {addErrorSintactico(String.format("Falta la condicion en el encabezado del if en linea %1$d",al.getLinea()));}
        |
        IF '(' condicion error                      {addErrorSintactico(String.format("Falta un ')' en el encabezado del if en linea %1$d",al.getLinea()));}
    ;

    cuerpoIf :
        THEN bloqueSentencias ENDIF
        |
        THEN bloqueSentencias ELSE bloqueSentencias ENDIF
        |
        error bloqueSentencias ENDIF                             {addErrorSintactico(String.format("Falta un THEN en el cuerpo del if en linea %1$d",al.getLinea()));}
        |
        THEN bloqueSentencias error                                 {addErrorSintactico(String.format("Falta un ENDIF en el cuerpo del if en linea %1$d",al.getLinea()));}
        |
        error bloqueSentencias ELSE bloqueSentencias ENDIF           {addErrorSintactico(String.format("Falta un THEN en el cuerpo del if en linea %1$d",al.getLinea()));}
        |
        THEN bloqueSentencias error bloqueSentencias ENDIF               {addErrorSintactico(String.format("Falta un ELSE en el cuerpo del if en linea %1$d",al.getLinea()));}
        |
        THEN bloqueSentencias ELSE bloqueSentencias error               {addErrorSintactico(String.format("Falta un ENDIF en el cuerpo del if en linea %1$d",al.getLinea()));}
    ;


    bloqueSentencias :
        sentenciaEjecutable
        |
        BEGIN conjuntoSentenciasEjecutables END ';'
        |
        BEGIN conjuntoSentenciasEjecutables error ';'                 {addErrorSintactico(String.format("Falta un END en linea %1$d",al.getLinea()));}
        |
        BEGIN conjuntoSentenciasEjecutables END error                 {addErrorSintactico(String.format("Falta un ';' en linea %1$d",al.getLinea()));}
    ;

    sentenciaWhile :
        WHILE '(' condicion ')' DO bloqueSentencias         {addReglaSintacticaReconocida(String.format("Sentencia while reconocida en linea %1$d",al.getLinea()));}
        |
        WHILE error condicion ')' DO bloqueSentencias         {addErrorSintactico(String.format("Falta un '(' en la sentencia while en linea %1$d",al.getLinea()));}
        |
        WHILE '(' error ')' DO bloqueSentencias             {addErrorSintactico(String.format("Falta la condicion en la sentencia while en linea %1$d",al.getLinea()));}
        |
        WHILE '(' condicion error DO bloqueSentencias       {addErrorSintactico(String.format("Falta un ')' en la sentencia while en linea %1$d",al.getLinea()));}
        |
        WHILE '(' condicion ')' error bloqueSentencias     {addErrorSintactico(String.format("Falta un DO en la sentencia while en linea %1$d",al.getLinea()));}
    ;


    sentenciaPrint :
        PRINT '(' CADENA ')' ';'                          {addReglaSintacticaReconocida(String.format("Sentencia print reconocida en linea %1$d",al.getLinea()));}
        |
        PRINT error CADENA ')' ';'                        {addErrorSintactico(String.format("Falta un '(' en la sentencia print en linea %1$d",al.getLinea()));}
        |
        PRINT '(' error ')' ';'                        {addErrorSintactico(String.format("Falta la cadena en la sentencia print en linea %1$d",al.getLinea()));}
        |
        PRINT '(' CADENA error ';'                        {addErrorSintactico(String.format("Falta un ')' en la sentencia print en linea %1$d",al.getLinea()));}
        |
        PRINT '(' CADENA ')' error                        {addErrorSintactico(String.format("Falta un ';' en la sentencia print en linea %1$d",al.getLinea()));}
    ;


    condicion :
        condicion OR subcondicion
        |
        subcondicion
    ;


    subcondicion :
        subcondicion AND comparacion
        |
        comparacion
    ;


    comparacion :
        expresionSimple comparador expresionSimple
    ;


    expresionSimple :
        expresionSimple '+' termino
        |
        expresionSimple '-' termino
        |
        termino
    ;


    termino :
        termino '*' factor
        |
        termino '/' factor
        |
        factor
    ;


    factor :
        identificador
        |
        cte
    ;


    comparador :
        '>'
        |
        '<'
        |
        MAYORIGUAL
        |
        MENORIGUAL
        |
        IGUAL
        |
        DISTINTO
    ;


    expresionCompleta :
        expresionSimple
        |
        invocacionFuncion
        |
        conversionExplicita
    ;


    conversionExplicita :
        SINGLE '(' expresionCompleta ')'            {addReglaSintacticaReconocida(String.format("Conversion explicita reconocida en linea %1$d",al.getLinea()));}
        |
        SINGLE error expresionCompleta ')'           {addErrorSintactico(String.format("Falta un '(' en la conversion en linea %1$d",al.getLinea()));}
        |
        SINGLE '(' error ')'                        {addErrorSintactico(String.format("Falta la expresion en la conversion en linea %1$d",al.getLinea()));}
        |
        SINGLE '(' expresionCompleta error          {addErrorSintactico(String.format("Falta un ')' en la conversion en linea %1$d",al.getLinea()));}
    ;


    invocacionFuncion :
        ID '(' ID ')'                   {addReglaSintacticaReconocida(String.format("Invocacion a funcion reconocida en linea %1$d",al.getLinea()));}
        |
        ID '(' error ')'               {addErrorSintactico(String.format("Falta un ID en la invocacion a funcion en linea %1$d",al.getLinea()));}
        |
        ID '(' ID error               {addErrorSintactico(String.format("Falta un ')' en la invocacion a funcion en linea %1$d",al.getLinea()));}
    ;


    cte :
            CTE											{   EntradaTablaSimbolos entradaTablaSimbolos = (EntradaTablaSimbolos) ($1.obj);
    														if (entradaTablaSimbolos.getTipo().equals(EntradaTablaSimbolos.INT)) {
    															   if ((Integer.parseInt(entradaTablaSimbolos.getLexema())) == AnalizadorLexico.MAX_INT_ABSOLUTO) {
    																			addErrorSintactico(String.format("Warning integer cte positiva mayor al maximo permitido en linea %1$d, se acota al maximo permitido", al.getLinea()));
    																			Integer nuevoLexema = AnalizadorLexico.MAX_INT;
    																			al.getTablaDeSimbolos().remove(entradaTablaSimbolos.getLexema());
    																			entradaTablaSimbolos.setLexema(String.valueOf(nuevoLexema));
    																			al.getTablaDeSimbolos().put(entradaTablaSimbolos.getLexema(), entradaTablaSimbolos);
    																}
    														  }

    													}
            |
            '-'CTE                           			{   EntradaTablaSimbolos entradaTablaSimbolos = (EntradaTablaSimbolos) ($2.obj);
    															if (entradaTablaSimbolos.getTipo().equals(EntradaTablaSimbolos.INT)) {

    																	String nuevoLexema = String.valueOf(Integer.parseInt(entradaTablaSimbolos.getLexema()) * -1);
    																	al.getTablaDeSimbolos().remove(entradaTablaSimbolos.getLexema());
    																	entradaTablaSimbolos.setLexema(nuevoLexema);
    																	al.getTablaDeSimbolos().put(entradaTablaSimbolos.getLexema(), entradaTablaSimbolos);
    															  }
    															  else if (entradaTablaSimbolos.getTipo().equals(EntradaTablaSimbolos.SINGLE)){

    																	String nuevoLexema = String.valueOf(Double.parseDouble(entradaTablaSimbolos.getLexema()) * -1);
    																	al.getTablaDeSimbolos().remove(entradaTablaSimbolos.getLexema());
    																	entradaTablaSimbolos.setLexema(nuevoLexema);
    																	al.getTablaDeSimbolos().put(entradaTablaSimbolos.getLexema(), entradaTablaSimbolos);
    															  }

    														}
    ;

%%

    private AnalizadorLexico al;
    private ArrayList<String> listaDeReglas;
    private ArrayList<String> listaDeErroresSintacticos;

    public Parser(Reader fuente) {
        al = new AnalizadorLexico(fuente);
        listaDeReglas = new ArrayList<>();
        listaDeErroresSintacticos = new ArrayList<>();
    }

    private void yyerror(String syntax_error) {
    }

    private int yylex() {
        int token = al.getToken();
        yylval = new ParserVal(al.getEntradaTablaSimbolo());
        return token;
    }

    public ArrayList<String> getListaDeTokens() {
        return al.getListaDeTokens();
    }

    public ArrayList<String> getListaDeReglas() {
        return listaDeReglas;
    }

    public ArrayList<String> getListaDeErroresLexicos() {
        return al.getListaDeErroresLexicos();
    }

    public ArrayList<String> getListaDeErroresSintacticos() {
        return listaDeErroresSintacticos;
    }

    private void addErrorSintactico(String error) {
        listaDeErroresSintacticos.add(error);
    }

    private void addReglaSintacticaReconocida(String regla) {
        listaDeReglas.add(regla);
    }

    public HashMap<String, EntradaTablaSimbolos> getTablaSimbolos() {
        return al.getTablaDeSimbolos();
    }