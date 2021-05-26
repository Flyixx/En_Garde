package Structures;

public interface Iterateur<E> {

    boolean aProchain();

    E prochain();

    void supprime();
}
