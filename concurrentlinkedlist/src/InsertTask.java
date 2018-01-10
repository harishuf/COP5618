package harish.concurrent.linkedlist;

public class InsertTask implements Runnable {

	private ConcurrentSearcherList<Integer> list = null;
	private int num = 0;

	public InsertTask(final ConcurrentSearcherList<Integer> list, int num) {
		this.list = list;
		this.num = num;
	}

	@Override
	public void run() {
		try {
			System.out.println("Inserting : " + this.num);
			this.list.insert(num);

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
