package Agents;

import jade.core.AID;
import jade.core.Agent;

public class AgentRestaurant extends Agent {

    private AID id;
    private final int capaciteMax;
    private int placesLibres;
    private final String nom;

    public AgentRestaurant(int capaciteMax, String nom) {
        this.id = this.getAID();

        this.capaciteMax = capaciteMax;
        this.nom = nom;
    }

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
