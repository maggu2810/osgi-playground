
package de.maggu2810.playground.osgiplayground.tetris.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class TetrominoesRegistryImpl {

    private final Random rand = new Random();
    private final List<TetrominoesCreator<?>> creators;

    public TetrominoesRegistryImpl() {
        final List<TetrominoesCreator<?>> creators = new ArrayList<>();
        creators.add(new TetrominoesCreatorImpl<>(TetrominoesI.class, TetrominoesI::new));
        creators.add(new TetrominoesCreatorImpl<>(TetrominoesJ.class, TetrominoesJ::new));
        creators.add(new TetrominoesCreatorImpl<>(TetrominoesL.class, TetrominoesL::new));
        creators.add(new TetrominoesCreatorImpl<>(TetrominoesO.class, TetrominoesO::new));
        creators.add(new TetrominoesCreatorImpl<>(TetrominoesS.class, TetrominoesS::new));
        creators.add(new TetrominoesCreatorImpl<>(TetrominoesT.class, TetrominoesT::new));
        creators.add(new TetrominoesCreatorImpl<>(TetrominoesZ.class, TetrominoesZ::new));
        this.creators = Collections.unmodifiableList(creators);
    }

    public TetrominoesCreator<?> randomCreator() {
        return creators.get(rand.nextInt(creators.size()));
    }

}
