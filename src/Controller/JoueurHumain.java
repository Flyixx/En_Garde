package Controller;

import Modele.*;

import java.util.ArrayList;
import java.util.List;

public class JoueurHumain extends Joueur {
    Jeu jeu;
    public ArrayList<Integer> main= new ArrayList<>();
    public int position;
    public int direction;
    public ArrayList<CarteIHM> carteI = new ArrayList<>();
    public int vie;

    public JoueurHumain(Jeu j){
        jeu = j;

    }


    public void supprMain(int index){

        main.set(index,0);
    }

    public void initCarteI(int id, int valeur, int Cx, int Cy, int l, int h){
        CarteIHM c1 = new CarteIHM(id, valeur, Cx, Cy, l, h);
        carteI.add(c1);
    }

    public void updateCarteI(int id, int valeur, int Cx, int Cy, int l, int h){
        CarteIHM c1 = carteI.get(id);
        c1.update(id, valeur, Cx, Cy, l, h);

    }

    public ArrayList<CarteIHM> getCarteI() {
        return carteI;
    }

    public List<Integer> getMain(){
        return main;
    }

    public int getPosition(){
        return this.position;
    }

    public int getDirection(){
        return this.direction;
    }

    @Override
    public void deplace(int val) {
        // direction is 1 or -1. We will know which way the character is going to take.
        // For the left one, going forward = going right
        // For the right one, going forward = going left
        // This is why we multiply val by the direction

        /* Version calcul
         * this.position = this.position+(this.direction*val);
         */

        /* Version remplacement d'indice */
        this.position = val;
    }

    @Override
    // Renvoie l'indice où le joueur serait s'il se déplaçait vers l'avant
    public int targetAvant(int val) {
        return this.position+(this.direction*val);
    }

    @Override
        // Renvoie l'indice où le joueur serait s'il se déplaçait vers l'arriere
    public int targetArriere(int val) {
        return this.position+(this.direction*(-val));
    }
}
