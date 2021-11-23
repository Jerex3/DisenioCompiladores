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
    4,    7,    7,    8,    8,   10,   10,   10,    9,    5,
    5,    5,    5,    6,    6,    6,    6,   13,   13,   13,
   13,   13,   12,   12,   12,   12,   11,   16,   14,   14,
   14,   14,   15,   15,   15,   15,   15,    3,    3,   19,
   19,   19,   19,   20,   20,   21,   21,   24,   25,   25,
   25,   25,   25,   27,   28,   29,   30,   30,   30,   30,
   30,   23,   31,   26,   26,   26,   26,   26,   26,   26,
   26,   26,   32,   22,   18,   18,   33,   33,   34,   17,
   17,   17,   17,   36,   36,   36,   37,   37,   37,   37,
   38,   35,   35,   35,   35,   35,   35,   39,   40,   40,
};
final static short yylen[] = {                            2,
    1,    4,    3,    4,    3,    4,    3,    4,    3,    4,
    1,    1,    1,    2,    2,    2,    3,    2,    2,    3,
    3,    1,    1,    1,    3,    1,    3,    3,    1,    7,
    7,    7,    7,    7,    7,    7,    7,    4,    2,    4,
    4,    2,    6,    6,    5,    5,    1,    2,    4,    4,
    4,    4,    7,    7,    7,    7,    7,    1,    2,    1,
    1,    1,    1,    4,    3,    3,    2,    1,    3,    5,
    3,    3,    5,    1,    1,    1,    1,    4,    4,    4,
    4,    2,    2,    4,    4,    4,    3,    3,    3,    3,
    4,    4,    1,    5,    3,    1,    3,    1,    3,    3,
    3,    1,    1,    3,    3,    1,    1,    3,    1,    1,
    2,    1,    1,    1,    1,    1,    1,    4,    1,    2,
};
final static short yydefred[] = {                         0,
    0,    0,   22,   23,    0,    1,    0,   11,   12,   13,
    0,    0,    0,    0,    0,    0,   29,    0,    0,   24,
    0,    0,    0,   93,    0,    0,   58,   60,   61,   62,
   63,    0,    0,    0,    0,    0,    0,    0,   14,   15,
   16,    0,   19,    0,    0,    0,    0,    0,    0,   21,
    0,    0,  103,    0,    0,  119,    0,    0,  107,    0,
    0,    0,   98,    0,  106,    0,  109,  110,    0,    3,
   59,    0,    0,    0,    0,   83,    0,    0,   77,   82,
    0,    0,    0,    0,    0,    0,    7,    0,    5,   20,
   17,    0,    0,    0,    0,    0,    0,    4,    0,   25,
    0,  111,    0,  120,    0,    0,  115,  116,  114,  117,
    0,    0,  112,  113,    0,    0,   87,    0,    0,    0,
    0,    0,    0,    0,   74,    0,   66,    0,    0,    0,
    0,   90,    6,    8,    0,    2,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   86,
    0,   91,   85,    0,    0,    0,    0,   97,  104,  105,
  108,    0,   64,   71,   72,   75,   69,    0,    0,    0,
    0,   92,   84,    0,    0,   48,   45,    0,    0,    0,
    0,    0,   44,    0,    0,    0,    0,    0,    0,  118,
   94,    0,   76,   81,   80,   79,   78,   26,    0,   43,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   37,
    0,    0,    0,   73,   70,    0,   32,    0,   33,   31,
   30,   35,   36,   34,   50,   51,   52,   49,    0,    0,
   41,   40,   38,   28,   27,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   54,   55,   56,   57,   53,
};
final static short yydgoto[] = {                          5,
    6,   18,   25,    8,    9,   10,   11,   19,   26,  199,
   12,   13,  148,  149,  212,  140,   60,  101,   27,   28,
   29,   30,   31,   32,   75,   33,  124,  168,  192,  125,
   34,   35,   62,   63,  115,   64,   65,   66,   67,   68,
};
final static short yysindex[] = {                      -182,
 -112,  -55,    0,    0,    0,    0,  -89,    0,    0,    0,
  -47, -233, -115, -125, -232,  -55,    0,  -79,   -9,    0,
   31,  -40,   41,    0,  262, -173,    0,    0,    0,    0,
    0, -199, -145,  281,  -38,  179,  298,  317,    0,    0,
    0,   70,    0,   61,   63,  134,   96,  339,  -55,    0,
 -109,  121,    0,  117,  144,    0,  -93,  125,    0,  -39,
  -19,  -78,    0,   75,    0,  121,    0,    0,  -80,    0,
    0,  121,  281,  281,  146,    0,  202,  -55,    0,    0,
  128,   -3,  254,  356, -173,  -37,    0,  368,    0,    0,
    0,  -50,   72,  -91,  209,  -55,  -50,    0,  375,    0,
   -2,    0,  -61,    0,  172,   13,    0,    0,    0,    0,
  -18,  -18,    0,    0,  121,  121,    0,  121,  -18,  -18,
  153,  181,    8,  -36,    0,  -56,    0,  387,  399,  186,
   15,    0,    0,    0,   31,    0,  188,  -50,  -16,  194,
  224,  -33,  375,  375,  226,  273,  -30,   79,  -41,    0,
  274,    0,    0,   75,   75,   50,  -78,    0,    0,    0,
    0,  261,    0,    0,    0,    0,    0,  281,  278,   -7,
   18,    0,    0, -109,  305,    0,    0, -109, -109, -109,
   90, -201,    0,  121,  121,  121,  309,  101,  101,    0,
    0, -215,    0,    0,    0,    0,    0,    0,   42,    0,
   62,   66,   83,  320,  326,  332,  -32,  318,   55,    0,
  -42,  336,   40,    0,    0, -109,    0, -109,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  371,  -26,
    0,    0,    0,    0,    0,  121,  121,  121,   17,   21,
   -4,  374,  377,  381,  -20,  156,  161,  164,  166,  195,
    0,    0,    0,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  169,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  -82,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  237,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    1,    0,    0,    0,    0,    0,
    0,  154,    0,   25,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   97,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  110,    0,    0,  445,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  -28,  159,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  115,    0,    0,    0,    0,    0,    0,  -28,
  196,    0,    0,    0,  472,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   49,   73,  145,  158,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  177,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0, -122,  -94,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
    0,  113,  112,   29,   58,   84,  -14,  464,  372,  282,
    0,    0,  245,    0,  288,  -66,  -46,  129,  448,    0,
    0,    0,    0,    0,    0,    0,  404,    0,    0,  -15,
    0,    0,  369,  379,    0,  331,  337,    0,    0,    0,
};
final static int YYTABLESIZE=671;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         58,
   29,   81,   52,  111,   57,  112,   57,  180,  225,  186,
  111,   43,  112,  238,  103,  230,  103,  189,   80,  121,
  114,  117,  113,  250,  102,  123,   57,  114,   45,  113,
  145,  103,   52,  103,   51,   39,  245,  132,  150,   47,
  214,   29,   29,   29,  215,   29,   39,   29,  100,   50,
  111,  195,  112,  153,  205,  173,   73,  242,   74,   29,
   29,  243,   29,  206,   40,  102,  163,  102,  156,  102,
   52,  175,  101,    1,   39,   40,  197,  137,  139,  142,
   69,    2,  139,  102,  102,  218,  102,    3,    4,  100,
   41,  100,  111,  100,  112,  228,   67,  111,  233,  112,
  217,   41,   94,   40,   51,  218,   72,  100,  100,  218,
  100,  138,    7,  101,   65,  101,  119,  101,   38,   91,
  219,  120,   76,  139,  220,   46,  218,   48,   90,   41,
   14,  101,  101,   42,  101,   97,   15,  207,  208,  209,
   14,  221,   42,   14,    3,    4,   17,   84,   88,   15,
   61,   16,  193,   24,    3,    4,  102,    3,    4,   17,
   99,   39,   17,   82,  141,   57,   36,   22,   24,   57,
   39,   23,   57,   68,   37,   68,   14,   24,    3,    4,
    3,    4,   17,  103,   49,   99,  106,  104,  128,  129,
    3,    4,  122,  161,   96,  111,  118,  112,   95,  165,
   21,   22,  166,  167,  127,   23,  143,  144,   42,  131,
  151,   24,  152,  229,  188,   53,   17,   53,   52,    3,
    4,  162,  179,  164,   17,  185,  172,  134,  174,  237,
   54,   55,   54,   55,  177,  249,  107,  108,  109,  110,
   56,   52,   56,  107,  108,  109,  110,  103,  103,  103,
  103,  244,   54,   55,  116,  176,   29,   29,   29,   29,
   29,   29,   56,   29,  178,   29,  183,   29,   29,  116,
  116,  116,   29,  196,   29,   29,   29,   29,   29,   29,
  102,  102,  102,  102,  102,  102,  116,  102,  116,  102,
  116,  102,  102,   52,  116,  232,  102,  216,  102,  102,
  102,  102,  102,  102,  100,  100,  100,  100,  100,  100,
  227,  100,  184,  100,  190,  100,  100,  216,   92,  191,
  100,  216,  100,  100,  100,  100,  100,  100,  101,  101,
  101,  101,  101,  101,   93,  101,  194,  101,  216,  101,
  101,    3,    4,  187,  101,  200,  101,  101,  101,  101,
  101,  101,   67,   67,  204,   67,   67,   67,  226,   67,
  111,   67,  112,   67,  239,  240,  241,  210,   67,  211,
   65,   65,   20,   65,   65,   65,   53,   65,  222,   65,
  105,   65,   20,  130,  223,   20,   65,  181,  182,   95,
  224,   54,   55,   59,  231,   54,   55,   96,   54,   55,
   99,   56,   99,    3,    4,   56,   59,   85,   56,   96,
  236,   96,   99,   95,   88,   95,   88,  246,   99,   99,
  247,   96,  100,   59,  248,   95,   88,   96,  251,   59,
   47,   95,   46,  252,   83,   22,  253,   59,  254,   23,
   15,  154,  155,   59,    9,   24,   46,   46,    3,    4,
   17,   89,   59,   89,   20,  159,  160,   21,   22,  201,
  202,  203,   23,   89,   83,   22,   85,  255,   24,   23,
   15,   10,   71,   17,   44,   24,  213,  126,    3,    4,
   17,   79,   59,   59,  157,   71,   59,   59,    0,   59,
   59,   59,   18,   18,    0,   71,  158,   18,    0,    0,
   18,    0,    0,   18,    0,    0,   18,   18,   18,   14,
    0,    0,    0,    0,    0,   15,    0,   21,   22,    0,
   79,   79,   23,    3,    4,   17,   70,    0,   24,    0,
    0,   71,    0,   17,    0,   71,   77,   22,    0,    0,
    0,   23,    0,    0,   78,  198,   71,   24,    0,  198,
  198,  198,   17,   86,   22,   59,   59,   59,   23,    0,
    0,    0,   87,    0,   24,    0,    0,    0,    0,   17,
    0,    0,   21,   22,    0,   71,   71,   23,    0,    0,
    0,   89,    0,   24,    0,    0,    0,  234,   17,  235,
   71,   71,    0,    0,   21,   22,    0,    0,    0,   23,
    0,    0,    0,   98,    0,   24,    0,   59,   59,   59,
   17,   21,   22,    0,    0,   79,   23,    0,    0,    0,
  133,    0,   24,  135,   22,    0,    0,   17,   23,    0,
  146,   22,  136,    0,   24,   23,    0,  147,    0,   17,
    0,   24,   21,   22,    0,    0,   17,   23,    0,    0,
    0,  169,    0,   24,  170,   22,    0,    0,   17,   23,
    0,    0,    0,  171,    0,   24,    0,    0,    0,    0,
   17,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         40,
    0,   40,   40,   43,   45,   45,   45,   41,   41,   40,
   43,   59,   45,   40,   43,   58,   45,   59,   34,   66,
   60,   41,   62,   44,    0,   72,   45,   60,  262,   62,
   97,   60,   40,   62,   44,    7,   41,   41,   41,  272,
  256,   41,   42,   43,  260,   45,   18,   47,    0,   59,
   43,   59,   45,   41,  256,   41,  256,   41,  258,   59,
   60,   41,   62,  265,    7,   41,   59,   43,  115,   45,
   40,  138,    0,  256,   46,   18,   59,   92,   93,   94,
   40,  264,   97,   59,   60,   44,   62,  270,  271,   41,
    7,   43,   43,   45,   45,   41,    0,   43,   59,   45,
   59,   18,   40,   46,   44,   44,  280,   59,   60,   44,
   62,   40,    0,   41,    0,   43,   42,   45,    7,   59,
   59,   47,  268,  138,   59,   13,   44,   16,   59,   46,
  256,   59,   60,  256,   62,   40,  262,  184,  185,  186,
  256,   59,  265,  256,  270,  271,  272,   36,   37,  262,
   22,  264,  168,   44,  270,  271,   40,  270,  271,  272,
   49,  256,  272,   35,  256,   45,  256,  257,   59,   45,
  265,  261,   45,  256,  264,  258,  256,  267,  270,  271,
  270,  271,  272,   40,  264,   41,   58,  281,   77,   78,
  270,  271,  273,   41,   41,   43,  275,   45,   41,  256,
  256,  257,  259,  260,   59,  261,   95,   96,  256,   81,
  272,  267,   41,  256,  256,  256,  272,  256,   40,  270,
  271,   41,  256,  260,  272,  256,   41,  265,   41,  256,
  271,  272,  271,  272,   41,  256,  276,  277,  278,  279,
  281,   40,  281,  276,  277,  278,  279,  276,  277,  278,
  279,  256,  271,  272,  274,  272,  256,  257,  258,  259,
  260,  261,  281,  263,   41,  265,   41,  267,  268,  274,
  274,  274,  272,  256,  274,  275,  276,  277,  278,  279,
  256,  257,  258,  259,  260,  261,  274,  263,  274,  265,
  274,  267,  268,   40,  274,  256,  272,  256,  274,  275,
  276,  277,  278,  279,  256,  257,  258,  259,  260,  261,
  256,  263,   40,  265,   41,  267,  268,  256,  256,   59,
  272,  256,  274,  275,  276,  277,  278,  279,  256,  257,
  258,  259,  260,  261,  272,  263,   59,  265,  256,  267,
  268,  270,  271,  265,  272,   41,  274,  275,  276,  277,
  278,  279,  256,  257,  265,  259,  260,  261,   41,  263,
   43,  265,   45,  267,  236,  237,  238,   59,  272,  269,
  256,  257,    1,  259,  260,  261,  256,  263,   59,  265,
  256,  267,   11,  256,   59,   14,  272,  143,  144,  256,
   59,  271,  272,   22,   59,  271,  272,  264,  271,  272,
  256,  281,  258,  270,  271,  281,   35,   36,  281,  256,
   40,  258,  268,  256,  256,  258,  258,   44,  274,  275,
   44,  268,   51,   52,   44,  268,  268,  274,  273,   58,
  262,  274,  256,  273,  256,  257,  273,   66,  273,  261,
  262,  111,  112,   72,    0,  267,  270,  271,  270,  271,
  272,  256,   81,  258,   83,  119,  120,  256,  257,  178,
  179,  180,  261,  268,  256,  257,   95,  273,  267,  261,
  262,    0,   25,  272,   11,  267,  189,   74,  270,  271,
  272,   34,  111,  112,  116,   38,  115,  116,   -1,  118,
  119,  120,  256,  257,   -1,   48,  118,  261,   -1,   -1,
  264,   -1,   -1,  267,   -1,   -1,  270,  271,  272,  256,
   -1,   -1,   -1,   -1,   -1,  262,   -1,  256,  257,   -1,
   73,   74,  261,  270,  271,  272,  265,   -1,  267,   -1,
   -1,   84,   -1,  272,   -1,   88,  256,  257,   -1,   -1,
   -1,  261,   -1,   -1,  264,  174,   99,  267,   -1,  178,
  179,  180,  272,  256,  257,  184,  185,  186,  261,   -1,
   -1,   -1,  265,   -1,  267,   -1,   -1,   -1,   -1,  272,
   -1,   -1,  256,  257,   -1,  128,  129,  261,   -1,   -1,
   -1,  265,   -1,  267,   -1,   -1,   -1,  216,  272,  218,
  143,  144,   -1,   -1,  256,  257,   -1,   -1,   -1,  261,
   -1,   -1,   -1,  265,   -1,  267,   -1,  236,  237,  238,
  272,  256,  257,   -1,   -1,  168,  261,   -1,   -1,   -1,
  265,   -1,  267,  256,  257,   -1,   -1,  272,  261,   -1,
  256,  257,  265,   -1,  267,  261,   -1,  263,   -1,  272,
   -1,  267,  256,  257,   -1,   -1,  272,  261,   -1,   -1,
   -1,  265,   -1,  267,  256,  257,   -1,   -1,  272,  261,
   -1,   -1,   -1,  265,   -1,  267,   -1,   -1,   -1,   -1,
  272,
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
"listaVariablesFuncion : listaVariablesFuncion error identificador",
"identificador : ID",
"declaracionEncabezadoFuncion : tipoFuncion FUNC '(' tipo ')' listaVariablesFuncion ';'",
"declaracionEncabezadoFuncion : tipoFuncion FUNC '(' tipo error listaVariablesFuncion ';'",
"declaracionEncabezadoFuncion : tipoFuncion FUNC error tipo ')' listaVariablesFuncion ';'",
"declaracionEncabezadoFuncion : tipoFuncion FUNC '(' error ')' listaVariablesFuncion ';'",
"declaracionFuncion : encabezadoFuncion sentenciaDeclarativa BEGIN conjuntoSentenciasEjecutables finFuncion END ';'",
"declaracionFuncion : encabezadoFuncion sentenciaDeclarativa error conjuntoSentenciasEjecutables finFuncion END ';'",
"declaracionFuncion : encabezadoFuncion sentenciaDeclarativa BEGIN conjuntoSentenciasEjecutables finFuncion error ';'",
"declaracionFuncion : error sentenciaDeclarativa BEGIN conjuntoSentenciasEjecutables finFuncion END ';'",
"finFuncion : retorno ';' postcondicion ';'",
"finFuncion : retorno ';'",
"finFuncion : retorno ';' postcondicion error",
"finFuncion : retorno error postcondicion ';'",
"finFuncion : retorno error",
"encabezadoFuncion : tipoFuncion FUNC ID '(' parametro ')'",
"encabezadoFuncion : error FUNC ID '(' parametro ')'",
"encabezadoFuncion : tipoFuncion FUNC ID parametro ')'",
"encabezadoFuncion : tipoFuncion FUNC ID '(' parametro",
"tipoFuncion : tipo",
"parametro : tipo ID",
"retorno : RETURN '(' expresionSimple ')'",
"retorno : error '(' expresionSimple ')'",
"retorno : RETURN error expresionSimple ')'",
"retorno : RETURN '(' expresionSimple error",
"postcondicion : POST ':' '(' condicion ')' ',' CADENA",
"postcondicion : POST error '(' condicion ')' ',' CADENA",
"postcondicion : POST ':' error condicion ')' ',' CADENA",
"postcondicion : POST ':' '(' condicion error ',' CADENA",
"postcondicion : POST ':' '(' condicion ')' error CADENA",
"conjuntoSentenciasEjecutables : sentenciaEjecutable",
"conjuntoSentenciasEjecutables : conjuntoSentenciasEjecutables sentenciaEjecutable",
"sentenciaEjecutable : asignacion",
"sentenciaEjecutable : sentenciaIf",
"sentenciaEjecutable : sentenciaPrint",
"sentenciaEjecutable : sentenciaWhile",
"asignacion : identificador ASIGNACION expresionSimple ';'",
"asignacion : identificador ASIGNACION expresionSimple",
"sentenciaIf : encabezadoIf cuerpoIf ';'",
"sentenciaIf : encabezadoIf cuerpoIf",
"encabezadoIf : estructuraDeControl",
"cuerpoIf : THEN cuerpoThen ENDIF",
"cuerpoIf : THEN cuerpoThen elseNT cuerpoElse ENDIF",
"cuerpoIf : error cuerpoThen ENDIF",
"cuerpoIf : THEN cuerpoThen error",
"cuerpoIf : THEN cuerpoThen elseNT cuerpoElse error",
"cuerpoThen : bloqueSentencias",
"elseNT : ELSE",
"cuerpoElse : bloqueSentencias",
"bloqueSentencias : sentenciaEjecutable",
"bloqueSentencias : BEGIN conjuntoSentenciasEjecutables END ';'",
"bloqueSentencias : BEGIN conjuntoSentenciasEjecutables END error",
"bloqueSentencias : BEGIN conjuntoSentenciasEjecutables error ';'",
"bloqueSentencias : error conjuntoSentenciasEjecutables END ';'",
"sentenciaWhile : encabezadoWhile bloqueSentencias",
"encabezadoWhile : estructuraDeControl DO",
"estructuraDeControl : whileNT '(' condicion ')'",
"estructuraDeControl : IF '(' condicion ')'",
"estructuraDeControl : error '(' condicion ')'",
"estructuraDeControl : IF condicion ')'",
"estructuraDeControl : IF '(' condicion",
"estructuraDeControl : whileNT '(' condicion",
"estructuraDeControl : whileNT condicion ')'",
"estructuraDeControl : IF '(' error ')'",
"estructuraDeControl : whileNT '(' error ')'",
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
"expresionSimple : error",
"termino : termino '*' factor",
"termino : termino '/' factor",
"termino : factor",
"factor : identificador",
"factor : inicioCasteo expresionSimple ')'",
"factor : invocacionFuncion",
"factor : cte",
"inicioCasteo : SINGLE '('",
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

//#line 1042 "gramatica2.y"

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
	private boolean creandoCasteo = false;
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
	
	private String estructuraActual;
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
  private void postCondicion(String cadena){
  	TercetoOperandos print = new TercetoOperandos("print", new TercetoLexema(cadena));
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
//#line 879 "Parser.java"
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
//#line 106 "gramatica2.y"
{ 
        												this.listaVariablesFuncion = new ArrayList<String>();
        												this.listaVariablesFuncion.add(val_peek(0).sval);
                                                    }
break;
case 27:
//#line 111 "gramatica2.y"
{
        													this.listaVariablesFuncion.add(val_peek(0).sval);
                                                    		
                                                    	 }
break;
case 28:
//#line 116 "gramatica2.y"
{addErrorSintactico(String.format("Falta , entre identificadores en linea %1$d",al.getLinea()));}
break;
case 30:
//#line 128 "gramatica2.y"
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
case 31:
//#line 139 "gramatica2.y"
{addErrorSintactico("falta un )" + " en la declaracion de encabezado de funcion en linea " + al.getLinea());}
break;
case 32:
//#line 141 "gramatica2.y"
{addErrorSintactico("falta un (" + " en la declaracion de encabezado de funcion en linea " + al.getLinea());}
break;
case 33:
//#line 143 "gramatica2.y"
{addErrorSintactico("Falta tipo o es incorrecto el tipo " + " en la declaracion de encabezado de funcion en linea " + al.getLinea());}
break;
case 34:
//#line 149 "gramatica2.y"
{addReglaSintacticaReconocida(String.format("Declaracion de funcion reconocida en linea %1$d",al.getLinea()));
                                                                                                                finDeFuncion();}
break;
case 35:
//#line 152 "gramatica2.y"
{addErrorSintactico("Falta BEGIN o esta mal escrito, en declaracion de funcion " + this.ambitoActual + " en linea " + al.getLinea() );}
break;
case 36:
//#line 154 "gramatica2.y"
{addErrorSintactico("Falta END o esta mal escrito, en declaracion de funcion " + this.ambitoActual + " en linea " + al.getLinea() );}
break;
case 37:
//#line 156 "gramatica2.y"
{addErrorSintactico("WTFSXD");}
break;
case 38:
//#line 161 "gramatica2.y"
{
        											TercetoOperandos tercetoBI = (TercetoOperandos) this.pilaTercetoFinFuncion.pop();
        											TercetoOperandos etiqueta = new TercetoOperandos("Label_" + (this.numeroTercetos + 1));
        											TercetoOperandos finFuncion = new TercetoOperandos("finfuncion");
        											tercetoBI.setOperador1(etiqueta);
        											this.addTerceto(etiqueta);       										
        											this.addTerceto(finFuncion);
        										}
break;
case 39:
//#line 170 "gramatica2.y"
{
       												TercetoOperandos finFuncion = new TercetoOperandos("finfuncion");
        											this.addTerceto(finFuncion);
        										}
break;
case 40:
//#line 175 "gramatica2.y"
{addErrorSintactico("Falta ; en linea " + al.getLinea());}
break;
case 41:
//#line 177 "gramatica2.y"
{addErrorSintactico("Falta ; en linea " + al.getLinea());}
break;
case 42:
//#line 179 "gramatica2.y"
{addErrorSintactico("Falta ; en linea " + al.getLinea());}
break;
case 43:
//#line 186 "gramatica2.y"
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
case 44:
//#line 221 "gramatica2.y"
{
           addErrorSintactico("Falta tipo en declaracion de funcion " + val_peek(3).sval);
           addWarningSintactico("El tipo de la funcion " + val_peek(3).sval + " no fue correctamente declarado, asi que se usara SINGLE, pero no sera posible generar codigo ya que esto es un error.");
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
               atributos.setTipo(EntradaTablaSimbolos.SINGLE);
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
case 45:
//#line 258 "gramatica2.y"
{
           addErrorSintactico("Falta ( en declaracion de funcion ");
           String lexema;
           EntradaTablaSimbolos esRedeclarada = this.al.esRedeclarada(val_peek(2).sval + this.ambitoActual);
           if(esRedeclarada != null)
           {
               this.addErrorCodigoIntermedio("Funcion " + val_peek(2).sval + " redeclarada. La funcion ya fue declarada en la linea: " + esRedeclarada.getLineaDeclaracion() + ".");
               lexema = "#REDECLARADO" + val_peek(2).sval + this.ambitoActual;
               this.ultimoAmbito = "." + "#REDECLARADO" + val_peek(2).sval;
               this.ultimaFuncion = lexema;
           }
           else
           {
               lexema = val_peek(2).sval + this.ambitoActual;
               this.ultimoAmbito = "." + val_peek(2).sval;
               this.ultimaFuncion = val_peek(2).sval + this.ambitoActual;
           }
           this.al.cambiarClave(val_peek(2).sval, lexema);
           if(this.al.estaEnTabla(lexema))
           {
               EntradaTablaSimbolos atributos = this.al.getEntrada(lexema);
               atributos.setUso("funcion");
               atributos.setTipo(EntradaTablaSimbolos.SINGLE);
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
case 46:
//#line 294 "gramatica2.y"
{
           addErrorSintactico("Falta ) en declaracion de funcion ");
           String lexema;
           EntradaTablaSimbolos esRedeclarada = this.al.esRedeclarada(val_peek(2).sval + this.ambitoActual);
           if(esRedeclarada != null)
           {
               this.addErrorCodigoIntermedio("Funcion " + val_peek(2).sval + " redeclarada. La funcion ya fue declarada en la linea: " + esRedeclarada.getLineaDeclaracion() + ".");
               lexema = "#REDECLARADO" + val_peek(2).sval + this.ambitoActual;
               this.ultimoAmbito = "." + "#REDECLARADO" + val_peek(2).sval;
               this.ultimaFuncion = lexema;
           }
           else
           {
               lexema = val_peek(2).sval + this.ambitoActual;
               this.ultimoAmbito = "." + val_peek(2).sval;
               this.ultimaFuncion = val_peek(2).sval + this.ambitoActual;
           }
           this.al.cambiarClave(val_peek(2).sval, lexema);
           if(this.al.estaEnTabla(lexema))
           {
               EntradaTablaSimbolos atributos = this.al.getEntrada(lexema);
               atributos.setUso("funcion");
               atributos.setTipo(EntradaTablaSimbolos.SINGLE);
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
case 47:
//#line 334 "gramatica2.y"
{this.ultimoTipoFuncion = this.ultimoTipo;}
break;
case 48:
//#line 337 "gramatica2.y"
{
        			this.tipoParametro = this.ultimoTipo;
        			this.lexemaParametro = val_peek(0).sval;
        		}
break;
case 49:
//#line 344 "gramatica2.y"
{
									        	addReglaSintacticaReconocida(String.format("Retorno reconocido en linea %1$d",al.getLinea()));
									        	String lexemaFuncionActual = this.ambitoActual.substring(this.ambitoActual.lastIndexOf(".") + 1, this.ambitoActual.length()) +  this.ambitoActual.substring(0, this.ambitoActual.lastIndexOf("."));
									        	String tipoFuncionActual = al.getEntrada(lexemaFuncionActual).getTipo();
									        	if(!tipoFuncionActual.equals(this.tipoExpresion.toString())){
									        		this.addErrorCodigoIntermedio("El tipo de la funcion" + lexemaFuncionActual + " es " + tipoFuncionActual + " y se intenta retornar " + this.tipoExpresion);
									        	}
									        	TercetoOperandos tercetoRetorno = new TercetoOperandos("retorno", this.expresion);
									        	this.addTerceto(tercetoRetorno);
									        }
break;
case 50:
//#line 355 "gramatica2.y"
{addErrorSintactico("Falta o esta mal escrita palabra reservada RETURN en linea " + al.getLinea());}
break;
case 51:
//#line 357 "gramatica2.y"
{addErrorSintactico("Falta ( en linea " + al.getLinea());}
break;
case 52:
//#line 359 "gramatica2.y"
{addErrorSintactico("Falta ) en linea " + al.getLinea());}
break;
case 53:
//#line 365 "gramatica2.y"
{
    														addReglaSintacticaReconocida(String.format("Postcondicion reconocida en linea %1$d",al.getLinea()));
    														postCondicion(val_peek(0).sval);
    														
    													}
break;
case 54:
//#line 371 "gramatica2.y"
{addErrorSintactico("Falta : en linea " + al.getLinea());
												postCondicion(val_peek(0).sval);	
												}
break;
case 55:
//#line 375 "gramatica2.y"
{addErrorSintactico("Falta : en linea " + al.getLinea());
												postCondicion(val_peek(0).sval);	
												}
break;
case 56:
//#line 379 "gramatica2.y"
{addErrorSintactico("Falta : en linea " + al.getLinea());
												postCondicion(val_peek(0).sval);	
												}
break;
case 57:
//#line 383 "gramatica2.y"
{addErrorSintactico("Falta : en linea " + al.getLinea());
												postCondicion(val_peek(0).sval);	
												}
break;
case 64:
//#line 410 "gramatica2.y"
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
case 65:
//#line 471 "gramatica2.y"
{addErrorSintactico("Falta ; en la asignacion en linea " + (al.getLinea() - 1)); if(hayFunc){
                                    hayFunc = false;
                                    EntradaTablaSimbolos entFun = al.getEntrada(lexemaVarFunc);

                                     EntradaTablaSimbolos estaEnTablaSimbolos = this.al.estaEnTablaSimbolos(val_peek(2).sval + this.ambitoActual);
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
                                        this.addErrorCodigoIntermedio(": La variable '" + val_peek(2).sval + "' en el ámbito actual no fue declarada.'");
                                         this.asignacion = new TercetoOperandos(":=",new TercetoLexema("VariableNoEncontrada"), this.expresion);

                                     }

                                     this.addTerceto(asignacion);
                                     this.tipoAsignacion = asignacion.getTipo();
                                     this.al.bajaTablaDeSimbolos(val_peek(2).sval);

                                }

                                else{
                                     EntradaTablaSimbolos estaEnTablaSimbolos = this.al.estaEnTablaSimbolos(val_peek(2).sval + this.ambitoActual);
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
                                        this.addErrorCodigoIntermedio(": La variable '" + val_peek(2).sval + "' en el ámbito actual no fue declarada.'");
                                         this.asignacion = new TercetoOperandos(":=",new TercetoLexema("VariableNoEncontrada"), this.expresion);

                                     }

                                     this.addTerceto(asignacion);
                                     this.tipoAsignacion = asignacion.getTipo();
                                     this.al.bajaTablaDeSimbolos(val_peek(2).sval);
                                 }}
break;
case 66:
//#line 533 "gramatica2.y"
{
                                                                addReglaSintacticaReconocida(String.format("Sentencia if reconocida en linea %1$d",al.getLinea()));                                                                                                      
                                                                TercetoOperandos tercetoBI = (TercetoOperandos) this.pilaTercetos.pop();
                                                                int numeroDestino = this.numeroTercetos + 1;
                                                                tercetoBI.setOperador1(new TercetoPosicion(numeroDestino));
                                                                TercetoOperandos etiqueta = new TercetoOperandos("Label_" + numeroDestino);
                                                                this.addTerceto(etiqueta);
													  
                                                            }
break;
case 67:
//#line 543 "gramatica2.y"
{addErrorSintactico("Falta ; al final de la sentencia IF, en linea " + al.getLinea());}
break;
case 68:
//#line 549 "gramatica2.y"
{if(!estructuraActual.equals("IF")) addErrorSintactico("Falta o esta mal escrita la palabra reservada IF  en linea " + al.getLinea());}
break;
case 71:
//#line 557 "gramatica2.y"
{addErrorSintactico("Falta o esta mal escrita palabra reservada THEN en cuerpo del IF");}
break;
case 72:
//#line 559 "gramatica2.y"
{addErrorSintactico("Falta o esta mal escrita palabra reservada ENDIF en cuerpo del IF");}
break;
case 73:
//#line 561 "gramatica2.y"
{addErrorSintactico("Falta o esta mal escrita palabra reservada ENDIF en cuerpo del IF");}
break;
case 74:
//#line 566 "gramatica2.y"
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
//#line 577 "gramatica2.y"
{
                    int numeroDestino = this.numeroTercetos + 1;
					TercetoOperandos etiqueta = new TercetoOperandos("Label_" + numeroDestino);
					this.addTerceto(etiqueta);
                 }
break;
case 79:
//#line 593 "gramatica2.y"
{addErrorSintactico("Falta ; al final del bloque de sentencias en linea " + al.getLinea());}
break;
case 80:
//#line 595 "gramatica2.y"
{addErrorSintactico("Falta o esta mal escrito END al final del bloque de sentencias en linea " + al.getLinea());}
break;
case 81:
//#line 597 "gramatica2.y"
{addErrorSintactico("Falta o esta mal escrito BEGIN al principio del bloque de sentencias de la linea " + al.getLinea());}
break;
case 82:
//#line 602 "gramatica2.y"
{
                                                                addReglaSintacticaReconocida(String.format("Sentencia while reconocida en linea %1$d",al.getLinea()));
                                                                TercetoOperandos tercetoBF = (TercetoOperandos) this.pilaTercetos.pop();   
                                                                
                                                             	
                                                                
                                                                int numeroDestino = this.numeroTercetos + 2;                                	                          
                                                                tercetoBF.setOperador2(new TercetoPosicion(numeroDestino));
																
																if(pilaTercetos.size() > 0) {
																 TercetoPosicion posicionBI = (TercetoPosicion) this.pilaTercetos.pop();
																 TercetoOperandos TercetoBI = new TercetoOperandos("BI", posicionBI);
                                                                 this.addTerceto(TercetoBI);
                                                                 
																}
																
                                                                
                                                                TercetoOperandos etiqueta = new TercetoOperandos("Label_" + numeroDestino);
                                                                this.addTerceto(etiqueta);

                                                            }
break;
case 83:
//#line 627 "gramatica2.y"
{if(!estructuraActual.equals("WHILE")) addErrorSintactico("Falta o esta mal escrita la palabra reservada WHILE en linea " + al.getLinea());}
break;
case 84:
//#line 632 "gramatica2.y"
{
							 		estructuraActual = "WHILE";
					    			TercetoOperandos tercetoBF = new TercetoOperandos("BF", this.TercetoCondicion);
					                this.addTerceto(tercetoBF);
					                this.pilaTercetos.push(tercetoBF);
				        		}
break;
case 85:
//#line 639 "gramatica2.y"
{ 
									estructuraActual = "IF";
									TercetoOperandos tercetoBF = new TercetoOperandos("BF", this.TercetoCondicion);
			                    	this.addTerceto(tercetoBF);
                                    this.pilaTercetos.push(tercetoBF);
								}
break;
case 86:
//#line 646 "gramatica2.y"
{				
									estructuraActual = "";					
									TercetoOperandos tercetoBF = new TercetoOperandos("BF", this.TercetoCondicion);
			                    	this.addTerceto(tercetoBF);
                                    this.pilaTercetos.push(tercetoBF);
								}
break;
case 87:
//#line 653 "gramatica2.y"
{
									estructuraActual = "IF";					
									TercetoOperandos tercetoBF = new TercetoOperandos("BF", this.TercetoCondicion);
			                    	this.addTerceto(tercetoBF);
                                    this.pilaTercetos.push(tercetoBF);
                                    addErrorSintactico("Falta ( en condicion de linea " + al.getLinea());
								}
break;
case 88:
//#line 661 "gramatica2.y"
{
							estructuraActual = "IF";					
							TercetoOperandos tercetoBF = new TercetoOperandos("BF", this.TercetoCondicion);
	                    	this.addTerceto(tercetoBF);
                            this.pilaTercetos.push(tercetoBF);
                            addErrorSintactico("Falta ) en condicion de linea " + al.getLinea());
						}
break;
case 89:
//#line 669 "gramatica2.y"
{
									estructuraActual = "WHILE";					
									TercetoOperandos tercetoBF = new TercetoOperandos("BF", this.TercetoCondicion);
			                    	this.addTerceto(tercetoBF);
                                    this.pilaTercetos.push(tercetoBF);
                                    addErrorSintactico("Falta ) en condicion de linea " + al.getLinea());
								}
break;
case 90:
//#line 677 "gramatica2.y"
{
									estructuraActual = "WHILE";					
									TercetoOperandos tercetoBF = new TercetoOperandos("BF", this.TercetoCondicion);
			                    	this.addTerceto(tercetoBF);
                                    this.pilaTercetos.push(tercetoBF);
                                    addErrorSintactico("Falta ( en condicion de linea " + al.getLinea());
								}
break;
case 91:
//#line 685 "gramatica2.y"
{
									estructuraActual = "IF";					
									TercetoOperandos tercetoBF = new TercetoOperandos("BF", this.TercetoCondicion);
			                    	this.addTerceto(tercetoBF);
                                    this.pilaTercetos.push(tercetoBF);
                                    /*addErrorSintactico();*/
								}
break;
case 92:
//#line 693 "gramatica2.y"
{
									estructuraActual = "WHILE";					
									TercetoOperandos tercetoBF = new TercetoOperandos("BF", this.TercetoCondicion);
			                    	this.addTerceto(tercetoBF);
                                    this.pilaTercetos.push(tercetoBF);
                                    /*addErrorSintactico("Falta ( en condicion de linea " + al.getLinea());*/
								}
break;
case 93:
//#line 705 "gramatica2.y"
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
case 94:
//#line 720 "gramatica2.y"
{
													        addReglaSintacticaReconocida(String.format("Sentencia print reconocida en linea %1$d",al.getLinea()));
													        TercetoOperandos terPrint = new TercetoOperandos("print", new TercetoLexema(val_peek(2).sval));
													        this.addTerceto(terPrint);
													       }
break;
case 95:
//#line 731 "gramatica2.y"
{
                                                    TercetoOperandos ter = new TercetoOperandos("||", this.TercetoCondicion, TercetoSubcondicion );
                                                    this.addTerceto(ter);
                                                    this.TercetoCondicion = ter;
                                                 	   
                                                    }
break;
case 96:
//#line 738 "gramatica2.y"
{this.TercetoCondicion = this.TercetoSubcondicion;

                                                    }
break;
case 97:
//#line 746 "gramatica2.y"
{
                                                    TercetoOperandos ter = new TercetoOperandos("&&", TercetoSubcondicion, TercetoComparacion );
                                                    this.addTerceto(ter);
                                                    this.TercetoSubcondicion = ter;
                                                }
break;
case 98:
//#line 752 "gramatica2.y"
{this.TercetoSubcondicion = this.TercetoComparacion;  }
break;
case 99:
//#line 757 "gramatica2.y"
{
                                                      TercetoOperandos ter = new TercetoOperandos(this.comparador, this.expresionPreComparador, this.expresion );
                                                            if(!this.tipoExpresionPreComparador.toString().equals(this.tipoExpresion.toString())){
                                                                listaErroresCodigoIntermedio.add("No es posible realizar una comparacion entre dos tipos distintos en linea " + al.getLinea());
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
case 100:
//#line 780 "gramatica2.y"
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
case 101:
//#line 792 "gramatica2.y"
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
case 102:
//#line 805 "gramatica2.y"
{
                                    this.expresion = this.termino;
                                    this.tipoExpresion.setLength(0);
                                    this.tipoExpresion.append(this.tipoTermino.toString());
                                    
                                    this.listaExpresiones.add(this.expresion);
                            
                                    
                                    
                                   }
break;
case 103:
//#line 816 "gramatica2.y"
{addErrorSintactico("Error en la expresion en linea " + al.getLinea());
       								this.expresion = this.termino;
                                    this.tipoExpresion.setLength(0);
                                    this.tipoExpresion.append(this.tipoTermino.toString());
                                    
                                    this.listaExpresiones.add(this.expresion);}
break;
case 104:
//#line 829 "gramatica2.y"
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
case 105:
//#line 843 "gramatica2.y"
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
case 106:
//#line 857 "gramatica2.y"
{this.termino = this.factor;
                                     this.tipoTermino.setLength(0);
                                     this.tipoTermino.append(this.tipoFactor.toString());
                                	 this.listaTerminos.add(this.termino);	
                               
                                    }
break;
case 107:
//#line 868 "gramatica2.y"
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
case 108:
//#line 887 "gramatica2.y"
{
        
                		 this.creandoCasteo = false;     
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
case 109:
//#line 910 "gramatica2.y"
{}
break;
case 110:
//#line 912 "gramatica2.y"
{
                                    this.factor =  new TercetoLexema(this.constante);
                                    this.tipoFactor.setLength(0);
                                    this.tipoFactor.append(al.getEntrada(this.constante).getTipo());
                                }
break;
case 111:
//#line 921 "gramatica2.y"
{
    										if(creandoCasteo){
    											addErrorCodigoIntermedio("No es posible realizar una anidacion de casteos");
    										} else {
    										  creandoCasteo = true;
    										
    										}	
    										
    							
    									}
break;
case 112:
//#line 932 "gramatica2.y"
{
					  this.accionSemanticaComparador();
					  this.comparador = ">";
				  }
break;
case 113:
//#line 937 "gramatica2.y"
{
					  this.accionSemanticaComparador();
					  this.comparador = "<";
				 }
break;
case 114:
//#line 942 "gramatica2.y"
{
							this.accionSemanticaComparador();
							this.comparador = "==";
						}
break;
case 115:
//#line 947 "gramatica2.y"
{
								this.accionSemanticaComparador();
								this.comparador = ">=";
							  }
break;
case 116:
//#line 952 "gramatica2.y"
{
								this.accionSemanticaComparador();
								this.comparador = "<=";
							  }
break;
case 117:
//#line 957 "gramatica2.y"
{
						this.accionSemanticaComparador();
						this.comparador = "!=";
					  }
break;
case 118:
//#line 970 "gramatica2.y"
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
case 119:
//#line 1007 "gramatica2.y"
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
case 120:
//#line 1021 "gramatica2.y"
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
//#line 2059 "Parser.java"
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
