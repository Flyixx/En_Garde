package Modele;

import Controller.JoueurHumain;
import Patterns.Observable;

public class Partie extends Historique<CoupParTour>{
    boolean J1Gagnant, J2Gagnant;
    Jeu jeu;
    Manche courant;
    JoueurHumain joueur1, joueur2;
    public int type;
    int gagnant;

    public Partie(Jeu j){
        jeu = j;
        initialisePartie();
    }

    public Partie(Jeu j, int pdv1, int pdv2, int positionJ1, int positionJ2, String pioche, String MainJ1, String MainJ2, int tourCourant){
        jeu = j;
        initialisePartieSauve(pdv1, pdv2, positionJ1, positionJ2, pioche, MainJ1, MainJ2, tourCourant);
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

    public void initialisePartieSauve(int pdv1, int pdv2, int positionJ1, int positionJ2, String pioche, String MainJ1, String MainJ2, int tourCourant){
        joueur1 = new JoueurHumain(jeu);
        joueur2 = new JoueurHumain(jeu);
        joueur1.vie = pdv1;
        joueur2.vie = pdv2;
        courant = new Manche(this, positionJ1, positionJ2, pioche, MainJ1, MainJ2, tourCourant);
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

    public JoueurHumain Joueur(int numJoueur) {
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
