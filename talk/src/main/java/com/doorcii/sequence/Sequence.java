package com.doorcii.sequence;



public interface Sequence {
	/**
	 * ȡ��������һ��ֵ
	 *
	 * @return ����������һ��ֵ
	 * @throws SequenceException
	 */
	long nextValue() throws SequenceException;
}
