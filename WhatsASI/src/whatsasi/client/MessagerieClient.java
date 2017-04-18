package whatsasi.client;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.util.logging.Filter;

public class MessagerieClient extends Application {
    //==== Menu nodes
    TitledPane connexionPane = new TitledPane();
    TitledPane chatPane = new TitledPane();
    TitledPane filterPane = new TitledPane();
    Accordion accordion = new Accordion(connexionPane, filterPane, chatPane);
    Rectangle2D screenSize = Screen.getPrimary().getBounds();

    //==== Connexion nodes
    ImageView avatar = new ImageView(new Image("https://i1.social.s-msft.com/profile/u/avatar.jpg?displayname=kabir+shenvi&size=extralarge&version=00000000-0000-0000-0000-000000000000", 120, 120, true, false));
    TextField pseudoTextField = new TextField();
    Button connexionOK = new Button("Se connecter");

    //==== Filter nodes
    TextField filterTextField = new TextField();
    Button addFilteredWord = new Button("Ajouter");
    Label filterTextFieldAlert = new Label("Ce mot est déjà dans la liste !");
    ObservableList<String> filterList;
    Button filterOK = new Button("Valider");

    //==== Chat nodes
    ObservableList<String> items;
    ObservableList<String> convList;
    Button addConvButton;
    TextField nouveauMessage;
    Button sendMessage;

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

        filterList = FXCollections.observableArrayList("putain");/*new FilterCell("putain"),
                new FilterCell("Voldemort"),
                new FilterCell("Donald Trump"));*/
        FilterListView filterListView = new FilterListView(filterList);

        vbox.getChildren().add(new Label("Saisissez les mots à filtrer. Les mots filtrés seront remplacés " +
                                         "par des étoiles dans les conversations. Par exemple, \"gros mot\" sera " +
                                         "remplacé par \"**** ***\". Le filtrage est indépendant de la casse"));
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(6, 6, 6, 6));
        grid.setVgap(6);
        grid.setHgap(6);
        grid.add(new Label("Saisissez un mot-filtre :"), 0, 0);
        grid.add(filterTextField, 1, 0);
        grid.add(addFilteredWord, 2, 0);
        filterTextFieldAlert.setStyle("-fx-text-fill: red");
        filterTextFieldAlert.setVisible(false);
        grid.add(filterTextFieldAlert, 1, 1);
        grid.setAlignment(Pos.BASELINE_LEFT);
        addFilteredWord.setOnAction(new AddFilteredWordEventHandler());
        vbox.getChildren().add(grid);

        vbox.getChildren().add(filterListView);

        filterOK.setAlignment(Pos.BASELINE_RIGHT);
        filterOK.setOnAction(new FilterEventHandler());

        vbox.getChildren().add(filterOK);

        filterPane.setText("Filtres");

        filterPane.setContent(vbox);
    }

    private class FilterEventHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent e) {
            chatPane.setExpanded(true);
        }
    }

    public class AddFilteredWordEventHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent e) {
            String text = filterTextField.getText().trim().toLowerCase();
            if (!filterList.contains(text)) {
                System.out.println("Mot-filtre ajouté : " + text);
                filterList.add(text);
                filterTextFieldAlert.setVisible(false);
            } else {
                filterTextFieldAlert.setVisible(true);
            }
        }
    }

    public class FilterListView extends ListView<String> {
        public FilterListView() {
            super();
            initCellFactory();
        }

        public FilterListView(ObservableList<String> items) {
            super(items);
            initCellFactory();
        }

        public void initCellFactory() {
            setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
                @Override
                public ListCell<String> call(ListView<String> param) {
                    return new FilterCell();
                }
            });
        }
    }

    public class FilterCell extends ListCell<String> {
        private TextField textField = new TextField("");
        private Button plus = new Button("+");
        private Button minus = new Button("-");

        public FilterCell() {
            setTextField(null);
            initCell();
        }

        public FilterCell(String text) {
            setTextField(text);
            initCell();
        }

        public void setTextField(String text) {
            if (text != null) {
                text = text.trim().toLowerCase();
            }
            setText(null);
            textField.setText(text);
        }

        public void initCell() {
            GridPane gridPane = new GridPane();
            gridPane.add(textField, 0, 0);
            gridPane.add(plus, 1, 0);
            gridPane.add(minus, 2, 0);
            setGraphic(gridPane);
        }

        @Override
        public void updateItem(String text, boolean empty) {
            super.updateItem(text, empty);
            if (empty) {
                // setTextField(null);
                setGraphic(null);
            } else {
                setTextField(text);
                initCell();
            }
        }

        @Override
        public String toString() {
            return textField.getText();
        }
    }

    private void initConversationPane() {
        chatPane.setText("Chat");
        GridPane grid = new GridPane();
        grid.setHgap(5);
        grid.setVgap(5);
        grid.setPadding(new Insets(0, 5, 0, 5));

        ListView<String> listeConv = new ListView<String>();
        items =FXCollections.observableArrayList("Conversation 1", "Conversation 2", "Conversation 3", "Conversation 4");
        listeConv.setItems(items);
        listeConv.setPrefHeight(0.7*screenSize.getHeight());

        addConvButton = new Button("+");

        VBox vboxConv = new VBox();
        vboxConv.setPadding(new Insets(10));
        vboxConv.setSpacing(8);

        vboxConv.getChildren().add(listeConv);
        vboxConv.getChildren().add(addConvButton);


        VBox vbox = new VBox();
        vbox.setPadding(new Insets(10));
        vbox.setSpacing(8);

        convList = FXCollections.observableArrayList("C'est qui ?", "C'est Pinpin !");
        ConvListView convListView = new ConvListView(convList);

        convListView.setPrefHeight(0.7*screenSize.getHeight());
        convListView.setPrefWidth(0.75*screenSize.getWidth());


        nouveauMessage = new TextField();
        nouveauMessage.setPromptText("Nouveau message");
        sendMessage = new Button("Envoyer");
        nouveauMessage.setPrefWidth(0.65*screenSize.getWidth());
        sendMessage.setPrefWidth(0.10*screenSize.getWidth());
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(15, 12, 15, 12));
        hbox.setSpacing(10);
        hbox.getChildren().add(nouveauMessage);
        hbox.getChildren().add(sendMessage);


        vbox.getChildren().add(convListView);
        vbox.getChildren().add(hbox);

        grid.add(vboxConv, 1, 1);
        grid.add(vbox, 3, 1);
        chatPane.setContent(grid);
    }



    public class ConvListView extends ListView<String> {
        public ConvListView() {
            super();
            initMessageCellFactory();
        }

        public ConvListView(ObservableList<String> items) {
            super(items);
            initMessageCellFactory();
        }

        public void initMessageCellFactory() {
            setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
                @Override
                public ListCell<String> call(ListView<String> param) {
                    return new MessageCell();
                }
            });
        }
    }

    public class MessageCell extends ListCell<String> {
        private Label texte = new Label("Contenu");
        private ImageView avatarMessage = new ImageView(new Image("https://i1.social.s-msft.com/profile/u/avatar.jpg?displayname=kabir+shenvi&size=extralarge&version=00000000-0000-0000-0000-000000000000", 45, 45, true, false));

        public MessageCell() {
            setTexte(null);
            initCell();
        }

        public MessageCell(String text) {
            setTexte(text);
            initCell();
        }

        public void setTexte(String text) {
            if (text != null) {
                text = text.trim().toLowerCase();
            }
            setText(null);
            texte.setText(text);
        }

        public void initCell() {
            GridPane gridPane = new GridPane();
            gridPane.setHgap(10);
            gridPane.setVgap(10);
            gridPane.setPadding(new Insets(15, 15, 15, 15));
            gridPane.add(avatarMessage, 0, 0);
            gridPane.add(texte, 2, 0);
            setGraphic(gridPane);
        }

        @Override
        public void updateItem(String text, boolean empty) {
            super.updateItem(text, empty);
            if (empty) {
                // setTextField(null);
                setGraphic(null);
            } else {
                setTexte(text);
                initCell();
            }
        }

        @Override
        public String toString() {
            return texte.getText();
        }
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
