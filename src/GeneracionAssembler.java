import java.util.ArrayList;
import java.util.HashMap;

public class GeneracionAssembler
{
    private ArrayList<TercetoOperandos> listaDeTercetos;
    private HashMap<String, EntradaTablaSimbolos> tablaDeSimbolos;
    private HashMap<String, String> tablaDeConstantes;
    private ArrayList<Registro> registros16Bits;
    private String saltoCondicional;
    private boolean disponibleAx;
    private int tercetoACambiarRegistro;

    private StringBuilder code;

    public GeneracionAssembler(ArrayList<TercetoOperandos> listaDeTercetos, HashMap<String, EntradaTablaSimbolos> tablaDeSimbolos)
    {
        this.listaDeTercetos = listaDeTercetos;
        this.tablaDeSimbolos = tablaDeSimbolos;
        this.tablaDeConstantes = new HashMap<String, String>();
        this.registros16Bits = new ArrayList<Registro>();
        this.saltoCondicional = "";
        this.disponibleAx = true;

        this.code = new StringBuilder();

        this.inicializarRegistros();
    }

    private void generarImpresionCadena(String valorAImprimir)
    {
        //invoke MessageBox, NULL, addr HelloWorld, addr HelloWorld, MB_OK
        this.code.append("invoke MessageBox, NULL, addr ");
        //this.code.append("invoke printf, cfm$(\"%s\\n\"), OFFSET ");
        this.code.append(valorAImprimir);
        this.code.append(", addr _Mensaje, MB_OK");
        this.code.append("\n");
    }

    private void inicializarRegistros()
    {
        this.registros16Bits.add(new Registro("BX", true));
        this.registros16Bits.add(new Registro("CX", true));
    }

    public String getConversionAssembler()
    {
        StringBuilder assembler = new StringBuilder();
        assembler.append(this.getEncabezadoAssembler());
        assembler.append("\n");
        assembler.append(this.getZonaDatosAssembler());
        assembler.append("\n");
        assembler.append(this.getZonaCodigoAssembler());
        assembler.append("\n");
        return assembler.toString();
    }

    private String getEncabezadoAssembler()
    {
        StringBuilder encabezado = new StringBuilder();
        encabezado.append(".386");
        encabezado.append("\n");
        encabezado.append(".model flat, stdcall ; Indica el tamaño de programa");
        encabezado.append("\n");
        encabezado.append(".STACK 200h ; Inicializa Stacken dir.indicada");
        encabezado.append("\n");
        encabezado.append("option casemap :none");
        encabezado.append("\n");
        encabezado.append("include \\masm32\\include\\windows.inc");
        encabezado.append("\n");
        encabezado.append("include \\masm32\\include\\kernel32.inc");
        encabezado.append("\n");
        encabezado.append("include \\masm32\\include\\user32.inc");
        encabezado.append("\n");
        encabezado.append("includelib \\masm32\\lib\\kernel32.lib");
        encabezado.append("\n");
        encabezado.append("includelib \\masm32\\lib\\user32.lib");
        encabezado.append("\n");
        encabezado.append("include \\masm32\\include\\masm32rt.inc");
        encabezado.append("\n");
        encabezado.append("dll_dllcrt0 PROTO C");
        encabezado.append("\n");
        encabezado.append("printf PROTO C :VARARG");
        encabezado.append("\n");
        return encabezado.toString();
    }

    private String getZonaDatosAssembler()
    {
        StringBuilder data = new StringBuilder();
        data.append(".data");
        data.append("\n");

        int contadorConstantesEnterasSinSigno = 1;
        int contadorConstantesFlotantes = 1;
        int contadorCadenasDeCaracteres = 1;
        for(HashMap.Entry<String, EntradaTablaSimbolos> lexemaActual : this.tablaDeSimbolos.entrySet())
        {
            if(lexemaActual.getValue().getTipo().equals("UINT") && lexemaActual.getValue().getUso().equals("variable")
                    ||
                    lexemaActual.getValue().getTipo().equals("UINT") && lexemaActual.getValue().getUso().equals("nombre de parametro")
                    || lexemaActual.getValue().getTipo().equals("DOUBLE") && lexemaActual.getValue().getUso().equals("auxiliar conversion"))
            {
                data.append("_");
                data.append(lexemaActual.getKey());
                data.append(" DW ?");
                data.append("\n");
            }

            else if(lexemaActual.getValue().getTipo().equals("UINT"))
            {
                if(!this.tablaDeConstantes.containsKey(lexemaActual.getKey()))
                {
                    data.append("_CTE_UINT_");
                    data.append(contadorConstantesEnterasSinSigno);
                    data.append(" DW ");
                    data.append(lexemaActual.getKey());
                    data.append("\n");
                    this.tablaDeConstantes.put(lexemaActual.getKey(), "_CTE_UINT_" + contadorConstantesEnterasSinSigno);
                    contadorConstantesEnterasSinSigno++;
                }
            }

            else if(lexemaActual.getValue().getTipo().equals("DOUBLE") && lexemaActual.getValue().getUso().equals("variable")
                    ||
                    lexemaActual.getValue().getTipo().equals("DOUBLE") && lexemaActual.getValue().getUso().equals("nombre de parametro")
                    ||
                    lexemaActual.getValue().getTipo().equals("DOUBLE") && lexemaActual.getValue().getUso().equals("auxiliar"))
            {
                data.append("_");
                data.append(lexemaActual.getKey());
                data.append(" DQ ?");
                data.append("\n");
            }

            else if(lexemaActual.getValue().getTipo().equals("DOUBLE"))
            {
                if(!this.tablaDeConstantes.containsKey(lexemaActual.getKey()))
                {
                    data.append("_CTE_DOUBLE_");
                    data.append(contadorConstantesFlotantes);
                    data.append(" DQ ");
                    data.append(lexemaActual.getKey());
                    data.append("\n");
                    this.tablaDeConstantes.put(lexemaActual.getKey(), "_CTE_DOUBLE_" + contadorConstantesFlotantes);
                    contadorConstantesFlotantes++;
                }
            }

            else if(lexemaActual.getValue().getTipo().equals("Cadena de caracteres"))
            {
                if(!this.tablaDeConstantes.containsKey(lexemaActual.getKey()))
                {
                    data.append("_CTE_STRING_");
                    data.append(contadorCadenasDeCaracteres);
                    data.append(" DB ");
                    data.append(lexemaActual.getKey().replace('\'', '"'));
                    data.append(", 0");
                    data.append("\n");
                    this.tablaDeConstantes.put(lexemaActual.getKey(), "_CTE_STRING_" + contadorCadenasDeCaracteres);
                    contadorCadenasDeCaracteres++;
                }
            }
        }
        data.append("_Mensaje DB \"Mensaje\", 0");
        data.append("\n");
        data.append("_Error DB \"Error\", 0");
        data.append("\n");
        data.append("_ErrorOverflowProductos DB \"Error al realizar la multiplicacion ya que se produce overflow\", 0");
        data.append("\n");
        data.append("_ErrorResultadoNegativoRestas DB \"Error al realizar la resta ya que se obtiene un resultado negativo siendo que el tipo es entero sin signo\", 0");
        data.append("\n");
        data.append("_mensajeSalida DB \"Gracias por usar el compilador\", 0");
        data.append("\n");
        return data.toString();
    }

    private String getZonaCodigoAssembler()
    {
        this.code.append(".code");
        this.code.append("\n");
        this.code.append("START:");
        this.code.append("\n");
        TercetoOperandos tercetoActual;
        for(int i = 0; i < this.listaDeTercetos.size(); i++)
        {
            tercetoActual = this.listaDeTercetos.get(i);
            switch (tercetoActual.getOperando())
            {
                case "+": case "*": case "-": case "=": case "<": case ">": case "<=": case ">=": case "!=": case "==":
                if(tercetoActual.getTipo().equals("UINT"))
                {
                    this.getAssemblerEntero(tercetoActual);
                }
                else
                {
                    this.getAssemblerDouble(tercetoActual);
                }
                break;

                case "/":
                    if(this.tablaDeSimbolos.containsKey(tercetoActual.getOperador1().getResultado()) && this.tablaDeSimbolos.containsKey(tercetoActual.getOperador2().getResultado()))
                    {
                        if(this.tablaDeSimbolos.get(tercetoActual.getOperador1().getResultado()).getTipo().equals("UINT") && this.tablaDeSimbolos.get(tercetoActual.getOperador2().getResultado()).getTipo().equals("UINT"))
                        {
                            this.getAssemblerEntero(tercetoActual);
                        }
                        else
                        {
                            this.getAssemblerDouble(tercetoActual);
                        }
                    }
                    else
                    {
                        this.getAssemblerDouble(tercetoActual);
                    }
                    break;

                case "BF":
                    this.code.append(this.saltoCondicional);
                    this.code.append(" Label_");
                    this.code.append(this.getNumeroTercetoSinCorchetes(tercetoActual.getOperador2().toString()));
                    this.code.append("\n");
                    break;

                case "BI":
                    this.code.append("JMP Label_");
                    this.code.append(this.getNumeroTercetoSinCorchetes(tercetoActual.getOperador1().toString()));
                    this.code.append("\n");
                    break;

                case "OUT":
                    this.generarImpresionCadena(this.tablaDeConstantes.get(tercetoActual.getOperador1().toString()));
                    break;

                case "call":
                    this.code.append("call ");
                    this.code.append(tercetoActual.getOperador1());
                    this.code.append("\n");
                    break;

                case "utod":
                    this.code.append("MOV ");
                    this.code.append(tercetoActual.getResultado());
                    this.code.append(", ");
                    this.code.append(tercetoActual.getOperador1().getResultado());
                    this.code.append("\n");
                    this.liberarRegistro(tercetoActual.getOperador1().getResultado());
                    break;

                case "@main PROC":
                    break;

                default:
                    if(tercetoActual.getOperando().contains("PROC"))
                    {
                        this.code.append(tercetoActual.getOperando());
                        this.code.append("\n");
                    }
                    else if (tercetoActual.getOperando().contains("ENDP"))
                    {
                        if(tercetoActual.getOperando().equals("@main ENDP"))
                        {
                            this.code.append("invoke MessageBox, NULL, addr _mensajeSalida, addr _Mensaje, MB_OK");
                            this.code.append("\n");
                            this.code.append("invoke ExitProcess, 0");
                            this.code.append("\n");
                        }
                        else
                        {
                            this.code.append("RET");
                            this.code.append("\n");
                            this.code.append(tercetoActual.getOperando());
                            this.code.append("\n");
                        }
                    }
                    else
                    {
                        this.code.append(tercetoActual.getOperando());
                        this.code.append(": ");
                        this.code.append("\n");
                    }
            }
        }

        this.code.append("Label_errorOverflowProductos:");
        this.code.append("\n");
        this.code.append("invoke MessageBox, NULL, addr _ErrorOverflowProductos, addr _Error, MB_OK");
        this.code.append("\n");
        this.code.append("invoke ExitProcess, 0");
        this.code.append("\n");

        this.code.append("Label_errorResultadoNegativoRestas:");
        this.code.append("\n");
        this.code.append("invoke MessageBox, NULL, addr _ErrorResultadoNegativoRestas, addr _Error, MB_OK");
        this.code.append("\n");
        this.code.append("invoke ExitProcess, 0");
        this.code.append("\n");

        this.code.append("END START");
        this.code.append("\n");
        return this.code.toString();
    }

    private void cargarOperandosAssembler(TercetoOperandos tercetoActual)
    {
        if(tercetoActual.getOperador1().getResultado().contains("@"))
        {
            if(tercetoActual.getOperador1().getResultado().contains("@AUXCONV"))
            {

                this.code.append("FILD ");
                this.code.append(tercetoActual.getOperador1().getResultado());
                this.code.append("\n");
            }
            else
            {
                if(this.tablaDeSimbolos.containsKey(tercetoActual.getOperador1().getResultado().substring(1)))
                {
                    String tipo = this.tablaDeSimbolos.get(tercetoActual.getOperador1().getResultado().substring(1)).getTipo();
                    if(tipo.equals("UINT"))
                    {
                        this.code.append("FILD ");
                        this.code.append(tercetoActual.getOperador1().getResultado());
                        this.code.append("\n");
                    }
                    else
                    {
                        this.code.append("FLD ");
                        this.code.append(tercetoActual.getOperador1().getResultado());
                        this.code.append("\n");
                    }
                }
                else
                {
                    this.code.append("FLD ");
                    this.code.append(tercetoActual.getOperador1().getResultado());
                    this.code.append("\n");
                }
            }
        }
        else
        {
            this.code.append("FLD ");
            this.code.append(this.tablaDeConstantes.get(tercetoActual.getOperador1().getResultado()));
            this.code.append("\n");
        }
        if(tercetoActual.getOperador2().getResultado().contains("@"))
        {
            if(tercetoActual.getOperador2().getResultado().contains("@AUXCONV"))
            {
                this.code.append("FILD ");
                this.code.append(tercetoActual.getOperador2().getResultado());
                this.code.append("\n");
            }
            else
            {
                if(this.tablaDeSimbolos.containsKey(tercetoActual.getOperador2().getResultado().substring(1)))
                {
                    String tipo = this.tablaDeSimbolos.get(tercetoActual.getOperador2().getResultado().substring(1)).getTipo();
                    if(tipo.equals("UINT"))
                    {
                        this.code.append("FILD ");
                        this.code.append(tercetoActual.getOperador2().getResultado());
                        this.code.append("\n");
                    }
                    else
                    {
                        this.code.append("FLD ");
                        this.code.append(tercetoActual.getOperador2().getResultado());
                        this.code.append("\n");
                    }
                }
                else
                {
                    this.code.append("FLD ");
                    this.code.append(tercetoActual.getOperador2().getResultado());
                    this.code.append("\n");
                }
            }
        }
        else
        {
            this.code.append("FLD ");
            this.code.append(this.tablaDeConstantes.get(tercetoActual.getOperador2().getResultado()));
            this.code.append("\n");
        }
    }

    private void getAssemblerDouble(TercetoOperandos tercetoActual)
    {
        switch (tercetoActual.getOperando())
        {
            case "+":
                this.cargarOperandosAssembler(tercetoActual);
                this.code.append("FADD");
                this.code.append("\n");
                this.code.append("FSTP ");
                this.code.append(tercetoActual.getResultado());
                this.code.append("\n");
                break;

            case "-":
                this.cargarOperandosAssembler(tercetoActual);
                this.code.append("FSUB");
                this.code.append("\n");
                this.code.append("FSTP ");
                this.code.append(tercetoActual.getResultado());
                this.code.append("\n");
                break;

            case "*":
                this.cargarOperandosAssembler(tercetoActual);
                this.code.append("FMUL");
                this.code.append("\n");
                this.code.append("JO Label_errorOverflowProductos");
                this.code.append("\n");
                this.code.append("FSTP ");
                this.code.append(tercetoActual.getResultado());
                this.code.append("\n");
                break;

            case "/":
                this.cargarOperandosAssembler(tercetoActual);
                this.code.append("FDIV");
                this.code.append("\n");
                this.code.append("FSTP ");
                this.code.append(tercetoActual.getResultado());
                this.code.append("\n");
                break;

            case "=":
                if(tercetoActual.getOperador2() != null)
                {
                    if(tercetoActual.getOperador2().getResultado().contains("@"))
                    {
                        if(tercetoActual.getOperador2().getResultado().contains("@AUXCONV"))
                        {
                            this.code.append("FILD ");
                            this.code.append(tercetoActual.getOperador2().getResultado());
                            this.code.append("\n");
                        }
                        else
                        {
                            if(this.tablaDeSimbolos.containsKey(tercetoActual.getOperador2().getResultado().substring(1)))
                            {
                                String tipo = this.tablaDeSimbolos.get(tercetoActual.getOperador2().getResultado().substring(1)).getTipo();
                                if(tipo.equals("UINT"))
                                {
                                    this.code.append("FILD ");
                                    this.code.append(tercetoActual.getOperador2().getResultado());
                                    this.code.append("\n");
                                }
                                else
                                {
                                    this.code.append("FLD ");
                                    this.code.append(tercetoActual.getOperador2().getResultado());
                                    this.code.append("\n");
                                }
                            }
                            else
                            {
                                this.code.append("FLD ");
                                this.code.append(tercetoActual.getOperador2().getResultado());
                                this.code.append("\n");
                            }
                        }
                    }
                    else
                    {
                        this.code.append("FLD ");
                        this.code.append(this.tablaDeConstantes.get(tercetoActual.getOperador2().getResultado()));
                        this.code.append("\n");
                    }
                }
                else
                {
                    this.code.append("FLDZ");
                    this.code.append("\n");
                }
                this.code.append("FSTP ");
                this.code.append(tercetoActual.getOperador1().getResultado());
                this.code.append("\n");
                break;

            case "<": case ">": case "<=": case ">=": case "!=": case "==":
            this.cargarOperandosAssembler(tercetoActual);
            this.code.append("FCOMP");
            this.code.append("\n");
            this.code.append("FSTSW AX");
            this.code.append("\n");
            this.code.append("SAHF");
            this.code.append("\n");
            this.saltoCondicional = this.getSaltoCondicionalDoubles(tercetoActual.getOperando());
            break;
        }
    }

    private void liberarRegistro(String registro)
    {
        if(registro.equals("AX"))
        {
            this.disponibleAx = true;
        }
        else
        {
            boolean encontro = false;
            for(int i = 0; i < this.registros16Bits.size() && !encontro; i++)
            {
                if(this.registros16Bits.get(i).getRegistro().equals(registro))
                {
                    this.registros16Bits.get(i).setEstado(true);
                    encontro = true;
                }
            }
        }
    }

    private String getRegistroDisponible16Bits()
    {
        String registro16Bits = "";
        boolean encontro = false;
        for(int i = 0; i < this.registros16Bits.size() && !encontro; i++)
        {
            if(this.registros16Bits.get(i).estaDisponible())
            {
                registro16Bits = this.registros16Bits.get(i).getRegistro();
                this.registros16Bits.get(i).setEstado(false);
                encontro = true;
            }
        }
        return registro16Bits;
    }

    private void algoritmoRegistrosSumaResta(TercetoOperandos tercetoActual, String assemblerOperando)
    {
        //Situación 1
        if(tercetoActual.getOperador1().esVariableOConstante() && tercetoActual.getOperador2().esVariableOConstante())
        {
            String registro = this.getRegistroDisponible16Bits();
            if(tercetoActual.getOperando().equals("="))
            {
                /*
                    MOV R1, vble2
                    MOV vble1, R1
                */
                this.code.append("MOV ");
                this.code.append(registro);
                this.code.append(", ");
                this.code.append(tercetoActual.getOperador2().getResultado());
                this.code.append("\n");
                this.code.append(assemblerOperando);
                this.code.append(" ");
                this.code.append(tercetoActual.getOperador1().getResultado());
                this.code.append(", ");
                this.code.append(registro);
                this.code.append("\n");
                this.liberarRegistro(registro);
            }
            else
            {
                /*
                    MOV  R1, vble1
                    OP R1, vble2
                */
                this.code.append("MOV ");
                this.code.append(registro);
                this.code.append(", ");
                this.code.append(tercetoActual.getOperador1().getResultado());
                this.code.append("\n");
                this.code.append(assemblerOperando); //assemblerOperando es ADD, SUB
                this.code.append(" ");
                this.code.append(registro);
                this.code.append(", ");
                this.code.append(tercetoActual.getOperador2().getResultado());
                this.code.append("\n");
                tercetoActual.setResultado(registro); //(+, b, c) R1
                if(assemblerOperando.equals("SUB"))
                {
                    this.code.append("JC Label_errorResultadoNegativoRestas");
                    this.code.append("\n");
                }
            }
        }

        //Situación 2
        else if (!tercetoActual.getOperador1().esVariableOConstante() && tercetoActual.getOperador2().esVariableOConstante())
        {
            this.code.append(assemblerOperando); //assemblerOperando es ADD, SUB
            this.code.append(" ");
            this.code.append(tercetoActual.getOperador1().getResultado());
            this.code.append(", ");
            this.code.append(tercetoActual.getOperador2().getResultado());
            this.code.append("\n");
            tercetoActual.setResultado(tercetoActual.getOperador1().getResultado());
            if(assemblerOperando.equals("SUB"))
            {
                this.code.append("JC Label_errorResultadoNegativoRestas");
                this.code.append("\n");
            }
        }

        //Situación 3
        else if (!tercetoActual.getOperador1().esVariableOConstante() && !tercetoActual.getOperador2().esVariableOConstante())
        {
            this.code.append(assemblerOperando); //assemblerOperando es ADD, SUB
            this.code.append(" ");
            this.code.append(tercetoActual.getOperador1().getResultado());
            this.code.append(", ");
            this.code.append(tercetoActual.getOperador2().getResultado());
            this.code.append("\n");
            tercetoActual.setResultado(tercetoActual.getOperador1().getResultado());
            this.liberarRegistro(tercetoActual.getOperador2().getResultado());
            if(assemblerOperando.equals("SUB"))
            {
                this.code.append("JC Label_errorResultadoNegativoRestas");
                this.code.append("\n");
            }
        }

        //Situación 4
        else if (tercetoActual.getOperador1().esVariableOConstante() && !tercetoActual.getOperador2().esVariableOConstante())
        {
            //Situación 4.a
            if(tercetoActual.getOperando().equals("+"))
            {
                this.code.append(assemblerOperando); //assemblerOperando es ADD, SUB
                this.code.append(" ");
                this.code.append(tercetoActual.getOperador1().getResultado());
                this.code.append(", ");
                this.code.append(tercetoActual.getOperador2().getResultado());
                this.code.append("\n");
                tercetoActual.setResultado(tercetoActual.getOperador1().getResultado());
            }

            //Situación 4.b
            else if(tercetoActual.getOperando().equals("-"))
            {
                String registro = this.getRegistroDisponible16Bits();
                this.code.append("MOV ");
                this.code.append(registro);
                this.code.append(", ");
                this.code.append(tercetoActual.getOperador1().getResultado());
                this.code.append("\n");
                this.code.append(assemblerOperando); //assemblerOperando es ADD, SUB
                this.code.append(" ");
                this.code.append(registro);
                this.code.append(", ");
                this.code.append(tercetoActual.getOperador2().getResultado());
                this.code.append("\n");
                tercetoActual.setResultado(registro);
                this.liberarRegistro(tercetoActual.getOperador2().getResultado());
                this.code.append("JC Label_errorResultadoNegativoRestas");
                this.code.append("\n");
            }

            //Asignaciones situación a
            else if (tercetoActual.getOperando().equals("="))
            {
                this.code.append(assemblerOperando); //Tiene MOV
                this.code.append(" ");
                this.code.append(tercetoActual.getOperador1().getResultado());
                this.code.append(", ");
                this.code.append(tercetoActual.getOperador2().getResultado());
                this.code.append("\n");
                this.liberarRegistro(tercetoActual.getOperador2().getResultado());
            }
        }
    }

    private void algoritmoRegistrosMultDiv(TercetoOperandos tercetoActual, String assemblerOperando)
    {

        //Situación 1
        if(tercetoActual.getOperador1().esVariableOConstante() && tercetoActual.getOperador2().esVariableOConstante())
        {
            String registro = "AX";
            if(!this.disponibleAx)
            {
                String registroLibre = this.getRegistroDisponible16Bits();
                this.code.append("MOV ");
                this.code.append(registroLibre);
                this.code.append(", ");
                this.code.append("AX");
                this.code.append("\n");
                this.listaDeTercetos.get(this.tercetoACambiarRegistro - 1).setResultado(registroLibre);
            }
            this.disponibleAx = false;
            /*
                MOV  R1, vble1
                OP R1, vble2
            */
            this.code.append("MOV ");
            this.code.append(registro);
            this.code.append(", ");
            this.code.append(tercetoActual.getOperador1().getResultado());
            this.code.append("\n");
            if(assemblerOperando.equals("DIV"))
            {
                this.code.append("CWD");
                this.code.append("\n");
            }
            this.code.append(assemblerOperando);
            this.code.append(" ");
            this.code.append(this.tablaDeConstantes.get(tercetoActual.getOperador2().getResultado()));
            this.code.append("\n");
            if(assemblerOperando.equals("MUL"))
            {
                this.code.append("JC Label_errorOverflowProductos");
                this.code.append("\n");
            }
            tercetoActual.setResultado(registro);
            this.tercetoACambiarRegistro = tercetoActual.getNumero();
        }

        //Situación 2
        else if (!tercetoActual.getOperador1().esVariableOConstante() && tercetoActual.getOperador2().esVariableOConstante())
        {
            if(assemblerOperando.equals("DIV"))
            {
                this.code.append("CWD");
                this.code.append("\n");
            }
            this.code.append(assemblerOperando);
            this.code.append(" ");
            this.code.append(this.tablaDeConstantes.get(tercetoActual.getOperador2().getResultado()));
            this.code.append("\n");
            if(assemblerOperando.equals("MUL"))
            {
                this.code.append("JC Label_errorOverflowProductos");
                this.code.append("\n");
            }
            tercetoActual.setResultado(tercetoActual.getOperador1().getResultado());
        }

        //Situación 3
        else if (!tercetoActual.getOperador1().esVariableOConstante() && !tercetoActual.getOperador2().esVariableOConstante())
        {
            if(assemblerOperando.equals("DIV"))
            {
                this.code.append("CWD");
                this.code.append("\n");
            }
            this.code.append(assemblerOperando);
            this.code.append(" ");
            this.code.append(tercetoActual.getOperador2().getResultado());
            this.code.append("\n");
            if(assemblerOperando.equals("MUL"))
            {
                this.code.append("JC Label_errorOverflowProductos");
                this.code.append("\n");
            }
            tercetoActual.setResultado(tercetoActual.getOperador1().getResultado());
            this.liberarRegistro(tercetoActual.getOperador2().getResultado());
        }

        //Situación 4
        else if (tercetoActual.getOperador1().esVariableOConstante() && !tercetoActual.getOperador2().esVariableOConstante())
        {
            //Situación 4.a
            if(tercetoActual.getOperando().equals("*"))
            {
                this.code.append(assemblerOperando);
                this.code.append(" ");
                this.code.append(tercetoActual.getOperador2().getResultado());
                this.code.append("\n");
                this.code.append("JC Label_errorOverflowProductos");
                this.code.append("\n");
                tercetoActual.setResultado(tercetoActual.getOperador1().getResultado());
            }
            //Situación 4.b
            else if(tercetoActual.getOperando().equals("/"))
            {
                String registro = "AX";
                this.code.append("MOV ");
                this.code.append(registro);
                this.code.append(", ");
                this.code.append(tercetoActual.getOperador1().getResultado());
                this.code.append("\n");
                this.code.append("CWD");
                this.code.append("\n");
                this.code.append(assemblerOperando);
                this.code.append(" ");
                this.code.append(tercetoActual.getOperador2().getResultado());
                this.code.append("\n");
                tercetoActual.setResultado(registro);
                this.liberarRegistro(tercetoActual.getOperador2().getResultado());
            }
        }
    }

    private void getAssemblerEntero(TercetoOperandos tercetoActual)
    {
        switch (tercetoActual.getOperando())
        {
            case "+":
                this.algoritmoRegistrosSumaResta(tercetoActual, "ADD");
                break;

            case "-":
                this.algoritmoRegistrosSumaResta(tercetoActual, "SUB");
                break;

            case "*":
                this.algoritmoRegistrosMultDiv(tercetoActual, "MUL");
                break;

            case "/":
                this.algoritmoRegistrosMultDiv(tercetoActual, "DIV");
                break;

            case "=":
                this.algoritmoRegistrosSumaResta(tercetoActual, "MOV");
                break;

            case "<": case ">": case "<=": case ">=": case "!=": case "==":
            if(tercetoActual.getOperador1().esVariableOConstante() && tercetoActual.getOperador2().esVariableOConstante())
            {
                String registro = this.getRegistroDisponible16Bits();
                this.code.append("MOV ");
                this.code.append(registro);
                this.code.append(", ");
                this.code.append(tercetoActual.getOperador2().getResultado());
                this.code.append("\n");
                this.code.append("CMP ");
                this.code.append(tercetoActual.getOperador1().getResultado());
                this.code.append(", ");
                this.code.append(registro);
                this.code.append("\n");
                this.saltoCondicional = this.getSaltoCondicionalEnterosSinSigno(tercetoActual.getOperando());
                this.liberarRegistro(registro);
            }
            else
            {
                this.code.append("CMP ");
                this.code.append(tercetoActual.getOperador1().getResultado());
                this.code.append(", ");
                this.code.append(tercetoActual.getOperador2().getResultado());
                this.code.append("\n");
                this.saltoCondicional = this.getSaltoCondicionalEnterosSinSigno(tercetoActual.getOperando());
            }
            break;
        }
    }

    private String getNumeroTercetoSinCorchetes(String numeroTerceto)
    {
        return numeroTerceto.substring(numeroTerceto.indexOf("[") + 1, numeroTerceto.indexOf("]"));
    }

    private String getSaltoCondicionalEnterosSinSigno(String operando){
        switch (operando) {
            case ">":
                return "JBE";
            case "<":
                return "JAE";
            case "==":
                return "JNE";
            case "!=":
                return "JE";
            case "<=":
                return "JA";
            case ">=":
                return "JB";
        }
        return null;
    }

    private String getSaltoCondicionalDoubles(String operando){
        switch (operando) {
            case ">":
                return "JLE";
            case "<":
                return "JGE";
            case "==":
                return "JNE";
            case "!=":
                return "JE";
            case "<=":
                return "JG";
            case ">=":
                return "JL";
        }
        return null;
    }
}

