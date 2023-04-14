package main.java.it.polimi.ingsw.Controller;

public class Response {
    ResponseType responseType;
    String[] strArgs;
    int[] intArgs;
    Object[] objArgs;

    /**
     * Class' constructor
     * @author Alessandro Annechini
     * @param responseType Response type
     * @param strArgs string arguments
     * @param intArgs integer arguments
     * @param objArgs object arguments
     */
    public Response(ResponseType responseType, String[] strArgs, int[] intArgs, Object[] objArgs){
        this.responseType=responseType;
        this.strArgs=strArgs;
        this.intArgs=intArgs;
        this.objArgs=objArgs;
    }

    public ResponseType getResponseType(){
        return responseType;
    }

    public String[] getStrArgs(){
        return strArgs;
    }

    public int[] getIntArgs(){
        return intArgs;
    }

    public Object[] getObjArgs(){
        return objArgs;
    }
}
