
package de.maggu2810.playground.osgiplayground.aries_1965;

import javax.ws.rs.GET;
import javax.ws.rs.core.Response;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ServiceScope;
import org.osgi.service.jaxrs.whiteboard.JaxrsWhiteboardConstants;
import org.osgi.service.jaxrs.whiteboard.propertytypes.JaxrsApplicationSelect;
import org.osgi.service.jaxrs.whiteboard.propertytypes.JaxrsResource;

@Component(service = { ResourceImpl.class }, scope = ServiceScope.PROTOTYPE)
@JaxrsResource
@JaxrsApplicationSelect("(" + JaxrsWhiteboardConstants.JAX_RS_NAME + "=myApp)")
public class ResourceImpl {
    @GET
    public Response get() {
        return Response.ok().build();
    }
}
