package Structures;

import Structures.Iterateur;

public interface Sequence<E> {

    void insereTete(E element);

    void insereQueue(E element);

    E extraitTete();

    boolean estVide();

    Iterateur<E> iterateur();
}
