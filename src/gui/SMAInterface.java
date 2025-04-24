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
        Button startPersonne = new Button("Start Personne");
        Button startRestaurant = new Button("Start Restaurant");
        Button startMediateur = new Button("Start Mediateur");
        Button startStatistique = new Button("Start Statistique");

        // === Kill Buttons ===
        Button killPersonne = new Button("Kill Personne");
        Button killRestaurant = new Button("Kill Restaurant");
        Button killMediateur = new Button("Kill Mediateur");
        Button killStatistique = new Button("Kill Statistique");

        // === Start Actions ===
        startPersonne.setOnAction(e -> launchContainer("Containers.PersonneContainer"));
        startRestaurant.setOnAction(e -> launchContainer("Containers.RestaurantContainer"));
        startMediateur.setOnAction(e -> launchContainer("Containers.MediateurContainer"));
        startStatistique.setOnAction(e -> launchContainer("Containers.StatistiqueContainer"));

        // === Kill Actions ===
        killPersonne.setOnAction(e -> shutdownContainer("Containers.PersonneContainer"));
        killRestaurant.setOnAction(e -> shutdownContainer("Containers.RestaurantContainer"));
        killMediateur.setOnAction(e -> shutdownContainer("Containers.MediateurContainer"));
        killStatistique.setOnAction(e -> shutdownContainer("Containers.StatistiqueContainer"));

        // === Layout with GridPane ===
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setHgap(10);
        grid.setVgap(10);

        grid.addRow(0, startPersonne, killPersonne);
        grid.addRow(1, startRestaurant, killRestaurant);
        grid.addRow(2, startMediateur, killMediateur);
        grid.addRow(3, startStatistique, killStatistique);

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


    private void shutdownContainer(String className) {
        try {
            Class<?> clazz = Class.forName(className);
            clazz.getMethod("shutdown").invoke(null);  // Call static shutdown()
            statsArea.appendText("Shutdown invoked for " + className + "\n");
        } catch (Exception e) {
            e.printStackTrace();
            statsArea.appendText("Failed to shutdown: " + className + "\n");
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
