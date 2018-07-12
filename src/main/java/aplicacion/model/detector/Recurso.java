package aplicacion.model.detector;

import operativa.system.Resource;

public class Recurso {
    private int id;
    private String name;
    private String stemm;
    private int cant = -1;
    private boolean isValid;
    private boolean isCoincidence;
    private boolean isNew;

    public Recurso(Resource resource) {
        this.id = resource.getID();
        this.name = resource.getName();
        this.stemm = resource.getStemm();
        this.cant = resource.getCant();
        this.isValid = resource.isValid();
        this.isCoincidence = resource.isCoincidence();
        this.isNew = resource.isNew();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getStemm() {
        return stemm;
    }

    public int getCant() {
        return cant;
    }

    public boolean isValid() {
        return isValid;
    }

    public boolean isCoincidence() {
        return isCoincidence;
    }

    public boolean isNew() {
        return isNew;
    }

    //TODO: lo ideal sería pasarlo a JSON
    @Override
    public String toString() {
        return "Recurso{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", stemm='" + stemm + '\'' +
                ", cant=" + cant +
                ", isValid=" + isValid +
                ", isCoincidence=" + isCoincidence +
                ", isNew=" + isNew +
                '}';
    }
}
