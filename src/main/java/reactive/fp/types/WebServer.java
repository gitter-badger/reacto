package reactive.fp.types;

/**
 * @author Cipolinas on 2015.11.16.
 */
public interface WebServer {

    void start();

    void stop();

    void setupRoutes();

}