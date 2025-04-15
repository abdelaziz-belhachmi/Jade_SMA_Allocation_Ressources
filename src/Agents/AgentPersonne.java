package Agents;

import jade.core.Agent;

public class AgentPersonne extends Agent {

    private int id;
    private int tentatives;
    private String nom;
    private String comportement;

    public AgentPersonne(){
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
