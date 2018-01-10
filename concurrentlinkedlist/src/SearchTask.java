package harish.concurrent.linkedlist;

public class SearchTask implements Runnable {

	private ConcurrentSearcherList<Integer> list = null;
	private int num = 0;

	public SearchTask(final ConcurrentSearcherList<Integer> list, int num) {
		this.list = list;
		this.num = num;
	}

	@Override
	public void run() {
		try {
			boolean result = this.list.search(num);
			System.out.println("Search result for Number : " + num + " = " + result);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
