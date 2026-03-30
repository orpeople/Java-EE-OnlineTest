package com.mrkj.ygl.entity;

public enum BusinessInfo {

	TABLENAME("bus_info"), // 数据表名

	ID("info_id"), // ID

	username("USERNAME"), // 用户名

	score("FENSHU"), // 分数

	foreignkey("main_id"), // 外键

	insert("insert into bus_info (`info_id`,`username`,`score`,`main_id`) VALUES (?,?,?,?) "), // 添加
	//分页查询考试成绩
	selectPaging(
			"select main.`main_id`,main.`title`,main.`createtime`,main.`answertime`,"
			+ "main.`sub_id`,info.`score` from bus_main as main RIGHT JOIN bus_info as info "
			+ "on main.main_id = info.main_id WHERE info.`username`=? ORDER BY `createtime` LIMIT ?,?"),

	selectCount("select count(`info_id`) as count from bus_info where `username`= ?"),;//查询指定用户的有效考试次数

	private String field;

	private BusinessInfo(String field) {
		this.field = field;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.field;
	}
}
