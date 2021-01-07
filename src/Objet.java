public class Objet extends Case{
    protected String label;

    public Objet(String label) {
        this.label = label;
    }

    public String toString(){
        return label;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
