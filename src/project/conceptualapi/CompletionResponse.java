package project.conceptualapi;

/** Response from job finalization */
public interface CompletionResponse {
    /** Get finalization status */
    OperationStatus getStatus();
    
    /** Get completion message */
    String getMessage();
    
    /** Get total data units processed */
    int getTotalUnitsProcessed();
    
    /** Get any final results summary */
    String getSummary();
    
    /** Get final computation outcome */
    ComputationOutcome getOutcome();
}