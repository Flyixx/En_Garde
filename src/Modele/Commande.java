package Modele;

public abstract class Commande {
    abstract void execute(CoupParTour cp);
    abstract void desexecute(CoupParTour cp);
}