package smu.DTO;


public class Wallet {

	//Attributes
    private int id_wallet;
    private String walletName;
    private String walletCategory;
    private int totalAmount;
    private String ownerEmail;




	//Constructor
    public Wallet(int id_wallet, String walletName, String walletCategory, int totalAmount, String ownerEmail) {
        this.id_wallet = id_wallet;
        this.walletName = walletName;
        this.walletCategory = walletCategory;
        this.totalAmount = totalAmount;
        this.ownerEmail = ownerEmail;
    }

    //Getters and Setters
    
    public int getId_wallet() {
        return id_wallet;
    }

    public void setId_wallet(int id_wallet) {
        this.id_wallet = id_wallet;
    }

    public String getWalletName() {
        return walletName;
    }

    public void setWalletName(String walletName) {
        this.walletName = walletName;
    }

    public String getWalletCategory() {
        return walletCategory;
    }

    public void setWalletCategory(String walletCategory) {
        this.walletCategory = walletCategory;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    @Override
    public String toString() {
        return "Wallet [id_wallet=" + id_wallet + ", walletName=" + walletName + ", walletCategory=" + walletCategory
                + ", totalAmount=" + totalAmount + "]";
    }

	public String getOwnerEmail() {
		return ownerEmail;
	}

    public void setOwnerEmail(String ownerEmail) {
        this.ownerEmail = ownerEmail;
    }

}
