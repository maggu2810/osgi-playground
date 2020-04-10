
package de.maggu2810.playground.osgiplayground.tetris.data;

public interface TetrominoesCreator<T extends Tetrominoes<T>> {

    Class<T> getType();

    T create();

}
