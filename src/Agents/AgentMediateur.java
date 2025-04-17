package Agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.ControllerException;
import jade.wrapper.ContainerController;

import java.util.*;

public class AgentMediateur extends Agent {

    List<AID> listeRestaurants;  // List of Restaurant AIDs
    Queue<Object> fileDemandes;  // Queue to handle incoming requests

    public AgentMediateur() {
        this.listeRestaurants = new LinkedList<AID>();
        this.fileDemandes = new LinkedList<Object>();
    }

    public AgentMediateur(List<AID> listeRestaurants) {
        this.listeRestaurants = listeRestaurants;
        this.fileDemandes = new LinkedList<Object>();
    }

    // Method to receive a reservation request
    public void recevoirDemande(Object demande) {
        this.fileDemandes.add(demande);
    }

    // Method to dynamically fetch all restaurant agents from the container (assuming they are predefined or created manually)
    // Method to dynamically fetch all restaurant agents from the container
    private List<AID> getRestaurantAgents() {
        List<AID> restaurantAIDs = new ArrayList<>();
        try {
            // Assuming the container is running on the same host
            jade.core.Runtime runtime = jade.core.Runtime.instance();
            jade.wrapper.AgentContainer restaurantContainer = runtime.getAgentContainer("Restaurant-Container");

            // Get all agents of type AgentRestaurant
            for (AID restaurant : restaurantContainer.getAgentNames()) {
                if (restaurant.getLocalName().startsWith("Restaurant")) {
                    restaurantAIDs.add(restaurant);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return restaurantAIDs;
    }


    // Try to reserve a seat at any restaurant from the dynamically fetched list
    private boolean handleReservation(AID personneAgent) {
        // Dynamically fetch the list of restaurant agents
        List<AID> restaurantList = getRestaurantAgents();

        // Loop through the restaurants and try to make a reservation
        for (AID restaurant : restaurantList) {
            if (requestRestaurantReservation(personneAgent, restaurant)) {
                return true;  // Reservation successful
            }
        }
        return false; // No restaurants could accept the reservation
    }

    // Request a reservation from a specific restaurant
    private boolean requestRestaurantReservation(AID personneAgent, AID restaurant) {
        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        msg.addReceiver(restaurant);
        msg.setContent("reserve");

        // Send the request to the restaurant
        send(msg);

        // Wait for the restaurant's response
        ACLMessage response = blockingReceive();
        if (response != null && response.getPerformative() == ACLMessage.AGREE) {
            // Notify the "personne" agent if the reservation was successful
            ACLMessage reply = new ACLMessage(ACLMessage.INFORM);
            reply.addReceiver(personneAgent);
            reply.setContent("Reservation accepted at " + restaurant.getLocalName());
            send(reply);
            return true;  // Reservation was successful
        } else {
            // Notify the "personne" agent if the reservation was rejected
            ACLMessage reply = new ACLMessage(ACLMessage.INFORM);
            reply.addReceiver(personneAgent);
            reply.setContent("Reservation rejected by " + restaurant.getLocalName());
            send(reply);
            return false;  // Reservation was rejected
        }
    }

    @Override
    protected void setup() {
        // Add a cyclic behaviour to handle multiple requests
        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                // Receive the reservation request from a "personne" agent
                ACLMessage msg = receive();
                if (msg != null && msg.getContent().equals("request-reservation")) {
                    AID sender = msg.getSender();  // The "personne" agent who made the request

                    // Process the reservation request
                    boolean reservationSuccess = handleReservation(sender);

                    // Respond to the "personne" agent
                    ACLMessage reply = msg.createReply();
                    if (reservationSuccess) {
                        reply.setPerformative(ACLMessage.AGREE);  // Reservation accepted
                        reply.setContent("Reservation confirmed");
                    } else {
                        reply.setPerformative(ACLMessage.REFUSE);  // Reservation rejected
                        reply.setContent("Reservation failed");
                    }
                    send(reply);  // Send the response back to the "personne" agent
                }
            }
        });
    }
}
