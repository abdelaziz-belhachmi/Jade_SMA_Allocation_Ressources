import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;

public class Main {
    public static void main(String[] args) {
        Runtime rt = Runtime.instance();

        Profile p = new ProfileImpl();
        AgentContainer mainContainer = rt.createMainContainer(p);

        try {
            AgentController agent = mainContainer.createNewAgent("hello", "HelloAgent", null);
            agent.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
