package com.doorcii.ibatis;

import com.doorcii.beans.UserInfo;

public interface UserDAO {
	
	public UserInfo getUserById(String userId,String password) throws Exception;
	
}
