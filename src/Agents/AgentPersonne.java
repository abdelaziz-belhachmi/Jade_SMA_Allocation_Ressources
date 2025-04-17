package Agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class AgentPersonne extends Agent {

    private AID id;  // The agent's AID
    private int tentatives;  // Number of attempts to make a reservation
    private String nom;  // The agent's name
    private String comportement;  // Behavior description

    public AgentPersonne() {
        this.tentatives = 0;
        this.comportement = "Demande de reservation";
    }

    // Method to send the reservation request to the Mediateur agent
    public void envoyerDemande() {
        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        msg.addReceiver(new AID("Mediateur", AID.ISLOCALNAME));  // Address the message to the Mediateur agent
        msg.setContent("request-reservation");  // Message content (request for reservation)

        // Send the request
        send(msg);
        System.out.println(nom + " has sent a reservation request to the Mediateur.");
    }

    // Method to receive the response from the Mediateur agent
    public ACLMessage recevoirReponse() {
        return blockingReceive();  // Block until a response is received
    }

    // Method to notify the Mediateur agent if reservation is successful or not
    public void notifierMediateur(boolean reservationStatus) {
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.addReceiver(new AID("Mediateur", AID.ISLOCALNAME));  // Notify the Mediateur
        msg.setContent(reservationStatus ? "Reservation successful" : "Reservation failed");
        send(msg);
        System.out.println(nom + " has notified the Mediateur about the reservation status.");
    }

    @Override
    protected void setup() {
        this.id = this.getAID();
        this.nom = this.getAID().getLocalName();
        System.out.println(this);

        // Add a behavior to send the reservation request and handle responses
        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                // Simulate trying to send a reservation request after some number of attempts
                if (tentatives < 3) {  // Limit the number of attempts
                    envoyerDemande();  // Send the request

                    // Wait for the response
                    ACLMessage response = recevoirReponse();

                    // After receiving the response, notify the Mediateur (if needed)
                    boolean reservationStatus = (response != null && response.getPerformative() == ACLMessage.AGREE);
                    notifierMediateur(reservationStatus);

                    tentatives++;  // Increment the attempt counter
                } else {
                    System.out.println(nom + " has made 3 reservation attempts. No further action will be taken.");
                    doDelete();  // Delete the agent after 3 attempts
                }
            }
        });
    }

    @Override
    protected void takeDown() {
        super.takeDown();
        System.out.println("sending stats to agent stat before destruction...");
        System.out.println("Done");
    }

    @Override
    public String toString() {
        return "Hi, I am " + this.nom + " and my Comportement is: " + this.comportement;
    }
}
