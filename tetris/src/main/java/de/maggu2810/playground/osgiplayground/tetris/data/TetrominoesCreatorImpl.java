
package de.maggu2810.playground.osgiplayground.tetris.data;

import java.util.function.Supplier;

public class TetrominoesCreatorImpl<T extends Tetrominoes<T>> implements TetrominoesCreator<T> {

    private final Class<T> type;
    private final Supplier<T> supp;

    public TetrominoesCreatorImpl(final Class<T> type, final Supplier<T> supp) {
        this.type = type;
        this.supp = supp;
    }

    @Override
    public Class<T> getType() {
        return type;
    }

    @Override
    public T create() {
        return supp.get();
    }

}
