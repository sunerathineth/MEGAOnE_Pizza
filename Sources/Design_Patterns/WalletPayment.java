
package Design_Patterns;

public class WalletPayment implements PaymentStrategy {
    private String walletID;
    
    public WalletPayment(String walletID) {
        this.walletID = walletID;
    }
    
    @Override
    public void pay(double amount) {
        System.out.println("Paid Rs." + amount + " using Digital Wallet (ID:" + walletID + ").");
    }
}
