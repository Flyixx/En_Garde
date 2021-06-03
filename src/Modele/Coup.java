package Modele;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Coup {
    Manche manche;
    int[] mapAvant;
    Action action;
    int target;

    @Override
    public String toString() {
        return "Coup{" +
                "manche=" + manche +
                ", mapAvant=" + Arrays.toString(mapAvant) +
                ", action=" + action +
                '}';
    }

    public Coup(int []grilleJeu, Action action, int Target){
        mapAvant = grilleJeu;
        this.action = action;
        target = Target;
    }

    public Action GetAction(){
        return this.action;
    }

    void fixerManche(Manche m)
    {
        manche = m;
    }

    public void execute(Coup cp) {
        Revenir(cp);
    }

    public void desexecute(Coup cp) {
        Inverse(cp);
    }

    public void Revenir(Coup cp){
        manche.RevenirCoup(cp);
    }

    public void Inverse(Coup cp){
        manche.InverseCoup(cp);
    }

}
