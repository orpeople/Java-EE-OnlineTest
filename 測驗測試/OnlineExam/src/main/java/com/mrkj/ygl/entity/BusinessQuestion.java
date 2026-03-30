package com.mrkj.ygl.entity;

public enum BusinessQuestion {

	TABLENAME("bus_question"),
	
	ID("que_id"),
	
	questiontitle("WENTI"),
	
	questiontype("LEIXING"),
	
	createtime("CHUANGJIAN"),
	
	answerid("BIANMA"),
	
	score("FENSHU"),
	
	foreignkey("main_id"),
	
	selectPaging("select `que_id`,`questiontitle`,`questiontype`,`createtime`,`answerid`,`score`,`main_id` from bus_question ORDER BY `createtime` LIMIT ?,?"),
	
	selectCount("select count(`que_id`) as count from bus_question"),
	
	selectByMainId("select `que_id`,`questiontitle`,`questiontype`,`createtime`,`answerid`,`score`,`main_id` from bus_question where `main_id`=? ORDER BY `createtime`"),
	
	selectById("select `questiontitle`,`questiontype`,`createtime`,`answerid`,`score`,`main_id` from bus_question where `que_id`=?");
	
	private String field;
	
	private BusinessQuestion (String field){
		this.field = field;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.field;
	}
}
