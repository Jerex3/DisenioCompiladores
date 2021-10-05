import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

//Clase que a partir de un archivo genrea la matriz de acciones semánticas
public class LectorMatrizAS {
    public static int fila = 20;
    public static int columna = 20;
    private static AccionSemantica[][] mAS = new AccionSemantica[columna][fila];

    public LectorMatrizAS(String path) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        //lee el archivo indicado en el path
        BufferedReader inputReader = new BufferedReader(new FileReader(path));
        int read;
        StringBuilder sourceCode = new StringBuilder();
        while ((read = inputReader.read()) != -1) {
            sourceCode.append((char) read);
        }

        //divide lo leido separandolo por tabulaciones y saltos de linea
        sourceCode = new StringBuilder(sourceCode.toString().replace("\r", "\t"));
        sourceCode = new StringBuilder(sourceCode.toString().replace("\n", ""));
        String[] separados = sourceCode.toString().split("\t");
        int cont = 0;
        String aux = new String();

        //va generando instancias de las acciones semanticas según lo que se leyó en el archivo
        for (int i = 0; i < mAS[0].length; i++) {
            for (int j = 0; j < mAS.length; j++) {
                aux = String.valueOf(separados[cont]);
                Class temporal = Class.forName(aux);
                AccionSemantica asTemp = (AccionSemantica) temporal.newInstance(); // inicializo una instancia del nombre de temporal
                mAS[j][i] = asTemp;
                cont++;
            }
        }
    }

    public static AccionSemantica[][] getMatriz() {
        return mAS;
    }

}