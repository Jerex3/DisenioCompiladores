public class AsFinEntero implements AccionSemantica {
    @Override
    public void ejecutar(AnalizadorLexico a) {
        
        String buffer = a.getBuffer();
        int bufferInteger = Integer.parseInt(buffer);

        if(bufferInteger > AnalizadorLexico.MAX_INT_ABSOLUTO){
            
            a.addWarningLexico(String.format("Warning cte int fuera de rango, se acotara al limite permitido en linea: %1$d", a.getLinea()));
            bufferInteger = AnalizadorLexico.MAX_INT_ABSOLUTO;

        }
        a.addListaDeTokens(String.format("CTE Integer %2$s (linea %1$d)", a.getLinea(), String.valueOf(bufferInteger)));
        a.setTokenActual(a.getIDforPalabra("CTE"));
        
        if(a.estaEnTabla(String.valueOf(bufferInteger))){

            a.setEntrada(String.valueOf(bufferInteger));

        } else {

            EntradaTablaSimbolos elementoTS = new EntradaTablaSimbolos(String.valueOf(bufferInteger), EntradaTablaSimbolos.INT, a.getLinea());
            a.agregarATablaSimbolos(elementoTS);
            a.setEntrada(String.valueOf(bufferInteger));

        }

    }
    @Override
    public String toString() {
        return "AsFinEntero";
    }
}
