package Controller;

import Modele.CarteIHM;
import Modele.Jeu;
import Modele.SelectionCaseIHM;
import Structures.Iterateur;
import Structures.Sequence;
import Structures.SequenceListe;
import Vue.ButtonIHM;
import Vue.CollecteurEvenements;
import Vue.InterfaceGraphique;
import Vue.NiveauGraphique;
import Modele.Coup;

import java.util.ArrayList;

public class ControllerMediateur implements CollecteurEvenements {
	Jeu jeu;
	JoueurHumain Joueur1, Joueur2;
	final int lenteurAttente = 50;
	int joueurCourant;
	int decompte;
	Sequence<Animation> animations;
	InterfaceGraphique inter;

	public ControllerMediateur(Jeu j){
		jeu = j;
		animations = new SequenceListe<>();
		Joueur1 = j.partie().Joueur(1);
		Joueur2 = j.partie().Joueur(2);
		joueurCourant = 1; //jeu.partie().getTourJoueur();
	}

	public void fixerInterfaceGraphique(InterfaceGraphique i){
		inter = i;
		animations.insereQueue(new AnimationJoueur(inter));
	}


	public void clickCarte(int x, int y){

		JoueurHumain joueur = jeu.partie().Joueur(jeu.partie().manche().getTourJoueur());
		for(int i = 0; i < joueur.getCarteI().size(); i++){
			CarteIHM c = joueur.getCarteI().get(i);
			if(x >= c.getCoordX() && x <= (c.getCoordX() + c.getLargeur()) && c.getEtat() != 1){
				if((y >= c.getCoordY() && y <= (c.getCoordY() + c.getHauteur()))){

					 jeu.SelectionCarte(i, c.getValeur(), c.getCoordX(), c.getCoordY(), c.getLargeur(), c.getHauteur());
					 jeu.partie().manche().listerCoups(joueur, jeu.selectedCarte);

				}
			}
		}
	}

	public void clickDeplacement(int x, int y){
		ArrayList<SelectionCaseIHM> CaseIHM = new ArrayList<>();
		CaseIHM = jeu.partie().manche().getCaseIHM();

		for(int i = 0; i < CaseIHM.size(); i++){
			SelectionCaseIHM c = CaseIHM.get(i);
			if(c.getEtat() != 0 && x >= c.getX() && x <= (c.getX() + c.getLargeur())){
				if((y >= c.getY() && y <= (c.getY() + c.getHauteur()))){


					if (c.getEtat() == 1){
						int[] valeurs= new int[5];
						valeurs[0] = jeu.selectedCarte.get(0).getValeur();
						Coup cp = jeu.determinerCoup(c.getId(), valeurs,jeu.partie().manche().grilleJeu, 1);
						jeu.jouerCoup(cp);

						for(int j =0; j<jeu.selectedCarte.size();j ++)
						{
							jeu.selectedCarte.get(0).reset();
						}


					} else if (c.getEtat() == 2){
						int[] valeurs = new int[5];

						for(int j = 0; j<jeu.selectedCarte.size(); j++)
						{
							valeurs[j] = jeu.selectedCarte.get(j).getValeur();
						}

						Coup cp = jeu.determinerCoup(c.getId(), valeurs, jeu.partie().manche().grilleJeu, 2);
						jeu.jouerCoup(cp);

						for(int f = 0; f<jeu.selectedCarte.size(); f++)
						{
							jeu.selectedCarte.remove(f);
							f=0;
						}

						if(jeu.selectedCarte.size()>0)
						{
							jeu.selectedCarte.remove(0);
						}
						//jeu.partie().manche().updateAll();
						jeu.partie().manche().changeTourJoueur(jeu.partie().manche().getTourJoueur());
						jeu.partie().initialiseManche();

					}
				}
			}
		}
	}

	public void clickChangeTour(int x, int y)
	{
		if(jeu.partie().manche().boutonChangeTour != null)
		{
			ButtonIHM but = jeu.partie().manche().boutonChangeTour;
			if (x >= but.getX() && x < but.getX() + but.getLargeur()){
				if (y >= but.getY() && y < but.getY() + but.getHauteur()){
					jeu.partie().manche().changeTourJoueur(jeu.partie().manche().tourJoueur);

					for(int f = 0; f<jeu.selectedCarte.size(); f++)
					{
						jeu.selectedCarte.remove(f);
						f=0;
					}

					if(jeu.selectedCarte.size()>0)
					{
						jeu.selectedCarte.remove(0);
					}

					jeu.partie().manche().updateAll();
					System.out.println("Je change le tour");
				}
			}
		}
	}


	public void clickChange(int x, int y){
		if((y >= inter.niv().yBouton && y <= (inter.niv().yBouton + inter.niv().tailleBouton))){
			System.out.println("Salut y");
			if((x >= inter.niv().xBouton1 && x <= (inter.niv().xBouton1 + inter.niv().tailleBouton))) {
				System.out.println("Salut x1");
				if(inter.niv().compteurJ1 == 0){
					inter.niv().compteurJ1 = 1;
				}else{
					inter.niv().compteurJ1--;
				}
			}else if((x >= inter.niv().xBouton2 && x <= (inter.niv().xBouton2 + inter.niv().tailleBouton))) {
				System.out.println("Salut x2");
				inter.niv().compteurJ1 = (inter.niv().compteurJ1+1)%2;
			}else if((x >= inter.niv().xBouton3 && x <= (inter.niv().xBouton3 + inter.niv().tailleBouton))){
				System.out.println("Salut x3");
				if(inter.niv().compteurJ2 == 0){
					inter.niv().compteurJ2 = 1;
				}else{
					inter.niv().compteurJ2--;
				}
			}else if((x >= inter.niv().xBouton4 && x <= (inter.niv().xBouton4 + inter.niv().tailleBouton))){
				System.out.println("Salut x4");
				inter.niv().compteurJ2 = (inter.niv().compteurJ2+1)%2;
			}else if((x >= inter.niv().xBouton5 && x <= (inter.niv().xBouton5 + inter.niv().tailleBouton))){
				System.out.println("Salut x5");
				if(inter.niv().compteurMap == 0){
					inter.niv().compteurMap = 7;
				}else{
					inter.niv().compteurMap--;
				}
			}else if((x >= inter.niv().xBouton6 && x <= (inter.niv().xBouton6 + inter.niv().tailleBouton))){
				System.out.println("Salut x6");
				inter.niv().compteurMap = (inter.niv().compteurMap+1)%8;
			}
		}
		System.out.println(inter.niv().compteurJ1);
		System.out.println(inter.niv().compteurJ2);
		System.out.println(inter.niv().compteurMap);
		inter.niv().metAJour();
	}

	/*public void avancer() {
		int[] valeurs= new int[5];
		valeurs[0] = jeu.selectedCarte.getValeur();
		Coup cp = jeu.determinerCoup(1, valeurs,jeu.partie().manche().grilleJeu);
		jeu.jouerCoup(cp);
		jeu.selectedCarte.reset();

	}

	public void reculer() {
		int[] valeurs= new int[5];
		valeurs[0] = jeu.selectedCarte.getValeur();
		Coup cp = jeu.determinerCoup(2, valeurs,jeu.partie().manche().grilleJeu);
		jeu.jouerCoup(cp);
		jeu.selectedCarte.reset();
	}*/



	public void tictac(){
		if(inter.getMenu()){
			inter.MAJPanelMenu();
		}
		if (inter.getRegles()) {
			inter.MAJPanelRegle();
		}
		if(inter.getNewPartie()){
			inter.MAJPanelNewPartie();
		}
		Iterateur<Animation> it = animations.iterateur();
		while(it.aProchain()){
			Animation anim = it.prochain();
			anim.tictac();
		}
		if(!jeu.partie().aGagner()){
			if(decompte == 0){
				if(joueurCourant == 2){
					//Faire jouer le joueur 2
					changeJoueur();
				}else{
					decompte = lenteurAttente;
				}
			}else{
				decompte--;
			}
		}else{
			joueurCourant = 1;
		}
	}

	public void changeJoueur(){
		if(joueurCourant == 1){
			joueurCourant = 2;
		}else{
			joueurCourant = 1;
		}
	}

    @Override
    public boolean commande(String c) {
        switch (c) {
			case "NewPartie":
				inter.changeBackground(false, false, false, true);
				break;
			case "Regles":
				inter.changeBackground(false, false, true, false);
				break;
			case "RetourMenu":
				inter.changeBackground(true, false, false, false);
				break;
			case "PartieLance":
				Jeu jeu = new Jeu();
				inter.changeBackground(false, true, false, false);
			case "Suivant":
				if(inter.niv().compteur < 10){
					inter.niv().compteur++;
				}
				break;
			case "Revenir":
				if(inter.niv().compteur > 0){
					inter.niv().compteur--;
				}
				break;
			case "quit":
				System.exit(0);
				break;
			case "fullscreen":
				break;

			default:
				return false;
		}
		return true;
    }
}
