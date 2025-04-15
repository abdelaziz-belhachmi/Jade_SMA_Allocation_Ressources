package Agents;

import jade.core.Agent;

import java.util.HashMap;

public class AgentStatistique extends Agent {

    private int totalMessages;
    private int totalFailed;
    HashMap<String , Integer> statsAgents;

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
    }
}
