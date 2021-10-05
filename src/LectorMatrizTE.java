import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

//Clase usada para generar la matriz de transicion de estados desde un archivo
public class LectorMatrizTE {
    public static int fila = 20;
    public static int columna = 20;
    private static int[][] mTE = new int[columna][fila];

    public LectorMatrizTE(String path) throws IOException {
        //Lee el archivo
        BufferedReader inputReader = new BufferedReader(new FileReader(path));
        int read;
        StringBuilder sourceCode = new StringBuilder();
        while ((read = inputReader.read()) != -1) {
            sourceCode.append((char) read);
        }

        //separa lo leido por tabulaciones y saltos de linea
        sourceCode = new StringBuilder(sourceCode.toString().replace("\r", "\t"));
        sourceCode = new StringBuilder(sourceCode.toString().replace("\n", ""));
        String[] separados = sourceCode.toString().split("\t");
        int cont = 0;

        //genera la matriz de transicion de estados
        for (int i = 0; i < mTE[0].length; i++) {
            for (int j = 0; j < mTE.length; j++) {
                mTE[j][i] = Integer.parseInt(separados[cont]);
                cont++;
            }
        }
    }



    public static int[][] getMatriz() {
        return mTE;
    }
}