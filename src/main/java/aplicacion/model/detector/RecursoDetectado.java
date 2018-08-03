package aplicacion.model.detector;

import java.util.HashMap;
import java.util.Map;

public class RecursoDetectado {

    private int recurso;
    private int recursoIDW;
    private String descripcion;
    private int cantidad;

    public RecursoDetectado(int recurso, int recursoIDW, String descripcion, int cantidad) {
        this.recurso = recurso;
        this.recursoIDW = recursoIDW;
        this.descripcion = descripcion;
        this.cantidad = cantidad;
    }

    public int getRecurso() {
        return recurso;
    }

    public int getRecursoIDW() {
        return recursoIDW;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getCantidad() {
        return cantidad;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> auxRetornar = new HashMap<>();

        auxRetornar.put("recurso", getRecurso());
        auxRetornar.put("recursoIDW", getRecursoIDW());
        auxRetornar.put("descripcion", getDescripcion());
        auxRetornar.put("cantidad", getCantidad());

        return auxRetornar;
    }
}
