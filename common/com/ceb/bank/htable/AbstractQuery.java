package com.ceb.bank.htable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.PageFilter;
import org.apache.hadoop.hbase.util.Bytes;

import com.ceb.hdqs.query.utils.QueryConfUtils;

public abstract class AbstractQuery<T> extends PooledHTableProxy {
	public static final long UNLIMIT = -1;
	public static final long NOSKIP = -1;

	/**
	 * 调用getHTable()方式前,必须调用setTableName(String tableName)
	 * 
	 * @param conf
	 */
	public AbstractQuery() {
		super(QueryConfUtils.getActiveConfig());
	}

	public AbstractQuery(String tableName) {
		this(QueryConfUtils.getActiveConfig(), tableName);
	}

	public AbstractQuery(Configuration conf, String tableName) {
		super(conf, tableName);
	}

	public T doGet(String rowKey) throws IOException {
		return doGet(Bytes.toBytes(rowKey));
	}

	public T doGet(byte[] rowKey) throws IOException {
		Get get = new Get(rowKey);
		return doGet(get);
	}

	public T doGet(Get getter) throws IOException {
		HTableInterface htable = getHTable();
		try {
			Result result = htable.get(getter);
			if (result == null || result.isEmpty()) {
				return null;
			}
			return parse(result);
		} finally {
			release(htable);
		}
	}

	/**
	 * 解析HBase查询结果为具体的对象
	 * 
	 * @param result
	 * @return
	 * @throws IOException
	 */
	protected abstract T parse(Result result) throws IOException;

	public long getTotal(String startRow, String stopRow) throws IOException {
		return getTotal(Bytes.toBytes(startRow), Bytes.toBytes(stopRow));
	}

	public long getTotal(byte[] startRow, byte[] stopRow) throws IOException {
		Scan scan = new Scan(startRow, stopRow);
		return getTotal(scan);
	}

	public long getTotal(Scan scan) throws IOException {
		HTableInterface htable = getHTable();
		scan.setCaching(1000);
		long total = 0;
		ResultScanner scanner = null;
		try {
			scanner = htable.getScanner(scan);
			if (scanner == null) {
				return 0;
			}
			Result[] array = null;
			while ((array = scanner.next(1000)) != null) {
				total += array.length;
			}
		} finally {
			if (scanner != null) {
				scanner.close();
			}
			release(htable);
		}
		return total;
	}

	public Result getResult(byte[] rowKey) throws IOException {
		return getResult(new Get(rowKey));
	}

	public Result getResult(Get get) throws IOException {
		HTableInterface htable = getHTable();
		try {
			return htable.get(get);
		} finally {
			release(htable);
		}
	}

	public byte[] getValue(String rowKey, byte[] family, byte[] qualifier) throws IOException {
		HTableInterface htable = getHTable();
		try {
			Get get = new Get(Bytes.toBytes(rowKey));
			get.addFamily(family);
			get.addColumn(family, qualifier);
			Result result = htable.get(get);
			if (result == null || result.isEmpty()) {
				return null;
			}
			return result.getValue(family, qualifier);
		} finally {
			release(htable);
		}
	}

	private boolean skip(ResultScanner scanner, long skip) throws IOException {
		if (skip <= 0) {
			return true;
		}
		long count = 0;
		while (count < skip && scanner.next() != null) {
			count++;
		}
		return count == skip;
	}

	public List<T> scan(Scan scan) throws IOException {
		return this.scan(scan, UNLIMIT);
	}

	public List<T> scan(Scan scan, long limit) throws IOException {
		return scan(scan, limit, NOSKIP);
	}

	public List<T> scan(Scan scan, long limit, long skip) throws IOException {
		HTableInterface htable = getHTable();
		ResultScanner scanner = null;
		List<T> list = new ArrayList<T>();
		try {
			Filter filter = this.getPageFilter(skip, limit);
			if (filter != null) {
				scan.setFilter(filter);
			}
			// scan.setCaching(200);本处不进行设置caching,在构造Scan时进行设置
			scanner = htable.getScanner(scan);
			if (scanner != null) {
				if (this.skip(scanner, skip)) {
					for (Result result : scanner) {
						T v = parse(result);
						if (v != null) {
							list.add(v);
						}
					}
				}
			}
		} finally {
			if (scanner != null) {
				scanner.close();
			}
			release(htable);
		}
		return list;
	}

	private Filter getPageFilter(long skip, long limit) {
		if (limit >= 0) {
			long pageSize = skip >= 0 ? limit + skip : limit;
			Filter filter = new PageFilter(pageSize);
			return filter;
		}
		return null;
	}

	public T oneScan(Scan scan) throws IOException {
		HTableInterface htable = getHTable();
		ResultScanner scanner = null;
		try {
			scanner = htable.getScanner(scan);
			if (scanner != null) {
				Result result = scanner.next();
				return result == null ? null : parse(result);

			}
			return null;
		} finally {
			if (scanner != null) {
				scanner.close();
			}
			release(htable);
		}
	}

	public List<T> doGet(List<Get> gets) throws IOException {
		HTableInterface htable = getHTable();
		List<T> items = new ArrayList<T>();
		try {
			Result[] result = htable.get(gets);
			for (Result res : result) {
				T v = parse(res);
				if (v != null) {
					items.add(v);
				}
			}
		} finally {
			release(htable);
		}
		return items;
	}
}