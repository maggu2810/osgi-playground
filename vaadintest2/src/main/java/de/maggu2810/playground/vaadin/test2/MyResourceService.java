
package de.maggu2810.playground.vaadin.test2;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.http.whiteboard.propertytypes.HttpWhiteboardResource;

@Component(service = MyResourceService.class)
@HttpWhiteboardResource(pattern = "/frontend/*", prefix = "/frontend")
public class MyResourceService {
}
