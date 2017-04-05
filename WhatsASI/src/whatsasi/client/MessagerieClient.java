package whatsasi.client;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.Observable;

public class MessagerieClient extends Application {
    TitledPane connexionPane = new TitledPane();
    TitledPane chatPane = new TitledPane();
    TitledPane filterPane = new TitledPane();
    Accordion accordion = new Accordion(connexionPane, filterPane, chatPane);
    Rectangle2D screenSize = Screen.getPrimary().getBounds();

    ImageView avatar = new ImageView(new Image("https://i1.social.s-msft.com/profile/u/avatar.jpg?displayname=kabir+shenvi&size=extralarge&version=00000000-0000-0000-0000-000000000000", 120, 120, true, false));
    TextField pseudoTextField = new TextField();
    Button connexionOK = new Button("Se connecter");
    Button filterOK = new Button("Valider");

    private void initConnexionPane() {
        VBox vbox = new VBox(12);
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().add(new Label("Bienvenue sur WhatsASI !"));

        vbox.getChildren().add(avatar);
        vbox.getChildren().add(new Label("Avatar"));

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(6, 6, 6, 6));
        grid.setVgap(6);
        grid.setHgap(6);
        grid.add(new Label("Pseudo :"), 0, 0);
        grid.add(pseudoTextField, 1, 0);
        grid.setAlignment(Pos.CENTER);
        vbox.getChildren().add(grid);

        vbox.getChildren().add(connexionOK);
        connexionOK.setOnAction(new ConnexionEventHandler());


        connexionPane.setText("Connexion");
        connexionPane.setContent(vbox);
    }

    private class ConnexionEventHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent e) {
            filterPane.setExpanded(true);
        }
    }

    private void initFilterPane() {
        VBox vbox = new VBox(12);


        FilterListView filterListView = new FilterListView();
        
        vbox.getChildren().add(filterOK);

        filterPane.setText("Filtres");
    }

    public class FilterListView extends ListView<FilterCell> {
        public FilterListView() {
            super();
        }

        public FilterListView(ObservableList<FilterCell> items) {
            super(items);
        }
    }

    public class FilterCell extends ListCell<String> {
        private TextField text = new TextField("");
        private Button plus = new Button("+");
        private Button minus = new Button("-");

        public FilterCell(String text) {
            setTextField(text);
            initLayout();
        }

        public void setTextField(String text) {
            setText(text);
            this.text.setText(text);
        }

        public void initLayout() {
            GridPane gridPane = new GridPane();
            gridPane.add(text, 0, 0);
            gridPane.add(plus, 1, 0);
            gridPane.add(minus, 2, 0);
            setGraphic(gridPane);
        }

        @Override
        public void updateItem(String text, boolean empty) {
            super.updateItem(text, empty);
            if (empty) {
                setTextField(null);
                setGraphic(null);
            } else {
                setTextField(text);
                initLayout();
            }
        }
    }

    private void initConversationPane() {
        chatPane.setText("Chat");
    }

    @Override
    public void init() {
        initConnexionPane();
        initFilterPane();
        initConversationPane();
    }

    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(accordion, screenSize.getWidth(), screenSize.getHeight());

        stage.setTitle("WhatsASI : La messagerie révolutionnaire d'hier");
        stage.setScene(scene);
        // stage.setFullScreen(true);
        stage.show();

        connexionPane.setExpanded(true);
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}