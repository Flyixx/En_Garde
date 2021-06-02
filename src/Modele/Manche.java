package Modele;
import java.util.*;
import java.util.concurrent.atomic.AtomicIntegerArray;
import javax.swing.text.html.parser.Element;

import Controller.*;
import Vue.ButtonIHM;

import javax.swing.text.html.parser.Element;

public class Manche extends Historique<CoupParTour>{

    static final int AVANCER = 1;
    static final int ATTAQUER = 2;
    static final int PARADE_INDIRECTE = 3;
    static final int PARADE_DIRECTE = 4;
    public int NOMBRE_CASES = 23;


    Partie partie;
    ArrayList<Integer> piocheCartes = new ArrayList<>();
    public ArrayList<SelectionCaseIHM> CaseIHM = new ArrayList<>();
    public ButtonIHM boutonChangeTour;
    public int[] grilleJeu;
    public int tourJoueur;
    public JoueurHumain joueur1, joueur2;
    ArrayList<Coup> coupsTour;
    Coup[] coupsTourTab;
    public int nbCoupsJoues;
    public boolean doitParer, peutSauvegarderEtHistorique;


    public Manche(Partie p, int premierTourPrecedent){
        doitParer = false;
        peutSauvegarderEtHistorique = true;
        partie = p;

        //Les joueurs de la partie associés à la manche
        joueur1 = partie.Joueur(1);
        joueur2 = partie.Joueur(2);

        //Initialiser la pioche de la manche
        initialiserPioche();

        grilleJeu = new int[NOMBRE_CASES];
        //Situation du joueur 1 au début de la partie
        grilleJeu[0] = 1;
        joueur1.position = 0;
        joueur1.direction = 1;
        //joueur1.vie = 5;
        //Situation du joueur 2 au début de la partie
        grilleJeu[22] = 2;
        joueur2.position = 22;
        joueur2.direction = -1;
        //joueur2.vie = 5;
        viderMain(joueur1);
        System.out.println("Main joueur1 :" + joueur1.getMain());
        viderMain(joueur2);
        System.out.println("Main joueur2 :" + joueur2.getMain());
        remplirMain(joueur1);
        remplirMain(joueur2);
        System.out.println("Pioche complete : " + piocheCartes);

        coupsTour = new ArrayList<>();
        coupsTourTab = new Coup[3];

        tourJoueur = premierTourPrecedent;

    }

    //Constructeur de la manche en cour lors de la sauvegarde
    public Manche(Partie p, int positionJ1, int positionJ2, String pioche, String MainJ1, String MainJ2, int tourCourant){
        doitParer = false;
        peutSauvegarderEtHistorique = true;
        partie = p;

        //Les joueurs de la partie associés à la manche
        joueur1 = partie.Joueur(1);
        joueur2 = partie.Joueur(2);

        grilleJeu = new int[NOMBRE_CASES];

        //Mise en place du joueur 1
        grilleJeu[positionJ1] = 1;
        joueur1.position = positionJ1;
        joueur1.direction = 1;

        //Mise en place du joueur 2
        grilleJeu[positionJ2] = 2;
        joueur2.position = positionJ2;
        joueur2.direction = -1;

        //Vider la main des joueurs pour les remplir correctement
        viderMain(joueur1);
        viderMain(joueur2);

        //Remplissage de la main du joueur 1
        char MainJ1Char[] = MainJ1.toCharArray();
        for(int i = 0; i < 5; i++){
            joueur1.main.add(Character.digit(MainJ1Char[i],10));
        }

        //Remplissage de la main du joueur 2
        char MainJ2Char[] = MainJ2.toCharArray();
        for(int i = 0; i < 5; i++){
            joueur2.main.add(Character.digit(MainJ2Char[i],10));

        }

        //Remplissage de la pioche
        char PiocheChar[] = pioche.toCharArray();
        for(int i = 0; i < PiocheChar.length; i++){
            piocheCartes.add(Character.digit(PiocheChar[i],10));
        }

        System.out.println("Pioche complete : " + piocheCartes);

        coupsTour = new ArrayList<>();
        coupsTourTab = new Coup[3];

        //Mise en place du tour
        tourJoueur = tourCourant;

    }

    public void tourAutomatique(JoueurHumain j) { // essai IA
        System.out.println("Tour IA : ma pioche = " + j.getMain());
        Random rand = new Random();
        int random = rand.nextInt(4);
        System.out.println("Je choisis la carte : " + random);
    }


    public boolean piocheVide(){
        return piocheCartes.size()==0;
    }
    public int restantPioche(){
        return piocheCartes.size();
    }

    public void initialiserPioche(){
        for(int i =1; i<=5;i++){
            for(int j=1; j<=5;j++){
                piocheCartes.add(i);
            }
        }
        System.out.println("Pioche non triee : " + piocheCartes);
        Collections.shuffle(piocheCartes);
        System.out.println("Pioche melangee :" + piocheCartes);

    }

    public void attaque(int j){
        System.out.println("joueur : " + j);

        if(j == 1){
            partie.joueur1.vie-= 1;
        }else{
            partie.joueur2.vie-= 1;
        }


    }

    public void remplirMain(JoueurHumain j){
        for(int i=0;i<j.main.size();i++){
            if(j.main.get(i) == 0){

                j.main.remove(i);
                i =0;
            }
        }

        if(j.main.size() >0)
        {
            if(j.main.get(0) == 0)
            {
                j.main.remove(0);
            }
        }

        while(j.main.size() < 5){
            int carte = pioche();
            j.main.add(carte);

        }
        System.out.println("main complete joueur " + inverse() + " : " + j.main);
    }

    public int inverse()
    {
        if(tourJoueur == 1)
        {
            return 2;
        }
        else
        {
            return 1;
        }
    }

    public void viderMain(JoueurHumain j){
        //System.out.println("vidage de main joueur " + j.getDirection() + " : " + j.getMain());
        int tailleMain = j.main.size();
        for(int i=0;i<tailleMain;i++){
            j.main.remove(0);
        }
        //System.out.println("vidage de main fini");
    }

    public int pioche(){ // le joueur récupère une carte dans l
        int res;
        if(piocheVide())
        {
            System.out.println("Pioche vide: Evalution du joueur gagnant:");
            int Pj1 = 0, Pj2 = 0;
            int distance = joueur2.getPosition() - joueur1.getPosition();
            for (int i = 0; i < joueur1.getMain().size(); i++){
                if(distance == joueur1.getMain().get(i)){
                    Pj1 += 1;
                }
            }
            for (int i = 0; i < joueur2.getMain().size(); i++){
                if(distance == joueur2.getMain().get(i)){
                    Pj2 += 1;
                }
            }

            if (Pj1 < Pj2){
                attaque(1);
                System.out.println("Joueur 2 gagne: Plus de carte d'attaque direct");
                partie.initialiseManche();
            }else if(Pj2 < Pj1){
                attaque(2);
                System.out.println("Joueur 1 gagne: Plus de carte d'attaque direct");
                partie.initialiseManche();
            } else if(Pj1 == Pj2){
                int PosJ1 = joueur1.getPosition();
                int PosJ2 = NOMBRE_CASES - joueur1.getPosition();

                if (PosJ1 > PosJ2){
                    attaque(1);
                    System.out.println("Joueur 2 gagne: Joueur le plus avancé");
                    partie.initialiseManche();
                } else {
                    attaque(2);
                    System.out.println("Joueur 1 gagne: Joueur le plus avancé");
                    partie.initialiseManche();
                }
            }

            return 0;

        } else {
            res = piocheCartes.get(0);
            piocheCartes.remove(0);
        }

        return res;
    }

    public boolean listerCoups(JoueurHumain j, ArrayList<CarteIHM> cartes, boolean afficher){ // lister tous les coups possible à partir d'une carte (action simple)

        boolean possibilites = false;
        boolean peutseDeplacer = false;
        if(nbCoupsJoues == 0 || (nbCoupsJoues == 1 && coupsTourTab[0].action.id == 4))
        {
            peutseDeplacer = true;
        }

        if(cartes.size() == 1)// 1 seule carte selectionnee
        {
            int newPos;
            int dir = j.direction;
            int valeurCarte = cartes.get(0).getValeur();
            //System.out.println("carte : " + valeurCarte);
            if(dir == 1) { // joueur à gauche

                if(j.position >= valeurCarte && peutseDeplacer){
                    possibilites = true;
                    newPos = j.position - valeurCarte;
                    //System.out.println("peut reculer en " + newPos);
                    //peutAttaquer(cartes.get(0), newPos, j);

                    if(afficher)
                    {
                        CaseIHM.get(newPos).updateEtat(1);
                    }

                }
                newPos = j.position + valeurCarte;
                if(newPos <= 22){
                    if(newPos == joueur2.position ){
                        possibilites = true;

                        if(afficher)
                        {
                            CaseIHM.get(newPos).updateEtat(2);
                        }

                        //System.out.println("peut attaquer le joueur avec carte " + valeurCarte);
                        //peutAttaquer(cartes.get(0), j.getPosition(), j);
                    }else if(newPos < joueur2.position && peutseDeplacer){
                        possibilites = true;
                        //System.out.println("peut avancer en " + newPos);
                        //peutAttaquer(cartes.get(0), newPos, j);

                        if(afficher)
                        {
                            CaseIHM.get(newPos).updateEtat(1);
                        }

                        //Coup coup = new Coup()
                        //joue(1,valeurCarte);
                    }else{
                        CaseIHM.get(newPos).updateEtat(0);
                        //System.out.println("bloqué par joueur");
                    }
                }
            }

            if(dir == -1){ // joueur à droite
                if(j.position +valeurCarte <= 22 && peutseDeplacer){
                    possibilites = true;
                    newPos = j.position + valeurCarte;

                    if(afficher)
                    {
                        CaseIHM.get(newPos).updateEtat(1);
                    }

                    //System.out.println("peut reculer en " + newPos);
                    //peutAttaquer(cartes.get(0), newPos, j);
                }
                newPos = j.position - valeurCarte;
                if(newPos >= 0){
                    if(newPos == joueur1.position ){
                        possibilites = true;
                        //System.out.println("peut attaquer le joueur avec carte " + valeurCarte);

                        if(afficher)
                        {
                            CaseIHM.get(newPos).updateEtat(2);
                        }

                        //peutAttaquer(cartes.get(0), j.getPosition(), j);
                    }else if(newPos > joueur1.position && peutseDeplacer){
                        possibilites = true;
                        //System.out.println("peut avancer en " + newPos);

                        if(afficher)
                        {
                            CaseIHM.get(newPos).updateEtat(1);
                        }

                    }else{
                        //System.out.println("bloqué par joueur");
                    }
                }
            }
        }
        else if(cartes.size() > 1)//Plusieurs cartes selectionnées
        {
            int newPos;
            int dir = j.direction;
            int valeurCarte = cartes.get(0).getValeur();

            if(dir == 1) { // joueur à gauche
                newPos = j.position + valeurCarte;
                if(newPos <= 22){
                    if(newPos == joueur2.position ){
                        possibilites = true;

                        if(afficher)
                        {
                            CaseIHM.get(newPos).updateEtat(2);
                        }
                        //System.out.println("peut attaquer le joueur avec carte " + valeurCarte);
                        //peutAttaquer(cartes.get(0), j.getPosition(), j);
                    }
                }
            }

            if(dir == -1){ // joueur à droite
                newPos = j.position - valeurCarte;
                if(newPos >= 0){
                    if(newPos == joueur1.position ){
                        possibilites = true;
                        //System.out.println("peut attaquer le joueur avec carte " + valeurCarte);

                        if(afficher)
                        {
                            CaseIHM.get(newPos).updateEtat(2);
                        }

                        //peutAttaquer(cartes.get(0), j.getPosition(), j);
                    }
                }
            }
        }
        return possibilites;

    }
    public int getDistance(){
        return joueur2.getPosition() - joueur1.getPosition();
    }

    public ArrayList<Coup> listerCoupIA(JoueurHumain j) { // va renvoyer un tableau de coups possible dans lequel on ira piocher aléatoirement
        ArrayList coups = new ArrayList<Coup>();
        if(nbCoupsJoues < 2 || coupsTourTab[0].action.id == 4)
        {
            boolean peutseDeplacer = false;
            if(nbCoupsJoues == 0 || (nbCoupsJoues == 1 && coupsTourTab[0].action.id == 4))
            {
                peutseDeplacer = true;
            }

            // 1 = avancer 2 = reculer 3 = attaquer
            int newPos;
            int distance = this.getDistance();
            int dir = j.direction;

            for(int i  = 0; i< j.getMain().size(); i++)
            {
                int valeurCarte =  j.getMain().get(i);
                //System.out.println("Valeur carte : " + valeurCarte);
                int[] possibilites = {0};
                //System.out.println("carte : " + valeurCarte);

                if(dir == -1){ // joueur à droite
                    if(j.position +valeurCarte <= 22 && peutseDeplacer){
                        newPos = j.position + valeurCarte;

                        int[] valeurs = new int[5];
                        valeurs[0] = valeurCarte;
                        Action ac = new Action(1,valeurs);
                        Coup cp = new Coup(grilleJeu, ac, newPos);
                        cp.fixerManche(this);
                        coups.add(cp);

                        //System.out.println("peut reculer en " + newPos);
                    }
                    newPos = j.position - valeurCarte;
                    if(newPos >= 0){
                        if(newPos == joueur1.position ){
                            int nbAttks  =  peutAttaquer(valeurCarte, distance, j);
                            //System.out.println("Nb attaques possibles :" + nbAttks);
                            for(int k = 1; k<= nbAttks ; k++)
                            {
                                int[] valeurs = new int[5];

                                for(int l = 0; l<k;l++ )
                                    valeurs[l] = valeurCarte;

                                Action ac = new Action(2,valeurs);
                                int target = joueur1.getPosition();
                                Coup cp = new Coup(grilleJeu, ac, target);
                                cp.fixerManche(this);
                                coups.add(cp);
                            }
                            //System.out.println("peut attaquer le joueur avec carte " + valeurCarte);
                        }else if(newPos > joueur1.position && peutseDeplacer){

                            int[] valeurs = new int[5];
                            valeurs[0] = valeurCarte;
                            Action ac = new Action(1,valeurs);
                            Coup cp = new Coup(grilleJeu, ac, newPos);
                            cp.fixerManche(this);
                            coups.add(cp);
                            //System.out.println("peut avancer en " + newPos);
                            //CaseIHM.get(newPos).updateEtat(1);

                        }else{
                           // System.out.println("bloqué par joueur");
                        }
                    }
                }
            }
            return coups;
        }
        else
        {
            return coups;
        }

    }

    public int peutAttaquer(int valeurCarte,int distance, JoueurHumain j){ // regarde si la carte selectionnée lui permet d'attaquer l'autre joueur
        //int distance;

       /* if(j.getDirection()==1){
            distance = joueur2.getPosition() - pos;
        }else{
            distance =  pos - joueur1.getPosition();
        }*/
            //System.out.println("distance : " + distance);
            if(distance > 5){
                //System.out.println("Trop loin pour attaquer");
                return 0;
            }else{
                int nbAtk = 0;
                int valCarte = 0;
                /*
                if(valeurCarte == distance){
                    nbAtk++;
                }*/
                for(int i=0;i<j.main.size();i++){
                        if(distance == j.main.get(i)){
                            valCarte = j.main.get(i);
                            nbAtk++;
                        }

                }
                if(valCarte != 0){
                    //System.out.println("Peut attaquer avec la carte " + valCarte + " en " + nbAtk + " exemplaires !");
                    return nbAtk;
                }else{
                    //System.out.println("N'a pas de carte pour attaquer");
                    return 0;
                }
            }

    }

    public void jouerCoup(Coup cp) {
        peutSauvegarderEtHistorique = false;

        System.out.println("je fais jouer coup !");
        //efface les cases select
        updateAll();

        nbCoupsJoues++;
        cp.fixerManche(this);
        //nouveau(cp);

        //on supprime de la main du joueur toutes les cartes selectionnees pour jouer
        if(cp.action.id != PARADE_DIRECTE && cp.action.id!=PARADE_INDIRECTE)
        {
            if(partie.type == 2 && tourJoueur == 2)
            {
                int carteJouer = cp.GetAction().valeurs[0];
                int suppr = 0;
                for(int i = 0; i < joueur2.main.size() && cp.GetAction().valeurs[suppr]!= 0; i++)
                {
                    if(joueur2.main.get(i) == carteJouer)
                    {
                        suppr++;
                        partie.Joueur(tourJoueur).supprMain(i);
                    }

                }
            }
            else
            {
                for(int i = 0; i < partie.jeu.selectedCarte.size(); i++)
                {
                    partie.Joueur(tourJoueur).supprMain(partie.jeu.selectedCarte.get(i).getId());
                }
            }
        }

        // Si l'action est une parade
        if(cp.action.id == PARADE_DIRECTE)
        {
            int nbSuppr = 0;
            int l = 0;
            //Supprimer le nombre de cartes de la bonne valeur pour parer
            while(l< partie.Joueur(tourJoueur).main.size() && cp.action.valeurs[nbSuppr] !=0)
            {
                if(partie.Joueur(tourJoueur).main.get(l) == cp.action.valeurs[0])
                {
                    partie.Joueur(tourJoueur).supprMain(l);
                    nbSuppr ++;
                }
                l++;

            }
        }

        coupsTour.add(cp);

        if(coupsTourTab[0] == null)
        {
            coupsTourTab[0] = cp;

            if(cp.action.id == ATTAQUER)
            {
                //updateAll();

                for(int f = 0; f<partie.jeu.selectedCarte.size(); f++)
                {
                    partie.jeu.selectedCarte.remove(f);
                    f=0;
                }

                if(partie.jeu.selectedCarte.size()>0)
                {
                    partie.jeu.selectedCarte.remove(0);
                }

                changeTourJoueur();
            }
            else if(cp.action.id == PARADE_INDIRECTE)
            {
                changeTourJoueur();
            }
        }
        else if(coupsTourTab[1] == null && coupsTourTab[0] != null)
        {
            coupsTourTab[1] = cp;

            if(cp.action.id == ATTAQUER)
            {
                //updateAll();

                for(int f = 0; f<partie.jeu.selectedCarte.size(); f++)
                {
                    partie.jeu.selectedCarte.remove(f);
                    f=0;
                }

                if(partie.jeu.selectedCarte.size()>0)
                {
                    partie.jeu.selectedCarte.remove(0);
                }

                changeTourJoueur();

            }
            else if(coupsTourTab[0].action.id != PARADE_DIRECTE){
                changeTourJoueur();
            }
        }
        else // Quand le joueur a joué 3 coups
        {
            coupsTourTab[2] = cp;
            changeTourJoueur();
        }





        //changeTourJoueur(tourJoueur);

    }

    public void miseAJourGrille(int oldPosJ1, int oldPosJ2, int posJ1, int posJ2){
        // Les conditions sont codées de manière à ce que si un joueur bouge, l'autre ne bouge pas
        // Si les deux conditions ne sont pas respectées, cela veut dire qu'aucun joueur n'a bougé
        if(posJ1 != oldPosJ1){
            this.grilleJeu[oldPosJ1] = 0;
            this.grilleJeu[posJ1] = 1;
        } else if (posJ2 != oldPosJ2) {
            this.grilleJeu[oldPosJ2] = 0;
            this.grilleJeu[posJ2] = 2;
        } else {
            System.out.println("Aucun joueur n'a bougé");
        }
    }


    public Coup joue(int target, int[] valeurs, int[] grilleJeu, int typeAction){
        JoueurHumain joueurCourant;
        Action action = new Action(typeAction, valeurs);
        Coup coupCourant = new Coup(grilleJeu, action, target);
        coupCourant.fixerManche(this);

        int oldPosJ1 = this.joueur1.getPosition();
        int oldPosJ2 = this.joueur2.getPosition();


        //_____________________  Recupérer le joueur courant



        joueurCourant = partie.Joueur(tourJoueur);

        if(typeAction == AVANCER || (typeAction == PARADE_INDIRECTE && doitParer))
        {
            joueurCourant.deplace(target);

            if (tourJoueur == 1) {
                this.joueur1 = joueurCourant;
            } else {
                this.joueur2 = joueurCourant;
            }

            miseAJourGrille(oldPosJ1, oldPosJ2, this.joueur1.getPosition(), this.joueur2.getPosition());

        }
        else{

            //attaque(tourJoueur);
        }


        return coupCourant;
        //======================================================================= CAS ACTION EST UN DPLACEMENT
        /*if(type == AVANCER || type == RECULER) {

            //_____________________ Détermine de combien de cases on va se déplacer
            int nbDeplacement = valeurs[0];

            //_____________________ Récuperation des positions courantes
            int target = 0;
            int oldPosJ1 = this.joueur1.getPosition();
            int oldPosJ2 = this.joueur2.getPosition();

            //======================================================================= CAS ACTION = AVANCER
            if (type == AVANCER) {
                target = joueurCourant.targetAvant(nbDeplacement);
            }
            //======================================================================= CAS ACTION = RECULER
            else if (type == RECULER) {
                target = joueurCourant.targetArriere(nbDeplacement);
            }

            if (target > 0 && target < grilleJeu.length && estVide(target) && testPosition(target)) {

                //_____________________  On met à jour le joueur courant
                joueurCourant.deplace(target);
                if (tourJoueur == 1) {
                    this.joueur1 = joueurCourant;
                } else {
                    this.joueur2 = joueurCourant;
                }

                //_____________________ On met à jour les infos générales du jeu.
                miseAJourGrille(oldPosJ1, oldPosJ2, this.joueur1.getPosition(), this.joueur2.getPosition());


            return coupCourrant;
        } else {
            System.out.println("Déplacement impossible, personnage sur la case destination ou destination hors map");
                return null;
            }
        }*/
        //return null;
    }

    public void initCaseIHM(int i, int val, int x, int y, int largeur, int hauteur, int etat){
        SelectionCaseIHM caseI = new SelectionCaseIHM(i, val, x, y, largeur, hauteur, etat);
        CaseIHM.add(caseI);
    }

    public void initButtonChangeTour(int x, int y, int largeur, int hauteur) {
       boutonChangeTour = new ButtonIHM(1, "ChangeTour", x, y, largeur, hauteur);
    }
    public void updateCaseIHM(int i, int val, int x, int y, int largeur, int hauteur){
        CaseIHM.get(i).update(i, val, x, y, largeur, hauteur);
    }

    public ArrayList<SelectionCaseIHM> getCaseIHM() {
        return CaseIHM;
    }

    public int getTourJoueur(){ return tourJoueur;}

    // Changement du tour du joueur
    // Cette fonction enregistre les coups du joueur précédent dans un CoupParTour avant de changer le
    // tour du joueur
    public void changeTourJoueur() {
        System.out.println("Je change le tour");
        CoupParTour coupTour = null;

        //Suppression des cartes sélectionnées pour le dernier coup
        if(partie.jeu.selectedCarte != null)
        {
            for(int i = 0; i<partie.jeu.selectedCarte.size(); i++)
            {
                partie.jeu.selectedCarte.remove(i);
                i = 0;
            }

            if(partie.jeu.selectedCarte.size() != 0)
            {
                partie.jeu.selectedCarte.remove(0);
            }
        }

        // Si la variable coupsTourTab contient au moins un Coup
        if(coupsTourTab[0] !=null) {

            //Si le premier coup joué n'est pas une parade
            if(coupsTourTab[0].action.id != 4) {
                if (/*coupsTour.size() == 1 && coupsTour.get(coupsTour.size()-1).action.id == 1*/coupsTourTab[1] == null && coupsTourTab[0].action.id == 1) {

                    //Simple deplacement
                    coupTour = new CoupParTour(1, coupsTour, coupsTourTab,nbCoupsJoues);

                } else if (/*coupsTour.size() == 1 && coupsTour.get(coupsTour.size() - 1).action.id == 2*/ coupsTourTab[1] == null && coupsTourTab[0].action.id == 2) {

                    //Attaque directe
                    coupTour = new CoupParTour(2, coupsTour, coupsTourTab, nbCoupsJoues);

                } else if (/*coupsTour.size() == 2 && coupsTour.get(coupsTour.size() - 1).action.id == 2*/ coupsTourTab[1] != null && coupsTourTab[1].action.id == 2) {

                    //Attaque indirecte
                    coupTour = new CoupParTour(3, coupsTour, coupsTourTab, nbCoupsJoues);
                }
                else
                {
                    // Simple parade d'une attaque indirecte
                    coupTour = new CoupParTour(4, coupsTour, coupsTourTab, nbCoupsJoues);
                }
            }
            else
            {
                if(coupsTourTab[1] == null)
                {
                    coupTour = new CoupParTour(4, coupsTour, coupsTourTab, nbCoupsJoues);
                }
                else if (/*coupsTour.size() == 1 && coupsTour.get(coupsTour.size()-1).action.id == 1*/coupsTourTab[2] == null && coupsTourTab[1].action.id == 1) {

                    //Déplacement après une parade directe
                    coupTour = new CoupParTour(1, coupsTour, coupsTourTab, nbCoupsJoues);

                } else if (/*coupsTour.size() == 1 && coupsTour.get(coupsTour.size() - 1).action.id == 2*/ coupsTourTab[2] == null && coupsTourTab[1].action.id == 2) {

                    //Attaque directe après une parade directe
                    coupTour = new CoupParTour(2, coupsTour, coupsTourTab, nbCoupsJoues);

                } else if (/*coupsTour.size() == 2 && coupsTour.get(coupsTour.size() - 1).action.id == 2*/ coupsTourTab[2] != null && coupsTourTab[2].action.id == 2) {

                    //Attaque indirecte après une parade directe
                    coupTour = new CoupParTour(3, coupsTour, coupsTourTab, nbCoupsJoues);
                }
                else
                {
                    //Rien après une parade
                    coupTour = new CoupParTour(4, coupsTour, coupsTourTab, nbCoupsJoues);
                }
            }
        }


        // Enregistrement du coupTour dans l'historique
        if(coupTour != null)
        {
            coupTour.fixerManche(this);
            nouveau(coupTour);
        }

        // Récupération du CoupTour joué par le joueur adverse : il permet de récupérer les actions
        // effectuées par le joueur adverse au tour précédent
        CoupParTour coupPrecedent = coupPrecedent();


        videListe(coupsTour);

        // Remet le nombre de coup joués à 0 et du tableau des Coups pour un tour
        nbCoupsJoues = 0;
        coupsTourTab = new Coup[3];


        // Remplissage de la main du joueur précédent
        if(tourJoueur == 1)
        {
            this.tourJoueur = 2;
        }
        else
        {
            this.tourJoueur = 1;

        }


        JoueurHumain joueurcourant = Joueur(tourJoueur);

        if(joueurcourant.carteI.size() > 0)
        {
            TestProchainCoup(coupPrecedent);
           /* boolean poss = false;
            for(int i = 0; i<joueurcourant.carteI.size(); i++)
            {
                ArrayList<CarteIHM> liste = new ArrayList();
                liste.add(joueurcourant.carteI.get(i));
                if(listerCoups(joueurcourant,liste, false))
                {
                    poss = true;
                }
            }

            System.out.println("Possibilites : " + poss);

            if(!poss)
            {
                partie.Joueur(tourJoueur).vie -= 1;
                partie.initialiseManche();
            }
            else
            {
                // Voir sur la fonction
                TestProchainCoup(coupPrecedent);
            }*/
        }
        else
        {
            TestProchainCoup(coupPrecedent);
        }

        // Remplissage de la main du joueur précédent
        if(tourJoueur == 1)
        {
            //System.out.println("Joueur 1 pioche");
            remplirMain(joueur2);
            //System.out.println("Cartes restantes pioche : " + this.restantPioche());
        }
        else
        {
            //System.out.println("Joueur 2 pioche");
            remplirMain(joueur1);

            // gestion IA
            if(partie.type == 2){
                System.out.println("Joueur 2 est IA ");
                jouerIA(joueur2);
                //this.tourJoueur = 1;
            }

            //System.out.println("Cartes restantes pioche : " + this.restantPioche());

        }
        peutSauvegarderEtHistorique = true;
    }

    public void jouerIA(JoueurHumain j){
        System.out.println("Tour de l'IA ! ");
        ArrayList<Coup> coups = this.listerCoupIA(j);
        System.out.println("tous les coups possibles : " + coups);
        Random rnd = new Random();
        int total = coups.size();
        int random = 0;
        if(total != 0){
            random = rnd.nextInt(total)+1;
        }
        System.out.println("total : " + total + " choisi : " + random);
        Coup cp = coups.get(random-1);
        System.out.println("selection du coup : " + cp );
;
        cp = joue(cp.target, cp.action.valeurs, cp.mapAvant, cp.action.id);
        partie.jeu.jouerCoup(cp);

        coups = this.listerCoupIA(j);



        if(coups.size() != 0 && getTourJoueur() == 2){
            System.out.println("IA: Attaque indirecte ");
            jouerIA(j);
        }
        else if(getTourJoueur() == 2)
        {

            changeTourJoueur();
        }


    }

    public void updateAll() {
        for (int i = 0; i < CaseIHM.size(); i++){
            CaseIHM.get(i).updateEtat(0);
        }
    }

    public void videListe(ArrayList<Coup> liste) {
        for(int i = 0; i<liste.size(); i++)
        {
            liste.remove(i);
            i =0;
        }

        if(liste.size() > 0)
        {
            liste.remove(0);
        }
    }

    //Pour connaitre l'action que le joueur doit effectuer par rapport au coup precedent
    public void TestProchainCoup(CoupParTour coupPrecedent) {
        if(coupPrecedent != null)
        {
            // Si le CoupParTour est un simple déplacement
            if(coupPrecedent.typeAction == 1)
            {
                System.out.println("coup precedent: Le joueur adverse a juste avancé/reculé");

            }
            // Si le CoupParTour est une attaque directe ou une attaque indirecte
            if(coupPrecedent.typeAction == 2 || coupPrecedent.typeAction == 3)
            {

                // Récupération du nombre de cartes utilisées pour l'attaque
                int nbCartes = 0;
                Coup cp = coupPrecedent.coupsTourTab[coupPrecedent.nbCoups-1];
                for(int j = 0; j<cp.action.valeurs.length; j++)
                {
                    if(cp.action.valeurs[j] != 0)
                    {
                        nbCartes++;
                    }
                }

                // Si le CoupParTour est une attaque directe
                if(coupPrecedent.typeAction == 2)
                {
                    System.out.println("coup precedent: Le joueur adverse a effectué une attaque directe");
                    ParerAttaqueDirecte(coupPrecedent, nbCartes);
                    //System.out.println("Vous devez parer avec "+ nbCartes + " carte(s) de valeur : " + cp.action.valeurs[0]);
                }
                else // Si le CoupParTour est une attaque indirecte
                {
                    System.out.println("coup precedent: Le joueur adverse a effectué une attaque indirecte");
                    ParerAttaqueIndirecte(coupPrecedent, nbCartes);
                }
            }

        }
    }

    public boolean peutParerDirectement(CoupParTour coupPrecedent, int nbCartes) {
        //Si le nombre de cartes de l'attaque précédente est supérieur à 2, il est impossible de parer
        if(nbCartes > 2)
        {
            System.out.println("Je ne peux pas parer directement");
            return false;
        }
        else //Regarde si le joueur a le nombre de cartes de la valeur recherchée
        {
            JoueurHumain joueur = partie.Joueur(getTourJoueur());
            JoueurHumain joueuradverse = null;
            if(getTourJoueur() == 1)
            {
                joueuradverse = Joueur(2);
            }
            else
            {
                joueuradverse = Joueur(1);
            }

            int valeurRecherchee = coupPrecedent.coupsTourTab[coupPrecedent.nbCoups-1].action.valeurs[0];
            int nbCartesRecherchees =0;
            int i = 0;
            while (i <joueur.main.size() && nbCartesRecherchees < nbCartes)
            {
                if(joueur.main.get(i) == valeurRecherchee)
                {
                    nbCartesRecherchees++;
                }
                i++;
            }

            if(nbCartesRecherchees == nbCartes)
            {
                System.out.println("je peux parer directement!");
                return true;
            }
            else
            {
                return false;
            }
        }
    }

    //Parer une attaque directe
    public void ParerAttaqueDirecte(CoupParTour coupPrecedent, int nbCartes)
    {
        // Si le joueur peut parer directement alors il effectue autamtiquement la parade
        if(peutParerDirectement(coupPrecedent, nbCartes))
        {
            int pos;
            if(getTourJoueur() == 1){
                pos = joueur2.getPosition();
            } else {
                pos = joueur1.getPosition();
            }

            // On change l'etat de la case IHM à la position du joueur adverse :
            // Elle passe à l'etat 3 ce qui signifie que si on clique sur cette case on effectue
            // une parade directe
            CaseIHM.get(pos).updateEtat(3);
            doitParer = true;
        }
        else // Sinon, le joueur subit une attaque et perd un point de vie et engendre une nouvelle manche
        {
            attaque(tourJoueur);
            partie.initialiseManche();
        }
    }

    // Parer une attaque indirecte (déplacement + attaque)
    public void ParerAttaqueIndirecte(CoupParTour coupPrecedent, int nbCartes)
    {
        int valeurRecherchee = coupPrecedent.coupsTourTab[0].action.valeurs[0];

        // Si le joueur peut parer directement (c'est à dire sans reculer)
        if(peutParerDirectement(coupPrecedent, nbCartes))
        {
            int pos;
            if(getTourJoueur() == 1){
                pos = joueur2.getPosition();
            } else {
                pos = joueur1.getPosition();
            }

            // On change l'etat de la case IHM à la position du joueur adverse :
            // Elle passe à l'etat 3 ce qui signifie que si on clique sur cette case on effectue
            // une parade directe
            CaseIHM.get(pos).updateEtat(3);
        }
        else if(!peutReculer() && !peutParerDirectement(coupPrecedent, nbCartes)){
            // Si le joueur ne peut pas reculer ni parer directement, le joueur subit une attaque
            // et perd une vie --> nouvelle manche
            attaque(tourJoueur);
            partie.initialiseManche();
        }
        JoueurHumain j = Joueur(getTourJoueur());
        //System.out.println("tour:" + getTourJoueur());
        for(int i = 0; i < j.getMain().size(); i++){
            int target = 0;
            if(tourJoueur == 1)
            {
                target = j.position - j.main.get(i);
                if(target >= 0)
                {
                    CaseIHM.get(target).updateEtat(1);
                }
            }
            else{
                target = j.position + j.main.get(i);
                if(target <=23)
                {
                    CaseIHM.get(target).updateEtat(1);
                }
            }
        }

        doitParer = true;
        peutSauvegarderEtHistorique = false;


    }

    public JoueurHumain Joueur(int tourJoueur) {
        if(tourJoueur == 1)
        {
            return joueur1;
        }
        else
        {
            return joueur2;
        }
    }

    public boolean peutReculer() {
        boolean peutReculer = false;
        JoueurHumain joueurcourant = Joueur(tourJoueur);

        for(int i = 0; i <joueurcourant.main.size(); i++)
        {
            if(tourJoueur == 1)
            {
                if(joueurcourant.position - joueurcourant.main.get(i) >= 0)
                {
                    peutReculer = true;
                }
            }
            else{
                if(joueurcourant.position + joueurcourant.main.get(i) <=23)
                {
                    peutReculer = true;
                }
            }
        }

        return peutReculer;
    }

    public void parerDirectement() {
        CoupParTour coupPrecedent = coupPrecedent();
        int valeurRecherchee = coupPrecedent.coupsTourTab[coupPrecedent.nbCoups-1].action.valeurs[0];
        int nbCartes = 0;

        int i =0;

        while( coupPrecedent.coupsTourTab[coupPrecedent.nbCoups-1].action.valeurs[i] != 0 && i< coupPrecedent.coupsTourTab[coupPrecedent.nbCoups-1].action.valeurs.length )
        {
            nbCartes++;
            i++;
        }

        JoueurHumain joueuradverse = null;
        if(getTourJoueur() == 1)
        {
            joueuradverse = Joueur(2);
        }
        else
        {
            joueuradverse = Joueur(1);
        }

        int[] riposte = new int[5];
        for(int j = 0; j< nbCartes; j++)
        {
            riposte[j] = valeurRecherchee;
        }

        //Il s'agit d'une parade directe d'attaque
        Coup cp = partie.jeu.determinerCoup(joueuradverse.position, riposte, grilleJeu, 4);
        partie.jeu.jouerCoup(cp);
        System.out.println("j'ai paré !");
        System.out.println("Ma main après avoir parer :" + Joueur(tourJoueur).main);

    }

    public void Revenir(Coup cp){
        grilleJeu = cp.mapAvant;
    }

    public void Inverse(Coup cp){
        grilleJeu = cp.mapAvant;
    }
}
