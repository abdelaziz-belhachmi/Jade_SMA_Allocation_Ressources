package Agents;

import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;

import java.util.HashMap;

public class AgentStatistique extends Agent {

    private int totalMessages;
    private int totalFailed;
    HashMap<String , Integer> statsAgents;
    private String nom ;

    public AgentStatistique() {
        this.totalMessages = 0;
        this.totalFailed = 0;
        this.statsAgents = new HashMap<>();
    }

    public int getTotalMessages() {
        return totalMessages;
    }

    public void addToTotalMessages(int value) {
        this.totalMessages += value;
    }

    public int getTotalFailed() {
        return totalFailed;
    }

    public void addToTotalFailed(int value) {
        this.totalFailed += value;
    }

    public void addAgent(String AgentName , int tentatives) {
        statsAgents.put(AgentName, tentatives);
    }



    @Override
    protected void setup() {
        System.out.println("Hello! My name is " + getLocalName());

        this.nom = getLocalName(); // Set restaurant name

        try {
            // Clean any previous DF entry (in case of restart)
            DFService.deregister(this);
        } catch (Exception ignore) {
            // Can safely ignore: no registration yet
        }

        // Register this restaurant in the Directory Facilitator (DF)
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType("StatisticAgent"); // This must match the search type in AgentMediateur
        sd.setName(getLocalName());
        dfd.addServices(sd);
        try {
            DFService.register(this, dfd);
            System.out.println(getLocalName() + " registered in DF as StatisticAgent.");
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }


    }
}
