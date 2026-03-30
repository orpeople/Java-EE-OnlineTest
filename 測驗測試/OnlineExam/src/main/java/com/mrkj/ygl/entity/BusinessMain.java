package com.mrkj.ygl.entity;

public enum BusinessMain {

	TABLENAME("bus_main"),
	
	ID("main_id"),
	
	title("BIAOTI"),	//标题
	
	createtime("CHUANGJIAN"),	//创建时间
	
	answertime("DATISHIJIAN"),	//答题时间
	
	foreignkey("SUB_ID"),	//外键
	
	foreignkeyvalue("SUB_VALUE"),//外键值
	
	selectPaging("select main.`main_id`,main.`title`,main.`createtime`,main.`answertime`,main.`sub_id`,subject.`subject` from bus_main as main LEFT JOIN bus_subject as subject on main.sub_id = subject.sub_id ORDER BY `createtime` LIMIT ?,?"),
	
	selectCount("select count(`main_id`) as count from bus_main"),

	selectById ("SELECT `main_id`,`title`,`createtime`,`answertime`,`sub_id` FROM bus_main where `main_id` = ?"),
	
	selectBySub_id ("SELECT `main_id`,`title`,`createtime`,`answertime`,`sub_id` FROM bus_main where `sub_id` = ?");
	
	private String field;
	
	private BusinessMain (String field){
		this.field = field;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.field;
	}
}
