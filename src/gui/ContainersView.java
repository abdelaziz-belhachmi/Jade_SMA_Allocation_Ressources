package gui.widgets;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
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

public class ContainersView extends VBox {

    private Button startPersonneBtn;
    private Button startRestaurantBtn;
    private Button startMediateurBtn;
    private Button startStatistiqueBtn;
    private Label statusLabel;
    private TextArea logArea;

    private Circle personneStatus;
    private Circle restaurantStatus;
    private Circle mediateurStatus;
    private Circle statistiqueStatus;

    // Nouvelles couleurs basées sur AgentsView
    private final Color STATISTIQUE_COLOR = Color.web("#8b5cf6"); // Violet pour statistique
    private final Color MEDIATEUR_COLOR = Color.web("#3b82f6");   // Bleu pour médiateur
    private final Color RESTAURANT_COLOR = Color.web("#0ea5e9");  // Bleu clair pour restaurant
    private final Color PERSONNE_COLOR = Color.web("#06b6d4");    // Cyan pour personne
    private final Color START_COLOR = Color.web("#22c55e");       // Vert pour démarrer
    private final Color SUCCESS_COLOR = Color.web("#22c55e");     // Vert pour les statuts actifs
    private final Color ERROR_COLOR = Color.web("#ef4444");       // Rouge pour les erreurs
    private final Color NEUTRAL_COLOR = Color.web("#6c757d");     // Gris neutre
    private final Color DARK_TEXT = Color.web("#2b2d42");         // Texte foncé
    private final Color LIGHT_TEXT = Color.web("#FFFFFF");        // Texte clair
    private final Color BACKGROUND_COLOR = Color.web("#f8f9fa");  // Fond de l'application
    private final Color CARD_BACKGROUND = Color.web("#FFFFFF");   // Fond des cartes

    public ContainersView() {
        this.setPadding(new Insets(20));
        this.setSpacing(20);
        this.setStyle("-fx-background-color: " + BACKGROUND_COLOR.toString().replace("0x", "#") + ";");

        // En-tête moderne semblable à AgentsView
        VBox headerBox = createHeader();

        // Section principale avec cartes et statuts
        HBox mainSection = createMainSection();

        // Section journal modernisée
        VBox journalBox = createLogBox();
        VBox.setVgrow(journalBox, Priority.ALWAYS);

        this.getChildren().addAll(headerBox, mainSection, journalBox);
    }

    private VBox createHeader() {
        Label headerLabel = new Label("Gestion des Conteneurs");
        headerLabel.setStyle("-fx-font-size: 26px; -fx-font-weight: bold; -fx-text-fill: #2b2d42;");

        Label subHeaderLabel = new Label("Tableau de bord de contrôle");
        subHeaderLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #8d99ae;");

        statusLabel = new Label("Statut des opérations");
        statusLabel.setStyle("-fx-font-size: 14px; -fx-font-style: italic; -fx-text-fill: #6c757d;");

        VBox headerBox = new VBox(4, headerLabel, subHeaderLabel, new Separator(), statusLabel);
        headerBox.setAlignment(Pos.CENTER_LEFT);
        headerBox.setPadding(new Insets(0, 0, 15, 0));

        return headerBox;
    }

    private HBox createMainSection() {
        VBox actionsCard = createActionsCard();
        VBox statusCard = createStatusCard();

        // Espacement égal entre les cartes et taille égale
        HBox mainSection = new HBox(20);
        HBox.setHgrow(actionsCard, Priority.ALWAYS);
        HBox.setHgrow(statusCard, Priority.ALWAYS);

        // Force les deux conteneurs à avoir la même largeur
        actionsCard.setMinWidth(Region.USE_PREF_SIZE);
        statusCard.setMinWidth(Region.USE_PREF_SIZE);
        actionsCard.setPrefWidth(Region.USE_COMPUTED_SIZE);
        statusCard.setPrefWidth(Region.USE_COMPUTED_SIZE);
        actionsCard.setMaxWidth(Double.MAX_VALUE);
        statusCard.setMaxWidth(Double.MAX_VALUE);

        mainSection.getChildren().addAll(actionsCard, statusCard);
        mainSection.setPrefHeight(280);

        return mainSection;
    }

    private VBox createActionsCard() {
        Label cardTitle = new Label("Contrôles");
        cardTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2b2d42;");

        // Création des boutons avec icônes
        startStatistiqueBtn = createActionButton("📊 Démarrer Statistique", STATISTIQUE_COLOR);
        startMediateurBtn = createActionButton("🔄 Démarrer Médiateur", MEDIATEUR_COLOR);
        startRestaurantBtn = createActionButton("🍽️ Démarrer Restaurant", RESTAURANT_COLOR);
        startPersonneBtn = createActionButton("👤 Démarrer Personne", PERSONNE_COLOR);

        VBox buttonsBox = new VBox(10); // Espacement comme dans AgentsView
        buttonsBox.getChildren().addAll(
                startStatistiqueBtn,
                startMediateurBtn,
                startRestaurantBtn,
                startPersonneBtn
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

    private VBox createStatusCard() {
        Label cardTitle = new Label("État des Conteneurs");
        cardTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2b2d42;");

        personneStatus = createStatusIndicator();
        restaurantStatus = createStatusIndicator();
        mediateurStatus = createStatusIndicator();
        statistiqueStatus = createStatusIndicator();

        VBox statusItemsBox = new VBox(10); // Espacement comme dans AgentsView
        statusItemsBox.getChildren().addAll(
                buildStatusRow("Conteneur Personne", personneStatus, "👤"),
                buildStatusRow("Conteneur Restaurant", restaurantStatus, "🍽️"),
                buildStatusRow("Conteneur Médiateur", mediateurStatus, "🔄"),
                buildStatusRow("Conteneur Statistique", statistiqueStatus, "📊")
        );
        statusItemsBox.setPadding(new Insets(15, 10, 10, 10));

        VBox card = new VBox(15, cardTitle, statusItemsBox);
        card.setPadding(new Insets(20));
        card.setStyle("-fx-background-color: white; -fx-background-radius: 10px; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.08), 10, 0, 0, 5);");

        return card;
    }

    private HBox buildStatusRow(String labelText, Circle statusCircle, String icon) {
        Label iconLabel = new Label(icon);
        iconLabel.setStyle("-fx-font-size: 16px;");
        iconLabel.setMinWidth(25);

        Label label = new Label(labelText);
        label.setStyle("-fx-font-size: 14px; -fx-text-fill: #2b2d42;");

        Label statusLabel = new Label("Inactif");
        statusLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #6c757d;");

        VBox statusTextBox = new VBox(2, label, statusLabel);
        statusTextBox.setAlignment(Pos.CENTER_LEFT);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox row = new HBox(10, iconLabel, statusTextBox, spacer, statusCircle);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(8, 12, 8, 12));
        row.setPrefHeight(50);
        row.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 8px;");

        // Mettre à jour le texte du statut en fonction de la couleur du cercle
        statusCircle.fillProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.equals(SUCCESS_COLOR)) {
                statusLabel.setText("Actif");
                statusLabel.setStyle("-fx-text-fill: #22c55e; -fx-font-weight: bold;");
            } else if (newVal.equals(ERROR_COLOR)) {
                statusLabel.setText("Arrêté");
                statusLabel.setStyle("-fx-text-fill: #ef4444; -fx-font-weight: bold;");
            } else {
                statusLabel.setText("Inactif");
                statusLabel.setStyle("-fx-text-fill: #6c757d;");
            }
        });

        return row;
    }

    private Circle createStatusIndicator() {
        Circle circle = new Circle(6);
        circle.setFill(NEUTRAL_COLOR);
        return circle;
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

    public void updateContainerStatus(String containerName, boolean isActive) {
        Circle target;
        switch (containerName) {
            case "Containers.PersonneContainer" -> target = personneStatus;
            case "Containers.RestaurantContainer" -> target = restaurantStatus;
            case "Containers.MediateurContainer" -> target = mediateurStatus;
            case "Containers.StatistiqueContainer" -> target = statistiqueStatus;
            default -> {
                return;
            }
        }
        target.setFill(isActive ? SUCCESS_COLOR : ERROR_COLOR);
    }

    public void setStatusMessage(String message) {
        statusLabel.setText(message);
        statusLabel.setStyle("-fx-font-size: 14px; -fx-font-style: italic; -fx-text-fill: #3b82f6;");
    }

    public void appendLog(String message) {
        logArea.appendText(message + "\n");
        logArea.setScrollTop(Double.MAX_VALUE);
    }

    public Button getStartPersonneButton() {
        return startPersonneBtn;
    }

    public Button getStartRestaurantButton() {
        return startRestaurantBtn;
    }

    public Button getStartMediateurButton() {
        return startMediateurBtn;
    }

    public Button getStartStatistiqueButton() {
        return startStatistiqueBtn;
    }
}