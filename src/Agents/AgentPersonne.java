package Agents;

import jade.core.AID;
import jade.core.Agent;

public class AgentPersonne extends Agent {

    private AID id;
    private int tentatives;
    private String nom;
    private String comportement;

    public AgentPersonne(){
        this.tentatives = 0 ;
        this.comportement = "Demande de reservation";
    }

    public void envoyerDemande(){}
    public void recevoirReponse(){}
    public void notifierMediateur(){}


    @Override
    protected void setup() {
        this.id = this.getAID();
        this.nom = this.getAID().getLocalName();
        System.out.println(this);
    }

    @Override
    protected void takeDown() {
        super.takeDown();
        System.out.println("sending stats to agent stat before destruction...");
        /**/
        System.out.println("Done");
    }

    @Override
    public String toString() {
        return "Hi I am  "+this.nom + " and my Comportement is :" + this.comportement;
    }
}
