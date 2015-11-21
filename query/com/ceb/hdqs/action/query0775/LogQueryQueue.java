package com.ceb.hdqs.action.query0775;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ceb.hdqs.wtc.form.Handle0775Form;

public class LogQueryQueue {
	private static final Log log = LogFactory.getLog(LogQueryQueue.class);

	protected final ExecutorService threadPool;

	public LogQueryQueue() {
		threadPool = Executors.newFixedThreadPool(15);
	}

	/**
	 * 根据list参数在线程池中启动size大小的线程，并用CountDownLatch来控制该批线程结束后的处理。
	 * 
	 * @param list
	 */
	public void process(Handle0775Form form, List<String> list, LogQueryTraceQueue queue, String exCludeCode) {
		log.debug("processor 中 list size 是" + list.size());
		try {
			final CountDownLatch doneSignal = new CountDownLatch(list.size());
			for (int i = 0, size = list.size(); i < size; i++) {
				Handle0775Form newForm = new Handle0775Form();
				BeanUtils.copyProperties(newForm, form);
				newForm.setYngyjg(list.get(i));
				threadPool.execute(new TPParseRunnable(newForm, queue, doneSignal, exCludeCode));
			}
			try {
				doneSignal.await();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				log.error(e.getMessage(), e);
			}
		} catch (Exception e) {
			log.error("Use pool handle files occur Exception.", e);
		} finally {
			shutdownAndAwaitTermination(threadPool);
		}
	}

	public class TPParseRunnable implements Runnable, Serializable {
		private static final long serialVersionUID = 6800676323086501763L;
		private Handle0775Form form;
		private LogQueryTraceQueue queue;
		private CountDownLatch doneSignal;
		private String exCludeCode;

		public TPParseRunnable(Handle0775Form form, LogQueryTraceQueue queue, CountDownLatch doneSignal, String excludeCode) {
			this.form = form;
			this.queue = queue;
			this.doneSignal = doneSignal;
			this.exCludeCode = excludeCode;
		}

		@Override
		public void run() {
			try {
				HandleQuery0775 parser = new HandleQuery0775();
				List<String> result = parser.getCounts(form, exCludeCode);
				if (result != null) {
					queue.enqueue(result);
				}
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
