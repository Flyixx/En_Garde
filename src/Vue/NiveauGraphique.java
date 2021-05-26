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
    Jeu jeu;
    Image flecheDroit, flecheGauche, fondMenu, fond, fondNewPartie, joueur1, joueur1Choix, joueur2Choix, joueur2, sol, map, teteJ1, teteJ2, TiretBleu, TiretRouge, NomJ1, NomJ2, carte1, carte2, carte3, carte4, carte5, carte0, carte1_select, carte2_select, carte3_select, carte4_select, carte5_select, Map0, Map1, Map2, Map3, Map4, Map5, Map6, Map7;
    int dimensionTete, xTeteDroite, xTeteGauche, yTete, etape, largeur, hauteur, nbColonnes, largeurCase, hauteurNom, largeurNom, hauteurCase, hauteurLuke, hauteurVador, largeurVador, yPoint, xPointGauche, xPointDroit, hauteurTiret, largeurTiret, yNom, xNom;
    public int tailleBouton, xBouton1, yBouton, xBouton2, xBouton3, xBouton4, xBouton5, xBouton6, compteurJ1, compteurJ2, compteurMap;
    Image[] joueurs1;
    Image[] joueurs2;
    Image carte1_disabled, carte2_disabled, carte3_disabled, carte4_disabled, carte5_disabled;
    Image ButtonChangeTour;
    Random r;
    Clip clip;
    Graphics2D drawable;
    boolean Menu, Partie, Regles, NewPartie, PartieSet, MenuSet, ReglesSet, NewPartieSet;
    public int compteur;
    Image[] cartes = {};
    Image[] cartesSel = {};
    Image[] cartesDisabled = {};

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

    //Fonction qui choisi un décors au hasard et choisi le sol et la musique associée.
    /*public void randomDecors(){
        Random r = new Random();
        int nb = r.nextInt(8);
        int nb2 = nb%4;
        map = chargeImage("Map/Map"+nb);
        sol = chargeImage("Sol/Sol"+nb2);
        //Gestion Musique en fonction du décors choisi
        try {
            AudioInputStream input = AudioSystem.getAudioInputStream(new File("res/Music/Duel"+nb+".wav"));
            clip = AudioSystem.getClip();
            clip.open(input);
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(-80.0f);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            //gainControl.setValue(0.0f);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    public void setDecors(){
        teteJ1 = chargeImage("Sprite"+compteurJ1+"/Head");
        teteJ2 = chargeImage("Sprite"+compteurJ2+"/Head");

        map = chargeImage("Map/Map"+compteurMap);
        sol = chargeImage("Sol/Sol"+compteurMap%2);


        for(int i = 0; i < 4; i++){
            joueurs1[i] = chargeImage("Sprite"+compteurJ1+"/stand_0"+i);
            joueurs2[i] = chargeImage("Sprite"+compteurJ2+"/stand_0"+i);
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
        joueurs1 = new Image[4];
        joueurs2 = new Image[4];
        for(int i = 0; i < 4; i++){
            joueurs1[i] = chargeImage("Sprite0/stand_0"+i);
            joueurs2[i] = chargeImage("Sprite1/stand_0"+i);
        }

        etape = 0;
        joueur1 = joueurs1[etape];
        joueur2 = joueurs2[etape];

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


        if (jeu.partie().aGagner()){
            changeBackground(true, false, false,false);
            jeu.initialisePartie();
        }


        drawable.clearRect(0, 0, largeur, hauteur);
        if(Partie){
            tracerPartie();
        }else if(Menu){
            tracerMenu();
        }else if(Regles){
            tracerRegles();
        }else if(NewPartie){
            tracerNewPartie();
        }
    }

    //Fonction qui démarre la musique du menu principal
    public void startMusique(){
        try {
            AudioInputStream input = AudioSystem.getAudioInputStream(new File("res/Music/Menu.wav"));
            clip = AudioSystem.getClip();
            clip.open(input);
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(-80.0f);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Fonction qui permet de stopper la musique en cours
    public void stopMusique(){
        if(MenuSet || PartieSet){
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

        largeurNom = (int)Math.round(largeur*0.30);
        hauteurNom = (int)Math.round(hauteur*0.25);
        xNom = (int)Math.round(largeur*0.35);
        yNom = (int)Math.round(hauteur*0.05);

        drawable.clearRect(0, 0, largeur, hauteur);
        drawable.drawImage(fondMenu, 0,0,largeur, hauteur, null);
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
    }

    //Fonction qui trace les différents éléments graphique de la partie.
    public void tracerPartie(){
        if(!PartieSet){
            stopMusique();
            setDecors();
            //randomDecors();
        }

        PartieSet = true;
        MenuSet = false;
        ReglesSet = false;
        NewPartieSet = false;

        largeurCase = largeur / nbColonnes;
        hauteurCase = (int)Math.round(largeurCase*0.35);
        hauteurLuke = (int)Math.round(largeurCase * 2.10);
        hauteurVador = (int)Math.round(largeurCase * 1.94);
        largeurVador = (int)Math.round(largeurCase * 1.50);
        xTeteGauche = (int)Math.round(largeur*0.05);
        xTeteDroite = (int)Math.round((largeur*.95)-(largeurCase*1.75));
        yTete = (int)Math.round(hauteur*0.05);
        xPointGauche = (xTeteGauche+dimensionTete);
        xPointDroit = xTeteDroite;
        yPoint = (int)Math.round(yTete+dimensionTete*0.75);
        dimensionTete = (int)Math.round(largeurCase*1.75);
        hauteurTiret = (int)Math.round(hauteur*0.01);
        largeurTiret = (int)Math.round(largeur*0.025);
        hauteurNom = (int)Math.round(dimensionTete / 2);
        largeurNom = (int)Math.round(dimensionTete * 3);
        yNom = (int)Math.round(hauteur*0.08);

        int[] grilleJeu = jeu.partie().manche().grilleJeu;

        drawable.clearRect(0, 0, largeur, hauteur);
        drawable.drawImage(map, 0, 0, largeur, hauteur, null);
        //affichage de la tete et du nom des deux joueurs
        drawable.drawImage(teteJ1, xTeteGauche, yTete, dimensionTete ,dimensionTete,null);
        drawable.drawImage(teteJ2, xTeteDroite+dimensionTete, yTete, -dimensionTete ,dimensionTete,null);
        drawable.drawImage(NomJ1, xPointGauche+(1*xTeteGauche), yNom, largeurNom, hauteurNom, null);
        drawable.drawImage(NomJ2, xPointDroit-(5*xTeteGauche), yNom, largeurNom, hauteurNom, null);

        // affichage des barres de vie à partir de la santé de chaque joueur
        int joueur1Vie = jeu.partie().manche().joueur1.vie;
        int joueur2Vie = jeu.partie().manche().joueur2.vie;
        for(int i=0; i<joueur1Vie;i++){
            drawable.drawImage(TiretBleu, xPointGauche+((i+1)*xTeteGauche), yPoint, largeurTiret, hauteurTiret, null);
        }
        for(int i=joueur1Vie; i<5;i++){
            drawable.drawImage(TiretRouge, xPointGauche+((i+1)*xTeteGauche), yPoint, largeurTiret, hauteurTiret, null);
        }
        for(int i=0; i<joueur2Vie;i++){
            drawable.drawImage(TiretBleu, xPointDroit-((i+1)*xTeteGauche), yPoint, largeurTiret, hauteurTiret, null);
        }
        for(int i=joueur2Vie; i<5;i++){
            drawable.drawImage(TiretRouge, xPointDroit-((i+1)*xTeteGauche), yPoint, largeurTiret, hauteurTiret, null);
        }

        for(int c = 0; c < jeu.partie().manche().NOMBRE_CASES; c++){
            int x = c * largeurCase;
            int y = (int) Math.round(hauteur * 0.62);

            if (jeu.partie().manche().getCaseIHM().size() < jeu.partie().manche().NOMBRE_CASES){
                jeu.partie().manche().initCaseIHM(c, grilleJeu[c], x, y - hauteurCase*3, largeurCase, hauteurCase*4, 0);
            } else {
                jeu.partie().manche().updateCaseIHM(c, grilleJeu[c], x, y - hauteurCase*3, largeurCase, hauteurCase*4);
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

        afficheBoutonChangeTour();

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
    }

    @Override
    public void metAJour() {
        repaint();
    }

    //Fontion permettant d'animer les joueurs à l'arret.
    public void animJoueur() {
        etape = (etape+1)%4;
        joueur1 = joueurs1[etape];
        joueur2 = joueurs2[etape];
        metAJour();
    }

    public void selectCartes(int val ,int x, int y, int l, int h, Graphics2D drawable){
        drawable.drawImage(cartesSel[val-1],x,y,l,h,null);
    }

    public void afficheMainJoueur(JoueurHumain j, Graphics2D drawable){
        int nbCartes = j.main.size();
        //System.out.print("joueur: "+ j.main + "\n");

        int largeurCarte = (int) Math.round(largeur * 0.30)/5;
        int hauteurCarte = (int) Math.round(hauteur * 0.15);


        int x = (largeur/2)-((nbCartes*largeurCarte)/2)-largeurCarte;
        int y = (int) Math.round(hauteur * 0.80);


        for(int i = 0; i < j.main.size(); i++){

            int valeurCarte = j.main.get(i);


            x = x+largeurCarte;


            if(j.getCarteI().size() < 5){
                j.initCarteI(i, valeurCarte, x, y, largeurCarte, hauteurCarte);
            } else {
                j.updateCarteI(i, valeurCarte, x, y, largeurCarte, hauteurCarte);
            }

            if (j.getCarteI().get(i).getEtat() == 1){
                drawable.drawImage(cartesDisabled[valeurCarte-1], x , y, largeurCarte, hauteurCarte, null);
            } else {
                drawable.drawImage(cartes[valeurCarte], x , y, largeurCarte, hauteurCarte, null);
            }

            if (jeu.selectedCarte.size()>i && jeu.selectedCarte.get(i).getId() == i){

                jeu.selectedCarte.get(i).update(jeu.selectedCarte.get(i).getId(), jeu.selectedCarte.get(i).getValeur(), x, y, largeurCarte, hauteurCarte);
            }

        }
    }

    public void affichePossibilites(Graphics2D drawable){
        ArrayList<SelectionCaseIHM> CaseIHM = new ArrayList<>();
        CaseIHM = jeu.partie().manche().CaseIHM;
        for(int i = 0; i< CaseIHM.size(); i++)
        {
            int etat = CaseIHM.get(i).getEtat();

            switch(etat){
                case 1:
                    Color cBlue = new Color(100, 250, 255, 20);
                    drawable.setColor(cBlue);
                    drawable.fillRect(CaseIHM.get(i).getX(), CaseIHM.get(i).getY(), CaseIHM.get(i).getLargeur(), CaseIHM.get(i).getHauteur());

                    break;
                case 2:
                    drawable.setColor(Color.RED);
                    drawable.fillRect(CaseIHM.get(i).getX(), CaseIHM.get(i).getY(), CaseIHM.get(i).getLargeur(), CaseIHM.get(i).getHauteur());
                    break;
                default:
                    break;

            }

        }

    }

    //Fonction qui met à jour les booléens pour changer l'affichage de la fenêtre en fonction de la page
    //que l'on veut afficher
    public void changeBackground(boolean b1, boolean b2, boolean b3, boolean b4) {
        Menu = b1;
        Partie = b2;
        Regles = b3;
        NewPartie = b4;
        metAJour();
    }

    public void afficheBoutonChangeTour()
    {
        int largeurButton = (int) Math.round(largeur * 0.15);
        int hauteurButton = (int) Math.round(hauteur * 0.12);

        int x = (int) Math.round(largeur * 0.8);
        int y = (int) Math.round(hauteur * 0.8);

        jeu.partie().manche().initButtonChangeTour(x, y, largeurButton, hauteurButton);

        drawable.drawImage(ButtonChangeTour, x , y, largeurButton, hauteurButton, null);
    }

}
