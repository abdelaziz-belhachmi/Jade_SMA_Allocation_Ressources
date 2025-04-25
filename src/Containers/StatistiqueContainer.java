package Containers;

import Agents.DAO.trio;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;
import jade.wrapper.StaleProxyException;

import java.util.ArrayList;
import java.util.List;

public class StatistiqueContainer {

    private static AgentContainer container;

    private static AgentController ag;

    public static void main(String[] args) {

        // Créer une instance de la MV
        Runtime rt = Runtime.instance();
        // Pas de propriétés, ce n'et pas un main container, mais un profile !
        ProfileImpl profile = new ProfileImpl(false);
        // Le main container associé est déjà démarré sur localhost
        profile.setParameter(ProfileImpl.MAIN_HOST, "localhost");
        profile.setParameter(ProfileImpl.CONTAINER_NAME, "Statistique-Container");

        container = rt.createAgentContainer(profile);
    }

    public static List<Agents.DAO.trio<String, String, Integer>> fetchStats() {
        if (Agents.AgentStatistique.getInstance() != null) {
            return Agents.AgentStatistique.getInstance().getStats();
        } else {
            System.out.println("AgentStatistique instance is null.");
            return new ArrayList<>();
        }
    }

    public static void startAgents() throws StaleProxyException {
        if (container != null){
        ag = container.createNewAgent("Statistique","Agents.AgentStatistique",new Object[]{});
        ag.start();
    }}


    // 👇 Shutdown method
    public static void shutdown() {
        if (container != null) {
            try {
                ag.kill();
                System.out.println("Statistique agent shut down successfully.");
            } catch (ControllerException e) {
                e.printStackTrace();
            }
        }
    }



}
