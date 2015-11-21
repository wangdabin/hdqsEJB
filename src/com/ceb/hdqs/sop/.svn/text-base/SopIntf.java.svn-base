package com.ceb.hdqs.sop;

/**
 * @author LIYD
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SopIntf {

	public static final int SIZEOF_SHORT = Short.SIZE / Byte.SIZE;
	public static final int SIZEOF_BOOLEAN = Byte.SIZE / Byte.SIZE;
	public static final int SIZEOF_BYTE = SIZEOF_BOOLEAN;
	public static final int SIZEOF_CHAR = Character.SIZE / Byte.SIZE;
	public static final int SIZEOF_DOUBLE = Double.SIZE / Byte.SIZE;
	public static final int SIZEOF_FLOAT = Float.SIZE / Byte.SIZE;
	public static final int SIZEOF_INT = Integer.SIZE / Byte.SIZE;
	public static final int SIZEOF_LONG = Long.SIZE / Byte.SIZE;

	public static String CharacterEncoding = "GBK"; // 全局字符集
	public static String SOP_SUCC = "AAAAAAA";
	public static String SOP_SYSERR = "SOP";
	public static String ERR_NOTFOUND = "SOP0100"; // 数据项不存在
	public static String ERR_SYSERROR = "SOP0000"; // 系统错误
	public static String ERR_NOCONNECT = "SOP0002"; // 连接失败
	public static String ERR_IOERROR = "SOP0003"; // 网络读写错误
	public static String ERR_ASSEMBPKT = "SOP0006"; // 打包失败
	public static String ERR_DISASSPKT = "SOP0008"; // 拆包错误
	public static boolean DEBUG = false; // 打印调试信息
	public static String SYSCFG_SEP = "\t"; // syscfg文件分隔符
	public static String MAPFILE_SEP = " "; // mapfile文件分隔符
	public static String PTCWXX = "PTCWXX"; // sop返回状态信息存放的key
	public static String TPU_RETCODE = "YYCWDM"; // “返回码”字段在syscfg中的名称
	public static String PDTRCD = "PDTRCD"; // “交易”字段在syscfg中的名称
	public static String FDCOMMLN = "FDCOMMLN"; // “交易长度”字段在syscfg中的名称

	// public static final int USE_COP = 1;
	static final int CONVERT_ERROR_SEND_MAPFILE_READ = -6;
	static final int CONVERT_ERROR_NO_ENOUGH_DATA = -7;
	static final int CONVERT_ERROR_RECV_MAPFILE_READ = -8;
	static final byte F_SPECIAL_CHAR = (byte) 0xfe;
	static final byte F_SUPER_LEN_SIGN = (byte) 0xff;
	static final int BYTE_MAX_LEN = 0x0fa;
	private static final Map<String, List<String>> cfgs = new HashMap<String, List<String>>();
	// 配置文件根目录
	public static final String CONF_PATH;
	// 系统配置文件目录
	public static final String SYSCFG_PATH;
	public static final String MAPFILE_PATH;

	public static final String SYSTEM_HEAD_PATH;
	public static final String CMTRAN_RCV_HEAD_PATH;
	public static final String ERR000_PATH;
	public static final String CMTRAN_HEAD_PATH;
	public static final String TRAN_HEAD_PATH;
	public static final String SECRET_KEY_IN_PATH;
	public static final String SECRET_KEY_OUT_PATH;

	static final int PROT_SOP = 0;
	static final int PROT_IPP = 1;
	static final int PROT_TUX = 2;
	public static String home;
	public static String separator;

	int flag = 0;
	int iVal;
	HashMap<Object, Object> pool = new HashMap<Object, Object>(30);
	HashMap<Object, Object> pool2;
	byte[] buffer;
	String[] inmap;
	String[] outmap;

	public String code;
	public String errmsg;
	public String errcode;

	static String g_aczHost;
	static String g_aczHostC;
	static int g_iPort = 6005;
	static int g_iPortC = 20032;
	static int g_iProtocol = PROT_IPP;

	static {
		separator = System.getProperty("file.separator");
		// separator = "/";
		CONF_PATH = separator + "config";
		SYSCFG_PATH = CONF_PATH + separator + "syscfg";
		MAPFILE_PATH = CONF_PATH + separator + "mapfile";

		SYSTEM_HEAD_PATH = SYSCFG_PATH + separator + "system_head.cfg";
		CMTRAN_RCV_HEAD_PATH = SYSCFG_PATH + separator + "cmtran_rcv_head.cfg";
		ERR000_PATH = SYSCFG_PATH + separator + "ERR000";
		CMTRAN_HEAD_PATH = SYSCFG_PATH + separator + "cmtran_head.cfg";
		TRAN_HEAD_PATH = SYSCFG_PATH + separator + "tran_head.cfg";
		SECRET_KEY_IN_PATH = SYSCFG_PATH + separator + "secret_key_in.cfg";
		SECRET_KEY_OUT_PATH = SYSCFG_PATH + separator + "secret_key_out.cfg";

		home = SopIntf.getEnv("SOPHOME");
		initEnv("ww");

		try {
			init();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static final void init() throws IOException {
		List<String> lines = readLines(SYSTEM_HEAD_PATH); // 系统信息头——system_head.cfg
		cfgs.put(SYSTEM_HEAD_PATH, lines);

		lines = readLines(CMTRAN_RCV_HEAD_PATH); // 交易公共头——cmtran_rcv_head.cfg
		cfgs.put(CMTRAN_RCV_HEAD_PATH, lines);

		lines = readLines(ERR000_PATH); // 如果TPU_RETCODE不为AAAAAAA,则按照ERR000打包
		cfgs.put(ERR000_PATH, lines);

		lines = readLines(CMTRAN_HEAD_PATH); //
		cfgs.put(CMTRAN_HEAD_PATH, lines);

		lines = readLines(TRAN_HEAD_PATH); //
		cfgs.put(TRAN_HEAD_PATH, lines);

		lines = readLines(SECRET_KEY_IN_PATH); //
		cfgs.put(SECRET_KEY_IN_PATH, lines);

		// lines = readLines(SECRET_KEY_OUT_PATH); //
		// cfgs.put(SECRET_KEY_OUT_PATH, lines);
	}

	/**
	 * 
	 * @param key
	 * @return
	 * @throws IOException
	 */
	private static final List<String> getLines(String key) throws IOException {
		List<String> lines = cfgs.get(key);
		if (lines == null) {
			lines = readLines(key);
			if (lines != null) {
				cfgs.put(key, lines);
			}
		}
		return lines;
	}

	public static boolean initEnv(String flag) {
		try {
			InputStream is = null;

			String s = home + separator + "syscfg" + separator + "wop.xml";
			File f = new File(s);
			if (f.exists()) {
				is = new FileInputStream(f);
			}
			if (is == null) {
				return false;
			}

			org.jdom.input.SAXBuilder sb = new org.jdom.input.SAXBuilder();
			org.jdom.Document doc = sb.build(is);
			org.jdom.Element fld, param;
			fld = doc.getRootElement().getChild("Config");
			param = fld.getChild("sopurl_" + flag);

			if (param != null) {
				g_aczHost = param.getText().trim();
				int t = g_aczHost.indexOf("://");
				if (t > 0) {
					if (g_aczHost.startsWith("sop")) {
						g_iProtocol = PROT_SOP;
					} else if (g_aczHost.startsWith("tux")) {
						g_iProtocol = PROT_TUX;
					} else {
						g_iProtocol = PROT_IPP;
					}
					g_aczHost = g_aczHost.substring(t + 3);
				}
				t = g_aczHost.indexOf(':');

				if (t > 0) {
					g_iPort = Integer.parseInt(g_aczHost.substring(t + 1));
					g_aczHost = g_aczHost.substring(0, t);
				}
			}
			param = fld.getChild("crypturl_" + flag);
			if (param != null) {
				g_aczHostC = param.getText().trim();
				int t = g_aczHostC.indexOf(':');
				if (t > 0) {
					g_iPortC = Integer.parseInt(g_aczHostC.substring(t + 1));
					g_aczHostC = g_aczHostC.substring(0, t);
				}
			}
			return true;
		} catch (Exception err) {
			err.printStackTrace();
		}
		return false;
	}

	int sopCall(byte[] in, byte[] out, int ilen, int olen) {
		Socket socket;

		try {
			// InetAddress addr = InetAddress.getByName(g_aczHost);
			socket = new Socket(g_aczHost, g_iPort);
			socket.setSoTimeout(3 * 60 * 1000);
		} catch (IOException e) {
			return -2;
		}
		try {
			InputStream input = socket.getInputStream();
			OutputStream output = socket.getOutputStream();
			String prefix = null;
			if (DEBUG) {
				prefix = home + separator + "log" + separator + "SOP_" + code + "_" + System.currentTimeMillis();
				String fname = prefix + "_S.dat";
				OutputStream outf = new FileOutputStream(fname);
				outf.write(in, 0, ilen);
				outf.close();
			}
			if (g_iProtocol == PROT_SOP) {
				out[0] = 0;
				out[1] = 0;
				out[2] = (byte) ((ilen >> 8) & 0x0ff);
				out[3] = (byte) (ilen & 0x0ff);
				output.write(out, 0, 4);
				int x = ilen / 1024;
				int y = ilen % 1024;

				if (x > 0) {
					out[2] = 4;
					out[3] = 0;
					int z = 0;
					for (int i = 0; i < x; i++) {
						output.write(in, z, 1024);
						z += 1024;
						if (i < x - 1) {
							output.write(out, 0, 4);
						}
					}
					if (y > 0) {
						out[2] = (byte) ((y >> 8) & 0x0ff);
						out[3] = (byte) (y & 0x0ff);
						output.write(out, 0, 4);
						output.write(in, z, y);
					}
				} else {
					out[2] = 4;
					out[3] = 0;
					output.write(in, 0, ilen);
				}
			} else {
				output.write(in, 0, ilen);
			}
			if (g_iProtocol == PROT_SOP) {
				if (input.read(out, 0, 4) < 4) {
					// System.out.println("bbbbbbbbbbbbbbbbbbbbbb" +
					// input.read(out, 0, 2));
					return -3;
				}
			}
			if (input.read(out, 0, 2) < 2) {
				// System.out.println("222222222222222222222" + input.read(out,
				// 0, 2));
				return -3;
			}
			int len, left, ptr, t;
			if ((len = out[0]) < 0) {
				len += 256;
			}
			len <<= 8;
			if (out[1] < 0) {
				len += 256 + out[1];
			} else {
				len += out[1];
			}
			if (len > olen) {
				return -1;
			}

			if (g_iProtocol == PROT_SOP) {
				int x = len / 1024;
				int y = len % 1024;
				ptr = 2;
				if (x == 0) {
					left = y - 2;
				} else {
					left = 1022;
					for (int i = 0; i < x; i++) {
						while ((t = input.read(out, ptr, left)) > 0) {
							ptr += t;
							if ((left -= t) <= 0) {
								break;
							}
						}
						if (left > 0) {
							// System.out.println("aaaaaaaaaaaaaaaaaaaaa"+
							// input.read(out, 0, 2));
							return -3;
						}

						left = 1024;
						if (y > 0 || i < x - 1) {
							if (input.read(out, ptr, 4) < 4) {
								// System.out.println("444444444444444444");
								return -3;
							}
						}
					}
					left = y;
				}
				if (left > 0) {
					while ((t = input.read(out, ptr, left)) > 0) {
						if ((left -= t) <= 0)
							break;

						ptr += t;
					}
					if (left > 0) {
						// System.out.println("55555555555555555");
						return -3;
					}
				}
			} else {
				left = len - 2;
				ptr = 2;
				while ((t = input.read(out, ptr, left)) > 0) {
					if ((left -= t) <= 0) {
						break;
					}
					ptr += t;
				}
				if (left > 0) {
					// System.out.println("666666666666666666");
					return -3;
				}
			}
			if (DEBUG) {
				String fname = prefix + "_R.dat";
				OutputStream outf = new FileOutputStream(fname);
				outf.write(out, 0, len);
				outf.close();
			}
			return len;
		} catch (IOException e) {
			// System.out.println("7777777777777777");
			return -3;
		} finally {
			try {
				socket.close();
			} catch (IOException e2) {
			}
		}
	}

	static int crypt(byte[] in, int ilen, byte[] out, int olen) {
		Socket socket;
		try {
			socket = new Socket(g_aczHostC, g_iPortC);
		} catch (IOException e) {
			return -5;
		}
		try {
			InputStream input = socket.getInputStream();
			OutputStream output = socket.getOutputStream();
			output.write(in, 0, ilen);
			int left = olen;
			int ptr = 0;
			int t;
			while ((t = input.read(out, ptr, left)) > 0) {
				if ((left -= t) <= 0) {
					break;
				}
				ptr += t;
			}
			if (left > 0) {
				return -5;
			}

			return olen;
		} catch (IOException e) {
		} finally {
			try {
				socket.close();
			} catch (IOException e2) {
			}
		}
		return -5;
	}

	public static String getEnv(String env) {
		try {
			String s;
			Process pp;
			if (separator.equals("/")) {
				String ss[] = { "sh", "-c", "echo $" + env };
				pp = Runtime.getRuntime().exec(ss);
			} else {
				s = "cmd /C echo %" + env + "%";
				pp = Runtime.getRuntime().exec(s);
			}
			InputStream out = pp.getInputStream();
			byte[] b = new byte[200];
			int t = out.read(b);
			if (t > 0) {
				s = new String(b, 0, t);
				int t1;
				t = s.indexOf('\r');
				t1 = s.indexOf('\n');
				if (t > 0 || t1 > 0) {
					if (t <= 0) {
						t = t1;
					}
					s = s.substring(0, t);
				}
				return s;
			}
		} catch (Exception e) {
		}
		return "";
	}

	static int split(String parentStr, String subStr, String temp[], int size) {
		int cnt = 0;
		int oldPos = 0;

		if (parentStr == null) {
			parentStr = "";
			return 0;
		}
		int newPos = parentStr.indexOf(subStr);
		int parentLength = parentStr.length();
		int subStrLength = subStr.length();
		if (newPos != -1) {
			newPos += subStrLength;
		}

		while ((newPos <= parentLength) && (newPos != -1)) {
			if (cnt < size) {
				temp[cnt] = parentStr.substring(oldPos, newPos - subStrLength);
			} else {
				return cnt;
			}
			cnt++;
			oldPos = newPos;
			newPos = parentStr.indexOf(subStr, oldPos);
			if (newPos != -1) {
				newPos += subStrLength;
			}
		}
		if (oldPos < parentLength) {
			if (cnt < size) {
				temp[cnt] = parentStr.substring(oldPos);
			} else {
				return cnt;
			}
			cnt++;
		}
		return cnt;
	}

	public boolean put(String obj, String key, String value) {
		try {
			if (obj != null) {
				key = obj + "." + key;
			}
			if (!pool.containsKey(key))
				pool.put(key, value);
			else {
				pool.remove(key);
				pool.put(key, value);
			}
			return true;
		} catch (Exception err) {
			err.printStackTrace();
			errmsg = err.getMessage();
		}
		errcode = ERR_SYSERROR;
		return false;
	}

	public boolean put(String obj, String key, Long value) {
		try {
			if (obj != null) {
				key = obj + "." + key;
			}
			pool.put(key, value);
			return true;
		} catch (Exception err) {
			err.printStackTrace();
			errmsg = err.getMessage();
		}
		errcode = ERR_SYSERROR;
		return false;
	}

	public boolean put(String obj, String key, Integer value) {
		try {
			if (obj != null) {
				key = obj + "." + key;
			}
			pool.put(key, convertIntegerToByte(value));
			return true;
		} catch (Exception err) {
			err.printStackTrace();
			errmsg = err.getMessage();
		}
		errcode = ERR_SYSERROR;
		return false;
	}

	public boolean put(String obj, String key, byte[] value) {
		try {
			if (obj != null) {
				key = obj + "." + key;
			}
			pool.put(key, value);
			return true;
		} catch (Exception err) {
			err.printStackTrace();
			errmsg = err.getMessage();
		}
		errcode = ERR_SYSERROR;
		return false;
	}

	public boolean put(String obj, String key, Short value) {
		try {
			if (obj != null) {
				key = obj + "." + key;
			}
			pool.put(key, value);
			return true;
		} catch (Exception err) {
			err.printStackTrace();
			errmsg = err.getMessage();
		}
		errcode = ERR_SYSERROR;
		return false;
	}

	public boolean put(String obj, String form, String key, String[] value) {
		try {
			if (form != null) {
				key = form + "_" + key;
			}
			if (obj != null) {
				key = obj + "." + key;
			}
			pool.put(key, value);
			return true;
		} catch (Exception err) {
			err.printStackTrace();
			errmsg = err.getMessage();
		}
		errcode = ERR_SYSERROR;
		return false;
	}

	public boolean put(String obj, String form, String key, byte[][] value) {
		try {
			if (form != null) {
				key = form + "_" + key;
			}
			if (obj != null) {
				key = obj + "." + key;
			}
			pool.put(key, value);
			return true;
		} catch (Exception err) {
			err.printStackTrace();
			errmsg = err.getMessage();
		}
		errcode = ERR_SYSERROR;
		return false;
	}

	public String getStr(String obj, String key) {
		try {
			if (obj != null) {
				key = obj + "." + key;
			}
			Object val = pool.get(key);
			if (val == null) {
				if (pool.containsKey(key)) {
					return "";
				} else {
					errmsg = "该字段不存在";
					errcode = ERR_NOTFOUND;
					return "";
				}
			}
			if (val instanceof byte[]) {
				if (key.equals("SJBSXH")) {
					return String.valueOf(convertBytesToShort((byte[]) val));
				} else if (key.equals("MIYBBH")) {
					return String.valueOf(convertBytesToInteger((byte[]) val));
				} else if (key.equals("SHJBCD")) {
					return String.valueOf(convertBytesToShort((byte[]) val));
				} else if (key.equals("JYSJCD")) {
					return String.valueOf(convertBytesToShort((byte[]) val));
				} else if (key.equals("JIOYXH")) {
					return String.valueOf(convertBytesToInteger((byte[]) val));
				} else if (key.equals("JIOYSJ")) {
					return String.valueOf(convertBytesToInteger((byte[]) val));
				}
				return new String((byte[]) val, CharacterEncoding);
			}
			if (val instanceof String) {
				return (String) val;
			}

			errmsg = "该字段类型不正确";
		} catch (Exception err) {
			err.printStackTrace();
			errmsg = err.getMessage();
		}
		errcode = ERR_SYSERROR;
		return "";
	}

	public String[] getStrs(String obj, String form, String key) {
		try {
			if (form != null) {
				key = form + "_" + key;
			}
			if (obj != null) {
				key = obj + "." + key;
			}
			Object val = pool.get(key);
			if (val == null) {
				if (pool.containsKey(key)) {
					return new String[0];
				} else {
					errmsg = "该字段不存在";
					errcode = ERR_NOTFOUND;
					return null;
				}
			}
			if (val instanceof byte[][]) {
				int cnt = ((byte[][]) val).length;
				String[] arr = new String[cnt];
				for (int i = 0; i < cnt; i++) {
					arr[i] = new String(((byte[][]) val)[i], CharacterEncoding);
				}
				return arr;
			}
			if (val instanceof String[]) {
				return (String[]) val;
			}
			errmsg = "该字段类型不正确";
		} catch (Exception ex) {
			errmsg = ex.getMessage();
		}
		errcode = ERR_SYSERROR;
		return null;
	}

	public byte[] get(String obj, String key) {
		try {
			if (obj != null) {
				key = obj + "." + key;
			}
			Object val = pool.get(key);
			if (val == null) {
				if (pool.containsKey(key)) {
					return new byte[0];
				} else {
					errmsg = "该字段不存在";
					errcode = ERR_NOTFOUND;
					return null;
				}
			}
			if (val instanceof byte[]) {
				return (byte[]) val;
			}

			if (val instanceof String) {
				return ((String) val).getBytes(CharacterEncoding);
			}

			errmsg = "该字段类型不正确";
		} catch (Exception err) {
			err.printStackTrace();
			errmsg = err.getMessage();
		}
		errcode = ERR_SYSERROR;
		return null;
	}

	public byte[][] gets(String obj, String form, String key) {
		try {
			if (form != null) {
				key = form + "_" + key;
			}
			if (obj != null) {
				key = obj + "." + key;
			}
			Object val = pool.get(key);
			if (val == null) {
				if (pool.containsKey(key)) {
					return new byte[0][0];
				} else {
					errmsg = "该字段不存在";
					errcode = ERR_NOTFOUND;
					return null;
				}
			}
			if (val instanceof byte[][])
				return (byte[][]) val;

			if (val instanceof String[]) {
				int cnt = ((byte[][]) val).length;
				byte[][] arr = new byte[cnt][];
				for (int i = 0; i < cnt; i++) {
					arr[i] = ((String[]) val)[i].getBytes(CharacterEncoding);
				}
				return arr;
			}
			errmsg = "该字段类型不正确";
		} catch (Exception ex) {
			errmsg = ex.getMessage();
		}
		errcode = ERR_SYSERROR;
		return null;
	}

	public byte[] getAttrib(String obj, String form, String key) {
		try {
			if (form != null) {
				key = form + "_" + key;
			}
			if (obj != null) {
				key = obj + "." + key;
			}
			Object val = pool.get(key + "_ATTRIB");
			return (byte[]) val;
		} catch (Exception ex) {
			errmsg = ex.getMessage();
		}
		return null;
	}

	public int getSize(String obj, String form) {
		int ret = 0;
		try {
			String val = getStr(obj, form);
			if (val == null) {
				return -1;
			}

			if (!val.equals("")) {
				ret = Integer.parseInt(val);
			}
		} catch (Exception ex) {
			errmsg = ex.getMessage();
			ret = -1;
		}
		return ret;
	}

	public String[] getObjects() {
		return outmap;
	}

	int getDduStringMem(byte[] buf, byte[] data, int iCurrPos, int iLen) {
		int len, iLen2;
		try {
			iLen2 = 0;
			while (buf[iCurrPos] == F_SPECIAL_CHAR) {
				iCurrPos++;
				if ((len = buf[iCurrPos]) < 0) {
					len = 256 + len;
				}
				iCurrPos += len + 1;
			}
			while (buf[iCurrPos] == F_SUPER_LEN_SIGN) {
				len = BYTE_MAX_LEN;
				iCurrPos++;
				if ((iLen2 + len) <= iLen) {
					System.arraycopy(buf, iCurrPos, data, 0, len);
				}
				iLen2 += len;
				iCurrPos += len;
			}
			if ((len = buf[iCurrPos]) < 0) {
				len = 256 + len;
			}
			if (len <= BYTE_MAX_LEN) {
				iCurrPos++;
				if ((iLen2 + len) <= iLen) {
					System.arraycopy(buf, iCurrPos, data, 0, len);
				}
				iLen2 += len;
				iCurrPos += len;
			}
			if (iLen2 <= iLen) {
				// System.out.println("数字[" + iLen2 + "]和数字[" + iVal + "]");
				iVal = iLen2;
			} else {
				iVal = 0;
			}
		} catch (Exception ex) {
			iVal = 0;
			iCurrPos = -1;
		}
		return iCurrPos;
	}

	int calDduStringMem(byte[] buf, int iCurrPos) {
		int len;

		while (buf[iCurrPos] == F_SPECIAL_CHAR) {
			iCurrPos++;
			if ((len = buf[iCurrPos]) < 0) {
				len = 256 + len;
			}
			iCurrPos += len + 1;
		}
		while (buf[iCurrPos] == F_SUPER_LEN_SIGN) {
			iCurrPos += BYTE_MAX_LEN + 1;
		}
		if ((len = buf[iCurrPos]) < 0) {
			len = 256 + len;
		}
		if (len <= BYTE_MAX_LEN) {
			iCurrPos += len + 1;
		}
		return iCurrPos;
	}

	int putDduLong(byte[] buf, Long value, int iCurrPos) {
		int iLen;
		int iTmp;

		try {
			if (value == null) {
				buf[iCurrPos++] = 0;
				return iCurrPos;
			}
			byte val[] = this.convertLongToByte(value);
			iLen = val.length;

			if (iLen <= BYTE_MAX_LEN) {
				buf[iCurrPos++] = (byte) iLen;
				System.arraycopy(val, 0, buf, iCurrPos, iLen);
				iCurrPos += iLen;
				return iCurrPos;
			}
			int value_pos = 0;
			while (iLen > 0) {
				if (iLen > BYTE_MAX_LEN) {
					iTmp = BYTE_MAX_LEN;
					buf[iCurrPos++] = F_SUPER_LEN_SIGN;
				} else {
					iTmp = iLen;
					buf[iCurrPos++] = (byte) iLen;
				}
				System.arraycopy(val, value_pos, buf, iCurrPos, iTmp);
				iCurrPos += iTmp;
				iLen -= iTmp;
				value_pos += iTmp;
			}
		} catch (Exception err) {
			iCurrPos = -1;
		}
		return iCurrPos;
	}

	int putDduShort(byte[] buf, Short value, int iCurrPos) {
		int iLen;
		int iTmp;

		try {
			if (value == null) {
				buf[iCurrPos++] = 0;
				return iCurrPos;
			}
			byte val[] = this.convertShortToByte(value);
			iLen = val.length;

			if (iLen <= BYTE_MAX_LEN) {
				buf[iCurrPos++] = (byte) iLen;
				System.arraycopy(val, 0, buf, iCurrPos, iLen);
				iCurrPos += iLen;
				return iCurrPos;
			}
			int value_pos = 0;
			while (iLen > 0) {
				if (iLen > BYTE_MAX_LEN) {
					iTmp = BYTE_MAX_LEN;
					buf[iCurrPos++] = F_SUPER_LEN_SIGN;
				} else {
					iTmp = iLen;
					buf[iCurrPos++] = (byte) iLen;
				}
				System.arraycopy(val, value_pos, buf, iCurrPos, iTmp);
				iCurrPos += iTmp;
				iLen -= iTmp;
				value_pos += iTmp;
			}
		} catch (Exception err) {
			iCurrPos = -1;
		}
		return iCurrPos;
	}

	int putDduString(byte[] buf, String value, int iCurrPos) {
		int iLen;
		int iTmp;

		try {
			if (value == null) {
				buf[iCurrPos++] = 0;
				return iCurrPos;
			}
			byte val[] = value.getBytes(CharacterEncoding);
			iLen = val.length;

			if (iLen <= BYTE_MAX_LEN) {
				buf[iCurrPos++] = (byte) iLen;
				System.arraycopy(val, 0, buf, iCurrPos, iLen);
				iCurrPos += iLen;
				return iCurrPos;
			}
			int value_pos = 0;
			while (iLen > 0) {
				if (iLen > BYTE_MAX_LEN) {
					iTmp = BYTE_MAX_LEN;
					buf[iCurrPos++] = F_SUPER_LEN_SIGN;
				} else {
					iTmp = iLen;
					buf[iCurrPos++] = (byte) iLen;
				}
				System.arraycopy(val, value_pos, buf, iCurrPos, iTmp);
				iCurrPos += iTmp;
				iLen -= iTmp;
				value_pos += iTmp;
			}
		} catch (Exception err) {
			iCurrPos = -1;
		}
		return iCurrPos;
	}

	int putDduStringMem(byte[] buf, byte[] val, int iCurrPos, int iLen) {
		int iTmp;

		try {
			if (iLen == 0) {
				buf[iCurrPos++] = 0;
				return iCurrPos;
			}
			if (iLen <= BYTE_MAX_LEN) {
				buf[iCurrPos++] = (byte) iLen;
				System.arraycopy(val, 0, buf, iCurrPos, iLen);
				iCurrPos += iLen;
				return iCurrPos;
			}
			int value_pos = 0;
			while (iLen > 0) {
				if (iLen > BYTE_MAX_LEN) {
					iTmp = BYTE_MAX_LEN;
					buf[iCurrPos++] = F_SUPER_LEN_SIGN;
				} else {
					iTmp = iLen;
					buf[iCurrPos++] = (byte) iLen;
				}
				System.arraycopy(val, value_pos, buf, iCurrPos, iTmp);
				iCurrPos += iTmp;
				iLen -= iTmp;
				value_pos += iTmp;
			}
		} catch (Exception err) {
			iCurrPos = -1;
		}
		return iCurrPos;
	}

	public int convertPoolToSopServer(byte[] buffer, int len, String imapf) {
		return convertPoolToSopServer(buffer, len, new String[] { imapf });
	}

	int convertPoolToSopServer(byte[] buffer, int len, String[] imapf) {
		this.buffer = buffer;
		int res = convertPoolToPktServer(buffer, imapf);
		if (res < 0) {
			errmsg = "请检查交易[" + code + "]的mapfile是否已更新(" + res + ")";
			errcode = ERR_ASSEMBPKT;
		}
		this.buffer = null;
		return res;
	}

	/**
	 * NEW ADD 通用的得到资源流
	 * 
	 * @param path
	 * @return
	 */
	private static final InputStream getResourceAsStream(String path) {
		return SopIntf.class.getResourceAsStream(path);
	}

	private static List<String> readLines(String path) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(getResourceAsStream(path)));
		String line = br.readLine();
		List<String> lines = new ArrayList<String>();
		while (line != null) {
			lines.add(line);
			line = br.readLine();
		}
		br.close();
		return lines;
	}

	/**
	 * 将对象转换报文
	 * 
	 * @param buffer
	 *            字节数组
	 * @param imapf
	 *            文件名称数组
	 * @return
	 */
	public int convertPoolToPktServer(byte[] buffer, String[] imapf) {

		int iTotalLen = 0;
		try {
			List<String> lines = getLines(SYSTEM_HEAD_PATH);
			String flds[] = new String[5];
			int len;
			for (String s : lines) {
				if (split(s, SYSCFG_SEP, flds, 3) >= 3) {
					if (flds[2].equals("I")) {
						len = SIZEOF_SHORT; // short长度
					} else {
						len = Integer.parseInt(flds[1]);
					}
					Object val = pool.get(flds[0]);
					byte[] bs = null;
					if (val != null) {
						if (val instanceof String) {
							if (flds[0].equals("SYSSEQNUM")) {
								Short tempval = Short.valueOf(val.toString());
								bs = convertShortToByte(tempval);
							} else if (flds[0].equals("SYSMYAOBBH")) {
								Long tempval = Long.valueOf(val.toString());
								bs = convertLongToByte(tempval);
							} else
								bs = ((String) val).getBytes(CharacterEncoding);
						} else if (val instanceof byte[])
							bs = (byte[]) val;
					}

					if (bs != null) {
						System.arraycopy(bs, 0, buffer, iTotalLen, bs.length);
					}
					iTotalLen += len;
				}
			}

			/***************** 系统信息头——system_head.cfg **************************************/
			// System.out.println("HELLO WORLD!!System_head.cfg![" + iTotalLen +
			// "]");
			/***************** 交易公共头——cmtran_rcv_head.cfg **********************************/

			List<String> lines2 = getLines(CMTRAN_RCV_HEAD_PATH);
			for (String s : lines2) {
				if (split(s, SYSCFG_SEP, flds, 3) >= 3) {
					if (flds[2].equals("I")) {
						len = SIZEOF_SHORT;
					} else {
						len = Integer.parseInt(flds[1]);
					}
					Object val = pool.get(flds[0]);
					byte[] bs = null;
					if (val != null) {
						if (val instanceof String) {
							bs = ((String) val).getBytes();
						} else if (val instanceof Long) {
							bs = this.convertLongToByte((Long) val);
						} else if (val instanceof Short) {
							bs = this.convertShortToByte((Short) val);
						} else if (val instanceof Integer) {
							bs = this.convertIntegerToByte((Integer) val);
						} else if (val instanceof byte[]) {
							bs = (byte[]) val;
						}
					}
					if (bs != null) {
						System.arraycopy(bs, 0, buffer, iTotalLen, bs.length);
					}
					iTotalLen += len;
				}
			}
			/***************** 交易公共头——cmtran_rcv_head.cfg **********************************/

			/***************** 判断TPU_RETCODE的值决定如何打包 **********************************/
			String strRetCode = getStr(null, TPU_RETCODE);
			/***************** 判断TPU_RETCODE的值决定如何打包 **********************************/
			// System.out.println("[" + strRetCode + "]");
			/***************** 如果TPU_RETCODE等于AAAAAAA,则按照mapfile打包 *********************/
			if (strRetCode.equals("AAAAAAA")) {
				if (imapf instanceof String[]) {
					// int row = ((String[]) imapf).length;
					// if (row > 0) {
					// iTotalLen = putDduString(buffer, ((String[]) imapf)[0],
					// iTotalLen);
					// }

				}
				int cnt = imapf.length;
				for (int ii = 0; ii < cnt; ii++) {
					iTotalLen = putDduString(buffer, imapf[ii], iTotalLen);
					int pos = 0;
					int t = iTotalLen;
					code = imapf[ii].substring(1, 5);
					String imapfPath = MAPFILE_PATH + separator + imapf[ii];
					List<String> lines3 = getLines(imapfPath);
					if (lines3.isEmpty()) {
						return CONVERT_ERROR_SEND_MAPFILE_READ;
					}
					for (int f = 1; f < lines3.size(); f++) {
						String s = lines3.get(f);
						if (s.indexOf("FLD:") >= 0) {
							if (split(s, MAPFILE_SEP, flds, 1) >= 1) {
								s = flds[0].substring(4);
								Object val = pool.get(imapf[ii] + "." + s);
								if (val != null) {
									if (val instanceof String)
										iTotalLen = putDduString(buffer, (String) val, iTotalLen);
									else if (val instanceof byte[])
										iTotalLen = putDduStringMem(buffer, (byte[]) val, iTotalLen, ((byte[]) val).length);
								} else
									buffer[iTotalLen++] = 0;
							}
						} else if (s.indexOf("GRD:") >= 0) {
							int j, i = 0;
							int col = 0, row = 0;
							int pos2 = 0;
							Object[] arr = new Object[100];
							try {
								String ss = s.substring(4);
								iTotalLen = putDduString(buffer, ss, iTotalLen);
								pos2 = iTotalLen;
								iTotalLen += 2;

								String key = MAPFILE_PATH + separator + ss;
								List<String> lines4 = getLines(key);
								if (lines4.isEmpty()) {
									return CONVERT_ERROR_SEND_MAPFILE_READ;
								}
								ss = imapf[ii] + "." + ss;
								for (int k = 1; k < lines4.size(); k++) {
									String ss1 = lines4.get(k);
									if (ss1.indexOf("FLD:") >= 0) {
										if (split(ss1, MAPFILE_SEP, flds, 1) >= 1) {
											Object val = pool.get(ss + "_" + flds[0].substring(4));
											if (val != null) {
												if (val instanceof byte[][]) {
													row = ((byte[][]) val).length;
													if (row > 0)
														iTotalLen = putDduStringMem(buffer, ((byte[][]) val)[0], iTotalLen, ((byte[][]) val)[0].length);
												} else if (val instanceof String[]) {
													row = ((String[]) val).length;
													if (row > 0)
														iTotalLen = putDduString(buffer, ((String[]) val)[0], iTotalLen);
												}
												arr[col] = val;
											} else {
												iTotalLen = putDduString(buffer, (String) val, iTotalLen);
												arr[col] = null;
											}
										}
									}
									col++;
								}
							} catch (Exception err) {
							}
							if (row == 0) {
								buffer[pos2] = 0;
								buffer[pos2 + 1] = 0;
								iTotalLen = pos2 + 2;
							} else {
								buffer[pos2] = (byte) row;
								buffer[pos2 + 1] = (byte) col;
								for (j = 1; j < row; j++) {
									for (i = 0; i < col; i++) {
										if (arr[i] == null)
											iTotalLen = putDduString(buffer, null, iTotalLen);
										else {
											if (arr[i] instanceof byte[][]) {
												iTotalLen = putDduStringMem(buffer, ((byte[][]) arr[i])[j], iTotalLen, ((byte[][]) arr[i])[j].length);
											} else if (arr[i] instanceof String[]) {
												iTotalLen = putDduString(buffer, ((String[]) arr[i])[j], iTotalLen);
											}
										}
									}
								}
							}
						}
					}
					if (pos > 0) {
						buffer[pos] = (byte) (((iTotalLen - t) >> 8) & 0x0ff);
						buffer[pos + 1] = (byte) ((iTotalLen - t) & 0x0ff);
					}
				}
				buffer[0] = (byte) ((iTotalLen >> 8) & 0x0ff);
				buffer[1] = (byte) (iTotalLen & 0x0ff);

			}
			/***************** 如果TPU_RETCODE等于AAAAAAA,则按照mapfile打包 *********************/

			/***************** 如果TPU_RETCODE不为AAAAAAA,则按照ERR000打包 **********************/
			else {
				imapf = new String[] { "ERR000" };
				if (imapf instanceof String[]) {
					int row = ((String[]) imapf).length;
					if (row > 0) {
						iTotalLen = putDduString(buffer, ((String[]) imapf)[0], iTotalLen);
					}

				}

				List<String> lines3 = getLines(ERR000_PATH);
				for (String s : lines3) {
					if (s.indexOf("FLD:") >= 0) {
						if (split(s, SYSCFG_SEP, flds, 3) >= 3) {
							s = flds[0].substring(4);
							Object val = pool.get(s);
							if (val != null) {
								if (val instanceof String)
									iTotalLen = putDduString(buffer, (String) val, iTotalLen);
								else if (val instanceof byte[])
									iTotalLen = putDduStringMem(buffer, (byte[]) val, iTotalLen, ((byte[]) val).length);
							} else
								buffer[iTotalLen++] = 0;
						}
					}

				}
			}
			/***************** 如果TPU_RETCODE不为AAAAAAA,则按照ERR000打包 **********************/

		} catch (Exception err) {
			return CONVERT_ERROR_SEND_MAPFILE_READ;
		}
		return iTotalLen;
		/* try 内容：后』 */
	}

	public int convertPoolToSop(byte[] buffer, int len, String imapf) {
		return convertPoolToSop(buffer, len, new String[] { imapf });
	}

	int convertPoolToSop(byte[] buffer, int len, String[] imapf) {
		this.buffer = buffer;
		int res = convertPoolToPkt(buffer, imapf);
		if (res < 0) {
			errmsg = "请检查交易[" + code + "]的mapfile是否已更新(" + res + ")";
			errcode = ERR_ASSEMBPKT;
		}
		this.buffer = null;
		return res;
	}

	public int convertPoolToPkt(byte[] buffer, String[] imapf) {
		int iTotalLen = 0;
		try {
			List<String> lines = getLines(SYSTEM_HEAD_PATH);
			String flds[] = new String[5];
			int len = 0;
			for (String s : lines) {
				if (split(s, SYSCFG_SEP, flds, 3) >= 3) {
					if (flds[2].equals("I")) {
						len = SIZEOF_SHORT;
					} else {
						len = Integer.parseInt(flds[1]);
					}
					Object val = pool.get(flds[0]);
					byte[] bs = null;
					if (val != null) {
						if (val instanceof String) {
							if (flds[0].equals("SYSSEQNUM")) {
								Short tempval = Short.valueOf(val.toString());
								bs = convertShortToByte(tempval);
							} else if (flds[0].equals("SYSMYAOBBH")) {
								Long tempval = Long.valueOf(val.toString());
								bs = convertLongToByte(tempval);
							} else
								bs = ((String) val).getBytes();
						} else if (val instanceof byte[])
							bs = (byte[]) val;
					}

					if (bs != null) {
						System.arraycopy(bs, 0, buffer, iTotalLen, bs.length);
					}
					iTotalLen += len;
				}
			}
			List<String> lines2 = getLines(CMTRAN_HEAD_PATH);
			for (String s : lines2) {
				if (split(s, SYSCFG_SEP, flds, 3) >= 3) {
					if (flds[2].equals("I")) {
						len = SIZEOF_SHORT;
					} else {
						len = Integer.parseInt(flds[1]);
					}
					Object val = pool.get(flds[0]);
					byte[] bs = null;
					if (val != null) {
						if (val instanceof String) {
							bs = ((String) val).getBytes();
						} else if (val instanceof byte[])
							bs = (byte[]) val;
					}
					if (bs != null) {
						System.arraycopy(bs, 0, buffer, iTotalLen, bs.length);
					}
					iTotalLen += len;
				}
			}

			int cnt = imapf.length;
			for (int ii = 0; ii < cnt; ii++) {
				int pos = 0;
				int t = iTotalLen;
				List<String> lines3 = getLines(TRAN_HEAD_PATH); // NEW ADD
				for (String s : lines3) {
					if (split(s, SYSCFG_SEP, flds, 3) >= 3) {
						if (flds[2].equals("I")) {
							len = SIZEOF_SHORT;
						} else {
							len = Integer.parseInt(flds[1]);
						}
						Object val = pool.get(flds[0]);
						byte[] bs = null;
						if (val != null) {
							if (val instanceof String) {
								bs = ((String) val).getBytes();
							} else if (val instanceof byte[])
								bs = (byte[]) val;
							else if (val instanceof Integer)
								bs = this.convertIntegerToByte((Integer) val);
							else if (val instanceof Short)
								bs = this.convertShortToByte((Short) val);
							else if (val instanceof Long)
								bs = this.convertLongToByte((Long) val);
						}
						if (bs != null) {
							System.arraycopy(bs, 0, buffer, iTotalLen, bs.length);
						}
						iTotalLen += len;
					}
				}
				String imapfPath = MAPFILE_PATH + separator + imapf[ii];
				List<String> lines4 = getLines(imapfPath);
				if (lines4.isEmpty()) {
					return CONVERT_ERROR_SEND_MAPFILE_READ;
				}
				for (int f = 1; f < lines4.size(); f++) {
					String s = lines4.get(f);
					if (s.indexOf("FLD:") >= 0) {
						if (split(s, MAPFILE_SEP, flds, 1) >= 1) {
							s = flds[0].substring(4);
							Object val = pool.get(imapf[ii] + "." + s);
							if (val != null) {
								if (val instanceof String) {
									iTotalLen = putDduString(buffer, (String) val, iTotalLen);
								} else if (val instanceof Long) {
									iTotalLen = putDduLong(buffer, (Long) val, iTotalLen);
								} else if (val instanceof Short) {
									iTotalLen = putDduShort(buffer, (Short) val, iTotalLen);
								} else if (val instanceof byte[]) {
									iTotalLen = putDduStringMem(buffer, (byte[]) val, iTotalLen, ((byte[]) val).length);
								}
							} else {
								buffer[iTotalLen++] = 0;
							}
						}
					} else if (s.indexOf("GRD:") >= 0) {
						int j, i = 0;
						int col = 0, row = 0;
						int pos2 = 0;
						Object[] arr = new Object[100];

						try {
							String ss = s.substring(4);
							iTotalLen = putDduString(buffer, ss, iTotalLen);
							pos2 = iTotalLen;
							iTotalLen += 2;
							imapfPath = MAPFILE_PATH + separator + ss;
							List<String> lines5 = getLines(imapfPath);
							if (lines5.isEmpty()) {
								return CONVERT_ERROR_SEND_MAPFILE_READ;
							}
							ss = imapf[ii] + "." + ss;
							for (int k = 1; k < lines5.size(); k++) {
								String s1 = lines5.get(k);
								if (s1.indexOf("FLD:") >= 0) {
									if (split(s1, MAPFILE_SEP, flds, 1) >= 1) {
										Object val = pool.get(ss + "_" + flds[0].substring(4));
										if (val != null) {
											if (val instanceof byte[][]) {
												row = ((byte[][]) val).length;
												if (row > 0) {
													iTotalLen = putDduStringMem(buffer, ((byte[][]) val)[0], iTotalLen, ((byte[][]) val)[0].length);
												}
											} else if (val instanceof String[]) {
												row = ((String[]) val).length;
												if (row > 0) {
													iTotalLen = putDduString(buffer, ((String[]) val)[0], iTotalLen);
												}
											}
											arr[col] = val;
										} else {
											iTotalLen = putDduString(buffer, (String) val, iTotalLen);
											arr[col] = null;
										}
									}
								}
								col++;
							}
						} catch (Exception err) {
						}
						/*
						 * if (i != col) return CONVERT_ERROR_COP_MAPFILE_READ;
						 */
						if (row == 0) {
							buffer[pos2] = 0;
							buffer[pos2 + 1] = 0;
							iTotalLen = pos2 + 2;
						} else {
							buffer[pos2] = (byte) row;
							buffer[pos2 + 1] = (byte) col;
							for (j = 1; j < row; j++) {
								for (i = 0; i < col; i++) {
									if (arr[i] == null) {
										iTotalLen = putDduString(buffer, null, iTotalLen);
									} else {
										if (arr[i] instanceof byte[][]) {
											iTotalLen = putDduStringMem(buffer, ((byte[][]) arr[i])[j], iTotalLen, ((byte[][]) arr[i])[j].length);
										} else if (arr[i] instanceof String[]) {
											iTotalLen = putDduString(buffer, ((String[]) arr[i])[j], iTotalLen);
										}
									}
								}
							}
						}
					}
				}
				if (pos > 0) {
					buffer[pos] = (byte) (((iTotalLen - t) >> 8) & 0x0ff);
					buffer[pos + 1] = (byte) ((iTotalLen - t) & 0x0ff);
				}
			}
			byte[] tempbs = convertShortToByte((short) iTotalLen);
			buffer[0] = tempbs[0];// (byte) ((iTotalLen >> 8) & 0x0ff);
			buffer[1] = tempbs[1];// (byte) (iTotalLen & 0x0ff);

		} catch (Exception err) {
			return CONVERT_ERROR_SEND_MAPFILE_READ;
		}
		return iTotalLen;
	}

	public int convertPoolToPktForSecret(byte[] buffer) {
		int iTotalLen = 0;
		try {
			List<String> lines = getLines(SECRET_KEY_IN_PATH);
			String flds[] = new String[5];
			int len = 0;
			for (String s : lines) {
				if (split(s, SYSCFG_SEP, flds, 3) >= 3) {
					if (flds[2].equals("I")) {
						len = SIZEOF_SHORT;
					} else {
						len = Integer.parseInt(flds[1]);
					}
					Object val = pool.get(flds[0]);
					byte[] bs = null;
					if (val != null) {
						if (val instanceof String) {
							bs = ((String) val).getBytes();
						} else if (val instanceof byte[])
							bs = (byte[]) val;
					}
					if (bs != null) {
						System.arraycopy(bs, 0, buffer, iTotalLen, bs.length);
					}
					iTotalLen += len;
				}
			}
		} catch (Exception err) {
			return CONVERT_ERROR_SEND_MAPFILE_READ;
		}
		return iTotalLen;
	}

	public byte[] convertLongToByte(Long tempval) throws IOException {
		long val = tempval.longValue();
		byte[] b = new byte[8];
		for (int i = 7; i > 0; i--) {
			b[i] = (byte) val;
			val >>>= 8;
		}
		b[0] = (byte) val;
		return b;
	}

	public long convertBytesToLong(byte[] val) throws IOException {
		long l = 0;
		for (int i = 0; i < SIZEOF_LONG; i++) {
			l <<= 8;
			l ^= val[i] & 0xFF;
		}
		return l;
	}

	public static Integer convertBytesToInteger(byte[] val) throws IOException {
		int n = 0;
		for (int i = 0; i < SIZEOF_INT; i++) {
			n <<= 8;
			n ^= val[i] & 0xFF;
		}
		return n;
	}

	private Short convertBytesToShort(byte[] val) throws IOException {
		short n = 0;
		n ^= val[0] & 0xFF;
		n <<= 8;
		n ^= val[1] & 0xFF;
		return n;
	}

	private byte[] convertShortToByte(Short tempval) throws IOException {
		short val = tempval.shortValue();
		byte[] b = new byte[SIZEOF_SHORT];
		b[1] = (byte) val;
		val >>= 8;
		b[0] = (byte) val;
		return b;
	}

	private byte[] convertIntegerToByte(Integer tempval) throws IOException {
		int val = tempval.intValue();
		byte[] b = new byte[SIZEOF_INT];
		for (int i = 3; i > 0; i--) {
			b[i] = (byte) val;
			val >>>= 8;
		}
		b[0] = (byte) val;
		return b;
	}

	int skipObject(String obj, byte[] buffer, int iTotalLen) {
		try {
			String key = MAPFILE_PATH + separator + obj;
			List<String> lines = getLines(key);
			String style = "";
			if (lines.isEmpty()) {
				return CONVERT_ERROR_RECV_MAPFILE_READ;
			} else {
				style = lines.get(0);
			}
			boolean po = false;
			if (style.indexOf("PRINTER") >= 0) {
				po = true;
			}
			for (int k = 1; k < lines.size(); k++) {
				String s = lines.get(k);
				if (s.indexOf("FLD:") >= 0) {
					if (po) {
						iTotalLen += 3;
					}
					iTotalLen = calDduStringMem(buffer, iTotalLen);
				} else if (s.indexOf("GRD:") >= 0) {
					int j, i;
					int col = 0, row = 0;
					iTotalLen = calDduStringMem(buffer, iTotalLen);
					if ((row = buffer[iTotalLen]) < 0) {
						row = 256 + row;
					}
					if ((col = buffer[iTotalLen + 1]) < 0) {
						col = 256 + col;
					}
					iTotalLen += 2;
					if (row == 0) {
						continue;
					}

					if (po) {
						iTotalLen += 1 + col * 2;
					}

					s = s.substring(4);
					for (j = 0; j < row; j++) {
						for (i = 0; i < col; i++) {
							iTotalLen = calDduStringMem(buffer, iTotalLen);
						}
					}
				}
			}
		} catch (Exception err) {
			return CONVERT_ERROR_RECV_MAPFILE_READ;
		}
		return iTotalLen;
	}

	public boolean convertSopToPoolServer(byte[] buffer, int iLen, String strMap) {
		int res;
		pool.clear();
		if ((res = convertPktToPoolServer(buffer, iLen, strMap)) >= 0) {
			String s = getStr(null, TPU_RETCODE);
			if (DEBUG) {
				System.out.println("SopIntf[" + code + "]:" + TPU_RETCODE + ":" + s);
			}
			if (s.equals(SOP_SUCC)) {
				return true;
			}
			errcode = s;
			errmsg = getStr(null, PTCWXX);
			if (errmsg == null) {
				errmsg = "";
			}
			if (DEBUG) {
				System.out.println("SopIntf[" + code + "]:" + PTCWXX + ":" + errmsg);
			}
			if (errmsg.trim().equals("")) {
				errmsg = "(空)[" + s + "]";
			}
		} else {
			errmsg = "请检查交易[" + code + "]的mapfile是否已更新(" + res + ")";
			errcode = ERR_DISASSPKT;
		}
		return false;
	}

	/**
	 * 报文解包：将报文封装成对象,以获取相应的字段参数
	 * 
	 * @param buffer
	 *            //报文字节数组
	 * @param iLen
	 *            //报文长度
	 * @param strMap
	 *            //相应交易文件名称
	 * @return
	 */
	public int convertPktToPoolServer(byte[] buffer, int iLen, String strMap) {
		int iTotalLen = 0;
		try {
			/***************** 系统信息头——system_head.cfg **************************************/
			List<String> lines = getLines(SYSTEM_HEAD_PATH);
			String flds[] = new String[5];
			byte[] data = new byte[1024];
			int len;
			for (String s : lines) {
				if (split(s, SYSCFG_SEP, flds, 3) >= 3) {
					if (flds[2].equals("I")) {
						len = SIZEOF_SHORT; // short 长度
					} else {
						len = Integer.parseInt(flds[1]);
					}
					if (iLen - iTotalLen < len) {
						return CONVERT_ERROR_NO_ENOUGH_DATA;
					}

					byte[] ss = new byte[len];
					if (len > 0) {
						System.arraycopy(buffer, iTotalLen, ss, 0, len);

					}
					pool.put(flds[0], ss);
					iTotalLen += len;
				}
			}
			/***************** 系统信息头——system_head.cfg **************************************/

			/***************** 交易公共头——cmtran_head.cfg **************************************/
			List<String> lines2 = getLines(CMTRAN_HEAD_PATH);
			for (String s : lines2) {
				if (split(s, SYSCFG_SEP, flds, 3) >= 3) {
					if (flds[2].equals("I")) {
						len = SIZEOF_SHORT;
					} else {
						len = Integer.parseInt(flds[1]);
					}
					if (iLen - iTotalLen < len) {
						return CONVERT_ERROR_NO_ENOUGH_DATA;
					}
					byte[] ss = new byte[len];
					if (len > 0) {
						System.arraycopy(buffer, iTotalLen, ss, 0, len);
					}
					pool.put(flds[0], ss);
					iTotalLen += len;
				}
			}
			/***************** 交易公共头——cmtran_head.cfg **************************************/

			/***************** 交易数据头——tran_head.cfg **************************************/
			List<String> lines3 = getLines(TRAN_HEAD_PATH);
			for (String s : lines3) {
				if (split(s, SYSCFG_SEP, flds, 3) >= 3) {
					if (flds[2].equals("I")) {
						len = SIZEOF_SHORT;
					} else {
						len = Integer.parseInt(flds[1]);
					}
					if (iLen - iTotalLen < len) {
						return CONVERT_ERROR_NO_ENOUGH_DATA;
					}
					byte[] ss = new byte[len];
					if (len > 0) {
						System.arraycopy(buffer, iTotalLen, ss, 0, len);
					}
					pool.put(flds[0], ss);
					iTotalLen += len;
				}
			}
			/***************** 交易数据头——tran_head.cfg **************************************/
			String outmap = null;
			int cnt = 0;
			while (iTotalLen < iLen) {
				String sss = strMap;
				String style = "";
				String key = MAPFILE_PATH + separator + sss;
				List<String> lines4 = getLines(key);
				if (lines4.isEmpty()) {
					return CONVERT_ERROR_RECV_MAPFILE_READ;
				} else {
					style = lines4.get(0);
				}
				boolean po = false;
				if (style.indexOf("PRINTER") >= 0) {
					po = true;
				}
				for (int k = 1; k < lines4.size(); k++) {
					String s = lines4.get(k);
					// System.out.println("Hello" + kkk + s);
					if (s.indexOf("FLD:") >= 0) {
						if (split(s, MAPFILE_SEP, flds, 1) >= 1) {
							if (po) {
								byte[] attri = new byte[3];
								attri[0] = buffer[iTotalLen++];
								attri[1] = buffer[iTotalLen++];
								attri[2] = buffer[iTotalLen++];
								pool.put(sss + "." + flds[0].substring(4) + "_ATTRIB", attri);
							} else {
								iTotalLen = getDduStringMem(buffer, data, iTotalLen, 1024);
								byte[] ss = new byte[iVal];
								if (iVal > 0) {
									System.arraycopy(data, 0, ss, 0, iVal);
								}
								pool.put(sss + "." + flds[0].substring(4), ss);
							}
						}
					} else if (s.indexOf("GRD:") >= 0) {
						int j, i = 0;
						int col = 0, row = 0;
						iTotalLen = calDduStringMem(buffer, iTotalLen);
						if ((row = buffer[iTotalLen]) < 0) {
							row = 256 + row;
						}
						if ((col = buffer[iTotalLen + 1]) < 0) {
							col = 256 + col;
						}
						iTotalLen += 2;
						String ss = s.substring(4);
						pool.put(sss + "." + ss, String.valueOf(row));
						if (row == 0) {
							continue;
						}
						int pos = 0;
						if (po) {
							pos = iTotalLen;
							iTotalLen += 1 + 2 * col;
						}

						byte[][][] arr = new byte[col][row][];
						try {
							String key2 = MAPFILE_PATH + separator + ss;
							List<String> lines5 = getLines(key2);
							if (lines5.isEmpty()) {
								return CONVERT_ERROR_RECV_MAPFILE_READ;
							}
							ss = sss + "." + ss;
							for (int f = 1; f < lines5.size(); f++) {
								String s1 = lines5.get(f);
								if (s1.indexOf("FLD:") >= 0) {
									if (split(s1, MAPFILE_SEP, flds, 1) >= 1) {
										iTotalLen = getDduStringMem(buffer, data, iTotalLen, 1024);
										byte[] sx = new byte[iVal];
										if (iVal > 0) {
											System.arraycopy(data, 0, sx, 0, iVal);
										}
										arr[i][0] = sx;
										pool.put(ss + "_" + flds[0].substring(4), arr[i]);

										if (po) {
											byte[] attri = new byte[3];
											attri[0] = buffer[pos];
											attri[1] = buffer[pos + i * 2 + 1];
											attri[2] = buffer[pos + i * 2 + 2];
											System.arraycopy(buffer, iTotalLen, attri, 0, 3);
											pool.put(ss + "_" + flds[0].substring(4) + "_ATTRIB", attri);
										}
									}
								}
								i++;
							}
						} catch (Exception err) {
							err.printStackTrace();
						}
						if (i != col) {
							return CONVERT_ERROR_RECV_MAPFILE_READ;
						}
						for (j = 1; j < row; j++) {
							for (i = 0; i < col; i++) {
								iTotalLen = getDduStringMem(buffer, data, iTotalLen, 1024);
								byte[] sx = new byte[iVal];
								if (iVal > 0) {
									System.arraycopy(data, 0, sx, 0, iVal);
								}
								arr[i][j] = sx;
							}
						}
					}
				}
			}
			if (cnt > 0) {
				this.outmap = new String[cnt];
				split(outmap, ",", this.outmap, cnt);
			}
		} catch (Exception err) {
			return CONVERT_ERROR_RECV_MAPFILE_READ;
		}
		return 0;
	}

	public int convertPktToPoolServerForSecret(byte[] buffer, int iLen) {
		int iTotalLen = 0;
		try {
			/***************** secret_key_out.cfg **************************************/
			List<String> lines = getLines(SECRET_KEY_OUT_PATH);
			String flds[] = new String[5];
			int len = 0;
			for (String s : lines) {
				if (split(s, SYSCFG_SEP, flds, 3) >= 3) {
					if (flds[2].equals("I")) {
						len = 2;
					} else {
						len = Integer.parseInt(flds[1]);
					}
					if (iLen - iTotalLen < len) {
						return CONVERT_ERROR_NO_ENOUGH_DATA;
					}
					byte[] ss = new byte[len];
					if (len > 0) {
						System.arraycopy(buffer, iTotalLen, ss, 0, len);

					}
					pool.put(flds[0], ss);
					iTotalLen += len;
				}
			}
		} catch (Exception err) {
			return CONVERT_ERROR_RECV_MAPFILE_READ;
		}
		return 0;
	}

	public int convertPktToPoolServer(byte[] buffer, int iLen) {
		int iTotalLen = 0;
		try {
			/***************** 系统信息头——system_head.cfg **************************************/
			List<String> lines = getLines(SYSTEM_HEAD_PATH);
			String flds[] = new String[5];
			int len;

			for (String s : lines) {
				if (split(s, SYSCFG_SEP, flds, 3) >= 3) {
					if (flds[2].equals("I")) {
						len = 2;
					} else {
						len = Integer.parseInt(flds[1]);
					}
					if (iLen - iTotalLen < len) {
						return CONVERT_ERROR_NO_ENOUGH_DATA;
					}
					byte[] ss = new byte[len];
					if (len > 0) {
						System.arraycopy(buffer, iTotalLen, ss, 0, len);

					}

					pool.put(flds[0], ss);
					iTotalLen += len;
				}
			}
			/***************** 系统信息头——system_head.cfg **************************************/

			/***************** 交易公共头——cmtran_head.cfg **************************************/
			List<String> lines2 = getLines(CMTRAN_HEAD_PATH);
			for (String s : lines2) {
				if (split(s, SYSCFG_SEP, flds, 3) >= 3) {
					if (flds[2].equals("I")) {
						len = 2;
					} else {
						len = Integer.parseInt(flds[1]);
					}
					if (iLen - iTotalLen < len) {
						return CONVERT_ERROR_NO_ENOUGH_DATA;
					}
					byte[] ss = new byte[len];
					if (len > 0) {
						System.arraycopy(buffer, iTotalLen, ss, 0, len);
					}
					pool.put(flds[0], ss);
					iTotalLen += len;
				}
			}
			/***************** 交易公共头——cmtran_head.cfg **************************************/

			/***************** 交易数据头——tran_head.cfg **************************************/
			List<String> lines3 = getLines(TRAN_HEAD_PATH);
			for (String s : lines3) {
				if (split(s, SYSCFG_SEP, flds, 3) >= 3) {
					if (flds[2].equals("I")) {
						len = 2;
					} else {
						len = Integer.parseInt(flds[1]);
					}
					if (iLen - iTotalLen < len) {
						return CONVERT_ERROR_NO_ENOUGH_DATA;
					}
					byte[] ss = new byte[len];
					if (len > 0) {
						System.arraycopy(buffer, iTotalLen, ss, 0, len);
					}
					pool.put(flds[0], ss);
					iTotalLen += len;
				}
			}
			/***************** 交易数据头——tran_head.cfg **************************************/
		} catch (Exception err) {
			return CONVERT_ERROR_RECV_MAPFILE_READ;
		}
		return 0;
	}

	public boolean convertSopToPool(byte[] buffer, int iLen) {
		int res;
		pool.clear();
		if ((res = convertPktToPool(buffer, iLen)) >= 0) {
			String s = getStr(null, TPU_RETCODE);
			if (DEBUG) {
				System.out.println("SopIntf[" + code + "]:" + TPU_RETCODE + ":" + s);
			}
			if (s.equals(SOP_SUCC)) {
				return true;
			}

			errcode = s;
			errmsg = getStr(null, PTCWXX);
			if (errmsg == null) {
				errmsg = "";
			}
			if (DEBUG) {
				System.out.println("SopIntf[" + code + "]:" + PTCWXX + ":" + errmsg);
			}
			if (errmsg.trim().equals("")) {
				errmsg = "(空)[" + s + "]";
			}
		} else {
			errmsg = "请检查交易[" + code + "]的mapfile是否已更新(" + res + ")";
			errcode = ERR_DISASSPKT;
		}
		return false;
	}

	public int convertPktToPool(byte[] buffer, int iLen) {
		int iTotalLen = 0;
		try {
			List<String> lines = getLines(SYSTEM_HEAD_PATH);
			String flds[] = new String[5];
			byte[] data = new byte[1024];
			int len;
			for (String s : lines) {
				if (split(s, SYSCFG_SEP, flds, 3) >= 3) {
					if (flds[2].equals("I")) {
						len = SIZEOF_SHORT;
					}
					// else if(flds[2].equals("H")){
					// len=8;
					// }
					else {
						len = Integer.parseInt(flds[1]);
					}
					if (iLen - iTotalLen < len) {
						return CONVERT_ERROR_NO_ENOUGH_DATA;
					}

					byte[] ss = new byte[len];
					if (len > 0) {
						System.arraycopy(buffer, iTotalLen, ss, 0, len);
					}
					pool.put(flds[0], ss);
					// System.out.println("FLD=["+flds[0]+"];VALUE=["+ss+"]");
					iTotalLen += len;
				}
			}

			List<String> lines2 = getLines(CMTRAN_RCV_HEAD_PATH);
			for (String s : lines2) {
				if (split(s, SYSCFG_SEP, flds, 3) >= 3) {
					if (flds[2].equals("I")) {
						len = SIZEOF_SHORT;
					} else {
						len = Integer.parseInt(flds[1]);
					}
					// System.out.println("LEN=["+len+"]");
					if (iLen - iTotalLen < len) {
						return CONVERT_ERROR_NO_ENOUGH_DATA;
					}

					byte[] ss = new byte[len];
					if (len > 0) {
						System.arraycopy(buffer, iTotalLen, ss, 0, len);
					}
					pool.put(flds[0], ss);
					// System.out.println("111FLD=["+flds[0]+"];VALUE=["+ss+"]");
					iTotalLen += len;
				}
			}

			String s = getStr(null, TPU_RETCODE);
			// System.out.println("HELLO[TPU_RETCODE]"+aaa);
			if (s == null || !s.equals(SOP_SUCC)) {
				if (iTotalLen < iLen) {
					iTotalLen = getDduStringMem(buffer, data, iTotalLen, 1024);
					byte[] ss = new byte[iVal];
					if (iVal > 0) {
						System.arraycopy(data, 0, ss, 0, iVal);
					}
					pool.put("TPU_CTX1", ss);
				}
				if (iTotalLen < iLen) {
					iTotalLen = getDduStringMem(buffer, data, iTotalLen, 1024);
					byte[] ss = new byte[iVal];
					if (iVal > 0) {
						System.arraycopy(data, 0, ss, 0, iVal);
					}
					pool.put("TPU_CTX2", ss);
					if (iTotalLen >= iLen) {
						pool.put(TPU_RETCODE, ss);
					}
				}
				if (iTotalLen < iLen) {
					iTotalLen = getDduStringMem(buffer, data, iTotalLen, 1024);
					byte[] ss = new byte[iVal];
					if (iVal > 0) {
						System.arraycopy(data, 0, ss, 0, iVal);
					}
					pool.put(TPU_RETCODE, ss);
				}
				if (iTotalLen < iLen) {
					iTotalLen = getDduStringMem(buffer, data, iTotalLen, 1024);
					byte[] ss = new byte[iVal];
					if (iVal > 0) {
						System.arraycopy(data, 0, ss, 0, iVal);
					}
					pool.put(PTCWXX, ss);
				}
			} else {
				String outmap = null;
				int cnt = 0;
				// while (true) {
				while (iTotalLen < iLen) {
					iTotalLen = getDduStringMem(buffer, data, iTotalLen, 256);
					if (iVal == 0) {
						break;
					}

					String sss;
					try {
						sss = new String(data, 0, iVal, CharacterEncoding);
						// System.out.println(sss);
					} catch (Exception err) {
						sss = "";
					}
					if (cnt == 0) {
						outmap = sss;
					} else {
						outmap += "," + sss;
					}
					cnt++;

					System.out.println("sss:" + sss);

					String key = MAPFILE_PATH + separator + sss;
					List<String> lines3 = getLines(key);
					String style = "";
					if (lines3.isEmpty()) {
						return CONVERT_ERROR_RECV_MAPFILE_READ;
					} else {
						style = lines3.get(0);
					}
					boolean po = false;
					if (style.indexOf("PRINTER") >= 0) {
						po = true;
					}
					for (int k = 1; k < lines3.size(); k++) {
						String s1 = lines3.get(k);
						if (s1.indexOf("FLD:") >= 0) {
							if (split(s1, MAPFILE_SEP, flds, 1) >= 1) {
								if (po) {
									byte[] attri = new byte[3];
									attri[0] = buffer[iTotalLen++];
									attri[1] = buffer[iTotalLen++];
									attri[2] = buffer[iTotalLen++];
									pool.put(sss + "." + flds[0].substring(4) + "_ATTRIB", attri);
								}
								iTotalLen = getDduStringMem(buffer, data, iTotalLen, 1024);
								byte[] ss = new byte[iVal];
								if (iVal > 0) {
									System.arraycopy(data, 0, ss, 0, iVal);
								}
								pool.put(sss + "." + flds[0].substring(4), ss);
							}
						} else if (s1.indexOf("GRD:") >= 0) {
							int j, i = 0;
							int col = 0, row = 0;

							iTotalLen = calDduStringMem(buffer, iTotalLen);
							if ((row = buffer[iTotalLen]) < 0) {
								row = 256 + row;
							}
							if ((col = buffer[iTotalLen + 1]) < 0) {
								col = 256 + col;
							}
							iTotalLen += 2;
							String ss = s1.substring(4);
							pool.put(sss + "." + ss, String.valueOf(row));
							if (row == 0) {
								continue;
							}

							int pos = 0;
							if (po) {
								pos = iTotalLen;
								iTotalLen += 1 + 2 * col;
							}

							byte[][][] arr = new byte[col][row][];
							try {
								String key3 = MAPFILE_PATH + separator + sss;
								List<String> lines4 = getLines(key3);
								if (lines4.isEmpty()) {
									return CONVERT_ERROR_RECV_MAPFILE_READ;
								}
								ss = sss + "." + ss;
								for (int f = 1; f < lines4.size(); f++) {
									String s11 = lines4.get(f);
									if (s11.indexOf("FLD:") >= 0) {
										if (split(s11, MAPFILE_SEP, flds, 1) >= 1) {
											iTotalLen = getDduStringMem(buffer, data, iTotalLen, 1024);
											byte[] sx = new byte[iVal];
											if (iVal > 0) {
												System.arraycopy(data, 0, sx, 0, iVal);
											}
											arr[i][0] = sx;
											pool.put(ss + "_" + flds[0].substring(4), arr[i]);

											if (po) {
												byte[] attri = new byte[3];
												attri[0] = buffer[pos];
												attri[1] = buffer[pos + i * 2 + 1];
												attri[2] = buffer[pos + i * 2 + 2];
												System.arraycopy(buffer, iTotalLen, attri, 0, 3);
												pool.put(ss + "_" + flds[0].substring(4) + "_ATTRIB", attri);
											}
										}
									}
									i++;
								}
							} catch (Exception err) {
							}
							if (i != col) {
								return CONVERT_ERROR_RECV_MAPFILE_READ;
							}

							for (j = 1; j < row; j++) {
								for (i = 0; i < col; i++) {
									iTotalLen = getDduStringMem(buffer, data, iTotalLen, 1024);
									byte[] sx = new byte[iVal];
									if (iVal > 0) {
										System.arraycopy(data, 0, sx, 0, iVal);
									}
									arr[i][j] = sx;
								}
							}
						}
					}
				}
				if (cnt > 0) {
					this.outmap = new String[cnt];
					split(outmap, ",", this.outmap, cnt);
				}
			}
		} catch (Exception err) {
			return CONVERT_ERROR_RECV_MAPFILE_READ;
		}
		// System.out.println("**************keySet**************");
		// Iterator keyIterator = this.pool.keySet().iterator();
		// while (keyIterator.hasNext()) {
		// System.out.println(keyIterator.next());
		//
		// }
		return 0;
	}

	public SopIntf() {
		pool.put("PDWSNO", new byte[] { 'i', 'p' });
	}

	public void clear() {
		pool.clear();
	}

	public boolean exec(String inmap) {
		return exec(new String[] { inmap });
	}

	boolean exec(String[] inmap) {
		int res = 0;
		byte out[] = new byte[1024 * 32];
		if (buffer == null) {
			buffer = new byte[8192];
		}
		this.inmap = inmap;
		if ((res = convertPoolToPkt(buffer, this.inmap)) > 0) {
			res = sopCall(buffer, out, res, 1024 * 32);
			if (DEBUG) {
				System.out.println("SopIntf[" + code + "]:sopCall:" + res);
			}
		}
		if (res > 0) {
			if (flag != 0) {
				pool2 = pool;
				pool = new HashMap<Object, Object>(30);
			} else
				pool.clear();
			if ((res = convertPktToPool(out, res)) >= 0) {
				String s = getStr(null, TPU_RETCODE);
				if (DEBUG) {
					System.out.println("SopIntf[" + code + "]:" + TPU_RETCODE + ":" + s);
				}
				if (s.equals(SOP_SUCC)) {
					return true;
				}

				errcode = s;
				errmsg = getStr(null, PTCWXX);
				if (errmsg == null) {
					errmsg = "";
				}
				if (DEBUG) {
					System.out.println("SopIntf[" + code + "]:" + PTCWXX + ":" + errmsg);
				}
				if (errmsg.trim().equals("")) {
					errmsg = "(空)[" + s + "]";
				}
				return false;
			}
		}
		switch (res) {
		case 0:
			errmsg = "打包/拆包失败(0)";
			errcode = ERR_SYSERROR;
			break;
		case -2:
			errmsg = "IPP连接失败(" + res + ")";
			errcode = ERR_NOCONNECT;
			break;
		case -3:
			errmsg = "交易通讯失败，请查询上笔交易是否成功(" + res + ")";
			errcode = ERR_IOERROR;
			break;
		case -6:
			errmsg = "请检查交易[" + code + "]的mapfile是否已更新(" + res + ")";
			errcode = ERR_ASSEMBPKT;
		case -7:
		case -8:
			errmsg = "请检查交易[" + code + "]的mapfile是否已更新(" + res + ")";
			errcode = ERR_DISASSPKT;
			break;
		default:
			errmsg = "SOP调用失败(" + res + ")";
			errcode = ERR_SYSERROR;
		}
		return false;
	}

	public void fileTest() {
		FileReader fileReader = null;
		URL url = this.getClass().getResource("/" + "config" + "/" + "syscfg" + "/" + "system_head.cfg");

		try {
			File myFile = new File(url.getFile());
			fileReader = new FileReader(myFile);

			BufferedReader fileReaders = new BufferedReader(fileReader);
			while (fileReaders.readLine() != null) {
				System.out.println(fileReaders.readLine());
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fileReader != null)
					fileReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println("pass");
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Object key : pool.keySet()) {
			sb.append("key = " + key).append(" value = ").append(new String((byte[]) pool.get(key))).append("\n");
		}
		return sb.toString();
	}

	public static void main(String[] args) {
		/* 初始化sopapi */
		SopIntf sop = new SopIntf();
		// sop.initEnv("ww");
		// sop.DEBUG = true;
		/* 交易公共头：cmtran_head.cfg */
		// sop.put(null, "PDSBNO", "9950");
		// sop.put(null, "PDUSID", "99509000");
		/* 交易信息头：tran_head.cfg */
		// sop.put("F07701", "JIAOYM", "7701");
		// sop.put("F07701", "JIOYRQ", "20050704");
		// sop.put("F07701", "JIO1JE", "000000000001");
		// /* 交易数据 */
		// sop.put("F07701", "ZHHUYE", "yeyyyyyy");
		// sop.put("F07701", "ZHYODM", "zhzh");
		// sop.put("F07701", "PNGZHH", "111111");
		// System.out.println(sop);
		// System.out.println("");
		// System.out.println(sop.getStr("F07701", "JIO1JE"));

		// if (sop.exec("F07701")) {
		// System.out.print("transaction successful!!!");
		// } else {
		// System.out.print("transaction failed!!!");
		// System.out.println("Error: 4" + sop.errcode + "-" + sop.errmsg);
		// }
		byte[] b = new byte[] { 0x1, 0x1, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30,
				0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x31, 0x30, 0x38, 0x37, 0x30, 0x30, 0x30, 0x30, 0x63, 0x68, 0x75, 0x78, 0x75,
				0x75, 0x5f, 0x30, 0x32, 0x38, 0x31, 0x32, 0x30, 0x38, 0x38, 0x0, 0x0, 0x38, 0x37, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30,
				0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30,
				0x30, 0x78, 0x78, 0x0, 0x0, 0x0, 0x30, 0x30, 0x30, 0x30, 0x31, 0x30, 0x30, 0x31, 0x39, 0x30, 0x31, 0x33, 0x30, 0x31, 0x30, 0x30, 0x30, 0x37, 0x37, 0x31, 0x30,
				0x30, 0x0, 0x0, 0x7e, 0x0, 0x0, 0x0, 0x7e, 0x0, 0x0, 0x0, 0x0, 0x30, 0x30, 0x30, 0x38, 0x37, 0x34, 0x31, 0x36, 0x34, 0x36, 0x39, 0x39, 0x32, 0x30, 0x31, 0x33,
				0x30, 0x32, 0x32, 0x30, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x1,
				0x31, 0x0, 0x1, 0x31, 0x10, 0x39, 0x30, 0x30, 0x33, 0x30, 0x32, 0x31, 0x39, 0x30, 0x31, 0x36, 0x31, 0x39, 0x34, 0x36, 0x34, 0x0, 0x2, 0x30, 0x31, 0x1, 0x30, 0x0,
				0x0, 0x0, 0x0, 0x0, 0x8, 0x32, 0x30, 0x31, 0x33, 0x30, 0x35, 0x31, 0x32, 0x8, 0x32, 0x30, 0x31, 0x33, 0x30, 0x36, 0x32, 0x30, 0x0, 0x0, 0x1, 0x31, 0x2, 0x31, 0x30,
				0x1, 0x31, 0x4, 0x30, 0x30, 0x30, 0x32 };

		long start = System.currentTimeMillis();
		sop.convertPktToPool(b, b.length);
		System.out.println("耗时：" + (System.currentTimeMillis() - start));

		// start = System.currentTimeMillis();
		// re = sop.convertPktToPoolServer(b, b.length, "O07711");
		// System.out.println("耗时：" + (System.currentTimeMillis() - start));
		//
		// start = System.currentTimeMillis();
		// re = sop.convertPktToPoolServer(b, b.length, "O07711");
		// System.out.println("耗时：" + (System.currentTimeMillis() - start));
		//
		// start = System.currentTimeMillis();
		// re = sop.convertPktToPoolServer(b, b.length, "O07711");
		// System.out.println("耗时：" + (System.currentTimeMillis() - start));
		//
		// start = System.currentTimeMillis();
		// re = sop.convertPktToPoolServer(b, b.length, "O07711");
		// System.out.println("耗时：" + (System.currentTimeMillis() - start));
		System.out.println(sop);

	}

}
