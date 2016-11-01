package cn.zlb.goods.pager;

public class text implements Runnable {

	private int x=1;

	private int y=1;

	public static void main(String[] args) {

		text that = new text();

		(new Thread(that)).start();

		(new Thread(that)).start();

	}

	public synchronized void run() {

		for (;;) {

			x++;

			y++;

			System.out.println("x =  " + x + ", y =  " + y);

		}

	}

}