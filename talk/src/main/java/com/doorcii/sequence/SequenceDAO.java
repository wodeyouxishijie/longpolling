package com.doorcii.sequence;


public interface SequenceDAO {
	
	/**
	 * ȡ����һ�����õ��������
	 *
	 * @param name �������
	 * @return ������һ�����õ��������
	 * @throws SequenceException
	 */
	SequenceRange nextRange(String name) throws SequenceException;
	
}
