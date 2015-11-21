package com.ceb.bank.query.output.pdf;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ceb.bank.constants.FieldConstants;
import com.ceb.bank.context.OutPdfContext;
import com.ceb.bank.context.ZhanghContext;
import com.ceb.bank.item.Item0770;
import com.ceb.bank.utils.HdqsCommonUtils;
import com.ceb.bank.utils.TemplateEngine;
import com.ceb.hdqs.entity.PybjyEO;

public class PdfGenerator0777 extends PdfGenerator<Item0770> {

	@Override
	public String makeXhtml(PybjyEO record, OutPdfContext ctx, List<Item0770> itemList, int pageNum, Object obj) throws IOException {
		Map<String, Object> context = new HashMap<String, Object>();
		if (obj == null) {
			ctx.setQueryExist(false);
			ctx.getTips().add(HdqsCommonUtils.getNoRecordTips(record));
		} else {
			ZhanghContext zhanghCtx = (ZhanghContext) obj;
			if (itemList == null || itemList.isEmpty()) {
				String tips = HdqsCommonUtils.getNoItemsTips(record, zhanghCtx.getZhangh());
				ctx.setQueryExist(false);
				ctx.getTips().add(tips);
			}
			context.put("zhanghCtx", zhanghCtx);
		}

		context.put("pageNum", pageNum);
		context.put("page", ctx);
		context.put("itemList", itemList);

		String str = null;
		if (FieldConstants.PRINT_STYLE_KEHU.equals(record.getPrintStyle())) {
			str = TemplateEngine.getInstance().parseTemp(context, "0770.xhtml");
		} else {
			str = TemplateEngine.getInstance().parseTemp(context, "0777.xhtml");
		}
		return str;
	}

	@Override
	public String makeBlxXhtml(PybjyEO record, OutPdfContext ctx, List<String> blxList, int pageNum, Object obj) throws IOException {
		Map<String, Object> context = new HashMap<String, Object>();
		ZhanghContext zhanghCtx = (ZhanghContext) obj;
		ctx.setQueryExist(false);
		ctx.setTips(blxList);
		context.put("zhanghCtx", zhanghCtx);

		context.put("pageNum", pageNum);
		context.put("page", ctx);

		String str = null;
		if (FieldConstants.PRINT_STYLE_KEHU.equals(record.getPrintStyle())) {
			str = TemplateEngine.getInstance().parseTemp(context, "0770.xhtml");
		} else {
			str = TemplateEngine.getInstance().parseTemp(context, "0777.xhtml");
		}
		return str;
	}
}