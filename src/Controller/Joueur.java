package Controller;

public abstract class Joueur {
    abstract void deplace(int val);
    abstract int targetAvant(int val);
    abstract int targetArriere(int val);
}
