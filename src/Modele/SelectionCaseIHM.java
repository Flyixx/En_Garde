package Modele;

public class SelectionCaseIHM {
    int id;
    int valeur;
    int x,y;
    int largeur, hauteur;
    int etat;

    public SelectionCaseIHM(int i, int val, int cx, int cy, int l, int h, int e){
        id = i;
        valeur = val;
        x = cx;
        y = cy;
        largeur = l;
        hauteur = h;
        etat = e;
    }

    public SelectionCaseIHM(int i, int val, int cx, int cy, int l, int h){
        id = i;
        valeur = val;
        x = cx;
        y = cy;
        largeur = l;
        hauteur = h;
    }

    public void update(int i, int val, int cx, int cy, int l, int h){
        id = i;
        valeur = val;
        x = cx;
        y = cy;
        largeur = l;
        hauteur = h;

    }

    public void updateEtat(int e){
        etat = e;
    }

    public int getId() {
        return id;
    }

    public int getValeur() {
        return valeur;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getLargeur() {
        return largeur;
    }

    public int getHauteur() {
        return hauteur;
    }

    public int getEtat() {
        return etat;
    }
}
