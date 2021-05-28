package Vue;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AdaptateurSouris extends MouseAdapter {
    NiveauGraphique niv;
    CollecteurEvenements control;

    AdaptateurSouris(NiveauGraphique n, CollecteurEvenements c) {
        niv = n;
        control = c;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(niv.NewPartie){
            control.clickChange(e.getX(), e.getY());
        }
        if(niv.Partie){
            control.clickCarte(e.getX(), e.getY());
            control.clickDeplacement(e.getX(), e.getY());
            control.clickChangeTour(e.getX(), e.getY());
            control.clickSauvegarder(e.getX(), e.getY());
            control.clickAnnuler(e.getX(), e.getY());
            control.clickRefaire(e.getX(), e.getY());
        }
        control.clickQuitterJeu(e.getX(), e.getY());
        control.clickMute(e.getX(), e.getY());
    }

}
