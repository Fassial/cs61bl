/**
 * This class represents a bank account whose current balance is a nonnegative
 * amount in US dollars.
 */
public class Account {

    public int balance;
	public Account parentAccount;

    /** Initialize an account with the given balance. */
    public Account(int balance) {
        this.balance = balance;
		this.parentAccount = null;
    }
	
	public Account(int balance, Account parentAccount) {
		this.balance = balance;
		this.parentAccount = parentAccount;
	}

    /** Deposits amount into the current account. */
    public void deposit(int amount) {
        if (amount < 0) {
            System.out.println("Cannot deposit negative amount.");
        } else {
            balance += amount;
        }
    }

    /**
     * Subtract amount from the account if possible. If subtracting amount
     * would leave a negative balance, print an error message and leave the
     * balance unchanged.
     */
    public boolean withdraw(int amount) {
        // TODO
        if (amount < 0) {
            System.out.println("Cannot withdraw negative amount.");
			return false;
        } else if ((this.parentAccount != null && this.balance + this.parentAccount.balance < amount) || (this.parentAccount == null && this.balance < amount)) {
            System.out.println("Insufficient funds");
			return false;
        } else {
            if (this.parentAccount != null && this.balance < amount){
				System.out.println("His own account is less than amount, so withdraw the amount from both his and his parent's!");
				this.parentAccount.balance -= (amount - this.balance);
				this.balance = 0;
			} else {
				System.out.println("His own account is greater than amount, so withdraw the amount from his account!");
				this.balance -= amount;
			}
			return true;
        }
    }

    /**
     * Merge account other into this account by removing all money from other
     * and depositing it into this account.
     */
    public void merge(Account other) {
        // TODO
		this.balance += other.balance;
		other.balance = 0;
    }
	
	public static void main(String[] args) {
		Account christine = new Account(500);
		Account matt = new Account(100, christine);
		matt.withdraw(50);
		matt.withdraw(200);
		matt.withdraw(700);
	}
}
