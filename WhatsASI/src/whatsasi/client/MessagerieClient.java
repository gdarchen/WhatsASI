// si on veut faire visio : GStreamer

package whatsasi.client;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.util.Callback;
import javafx.beans.value.*;
import javafx.beans.binding.Bindings;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.ContextMenuEvent;


import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javafx.stage.FileChooser;
import javafx.scene.input.MouseEvent;

import javafx.scene.paint.Color;
// import javafx.scene.shape.Circle;
import java.text.DateFormat;
import java.util.*;
import java.io.*;
import java.lang.IllegalStateException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;


import whatsasi.serveur.conversations.*;
import whatsasi.serveur.filtrage.Filtre;


import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import javax.swing.*;
import java.util.List;
import java.util.logging.Filter;

public class MessagerieClient extends Application {
    private static final int PORTRMI = 1099;
    private static final String ENDPOINT = "localhost";
    private static Stage primaryStage;
    private static MessagerieInterface messagerie;
    private static String pseudo;
    private static int refConv = -1 ;
    private static MessageCallbackInterface callback = null;
    private static MessagerieClient me = null;

    //==== Menu nodes
    TitledPane connexionPane = new TitledPane();
    TitledPane chatPane = new TitledPane();
    TitledPane filterPane = new TitledPane();
    Accordion accordion = new Accordion(connexionPane, filterPane, chatPane);
    Rectangle2D screenSize = Screen.getPrimary().getBounds();
    private Desktop desktop = Desktop.getDesktop();

    //==== Connexion nodes
    //private ImageView avatarView = new ImageView(new Image("https://i1.social.s-msft.com/profile/u/avatar.jpg?displayname=kabir+shenvi&size=extralarge&version=00000000-0000-0000-0000-000000000000", 120, 120, true, false));
    private ImageView avatarView = new ImageView(new Image("file:res/im8.png"));
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
            messagerie.setIAIcon();
            Application.launch(args);
        } catch (RemoteException e) {
            e.toString();
            e.printStackTrace();
        } catch (IllegalStateException e) {
            // On ne fait rien
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
        this.me = this;
        setPrimaryStage(stage);
        Scene scene = new Scene(accordion, screenSize.getWidth(), screenSize.getHeight());

        stage.getIcons().add(new Image("file:icon.png"));
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
                //System.out.println(file.toURI().toString());
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
                // Indique si premiere connexion
                boolean premiereFois = (pseudo == null);
                String oldPseudo = null;
                if (!premiereFois) {
                    oldPseudo = pseudo;
                }
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
                        if (premiereFois) {
                            messagerie.creerCompte(pseudo, fromFXImage(avatarView.getImage()), Mode.DEFAUT, null, new IHMConversationCallback(me));
                        }
                        else {
                            messagerie.modifierPseudo(oldPseudo, pseudo);
                        }
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
        filterList = FXCollections.observableArrayList();
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

            if (filterTextField.getText().isEmpty()){
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Mot à filtrer vide");
                alert.setHeaderText("Veuillez entrer un mot à filtrer différent d'une chaine de caractère vide.");
                alert.setContentText("Le mot filtré apparaitra avec des \"***\" dans les messages reçus.");
                alert.showAndWait();
            }else{
                if (!filterList.contains(text)) {
                    System.out.println("Mot-filtre ajouté : " + text);
                    filterList.add(text);
                    try{
                        messagerie.addMotInterdit(text,pseudo);
                    }catch(RemoteException ex){
                        ex.printStackTrace();
                    }
                    filterTextFieldAlert.setVisible(false);
                } else {
                    filterTextFieldAlert.setVisible(true);
                }
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
            minus.setOnAction(new EventHandler<ActionEvent>() {
                @Override public void handle(ActionEvent event) {
                    filterList.remove(textField.getText().trim().toLowerCase());
                    try{
                        messagerie.removeMotInterdit(textField.getText().trim().toLowerCase(),pseudo);
                    }catch(RemoteException ex){
                        ex.printStackTrace();
                    }
                }
            });
            GridPane gridPane = new GridPane();
            gridPane.add(textField, 0, 0);
            gridPane.add(minus, 1, 0);
            setGraphic(gridPane);
        }

        @Override
        public void updateItem(String text, boolean empty) {
            super.updateItem(text, empty);
            if (empty) {
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

        listeConv = new ListView<>();

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
                            messagesList = FXCollections.observableArrayList();
                            messagesListView.setItems(messagesList);
                        }
                        else{
                            callback = new IHMMessageCallback(refConv, pseudo, me);
                            messagerie.addUserToConv(pseudo, refConv, callback);
                            loadConvMessages();
                        }
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
                    String res = result.get();
                    if (res.isEmpty()){
                        Alert alert = new Alert(AlertType.ERROR);
                        alert.setTitle("Une conversation doit avoir un nom");
                        alert.setHeaderText("Veuillez entrer un nom de conversation.");
                        alert.setContentText("Le nom de conversation permet aux autres utilisateurs de pouvoir vous rejoindre et discuter avec vous.");
                        alert.showAndWait();
                    }else{
                        try{
                            if (!getConversationsTitre().contains((String)result.get())) {
                                items =  FXCollections.observableArrayList(getConversationsTitre());
                                items.add(result.get());
                                listeConv.setItems(items);
                                listeConv.getSelectionModel().select(result.get());
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

        messagesListView = new MessagesListView();
        messagesListView.setItems(messagesList);

        messagesListView.setPrefHeight(0.7*screenSize.getHeight());
        messagesListView.setPrefWidth(0.75*screenSize.getWidth());

        ContextMenu menu = new ContextMenu();
        MenuItem deleteItem = new MenuItem("Supprimer le message");
        deleteItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Message msg = messagesListView.getSelectionModel().getSelectedItem();
                System.out.println("delete pressed for Message (objet de type message à supprimer de la conv) : " + messagesListView.getSelectionModel().getSelectedItem());
            }
        });
        menu.getItems().addAll(deleteItem);

        messagesListView.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
            @Override
            public void handle(ContextMenuEvent event) {
                menu.show(messagesListView, event.getScreenX(), event.getScreenY());
            }
        });

        nouveauMessage = new TextField();
        nouveauMessage.setPromptText("Nouveau message");
        sendMessage = new Button("Envoyer");
        sendMessage.setDefaultButton(true);
        sendMessage.setOnAction(event -> {
            try{
                if (!pseudo.isEmpty() && refConv != -1 && !nouveauMessage.getText().isEmpty()) {
                    messagerie.addMessage(nouveauMessage.getText(), refConv, pseudo);
                    messagesList.add(new Message(messagerie.getCompte(pseudo), nouveauMessage.getText()));
                    nouveauMessage.setText("");
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
        private Label texte = new Label();
        private static final int avatarMaxDim = 50;
        private ImageView avatarMessage;
        private Text expediteur = new Text();
        private Label date = new Label();

        public MessageCell() {
            setTexte(null);
            setDate(null);
            avatarMessage = new ImageView(new Image("https://i1.social.s-msft.com/profile/u/avatar.jpg?displayname=kabir+shenvi&size=extralarge&version=00000000-0000-0000-0000-000000000000",
                    avatarMaxDim, avatarMaxDim, true, false));
            setExpediteur(null);
            initCell();
        }

        public MessageCell(Message msg) {
            setTexte(msg.getMessage());
            setAvatar(msg.getAvatar(), msg.getAvatarWidth(), msg.getAvatarHeight());
            setDate(DateFormat.getDateTimeInstance(
                    DateFormat.SHORT, DateFormat.SHORT).format(msg.getDate()));
            setExpediteur(msg.getPseudo());
            initCell();
        }

        public String getTexte(){
            return this.texte.getText();
        }

        public void setTexte(String text) {
            if (text != null) {
                text = text.trim();
            }
            setText(null);
            texte.setText(text);
        }

        public void setAvatar(java.awt.Image img, int width, int height) {
            int w = (int) avatarView.getImage().getWidth();
            int h = (int) avatarView.getImage().getHeight();
            Image fximg = toFXImage(img, w, h);
            avatarMessage.setPreserveRatio(true);
            avatarMessage.setImage(fximg);
            avatarMessage.setFitWidth(avatarMaxDim);
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
                setAvatar(msg.getAvatar(), msg.getAvatarWidth(), msg.getAvatarHeight());
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
        callback = new IHMMessageCallback(pseudo, me);
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

    public void loadConvMessages() throws RemoteException {
        List<Message> contenu = messagerie.getContenu(refConv, pseudo);
        if (!contenu.isEmpty()) {
            messagesList = FXCollections.observableArrayList(contenu);
        }
        else{
            messagesList = FXCollections.observableArrayList();
        }
        messagesListView.setItems(messagesList);
    }

    public void receiveMessage(Message msg) throws RemoteException {
        // Support pour le multithread avec JavaFX : il ajoute un Runnable sur la pile de évènements
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                messagesList.add(msg);
                Toolkit.getDefaultToolkit().beep();
            }
        });
    }

    public void receiveConversation(int refConv) throws RemoteException {
        // Support pour le multithread avec JavaFX : il ajoute un Runnable sur la pile de évènements
        if (this.refConv != refConv) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    try {
                        items.add(messagerie.getTitreConv(refConv));
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    /*public static Collection<Message> getConversationContenu(int refConv) {
        List<Message> liste = messagerie.getContenu(refConv);
        List<String> listeString = new ArrayList<String>();
        for (Message msg : liste) {
            listeString.add((String));
        }
    }*/

    public static ImageIcon fromFXImage(Image img) {
        java.awt.Image awtimg = SwingFXUtils.fromFXImage(img, null);
        return new ImageIcon(awtimg);
    }

    public static Image toFXImage(java.awt.Image img, int width, int height) {
        BufferedImage bimg = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D g2d = bimg.createGraphics();
        g2d.drawImage(img, 0, 0, null);
        g2d.dispose();
        return SwingFXUtils.toFXImage(bimg, null);
    }



}
