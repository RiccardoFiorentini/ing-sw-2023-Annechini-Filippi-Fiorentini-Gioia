package main.java.it.polimi.ingsw.Controller;

public class Command {
        private CommandType commandType;
        String[] strArgs;
        int[] intArgs;
        Object[] objArgs;

        /**
         * Class' constructor
         * @author Alessandro Annechini
         * @param commandType Command type
         * @param strArgs string arguments
         * @param intArgs integer arguments
         * @param objArgs object arguments
         */
        public Command(CommandType commandType, String[] strArgs, int[] intArgs, Object[] objArgs){
                this.commandType=commandType;
                this.strArgs=strArgs;
                this.intArgs=intArgs;
                this.objArgs=objArgs;
        }

        public CommandType getCommandType(){
                return commandType;
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