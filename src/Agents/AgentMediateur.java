package Agents;

import jade.core.Agent;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class AgentMediateur extends Agent {

    List<AgentRestaurant> listeRestaurants;
    Queue<Object> fileDemandes;

    public AgentMediateur() {
        this.listeRestaurants = new LinkedList<AgentRestaurant>();
        this.fileDemandes = new LinkedList<Object>();
    }

    public AgentMediateur(List<AgentRestaurant> listeRestaurants) {
        this.listeRestaurants = listeRestaurants;
        this.fileDemandes = new LinkedList<Object>();
    }



    public void recevoirNotification(){}
    public void recevoirDemande(Object demande){
        this.fileDemandes.add(demande);
    }
    public void analyserDemande(){}
    public void redigerVersRestaurant(){}


    @Override
    protected void setup() {
        System.out.println("Hello! My name is " + getLocalName());
    }
}
