import java.util.Objects;
import java.util.Random;

public class Agent extends Case {
    protected int id;
    protected String memoire ="0";
    protected int tailleMemoire;
    protected int[][] voisins;
    protected int[][] placeLibreAutour;
    protected int porte =0; //0 porte rien/ 1 porte A/ 2 porte B


    public Agent(int id, int tailleMemoire){
        this.id = id;
        this.tailleMemoire = tailleMemoire;
    }

    /**
     * regarde l'état de la grille pour mettre à jour les informations de l'agent
     * @param g la grille liée à l'agent
     */
    public void perception(Grille g) {
        this.voisins = g.getVoisins(this);
        this.placeLibreAutour = g.getPlaceLibreAutour(this);
    }

    /**
     * fait ses actions dans la grille (ce déplace, prend/pose un objet)
     * @param g la grille associé
     */
    public void action(Grille g) {
        Random random = new Random();
        /* On regarde s'il y a au moins une place disponible */
        if(this.placeLibreAutour.length > 0) {
            int[] seDeplaceVers = placeLibreAutour[random.nextInt(placeLibreAutour.length)];
            g.deplacer(seDeplaceVers, this);
            perception(g);
            if(porte !=0){
                int[] ouPoserObjet =  placeLibreAutour[random.nextInt(placeLibreAutour.length)];
                if(g.peuxPoser(this,ouPoserObjet)){
                    g.pose(this, ouPoserObjet);
                }
            }
        }
        if(porte == 0){
            int[] directionChop = g.getOneObjetVoisins(this);
            if(directionChop != null){
                if(g.peuxPrendre(this, directionChop)){
                    g.prend(this,directionChop);
                }
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Agent agent = (Agent) o;
        return id == agent.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public String toString() {
        return "" + id;
    }

    public int getId() {
        return id;
    }

    public String getMemoire() {
        return memoire;
    }

    public void setMemoire(String memoire) {
        this.memoire = memoire;
    }

    public int getTailleMemoire() {
        return tailleMemoire;
    }

    public int getPorte() {
        return porte;
    }

    public void setPorte(int porte) {
        this.porte = porte;
    }
}
