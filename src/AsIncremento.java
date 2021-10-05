public class AsIncremento implements AccionSemantica {
    @Override
    public void ejecutar(AnalizadorLexico a) {
        if(a.getChar() != (char)'\r'){
            a.setBuffer(a.getBuffer() + a.getChar());
        }
        a.incPosition();

    }
    @Override
    public String toString() {
        return "AsIncremento";
    }
}
