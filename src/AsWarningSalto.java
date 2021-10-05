public class AsWarningSalto implements AccionSemantica {
    @Override
    public void ejecutar(AnalizadorLexico a) {

        a.addListaDeErroresLexicos(String.format("Warning se eliminara el salto de linea de la cadena at linea: %1$d", a.getLinea()));
        a.incPosition();
    }
    @Override
    public String toString() {
        return "AsWarningSalto";
    }
}
