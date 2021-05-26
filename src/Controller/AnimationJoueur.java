package Controller;

import Vue.InterfaceGraphique;

public class AnimationJoueur extends Animation{
    InterfaceGraphique vue;

    AnimationJoueur(InterfaceGraphique i){
        super(10);
        vue = i;
    }

    @Override
    void miseAJour() {
        vue.animJoueur();
    }
}
