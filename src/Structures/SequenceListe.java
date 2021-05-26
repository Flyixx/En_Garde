package Structures;

public class SequenceListe<E> implements Sequence<E>{
    Maillon<E> tete, queue;

    @Override
    public void insereTete(E element) {
        Maillon<E> nouveau = new Maillon<>(element, tete);
        if(tete == null) {
            queue = nouveau;
        }
        tete = nouveau;
    }

    @Override
    public void insereQueue(E element) {
        Maillon<E> nouveau = new Maillon<>(element, null);
        if(queue == null){
            tete = nouveau;
            queue = nouveau;
        }else{
            queue.suivant = nouveau;
            queue = nouveau;
        }
    }

    @Override
    public E extraitTete() {
        if(tete == null){
            throw new RuntimeException("Sequence Vide !");
        }
        E resultat = tete.element;
        tete = tete.suivant;
        if(tete == null){
            queue = null;
        }
        return resultat;
    }

    @Override
    public boolean estVide() {
        return tete == null;
    }

    @Override
    public Iterateur<E> iterateur() {
        return new IterateurListe<E>(this);
    }

    public String toString(){
        String resultat = "SequenceListe [ ";
        Maillon courant = tete;
        while(courant != null){
            resultat += courant.element + " ";
            courant = courant.suivant;
        }
        resultat += "]";
        return resultat;
    }
}
