package Laba9.first;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class BankOperations
{
	private final List<BankAccount> accounts = new ArrayList<>();
	public void transfer(final BankAccount from, final BankAccount to, double amount)
	{
		if (from != to && amount > 0)
		{
			boolean wasWithdrawn = from.withdrawMoney(amount);

			if (wasWithdrawn)
			{
				to.addMoney(amount);

				System.out.println("Переведено " + amount + "$ з акаунту " + from.getId() + " на акаунт " + to.getId());
			}
		}
	}

	public BigDecimal totalMoney()
	{
		BigDecimal total = BigDecimal.valueOf(0);

		for (BankAccount account : accounts) // додавання баланс кожного рахунку в accounts
		{
			total = total.add(account.getMoney());
		}
		return total;
	}


	public void addAccount(BankAccount account)
	{
		accounts.add(account);
	}

	public List<BankAccount> getAccounts()
	{
		return accounts;
	}
}
