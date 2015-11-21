package com.ceb.hdqs.action.asyn;

import java.io.Serializable;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 异步处理线程池
 * 
 * @author user
 * 
 */
public class AsynHandlerPool {
	private static final Log log = LogFactory.getLog(AsynHandlerPool.class);
	private final ExecutorService threadPool;

	public AsynHandlerPool(int poolSize) {
		threadPool = Executors.newFixedThreadPool(poolSize);
	}

	public void destroy() {
		log.info("开始关闭异步处理线程池!");
		if (threadPool != null)
			shutdownAndAwaitTermination(threadPool);
		log.info("关闭异步处理线程池结束!");
	}

	/**
	 * 根据list参数在线程池中启动size大小的线程，并用CountDownLatch来控制该批线程结束后的处理。
	 * 
	 * @param list
	 */
	public void process(List<IAsynchronizeQuery> list) {
		final CountDownLatch doneSignal = new CountDownLatch(list.size());
		try {
			for (IAsynchronizeQuery asynchronizeQuery : list) {
				TPParseRunnable query = new TPParseRunnable(doneSignal, asynchronizeQuery);
				threadPool.execute(query);
			}
			try {
				doneSignal.await();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				log.error(e.getMessage(), e);
			}
		} catch (Exception e) {
			log.error("异步处理查询发现异常", e);
		}
	}

	public class TPParseRunnable implements Runnable, Serializable {
		private static final long serialVersionUID = 6800676323086501763L;
		private CountDownLatch doneSignal;
		private IAsynchronizeQuery asynchronizeQuery;

		public TPParseRunnable(CountDownLatch doneSignal, IAsynchronizeQuery asynchronizeQuery) {
			this.doneSignal = doneSignal;
			this.asynchronizeQuery = asynchronizeQuery;
			Thread.currentThread().setName(asynchronizeQuery.getTaskName());
			Thread.currentThread().setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
				@Override
				public void uncaughtException(Thread t, Throwable e) {
					t.interrupt();
					log.error("任务" + t.getName() + "查询异常", e);
				}
			});
		}

		@Override
		public void run() {
			try {
				asynchronizeQuery.startAsynchronizeQuery(false);
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			} finally {
				doneSignal.countDown();
			}
		}
	}

	private void shutdownAndAwaitTermination(ExecutorService pool) {
		pool.shutdown(); // Disable new tasks from being submitted
		try {
			// Wait a while for existing tasks to terminate
			if (!pool.awaitTermination(60, TimeUnit.SECONDS)) {
				pool.shutdownNow(); // Cancel currently executing tasks
				// Wait a while for tasks to respond to being cancelled
				if (!pool.awaitTermination(60, TimeUnit.SECONDS))
					log.debug("Pool did not terminate");
			}
		} catch (InterruptedException ie) {
			// (Re-)Cancel if current thread also interrupted
			pool.shutdownNow();
			// Preserve interrupt status
			Thread.currentThread().interrupt();
		}
	}
}