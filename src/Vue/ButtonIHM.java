package Vue;

public class ButtonIHM {
    int id;
    String tag;
    int x, y;
    int largeur, hauteur;
    int etat;

    public ButtonIHM(int id, String tag, int x, int y, int largeur, int hauteur) {
        this.id = id;
        this.tag = tag;
        this.x = x;
        this.y = y;
        this.largeur = largeur;
        this.hauteur = hauteur;
        this.etat = 0;
    }

    public void updateButtonIHM(int id, String tag, int x, int y, int largeur, int hauteur) {
        this.id = id;
        this.tag = tag;
        this.x = x;
        this.y = y;
        this.largeur = largeur;
        this.hauteur = hauteur;
    }

    public int getId() {
        return id;
    }

    public String getTag() {
        return tag;
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
