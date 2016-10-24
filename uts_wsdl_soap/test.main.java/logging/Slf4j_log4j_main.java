package logging;

import org.apache.log4j.PropertyConfigurator; 
import org.slf4j.LoggerFactory;
import org.slf4j.impl.Log4jLoggerAdapter;


public class Slf4j_log4j_main {

    private static Log4jLoggerAdapter log = (Log4jLoggerAdapter) LoggerFactory.getLogger(Slf4j_log4j_main.class);

    public static void main(String[] args) {
        PropertyConfigurator.configure(Slf4j_log4j_main.class.getClassLoader().getResource("cfg/log4j.properties"));
        log.debug( "a debug" );
        log.info( "an info" );
        log.warn("a warn");
        log.error("an error");
        //log.fatal("a fatal");  // slf4j misses fatal log.
        log.trace("a fatal");
        System.out.println("");
        System.out.println("[INFO]: done");
    }
}