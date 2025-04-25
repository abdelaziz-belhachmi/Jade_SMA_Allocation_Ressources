package Agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;

public class AgentPersonne extends Agent {

    private AID id;  // The agent's AID
    private int tentatives;  // Number of attempts to make a reservation
    private String nom;  // The agent's name
    private String comportement;  // Behavior description
    private AID agentStatistic;

    public AgentPersonne() throws InterruptedException {
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
//        try {
//            this.agentStatistic = FindAgentStatistique();
//        } catch (FIPAException e) {
//            e.printStackTrace();
//            System.out.println("Agent Statistique not found");
//            throw new RuntimeException(e);
//        }
        do {
            try {
                this.agentStatistic = FindAgentStatistique();
            } catch (FIPAException e) {
                e.printStackTrace();
                System.out.println("Agent Statistique not found, retrying...");
            }

            if (this.agentStatistic == null) {
                try {
                    Thread.sleep(1000); // wait 1 second before retrying
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
            }
        } while (this.agentStatistic == null);

        // Add a behavior to send the reservation request and handle responses
        addBehaviour(new OneShotBehaviour() {
            @Override
            public void action() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                ACLMessage response = null;
                // Simulate trying to send a reservation request after some number of attempts
                while (response == null || response.getPerformative() == ACLMessage.REFUSE) {  // Limit the number of attempts
                    envoyerDemande();  // Send the request

                    // Wait for the response
                    response = recevoirReponse();
//                    System.out.println("hello -- responce got is "+response.getPerformative());
                    // After receiving the response, notify the Mediateur (if needed)
                    boolean reservationStatus = (response != null && response.getPerformative() == ACLMessage.AGREE);

                    tentatives++;  // Increment the attempt counter

                    if (reservationStatus){
                        System.out.println("found reservation after "+tentatives+" tentatives AT RESTAURENT :"+response.getContent());
                        break;
                    }
                    else {
                        System.out.println(nom + " failed to make reservation. with restaurant :"+response.getContent());
                    }

                    try {
                        Thread.sleep((int) ((Math.random() * (1000 - 500 + 1)) + 500));  // Sleep for 500 ms before retrying
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //

                }

                /* send statistics */
                ACLMessage statMsg = new ACLMessage(ACLMessage.INFORM);
                statMsg.addReceiver(agentStatistic);
                statMsg.setContent(nom + ":" +response.getContent()+ ":" + tentatives); // format: "Personne-1:3"
                send(statMsg);
                System.out.println("Stats sent to AgentStatistique: " + nom + " with " + tentatives + " tentatives.");

            }
        });
    }

    private AID FindAgentStatistique() throws FIPAException {
        // Now search for ONE StatisticAgent
        DFAgentDescription statTemplate = new DFAgentDescription();
        ServiceDescription statSD = new ServiceDescription();
        statSD.setType("StatisticAgent");
        statTemplate.addServices(statSD);

        DFAgentDescription[] statResult = DFService.search(this, statTemplate);
        if (statResult.length > 0) {
            AID statisticAgent = statResult[0].getName();
            System.out.println("Found a statistic agent: " + statisticAgent.getLocalName());
            return statisticAgent;
        }else{
            return null;
        }
    }

    @Override
    protected void takeDown() {
        super.takeDown();
    }

    @Override
    public String toString() {
        return "Hi, I am " + this.nom + " and my Comportement is: " + this.comportement;
    }
}
