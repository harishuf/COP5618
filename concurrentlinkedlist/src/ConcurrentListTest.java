package harish.concurrent.linkedlist;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConcurrentListTest {
	public static void main(String[] args) {
		ConcurrentSearcherList<Integer> concurrentSearcherList = new ConcurrentSearcherList<Integer>();

		ExecutorService executorService = Executors.newCachedThreadPool();

		executorService.execute(new InsertTask(concurrentSearcherList, 10));
		executorService.execute(new InsertTask(concurrentSearcherList, 20));
		executorService.execute(new InsertTask(concurrentSearcherList, 30));

		executorService.execute(new SearchTask(concurrentSearcherList, 30));
		executorService.execute(new SearchTask(concurrentSearcherList, 40));
		

		executorService.shutdown();
	}
}
