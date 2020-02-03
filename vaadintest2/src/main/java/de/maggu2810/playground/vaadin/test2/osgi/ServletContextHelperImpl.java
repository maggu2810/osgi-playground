
package de.maggu2810.playground.vaadin.test2.osgi;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ServiceScope;
import org.osgi.service.http.context.ServletContextHelper;
import org.osgi.service.http.whiteboard.HttpWhiteboardConstants;
import org.osgi.service.http.whiteboard.propertytypes.HttpWhiteboardContext;

@Component(service = ServletContextHelper.class, scope = ServiceScope.BUNDLE, //
        property = { //
                HttpWhiteboardConstants.HTTP_WHITEBOARD_CONTEXT_INIT_PARAM_PREFIX + "vaadin3.compatibilityMode=true" //
        })
@HttpWhiteboardContext(name = ServletContextHelperImpl.NAME, path = "/vaadin")
public class ServletContextHelperImpl extends ServletContextHelper {
    public static final String NAME = "my-vaadin-test";
}