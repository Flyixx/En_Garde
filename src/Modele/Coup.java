package Modele;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Coup {
    Manche manche;
    int[] mapAvant;
    Action action;
    int target;
    ArrayList<Integer> mainJ1= new ArrayList<>();
    ArrayList<Integer> mainJ2= new ArrayList<>();
    ArrayList<Integer> pioche = new ArrayList<>();

    @Override
    public String toString() {
        return "Coup{" +
                "manche=" + manche +
                ", mapAvant=" + Arrays.toString(mapAvant) +
                ", action=" + action +
                ", target=" + target+'}';
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

    public Coup execute(Coup cp) {
        Coup cp1 = Revenir(cp);
        return cp1;
    }

    public void desexecute(Coup cp, int tour, boolean devaitParer, int[] etatCasesIHM) {
        Inverse(cp, tour, devaitParer, etatCasesIHM);
    }

    public Coup Revenir(Coup cp){
        Coup cp1 = manche.RevenirCoup(cp);
        return cp1;
    }

    public void Inverse(Coup cp, int tour, boolean devaitParer, int[] etatCasesIHM){
        manche.InverseCoup(cp, tour,devaitParer, etatCasesIHM);
    }

}
