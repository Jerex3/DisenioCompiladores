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
        identificador ASIGNACION expresionCompleta ';'          
                                { 
								 EntradaTablaSimbolos estaEnTablaSimbolos = this.al.estaEnTablaSimbolos($1.sval + this.ambitoActual);
                                 if(estaEnTablaSimbolos != null) {  
                                	 
   									 this.asignacion = new TercetoOperandos(":=",new TercetoLexema(estaEnTablaSimbolos.getLexema()), this.expresion);
   									 
                                	 if(!this.tipoExpresion.equals(estaEnTablaSimbolos.getTipo())) {
     									this.addErrorCodigoIntermedio("No es posible realizar la asignacion por incompatibilidad de tipos");
     									this.asignacion.setTipo(EntradaTablaSimbolos.SINGLE);
                                	 } else {
      									this.asignacion.setTipo(estaEnTablaSimbolos.getTipo());
                                	 }
                                	 
                        
								 } else { 									
									this.addErrorCodigoIntermedio(": La variable '" + $3.sval + "' en el ámbito actual no fue declarada.'");
  									 this.asignacion = new TercetoOperandos(":=",new TercetoLexema("VariableNoEncontrada"), this.expresion);

								 }
                                 
                            	 this.addTerceto(asignacion);
                            	 this.tipoAsignacion = asignacion.getTipo();
								 this.al.bajaTablaDeSimbolos($3.sval);
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
                                                    {
                                                    TercetoOperandos ter = new TercetoOperandos("||", this.TercetoCondicion, TercetoSubcondicion );
                                                    this.addTerceto(ter);
                                                    this.TercetoCondicion = ter;
                                                 	   
                                                    }
        |
        subcondicion								{this.TercetoCondicion = this.TercetoSubcondicion;

                                                    }
    ;


    subcondicion :
        subcondicion AND comparacion            {
                                                    TercetoOperandos ter = new TercetoOperandos("&&", TercetoSubcondicion, TercetoComparacion );
                                                    this.addTerceto(ter);
                                                    this.TercetoSubcondicion = ter;
                                                }
        |
        comparacion								{this.TercetoSubcondicion = this.TercetoComparacion;  }
    ;


    comparacion :
        expresionSimple comparador expresionSimple       {
                                                      TercetoOperandos ter = new TercetoOperandos(this.comparador, this.expresionPreComparador, this.expresion );
                                                            if(!this.tipoExpresionPreComparador.equals(this.tipoExpresion)){
                                                                listaErroresCodigoIntermedio.add("No es posible realizar una comparacion entre dos tipos distintos");
                                                                ter.setTipo(EntradaTablaSimbolos.SINGLE);

                                                            } else {
                                                                ter.setTipo(this.tipoExpresion.toString());
                                                            }
                                                            this.addTerceto(ter);
                                                            this.TercetoComparacion = ter;
                                                         }
                                                         		
    ;


    expresionSimple :
        expresionSimple '+' termino             {this.expresion = crearTercetoOperandos("+", tipoExpresion, this.tipoTermino, this.expresion, this.termino);
        										  if(this.listaExpresiones.size() > 0) {
														this.listaExpresiones.remove(this.listaExpresiones.size() - 1);
												  }
												 
        										  this.listaExpresiones.add(this.expresion);
        							
        											}
        |
        expresionSimple '-' termino             {this.expresion = crearTercetoOperandos("-", tipoExpresion, this.tipoTermino, this.expresion, this.termino);
                								 if(this.listaExpresiones.size() > 0) {
													 this.listaExpresiones.remove(this.listaExpresiones.size() - 1);
												  }
											  
                                                 this.listaExpresiones.add(this.expresion);
                                                 
        
        }
        |
        termino                    {
                                    this.expresion = this.termino;
                                    this.tipoExpresion.setLength(0);
                                    this.tipoExpresion.append(this.tipoTermino.toString());
                                    
                                    this.listaExpresiones.add(this.expresion);
                            
                                    
                                    
                                   }
       
        

    ;


    termino :
        termino '*' factor          {this.termino = crearTercetoOperandos("*", this.tipoTermino, this.tipoFactor, this.termino, this.factor);
                              		 if(this.listaTerminos.size() > 0) {
										 this.listaTerminos.remove(this.listaTerminos.size() - 1);
									  }
                                   	
                                   	 this.listaTerminos.add(this.termino);	
                                   	
                                    }
        |
        termino '/' factor          { this.termino = crearTercetoOperandos("/", this.tipoTermino, this.tipoFactor, this.termino, this.factor);
      								  if(this.listaTerminos.size() > 0) {
										 this.listaTerminos.remove(this.listaTerminos.size() - 1);
									  }
                                   	
                                   	  this.listaTerminos.add(this.termino);	
                                    }
        |
        factor                      {this.termino = this.factor;
                                     this.tipoTermino.setLength(0);
                                     this.tipoTermino.append(this.tipoFactor.toString());
                                	 this.listaTerminos.add(this.termino);	
                               
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
        SINGLE '(' expresionSimple ')' {
		       			 this.factor = this.conversionExplicita(this.tipoExpresion, this.expresion);
		        		 this.tipoFactor.setLength(0);
						 this.tipoFactor.append(EntradaTablaSimbolos.SINGLE); 
						  if(this.listaExpresiones.size() > 0) {
							 this.listaExpresiones.remove(this.listaExpresiones.size() - 1);
						 }
						   if(this.listaTerminos.size() > 0) {
							 this.listaTerminos.remove(this.listaTerminos.size() - 1);
						 }
						  if(this.listaExpresiones.size() > 0) {
							 this.expresion = this.listaExpresiones.get(this.listaExpresiones.size() - 1);
						 }
						 if(this.listaTerminos.size() > 0){
 							 this.termino = this.listaTerminos.get(this.listaTerminos.size() -1 );
 							
						 }
							
						 
				}
        |
        cte                     {
                                    this.factor =  new TercetoLexema(this.constante);
                                    this.tipoFactor.setLength(0);
                                    this.tipoFactor.append(al.getEntrada(this.constante).getTipo());
                                }

    ;

comparador : '>'  {
					  this.accionSemanticaComparador();
					  this.comparador = ">";
				  }
           
           | '<' {
					  this.accionSemanticaComparador();
					  this.comparador = "<";
				 }
           
           | IGUAL {
							this.accionSemanticaComparador();
							this.comparador = "==";
						}
           
           | MAYORIGUAL {
								this.accionSemanticaComparador();
								this.comparador = ">=";
							  }
           
           | MENORIGUAL {
								this.accionSemanticaComparador();
								this.comparador = "<=";
							  }
           
           | DISTINTO {
						this.accionSemanticaComparador();
						this.comparador = "!=";
					  }
           
           ; 
    expresionCompleta :
        invocacionFuncion
        |
        expresionSimple
        |        
        CADENA 
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
    														  this.constante = entradaTablaSimbolos.getLexema();

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
    															  this.constante = entradaTablaSimbolos.getLexema();

    														}
    ;

%%

    private AnalizadorLexico al;
    private ArrayList<String> listaDeReglas = new ArrayList<String>();
    private ArrayList<String> listaDeErroresSintacticos = new ArrayList<String>();
    private ArrayList<String> listaErroresCodigoIntermedio = new ArrayList<String>();
	private ArrayList<Terceto> listaExpresiones = new ArrayList<Terceto>();
	private ArrayList<Terceto> listaTerminos = new ArrayList<Terceto>();


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
	private String constante = "";

	private Terceto factor;
	private Terceto termino;
	private Terceto expresion;
	private TercetoOperandos asignacion;
	private Terceto expresionPreComparador;
	private TercetoOperandos condicion;
	private TercetoOperandos TercetoSubcondicion;
	private TercetoOperandos TercetoCondicion;
	private TercetoOperandos TercetoComparacion;
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
		 TercetoOperandos expresionConOperando = new TercetoOperandos(operando, tercetoPrimerOperando, tercetoSegundoOperando);
      //addTerceto(expresionConOperando);
		///*
	
		if(!tipoPrimerOperando.toString().equals(tipoSegundoOperando.toString()))
		{
			this.listaErroresCodigoIntermedio.add("Incompatibilidad de tipos entre " + tercetoPrimerOperando.getTextoOperando() + " y " + tercetoSegundoOperando.getTextoOperando());
			expresionConOperando.setTipo(EntradaTablaSimbolos.SINGLE);

			
		} else {
			
			expresionConOperando.setTipo(tipoPrimerOperando.toString());
			
		}
		
		
		this.addTerceto(expresionConOperando);

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
        conversionTercetoAritmetico.setTipo(EntradaTablaSimbolos.SINGLE);

        this.agregarAuxiliarConversionTablaDeSimbolos(EntradaTablaSimbolos.SINGLE); // Agregamos el auxiliar a la tabla de simbolos. 
        this.contadorVariablesAuxiliaresConversion++;

        conversionTercetoAritmetico.setTipo(EntradaTablaSimbolos.SINGLE); // Seteamos el tipo al nuevo terceto 
        this.addTerceto(conversionTercetoAritmetico); // lo agregamos a la tabla 

        tipoTercetoAConvertir.setLength(0); // esto no estoy seguro, seria para cambiar el tipo de tipoExpresion.
        tipoTercetoAConvertir.append(EntradaTablaSimbolos.SINGLE);


        return conversionTercetoAritmetico;
      
  }

  private void accionSemanticaComparador()
	{
		this.expresionPreComparador = this.expresion;
		this.tipoExpresionPreComparador.setLength(0);
		this.tipoExpresionPreComparador.append(this.tipoExpresion.toString());
	}