
package de.maggu2810.playground.osgiplayground.aries_1968;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.jaxrs.whiteboard.JaxrsWhiteboardConstants;
import org.osgi.service.jaxrs.whiteboard.propertytypes.JaxrsApplicationSelect;
import org.osgi.service.jaxrs.whiteboard.propertytypes.JaxrsExtension;

@Component
@JaxrsExtension
@JaxrsApplicationSelect("(" + JaxrsWhiteboardConstants.JAX_RS_NAME + "=rest)")
public class SimpleRequestFilter implements ContainerRequestFilter {
    @Override
    public void filter(final ContainerRequestContext context) {
        System.out.println("simple request filter");
    }
}
