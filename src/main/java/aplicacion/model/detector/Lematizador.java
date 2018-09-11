package aplicacion.model.detector;

import com.google.common.base.Splitter;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.*;

import java.util.HashMap;
import java.util.Map;

public class Lematizador {

    private Map<String, String> diccionario_lemas = new HashMap<>();

    public Lematizador() {
        try {
            InputStream fileStream = ResourceUtils.getURL("classpath:lemmatization-es.txt").openStream();

            new BufferedReader(new InputStreamReader(fileStream)).lines()
                    .map(linea -> Splitter.on('\t').splitToList(linea))
                    .forEach(parLemaPalabra -> diccionario_lemas.put(parLemaPalabra.get(1), parLemaPalabra.get(0)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String lemmatize(String word) {
        if (diccionario_lemas.keySet().contains(word))
            return diccionario_lemas.get(word);

        return word;
    }
}
