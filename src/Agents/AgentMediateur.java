package Agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

import java.util.*;

public class AgentMediateur extends Agent {

    private List<AID> listeRestaurants;  // List of Restaurant AIDs
    private Queue<Object> fileDemandes;  // Queue to handle incoming requests

    public AgentMediateur() {
        this.fileDemandes = new LinkedList<>();
        this.listeRestaurants = new ArrayList<>(); // just initialize empty here
    }

    // Method to dynamically fetch all restaurant agents from the container by type
    private List<AID> getRestaurantAgents() {
        List<AID> restaurantAIDs = new ArrayList<>();

        try {
            DFAgentDescription template = new DFAgentDescription();
            ServiceDescription sd = new ServiceDescription();
            sd.setType("Restaurant");
            template.addServices(sd);
            DFAgentDescription[] result = DFService.search(this, template);

            for (DFAgentDescription agentDescription : result) {
                AID restaurantAID = agentDescription.getName();
                System.out.println(" found a restaurant, its name: **{ " + restaurantAID + " }**");
                restaurantAIDs.add(restaurantAID);
            }

        } catch (FIPAException e) {
            e.printStackTrace();
        }

        return restaurantAIDs;
    }

    private boolean handleReservation(AID personneAgent) {
        this.listeRestaurants = getRestaurantAgents(); // fetch latest list

        int numITR = 0;
        System.out.println(" i will start trying reserving in restos ");

        for (AID restaurant : listeRestaurants) {
            numITR++;
            System.out.println("**" + numITR + "** trying reserving in resto : " + restaurant.getLocalName());
            if (requestRestaurantReservation(personneAgent, restaurant)) {
                System.out.println("successful reservation");
                return true;
            }
        }

        return false;
    }

    private boolean requestRestaurantReservation(AID personneAgent, AID restaurant) {
        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        msg.addReceiver(restaurant);
        msg.setContent("reserve");
        send(msg);

        ACLMessage response = blockingReceive();
        if (response != null && response.getPerformative() == ACLMessage.AGREE) {
            ACLMessage reply = new ACLMessage(ACLMessage.AGREE);
            reply.addReceiver(personneAgent);
            reply.setContent("Reservation accepted at " + restaurant.getLocalName());
            send(reply);
            return true;
        } else {
            ACLMessage reply = new ACLMessage(ACLMessage.REFUSE);
            reply.addReceiver(personneAgent);
            reply.setContent("Reservation rejected by " + restaurant.getLocalName());
            send(reply);
            return false;
        }
    }

    @Override
    protected void setup() {
        System.out.println(getLocalName() + " is ready and waiting for requests...");

        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                ACLMessage msg = receive();
                if (msg != null && msg.getContent().equals("request-reservation")) {
                    AID sender = msg.getSender();

                    boolean reservationSuccess = handleReservation(sender);

                    System.out.println("mediateur reservation status : " + reservationSuccess);
                    ACLMessage reply = msg.createReply();
                    if (reservationSuccess) {
                        reply.setPerformative(ACLMessage.AGREE);
                        reply.setContent("Reservation confirmed");
                    } else {
                        reply.setPerformative(ACLMessage.REFUSE);
                        reply.setContent("Reservation failed");
                    }
                    send(reply);
                } else {
                    block();
                }
            }
        });
    }
}
