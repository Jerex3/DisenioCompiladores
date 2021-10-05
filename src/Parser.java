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






//#line 2 "gramatica.y"
import java.util.ArrayList;
import java.util.HashMap;

//#line 21 "Parser.java"




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
    0,    1,    1,    1,    2,    2,    2,    2,    2,    2,
    4,    4,    7,    7,    8,    8,    8,    9,    5,    5,
    5,    5,    5,    5,    6,    6,    6,    6,   12,   12,
   12,   12,   12,   10,   10,   10,   10,   15,   11,   11,
   13,   13,   13,   13,   14,   14,   14,   14,   14,   14,
   14,    3,    3,   18,   18,   18,   18,   19,   19,   19,
   20,   20,   24,   24,   24,   24,   25,   25,   25,   25,
   25,   25,   25,   26,   26,   26,   26,   22,   22,   22,
   22,   22,   21,   21,   21,   21,   21,   17,   17,   27,
   27,   28,   16,   16,   16,   30,   30,   30,   31,   31,
   29,   29,   29,   29,   29,   29,   23,   23,   23,   34,
   34,   34,   34,   33,   33,   33,   32,   32,
};
final static short yylen[] = {                            2,
    1,    4,    4,    4,    1,    1,    1,    2,    2,    2,
    3,    3,    1,    1,    1,    3,    3,    1,    7,    7,
    7,    7,    7,    7,    7,    7,    7,    7,    4,    2,
    4,    4,    2,    6,    6,    6,    6,    2,    1,    2,
    4,    4,    4,    4,    7,    7,    7,    7,    7,    7,
    7,    1,    2,    1,    1,    1,    1,    4,    4,    4,
    3,    3,    4,    4,    4,    4,    3,    5,    3,    3,
    5,    5,    5,    1,    4,    4,    4,    6,    6,    6,
    6,    6,    5,    5,    5,    5,    5,    3,    1,    3,
    1,    3,    3,    3,    1,    3,    3,    1,    1,    1,
    1,    1,    1,    1,    1,    1,    1,    1,    1,    4,
    4,    4,    4,    4,    4,    4,    1,    2,
};
final static short yydefred[] = {                         0,
   13,   14,    0,    1,    0,    5,    6,    7,    0,    0,
    0,    0,    8,    9,   10,    0,   18,    0,   15,   39,
    0,    0,    0,    0,    0,    0,    0,   52,   54,   55,
   56,   57,    0,    0,    0,    0,    0,    0,   11,    0,
    0,    0,   40,    0,    0,    0,    0,    0,    0,    3,
   53,    0,    0,    0,    0,    0,    4,    2,    0,    0,
    0,    0,    0,   17,   16,    0,    0,  117,    0,   99,
    0,    0,    0,   91,    0,   98,  100,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  108,
  109,    0,    0,   74,    0,    0,   62,   61,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  118,  103,  104,  105,  106,    0,    0,  101,  102,    0,
    0,   64,    0,    0,    0,   65,   66,   63,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   59,
   60,   58,    0,    0,   69,    0,    0,   67,    0,   38,
   35,   36,   37,   34,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   90,
   96,   97,   84,   85,   86,   87,   83,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   20,   21,   22,   23,    0,   19,    0,
    0,    0,   26,    0,    0,    0,   27,   28,   25,   79,
   80,   81,   82,   78,  111,  112,  113,  110,  115,  116,
  114,   76,   77,   75,   71,   72,   73,   68,   42,   43,
   44,   41,    0,    0,   31,   32,   29,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   46,   47,   50,   48,   49,
   51,   45,
};
final static short yydgoto[] = {                          3,
    4,    5,   26,    6,    7,    8,    9,   18,   27,   10,
   22,  108,  109,  205,  101,   71,   72,   94,   29,   30,
   31,   32,   89,   33,   56,   95,   73,   74,  120,   75,
   76,   77,   90,   91,
};
final static short yysindex[] = {                      -242,
    0,    0,    0,    0,  -71,    0,    0,    0, -194, -242,
  -60,  -60,    0,    0,    0,   35,    0,   49,    0,    0,
 -205,   69,  -39,  -37,  -33, -119, -220,    0,    0,    0,
    0,    0, -154, -116, -242,  -31, -161, -205,    0, -205,
  -60,  -60,    0,  -10,   -4, -123, -212,  -10,    1,    0,
    0,  -11,  -11,  -77,  -77,   44,    0,    0,  106, -242,
 -135,  133,  -30,    0,    0,  -69,  -69,    0, -118,    0,
  -23,   -9,  -97,    0,   42,    0,    0,  148,   16,  150,
  155,  -24,   11,  161,   22,  -27,  168,   62,  154,    0,
    0,   47,  -60,    0, -133,  -98,    0,    0, -205,  -50,
  187,  189,    8, -205, -205, -201,  -25,   30,   53, -164,
    0,    0,    0,    0,    0,  -10,  -10,    0,    0,  -10,
  -10,    0,  -10,  -10,  -10,    0,    0,    0,  263,  265,
  267,   57,   70,   73,   77, -174,  -11,   -5, -198,    0,
    0,    0,  -86,  -77,    0,  -77,  -77,    0,   52,    0,
    0,    0,    0,    0,   54,   55,  278,   56,  -10,    2,
  287,   81,   81,  288,   58,   42,   42,   62,  -97,    0,
    0,    0,    0,    0,    0,    0,    0,  -77,  -77,  -77,
  -77,  -77,  312,  313,   46,  314,   50,  297,   59,  101,
  102,  -96, -205,    0,    0,    0,    0, -205,    0,  173,
  322,   45,    0,  -32,  305,   60,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  325,   37,    0,    0,    0,  -10,  -10,    3,
   12,   15,  326,   23,  324,  327,    7,  328,  329,   93,
   96,   97,  103,  104, -197,    0,    0,    0,    0,    0,
    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   78,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   24,    0,  -41,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   38,  -36,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   61,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0, -126, -113,    0,    0,  -35,  -29,   13,   25,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   87,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,
};
final static short yygindex[] = {                         0,
    0,    0,   31,   40,  369,  370,  122,  105,  283,    0,
    0,  311,    0,  216,  319,   17,   80,  318,    0,    0,
    0,    0,  -15,    0,    0,  -13,  260,  268,    0,   89,
   48,    0,    0,    0,
};
final static int YYTABLESIZE=523;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         95,
   45,   95,   47,   95,  107,   93,   49,   93,   61,   93,
  106,   94,  138,   94,  160,   94,  132,   95,   95,  116,
   95,  117,  107,   93,   93,  234,   93,    1,    2,   94,
   94,  122,   94,   69,   69,   52,  119,   92,  118,   69,
   69,   96,   34,   81,   13,   69,   69,   69,  154,   20,
  253,  133,  245,   92,  157,  246,  128,  186,  261,   53,
   82,   43,  136,  249,   89,   88,   17,   16,   88,   88,
   17,   66,   67,  187,   37,  262,  240,   17,   18,   18,
   18,  181,   18,  124,   18,  232,  218,  116,  125,  117,
  221,  164,   40,  182,   62,   40,   18,   40,   40,   40,
  165,   54,   98,   55,  116,  142,  117,   39,    1,    2,
  194,  163,  195,  196,  199,  177,  209,  224,  237,   70,
  102,  183,  185,  143,   79,  144,  145,   83,   85,   33,
  190,   21,  191,  192,    1,    2,  168,   23,   33,   57,
   23,   24,   30,   21,   24,   50,   99,   25,   58,   80,
   25,   30,   17,   88,   88,   17,   59,  146,   63,  227,
  147,  148,  111,  228,  210,  211,  212,  213,  214,  188,
   23,  171,  172,  104,   24,  200,  202,  123,  189,   23,
   25,  100,  100,   24,   11,   17,   93,   23,  126,   25,
  129,   24,   12,  107,   17,  130,   23,   25,    1,    2,
   24,  134,   17,  149,  166,  167,   25,  139,  155,  156,
  158,   17,  140,  229,   95,  116,   44,  117,   46,  107,
   93,  150,   48,  233,   60,  105,   94,  151,  137,  152,
  159,  131,   95,   95,   95,   95,   95,   95,   93,   93,
   93,   93,   93,   93,   94,   94,   94,   94,   94,   94,
  184,   78,  112,  113,  114,  115,   84,  201,  243,   86,
   87,   17,  252,  153,  121,   86,   87,   17,   92,   68,
   68,  127,   17,   17,   17,   68,   68,  135,  248,   89,
   88,   68,   68,   68,  121,  121,   92,   92,  121,  121,
   35,   19,  239,   18,  161,  121,  121,   89,   88,   97,
  231,  217,  141,   19,   38,  220,   36,  193,  162,  193,
  193,  198,  176,  208,  223,  236,   70,  241,  242,  244,
   64,  173,   65,  174,   41,  175,   70,   70,   28,   28,
   70,   70,   42,   12,   70,   70,  197,  178,    1,    2,
  179,   12,   24,   51,  180,  203,  207,   12,   12,  204,
   24,   51,  215,  216,  219,  222,   24,   24,   28,   28,
  225,  226,  230,  235,  238,  256,  247,  250,  257,  258,
  251,  254,  255,   14,   15,  259,  260,  110,  206,  103,
  169,   19,    0,   51,   51,    0,   19,   19,   19,    0,
  170,    0,    0,    0,    0,    0,    0,    0,   70,   70,
    0,    0,   70,   70,    0,   70,   70,   70,    0,    0,
   28,    0,    0,    0,    0,    0,    0,    0,    0,   70,
   70,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   70,   70,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   51,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   64,    0,    0,    0,    0,
   64,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   70,   70,   70,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
   40,   43,   40,   45,   41,   41,   40,   43,   40,   45,
   41,   41,   40,   43,   40,   45,   41,   59,   60,   43,
   62,   45,   59,   59,   60,   58,   62,  270,  271,   59,
   60,   41,   62,   45,   45,  256,   60,   53,   62,   45,
   45,   55,   12,  256,    5,   45,   45,   45,   41,   10,
   44,   41,   41,   41,  256,   41,   41,  256,  256,  280,
  273,   22,   41,   41,   41,   41,  272,  262,   52,   53,
  272,   41,   42,  272,   40,  273,   40,  272,   41,   42,
   43,  256,   45,   42,   47,   41,   41,   43,   47,   45,
   41,  256,   44,  268,  256,   44,   59,   44,   44,   44,
  265,  256,   59,  258,   43,   59,   45,   59,  270,  271,
   59,   59,   59,   59,   59,   59,   59,   59,   59,   59,
  256,  137,  138,   93,   45,  259,  260,   48,   49,  256,
  144,   10,  146,  147,  270,  271,  120,  257,  265,  256,
  257,  261,  256,   22,  261,  265,   41,  267,  265,  273,
  267,  265,  272,  137,  138,  272,   35,  256,   37,  256,
  259,  260,  281,  260,  178,  179,  180,  181,  182,  256,
  257,  124,  125,   41,  261,  159,  160,  275,  265,  257,
  267,   60,   61,  261,  256,  272,  264,  257,   41,  267,
   41,  261,  264,  263,  272,   41,  257,  267,  270,  271,
  261,   41,  272,   99,  116,  117,  267,   40,  104,  105,
  106,  272,   59,   41,  256,   43,  256,   45,  256,  256,
  256,  272,  256,  256,  256,  256,  256,   41,  256,   41,
  256,  256,  274,  275,  276,  277,  278,  279,  274,  275,
  276,  277,  278,  279,  274,  275,  276,  277,  278,  279,
  256,  256,  276,  277,  278,  279,  256,  256,  256,  271,
  272,  272,  256,  256,  274,  271,  272,  272,  256,  281,
  281,  256,  272,  272,  272,  281,  281,  256,  256,  256,
  256,  281,  281,  281,  274,  274,  274,  275,  274,  274,
  256,    9,  256,  256,  265,  274,  274,  274,  274,  256,
  256,  256,  256,   21,  256,  256,  272,  256,  256,  256,
  256,  256,  256,  256,  256,  256,  256,  238,  239,  240,
   38,   59,   40,   59,  256,   59,   44,   45,   11,   12,
   48,   49,  264,  256,   52,   53,   59,  268,  270,  271,
  268,  264,  256,   26,  268,   59,   59,  270,  271,  269,
  264,   34,   41,   41,   41,   59,  270,  271,   41,   42,
  260,  260,   41,   59,   40,  273,   41,   44,  273,  273,
   44,   44,   44,    5,    5,  273,  273,   67,  163,   61,
  121,   99,   -1,   66,   67,   -1,  104,  105,  106,   -1,
  123,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  116,  117,
   -1,   -1,  120,  121,   -1,  123,  124,  125,   -1,   -1,
   93,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  137,
  138,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  159,  160,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
  143,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  193,   -1,   -1,   -1,   -1,
  198,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
  238,  239,  240,
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
"contenidoPrograma : sentenciaDeclarativa error conjuntoSentenciasEjecutables END",
"contenidoPrograma : sentenciaDeclarativa BEGIN conjuntoSentenciasEjecutables error",
"sentenciaDeclarativa : declaracionSimple",
"sentenciaDeclarativa : declaracionEncabezadoFuncion",
"sentenciaDeclarativa : declaracionFuncion",
"sentenciaDeclarativa : sentenciaDeclarativa declaracionSimple",
"sentenciaDeclarativa : sentenciaDeclarativa declaracionEncabezadoFuncion",
"sentenciaDeclarativa : sentenciaDeclarativa declaracionFuncion",
"declaracionSimple : tipo listaVariables ';'",
"declaracionSimple : tipo listaVariables error",
"tipo : INT",
"tipo : SINGLE",
"listaVariables : identificador",
"listaVariables : listaVariables ',' identificador",
"listaVariables : listaVariables error identificador",
"identificador : ID",
"declaracionEncabezadoFuncion : tipo FUNC '(' tipo ')' listaVariables ';'",
"declaracionEncabezadoFuncion : tipo FUNC error tipo ')' listaVariables ';'",
"declaracionEncabezadoFuncion : tipo FUNC '(' error ')' listaVariables ';'",
"declaracionEncabezadoFuncion : tipo FUNC '(' tipo error listaVariables ';'",
"declaracionEncabezadoFuncion : tipo FUNC '(' tipo ')' error ';'",
"declaracionEncabezadoFuncion : tipo FUNC '(' tipo ')' listaVariables error",
"declaracionFuncion : encabezadoFuncion sentenciasDeclarativasFuncion BEGIN conjuntoSentenciasEjecutables finFuncion END ';'",
"declaracionFuncion : encabezadoFuncion sentenciasDeclarativasFuncion error conjuntoSentenciasEjecutables finFuncion END ';'",
"declaracionFuncion : encabezadoFuncion sentenciasDeclarativasFuncion BEGIN conjuntoSentenciasEjecutables finFuncion error ';'",
"declaracionFuncion : encabezadoFuncion sentenciasDeclarativasFuncion BEGIN conjuntoSentenciasEjecutables finFuncion END error",
"finFuncion : retorno ';' postcondicion ';'",
"finFuncion : retorno ';'",
"finFuncion : retorno error postcondicion ';'",
"finFuncion : retorno ';' postcondicion error",
"finFuncion : retorno error",
"encabezadoFuncion : tipo FUNC ID '(' parametro ')'",
"encabezadoFuncion : tipo FUNC ID error parametro ')'",
"encabezadoFuncion : tipo FUNC ID '(' error ')'",
"encabezadoFuncion : tipo FUNC ID '(' parametro error",
"parametro : tipo ID",
"sentenciasDeclarativasFuncion : declaracionSimple",
"sentenciasDeclarativasFuncion : sentenciasDeclarativasFuncion declaracionSimple",
"retorno : RETURN '(' expresionSimple ')'",
"retorno : RETURN error expresionSimple ')'",
"retorno : RETURN '(' error ')'",
"retorno : RETURN '(' expresionSimple error",
"postcondicion : POST ':' '(' condicion ')' ',' CADENA",
"postcondicion : POST error '(' condicion ')' ',' CADENA",
"postcondicion : POST ':' error condicion ')' ',' CADENA",
"postcondicion : POST ':' '(' error ')' ',' CADENA",
"postcondicion : POST ':' '(' condicion error ',' CADENA",
"postcondicion : POST ':' '(' error ')' error CADENA",
"postcondicion : POST ':' '(' condicion ')' ',' error",
"conjuntoSentenciasEjecutables : sentenciaEjecutable",
"conjuntoSentenciasEjecutables : conjuntoSentenciasEjecutables sentenciaEjecutable",
"sentenciaEjecutable : asignacion",
"sentenciaEjecutable : sentenciaIf",
"sentenciaEjecutable : sentenciaPrint",
"sentenciaEjecutable : sentenciaWhile",
"asignacion : identificador ASIGNACION expresionCompleta ';'",
"asignacion : identificador error expresionCompleta ';'",
"asignacion : identificador ASIGNACION expresionCompleta error",
"sentenciaIf : encabezadoIf cuerpoIf ';'",
"sentenciaIf : encabezadoIf cuerpoIf error",
"encabezadoIf : IF '(' condicion ')'",
"encabezadoIf : IF error condicion ')'",
"encabezadoIf : IF '(' error ')'",
"encabezadoIf : IF '(' condicion error",
"cuerpoIf : THEN bloqueSentencias ENDIF",
"cuerpoIf : THEN bloqueSentencias ELSE bloqueSentencias ENDIF",
"cuerpoIf : error bloqueSentencias ENDIF",
"cuerpoIf : THEN bloqueSentencias error",
"cuerpoIf : error bloqueSentencias ELSE bloqueSentencias ENDIF",
"cuerpoIf : THEN bloqueSentencias error bloqueSentencias ENDIF",
"cuerpoIf : THEN bloqueSentencias ELSE bloqueSentencias error",
"bloqueSentencias : sentenciaEjecutable",
"bloqueSentencias : BEGIN conjuntoSentenciasEjecutables END ';'",
"bloqueSentencias : BEGIN conjuntoSentenciasEjecutables error ';'",
"bloqueSentencias : BEGIN conjuntoSentenciasEjecutables END error",
"sentenciaWhile : WHILE '(' condicion ')' DO bloqueSentencias",
"sentenciaWhile : WHILE error condicion ')' DO bloqueSentencias",
"sentenciaWhile : WHILE '(' error ')' DO bloqueSentencias",
"sentenciaWhile : WHILE '(' condicion error DO bloqueSentencias",
"sentenciaWhile : WHILE '(' condicion ')' error bloqueSentencias",
"sentenciaPrint : PRINT '(' CADENA ')' ';'",
"sentenciaPrint : PRINT error CADENA ')' ';'",
"sentenciaPrint : PRINT '(' error ')' ';'",
"sentenciaPrint : PRINT '(' CADENA error ';'",
"sentenciaPrint : PRINT '(' CADENA ')' error",
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
"factor : cte",
"comparador : '>'",
"comparador : '<'",
"comparador : MAYORIGUAL",
"comparador : MENORIGUAL",
"comparador : IGUAL",
"comparador : DISTINTO",
"expresionCompleta : expresionSimple",
"expresionCompleta : invocacionFuncion",
"expresionCompleta : conversionExplicita",
"conversionExplicita : SINGLE '(' expresionCompleta ')'",
"conversionExplicita : SINGLE error expresionCompleta ')'",
"conversionExplicita : SINGLE '(' error ')'",
"conversionExplicita : SINGLE '(' expresionCompleta error",
"invocacionFuncion : ID '(' ID ')'",
"invocacionFuncion : ID '(' error ')'",
"invocacionFuncion : ID '(' ID error",
"cte : CTE",
"cte : '-' CTE",
};

//#line 379 "gramatica.y"

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
//#line 555 "Parser.java"
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
//#line 14 "gramatica.y"
{addReglaSintacticaReconocida(String.format("Programa reconocido en linea %1$d",al.getLinea()));}
break;
case 3:
//#line 20 "gramatica.y"
{addErrorSintactico(String.format("Falta BEGIN en linea %1$d",al.getLinea()));}
break;
case 4:
//#line 22 "gramatica.y"
{addErrorSintactico(String.format("Falta END en linea %1$d",al.getLinea()));}
break;
case 11:
//#line 42 "gramatica.y"
{addReglaSintacticaReconocida(String.format("Declaracion simple reconocida en linea %1$d",al.getLinea()));}
break;
case 12:
//#line 44 "gramatica.y"
{addErrorSintactico(String.format("Falta un ';' en linea %1$d",al.getLinea()));}
break;
case 17:
//#line 60 "gramatica.y"
{addErrorSintactico(String.format("Falta una ',' en linea %1$d",al.getLinea()));}
break;
case 19:
//#line 70 "gramatica.y"
{addReglaSintacticaReconocida(String.format("Declaracion encabezado funcion reconocida en linea %1$d",al.getLinea()));}
break;
case 20:
//#line 72 "gramatica.y"
{addErrorSintactico(String.format("Falta un '(' en la declaracion del encabezado funcion en linea %1$d",al.getLinea()));}
break;
case 21:
//#line 74 "gramatica.y"
{addErrorSintactico(String.format("Falta un tipo entre los parentesis en la declaracion del encabezado funcion en linea %1$d",al.getLinea()));}
break;
case 22:
//#line 76 "gramatica.y"
{addErrorSintactico(String.format("Falta un ')' en la declaracion del encabezado funcion en linea %1$d",al.getLinea()));}
break;
case 23:
//#line 78 "gramatica.y"
{addErrorSintactico(String.format("Faltan los nombre de las variables en la declaracion del encabezado funcion en linea %1$d",al.getLinea()));}
break;
case 24:
//#line 80 "gramatica.y"
{addErrorSintactico(String.format("Falta un ';' en la declaracion del encabezado funcion en linea %1$d",al.getLinea()));}
break;
case 25:
//#line 86 "gramatica.y"
{addReglaSintacticaReconocida(String.format("Declaracion de funcion reconocida en linea %1$d",al.getLinea()));}
break;
case 26:
//#line 88 "gramatica.y"
{addErrorSintactico(String.format("Falta un BEGIN en la declaracion de la funcion en linea %1$d",al.getLinea()));}
break;
case 27:
//#line 90 "gramatica.y"
{addErrorSintactico(String.format("Falta un END en la declaracion de la funcion en linea %1$d",al.getLinea()));}
break;
case 28:
//#line 92 "gramatica.y"
{addErrorSintactico(String.format("Falta un ';' en la declaracion de la funcion en linea %1$d",al.getLinea()));}
break;
case 31:
//#line 101 "gramatica.y"
{addErrorSintactico(String.format("Falta un ';' en el fin de la funcion en linea %1$d",al.getLinea()));}
break;
case 32:
//#line 103 "gramatica.y"
{addErrorSintactico(String.format("Falta un ';' en el fin de la funcion en linea %1$d",al.getLinea()));}
break;
case 33:
//#line 105 "gramatica.y"
{addErrorSintactico(String.format("Falta un ';' en el fin de la funcion en linea %1$d",al.getLinea()));}
break;
case 35:
//#line 112 "gramatica.y"
{addErrorSintactico(String.format("Falta un '(' en el encabezado de la funcion en linea %1$d",al.getLinea()));}
break;
case 36:
//#line 114 "gramatica.y"
{addErrorSintactico(String.format("Falta el parametro en el encabezado de la funcion en linea %1$d",al.getLinea()));}
break;
case 37:
//#line 116 "gramatica.y"
{addErrorSintactico(String.format("Falta un ')' en el encabezado de la funcion en linea %1$d",al.getLinea()));}
break;
case 41:
//#line 133 "gramatica.y"
{addReglaSintacticaReconocida(String.format("Retorno reconocido en linea %1$d",al.getLinea()));}
break;
case 42:
//#line 135 "gramatica.y"
{addErrorSintactico(String.format("Falta un '(' en el retorno de la funcion en linea %1$d",al.getLinea()));}
break;
case 43:
//#line 137 "gramatica.y"
{addErrorSintactico(String.format("Falta una expresion en el retorno de la funcion en linea %1$d",al.getLinea()));}
break;
case 44:
//#line 139 "gramatica.y"
{addErrorSintactico(String.format("Falta un ')' en el retorno de la funcion en linea %1$d",al.getLinea()));}
break;
case 45:
//#line 144 "gramatica.y"
{addReglaSintacticaReconocida(String.format("Postcondicion reconocida en linea %1$d",al.getLinea()));}
break;
case 46:
//#line 146 "gramatica.y"
{addErrorSintactico(String.format("Falta un ':' en la postcondicion de la funcion en linea %1$d",al.getLinea()));}
break;
case 47:
//#line 148 "gramatica.y"
{addErrorSintactico(String.format("Falta un '(' en la postcondicion de la funcion en linea %1$d",al.getLinea()));}
break;
case 48:
//#line 150 "gramatica.y"
{addErrorSintactico(String.format("Falta la condicion en la postcondicion de la funcion en linea %1$d",al.getLinea()));}
break;
case 49:
//#line 152 "gramatica.y"
{addErrorSintactico(String.format("Falta un ')' en la postcondicion de la funcion en linea %1$d",al.getLinea()));}
break;
case 50:
//#line 154 "gramatica.y"
{addErrorSintactico(String.format("Falta una ',' en la postcondicion de la funcion en linea %1$d",al.getLinea()));}
break;
case 51:
//#line 156 "gramatica.y"
{addErrorSintactico(String.format("Falta la cadena en la postcondicion de la funcion en linea %1$d",al.getLinea()));}
break;
case 58:
//#line 179 "gramatica.y"
{addReglaSintacticaReconocida(String.format("Asignacion reconocida en linea %1$d",al.getLinea()));}
break;
case 59:
//#line 181 "gramatica.y"
{addErrorSintactico(String.format("Falta la asignacion en linea %1$d",al.getLinea()));}
break;
case 60:
//#line 183 "gramatica.y"
{addErrorSintactico(String.format("Falta un ';' en linea %1$d",al.getLinea()));}
break;
case 61:
//#line 188 "gramatica.y"
{addReglaSintacticaReconocida(String.format("Sentencia if reconocida en linea %1$d",al.getLinea()));}
break;
case 62:
//#line 190 "gramatica.y"
{addErrorSintactico(String.format("Falta un ';' en linea %1$d",al.getLinea()));}
break;
case 64:
//#line 197 "gramatica.y"
{addErrorSintactico(String.format("Falta un '(' en el encabezado del if en linea %1$d",al.getLinea()));}
break;
case 65:
//#line 199 "gramatica.y"
{addErrorSintactico(String.format("Falta la condicion en el encabezado del if en linea %1$d",al.getLinea()));}
break;
case 66:
//#line 201 "gramatica.y"
{addErrorSintactico(String.format("Falta un ')' en el encabezado del if en linea %1$d",al.getLinea()));}
break;
case 69:
//#line 209 "gramatica.y"
{addErrorSintactico(String.format("Falta un THEN en el cuerpo del if en linea %1$d",al.getLinea()));}
break;
case 70:
//#line 211 "gramatica.y"
{addErrorSintactico(String.format("Falta un ENDIF en el cuerpo del if en linea %1$d",al.getLinea()));}
break;
case 71:
//#line 213 "gramatica.y"
{addErrorSintactico(String.format("Falta un THEN en el cuerpo del if en linea %1$d",al.getLinea()));}
break;
case 72:
//#line 215 "gramatica.y"
{addErrorSintactico(String.format("Falta un ELSE en el cuerpo del if en linea %1$d",al.getLinea()));}
break;
case 73:
//#line 217 "gramatica.y"
{addErrorSintactico(String.format("Falta un ENDIF en el cuerpo del if en linea %1$d",al.getLinea()));}
break;
case 76:
//#line 226 "gramatica.y"
{addErrorSintactico(String.format("Falta un END en linea %1$d",al.getLinea()));}
break;
case 77:
//#line 228 "gramatica.y"
{addErrorSintactico(String.format("Falta un ';' en linea %1$d",al.getLinea()));}
break;
case 78:
//#line 232 "gramatica.y"
{addReglaSintacticaReconocida(String.format("Sentencia while reconocida en linea %1$d",al.getLinea()));}
break;
case 79:
//#line 234 "gramatica.y"
{addErrorSintactico(String.format("Falta un '(' en la sentencia while en linea %1$d",al.getLinea()));}
break;
case 80:
//#line 236 "gramatica.y"
{addErrorSintactico(String.format("Falta la condicion en la sentencia while en linea %1$d",al.getLinea()));}
break;
case 81:
//#line 238 "gramatica.y"
{addErrorSintactico(String.format("Falta un ')' en la sentencia while en linea %1$d",al.getLinea()));}
break;
case 82:
//#line 240 "gramatica.y"
{addErrorSintactico(String.format("Falta un DO en la sentencia while en linea %1$d",al.getLinea()));}
break;
case 83:
//#line 245 "gramatica.y"
{addReglaSintacticaReconocida(String.format("Sentencia print reconocida en linea %1$d",al.getLinea()));}
break;
case 84:
//#line 247 "gramatica.y"
{addErrorSintactico(String.format("Falta un '(' en la sentencia print en linea %1$d",al.getLinea()));}
break;
case 85:
//#line 249 "gramatica.y"
{addErrorSintactico(String.format("Falta la cadena en la sentencia print en linea %1$d",al.getLinea()));}
break;
case 86:
//#line 251 "gramatica.y"
{addErrorSintactico(String.format("Falta un ')' en la sentencia print en linea %1$d",al.getLinea()));}
break;
case 87:
//#line 253 "gramatica.y"
{addErrorSintactico(String.format("Falta un ';' en la sentencia print en linea %1$d",al.getLinea()));}
break;
case 110:
//#line 326 "gramatica.y"
{addReglaSintacticaReconocida(String.format("Conversion explicita reconocida en linea %1$d",al.getLinea()));}
break;
case 111:
//#line 328 "gramatica.y"
{addErrorSintactico(String.format("Falta un '(' en la conversion en linea %1$d",al.getLinea()));}
break;
case 112:
//#line 330 "gramatica.y"
{addErrorSintactico(String.format("Falta la expresion en la conversion en linea %1$d",al.getLinea()));}
break;
case 113:
//#line 332 "gramatica.y"
{addErrorSintactico(String.format("Falta un ')' en la conversion en linea %1$d",al.getLinea()));}
break;
case 114:
//#line 337 "gramatica.y"
{addReglaSintacticaReconocida(String.format("Invocacion a funcion reconocida en linea %1$d",al.getLinea()));}
break;
case 115:
//#line 339 "gramatica.y"
{addErrorSintactico(String.format("Falta un ID en la invocacion a funcion en linea %1$d",al.getLinea()));}
break;
case 116:
//#line 341 "gramatica.y"
{addErrorSintactico(String.format("Falta un ')' en la invocacion a funcion en linea %1$d",al.getLinea()));}
break;
case 117:
//#line 346 "gramatica.y"
{   EntradaTablaSimbolos entradaTablaSimbolos = (EntradaTablaSimbolos) (val_peek(0).obj);
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
break;
case 118:
//#line 359 "gramatica.y"
{   EntradaTablaSimbolos entradaTablaSimbolos = (EntradaTablaSimbolos) (val_peek(0).obj);
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
break;
//#line 999 "Parser.java"
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
