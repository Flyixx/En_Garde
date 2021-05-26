package Modele;

import java.util.Arrays;

public class Action {
    int id;
    int[] valeurs;


    public Action(int type, int[] valeurs){
        this.id = type;
        this.valeurs = valeurs;
    }

    @Override
    public String toString() {
        return "Action{" +
                "id=" + id +
                ", valeurs=" + Arrays.toString(valeurs) +
                '}';
    }
}
