package Agents;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;

import java.util.HashMap;

public class AgentStatistique extends Agent {

    private int totalMessages;
    private int totalFailed;
    private HashMap<String, Integer> statsAgents;

    @Override
    protected void setup() {
        System.out.println("Hello! My name is " + getLocalName());

        this.totalMessages = 0;
        this.totalFailed = 0;
        this.statsAgents = new HashMap<>();

        // Register in the DF
        try {
            DFService.deregister(this);
        } catch (Exception ignored) {}

        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType("StatisticAgent");
        sd.setName(getLocalName());
        dfd.addServices(sd);

        try {
            DFService.register(this, dfd);
            System.out.println(getLocalName() + " registered in DF as StatisticAgent.");
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }

        // Behavior to listen for incoming stats
        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                ACLMessage msg = receive();
                if (msg != null) {
                    if (msg.getPerformative() == ACLMessage.INFORM) {
                        String[] parts = msg.getContent().split(":");
                        if (parts.length == 2) {
                            String agentName = parts[0];
                            try {
                                int attempts = Integer.parseInt(parts[1]);
                                statsAgents.put(agentName, attempts);
                                totalMessages++;
                                System.out.println("Received stats from " + agentName + " | Attempts: " + attempts);
                            } catch (NumberFormatException e) {
                                System.out.println("Invalid attempt number in message: " + msg.getContent());
                                totalFailed++;
                            }
                        } else {
                            System.out.println("Invalid stats message format: " + msg.getContent());
                            totalFailed++;
                        }
                    }
                    // Optional: Respond to get-stats request from a JavaFX interface agent
                    else if (msg.getPerformative() == ACLMessage.REQUEST && msg.getContent().equals("get-stats")) {
                        ACLMessage reply = msg.createReply();
                        reply.setPerformative(ACLMessage.INFORM);
                        reply.setContent(statsAgents.toString());
                        send(reply);
                        System.out.println("Stats sent to " + msg.getSender().getLocalName());
                    }
                } else {
                    block();
                }
            }
        });
    }

    @Override
    protected void takeDown() {
        System.out.println("AgentStatistique shutting down...");
        System.out.println("Final statistics: " + statsAgents);
        System.out.println("Total messages received: " + totalMessages);
        System.out.println("Total failed messages: " + totalFailed);
    }
}
