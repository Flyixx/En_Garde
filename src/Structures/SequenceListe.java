package Structures;

public class SequenceListe<Titi> implements Sequence<Titi> {
    Maillon<Titi> tete, queue;

    public void insereTete(Titi element) {
        Maillon<Titi> nouveau = new Maillon<>();
        nouveau.element = element;
        nouveau.suivant = tete;
        if (tete == null) {
            tete = nouveau;
            queue = nouveau;
        } else {
            tete = nouveau;
        }
    }

    public void insereQueue(Titi element) {
        Maillon<Titi> nouveau = new Maillon<>();
        nouveau.element = element;
        nouveau.suivant = null;
        if (tete == null) {
            tete = nouveau;
            queue = nouveau;
        } else {
            queue.suivant = nouveau;
            queue = nouveau;
        }
    }

    public Titi extraitTete() {
        if (tete == null)
            throw new RuntimeException("Sequence vide !");
        Titi resultat = tete.element;
        tete = tete.suivant;
        // Ici, oubli de la mise à jour de la queue probablement sans conséquences :
        // la queue n'est incohérente qu'en cas de liste vide, dans ce cas pas d'itération
        // possible sur ses éléments et tout sera remis en cohérence à la prochaine insertion
        return resultat;
    }

    public boolean estVide() {
        return tete == null;
    }

    @Override
    public Iterateur<Titi> iterateur() {
        return new IterateurListe<>(this);
    }

    public String toString() {
        String resultat = "Sequence liste : [ ";
        Maillon<Titi> courant = tete;
        while (courant != null) {
            resultat += courant.element + " ";
            courant = courant.suivant;
        }
        resultat += "]";
        return resultat;
    }
}


/*public class SequenceListe<E> implements Sequence<E>{
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
}*/


