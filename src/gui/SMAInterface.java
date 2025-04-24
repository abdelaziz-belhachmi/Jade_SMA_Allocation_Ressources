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
//        launchContainer("Containers.Main");

        Button startStatistique = new Button("Start Statistique");
        Button startRestaurant = new Button("Start Restaurant Agents");
        Button startMediateur = new Button("Start Mediateur");
        Button startPersonne = new Button("Start Personne Agents");

        startStatistique.setOnAction(e -> launchContainer("Containers.StatistiqueContainer"));
        startRestaurant.setOnAction(e -> launchContainer("Containers.RestaurantContainer"));
        startMediateur.setOnAction(e -> launchContainer("Containers.MediateurContainer"));
        startPersonne.setOnAction(e -> launchContainer("Containers.PersonneContainer"));

        VBox buttons = new VBox(10, startPersonne, startRestaurant, startMediateur, startStatistique);
        statsArea.setEditable(false);
        statsArea.setPrefHeight(250);

        BorderPane root = new BorderPane();
        root.setTop(buttons);
        root.setCenter(statsArea);
        root.setPadding(new Insets(10));

        Scene scene = new Scene(root, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("SMA Multi-Agent Interface");
        primaryStage.show();
    }


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
