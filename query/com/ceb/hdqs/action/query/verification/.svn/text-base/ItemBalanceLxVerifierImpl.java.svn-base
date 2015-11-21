package com.ceb.hdqs.action.query.verification;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ceb.hdqs.action.query.exception.BalanceBrokedException;
import com.ceb.hdqs.constants.HdqsConstants;
import com.ceb.hdqs.entity.result.ZhangHParseResult;
import com.ceb.hdqs.query.entity.BalanceVerifyEntity;
import com.ceb.hdqs.query.entity.EnumKhzhlx;
import com.ceb.hdqs.query.entity.QueryDocumentContext;
import com.ceb.hdqs.query.entity.TransferItem;
import com.ceb.hdqs.query.utils.BrokedReordUtils;

/**
 * 账户余额连续性校验
 * 
 * @author user
 * 
 */
public class ItemBalanceLxVerifierImpl implements IBalanceVerifier {
	private static final Log LOG = LogFactory.getLog(ItemBalanceLxVerifierImpl.class);

	private boolean isFirst = true;// 校验入口需要重置为true

	private BigDecimal preBalance = null;
	private String preJioyrq = null;
	private BigDecimal jioyje = null;
	private BigDecimal currentBalance = null;
	private String curJioyrq = null;

	public void reset() {
		this.isFirst = true;
	}

	public boolean verify(BalanceVerifyEntity recordInfo, ZhangHParseResult zhangHParseResult, List<? extends TransferItem> balanceList) throws BalanceBrokedException {
		return verify(recordInfo, zhangHParseResult, balanceList, null);

	}

	public boolean verify(BalanceVerifyEntity recordInfo, ZhangHParseResult zhangHParseResult, List<? extends TransferItem> balanceList, QueryDocumentContext documentContext)
			throws BalanceBrokedException {

		if (balanceList == null || balanceList.isEmpty() || balanceList.size() == 1) {
			return true;
		}
		Boolean isAsyn = zhangHParseResult.getRecord().getIsAsyn();
		int yueefx = -1;
		int jiedbz = -1;
		for (int i = 0; i < balanceList.size(); i++) {
			if (isFirst && i == 0) {// 第一批的第一条，直接赋值，不进行验证
				currentBalance = new BigDecimal(balanceList.get(i).getZHHUYE());
				curJioyrq = balanceList.get(i).getJIOYRQ();

				isFirst = false;
				continue;
			}
			preBalance = currentBalance;
			preJioyrq = curJioyrq;
			jioyje = new BigDecimal(balanceList.get(i).getJIO1JE());
			currentBalance = new BigDecimal(balanceList.get(i).getZHHUYE());
			curJioyrq = balanceList.get(i).getJIOYRQ();
			yueefx = Integer.parseInt(balanceList.get(i).getYUEEFX().trim());
			jiedbz = Integer.parseInt(balanceList.get(i).getJIEDBZ().trim());
			LOG.debug("preBalance:" + preBalance + "  JIOYJE: " + jioyje + " currentBalance:" + currentBalance + " yueefx:" + yueefx + " jiedbz:" + jiedbz);
			if (!verifyItem(yueefx, jiedbz)) {

				// 账户******（账号）从**年**月**日开始余额不连续，请联系总行查证！
				String alterMessage = "";
				if (isAsyn) {
					if (HdqsConstants.CHAXZL_ZHANGH.equals(recordInfo.getQueryKindKey())) {
						alterMessage = "账户" + zhangHParseResult.getZHANGH() + "在" + preJioyrq + "到" + curJioyrq + "之间余额不连续";
						// throw new BalanceBrokedException();
					} else {
						String khzh = zhangHParseResult.getKehuzh();
						String khzhlx = zhangHParseResult.getKHZHLX();
						String khzhlxmc = EnumKhzhlx.valueOf(EnumKhzhlx.LX + khzhlx).getDisplay().getValue();

						alterMessage += recordInfo.getQueryKind() + recordInfo.getValue();
						if (!zhangHParseResult.getRecord().getChaxzl().equals(HdqsConstants.CHAXZL_KEHUZH)) {
							alterMessage += "的" + khzhlxmc + khzh;
						}
						alterMessage += "的账户" + zhangHParseResult.getZHANGH() + "在" + preJioyrq + "到" + curJioyrq + "之间余额不连续";

					}
				} else {

					alterMessage = "账户" + zhangHParseResult.getZHANGH() + "在" + preJioyrq + "到" + curJioyrq + "之间余额不连续";
				}
				if (isAsyn && zhangHParseResult.getRecord().getAuthorize()) {
					String key = recordInfo.getValue() + zhangHParseResult.getRecord().getId();// updated
																								// by
																								// chengqi
																								// 20140607
																								// for
																								// the
																								// blx
																								// file
																								// output
					BrokedReordUtils.cacheBrokedReord(documentContext, key, alterMessage);
				} else {
					throw new BalanceBrokedException(alterMessage);
				}
			}
		}

		return true;

	}

	private boolean verifyItem(int yueefx, int jiedbz) {
		// 处理的规则为： 账户的余额方向与发生额的借贷标志一致余额增加，否则减少
		BigDecimal handleValue;
		if (yueefx == jiedbz) {
			handleValue = preBalance.add(jioyje);
		} else {
			handleValue = preBalance.subtract(jioyje);
		}

		// 精确newBalance
		BigDecimal handleScale = handleValue.setScale(2, BigDecimal.ROUND_HALF_UP);
		BigDecimal currentScale = currentBalance.setScale(2, BigDecimal.ROUND_HALF_UP);
		// 计算精确后的余额和获取的余额的差
		double sub = Math.abs(handleScale.subtract(currentScale).doubleValue());
		LOG.debug(" newValue.compareTo(currentBalance)" + handleScale.compareTo(currentScale) + " sub:" + sub);
		return handleScale.compareTo(currentScale) == 0 || sub < 0.01;
	}

	public static void main(String[] args) {
		testVerify(new BigDecimal(75014.5), new BigDecimal(3281.1), new BigDecimal(71733.39999999999));
		testVerify(new BigDecimal(71733.39999999999), new BigDecimal(6093.0), new BigDecimal(65640.39999999999));
		testVerify(new BigDecimal(65640.39999999999), new BigDecimal(2075.0), new BigDecimal(63565.4));
	}

	public static boolean testVerify(BigDecimal preBalance, BigDecimal jioyje, BigDecimal currentBalance) {
		// BigDecimal preBalance = new BigDecimal(75014.5);
		preBalance = preBalance.subtract(jioyje);
		System.out.println("preBalance:" + preBalance);
		BigDecimal newValue = preBalance.setScale(2, BigDecimal.ROUND_HALF_UP);
		System.out.println("newValue:" + newValue);
		// 计算精确后的余额和获取的余额的差
		// BigDecimal currentBalance=new BigDecimal(71733.39999999999);
		System.out.println("currentBalance:" + currentBalance);
		double sub = Math.abs(newValue.subtract(currentBalance).doubleValue());
		System.out.println("sub:" + sub);
		System.out.println(newValue.compareTo(currentBalance) == 0);
		System.out.println(sub < 0.01);
		System.out.println(newValue.compareTo(currentBalance) == 0 || sub < 0.01);
		return newValue.compareTo(currentBalance) == 0 || sub < 0.01;
	}

}
