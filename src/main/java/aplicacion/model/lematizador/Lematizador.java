package aplicacion.model.lematizador;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import org.springframework.util.ResourceUtils;

import java.io.*;

public class Lematizador {

    private InputStream fileStream;

    // Enfoque original
//    private Map<String, String> diccionario_lemas = new HashMap<>();

    // Enfoque multimap, ahorra un poco de memoria pero no mucho
//    private Multimap<String, String> mapeoPalabrasLema = ArrayListMultimap.create();

//    private RepositorioLemas repositorioLemas = new RepositorioLemas("classpath:lemmatization-es.db");
    private RepositorioLemas repositorioLemas;

    public Lematizador() {

        try {
//            fileStream = ResourceUtils.getURL("classpath:lemmatization-es.txt").openStream();
            repositorioLemas = new RepositorioLemas(ResourceUtils.getURL("classpath:lemmatization-es.db").toString());
            repositorioLemas.connect();
//            new BufferedReader(new InputStreamReader(fileStream)).lines()
//                    .map(linea -> Splitter.on('\t').splitToList(linea))
//                    .forEach(parLemaPalabra -> diccionario_lemas.put(parLemaPalabra.get(1), parLemaPalabra.get(0)));

//            new BufferedReader(new InputStreamReader(fileStream)).lines()
//                    .forEach(linea -> {
//                        String[] aux = linea.split("\t");
//                        mapeoPalabrasLema.put(aux[0], aux[1]);
//                    });

//            new BufferedReader(new InputStreamReader(fileStream)).lines()
//                    .forEach(linea -> {
//                        repositorioLemas.cargarTabla(linea);
//                    });

//            System.out.println("Finalizó el alta de la base");

//            System.gc();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String lemmatize(String word) {
        // Enfoque 1
//        if (diccionario_lemas.keySet().contains(word))
//            return diccionario_lemas.get(word);
//
//        return word;

        // Enfoque 2
//        if (mapeoPalabrasLema.keySet().contains(word))
//            return word;
//
//        for (String lema : mapeoPalabrasLema.keySet()) {
//            if (mapeoPalabrasLema.get(lema).contains(word))
//                return lema;
//        }
//
//        throw new RuntimeException("No debiera llegar");

        // Enfoque 3
        return repositorioLemas.getLema(word);
    }
}
