package gui;

import gui.widgets.InfoCard;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

public class DashboardView extends BorderPane {

    private SMAInterface mainApp;
    private TextArea logArea;
    private XYChart.Series<Number, Number> reservationSeries;
    private XYChart.Series<Number, Number> agentActivitySeries;
    private PieChart pieChart;

    private InfoCard containersCard, agentsCard, reservationsCard, performanceCard;

    // Variables pour suivre le temps pour les graphiques
    private int timeCounter = 0;

    // Constants for styling
    private static final int CARD_WIDTH = 250;
    private static final int CARD_HEIGHT = 150;
    private static final int CHART_HEIGHT = 350;
    private static final int LOG_HEIGHT = 200;
    private static final String TITLE_STYLE = "-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;";
    private static final String SECTION_STYLE = "-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #34495e;";

    public DashboardView(SMAInterface mainApp) {
        this.mainApp = mainApp;
        this.setPadding(new Insets(20));
        this.setStyle("-fx-background-color: #f8f9fa;");

        setupDashboard();
    }

    private void setupDashboard() {
        // Main container
        VBox mainContainer = new VBox(25);
        mainContainer.setPrefWidth(USE_COMPUTED_SIZE);
        mainContainer.setFillWidth(true);

        // Title section
        HBox titleSection = createTitleSection();

        // Info cards section
        FlowPane infoCardsPane = createInfoCardsSection();

        // Charts section
        VBox chartsSection = createChartsSection();

        // Logs section
        VBox logsSection = createLogsSection();

        // Add all sections to main container
        mainContainer.getChildren().addAll(titleSection, infoCardsPane, chartsSection, logsSection);

        // Wrap in ScrollPane for responsive layout
        ScrollPane scrollPane = new ScrollPane(mainContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle("-fx-background-color: transparent;");

        this.setCenter(scrollPane);
    }

    private HBox createTitleSection() {
        HBox titleBox = new HBox();
        titleBox.setAlignment(Pos.CENTER_LEFT);

        Label dashboardTitle = new Label("Tableau de bord");
        dashboardTitle.setStyle(TITLE_STYLE);

        // Add dashboard controls here if needed
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        titleBox.getChildren().addAll(dashboardTitle, spacer);
        return titleBox;
    }

    private FlowPane createInfoCardsSection() {
        FlowPane cardsPane = new FlowPane();
        cardsPane.setHgap(20);
        cardsPane.setVgap(20);
        cardsPane.setPrefWrapLength(1200);
        cardsPane.setAlignment(Pos.CENTER);

        // Create info cards with improved styles
        containersCard = new InfoCard("Conteneurs", "0", "Aucun conteneur actif", "container-icon");
        containersCard.setPrefSize(CARD_WIDTH, CARD_HEIGHT);
        containersCard.setStyle("-fx-background-color: white; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 5);");

        agentsCard = new InfoCard("Agents", "0", "Aucun agent", "agent-icon");
        agentsCard.setPrefSize(CARD_WIDTH, CARD_HEIGHT);
        agentsCard.setStyle("-fx-background-color: white; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 5);");

        reservationsCard = new InfoCard("Réservations", "0", "Aucune réservation", "reservation-icon");
        reservationsCard.setPrefSize(CARD_WIDTH, CARD_HEIGHT);
        reservationsCard.setStyle("-fx-background-color: white; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 5);");

        performanceCard = new InfoCard("Performance", "0%", "Pas encore de transactions", "performance-icon");
        performanceCard.setPrefSize(CARD_WIDTH, CARD_HEIGHT);
        performanceCard.setStyle("-fx-background-color: white; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 5);");

        cardsPane.getChildren().addAll(containersCard, agentsCard, reservationsCard, performanceCard);
        return cardsPane;
    }

    private VBox createChartsSection() {
        VBox chartsBox = new VBox(15);

        Label chartsTitle = new Label("Activité du système");
        chartsTitle.setStyle(SECTION_STYLE);

        HBox chartsRow = new HBox(20);
        chartsRow.setAlignment(Pos.CENTER);

        // Configure line chart for reservations
        NumberAxis xAxis = new NumberAxis(0, 50, 10);
        NumberAxis yAxis = new NumberAxis(0, 10, 10);
        xAxis.setLabel("Temps");
        yAxis.setLabel("Nombre");

        // Amélioration: Rendre le graphique plus lisible
        xAxis.setAnimated(false);
        yAxis.setAnimated(false);

        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Réservations en temps réel");
        lineChart.setPrefHeight(CHART_HEIGHT);
        lineChart.setMinWidth(500);
        lineChart.setMaxWidth(700);
        lineChart.setAnimated(false);
        lineChart.setCreateSymbols(true); // S'assurer que les symboles sont créés
        lineChart.setStyle("-fx-background-color: white; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 5);");

        // Appliquer du CSS personnalisé pour de plus grands symboles et des lignes plus épaisses
        lineChart.getStylesheets().add(getClass().getResource("styles/chart-styles.css").toExternalForm());

        // Si le fichier CSS n'est pas disponible, appliquer le style directement
        lineChart.setStyle(lineChart.getStyle() +
                "-fx-stroke-width: 3px; " +
                "-fx-symbols-size: 12px;");

        // Configure la série avec un style visible pour les lignes et les points
        reservationSeries = new XYChart.Series<>();
        reservationSeries.setName("Réservations");

        agentActivitySeries = new XYChart.Series<>();
        agentActivitySeries.setName("Activité des agents");

        lineChart.getData().add(reservationSeries);
        lineChart.getData().add(agentActivitySeries);

        // Appliquer un style CSS pour améliorer la visibilité des lignes
        lineChart.applyCss();

        // Amélioration: Style pour les séries
        String reservationStyle = "-fx-stroke: #ff6384; -fx-stroke-width: 2px;";
        String agentStyle = "-fx-stroke: #36a2eb; -fx-stroke-width: 2px;";

        // Customize the line chart appearance
        lineChart.lookup(".chart-plot-background").setStyle("-fx-background-color: #f8f9fa;");

        // Configure pie chart for agent distribution
        pieChart = new PieChart();
        pieChart.setTitle("Distribution des agents");
        pieChart.setPrefHeight(CHART_HEIGHT);
        pieChart.setMinWidth(350);
        pieChart.setMaxWidth(450);
        pieChart.setLabelsVisible(true);
        pieChart.setStyle("-fx-background-color: white; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 5);");

        // Add initial data to pie chart
        pieChart.getData().addAll(
                new PieChart.Data("Personne", 25),
                new PieChart.Data("Restaurant", 25),
                new PieChart.Data("Médiateur", 25),
                new PieChart.Data("Statistique", 25)
        );

        // Apply custom colors to pie chart slices
        applyCustomColorsToPieChart();

        // Create a VBox for each chart with title and legend
        VBox lineChartBox = new VBox(5);
        lineChartBox.getChildren().add(lineChart);
        HBox.setHgrow(lineChartBox, Priority.ALWAYS);

        VBox pieChartBox = new VBox(5);
        pieChartBox.getChildren().add(pieChart);

        chartsRow.getChildren().addAll(lineChartBox, pieChartBox);
        chartsBox.getChildren().addAll(chartsTitle, chartsRow);

        return chartsBox;
    }

    private void applyCustomColorsToPieChart() {
        String[] pieColors = {
                "#ff6384", // Personne
                "#ffcd56", // Restaurant
                "#4bc0c0", // Médiateur
                "#36a2eb"  // Statistique
        };

        int i = 0;
        for (PieChart.Data data : pieChart.getData()) {
            String color = pieColors[i % pieColors.length];
            data.getNode().setStyle("-fx-pie-color: " + color + ";");
            i++;
        }
    }

    private VBox createLogsSection() {
        VBox logsBox = new VBox(10);

        HBox logHeader = new HBox();
        logHeader.setAlignment(Pos.CENTER_LEFT);

        Label logsTitle = new Label("Journal d'activité");
        logsTitle.setStyle(SECTION_STYLE);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Could add log controls here if needed

        logHeader.getChildren().addAll(logsTitle, spacer);

        logArea = new TextArea();
        logArea.setEditable(false);
        logArea.setPrefHeight(LOG_HEIGHT);
        logArea.setStyle("-fx-control-inner-background: #f8f9fa; -fx-font-family: 'Consolas', 'Monaco', monospace; -fx-font-size: 12px;");

        // Initial log messages
        logArea.appendText("Système démarré.\n");
        logArea.appendText("En attente d'actions...\n");

        logsBox.getChildren().addAll(logHeader, logArea);
        VBox.setVgrow(logArea, Priority.ALWAYS);

        return logsBox;
    }

    // === Public methods ===

    public void addLogMessage(String message) {
        logArea.appendText(message + "\n");
        logArea.setScrollTop(Double.MAX_VALUE);
    }

    public void updateContainersCount(int count) {
        containersCard.updateValue(String.valueOf(count));
        containersCard.updateDescription(count + " conteneurs actifs");
    }

    public void updateAgentsCount(int count) {
        agentsCard.updateValue(String.valueOf(count));
        agentsCard.updateDescription("Tous les agents sont opérationnels");

        // Update pie chart with new agent distribution
        updateAgentDistribution(count);
    }

    private void updateAgentDistribution(int totalAgents) {
        if (totalAgents == 0) {
            // Reset pie chart
            for (PieChart.Data data : pieChart.getData()) {
                data.setPieValue(25); // Equal values for empty state
            }
        } else {
            // Update with simulated distribution
            pieChart.getData().get(0).setPieValue(Math.round(totalAgents * 0.4)); // 40% Personne
            pieChart.getData().get(1).setPieValue(Math.round(totalAgents * 0.3)); // 30% Restaurant
            pieChart.getData().get(2).setPieValue(Math.round(totalAgents * 0.2)); // 20% Médiateur
            pieChart.getData().get(3).setPieValue(Math.round(totalAgents * 0.1)); // 10% Statistique
        }
    }

    public void updateReservationsCount(int total, int pending) {
        reservationsCard.updateValue(String.valueOf(total));
        reservationsCard.updateDescription(pending + " réservations en attente");
    }

    public void updateSuccessRate(double rate) {
        performanceCard.updateValue(String.format("%.1f%%", rate));
        performanceCard.updateDescription("Taux de réussite des transactions");
    }

    public XYChart.Series<Number, Number> getReservationSeries() {
        return reservationSeries;
    }

    public XYChart.Series<Number, Number> getAgentActivitySeries() {
        return agentActivitySeries;
    }

    public PieChart getPieChart() {
        return pieChart;
    }

    /**
     * Creates a demo data simulation for the dashboard
     * This can be enabled for testing and demonstration purposes
     */
    public void setupDataSimulation() {
        // Initialiser avec des données significatives pour avoir des lignes visibles dès le départ
        for (int i = 0; i < 5; i++) {
            // Ajouter des valeurs non nulles pour que les lignes soient visibles
            reservationSeries.getData().add(new XYChart.Data<>(i, 10 + i * 5)); // Valeurs croissantes
            agentActivitySeries.getData().add(new XYChart.Data<>(i, 20 + i * 3)); // Différent des réservations
        }

        timeCounter = 5; // Commencer à 5 puisque nous avons déjà ajouté 5 points

        // Appliquer immédiatement des styles aux points initiaux
        applySeriesToLinechart();

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), event -> {
                    // Update counters with random data
                    int containers = (int) (Math.random() * 10) + 1; // Minimum 1
                    int agents = (int) (Math.random() * 15) + 5; // Minimum 5
                    int reservations = (int) (Math.random() * 50) + 20; // Valeurs significatives entre 20-70
                    int pending = (int) (Math.random() * 10);
                    double rate = Math.random() * 100;

                    updateContainersCount(containers);
                    updateAgentsCount(agents);
                    updateReservationsCount(reservations, pending);
                    updateSuccessRate(rate);

                    // Mettre à jour les données du graphique linéaire
                    if (reservationSeries.getData().size() > 20) {
                        // Supprimer les points les plus anciens pour garder le graphique lisible
                        reservationSeries.getData().remove(0);
                        agentActivitySeries.getData().remove(0);
                    }

                    // Ajouter les nouveaux points avec le compteur de temps incrémenté
                    timeCounter++;

                    // Ajouter des données avec des variations pour mieux visualiser les lignes
                    // Utiliser les valeurs précédentes pour créer une continuité
                    double lastReservationValue = reservationSeries.getData().get(reservationSeries.getData().size()-1).getYValue().doubleValue();
                    double lastAgentValue = agentActivitySeries.getData().get(agentActivitySeries.getData().size()-1).getYValue().doubleValue();

                    // Créer une variation progressive (pas trop brutale) pour une ligne plus douce
                    double newReservationValue = Math.max(10, Math.min(90, lastReservationValue + (Math.random() * 20 - 10)));
                    double newAgentValue = Math.max(15, Math.min(80, lastAgentValue + (Math.random() * 16 - 8)));

                    // Ajouter les nouveaux points
                    XYChart.Data<Number, Number> resData = new XYChart.Data<>(timeCounter, newReservationValue);
                    XYChart.Data<Number, Number> agentData = new XYChart.Data<>(timeCounter, newAgentValue);

                    reservationSeries.getData().add(resData);
                    agentActivitySeries.getData().add(agentData);

                    // Appliquer les styles aux nouveaux points immédiatement
                    styleDataPoint(resData, "#ff6384", 8);
                    styleDataPoint(agentData, "#36a2eb", 8);

                    // Add log message
                    String[] actions = {"Conteneur créé", "Agent démarré", "Réservation effectuée", "Transaction complétée"};
                    String action = actions[(int) (Math.random() * actions.length)];
                    addLogMessage(action + " à " + java.time.LocalTime.now().toString());
                })
        );

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    /**
     * Applique un style à un point de données spécifique
     */
    private void styleDataPoint(XYChart.Data<Number, Number> data, String color, int size) {
        // Le nœud peut être null jusqu'à ce qu'il soit affiché, donc on attache un écouteur
        if (data.getNode() != null) {
            // Style direct si le nœud existe déjà
            data.getNode().setStyle(
                    "-fx-background-color: " + color + ", white;" +
                            "-fx-background-insets: 0, 2;" +
                            "-fx-background-radius: " + size + "px;" +
                            "-fx-padding: " + size + "px;"
            );
        } else {
            // Pour les points qui n'ont pas encore été rendus, JavaFX les créera plus tard
            data.nodeProperty().addListener((obs, oldNode, newNode) -> {
                if (newNode != null) {
                    newNode.setStyle(
                            "-fx-background-color: " + color + ", white;" +
                                    "-fx-background-insets: 0, 2;" +
                                    "-fx-background-radius: " + size + "px;" +
                                    "-fx-padding: " + size + "px;"
                    );
                }
            });
        }
    }

    /**
     * Applique des styles spécifiques aux séries du graphique linéaire
     * pour améliorer la visibilité des lignes et des points
     */
    private void applySeriesToLinechart() {
        if (getReservationSeries().getChart() == null) {
            return; // Protection contre NullPointerException
        }

        // Appliquer un style global aux séries pour des lignes plus épaisses
        getReservationSeries().getChart().lookupAll(".chart-series-line").forEach(node -> {
            node.setStyle("-fx-stroke-width: 3px;");
        });

        // Appliquer des styles aux séries
        for (XYChart.Series<Number, Number> series : getReservationSeries().getChart().getData()) {
            String color = series.getName().equals("Réservations") ? "#ff6384" : "#36a2eb";

            // Style pour les lignes de la série
            if (series.getNode() != null) {
                series.getNode().lookup(".chart-series-line").setStyle(
                        "-fx-stroke: " + color + ";" +
                                "-fx-stroke-width: 3px;"
                );
            }

            // Style pour les points
            for (XYChart.Data<Number, Number> data : series.getData()) {
                if (data.getNode() != null) {
                    data.getNode().setStyle(
                            "-fx-background-color: " + color + ", white;" +
                                    "-fx-background-insets: 0, 2;" +
                                    "-fx-background-radius: 8px;" +
                                    "-fx-padding: 8px;"
                    );
                } else {
                    // Pour les points qui seront rendus plus tard
                    data.nodeProperty().addListener((obs, oldNode, newNode) -> {
                        if (newNode != null) {
                            newNode.setStyle(
                                    "-fx-background-color: " + color + ", white;" +
                                            "-fx-background-insets: 0, 2;" +
                                            "-fx-background-radius: 8px;" +
                                            "-fx-padding: 8px;"
                            );
                        }
                    });
                }
            }
        }
    }
}