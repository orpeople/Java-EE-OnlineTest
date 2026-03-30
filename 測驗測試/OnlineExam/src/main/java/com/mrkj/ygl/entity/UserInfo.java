package com.mrkj.ygl.entity;

public enum UserInfo {

	TABLENAME("sys_userinfo"),
	
	ID("id"),
	
	username("USERNAME"),//用户名
	
	password("PASSWORD"),//密码
	
	fullname("NAME"),//真实姓名
	
	email("EMAIL"),//E-mail地址

	SELECTBYusername("select `id`,`username`,`password`,`fullname`,`email` from `sys_userinfo` where `username` = ?"),
	
	INSERT("insert into `sys_userinfo` (`username`,`password`,`fullname`,`email`) values (?,?,?,?);");
	
	
	private String field;
	
	private UserInfo (String field){
		this.field = field;
	}

	/**
	 * @return the field
	 */
	public String getField() {
		return field;
	}

	/**
	 * @param field the field to set
	 */
	public void setField(String field) {
		this.field = field;
	}

	/* (non-Javadoc)
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.field;
	}
}
