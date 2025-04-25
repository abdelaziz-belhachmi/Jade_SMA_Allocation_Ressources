package gui;

import Agents.DAO.trio;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.util.ExtendedProperties;
import jade.util.leap.Properties;
import jade.wrapper.AgentContainer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.io.*;
import java.lang.reflect.Method;
import java.util.List;

public class SMAInterface extends Application {

    private TextArea statsArea = new TextArea();

    @Override
    public void start(Stage primaryStage) {

        // === Start Buttons ===
        Button startPersonne = new Button("Start Personne Container");
        Button startRestaurant = new Button("Start Restaurant Container");
        Button startMediateur = new Button("Start Mediateur Container");
        Button startStatistique = new Button("Start Statistique Container");

        // === Create Buttons ===
        Button createAgentsPersonne = new Button("Create Agents Personnes");
        Button createAgentsRestaurant = new Button("Create Agents Restaurant");
        Button createAgentsMediateur = new Button("Create Agent Mediateur");
        Button createAgentsStatistique = new Button("Create Agent Statistique");


        Button getStatistics = new Button("Get Statistics");
//

        Button startReservations = new Button("Start Reservations");
        startReservations.setOnAction(e -> {
            try {
                Class<?> clazz = Class.forName("Containers.PersonneContainer");
                java.lang.reflect.Method m = clazz.getMethod("triggerReservations");
                m.invoke(null);
                statsArea.appendText("Reservations triggered via GUI\n");
            } catch (Exception ex) {
                ex.printStackTrace();
                statsArea.appendText("Error during reservation trigger.\n");
            }
        });


        getStatistics.setOnAction(e -> {
            try {
                Class<?> clazz = Class.forName("Containers.StatistiqueContainer");
                java.lang.reflect.Method m = clazz.getMethod("fetchStats");
                List<Agents.DAO.trio<String, String, Integer>> stats =
                        (List<trio<String, String, Integer>>) m.invoke(null);

                statsArea.clear();
                for (Agents.DAO.trio<String, String, Integer> stat : stats) {
                    statsArea.appendText("Agent: " + stat.getA() +
                            ", Restaurant: " + stat.getB() +
                            ", Tentatives: " + stat.getC() + "\n");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                statsArea.appendText("Erreur lors de la récupération des statistiques.\n");
            }
        });

        // === Start Actions ===
        startPersonne.setOnAction(e -> launchContainer("Containers.PersonneContainer"));
        startRestaurant.setOnAction(e -> launchContainer("Containers.RestaurantContainer"));
        startMediateur.setOnAction(e -> launchContainer("Containers.MediateurContainer"));
        startStatistique.setOnAction(e -> launchContainer("Containers.StatistiqueContainer"));

        // === Create Agent Actions ===
        createAgentsPersonne.setOnAction(e -> CreateAgents("Containers.PersonneContainer"));
        createAgentsRestaurant.setOnAction(e -> CreateAgents("Containers.RestaurantContainer"));
        createAgentsMediateur.setOnAction(e -> CreateAgents("Containers.MediateurContainer"));
        createAgentsStatistique.setOnAction(e -> CreateAgents("Containers.StatistiqueContainer"));

        // === Layout with GridPane ===
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setHgap(80);
        grid.setVgap(10);

        grid.addRow(0, startStatistique, createAgentsStatistique);
        grid.addRow(1, startMediateur, createAgentsMediateur);
        grid.addRow(2, startRestaurant, createAgentsRestaurant);
        grid.addRow(3, startPersonne, createAgentsPersonne);
        grid.addRow(4, startReservations, getStatistics);

        // === Text area ===
        statsArea.setEditable(false);
        statsArea.setPrefHeight(250);

        // === Main layout ===
        BorderPane root = new BorderPane();
        root.setTop(grid);
        root.setCenter(statsArea);
        root.setPadding(new Insets(10));

        Scene scene = new Scene(root, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("SMA Multi-Agent Interface");
        primaryStage.show();
    }


    private void CreateAgents(String className) {
        try {
            Class<?> clazz = Class.forName(className);
            clazz.getMethod("startAgents").invoke(null);  // Call static startAgents()
            statsArea.appendText("Staring agents of " + className + "\n");
        } catch (Exception e) {
            e.printStackTrace();
            statsArea.appendText("Failed to start agents: " + className + "\n");
        }
    }



    private void launchContainer(String className) {
        try {
            Class<?> clazz = Class.forName(className);
            Method mainMethod = clazz.getMethod("main", String[].class);

            String[] args = new String[]{}; // or provide actual args if needed
            mainMethod.invoke(null, (Object) args); // important: cast to Object!

            statsArea.appendText("Launched container: " + className + "\n");
        } catch (Exception e) {
            e.printStackTrace();
            statsArea.appendText("Failed to launch container: " + className + "\n");
        }
    }


    public static void main(String[] args) {

        try {
            Runtime rt = Runtime.instance();

            Properties prop = new ExtendedProperties();
            prop.setProperty(Profile.GUI,"true");

            Profile profile = new ProfileImpl(prop);
            AgentContainer mainContainer = rt.createMainContainer(profile);

            mainContainer.start();

        } catch (Exception e) {
            e.printStackTrace();
        }


        launch(args);
    }
}
