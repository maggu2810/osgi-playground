
package de.maggu2810.playground.osgiplayground.tetris.data;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

public class ResourceProviderImpl implements ResourceProvider {

    private final Bundle bundle;
    private final Path pathTmpDir;

    public ResourceProviderImpl() {
        bundle = FrameworkUtil.getBundle(ResourceProviderImpl.class);
        try {
            pathTmpDir = Files.createTempDirectory("tetris");
        } catch (final IOException ex) {
            throw new IllegalStateException(ex);
        }
    }

    private URL toFileURL(final URL url, final String pathHint) {
        final String suffix = "_" + Paths.get(pathHint).getFileName().toString();
        final Path filePath;
        try {
            filePath = Files.createTempFile(pathTmpDir, "tetris", suffix);
        } catch (final IOException ex) {
            throw new IllegalStateException(ex);
        }
        try (InputStream is = url.openStream()) {
            Files.write(filePath, is.readAllBytes());
        } catch (final IOException ex) {
            throw new IllegalStateException(ex);
        }
        try {
            return filePath.toUri().toURL();
        } catch (final MalformedURLException ex) {
            throw new IllegalStateException(ex);
        }
    }

    private URL bundleEntryToFileURL(final String path) {
        return toFileURL(bundle.getEntry(path), path);
    }

    @Override
    public URL imageGameOver() {
        return bundleEntryToFileURL("rsc/gameOver.png");
    }

    @Override
    public URL imagePause() {
        return bundleEntryToFileURL("rsc/pause.png");
    }

    @Override
    public URL imageTetris() {
        return bundleEntryToFileURL("rsc/tetris.png");
    }

    @Override
    public URL mediaGameOver() {
        // return bundleEntryToFileURL("rsc/gameOver.mp3");
        return bundleEntryToFileURL("rsc/gameOver.wav");
    }

    @Override
    public URL mediaTetris() {
        // return bundleEntryToFileURL("rsc/Tetris.mp3");
        return bundleEntryToFileURL("rsc/Tetris.wav");
    }

    @Override
    public URL stylesheetApplication() {
        return bundleEntryToFileURL("rsc/application.css");
    }

}
