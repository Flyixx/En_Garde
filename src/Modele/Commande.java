package Modele;

public abstract class Commande {
    abstract CoupParTour execute(CoupParTour cp);
    abstract void desexecute(CoupParTour cp);
}