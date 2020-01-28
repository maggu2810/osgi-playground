
package de.maggu2810.playground.osgiplayground.aries_1965;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ServiceScope;
import org.osgi.service.http.whiteboard.propertytypes.HttpWhiteboardServletPattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = { Servlet.class }, scope = ServiceScope.PROTOTYPE)
@HttpWhiteboardServletPattern("/servlet")
public class ServletImpl extends HttpServlet {

    private final Logger logger = LoggerFactory.getLogger(ServletImpl.class);

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            final XMLInputFactory tmp = XMLInputFactory.newInstance();
            logger.info("XML input factory succeeded");
        } catch (final Throwable th) {
            logger.error("XML input factory failed.", th);
        }
        try {
            final XMLOutputFactory tmp = XMLOutputFactory.newInstance();
            logger.info("XML output factory succeeded");
        } catch (final Throwable th) {
            logger.error("XML output factory failed.", th);
        }
        try {
            final XMLEventFactory tmp = XMLEventFactory.newInstance();
            logger.info("XML event factory succeeded");
        } catch (final Throwable th) {
            logger.error("XML event factory failed.", th);
        }
    }

}
