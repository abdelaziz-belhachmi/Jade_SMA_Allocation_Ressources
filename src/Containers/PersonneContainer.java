package Containers;

import jade.core.ProfileImpl;
import jade.wrapper.AgentContainer;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;

import java.util.ArrayList;
import java.util.List;

public class PersonneContainer {

    private static AgentContainer container;
    private static List<AgentController> ag = new ArrayList<>();

    private static int numberOfPersonnes = 9;

    public static void main(String[] args) {
        try {

            // Créer une instance de la MV
            Runtime rt = Runtime.instance();
            // Pas de propriétés, ce n'et pas un main container, mais un profile !
            ProfileImpl profile = new ProfileImpl(false);
            // Le main container associé est déjà démarré sur localhost
            profile.setParameter(ProfileImpl.MAIN_HOST, "localhost");
            profile.setParameter(ProfileImpl.CONTAINER_NAME, "Personne-Container");

            container = rt.createAgentContainer(profile);

            for (int i = 0; i < numberOfPersonnes; i++) {
                ag.add(container.createNewAgent("Personne-"+i,"Agents.AgentPersonne",new Object[]{}));
                ag.get(i).start();
            }

        //    agent.start();


        } catch (ControllerException e) {
            e.printStackTrace();
        }
    }


    // 👇 Shutdown method
    public static void shutdown() {
        if (container != null) {
            try {
                for (AgentController a: ag){
                    a.kill();
                }
                System.out.println("ALL Personnes shut down successfully.");
            } catch (ControllerException e) {
                e.printStackTrace();
            }
        }
    }



}