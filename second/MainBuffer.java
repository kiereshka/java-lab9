package Laba9.second;

import java.util.concurrent.atomic.AtomicInteger;

public class MainBuffer
{
	private static final CircuitBuffer<String> circuitBuffer1 = new CircuitBuffer<>(10);
	private static final CircuitBuffer<String> circuitBuffer2 = new CircuitBuffer<>(10);

	// Лічильник для генерації унікальних номерів повідомлень
	private static final AtomicInteger prodCounter = new AtomicInteger(0);

	public static void main(String[] args)
	{
		for (int i = 0; i < 5; i++) // Створення потоків, які генерують повідомлення
		{
			Thread generator = new Thread(() ->
			{
				while (!Thread.interrupted())
				{
					String toPut = Thread.currentThread().getName() + " згенерував повідомлення: " + prodCounter.getAndIncrement();
					circuitBuffer1.put(toPut); 	// Додавання повідомлення в перший буфер
				}
			});
			generator.setDaemon(true); // Установлювання потоків-демонів (не блокують завершення програми)
			generator.start();
		}

		// Створення для перенесення повідомлень з першого буфера в другий
		for (int i = 0; i < 2; i++)
		{
			Thread transfer = new Thread(() ->
			{
				while (!Thread.interrupted())
				{
					String msg = circuitBuffer1.get();
					String toPut = Thread.currentThread().getName() + " переклав повідомлення: " + msg;

					circuitBuffer2.put(toPut);
				}
			});
			transfer.setDaemon(true);
			transfer.start();
		}

		for (int i = 0; i < 100; i++) // Виведення 100 повідомлень із другого буферу
		{
			String msg = circuitBuffer2.get();
			System.out.println(msg);
		}
	}
}
