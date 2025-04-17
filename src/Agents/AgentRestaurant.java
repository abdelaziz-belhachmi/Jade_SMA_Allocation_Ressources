package Agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;

public class AgentRestaurant extends Agent {

    private AID id;
    private int capaciteMax;
    private int placesLibres;
    private String nom;

    @Override
    protected void setup() {
        this.nom = getLocalName(); // Set restaurant name

        // Register this restaurant in the Directory Facilitator (DF)
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType("Restaurant"); // This must match the search type in AgentMediateur
        sd.setName(getLocalName());
        dfd.addServices(sd);
        try {
            DFService.register(this, dfd);
            System.out.println(getLocalName() + " registered in DF as Restaurant.");
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }


        capaciteMax = getRandomNumber(10, 15);  // Random capacity between 10-15
        placesLibres = getRandomNumber(2, Math.min(6, capaciteMax));  // Random available seats, ensuring it's <= capaciteMax
        System.out.println(getLocalName() + " initialized with " + placesLibres + " available seats and max capacity of: " + capaciteMax);

        // Cyclic behaviour to handle reservation requests
        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                ACLMessage msg = receive();
                if (msg != null && msg.getContent().equals("reserve")) {
                    ACLMessage reply = msg.createReply();

                    // Check if there are available seats
                    if (placesLibres > 0) {
                        placesLibres--;  // Decrease available seats after reservation
                        reply.setPerformative(ACLMessage.AGREE);  // Accept reservation
                        reply.setContent("Reservation successful");  // Optional content for further feedback
                    } else {
                        reply.setPerformative(ACLMessage.REFUSE);  // Reject reservation
                        reply.setContent("No available seats");  // Optional content for further feedback
                    }

                    // Send the response back
                    send(reply);
                    System.out.println(getLocalName() + " has " + placesLibres + " seats remaining.");
                }
            }
        });
    }

    // Utility method to get a random number between min and max (inclusive)
    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min + 1)) + min);
    }

    @Override
    protected void takeDown() {
        super.takeDown();
        System.out.println(getLocalName() + " is being destroyed.");
    }

    @Override
    public String toString() {
        return "Restaurant: " + this.nom + ", Available seats: " + this.placesLibres + ", Max capacity: " + this.capaciteMax;
    }
}
