package com.mrkj.ygl.service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import com.mrkj.ygl.Connection.DaoConnection;
import com.mrkj.ygl.entity.BusinessAnswer;
import com.mrkj.ygl.entity.BusinessInfo;
import com.mrkj.ygl.entity.BusinessMain;
import com.mrkj.ygl.entity.BusinessQuestion;
import com.mrkj.ygl.entity.BusinessSubject;
import com.mrkj.ygl.util.MrksUtils;

/**
 * The beauty of the code
 * 
 * @author yuguoliang
 *
 *
 *
 */

public class BusinessService {

	public static List<Map<String, String>> getSubPage (Integer page,Integer row){
		List<Map<String, String>> resultListMap = new ArrayList<>();
		
		DaoConnection dc = DaoConnection.initDaoConnection();
		try {
			PreparedStatement sp = dc.getPreparedExec(BusinessSubject.selectPaging.toString(), page,row);//预处理执行
			ResultSet rs = sp.executeQuery();
			if (rs != null){
				while (rs.next()){
					Map<String, String> reaultMap = new HashMap<String, String>();
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
					reaultMap.put(BusinessSubject.ID.toString(), rs.getString(1));
					reaultMap.put(BusinessSubject.subject.toString(),rs.getString(2));
					Long sqlDateLong = rs.getTimestamp(3).getTime();//rs.getDate(3).getTime();
					reaultMap.put(BusinessSubject.createtime.toString(), sdf.format(new Date(sqlDateLong)));
					resultListMap.add(reaultMap);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultListMap;
	}
	
	public static List<Map<String, String>> getAllSubPage (){
		List<Map<String, String>> resultListMap = new ArrayList<>();
		DaoConnection dc = DaoConnection.initDaoConnection();
		try {
			//SQL语句存在枚举类当中，枚举类起到解释的作用，方便管理
			PreparedStatement sp = dc.getPreparedExec(BusinessSubject.selectAll.toString());
			ResultSet rs = sp.executeQuery();
			if (rs != null){
				while (rs.next()) {
					Map<String, String>reaultMap = new HashMap<String, String>();
					//时间格式化，格式为年-月-日 时：分：秒（如：2017-10-11 13:01:40）
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
					reaultMap.put(BusinessSubject.ID.toString(), rs.getString(1));
					String kskmAndJk = rs.getString(2);
					//获取科目名称与监考人，用 - 分隔
					String[] kskmJks = kskmAndJk.split("-");
					if (kskmJks.length==2) {
						reaultMap.put(BusinessSubject.subject.toString(),kskmJks[0]);
						reaultMap.put("jiankao",kskmJks[1]);
					} else {
						reaultMap.put(BusinessSubject.subject.toString(),"未知");
						reaultMap.put("jiankao","未知");
					}
					//获取时间，DateTime数据类型，要使用Timestamp接收
					Long sqlDateLong = rs.getTimestamp(3).getTime();
					reaultMap.put(BusinessSubject.createtime.toString(),
							sdf.format(new Date(sqlDateLong)));
					resultListMap.add(reaultMap);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return  resultListMap;
	}
	
	public static Long getSubCount (){
		DaoConnection dc = DaoConnection.initDaoConnection();
		try {
			PreparedStatement ps = dc.getPreparedExec(BusinessSubject.selectCount.toString());
			ResultSet rs = ps.executeQuery();
			if (rs!=null){
				rs.next();
				return rs.getLong(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0L;
	}
	
	/***************************************************试卷主表service***************************************************/
	/**
	 * 根据sub_id获取科目下所有试卷
	 * @param sub_id
	 * @return
	 */
	public static List<Map<String,String>> getMainBySubject (String sub_id){
		List<Map<String,String>> mains = new ArrayList<>();
		
		DaoConnection dc = DaoConnection.initDaoConnection();
		try {
			//SELECT `main_id`,`title`,`createtime`,`answertime`,`sub_id` FROM bus_main where `sub_id` = ?
			PreparedStatement ps = dc.getPreparedExec(BusinessMain.selectBySub_id.toString(),sub_id);
			ResultSet rs = ps.executeQuery();
			while (rs.next()){
				Map<String,String> entity = new HashMap<>();
				String main_id = rs.getString(1);
				String title = rs.getString(2);
				entity.put(BusinessMain.ID.toString(), main_id);
				entity.put(BusinessMain.title.toString(), title);
				mains.add(entity);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return mains;
	}
	/************************************************Action*************************************************************/
	/**
	 * 根据 main_id 获取一份完整的试卷
	 * @param parmMain_id
	 * @return
	 */
	public static Map<String,Object> getAllQuestion (String parmMain_id){
		
		//SELECT `main_id`,`title`,`createtime`,`answertime`,`sub_id` FROM bus_main where `main_id` = ?
		String selectMainSQL = BusinessMain.selectById.toString();
		/*
		 *select `que_id`,`questiontitle`,`questiontype`,`createtime`,`answerid`,`score`,`main_id` 
		 *from bus_question where `main_id`=? ORDER BY `createtime`
		 */
		String selectQuestionSQL = BusinessQuestion.selectByMainId.toString();
		/*
		 * SELECT answer.ans_id,answer.`answerContent`,answer.`createtime`,answer.que_id FROM bus_main AS main 
		 * LEFT JOIN bus_question AS question on main.main_id=question.main_id 
		 * LEFT JOIN bus_answer AS answer ON question.que_id=answer.que_id WHERE main.main_id = ?
		 */
		String selectAnswerSQL =  BusinessAnswer.selectByMainId.toString();
		int result = 0;
		
		DaoConnection dc = DaoConnection.initDaoConnection();
		PreparedStatement ps;
		ResultSet rs;
		Map<String,Object> resultMainMap = new HashMap<>();
		List<Map<String,String>> resultQuestionsMaps = new ArrayList<>();
		List<Map<String,String>> resultAnswerMaps = new ArrayList<>();
		try {
			//selectById ("SELECT `main_id`,`title`,`createtime`,`answertime`,`sub_id` FROM bus_main where main_id = ?");
			ps = dc.getPreparedExec(selectMainSQL,parmMain_id);
			rs = ps.executeQuery();
			rs.next();
			
			String main_id = rs.getString(1);
			String title = rs.getString(2);
			Long createtime = rs.getTimestamp(3).getTime();
			String answertime = rs.getString(4);
			String sub_id = rs.getString(5);
			resultMainMap.put("main_id", main_id);
			resultMainMap.put(BusinessMain.title.toString(), title);
			resultMainMap.put(BusinessMain.createtime.toString(), MrksUtils.TrasformGetimeToString(createtime));
			resultMainMap.put(BusinessMain.answertime .toString(), answertime);
			resultMainMap.put("sub_id",sub_id);     //main 数据封装完毕
			
			//select `que_id`,`questiontitle`,`questiontype`,`createtime`,`answerid`,`score`,`main_id` from bus_question where `main_id`=? ORDER BY `createtime`
			ps = dc.getPreparedExec(selectQuestionSQL,parmMain_id);
			rs = ps.executeQuery();
			
			int totalScore=0;	//卷面分数
			while (rs.next()){
				Map<String,String> question = new HashMap<>();
			
				String que_id = rs.getString(1);
				String questiontitle = rs.getString(2);
				String questiontype = rs.getString(3);
				createtime = rs.getTimestamp(4).getTime();
				String answerid = rs.getInt(5)+"";
				int score = rs.getInt(6);
				main_id = rs.getString(7);
				question.put("que_id", que_id);
				question.put(BusinessQuestion.questiontitle.toString(),questiontitle);
				question.put(BusinessQuestion.questiontype.toString(), questiontype);
				question.put(BusinessQuestion.createtime.toString(), MrksUtils.TrasformGetimeToString(createtime));
				question.put(BusinessQuestion.answerid.toString(),answerid);
				question.put(BusinessQuestion.score.toString(),score+"");
				question.put("main_id",main_id);
				resultQuestionsMaps.add(question);
				totalScore+=score;
			}
			resultMainMap.put("questions", resultQuestionsMaps);
			resultMainMap.put("totalScore", totalScore);//保存卷面分数
			//SELECT answer.ans_id,answer.`answerContent`,answer.`createtime`,answer.que_id FROM bus_main AS main LEFT JOIN bus_question AS question on main.main_id=question.main_id LEFT JOIN bus_answer AS answer ON question.que_id=answer.que_id WHERE main.main_id = ?
			ps = dc.getPreparedExec(selectAnswerSQL,parmMain_id);
			rs = ps.executeQuery();
			while (rs.next()){
				Map<String,String> answer = new HashMap<>();
				
				String ans_id = rs.getString(1);
				String answerContent = rs.getString(2);
				createtime = rs.getTimestamp(3).getTime();
				String que_id = rs.getString(4);
				answer.put("ans_id", ans_id);
				answer.put(BusinessAnswer.answerContent.toString(), answerContent);
				answer.put(BusinessAnswer.createtime.toString(), MrksUtils.TrasformGetimeToString(createtime));
				answer.put("que_id", que_id);
				
				resultAnswerMaps.add(answer);
			}
			resultMainMap.put("answers", resultAnswerMaps);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return resultMainMap;
	}
	
	/**
	 * 计算分数
	 * @param parmMap
	 * @return
	 */
	public static int countScore (Map<String,String[]> parmMap,String username){
		
		int result = 0;
		String main_id = parmMap.get("main_id")[0];
		DaoConnection dc = DaoConnection.initDaoConnection();
		Set<Entry<String, String[]>> entrySet = parmMap.entrySet(); // 将提交过来的表单数据转换为特殊的Set，之后通过其遍历
		int count = 0;
		for(Entry<String, String[]> entry : entrySet){ //遍历提交过来的表单数据
			if ("action".equals(entry.getKey())){
				break ;
			}
			String que_id = entry.getKey();
			String[] values = entry.getValue();
			try {
				// 获取一道试题的详细信息
				//"select `questiontitle`,`questiontype`,`createtime`,`answerid`,`score`,`main_id` from bus_question where `que_id`=?"
				PreparedStatement ps = dc.getPreparedExec(BusinessQuestion.selectById.toString(), que_id);
				ResultSet rs = ps.executeQuery();
				if (rs.next()){
					Integer answerid = rs.getInt(4);
					Integer score = rs.getInt(5);
					int changeanswerid = MrksUtils.statistics(values);//获取答案
					if (changeanswerid == answerid){
						count+=score;
					}
				}
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		//"insert into bus_info (`info_id`,`username`,`score`,`main_id`) VALUES (?,?,?,?) "
		try {
			PreparedStatement ps = dc.getPreparedExec(BusinessInfo.insert.toString(),UUID.randomUUID().toString(),username,count,main_id);
			result = ps.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}
	//查询考试成绩
	public static List<Map<String, String>> getKsfs (String username,Integer page,Integer row){
		List<Map<String, String>> resultListMap = new ArrayList<>();
		
		DaoConnection dc = DaoConnection.initDaoConnection();
		
		try {
			
			PreparedStatement ps = dc.getPreparedExec(BusinessInfo.selectPaging.toString(),username,page,row);
			ResultSet rs = ps.executeQuery();
			while (rs.next()){
				Map<String,String> resultMap = new HashMap<>();
				String main_id = rs.getString(1);
				String title = rs.getString(2);
				long timestamp = rs.getTimestamp(3).getTime();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				String createtime = sdf.format(new Date(timestamp));
				long answertime = rs.getLong(4);
				String score = rs.getString(6);
				resultMap.put(BusinessMain.ID.toString(), main_id);
				resultMap.put(BusinessMain.title.toString(),title);
				resultMap.put(BusinessMain.createtime.toString(),createtime);
				resultMap.put(BusinessMain.answertime.toString(),sdf.format(new Date(answertime)));
				resultMap.put(BusinessInfo.score.toString(),score);
				resultListMap.add(resultMap);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return resultListMap;
	}
	//查询指定用户的有效考试次数
	public static Long getInfoCount (String username){
		DaoConnection dc = DaoConnection.initDaoConnection();
		try {
			PreparedStatement ps = dc.getPreparedExec(BusinessInfo.selectCount.toString(),username);
			ResultSet rs = ps.executeQuery();
			if (rs!=null){
				rs.next();
				return rs.getLong(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0L;
	}
}
