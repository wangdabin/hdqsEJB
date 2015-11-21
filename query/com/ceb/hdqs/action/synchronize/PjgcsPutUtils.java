package com.ceb.hdqs.action.synchronize;

import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.util.Bytes;

import com.ceb.hdqs.entity.JgcsEO;
import com.ceb.hdqs.entity.field.PjgcsField;
import com.ceb.hdqs.query.utils.QueryConstants;

public class PjgcsPutUtils implements IKeyValueUtils<JgcsEO> {

	@Override
	public JgcsEO getEntity(String[] valueSplit) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<KeyValue> getPutter(byte[] rowkey, JgcsEO entity) throws Exception {
		List<KeyValue> kvs = new ArrayList<KeyValue>();
		kvs.add(new KeyValue(rowkey, QueryConstants.HBASE_TABLE_FAMILY_BYTE, PjgcsField.id, Bytes.toBytes(entity.getId())));
		kvs.add(new KeyValue(rowkey, QueryConstants.HBASE_TABLE_FAMILY_BYTE, PjgcsField.qszxdh, Bytes.toBytes(entity.getQszxdh() == null ? "" : entity.getQszxdh())));
		kvs.add(new KeyValue(rowkey, QueryConstants.HBASE_TABLE_FAMILY_BYTE, PjgcsField.yinhdm, Bytes.toBytes(entity.getYinhdm() == null ? "" : entity.getYinhdm())));
		kvs.add(new KeyValue(rowkey, QueryConstants.HBASE_TABLE_FAMILY_BYTE, PjgcsField.yngyjg, Bytes.toBytes(entity.getYngyjg() == null ? "" : entity.getYngyjg())));
		kvs.add(new KeyValue(rowkey, QueryConstants.HBASE_TABLE_FAMILY_BYTE, PjgcsField.fkjgdm, Bytes.toBytes(entity.getFkjgdm() == null ? "" : entity.getFkjgdm())));
		kvs.add(new KeyValue(rowkey, QueryConstants.HBASE_TABLE_FAMILY_BYTE, PjgcsField.zkjgdm, Bytes.toBytes(entity.getZkjgdm() == null ? "" : entity.getZkjgdm())));
		kvs.add(new KeyValue(rowkey, QueryConstants.HBASE_TABLE_FAMILY_BYTE, PjgcsField.waigdm, Bytes.toBytes(entity.getWaigdm() == null ? "" : entity.getWaigdm())));
		kvs.add(new KeyValue(rowkey, QueryConstants.HBASE_TABLE_FAMILY_BYTE, PjgcsField.jggljb, Bytes.toBytes(entity.getJggljb() == null ? "" : entity.getJggljb())));
		kvs.add(new KeyValue(rowkey, QueryConstants.HBASE_TABLE_FAMILY_BYTE, PjgcsField.jgyyjb, Bytes.toBytes(entity.getJgyyjb() == null ? "" : entity.getJgyyjb())));
		kvs.add(new KeyValue(rowkey, QueryConstants.HBASE_TABLE_FAMILY_BYTE, PjgcsField.jgpzjb, Bytes.toBytes(entity.getJgpzjb() == null ? "" : entity.getJgpzjb())));
		kvs.add(new KeyValue(rowkey, QueryConstants.HBASE_TABLE_FAMILY_BYTE, PjgcsField.jgzjjb, Bytes.toBytes(entity.getJgzjjb() == null ? "" : entity.getJgzjjb())));
		kvs.add(new KeyValue(rowkey, QueryConstants.HBASE_TABLE_FAMILY_BYTE, PjgcsField.gnlisj, Bytes.toBytes(entity.getGnlisj() == null ? "" : entity.getGnlisj())));
		kvs.add(new KeyValue(rowkey, QueryConstants.HBASE_TABLE_FAMILY_BYTE, PjgcsField.zngwsj, Bytes.toBytes(entity.getZngwsj() == null ? "" : entity.getZngwsj())));
		kvs.add(new KeyValue(rowkey, QueryConstants.HBASE_TABLE_FAMILY_BYTE, PjgcsField.pngzsj, Bytes.toBytes(entity.getPngzsj() == null ? "" : entity.getPngzsj())));
		kvs.add(new KeyValue(rowkey, QueryConstants.HBASE_TABLE_FAMILY_BYTE, PjgcsField.yngxsj, Bytes.toBytes(entity.getYngxsj() == null ? "" : entity.getYngxsj())));
		kvs.add(new KeyValue(rowkey, QueryConstants.HBASE_TABLE_FAMILY_BYTE, PjgcsField.xinjsj, Bytes.toBytes(entity.getXinjsj() == null ? "" : entity.getXinjsj())));
		kvs.add(new KeyValue(rowkey, QueryConstants.HBASE_TABLE_FAMILY_BYTE, PjgcsField.jhjgbz, Bytes.toBytes(entity.getJhjgbz() == null ? "" : entity.getJhjgbz())));
		kvs.add(new KeyValue(rowkey, QueryConstants.HBASE_TABLE_FAMILY_BYTE, PjgcsField.jigolx, Bytes.toBytes(entity.getJigolx() == null ? "" : entity.getJigolx())));
		kvs.add(new KeyValue(rowkey, QueryConstants.HBASE_TABLE_FAMILY_BYTE, PjgcsField.dlhsbz, Bytes.toBytes(entity.getDlhsbz() == null ? "" : entity.getDlhsbz())));
		kvs.add(new KeyValue(rowkey, QueryConstants.HBASE_TABLE_FAMILY_BYTE, PjgcsField.jigomc, Bytes.toBytes(entity.getJigomc() == null ? "" : entity.getJigomc())));
		kvs.add(new KeyValue(rowkey, QueryConstants.HBASE_TABLE_FAMILY_BYTE, PjgcsField.jgywmc, Bytes.toBytes(entity.getJgywmc() == null ? "" : entity.getJgywmc())));
		kvs.add(new KeyValue(rowkey, QueryConstants.HBASE_TABLE_FAMILY_BYTE, PjgcsField.wxsjbz, Bytes.toBytes(entity.getWxsjbz() == null ? "" : entity.getWxsjbz())));
		kvs.add(new KeyValue(rowkey, QueryConstants.HBASE_TABLE_FAMILY_BYTE, PjgcsField.ynhbic, Bytes.toBytes(entity.getYnhbic() == null ? "" : entity.getYnhbic())));
		kvs.add(new KeyValue(rowkey, QueryConstants.HBASE_TABLE_FAMILY_BYTE, PjgcsField.dizhii, Bytes.toBytes(entity.getDizhii() == null ? "" : entity.getDizhii())));
		kvs.add(new KeyValue(rowkey, QueryConstants.HBASE_TABLE_FAMILY_BYTE, PjgcsField.dianhh, Bytes.toBytes(entity.getDianhh() == null ? "" : entity.getDianhh())));
		kvs.add(new KeyValue(rowkey, QueryConstants.HBASE_TABLE_FAMILY_BYTE, PjgcsField.dbguah, Bytes.toBytes(entity.getDbguah() == null ? "" : entity.getDbguah())));
		kvs.add(new KeyValue(rowkey, QueryConstants.HBASE_TABLE_FAMILY_BYTE, PjgcsField.lnxirm, Bytes.toBytes(entity.getLnxirm() == null ? "" : entity.getLnxirm())));
		kvs.add(new KeyValue(rowkey, QueryConstants.HBASE_TABLE_FAMILY_BYTE, PjgcsField.qyngrq, Bytes.toBytes(entity.getQyngrq() == null ? "" : entity.getQyngrq())));
		kvs.add(new KeyValue(rowkey, QueryConstants.HBASE_TABLE_FAMILY_BYTE, PjgcsField.sbshbm, Bytes.toBytes(entity.getSbshbm() == null ? "" : entity.getSbshbm())));
		kvs.add(new KeyValue(rowkey, QueryConstants.HBASE_TABLE_FAMILY_BYTE, PjgcsField.dyndlm, Bytes.toBytes(entity.getDyndlm() == null ? "" : entity.getDyndlm())));
		kvs.add(new KeyValue(rowkey, QueryConstants.HBASE_TABLE_FAMILY_BYTE, PjgcsField.tszfjh, Bytes.toBytes(entity.getTszfjh() == null ? "" : entity.getTszfjh())));
		kvs.add(new KeyValue(rowkey, QueryConstants.HBASE_TABLE_FAMILY_BYTE, PjgcsField.lianhh, Bytes.toBytes(entity.getLianhh() == null ? "" : entity.getLianhh())));
		kvs.add(new KeyValue(rowkey, QueryConstants.HBASE_TABLE_FAMILY_BYTE, PjgcsField.rjiebz, Bytes.toBytes(entity.getRjiebz() == null ? "" : entity.getRjiebz())));
		kvs.add(new KeyValue(rowkey, QueryConstants.HBASE_TABLE_FAMILY_BYTE, PjgcsField.gbcgbz, Bytes.toBytes(entity.getGbcgbz() == null ? "" : entity.getGbcgbz())));
		kvs.add(new KeyValue(rowkey, QueryConstants.HBASE_TABLE_FAMILY_BYTE, PjgcsField.wlywbz, Bytes.toBytes(entity.getWlywbz() == null ? "" : entity.getWlywbz())));
		kvs.add(new KeyValue(rowkey, QueryConstants.HBASE_TABLE_FAMILY_BYTE, PjgcsField.yatmbz, Bytes.toBytes(entity.getYatmbz() == null ? "" : entity.getYatmbz())));
		kvs.add(new KeyValue(rowkey, QueryConstants.HBASE_TABLE_FAMILY_BYTE, PjgcsField.yposbz, Bytes.toBytes(entity.getYposbz() == null ? "" : entity.getYposbz())));
		kvs.add(new KeyValue(rowkey, QueryConstants.HBASE_TABLE_FAMILY_BYTE, PjgcsField.fhipdz, Bytes.toBytes(entity.getFhipdz() == null ? "" : entity.getFhipdz())));
		kvs.add(new KeyValue(rowkey, QueryConstants.HBASE_TABLE_FAMILY_BYTE, PjgcsField.fhport, Bytes.toBytes(entity.getFhport() == null ? "" : entity.getFhport())));
		kvs.add(new KeyValue(rowkey, QueryConstants.HBASE_TABLE_FAMILY_BYTE, PjgcsField.youzbm, Bytes.toBytes(entity.getYouzbm() == null ? "" : entity.getYouzbm())));
		kvs.add(new KeyValue(rowkey, QueryConstants.HBASE_TABLE_FAMILY_BYTE, PjgcsField.dzyjdz, Bytes.toBytes(entity.getDzyjdz() == null ? "" : entity.getDzyjdz())));
		kvs.add(new KeyValue(rowkey, QueryConstants.HBASE_TABLE_FAMILY_BYTE, PjgcsField.jgfwdz, Bytes.toBytes(entity.getJgfwdz() == null ? "" : entity.getJgfwdz())));
		kvs.add(new KeyValue(rowkey, QueryConstants.HBASE_TABLE_FAMILY_BYTE, PjgcsField.rhjsbz, Bytes.toBytes(entity.getRhjsbz() == null ? "" : entity.getRhjsbz())));
		kvs.add(new KeyValue(rowkey, QueryConstants.HBASE_TABLE_FAMILY_BYTE, PjgcsField.dbqfbz, Bytes.toBytes(entity.getDbqfbz() == null ? "" : entity.getDbqfbz())));
		kvs.add(new KeyValue(rowkey, QueryConstants.HBASE_TABLE_FAMILY_BYTE, PjgcsField.shjnch, Bytes.toBytes(entity.getShjnch())));
		kvs.add(new KeyValue(rowkey, QueryConstants.HBASE_TABLE_FAMILY_BYTE, PjgcsField.jiluzt, Bytes.toBytes(entity.getJiluzt() == null ? "" : entity.getJiluzt())));
		return kvs;
	}

}
