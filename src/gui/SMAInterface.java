package gui;

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

//    public void start(Stage primaryStage) {
////        launchContainer("Containers.Main");
//
//        Button startStatistique = new Button("Start Statistique");
//        Button startRestaurant = new Button("Start Restaurant Agents");
//        Button startMediateur = new Button("Start Mediateur");
//        Button startPersonne = new Button("Start Personne Agents");
//
//        startStatistique.setOnAction(e -> launchContainer("Containers.StatistiqueContainer"));
//        startRestaurant.setOnAction(e -> launchContainer("Containers.RestaurantContainer"));
//        startMediateur.setOnAction(e -> launchContainer("Containers.MediateurContainer"));
//        startPersonne.setOnAction(e -> launchContainer("Containers.PersonneContainer"));
//
//        VBox buttons = new VBox(10, startPersonne, startRestaurant, startMediateur, startStatistique);
//        statsArea.setEditable(false);
//        statsArea.setPrefHeight(250);
//
//        BorderPane root = new BorderPane();
//        root.setTop(buttons);
//        root.setCenter(statsArea);
//        root.setPadding(new Insets(10));
//
//        Scene scene = new Scene(root, 600, 400);
//        primaryStage.setScene(scene);
//        primaryStage.setTitle("SMA Multi-Agent Interface");
//        primaryStage.show();
//    }
//

    private void launchContainer(String className) {
        try {
            String jadePath = "lib/jade.jar";
            String compiledPath = "out/production/jade_sma_allocation_de_ressources";
            String classpath = jadePath + ";" + compiledPath;  // Windows uses ';'

            ProcessBuilder pb = new ProcessBuilder(
                    "java",
                    "-cp", classpath,
                    className
            );

            pb.directory(new File(".")); // working directory: project root
            pb.redirectErrorStream(true);
            Process p = pb.start();

            new Thread(() -> {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        System.out.println(line);
                        final String finalLine = line;
                        javafx.application.Platform.runLater(() -> statsArea.appendText(finalLine + "\n"));
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }).start();

        } catch (IOException e) {
            e.printStackTrace();
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
