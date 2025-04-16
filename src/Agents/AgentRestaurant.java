package Agents;

import jade.core.AID;
import jade.core.Agent;

public class AgentRestaurant extends Agent {

    private AID id;
    private final int capaciteMax;
    private int placesLibres;
    private String nom;

    @Override
    protected void setup() {
        this.id = this.getAID();
        System.out.println("Hello! My name is " + getLocalName() + "and my Max Capacity is " + capaciteMax);
    }

    public AgentRestaurant() {
        this.capaciteMax = 10;
    }


}
