
package de.maggu2810.playground.osgiplayground.tetris.data;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TetrominoesI extends TetrominoesBase<TetrominoesI> {

    private static final List<Block> BLOCKS = Stream.of( //
            new BlockUnmodifiable(3, 0), //
            new BlockUnmodifiable(4, 0), //
            new BlockUnmodifiable(5, 0), //
            new BlockUnmodifiable(6, 0) //
    ).collect(Collectors.toUnmodifiableList());

    private static void rotate(final int rotation, final int lowX, final int lowY, final List<BlockModifiable> blocks) {
        for (final BlockModifiable i : blocks) {
            if (rotation == 0) {
                i.setLocation(i.getY() - lowY + lowX + 2, i.getX() - lowX + lowY - 1);
            } else if (rotation == 1) {
                i.setLocation(i.getY() - lowY + lowX - 2, i.getX() - lowX + lowY + 2);
            } else if (rotation == 2) {
                i.setLocation(i.getY() - lowY + lowX + 1, i.getX() - lowX + lowY - 2);
            } else {
                i.setLocation(i.getY() - lowY + lowX - 1, i.getX() - lowX + lowY + 1);
            }
        }
    }

    public TetrominoesI() {
        super(TetrominoesI.class, TetrominoesI::rotate, BLOCKS);
    }

}
