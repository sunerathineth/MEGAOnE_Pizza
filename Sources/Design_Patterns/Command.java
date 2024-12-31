
package Design_Patterns;

public interface Command {
    void execute();
    void undo();
    String getCommandLog();
}
