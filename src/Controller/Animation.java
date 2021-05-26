package Controller;

public abstract class Animation {
    int vitesse;
    int decompte;

    Animation(int v){
        vitesse = v;
    }

    abstract void miseAJour();

    void tictac(){
        decompte++;
        if(decompte >= vitesse){
            miseAJour();
            decompte = 0;
        }
    }

    boolean estTerminee(){
        return false;
    }
}
