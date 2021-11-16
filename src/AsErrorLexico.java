public class AsErrorLexico implements AccionSemantica {
    @Override
    public void ejecutar(AnalizadorLexico a) {
        a.setBuffer("");
        a.addListaDeErroresLexicos(String.format("No se puedo reconocer un token at linea: %1$d", a.getLinea()));
        ACA DEJO UN ERROR, PARA DECIDIR SI ESTO SE CONSIDERA ERROR O WARNINGS,
        DEBIDO A QUE COMO LO ARREGLAMOS PUEDE SER WARNING, PERO NO ESTOY SEGURO
    }

    @Override
    public String toString() {
        return "AsErrorLexico";
    }
}
