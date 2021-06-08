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
    boolean devaitParer;
    int[] etatCasesIHM = new int[23];

    public CoupParTour(int typeaction, ArrayList<Coup> coupstour, Coup[] coupstourtab, int nbcoups, boolean DevaitParer, int[] EtatCasesIHM) {
        nbCoups = nbcoups;
        coupstour = new ArrayList<>();
        coupsTourTab = new Coup[2];
        typeAction = typeaction;
        coupsTour = coupstour;
        coupsTourTab = coupstourtab;
        devaitParer = DevaitParer;
        etatCasesIHM = EtatCasesIHM;

    }

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

    CoupParTour execute(CoupParTour cp) {
        CoupParTour cp1 = cp;
        for(int i = 0; i < cp.nbCoups; i++){
            Coup cpe = cp.coupsTourTab[i];
            Coup cpf;
            cpf = cpe.execute(cpe);
            cp1.coupsTourTab[i] = cpf;
        }
        return  cp1;
    }

    void desexecute(CoupParTour cp) {
        Coup cpd = cp.coupsTourTab[0];
        boolean devaitParer = cp.devaitParer;
        int[] etatCasesIHM = cp.etatCasesIHM;
        cpd.desexecute(cpd, cp.tourJoueur, devaitParer, etatCasesIHM);
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
