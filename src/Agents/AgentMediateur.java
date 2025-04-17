package Agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.MessageTemplate;

import java.util.*;

public class AgentMediateur extends Agent {

    private List<AID> listeRestaurants = null;
    private ConcurrentLinkedQueue<AID> fileDemandes; // Concurrent queue for managing requests

    public AgentMediateur() {
        this.fileDemandes = new ConcurrentLinkedQueue<>();
    }

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
        boolean resSuccess = false;

        if (this.listeRestaurants == null) {
            this.listeRestaurants = getRestaurantAgents(); // fetch latest list
        }

        for (AID restaurant : listeRestaurants) {
            if (requestRestaurantReservation(personneAgent, restaurant)) {
                resSuccess = true;
                break;
            }
        }

        return resSuccess;
    }

    private boolean requestRestaurantReservation(AID personneAgent, AID restaurant) {
        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        msg.addReceiver(restaurant);
        msg.setContent("reserve");
        send(msg);

//        ACLMessage response = blockingReceive();
        MessageTemplate mt = MessageTemplate.MatchSender(restaurant);
        ACLMessage response = blockingReceive(mt, 2000);  // add a timeout too


        if (response != null && response.getPerformative() == ACLMessage.AGREE) {
            // replace this code with sending to statistic agent to log stats if needed
//            ACLMessage reply = new ACLMessage(ACLMessage.AGREE);
//            reply.addReceiver(personneAgent);
//            reply.setContent("Reservation accepted at " + restaurant.getLocalName());
//            send(reply);
            return true;
        } else {
            // replace this code with sending to statistic agent to log stats if needed

//            ACLMessage reply = new ACLMessage(ACLMessage.REFUSE);
//            reply.addReceiver(personneAgent);
//            reply.setContent("Reservation rejected by " + restaurant.getLocalName());
//            send(reply);
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
                    fileDemandes.add(sender);  // Add the Personne agent to the request queue

                    // Process each request concurrently using a worker thread
                    addBehaviour(new OneShotBehaviour() {
                        @Override
                        public void action() {
                            AID personneAgent = fileDemandes.poll();  // Fetch next request from the queue
                            if (personneAgent != null) {
                                boolean reservationSuccess = handleReservation(personneAgent);
                                System.out.println("Mediateur reservation status: " + reservationSuccess);

                                ACLMessage reply = msg.createReply();
                                if (reservationSuccess) {
                                    reply.setPerformative(ACLMessage.AGREE);
                                    reply.setContent("Reservation confirmed");
                                    send(reply);
                                } else {
                                    reply.setPerformative(ACLMessage.REFUSE);
                                    reply.setContent("Reservation failed");
                                    send(reply);
                                }
                            }
                        }
                    });
                } else {
                    block();
                }
            }
        });
    }
}
