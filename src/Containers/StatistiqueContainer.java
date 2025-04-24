package Containers;

import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;

public class StatistiqueContainer {

    public static void main(String[] args) {
        try {

            // Créer une instance de la MV
            Runtime rt = Runtime.instance();
            // Pas de propriétés, ce n'et pas un main container, mais un profile !
            ProfileImpl profile = new ProfileImpl(false);
            // Le main container associé est déjà démarré sur localhost
            profile.setParameter(ProfileImpl.MAIN_HOST, "localhost");
            profile.setParameter(ProfileImpl.CONTAINER_NAME, "Statistique-Container");

            AgentContainer ac = rt.createAgentContainer(profile);

            AgentController ag = ac.createNewAgent("Statistique","Agents.AgentStatistique",new Object[]{});
            ag.start();


            try {
                Thread.sleep(100000);  // Sleep for 60000 ms before dying
                ag.kill();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }



        } catch (ControllerException e) {
            e.printStackTrace();
        }





    }


}
