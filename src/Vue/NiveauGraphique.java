package Vue;

import Controller.JoueurHumain;
import Modele.Jeu;
import Modele.SelectionCaseIHM;
import Patterns.Observateur;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

public class NiveauGraphique extends JComponent implements Observateur {
    private static final int NB_MESSAGE = 6;
    Jeu jeu;
    Image revenirALaPartie, menuPartie, fondMenuPartie, muteIm, unMute, victoireName, annuler, refaire, quitter, save, fin, fondJoueur, flecheDroit, flecheGauche, fondMenu, fond, fondNewPartie, joueur1, joueur1Choix, joueur2Choix, joueur2, sol, map, teteJ1, teteJ2, TiretBleu, TiretRouge, NomJ1, NomJ2, carte1, carte2, carte3, carte4, carte5, carte0, carte1_select, carte2_select, carte3_select, carte4_select, carte5_select;
    int y;
    int joueur2Vie;
    int joueur1Vie;
    public int action1;
    public int action2;
    int EspacementTiret;
    int dimensionTete;
    int xTeteDroite;
    int xTeteGauche;
    int yTete;
    int etape;
    int largeur;
    int hauteur;
    int nbColonnes;
    int largeurCase;
    int hauteurNom;
    int largeurNom;
    int hauteurCase;
    int hauteurLuke;
    int hauteurVador;
    int largeurVador;
    int yPoint;
    int xPointGauche;
    int xPointDroit;
    int hauteurTiret;
    int largeurTiret;
    int yNom;
    int xNom;
    public int hauteurBoutonMenu, largeurBoutonMenu, yBoutonUn, yBoutonDeux, xBoutonMenu, yBoutonTrois, tailleMute, xBoutonMute, yBoutonMute, yBoutonVictoire, tailleBouton, xBouton1, yBouton, xBouton2, xBouton3, xBouton4, xBouton5, xBouton6, compteurJ1, compteurJ2, compteurMap, largeurBouton, hauteurBouton, xBoutonDroite, xBoutonGauche, yBoutonMilieu, yBoutonBas, yBoutonMilieu2, yBoutonHaut;
    Image[][] joueurs1;
    Image[][] joueurs2;
    Image[][] BandeVie;
    Image carte1_disabled, carte2_disabled, carte3_disabled, carte4_disabled, carte5_disabled, cartePioche;
    Image ButtonChangeTour;
    Random r;
    Clip clip;
    FloatControl gainControl;
    Graphics2D drawable;
    public boolean mute, Victoire, VictoireSet, Menu, Partie, Regles, NewPartie, PartieSet, MenuSet, ReglesSet, NewPartieSet, MenuPartieSet, MenuPartie;
    public int compteur, msg, msg2;
    Image[] cartes = {};
    Image[] cartesSel = {};
    Image[] cartesDisabled = {};
    String[] Message, Message2;

    //Fonction Permettant de charger une image
    public Image chargeImage(String nom){
        Image img = null;
        try{
            InputStream in = ClassLoader.getSystemClassLoader().getResourceAsStream(nom + ".png");
            if(in != null){
                img = ImageIO.read(in);
            }
        }catch(Exception e ){
            System.err.println("Erreur lors du chargement de l'image : " + e);
            System.exit(1);
        }
        return img;
    }

    public void modifMessage(int type, int nb, int nb2, int nb1){
        int avant = jeu.partie().manche().Joueur(nb).positionAvant;
        int maintenant = jeu.partie().manche().Joueur(nb).newPosition;
        if(nb == 2){
            avant = -avant;
            maintenant = -maintenant;
        }
        if(type == 0){
            if(maintenant > avant){
                Message[2] = "Joueur " + nb + " : A avancé de " + nb2 + " case(s) !";
            }else{
                Message[2] = "Joueur " + nb + " : A reculé de " + nb2 + " case(s) !";
            }
            if(nb == 1){
                action1 = 2;
                action2 = 0;
            }else{
                action1 = 0;
                action2 = 2;
            }
        }else if(type == 1){
            Message[2] = "Joueur " + nb + " : Attaque Directe avec "+ nb1 + " carte(s) de valeur " + nb2;
            if(nb == 1){
                action1 = 1;
                action2 = 0;
            }else{
                action1 = 0;
                action2 = 1;
            }
        }else if(type == 2){
            Message2[1] = "Joueur " + nb + " : A parer !";
        }else if(type == 3){
            if(nb == 1){
                Message2[2] = "Joueur " + nb + " : A terminé son tour ! Tour du Joueur 2";
                action1 = 0;
            }else{
                Message2[2] = "Joueur " + nb + " : A terminé son tour ! Tour du Joueur 1";
                action2 = 0;
            }
        }else if(type == 4){
            Message2[3] = "Joueur " + nb + " : A perdu une vie, il lui en reste " + nb2;
            action1 = 0;
            action2 = 0;
        }else if(type == 5){
            Message[3] = "Joueur " + nb + " : Attaque Indirecte avec " + nb1 + " carte(s) de valeur " + nb2;
            if(nb == 1){
                action1 = 1;
                action2 = 0;
            }else{
                action1 = 0;
                action2 = 1;
            }
        }else if(type == 6){
            Message[4] = "Joueur " + nb + " : A esquivé en reculant de " + nb2 + " case(s)";
            if(nb == 1){
                action1 = 2;
                action2 = 0;
            }else{
                action2 = 2;
                action1 = 0;
            }
        }else if(type == 7){
            Message[5] = "Joueur " + nb + " : A fait une Parade Indirecte";
            action1 = 0;
            action2 = 0;
        }
    }

    //Fonction qui charge les images nécessaires pour la partie en fonction du choix du joueur
    public void setDecors(){
        stopMusique();
        teteJ1 = chargeImage("Sprite"+compteurJ1+"/Head");
        teteJ2 = chargeImage("Sprite"+compteurJ2+"/Head");

        int nb = compteurMap;
        int nb2 = nb%4;

        map = chargeImage("Map/Map"+nb);
        sol = chargeImage("Sol/Sol"+nb2);


        for(int i = 0; i < 4; i++){
            for(int a = 0; a < 3; a++){
                joueurs1[a][i] = chargeImage("Sprite"+compteurJ1+"/stand_"+a+""+i);
                joueurs2[a][i] = chargeImage("Sprite"+compteurJ2+"/stand_"+a+""+i);
            }
        }

        try {
            AudioInputStream input = AudioSystem.getAudioInputStream(new File("res/Music/Duel"+compteurMap+".wav"));
            clip = AudioSystem.getClip();
            clip.open(input);
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(-30.0f);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            //gainControl.setValue(0.0f);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public NiveauGraphique(Jeu j){
        jeu = j;
        nbColonnes = 23;
        mute = false;

        //Chargement des images
        teteJ1 = chargeImage("Sprite0/Head");
        teteJ2 = chargeImage("Sprite1/Head");
        TiretBleu = chargeImage("Partie/TiretBleu");
        TiretRouge = chargeImage("Partie/TiretRouge");
        fondMenu = chargeImage("Menu/MenuDroit");
        flecheDroit = chargeImage("Menu/Flèche");
        flecheGauche = chargeImage("Menu/FlècheReverse");
        fondNewPartie = chargeImage("Menu/MenuHaut");
        fond = chargeImage("Menu/Menu");
        NomJ1 = chargeImage("Partie/NomJ1");
        NomJ2 = chargeImage("Partie/NomJ2");
        fondJoueur = chargeImage("Partie/FondJoueur");
        fondMenuPartie = chargeImage("Partie/NomMenu");
        revenirALaPartie = chargeImage("Partie/RevenirPartie");
        menuPartie = chargeImage("Partie/Menu");

        victoireName = chargeImage("Partie/Victoire");
        muteIm = chargeImage("Partie/Mute");
        unMute = chargeImage("Partie/UnMute");

        //Boutons
        fin = chargeImage("Partie/Fin");
        quitter = chargeImage("Partie/Quitter");
        save = chargeImage("Partie/Save");
        annuler = chargeImage("Partie/AnnulerCoup");
        refaire = chargeImage("Partie/RefaireCoup");

        cartePioche = chargeImage("Carte/DeckCard");

        carte0 = chargeImage("Carte/Card_0");
        carte1 = chargeImage("Carte/Card_1");
        carte2 = chargeImage("Carte/Card_2");
        carte3 = chargeImage("Carte/Card_3");
        carte4 = chargeImage("Carte/Card_4");
        carte5 = chargeImage("Carte/Card_5");
        cartes = new Image[] { carte0, carte1, carte2, carte3, carte4, carte5};

        carte1_select = chargeImage("Carte/Card_1_selected");
        carte2_select = chargeImage("Carte/Card_2_selected");
        carte3_select = chargeImage("Carte/Card_3_selected");
        carte4_select = chargeImage("Carte/Card_4_selected");
        carte5_select = chargeImage("Carte/Card_5_selected");
        cartesSel = new Image[] {carte1_select, carte2_select, carte3_select, carte4_select, carte5_select};

        carte1_disabled = chargeImage("Carte/Card_1_disabled");
        carte2_disabled = chargeImage("Carte/Card_2_disabled");
        carte3_disabled = chargeImage("Carte/Card_3_disabled");
        carte4_disabled = chargeImage("Carte/Card_4_disabled");
        carte5_disabled = chargeImage("Carte/Card_5_disabled");
        cartesDisabled = new Image[] {carte1_disabled, carte2_disabled, carte3_disabled, carte4_disabled, carte5_disabled};

        ButtonChangeTour = chargeImage("Partie/ChangeTour");

        //Chargement des images pour Animations
        joueurs1 = new Image[3][4];
        joueurs2 = new Image[3][4];
        for(int i = 0; i < 4; i++){
            for(int a = 0; a < 3; a++){
                joueurs1[a][i] = chargeImage("Sprite0/stand_0"+i);
                joueurs2[a][i] = chargeImage("Sprite1/stand_0"+i);
            }
        }

        BandeVie = new Image[2][2];
        for(int v = 0; v < 2; v++){
            for(int z = 0; z < 2; z++){
                BandeVie[v][z] = chargeImage("Map/BandeVie_"+v+z);
            }
        }

        Message = new String[NB_MESSAGE];
        Message[0] = "";
        Message[1] = "Vous devez jouer au moins une carte pour finir votre tour";
        msg = 0;

        Message2 = new String[NB_MESSAGE];
        Message2[0] = "";
        msg2 = 0;

        etape = 0;
        action1 = 0;
        action2 = 0;
        joueur1 = joueurs1[action1][etape];
        joueur2 = joueurs2[action2][etape];

        //initialisation des booléens pour savoir dans quel page on est.
        Menu = true;
        Partie = false;
        Regles = false;

        //initialisation de booléen pour savoir quand est ce qu'il faut démarrez ou arrêter la musique
        PartieSet = false;
        MenuSet = false;
        ReglesSet = false;
        NewPartieSet = false;
    }

    public void paintComponent(Graphics g){

        drawable = (Graphics2D) g;
        largeur = getSize().width;
        hauteur = getSize().height;

        drawable.clearRect(0, 0, largeur, hauteur);

        if(Partie){
            if(jeu.partie().aGagner()){
                joueur1Vie = jeu.partie().Joueur(1).vie;
                joueur2Vie = jeu.partie().Joueur(2).vie;
                changeBackground(false, false, false,false, true, false);
                jeu.initialisePartie(compteurMap, compteurJ1, compteurJ2);
            }
        }

        if(Partie){
            tracerPartie();
        }else if(Menu){
            tracerMenu();
        }else if(Regles){
            tracerRegles();
        }else if(NewPartie){
            tracerNewPartie();
        }else if(Victoire){
            tracerVictoire();
        }else if(MenuPartie){
            tracerMenuPartie();
        }
        //System.out.println("J1 " + joueur1Vie + "/ J2 " + joueur2Vie);
        mute();
    }

    //Fonction qui démarre la musique du menu principal
    public void startMusique(){
        try {
            AudioInputStream input = AudioSystem.getAudioInputStream(new File("res/Music/Menu.wav"));
            clip = AudioSystem.getClip();
            clip.open(input);
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(-30.0f);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Fonction qui permet d'arrêter la musique
    public void mute(){
        if(mute){
            clip.stop();
        }else{
            clip.start();
        }
    }

    //Fonction qui lance la musique de la victoire en fonction du vainqueur
    public void startVictoire(){
        try {
            int nb;
            AudioInputStream input = null;
            if(joueur2Vie > joueur1Vie){
                nb = compteurJ2%2;
                input = AudioSystem.getAudioInputStream(new File("res/Music/Victoire"+nb+".wav"));
            }else{
                nb = compteurJ1%2;
                input = AudioSystem.getAudioInputStream(new File("res/Music/Victoire"+nb+".wav"));
            }
            clip = AudioSystem.getClip();
            clip.open(input);
            gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(-30.0f);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Fonction qui permet de stopper la musique en cours
    public void stopMusique(){
        if(MenuSet || PartieSet || VictoireSet){
            clip.stop();
        }
    }

    //Fonction qui trace les différents éléments graphique du menu
    public void tracerMenu(){
        if(!MenuSet){
            stopMusique();
            startMusique();
        }
        PartieSet = false;
        MenuSet = true;
        ReglesSet = false;
        NewPartieSet = false;
        VictoireSet = false;

        largeurNom = (int)Math.round(largeur*0.30);
        hauteurNom = (int)Math.round(hauteur*0.25);
        xNom = (int)Math.round(largeur*0.35);
        yNom = (int)Math.round(hauteur*0.05);

        drawable.clearRect(0, 0, largeur, hauteur);
        drawable.drawImage(fondMenu, 0,0,largeur, hauteur, null);

        tailleMute = (int)Math.round(largeur*0.1);
        xBoutonMute = largeur-tailleMute;
        yBoutonMute = hauteur-tailleMute;

        if(mute){
            drawable.drawImage(muteIm, xBoutonMute, yBoutonMute, tailleMute, tailleMute, null);
        }else{
            drawable.drawImage(unMute, xBoutonMute, yBoutonMute, tailleMute, tailleMute, null);
        }
    }

    //Fonction qui trace les différents éléments graphique des règles du jeu
    public void tracerRegles(){
        largeurNom = (int)Math.round(largeur*0.30);
        hauteurNom = (int)Math.round(hauteur*0.25);
        xNom = (int)Math.round(largeur*0.35);
        yNom = (int)Math.round(hauteur*0.05);

        drawable.clearRect(0, 0, largeur, hauteur);
        drawable.drawImage(fond, 0,0,largeur, hauteur, null);

        if(compteur == 0){
            drawable.drawImage(teteJ1, 0, 0, 150,150, null);
        }

        tailleMute = (int)Math.round(largeur*0.05);
        xBoutonMute = largeur-tailleMute;
        yBoutonMute = 0;

        if(mute){
            drawable.drawImage(muteIm, xBoutonMute, yBoutonMute, tailleMute, tailleMute, null);
        }else{
            drawable.drawImage(unMute, xBoutonMute, yBoutonMute, tailleMute, tailleMute, null);
        }
    }

    //Fonction qui trace les différents éléments graphique de la partie.
    public void tracerPartie(){
        if(!PartieSet){
            stopMusique();
            //setDecors();
        }

        PartieSet = true;
        MenuSet = false;
        ReglesSet = false;
        NewPartieSet = false;
        VictoireSet = false;
        MenuPartie = false;

        largeurCase = largeur / nbColonnes;
        hauteurCase = (int)Math.round(largeurCase*0.35);
        hauteurLuke = (int)Math.round(largeurCase * 2.10);
        hauteurVador = (int)Math.round(largeurCase * 1.94);
        largeurVador = (int)Math.round(largeurCase * 1.50);
        xTeteGauche = (int)Math.round(largeur*0.01);
        EspacementTiret = (int)Math.round(largeur*0.05);
        dimensionTete = (int)Math.round(largeurCase*1.50);
        xTeteDroite = (int)Math.round((largeur*.99)-(dimensionTete));
        yTete = (int)Math.round(hauteur*0.05);
        xPointGauche = (xTeteGauche+dimensionTete);
        xPointDroit = xTeteDroite;
        yPoint = (int)Math.round(yTete+dimensionTete*0.75);
        hauteurTiret = (int)Math.round(hauteur*0.01);
        largeurTiret = (int)Math.round(largeur*0.025);
        hauteurNom = (int)Math.round(dimensionTete *0.35);
        largeurNom = (int)Math.round(dimensionTete * 3);
        yNom = (int)Math.round(hauteur*0.08);


        //int pour les boutons de la partie
        xBoutonDroite = (int)Math.round((largeur*0.905)-largeurBouton);
        xBoutonGauche = (int)Math.round(largeur*0.095);
        yBoutonBas = (int)Math.round(hauteur*0.90);
        yBoutonMilieu = (int)Math.round(hauteur*0.825);
        yBoutonMilieu2 = (int)Math.round(hauteur*0.750);
        yBoutonHaut = (int)Math.round(hauteur*0.675);
        largeurBouton = (int)Math.round(largeur*0.15);
        hauteurBouton = (int)Math.round(hauteur*0.059);

        int[] grilleJeu = jeu.partie().manche().grilleJeu;

        drawable.clearRect(0, 0, largeur, hauteur);
        drawable.drawImage(map, 0, 0, largeur, hauteur, null);

        drawable.setFont(new Font("Segeo UI Black", Font.BOLD, (int)Math.round(largeur*0.025)));
        drawable.setColor(new Color(253, 230,30));
        drawable.drawString(Message[msg], (int)Math.round(largeur*0.30), (int)Math.round(hauteur*0.70));
        drawable.drawString(Message2[msg2], (int)Math.round(largeur*0.30), (int)Math.round(hauteur*0.71)+(int)Math.round(largeur*0.025));


        if(jeu.partie().manche().getTourJoueur() == 1){
            drawable.drawImage(BandeVie[0][compteurJ1%2], 0, yTete-(int)Math.round(hauteur*0.040), largeur, dimensionTete+hauteurNom+largeurTiret, null);
        }else{
            drawable.drawImage(BandeVie[1][compteurJ2%2], 0, yTete-(int)Math.round(hauteur*0.040), largeur, dimensionTete+hauteurNom+largeurTiret, null);
        }

        //affichage de la tete et du nom des deux joueurs
        //drawable.drawImage(fondJoueur, 0, (int)Math.round(yTete-0.005*hauteur), (int)Math.round(dimensionTete+largeurNom*1.50), (int)Math.round(dimensionTete+largeurTiret*0.25), null);
        //drawable.drawImage(fondJoueur, largeur, (int)Math.round(yTete-0.005*hauteur), -(int)Math.round(dimensionTete+largeurNom*1.50), (int)Math.round(dimensionTete+largeurTiret*0.25), null);
        drawable.drawImage(teteJ1, xTeteGauche, yTete, dimensionTete ,dimensionTete,null);
        drawable.drawImage(teteJ2, xTeteDroite+dimensionTete, yTete, -dimensionTete ,dimensionTete,null);
        drawable.drawImage(NomJ1, (int)Math.round(xPointGauche+(0.5*EspacementTiret)), yNom, largeurNom, hauteurNom, null);
        drawable.drawImage(NomJ2, (int)Math.round(xPointDroit-(5*EspacementTiret)), yNom, largeurNom, hauteurNom, null);

        // affichage de la pioche
        int largeurCarte = (int) Math.round(largeur * 0.30)/5;
        int hauteurCarte = (int) Math.round(hauteur * 0.15);
        //int[] oldPos;
        int k =1;
        do{
            drawable.drawImage(cartes[0],(int)Math.round((largeur*0.46) + (k*2)),(int)Math.round(hauteur*0.045),largeurCarte,hauteurCarte, null);
            k+=1;
        }while(k<jeu.partie().manche().restantPioche());
        drawable.drawImage(cartes[1],(int)Math.round((largeur*0.46) + (k*2)),(int)Math.round(hauteur*0.045),largeurCarte,hauteurCarte, null);
        /*for(int i=jeu.partie().manche().restantPioche(); i<15;i++){
            drawable.drawImage(cartePioche,(int)Math.round((largeur*0.45) + (i*2)),(int)Math.round(hauteur*0.20),largeurCarte,hauteurCarte, null);
        }*/

        //affichage Des Bouton
        drawable.drawImage(fin, xBoutonDroite, yBoutonBas, largeurBouton, hauteurBouton, null);
        drawable.drawImage(annuler, xBoutonGauche, yBoutonMilieu2, largeurBouton, hauteurBouton, null);
        drawable.drawImage(refaire, xBoutonGauche, yBoutonMilieu, largeurBouton, hauteurBouton, null);
        drawable.drawImage(menuPartie, xBoutonGauche, yBoutonBas, largeurBouton, hauteurBouton, null);
        // affichage des barres de vie à partir de la santé de chaque joueur
        joueur1Vie = jeu.partie().manche().joueur1.vie;
        joueur2Vie = jeu.partie().manche().joueur2.vie;
        for(int i=0; i<joueur1Vie;i++){
            drawable.drawImage(TiretBleu, (int)Math.round(xPointGauche+((i+0.5)*EspacementTiret)), yPoint, largeurTiret, hauteurTiret, null);
        }
        for(int i=joueur1Vie; i<5;i++){
            drawable.drawImage(TiretRouge, (int)Math.round(xPointGauche+((i+0.5)*EspacementTiret)), yPoint, largeurTiret, hauteurTiret, null);
        }
        for(int i=0; i<joueur2Vie;i++){
            drawable.drawImage(TiretBleu, (int)Math.round(xPointDroit-((i+1)*EspacementTiret)), yPoint, largeurTiret, hauteurTiret, null);
        }
        for(int i=joueur2Vie; i<5;i++){
            drawable.drawImage(TiretRouge, (int)Math.round(xPointDroit-((i+1)*EspacementTiret)), yPoint, largeurTiret, hauteurTiret, null);
        }
        for(int c = 0; c < jeu.partie().manche().NOMBRE_CASES; c++){
            int x = c * largeurCase;
            y = (int) Math.round(hauteur * 0.62);
            if (jeu.partie().manche().getCaseIHM().size() < jeu.partie().manche().NOMBRE_CASES){
                jeu.partie().manche().initCaseIHM(c, grilleJeu[c], x, (int)Math.round(y-hauteurVador+(hauteurCase*0.5)), largeurCase, hauteurVador, 0);
            } else {
                jeu.partie().manche().updateCaseIHM(c, grilleJeu[c], x, (int)Math.round(y-hauteurVador+(hauteurCase*0.5)), largeurCase, hauteurVador);
            }
            drawable.drawImage(sol, x, y, largeurCase, hauteurCase, null);
            if(grilleJeu[c] == 1){
                drawable.drawImage(joueur1, x, (int)Math.round(y-hauteurVador+(hauteurCase*0.5)), largeurVador, hauteurVador, null);
            }else if(grilleJeu[c] == 2){
                drawable.drawImage(joueur2, x+largeurCase, (int)Math.round(y-hauteurVador+(hauteurCase*0.5)), -largeurVador, hauteurVador, null);
            }

            affichePossibilites(drawable);
        }

        afficheMainJoueur(jeu.partie().Joueur(jeu.partie().manche().getTourJoueur()), drawable);

        if(jeu.selectedCarte.size()>0 && jeu.selectedCarte.get(0).getId() != -1)      {

            for(int i = 0; i<jeu.selectedCarte.size() ; i++)
            {
                selectCartes(jeu.selectedCarte.get(i).getValeur(), jeu.selectedCarte.get(i).getCoordX(), jeu.selectedCarte.get(i).getCoordY(), jeu.selectedCarte.get(i).getLargeur(), jeu.selectedCarte.get(i).getHauteur(), drawable);
            }
        }
    }

    public void tracerMenuPartie(){
        drawable.clearRect(0, 0, largeur, hauteur);

        PartieSet = true;
        MenuPartieSet = true;
        MenuSet = false;
        ReglesSet = false;
        NewPartieSet = false;
        VictoireSet = false;

        drawable.drawRect(0,0,largeur, hauteur);
        Color cGris = new Color(150, 150, 150, 20);
        drawable.setColor(cGris);
        drawable.fillRect(0, 0, largeur, hauteur);

        drawable.drawImage(fondMenuPartie, 0, 0, largeur, hauteur, null);


        tailleMute = (int)Math.round(largeur*0.05);
        xBoutonMute = largeur-tailleMute;
        yBoutonMute = hauteur-tailleMute;
        xBoutonMenu = (int)Math.round(largeur*0.375);
        yBoutonUn = (int)Math.round(hauteur*0.35);
        yBoutonDeux = (int)Math.round(hauteur*0.55);
        yBoutonTrois = (int)Math.round(hauteur*0.75);
        largeurBoutonMenu = (int)Math.round(largeur*0.250);
        hauteurBoutonMenu = (int)Math.round(hauteur*0.100);

        drawable.drawImage(revenirALaPartie, xBoutonMenu, yBoutonUn, largeurBoutonMenu, hauteurBoutonMenu, null);
        drawable.drawImage(quitter, xBoutonMenu, yBoutonTrois, largeurBoutonMenu, hauteurBoutonMenu, null);
        drawable.drawImage(save, xBoutonMenu, yBoutonDeux, largeurBoutonMenu, hauteurBoutonMenu, null);


        if(mute){
            drawable.drawImage(muteIm, xBoutonMute, yBoutonMute, tailleMute, tailleMute, null);
        }else{
            drawable.drawImage(unMute, xBoutonMute, yBoutonMute, tailleMute, tailleMute, null);
        }

    }

    //Fonction qui trace les différents éléments graphique de la partie.
    public void tracerNewPartie(){
        drawable.clearRect(0, 0, largeur, hauteur);
        drawable.drawImage(fondNewPartie, 0,0,largeur, hauteur, null);

        if(!NewPartieSet){
            compteurJ1 = 0;
            compteurJ2 = 1;
            compteurMap = 0;
            NewPartieSet = true;
        }


        hauteurLuke = (int)Math.round(largeur * 0.1);
        hauteurVador = (int)Math.round(largeur * 0.15);
        largeurVador = (int)Math.round(largeur * 0.15);

        tailleBouton = largeurVador/4;
        yBouton = (int)Math.round(hauteur*0.1)+(hauteurVador/2);
        xBouton1 = (int)Math.round((largeur*0.1)+(largeurVador*0.75));
        xBouton2 = (int)Math.round((largeur*0.1)-(largeurVador*0.40));
        xBouton3 = (int)Math.round((largeur*0.9)+(largeurVador*0.25));
        xBouton4 = (int)Math.round((largeur*0.9)-(largeurVador*1.1));
        xBouton5 = (int)Math.round((largeur*0.35)-largeurVador*0.35);
        xBouton6 = (int)Math.round((largeur*0.35)+(largeur*0.315));


        map = chargeImage("Map/MiniMap"+compteurMap);
        joueur1Choix = chargeImage("Sprite"+compteurJ1+"/stand_00");
        joueur2Choix = chargeImage("Sprite"+compteurJ2+"/stand_00");

        drawable.drawImage(map, (int)Math.round(largeur*0.35),(int)Math.round(hauteur*0.1),(int)Math.round(largeur*0.30), (int)Math.round(largeur*0.150), null);
        drawable.drawImage(joueur1Choix, (int)Math.round(largeur*0.1), (int)Math.round(hauteur*0.1), largeurVador, hauteurVador, null);
        drawable.drawImage(joueur2Choix, (int)Math.round(largeur*0.9), (int)Math.round(hauteur*0.1), -largeurVador, hauteurVador, null);



        //Flèche Joueur 1
        drawable.drawImage(flecheDroit, xBouton1 , yBouton, tailleBouton, tailleBouton, null);
        drawable.drawImage(flecheGauche, xBouton2 , yBouton, tailleBouton, tailleBouton, null);

        //Flèche Joueur 2
        drawable.drawImage(flecheDroit, xBouton3 , yBouton, tailleBouton, tailleBouton, null);
        drawable.drawImage(flecheGauche, xBouton4 , yBouton, tailleBouton, tailleBouton, null);

        //Flèche Map
        drawable.drawImage(flecheGauche, xBouton5, yBouton, tailleBouton, tailleBouton, null);
        drawable.drawImage(flecheDroit, xBouton6, yBouton, tailleBouton, tailleBouton, null);


        tailleMute = (int)Math.round(largeur*0.05);
        xBoutonMute = largeur-tailleMute;
        yBoutonMute = 0;

        if(mute){
            drawable.drawImage(muteIm, xBoutonMute, yBoutonMute, tailleMute, tailleMute, null);
        }else{
            drawable.drawImage(unMute, xBoutonMute, yBoutonMute, tailleMute, tailleMute, null);
        }
    }

    //Fonction qui trace les différents éléments graphique de la page de Victoire.
    public void tracerVictoire(){
        if(!VictoireSet){
            stopMusique();
            startVictoire();
        }

        VictoireSet = true;
        MenuSet = false;
        NewPartieSet = false;
        PartieSet = false;
        ReglesSet = false;
        MenuPartie = false;

        largeurBouton = (int)Math.round(largeur*0.15);
        hauteurBouton = (int)Math.round(hauteur*0.059);
        yBoutonVictoire = hauteur-hauteurBouton;

        drawable.clearRect(0, 0, largeur, hauteur);
        drawable.drawImage(fond, 0,0,largeur, hauteur, null);

        drawable.drawImage(quitter, 0, hauteur-hauteurBouton, largeurBouton, hauteurBouton, null);
        drawable.drawImage(victoireName, (int)Math.round(0.25*largeur), (int)Math.round(0.1*hauteur), (int)Math.round(0.50*largeur), (int)Math.round(0.07*largeur), null);

        /*int pdv2 = jeu.partie().manche().joueur2.vie;
        int pdv1 = jeu.partie().manche().joueur1.vie;*/

        if(joueur1Vie > joueur2Vie){
            drawable.drawImage(joueur1, (int)Math.round(0.45*largeur), (int)Math.round(0.3*hauteur), (int)Math.round(0.20*largeur), (int)Math.round(0.20*largeur), null);
            drawable.drawImage(NomJ1, (int)Math.round(0.30*largeur), (int)Math.round(0.7*hauteur), (int)Math.round(0.40*largeur), (int)Math.round(0.06*largeur), null);
        }else{
            drawable.drawImage(joueur2, (int)Math.round(0.45*largeur), (int)Math.round(0.3*hauteur), (int)Math.round(0.20*largeur), (int)Math.round(0.20*largeur), null);
            drawable.drawImage(NomJ2, (int)Math.round(0.30*largeur), (int)Math.round(0.7*hauteur), (int)Math.round(0.40*largeur), (int)Math.round(0.06*largeur), null);
        }

        tailleMute = (int)Math.round(largeur*0.05);
        xBoutonMute = largeur-tailleMute;
        yBoutonMute = hauteur-tailleMute;

        if(mute){
            drawable.drawImage(muteIm, xBoutonMute, yBoutonMute, tailleMute, tailleMute, null);
        }else{
            drawable.drawImage(unMute, xBoutonMute, yBoutonMute, tailleMute, tailleMute, null);
        }
    }

    //Fonction permettant de mettre à jour la fenêtre graphique
    @Override
    public void metAJour() {
        repaint();
    }


    //Fontion permettant d'animer les joueurs à l'arret.
    public void animJoueur() {
        etape = (etape+1)%4;
        joueur1 = joueurs1[action1][etape];
        joueur2 = joueurs2[action2][etape];
        metAJour();
    }

    //Fonction permettant de dessiner les cartes sélectionnées.
    public void selectCartes(int val ,int x, int y, int l, int h, Graphics2D drawable){
        drawable.drawImage(cartesSel[val-1],x,y,l,h,null);
    }

    //Fonction permettant de dessiner la main du joueur à qui c'est le tour.
    public void afficheMainJoueur(JoueurHumain j, Graphics2D drawable){
        int nbCartes = j.main.size();
        //System.out.print("joueur: "+ j.main + "\n");

        int largeurCarte = (int) Math.round(largeur * 0.30)/5;
        int hauteurCarte = (int) Math.round(hauteur * 0.15);


        int x = (largeur/2)-((nbCartes*largeurCarte)/2)-largeurCarte;
        int y = (int) Math.round(hauteur * 0.80);

        int select = 0;
        for(int i = 0; i < j.main.size(); i++){

            int valeurCarte = j.main.get(i);


            x = x+largeurCarte;



            if(j.getCarteI().size() < 5 ){
                j.initCarteI(i, valeurCarte, x, y, largeurCarte, hauteurCarte);
            } else {
                if(i != 5)
                {
                    j.updateCarteI(i, valeurCarte, x, y, largeurCarte, hauteurCarte);
                }

            }

            if(i<j.getCarteI().size())
            {
                if (j.getCarteI().get(i).getEtat() == 1){
                    drawable.drawImage(cartesDisabled[valeurCarte-1], x , y, largeurCarte, hauteurCarte, null);
                } else {
                    drawable.drawImage(cartes[valeurCarte], x , y, largeurCarte, hauteurCarte, null);
                }
            }

            if (jeu.selectedCarte.size()>select && jeu.selectedCarte.get(select).getId() == i){
                jeu.selectedCarte.get(select).update(jeu.selectedCarte.get(select).getId(), jeu.selectedCarte.get(select).getValeur(), x, y, largeurCarte, hauteurCarte);
                select +=1;
            }

        }
    }

    //Fonction permettant d'afficher les différentes possibilitées quand on sélectionne une ou plusieurs cartes.
    public void affichePossibilites(Graphics2D drawable){
        ArrayList<SelectionCaseIHM> CaseIHM = new ArrayList<>();
        CaseIHM = jeu.partie().manche().CaseIHM;
        for(int i = 0; i< CaseIHM.size(); i++)
        {
            int etat = CaseIHM.get(i).getEtat();

            switch(etat){
                case 1:
                    Color cBleu = new Color(100, 250, 255, 20);
                    drawable.setColor(cBleu);
                    drawable.fillRect(CaseIHM.get(i).getX(), (int)Math.round(y-hauteurVador+(hauteurCase*0.5)), CaseIHM.get(i).getLargeur(), hauteurVador);
                    break;
                case 2:
                    Color cRouge = new Color(255,0,0,20);
                    drawable.setColor(cRouge);
                    drawable.fillRect(CaseIHM.get(i).getX(), (int)Math.round(y-hauteurVador+(hauteurCase*0.5)), CaseIHM.get(i).getLargeur(), hauteurVador);
                    break;
                case 3:
                    Color cVert = new Color(0,255,0, 20);
                    drawable.setColor(cVert);
                    drawable.fillRect(CaseIHM.get(i).getX(), (int)Math.round(y-hauteurVador+(hauteurCase*0.5)), CaseIHM.get(i).getLargeur(), hauteurVador);
                    break;
                default:
                    break;

            }

        }

    }

    //Fonction qui met à jour les booléens pour changer l'affichage de la fenêtre en fonction de la page
    //que l'on veut afficher
    public void changeBackground(boolean b1, boolean b2, boolean b3, boolean b4, boolean b5, boolean b6) {
        Menu = b1;
        Partie = b2;
        Regles = b3;
        NewPartie = b4;
        Victoire = b5;
        MenuPartie = b6;
        metAJour();
    }

    public void afficheBoutonChangeTour() {
        int largeurButton = (int) Math.round(largeur * 0.15);
        int hauteurButton = (int) Math.round(hauteur * 0.12);

        int x = (int) Math.round(largeur * 0.8);
        int y = (int) Math.round(hauteur * 0.8);

        jeu.partie().manche().initButtonChangeTour(x, y, largeurButton, hauteurButton);

        drawable.drawImage(ButtonChangeTour, x , y, largeurButton, hauteurButton, null);
    }

    public void setDecorsSauve(int compteurJ12, int compteurJ22, int compteurMap2) {
        stopMusique();
        teteJ1 = chargeImage("Sprite"+compteurJ12+"/Head");
        teteJ2 = chargeImage("Sprite"+compteurJ22+"/Head");

        int nb = compteurMap2;
        int nb2 = nb%4;

        map = chargeImage("Map/Map"+nb);
        sol = chargeImage("Sol/Sol"+nb2);


        for(int i = 0; i < 4; i++){
            for(int a = 0; a < 3; a++){
                joueurs1[a][i] = chargeImage("Sprite"+compteurJ12+"/stand_"+a+""+i);
                joueurs2[a][i] = chargeImage("Sprite"+compteurJ22+"/stand_"+a+""+i);
            }
        }

        try {
            AudioInputStream input = AudioSystem.getAudioInputStream(new File("res/Music/Duel"+compteurMap2+".wav"));
            clip = AudioSystem.getClip();
            clip.open(input);
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(-30.0f);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            //gainControl.setValue(0.0f);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
