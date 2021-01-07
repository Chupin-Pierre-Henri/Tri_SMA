import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {

    public static void main(String[] args) {
        launch();
    }

    public void start(Stage stage) throws Exception {
        int n = 50, m = 50;
        Grille g = new Grille(0.1, 0.3,n, m, 20, 200, 200);
        VueGrille vG = new VueGrille(n*13, m*8, g);
        g.addObserver(vG);
            stage.setScene(vG.getScene());
            stage.show();
    }

}
