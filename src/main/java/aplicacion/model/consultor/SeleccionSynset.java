package aplicacion.model.consultor;

public class SeleccionSynset {
    private String recurso;
    private int idSynset;
    private String sinonimos;
    private boolean donacion;

    public SeleccionSynset(String recurso, int idSynset, String sinonimos, boolean donacion) {
        this.recurso = recurso;
        this.idSynset = idSynset;
        this.sinonimos = sinonimos;
        this.donacion = donacion;
    }

    public String getRecurso() {
        return recurso;
    }

    public int getIdSynset() {
        return idSynset;
    }

    public String getSinonimos() {
        return sinonimos;
    }

    public boolean isDonacion() {
        return donacion;
    }
}
