
package de.maggu2810.playground.vaadin.test2.osgi;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.http.whiteboard.HttpWhiteboardConstants;
import org.osgi.service.http.whiteboard.propertytypes.HttpWhiteboardFilterAsyncSupported;
import org.osgi.service.http.whiteboard.propertytypes.HttpWhiteboardServletPattern;

import com.vaadin.flow.server.VaadinServlet;

@Component(service = Servlet.class, /* scope = ServiceScope.PROTOTYPE */
        property = { //
                HttpWhiteboardConstants.HTTP_WHITEBOARD_SERVLET_INIT_PARAM_PREFIX + "compatibilityMode:String=true" //
        })
// @HttpWhiteboardContextSelect("(" + HttpWhiteboardConstants.HTTP_WHITEBOARD_CONTEXT_NAME + "="
// + ServletContextHelperImpl.NAME + ")")
@HttpWhiteboardFilterAsyncSupported
@HttpWhiteboardServletPattern("/gui/*")
public class FixedVaadinServlet extends VaadinServlet {

    @Override
    public void init(final ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);
        getService().setClassLoader(getClass().getClassLoader());
    }

}
