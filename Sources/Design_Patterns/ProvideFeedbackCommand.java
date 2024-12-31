
package Design_Patterns;

import Components.DataRepository;
import Components.Feedback;

public class ProvideFeedbackCommand implements Command {
    private Feedback feedback;
    private DataRepository repository;

    public ProvideFeedbackCommand(Feedback feedback, DataRepository repository) {
        this.feedback = feedback;
        this.repository = repository;
    }

    @Override
    public void execute() {
        System.out.println("Submitting feedback...");
        repository.addFeedback(feedback);
        System.out.println("Feedback submitted successfully:\n" + feedback);
    }

    @Override
    public void undo() {
        System.out.println("Undoing feedback submission...");
        repository.removeFeedback(feedback.getFeedbackID());
    }

    @Override
    public String getCommandLog() {
        return "Provide Feedback Command: FeedbackID - " + feedback.getFeedbackID();
    }
}
