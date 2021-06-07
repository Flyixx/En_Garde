package Vue;

import Modele.Jeu;
import Patterns.Observateur;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.*;

public class InterfaceGraphique implements Runnable, Observateur {
    public Jeu jeu;
    CollecteurEvenements control;
    NiveauGraphique niv;
    JFrame frame;
    Box barreLaterale;
    JPanel pan, optionGauche, optionDroit, Regles, NewPartie;
    public int hauteurPanel, largeurPanel;
    JButton Niveau, Option, Charger, Quitter, Droit, Gauche, Retour, Retour2, PvP, PvIAF, PvIAM;

    InterfaceGraphique(Jeu j,CollecteurEvenements c){
        jeu = j;
        control = c;
        jeu.fixerCollecteurEvenement(c);
        jeu.ajouteObservateur(this);
    }

    public static void demarrer(Jeu j, CollecteurEvenements control){
        SwingUtilities.invokeLater(new InterfaceGraphique(j,control));
    }

    //Fonction permettant de récupérer le niveau Graphique associé à l'interface Graphique dans les autres classes.
    public NiveauGraphique niv(){
        return niv;
    }

    @Override
    public void run() {
        frame = new JFrame("En Garde");
        niv = new NiveauGraphique(jeu);
        niv.addMouseListener(new AdaptateurSouris(niv, control));
        frame.add(niv);

        Timer chrono = new Timer(16 , new AdaptateurTemps(control));
        chrono.start();

        control.fixerInterfaceGraphique(this);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1100, 660);
        frame.setVisible(true);


        largeurPanel = (frame.getSize().width/2);
        hauteurPanel = (frame.getSize().height);

        pan = new JPanel(){
            protected void paintComponent(Graphics g)
            {
                super.paintComponent(g);

                ImageIcon m = new ImageIcon("res/Menu/MenuGauche.png");
                Image monImage = m.getImage();
                g.drawImage(monImage, 0, 0,  (frame.getSize().width/2), (frame.getSize().height),this);

            }
        };
        pan.setLayout(null);
        pan.setBackground(new Color(0, 0, 0, 255));
        //pan.setBorder(BorderFactory.createBevelBorder(0, Color.yellow, Color.yellow));
        pan.setOpaque(true);

        Niveau = createButton("Nouvelle Partie", "NewPartie");
        Charger = createButton("Charger Une Partie", "ChargePartie");
        Option = createButton("Règles du jeu", "Regles");
        Quitter = createButton("Quitter le jeu", "quit");
        pan.add(Niveau);
        pan.add(Charger);
        pan.add(Option);
        pan.add(Quitter);

        pan.setPreferredSize(new Dimension(largeurPanel, hauteurPanel));

        Regles = new JPanel();
        Regles.setLayout(null);
        Regles.setBackground(new Color(0,0,0));
        Regles.setOpaque(true);

        Regles.setPreferredSize(new Dimension(largeurPanel, (int)Math.round(largeurPanel*0.1)));

        Droit = createButton("  >  ", "Suivant");
        Gauche = createButton("  <  ", "Revenir");
        Retour = createButton("Menu", "RetourMenu");

        Regles.add(Gauche);
        Regles.add(Retour);
        Regles.add(Droit);

        NewPartie = new JPanel(){
            protected void paintComponent(Graphics g)
            {
                super.paintComponent(g);

                ImageIcon m = new ImageIcon("res/Menu/MenuBas.png");
                Image monImage = m.getImage();
                g.drawImage(monImage, 0, 0,  (frame.getSize().width), (frame.getSize().height/2),this);

            }
        };
        NewPartie.setLayout(null);
        NewPartie.setBackground(new Color(0,0,0));
        NewPartie.setOpaque(true);

        NewPartie.setPreferredSize(new Dimension(largeurPanel, (int)Math.round(hauteurPanel*0.5)));

        PvP = createButton("Joueur VS Joueur", "PartieLance");
        PvIAF = createButton("Joueur Vs IA Facile", "PartieIAF");
        PvIAM = createButton("Joueur VS IA Moyen", "PartieIAM");
        Retour2 = createButton("Menu", "RetourMenu");

        NewPartie.add(PvP);
        NewPartie.add(PvIAF);
        NewPartie.add(PvIAM);
        NewPartie.add(Retour2);


    }

    //Fonction pour Ajouter ou enlever le Menu en fonction des pages
    public void InterfaceMenu(){
        if(niv.Menu){
            frame.add(pan, BorderLayout.WEST);
            frame.revalidate();
        }else{
            frame.remove(pan);
            frame.revalidate();
        }
    }

    //Fonction permettant d'ajouter et de supprimer les panels de la page règles
    public void InterfaceRegles(){
        if(niv.Regles){
            niv().compteur = 0;
            frame.add(Regles, BorderLayout.SOUTH);
            frame.revalidate();
        }else{
            frame.remove(Regles);
            frame.revalidate();
        }
    }

    //Fonction permettant d'ajouter et de supprimer les panels de la page New Partie
    public void InterfaceNewPartie(){
        if(niv.NewPartie){
            frame.add(NewPartie, BorderLayout.SOUTH);
            frame.revalidate();
        }else{
            frame.remove(NewPartie);
            frame.revalidate();
        }
    }

    //Fonction Permettant de rendre responsive le panel du menu
    public void MAJPanelMenu(){
        if(largeurPanel != ((pan.getWidth() + getLargeur()) / 2)){

            largeurPanel = (pan.getWidth() + getLargeur()) / 2;
            hauteurPanel = (getHauteur());
            frame.remove(pan);
            frame.revalidate();

            pan.setPreferredSize(new Dimension(largeurPanel, hauteurPanel));

            int largeurBouton = (int)Math.round(largeurPanel * 0.5);
            int hauteurBouton = (int)Math.round(hauteurPanel * 0.18);
            int yBouton = (int)Math.round(hauteurPanel * 0.05);
            int xBouton = (int)Math.round(largeurPanel * 0.25);

            Niveau.setBounds(xBouton, yBouton, largeurBouton, hauteurBouton);
            Charger.setBounds(xBouton, (yBouton*2)+hauteurBouton, largeurBouton, hauteurBouton);
            Option.setBounds(xBouton, ((yBouton*3) + (hauteurBouton*2)), largeurBouton, hauteurBouton);
            Quitter.setBounds(xBouton, ((yBouton*4) + (hauteurBouton*3)), largeurBouton, hauteurBouton);

            frame.add(pan, BorderLayout.WEST);
            frame.revalidate();
        }
    }

    //Fonction permettant de gérer le responsive du menu des règles
    public void MAJPanelRegle(){
        if(largeurPanel != ((pan.getWidth() + getLargeur()) / 2)){

            largeurPanel = (pan.getWidth() + getLargeur()) / 2;
            hauteurPanel = (getHauteur());
            int largeurFenetre = frame.getWidth();

            frame.remove(Regles);


            Regles.setPreferredSize(new Dimension(largeurFenetre, (int)Math.round(largeurFenetre*0.075)));

            int largeurBouton = (int)Math.round(largeurFenetre*0.075);
            int hauteurBouton = (int)Math.round(largeurFenetre*0.075);
            int yBouton = 0;
            int xBouton = (int)Math.round(largeurFenetre*0.3125);

            Gauche.setBounds(xBouton, yBouton, largeurBouton, hauteurBouton);
            Retour.setBounds(xBouton+(largeurBouton*2), yBouton,largeurBouton,hauteurBouton);
            Droit.setBounds(xBouton+(largeurBouton*4), yBouton, largeurBouton, hauteurBouton);

            frame.add(Regles, BorderLayout.SOUTH);
            frame.revalidate();
        }
        if(niv.compteur == 0){
            Gauche.setVisible(false);
        }else{
            Gauche.setVisible(true);
        }
        if(niv.compteur == 5){
            Droit.setVisible(false);
        }else{
            Droit.setVisible(true);
        }
    }

    //Fonction permettant de gérer le responsive du menu des nouvelles parties
    public void MAJPanelNewPartie(){
        if(hauteurPanel != ((NewPartie.getHeight() + getHauteur()) / 2)){

            largeurPanel = getLargeur();
            hauteurPanel = (NewPartie.getHeight() + getHauteur()) / 2;

            System.out.println(largeurPanel+ " " + hauteurPanel);

            frame.remove(NewPartie);
            frame.revalidate();

            NewPartie.setPreferredSize(new Dimension(largeurPanel, hauteurPanel));

            int largeurBouton = (int)Math.round(largeurPanel * 0.25);
            int hauteurBouton = (int)Math.round(hauteurPanel * 0.25);
            int yBouton = (int)Math.round(hauteurPanel * 0.05);
            int xBouton = (int)Math.round(largeurPanel * 0.375);

            PvP.setBounds(xBouton, yBouton, largeurBouton, hauteurBouton);
            PvIAF.setBounds(xBouton, (yBouton*2)+hauteurBouton, largeurBouton, hauteurBouton);
            PvIAM.setBounds(xBouton, ((yBouton*3) + (hauteurBouton*2)), largeurBouton, hauteurBouton);
            Retour2.setBounds(largeurPanel-(largeurBouton/2), hauteurPanel-hauteurBouton, largeurBouton/2, hauteurBouton);

            frame.add(NewPartie, BorderLayout.SOUTH);
            frame.revalidate();
        }
    }

    //Fonction permettant de mettre a jour les booléens permettant de changer l'affichage de la fenêtre
    //en appelant la fonction changeBackground de NiveauGraphique
    public void changeBackground(boolean Menu, boolean Partie, boolean Regles, boolean NewPartie, boolean Victoire, boolean MenuPartie){
        niv.changeBackground(Menu, Partie, Regles, NewPartie, Victoire, MenuPartie);
        InterfaceMenu();
        InterfaceRegles();
        InterfaceNewPartie();
        metAJour();
    }

    //Fonction permettant de faire des boutons avec un hover
    private JButton createButton(String s, String c) {
        JButton but = new JButton(s);
        but.addMouseListener(new java.awt.event.MouseAdapter(){
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                but.setBackground(new Color(255, 222, 06));
                but.setBorder(BorderFactory.createBevelBorder(0, Color.BLACK, Color.BLACK));
                but.setForeground(Color.BLACK);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                but.setBackground(Color.BLACK);
                but.setBorder(BorderFactory.createBevelBorder(0, new Color(255, 222, 06), new Color(255, 222, 06)));
                but.setForeground(new Color(255, 222, 06));
            }
        });
        but.setFont(new Font("Segoe UI Black", Font.BOLD, 18));
        but.addActionListener(new AdaptateurCommande(control, c));
        but.setAlignmentX(Component.CENTER_ALIGNMENT);
        but.setForeground(new Color(255, 222, 06));
        but.setBackground(Color.BLACK);
        but.setBorder(BorderFactory.createBevelBorder(0, new Color(255, 222, 06), new Color(255, 222, 06)));
        //but.setBounds(137, 35+(i*70), 275, 110);
        but.setFocusable(false);
        return but;
    }

    //Fonction permettant d'appeler la fonction repaint de la classe NiveauGraphique
    @Override
    public void metAJour() {
        niv.repaint();
    }

    //Fonction permettant de faire les animations en appelant la fonction animJoueur de NiveauGraphique
    public void animJoueur() {
        niv.animJoueur();
        metAJour();
    }

    //permet de récupérer le booléen pour savoir si on est dans le Menu.
    public boolean getMenu(){
        return niv.Menu;
    }

    //permet de récupérer le booléen pour savoir si on est dans la partie.
    public boolean getPartie(){
        return niv.Partie;
    }

    //permet de récupérer le booléen pour savoir si on est dans la page NewPartie.
    public boolean getNewPartie(){
        return niv.NewPartie;
    }

    //Fonction permettant de récupérer la hauteur de la fenêtre depuis NiveauGraphique
    public int getHauteur(){
        return niv.hauteur;
    }

    //Fonction permettant de récupérer la largeur de la fenêtre depuis NiveauGraphique
    public int getLargeur(){
        return niv.largeur;
    }

    //Fonction permettant de récupérer le booléens Regles de la classe NiveauGraphique
    public boolean getRegles() {
        return niv.Regles;
    }

    //Fonction qui permet d'ouvrir la fenêtre pour sauvegarder une partie.
    public void sauve(){
        if(jeu.partie().manche().peutSauvegarderEtHistorique){
            JFileChooser selecteur = new JFileChooser();
            selecteur.setDialogTitle("Fichier de sauvegarde");
            selecteur.setFileFilter(new FileNameExtensionFilter("Save", "*.cfg"));
            int resultat = selecteur.showSaveDialog(frame);
            if(resultat == JFileChooser.APPROVE_OPTION){
                File fichier = selecteur.getSelectedFile();
                try{
                    OutputStream out = new FileOutputStream(fichier);
                    jeu.sauve(out);
                    out.close();
                } catch (Exception e) {
                    System.err.println("Erreur : "+ e);
                }
            }
        }else{
            System.out.println("Sauvegarde Impossible, Veuillez finir votre tour");
        }

    }

    //Fonction qui permet d'ouvrir la fenêtre pour charger une partie.
    public boolean charge() {
        JFileChooser selecteur = new JFileChooser();
        selecteur.setDialogTitle("Fichier de Sauvegarde");
        int resultat = selecteur.showOpenDialog(frame);
        if(resultat == JFileChooser.APPROVE_OPTION){
            String FileName = selecteur.getSelectedFile().getAbsolutePath();
            jeu.initialisePartieSauve(FileName);

            return true;
        }
        return false;
    }
}
