package Agents;

import Agents.DAO.trio;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AgentStatistique extends Agent implements IStatistique {

    private int totalMessages;
    private int totalFailed;
    // Agent persone name , restaut of successfull reservation , number of tentatives
    private List<trio<String,String,Integer>> statsAgents;

    private static AgentStatistique instance;

    public static AgentStatistique getInstance() {
        return instance;
    }

    @Override
    protected void setup() {

//        setEnabledO2AInterface(true, 0);
        registerO2AInterface(IStatistique.class, this);


        System.out.println("Hello! My name is " + getLocalName());

        this.totalMessages = 0;
        this.totalFailed = 0;
        this.statsAgents = new ArrayList<>();
        instance = this;

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
                        if (parts.length == 3) {
                            String agentName = parts[0];
                            String restoName = parts[1];
                            try {
                                int attempts = Integer.parseInt(parts[1]);
                                statsAgents.add(new trio<>(agentName, restoName, attempts));
//                                totalMessages++;
                                System.out.println("Received stats from " + agentName + " | Attempts: " + attempts);
                            } catch (NumberFormatException e) {
                                System.out.println("NumberFormatException : " + msg.getContent());
//                                totalFailed++;
                            }
                        } else {
                            System.out.println("Invalid stats message format: " + msg.getContent());
//                            totalFailed++;
                        }
                    }

                } else {
                    block();
                }
            }
        });
    }

    @Override
    public List<trio<String,String,Integer>> getStats(){
        return this.statsAgents;
    }

    @Override
    protected void takeDown() {
        System.out.println("AgentStatistique shutting down...");
        System.out.println("Final statistics: " + statsAgents);
        System.out.println("Total messages received: " + totalMessages);
        System.out.println("Total failed messages: " + totalFailed);
    }
}
