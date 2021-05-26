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
        control.clickCarte(e.getX(), e.getY());
        control.clickDeplacement(e.getX(), e.getY());
        control.clickChangeTour(e.getX(), e.getY());
        if(niv.NewPartie){
            control.clickChange(e.getX(), e.getY());
        }
    }

}
