package it.polimi.ingsw.Controller;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Command implements Serializable {
    private CommandType commandType;
    Map<String,String> strArgs;
    Map<String,Integer> intArgs;
    Map<String,Object> objArgs;

    /**
    * Class' constructor
    * @author Alessandro Annechini
    * @param commandType Command type
    */
    public Command(CommandType commandType){
        this.commandType=commandType;
        strArgs = new HashMap<>();
        intArgs = new HashMap<>();
        objArgs = new HashMap<>();
    }

    public CommandType getCommandType(){
                return commandType;
        }

    /**
     * This method sets (or adds if not present) an Object parameter with the specified name. The name of the parameter is case-insensitive
     * @author Alessandro Annechini
     * @param name The (case-insensitive) name of the parameter
     * @param value The value of the parameter
     */
    public void setObjParameter(String name, Object value){
        if(name!=null) objArgs.put(name.toLowerCase(),value);
    }

    /**
     * This method sets (or adds if not present) a String parameter with the specified name. The name of the parameter is case-insensitive
     * @author Alessandro Annechini
     * @param name The (case-insensitive) name of the parameter
     * @param value The value of the parameter
     */
    public void setStrParameter(String name, String value){
        if(name!=null) strArgs.put(name.toLowerCase(),value);
    }

    /**
     * This method sets (or adds if not present) an integer parameter with the specified name. The name of the parameter is case-insensitive
     * @author Alessandro Annechini
     * @param name The (case-insensitive) name of the parameter
     * @param value The value of the parameter
     */
    public void setIntParameter(String name, int value){
        if(name!=null) intArgs.put(name.toLowerCase(),value);
    }

    /**
     * This method returns the Object parameter specified by the case-insensitive name
     * @author Alessandro Annechini
     * @param name The (case-insensitive) name of the parameter
     * @return The value of the specified parameter (or null if it doesn't exist)
     */
    public Object getObjParameter(String name){
        if(name==null) return null;
        return objArgs.get(name.toLowerCase());
    }

    /**
     * This method returns the String parameter specified by the case-insensitive name
     * @author Alessandro Annechini
     * @param name The (case-insensitive) name of the parameter
     * @return The value of the specified parameter (or null if it doesn't exist)
     */
    public String getStrParameter(String name){
        if(name==null) return null;
        return strArgs.get(name.toLowerCase());
    }

    /**
     * This method returns the integer parameter specified by the case-insensitive name. If the parameter does not exist, 0 is returned
     * @author Alessandro Annechini
     * @param name The (case-insensitive) name of the parameter
     * @return The value of the specified parameter (or 0 if it doesn't exist)
     */
    public int getIntParameter(String name){
        if(name==null) return 0;
        return intArgs.getOrDefault(name.toLowerCase(),0);
    }
}