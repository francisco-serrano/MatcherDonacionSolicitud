import corrector.CorrectorOrtografico;
import org.languagetool.rules.RuleMatch;

import java.util.List;

public class Main {


    // También se puede hacer mediante HTTP POST (ver Postman)

    public static void main(String[] args) {

//        // Armo la cadena de texto a chequear
//        String textoChequear = "hola cómo andazz";
//
//        // Chequeo la cadena generando lista de errores
//        // TODO: implementar como un servicio ya que la creación del lenguaje es un toque lenta
//        // http://wiki.languagetool.org/java-api
//        List<RuleMatch> matches = new CorrectorOrtografico().check(textoChequear);
//
//        System.out.println("Texto ingresado: " + textoChequear);
//
//        // Chequeo si hay errores
//        if (matches.size() == 0) {
//            System.out.println("No hay errores");
//            return;
//        }
//
//        // Recorro la lista de errores
//        for (RuleMatch match : matches) {
//            String palabraErronea = textoChequear.substring(match.getFromPos(), match.getToPos());
//
//            System.out.println("Potential error at characters " +
//                    match.getFromPos() + "-" + match.getToPos() + " (" + palabraErronea + "): " +
//                    match.getMessage());
//            System.out.println("Suggested correction(s): " +
//                    match.getSuggestedReplacements());
//        }

    }
}
