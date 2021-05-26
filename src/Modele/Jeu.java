package Modele;

import Patterns.Observable;

import java.util.ArrayList;

public class Jeu extends Observable {
    Partie courant;
    public ArrayList<CarteIHM> selectedCarte;
    public SelectionCaseIHM selectedCase;

    public Jeu()
    {
        initialisePartie();
    }

    public void initialisePartie(){
        courant = new Partie(this);
        selectedCarte = new ArrayList<>();
        miseAJour();
    }

    public Partie partie() {
        return courant;
    }

    public Coup determinerCoup(int type, int[] valeurs, int[] grilleJeu, int typeAction)
    {
        return courant.manche().joue(type, valeurs, grilleJeu,typeAction );
    }

    public void jouerCoup(Coup cp) {
        if (cp == null) {
            System.out.println("Salut");
            //Configuration.instance().logger().info("Déplacement impossible");
        } else {
            courant.jouerCoup(cp);
            miseAJour();
        }
    }

    public void SelectionCarte(int id, int val ,int x, int y, int l, int h) {
        ArrayList<SelectionCaseIHM> CaseIHM = new ArrayList<>();
        CaseIHM = partie().manche().CaseIHM;
        for (int i = 0; i < CaseIHM.size(); i++){
            partie().manche().CaseIHM.get(i).updateEtat(0);
        }

        int taille = selectedCarte.size();

        if(selectedCarte.size()>0 && id == selectedCarte.get(taille -1).getId())
        {
            selectedCarte.remove(taille -1);
        }
        else
        {
            boolean peutAjouter = true;

            for(int k =0; k<selectedCarte.size(); k++)
            {
                if(selectedCarte.get(k).getId() == id)
                {
                    peutAjouter = false;
                }
            }
            if(peutAjouter)
            {
                if(selectedCarte.size()>0 && selectedCarte.get(taille -1).getValeur() == val)
                {
                    selectedCarte.add(new CarteIHM(id, val, x, y, l, h));
                }
                else
                {
                    for(int i = 0; i<selectedCarte.size(); i++)
                    {
                        selectedCarte.remove(i);
                        i =0;
                    }

                    if(selectedCarte.size()>0)
                    {
                        selectedCarte.remove(0);
                    }

                    selectedCarte.add(new CarteIHM(id, val, x, y, l, h));
                }
            }
        }



        miseAJour();
    }
}
