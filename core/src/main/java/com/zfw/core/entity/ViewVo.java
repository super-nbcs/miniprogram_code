package com.zfw.core.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 回调信息
 * @author yzh
 * @version 2019-10-21
 */
@Data
public class ViewVo implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -3537510456925293728L;
	private Integer short_group;// 未使用字段
	private Double confidence;// 置信度
	private Integer fmp_error; // 是否通过fmp检测
	private Integer group; // 未使用字段
	private Integer event_type; //0表示可信
	private Integer timestamp; // 识别发生时候时间戳
	private String photo; // 图片的base64编码
	private String age;// 识别年龄
	private String photo_md5;// 图片md5，用于去重
	private Integer fmp; // fmp分数，未开启未0，开始为0～1的一个浮点数
	private String screen_token;// 屏幕的token，可用于查找摄像机
	private String gender; // 性别
	private String quality; // 图片质量
	private String subject_id; // 陌生人为None，员工或访客为实际的subject_id
	private String subject_photo_id;// 陌生人没有此项，员工或访客为对应的识别到photo id
	private String box_addr; //主机ip，从resquest中获取


}
