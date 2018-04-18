package corrector;

import org.languagetool.rules.RuleMatch;

import java.util.List;

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
}
