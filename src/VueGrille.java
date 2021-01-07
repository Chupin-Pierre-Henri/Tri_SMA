import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.Observable;
import java.util.Observer;

public class VueGrille implements Observer {
    protected Scene scene;
    HBox hbox = new HBox();
    Grille grille;
    GridPane gridPane = new GridPane();

    public VueGrille(int width, int height, Grille grille) {
        this.grille = grille;
        this.initView();
        this.scene = new Scene(this.hbox, width, height);
    }

    protected void initView() {
        this.updateGrid(true);
        this.hbox.setPadding(new Insets(10));
        HBox rightHbox = new HBox();
        rightHbox.setSpacing(10);
        rightHbox.setPadding(new Insets(10));
        Button runBtn1 = new Button("Run 50000 steps Q1");
        Button runBtn2 = new Button("Run 50000 steps Q2");
        rightHbox.getChildren().addAll(runBtn1,runBtn2);
        this.hbox.getChildren().addAll(gridPane, rightHbox);
        runBtn1.setOnAction(actionEvent -> {
            System.out.println("start");
            grille.run(1);
            update();

        });
        runBtn2.setOnAction(actionEvent -> {
            System.out.println("start");
            grille.run(2);
            update();

        });
    }

    public void update() {
        this.updateGrid(false);
    }

    private Color getColor(Case gridCase) {
        if (gridCase == null) return  Color.WHITE;

        if (gridCase.getClass().equals(Agent.class)) return Color.RED;

        Objet obj = (Objet) gridCase;

        if (obj.getLabel().equals("A")) return Color.GREEN;

        return Color.BLUE;
    }

    private void updateGrid(boolean init) {
        for (int i = 0; i < this.grille.getN(); i++) {
            for (int j = 0; j < this.grille.getM(); j++) {
                Case gridCase = this.grille.getGrille()[i][j];
                Circle caseCircle = new Circle(3);
                caseCircle.setFill(this.getColor(gridCase));
                gridPane.add(caseCircle, i, j);
            }
        }
    }

    public Scene getScene() {
        return scene;
    }

    @Override
    public void update(Observable o, Object arg) {
        this.update();
    }
}
