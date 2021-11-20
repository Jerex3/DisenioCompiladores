//### This file created by BYACC 1.8(/Java extension  1.15)
//### Java capabilities added 7 Jan 97, Bob Jamison
//### Updated : 27 Nov 97  -- Bob Jamison, Joe Nieten
//###           01 Jan 98  -- Bob Jamison -- fixed generic semantic constructor
//###           01 Jun 99  -- Bob Jamison -- added Runnable support
//###           06 Aug 00  -- Bob Jamison -- made state variables class-global
//###           03 Jan 01  -- Bob Jamison -- improved flags, tracing
//###           16 May 01  -- Bob Jamison -- added custom stack sizing
//###           04 Mar 02  -- Yuval Oren  -- improved java performance, added options
//###           14 Mar 02  -- Tomas Hurka -- -d support, static initializer workaround
//### Please send bug reports to tom@hukatronic.cz
//### static char yysccsid[] = "@(#)yaccpar	1.8 (Berkeley) 01/20/90";






//#line 2 "gramatica2.y"
import java.util.ArrayList;
import java.util.HashMap;
import java.util.*;

//#line 22 "Parser.java"




public class Parser
{

boolean yydebug;        //do I want debug output?
int yynerrs;            //number of errors so far
int yyerrflag;          //was there an error?
int yychar;             //the current working character

//########## MESSAGES ##########
//###############################################################
// method: debug
//###############################################################
void debug(String msg)
{
  if (yydebug)
    System.out.println(msg);
}

//########## STATE STACK ##########
final static int YYSTACKSIZE = 500;  //maximum stack size
int statestk[] = new int[YYSTACKSIZE]; //state stack
int stateptr;
int stateptrmax;                     //highest index of stackptr
int statemax;                        //state when highest index reached
//###############################################################
// methods: state stack push,pop,drop,peek
//###############################################################
final void state_push(int state)
{
  try {
		stateptr++;
		statestk[stateptr]=state;
	 }
	 catch (ArrayIndexOutOfBoundsException e) {
     int oldsize = statestk.length;
     int newsize = oldsize * 2;
     int[] newstack = new int[newsize];
     System.arraycopy(statestk,0,newstack,0,oldsize);
     statestk = newstack;
     statestk[stateptr]=state;
  }
}
final int state_pop()
{
  return statestk[stateptr--];
}
final void state_drop(int cnt)
{
  stateptr -= cnt; 
}
final int state_peek(int relative)
{
  return statestk[stateptr-relative];
}
//###############################################################
// method: init_stacks : allocate and prepare stacks
//###############################################################
final boolean init_stacks()
{
  stateptr = -1;
  val_init();
  return true;
}
//###############################################################
// method: dump_stacks : show n levels of the stacks
//###############################################################
void dump_stacks(int count)
{
int i;
  System.out.println("=index==state====value=     s:"+stateptr+"  v:"+valptr);
  for (i=0;i<count;i++)
    System.out.println(" "+i+"    "+statestk[i]+"      "+valstk[i]);
  System.out.println("======================");
}


//########## SEMANTIC VALUES ##########
//public class ParserVal is defined in ParserVal.java


String   yytext;//user variable to return contextual strings
ParserVal yyval; //used to return semantic vals from action routines
ParserVal yylval;//the 'lval' (result) I got from yylex()
ParserVal valstk[];
int valptr;
//###############################################################
// methods: value stack push,pop,drop,peek.
//###############################################################
void val_init()
{
  valstk=new ParserVal[YYSTACKSIZE];
  yyval=new ParserVal();
  yylval=new ParserVal();
  valptr=-1;
}
void val_push(ParserVal val)
{
  if (valptr>=YYSTACKSIZE)
    return;
  valstk[++valptr]=val;
}
ParserVal val_pop()
{
  if (valptr<0)
    return new ParserVal();
  return valstk[valptr--];
}
void val_drop(int cnt)
{
int ptr;
  ptr=valptr-cnt;
  if (ptr<0)
    return;
  valptr = ptr;
}
ParserVal val_peek(int relative)
{
int ptr;
  ptr=valptr-relative;
  if (ptr<0)
    return new ParserVal();
  return valstk[ptr];
}
final ParserVal dup_yyval(ParserVal val)
{
  ParserVal dup = new ParserVal();
  dup.ival = val.ival;
  dup.dval = val.dval;
  dup.sval = val.sval;
  dup.obj = val.obj;
  return dup;
}
//#### end semantic value section ####
public final static short IF=257;
public final static short THEN=258;
public final static short ELSE=259;
public final static short ENDIF=260;
public final static short PRINT=261;
public final static short FUNC=262;
public final static short RETURN=263;
public final static short BEGIN=264;
public final static short END=265;
public final static short BREAK=266;
public final static short WHILE=267;
public final static short DO=268;
public final static short POST=269;
public final static short INT=270;
public final static short SINGLE=271;
public final static short ID=272;
public final static short CADENA=273;
public final static short OR=274;
public final static short AND=275;
public final static short MAYORIGUAL=276;
public final static short MENORIGUAL=277;
public final static short IGUAL=278;
public final static short DISTINTO=279;
public final static short ASIGNACION=280;
public final static short CTE=281;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    1,    2,    2,    2,    2,    2,    2,    4,    7,
    7,    8,    8,   10,   10,    9,    5,    6,   13,   13,
   12,   11,   16,   14,   15,    3,    3,   19,   19,   19,
   19,   20,   21,   24,   25,   25,   26,   27,   28,   29,
   29,   23,   31,   30,   22,   18,   18,   32,   32,   33,
   17,   17,   17,   35,   35,   35,   36,   36,   36,   36,
   34,   34,   34,   34,   34,   34,   37,   38,   38,
};
final static short yylen[] = {                            2,
    1,    4,    1,    1,    1,    2,    2,    2,    3,    1,
    1,    1,    3,    1,    3,    1,    7,    7,    4,    2,
    6,    1,    2,    4,    7,    1,    2,    1,    1,    1,
    1,    4,    3,    4,    3,    5,    1,    1,    1,    1,
    4,    4,    3,    1,    5,    3,    1,    3,    1,    3,
    3,    3,    1,    3,    3,    1,    1,    4,    1,    1,
    1,    1,    1,    1,    1,    1,    4,    1,    2,
};
final static short yydefred[] = {                         0,
   10,   11,    0,    1,    0,    3,    4,    5,    0,    0,
    0,    0,    6,    7,    8,   16,    0,   12,    0,    0,
    0,    0,   44,    0,    0,   26,   28,   29,   30,   31,
    0,    0,    9,    0,    0,    0,    0,    0,    0,    2,
   27,    0,    0,    0,    0,    0,   13,    0,    0,    0,
    0,    0,   68,    0,   57,    0,    0,    0,   49,    0,
   56,   59,   60,    0,    0,    0,   40,    0,   37,   33,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   69,   64,   65,   63,   66,    0,    0,   61,   62,    0,
    0,   34,    0,    0,    0,    0,   32,    0,   38,   35,
    0,   43,   42,   23,   21,   14,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   48,   54,   55,   45,
    0,    0,   39,   17,    0,    0,   18,    0,    0,   58,
   67,   41,   36,   15,   24,    0,   19,    0,    0,    0,
    0,   25,
};
final static short yydgoto[] = {                          3,
    4,    5,   24,    6,    7,    8,    9,   17,   55,  107,
   10,   11,   77,   78,  129,   74,   56,   57,   26,   27,
   28,   29,   30,   31,   44,   68,  101,  122,   69,   32,
   46,   58,   59,   90,   60,   61,   62,   63,
};
final static short yysindex[] = {                      -152,
    0,    0,    0,    0, -150,    0,    0,    0, -256, -232,
 -152, -155,    0,    0,    0,    0,    7,    0,    5, -141,
   14,   23,    0, -181, -210,    0,    0,    0,    0,    0,
 -168,   54,    0, -256,   67, -152, -155,  -42, -158,    0,
    0,  -42, -172,   68,  -42, -133,    0, -152,   99, -164,
  101,  102,    0, -138,    0,  -16,   -6, -131,    0,   40,
    0,    0,    0,  104,   10, -155,    0, -128,    0,    0,
   -5, -172, -126,  106, -256,  108, -116,   91,  -42, -121,
    0,    0,    0,    0,    0,  -42,  -42,    0,    0,  -42,
  -42,    0,  -42,  -42,  -42,   93,    0, -156,    0,    0,
 -172,    0,    0,    0,    0,    0,   12,  -42,   94, -115,
   16,  114,   40,   40,  -35, -131,    0,    0,    0,    0,
   97, -103,    0,    0, -256,   83,    0,  100,  103,    0,
    0,    0,    0,    0,    0,  119,    0,  -42,   -4,  116,
 -112,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,  -98,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  -41,    0,    0,    0,    0,    0,   -1,    0,  -34,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0, -100,
    0,    0,  -28,  -21,   -8,    1,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,
};
final static short yygindex[] = {                         0,
    0,  155,  -23,   53,   59,   63,   29,    0,   38,    0,
    0,    0,    0,    0,    0,    0,  -30,  -40,   24,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  -49,    0,
    0,   76,   75,    0,   47,   43,    0,    0,
};
final static int YYTABLESIZE=277;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         16,
   16,   16,   54,   16,   71,   16,   53,   86,   53,   87,
   53,   65,   51,   50,   51,   16,   51,   16,   16,   52,
   16,   52,  103,   52,   53,   53,   86,   53,   87,   19,
   51,   51,   50,   51,   92,  102,  140,   52,   52,   47,
   52,   46,   98,   89,   36,   88,   18,   41,  111,   25,
   34,  123,   86,   38,   87,  125,  130,   13,   86,  115,
   87,   25,   39,   14,   49,   33,   67,   15,   97,   42,
  124,   47,   13,   41,   25,   21,   73,  126,   14,   22,
   25,   94,   15,   40,   21,   23,   95,   25,   22,   43,
   16,   66,   21,   45,   23,   67,   22,  139,   76,   16,
   21,   21,   23,   25,   22,   22,   48,   16,  121,   25,
   23,   23,  106,   12,   64,   16,   16,    1,    2,    1,
    2,   41,   37,  135,   67,   86,   70,   87,    1,    2,
   99,  100,  113,  114,   72,   25,  118,  119,   25,   75,
   79,   80,   81,   93,   96,  104,  105,  108,  109,  110,
  112,  120,  127,  128,  131,  132,  133,  136,  138,  141,
  142,  137,  134,   22,   20,   20,  116,  117,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   51,   52,
    0,    0,   16,   16,   16,   16,   16,   16,   53,   53,
   53,   53,   53,   53,   53,   51,   51,   51,   51,   51,
   51,    0,   52,   52,   52,   52,   52,   52,    0,   82,
   83,   84,   85,    0,    0,   50,   50,   91,   91,   91,
    0,    0,   47,    0,   46,    0,   35,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
   42,   43,   45,   45,   45,   47,   41,   43,   43,   45,
   45,   42,   41,   37,   43,  272,   45,   59,   60,   41,
   62,   43,   72,   45,   59,   60,   43,   62,   45,  262,
   59,   60,   41,   62,   41,   41,   41,   59,   60,   41,
   62,   41,   66,   60,   40,   62,    9,   24,   79,   12,
   44,  101,   43,   40,   45,   44,   41,    5,   43,   90,
   45,   24,   40,    5,   36,   59,   43,    5,   59,  280,
   59,   34,   20,   50,   37,  257,   48,  108,   20,  261,
   43,   42,   20,  265,  257,  267,   47,   50,  261,  258,
  272,  264,  257,   40,  267,   72,  261,  138,  263,  272,
  257,  257,  267,   66,  261,  261,   40,  272,  265,   72,
  267,  267,   75,  264,  273,  272,  272,  270,  271,  270,
  271,   98,  264,   41,  101,   43,   59,   45,  270,  271,
  259,  260,   86,   87,  268,   98,   94,   95,  101,   41,
   40,   40,  281,  275,   41,  272,   41,   40,  265,   59,
  272,   59,   59,  269,   41,   59,  260,   58,   40,   44,
  273,   59,  125,  262,  265,   11,   91,   93,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  271,  272,
   -1,   -1,  274,  275,  276,  277,  278,  279,  281,  274,
  275,  276,  277,  278,  279,  274,  275,  276,  277,  278,
  279,   -1,  274,  275,  276,  277,  278,  279,   -1,  276,
  277,  278,  279,   -1,   -1,  274,  275,  274,  274,  274,
   -1,   -1,  274,   -1,  274,   -1,  272,
};
}
final static short YYFINAL=3;
final static short YYMAXTOKEN=281;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,"'('","')'","'*'","'+'","','",
"'-'",null,"'/'",null,null,null,null,null,null,null,null,null,null,"':'","';'",
"'<'",null,"'>'",null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,"IF","THEN","ELSE","ENDIF","PRINT","FUNC",
"RETURN","BEGIN","END","BREAK","WHILE","DO","POST","INT","SINGLE","ID","CADENA",
"OR","AND","MAYORIGUAL","MENORIGUAL","IGUAL","DISTINTO","ASIGNACION","CTE",
};
final static String yyrule[] = {
"$accept : programa",
"programa : contenidoPrograma",
"contenidoPrograma : sentenciaDeclarativa BEGIN conjuntoSentenciasEjecutables END",
"sentenciaDeclarativa : declaracionSimple",
"sentenciaDeclarativa : declaracionEncabezadoFuncion",
"sentenciaDeclarativa : declaracionFuncion",
"sentenciaDeclarativa : sentenciaDeclarativa declaracionSimple",
"sentenciaDeclarativa : sentenciaDeclarativa declaracionEncabezadoFuncion",
"sentenciaDeclarativa : sentenciaDeclarativa declaracionFuncion",
"declaracionSimple : tipo listaVariables ';'",
"tipo : INT",
"tipo : SINGLE",
"listaVariables : identificador",
"listaVariables : listaVariables ',' identificador",
"listaVariablesFuncion : identificador",
"listaVariablesFuncion : listaVariablesFuncion ',' identificador",
"identificador : ID",
"declaracionEncabezadoFuncion : tipoFuncion FUNC '(' tipo ')' listaVariablesFuncion ';'",
"declaracionFuncion : encabezadoFuncion sentenciaDeclarativa BEGIN conjuntoSentenciasEjecutables finFuncion END ';'",
"finFuncion : retorno ';' postcondicion ';'",
"finFuncion : retorno ';'",
"encabezadoFuncion : tipoFuncion FUNC ID '(' parametro ')'",
"tipoFuncion : tipo",
"parametro : tipo ID",
"retorno : RETURN '(' expresionSimple ')'",
"postcondicion : POST ':' '(' condicion ')' ',' CADENA",
"conjuntoSentenciasEjecutables : sentenciaEjecutable",
"conjuntoSentenciasEjecutables : conjuntoSentenciasEjecutables sentenciaEjecutable",
"sentenciaEjecutable : asignacion",
"sentenciaEjecutable : sentenciaIf",
"sentenciaEjecutable : sentenciaPrint",
"sentenciaEjecutable : sentenciaWhile",
"asignacion : identificador ASIGNACION expresionSimple ';'",
"sentenciaIf : encabezadoIf cuerpoIf ';'",
"encabezadoIf : IF '(' condicion ')'",
"cuerpoIf : THEN cuerpoThen ENDIF",
"cuerpoIf : THEN cuerpoThen elseNT cuerpoElse ENDIF",
"cuerpoThen : bloqueSentencias",
"elseNT : ELSE",
"cuerpoElse : bloqueSentencias",
"bloqueSentencias : sentenciaEjecutable",
"bloqueSentencias : BEGIN conjuntoSentenciasEjecutables END ';'",
"sentenciaWhile : whileNT condicionWhile DO bloqueSentencias",
"condicionWhile : '(' condicion ')'",
"whileNT : WHILE",
"sentenciaPrint : PRINT '(' CADENA ')' ';'",
"condicion : condicion OR subcondicion",
"condicion : subcondicion",
"subcondicion : subcondicion AND comparacion",
"subcondicion : comparacion",
"comparacion : expresionSimple comparador expresionSimple",
"expresionSimple : expresionSimple '+' termino",
"expresionSimple : expresionSimple '-' termino",
"expresionSimple : termino",
"termino : termino '*' factor",
"termino : termino '/' factor",
"termino : factor",
"factor : identificador",
"factor : SINGLE '(' expresionSimple ')'",
"factor : invocacionFuncion",
"factor : cte",
"comparador : '>'",
"comparador : '<'",
"comparador : IGUAL",
"comparador : MAYORIGUAL",
"comparador : MENORIGUAL",
"comparador : DISTINTO",
"invocacionFuncion : ID '(' ID ')'",
"cte : CTE",
"cte : '-' CTE",
};

//#line 700 "gramatica2.y"

    private AnalizadorLexico al;
    private ArrayList<String> listaDeReglas = new ArrayList<String>();
    private ArrayList<String> listaDeErroresSintacticos = new ArrayList<String>();
    private ArrayList<String> listaErroresCodigoIntermedio = new ArrayList<String>();
    private ArrayList<String> listaDeWarningsSintacticos = new ArrayList<String>();
	private ArrayList<Terceto> listaExpresiones = new ArrayList<Terceto>();
	private ArrayList<Terceto> listaTerminos = new ArrayList<Terceto>();


    private Stack<TercetoPosicion> pilaTercetos = new Stack<TercetoPosicion>();
    private Stack<TercetoPosicion> pilaTercetoFinFuncion = new Stack<TercetoPosicion>();
	
    private ArrayList<String> listaVariablesFuncion = new ArrayList<String>();
	
	
	private HashMap<String, ArrayList<TercetoOperandos>> hashDeTercetos = new HashMap<String, ArrayList<TercetoOperandos>>();
	private HashMap<String, Tripla> tablaDeConversionDeTiposSumaRestaComp = new HashMap<String, Tripla>();
	private HashMap<String, Tripla> tablaDeConversionDeTiposMult = new HashMap<String, Tripla>();
	private HashMap<String, Tripla> tablaDeConversionDeTiposDiv = new HashMap<String, Tripla>();
	private HashMap<String, Tripla> tablaDeConversionDeTiposAsignacion = new HashMap<String, Tripla>();

	private int numeroTercetos = 0;

    private String tipo = "";
    private String ultimoTipo = "";
    private String ultimoTipoFuncion;
	private String ultimaFuncion = ".main";
	private String ultimoAmbito = ".main";
	private String ambitoActual = ".main";
	
	private String tipoParametro;
	private String lexemaParametro;

	private boolean hayFunc = false;
	private String lexemaVarFunc ="";
		
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

    public ArrayList<String> getListaDeWarningsLexicos() {
        return al.getListaDeWarningsLexicos();
    }

    public ArrayList<String> getListaDeWarningsSintacticos() {
        return listaDeWarningsSintacticos;
    }

    private void addWarningSintactico(String warning) {
        listaDeWarningsSintacticos.add(warning);
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
 	public String renombrarLexemaParametro(String lex){
		String lexema;
	    EntradaTablaSimbolos esRedeclarada = this.al.esRedeclarada(lex + this.ambitoActual);
	    if(esRedeclarada != null)
	    {
			this.addErrorCodigoIntermedio("Variable " + lex + " redeclarada. La variable ya fue declarado en la linea: " + esRedeclarada.getLineaDeclaracion() + ".");
		    lexema = "#REDECLARADA" + lex + this.ambitoActual;
	    }
	    else
	    {
			lexema = lex + this.ambitoActual;
	    }
	    this.al.cambiarClave(lex, lexema);
	    if(al.estaEnTabla(lexema))
	    {
		    this.al.getEntrada(lexema).setUso("funcion");
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
//#line 684 "Parser.java"
//###############################################################
// method: yylexdebug : check lexer state
//###############################################################
void yylexdebug(int state,int ch)
{
String s=null;
  if (ch < 0) ch=0;
  if (ch <= YYMAXTOKEN) //check index bounds
     s = yyname[ch];    //now get it
  if (s==null)
    s = "illegal-symbol";
  debug("state "+state+", reading "+ch+" ("+s+")");
}





//The following are now global, to aid in error reporting
int yyn;       //next next thing to do
int yym;       //
int yystate;   //current parsing state from state table
String yys;    //current token string


//###############################################################
// method: yyparse : parse input and execute indicated items
//###############################################################
int yyparse()
{
boolean doaction;
  init_stacks();
  yynerrs = 0;
  yyerrflag = 0;
  yychar = -1;          //impossible char forces a read
  yystate=0;            //initial state
  state_push(yystate);  //save it
  val_push(yylval);     //save empty value
  while (true) //until parsing is done, either correctly, or w/error
    {
    doaction=true;
    if (yydebug) debug("loop"); 
    //#### NEXT ACTION (from reduction table)
    for (yyn=yydefred[yystate];yyn==0;yyn=yydefred[yystate])
      {
      if (yydebug) debug("yyn:"+yyn+"  state:"+yystate+"  yychar:"+yychar);
      if (yychar < 0)      //we want a char?
        {
        yychar = yylex();  //get next token
        if (yydebug) debug(" next yychar:"+yychar);
        //#### ERROR CHECK ####
        if (yychar < 0)    //it it didn't work/error
          {
          yychar = 0;      //change it to default string (no -1!)
          if (yydebug)
            yylexdebug(yystate,yychar);
          }
        }//yychar<0
      yyn = yysindex[yystate];  //get amount to shift by (shift index)
      if ((yyn != 0) && (yyn += yychar) >= 0 &&
          yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
        {
        if (yydebug)
          debug("state "+yystate+", shifting to state "+yytable[yyn]);
        //#### NEXT STATE ####
        yystate = yytable[yyn];//we are in a new state
        state_push(yystate);   //save it
        val_push(yylval);      //push our lval as the input for next rule
        yychar = -1;           //since we have 'eaten' a token, say we need another
        if (yyerrflag > 0)     //have we recovered an error?
           --yyerrflag;        //give ourselves credit
        doaction=false;        //but don't process yet
        break;   //quit the yyn=0 loop
        }

    yyn = yyrindex[yystate];  //reduce
    if ((yyn !=0 ) && (yyn += yychar) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
      {   //we reduced!
      if (yydebug) debug("reduce");
      yyn = yytable[yyn];
      doaction=true; //get ready to execute
      break;         //drop down to actions
      }
    else //ERROR RECOVERY
      {
      if (yyerrflag==0)
        {
        yyerror("syntax error");
        yynerrs++;
        }
      if (yyerrflag < 3) //low error count?
        {
        yyerrflag = 3;
        while (true)   //do until break
          {
          if (stateptr<0)   //check for under & overflow here
            {
            yyerror("stack underflow. aborting...");  //note lower case 's'
            return 1;
            }
          yyn = yysindex[state_peek(0)];
          if ((yyn != 0) && (yyn += YYERRCODE) >= 0 &&
                    yyn <= YYTABLESIZE && yycheck[yyn] == YYERRCODE)
            {
            if (yydebug)
              debug("state "+state_peek(0)+", error recovery shifting to state "+yytable[yyn]+" ");
            yystate = yytable[yyn];
            state_push(yystate);
            val_push(yylval);
            doaction=false;
            break;
            }
          else
            {
            if (yydebug)
              debug("error recovery discarding state "+state_peek(0)+" ");
            if (stateptr<0)   //check for under & overflow here
              {
              yyerror("Stack underflow. aborting...");  //capital 'S'
              return 1;
              }
            state_pop();
            val_pop();
            }
          }
        }
      else            //discard this token
        {
        if (yychar == 0)
          return 1; //yyabort
        if (yydebug)
          {
          yys = null;
          if (yychar <= YYMAXTOKEN) yys = yyname[yychar];
          if (yys == null) yys = "illegal-symbol";
          debug("state "+yystate+", error recovery discards token "+yychar+" ("+yys+")");
          }
        yychar = -1;  //read another
        }
      }//end error recovery
    }//yyn=0 loop
    if (!doaction)   //any reason not to proceed?
      continue;      //skip action
    yym = yylen[yyn];          //get count of terminals on rhs
    if (yydebug)
      debug("state "+yystate+", reducing "+yym+" by rule "+yyn+" ("+yyrule[yyn]+")");
    if (yym>0)                 //if count of rhs not 'nil'
      yyval = val_peek(yym-1); //get current semantic value
    yyval = dup_yyval(yyval); //duplicate yyval if ParserVal is used as semantic value
    switch(yyn)
      {
//########## USER-SUPPLIED ACTIONS ##########
case 1:
//#line 15 "gramatica2.y"
{addReglaSintacticaReconocida(String.format("Programa reconocido en linea %1$d",al.getLinea()));}
break;
case 9:
//#line 41 "gramatica2.y"
{addReglaSintacticaReconocida(String.format("Declaracion simple reconocida en linea %1$d",al.getLinea()));}
break;
case 10:
//#line 47 "gramatica2.y"
{this.ultimoTipo = EntradaTablaSimbolos.INT;}
break;
case 11:
//#line 49 "gramatica2.y"
{this.ultimoTipo = EntradaTablaSimbolos.SINGLE;}
break;
case 12:
//#line 54 "gramatica2.y"
{ String lex = renombrarLexema();
                                                    EntradaTablaSimbolos en = al.getEntrada(lex);
                                                    en.setTipo(this.ultimoTipo);
                                                    }
break;
case 13:
//#line 59 "gramatica2.y"
{String lex = renombrarLexema();
                                                    EntradaTablaSimbolos en = al.getEntrada(lex);
                                                    en.setTipo(this.ultimoTipo);
                                                    }
break;
case 14:
//#line 67 "gramatica2.y"
{ 
        												this.listaVariablesFuncion = new ArrayList<String>();
        												this.listaVariablesFuncion.add(val_peek(0).sval);
                                                    }
break;
case 15:
//#line 72 "gramatica2.y"
{
        													this.listaVariablesFuncion.add(val_peek(0).sval);
                                                    		
                                                    	 }
break;
case 17:
//#line 86 "gramatica2.y"
{
        																addReglaSintacticaReconocida(String.format("Declaracion encabezado funcion reconocida en linea %1$d",al.getLinea()));
        																	for(String lex : this.listaVariablesFuncion){
        																		EntradaTablaSimbolos ent = al.getEntrada(lex);
        																		ent.setTipo(this.ultimoTipoFuncion);
        																		ent.setTipoParametro(this.ultimoTipo);
        																		String lexNuevo = renombrarLexemaParametro(lex);
        																		ent.setUso("funcion");
        																	}
        															  }
break;
case 18:
//#line 101 "gramatica2.y"
{addReglaSintacticaReconocida(String.format("Declaracion de funcion reconocida en linea %1$d",al.getLinea()));
                                                                                                                finDeFuncion();}
break;
case 19:
//#line 108 "gramatica2.y"
{
        											TercetoOperandos tercetoBI = (TercetoOperandos) this.pilaTercetoFinFuncion.pop();
        											TercetoOperandos etiqueta = new TercetoOperandos("Label_" + (this.numeroTercetos + 1));
        											TercetoOperandos finFuncion = new TercetoOperandos("finfuncion");
        											tercetoBI.setOperador1(etiqueta);
        											this.addTerceto(etiqueta);       										
        											this.addTerceto(finFuncion);
        										}
break;
case 20:
//#line 117 "gramatica2.y"
{
       												TercetoOperandos finFuncion = new TercetoOperandos("finfuncion");
        											this.addTerceto(finFuncion);
        										}
break;
case 21:
//#line 127 "gramatica2.y"
{
           String lexema;
           EntradaTablaSimbolos esRedeclarada = this.al.esRedeclarada(val_peek(3).sval + this.ambitoActual);
           if(esRedeclarada != null)
           {
               this.addErrorCodigoIntermedio("Funcion " + val_peek(3).sval + " redeclarada. La funcion ya fue declarada en la linea: " + esRedeclarada.getLineaDeclaracion() + ".");
               lexema = "#REDECLARADO" + val_peek(3).sval + this.ambitoActual;
               this.ultimoAmbito = "." + "#REDECLARADO" + val_peek(3).sval;
               this.ultimaFuncion = lexema;
           }
           else
           {
               lexema = val_peek(3).sval + this.ambitoActual;
               this.ultimoAmbito = "." + val_peek(3).sval;
               this.ultimaFuncion = val_peek(3).sval + this.ambitoActual;
           }
           this.al.cambiarClave(val_peek(3).sval, lexema);
           if(this.al.estaEnTabla(lexema))
           {
               EntradaTablaSimbolos atributos = this.al.getEntrada(lexema);
               atributos.setUso("funcion");
               atributos.setTipo(this.ultimoTipoFuncion);
               atributos.setTipoParametro(this.tipoParametro);
               atributos.setLlamadoLexema(atributos.getLexema());
           }
           this.ambitoActual = this.ambitoActual + this.ultimoAmbito;
           EntradaTablaSimbolos param = al.getEntrada(this.lexemaParametro);
           param.setLexema(this.lexemaParametro + this.ambitoActual);
           param.setTipo(this.tipoParametro);
           al.cambiarClave(this.lexemaParametro, this.lexemaParametro + this.ambitoActual);
           this.al.getEntrada(lexema).setParametro(param.getLexema());
           
        }
break;
case 22:
//#line 164 "gramatica2.y"
{this.ultimoTipoFuncion = this.ultimoTipo;}
break;
case 23:
//#line 167 "gramatica2.y"
{
        			this.tipoParametro = this.ultimoTipo;
        			this.lexemaParametro = val_peek(0).sval;
        		}
break;
case 24:
//#line 174 "gramatica2.y"
{
									        	addReglaSintacticaReconocida(String.format("Retorno reconocido en linea %1$d",al.getLinea()));
									        	String lexemaFuncionActual = this.ambitoActual.substring(this.ambitoActual.lastIndexOf(".") + 1, this.ambitoActual.length()) +  this.ambitoActual.substring(0, this.ambitoActual.lastIndexOf("."));
									        	String tipoFuncionActual = al.getEntrada(lexemaFuncionActual).getTipo();
									        	if(!tipoFuncionActual.equals(this.tipoExpresion)){
									        		this.addErrorSintactico("El tipo de la funcion" + lexemaFuncionActual + " es " + tipoFuncionActual + " y se intenta retornar " + this.tipoExpresion);
									        	}
									        	TercetoOperandos tercetoRetorno = new TercetoOperandos("retorno", this.expresion);
									        	this.addTerceto(tercetoRetorno);
									        }
break;
case 25:
//#line 189 "gramatica2.y"
{
    														addReglaSintacticaReconocida(String.format("Postcondicion reconocida en linea %1$d",al.getLinea()));
    														TercetoOperandos print = new TercetoOperandos("print", new TercetoLexema(val_peek(0).sval));
    														TercetoOperandos fin = new TercetoOperandos("fin");
    														
    														TercetoOperandos tercetoBF = new TercetoOperandos("BF", this.TercetoCondicion);
    													    this.addTerceto(tercetoBF);
    													    
    													    TercetoOperandos tercetoBI = new TercetoOperandos("BI");
    													   	this.pilaTercetoFinFuncion.push(tercetoBI);
    													   	
    														this.addTerceto(tercetoBI);
    													
     														TercetoOperandos etiqueta = new TercetoOperandos("Label_" + ( this.numeroTercetos + 1));
                                                            this.addTerceto(etiqueta);		
                                                            
                                                            tercetoBF.setOperador2(etiqueta);		
                                                            							
    														this.addTerceto(print);
    														this.addTerceto(fin);
    													}
break;
case 32:
//#line 233 "gramatica2.y"
{
                                if(hayFunc){
                                    hayFunc = false;
                                    EntradaTablaSimbolos entFun = al.getEntrada(lexemaVarFunc);

                                     EntradaTablaSimbolos estaEnTablaSimbolos = this.al.estaEnTablaSimbolos(val_peek(3).sval + this.ambitoActual);
                                     if(estaEnTablaSimbolos != null) {

                                         this.asignacion = new TercetoOperandos(":=",new TercetoLexema(estaEnTablaSimbolos.getLexema()), this.expresion);

                                         if(entFun.getUso().equals(estaEnTablaSimbolos.getUso()) && entFun.getTipoParametro().equals(estaEnTablaSimbolos.getTipoParametro()) && entFun.getTipo().equals(estaEnTablaSimbolos.getTipo())) {
                                            this.asignacion.setTipo(estaEnTablaSimbolos.getTipo());
                                            estaEnTablaSimbolos.setLlamadoLexema(entFun.getLexema());
                                            estaEnTablaSimbolos.setParametro(entFun.getParametro());
                                         } else {

                                            this.addErrorCodigoIntermedio("No es posible realizar la asignacion por incompatibilidad de tipos");
                                            this.asignacion.setTipo(EntradaTablaSimbolos.SINGLE);
                                         }


                                     } else {
                                        this.addErrorCodigoIntermedio(": La variable '" + val_peek(3).sval + "' en el ámbito actual no fue declarada.'");
                                         this.asignacion = new TercetoOperandos(":=",new TercetoLexema("VariableNoEncontrada"), this.expresion);

                                     }

                                     this.addTerceto(asignacion);
                                     this.tipoAsignacion = asignacion.getTipo();
                                     this.al.bajaTablaDeSimbolos(val_peek(3).sval);

                                }

                                else{
                                     EntradaTablaSimbolos estaEnTablaSimbolos = this.al.estaEnTablaSimbolos(val_peek(3).sval + this.ambitoActual);
                                     if(estaEnTablaSimbolos != null) {

                                         this.asignacion = new TercetoOperandos(":=",new TercetoLexema(estaEnTablaSimbolos.getLexema()), this.expresion);

                                         if(!this.tipoExpresion.equals(estaEnTablaSimbolos.getTipo())) {
                                            this.addErrorCodigoIntermedio("No es posible realizar la asignacion por incompatibilidad de tipos");
                                            this.asignacion.setTipo(EntradaTablaSimbolos.SINGLE);
                                         } else {
                                            this.asignacion.setTipo(estaEnTablaSimbolos.getTipo());
                                         }


                                     } else {
                                        this.addErrorCodigoIntermedio(": La variable '" + val_peek(3).sval + "' en el ámbito actual no fue declarada.'");
                                         this.asignacion = new TercetoOperandos(":=",new TercetoLexema("VariableNoEncontrada"), this.expresion);

                                     }

                                     this.addTerceto(asignacion);
                                     this.tipoAsignacion = asignacion.getTipo();
                                     this.al.bajaTablaDeSimbolos(val_peek(3).sval);
                                 }

                                  }
break;
case 33:
//#line 297 "gramatica2.y"
{
                                                                addReglaSintacticaReconocida(String.format("Sentencia if reconocida en linea %1$d",al.getLinea()));                                                                                                      
                                                                TercetoOperandos tercetoBI = (TercetoOperandos) this.pilaTercetos.pop();
                                                                int numeroDestino = this.numeroTercetos + 1;
                                                                tercetoBI.setOperador1(new TercetoPosicion(numeroDestino));
                                                                TercetoOperandos etiqueta = new TercetoOperandos("Label_" + numeroDestino);
                                                                this.addTerceto(etiqueta);
													  
                                                            }
break;
case 34:
//#line 311 "gramatica2.y"
{	
                                                        TercetoOperandos tercetoBF = new TercetoOperandos("BF", this.TercetoCondicion);
								                    	this.addTerceto(tercetoBF);
                                                        this.pilaTercetos.push(tercetoBF);
                                                    }
break;
case 37:
//#line 327 "gramatica2.y"
{
										   TercetoOperandos tercetoBF = (TercetoOperandos) this.pilaTercetos.pop();
										   int numeroDestino = this.numeroTercetos + 2;
										   tercetoBF.setOperador2(new TercetoPosicion(numeroDestino));
										   TercetoOperandos tercetoBI = new TercetoOperandos("BI");
										   this.addTerceto(tercetoBI);
										   this.pilaTercetos.push(tercetoBI);
									   }
break;
case 38:
//#line 338 "gramatica2.y"
{
                    int numeroDestino = this.numeroTercetos + 1;
					TercetoOperandos etiqueta = new TercetoOperandos("Label_" + numeroDestino);
					this.addTerceto(etiqueta);
                 }
break;
case 39:
//#line 346 "gramatica2.y"
{System.out.println("HOLA");}
break;
case 42:
//#line 356 "gramatica2.y"
{
                                                                addReglaSintacticaReconocida(String.format("Sentencia while reconocida en linea %1$d",al.getLinea()));
                                                                TercetoOperandos tercetoBF = (TercetoOperandos) this.pilaTercetos.pop();   
                                                                
                                                                
                                                                /**/
                                                                
                                                                /**/
                                                                
                                                                int numeroDestino = this.numeroTercetos + 2;                                	                          
                                                                tercetoBF.setOperador2(new TercetoPosicion(numeroDestino));
																
                                                                TercetoPosicion posicionBI = (TercetoPosicion) this.pilaTercetos.pop();

                                                                TercetoOperandos TercetoBI = new TercetoOperandos("BI", posicionBI);
                                                                this.addTerceto(TercetoBI);
                                                                TercetoOperandos etiqueta = new TercetoOperandos("Label_" + numeroDestino);
                                                                this.addTerceto(etiqueta);

                                                            }
break;
case 43:
//#line 380 "gramatica2.y"
{
                            TercetoOperandos tercetoBF = new TercetoOperandos("BF", this.TercetoCondicion);
                            this.addTerceto(tercetoBF);
                            this.pilaTercetos.push(tercetoBF);
                         }
break;
case 44:
//#line 389 "gramatica2.y"
{
    					int incremento = 1;
    					if(!this.hashDeTercetos.containsKey(this.ambitoActual)){
    						incremento = 2;
    					} 
    					int numeroDestino = this.numeroTercetos + incremento;
						TercetoOperandos etiqueta = new TercetoOperandos("Label_" + numeroDestino);
						this.addTerceto(etiqueta);
                        TercetoPosicion posicion = new TercetoPosicion(numeroDestino);
                        this.pilaTercetos.push(posicion);
                    }
break;
case 45:
//#line 402 "gramatica2.y"
{
													        addReglaSintacticaReconocida(String.format("Sentencia print reconocida en linea %1$d",al.getLinea()));
													        TercetoOperandos terPrint = new TercetoOperandos("print", new TercetoLexema(val_peek(2).sval));
													        this.addTerceto(terPrint);
													       }
break;
case 46:
//#line 413 "gramatica2.y"
{
                                                    TercetoOperandos ter = new TercetoOperandos("||", this.TercetoCondicion, TercetoSubcondicion );
                                                    this.addTerceto(ter);
                                                    this.TercetoCondicion = ter;
                                                 	   
                                                    }
break;
case 47:
//#line 420 "gramatica2.y"
{this.TercetoCondicion = this.TercetoSubcondicion;

                                                    }
break;
case 48:
//#line 427 "gramatica2.y"
{
                                                    TercetoOperandos ter = new TercetoOperandos("&&", TercetoSubcondicion, TercetoComparacion );
                                                    this.addTerceto(ter);
                                                    this.TercetoSubcondicion = ter;
                                                }
break;
case 49:
//#line 433 "gramatica2.y"
{this.TercetoSubcondicion = this.TercetoComparacion;  }
break;
case 50:
//#line 438 "gramatica2.y"
{
                                                      TercetoOperandos ter = new TercetoOperandos(this.comparador, this.expresionPreComparador, this.expresion );
                                                            if(!this.tipoExpresionPreComparador.equals(this.tipoExpresion)){
                                                                listaErroresCodigoIntermedio.add("No es posible realizar una comparacion entre dos tipos distintos");
                                                                ter.setTipo(EntradaTablaSimbolos.SINGLE);

                                                            } else {
                                                                ter.setTipo(this.tipoExpresion.toString());
                                                            }
                                                            this.addTerceto(ter);
                                                            this.TercetoComparacion = ter;
                                                             if(hayFunc){
                                                                this.addErrorCodigoIntermedio("Se esta usando el identificador de una funcion en una expresion.");
                                                                hayFunc = false;
                                                             }
                                                         }
break;
case 51:
//#line 459 "gramatica2.y"
{this.expresion = crearTercetoOperandos("+", tipoExpresion, this.tipoTermino, this.expresion, this.termino);
        										  if(this.listaExpresiones.size() > 0) {
														this.listaExpresiones.remove(this.listaExpresiones.size() - 1);
												  }
												 
        										  this.listaExpresiones.add(this.expresion);
                                                 if(hayFunc){
                                                    this.addErrorCodigoIntermedio("Se esta usando el identificador de una funcion en una expresion.");
                                                    hayFunc = false;
                                                 }
        											}
break;
case 52:
//#line 471 "gramatica2.y"
{this.expresion = crearTercetoOperandos("-", tipoExpresion, this.tipoTermino, this.expresion, this.termino);
                								 if(this.listaExpresiones.size() > 0) {
													 this.listaExpresiones.remove(this.listaExpresiones.size() - 1);
												  }
											  
                                                 this.listaExpresiones.add(this.expresion);
                                                 
                                                 if(hayFunc){
                                                    this.addErrorCodigoIntermedio("Se esta usando el identificador de una funcion en una expresion.");
                                                    hayFunc = false;
                                                 }
        }
break;
case 53:
//#line 484 "gramatica2.y"
{
                                    this.expresion = this.termino;
                                    this.tipoExpresion.setLength(0);
                                    this.tipoExpresion.append(this.tipoTermino.toString());
                                    
                                    this.listaExpresiones.add(this.expresion);
                            
                                    
                                    
                                   }
break;
case 54:
//#line 501 "gramatica2.y"
{this.termino = crearTercetoOperandos("*", this.tipoTermino, this.tipoFactor, this.termino, this.factor);
                              		 if(this.listaTerminos.size() > 0) {
										 this.listaTerminos.remove(this.listaTerminos.size() - 1);
									  }
                                   	
                                   	 this.listaTerminos.add(this.termino);

                                   	 if(hayFunc){
                                   	    this.addErrorCodigoIntermedio("Se esta usando el identificador de una funcion en una expresion.");
                                   	    hayFunc = false;
                                   	 }
                                   	
                                    }
break;
case 55:
//#line 515 "gramatica2.y"
{ this.termino = crearTercetoOperandos("/", this.tipoTermino, this.tipoFactor, this.termino, this.factor);
      								  if(this.listaTerminos.size() > 0) {
										 this.listaTerminos.remove(this.listaTerminos.size() - 1);
									  }
                                   	
                                   	  this.listaTerminos.add(this.termino);

                                     if(hayFunc){
                                        this.addErrorCodigoIntermedio("Se esta usando el identificador de una funcion en una expresion.");
                                        hayFunc = false;
                                     }

                                    }
break;
case 56:
//#line 529 "gramatica2.y"
{this.termino = this.factor;
                                     this.tipoTermino.setLength(0);
                                     this.tipoTermino.append(this.tipoFactor.toString());
                                	 this.listaTerminos.add(this.termino);	
                               
                                    }
break;
case 57:
//#line 540 "gramatica2.y"
{
            EntradaTablaSimbolos entrada = al.estaEnTablaSimbolos(val_peek(0).sval + ambitoActual);
			if(entrada != null)
	        {
				String lexema = entrada.getLexema();
				/*entrada.agregarReferencia(this.al.getLineaActual());*/
				this.factor = new TercetoLexema(lexema);
				this.tipoFactor.setLength(0);
				this.tipoFactor.append(entrada.getTipo());

				if(entrada.getUso().equals("funcion")) {hayFunc = true; lexemaVarFunc = lexema;}
		    }
			else
			  {
				this.addErrorCodigoIntermedio("La variable " + val_peek(0).sval + " en el ámbito " + ambitoActual.substring(ambitoActual.lastIndexOf(".")) + " no fue declarada.");
			  }
			  this.al.bajaTablaDeSimbolos(val_peek(0).sval);
        }
break;
case 58:
//#line 559 "gramatica2.y"
{
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
break;
case 59:
//#line 580 "gramatica2.y"
{}
break;
case 60:
//#line 582 "gramatica2.y"
{
                                    this.factor =  new TercetoLexema(this.constante);
                                    this.tipoFactor.setLength(0);
                                    this.tipoFactor.append(al.getEntrada(this.constante).getTipo());
                                }
break;
case 61:
//#line 590 "gramatica2.y"
{
					  this.accionSemanticaComparador();
					  this.comparador = ">";
				  }
break;
case 62:
//#line 595 "gramatica2.y"
{
					  this.accionSemanticaComparador();
					  this.comparador = "<";
				 }
break;
case 63:
//#line 600 "gramatica2.y"
{
							this.accionSemanticaComparador();
							this.comparador = "==";
						}
break;
case 64:
//#line 605 "gramatica2.y"
{
								this.accionSemanticaComparador();
								this.comparador = ">=";
							  }
break;
case 65:
//#line 610 "gramatica2.y"
{
								this.accionSemanticaComparador();
								this.comparador = "<=";
							  }
break;
case 66:
//#line 615 "gramatica2.y"
{
						this.accionSemanticaComparador();
						this.comparador = "!=";
					  }
break;
case 67:
//#line 628 "gramatica2.y"
{
        								    addReglaSintacticaReconocida(String.format("Invocacion a funcion reconocida en linea %1$d",al.getLinea()));
                                            EntradaTablaSimbolos entradaFuncion = al.estaEnTablaSimbolos(val_peek(3).sval + this.ambitoActual);
                                            EntradaTablaSimbolos entradaParametro = al.estaEnTablaSimbolos(val_peek(1).sval + this.ambitoActual);
											EntradaTablaSimbolos entradaParametroFormal = al.getEntrada(entradaFuncion.getParametro());
											String lexemaFuncion;
                                            if(entradaFuncion != null){
                                            	lexemaFuncion = entradaFuncion.getLlamadoLexema();
                                                if(entradaParametro != null){
                                                    if(!entradaParametro.getTipo().equals(entradaParametroFormal.getTipo())){
                                                    	this.addErrorCodigoIntermedio("Se intenta llamar a la funcion " + val_peek(3).sval + " Con el tipo " + entradaParametro.getTipo() + " Pero usa " + entradaParametroFormal.getTipo());
                                                    } 
                                                    else {
                                                    	TercetoOperandos tercetoParametro = new TercetoOperandos(":=", new TercetoLexema(entradaParametroFormal.getLexema()), new TercetoLexema(entradaParametro.getLexema()));
                                             			tercetoParametro.setTipo(entradaParametroFormal.getTipo());
                                             			this.addTerceto(tercetoParametro);
                                                    }
                                                } else {
												   this.addErrorCodigoIntermedio("La variable " + val_peek(1).sval + " no esta definida en el ambito actual");
														
                                                }                                   
                                            } else {
												this.addErrorCodigoIntermedio("La funcion " + val_peek(3).sval + " no esta definida en el ambito actual");
												lexemaFuncion = "Funcion no definida";						
                                            }
        									
        									TercetoOperandos tercetoInvocacion = new TercetoOperandos("call", new TercetoLexema(lexemaFuncion)); 
        									tercetoInvocacion.setTipo(entradaFuncion.getTipo());
        									this.factor = tercetoInvocacion;
        									this.addTerceto(tercetoInvocacion);
        									
    									}
break;
case 68:
//#line 665 "gramatica2.y"
{   EntradaTablaSimbolos entradaTablaSimbolos = al.getEntrada(val_peek(0).sval);
    														if (entradaTablaSimbolos.getTipo().equals(EntradaTablaSimbolos.INT)) {
    															   if ((Integer.parseInt(entradaTablaSimbolos.getLexema())) == AnalizadorLexico.MAX_INT_ABSOLUTO) {
    																			addWarningSintactico(String.format("Warning integer cte positiva mayor al maximo permitido en linea %1$d, se acota al maximo permitido", al.getLinea()));
    																			Integer nuevoLexema = AnalizadorLexico.MAX_INT;
    																			al.getTablaDeSimbolos().remove(entradaTablaSimbolos.getLexema());
    																			entradaTablaSimbolos.setLexema(String.valueOf(nuevoLexema));
    																			al.getTablaDeSimbolos().put(entradaTablaSimbolos.getLexema(), entradaTablaSimbolos);
    																}
    														  }
    														  this.constante = entradaTablaSimbolos.getLexema();

    													}
break;
case 69:
//#line 679 "gramatica2.y"
{   EntradaTablaSimbolos entradaTablaSimbolos = al.getEntrada(val_peek(0).sval);
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
break;
//#line 1454 "Parser.java"
//########## END OF USER-SUPPLIED ACTIONS ##########
    }//switch
    //#### Now let's reduce... ####
    if (yydebug) debug("reduce");
    state_drop(yym);             //we just reduced yylen states
    yystate = state_peek(0);     //get new state
    val_drop(yym);               //corresponding value drop
    yym = yylhs[yyn];            //select next TERMINAL(on lhs)
    if (yystate == 0 && yym == 0)//done? 'rest' state and at first TERMINAL
      {
      if (yydebug) debug("After reduction, shifting from state 0 to state "+YYFINAL+"");
      yystate = YYFINAL;         //explicitly say we're done
      state_push(YYFINAL);       //and save it
      val_push(yyval);           //also save the semantic value of parsing
      if (yychar < 0)            //we want another character?
        {
        yychar = yylex();        //get next character
        if (yychar<0) yychar=0;  //clean, if necessary
        if (yydebug)
          yylexdebug(yystate,yychar);
        }
      if (yychar == 0)          //Good exit (if lex returns 0 ;-)
         break;                 //quit the loop--all DONE
      }//if yystate
    else                        //else not done yet
      {                         //get next state and push, for next yydefred[]
      yyn = yygindex[yym];      //find out where to go
      if ((yyn != 0) && (yyn += yystate) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yystate)
        yystate = yytable[yyn]; //get new state
      else
        yystate = yydgoto[yym]; //else go to new defred
      if (yydebug) debug("after reduction, shifting from state "+state_peek(0)+" to state "+yystate+"");
      state_push(yystate);     //going again, so push state & val...
      val_push(yyval);         //for next action
      }
    }//main loop
  return 0;//yyaccept!!
}
//## end of method parse() ######################################



//## run() --- for Thread #######################################
/**
 * A default run method, used for operating this parser
 * object in the background.  It is intended for extending Thread
 * or implementing Runnable.  Turn off with -Jnorun .
 */
public void run()
{
  yyparse();
}
//## end of method run() ########################################



//## Constructors ###############################################
/**
 * Default constructor.  Turn off with -Jnoconstruct .

 */
public Parser()
{
  //nothing to do
}


/**
 * Create a parser, setting the debug to true or false.
 * @param debugMe true for debugging, false for no debug.
 */
public Parser(boolean debugMe)
{
  yydebug=debugMe;
}
//###############################################################



}
//################### END OF CLASS ##############################
