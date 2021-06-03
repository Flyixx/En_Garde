package Modele;

import Structures.SequenceListe;

import java.util.ArrayList;
import java.util.Arrays;

public class CoupParTour extends Commande{

    //Type d'action :
    //Si deplacement --> 1
    //Si attaque directe --> 2
    //Si deplacement + attaque --> 3
    int typeAction ;

    ArrayList<Coup> coupsTour;
    Coup[] coupsTourTab;
    int nbCoups;
    Manche manche;
    int tourJoueur;

    public CoupParTour(int typeaction, ArrayList<Coup> coupstour, Coup[] coupstourtab, int nbcoups) {
        nbCoups = nbcoups;
        coupstour = new ArrayList<>();
        coupsTourTab = new Coup[2];
        typeAction = typeaction;
        coupsTour = coupstour;
        coupsTourTab = coupstourtab;
    }

    public Coup getCoup(int numero) {
        if(coupsTour.size() == 0) {
            return null;
        }
        Coup c = coupsTour.get(numero);
        //coupsTour.insereTete(c);
        return c;
    }

    void fixerManche(Manche m)
    {
        manche = m;
    }


    void execute(CoupParTour cp) {
        Coup cpe = cp.coupsTourTab[cp.nbCoups-1];
        cpe.execute(cpe);
    }

    void desexecute(CoupParTour cp) {
        Coup cpd = cp.coupsTourTab[0];
        cpd.desexecute(cpd);
    }

    @Override
    public String toString() {
        return "CoupParTour{" +
                "typeAction=" + typeAction +
                ", coupsTour=" + coupsTour +
                ", coupsTourTab=" + Arrays.toString(coupsTourTab) +
                ", manche=" + manche +
                '}';
    }
}
