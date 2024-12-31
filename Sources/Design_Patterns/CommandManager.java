
package Design_Patterns;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class CommandManager {
    private Queue<Command> commandQueue = new LinkedList<>();
    private List<String> commandLog = new ArrayList<>();

    public void addCommand(Command command) {
        commandQueue.add(command);
    }

    public void executeCommands() {
        while (!commandQueue.isEmpty()) {
            Command command = commandQueue.poll();
            command.execute();
            commandLog.add(command.getCommandLog());
        }
    }
        
    public void undoLastCommand() {
         if (!commandQueue.isEmpty()) {
             Command command = commandQueue.poll();
             command.undo();
         }
    }

    public void viewCommandLogs() {
        for (String log : commandLog) {
            System.out.println(log);
         }
    }
}
