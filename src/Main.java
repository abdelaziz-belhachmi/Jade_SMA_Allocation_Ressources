import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.util.ExtendedProperties;
import jade.util.leap.Properties;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;


public class Main {
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


    }
}
