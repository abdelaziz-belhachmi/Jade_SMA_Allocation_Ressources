package gui.widgets;

import javafx.geometry.Insets;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.util.Map;
import java.util.Random;

public class ChartComponent extends VBox {

    private LineChart<Number, Number> realtimeChart;
    private BarChart<String, Number> barChart;
    private XYChart.Series<Number, Number> reservationSeries;
    private XYChart.Series<Number, Number> agentActivitySeries;
    private Timeline timeline;
    private int timeCounter = 0;
    private Random random = new Random();

    public ChartComponent() {
        this.setPadding(new Insets(15));
        this.setSpacing(20);
        this.setStyle("-fx-background-color: #f8fafc; -fx-background-radius: 8px; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 5);");

        // Créer le graphique temps réel
        createRealtimeChart();

        // Simuler des données temps réel
        setupSimulation();
    }

    private void createRealtimeChart() {
        // Axe X et Y pour le graphique temps réel
        NumberAxis xAxisTime = new NumberAxis("Temps", 0, 50, 5);
        NumberAxis yAxisActivity = new NumberAxis("Nombre", 0, 50, 5);

        // Créer le graphique linéaire
        realtimeChart = new LineChart<>(xAxisTime, yAxisActivity);
        realtimeChart.setTitle("Réservations en temps réel");
        realtimeChart.setAnimated(false);
        realtimeChart.setCreateSymbols(true);

        // Séries pour les données
        reservationSeries = new XYChart.Series<>();
        reservationSeries.setName("Réservations");

        agentActivitySeries = new XYChart.Series<>();
        agentActivitySeries.setName("Activité des agents");

        // Ajouter les séries au graphique
        realtimeChart.getData().addAll(reservationSeries, agentActivitySeries);

        // Légende pour le graphique
        Label chartLabel = new Label("Activité du système");
        chartLabel.setStyle("-fx-font-weight: bold;");

        // Ajouter au conteneur
        this.getChildren().addAll(chartLabel, realtimeChart);
    }

    private void setupSimulation() {
        // Timeline pour simuler des données en temps réel
        timeline = new Timeline(new KeyFrame(Duration.seconds(2), event -> {
            // Simuler des données de réservation
            int reservationValue = random.nextInt(40) + 10;
            reservationSeries.getData().add(new XYChart.Data<>(timeCounter, reservationValue));

            // Simuler des données d'activité des agents
            int agentValue = random.nextInt(30) + 15;
            agentActivitySeries.getData().add(new XYChart.Data<>(timeCounter, agentValue));

            // Limiter le nombre de points affichés
            if (reservationSeries.getData().size() > 20) {
                reservationSeries.getData().remove(0);
                agentActivitySeries.getData().remove(0);
            }

            timeCounter++;
        }));

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    // Méthode pour mettre à jour les données à partir de statistiques réelles
    public void updateData(Map<String, Integer> restaurantStats, Map<String, Integer> agentStats) {
        // Mettre à jour les données existantes ou ajouter de nouvelles données
        // Pour cet exemple, nous allons simplement ajouter des données simulées
        addDataPoint(random.nextInt(40) + 10, random.nextInt(30) + 15);
    }

    // Méthode pour ajouter un point de données manuellement
    public void addDataPoint(int reservationValue, int agentValue) {
        reservationSeries.getData().add(new XYChart.Data<>(timeCounter, reservationValue));
        agentActivitySeries.getData().add(new XYChart.Data<>(timeCounter, agentValue));

        // Limiter le nombre de points affichés
        if (reservationSeries.getData().size() > 20) {
            reservationSeries.getData().remove(0);
            agentActivitySeries.getData().remove(0);
        }

        timeCounter++;
    }

    // Méthode pour arrêter la simulation
    public void stopSimulation() {
        if (timeline != null) {
            timeline.stop();
        }
    }

    // Méthode pour démarrer ou redémarrer la simulation
    public void startSimulation() {
        if (timeline != null) {
            timeline.play();
        }
    }

    // Méthode pour réinitialiser les données
    public void resetData() {
        reservationSeries.getData().clear();
        agentActivitySeries.getData().clear();
        timeCounter = 0;
    }
}