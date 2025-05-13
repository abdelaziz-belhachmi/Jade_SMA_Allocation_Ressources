package gui.widgets;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Separator;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.effect.DropShadow;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.Node;

public class AgentsView extends VBox {

    private Button createStatistiqueBtn;
    private Button createMediateurBtn;
    private Button createRestaurantBtn;
    private Button createPersonnesBtn;
    private Button startReservationsBtn;
    private Label statusLabel;
    private TextArea logArea;
    private TableView<AgentInfo> agentsTable;

    // Nouvelles couleurs modernes
    private final Color STATISTIQUE_COLOR = Color.web("#8b5cf6"); // Violet pour statistique
    private final Color MEDIATEUR_COLOR = Color.web("#3b82f6");   // Bleu pour médiateur
    private final Color RESTAURANT_COLOR = Color.web("#0ea5e9");  // Bleu clair pour restaurant
    private final Color PERSONNE_COLOR = Color.web("#06b6d4");    // Cyan pour personne
    private final Color RESERVATION_COLOR = Color.web("#22c55e"); // Vert pour réservations
    private final Color SUCCESS_COLOR = Color.web("#22c55e");     // Vert pour les statuts actifs
    private final Color ERROR_COLOR = Color.web("#ef4444");       // Rouge pour les erreurs
    private final Color NEUTRAL_COLOR = Color.web("#6c757d");     // Gris neutre
    private final Color DARK_TEXT = Color.web("#2b2d42");         // Texte foncé
    private final Color LIGHT_TEXT = Color.web("#FFFFFF");        // Texte clair
    private final Color BACKGROUND_COLOR = Color.web("#f8f9fa");  // Fond de l'application
    private final Color CARD_BACKGROUND = Color.web("#FFFFFF");   // Fond des cartes

    public AgentsView() {
        this.setPadding(new Insets(20));
        this.setSpacing(20);
        this.setStyle("-fx-background-color: " + BACKGROUND_COLOR.toString().replace("0x", "#") + ";");

        // En-tête moderne
        VBox headerBox = createHeader();

        // Section principale avec contrôles et tableau d'agents
        HBox mainSection = createMainSection();

        // Section journal modernisée
        VBox journalBox = createLogBox();
        VBox.setVgrow(journalBox, Priority.ALWAYS);

        this.getChildren().addAll(headerBox, mainSection, journalBox);
    }

    private VBox createHeader() {
        Label headerLabel = new Label("Gestion des Agents");
        headerLabel.setStyle("-fx-font-size: 26px; -fx-font-weight: bold; -fx-text-fill: #2b2d42;");

        Label subHeaderLabel = new Label("Système de réservation multi-agents");
        subHeaderLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #8d99ae;");

        statusLabel = new Label("Statut des opérations");
        statusLabel.setStyle("-fx-font-size: 14px; -fx-font-style: italic; -fx-text-fill: #6c757d;");

        VBox headerBox = new VBox(4, headerLabel, subHeaderLabel, new Separator(), statusLabel);
        headerBox.setAlignment(Pos.CENTER_LEFT);
        headerBox.setPadding(new Insets(0, 0, 15, 0));

        return headerBox;
    }

    private HBox createMainSection() {
        VBox controlsCard = createControlsCard();
        VBox agentsCard = createAgentsCard();

        // Espacement égal entre les cartes et taille égale
        HBox mainSection = new HBox(20);
        HBox.setHgrow(controlsCard, Priority.ALWAYS);
        HBox.setHgrow(agentsCard, Priority.ALWAYS);

        // Force les deux conteneurs à avoir la même largeur
        controlsCard.setMinWidth(Region.USE_PREF_SIZE);
        agentsCard.setMinWidth(Region.USE_PREF_SIZE);
        controlsCard.setPrefWidth(Region.USE_COMPUTED_SIZE);
        agentsCard.setPrefWidth(Region.USE_COMPUTED_SIZE);
        controlsCard.setMaxWidth(Double.MAX_VALUE);
        agentsCard.setMaxWidth(Double.MAX_VALUE);

        mainSection.getChildren().addAll(controlsCard, agentsCard);
        mainSection.setPrefHeight(300); // Légèrement plus haut pour accommoder le tableau

        return mainSection;
    }

    private VBox createControlsCard() {
        Label cardTitle = new Label("Contrôles");
        cardTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2b2d42;");

        // Création des boutons avec icônes
        createStatistiqueBtn = createActionButton("📊 Créer Agent Statistique", STATISTIQUE_COLOR);
        createMediateurBtn = createActionButton("🔄 Créer Agent Médiateur", MEDIATEUR_COLOR);
        createRestaurantBtn = createActionButton("🍽️ Créer Agents Restaurant", RESTAURANT_COLOR);
        createPersonnesBtn = createActionButton("👤 Créer Agents Personnes", PERSONNE_COLOR);
        startReservationsBtn = createActionButton("▶️ Démarrer Réservations", RESERVATION_COLOR);

        VBox buttonsBox = new VBox(10);
        buttonsBox.getChildren().addAll(
                createStatistiqueBtn,
                createMediateurBtn,
                createRestaurantBtn,
                createPersonnesBtn,
                startReservationsBtn
        );
        buttonsBox.setPadding(new Insets(15, 10, 10, 10));

        VBox card = new VBox(15, cardTitle, buttonsBox);
        card.setPadding(new Insets(20));
        card.setStyle("-fx-background-color: white; -fx-background-radius: 10px; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.08), 10, 0, 0, 5);");
        card.setMinWidth(260);

        return card;
    }

    private Button createActionButton(String text, Color color) {
        Button btn = new Button(text);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setPadding(new Insets(12));
        btn.setStyle("-fx-background-color: " + color.toString().replace("0x", "#") + "; " +
                "-fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 8px; -fx-cursor: hand;");

        btn.setOnMouseEntered(e -> btn.setStyle(btn.getStyle() + "-fx-opacity: 0.9;"));
        btn.setOnMouseExited(e -> btn.setStyle(btn.getStyle().replace("-fx-opacity: 0.9;", "")));

        return btn;
    }

    private VBox createAgentsCard() {
        Label cardTitle = new Label("Agents actifs");
        cardTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2b2d42;");

        // Tableau des agents
        agentsTable = new TableView<>();
        agentsTable.setPlaceholder(new Label("Aucun agent actif"));
        agentsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        agentsTable.setStyle("-fx-background-color: transparent;");

        TableColumn<AgentInfo, String> nameColumn = new TableColumn<>("Nom");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setStyle("-fx-alignment: CENTER-LEFT;");

        TableColumn<AgentInfo, String> typeColumn = new TableColumn<>("Type");
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        typeColumn.setStyle("-fx-alignment: CENTER;");

        TableColumn<AgentInfo, String> statusColumn = new TableColumn<>("Statut");
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusColumn.setStyle("-fx-alignment: CENTER-RIGHT;");

        agentsTable.getColumns().addAll(nameColumn, typeColumn, statusColumn);

        VBox.setVgrow(agentsTable, Priority.ALWAYS);

        VBox card = new VBox(15, cardTitle, agentsTable);
        card.setPadding(new Insets(20));
        card.setStyle("-fx-background-color: white; -fx-background-radius: 10px; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.08), 10, 0, 0, 5);");

        return card;
    }

    private VBox createLogBox() {
        Label title = new Label("Journal d'activité");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2b2d42;");

        HBox headerRow = new HBox();
        headerRow.setAlignment(Pos.CENTER_LEFT);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button clearLogBtn = new Button("Effacer");
        clearLogBtn.setPrefWidth(90);
        clearLogBtn.setPrefHeight(30);
        clearLogBtn.setStyle("-fx-background-color: white; " +
                "-fx-text-fill: #3b82f6; " +
                "-fx-font-weight: bold; " +
                "-fx-cursor: hand; " +
                "-fx-border-color: #3b82f6; " +
                "-fx-border-radius: 5px;");

        clearLogBtn.setOnAction(e -> logArea.clear());

        headerRow.getChildren().addAll(title, spacer, clearLogBtn);

        logArea = new TextArea();
        logArea.setEditable(false);
        logArea.setPrefHeight(160);
        logArea.setStyle("-fx-control-inner-background: #f8f9fa; -fx-font-family: 'Consolas';");

        VBox logBox = new VBox(10, headerRow, logArea);
        logBox.setPadding(new Insets(20));
        logBox.setStyle("-fx-background-color: white; -fx-background-radius: 10px; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.08), 10, 0, 0, 5);");
        VBox.setVgrow(logArea, Priority.ALWAYS);

        return logBox;
    }

    // Classe pour représenter les informations d'un agent dans le tableau
    public static class AgentInfo {
        private String name;
        private String type;
        private String status;

        public AgentInfo(String name, String type, String status) {
            this.name = name;
            this.type = type;
            this.status = status;
        }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getType() { return type; }
        public void setType(String type) { this.type = type; }

        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }

    public void setStatusMessage(String message) {
        statusLabel.setText(message);
        statusLabel.setStyle("-fx-font-size: 14px; -fx-font-style: italic; -fx-text-fill: #3b82f6;");
    }

    public void appendLog(String message) {
        logArea.appendText(message + "\n");
        logArea.setScrollTop(Double.MAX_VALUE);
    }

    // Méthode pour ajouter un agent au tableau
    public void addAgent(String name, String type, String status) {
        AgentInfo agent = new AgentInfo(name, type, status);
        agentsTable.getItems().add(agent);
    }

    // Méthode pour supprimer un agent du tableau
    public void removeAgent(String name) {
        agentsTable.getItems().removeIf(agent -> agent.getName().equals(name));
    }

    // Méthode pour vider le tableau
    public void clearAgents() {
        agentsTable.getItems().clear();
    }

    // Getters pour les boutons
    public Button getCreateStatistiqueButton() {
        return createStatistiqueBtn;
    }

    public Button getCreateMediateurButton() {
        return createMediateurBtn;
    }

    public Button getCreateRestaurantButton() {
        return createRestaurantBtn;
    }

    public Button getCreatePersonnesButton() {
        return createPersonnesBtn;
    }

    public Button getStartReservationsButton() {
        return startReservationsBtn;
    }
}