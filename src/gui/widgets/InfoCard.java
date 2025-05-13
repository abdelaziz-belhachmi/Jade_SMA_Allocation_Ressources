package gui.widgets;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class InfoCard extends VBox {

    private Label titleLabel;
    private Label valueLabel;
    private Label descriptionLabel;

    public InfoCard(String title, String value, String description, String iconClass) {
        this.setSpacing(5);
        this.setPadding(new Insets(15));
        this.setPrefWidth(250);
        this.setPrefHeight(150);
        this.getStyleClass().add("info-card");

        // En-tête avec icône
        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER_LEFT);

        // Placeholder pour icône (à remplacer par une vraie icône)
        Circle iconPlaceholder = new Circle(15);
        iconPlaceholder.setFill(Color.LIGHTBLUE);
        iconPlaceholder.getStyleClass().add(iconClass);

        titleLabel = new Label(title);
        titleLabel.getStyleClass().add("card-title");

        header.getChildren().addAll(iconPlaceholder, titleLabel);

        // Valeur principale
        valueLabel = new Label(value);
        valueLabel.getStyleClass().add("card-value");

        // Description
        descriptionLabel = new Label(description);
        descriptionLabel.getStyleClass().add("card-description");
        descriptionLabel.setWrapText(true);

        this.getChildren().addAll(header, valueLabel, descriptionLabel);
        VBox.setVgrow(descriptionLabel, Priority.ALWAYS);

        // Style supplémentaire (à ajouter dans style.css)
        this.setStyle("-fx-background-color: white; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 5); -fx-background-radius: 10;");
    }

    // Méthodes pour mettre à jour les données
    public void updateValue(String value) {
        valueLabel.setText(value);
    }

    public void updateDescription(String description) {
        descriptionLabel.setText(description);
    }

}


