package gui.widgets;

import Agents.DAO.trio;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatisticsView extends VBox {

    private TextArea statsTextArea;
    private Button getStatsButton;
    private ChartComponent chartsComponent;
    private PieChart distributionChart;

    public StatisticsView() {
        this.setPadding(new Insets(20));
        this.setSpacing(20);

        // En-tête
        Label headerLabel = new Label("Statistiques du Système");
        headerLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        // Créer les composants
        VBox textStatsPanel = createTextStatsPanel();
        chartsComponent = new ChartComponent();

        // Graphique de distribution
        distributionChart = createDistributionChart();
        VBox chartBox = new VBox(10);
        Label distributionLabel = new Label("Distribution des agents");
        distributionLabel.setStyle("-fx-font-weight: bold;");
        chartBox.getChildren().addAll(distributionLabel, distributionChart);
        chartBox.setPadding(new Insets(15));
        chartBox.setStyle("-fx-background-color: #f8fafc; -fx-background-radius: 8px; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 5);");

        // Layout
        HBox chartsRow = new HBox(20, chartBox, chartsComponent);
        HBox.setHgrow(chartsComponent, Priority.ALWAYS);

        // Ajouter tous les composants
        this.getChildren().addAll(headerLabel, textStatsPanel, chartsRow);
    }

    private VBox createTextStatsPanel() {
        // Zone de texte pour afficher les statistiques
        statsTextArea = new TextArea();
        statsTextArea.setEditable(false);
        statsTextArea.setPrefHeight(150);
        statsTextArea.setWrapText(true);

        // Bouton pour récupérer les statistiques
        getStatsButton = new Button("Récupérer les statistiques");
        getStatsButton.setStyle("-fx-background-color: #3b82f6; -fx-text-fill: white; -fx-font-weight: bold;");
        getStatsButton.setPadding(new Insets(10, 20, 10, 20));

        // Assembler le panneau
        VBox panel = new VBox(10);
        Label title = new Label("Rapport détaillé");
        title.setStyle("-fx-font-weight: bold;");
        panel.getChildren().addAll(title, statsTextArea, getStatsButton);
        panel.setPadding(new Insets(15));
        panel.setStyle("-fx-background-color: #f8fafc; -fx-background-radius: 8px; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 5);");

        return panel;
    }

    private PieChart createDistributionChart() {
        // Données initiales pour le graphique
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Personne", 0),
                new PieChart.Data("Restaurant", 0),
                new PieChart.Data("Médiateur", 0),
                new PieChart.Data("Statistique", 0)
        );

        PieChart chart = new PieChart(pieChartData);
        chart.setTitle("Types d'agents");
        chart.setLegendVisible(true);
        chart.setLabelsVisible(true);

        return chart;
    }

    // Méthodes publiques pour accéder aux composants
    public Button getGetStatsButton() {
        return getStatsButton;
    }

    public ChartComponent getChartsComponent() {
        return chartsComponent;
    }

    // Méthode pour mettre à jour les statistiques textuelles
    public void updateTextStats(List<trio<String, String, Integer>> stats) {
        statsTextArea.clear();

        if (stats == null || stats.isEmpty()) {
            statsTextArea.setText("Aucune statistique disponible.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Rapport de réservations:\n\n");

        for (trio<String, String, Integer> stat : stats) {
            sb.append("Agent: ").append(stat.getA())
                    .append(", Restaurant: ").append(stat.getB())
                    .append(", Tentatives: ").append(stat.getC())
                    .append("\n");
        }

        statsTextArea.setText(sb.toString());
    }

    // Méthode pour mettre à jour le graphique de distribution
    public void updateDistributionChart(int personneCount, int restaurantCount, int mediateurCount, int statistiqueCount) {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Personne", personneCount),
                new PieChart.Data("Restaurant", restaurantCount),
                new PieChart.Data("Médiateur", mediateurCount),
                new PieChart.Data("Statistique", statistiqueCount)
        );

        distributionChart.setData(pieChartData);
    }

    // Méthode pour mettre à jour les graphiques à partir des données de réservation
    public void updateCharts(List<trio<String, String, Integer>> stats) {
        if (stats == null || stats.isEmpty()) {
            return;
        }

        // Calculer les données pour les graphiques
        Map<String, Integer> restaurantStats = new HashMap<>();
        Map<String, Integer> agentStats = new HashMap<>();

        for (trio<String, String, Integer> stat : stats) {
            String agent = stat.getA();
            String restaurant = stat.getB();
            Integer attempts = stat.getC();

            // Mettre à jour les statistiques par restaurant
            restaurantStats.put(restaurant, restaurantStats.getOrDefault(restaurant, 0) + attempts);

            // Mettre à jour les statistiques par agent
            agentStats.put(agent, agentStats.getOrDefault(agent, 0) + attempts);
        }

        // Mettre à jour les graphiques
        chartsComponent.updateData(restaurantStats, agentStats);
    }
}