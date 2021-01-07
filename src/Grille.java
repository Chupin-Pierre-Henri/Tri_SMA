import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Random;

public class Grille extends Observable {
    protected Case[][] grille;
    protected int n;
    protected int m;
    protected int nbAgent;
    protected int nbA;
    protected int nbB;
    protected Agent[] agents;
    protected double kplus;
    protected double kmoins;
    protected int q;



    public Grille(double kplus, double kmoins, int n, int m, int nbAgent, int nbA, int nbB) {
        grille = new Case[n][m];
        this.kplus = kplus;
        this.kmoins = kmoins;
        this.n = n;
        this.m = m;
        this.nbAgent = nbAgent;
        this.nbA = nbA;
        this.nbB = nbB;
        this.placeAgent(nbAgent);
        this.placeA(nbA);
        this.placeB(nbB);
    }

    /**
     * récupère le placement de l'agent a
     * @param a l'agent
     * @return le placement de l'agent
     */
    public int[] placementAgent(Agent a) {
        int[] coord = new int[2];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (a.equals(this.grille[i][j])) {
                    coord[0] = i;
                    coord[1] = j;
                    return coord;
                }
            }
        }
        return null;
    }

    /**
     * place les agents dans la grille à l'initialisation
     * @param nbAgent nombre d'agent
     */
    private void placeAgent(int nbAgent) {
        int nbAgentsPlace = 0;
        Random random = new Random();
        this.agents = new Agent[nbAgent];
        while(nbAgentsPlace < nbAgent) {
            int i = random.nextInt(n);
            int j = random.nextInt(m);
            if(this.grille[i][j] == null) {
                this.agents[nbAgentsPlace] = new Agent(nbAgentsPlace, 10);
                this.grille[i][j] = this.agents[nbAgentsPlace];
                nbAgentsPlace++;
            }
        }
    }

    /**
     * place les objet de type A dans la grille à l'initialisation
     * @param nbA nombre d'objet A
     */
    private void placeA(int nbA) {
        int nbAPlace = 0;
        Random random = new Random();
        while(nbAPlace < nbA) {
            int i = random.nextInt(n);
            int j = random.nextInt(m);
            if(this.grille[i][j] == null) {
                this.grille[i][j] = new Objet("A");
                nbAPlace++;
            }
        }
    }

    /**
     * place les objet de type B dans la grille à l'initialisation
     * @param nbB nombre d'objet B
     */
    private void placeB(int nbB) {
        int nbBPlace = 0;
        Random random = new Random();
        while(nbBPlace < nbB) {
            int i = random.nextInt(n);
            int j = random.nextInt(m);
            if(this.grille[i][j] == null) {
                this.grille[i][j] = new Objet("B");
                nbBPlace++;
            }
        }
    }

    public int getN() {
        return n;
    }

    public int getM() {
        return m;
    }

    public Case[][] getGrille() {
        return grille;
    }

    /**
     * récupère les coordonnées voisins de l'agent
     * @param a l'agent chez qui on cherche les coordonnées voisine
     * @return les coordonnées voisines a l'agent
     */
    public int[][] getVoisins(Agent a) {
        int[] coordAgent = placementAgent(a);
        int[][] voisins = new int[4][2];
        voisins[0][0] = coordAgent[0]+1;
        voisins[0][1] = coordAgent[1];

        voisins[1][0] = coordAgent[0]-1;
        voisins[1][1] = coordAgent[1];

        voisins [2][0] = coordAgent[0];
        voisins [2][1] = coordAgent[1]+1;

        voisins [3][0] = coordAgent[0];
        voisins [3][1] = coordAgent[1]-1;
        return voisins;
    }

    /**
     * permet d'avoir les positions des différentes case libre autour de l'agent
     * @param agent l'agent chez qui on cherche les places libre autour de lui
     * @return la liste des coordonées des cases libre autour de l'agent
     */
    public int[][] getPlaceLibreAutour(Agent agent) {
        int[][] voisins = agent.voisins;
        List<int[]> placeLibre = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            if(estValide(voisins[i]) && this.grille[voisins[i][0]][voisins[i][1]] == null) {
                placeLibre.add(voisins[i]);
            }
        }
        int[][] placeLibreAutour = new int[placeLibre.size()][placeLibre.size()];
        for (int i = 0; i< placeLibre.size(); i++){
            placeLibreAutour[i]=placeLibre.get(i);
        }
        return placeLibreAutour;
    }

    /**
     * permet de savoir si la position est valide dans la grille (ne dépace pas les limites de taille de la grille
     * @param position la position tester
     * @return true si la position est valide et false sinon
     */
    private boolean estValide(int[] position) {
        if (position[0] < 0 || position[0] >= n) {
            return false;
        }
        return position[1] >= 0 && position[1] < m;
    }

    /**
     * déplace l'agent dans une nouvelle casse de la grille
     * @param direction les coordonné de la nouvelle case
     * @param a l'agent déplacé
     */
    public void deplacer(int[] direction, Agent a){
        int i = direction[0];
        int j = direction[1];
        if(estValide(direction)){
            retireAgent(a);
            this.grille[i][j] = agents[a.getId()];
        }
    }

    /**
     * permet de retirer l'agent a de la grille
     * @param a l'agent à retirer
     */
    private void retireAgent(Agent a) {
        for(int i= 0; i< this.n; i++){
            for(int j =0; j<this.m; j++){
                if (this.grille[i][j] == a) {
                    this.grille[i][j] = null;
                    return;
                }
            }
        }
    }

    /**
     * permet d'avoir les coordonnées d'un seul objet voisin à l'agent
     * @param agent l'agent qui fait l'action
     * @return les coordonnées d'un seul objet voisin à l'agent
     */
    public int[] getOneObjetVoisins(Agent agent) {
        int[][] voisins = agent.voisins;
        List<int[]> placeNonLibre = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            if(estValide(voisins[i]) && this.grille[voisins[i][0]][voisins[i][1]] != null && (this.grille[voisins[i][0]][voisins[i][1]].toString().equals("A")|| this.grille[voisins[i][0]][voisins[i][1]].toString().equals("B"))) {
                placeNonLibre.add(voisins[i]);
            }
        }
        int[][] placeNonLibreAutour = new int[placeNonLibre.size()][placeNonLibre.size()];
        for (int i = 0; i< placeNonLibre.size(); i++){
            placeNonLibreAutour[i]=placeNonLibre.get(i);
        }
        Random random = new Random();
        if(placeNonLibreAutour.length > 0){
            int[] objetVoisin = placeNonLibreAutour[random.nextInt(placeNonLibreAutour.length)];
            String s = this.grille[objetVoisin[0]][objetVoisin[1]].toString();
            if(s.equals("null")){
                s = "0";
            }
            agent.setMemoire(agent.getMemoire()+s);
            if (agent.getMemoire().length() > agent.getTailleMemoire()){
                agent.setMemoire((String) agent.getMemoire().subSequence(1,agent.getTailleMemoire()));
            }
            return objetVoisin;
        }
        return null;
    }

    /**
     * calcul la proportion de l'objet dans la mémoire de l'agent
     * @param agent l'agent qui fait l'action
     * @param nomObjet le nom de l'objet en interaction avec l'agent
     * @param action l'action voulu 1 prend l'objet, 2 pose l'objet
     * @return la proportion de l'objet présent dans la mémoire
     */
    private double fProportion1(Agent agent,String nomObjet, int action) {
        int fp = 0;
        if(action == 1){
            int cmp = 0;
            int[][] tab = getVoisins(agent);
            for (int[] ints : tab) {
                if (estValide(ints)) {
                    cmp++;
                    if (grille[ints[0]][ints[1]] != null && grille[ints[0]][ints[1]].toString().equals(nomObjet)) {
                        fp++;
                    }
                }
            }
            return (double) fp/cmp;
        }
        else{
            for(int i=0; i<agent.getMemoire().length(); i++){
                if(agent.getMemoire().charAt(i) == nomObjet.charAt(0)){
                    fp++;
                }
            }
            return (double) fp/agent.getMemoire().length();
        }

    }

    /**
     * calcul la proportion de l'objet dans la mémoire de l'agent avec un taux d'erreur de 0,1 sur les autres objets
     * @param agent l'agent qui fait l'action
     * @param nomObjet le nom de l'objet en interaction avec l'agent
     * @param action l'action voulu 1 prend l'objet, 2 pose l'objet
     * @return la proportion de l'objet présent dans la mémoire
     */
    private double fProportion2(Agent agent,String nomObjet, int action) {
        int fp = 0;
        int fnonp = 0;
        if(action == 1){
            int cmp = 0;
            int[][] tab = getVoisins(agent);
            for (int[] ints : tab) {
                if (estValide(ints)) {
                    cmp++;
                    if (grille[ints[0]][ints[1]] != null && grille[ints[0]][ints[1]].toString().equals(nomObjet)) {
                        fp++;
                    }
                }
            }
            return (double) fp/cmp;
        }
        else{
            for(int i=0; i<agent.getMemoire().length(); i++){
                if(agent.getMemoire().charAt(i) == nomObjet.charAt(0)){
                    fp++;
                }
                else if(agent.getMemoire().charAt(i) != '0'){
                    fnonp++;
                }
            }
            return (fp + 0.1*fnonp) /agent.getMemoire().length();
        }
    }

    /**
     * tester si l'agent peut prendre l'objet dans un direction donnée en fonction de la probabilité de prise
     * @param agent l'agent qui veut prendre
     * @param directionChop la direction de l'objet à prendre
     * @return true si il peut prendre false sinon
     */
    public boolean peuxPrendre(Agent agent, int[] directionChop) {
        double res;
        if(this.q==1){
            res = Math.pow(this.kplus /(this.kplus + fProportion1(agent,this.grille[directionChop[0]][directionChop[1]].toString(),2)),2);
        }
        else{
            res = Math.pow(this.kplus /(this.kplus + fProportion2(agent,this.grille[directionChop[0]][directionChop[1]].toString(),2)),2);
        }
        Random random = new Random();
        double i = random.nextDouble();

        return i < res;
    }

    /**
     * prend l'objet en position directionChop et l'enlève de la grille le temps que l'agent le replace
     * @param agent l'agent qui prend
     * @param directionChop la position de l'objet pris
     */
    public void prend(Agent agent, int[] directionChop) {
        if(this.grille[directionChop[0]][directionChop[1]].toString().equals("A")){
            agent.setPorte(1);
        }
        if(this.grille[directionChop[0]][directionChop[1]].toString().equals("B")){
            agent.setPorte(2);
        }
        this.grille[directionChop[0]][directionChop[1]] = null;
    }

    /**
     * tester si l'agent peut poser l'objet dans un direction donnée en fonction de la probabilité de pose
     * @param agent l'agent qui veut prendre
     * @param ouPoserObjet la direction de pose de l'objet
     * @return true si il peut poser false sinon
     */
    public boolean peuxPoser(Agent agent, int[] ouPoserObjet) {
        String objetPorter;
        double res;
        if(agent.getPorte() == 1){
            objetPorter = "A";
        }
        else{
            objetPorter = "B";
        }
        if(this.q ==1){
            res = Math.pow(fProportion1(agent,objetPorter,1) /(this.kmoins + fProportion1(agent,objetPorter,1)),2);
        }
        else{
            res = Math.pow(fProportion2(agent,objetPorter,1) /(this.kmoins + fProportion2(agent,objetPorter,1)),2);
        }
        Random random = new Random();
        double i = random.nextDouble();
        return i < res;
    }

    /**
     * pose l'objet en position ouPoserObjet et le rajoute à la grille
     * @param agent
     * @param ouPoserObjet
     */
    public void pose(Agent agent, int[] ouPoserObjet) {
        if(agent.getPorte()==1){
            this.grille[ouPoserObjet[0]][ouPoserObjet[1]] = new Objet("A");
        }
        else{
            this.grille[ouPoserObjet[0]][ouPoserObjet[1]] = new Objet("B");
        }
        agent.setPorte(0);
    }

    public void run(int q) {
        this.q = q;
        for (int i = 0; i < 50000; i++) {
            //System.out.println("i : "+ i);
            for(Agent a : this.agents){
                a.perception(this);
                a.action(this);
            }
        }
    }
}
