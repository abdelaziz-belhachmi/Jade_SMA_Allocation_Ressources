import jade.core.ProfileImpl;
import jade.wrapper.AgentContainer;
import jade.core.Runtime;
import jade.wrapper.ControllerException;

public class PersonneContainer {

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

            AgentContainer ac = rt.createAgentContainer(profile);

            for (int i = 0; i < numberOfPersonnes; i++) {
                ac.createNewAgent("Personne-"+i,"Agents.AgentPersonne",new Object[]{}).start();
            }

        //    agent.start();


        } catch (ControllerException e) {
            e.printStackTrace();
        }
    }

}