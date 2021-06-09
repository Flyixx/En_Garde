package Modele;
import java.util.*;

import Controller.*;
import Vue.ButtonIHM;

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
    int nbCoupsIA;
    Coup coupIndiIA = null;

    public int[] etatCasesIHM;


    public Manche(Partie p, int premierTourPrecedent){
        doitParer = false;
        peutSauvegarderEtHistorique = true;
        partie = p;

        etatCasesIHM = new int[23];

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
        //System.out.println("Main joueur1 :" + joueur1.getMain());
        viderMain(joueur2);
        //System.out.println("Main joueur2 :" + joueur2.getMain());
        remplirMain(joueur1);
        remplirMain(joueur2);
        //System.out.println("Pioche complete : " + piocheCartes);

        coupsTour = new ArrayList<>();
        coupsTourTab = new Coup[3];

        tourJoueur = premierTourPrecedent;

    }

    //Constructeur de la manche en cour lors de la sauvegarde
    public Manche(Partie p, int positionJ1, int positionJ2, String pioche, String MainJ1, String MainJ2, int tourCourant, String HistoriqueAnnule, String HistoriqueCoupFait){
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


        /*Chargement de l'historique des coupAnnuler avant la sauvegarde*/
        char HistoAnnule[] = HistoriqueAnnule.toCharArray();
        int tailleHisto = HistoAnnule.length;
        int nbCptAnnule = Character.digit(HistoAnnule[0],10);
        int Avancement = 1;
        if(nbCptAnnule != 0){
            for(int cpt = 0; cpt < nbCptAnnule; cpt++) {
                int typeAction = Character.digit(HistoAnnule[Avancement], 10);
                Coup coupTour[] = new Coup[3];
                Avancement++;
                int nbc = Character.digit(HistoAnnule[Avancement], 10);
                Avancement++;
                int tourJ = Character.digit(HistoAnnule[Avancement],10);
                Avancement++;
                for (int c = 0; c < nbc; c++) {
                    int map[] = new int[23];
                    for (int t = 0; t < 23; t++) {
                        map[t] = Character.digit(HistoAnnule[Avancement], 10);
                        Avancement++;
                    }
                    int typeAction2 = Character.digit(HistoAnnule[Avancement], 10);
                    Avancement++;
                    int[] val = new int[5];
                    for (int v = 0; v < 5; v++) {
                        val[v] = Character.digit(HistoAnnule[Avancement], 10);
                        Avancement++;
                    }
                    String targetS = HistoAnnule[Avancement] + "" + HistoAnnule[Avancement + 1];
                    int target = Integer.parseInt(targetS);
                    Avancement++;
                    Avancement++;
                    Action action = new Action(typeAction2, val);
                    Coup cp = new Coup(map, action, target);
                    cp.manche = this;
                    for(int m1 = 0; m1 < 5; m1++){
                        cp.mainJ1.add(Character.digit(HistoAnnule[Avancement], 10));
                        Avancement++;
                    }
                    for(int m2 = 0; m2 < 5; m2++){
                        cp.mainJ2.add(Character.digit(HistoAnnule[Avancement], 10));
                        Avancement++;
                    }
                    String nbPiocheS = HistoAnnule[Avancement] + "" + HistoAnnule[Avancement + 1];
                    int nbPioche = Integer.parseInt(nbPiocheS);
                    Avancement++;
                    Avancement++;
                    for(int pio = 0; pio < nbPioche; pio++){
                        cp.pioche.add(Character.digit(HistoAnnule[Avancement], 10));
                        Avancement++;
                    }
                    coupTour[c] = cp;
                }
                CoupParTour cpTCF = new CoupParTour(typeAction, null, coupTour, nbc);
                cpTCF.manche = this;
                cpTCF.tourJoueur = tourJ;
                this.CoupFait.insereQueue(cpTCF);
            }
        }

        /*Chargement de l'historique des coupFait avant la sauvegarde*/
        char HistoCF[] = HistoriqueCoupFait.toCharArray();
        int tailleCF = HistoCF.length;
        int AvancementCF = 1;
        int nbcptCF = Character.digit(HistoCF[0],10);
        if(nbcptCF != 0){
            for(int cpt = 0; cpt < nbcptCF; cpt++) {
                int typeActionCF = Character.digit(HistoCF[AvancementCF], 10);
                Coup coupTourCF[] = new Coup[3];
                AvancementCF++;
                int nbcCF = Character.digit(HistoCF[AvancementCF], 10);
                AvancementCF++;
                int tourJ = Character.digit(HistoCF[AvancementCF],10);
                AvancementCF++;
                for (int c = 0; c < nbcCF; c++) {
                    int map[] = new int[23];
                    for (int t = 0; t < 23; t++) {
                        map[t] = Character.digit(HistoCF[AvancementCF], 10);
                        AvancementCF++;
                    }
                    int typeAction2 = Character.digit(HistoCF[AvancementCF], 10);
                    AvancementCF++;
                    int[] val = new int[5];
                    for (int v = 0; v < 5; v++) {
                        val[v] = Character.digit(HistoCF[AvancementCF], 10);
                        AvancementCF++;
                    }
                    String targetS = HistoCF[AvancementCF] + "" + HistoCF[AvancementCF + 1];
                    int target = Integer.parseInt(targetS);
                    AvancementCF++;
                    AvancementCF++;
                    Action action = new Action(typeAction2, val);
                    Coup cp = new Coup(map, action, target);
                    cp.manche = this;
                    for(int m1 = 0; m1 < 5; m1++){
                        cp.mainJ1.add(Character.digit(HistoCF[AvancementCF], 10));
                        AvancementCF++;
                    }
                    for(int m2 = 0; m2 < 5; m2++){
                        cp.mainJ2.add(Character.digit(HistoCF[AvancementCF], 10));
                        AvancementCF++;
                    }
                    String nbPiocheS = HistoCF[AvancementCF] + "" + HistoCF[AvancementCF + 1];
                    int nbPioche = Integer.parseInt(nbPiocheS);
                    AvancementCF++;
                    AvancementCF++;
                    for(int pio = 0; pio < nbPioche; pio++){
                        cp.pioche.add(Character.digit(HistoCF[AvancementCF], 10));
                        AvancementCF++;
                    }
                    coupTourCF[c] = cp;
                }
                CoupParTour cpTCF = new CoupParTour(typeActionCF, null, coupTourCF, nbcCF);
                cpTCF.manche = this;
                cpTCF.tourJoueur = tourJ;
                this.CoupFait.insereQueue(cpTCF);
            }
        }



        //System.out.println("Sequence Annuler : " + this.CoupAnnuler);
        //System.out.println("Sequence Coup Fait : " + this.CoupFait);





        //System.out.println("Pioche complete : " + piocheCartes);

        coupsTour = new ArrayList<>();
        coupsTourTab = new Coup[3];

        //Mise en place du tour
        tourJoueur = tourCourant;

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
        //System.out.println("Pioche non triee : " + piocheCartes);
        Collections.shuffle(piocheCartes);
        //System.out.println("Pioche melangee :" + piocheCartes);

    }

    public void attaque(int j){
        //System.out.println("joueur : " + j);

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
        //System.out.println("main complete joueur " + inverse() + " : " + j.main);
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
            System.out.println("Pioche vide: Evaluation du joueur gagnant:");
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
            } else {
                int PosJ1 = joueur1.getPosition();
                int PosJ2 = NOMBRE_CASES - joueur2.getPosition();

                if (PosJ1 > PosJ2){
                    attaque(2);
                    System.out.println("Joueur 1 gagne: Joueur le plus avancé");
                    partie.initialiseManche();
                } else {
                    attaque(1);
                    System.out.println("Joueur 2 gagne: Joueur le plus avancé");
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

            if(valeurCarte == 0)
            {
                valeurCarte = j.main.get(cartes.get(0).getId());
            }
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
            int[] dejaCalcule = new int[6];

            for(int i  = 0; i< j.getMain().size(); i++)
            {

                int valeurCarte =  j.getMain().get(i);

                if(valeurCarte != 0 && dejaCalcule[valeurCarte]!=1)
                {
                    dejaCalcule[valeurCarte] = 1;
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
                        if(newPos >= 0) {
                            if (newPos == joueur1.position) {
                                int nbAttks = peutAttaquer(valeurCarte, distance, j);
                                //System.out.println("Nb attaques possibles :" + nbAttks);
                                for (int k = 1; k <= nbAttks; k++) {
                                    int[] valeurs = new int[5];

                                    for (int l = 0; l < k; l++)
                                        valeurs[l] = valeurCarte;

                                    Action ac = new Action(2, valeurs);
                                    int target = joueur1.getPosition();
                                    Coup cp = new Coup(grilleJeu, ac, target);
                                    cp.fixerManche(this);
                                    coups.add(cp);
                                }
                                //System.out.println("peut attaquer le joueur avec carte " + valeurCarte);
                            } else if (newPos > joueur1.position && peutseDeplacer) {

                                int[] valeurs = new int[5];
                                valeurs[0] = valeurCarte;
                                Action ac = new Action(1, valeurs);
                                Coup cp = new Coup(grilleJeu, ac, newPos);
                                cp.fixerManche(this);
                                coups.add(cp);
                                //System.out.println("peut avancer en " + newPos);
                                //CaseIHM.get(newPos).updateEtat(1);

                            }

                        }
                    } else if (dir == 1){
                        if(j.position - valeurCarte > 0 && peutseDeplacer){
                            newPos = j.position - valeurCarte;

                            int[] valeurs = new int[5];
                            valeurs[0] = valeurCarte;
                            Action ac = new Action(1,valeurs);
                            Coup cp = new Coup(grilleJeu, ac, newPos);
                            cp.fixerManche(this);
                            coups.add(cp);

                            //System.out.println("peut reculer en " + newPos);
                        }
                        newPos = j.position + valeurCarte;
                        if(newPos <= 22) {
                            if (newPos == joueur2.position) {
                                int nbAttks = peutAttaquer(valeurCarte, distance, j);
                                //System.out.println("Nb attaques possibles :" + nbAttks);
                                for (int k = 1; k <= nbAttks; k++) {
                                    int[] valeurs = new int[5];

                                    for (int l = 0; l < k; l++)
                                        valeurs[l] = valeurCarte;

                                    Action ac = new Action(2, valeurs);
                                    int target = joueur2.getPosition();
                                    Coup cp = new Coup(grilleJeu, ac, target);
                                    cp.fixerManche(this);
                                    coups.add(cp);
                                }
                                //System.out.println("peut attaquer le joueur avec carte " + valeurCarte);
                            } else if (newPos < joueur2.position && peutseDeplacer) {

                                int[] valeurs = new int[5];
                                valeurs[0] = valeurCarte;
                                Action ac = new Action(1, valeurs);
                                Coup cp = new Coup(grilleJeu, ac, newPos);
                                cp.fixerManche(this);
                                coups.add(cp);
                                //System.out.println("peut avancer en " + newPos);
                                //CaseIHM.get(newPos).updateEtat(1);

                            }

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

            if(distance > 5){
                //System.out.println("Trop loin pour attaquer");
                return 0;
            }else{
                int nbAtk = 0;
                int valCarte = 0;

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

    public void jouerCoup(Coup cp, boolean refaire) {
        peutSauvegarderEtHistorique = false;

        if(coupsTourTab[0] == null && (cp.action.id == 3 || cp.action.id == 4))
        {
            for(int j = 0; j < CaseIHM.size(); j++)
            {
                SelectionCaseIHM b = CaseIHM.get(j);
                etatCasesIHM[j] = b.getEtat();
            }
        }

        //System.out.println("je fais jouer coup !");
        //efface les cases select
        updateAll();

        nbCoupsJoues++;
        cp.fixerManche(this);
        //nouveau(cp);

        //on supprime de la main du joueur toutes les cartes selectionnees pour jouer
        if(cp.action.id != PARADE_DIRECTE && cp.action.id!=PARADE_INDIRECTE)
        {
            if((partie.type == 2 && tourJoueur == 2) || partie.type == 4 || (partie.type == 3 && tourJoueur == 2))
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
        if(cp.action.id == PARADE_DIRECTE || refaire)
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

                if(!refaire)
                {
                    changeTourJoueur(false);
                }

            }
            else if(cp.action.id == PARADE_INDIRECTE)
            {
                if(!refaire)
                {
                    changeTourJoueur(false);
                }
            }
        }
        else if(coupsTourTab[1] == null)
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

                if(!refaire)
                {
                    changeTourJoueur(false);
                }

            }
            else if(coupsTourTab[0].action.id != PARADE_DIRECTE){

                if(!refaire)
                {
                    changeTourJoueur(false);
                }
            }
        }
        else // Quand le joueur a joué 3 coups
        {
            coupsTourTab[2] = cp;

            if(!refaire)
            {
                changeTourJoueur(false);
            }
        }

    }

    public void miseAJourGrille(int oldPosJ1, int oldPosJ2, int posJ1, int posJ2){
        // Les conditions sont codées de manière à ce que si un joueur bouge, l'autre ne bouge pas
        // Si les deux conditions ne sont pas respectées, cela veut dire qu'aucun joueur n'a bougé
        if(posJ1 != oldPosJ1){
            grilleJeu[oldPosJ1] = 0;
            grilleJeu[posJ1] = 1;
        } else if (posJ2 != oldPosJ2) {
            grilleJeu[oldPosJ2] = 0;
            grilleJeu[posJ2] = 2;
        } else {
            //System.out.println("Aucun joueur n'a bougé");
        }
    }

    public Coup joue(int target, int[] valeurs, int[] GrrilleJeu, int typeAction){
        JoueurHumain joueurCourant;
        Action action = new Action(typeAction, valeurs);

        int[] grilleJeu2 = new int[NOMBRE_CASES];
        for(int i = 0; i<NOMBRE_CASES; i++)
        {
            grilleJeu2[i] = grilleJeu[i];
        }
        Coup coupCourant = new Coup(grilleJeu2, action, target);

        for(int i = 0; i<5; i++)
        {
            coupCourant.mainJ1.add(joueur1.getMain().get(i));
            coupCourant.mainJ2.add(joueur2.getMain().get(i));
        }

        for(int i = 0; i<piocheCartes.size(); i++)
        {
            coupCourant.pioche.add( piocheCartes.get(i));

        }

        coupCourant.fixerManche(this);

        int oldPosJ1 = this.joueur1.getPosition();
        int oldPosJ2 = this.joueur2.getPosition();

        //_____________________  Recupérer le joueur courant



        joueurCourant = partie.Joueur(tourJoueur);

        if(typeAction == AVANCER || (typeAction == PARADE_INDIRECTE && doitParer))
        {
            joueurCourant.deplace(target);
            int nb2;

            if (tourJoueur == 1) {
                this.joueur1 = joueurCourant;
                nb2 = Math.abs(target-oldPosJ1);
            } else {
                this.joueur2 = joueurCourant;
                nb2 = Math.abs(target-oldPosJ2);
            }

            if(typeAction == AVANCER){
                partie.jeu.control.inter().niv().modifMessage(0, tourJoueur, nb2, 0);
                partie.jeu.control.inter().niv().msg = 2;
                partie.jeu.control.inter().niv().msg2 = 0;
            }else if(typeAction == PARADE_INDIRECTE && doitParer){
                partie.jeu.control.inter().niv().modifMessage(6, tourJoueur, nb2, 0);
                partie.jeu.control.inter().niv().msg = 4;
                partie.jeu.control.inter().niv().modifMessage(3, tourJoueur, 0, 0);
                partie.jeu.control.inter().niv().msg2 = 2;
            }


            miseAJourGrille(oldPosJ1, oldPosJ2, this.joueur1.getPosition(), this.joueur2.getPosition());
            coupCourant.mapAvant[oldPosJ1] = 1;
            coupCourant.mapAvant[oldPosJ2] = 2;

        }
        else{
            int nbCartes = 0;
            for(int i = 0; i < 5; i++){
                if(coupCourant.action.valeurs[i] != 0){
                    nbCartes++;
                }
            }
            if(typeAction == ATTAQUER){
                partie.jeu.control.inter().niv().modifMessage(1, getTourJoueur(), coupCourant.action.valeurs[0], nbCartes);
                partie.jeu.control.inter().niv().msg = 2;
            }
        }


        return coupCourant;
    }

    public void initCaseIHM(int i, int val, int x, int y, int largeur, int hauteur, int etat){
        SelectionCaseIHM caseI = new SelectionCaseIHM(i, val, x, y, largeur, hauteur, etat);
        CaseIHM.add(caseI);
    }

    public void initButtonChangeTour(int x, int y, int largeur, int hauteur) {
       boutonChangeTour = new ButtonIHM(1, "ChangeTour", x, y, largeur, hauteur);
    }

    public void updateCaseIHM(int i, int val, int x, int y, int largeur, int hauteur, int recadrage){
        CaseIHM.get(i).update(i, val, x, y, largeur, hauteur,recadrage);
    }

    public ArrayList<SelectionCaseIHM> getCaseIHM() {
        return CaseIHM;
    }

    public int getTourJoueur(){ return tourJoueur;}

    // Changement du tour du joueur
    // Cette fonction enregistre les coups du joueur précédent dans un CoupParTour avant de changer le
    // tour du joueur
    public void changeTourJoueur(boolean refait) {
        nbCoupsIA = 0;
        //System.out.println("Je change le tour");
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
            boolean devaitParer = false;
            if(coupsTourTab[0].action.id == 4 || coupsTourTab[0].action.id == 3)
            {
                 devaitParer = true;
            }

            //Si le premier coup joué n'est pas une parade
            if(coupsTourTab[0].action.id != 4) {
                if (/*coupsTour.size() == 1 && coupsTour.get(coupsTour.size()-1).action.id == 1*/coupsTourTab[1] == null && coupsTourTab[0].action.id == 1) {

                    //Simple deplacement
                    coupTour = new CoupParTour(1, coupsTour, coupsTourTab,nbCoupsJoues,devaitParer, etatCasesIHM);

                } else if (/*coupsTour.size() == 1 && coupsTour.get(coupsTour.size() - 1).action.id == 2*/ coupsTourTab[1] == null && coupsTourTab[0].action.id == 2) {

                    //Attaque directe
                    coupTour = new CoupParTour(2, coupsTour, coupsTourTab, nbCoupsJoues, devaitParer, etatCasesIHM);

                } else if (/*coupsTour.size() == 2 && coupsTour.get(coupsTour.size() - 1).action.id == 2*/ coupsTourTab[1] != null && coupsTourTab[1].action.id == 2) {

                    //Attaque indirecte
                    coupTour = new CoupParTour(3, coupsTour, coupsTourTab, nbCoupsJoues, devaitParer, etatCasesIHM);
                }
                else
                {
                    // Simple parade d'une attaque indirecte
                    coupTour = new CoupParTour(4, coupsTour, coupsTourTab, nbCoupsJoues, devaitParer, etatCasesIHM);
                }
            }
            else
            {
                if(coupsTourTab[1] == null)
                {
                    coupTour = new CoupParTour(4, coupsTour, coupsTourTab, nbCoupsJoues, devaitParer, etatCasesIHM);
                }
                else if (/*coupsTour.size() == 1 && coupsTour.get(coupsTour.size()-1).action.id == 1*/coupsTourTab[2] == null && coupsTourTab[1].action.id == 1) {

                    //Déplacement après une parade directe
                    coupTour = new CoupParTour(1, coupsTour, coupsTourTab, nbCoupsJoues, devaitParer, etatCasesIHM);

                } else if (/*coupsTour.size() == 1 && coupsTour.get(coupsTour.size() - 1).action.id == 2*/ coupsTourTab[2] == null && coupsTourTab[1].action.id == 2) {

                    //Attaque directe après une parade directe
                    coupTour = new CoupParTour(2, coupsTour, coupsTourTab, nbCoupsJoues, devaitParer, etatCasesIHM);

                } else if (/*coupsTour.size() == 2 && coupsTour.get(coupsTour.size() - 1).action.id == 2*/ coupsTourTab[2] != null && coupsTourTab[2].action.id == 2) {

                    //Attaque indirecte après une parade directe
                    coupTour = new CoupParTour(3, coupsTour, coupsTourTab, nbCoupsJoues, devaitParer, etatCasesIHM);
                }
                else
                {
                    //Rien après une parade
                    coupTour = new CoupParTour(4, coupsTour, coupsTourTab, nbCoupsJoues, devaitParer, etatCasesIHM);
                }
            }
        }

        etatCasesIHM = new int[23];

        // Enregistrement du coupTour dans l'historique
        if(coupTour != null && !refait)
        {
            coupTour.tourJoueur = getTourJoueur();
            coupTour.fixerManche(this);
            nouveau(coupTour);
        }

        // Récupération du CoupTour joué par le joueur adverse : il permet de récupérer les actions
        // effectuées par le joueur adverse au tour précédent
        CoupParTour coupPrecedent = coupPrecedent();

        //System.out.println("Coup précédent : " + coupPrecedent);

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

        boolean test;

        if(partie.type == 2 && tourJoueur == 2 && coupPrecedent.typeAction == 3)
        {
            remplirMain(joueur1);
        }

        if(coupPrecedent!= null && coupPrecedent.tourJoueur != tourJoueur)
        {
            if(joueurcourant.carteI.size() > 0)
            {
                if((partie.type != 2 || getTourJoueur() != 2) && partie.type!=4)
                {
                    test = TestProchainCoup(coupPrecedent);
                }
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

                if((partie.type != 2 || getTourJoueur() != 2) && partie.type!=4)
                {
                    test = TestProchainCoup(coupPrecedent);
                }

            }
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

            /*// gestion IA
            if(partie.type == 2){
                System.out.println("Joueur 2 est IA ");

                if(test)
                {
                    //jouerIA(joueur2);
                }

                //this.tourJoueur = 1;
            }*/

            //System.out.println("Cartes restantes pioche : " + this.restantPioche());

        }
        peutSauvegarderEtHistorique = true;
    }

    public void jouerIA(JoueurHumain j){
        if(j.getMain().size() > 0){
            for (int i = 0; i < j.getMain().size(); i++) {
                j.getCarteI().get(i).setEtat(1);
            }
        }

        boolean test = true;
        if(nbCoupsJoues == 0 && nbCoupsIA == 0 )
        {
            TestProchainCoup(coupPrecedent());
            nbCoupsIA++;
        }
        else if(nbCoupsJoues <2 || coupsTourTab[0].action.id == 4)
        {
            //System.out.println("Tour de l'IA ! ");
            ArrayList<Coup> coups = this.listerCoupIA(j);
            //System.out.println("tous les coups possibles : " + coups);
            Random rnd = new Random();
            int total = coups.size();
            int random = 0;
            if(total != 0){
                random = rnd.nextInt(total);
            }
            //System.out.println("total : " + total + " choisi : " + random);

            if(coups.size() > 0)
            {
                Coup cp = coups.get(random);
                //System.out.println("selection du coup : " + cp );
                cp = joue(cp.target, cp.action.valeurs, cp.mapAvant, cp.action.id);
                partie.jeu.jouerCoup(cp, false);
                int nbCartes = 0;
                for(int i = 0; i < 5; i++){
                    if(cp.action.valeurs[i] != 0){
                        nbCartes++;
                    }
                }
                int tour;
                if(getTourJoueur() == 1){
                    tour = 2;
                }else{
                    tour = 1;
                }

                if(cp.action.id == AVANCER){
                    partie.jeu.control.inter().niv().modifMessage(0, tourJoueur, cp.action.valeurs[0], 0);
                    partie.jeu.control.inter().niv().msg = 2;
                    partie.jeu.control.inter().niv().msg2 = 0;
                }else if(cp.action.id == PARADE_INDIRECTE){
                    partie.jeu.control.inter().niv().modifMessage(6, tourJoueur, cp.action.valeurs[0], 0);
                    partie.jeu.control.inter().niv().msg = 4;
                    partie.jeu.control.inter().niv().modifMessage(3, tour, 0, 0);
                    partie.jeu.control.inter().niv().msg2 = 2;
                }else if(cp.action.id == ATTAQUER){
                    partie.jeu.control.inter().niv().modifMessage(1, tour, cp.action.valeurs[0], nbCartes);
                    partie.jeu.control.inter().niv().msg = 2;
                }
            }
            else
            {
                partie.jeu.control.inter().niv().modifMessage(3, tourJoueur, 0,0);
                partie.jeu.control.inter().niv().msg2 = 2;
                changeTourJoueur(false);
            }

        }


           /* coups = this.listerCoupIA(j);



            if(coups.size() != 0 && getTourJoueur() == 2){
                System.out.println("IA: Attaque indirecte ");
                jouerIA(j, null);
            }
            else if(getTourJoueur() == 2)
            {

                changeTourJoueur();
            }*/




    }

    public void jouerIAMoyen(JoueurHumain j){
        for (int i = 0; i < j.getMain().size(); i++) {
           j.getCarteI().get(i).setEtat(1);
        }
        boolean test = true;
        if(nbCoupsJoues == 0 && nbCoupsIA == 0 )
        {
            TestProchainCoup(coupPrecedent());
            nbCoupsIA++;
        }
        else if(nbCoupsJoues <2 || coupsTourTab[0].action.id == 4) {
            //System.out.println("Tour de l'IA ! ");
            ArrayList<Coup> coups = this.listerCoupIA(j);
            ArrayList<Coup> coupsChoisi = new ArrayList<>();
            ArrayList<Coup> coupsAttaqueIndirecte = new ArrayList<>();
            //System.out.println("tous les coups possibles : " + coups);

            for (int i = 0; i < coups.size(); i++) {
                if (coups.get(i).action.id == 2) {
                    coupsChoisi.add(coups.get(i));
                } else if (coups.get(i).action.id == 1) {
                    coupsAttaqueIndirecte.add(coups.get(i));
                }
            }

            if (coups.size() > 0 && getTourJoueur() == 2) {
                int random = 0;
                Random rnd = new Random();
                Coup cp = null;
                Coup cpAI = null;


                if (coupsChoisi.size() != 0) { //Si possibilité d'attaque
                    random = rnd.nextInt(coupsChoisi.size()) + 1;
                    cp = coupsChoisi.get(random - 1);
                } else if (coupsAttaqueIndirecte.size() != 0) { //Si possibilité d'attaque indirecte
                    for (int i = 0; i < coupsAttaqueIndirecte.size(); i++) {
                        for (int y = 0; y < j.getMain().size(); y++) {
                            cp = coupsAttaqueIndirecte.get(i);
                            if ((coupsAttaqueIndirecte.get(i).action.valeurs[0] + j.getMain().get(y)) == getDistance()) { //test Les attaques indirectes, prend la premiere trouvé

                                int[] valeurs = new int[5];
                                valeurs[0] = j.getMain().get(y);

                                Action ac = new Action(2, valeurs);
                                int target = joueur1.getPosition();
                                cpAI = new Coup(grilleJeu, ac, target);
                                cpAI.fixerManche(this);

                                break;

                            }
                        }
                    }
                } else { //Si cest un déplacement
                    random = rnd.nextInt(coups.size()) + 1;
                    cp = coups.get(random - 1);
                }



                if (coupIndiIA != null) {
                    //System.out.println("COUPS INDIRECTE !!!!!!!!!" + coupIndiIA);
                    coupIndiIA = joue(coupIndiIA.target, coupIndiIA.action.valeurs, coupIndiIA.mapAvant, coupIndiIA.action.id);
                    partie.jeu.jouerCoup(coupIndiIA,false);
                    coupIndiIA = null;
                }else {
                    //System.out.println("Main j2" + j.getMain() );
                    //System.out.println("DEPLACEMENT *************" + cp);
                    cp = joue(cp.target, cp.action.valeurs, cp.mapAvant, cp.action.id);
                    partie.jeu.jouerCoup(cp,false);
                    coupIndiIA = cpAI;
                }

            } else {

                changeTourJoueur(false);
            }
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
    public boolean TestProchainCoup(CoupParTour coupPrecedent) {
        JoueurHumain joueurcourant = Joueur(tourJoueur);
        boolean poss = false;

        if(joueurcourant.carteI.size()!= 0)
        {
            for(int i = 0; i<joueurcourant.carteI.size(); i++)
            {
                ArrayList<CarteIHM> liste = new ArrayList();
                liste.add(joueurcourant.carteI.get(i));
                if(listerCoups(joueurcourant,liste, false))
                {
                    poss = true;
                }
            }
        }
        else {
            poss = true;
        }


        //System.out.println("Possibilites : " + poss);

        if(!poss)
        {
            partie.Joueur(tourJoueur).vie -= 1;
            partie.initialiseManche();
        }
        else
        {
            if(coupPrecedent != null)
            {
                // Si le CoupParTour est un simple déplacement
                if(coupPrecedent.typeAction == 1)
                {
                    //System.out.println("coup precedent: Le joueur adverse a juste avancé/reculé");
                    return true;

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

                    int tour;
                    if(getTourJoueur() == 1){
                        tour = 2;
                    }else{
                        tour = 1;
                    }
                    // Si le CoupParTour est une attaque directe
                    if(coupPrecedent.typeAction == 2)
                    {
                        //System.out.println("coup precedent: Le joueur adverse a effectué une attaque directe");
                        //partie.jeu.control.inter().niv().modifMessage(1, tour, cp.action.valeurs[0], nbCartes);
                        //partie.jeu.control.inter().niv().msg = 2;
                        if(ParerAttaqueDirecte(coupPrecedent, nbCartes))
                        {
                            partie.jeu.control.inter().niv().modifMessage(8, getTourJoueur(), cp.action.valeurs[0], nbCartes);
                            partie.jeu.control.inter().niv().msg2 = 4;
                            return true;
                        }
                        else
                        {
                            partie.jeu.control.inter().niv().modifMessage(4, getTourJoueur(), partie.Joueur(getTourJoueur()).vie, 0);
                            partie.jeu.control.inter().niv().msg2 = 3;
                            return false;
                        }
                        //System.out.println("Vous devez parer avec "+ nbCartes + " carte(s) de valeur : " + cp.action.valeurs[0]);
                    }
                    else // Si le CoupParTour est une attaque indirecte
                    {
                        partie.jeu.control.inter().niv().modifMessage(5, tour, cp.action.valeurs[0], nbCartes);
                        partie.jeu.control.inter().niv().msg = 3;
                        //System.out.println("coup precedent: Le joueur adverse a effectué une attaque indirecte");
                        if(ParerAttaqueIndirecte(coupPrecedent, nbCartes))
                        {
                            //System.out.println("PARADE INDIRECTE");
                            partie.jeu.control.inter().niv().modifMessage(8, getTourJoueur(), cp.action.valeurs[0], nbCartes);
                            partie.jeu.control.inter().niv().msg2 = 4;
                            return true;
                        }
                        else
                        {
                            //System.out.println("PAS PARADE INDIRECTE");
                            partie.jeu.control.inter().niv().modifMessage(9, getTourJoueur(), cp.action.valeurs[0], nbCartes);
                            partie.jeu.control.inter().niv().msg2 = 4;
                            return false;
                        }
                    }
                }

            }
        }


        return true;
    }

    public boolean peutParerDirectement(CoupParTour coupPrecedent, int nbCartes) {
        //Si le nombre de cartes de l'attaque précédente est supérieur à 2, il est impossible de parer
        if(nbCartes > 2)
        {
            //System.out.println("Je ne peux pas parer directement");
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
                //System.out.println("je peux parer directement!");
                return true;
            }
            else
            {
                return false;
            }
        }
    }

    //Parer une attaque directe
    public boolean ParerAttaqueDirecte(CoupParTour coupPrecedent, int nbCartes)
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
            if(partie.type == 2 && tourJoueur==2 )
            {
                parerDirectement();
            }
            else
            {
                CaseIHM.get(pos).updateEtat(3);
                doitParer = true;
            }
            return true;

        }
        else // Sinon, le joueur subit une attaque et perd un point de vie et engendre une nouvelle manche
        {
            attaque(tourJoueur);
            partie.initialiseManche();
            return false;
        }
    }

    // Parer une attaque indirecte (déplacement + attaque)
    public boolean ParerAttaqueIndirecte(CoupParTour coupPrecedent, int nbCartes)
    {
        boolean test = false;
        int valeurRecherchee = coupPrecedent.coupsTourTab[0].action.valeurs[0];
        ArrayList<Coup> coupsParadeIA = new ArrayList<Coup>();
        // Si le joueur peut parer directement (c'est à dire sans reculer)
        if(peutParerDirectement(coupPrecedent, nbCartes))
        {

            int pos;
            if(getTourJoueur() == 1){
                pos = joueur2.getPosition();
            } else {
                pos = joueur1.getPosition();
            }

            if(partie.type == 2 && tourJoueur == 2)
            {
                int[] valeurs = new int[5];

                for(int i= 0; i<nbCartes; i++)
                {
                    valeurs[i] = valeurRecherchee;
                }
                Action ac = new Action(PARADE_DIRECTE, valeurs);
                Coup cp = new Coup(grilleJeu, ac, pos);
                coupsParadeIA.add(cp);
            }
            else
            {
                // On change l'etat de la case IHM à la position du joueur adverse :
                // Elle passe à l'etat 3 ce qui signifie que si on clique sur cette case on effectue
                // une parade directe
                CaseIHM.get(pos).updateEtat(3);
            }

        }
        else if(!peutReculer() && !peutParerDirectement(coupPrecedent, nbCartes)){
            // Si le joueur ne peut pas reculer ni parer directement, le joueur subit une attaque
            // et perd une vie --> nouvelle manche
            attaque(tourJoueur);
            partie.initialiseManche();
            return false;
        }
        JoueurHumain j = Joueur(getTourJoueur());
        //System.out.println("tour:" + getTourJoueur());
        for(int i = 0; i < j.getMain().size(); i++){
            int target;
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

                if(target <=22)
                {

                    if(partie.type == 2)
                    {
                        int[] valeurs = new int[5];
                        valeurs[0] = target - j.position;
                        Action ac = new Action(PARADE_INDIRECTE, valeurs);
                        Coup cp = new Coup(grilleJeu, ac, target);
                        coupsParadeIA.add(cp);
                    }
                    else
                    {
                        CaseIHM.get(target).updateEtat(1);
                    }

                }
            }
        }
        test =true;


        doitParer = true;
        peutSauvegarderEtHistorique = false;

        if(partie.type == 2 && getTourJoueur() == 2)
        {
            Random rnd = new Random();
            int total = coupsParadeIA.size();
            int random = 0;
            if(total != 0){
                random = rnd.nextInt(total)+1;
            }

            Coup cp = coupsParadeIA.get(random-1);
            //System.out.println("Tous les coups possibles parades indirecte IA :" +coupsParadeIA);
            //System.out.println("Coup choisi parade Indirecte IA :" + cp);
            if(cp.action.id == 4)
            {
                test = true;
            }
            cp = joue(cp.target, cp.action.valeurs, cp.mapAvant, cp.action.id);
            partie.jeu.jouerCoup(cp, false);
            doitParer = false;
            peutSauvegarderEtHistorique = true;


        }
        return test;


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
                if(joueurcourant.position + joueurcourant.main.get(i) <23)
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

        JoueurHumain joueuradverse;
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
        partie.jeu.jouerCoup(cp, false);
        //System.out.println("j'ai paré !");
        //System.out.println("Ma main après avoir parer :" + Joueur(tourJoueur).main);

        if(coupPrecedent.typeAction == PARADE_INDIRECTE){
            partie.jeu.control.inter().niv().modifMessage(7, tourJoueur, 0, 0);
            partie.jeu.control.inter().niv().msg = 5;
            int tour;
            if(tourJoueur == 1){
                tour = 2;
            }else{
                tour = 1;
            }
            partie.jeu.control.inter().niv().modifMessage(3, tour, 0, 0);
            partie.jeu.control.inter().niv().msg2 = 2;
        }else{
            partie.jeu.control.inter().niv().modifMessage(2, tourJoueur, 0, 0);
            partie.jeu.control.inter().niv().msg2 = 1;
            if(tourJoueur == 1){
                partie.jeu.control.inter().niv().action2 = 0;
            }else{
                partie.jeu.control.inter().niv().action1 = 0;
            }
        }



    }

    public Coup RevenirCoup(Coup cp){
        Coup cp1 = joue(cp.target, cp.action.valeurs, cp.mapAvant, cp.action.id);
        partie.jeu.jouerCoup(cp1, true);

        if(cp1.action.id == 4 || cp1.action.id == 3)
        {
            doitParer = false;
        }
        /*this.joueur1.main = cp.mainJ1;
        this.joueur2.main = cp.mainJ2;
        this.piocheCartes = cp.pioche;*/
        //System.out.println("Taille de la pioche :" + piocheCartes.size());

        return cp1;
    }

    public void InverseCoup(Coup cp, int tour, boolean DevaitParer, int[] etatCASESIHM){
        doitParer = DevaitParer;
        this.grilleJeu = cp.mapAvant;

        for(int i = 0; i<23; i++){
            if(this.grilleJeu[i] == 1){
                this.joueur1.position = i;
            }else if(this.grilleJeu[i] == 2){
                this.joueur2.position = i;
            }

            CaseIHM.get(i).etat = etatCASESIHM[i];
        }
        this.joueur1.main = cp.mainJ1;
        this.joueur2.main = cp.mainJ2;
        this.piocheCartes = cp.pioche;
        this.tourJoueur = tour;
        //System.out.println("Taille de la pioche :" + piocheCartes.size());
        //System.out.println("COUP INVERSE");
    }

    @Override
    CoupParTour refaire(){
        if(peutRefaire()){
            CoupParTour c = CoupAnnuler.extraitTete();
            CoupParTour cp1 = c.execute(c);
            CoupFait.insereTete(cp1);
            changeTourJoueur(true);
            return cp1;
        }else{
            return null;
        }
    }
}
