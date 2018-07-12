package aplicacion.model.corrector;

import org.languagetool.JLanguageTool;
import org.languagetool.language.Spanish;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class CorrectorOrtografico {
    private JLanguageTool languageTool = new JLanguageTool(new Spanish());
    private AtomicLong atomicLong = new AtomicLong();

    public List<Correccion> check(String plainText) {

        try {
            return languageTool.check(plainText).stream().map(ruleMatch ->
                new Correccion(atomicLong.incrementAndGet(), plainText, ruleMatch)
            ).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }
}
