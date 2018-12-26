package ru.hitrerros.purse.main;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;


public class FrontendMain {

    private static final int WEB_PORT = 8090;

    private final static String PUBLIC_HTML = "Frontend/src/main/webapp";
    private final static String WEBXML_PATH = "Frontend/src/main/webapp/WEB-INF/web.xml";

    public static void main(String... args) throws Exception{


        Server server = new Server( WEB_PORT );

        WebAppContext webContext = new WebAppContext();
        webContext.setResourceBase(PUBLIC_HTML);
        webContext.setContextPath("/");
        webContext.setDescriptor(WEBXML_PATH );

        server.setHandler(webContext);
        server.start();
        server.join();
    }


}
