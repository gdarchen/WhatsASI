// si on veut faire visio : GStreamer

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
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Popup;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.util.Callback;
import javafx.beans.value.*;
import java.io.File;
import javafx.stage.FileChooser;
import java.awt.Desktop;


// import javafx.scene.paint.Color;
// import javafx.scene.shape.Circle;
import java.text.DateFormat;
import java.util.*;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import whatsasi.serveur.conversations.MessagerieInterface;
import whatsasi.serveur.conversations.Conversation;
import whatsasi.serveur.conversations.Mode;
import whatsasi.serveur.filtrage.Filtre;
import whatsasi.serveur.conversations.Message;


import java.util.logging.Filter;

public class MessagerieClient extends Application {
    private static final int PORTRMI = 1099;
    private static final String ENDPOINT = "localhost";
    private static Stage primaryStage;
    private static MessagerieInterface messagerie;
    private static String pseudo;
    private static int refConv = -1 ;
    private static MessageCallbackInterface callback = null;

    //==== Menu nodes
    TitledPane connexionPane = new TitledPane();
    TitledPane chatPane = new TitledPane();
    TitledPane filterPane = new TitledPane();
    Accordion accordion = new Accordion(connexionPane, filterPane, chatPane);
    Rectangle2D screenSize = Screen.getPrimary().getBounds();
    private Desktop desktop = Desktop.getDesktop();

    //==== Connexion nodes
    private ImageView avatarView = new ImageView(new Image("https://i1.social.s-msft.com/profile/u/avatar.jpg?displayname=kabir+shenvi&size=extralarge&version=00000000-0000-0000-0000-000000000000", 120, 120, true, false));
    TextField pseudoTextField = new TextField();
    Label pseudoTextFieldAlert = new Label("Ce pseudo est déjà pris !");
    Button photoButton = new Button("Changer d'avatar");
    final FileChooser fileChooser = new FileChooser();
    Button connexionOK = new Button("Se connecter");

    //==== Filter nodes
    TextField filterTextField = new TextField();
    Button addFilteredWord = new Button("Ajouter");
    Label filterTextFieldAlert = new Label("Ce mot est déjà dans la liste !");
    ObservableList<String> filterList;
    Button filterOK = new Button("Valider");

    //==== Chat nodes
    private static ObservableList<String> items;
    ListView<String> listeConv;
    private static ObservableList<Message> messagesList;
    Button addConvButton;
    TextField nouveauMessage;
    Button sendMessage;
    private static MessagesListView messagesListView;


    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry(ENDPOINT, PORTRMI);
            messagerie = (MessagerieInterface) registry.lookup("Messagerie");

            //System.out.println(getConversationsTitre(messagerie));
            Application.launch(args);
            //connection();
            //interceptShutdown();
            //System.out.println(messagerie.sayHi());
            //displayBACKCHAR();
            //createAccount(messagerie);
            //indexActions(messagerie);
        } catch (RemoteException e) {
            e.toString();
            e.printStackTrace();
        } catch (Exception e) {
            e.toString();
            e.printStackTrace();
        }
    }

    @Override
    public void init() {
        try {
            initConnexionPane();
            initFilterPane();
            initConversationPane();
        } catch (RemoteException e) {
            e.toString();
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage stage) {
        setPrimaryStage(stage);
        Scene scene = new Scene(accordion, screenSize.getWidth(), screenSize.getHeight());

        stage.setTitle("WhatsASI : La messagerie révolutionnaire d'hier");
        stage.setScene(scene);
        // stage.setFullScreen(true);
        stage.show();

        connexionPane.setExpanded(true);
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    private void setPrimaryStage(Stage primaryStage) {
        MessagerieClient.primaryStage = primaryStage;
    }


    /* * * * * * * * * * * * * * * * * * */
    /* * * * * * * Connection * * * * * * */
    /* * * * * * * * * * * * * * * * * * */

    private void initConnexionPane() {
        VBox vbox = new VBox(12);
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().add(new Label("Bienvenue sur WhatsASI !"));

        vbox.getChildren().add(avatarView);
        vbox.getChildren().add(photoButton);

        photoButton.setOnAction(e -> {
            fileChooser.setTitle("View Pictures");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("All Images", "*.*"),
                    new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                    new FileChooser.ExtensionFilter("PNG", "*.png")
            );
            fileChooser.setInitialDirectory(new File("./res"));
            File file = fileChooser.showOpenDialog(getPrimaryStage());

            if (file != null) {
                avatarView.setImage(new Image(file.toURI().toString()));
            }
        });

        pseudoTextFieldAlert.setStyle("-fx-text-fill: red");
        pseudoTextFieldAlert.setVisible(false);

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(6, 6, 6, 6));
        grid.setVgap(6);
        grid.setHgap(6);
        grid.add(new Label("Pseudo :"), 0, 0);
        grid.add(pseudoTextField, 1, 0);
        grid.add(pseudoTextFieldAlert, 1, 1);
        grid.setAlignment(Pos.CENTER);
        vbox.getChildren().add(grid);

        vbox.getChildren().add(connexionOK);
        connexionOK.setOnAction(new ConnexionEventHandler());
        connexionOK.setDefaultButton(true);

        connexionPane.setText("Connexion");
        connexionPane.setContent(vbox);
    }

    private class ConnexionEventHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent e) {
            try {
                pseudo = pseudoTextField.getText();
                if (pseudo.isEmpty()) {
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Erreur");
                    alert.setHeaderText("Vous n'avez pas de pseudo");
                    alert.setContentText("Saisissez un pseudo, les autres utilisateurs aiment savoir avec qui ils " +
                                         "parlent.");
                    alert.showAndWait();
                } else {
                    if (messagerie.isPseudoAvailable(pseudo)) {
                        // Ajouter avatarView, mode, filtre
                        messagerie.creerCompte(pseudo, avatarView.getImage(), Mode.DEFAUT, null);
                        pseudoTextFieldAlert.setVisible(false);
                        filterPane.setCollapsible(true);
                        filterPane.setExpanded(true);
                    } else {
                        pseudoTextFieldAlert.setVisible(true);
                        filterPane.setCollapsible(false);
                        chatPane.setCollapsible(false);
                    }
                }
            } catch (RemoteException ex) {
                ex.toString();
                ex.printStackTrace();
            }
        }
    }

    /* * * * * * * * * * * * * * * * * * */
    /* * * * * * * Filter * * * * * * * */
    /* * * * * * * * * * * * * * * * * * */


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
        filterOK.setDefaultButton(true);

        vbox.getChildren().add(filterOK);

        filterPane.setText("Filtres");

        filterPane.setContent(vbox);

        filterPane.setCollapsible(false);
    }

    private class FilterEventHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent e) {
            chatPane.setCollapsible(true);
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


    /* * * * * * * * * * * * * * * * * * */
    /* * * * * * * Conversation * * * * * */
    /* * * * * * * * * * * * * * * * * * */
    private void initConversationPane() throws RemoteException{
        chatPane.setText("Chat");
        GridPane grid = new GridPane();
        grid.setHgap(5);
        grid.setVgap(5);
        grid.setPadding(new Insets(0, 5, 0, 5));

        chatPane.setCollapsible(false);

        listeConv = new ListView<String>();

        if (!getConversationsTitre().isEmpty())
            items = FXCollections.observableArrayList(getConversationsTitre());
        else
            items = FXCollections.observableArrayList();

        listeConv.setItems(items);
        listeConv.setPrefHeight(0.7*screenSize.getHeight());

        listeConv.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                try{
                    if (newValue!=null) {
                        refConv = getRefConv(newValue);
                        // La conversation n'existe pas
                        if (refConv == -1) {
                            refConv = createNewConv(newValue);
                        }
                        else{
                            callback = new IHMMessageCallback(refConv, pseudo);
                            messagerie.addUserToConv(pseudo, refConv, callback);
                        }
                        List<Message> contenu = messagerie.getContenu(refConv, pseudo);
                        if (!contenu.isEmpty()) {
                            messagesList = FXCollections.observableArrayList(contenu);
                        }
                        else{
                            messagesList = FXCollections.observableArrayList();
                        }
                        messagesListView.setItems(messagesList);
                    }
                } catch (RemoteException e) {
                    e.toString();
                    e.printStackTrace();
                }
            }
        });


        addConvButton = new Button("+");
        addConvButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent event) {
                TextInputDialog dialog = new TextInputDialog("Nouvelle Conversation");
                dialog.setTitle("Créer une nouvelle conversation");
                dialog.setHeaderText("Entrez le nom de la nouvelle conversation");

                Optional<String> result = dialog.showAndWait();
                if (result.isPresent()) {
                    try{
                        if (!getConversationsTitre().contains((String)result.get())) {
                            //createNewConv(result.get());
                            items =  FXCollections.observableArrayList(getConversationsTitre());
                            items.add((String) result.get());
                            listeConv.setItems(items);
                        } else {
                            Alert alert = new Alert(AlertType.ERROR);
                            alert.setTitle("Conversation déjà existante");
                            alert.setHeaderText("La conversation de nom \""+result.get()+"\" est déjà existante.");
                            alert.setContentText("Veuillez créer une autre conversation avec un nom différent.");
                            alert.showAndWait();
                        }
                    } catch (RemoteException e) {
                        e.toString();
                        e.printStackTrace();
                    }
                }
            }
        });


        VBox vboxConv = new VBox();
        vboxConv.setPadding(new Insets(10));
        vboxConv.setSpacing(8);

        vboxConv.getChildren().add(listeConv);
        vboxConv.getChildren().add(addConvButton);


        VBox vbox = new VBox();
        vbox.setPadding(new Insets(10));
        vbox.setSpacing(8);

        //messagesList = FXCollections.observableArrayList("C'est qui ?", "C'est Pinpin !");
        messagesListView = new MessagesListView();
        messagesListView.setItems(messagesList);

        messagesListView.setPrefHeight(0.7*screenSize.getHeight());
        messagesListView.setPrefWidth(0.75*screenSize.getWidth());


        nouveauMessage = new TextField();
        nouveauMessage.setPromptText("Nouveau message");
        sendMessage = new Button("Envoyer");
        sendMessage.setDefaultButton(true);
        sendMessage.setOnAction(event -> {
            try{
                if (!pseudo.isEmpty() && refConv != -1) {
                    messagerie.addMessage(nouveauMessage.getText(), refConv, pseudo);
                    if (!messagerie.getContenu(refConv, pseudo).isEmpty()) {
                        messagesList = FXCollections.observableArrayList(messagerie.getContenu(refConv, pseudo));
                    }
                    else {
                        messagesList = FXCollections.observableArrayList();
                    }
                    nouveauMessage.setText("");
                    messagesListView.setItems(messagesList);
                }else{
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Erreur");
                    alert.setHeaderText("Aucune conversation sélectionnée");
                    alert.setContentText("Sélectionnez une conversation dans la barre latérale gauche avant " +
                                         "d'envoyer des messages.");
                    alert.showAndWait();
                }
            } catch (RemoteException e) {
                e.toString();
                e.printStackTrace();
            }
        });

        nouveauMessage.setPrefWidth(0.65*screenSize.getWidth());
        sendMessage.setPrefWidth(0.10*screenSize.getWidth());
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(15, 12, 15, 12));
        hbox.setSpacing(10);
        hbox.getChildren().add(nouveauMessage);
        hbox.getChildren().add(sendMessage);

        vbox.getChildren().add(messagesListView);
        vbox.getChildren().add(hbox);

        grid.add(vboxConv, 1, 1);
        grid.add(vbox, 3, 1);
        chatPane.setContent(grid);
    }




    public class MessagesListView extends ListView<Message> {
        public MessagesListView() {
            super();
            initMessageCellFactory();
        }

        public MessagesListView(ObservableList<Message> items) {
            super(items);
            initMessageCellFactory();
        }

        public void initMessageCellFactory() {
            setCellFactory(new Callback<ListView<Message>, ListCell<Message>>() {
                @Override
                public ListCell<Message> call(ListView<Message> param) {
                    return new MessageCell();
                }
            });
        }
    }

    public class MessageCell extends ListCell<Message> {
        private Label texte = new Label("gg");

        // TODO : charger l'image à partir du compte
        private ImageView avatarMessage = new ImageView(new Image("https://i1.social.s-msft.com/profile/u/avatar.jpg?displayname=kabir+shenvi&size=extralarge&version=00000000-0000-0000-0000-000000000000", 45, 45, true, false));

        private Text expediteur = new Text("gg");
        private Label date = new Label("gg");

        public MessageCell() {
            setTexte(null);
            setDate(null);
            setExpediteur(null);
            initCell();
        }

        public MessageCell(Message msg) {
            setTexte(msg.getMessage());
            setDate(DateFormat.getDateTimeInstance(
                    DateFormat.SHORT, DateFormat.SHORT).format(msg.getDate()));
            setExpediteur(msg.getPseudo());
            initCell();
        }

        public void setTexte(String text) {
            if (text != null) {
                text = text.trim();
            }
            setText(null);
            texte.setText(text);
        }

        public void setDate(String date) {
            setText(null);
            this.date.setText(date);
        }
        public void setExpediteur(String exp) {
            setText(null);
            this.expediteur.setText(exp);
        }
        public void initCell() {
            expediteur.setFont(Font.font(null, FontWeight.BOLD, 16));
            GridPane gridPane = new GridPane();
            gridPane.setHgap(10);
            gridPane.setVgap(10);
            gridPane.setPadding(new Insets(15, 15, 15, 15));
            gridPane.add(expediteur, 0, 0);
            gridPane.add(avatarMessage, 0, 1);
            gridPane.add(date, 1, 0);
            gridPane.add(texte, 1, 1);
            setGraphic(gridPane);
        }

        @Override
        public void updateItem(Message msg, boolean empty) {
            super.updateItem(msg, empty);
            if (empty) {
                // setTextField(null);
                setGraphic(null);
            } else {
                setTexte(msg.getMessage());
                setDate(DateFormat.getDateTimeInstance(
                        DateFormat.SHORT, DateFormat.SHORT).format(msg.getDate()));
                setExpediteur(msg.getPseudo());
                initCell();
            }
        }

        @Override
        public String toString() {
            return texte.getText();
        }
    }


    /* * * * * * * * * Utils * * * * * * * * * */

    public static Map getConversationsList() throws RemoteException{
        Map<Integer, Conversation> liste = new HashMap<>();
        int i = 0;
        for (Conversation c : messagerie.getConversations()) {
            i++;
            liste.put(i, c);
        }
        //maxKey = i;
        return liste;
    }

    public static Collection<String> getConversationsTitre() throws RemoteException {
        Map mapConv = getConversationsList();
        List<String> liste = new ArrayList<>();
        Iterator it = mapConv.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            Conversation c = (Conversation)(pair.getValue());
            liste.add(c.getTitre());
        }
        return liste;
    }

    public static int createNewConv(String titre) throws RemoteException{
        callback = new IHMMessageCallback(pseudo);
        refConv = messagerie.creerConversation(null, pseudo, titre, null, null, callback);
        callback.setRefConv(refConv);
        return refConv;
        //chat(messagerie, refConv);
    }

    public static int getRefConv(String convName) throws RemoteException {
        int res = -1;
        Map mapConv = getConversationsList();
        Iterator it = mapConv.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            Conversation c = (Conversation)(pair.getValue());
            if (c.getTitre().equals(convName)) {
                res = (int) pair.getKey();
            }
        }
        return res;
    }

    @Override
    public void stop() throws Exception{
        try {
            messagerie.removeUserFromConv(pseudo, refConv);
        } catch (NullPointerException e) {}

        messagerie.removeCompte(pseudo);
        System.exit(0);
    }

    private void openFile(File file) {
        try {
            desktop.open(file);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /*public static Collection<Message> getConversationContenu(int refConv) {
        List<Message> liste = messagerie.getContenu(refConv);
        List<String> listeString = new ArrayList<String>();
        for (Message msg : liste) {
            listeString.add((String));
        }
    }*/

}
