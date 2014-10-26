package com.doorcii.ibatis;

import java.util.HashMap;
import java.util.Map;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.doorcii.beans.UserInfo;

public class UserDAOImpl extends SqlMapClientDaoSupport  implements UserDAO {

	@Override
	public UserInfo getUserById(String userId,String password) throws Exception {
		Map<String,String> param  = new HashMap<String,String>();
		param.put("userId", userId);
		param.put("password", password);
		return (UserInfo)this.getSqlMapClientTemplate().queryForObject("talk.queryUserInfo", param);
	}

}
