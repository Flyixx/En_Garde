import Controller.ControllerMediateur;
import Modele.*;
import Vue.InterfaceGraphique;

import java.util.ArrayList;

public class EnGarde {

    public static void main(String [] args) {
        ArrayList<Integer> tableauTest = new ArrayList<>();
        tableauTest.add(1);
        tableauTest.add(1);
        tableauTest.add(1);
        System.out.println("tableau test : " + tableauTest);
        tableauTest.remove(0);



        try {
            Jeu j = new Jeu();
            ControllerMediateur c = new ControllerMediateur(j);
            InterfaceGraphique.demarrer(j, c);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
