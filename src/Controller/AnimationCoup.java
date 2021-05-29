package Controller;

import Modele.Coup;
import Vue.InterfaceGraphique;

public class AnimationCoup extends Animation{
    Coup cp;
    double progres, vitesseAnim;
    InterfaceGraphique vue;
    int depuis, vers, tourJoueur;

    AnimationCoup(Coup c, InterfaceGraphique v, int direction, int tour){
        super(1);
        cp = c;
        vitesseAnim = 0.15;
        vue = v;
        vers = (direction+1)/2;
        depuis = 1-vers;
        tourJoueur = tour;

        miseAJour();
    }

    @Override
    void miseAJour() {
        progres += vitesseAnim;
        if(progres > 1){
            progres = 1;
        }
        if(vue.niv().Partie){
            if(tourJoueur == 1){

            }
        }
    }

    boolean estTerminee(){
        return progres >= 1;
    }
}
