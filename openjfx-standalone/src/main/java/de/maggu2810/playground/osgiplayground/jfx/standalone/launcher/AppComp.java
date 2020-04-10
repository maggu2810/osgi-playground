
package de.maggu2810.playground.osgiplayground.jfx.standalone.launcher;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import de.maggu2810.playground.osgiplayground.jfx.standalone.main.HelloFX;

/**
 * The bundle activator that will launch a pseudo JavaFX application and hold the primary stage.
 */
@Component
public class AppComp {

    @Activate
    public AppComp(
            final @Reference(target = "(launcher.ready=true)") ServiceReference<Object> launcherRuntimeInformation,
            final BundleContext bc) {
        final Object launcherArgs = launcherRuntimeInformation.getProperty("launcher.arguments");
        final String[] args;
        if (launcherArgs == null) {
            args = null;
        } else if (launcherArgs.getClass().isArray()) {
            final Object[] argsTmp = (Object[]) launcherArgs;
            if (argsTmp.length == 0) {
                args = new String[0];
            } else {
                if (argsTmp[0] instanceof String) {
                    args = (String[]) argsTmp;
                } else {
                    throw new IllegalStateException(
                            "The arguments are provided by something different then a string array.");
                }
            }
            // } else if (launcherArgs instanceof String) {
            // args = new String[] { (String) launcherArgs };
        } else {
            throw new IllegalStateException("The arguments are provided by something different then a string array.");
        }

        for (final String key : launcherRuntimeInformation.getPropertyKeys()) {
            final Object value = launcherRuntimeInformation.getProperty(key);
            System.out.printf("%s=%s%n", key, value);
        }

        AppUtils.launch(bc, HelloFX.class, args);
    }

}
