package Vue;

public interface CollecteurEvenements {
    void tictac();
    void clickCarte(int x, int y);
    void clickDeplacement(int x, int y);
    void clickChangeTour(int x, int y);
    boolean commande(String c);
    void fixerInterfaceGraphique(InterfaceGraphique i);
    InterfaceGraphique inter();
    void clickChange(int x, int y);
    void clickQuitterJeu(int x, int y);
    void clickSauvegarder(int x, int y);
    void clickAnnuler(int x, int y);
    void clickRefaire(int x, int y);
    void clickMute(int x, int y);
    void clickRevenirPartie(int x, int y);

    void clickMenuPartie(int x, int y);
}
