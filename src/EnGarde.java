import Controller.ControllerMediateur;
import Modele.*;
import Vue.InterfaceGraphique;

import java.util.ArrayList;

public class EnGarde {

    public static void main(String [] args) {

        try {
            Jeu j = new Jeu();
            ControllerMediateur c = new ControllerMediateur(j);
            InterfaceGraphique.demarrer(j, c);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
