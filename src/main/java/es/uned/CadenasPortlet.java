package es.uned;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.portlet.GenericPortlet;
import javax.portlet.HeaderRequest;
import javax.portlet.HeaderResponse;
import javax.portlet.PortletException;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.ProcessAction;
import javax.portlet.annotations.ActionMethod;
import javax.servlet.http.Part;

public class CadenasPortlet extends GenericPortlet {

	private static final Logger logger = LoggerFactory.getLogger(CadenasPortlet.class);

        private List<Linea> lineasFichero = new ArrayList<Linea>();
        
        private int i;
        
	public void renderHeaders(HeaderRequest headerRequest, HeaderResponse headerResponse)
		throws PortletException, IOException {

		String contextPath = headerRequest.getContextPath();
		String linkMarkup = "<link rel=\"stylesheet\" type=\"text/css\" href=\"" +
			contextPath + "/resources/css/main.css\"></link>";
		headerResponse.addDependency("main.css", "es.uned", null, linkMarkup);
		logger.debug("[HEADER_PHASE] Added resource: " + linkMarkup);
	}

	protected void doView(RenderRequest renderRequest, RenderResponse renderResponse)
		throws PortletException, IOException {  
            switch (i) {
                case 0:
                    {
                        renderRequest.getPortletSession().setAttribute("errorFormato","");
                        String viewPath = "/WEB-INF/views/portletViewMode.jspx";
                        PortletRequestDispatcher portletRequestDispatcher =
                                getPortletContext().getRequestDispatcher(viewPath);
                        portletRequestDispatcher.include(renderRequest, renderResponse);
                        logger.debug("[RENDER_PHASE] Rendering view: " + viewPath);
                        break;
                    }
                case 1:
                    {
                        renderRequest.getPortletSession().setAttribute("errorFormato","Error en el formato del fichero");
                        String viewPath = "/WEB-INF/views/portletViewMode.jspx";
                        PortletRequestDispatcher portletRequestDispatcher =
                                getPortletContext().getRequestDispatcher(viewPath);
                        portletRequestDispatcher.include(renderRequest, renderResponse);
                        break;
                    }
                default:
                    break;
            }
	}
        
        @ActionMethod(portletName = "cadenas")
        @ProcessAction(name = "SubirArchivoAction")
	public void SubirArchivoAction(ActionRequest req, ActionResponse resp) 
                throws IOException, PortletException {
            lineasFichero = new ArrayList<Linea>();
            Part fichero = req.getPart("fichero");
            i = 0;
            if (fichero != null) {
                InputStream streamFichero = fichero.getInputStream();
                BufferedReader buffer = new BufferedReader(new InputStreamReader(streamFichero));
                String separador = "\\s\\\\,\\s";
                String barra = "\\\\";
                String linea;
                String cadena1;
                String cadena2;                
                linea = buffer.readLine();
                while (linea != null) {
                    String[] lineas = linea.split(separador);
                    if (lineas.length != 2) {
                        i = 1;
                        req.getPortletSession().setAttribute("lineasFichero",lineasFichero);
                        throw new PortletException();
                    } else {
                        Pattern p = Pattern.compile(barra + barra);
                        Matcher m = p.matcher (lineas[0]);
                        cadena1 = m.replaceAll(barra);
                        m = p.matcher (lineas[1]);
                        cadena2 = m.replaceAll(barra);
                    }
                    Linea l = new Linea();
                    l.setCadena1(cadena1);
                    l.setCadena2(cadena2);
                    lineasFichero.add(l);
                    linea = buffer.readLine();
                }
            req.getPortletSession().setAttribute("lineasFichero",lineasFichero);
            }
        }
        
        public List<Linea> getLineasFichero() {
            return lineasFichero;
        }
   
}