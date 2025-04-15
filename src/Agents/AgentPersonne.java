package Agents;

import jade.core.AID;
import jade.core.Agent;

public class AgentPersonne extends Agent {

    private AID id;
    private int tentatives;
    private final String nom;
    private final String comportement;

    public AgentPersonne(String nom){
        this.id = this.getAID();
        this.nom = nom;
        this.tentatives = 0 ;
        this.comportement = "demande de reservation";
    }

    public void envoyerDemande(){}
    public void recevoirReponse(){}
    public void notifierMediateur(){}


    @Override
    protected void setup() {
        System.out.println("Hello! My name is " + getLocalName());
    }
}
