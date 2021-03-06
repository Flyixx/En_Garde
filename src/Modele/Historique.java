package Modele;


import Structures.Sequence;
import Structures.SequenceListe;

public class Historique<E extends Commande> {
    public Sequence<CoupParTour> CoupFait;
    public Sequence<CoupParTour> CoupAnnuler;

    Historique(){
        CoupFait = new SequenceListe<>();
        CoupAnnuler = new SequenceListe<>();
    }

    void nouveau(CoupParTour c){
        //System.out.println("Coup : " + c);
        CoupFait.insereTete(c);
        //System.out.println("La séquence de fdp " +CoupFait);
        //c.execute(c);
        while(!CoupAnnuler.estVide()){
            CoupAnnuler.extraitTete();
        }
    }

    void afficherListe(Sequence<CoupParTour> l){
        //System.out.println("j'affiche" + l);
    }

    public CoupParTour coupPrecedent() {
        if(CoupFait.estVide()) {
            return null;
        }
        CoupParTour c = CoupFait.extraitTete();
        CoupFait.insereTete(c);
        return c;
    }

    public boolean peutRefaire(){
        return !CoupAnnuler.estVide();
    }

    public boolean peutAnnuler(){
        //System.out.println(!CoupFait.estVide());
        //System.out.println("La sequence annuler " + CoupFait);
        return !CoupFait.estVide();
    }

    CoupParTour refaire(){
        if(peutRefaire()){
            CoupParTour c = CoupAnnuler.extraitTete();
            CoupParTour cp1 = c.execute(c);
            CoupFait.insereTete(cp1);
            return cp1;
        }else{
            return null;
        }
    }

    CoupParTour annuler(){
        if(peutAnnuler()){
            CoupParTour c = CoupFait.extraitTete();
            c.desexecute(c);
            CoupAnnuler.insereTete(c);
            //System.out.println("Sequence annuler :" + CoupAnnuler);
            return c;
        }else{
            return null;
        }
    }

}