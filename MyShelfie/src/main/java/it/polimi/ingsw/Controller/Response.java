package main.java.it.polimi.ingsw.Controller;

public class Response {
    ResponseType rt;
    String[] strArgs;
    int[] intArgs;
    Object[] objArgs;

    /**
     * Class' constructor
     * @author Alessandro Annechini
     * @param rt Response type
     * @param strArgs string arguments
     * @param intArgs integer arguments
     * @param objArgs object arguments
     */
    public Response(ResponseType rt, String[] strArgs, int[] intArgs, Object[] objArgs){
        this.rt=rt;
        this.strArgs=strArgs;
        this.intArgs=intArgs;
        this.objArgs=objArgs;
    }
}
