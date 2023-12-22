package Laba9.second;

public class CircuitBuffer<T>
{
	private final Object lock = new Object(); // об'єкт для синхронізації доступу до буфера декількома потоками
	private final int size;
	private final T[] buffer;
	private int head, tail; // індекси початку та кінця буфера
	private volatile int count;

	@SuppressWarnings("unchecked") // "придушення" попереджень компілятора про приведення типів
	public CircuitBuffer(int size) { // створення буферу
		this.size = size;
		buffer = (T[]) new Object[size]; // масив об'єктів для зберігання елементів буфера
		count = 0;
		head = 0;
		tail = 0;
	}

	public void put(T value)
	{
		synchronized (lock)
		{
			while (count == size)
			{
				try {
					lock.wait();
				} catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}

			buffer[tail] = value; // додаємо елемент в буфер за індексом
			tail = (tail + 1) % size; // обраховуємо індекс кінця
			count++;  // к-сть елементів збільшується на один
			lock.notifyAll(); // сповіщаємо всі потоки що буфер вільний
		}
	}

	public T get()
	{
		synchronized (lock)
		{ // запобігання одночасному доступу до буфера декількома потоками
			while (count == 0) // перевіряємо чи порожній буфер
			{
				try {
					lock.wait();  // очікуємо на дані
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			T value = buffer[head]; // отримуємо елемент з буфера за індексом
			head = (head + 1) % size; // переміщуємо індекс

			count--;
			lock.notifyAll(); // сповіщаємо потоки про наявність елементів

			return value;
		}
	}
}
