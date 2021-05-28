package Modele;


import Structures.SequenceListe;

public class Historique<E extends Commande> {
    SequenceListe<CoupParTour> CoupFait, CoupAnnuler;

    Historique(){
        CoupFait = new SequenceListe<>();
        CoupAnnuler = new SequenceListe<>();
    }

    void nouveau(CoupParTour c){
        System.out.println("Coup : " + c);
        CoupFait.insereTete(c);
        //c.execute(c);
        while(!CoupAnnuler.estVide()){
            CoupAnnuler.extraitTete();
        }


    }

    public CoupParTour coupPrecedent()
    {
        if(CoupFait.estVide())
        {
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
        return !CoupFait.estVide();
    }

    CoupParTour refaire(){
        if(peutRefaire()){

            CoupParTour c = CoupAnnuler.extraitTete();

            CoupFait.insereTete(c);
            return c;
        }else{
            return null;
        }
    }

    CoupParTour annuler(){
        if(peutAnnuler()){
            CoupParTour c = CoupFait.extraitTete();
            c.desexecute(c);

            CoupAnnuler.insereTete(c);
            return c;
        }else{
            return null;
        }
    }

}