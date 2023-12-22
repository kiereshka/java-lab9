package Laba9.first;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;

public class BankAccount
{
	private final ReentrantLock reentrantLock = new ReentrantLock(); // блокування доступу до рахунку
	private BigDecimal money; // баланс рахунку
	private final UUID id;

	public BankAccount()
	{
		this.money = BigDecimal.valueOf(0);
		this.id = UUID.randomUUID(); // генерація id
	}

	public BigDecimal getMoney()
	{
		reentrantLock.lock();  // блокування доступу
		BigDecimal result = new BigDecimal(money.toString());  // копія балансу
		reentrantLock.unlock(); // розблокування доступу
		return result;
	}

	public void addMoney(double amount)
	{
		if (amount > 0)
		{
			BigDecimal decimal = BigDecimal.valueOf(amount);
			reentrantLock.lock();
			money = money.add(decimal);
			reentrantLock.unlock();
		}
	}

	public boolean withdrawMoney(double amount) { //  зняти гроші з рахунку
		if (amount > 0) { // сума знімання додатна
			BigDecimal decimal = BigDecimal.valueOf(amount); // перетворення суми знімання в BigDecimal (щоб відняти суму від балансу рахунку, який також є типом)
			reentrantLock.lock(); // блокує доступ до рахунку, щоб уникнути конфліктів потоків

			// якщо коштів достатньо на рахунку, зміна балансу на суму знімання
			boolean ready = money.compareTo(decimal) >= 0;
			if (ready) { money = money.subtract(decimal);}

			reentrantLock.unlock(); // розблокування рахунку
			return ready;
		}
		return false;
	}

	public UUID getId() {
		return id;
	} // отримати унікальний id рахунку
}
