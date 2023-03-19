package shgo.innowise.trainee.recordssystem.servlet;


import shgo.innowise.trainee.recordssystem.exception.NotFoundException;
import java.util.HashMap;
import java.util.Map;

/**
 * Routes requests by paths.
 */
public class RoutingHelper {
    private Map<String, RequestHandler> getRequestHandlerMap;
    private Map<String, RequestHandler> postRequestHandlerMap;
    private Map<String, RequestHandler> putRequestHandlerMap;
    private Map<String, RequestHandler> deleteRequestHandlerMap;

    private static RoutingHelper instance;

    private RoutingHelper() {
        getRequestHandlerMap = new HashMap<>();
        postRequestHandlerMap = new HashMap<>();
        putRequestHandlerMap = new HashMap<>();
        deleteRequestHandlerMap = new HashMap<>();
    }

    /**
     * Gives instance of RoutingHelper object.
     *
     * @return instance of RoutingHelper
     */
    public static RoutingHelper getInstance() {
        if (instance == null) {
            instance = new RoutingHelper();
        }

        return instance;
    }

    /**
     * Adds handler with path to GET map.
     *
     * @param path    path
     * @param handler request handler
     */
    public void addGetPath(String path, RequestHandler handler) {
        getRequestHandlerMap.put(path, handler);
    }

    /**
     * Adds handler with path to POST map.
     *
     * @param path    path
     * @param handler request handler
     */
    public void addPostPath(String path, RequestHandler handler) {
        postRequestHandlerMap.put(path, handler);
    }

    /**
     * Adds handler with path to PUT map.
     *
     * @param path    path
     * @param handler request handler
     */
    public void addPutPath(String path, RequestHandler handler) {
        putRequestHandlerMap.put(path, handler);
    }

    /**
     * Adds handler with path to DELETE map.
     *
     * @param path    path
     * @param handler request handler
     */
    public void addDeletePath(String path, RequestHandler handler) {
        deleteRequestHandlerMap.put(path, handler);
    }

    /**
     * Finds GET handler to path.
     *
     * @param path path
     * @return request handler
     */
    public RequestHandler findGetHandler(String path) {
        return getHandler(getRequestHandlerMap, path);
    }

    /**
     * Finds POST handler to path.
     *
     * @param path path
     * @return request handler
     */
    public RequestHandler findPostHandler(String path) {
        return getHandler(postRequestHandlerMap, path);
    }

    /**
     * Finds PUT handler to path.
     *
     * @param path path
     * @return request handler
     */
    public RequestHandler findPuttHandler(String path) {
        return getHandler(putRequestHandlerMap, path);
    }

    /**
     * Finds DELETE handler to path.
     *
     * @param path path
     * @return request handler
     */
    public RequestHandler findDeleteHandler(String path) {
        return getHandler(deleteRequestHandlerMap, path);
    }

    /**
     * Finds handler in request handler map by path.
     *
     * @param requestHandlerMap map with paths and handlers
     * @param path              request path
     * @return request handler
     */
    private RequestHandler getHandler(Map<String, RequestHandler> requestHandlerMap, String path) {
        var match = requestHandlerMap.entrySet().stream()
                .filter(entry -> path.startsWith(entry.getKey()))
                .findFirst();

        return match.map(Map.Entry::getValue).orElseThrow(() -> new NotFoundException("Not Found"));
    }
}
