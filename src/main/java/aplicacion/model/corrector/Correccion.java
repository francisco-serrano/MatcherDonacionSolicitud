package aplicacion.model.corrector;

import org.languagetool.rules.RuleMatch;

import java.util.List;

/**
 * Caracteriza a una determinada corrección dentro de un texto libre.
 * Una corrección tiene las siguientes características:
 *  - Identificador único de corrección
 *  - Palabra mal escrita
 *  - Indice de char inicial donde está el error
 *  - Indice de char final donde está el error
 *  - Mensaje con el error ortográfico que se encontró
 *  - Lista de palabras con los reemplazos sugeridos
 *
 *  @author Francisco Serrano <francisco.serrano372@gmail.com>, Martín Santillán Cooper <marsancoo@gmail.com>
 */
public class Correccion {

    private final long id;
    private String palabraErronea;
    private int indiceInicial;
    private int indiceFinal;
    private String mensajeCorreccion;
    private List<String> reemplazosSugeridos;

    public Correccion(long id, String plainText, RuleMatch match) {
        this.id = id;
        this.indiceInicial = match.getFromPos();
        this.indiceFinal = match.getToPos();
        this.palabraErronea = plainText.substring(indiceInicial, indiceFinal);
        this.mensajeCorreccion = match.getMessage();
        this.reemplazosSugeridos = match.getSuggestedReplacements();
    }

    public String getPalabraErronea() {
        return palabraErronea;
    }

    public int getIndiceInicial() {
        return indiceInicial;
    }

    public int getIndiceFinal() {
        return indiceFinal;
    }

    public String getMensajeCorreccion() {
        return mensajeCorreccion;
    }

    public List<String> getReemplazosSugeridos() {
        return reemplazosSugeridos;
    }

    @Override
    public String toString() {
        return "Correccion{" +
                "id=" + id +
                ", palabraErronea='" + palabraErronea + '\'' +
                ", indiceInicial=" + indiceInicial +
                ", indiceFinal=" + indiceFinal +
                ", mensajeCorreccion='" + mensajeCorreccion + '\'' +
                ", reemplazosSugeridos=" + reemplazosSugeridos +
                '}';
    }
}
