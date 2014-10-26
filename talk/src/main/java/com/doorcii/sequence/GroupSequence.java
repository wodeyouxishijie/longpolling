package com.doorcii.sequence;

import java.sql.SQLException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * ���з���
 * @author Jacky
 * 2014-5-9
 */
public class GroupSequence implements Sequence {

	private final Lock lock = new ReentrantLock();
	private SequenceDAO sequenceDao;

	private String name;
	private volatile SequenceRange currentRange;

	/**
	 * ��ʼ��һ�£����name�����ڣ�������ʼֵ
	 * 
	 * @throws SequenceException
	 * @throws SQLException 
	 */
	public void init() throws SequenceException, SQLException {
		if (!(sequenceDao instanceof GroupSequenceDAO)) {
			throw new SequenceException("please use  GroupSequenceDao!");
		}
		GroupSequenceDAO groupSequenceDao=(GroupSequenceDAO)sequenceDao;
		synchronized(this) {
			groupSequenceDao.adjust(name);
		}
	}

	public long nextValue() throws SequenceException {
		if (currentRange == null) {
			lock.lock();
			try {
				if (currentRange == null) {
					currentRange = sequenceDao.nextRange(name);
				}
			} finally {
				lock.unlock();
			}
		}

		long value = currentRange.getAndIncrement();
		if (value == -1) {
			lock.lock();
			try {
				for (;;) {
					if (currentRange.isOver()) {
						currentRange = sequenceDao.nextRange(name);
					}

					value = currentRange.getAndIncrement();
					if (value == -1) {
						continue;
					}

					break;
				}
			} finally {
				lock.unlock();
			}
		}

		if (value < 0) {
			throw new SequenceException("Sequence value overflow, value = "
					+ value);
		}

		return value;
	}

	public SequenceDAO getSequenceDao() {
		return sequenceDao;
	}

	public void setSequenceDao(SequenceDAO sequenceDao) {
		this.sequenceDao = sequenceDao;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
