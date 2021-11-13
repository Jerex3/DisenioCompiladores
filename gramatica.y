%{
import java.util.ArrayList;
import java.util.HashMap;
import java.util.*;

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
     	|
     	error                   {addErrorSintactico(String.format("ErrorFEO en sentencia declarativa en linea %1$d",al.getLinea()));}
     	|
     	sentenciaDeclarativa error          {addErrorSintactico(String.format("Hay caracteres que sobran en sentencia declarativa en linea %1$d",al.getLinea()));}
	;


    declaracionSimple :
        tipo listaVariables ';'                     {addReglaSintacticaReconocida(String.format("Declaracion simple reconocida en linea %1$d",al.getLinea()));}
        |
        tipo listaVariables error                   {addErrorSintactico(String.format("Falta un ';' en linea %1$d",al.getLinea()));}
    ;


    tipo :
        INT             {this.ultimoTipo = EntradaTablaSimbolos.INT;}
        |
        SINGLE          {this.ultimoTipo = EntradaTablaSimbolos.SINGLE;}
    ;


    listaVariables :
        identificador                             {String lex = renombrarLexema();
                                                    EntradaTablaSimbolos en = al.getEntrada(lex);
                                                    en.setTipo(this.ultimoTipo);
                                                    }
        |
        listaVariables ',' identificador          {String lex = renombrarLexema();
                                                    EntradaTablaSimbolos en = al.getEntrada(lex);
                                                    en.setTipo(this.ultimoTipo);
                                                    }
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
        encabezadoFuncion sentenciaDeclarativa BEGIN conjuntoSentenciasEjecutables finFuncion END ';'          {addReglaSintacticaReconocida(String.format("Declaracion de funcion reconocida en linea %1$d",al.getLinea()));
                                                                                                                finDeFuncion();}
        |
        encabezadoFuncion sentenciaDeclarativa error conjuntoSentenciasEjecutables finFuncion END ';'          {addErrorSintactico(String.format("Falta un BEGIN en la declaracion de la funcion en linea %1$d",al.getLinea()));}
        |
        encabezadoFuncion sentenciaDeclarativa BEGIN conjuntoSentenciasEjecutables finFuncion error ';'        {addErrorSintactico(String.format("Falta un END en la declaracion de la funcion en linea %1$d",al.getLinea()));}
        |
        encabezadoFuncion sentenciaDeclarativa BEGIN conjuntoSentenciasEjecutables finFuncion END error        {addErrorSintactico(String.format("Falta un ';' en la declaracion de la funcion en linea %1$d",al.getLinea()));}
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
        {
           String lexema;
           EntradaTablaSimbolos esRedeclarada = this.al.esRedeclarada($3.sval + this.ambitoActual);
           if(esRedeclarada != null)
           {
               this.addErrorCodigoIntermedio("Funcion " + $3.sval + " redeclarada. La funcion ya fue declarada en la linea: " + esRedeclarada.getLineaDeclaracion() + ".");
               lexema = "#REDECLARADO" + $3.sval + this.ambitoActual;
               this.ultimoAmbito = "." + "#REDECLARADO" + $3.sval;
               this.ultimaFuncion = lexema;
           }
           else
           {
               lexema = $3.sval + this.ambitoActual;
               this.ultimoAmbito = "." + $3.sval;
               this.ultimaFuncion = $3.sval + this.ambitoActual;
           }
           this.al.cambiarClave($3.sval, lexema);
           if(this.al.estaEnTabla(lexema))
           {
               EntradaTablaSimbolos atributos = this.al.getEntrada(lexema);
               atributos.setUso("nombre de procedimiento");
               atributos.setNaContenidos(0);
           }
           this.ambitoActual = this.ambitoActual + this.ultimoAmbito;
        }

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
        identificador ASIGNACION expresionCompleta ';'          { 
								 AtributosTablaDeSimbolos estaEnTablaSimbolos = this.lexico.estaEnTablaSimbolos($1.sval + this.ambitoActual);
                                 if(estaEnTablaSimbolos != null)
								 {
									String lexema = estaEnTablaSimbolos.getLexema();
									estaEnTablaSimbolos.agregarReferencia(this.lexico.getLineaActual());
									TriplaConversion tripla = this.tablaDeConversionDeTiposAsignacion.get(estaEnTablaSimbolos.getTipo() + "-" + this.tipoExpresion);
									if(tripla.getTipo().equals("X"))
									{
										this.comenzarMensaje();
										this.textoPorPantalla.append(": Error por incompatibilidad de tipos. El tipo del lado izquierdo de la asignacion es UINT, mientras que el tipo del lado derecho de la asignacion es DOUBLE.");
										this.addErrorCodigoIntermedio(this.textoPorPantalla.toString());
									}
									else
									{
										this.asignacion = new TercetoConOperando("=", new TercetoTablaDeSimbolos(lexema), this.expresion);
										this.asignacion.setTipo(tripla.getTipo());
										if(!tripla.getConversionSegundoOperando().equals("-"))
										{
											TercetoConOperando expresionPrima = new TercetoConOperando(tripla.getConversionSegundoOperando(), this.expresion);
											expresionPrima.setResultado("@AUXCONV_" + this.contadorVariablesAuxiliaresConversion);
											expresionPrima.setTipo(tripla.getTipo());
											this.agregarAuxiliarConversionTablaDeSimbolos(tripla.getTipo());
											this.contadorVariablesAuxiliaresConversion++;
											this.addTerceto(expresionPrima);
											this.asignacion.setOperador2(expresionPrima);
										}
										this.addTerceto(this.asignacion);
										this.tipoAsignacion = estaEnTablaSimbolos.getTipo();
									}
								 }
								 else
								 { 
									this.comenzarMensaje();
									this.textoPorPantalla.append(": La variable '");
									this.textoPorPantalla.append($1.sval);
									this.textoPorPantalla.append("' en el ámbito actual no fue declarada.'");
									this.addErrorCodigoIntermedio(this.textoPorPantalla.toString());
								 }
								 this.lexico.bajaTablaDeSimbolos($1.sval);
                              }        
        
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
        expresionSimple '+' termino             {this.expresion = crearTercetoOperandos("+", tipoExpresion, this.tipoTermino, this.expresion, this.termino);}
        |
        expresionSimple '-' termino             {this.expresion = crearTercetoOperandos("-", tipoExpresion, this.tipoTermino, this.expresion, this.termino);}
        |
        termino                    {this.expresion = this.termino;
                                    this.tipoExpresion.setLength(0);
                                    this.tipoExpresion.append(this.tipoTermino.toString());
                                   }
        |
        SINGLE ( expresionSimple ) {this.expresion = this.conversionExplicita(this.tipoExpresion, this.expresion) }

    ;


    termino :
        termino '*' factor          {this.termino = crearTercetoOperandos("*", this.tipoTermino, this.tipoFactor, this.termino, this.factor);
                                    }
        |
        termino '/' factor          {this.termino = crearTercetoOperandos("/", this.tipoTermino, this.tipoFactor, this.termino, this.factor);
                                    }
        |
        factor                      {this.termino = this.factor;
                                     this.tipoTermino.setLength(0);
                                     this.tipoTermino.append(this.tipoFactor.toString());
                                    }
    ;


    factor :
        identificador
        {
            EntradaTablaSimbolos entrada = al.estaEnTablaSimbolos($1.sval + ambitoActual);
			if(entrada != null)
	        {
				String lexema = entrada.getLexema();
				//entrada.agregarReferencia(this.al.getLineaActual());
				this.factor = new TercetoLexema(lexema);
				this.tipoFactor.setLength(0);
				this.tipoFactor.append(entrada.getTipo());
		    }
			else
			  {
				this.addErrorCodigoIntermedio("La variable " + $1.sval + " en el ámbito " + ambitoActual.substring(ambitoActual.lastIndexOf(".")) + " no fue declarada.");
			  }
			  this.al.bajaTablaDeSimbolos($1.sval);
        }
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
        |
        CADENA {System.out.println("KKKKKKKKKKKKKKK");}
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
            CTE											{   EntradaTablaSimbolos entradaTablaSimbolos = al.getEntrada($1.sval);
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
            '-'CTE                           			{   EntradaTablaSimbolos entradaTablaSimbolos = al.getEntrada($2.sval);
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
    private ArrayList<String> listaDeReglas = new ArrayList<String>();
    private ArrayList<String> listaDeErroresSintacticos = new ArrayList<String>();
    private ArrayList<String> listaErroresCodigoIntermedio = new ArrayList<String>();

    private Stack<TercetoPosicion> pilaTercetos = new Stack<TercetoPosicion>();

	private HashMap<String, ArrayList<TercetoOperandos>> hashDeTercetos = new HashMap<String, ArrayList<TercetoOperandos>>();
	private HashMap<String, Tripla> tablaDeConversionDeTiposSumaRestaComp = new HashMap<String, Tripla>();
	private HashMap<String, Tripla> tablaDeConversionDeTiposMult = new HashMap<String, Tripla>();
	private HashMap<String, Tripla> tablaDeConversionDeTiposDiv = new HashMap<String, Tripla>();
	private HashMap<String, Tripla> tablaDeConversionDeTiposAsignacion = new HashMap<String, Tripla>();

	private int numeroTercetos = 0;

    private String tipo = "";
    private String ultimoTipo = "";
	private String ultimaFuncion = ".main";
	private String ultimoAmbito = ".main";
	private String ambitoActual = ".main";
	private int cantParametros = 0;
	//private boolean shadowing = false;
	private String formaPasaje =  "copia valor";
	private int cantidadProcedimientosAnidados = -1;
	private String comparador = "";
	private String variableIteraFor = "";
	private int contadorVariablesAuxiliares = 1;
	private int contadorVariablesAuxiliaresConversion = 1;

	private Terceto factor;
	private Terceto termino;
	private Terceto expresion;
	private TercetoOperandos asignacion;
	private Terceto expresionPreComparador;
	private TercetoOperandos condicion;

	private StringBuilder tipoFactor = new StringBuilder();
	private StringBuilder tipoTermino = new StringBuilder();
	private StringBuilder tipoExpresion = new StringBuilder();
	private String tipoAsignacion = "";
	private StringBuilder tipoExpresionPreComparador = new StringBuilder();

    public Parser(Reader fuente) {

        al = new AnalizadorLexico(fuente);
        inicializarTablasDeConversionesDeTipos();

    }

  public void inicializarTablasDeConversionesDeTipos()
  {
    this.tablaDeConversionDeTiposSumaRestaComp.put(EntradaTablaSimbolos.INT + "-" + EntradaTablaSimbolos.INT, new Tripla("-", EntradaTablaSimbolos.INT, "-"));
    this.tablaDeConversionDeTiposSumaRestaComp.put(EntradaTablaSimbolos.INT + "-" + EntradaTablaSimbolos.SINGLE, new Tripla("itos", EntradaTablaSimbolos.SINGLE, "-"));
    this.tablaDeConversionDeTiposSumaRestaComp.put(EntradaTablaSimbolos.SINGLE + "-" + EntradaTablaSimbolos.INT, new Tripla("-", EntradaTablaSimbolos.SINGLE, "itos"));
    this.tablaDeConversionDeTiposSumaRestaComp.put(EntradaTablaSimbolos.SINGLE + "-" + EntradaTablaSimbolos.SINGLE, new Tripla("-", EntradaTablaSimbolos.SINGLE, "-"));

    this.tablaDeConversionDeTiposMult.put(EntradaTablaSimbolos.INT + "-" + EntradaTablaSimbolos.INT, new Tripla("-", EntradaTablaSimbolos.INT, "-"));
    this.tablaDeConversionDeTiposMult.put(EntradaTablaSimbolos.INT + "-" + EntradaTablaSimbolos.SINGLE, new Tripla("itos", EntradaTablaSimbolos.SINGLE, "-"));
    this.tablaDeConversionDeTiposMult.put(EntradaTablaSimbolos.SINGLE + "-" + EntradaTablaSimbolos.INT, new Tripla("-", EntradaTablaSimbolos.SINGLE, "itos"));
    this.tablaDeConversionDeTiposMult.put(EntradaTablaSimbolos.SINGLE + "-" + EntradaTablaSimbolos.SINGLE, new Tripla("-", EntradaTablaSimbolos.SINGLE, "-"));

    this.tablaDeConversionDeTiposDiv.put(EntradaTablaSimbolos.INT + "-" + EntradaTablaSimbolos.INT, new Tripla("-", EntradaTablaSimbolos.SINGLE, "itos"));
    this.tablaDeConversionDeTiposDiv.put(EntradaTablaSimbolos.INT + "-" + EntradaTablaSimbolos.SINGLE, new Tripla("itos", EntradaTablaSimbolos.SINGLE, "-"));
    this.tablaDeConversionDeTiposDiv.put(EntradaTablaSimbolos.SINGLE + "-" + EntradaTablaSimbolos.INT, new Tripla("-", EntradaTablaSimbolos.SINGLE, "itos"));
    this.tablaDeConversionDeTiposDiv.put(EntradaTablaSimbolos.SINGLE + "-" + EntradaTablaSimbolos.SINGLE, new Tripla("-", EntradaTablaSimbolos.SINGLE, "-"));

    this.tablaDeConversionDeTiposAsignacion.put(EntradaTablaSimbolos.INT + "-" + EntradaTablaSimbolos.INT, new Tripla("-", EntradaTablaSimbolos.INT, "-"));
    this.tablaDeConversionDeTiposAsignacion.put(EntradaTablaSimbolos.INT + "-" + EntradaTablaSimbolos.SINGLE, new Tripla("-", "X", "-"));
    this.tablaDeConversionDeTiposAsignacion.put(EntradaTablaSimbolos.SINGLE + "-" + EntradaTablaSimbolos.INT, new Tripla("-", EntradaTablaSimbolos.SINGLE, "itos"));
    this.tablaDeConversionDeTiposAsignacion.put(EntradaTablaSimbolos.SINGLE + "-" + EntradaTablaSimbolos.SINGLE, new Tripla("-", EntradaTablaSimbolos.SINGLE, "-"));
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

    public void addErrorCodigoIntermedio(String error){
        listaErroresCodigoIntermedio.add(error);
    }

    public ArrayList<String> getErroresCodInt(){
        return listaErroresCodigoIntermedio;
    }

    public String renombrarLexema(){
		String lexema;
	    EntradaTablaSimbolos esRedeclarada = this.al.esRedeclarada(this.yylval.sval + this.ambitoActual);
	    if(esRedeclarada != null)
	    {
			this.addErrorCodigoIntermedio("Variable " + this.yyval.sval + " redeclarada. La variable ya fue declarado en la linea: " + esRedeclarada.getLineaDeclaracion() + ".");
		    lexema = "#REDECLARADA" + this.yylval.sval + this.ambitoActual;
	    }
	    else
	    {
			lexema = this.yylval.sval + this.ambitoActual;
	    }
	    this.al.cambiarClave(this.yylval.sval, lexema);
	    if(al.estaEnTabla(lexema))
	    {
			this.al.getEntrada(lexema).setTipo(this.tipo);
		    this.al.getEntrada(lexema).setUso("variable");
	    }
	    return lexema;
    }

	private void finDeFuncion()
	{
		/*
		if(this.al.estaEnTabla(this.ultimaFuncion))
	    {
		  EntradaTablaSimbolos atributos = this.al.getEntrada(this.ultimaFuncion);
		  if(atributos.getNaContenidos() > Integer.parseInt(atributos.getNa()))
		  {
			  this.addErrorCodigoIntermedio("La funcion " + this.ultimaFuncion.substring(0, this.ultimaFuncion.indexOf(".")) + " contiene " + atributos.getNaContenidos() + " niveles de anidamiento, cuando en realidad puede contener: " + atributos.getNa() + " niveles de anidamiento.");
		  }
	    }*/
	    if(this.ultimaFuncion.indexOf(".") == this.ultimaFuncion.lastIndexOf("."))
	    {
			this.ultimaFuncion = ".main";
	    }
	    else
	    {
			this.ultimaFuncion = this.ultimaFuncion.substring(this.ultimaFuncion.lastIndexOf(".") + 1) + this.ultimaFuncion.substring(this.ultimaFuncion.indexOf("."), this.ultimaFuncion.lastIndexOf("."));
	    }
	    this.ambitoActual = this.ambitoActual.substring(0, this.ambitoActual.lastIndexOf("."));
	}

    //expresion_o_termino si es true se trata de una expresión o una condición y si es false se trata de un término. Esto lo hacemos para saber en que HashMap de tabla de conversión buscar.
	private TercetoOperandos crearTercetoOperandos(String operando, StringBuilder tipoPrimerOperando, StringBuilder tipoSegundoOperando, Terceto tercetoPrimerOperando, Terceto tercetoSegundoOperando)
	{
		Tripla tripla;
		if(operando.equals("+") || operando.equals("-"))
		{
			tripla = this.tablaDeConversionDeTiposSumaRestaComp.get(tipoPrimerOperando.toString() + "-" + tipoSegundoOperando.toString());
		}
		else
		{
			if(operando.equals("*"))
			{
				tripla = this.tablaDeConversionDeTiposMult.get(tipoPrimerOperando.toString() + "-" + tipoSegundoOperando.toString());
			}
			else
			{
				tripla = this.tablaDeConversionDeTiposDiv.get(tipoPrimerOperando.toString() + "-" + tipoSegundoOperando.toString());
			}
		}

      TercetoOperandos expresionConOperando = new TercetoOperandos(operando, tercetoPrimerOperando, tercetoSegundoOperando);
      //addTerceto(expresionConOperando);
		///*
		expresionConOperando.setTipo(tripla.getTipo());
		if(tipoPrimerOperando.toString().equals(tipoSegundoOperando.toString()))
		{
			this.addTerceto(expresionConOperando);
			if(!tipoPrimerOperando.toString().equals(tripla.getTipo()))
			{
                TercetoOperandos conversionTercetoAritmetico = new TercetoOperandos("itos", expresionConOperando);
				conversionTercetoAritmetico.setResultado(".AUXCONV_" + this.contadorVariablesAuxiliaresConversion);
				conversionTercetoAritmetico.setTipo(tripla.getTipo());
				this.agregarAuxiliarConversionTablaDeSimbolos(tripla.getTipo());
				this.contadorVariablesAuxiliaresConversion++;
				conversionTercetoAritmetico.setTipo(EntradaTablaSimbolos.SINGLE);
				this.addTerceto(conversionTercetoAritmetico);
				tipoPrimerOperando.setLength(0);
                tipoPrimerOperando.append(tripla.getTipo());
				expresionConOperando = conversionTercetoAritmetico;
			}
			else if(!tripla.getTipo().equals(EntradaTablaSimbolos.INT))
			{
				this.agregarAuxiliarTablaDeSimbolos(tercetoPrimerOperando, tercetoSegundoOperando, tripla.getTipo());
				expresionConOperando.setResultado(".AUX_" + this.contadorVariablesAuxiliares);
				this.contadorVariablesAuxiliares++;
			}
		}
		else
		{
			if(!tripla.getConversionPrimerOperando().equals("-"))
			{
                TercetoOperandos primerOperandoPrima = new TercetoOperandos(tripla.getConversionPrimerOperando(), tercetoPrimerOperando);
				primerOperandoPrima.setResultado(".AUXCONV_" + this.contadorVariablesAuxiliaresConversion);
				primerOperandoPrima.setTipo(tripla.getTipo());
				this.agregarAuxiliarConversionTablaDeSimbolos(tripla.getTipo());
				this.contadorVariablesAuxiliaresConversion++;
				this.addTerceto(primerOperandoPrima);
				expresionConOperando.setOperador1(primerOperandoPrima);
				if(!tripla.getTipo().equals(EntradaTablaSimbolos.INT))
				{
					this.agregarAuxiliarTablaDeSimbolos(primerOperandoPrima, tercetoSegundoOperando, tripla.getTipo());
					expresionConOperando.setResultado(".AUX_" + this.contadorVariablesAuxiliares);
					this.contadorVariablesAuxiliares++;
				}
			}
			if(!tripla.getConversionSegundoOperando().equals("-"))
			{
                TercetoOperandos segundoOperandoPrima = new TercetoOperandos(tripla.getConversionSegundoOperando(), tercetoSegundoOperando);
				segundoOperandoPrima.setResultado(".AUXCONV_" + this.contadorVariablesAuxiliaresConversion);
				segundoOperandoPrima.setTipo(tripla.getTipo());
				this.agregarAuxiliarConversionTablaDeSimbolos(tripla.getTipo());
				this.contadorVariablesAuxiliaresConversion++;
				this.addTerceto(segundoOperandoPrima);
				expresionConOperando.setOperador2(segundoOperandoPrima);
				if(!tripla.getTipo().equals(EntradaTablaSimbolos.SINGLE))
				{
					this.agregarAuxiliarTablaDeSimbolos(tercetoPrimerOperando, segundoOperandoPrima, tripla.getTipo());
					expresionConOperando.setResultado(".AUX_" + this.contadorVariablesAuxiliares);
					this.contadorVariablesAuxiliares++;
				}
			}
			this.addTerceto(expresionConOperando);
			tipoPrimerOperando.setLength(0);
			tipoPrimerOperando.append(tripla.getTipo());
		}
		//*/
		return expresionConOperando;
	}

  private void agregarAuxiliarConversionTablaDeSimbolos(String tipo)
  {
    String lexema = ".AUXCONV_" + this.contadorVariablesAuxiliaresConversion;
    if(al.estaEnTabla(lexema))
    {
      this.al.getEntrada(lexema).agregarReferencia(this.al.getLinea());
    }
    else
    {
      EntradaTablaSimbolos atributos = new EntradaTablaSimbolos(tipo , this.al.getLinea());
      atributos.setUso("auxiliar conversion");
      this.al.altaTablaDeSimbolos(lexema, atributos);
    }
  }

  public void addTerceto(TercetoOperandos terceto)
  {
    this.numeroTercetos++;
    if(this.hashDeTercetos.containsKey(this.ambitoActual))
    {
      terceto.setNumero(this.numeroTercetos);
      this.hashDeTercetos.get(this.ambitoActual).add(terceto);
    }
    else
    {
      ArrayList<TercetoOperandos> lista = new ArrayList<TercetoOperandos>();
      TercetoOperandos etiquetaProcedimiento = new TercetoOperandos(this.ambitoActual + " FUNC");
      etiquetaProcedimiento.setNumero(this.numeroTercetos);
      lista.add(etiquetaProcedimiento);
      this.numeroTercetos++;
      terceto.setNumero(this.numeroTercetos);
      lista.add(terceto);
      this.hashDeTercetos.put(this.ambitoActual, lista);
    }
  }

  private void agregarAuxiliarTablaDeSimbolos(Terceto tercetoPrimerOperando, Terceto tercetoSegundoOperando, String tipo)
  {
    String lexema = "";
    if(tercetoPrimerOperando.getTextoOperando() != null && tercetoSegundoOperando.getTextoOperando() != null)
    {
      lexema = ".AUX_" + this.contadorVariablesAuxiliares;
    }

    if(al.estaEnTabla(lexema))
    {
      this.al.getEntrada(lexema).agregarReferencia(this.al.getLinea());
    }
    else
    {
      EntradaTablaSimbolos atributos = new EntradaTablaSimbolos(tipo , this.al.getLinea());
      atributos.setUso("auxiliar");
      this.al.altaTablaDeSimbolos(lexema, atributos);
    }
  }

  public HashMap<String, ArrayList<TercetoOperandos>> getTercetos(){
    return this.hashDeTercetos;
  }

  private TercetoOperandos conversionExplicita(StringBuilder tipoTercetoAConvertir, Terceto tercetoAConvertir){ // si tipoTercetoAConvertir es SINGLE, podriamos obviar la conversion? o hay que hacerla si o si?
        
        TercetoOperandos conversionTercetoAritmetico = new TercetoOperandos("itos", tercetoAConvertir); // creamos el terceto
        conversionTercetoAritmetico.setResultado(".AUXCONV_" + this.contadorVariablesAuxiliaresConversion); 
        conversionTercetoAritmetico.setTipo(tripla.getTipo());

        this.agregarAuxiliarConversionTablaDeSimbolos(tripla.getTipo()); // Agregamos el auxiliar a la tabla de simbolos. 
        this.contadorVariablesAuxiliaresConversion++;

        conversionTercetoAritmetico.setTipo(EntradaTablaSimbolos.SINGLE); // Seteamos el tipo al nuevo terceto 
        this.addTerceto(conversionTercetoAritmetico); // lo agregamos a la tabla 

        tipoTercetoAConvertir.setLength(0); // esto no estoy seguro, seria para cambiar el tipo de tipoExpresion.
        tipoTercetoAConvertir.append(tripla.getTipo());


        return conversionTercetoAritmetico;
      
  }