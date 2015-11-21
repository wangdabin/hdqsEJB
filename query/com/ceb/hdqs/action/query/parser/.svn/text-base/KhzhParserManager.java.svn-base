package com.ceb.hdqs.action.query.parser;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ceb.hdqs.entity.PybjyEO;
import com.ceb.hdqs.entity.result.KehzhParserResult;

public class KhzhParserManager {
	private static final Log log = LogFactory.getLog(KhzhParserManager.class);

	public KhzhParserManager() {
	}

	/**
	 * 根据list参数在线程池中启动size大小的线程，并用CountDownLatch来控制该批线程结束后的处理。
	 * 
	 * @param list
	 */
	public void process(PybjyEO record, List<KhzhParser> list, KhzhParseTraceQueue queue) {
		log.debug("processor 中 list size 是" + list.size());
		try {
			final CountDownLatch doneSignal = new CountDownLatch(list.size());
			for (int i = 0, size = list.size(); i < size; i++) {
				new Thread(new TPParseRunnable(record, list.get(i), queue, doneSignal)).start();
			}
			try {
				doneSignal.await();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				log.error(e.getMessage(), e);
			}
		} catch (Exception e) {
			log.error("Use pool handle files occur Exception.", e);
		}
	}

	public class TPParseRunnable implements Runnable, Serializable {
		private static final long serialVersionUID = 6800676323086501763L;

		private KhzhParseTraceQueue queue;
		private CountDownLatch doneSignal;
		private KhzhParser parser;
		private PybjyEO record;

		public TPParseRunnable(PybjyEO record, KhzhParser parser, KhzhParseTraceQueue queue, CountDownLatch doneSignal) {
			this.queue = queue;
			this.doneSignal = doneSignal;
			this.parser = parser;
			this.record = record;
		}

		@Override
		public void run() {
			try {
				KehzhParserResult result = parser.parseCondition(record);
				if (result != null) {
					queue.enqueue(parser.getTableName(), result);
				} else {
					log.debug(parser.getTableName() + "解析出的结果是null");
				}

			} catch (Exception e) {
				log.error(e.getMessage(), e);
			} finally {
				doneSignal.countDown();
			}
		}
	}
}