package com.ceb.bank.utils;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.Map;
import java.util.Properties;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import com.ceb.hdqs.config.ConfigLoader;
import com.ceb.hdqs.config.RegisterTable;
import com.ceb.hdqs.query.utils.DbcSbcUtils;
import com.ceb.hdqs.query.utils.Entities;
import com.ceb.hdqs.query.utils.QueryMethodUtils;

/**
 * 懒汉式单例实现的示例
 * 
 */
public final class TemplateEngine implements Serializable {
	private static final long serialVersionUID = 6698079858191490170L;
	public static final String ENCODING_UTF8 = "UTF-8";

	/**
	 * 定义一个变量来存储创建好的类实例
	 */
	private volatile static TemplateEngine instance = null;
	private static Object lock = new Object();

	/**
	 * 定义一个方法来为客户端提供类实例
	 * 
	 * @return 一个Singleton的实例
	 */
	public static TemplateEngine getInstance() {
		// 判断存储实例的变量是否有值
		if (instance == null) {
			synchronized (lock) {
				if (instance == null) {
					// 如果没有，就创建一个类实例，并把值赋值给存储类实例的变量
					String loaderPath = ConfigLoader.getInstance().getProperty(RegisterTable.HDQS_RESOURCE_PATH);
					if (!loaderPath.endsWith("/")) {
						loaderPath += "/";
					}
					instance = new TemplateEngine(loaderPath);
				}
			}
		}
		// 如果有值，那就直接使用
		return instance;
	}

	/**
	 * private的构造函数用于避免外界直接使用new来实例化对象
	 */
	private TemplateEngine(String loaderPath) {
		Properties p = new Properties();
		p.setProperty(Velocity.FILE_RESOURCE_LOADER_PATH, loaderPath);
		p.setProperty(Velocity.ENCODING_DEFAULT, ENCODING_UTF8);
		p.setProperty(Velocity.INPUT_ENCODING, ENCODING_UTF8);
		p.setProperty(Velocity.OUTPUT_ENCODING, ENCODING_UTF8);

		try {
			Velocity.init(p);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String parseTemp(Map<String, Object> context, String templateFile) throws IOException {
		VelocityContext vContext = new VelocityContext(context);
		vContext.put("zhaiy", new DbcSbcUtils());
		vContext.put("money", new QueryMethodUtils());
		vContext.put("html", new Entities());
		Template template = Velocity.getTemplate(templateFile, ENCODING_UTF8);
		StringWriter sw = new StringWriter();
		template.merge(vContext, sw);
		return sw.toString();
	}

	public void genFileFromTemp(Map<String, Object> root, String templateFile, BufferedWriter writer) throws IOException {
		try {
			VelocityContext context = new VelocityContext(root);
			Template template = Velocity.getTemplate(templateFile, "UTF-8");
			template.merge(context, writer);
		} finally {
			writer.flush();
			writer.close();
		}
	}

	public String parseStr(Map<String, Object> root, String instring) throws IOException {
		VelocityContext context = new VelocityContext(root);
		StringWriter sw = new StringWriter();
		Velocity.evaluate(context, sw, "VTL", instring);
		return sw.toString();
	}
}