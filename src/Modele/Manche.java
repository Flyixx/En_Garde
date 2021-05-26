package Modele;
import java.util.*;
import Controller.*;
import Vue.ButtonIHM;

public class Manche extends Historique<Coup>{

    static final int AVANCER = 1;
    static final int RECULER = 2;
    static final int ATTAQUER = 3;
    static final int ATTAQUE_INDIRECTE = 4;
    static final int BLOQUER = 5;
    public int NOMBRE_CASES = 23;

    Partie partie;
    ArrayList<Integer> piocheCartes = new ArrayList<>();
    public ArrayList<SelectionCaseIHM> CaseIHM = new ArrayList<>();
    public ButtonIHM boutonChangeTour;
    public int[] grilleJeu;
    public int tourJoueur;
    public JoueurHumain joueur1, joueur2;


    public Manche(Partie p){

        partie = p;
        //int sel = p.jeu.selectedCarte;

        //Les joueurs de la partie associés à la manche
        joueur1 = partie.Joueur(1);
        joueur2 = partie.Joueur(2);

        //Initialiser la pioche de la manche
        initialiserPioche();

        grilleJeu = new int [NOMBRE_CASES];
        //Situation du joueur 1 au début de la partie
        grilleJeu[10] = 1;
        joueur1.position = 10;
        joueur1.direction = 1;
        //joueur1.vie = 5;
        //Situation du joueur 2 au début de la partie
        grilleJeu[15] = 2;
        joueur2.position = 15;
        joueur2.direction = -1;
        //joueur2.vie = 5;
        viderMain(joueur1);
        viderMain(joueur2);
        remplirMain(joueur1);
        remplirMain(joueur2);
        System.out.println("Pioche complete : " + piocheCartes);

        tourJoueur = 1;

    }
    public boolean piocheVide(){
        if(piocheCartes.size() == 0){
            return true;
        }else {
            return false;
        }
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
            partie.joueur2.vie-= 1;
        }else{
            partie.joueur1.vie-= 1;
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
        System.out.println("main complete joueur " + tourJoueur + " : " + j.main);
    }

    public void viderMain(JoueurHumain j){
        for(int i=0;i<j.main.size();i++){
            j.main.remove(i);
        }
    }

    public int pioche(){ // le joueur récupère une carte dans l
        int res;
        if(piocheVide())
        {
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
                attaque(Pj2);
                partie.initialiseManche();
            }else if(Pj2 < Pj1){
                attaque(Pj1);
                partie.initialiseManche();
            } else if(Pj1 == Pj2){
                int PosJ1 = joueur1.getPosition();
                int PosJ2 = NOMBRE_CASES - joueur1.getPosition();

                if (PosJ1 > PosJ2){
                    attaque(Pj2);
                    partie.initialiseManche();
                } else {
                    attaque(Pj1);
                    partie.initialiseManche();
                }
            }

            return 0;

        }
        else{
            res = piocheCartes.get(0);
            piocheCartes.remove(0);
        }

        return res;
    }

    public void listerCoups(JoueurHumain j, ArrayList<CarteIHM> cartes){ // lister tous les coups possible à partir d'une carte (action simple)

        if(cartes.size() == 1)// 1 seule carte selectionnee
        {
            int newPos;
            int dir = j.direction;
            int valeurCarte = cartes.get(0).getValeur();
            //System.out.println("carte : " + valeurCarte);
            if(dir == 1) { // joueur à gauche
                if(j.position >= valeurCarte){
                    newPos = j.position - valeurCarte;
                    System.out.println("peut reculer en " + newPos);
                    peutAttaquer(cartes.get(0), newPos, j);

                    CaseIHM.get(newPos).updateEtat(1);
                }
                newPos = j.position + valeurCarte;
                if(newPos <= 22){
                    if(newPos == joueur2.position ){
                        CaseIHM.get(newPos).updateEtat(2);
                        //System.out.println("peut attaquer le joueur avec carte " + valeurCarte);
                        peutAttaquer(cartes.get(0), j.getPosition(), j);
                    }else if(newPos < joueur2.position){
                        System.out.println("peut avancer en " + newPos);
                        peutAttaquer(cartes.get(0), newPos, j);
                        CaseIHM.get(newPos).updateEtat(1);
                        //Coup coup = new Coup()
                        //joue(1,valeurCarte);
                    }else{
                        CaseIHM.get(newPos).updateEtat(0);
                        System.out.println("bloqué par joueur");
                    }
                }
            }

            if(dir == -1){ // joueur à droite
                if(j.position +valeurCarte <= 22){
                    newPos = j.position + valeurCarte;
                    CaseIHM.get(newPos).updateEtat(1);
                    System.out.println("peut reculer en " + newPos);
                    peutAttaquer(cartes.get(0), newPos, j);
                }
                newPos = j.position - valeurCarte;
                if(newPos >= 0){
                    if(newPos == joueur1.position ){
                        //System.out.println("peut attaquer le joueur avec carte " + valeurCarte);
                        CaseIHM.get(newPos).updateEtat(2);
                        peutAttaquer(cartes.get(0), j.getPosition(), j);
                    }else if(newPos > joueur1.position){
                        System.out.println("peut avancer en " + newPos);
                        if(peutAttaquer(cartes.get(0), newPos, j)){

                        }
                        CaseIHM.get(newPos).updateEtat(1);
                    }else{
                        System.out.println("bloqué par joueur");
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
                        CaseIHM.get(newPos).updateEtat(2);
                        //System.out.println("peut attaquer le joueur avec carte " + valeurCarte);
                        peutAttaquer(cartes.get(0), j.getPosition(), j);
                    }
                }
            }

            if(dir == -1){ // joueur à droite
                newPos = j.position - valeurCarte;
                if(newPos >= 0){
                    if(newPos == joueur1.position ){
                        //System.out.println("peut attaquer le joueur avec carte " + valeurCarte);
                        CaseIHM.get(newPos).updateEtat(2);
                        peutAttaquer(cartes.get(0), j.getPosition(), j);
                    }
                }
            }
        }

    }

    public void listerCoupComplexe(JoueurHumain j, CarteIHM carte){ // coups composés de plusieurs cartes
        int newPos;
        int dir = j.direction;
        int valeurCarte = carte.getValeur();
        //System.out.println("carte : " + valeurCarte);
        if(dir == 1) { // joueur à gauche
            if(j.position >= valeurCarte){
                newPos = j.position - valeurCarte;
                System.out.println("peut reculer en " + newPos);
                CaseIHM.get(newPos).updateEtat(1);
            }
            newPos = j.position + valeurCarte;
            if(newPos <= 22){
                if(newPos == joueur2.position ){
                    CaseIHM.get(newPos).updateEtat(2);
                    System.out.println("peut attaquer le joueur avec carte " + valeurCarte);
                }else if(newPos < joueur2.position){
                    System.out.println("peut avancer en " + newPos);
                    peutAttaquer(carte, newPos, j);
                    CaseIHM.get(newPos).updateEtat(1);
                    //Coup coup = new Coup()
                    //joue(1,valeurCarte);
                }else{
                    CaseIHM.get(newPos).updateEtat(0);
                    System.out.println("bloqué par joueur");
                }
            }
        }

        if(dir == -1){ // joueur à droite
            if(j.position +valeurCarte <= 22){
                newPos = j.position + valeurCarte;
                CaseIHM.get(newPos).updateEtat(1);
                System.out.println("peut reculer en " + newPos);
            }
            newPos = j.position - valeurCarte;
            if(newPos >= 0){
                if(newPos == joueur1.position ){
                    System.out.println("peut attaquer le joueur avec carte " + valeurCarte);
                    CaseIHM.get(newPos).updateEtat(2);
                }else if(newPos > joueur1.position){
                    System.out.println("peut avancer en " + newPos);
                    if(peutAttaquer(carte, newPos, j)){

                    };
                    CaseIHM.get(newPos).updateEtat(1);

                }else{
                    System.out.println("bloqué par joueur");
                }
            }
        }
    }

    public boolean peutAttaquer(CarteIHM carte,int pos, JoueurHumain j){ // regarde si la carte selectionnée lui permet d'attaquer l'autre joueur
        int distance;
        if(j.getDirection()==1){
            distance = joueur2.getPosition() - pos;
        }else{
            distance =  pos - joueur1.getPosition();
        }
            System.out.println("distance : " + distance);
            if(distance > 5){
                System.out.println("Trop loin pour attaquer");
                return false;
            }else{
                int nbAtk = 0;
                int valCarte = 0;
                if(carte.getValeur() == distance){
                    nbAtk++;
                }
                for(int i=0;i<5;i++){
                    if(i != carte.getId()){
                        if(distance == j.main.get(i)){
                            valCarte = j.main.get(i);
                            nbAtk++;
                        }
                    }
                }
                if(valCarte != 0){
                    System.out.println("Peut attaquer avec la carte " + valCarte + " en " + nbAtk + " exemplaires !");
                    return true;
                }else{
                    System.out.println("N'a pas de carte pour attaquer");
                    return false;
                }
            }

    }

    public void jouerCoup(Coup cp) {
        cp.fixerManche(this);
        nouveau(cp);

        for(int i = 0; i < partie.jeu.selectedCarte.size(); i++)
        {
            partie.Joueur(tourJoueur).supprMain(partie.jeu.selectedCarte.get(i).getId());
        }

        //efface les cases select
        updateAll();
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
        Coup coupCourant = new Coup(grilleJeu, action);
        coupCourant.fixerManche(this);

        int oldPosJ1 = this.joueur1.getPosition();
        int oldPosJ2 = this.joueur2.getPosition();


        //_____________________  Recupérer le joueur courant



        joueurCourant = partie.Joueur(tourJoueur);

        if(typeAction == 1)
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

            attaque(tourJoueur);
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

    public void initButtonChangeTour(int x, int y, int largeur, int hauteur)
    {
       boutonChangeTour = new ButtonIHM(1, "ChangeTour", x, y, largeur, hauteur);
    }
    public void updateCaseIHM(int i, int val, int x, int y, int largeur, int hauteur){
        CaseIHM.get(i).update(i, val, x, y, largeur, hauteur);
    }

    public ArrayList<SelectionCaseIHM> getCaseIHM() {
        return CaseIHM;
    }

    public int getTourJoueur(){ return tourJoueur;}

    public void changeTourJoueur(int tour)
    {
        Coup coupPrecedent = coupPrecedent();

        if(coupPrecedent != null)
        {
            System.out.println("Id coup precedent : " + coupPrecedent.action.id);
            if(coupPrecedent.action.id == 2)
            {
                int nbCartes = 0;
                for(int j = 0; j<coupPrecedent.action.valeurs.length; j++)
                {
                    if(coupPrecedent.action.valeurs[j] != 0)
                    {
                        nbCartes++;
                    }
                }
                System.out.println("Vous devez parer avec "+ nbCartes + " carte(s) de valeur : " + coupPrecedent.action.valeurs[0]);
            }
        }

        if(tour == 1)
        {
                remplirMain(joueur1);
                this.tourJoueur = 2;
        }
        else
        {
                remplirMain(joueur2);
                this.tourJoueur = 1;

        }
    }

    public void updateAll()
    {
        for (int i = 0; i < CaseIHM.size(); i++){
            CaseIHM.get(i).updateEtat(0);
        }
    }

}
