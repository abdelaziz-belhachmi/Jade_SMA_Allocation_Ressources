package gui;

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
        Button startStatistique = new Button("Start Statistique");
        Button startRestaurant = new Button("Start Restaurant Agents");
        Button startMediateur = new Button("Start Mediateur");
        Button startPersonne = new Button("Start Personne Agents");

        startPersonne.setOnAction(e -> launchContainer("Containers.PersonneContainer"));
        startRestaurant.setOnAction(e -> launchContainer("Containers.RestaurantContainer"));
        startMediateur.setOnAction(e -> launchContainer("Containers.MediateurContainer"));
        startStatistique.setOnAction(e -> launchContainer("Containers.StatistiqueContainer"));

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
            ProcessBuilder pb = new ProcessBuilder("java", className);
            pb.directory(new File("out/production/SMAProject")); // adjust to your output folder
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
        launch(args);
    }
}
