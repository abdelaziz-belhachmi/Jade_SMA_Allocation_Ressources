import jade.core.ProfileImpl;
import jade.wrapper.AgentContainer;
import jade.wrapper.ContainerController;
import jade.core.Runtime;
import jade.wrapper.ControllerException;

public class JadeContainer {

    public static void main(String[] args) throws ControllerException {
        // Créer une instance de la MV
        Runtime rt = Runtime.instance();
        // Pas de propriétés, ce n'et pas un main container, mais un profile !
        ProfileImpl profile = new ProfileImpl(false);
        // Le main container associé est déjà démarré sur localhost
        profile.setParameter(ProfileImpl.MAIN_HOST, "localhost");
        profile.setParameter(ProfileImpl.CONTAINER_NAME, "Reservation");
        AgentContainer ct = rt.createAgentContainer(profile);
        ct.start();
    }

}