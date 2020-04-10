
package de.maggu2810.playground.osgiplayground.aries_1968;

import java.io.IOException;
import java.time.Instant;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.MultivaluedMap;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.jaxrs.whiteboard.JaxrsWhiteboardConstants;
import org.osgi.service.jaxrs.whiteboard.propertytypes.JaxrsApplicationSelect;
import org.osgi.service.jaxrs.whiteboard.propertytypes.JaxrsExtension;

@Component
@JaxrsExtension
@JaxrsApplicationSelect("(" + JaxrsWhiteboardConstants.JAX_RS_NAME + "=rest)")
public class SimpleResponseFilter implements ContainerResponseFilter {

    private static final AtomicInteger CNT_VALUE = new AtomicInteger();
    private static final AtomicInteger CNT_KEY = new AtomicInteger();

    @Override
    public void filter(final ContainerRequestContext requestContext, final ContainerResponseContext responseContext)
            throws IOException {
        System.out.println("simple response filter");
        final String key = "test-response-filter";
        final MultivaluedMap<String, Object> headers = responseContext.getHeaders();
        final List<Object> values = headers.computeIfAbsent(key, unused -> new LinkedList<>());
        values.add(CNT_VALUE.getAndIncrement());
        headers.put("test-response-filter-" + CNT_KEY.getAndIncrement(),
                Collections.singletonList(Instant.now().toString()));
    }
}