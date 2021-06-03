package Controller;

import Modele.*;
import Structures.Iterateur;
import Structures.Sequence;
import Structures.SequenceListe;
import Vue.ButtonIHM;
import Vue.CollecteurEvenements;
import Vue.InterfaceGraphique;
import Vue.NiveauGraphique;

import java.util.ArrayList;

public class ControllerMediateur implements CollecteurEvenements {
	Jeu jeu;
	JoueurHumain Joueur1, Joueur2;
	final int lenteurAttente = 100;
	int joueurCourant;
	int decompte;
	Sequence<Animation> animations;
	Animation mouvement;
	InterfaceGraphique inter;

	public ControllerMediateur(Jeu j){
		jeu = j;
		animations = new SequenceListe<>();
		mouvement = null;
		/*Joueur1 = j.partie().Joueur(1);
		Joueur2 = j.partie().Joueur(2);*/
		joueurCourant = 1; //jeu.partie().getTourJoueur();
	}

	public void fixerInterfaceGraphique(InterfaceGraphique i){
		inter = i;
		animations.insereQueue(new AnimationJoueur(inter));
	}

	public InterfaceGraphique inter(){
		return inter;
	}


	public void clickCarte(int x, int y) {
		if(jeu.partie().type != 2 || jeu.partie().manche().tourJoueur != 2){
			// Si le joueur doit parer, il ne peut pas cliquer sur une carte
			if (inter.niv().Partie) {
				if (!jeu.partie().manche().doitParer) {
					JoueurHumain joueur = jeu.partie().Joueur(jeu.partie().manche().getTourJoueur());
					for (int i = 0; i < joueur.getCarteI().size(); i++) {
						CarteIHM c = joueur.getCarteI().get(i);
						if (x >= c.getCoordX() && x <= (c.getCoordX() + c.getLargeur()) && c.getEtat() != 1) {
							if ((y >= c.getCoordY() && y <= (c.getCoordY() + c.getHauteur()))) {

								jeu.SelectionCarte(i, c.getValeur(), c.getCoordX(), c.getCoordY(), c.getLargeur(), c.getHauteur());
								jeu.partie().manche().listerCoups(joueur, jeu.selectedCarte, true);

							}
						}
					}
				}
			}
		}
	}

	public void clickDeplacement(int x, int y){

		if(jeu.partie().type != 2 || jeu.partie().manche().tourJoueur != 2)
		{
			ArrayList<SelectionCaseIHM> CaseIHM = new ArrayList<>();
			CaseIHM = jeu.partie().manche().getCaseIHM();
			Coup cp = null;
			for(int i = 0; i < CaseIHM.size(); i++){
				SelectionCaseIHM c = CaseIHM.get(i);
				if(c.getEtat() != 0 && x >= c.getX() && x <= (c.getX() + c.getLargeur())){
					if((y >= c.getY() && y <= (c.getY() + c.getHauteur()))){


						if (c.getEtat() == 1){
							int[] valeurs= new int[5];

							cp = null;
							System.out.println("Doit parer : " + jeu.partie().manche().doitParer);
							if(jeu.partie().manche().doitParer)
							{
								if(jeu.partie().manche().getTourJoueur() == 1)
								{
									valeurs[0] = jeu.partie().Joueur(jeu.partie().manche().getTourJoueur()).getPosition() - i;
								}
								else
								{
									valeurs[0] = i - jeu.partie().Joueur(jeu.partie().manche().getTourJoueur()).getPosition();
								}
								cp = jeu.determinerCoup(c.getId(), valeurs,jeu.partie().manche().grilleJeu, 3);
								boolean suppr = false;
								for(int f = 0; f<jeu.partie().Joueur(jeu.partie().manche().getTourJoueur()).main.size() && suppr != true; f++)
								{
									if(jeu.partie().Joueur(jeu.partie().manche().getTourJoueur()).main.get(f) == valeurs[0])
									{
										jeu.partie().Joueur(jeu.partie().manche().getTourJoueur()).supprMain(f);
										suppr = true;

									}
								}
								jeu.partie().manche().doitParer = false;
							}
							else
							{
								valeurs[0] = jeu.selectedCarte.get(0).getValeur();
								int[] grilleuJeux = jeu.partie().manche().grilleJeu;
								cp = jeu.determinerCoup(c.getId(), valeurs,grilleuJeux, 1);
							}

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

							cp = jeu.determinerCoup(c.getId(), valeurs, jeu.partie().manche().grilleJeu, 2);
							jeu.jouerCoup(cp);


							//jeu.partie().manche().updateAll();
							//jeu.partie().initialiseManche();

						} else if (c.getEtat() == 3){
							jeu.partie().manche().parerDirectement();
							System.out.println("Tu peux parer mon pote");
							jeu.partie().manche().doitParer = false;

						}
					}
				}
			}
			if(cp != null){
				mouvement = new AnimationCoup(cp, inter, 1, 2);
				animations.insereQueue(mouvement);
			}
		}

	}

	public void clickChangeTour(int x, int y) {
		if(jeu.partie().type != 2 || jeu.partie().manche().tourJoueur != 2) {
			if(inter.niv().Partie) {
					//ButtonIHM but = jeu.partie().manche().boutonChangeTour;
					if (x >= inter.niv().xBoutonDroite && x < (inter.niv().xBoutonDroite+inter.niv().largeurBouton)){
						if (y >= inter.niv().yBoutonBas && y < (inter.niv().yBoutonBas+inter.niv().hauteurBouton)){
							if(jeu.partie().manche().nbCoupsJoues != 0) {
								int nb = jeu.partie().manche().tourJoueur;
								jeu.partie().manche().changeTourJoueur();

								for (int f = 0; f < jeu.selectedCarte.size(); f++) {
									jeu.selectedCarte.remove(f);
									f = 0;
								}

								if (jeu.selectedCarte.size() > 0) {
									jeu.selectedCarte.remove(0);
								}

								//jeu.partie().manche().updateAll();
								System.out.println("Je change le tour");
								inter.niv().modifMessage(3, nb, 0, 0);
								inter().niv().msg2 = 2;
							}else{
								System.out.println("Impossible le joueur doit jouer au moins une carte");
								inter.niv().msg = 1;
								inter.niv().msg2 = 0;
							}

						}
					}
			}
		}

	}

	public void clickChange(int x, int y){
		if((y >= inter.niv().yBouton && y <= (inter.niv().yBouton + inter.niv().tailleBouton))){
			if((x >= inter.niv().xBouton1 && x <= (inter.niv().xBouton1 + inter.niv().tailleBouton))) {
				if(inter.niv().compteurJ1 == 0){
					inter.niv().compteurJ1 = 1;
				}else{
					inter.niv().compteurJ1--;
				}
			}else if((x >= inter.niv().xBouton2 && x <= (inter.niv().xBouton2 + inter.niv().tailleBouton))) {
				inter.niv().compteurJ1 = (inter.niv().compteurJ1+1)%2;
			}else if((x >= inter.niv().xBouton3 && x <= (inter.niv().xBouton3 + inter.niv().tailleBouton))){
				if(inter.niv().compteurJ2 == 0){
					inter.niv().compteurJ2 = 1;
				}else{
					inter.niv().compteurJ2--;
				}
			}else if((x >= inter.niv().xBouton4 && x <= (inter.niv().xBouton4 + inter.niv().tailleBouton))){
				inter.niv().compteurJ2 = (inter.niv().compteurJ2+1)%2;
			}else if((x >= inter.niv().xBouton5 && x <= (inter.niv().xBouton5 + inter.niv().tailleBouton))){
				if(inter.niv().compteurMap == 0){
					inter.niv().compteurMap = 7;
				}else{
					inter.niv().compteurMap--;
				}
			}else if((x >= inter.niv().xBouton6 && x <= (inter.niv().xBouton6 + inter.niv().tailleBouton))){
				inter.niv().compteurMap = (inter.niv().compteurMap+1)%8;
			}
		}
		System.out.println(inter.niv().compteurJ1);
		System.out.println(inter.niv().compteurJ2);
		System.out.println(inter.niv().compteurMap);
		inter.niv().metAJour();
	}

	@Override
	public void clickQuitterJeu(int x, int y) {
		if(inter.niv().MenuPartie){
			if(x >= inter.niv().xBoutonMenu && x < (inter.niv().xBoutonMenu+inter.niv().largeurBoutonMenu)){
				if(y >= inter.niv().yBoutonTrois && y < (inter.niv().yBoutonTrois+inter.niv().hauteurBoutonMenu)){
					commande("RetourMenu");
				}
			}
		}else if(inter.niv().Victoire){
			if(x >= 0 && x < inter.niv().largeurBouton){
				if(y >= inter.niv().yBoutonVictoire && y < (inter.niv().yBoutonVictoire+inter.niv().hauteurBouton)){
					commande("RetourMenu");
				}
			}
		}
	}

	@Override
	public void clickMenuPartie(int x, int y){
		if(x > inter.niv().xBoutonGauche && x <= inter.niv().xBoutonGauche+inter.niv().largeurBouton){
			if(y > inter.niv().yBoutonBas && y <= inter.niv().yBoutonBas+inter.niv().hauteurBouton){
				commande("MenuPartie");
			}
		}
	}

	@Override
	public void clickSauvegarder(int x, int y) {
		if(x >= inter.niv().xBoutonMenu && x < (inter.niv().xBoutonMenu+inter.niv().largeurBoutonMenu)){
			if(y >= inter.niv().yBoutonDeux && y < (inter.niv().yBoutonDeux+inter.niv().hauteurBoutonMenu)){
				inter.sauve();
			}
		}
	}

	@Override
	public void clickAnnuler(int x, int y) {
		if(x >= inter.niv().xBoutonGauche && x < (inter.niv().xBoutonGauche+inter.niv().largeurBouton)){
			if(y >= inter.niv().yBoutonMilieu2 && y < (inter.niv().yBoutonMilieu2+inter.niv().hauteurBouton)){
				System.out.println("Annuler");
				commande("Annuler");
			}
		}
	}

	@Override
	public void clickRefaire(int x, int y) {
		if(x >= inter.niv().xBoutonGauche && x < (inter.niv().xBoutonGauche+inter.niv().largeurBouton)){
			if(y >= inter.niv().yBoutonMilieu && y < (inter.niv().yBoutonMilieu+inter.niv().hauteurBouton)){
				System.out.println("Refaire");
				commande("Refaire");
			}
		}
	}

	@Override
	public void clickRevenirPartie(int x, int y){
		if(x >= inter.niv().xBoutonMenu && x < (inter.niv().xBoutonMenu+inter.niv().largeurBoutonMenu)){
			if(y >= inter.niv().yBoutonUn && y < (inter.niv().yBoutonUn+inter.niv().hauteurBoutonMenu)){
				commande("RevenirPartie");
			}
		}
	}

	public void clickMute(int x, int y){
		if(inter.niv().MenuPartie || inter.niv().Menu || inter.niv().Regles || inter.niv().NewPartie || inter.niv().Victoire){
			if(x >= inter.niv().xBoutonMute && x < (inter.niv().xBoutonMute + inter.niv().tailleMute)){
				if(y >= inter.niv().yBoutonMute && y < (inter.niv().yBoutonMute + inter.niv().tailleMute)){
					inter.niv().mute = !inter.niv().mute;
					inter.niv().mute();
				}
			}
		}
	}

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
		if(inter.niv().Partie){
			if(!jeu.partie().aGagner()){
				if(decompte == 0){
					if(jeu.partie().manche().getTourJoueur() == 2 && jeu.partie().type == 2){
						//Faire jouer le joueur 2
						//changeJoueur();
						jeu.partie().manche().jouerIA(jeu.partie().manche().joueur2);
						decompte = lenteurAttente/2;
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
	}

	public void changeJoueur(){
		if(joueurCourant == 1){
			joueurCourant = 2;
		}else{
			joueurCourant = 1;
		}
	}

	void annule(){
		jeu.annule();
	}

	void refaire(){
		jeu.refaire();
	}

    @Override
    public boolean commande(String c) {
        switch (c) {
			case "NewPartie":
				inter.changeBackground(false, false, false, true, false, false);
				break;
			case "Regles":
				inter.changeBackground(false, false, true, false, false, false);
				break;
			case "RetourMenu":
				inter.changeBackground(true, false, false, false, false, false);
				break;
			case "PartieLance":
				jeu.initialisePartie(inter.niv().compteurMap, inter.niv().compteurJ1, inter.niv().compteurJ2);
				jeu.partie().type = 1;
				Joueur1 = jeu.partie().Joueur(1);
				Joueur2 = jeu.partie().Joueur(2);
				inter.changeBackground(false, true, false, false, false, false);
				break;
			case "PartieIAF":
				jeu.initialisePartie(inter.niv().compteurMap, inter.niv().compteurJ1, inter.niv().compteurJ2);
				jeu.partie().type = 2;
				Joueur1 = jeu.partie().Joueur(1);
				Joueur2 = jeu.partie().Joueur(2);
				inter.changeBackground(false, true, false, false, false,false);
				break;
			case "ChargePartie":
				boolean bool = inter.charge();
				if(bool){
					inter.changeBackground(false, true, false, false, false, false);
				}
				break;
			case "RevenirPartie":
				inter.changeBackground(false, true, false, false, false, false);
				break;
			case "MenuPartie":
				inter.changeBackground(false, false, false, false, false, true);
				break;
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
			case "Annuler":
				annule();
				break;
			case "Refaire":
				refaire();
				break;
			default:
				return false;
		}
		return true;
    }
}
