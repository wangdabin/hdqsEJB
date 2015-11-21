package com.ceb.hdqs.action.authorize;

import com.ceb.hdqs.entity.result.ParseResult;
import com.ceb.hdqs.query.entity.AuthorizeLevel;

/**
 * 换卡授权
 * 
 * @author user 、
 *  0773   根据客户号、客户账号和账号查询azhjx查询出的YNGYJG│营业机构号！=查的营业机构，输入授权编码1
 *  0773   如果是非客户查询，查BDSKH表 查出 SHFOBZ=1输入授权编码3
 */
public class Authorize0773 extends AbsAuthorize {

	@Override
	public AuthorizeLevel checkAuthority(ParseResult parseResult) {
//		YngyjgAuthorize yngyjgAuthorize = new YngyjgAuthorize();
//		EmployeeAuthorize employeeAuthorize = new EmployeeAuthorize();
//		ReplacementCardParseResult  result = (ReplacementCardParseResult)parseResult;
//		String yngyjg = result.getZHYYNG();
//		int shifbz = result.getShifbz();
		
		AuthorizeLevel authorizeLevel = new AuthorizeLevel();
//		
//		if(parseResult.getRecord().getQueryType().equals(QueryConstants.QUERY_TYPE_CUSTOMER)){
//			return authorizeLevel;
//		}
//		//进行营业机构验证
//		if(!parseResult.getRecord().getOperAgency().equals(yngyjg)){
//			authorizeLevel = yngyjgAuthorize.parse(parseResult);
//		}
//		
//		//进行是否本行柜员验证
//		if(!parseResult.getRecord().getQueryType().equals(QueryConstants.QUERY_TYPE_CUSTOMER)&&(1==shifbz)){
//			AuthorizeLevel level = employeeAuthorize.parse(parseResult);
//			if(authorizeLevel.setKey(level.getKey())){
//				authorizeLevel.setValue(level.getValue());
//			}
//		}
		return authorizeLevel;
	}
}
