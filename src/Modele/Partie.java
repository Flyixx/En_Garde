package Modele;

import Controller.JoueurHumain;
import Patterns.Observable;

public class Partie {
    boolean J1Gagnant, J2Gagnant;
    Jeu jeu;
    Manche courant;
    JoueurHumain joueur1, joueur2;
    int gagnant;

    public Partie(Jeu j){
        jeu = j;
        initialisePartie();
    }

    public void initialisePartie(){

        joueur1 = new JoueurHumain(jeu);
        joueur2 = new JoueurHumain(jeu);
        joueur1.vie = 5;
        joueur2.vie = 5;
        courant = new Manche(this);
        J1Gagnant = false;
        J2Gagnant = false;
    }

    public void initialiseManche(){
        courant = null;

        if(this.aGagner())
        {
            System.out.println("Le vainqueur est le joueur " + gagnant);

        }
        else
        {
            courant = new Manche(this);
        }

    }

    public boolean aGagner(){
        if(joueur1.vie == 0){
            J2Gagnant = true;
            gagnant = 2;
            return true;
        }else if(joueur2.vie == 0){
            J1Gagnant = true;
            gagnant = 1;
            return true;
        }
        return false;
    }

    public void jouerCoup(Coup cp) {
        courant.jouerCoup(cp);
    }

    public JoueurHumain Joueur(int numJoueur)
    {
        if(numJoueur == 1)
        {
            return joueur1;
        }
        else
        {
            return joueur2;
        }
    }

    public Manche manche()
    {
        return  courant;
    }
}
