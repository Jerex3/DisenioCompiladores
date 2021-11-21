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
    0,    1,    1,    1,    1,    1,    1,    1,    1,    1,
    2,    2,    2,    2,    2,    2,    4,    4,    4,    4,
    4,    7,    7,    8,    8,   10,   10,    9,    5,    5,
    6,   13,   13,   12,   11,   16,   14,   15,    3,    3,
   19,   19,   19,   19,   20,   21,   24,   25,   25,   26,
   27,   28,   29,   29,   23,   31,   30,   22,   18,   18,
   32,   32,   33,   17,   17,   17,   35,   35,   35,   36,
   36,   36,   36,   34,   34,   34,   34,   34,   34,   37,
   38,   38,
};
final static short yylen[] = {                            2,
    1,    4,    3,    4,    3,    4,    3,    4,    3,    4,
    1,    1,    1,    2,    2,    2,    3,    2,    2,    3,
    3,    1,    1,    1,    3,    1,    3,    1,    7,    7,
    7,    4,    2,    6,    1,    2,    4,    7,    1,    2,
    1,    1,    1,    1,    4,    3,    4,    3,    5,    1,
    1,    1,    1,    4,    4,    3,    1,    5,    3,    1,
    3,    1,    3,    3,    3,    1,    3,    3,    1,    1,
    4,    1,    1,    1,    1,    1,    1,    1,    1,    4,
    1,    2,
};
final static short yydefred[] = {                         0,
    0,    0,   22,   23,    0,    1,    0,   11,   12,   13,
    0,    0,    0,    0,   28,    0,   24,    0,    0,   57,
    0,    0,   39,   41,   42,   43,   44,    0,    0,    0,
    0,    0,   14,   15,   16,    0,   19,    0,    0,    0,
    0,    0,   21,    0,    0,    0,    3,   40,    0,    0,
    0,    0,    0,    0,    0,    0,    7,    0,    5,   20,
   17,    0,    0,    0,    4,   25,    0,    0,   81,    0,
   70,    0,    0,    0,   62,    0,   69,   72,   73,    0,
    0,    0,   53,    0,   50,   46,    0,    0,    6,    8,
   10,    2,    0,    0,    0,    0,    0,   82,   77,   78,
   76,   79,    0,    0,   74,   75,    0,    0,   47,    0,
    0,    0,    0,   45,    0,   51,   48,    0,   56,   55,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   61,   67,   68,   58,    0,    0,   52,
   36,   34,   26,    0,    0,    0,    0,    0,   71,   80,
   54,   49,   30,    0,   29,    0,   31,    0,    0,   27,
   37,    0,   32,    0,    0,    0,    0,   38,
};
final static short yydgoto[] = {                          5,
    6,    7,   21,    8,    9,   10,   11,   16,   22,  144,
   12,   13,  126,  127,  159,  122,   72,   73,   23,   24,
   25,   26,   27,   28,   51,   84,  118,  139,   85,   29,
   53,   74,   75,  107,   76,   77,   78,   79,
};
final static short yysindex[] = {                       -48,
 -166, -201,    0,    0,    0,    0, -168,    0,    0,    0,
  -51, -252, -161, -201,    0,   31,    0,   10,   12,    0,
 -160, -207,    0,    0,    0,    0,    0, -142,   52, -201,
  -98, -110,    0,    0,    0,   61,    0,   41,    5, -136,
  -46,  -89,    0, -136,  -42, -125,    0,    0,  -42, -202,
   97,  -42, -104,  -72, -207,  -92,    0,  -86,    0,    0,
    0,  137, -144, -201,    0,    0,  147,  152,    0,  -82,
    0,  -16,   -6,  -61,    0,   93,    0,    0,    0,  176,
   24, -201,    0, -117,    0,    0,   -5, -202,    0,    0,
    0,    0, -144,  -36,  -60,  -42,  -53,    0,    0,    0,
    0,    0,  -42,  -42,    0,    0,  -42,  -42,    0,  -42,
  -42,  -42,  167,    0,  -63,    0,    0, -202,    0,    0,
  -45,  187, -136, -136,  191,  -13,  173,  139,  218,   93,
   93,   94,  -61,    0,    0,    0,    0,  205,   11,    0,
    0,    0,    0,   64,   71,  -42,  206,    3,    0,    0,
    0,    0,    0, -136,    0,  170,    0,  216,  217,    0,
    0,  238,    0,  -42,   -4,  235,    8,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   18,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0, -139,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   75,    0,    0,  282,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  -41,    0,    0,
    0,    0,    0,   -1,    0,  -34,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  -28,
  -21,   -8,    1,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   19,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
    0,  270,   47,    7,   16,   23,  -14,  274,   42,  162,
    0,    0,    0,    0,    0,    0,  -33,  -40,   26,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  -37,    0,
    0,  179,  178,    0,   57,   79,    0,    0,
};
final static int YYTABLESIZE=288;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         28,
   28,   28,   70,   28,  124,   28,   66,   37,   66,   39,
   66,   87,   64,   33,   64,   81,   64,   28,   28,   65,
   28,   65,   34,   65,   66,   66,  103,   66,  104,   35,
   64,   64,   63,   64,  109,  119,  166,   65,   65,   60,
   65,   59,   17,  106,   63,  105,   48,   33,   94,   45,
  120,   46,   17,   32,   18,   18,   34,   48,   19,   19,
   42,   82,  128,   35,   20,   20,  103,   48,  104,   15,
   15,   55,   49,  132,   44,   83,   54,   58,  121,   48,
  140,   17,  114,   48,   44,   66,   71,   30,   18,   43,
   71,   52,   19,   71,   40,   31,   18,   14,   20,   61,
   19,    3,    4,   15,   47,   15,   20,  154,    3,    4,
   95,   15,  156,   83,  154,   50,   18,   18,   24,   60,
   48,   18,  153,  165,   18,    3,    4,   18,  115,  155,
   18,   18,   18,   24,  111,   15,  103,   71,  104,  112,
   48,  116,  117,   83,   71,   71,   18,   80,   71,   71,
   19,   71,   71,   71,   59,   86,   20,   56,   18,  130,
  131,   15,   19,   88,  143,  143,   57,   18,   20,   91,
   18,   19,   90,   15,   19,   65,   93,   20,   92,  149,
   20,  103,   15,  104,   18,   15,   96,   71,   19,  135,
  136,   97,   89,   18,   20,  160,   18,   19,   98,   15,
   19,  138,  125,   20,   36,   71,   20,    1,   15,   40,
  161,   15,  103,  110,  104,    2,  113,   64,  129,  123,
   15,    3,    4,    3,    4,  137,  141,  142,   67,   68,
  146,  148,   28,   28,   28,   28,   28,   28,   69,   66,
   66,   66,   66,   66,   66,   64,   64,   64,   64,   64,
   64,  147,   65,   65,   65,   65,   65,   65,  150,   99,
  100,  101,  102,  151,  157,   63,   63,  108,  108,  108,
  152,  158,   60,  162,   59,  163,   62,  164,  167,   35,
  168,    9,   41,   33,   38,  145,  133,  134,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
   42,   43,   45,   45,   41,   47,   41,   59,   43,  262,
   45,   52,   41,    7,   43,   49,   45,   59,   60,   41,
   62,   43,    7,   45,   59,   60,   43,   62,   45,    7,
   59,   60,   41,   62,   41,   41,   41,   59,   60,   41,
   62,   41,    1,   60,   40,   62,   21,   41,   63,   40,
   88,   40,   11,    7,  257,  257,   41,   32,  261,  261,
   14,  264,   96,   41,  267,  267,   43,   42,   45,  272,
  272,   30,  280,  107,   44,   50,   30,   31,   93,   54,
  118,   40,   59,   58,   44,   44,   45,  256,  257,   59,
   49,   40,  261,   52,  256,  264,  257,  264,  267,   59,
  261,  270,  271,  272,  265,  272,  267,   44,  270,  271,
   64,  272,  146,   88,   44,  258,  256,  257,   44,   59,
   95,  261,   59,  164,  264,  270,  271,  267,   82,   59,
  270,  271,  272,   59,   42,  272,   43,   96,   45,   47,
  115,  259,  260,  118,  103,  104,  257,  273,  107,  108,
  261,  110,  111,  112,  265,   59,  267,  256,  257,  103,
  104,  272,  261,  268,  123,  124,  265,  257,  267,  256,
  257,  261,  265,  272,  261,  265,   40,  267,  265,   41,
  267,   43,  272,   45,  257,  272,   40,  146,  261,  111,
  112,   40,  265,  257,  267,  154,  257,  261,  281,  272,
  261,  265,  263,  267,  256,  164,  267,  256,  272,  256,
   41,  272,   43,  275,   45,  264,   41,  264,  272,  256,
  272,  270,  271,  270,  271,   59,  272,   41,  271,  272,
   40,   59,  274,  275,  276,  277,  278,  279,  281,  274,
  275,  276,  277,  278,  279,  274,  275,  276,  277,  278,
  279,  265,  274,  275,  276,  277,  278,  279,   41,  276,
  277,  278,  279,   59,   59,  274,  275,  274,  274,  274,
  260,  269,  274,   58,  274,   59,  272,   40,   44,  262,
  273,    0,   13,  265,   11,  124,  108,  110,
};
}
final static short YYFINAL=5;
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
"contenidoPrograma : BEGIN conjuntoSentenciasEjecutables END",
"contenidoPrograma : error BEGIN conjuntoSentenciasEjecutables END",
"contenidoPrograma : sentenciaDeclarativa conjuntoSentenciasEjecutables END",
"contenidoPrograma : sentenciaDeclarativa error conjuntoSentenciasEjecutables END",
"contenidoPrograma : sentenciaDeclarativa BEGIN END",
"contenidoPrograma : sentenciaDeclarativa BEGIN error END",
"contenidoPrograma : sentenciaDeclarativa BEGIN conjuntoSentenciasEjecutables",
"contenidoPrograma : sentenciaDeclarativa BEGIN conjuntoSentenciasEjecutables error",
"sentenciaDeclarativa : declaracionSimple",
"sentenciaDeclarativa : declaracionEncabezadoFuncion",
"sentenciaDeclarativa : declaracionFuncion",
"sentenciaDeclarativa : sentenciaDeclarativa declaracionSimple",
"sentenciaDeclarativa : sentenciaDeclarativa declaracionEncabezadoFuncion",
"sentenciaDeclarativa : sentenciaDeclarativa declaracionFuncion",
"declaracionSimple : tipo listaVariables ';'",
"declaracionSimple : tipo listaVariables",
"declaracionSimple : tipo ';'",
"declaracionSimple : tipo error ';'",
"declaracionSimple : error listaVariables ';'",
"tipo : INT",
"tipo : SINGLE",
"listaVariables : identificador",
"listaVariables : listaVariables ',' identificador",
"listaVariablesFuncion : identificador",
"listaVariablesFuncion : listaVariablesFuncion ',' identificador",
"identificador : ID",
"declaracionEncabezadoFuncion : tipoFuncion FUNC '(' tipo ')' listaVariablesFuncion ';'",
"declaracionEncabezadoFuncion : tipoFuncion FUNC '(' tipo error listaVariablesFuncion ';'",
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

//#line 740 "gramatica2.y"

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
//#line 709 "Parser.java"
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
case 3:
//#line 22 "gramatica2.y"
{addErrorSintactico(String.format("Falta el conjunto de sentencias declarativas"));}
break;
case 4:
//#line 25 "gramatica2.y"
{addErrorSintactico(String.format("Falta el conjunto de sentencias declarativas"));}
break;
case 5:
//#line 28 "gramatica2.y"
{addErrorSintactico(String.format("Falta el 'BEGIN' del conjunto de sentencias ejecutables"));}
break;
case 6:
//#line 31 "gramatica2.y"
{addErrorSintactico(String.format("Falta el 'BEGIN' del conjunto de sentencias ejecutables"));}
break;
case 7:
//#line 34 "gramatica2.y"
{addErrorSintactico(String.format("Falta el conjunto de sentencias ejecutables"));}
break;
case 8:
//#line 37 "gramatica2.y"
{addErrorSintactico(String.format("Falta el conjunto de sentencias ejecutables"));}
break;
case 9:
//#line 40 "gramatica2.y"
{addErrorSintactico(String.format("Falta el 'END' del conjunto de sentencias ejecutables"));}
break;
case 10:
//#line 43 "gramatica2.y"
{addErrorSintactico(String.format("Falta el 'END' del conjunto de sentencias ejecutables"));}
break;
case 17:
//#line 65 "gramatica2.y"
{addReglaSintacticaReconocida(String.format("Declaracion simple reconocida en linea %1$d",al.getLinea()));}
break;
case 18:
//#line 68 "gramatica2.y"
{addErrorSintactico(String.format("Falta ; en la declaracion de variables en linea %1$d",al.getLinea()));}
break;
case 19:
//#line 71 "gramatica2.y"
{addErrorSintactico(String.format("Faltan las variables en linea %1$d",al.getLinea()));}
break;
case 20:
//#line 74 "gramatica2.y"
{addErrorSintactico(String.format("Faltan las variables en linea %1$d",al.getLinea()));}
break;
case 21:
//#line 78 "gramatica2.y"
{addErrorSintactico(String.format("Falta el tipo en la declaracion de variables en linea %1$d",al.getLinea()));}
break;
case 22:
//#line 85 "gramatica2.y"
{this.ultimoTipo = EntradaTablaSimbolos.INT;}
break;
case 23:
//#line 87 "gramatica2.y"
{this.ultimoTipo = EntradaTablaSimbolos.SINGLE;}
break;
case 24:
//#line 92 "gramatica2.y"
{ String lex = renombrarLexema();
                                                    EntradaTablaSimbolos en = al.getEntrada(lex);
                                                    if(en != null) en.setTipo(this.ultimoTipo);
                                                    
                                                    }
break;
case 25:
//#line 98 "gramatica2.y"
{String lex = renombrarLexema();
                                                    EntradaTablaSimbolos en = al.getEntrada(lex);
                                                    en.setTipo(this.ultimoTipo);
                                                    }
break;
case 26:
//#line 105 "gramatica2.y"
{ 
        												this.listaVariablesFuncion = new ArrayList<String>();
        												this.listaVariablesFuncion.add(val_peek(0).sval);
                                                    }
break;
case 27:
//#line 110 "gramatica2.y"
{
        													this.listaVariablesFuncion.add(val_peek(0).sval);
                                                    		
                                                    	 }
break;
case 29:
//#line 124 "gramatica2.y"
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
case 30:
//#line 135 "gramatica2.y"
{addErrorSintactico("falta un (" + " en linea " + al.getLinea());}
break;
case 31:
//#line 140 "gramatica2.y"
{addReglaSintacticaReconocida(String.format("Declaracion de funcion reconocida en linea %1$d",al.getLinea()));
                                                                                                                finDeFuncion();}
break;
case 32:
//#line 147 "gramatica2.y"
{
        											TercetoOperandos tercetoBI = (TercetoOperandos) this.pilaTercetoFinFuncion.pop();
        											TercetoOperandos etiqueta = new TercetoOperandos("Label_" + (this.numeroTercetos + 1));
        											TercetoOperandos finFuncion = new TercetoOperandos("finfuncion");
        											tercetoBI.setOperador1(etiqueta);
        											this.addTerceto(etiqueta);       										
        											this.addTerceto(finFuncion);
        										}
break;
case 33:
//#line 156 "gramatica2.y"
{
       												TercetoOperandos finFuncion = new TercetoOperandos("finfuncion");
        											this.addTerceto(finFuncion);
        										}
break;
case 34:
//#line 166 "gramatica2.y"
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
case 35:
//#line 203 "gramatica2.y"
{this.ultimoTipoFuncion = this.ultimoTipo;}
break;
case 36:
//#line 206 "gramatica2.y"
{
        			this.tipoParametro = this.ultimoTipo;
        			this.lexemaParametro = val_peek(0).sval;
        		}
break;
case 37:
//#line 213 "gramatica2.y"
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
case 38:
//#line 228 "gramatica2.y"
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
case 45:
//#line 272 "gramatica2.y"
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
										 System.out.println(estaEnTablaSimbolos.getTipo());
										 System.out.println(this.tipoExpresion);
                                         if(!this.tipoExpresion.toString().equals(estaEnTablaSimbolos.getTipo())) {
                                            this.addErrorCodigoIntermedio("No es posible realizar la asignacion por incompatibilidad de tipos entre " + estaEnTablaSimbolos.getLexema() + " y " + this.expresion.toString());
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
case 46:
//#line 337 "gramatica2.y"
{
                                                                addReglaSintacticaReconocida(String.format("Sentencia if reconocida en linea %1$d",al.getLinea()));                                                                                                      
                                                                TercetoOperandos tercetoBI = (TercetoOperandos) this.pilaTercetos.pop();
                                                                int numeroDestino = this.numeroTercetos + 1;
                                                                tercetoBI.setOperador1(new TercetoPosicion(numeroDestino));
                                                                TercetoOperandos etiqueta = new TercetoOperandos("Label_" + numeroDestino);
                                                                this.addTerceto(etiqueta);
													  
                                                            }
break;
case 47:
//#line 351 "gramatica2.y"
{	
                                                        TercetoOperandos tercetoBF = new TercetoOperandos("BF", this.TercetoCondicion);
								                    	this.addTerceto(tercetoBF);
                                                        this.pilaTercetos.push(tercetoBF);
                                                    }
break;
case 50:
//#line 367 "gramatica2.y"
{
										   TercetoOperandos tercetoBF = (TercetoOperandos) this.pilaTercetos.pop();
										   int numeroDestino = this.numeroTercetos + 2;
										   tercetoBF.setOperador2(new TercetoPosicion(numeroDestino));
										   TercetoOperandos tercetoBI = new TercetoOperandos("BI");
										   this.addTerceto(tercetoBI);
										   this.pilaTercetos.push(tercetoBI);
									   }
break;
case 51:
//#line 378 "gramatica2.y"
{
                    int numeroDestino = this.numeroTercetos + 1;
					TercetoOperandos etiqueta = new TercetoOperandos("Label_" + numeroDestino);
					this.addTerceto(etiqueta);
                 }
break;
case 52:
//#line 386 "gramatica2.y"
{System.out.println("HOLA");}
break;
case 55:
//#line 396 "gramatica2.y"
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
case 56:
//#line 420 "gramatica2.y"
{
                            TercetoOperandos tercetoBF = new TercetoOperandos("BF", this.TercetoCondicion);
                            this.addTerceto(tercetoBF);
                            this.pilaTercetos.push(tercetoBF);
                         }
break;
case 57:
//#line 429 "gramatica2.y"
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
case 58:
//#line 442 "gramatica2.y"
{
													        addReglaSintacticaReconocida(String.format("Sentencia print reconocida en linea %1$d",al.getLinea()));
													        TercetoOperandos terPrint = new TercetoOperandos("print", new TercetoLexema(val_peek(2).sval));
													        this.addTerceto(terPrint);
													       }
break;
case 59:
//#line 453 "gramatica2.y"
{
                                                    TercetoOperandos ter = new TercetoOperandos("||", this.TercetoCondicion, TercetoSubcondicion );
                                                    this.addTerceto(ter);
                                                    this.TercetoCondicion = ter;
                                                 	   
                                                    }
break;
case 60:
//#line 460 "gramatica2.y"
{this.TercetoCondicion = this.TercetoSubcondicion;

                                                    }
break;
case 61:
//#line 467 "gramatica2.y"
{
                                                    TercetoOperandos ter = new TercetoOperandos("&&", TercetoSubcondicion, TercetoComparacion );
                                                    this.addTerceto(ter);
                                                    this.TercetoSubcondicion = ter;
                                                }
break;
case 62:
//#line 473 "gramatica2.y"
{this.TercetoSubcondicion = this.TercetoComparacion;  }
break;
case 63:
//#line 478 "gramatica2.y"
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
case 64:
//#line 499 "gramatica2.y"
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
case 65:
//#line 511 "gramatica2.y"
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
case 66:
//#line 524 "gramatica2.y"
{
                                    this.expresion = this.termino;
                                    this.tipoExpresion.setLength(0);
                                    this.tipoExpresion.append(this.tipoTermino.toString());
                                    
                                    this.listaExpresiones.add(this.expresion);
                            
                                    
                                    
                                   }
break;
case 67:
//#line 541 "gramatica2.y"
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
case 68:
//#line 555 "gramatica2.y"
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
case 69:
//#line 569 "gramatica2.y"
{this.termino = this.factor;
                                     this.tipoTermino.setLength(0);
                                     this.tipoTermino.append(this.tipoFactor.toString());
                                	 this.listaTerminos.add(this.termino);	
                               
                                    }
break;
case 70:
//#line 580 "gramatica2.y"
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
case 71:
//#line 599 "gramatica2.y"
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
case 72:
//#line 620 "gramatica2.y"
{}
break;
case 73:
//#line 622 "gramatica2.y"
{
                                    this.factor =  new TercetoLexema(this.constante);
                                    this.tipoFactor.setLength(0);
                                    this.tipoFactor.append(al.getEntrada(this.constante).getTipo());
                                }
break;
case 74:
//#line 630 "gramatica2.y"
{
					  this.accionSemanticaComparador();
					  this.comparador = ">";
				  }
break;
case 75:
//#line 635 "gramatica2.y"
{
					  this.accionSemanticaComparador();
					  this.comparador = "<";
				 }
break;
case 76:
//#line 640 "gramatica2.y"
{
							this.accionSemanticaComparador();
							this.comparador = "==";
						}
break;
case 77:
//#line 645 "gramatica2.y"
{
								this.accionSemanticaComparador();
								this.comparador = ">=";
							  }
break;
case 78:
//#line 650 "gramatica2.y"
{
								this.accionSemanticaComparador();
								this.comparador = "<=";
							  }
break;
case 79:
//#line 655 "gramatica2.y"
{
						this.accionSemanticaComparador();
						this.comparador = "!=";
					  }
break;
case 80:
//#line 668 "gramatica2.y"
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
case 81:
//#line 705 "gramatica2.y"
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
case 82:
//#line 719 "gramatica2.y"
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
//#line 1533 "Parser.java"
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
