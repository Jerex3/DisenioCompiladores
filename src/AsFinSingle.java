public class AsFinSingle implements AccionSemantica {
    @Override
    public void ejecutar(AnalizadorLexico a) {


        //convierto a compatible con java
        String[] separados;
        String buffer = a.getBuffer();
        double tempDouble;

        //Entra si contiene 'S'
        if (String.valueOf(buffer).contains("S")) {
            separados = String.valueOf(buffer).split("S");  


            if(separados[0].split("\\.").length == 0){
                separados[0] = "0";

            } 
            tempDouble = Double.parseDouble(separados[0]);
            tempDouble = tempDouble * Math.pow(10, Double.parseDouble(separados[1]));
            
        } else {
            tempDouble = Double.parseDouble(buffer);
        }

        if(tempDouble != 0){
            if (tempDouble > AnalizadorLexico.MAX_SINGLE_POSITIVO) {
                a.addWarningLexico(String.format("Warning cte double fuera de rango, se acotara al limite permitido at linea: %1$d", a.getLinea()));
                tempDouble = AnalizadorLexico.MAX_SINGLE_POSITIVO;
            } else if (tempDouble < AnalizadorLexico.MIN_SINGLE_POSITIVO) {
                a.addWarningLexico(String.format("Warning cte double fuera de rango, se acotara al limite permitido at linea: %1$d", a.getLinea()));
                tempDouble = AnalizadorLexico.MIN_SINGLE_POSITIVO;
            }
        }

        a.addListaDeTokens(String.format("CTE SINGLE %2$s (linea %1$d)", a.getLinea(), String.valueOf(tempDouble)));
        a.setTokenActual(a.getIDforPalabra("CTE"));

        if (a.estaEnTabla(String.valueOf(tempDouble))) {
            //esta en tabla devuelvo referencia
            a.setEntrada(String.valueOf(tempDouble));
        } else {
            // no esta en tabla, agrega a TS y tambien setea entrada en getToken para darle al parser la referencia
            EntradaTablaSimbolos elementoTS = new EntradaTablaSimbolos(String.valueOf(tempDouble), EntradaTablaSimbolos.SINGLE, a.getLinea());
            a.agregarATablaSimbolos(elementoTS);
            a.setEntrada(String.valueOf(tempDouble));
        }

    }
    @Override
    public String toString() {
        return "AsFinSingle";
    }
}
