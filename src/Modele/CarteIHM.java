package Modele;

public class CarteIHM {
    int id;
    int valeur;
    int x, y;
    int largeur, hauteur;
    int etat;



    public CarteIHM(int num, int value, int Cx, int Cy, int l, int h){
        id = num;
        valeur = value;
        x = Cx;
        y = Cy;
        largeur = l;
        hauteur = h;
        etat = 0;
    }


    public void update(int num, int value, int Cx, int Cy, int l, int h){
        id = num;
        valeur = value;
        x = Cx;
        y = Cy;
        largeur = l;
        hauteur = h;
    }

    public void reset(){
        id = -1;
        valeur = 0;
        x = 0;
        y = 0;
        largeur = 0;
        hauteur = 0;
    }

    public void setEtat(int etat) {
        this.etat = etat;
    }

    public int getEtat() {
        return etat;
    }

    public int getCoordX(){
        return x;
    }

    public int getCoordY(){
        return y;
    }

    public int getId() {
        return id;
    }

    public int getValeur() {
        return valeur;
    }

    public int getLargeur() {
        return largeur;
    }

    public int getHauteur() {
        return hauteur;
    }

    @Override
    public String toString() {
        return "CarteIHM{" +
                "id=" + id +
                ", valeur=" + valeur +
                ", x=" + x +
                ", y=" + y +
                ", largeur=" + largeur +
                ", hauteur=" + hauteur +
                '}';
    }
}
