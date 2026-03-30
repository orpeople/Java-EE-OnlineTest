package com.mrkj.ygl.entity;

public enum BusinessSubject {

	TABLENAME("bus_subject"),
	
	ID("sub_id"),
	
	subject("KEMU"),//科目
	
	createtime("CHUANGJIAN"),//创建时间
	
	selectPaging("select `sub_id`,`subject`,`createtime` from bus_subject ORDER BY `createtime` LIMIT ?,?"),
	
	selectAll("select `sub_id`,`subject`,`createtime` from bus_subject ORDER BY `createtime`"),
	
	selectCount("select count(`sub_id`) as count from bus_subject")
	;
	
	
	private String field;
	
	private BusinessSubject(String field){
		this.field = field;
	}
	
	@Override
	public String toString() {
		return this.field;
	}
}
