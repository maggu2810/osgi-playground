
package de.maggu2810.playground.osgiplayground.aries_1968;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.sse.Sse;
import javax.ws.rs.sse.SseEventSink;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.jaxrs.whiteboard.JaxrsWhiteboardConstants;
import org.osgi.service.jaxrs.whiteboard.propertytypes.JaxrsApplicationSelect;
import org.osgi.service.jaxrs.whiteboard.propertytypes.JaxrsName;
import org.osgi.service.jaxrs.whiteboard.propertytypes.JaxrsResource;

@Component(service = { RESTResource.class })
@JaxrsResource
@JaxrsName("foo")
@JaxrsApplicationSelect("(" + JaxrsWhiteboardConstants.JAX_RS_NAME + "=rest)")
@Path("/foo")
public class RESTResource {
    @GET
    @Path("normal")
    public Response normal() {
        System.out.println("handle endpoint \"normal\"");
        return Response.ok().build();
    }

    @GET
    @Path("sse")
    @Produces(MediaType.SERVER_SENT_EVENTS)
    public void sse(@Context final SseEventSink eventSink, @Context final Sse sse) {
        System.out.println("handle endpoint \"sse\"");
        final ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try (SseEventSink sink = eventSink) {
                eventSink.send(sse.newEvent("event1"));
                eventSink.send(sse.newEvent("event2"));
            }
        });
        executor.shutdown();
    }
}
