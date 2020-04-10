
package de.maggu2810.playground.osgiplayground.tetris.data;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TetrominoesJ extends TetrominoesBase<TetrominoesJ> {

    private static final List<Block> BLOCKS = Stream.of( //
            new BlockUnmodifiable(3, 0), //
            new BlockUnmodifiable(4, 1), //
            new BlockUnmodifiable(5, 1), //
            new BlockUnmodifiable(3, 1) //
    ).collect(Collectors.toUnmodifiableList());

    private static void rotate(final int rotation, final int lowX, final int lowY, final List<BlockModifiable> blocks) {
        for (final BlockModifiable i : blocks) {
            if (rotation == 1 || rotation == 2) {
                i.setLocation(2 - (i.getY() - lowY) + lowX - 1, i.getX() - lowX - 1 + rotation % 2 * 2 + lowY);
            } else {
                i.setLocation(2 - (i.getY() - lowY) + lowX, i.getX() - lowX + lowY);
            }
        }
    }

    public TetrominoesJ() {
        super(TetrominoesJ.class, TetrominoesJ::rotate, BLOCKS);
    }

}
