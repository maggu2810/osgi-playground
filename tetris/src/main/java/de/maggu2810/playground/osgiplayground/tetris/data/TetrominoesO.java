
package de.maggu2810.playground.osgiplayground.tetris.data;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TetrominoesO extends TetrominoesBase<TetrominoesO> {

    private static final List<Block> BLOCKS = Stream.of( //
            new BlockUnmodifiable(4, 0), //
            new BlockUnmodifiable(5, 0), //
            new BlockUnmodifiable(4, 1), //
            new BlockUnmodifiable(5, 1) //
    ).collect(Collectors.toUnmodifiableList());

    private static void rotate(final int rotation, final int lowX, final int lowY, final List<BlockModifiable> blocks) {
    }

    public TetrominoesO() {
        super(TetrominoesO.class, TetrominoesO::rotate, BLOCKS);
    }

}
