package Containers;

import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.ControllerException;

public class MediateurContainer {

    public static void main(String[] args) {
        try {

            // Créer une instance de la MV
            Runtime rt = Runtime.instance();
            // Pas de propriétés, ce n'et pas un main container, mais un profile !
            ProfileImpl profile = new ProfileImpl(false);
            // Le main container associé est déjà démarré sur localhost
            profile.setParameter(ProfileImpl.MAIN_HOST, "localhost");
            profile.setParameter(ProfileImpl.CONTAINER_NAME, "Mediateur-Container");

            AgentContainer ac = rt.createAgentContainer(profile);

            ac.createNewAgent("Mediateur","Agents.AgentMediateur",new Object[]{}).start();

        } catch (ControllerException e) {
            e.printStackTrace();
        }
    }

}
