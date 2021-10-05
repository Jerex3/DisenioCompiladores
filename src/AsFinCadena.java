public class AsFinCadena implements AccionSemantica {
    @Override
    public void ejecutar(AnalizadorLexico a) {

        a.setTokenActual(a.getIDforPalabra("CADENA"));
        String cadena = a.getBuffer();


        a.addListaDeTokens(String.format("Cadena Caracteres: %2$s (linea %1$d)", a.getLinea(), cadena));

        //Si no esta en la tabla de simbolos lo agrego, sino devuelvo referencia
        if (a.estaEnTabla(cadena)) {
            //esta en tabla devuelvo referencia
            a.setEntrada(a.getEntrada(cadena));
        } else {
            // no esta en tabla, agrega a TS y tambien setea entrada en getToken para darle al parser la referencia
            EntradaTablaSimbolos elementoTS = new EntradaTablaSimbolos(cadena, EntradaTablaSimbolos.CADENA);
            a.agregarATablaSimbolos(elementoTS);
            a.setEntrada(elementoTS);
        }
        a.incPosition();
    }
    @Override
    public String toString() {
        return "AsFinCadena";
    }
}
