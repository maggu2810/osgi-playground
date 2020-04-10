
package de.maggu2810.playground.osgiplayground.tetris.data;

import java.util.List;

/**
 * This interface represents the "tetrominos" used in Tetris.
 *
 * <p>
 * {@link "https://tetris.fandom.com/wiki/Tetromino"}
 *
 * @param <T> the specific type
 */
public interface Tetrominoes<T extends Tetrominoes<T>> {

    /**
     * Gets the tetrominoes type.
     *
     * @return the type
     */
    Class<T> getType();

    List<BlockUnmodifiable> getBlocks();

    List<BlockUnmodifiable> getRotatedBlocks();

    void rotate();

    void moveDown();

    void moveLeft();

    void moveRight();

}
