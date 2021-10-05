public class AsFinSimbolo implements AccionSemantica {
    @Override
    public void ejecutar(AnalizadorLexico a) {
        a.addListaDeTokens(String.format("Simbolo Simple %2$s (linea %1$d)", a.getLinea(), a.getBuffer()));
        String buffer = a.getBuffer();
        a.setTokenActual(a.getIDforPalabra(buffer));
    }
    @Override
    public String toString() {
        return "AsFinSimbolo";
    }
}
