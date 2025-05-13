package gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

public class SidebarMenu extends VBox {

    // Interface pour gérer les changements de vue
    public interface ViewChangeListener {
        void onViewChange(String viewName);
    }

    private ViewChangeListener viewChangeListener;
    private String activeView = "dashboard";

    // Couleurs de la charte graphique
    private final String SIDEBAR_BG = "#2D3748";
    private final String BUTTON_ACTIVE_BG = "#4A5568";
    private final String TEXT_COLOR = "#CBD5E0";
    private final String TEXT_ACTIVE_COLOR = "white";
    private final String ICON_ACTIVE_COLOR = "#63B3ED"; // Bleu clair pour les icônes actives

    public SidebarMenu(ViewChangeListener listener) {
        this.viewChangeListener = listener;

        // Style de base
        this.setPrefWidth(200);
        this.setStyle("-fx-background-color: " + SIDEBAR_BG + "; -fx-padding: 10px;");
        this.setSpacing(15);

        // Logo et Titre
        HBox titleBox = new HBox();
        titleBox.setAlignment(Pos.CENTER);
        titleBox.setPadding(new Insets(10, 0, 15, 0));

        Label titleLabel = new Label("SMA Control");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: white;");
        titleBox.getChildren().add(titleLabel);

        this.getChildren().add(titleBox);
        this.getChildren().add(createSeparator());

        // Boutons de navigation
        addNavigationButton("Dashboard", "dashboard", "⚙️", true);
        addNavigationButton("Conteneurs", "containers", "📦", false);
        addNavigationButton("Agents", "agents", "👥", false);

        // Spacer pour pousser le statut en bas
        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        this.getChildren().add(spacer);

        this.getChildren().add(createSeparator());

        // Statut du système
        Label statusLabel = new Label("Statut Système");
        statusLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: white;");
        statusLabel.setPadding(new Insets(5, 0, 5, 0));

        HBox statusBox = new HBox(10);
        statusBox.setAlignment(Pos.CENTER_LEFT);
        statusBox.setPadding(new Insets(5, 0, 5, 15));

        Circle statusIndicator = new Circle(5);
        statusIndicator.setFill(Color.LIMEGREEN);

        Label statusText = new Label("En ligne");
        statusText.setStyle("-fx-text-fill: white;");

        statusBox.getChildren().addAll(statusIndicator, statusText);

        this.getChildren().addAll(statusLabel, statusBox);
    }

    private Separator createSeparator() {
        Separator separator = new Separator();
        separator.setStyle("-fx-background-color: #4A5568;");
        return separator;
    }

    private void addNavigationButton(String text, String viewName, String iconEmoji, boolean isActive) {
        Button navButton = new Button();

        // Style de base pour tous les boutons
        String baseStyle = "-fx-background-color: transparent; " +
                "-fx-padding: 10px 15px; " +
                "-fx-cursor: hand; " +
                "-fx-background-radius: 5px;";

        // Style pour le bouton actif
        String activeStyle = "-fx-background-color: " + BUTTON_ACTIVE_BG + "; " +
                "-fx-padding: 10px 15px; " +
                "-fx-cursor: hand; " +
                "-fx-background-radius: 5px;";

        // Appliquer le style approprié
        navButton.setStyle(isActive ? activeStyle : baseStyle);

        // Créer le contenu du bouton avec icône et texte
        HBox buttonContent = new HBox(12);
        buttonContent.setAlignment(Pos.CENTER_LEFT);

        // Icône (emoji)
        Label iconLabel = new Label(iconEmoji);
        iconLabel.setStyle("-fx-font-size: 16px;");

        // Texte du bouton
        Label textLabel = new Label(text);
        textLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: " + (isActive ? TEXT_ACTIVE_COLOR : TEXT_COLOR) + ";");

        // Ajouter les composants au conteneur
        buttonContent.getChildren().addAll(iconLabel, textLabel);
        navButton.setGraphic(buttonContent);

        // Effet hover
        navButton.setOnMouseEntered(e -> {
            if (!viewName.equals(activeView)) {
                navButton.setStyle("-fx-background-color: #3A4556; " +
                        "-fx-padding: 10px 15px; " +
                        "-fx-cursor: hand; " +
                        "-fx-background-radius: 5px;");
                textLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: white;");
            }
        });

        navButton.setOnMouseExited(e -> {
            if (!viewName.equals(activeView)) {
                navButton.setStyle(baseStyle);
                textLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: " + TEXT_COLOR + ";");
            }
        });

        // Définir la largeur maximale
        navButton.setMaxWidth(Double.MAX_VALUE);

        // Ajouter info-bulle
        navButton.setTooltip(new Tooltip("Afficher " + text));

        // Gérer le changement de vue
        navButton.setOnAction(e -> {
            // Mettre à jour la vue active
            setActiveView(viewName);

            // Notifier le listener du changement
            if (viewChangeListener != null) {
                viewChangeListener.onViewChange(viewName);
            }
        });

        // Ajouter au menu
        this.getChildren().add(navButton);

        // Stocker le bouton si c'est le bouton actif
        if (isActive) {
            activeView = viewName;
        }
    }

    // Méthode pour changer visuellement la vue active
    public void setActiveView(String viewName) {
        this.activeView = viewName;

        // Mettre à jour les styles des boutons
        for (int i = 0; i < this.getChildren().size(); i++) {
            if (this.getChildren().get(i) instanceof Button) {
                Button btn = (Button) this.getChildren().get(i);
                HBox content = (HBox) btn.getGraphic();

                if (content != null && content.getChildren().size() >= 2) {
                    Label iconLabel = (Label) content.getChildren().get(0);
                    Label textLabel = (Label) content.getChildren().get(1);
                    String buttonText = textLabel.getText();

                    // Déterminer le nom de la vue associée à ce bouton
                    String btnViewName = "";
                    if (buttonText.equalsIgnoreCase("Dashboard")) btnViewName = "dashboard";
                    else if (buttonText.equalsIgnoreCase("Conteneurs")) btnViewName = "containers";
                    else if (buttonText.equalsIgnoreCase("Agents")) btnViewName = "agents";

                    // Appliquer le style approprié
                    boolean isActiveButton = btnViewName.equals(viewName);

                    // Style du bouton
                    btn.setStyle(isActiveButton ?
                            "-fx-background-color: " + BUTTON_ACTIVE_BG + "; -fx-padding: 10px 15px; -fx-cursor: hand; -fx-background-radius: 5px;" :
                            "-fx-background-color: transparent; -fx-padding: 10px 15px; -fx-cursor: hand; -fx-background-radius: 5px;");

                    // Style du texte
                    textLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: " + (isActiveButton ? TEXT_ACTIVE_COLOR : TEXT_COLOR) + ";");
                }
            }
        }
    }
}