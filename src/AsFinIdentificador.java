public class AsFinIdentificador implements AccionSemantica {
    @Override
    public void ejecutar(AnalizadorLexico a) {
        if (a.esPalabraReservada(a.getBuffer())) {
            a.setTokenActual(a.getIDforPalabra(a.getBuffer()));
            a.addListaDeTokens(String.format("Palabra Reservada %2$s (linea %1$d)", a.getLinea(), a.getBuffer()));
        } else {
            //evaluo si es mayor de lo permitido
            String lexema = a.getBuffer();
            if (lexema.length() > 22) {
                a.addListaDeErroresLexicos(String.format("Warning ID demasiado largo en linea: %1$d, el ID queda de 25 caracteres", a.getLinea()));
                lexema = lexema.substring(0,21);
            }
            a.setTokenActual(a.getIDforPalabra("ID"));
            a.addListaDeTokens(String.format("ID (linea %1$d)", a.getLinea()));
            //Si no esta en la tabla de simbolos lo agrego, sino devuelvo referencia
            if (a.estaEnTabla(lexema)) {
                //esta en tabla devuelvo referencia
                a.setEntrada(lexema);
            } else {
                // no esta en tabla, agrega a TS y tambien setea entrada en getToken para darle al parser la referencia
                EntradaTablaSimbolos elementoTS = new EntradaTablaSimbolos(lexema, EntradaTablaSimbolos.IDENTIFICADOR, a.getLinea());
                a.agregarATablaSimbolos(elementoTS);
                a.setEntrada(lexema);
            }
        }
    }
    @Override
    public String toString() {
        return "AsFinIdentificador";
    }
}

