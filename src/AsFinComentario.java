public class AsFinComentario implements AccionSemantica {
    @Override
    public void ejecutar(AnalizadorLexico a) {
        a.setBuffer("");
        a.incPosition();
    }
    @Override
    public String toString() {
        return "AsFinComentario";
    }
}
