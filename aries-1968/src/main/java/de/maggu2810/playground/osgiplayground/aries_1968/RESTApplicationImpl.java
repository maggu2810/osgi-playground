
package de.maggu2810.playground.osgiplayground.aries_1968;

import javax.ws.rs.core.Application;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.jaxrs.whiteboard.propertytypes.JaxrsApplicationBase;
import org.osgi.service.jaxrs.whiteboard.propertytypes.JaxrsName;

@Component(service = Application.class)
@JaxrsName("rest")
@JaxrsApplicationBase("rest")
public class RESTApplicationImpl extends Application {
}