package Modele;


import Structures.Sequence;
import Structures.SequenceListe;

public class Historique<E extends Commande> {
    Sequence<CoupParTour> CoupFait, CoupAnnuler, CoupPasse;

    Historique(){
        CoupFait = new SequenceListe<>();
        CoupAnnuler = new SequenceListe<>();
        CoupPasse = new SequenceListe<>();
    }

    void nouveau(CoupParTour c){
        System.out.println("Coup : " + c);
        CoupFait.insereTete(c);
        //System.out.println("La séquence de fdp " +CoupFait);
        //c.execute(c);
        while(!CoupAnnuler.estVide()){
            CoupAnnuler.extraitTete();
        }
        CoupPasse = CoupFait;
    }

    void afficherListe(Sequence<CoupParTour> l){
        System.out.println("j'affiche" + l);
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
        System.out.println(!CoupFait.estVide());
        System.out.println("La sequence annuler " + CoupFait);
        return !CoupFait.estVide();
    }

    CoupParTour refaire(){
        if(peutRefaire()){
            CoupParTour c = CoupAnnuler.extraitTete();
            c.execute(c);
            CoupFait.insereTete(c);
            return c;
        }else{
            return null;
        }
    }

    CoupParTour annuler(){
        System.out.print("Salut ");
        afficherListe(CoupPasse);
        System.out.println("La tchoin");
        if(peutAnnuler()){
            System.out.println("Super ça marche");
            CoupParTour c = CoupFait.extraitTete();
            c.desexecute(c);
            CoupAnnuler.insereTete(c);
            return c;
        }else{
            return null;
        }
    }

}