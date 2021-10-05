
//Clase utilizada para que dado un char, sepas la columna a la que hace referencia el mismo según la matriz de transicion de estados
public class CambioEstado {
    public boolean esMayus(char c) {
        return c >= 65 && c <= 90;
    }

    public boolean esMinus(char c) {
        return c >= 97 && c <= 122;
    }

    public boolean esLetra(char c) {
        return (esMayus(c) || esMinus(c));
    }


    public boolean esDigito(char c) {
        return (c >= 48 && c <= 57);
    }

    public boolean isEOL(char c) {
        return (c == '\n' || c == '\r');
    }

    //dado un char retorna la colúmna de la matriz a la que hace referencia
    public int getColumnaSimbolo(char c) {

        if (esDigito(c)) {
            return 3;
        }
        if (esLetra(c) && c != 'S') {
            return 0;
        }

        switch (c) {
            case ' ':
            case '\r':
            case '\t': {
                return 1;
            }
            case '\n': {
                return 2;
            }
            case 'S': {
                return 4;
            }
            case '-': {
                return 5;
            }
            case '_': {
                return 6;
            }
            case '=': {
                return 7;
            }
            case '>': {
                return 8;
            }
            case '<': {
                return 9;
            }
            case '.': {
                return 10;
            }
            case ':': {
                return 11;
            }
            case '%': {
                return 12;
            }
            case '/': {
                return 13;
            }
            case '+': {
                return 14;
            }
            case '(':
            case ')':
            case '*':
            case ';':
            case ',': {
                return 15;
            }
            case '|':{
                return 16;
            }
            case '&':{
                return 17;
            }

            //Para el fin de archivo
            case (char)06: {
                return 19;
            }

        }
        return 18; //Otros
    }
}