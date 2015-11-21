package com.ceb.hdqs.action.query.output;

import java.io.File;
import java.io.StringWriter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;

import com.ceb.hdqs.query.entity.Page;
import com.ceb.hdqs.query.utils.DbcSbcUtils;
import com.ceb.hdqs.query.utils.Entities;
import com.ceb.hdqs.query.utils.QueryConfUtils;
import com.ceb.hdqs.query.utils.QueryMethodUtils;

public class HtmlTemplator {
	private static final Log LOG = LogFactory.getLog(HtmlTemplator.class);

	private VelocityEngine vEngine;
	private VelocityContext vContext;
	private Template template;
	private String TEMPLATE_LOAD_PATH = QueryConfUtils.weblogic_domain_home + File.separator + "config" + File.separator + "templates";

	public HtmlTemplator(String templatePath) {
		vEngine = new VelocityEngine();
		vEngine.setProperty(Velocity.FILE_RESOURCE_LOADER_PATH, TEMPLATE_LOAD_PATH);
		LOG.debug("模板文件目录是：" + TEMPLATE_LOAD_PATH);
		vEngine.setProperty(Velocity.ENCODING_DEFAULT, "UTF-8");
		vEngine.setProperty(Velocity.INPUT_ENCODING, "UTF-8");
		vEngine.setProperty(Velocity.OUTPUT_ENCODING, "UTF-8");
		vContext = new VelocityContext();
		vContext.put("zhaiy", new DbcSbcUtils());
		vContext.put("money", new QueryMethodUtils());
		vContext.put("html", new Entities());
		// vContext.put("number", new NumberTool());
		// vContext.put("aNumber", new Double(0.95));
		// vContext.put("aLocale",Locale.CHINA);

		vEngine.init();
		// 获取模板
		template = vEngine.getTemplate(templatePath, "UTF-8");
	}

	public void setVelocityTemplate(String templatePath) {
		template = vEngine.getTemplate(templatePath, "UTF-8");
	}

	/**
	 * 开始格式化输出
	 * 
	 * @param buffer
	 * @return
	 */
	public String formatOutput(Page<?> page) {
		vContext.put("page", page);
		StringWriter writer = new StringWriter();
		template.merge(vContext, writer);
		// clear(page);
		return writer.toString();
	}

	public static void clear(Page<?> page) {
		if (page.getPageItem() != null)
			page.getPageItem().clear();
		page.setHeader(null);
		page.setPageItem(null);
		page = null;
	}
}