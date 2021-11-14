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
    0,    1,    1,    1,    2,    2,    2,    2,    2,    2,
    2,    2,    4,    4,    7,    7,    8,    8,    8,    9,
    5,    5,    5,    5,    5,    5,    6,    6,    6,    6,
   11,   11,   11,   11,   11,   10,   10,   10,   10,   14,
   12,   12,   12,   12,   13,   13,   13,   13,   13,   13,
   13,    3,    3,   17,   17,   17,   17,   18,   18,   18,
   19,   19,   23,   23,   23,   23,   24,   24,   24,   24,
   24,   24,   24,   25,   26,   27,   28,   28,   28,   28,
   21,   21,   30,   30,   30,   30,   29,   20,   20,   20,
   20,   20,   16,   16,   31,   31,   32,   15,   15,   15,
   34,   34,   34,   35,   35,   35,   33,   33,   33,   33,
   33,   33,   22,   22,   22,   37,   37,   37,   36,   36,
};
final static short yylen[] = {                            2,
    1,    4,    4,    4,    1,    1,    1,    2,    2,    2,
    1,    2,    3,    3,    1,    1,    1,    3,    3,    1,
    7,    7,    7,    7,    7,    7,    7,    7,    7,    7,
    4,    2,    4,    4,    2,    6,    6,    6,    6,    2,
    4,    4,    4,    4,    7,    7,    7,    7,    7,    7,
    7,    1,    2,    1,    1,    1,    1,    4,    4,    4,
    3,    3,    4,    4,    4,    4,    3,    5,    3,    3,
    5,    5,    5,    1,    1,    1,    1,    4,    4,    4,
    4,    4,    3,    3,    3,    3,    1,    5,    5,    5,
    5,    5,    3,    1,    3,    1,    3,    3,    3,    1,
    3,    3,    1,    1,    4,    1,    1,    1,    1,    1,
    1,    1,    1,    1,    1,    4,    4,    4,    1,    2,
};
final static short yydefred[] = {                         0,
   11,   15,   16,    0,    1,    0,    5,    6,    7,    0,
    0,    0,    0,    8,    9,   10,    0,   20,    0,   17,
    0,    0,    0,   87,    0,    0,   52,   54,   55,   56,
   57,    0,    0,    0,    0,    0,    0,    0,   13,    0,
    0,    0,    0,    0,    0,    0,    3,   53,    0,    0,
    0,    0,    0,    0,    0,    0,    4,    2,    0,    0,
    0,    0,    0,   19,   18,    0,    0,    0,  119,    0,
  104,    0,    0,    0,   96,    0,  103,  106,    0,    0,
    0,    0,    0,    0,  115,    0,    0,  113,    0,    0,
   77,    0,   74,    0,   62,   61,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  120,  110,  111,  109,  112,    0,
    0,  107,  108,    0,    0,   64,    0,    0,    0,   65,
   66,   63,    0,    0,    0,    0,    0,   59,   60,   58,
    0,   75,   69,    0,    0,   67,    0,   86,   84,   85,
   83,   82,   81,    0,   40,   37,   38,   39,   36,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   95,  101,  102,   89,   90,
   91,   92,   88,    0,    0,    0,    0,    0,   76,    0,
    0,    0,   22,   23,   24,   25,    0,   21,    0,    0,
    0,   28,    0,    0,    0,   29,   30,   27,  105,  117,
  118,  116,   79,   80,   78,   71,   72,   73,   68,   42,
   43,   44,   41,    0,    0,   33,   34,   31,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   46,   47,   50,   48,
   49,   51,   45,
};
final static short yydgoto[] = {                          4,
    5,    6,   25,    7,    8,    9,   10,   19,   71,   11,
  111,  112,  204,  104,   72,   73,   91,   28,   29,   30,
   31,   87,   32,   53,   92,  144,  188,  189,   33,   56,
   74,   75,  124,   76,   77,   78,   88,
};
final static short yysindex[] = {                       -92,
    0,    0,    0,    0,    0,  -88,    0,    0,    0, -193,
  -92,  -77,  -77,    0,    0,    0,   38,    0,    5,    0,
  -71,  -39,  -37,    0, -109, -233,    0,    0,    0,    0,
    0,  -69,  -33, -134,   86,  -31,  -62, -243,    0, -243,
  -77,  -77,    3,   -5, -237, -228,    0,    0,  -13,  -13,
 -102, -102,   40,    3,    1, -136,    0,    0,   20,   86,
   72,   61,  -36,    0,    0,  -86,  -86,    2,    0, -213,
    0,  -23,   -3, -165,    0,   95,    0,    0,   88,    9,
  103,  120,  -30,  127,    0,  161,   54,    0,   46,  -77,
    0,   67,    0,  -46,    0,    0,   17,  157,   14, -102,
 -102, -243, -146,  160,  166,  -24, -243, -243, -215,  -27,
  -43,   53, -116,    3,    0,    0,    0,    0,    0,    3,
    3,    0,    0,    3,    3,    0,    3,    3,    3,    0,
    0,    0,  169,  171,  193,   55, -189,    0,    0,    0,
 -122,    0,    0, -102, -102,    0, -102,    0,    0,    0,
    0,    0,    0,   41,    0,    0,    0,    0,    0,   42,
   49,  204,   50,    3,    6,  259,   66,   66,  278,   56,
  293,   95,   95,  161, -165,    0,    0,    0,    0,    0,
    0,    0,    0,  303,   13,  287,   57,   91,    0,  104,
 -153, -243,    0,    0,    0,    0, -243,    0,  309,  312,
   39,    0,  -32,  296,   58,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  325,  -25,    0,    0,    0,    3,    3,
    8,   18,   19,  326,   29,  322,  324,   51,  327,  328,
   96,   97,  102,  105,  106, -221,    0,    0,    0,    0,
    0,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  -68,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  -59,    0,    0,
  -68,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   30,    0,  -41,    0,    0,    0,    0,
    0,    0,    0,   45,    0,   59,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   60,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  -99,  -96,    0,    0,
    0,  -35,  -29,   25,   34,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   69,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,
};
final static short yygindex[] = {                         0,
    0,  365,   21,   68,   75,   85,   93,  222,  307,    0,
  310,    0,  212,  320,   27,   92,   31,    0,    0,    0,
    0,  332,    0,    0,  331,  290,   71,   73,    0,    0,
  260,  261,    0,  240,  234,    0,    0,
};
final static int YYTABLESIZE=504;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                        100,
   44,  100,   46,  100,  109,   98,   55,   98,   61,   98,
  136,   99,  165,   99,  231,   99,  159,  100,  100,  120,
  100,  121,   49,   98,   98,  225,   98,   82,   18,   99,
   99,   70,   99,   34,  252,   81,  123,  126,  122,   70,
  162,  114,   27,   27,   83,   70,   50,   70,   40,  132,
   70,  253,   70,  212,  151,   48,   18,  148,  236,  237,
  102,   66,   67,   39,   48,   97,  184,  115,   17,  240,
   94,   27,   27,   14,   93,   86,   86,   37,   18,  223,
   15,  120,  185,  121,   40,   40,   20,   20,   14,   20,
   16,   20,   40,   40,  244,   15,   48,   48,   96,  193,
  194,  107,  218,   20,  140,   16,  219,  195,  198,  127,
  141,  168,  138,  183,  208,  215,  228,  114,   70,  100,
   27,   57,   22,   93,   93,  155,   23,   59,  130,   63,
   58,  101,   24,  186,   22,   80,  128,   18,   23,  169,
  171,  129,  187,  133,   24,   97,   99,   22,  170,   18,
  174,   23,  103,  103,   22,   47,   35,   24,   23,   32,
  134,   90,   18,    1,   24,   35,  137,   12,   32,   18,
   22,   48,  152,  153,   23,   13,  110,    2,    3,   22,
   24,    2,    3,   23,   41,   18,   51,   12,   52,   24,
  199,  201,   42,   62,   18,   12,   14,  149,    2,    3,
  156,   12,   12,  120,   14,  121,  157,    2,    3,  145,
   14,   14,  142,  146,  100,  190,   43,  191,   45,  108,
   98,  166,   54,  224,   60,  135,   99,  179,  164,  180,
  230,  158,  100,  100,  100,  100,  100,  100,   98,   98,
   98,   98,   98,   98,   99,   99,   99,   99,   99,   99,
   79,  181,  116,  117,  118,  119,   98,   68,   84,   85,
   38,  200,  196,  234,  131,   68,   18,   69,  211,  150,
  125,   68,   18,   68,   18,   69,   68,   18,   68,   18,
   97,   69,  125,   69,  239,   94,   69,  125,   69,   93,
  125,  125,  125,   35,  222,   95,  192,  192,   97,   97,
   20,  139,  125,   94,  192,  197,  243,   93,  167,   36,
  182,  207,  214,  227,  114,   70,   20,  202,   26,   26,
  232,  233,  235,  154,   26,  142,  143,  105,  160,  161,
  163,   26,   26,  209,  203,  120,  206,  121,   26,   26,
   26,    2,    3,  210,   64,  213,   65,   26,   26,  220,
  216,  120,  221,  121,  226,    2,    3,   26,   26,  172,
  173,  177,  178,  217,  229,  241,  238,  242,  247,  248,
  245,  246,   26,   26,  249,   21,  113,  250,  251,  205,
  106,   89,   94,  147,  175,    0,    0,  176,    0,    0,
    0,    0,    0,    0,    0,    0,   26,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   26,   26,   20,    0,
    0,    0,    0,   20,   20,   20,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   26,    0,    0,
   26,   26,    0,   26,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   64,    0,
    0,    0,    0,   64,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
   40,   43,   40,   45,   41,   41,   40,   43,   40,   45,
   41,   41,   40,   43,   40,   45,   41,   59,   60,   43,
   62,   45,  256,   59,   60,   58,   62,  256,  272,   59,
   60,   45,   62,   13,  256,  273,   60,   41,   62,   45,
  256,   40,   12,   13,  273,   45,  280,   45,   44,   41,
   45,  273,   45,   41,   41,   25,  272,   41,   41,   41,
   41,   41,   42,   59,   34,   41,  256,  281,  262,   41,
   41,   41,   42,    6,   41,   49,   50,   40,  272,   41,
    6,   43,  272,   45,   44,   44,   42,   43,   21,   45,
    6,   47,   44,   44,   44,   21,   66,   67,   59,   59,
   59,   41,  256,   59,   59,   21,  260,   59,   59,  275,
   90,   59,   59,   59,   59,   59,   59,   59,   59,  256,
   90,  256,  257,   51,   52,  272,  261,   35,   41,   37,
  265,  268,  267,  256,  257,   44,   42,  272,  261,  256,
  114,   47,  265,   41,  267,   54,   55,  257,  265,  272,
  124,  261,   60,   61,  257,  265,  256,  267,  261,  256,
   41,  264,  272,  256,  267,  265,   40,  256,  265,  272,
  257,  141,  100,  101,  261,  264,  263,  270,  271,  257,
  267,  270,  271,  261,  256,  272,  256,  256,  258,  267,
  164,  165,  264,  256,  272,  264,  256,   41,  270,  271,
   41,  270,  271,   43,  264,   45,   41,  270,  271,  256,
  270,  271,  259,  260,  256,  145,  256,  147,  256,  256,
  256,  265,  256,  256,  256,  256,  256,   59,  256,   59,
  256,  256,  274,  275,  276,  277,  278,  279,  274,  275,
  276,  277,  278,  279,  274,  275,  276,  277,  278,  279,
  256,   59,  276,  277,  278,  279,  256,  271,  272,  273,
  256,  256,   59,  256,  256,  271,  272,  281,  256,  256,
  274,  271,  272,  271,  272,  281,  271,  272,  271,  272,
  256,  281,  274,  281,  256,  256,  281,  274,  281,  256,
  274,  274,  274,  256,  256,  256,  256,  256,  274,  275,
  256,  256,  274,  274,  256,  256,  256,  274,  256,  272,
  256,  256,  256,  256,  256,  256,   10,   59,   12,   13,
  229,  230,  231,  102,  256,  259,  260,  256,  107,  108,
  109,   25,  264,   41,  269,   43,   59,   45,  270,  271,
   34,  270,  271,   41,   38,   59,   40,   41,   42,   41,
  260,   43,   41,   45,   59,  270,  271,   51,   52,  120,
  121,  128,  129,  260,   40,   44,   41,   44,  273,  273,
   44,   44,   66,   67,  273,   11,   67,  273,  273,  168,
   61,   50,   52,   94,  125,   -1,   -1,  127,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   90,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  100,  101,  102,   -1,
   -1,   -1,   -1,  107,  108,  109,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  141,   -1,   -1,
  144,  145,   -1,  147,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  192,   -1,
   -1,   -1,   -1,  197,
};
}
final static short YYFINAL=4;
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
"sentenciaDeclarativa : error",
"sentenciaDeclarativa : sentenciaDeclarativa error",
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
"declaracionFuncion : encabezadoFuncion sentenciaDeclarativa BEGIN conjuntoSentenciasEjecutables finFuncion END ';'",
"declaracionFuncion : encabezadoFuncion sentenciaDeclarativa error conjuntoSentenciasEjecutables finFuncion END ';'",
"declaracionFuncion : encabezadoFuncion sentenciaDeclarativa BEGIN conjuntoSentenciasEjecutables finFuncion error ';'",
"declaracionFuncion : encabezadoFuncion sentenciaDeclarativa BEGIN conjuntoSentenciasEjecutables finFuncion END error",
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
"cuerpoIf : THEN cuerpoThen ENDIF",
"cuerpoIf : THEN cuerpoThen elseNT cuerpoElse ENDIF",
"cuerpoIf : error cuerpoThen ENDIF",
"cuerpoIf : THEN cuerpoThen error",
"cuerpoIf : error cuerpoThen elseNT cuerpoElse ENDIF",
"cuerpoIf : THEN cuerpoThen error cuerpoElse ENDIF",
"cuerpoIf : THEN cuerpoThen elseNT cuerpoElse error",
"cuerpoThen : bloqueSentencias",
"elseNT : ELSE",
"cuerpoElse : bloqueSentencias",
"bloqueSentencias : sentenciaEjecutable",
"bloqueSentencias : BEGIN conjuntoSentenciasEjecutables END ';'",
"bloqueSentencias : BEGIN conjuntoSentenciasEjecutables error ';'",
"bloqueSentencias : BEGIN conjuntoSentenciasEjecutables END error",
"sentenciaWhile : whileNT condicionWhile DO bloqueSentencias",
"sentenciaWhile : whileNT condicionWhile error bloqueSentencias",
"condicionWhile : '(' condicion ')'",
"condicionWhile : '(' error ')'",
"condicionWhile : '(' condicion error",
"condicionWhile : error condicion ')'",
"whileNT : WHILE",
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
"factor : SINGLE '(' expresionSimple ')'",
"factor : cte",
"comparador : '>'",
"comparador : '<'",
"comparador : IGUAL",
"comparador : MAYORIGUAL",
"comparador : MENORIGUAL",
"comparador : DISTINTO",
"expresionCompleta : invocacionFuncion",
"expresionCompleta : expresionSimple",
"expresionCompleta : CADENA",
"invocacionFuncion : ID '(' ID ')'",
"invocacionFuncion : ID '(' error ')'",
"invocacionFuncion : ID '(' ID error",
"cte : CTE",
"cte : '-' CTE",
};

//#line 615 "gramatica.y"

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
//#line 783 "Parser.java"
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
//#line 15 "gramatica.y"
{addReglaSintacticaReconocida(String.format("Programa reconocido en linea %1$d",al.getLinea()));}
break;
case 3:
//#line 21 "gramatica.y"
{addErrorSintactico(String.format("Falta BEGIN en linea %1$d",al.getLinea()));}
break;
case 4:
//#line 23 "gramatica.y"
{addErrorSintactico(String.format("Falta END en linea %1$d",al.getLinea()));}
break;
case 11:
//#line 40 "gramatica.y"
{addErrorSintactico(String.format("ErrorFEO en sentencia declarativa en linea %1$d",al.getLinea()));}
break;
case 12:
//#line 42 "gramatica.y"
{addErrorSintactico(String.format("Hay caracteres que sobran en sentencia declarativa en linea %1$d",al.getLinea()));}
break;
case 13:
//#line 47 "gramatica.y"
{addReglaSintacticaReconocida(String.format("Declaracion simple reconocida en linea %1$d",al.getLinea()));}
break;
case 14:
//#line 49 "gramatica.y"
{addErrorSintactico(String.format("Falta un ';' en linea %1$d",al.getLinea()));}
break;
case 15:
//#line 54 "gramatica.y"
{this.ultimoTipo = EntradaTablaSimbolos.INT;}
break;
case 16:
//#line 56 "gramatica.y"
{this.ultimoTipo = EntradaTablaSimbolos.SINGLE;}
break;
case 17:
//#line 61 "gramatica.y"
{String lex = renombrarLexema();
                                                    EntradaTablaSimbolos en = al.getEntrada(lex);
                                                    en.setTipo(this.ultimoTipo);
                                                    }
break;
case 18:
//#line 66 "gramatica.y"
{String lex = renombrarLexema();
                                                    EntradaTablaSimbolos en = al.getEntrada(lex);
                                                    en.setTipo(this.ultimoTipo);
                                                    }
break;
case 19:
//#line 71 "gramatica.y"
{addErrorSintactico(String.format("Falta una ',' en linea %1$d",al.getLinea()));}
break;
case 21:
//#line 81 "gramatica.y"
{addReglaSintacticaReconocida(String.format("Declaracion encabezado funcion reconocida en linea %1$d",al.getLinea()));}
break;
case 22:
//#line 83 "gramatica.y"
{addErrorSintactico(String.format("Falta un '(' en la declaracion del encabezado funcion en linea %1$d",al.getLinea()));}
break;
case 23:
//#line 85 "gramatica.y"
{addErrorSintactico(String.format("Falta un tipo entre los parentesis en la declaracion del encabezado funcion en linea %1$d",al.getLinea()));}
break;
case 24:
//#line 87 "gramatica.y"
{addErrorSintactico(String.format("Falta un ')' en la declaracion del encabezado funcion en linea %1$d",al.getLinea()));}
break;
case 25:
//#line 89 "gramatica.y"
{addErrorSintactico(String.format("Faltan los nombre de las variables en la declaracion del encabezado funcion en linea %1$d",al.getLinea()));}
break;
case 26:
//#line 91 "gramatica.y"
{addErrorSintactico(String.format("Falta un ';' en la declaracion del encabezado funcion en linea %1$d",al.getLinea()));}
break;
case 27:
//#line 97 "gramatica.y"
{addReglaSintacticaReconocida(String.format("Declaracion de funcion reconocida en linea %1$d",al.getLinea()));
                                                                                                                finDeFuncion();}
break;
case 28:
//#line 100 "gramatica.y"
{addErrorSintactico(String.format("Falta un BEGIN en la declaracion de la funcion en linea %1$d",al.getLinea()));}
break;
case 29:
//#line 102 "gramatica.y"
{addErrorSintactico(String.format("Falta un END en la declaracion de la funcion en linea %1$d",al.getLinea()));}
break;
case 30:
//#line 104 "gramatica.y"
{addErrorSintactico(String.format("Falta un ';' en la declaracion de la funcion en linea %1$d",al.getLinea()));}
break;
case 33:
//#line 113 "gramatica.y"
{addErrorSintactico(String.format("Falta un ';' en el fin de la funcion en linea %1$d",al.getLinea()));}
break;
case 34:
//#line 115 "gramatica.y"
{addErrorSintactico(String.format("Falta un ';' en el fin de la funcion en linea %1$d",al.getLinea()));}
break;
case 35:
//#line 117 "gramatica.y"
{addErrorSintactico(String.format("Falta un ';' en el fin de la funcion en linea %1$d",al.getLinea()));}
break;
case 36:
//#line 123 "gramatica.y"
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
               atributos.setUso("nombre de procedimiento");
               atributos.setNaContenidos(0);
           }
           this.ambitoActual = this.ambitoActual + this.ultimoAmbito;
        }
break;
case 37:
//#line 150 "gramatica.y"
{addErrorSintactico(String.format("Falta un '(' en el encabezado de la funcion en linea %1$d",al.getLinea()));}
break;
case 38:
//#line 152 "gramatica.y"
{addErrorSintactico(String.format("Falta el parametro en el encabezado de la funcion en linea %1$d",al.getLinea()));}
break;
case 39:
//#line 154 "gramatica.y"
{addErrorSintactico(String.format("Falta un ')' en el encabezado de la funcion en linea %1$d",al.getLinea()));}
break;
case 41:
//#line 163 "gramatica.y"
{addReglaSintacticaReconocida(String.format("Retorno reconocido en linea %1$d",al.getLinea()));}
break;
case 42:
//#line 165 "gramatica.y"
{addErrorSintactico(String.format("Falta un '(' en el retorno de la funcion en linea %1$d",al.getLinea()));}
break;
case 43:
//#line 167 "gramatica.y"
{addErrorSintactico(String.format("Falta una expresion en el retorno de la funcion en linea %1$d",al.getLinea()));}
break;
case 44:
//#line 169 "gramatica.y"
{addErrorSintactico(String.format("Falta un ')' en el retorno de la funcion en linea %1$d",al.getLinea()));}
break;
case 45:
//#line 174 "gramatica.y"
{addReglaSintacticaReconocida(String.format("Postcondicion reconocida en linea %1$d",al.getLinea()));}
break;
case 46:
//#line 176 "gramatica.y"
{addErrorSintactico(String.format("Falta un ':' en la postcondicion de la funcion en linea %1$d",al.getLinea()));}
break;
case 47:
//#line 178 "gramatica.y"
{addErrorSintactico(String.format("Falta un '(' en la postcondicion de la funcion en linea %1$d",al.getLinea()));}
break;
case 48:
//#line 180 "gramatica.y"
{addErrorSintactico(String.format("Falta la condicion en la postcondicion de la funcion en linea %1$d",al.getLinea()));}
break;
case 49:
//#line 182 "gramatica.y"
{addErrorSintactico(String.format("Falta un ')' en la postcondicion de la funcion en linea %1$d",al.getLinea()));}
break;
case 50:
//#line 184 "gramatica.y"
{addErrorSintactico(String.format("Falta una ',' en la postcondicion de la funcion en linea %1$d",al.getLinea()));}
break;
case 51:
//#line 186 "gramatica.y"
{addErrorSintactico(String.format("Falta la cadena en la postcondicion de la funcion en linea %1$d",al.getLinea()));}
break;
case 58:
//#line 210 "gramatica.y"
{ 
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
									this.addErrorCodigoIntermedio(": La variable '" + val_peek(1).sval + "' en el ámbito actual no fue declarada.'");
  									 this.asignacion = new TercetoOperandos(":=",new TercetoLexema("VariableNoEncontrada"), this.expresion);

								 }
                                 
                            	 this.addTerceto(asignacion);
                            	 this.tipoAsignacion = asignacion.getTipo();
								 this.al.bajaTablaDeSimbolos(val_peek(1).sval);
                              }
break;
case 59:
//#line 236 "gramatica.y"
{addErrorSintactico(String.format("Falta la asignacion en linea %1$d",al.getLinea()));}
break;
case 60:
//#line 238 "gramatica.y"
{addErrorSintactico(String.format("Falta un ';' en linea %1$d",al.getLinea()));}
break;
case 61:
//#line 243 "gramatica.y"
{
                                                                addReglaSintacticaReconocida(String.format("Sentencia if reconocida en linea %1$d",al.getLinea()));                                                                                                      
                                                                TercetoOperandos tercetoBI = (TercetoOperandos) this.pilaTercetos.pop();
                                                                int numeroDestino = this.numeroTercetos + 1;
                                                                tercetoBI.setOperador1(new TercetoPosicion(numeroDestino));
                                                                TercetoOperandos etiqueta = new TercetoOperandos("Label_" + numeroDestino);
                                                                this.addTerceto(etiqueta);
													  
                                                            }
break;
case 62:
//#line 253 "gramatica.y"
{addErrorSintactico(String.format("Falta un ';' en linea %1$d",al.getLinea()));}
break;
case 63:
//#line 258 "gramatica.y"
{	
                                                        TercetoOperandos tercetoBF = new TercetoOperandos("BF", this.TercetoCondicion);
								                    	this.addTerceto(tercetoBF);
                                                        this.pilaTercetos.push(tercetoBF);
                                                    }
break;
case 64:
//#line 264 "gramatica.y"
{addErrorSintactico(String.format("Falta un '(' en el encabezado del if en linea %1$d",al.getLinea()));}
break;
case 65:
//#line 266 "gramatica.y"
{addErrorSintactico(String.format("Falta la condicion en el encabezado del if en linea %1$d",al.getLinea()));}
break;
case 66:
//#line 268 "gramatica.y"
{addErrorSintactico(String.format("Falta un ')' en el encabezado del if en linea %1$d",al.getLinea()));}
break;
case 69:
//#line 276 "gramatica.y"
{addErrorSintactico(String.format("Falta un THEN en el cuerpo del if en linea %1$d",al.getLinea()));}
break;
case 70:
//#line 278 "gramatica.y"
{addErrorSintactico(String.format("Falta un ENDIF en el cuerpo del if en linea %1$d",al.getLinea()));}
break;
case 71:
//#line 280 "gramatica.y"
{addErrorSintactico(String.format("Falta un THEN en el cuerpo del if en linea %1$d",al.getLinea()));}
break;
case 72:
//#line 282 "gramatica.y"
{addErrorSintactico(String.format("Falta un ELSE en el cuerpo del if en linea %1$d",al.getLinea()));}
break;
case 73:
//#line 284 "gramatica.y"
{addErrorSintactico(String.format("Falta un ENDIF en el cuerpo del if en linea %1$d",al.getLinea()));}
break;
case 74:
//#line 288 "gramatica.y"
{
										   TercetoOperandos tercetoBF = (TercetoOperandos) this.pilaTercetos.pop();
										   int numeroDestino = this.numeroTercetos + 2;
										   tercetoBF.setOperador2(new TercetoPosicion(numeroDestino));
										   TercetoOperandos tercetoBI = new TercetoOperandos("BI");
										   this.addTerceto(tercetoBI);
										   this.pilaTercetos.push(tercetoBI);
									   }
break;
case 75:
//#line 299 "gramatica.y"
{
                    int numeroDestino = this.numeroTercetos + 1;
					TercetoOperandos etiqueta = new TercetoOperandos("Label_" + numeroDestino);
					this.addTerceto(etiqueta);
                 }
break;
case 76:
//#line 307 "gramatica.y"
{System.out.println("HOLA");}
break;
case 79:
//#line 314 "gramatica.y"
{addErrorSintactico(String.format("Falta un END en linea %1$d",al.getLinea()));}
break;
case 80:
//#line 316 "gramatica.y"
{addErrorSintactico(String.format("Falta un ';' en linea %1$d",al.getLinea()));}
break;
case 81:
//#line 320 "gramatica.y"
{
                                                                addReglaSintacticaReconocida(String.format("Sentencia while reconocida en linea %1$d",al.getLinea()));
                                                                TercetoOperandos tercetoBF = (TercetoOperandos) this.pilaTercetos.pop();                                                              
                                                                tercetoBF.setOperador2(new TercetoPosicion(this.numeroTercetos + 2));

                                                                TercetoPosicion posicionBI = (TercetoPosicion) this.pilaTercetos.pop();

                                                                TercetoOperandos TercetoBI = new TercetoOperandos("BI", posicionBI);
                                                                this.addTerceto(TercetoBI);

                                                            }
break;
case 82:
//#line 332 "gramatica.y"
{addErrorSintactico(String.format("Falta un DO en la sentencia while en linea %1$d",al.getLinea()));}
break;
case 83:
//#line 336 "gramatica.y"
{
                            TercetoOperandos tercetoBF = new TercetoOperandos("BF", this.TercetoCondicion);
                            this.addTerceto(tercetoBF);
                            this.pilaTercetos.push(tercetoBF);
                         }
break;
case 84:
//#line 342 "gramatica.y"
{addErrorSintactico(String.format("Falta la condicion en la sentencia while en linea %1$d",al.getLinea()));}
break;
case 85:
//#line 344 "gramatica.y"
{addErrorSintactico(String.format("Falta un ')' en la sentencia while en linea %1$d",al.getLinea()));}
break;
case 86:
//#line 346 "gramatica.y"
{addErrorSintactico(String.format("Falta un '(' en la sentencia while en linea %1$d",al.getLinea()));}
break;
case 87:
//#line 351 "gramatica.y"
{
    					int incremento = 1;
    					if(!this.hashDeTercetos.containsKey(this.ambitoActual)){
    						incremento = 2;
    					} 
                        TercetoPosicion posicion = new TercetoPosicion(this.numeroTercetos + incremento);
                        this.pilaTercetos.push(posicion);
                    }
break;
case 88:
//#line 361 "gramatica.y"
{addReglaSintacticaReconocida(String.format("Sentencia print reconocida en linea %1$d",al.getLinea()));}
break;
case 89:
//#line 363 "gramatica.y"
{addErrorSintactico(String.format("Falta un '(' en la sentencia print en linea %1$d",al.getLinea()));}
break;
case 90:
//#line 365 "gramatica.y"
{addErrorSintactico(String.format("Falta la cadena en la sentencia print en linea %1$d",al.getLinea()));}
break;
case 91:
//#line 367 "gramatica.y"
{addErrorSintactico(String.format("Falta un ')' en la sentencia print en linea %1$d",al.getLinea()));}
break;
case 92:
//#line 369 "gramatica.y"
{addErrorSintactico(String.format("Falta un ';' en la sentencia print en linea %1$d",al.getLinea()));}
break;
case 93:
//#line 375 "gramatica.y"
{
                                                    TercetoOperandos ter = new TercetoOperandos("||", this.TercetoCondicion, TercetoSubcondicion );
                                                    this.addTerceto(ter);
                                                    this.TercetoCondicion = ter;
                                                 	   
                                                    }
break;
case 94:
//#line 382 "gramatica.y"
{this.TercetoCondicion = this.TercetoSubcondicion;

                                                    }
break;
case 95:
//#line 389 "gramatica.y"
{
                                                    TercetoOperandos ter = new TercetoOperandos("&&", TercetoSubcondicion, TercetoComparacion );
                                                    this.addTerceto(ter);
                                                    this.TercetoSubcondicion = ter;
                                                }
break;
case 96:
//#line 395 "gramatica.y"
{this.TercetoSubcondicion = this.TercetoComparacion;  }
break;
case 97:
//#line 400 "gramatica.y"
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
                                                         }
break;
case 98:
//#line 417 "gramatica.y"
{this.expresion = crearTercetoOperandos("+", tipoExpresion, this.tipoTermino, this.expresion, this.termino);
        										  if(this.listaExpresiones.size() > 0) {
														this.listaExpresiones.remove(this.listaExpresiones.size() - 1);
												  }
												 
        										  this.listaExpresiones.add(this.expresion);
        							
        											}
break;
case 99:
//#line 426 "gramatica.y"
{this.expresion = crearTercetoOperandos("-", tipoExpresion, this.tipoTermino, this.expresion, this.termino);
                								 if(this.listaExpresiones.size() > 0) {
													 this.listaExpresiones.remove(this.listaExpresiones.size() - 1);
												  }
											  
                                                 this.listaExpresiones.add(this.expresion);
                                                 
        
        }
break;
case 100:
//#line 436 "gramatica.y"
{
                                    this.expresion = this.termino;
                                    this.tipoExpresion.setLength(0);
                                    this.tipoExpresion.append(this.tipoTermino.toString());
                                    
                                    this.listaExpresiones.add(this.expresion);
                            
                                    
                                    
                                   }
break;
case 101:
//#line 453 "gramatica.y"
{this.termino = crearTercetoOperandos("*", this.tipoTermino, this.tipoFactor, this.termino, this.factor);
                              		 if(this.listaTerminos.size() > 0) {
										 this.listaTerminos.remove(this.listaTerminos.size() - 1);
									  }
                                   	
                                   	 this.listaTerminos.add(this.termino);	
                                   	
                                    }
break;
case 102:
//#line 462 "gramatica.y"
{ this.termino = crearTercetoOperandos("/", this.tipoTermino, this.tipoFactor, this.termino, this.factor);
      								  if(this.listaTerminos.size() > 0) {
										 this.listaTerminos.remove(this.listaTerminos.size() - 1);
									  }
                                   	
                                   	  this.listaTerminos.add(this.termino);	
                                    }
break;
case 103:
//#line 470 "gramatica.y"
{this.termino = this.factor;
                                     this.tipoTermino.setLength(0);
                                     this.tipoTermino.append(this.tipoFactor.toString());
                                	 this.listaTerminos.add(this.termino);	
                               
                                    }
break;
case 104:
//#line 481 "gramatica.y"
{
            EntradaTablaSimbolos entrada = al.estaEnTablaSimbolos(val_peek(0).sval + ambitoActual);
			if(entrada != null)
	        {
				String lexema = entrada.getLexema();
				/*entrada.agregarReferencia(this.al.getLineaActual());*/
				this.factor = new TercetoLexema(lexema);
				this.tipoFactor.setLength(0);
				this.tipoFactor.append(entrada.getTipo());
		    }
			else
			  {
				this.addErrorCodigoIntermedio("La variable " + val_peek(0).sval + " en el ámbito " + ambitoActual.substring(ambitoActual.lastIndexOf(".")) + " no fue declarada.");
			  }
			  this.al.bajaTablaDeSimbolos(val_peek(0).sval);
        }
break;
case 105:
//#line 498 "gramatica.y"
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
case 106:
//#line 519 "gramatica.y"
{
                                    this.factor =  new TercetoLexema(this.constante);
                                    this.tipoFactor.setLength(0);
                                    this.tipoFactor.append(al.getEntrada(this.constante).getTipo());
                                }
break;
case 107:
//#line 527 "gramatica.y"
{
					  this.accionSemanticaComparador();
					  this.comparador = ">";
				  }
break;
case 108:
//#line 532 "gramatica.y"
{
					  this.accionSemanticaComparador();
					  this.comparador = "<";
				 }
break;
case 109:
//#line 537 "gramatica.y"
{
							this.accionSemanticaComparador();
							this.comparador = "==";
						}
break;
case 110:
//#line 542 "gramatica.y"
{
								this.accionSemanticaComparador();
								this.comparador = ">=";
							  }
break;
case 111:
//#line 547 "gramatica.y"
{
								this.accionSemanticaComparador();
								this.comparador = "<=";
							  }
break;
case 112:
//#line 552 "gramatica.y"
{
						this.accionSemanticaComparador();
						this.comparador = "!=";
					  }
break;
case 116:
//#line 571 "gramatica.y"
{addReglaSintacticaReconocida(String.format("Invocacion a funcion reconocida en linea %1$d",al.getLinea()));}
break;
case 117:
//#line 573 "gramatica.y"
{addErrorSintactico(String.format("Falta un ID en la invocacion a funcion en linea %1$d",al.getLinea()));}
break;
case 118:
//#line 575 "gramatica.y"
{addErrorSintactico(String.format("Falta un ')' en la invocacion a funcion en linea %1$d",al.getLinea()));}
break;
case 119:
//#line 580 "gramatica.y"
{   EntradaTablaSimbolos entradaTablaSimbolos = al.getEntrada(val_peek(0).sval);
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
break;
case 120:
//#line 594 "gramatica.y"
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
//#line 1563 "Parser.java"
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
