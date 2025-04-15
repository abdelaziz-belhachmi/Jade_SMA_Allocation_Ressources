package Agents;

import jade.core.Agent;

public class AgentRestaurant extends Agent {

    private int id;
    private int capaciteMax;
    private int placesLibres;
    private String nom;

    public AgentRestaurant() {}

    public AgentRestaurant(String nom, int capaciteMax, int placesLibres) {
        this.nom = nom;
        this.capaciteMax = capaciteMax;
        this.placesLibres = placesLibres;
    }

    @Override
    protected void setup() {
        System.out.println("Hello! My name is " + getLocalName());
    }
}
