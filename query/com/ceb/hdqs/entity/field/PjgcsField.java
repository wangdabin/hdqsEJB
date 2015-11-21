package com.ceb.hdqs.entity.field;

import org.apache.hadoop.hbase.util.Bytes;

public class PjgcsField {
	public static byte[] id     = Bytes.toBytes("id");                                          
	public static byte[] qszxdh = Bytes.toBytes("qszxdh");// 地区代号(原为：清算中心代号)       
	public static byte[] yinhdm = Bytes.toBytes("yinhdm");// 银行代码                           
	public static byte[] yngyjg = Bytes.toBytes("yngyjg");// 营业机构号                         
	public static byte[] fkjgdm = Bytes.toBytes("fkjgdm");// 发卡机构代码                       
	public static byte[] zkjgdm = Bytes.toBytes("zkjgdm");// 制卡中心代码                       
	public static byte[] waigdm = Bytes.toBytes("waigdm");// 外管代码                           
	public static byte[] jggljb = Bytes.toBytes("jggljb");// 机构管理级别                       
	public static byte[] jgyyjb = Bytes.toBytes("jgyyjb");// 机构营业级别in use                 
	public static byte[] jgpzjb = Bytes.toBytes("jgpzjb");// 机构凭证级别                       
	public static byte[] jgzjjb = Bytes.toBytes("jgzjjb");// 出帐周期                           
	public static byte[] gnlisj = Bytes.toBytes("gnlisj");// 管理上级                           
	public static byte[] zngwsj = Bytes.toBytes("zngwsj");// 帐务上级in use                     
	public static byte[] pngzsj = Bytes.toBytes("pngzsj");// 凭证上级                           
	public static byte[] yngxsj = Bytes.toBytes("yngxsj");// 省区代号(前两位)                   
	public static byte[] xinjsj = Bytes.toBytes("xinjsj");// 现金上级                           
	public static byte[] jhjgbz = Bytes.toBytes("jhjgbz");// 稽核机构标志                       
	public static byte[] jigolx = Bytes.toBytes("jigolx");// 机构类型                           
	public static byte[] dlhsbz = Bytes.toBytes("dlhsbz");// 独立核算标志                       
	public static byte[] jigomc = Bytes.toBytes("jigomc");// 机构中文名称                       
	public static byte[] jgywmc = Bytes.toBytes("jgywmc");// 机构英文名称                       
	public static byte[] wxsjbz = Bytes.toBytes("wxsjbz");// 尾箱上缴标志                       
	public static byte[] ynhbic = Bytes.toBytes("ynhbic");// 银行SWIFT                          
	public static byte[] dizhii = Bytes.toBytes("dizhii");// 地址                               
	public static byte[] dianhh = Bytes.toBytes("dianhh");// 电话号码                           
	public static byte[] dbguah = Bytes.toBytes("dbguah");// 电报挂号                           
	public static byte[] lnxirm = Bytes.toBytes("lnxirm");// 联系人                             
	public static byte[] qyngrq = Bytes.toBytes("qyngrq");// 启用日期                           
	public static byte[] sbshbm = Bytes.toBytes("sbshbm");// 设备名                             
	public static byte[] dyndlm = Bytes.toBytes("dyndlm");// 报表队列名                         
	public static byte[] tszfjh = Bytes.toBytes("tszfjh");// 同城实时支付交换号                 
	public static byte[] lianhh = Bytes.toBytes("lianhh");// 联行行号                           
	public static byte[] rjiebz = Bytes.toBytes("rjiebz");// 日结标记                           
	public static byte[] gbcgbz = Bytes.toBytes("gbcgbz");// 广播成功标志                       
	public static byte[] wlywbz = Bytes.toBytes("wlywbz");// 外围落地业务处理标志               
	public static byte[] yatmbz = Bytes.toBytes("yatmbz");// 异地支行ATM联网标志                
	public static byte[] yposbz = Bytes.toBytes("yposbz");// 异地支行POS联网标志                
	public static byte[] fhipdz = Bytes.toBytes("fhipdz");// 分行IP地址                         
	public static byte[] fhport = Bytes.toBytes("fhport");// 分行PORT号                         
	public static byte[] youzbm = Bytes.toBytes("youzbm");// 邮政编码                           
	public static byte[] dzyjdz = Bytes.toBytes("dzyjdz");// E-mail                             
	public static byte[] jgfwdz = Bytes.toBytes("jgfwdz");// 机构服务器地址名称                 
	public static byte[] rhjsbz = Bytes.toBytes("rhjsbz");// 国结挂靠标志                       
	public static byte[] dbqfbz = Bytes.toBytes("dbqfbz");// 代办签发标志                       
	public static byte[] shjnch = Bytes.toBytes("shjnch");// 时间戳                             
	public static byte[] jiluzt = Bytes.toBytes("jiluzt");// 记录状态                           

}
