package main.java.it.polimi.ingsw.Controller;

public class Command {
        CommandType ct;
        String[] strArgs;
        int[] intArgs;
        Object[] objArgs;

        /**
         * Class' constructor
         * @author Alessandro Annechini
         * @param ct Command type
         * @param strArgs string arguments
         * @param intArgs integer arguments
         * @param objArgs object arguments
         */
        public Command(CommandType ct, String[] strArgs, int[] intArgs, Object[] objArgs){
                this.ct=ct;
                this.strArgs=strArgs;
                this.intArgs=intArgs;
                this.objArgs=objArgs;
        }
}