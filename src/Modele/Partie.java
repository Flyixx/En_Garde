package Modele;

import Controller.JoueurHumain;
import Patterns.Observable;

public class Partie {
    boolean J1Gagnant, J2Gagnant;
    Jeu jeu;
    Manche courant;
    JoueurHumain joueur1, joueur2;
    public int type;
    int gagnant;
    int premierTourPrecedent; // Changer à qui est le premier tour à chaque fois

    public Partie(Jeu j){
        jeu = j;

        premierTourPrecedent = 1;
        initialisePartie();
    }

    public Partie(Jeu j, int pdv1, int pdv2, int positionJ1, int positionJ2, String pioche, String MainJ1, String MainJ2, int tourCourant, int type, String HistoriqueAnnule, String HistoriqueCoupFait){
        jeu = j;
        initialisePartieSauve(pdv1, pdv2, positionJ1, positionJ2, pioche, MainJ1, MainJ2, tourCourant, type, HistoriqueAnnule, HistoriqueCoupFait);
    }

    public void initialisePartie(){

        joueur1 = new JoueurHumain(jeu);
        joueur2 = new JoueurHumain(jeu);
        joueur1.vie = 5;
        joueur2.vie = 5;
        courant = new Manche(this, premierTourPrecedent);
        J1Gagnant = false;
        J2Gagnant = false;
        jeu.control.inter().niv().msg = 0;
        jeu.control.inter().niv().msg2 = 0;
        jeu.control.inter().niv().action1 = 0;
        jeu.control.inter().niv().action2 = 0;
    }

    public void initialisePartieSauve(int pdv1, int pdv2, int positionJ1, int positionJ2, String pioche, String MainJ1, String MainJ2, int tourCourant, int typePartie, String HistoriqueAnnule, String HistoriqueCoupFait){
        joueur1 = new JoueurHumain(jeu);
        joueur2 = new JoueurHumain(jeu);
        joueur1.vie = pdv1;
        joueur2.vie = pdv2;
        courant = new Manche(this, positionJ1, positionJ2, pioche, MainJ1, MainJ2, tourCourant, HistoriqueAnnule, HistoriqueCoupFait);
        J1Gagnant = false;
        J2Gagnant = false;
        type=typePartie;
        jeu.control.inter().niv().msg = 0;
        jeu.control.inter().niv().msg2 = 0;
        jeu.control.inter().niv().action1 = 0;
        jeu.control.inter().niv().action2 = 0;
    }

    public void initialiseManche(){
        courant = null;


        if(premierTourPrecedent == 1)
        {
            premierTourPrecedent = 2;
        }
        else
        {
            premierTourPrecedent = 1;
        }
        if(this.aGagner())
        {
            System.out.println("Le vainqueur est le joueur " + gagnant);

        }
        else
        {
            courant = null;
            courant = new Manche(this, premierTourPrecedent);
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

    public void jouerCoup(Coup cp, boolean refaire) {
        courant.jouerCoup(cp, refaire);
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
