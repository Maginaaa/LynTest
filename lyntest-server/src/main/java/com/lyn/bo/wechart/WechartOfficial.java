package com.lyn.bo.wechart;

import lombok.Data;

import java.io.Serializable;

//企业微信消息体
@Data
public class WechartOfficial implements Serializable {

	private static final long serialVersionUID = -5436180970669446636L;
	// 发送方系统
	private String sendDept;
	// 发送人
	private String sender;
	// 消息ID
	private String unionId;
	// 企业微信栏目ID
	private String wxAgentid;
	// 发送内容
	private String wxContent;
	// 消息类型
	private String wxMegtype;
	// 发送目的人工号
	private String wxTouser;

}
