package gui;

import Agents.DAO.trio;
import gui.widgets.AgentsView;
import gui.widgets.ContainersView;
import gui.widgets.StatisticsView;
import gui.SidebarMenu;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.util.ExtendedProperties;
import jade.util.leap.Properties;
import jade.wrapper.AgentContainer;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.lang.reflect.Method;
import java.util.List;

public class SMAInterface extends Application implements SidebarMenu.ViewChangeListener {

    private DashboardView dashboardView;
    private AgentsView agentsView;
    private ContainersView containersView;
    private StatisticsView statisticsView;
    private StackPane viewContainer;

    private int launchedContainers = 0;
    private int createdAgents = 0;
    private int totalReservations = 0;
    private int failedReservations = 0;

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();

        SidebarMenu sidebar = new SidebarMenu(this);
        root.setLeft(sidebar);

        viewContainer = new StackPane();
        viewContainer.setPadding(new Insets(10));

        dashboardView = new DashboardView(this);
        containersView = new ContainersView();
        agentsView = new AgentsView();
        statisticsView = new StatisticsView();

        viewContainer.getChildren().addAll(dashboardView, containersView, agentsView, statisticsView);
        showView("dashboard");

        root.setCenter(viewContainer);
        Scene scene = new Scene(root, 1200, 800);
        scene.getStylesheets().add(getClass().getResource("/gui/style.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.setTitle("Centre de Contrôle SMA");
        primaryStage.show();

        setupContainerActions();
        setupAgentActions();
    }

    private void setupContainerActions() {
        containersView.getStartPersonneButton().setOnAction(e -> launchContainer("Containers.PersonneContainer"));
        containersView.getStartRestaurantButton().setOnAction(e -> launchContainer("Containers.RestaurantContainer"));
        containersView.getStartMediateurButton().setOnAction(e -> launchContainer("Containers.MediateurContainer"));
        containersView.getStartStatistiqueButton().setOnAction(e -> launchContainer("Containers.StatistiqueContainer"));
    }

    private void setupAgentActions() {
        agentsView.getCreatePersonnesButton().setOnAction(e -> createAgents("Containers.PersonneContainer", 9));
        agentsView.getCreateRestaurantButton().setOnAction(e -> createAgents("Containers.RestaurantContainer", 4));
        agentsView.getCreateMediateurButton().setOnAction(e -> createAgents("Containers.MediateurContainer", 1));
        agentsView.getCreateStatistiqueButton().setOnAction(e -> createAgents("Containers.StatistiqueContainer", 1));
        agentsView.getStartReservationsButton().setOnAction(e -> startReservations());
    }

    public void launchContainer(String className) {
        try {
            Class<?> clazz = Class.forName(className);
            Method mainMethod = clazz.getMethod("main", String[].class);
            mainMethod.invoke(null, (Object) new String[0]);
            launchedContainers++;
            dashboardView.updateContainersCount(launchedContainers);
            log("Launched container: " + className);

            // ➤ Mise à jour visuelle du statut du conteneur
            containersView.updateContainerStatus(className, true);

        } catch (Exception e) {
            e.printStackTrace();
            log("Échec lancement container: " + className);
            containersView.updateContainerStatus(className, false); // Affiche le cercle en rouge si échec
        }
    }


    public void showView(String name) {
        dashboardView.setVisible(false);
        containersView.setVisible(false);
        agentsView.setVisible(false);
        statisticsView.setVisible(false);

        switch (name) {
            case "dashboard" -> dashboardView.setVisible(true);
            case "containers" -> containersView.setVisible(true);
            case "agents" -> agentsView.setVisible(true);
            case "statistics" -> statisticsView.setVisible(true);
        }
    }

    @Override
    public void onViewChange(String viewName) {
        showView(viewName);
    }


    public void createAgents(String className, int count) {
        try {
            Class<?> clazz = Class.forName(className);
            clazz.getMethod("startAgents").invoke(null);
            createdAgents += count;
            dashboardView.updateAgentsCount(createdAgents);
            updatePieChart(className, count);
            log("Agents créés via: " + className);

            // Mise à jour visuelle dans la table des agents
            String typeLabel = getTypeFromClassName(className);
            for (int i = 1; i <= count; i++) {
                String agentName = typeLabel + "-" + i;
                agentsView.addAgent(agentName, typeLabel, "Actif");
            }

        } catch (Exception e) {
            e.printStackTrace();
            log("Échec création agents: " + className);
        }
    }

    private String getTypeFromClassName(String className) {
        if (className.contains("Personne")) return "Personne";
        if (className.contains("Restaurant")) return "Restaurant";
        if (className.contains("Mediateur")) return "Médiateur";
        if (className.contains("Statistique")) return "Statistique";
        return "Inconnu";
    }

    public void startReservations() {
        try {
            Class<?> clazz = Class.forName("Containers.PersonneContainer");
            Method m = clazz.getMethod("triggerReservations");
            m.invoke(null);
            log("Réservations lancées.");

            List<trio<String, String, Integer>> stats = getStatistics();
            if (stats != null) {
                totalReservations = stats.size();
                failedReservations = (int) stats.stream().filter(s -> s.getC() > 1).count();
                double successRate = totalReservations == 0 ? 0 : 100.0 * (totalReservations - failedReservations) / totalReservations;

                dashboardView.updateReservationsCount(totalReservations, failedReservations);
                dashboardView.updateSuccessRate(successRate);

                int t = 0;
                XYChart.Series<Number, Number> reservationSeries = dashboardView.getReservationSeries();
                XYChart.Series<Number, Number> agentActivitySeries = dashboardView.getAgentActivitySeries();
                reservationSeries.getData().clear();
                agentActivitySeries.getData().clear();

                for (trio<String, String, Integer> stat : stats) {
                    t++;
                    int attemptCount = stat.getC();
                    reservationSeries.getData().add(new XYChart.Data<>(t, 1));
                    agentActivitySeries.getData().add(new XYChart.Data<>(t, attemptCount));
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            log("Erreur lors du lancement des réservations.");
        }
    }

    public List<trio<String, String, Integer>> getStatistics() {
        try {
            Class<?> clazz = Class.forName("Containers.StatistiqueContainer");
            Method m = clazz.getMethod("fetchStats");
            return (List<trio<String, String, Integer>>) m.invoke(null);
        } catch (Exception ex) {
            ex.printStackTrace();
            log("Erreur récupération statistiques.");
            return null;
        }
    }

    private void updatePieChart(String className, int count) {
        PieChart chart = dashboardView.getPieChart();
        for (PieChart.Data data : chart.getData()) {
            String label = data.getName();
            if ((className.contains("Personne") && label.equals("Personne")) ||
                    (className.contains("Restaurant") && label.equals("Restaurant")) ||
                    (className.contains("Mediateur") && label.equals("Médiateur")) ||
                    (className.contains("Statistique") && label.equals("Statistique"))) {
                data.setPieValue(data.getPieValue() + count);
            }
        }
    }

    private void log(String message) {
        dashboardView.addLogMessage(message);
        containersView.appendLog(message);
        agentsView.appendLog(message);
    }

    public static void main(String[] args) {
        try {
            Runtime rt = Runtime.instance();
            Properties prop = new ExtendedProperties();
            prop.setProperty(Profile.GUI, "true");
            Profile profile = new ProfileImpl(prop);
            AgentContainer mainContainer = rt.createMainContainer(profile);
            mainContainer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        launch(args);
    }
}
