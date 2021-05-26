package Structures;

public class IterateurListe<E> implements Iterateur<E>{
    Maillon<E> courant, precedent, arrierePrecedent;
    SequenceListe<E> s;
    boolean supprime = false;

    IterateurListe(SequenceListe l){
        courant = l.tete;
        s = l;
    }

    @Override
    public boolean aProchain() {
        return courant != null;
    }

    @Override
    public E prochain() {
        E resultat = courant.element;
        arrierePrecedent = precedent;
        precedent = courant;
        courant = courant.suivant;
        supprime = true;
        return resultat;
    }

    @Override
    public void supprime() {
        if(supprime){
            if(arrierePrecedent == null){
                s.tete = courant;
            }else{
                arrierePrecedent.suivant = courant;
            }
            precedent = arrierePrecedent;
            supprime = false;
        }else{
            throw new IllegalStateException("Deux suppression d'affilé !");
        }
    }
}
