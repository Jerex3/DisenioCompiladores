public class AsOmitir implements AccionSemantica {
    @Override
    public void ejecutar(AnalizadorLexico a) {
        a.incPosition();
    }
    @Override
    public String toString() {
        return "AsOmitir";
    }
}
