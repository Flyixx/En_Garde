package Structures;

public class Maillon<E> {
    E element;
    Maillon suivant;

    Maillon(E element, Maillon<E> suivant){
        this.element = element;
        this.suivant = suivant;
    }


}