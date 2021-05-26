package Modele;

public abstract class Commande {
    abstract void execute(Coup cp);
    abstract void desexecute(Coup cp);
}