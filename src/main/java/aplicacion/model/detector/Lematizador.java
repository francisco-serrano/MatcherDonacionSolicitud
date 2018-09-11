package aplicacion.model.detector;

import com.google.common.base.Splitter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class Lematizador {

    private Map<String, String> diccionario_lemas = new HashMap<>();

    public Lematizador() {
        try {
            new BufferedReader(new FileReader(new File("lemmatization-es.txt"))).lines()
                    .map(linea -> Splitter.on('\t').splitToList(linea))
                    .forEach(parLemaPalabra -> diccionario_lemas.put(parLemaPalabra.get(1), parLemaPalabra.get(0)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public String lemmatize(String word) {
        if (diccionario_lemas.keySet().contains(word))
            return diccionario_lemas.get(word);

        return word;
    }
}
