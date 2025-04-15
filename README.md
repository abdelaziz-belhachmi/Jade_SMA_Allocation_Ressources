# jade_sma_allocation_de_ressources

### Contexte du projet :
Un groupe de N personnes (agents) souhaite aller dîner dans M restaurants, chacun ayant une capacité
d’accueil Ci.
##### Les contraintes sont :
    • Chaque Ci est inférieur à N, mais la somme des Ci est supérieure à 2*N.
    • Chaque restaurant est soumis à une probabilité d’être saturé.
    • Tous les agents connaissent les capacités des restaurants, mais pas leur état en temps réel.

### Objectif :
    • Simuler plusieurs comportements d’agents pour voir comment ils trouvent un restaurant.
    • Mesurer les performances (nombre de messages échangés, taux de réussite, etc.).
    • Comparer différentes stratégies (aléatoire, intelligente, collaborative).