package Patterns;


import Structures.Iterateur;
import Structures.Sequence;
import Structures.SequenceListe;

public class Observable {
    Sequence<Observateur> observateurs;

    public Observable() {
        observateurs = new SequenceListe<>() ;
    }

    public void ajouteObservateur(Observateur l) {
        observateurs.insereQueue(l);
    }

    public void miseAJour() {
        Iterateur<Observateur> it;
        it = observateurs.iterateur();
        while (it.aProchain()) {
            it.prochain().metAJour();
        }
    }
}