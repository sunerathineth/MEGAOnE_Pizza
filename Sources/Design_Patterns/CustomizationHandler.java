package Design_Patterns;

import Components.*;

public abstract class CustomizationHandler {
    protected CustomizationHandler nextHandler;
    
    public void setNextHandler(CustomizationHandler nextHandler) {
        this.nextHandler = nextHandler;
    }
    
    public abstract void handleRequest(CustomizedPizza pizza, String customization);
}