
package de.maggu2810.playground.osgiplayground.openjfx_test;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;

import javafx.application.Application;

@Component(immediate = true)
public class Setup {

    @Activate
    public void activate() {
        Application.launch(HelloFX.class);
    }

}
