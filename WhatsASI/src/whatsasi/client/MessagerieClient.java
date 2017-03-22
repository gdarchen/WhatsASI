package whatsasi.client;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class MessagerieClient extends Application {
    TitledPane connexionPane = new TitledPane();
    TitledPane conversationPane = new TitledPane();
    TitledPane filtrePane = new TitledPane();
    Accordion accordion = new Accordion(connexionPane, filtrePane, conversationPane);
    Rectangle2D tailleEcran = Screen.getPrimary().getBounds();

    ImageView avatar = new ImageView("https://i1.social.s-msft.com/profile/u/avatar.jpg?displayname=kabir+shenvi&size=extralarge&version=00000000-0000-0000-0000-000000000000");
    TextField pseudoTextField = new TextField();

    public void initConnexionPane() {
        VBox vbox = new VBox(8);
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().add(new Label("Bienvenue sur WhatsASI !"));

        vbox.getChildren().add(avatar);

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(6, 6, 6, 6));
        grid.setVgap(6);
        grid.setHgap(6);
        grid.add(new Label("Pseudo :"), 0, 0);
        grid.add(pseudoTextField, 1, 0);
        grid.setAlignment(Pos.CENTER);
        vbox.getChildren().add(grid);


        connexionPane.setText("Connexion");
        connexionPane.setContent(vbox);
    }

    @Override
    public void init() {
        initConnexionPane();

        filtrePane.setText("Filtres");

        conversationPane.setText("Conversations");
    }

    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(accordion, tailleEcran.getWidth(), tailleEcran.getHeight());

        stage.setTitle("WhatsASI : La messagerie révolutionnaire d'hier");
        stage.setScene(scene);
        // stage.setFullScreen(true);
        stage.show();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
