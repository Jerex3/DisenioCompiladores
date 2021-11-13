import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

//Clase que se utiliza para moverse dentro del programa fuente
public class Reader {
    private static int actualLine = 1;
    private String sourceCode = "";
    private int position = 1;
    public static final char FIN = (char) 06;

    //Almacena en programa fuente el archivo en forma de String
    public Reader(String path) throws IOException {
        BufferedReader inputReader = new BufferedReader(new FileReader(path));
        int read;
        while ((read = inputReader.read()) != -1) {
            sourceCode += (char) read;
        }
        sourceCode += FIN;
    }

    //retorna la línea actual
    public int getActualLine() {
        return actualLine;
    }

    //retorna el caracter que se esta observando
    public int getCaracter() {
        if(sourceCode.length() > (position -1))
            return sourceCode.charAt(position - 1);
        else return FIN;
    }

    //incrementa la línea
    public void incLinea() {
        actualLine++;
    }

    //incrementa la posicion para observar el caracter
    public void incPosition() {
        position++;
        if((char) getCaracter() == '\n'){
            incLinea();
        }
    }

    public void imprimir(){
        String[] arr = sourceCode.replaceFirst(".$","").split("\n");
        for(int i = 0; i < arr.length; i++){
           System.out.println("Linea " + (i+1) + ": " + arr[i]);
        }
    }

}