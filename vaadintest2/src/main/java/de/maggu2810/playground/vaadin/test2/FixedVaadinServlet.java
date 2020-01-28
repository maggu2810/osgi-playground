
package de.maggu2810.playground.vaadin.test2;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.http.whiteboard.propertytypes.HttpWhiteboardFilterAsyncSupported;
import org.osgi.service.http.whiteboard.propertytypes.HttpWhiteboardServletPattern;

import com.vaadin.flow.server.VaadinServlet;

@Component(service = Servlet.class /* , scope = ServiceScope.PROTOTYPE */)
@HttpWhiteboardFilterAsyncSupported
@HttpWhiteboardServletPattern("/vaadin/gui/*")
public class FixedVaadinServlet extends VaadinServlet {

    @Override
    public void init(final ServletConfig servletConfig) throws ServletException {
        System.setProperty("vaadin.compatibilityMode", "true");
        super.init(servletConfig);
        getService().setClassLoader(getClass().getClassLoader());
    }
}
