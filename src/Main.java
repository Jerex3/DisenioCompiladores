import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {
    	String source = "Fuente.txt";
        Reader read = new Reader(source);
        read.imprimir();

        LectorMatrizTE mte = new LectorMatrizTE("MTE.txt");
        LectorMatrizAS mas = new LectorMatrizAS("MAS.txt");
        Parser parser = new Parser(read);
        parser.yyparse();
        System.out.println();

        //Reglas sintacticas
        ArrayList<String> reglas = parser.getListaDeReglas();
        if(reglas.size() > 0){
            System.out.println("Las reglas sintacticas reconocidas son las siguientes: ");
            for (String reg : reglas){
                System.out.println(reg);
            }
        }
        else System.out.println("No se reconocieron reglas sintacticas.");
        System.out.println();

        //Errores sintacticos
        ArrayList<String> errSintacticos = parser.getListaDeErroresSintacticos();
        if(errSintacticos.size() > 0){
            System.out.println("Los errores sintactos reconocidos son los siguientes: ");
            for (String err : errSintacticos){
                System.out.println(err);
            }
        }
        else System.out.println("No se reconocieron errores sintacticos.");
        System.out.println();

        //Errores codigo intermedio
        ArrayList<String> errCodInt = parser.getErroresCodInt();
        if(errCodInt.size() > 0){
            System.out.println("Los errores en codigo intermedio reconocidos son los siguientes: ");
            for (String err : errCodInt){
                System.out.println(err);
            }
        }
        else System.out.println("No se reconocieron errores en codigo intermedio.");
        System.out.println();

        //Lista de tokens
        ArrayList<String> tokens = parser.getListaDeTokens();
        if(tokens.size() > 0){
            System.out.println("Los tokens reconocidos son los siguientes: ");
            for (String tok : tokens){
                System.out.println(tok);
            }
        }
        else System.out.println("No se reconocieron tokens.");
        System.out.println();

        //Lista de errores lexicos
        ArrayList<String> errLex = parser.getListaDeErroresLexicos();
        if(errLex.size() > 0){
            System.out.println("Los errores lexicos son los siguientes: ");
            for (String err : errLex){
                System.out.println(err);
            }
        }
        else System.out.println("No se reconocieron errores lexicos.");
        System.out.println();

        //Tabla de simbolos
        HashMap<String, EntradaTablaSimbolos> tabla = parser.getTablaSimbolos();
        System.out.println("La tabla de simbolos queda de la siguiente forma: ");
        for(Map.Entry<String, EntradaTablaSimbolos> en : tabla.entrySet()){
            System.out.println("Lexema: " + en.getKey() + " Tipo: " + en.getValue().getTipo());
        }

        //Tercetos
        HashMap<String, ArrayList<TercetoOperandos>> tercetos = parser.getTercetos();
        if(tercetos.size() > 0){
            System.out.println("Los tercetos son los siguientes: ");
            for(Map.Entry<String, ArrayList<TercetoOperandos>> en : tercetos.entrySet()){
                System.out.println("Para el ambito " + en.getKey() + " : ");
                for(TercetoOperandos ter : en.getValue()){
                    System.out.println(ter.toString());
                    System.out.println(ter.getTipo());
                }
            }
        }
        else System.out.println("No se generaron tercetos");
    }
}
