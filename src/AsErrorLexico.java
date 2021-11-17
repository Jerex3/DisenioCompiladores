public class AsErrorLexico implements AccionSemantica {
    @Override
    public void ejecutar(AnalizadorLexico a) {
        a.setBuffer("");
        a.addListaDeErroresLexicos(String.format("No se puedo reconocer un token at linea: %1$d", a.getLinea()));

    }

    @Override
    public String toString() {
        return "AsErrorLexico";
    }
}
