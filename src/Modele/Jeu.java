package Modele;

import Global.Configuration;
import Patterns.Observable;
import Vue.CollecteurEvenements;
import Vue.InterfaceGraphique;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

public class Jeu extends Observable {
    Partie courant;
    public ArrayList<CarteIHM> selectedCarte;
    public SelectionCaseIHM selectedCase;
    PrintStream sortie;
    int compteurMap, compteurJ1, compteurJ2;
    Configuration config;
    CollecteurEvenements control;

    public Jeu() {

    }

    public void initialisePartie(int nbMap, int nbJ1, int nbJ2){
        courant = new Partie(this);
        selectedCarte = new ArrayList<>();
        compteurJ1 = nbJ1;
        compteurMap = nbMap;
        compteurJ2 = nbJ2;
        control.inter().niv().setDecors();
        miseAJour();
    }

    public void fixerCollecteurEvenement(CollecteurEvenements c){
        control = c;
    }

    public void initialisePartieSauve(String Save){
        config = new Configuration(Save);
        int pdv1 = Integer.parseInt(config.lis("Joueur1Vie"));
        int pdv2 = Integer.parseInt(config.lis("Joueur2Vie"));
        int positionJ1 = Integer.parseInt(config.lis("PositionJ1"));
        int positionJ2 = Integer.parseInt(config.lis("PositionJ2"));
        String pioche = config.lis("Pioche");
        String MainJ1 = config.lis("MainJoueur1");
        String MainJ2 = config.lis("MainJoueur2");
        int type = Integer.parseInt(config.lis("TypePartie"));
        int tourCourant = Integer.parseInt(config.lis("TourJoueur"));
        courant = new Partie(this, pdv1, pdv2, positionJ1, positionJ2, pioche, MainJ1, MainJ2, tourCourant, type);
        selectedCarte = new ArrayList<>();
        compteurJ1 = Integer.parseInt(config.lis("CompteurJ1"));
        compteurJ2 = Integer.parseInt(config.lis("CompteurJ2"));
        compteurMap = Integer.parseInt(config.lis("CompteurMap"));
        control.inter().niv().setDecorsSauve(compteurJ1, compteurJ2, compteurMap);
        config.delete(Save);
        miseAJour();
    }

    public Partie partie() {
        return courant;
    }

    public Coup determinerCoup(int type, int[] valeurs, int[] grilleJeu, int typeAction) {
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

    public void sauve(OutputStream out) {
            sortie = new PrintStream(out);
            sortie.println("CompteurMap="+compteurMap);
            sortie.println("CompteurJ1="+compteurJ1);
            sortie.println("CompteurJ2="+compteurJ2);
            sortie.println("Joueur1Vie="+courant.joueur1.vie);
            sortie.println("Joueur2Vie="+courant.joueur2.vie);
            sortie.println("TourJoueur="+courant.manche().tourJoueur);
            sortie.println("PositionJ1="+courant.manche().joueur1.position);
            sortie.println("PositionJ2="+courant.manche().joueur2.position);
            sortie.print("Pioche=");
            for(int i = 0; i < courant.manche().piocheCartes.size(); i++){
                sortie.print(courant.manche().piocheCartes.get(i));
            }
            sortie.println();
            sortie.print("MainJoueur1=");
            for(int i = 0; i < courant.manche().joueur1.main.size(); i++){
                sortie.print(courant.manche().joueur1.main.get(i));
            }
            sortie.println();
            sortie.print("MainJoueur2=");
            for(int i = 0; i < courant.manche().joueur2.main.size(); i++){
                sortie.print(courant.manche().joueur2.main.get(i));
            }
            sortie.println();
            sortie.print("TypePartie=");
            sortie.print(courant.type);
            sortie.close();
    }

    public CoupParTour annule(){
        CoupParTour cp = partie().manche().annuler();
        miseAJour();
        return cp;
    }

    public CoupParTour refaire(){
        CoupParTour cp = partie().manche().refaire();
        miseAJour();
        return cp;
    }
}
