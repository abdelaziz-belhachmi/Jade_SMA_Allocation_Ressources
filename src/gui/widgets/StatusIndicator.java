package gui.widgets;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class StatusIndicator extends HBox {

    private Circle statusCircle;
    private Label statusLabel;

    public static final Color STATUS_OK = Color.rgb(34, 197, 94);       // Vert
    public static final Color STATUS_WARNING = Color.rgb(245, 158, 11); // Orange
    public static final Color STATUS_ERROR = Color.rgb(239, 68, 68);    // Rouge
    public static final Color STATUS_INACTIVE = Color.rgb(148, 163, 184); // Gris

    public StatusIndicator(String text, Color initialColor) {
        this.setSpacing(10);
        this.setAlignment(Pos.CENTER_LEFT);
        this.setPadding(new Insets(5));

        statusCircle = new Circle(6);
        statusCircle.setFill(initialColor);

        statusLabel = new Label(text);

        this.getChildren().addAll(statusCircle, statusLabel);
    }

    // Constructeur par défaut
    public StatusIndicator() {
        this("Inactif", STATUS_INACTIVE);
    }

    // Méthodes pour mettre à jour le statut
    public void setStatus(Color color, String text) {
        statusCircle.setFill(color);
        statusLabel.setText(text);
    }

    public void setOk(String text) {
        setStatus(STATUS_OK, text);
    }

    public void setWarning(String text) {
        setStatus(STATUS_WARNING, text);
    }

    public void setError(String text) {
        setStatus(STATUS_ERROR, text);
    }

    public void setInactive(String text) {
        setStatus(STATUS_INACTIVE, text);
    }
}
