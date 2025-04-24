package Containers;

import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;

import java.util.ArrayList;
import java.util.List;

public class RestaurantContainer {

    private static AgentContainer container;
    private static List<AgentController> ag= new ArrayList<>();


    private static int numberOfRestaurants = 4 ;

    public static void main(String[] args) {
        try {

            // Créer une instance de la MV
            Runtime rt = Runtime.instance();
            // Pas de propriétés, ce n'et pas un main container, mais un profile !
            ProfileImpl profile = new ProfileImpl(false);
            // Le main container associé est déjà démarré sur localhost
            profile.setParameter(ProfileImpl.MAIN_HOST, "localhost");
            profile.setParameter(ProfileImpl.CONTAINER_NAME, "Restaurant-Container");

            container = rt.createAgentContainer(profile);

            for (int i = 1; i <= numberOfRestaurants; i++) {
                // It's important that each restaurant agent's name starts with "Restaurant" so that it can be dynamically identified and communicated with.
                ag.add(container.createNewAgent("Restaurant-"+i,"Agents.AgentRestaurant",new Object[]{i*2}));
                ag.get(i-1).start();
            }

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
                System.out.println("ALL Restaurants shut down successfully.");
            } catch (ControllerException e) {
                e.printStackTrace();
            }
        }
    }





}
