package dayoo.ClickStat;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import dayoo.ClickStat.config.Config;
import dayoo.ClickStat.config.ConfigManager;
import dayoo.ClickStat.util.CSUtil;
import dayoo.ClickStat.util.ConnectionManager;
import dayoo.ClickStat.util.SqlHandler;

/**
 * @author hwj
 *
 */
public class StatData extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Log log = LogFactory.getLog(StatData.class);
	private static HashMap sd = new HashMap();
	static{
		log.info("点击量系统正在初始化");
		Thread.currentThread().setName("StatData");
		try {
//			initData();
			initStat();
		} catch (Exception e) {
			log.error("",e);
		}
//		Thread genxml = new Thread(new GenXML());
//		genxml.setName("GenXML");
//		genxml.start();
//		log.info("启动存储点击量线程完成");
		log.info("点击量系统初始化结束");
	}
	
	/*
	private static void initData() throws IOException, DocumentException{
		File file = new File(ConfigManager.getDataFile());
		if(!file.exists()){
			log.info("无初始数据，从0开始");
			return;
		}
		SAXReader reader = new SAXReader();
		Document doc = reader.read(file);
		List eles = doc.getRootElement().elements();
		Element ele = null;		
		for(int i=0;i<eles.size();i++){
			ele = (Element)eles.get(i);
			sd.put(ele.element("id").getText(), ele.element("num").getText());
		}
		log.info("载入初始数据成功");
	}*/
	
	public synchronized static void addOne(String id,Connection conn){
//		String numstr = getNum(id);
//		int num = StringValueUtils.getInt(numstr);
//		num++;
//		sd.put(id, String.valueOf(num));
		CSUtil.updateNum(id,conn);
	}
	
	public static String getNum(String id,Connection conn){
//		String num = "0";
//		if(sd.containsKey(id)){
//			num = (String)sd.get(id);
//		}else{
//			num = CSUtil.getNumFromDB(id);
//		}
		String num = CSUtil.getNumFromDB(id,conn);
		return num;
	}

	public static void main(String[] args){
		while(true){
//		addOne("11111",null);
//		addOne("105",null);
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		}
	}
	
	public static HashMap getStatData(){
		return sd;
	}
	
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String ip = req.getRemoteHost();
		//id1,稿件的id
		String id1 = req.getParameter("id1");
		//id2,稿件对应的记录的id,即实际存储的id
		String id2 = req.getParameter("id2");
		String op = req.getParameter("op");
		String yjvg = req.getParameter("yjvg");
		
		if(!(validParam(id1)&&validParam(id2)&&validParam(op)&&validParam(yjvg)))
			return;
		int first = Integer.parseInt(yjvg.substring(0,1));
		int sedond = Integer.parseInt(yjvg.substring(1));
		if(first+sedond==Config.validNum){
			Connection conn = null;
			String res = "try{";
			try{
				try {
					conn = ConnectionManager.getInstance().getConnection();
				} catch (SQLException e) {
					log.error("",e);
					//SqlHandler.close(conn);
					//conn = SourcePoolManager.getConnection();
				}
				if(conn == null){
					log.error("取得连接错误，此次消息不处理。消息id1【"+id1+"】id2【"+id2+"】op【"+op+"】");
					return;
				}

				if(op.equals("a")){//add
					boolean addSucc = Config.addIPOneClick(ip, id2, System.currentTimeMillis());
					if(addSucc)
						StatData.addOne(id2,conn);
					res = res+"changeNum(\"a"+id1+"\",\""+StatData.getNum(id2,conn)+"\");changeNum(\"a "+id1+"\",\""+StatData.getNum(id2,conn)+"\");";
				}else if(op.equals("l")){//look up
					if(id1.indexOf(";")!=-1 && id2.indexOf(";")!=-1){
						String[] id1s = id1.split(";");
						String[] id2s = id2.split(";");
						for(int i=0;i<id1s.length;i++){
							String id2value=StatData.getNum(id2s[i],conn);
							res=res+"changeNum(\"a"+id1s[i]+"\",\""+id2value+"\");changeNum(\"a "+id1s[i]+"\",\""+id2value+"\");";
						}
					}
				}else{
					return;
				}
				
				
			}finally{
				SqlHandler.close(conn);
			}
			res+="}catch(e){}";
			
			PrintWriter pw = resp.getWriter();
			//文章内容里有，id会变。。变成 "a 333"..郁闷
			//res = "changeNum(\"a"+id1+"\",\""+StatData.getNum(id2)+"\");changeNum(\"a "+id1+"\",\""+StatData.getNum(id2)+"\");";
			pw.write(res);
			pw.flush();
			pw.close();
		}
	}
	
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}
	
	private boolean validParam(String p){
		if(p == null || p.length()==0)
			return false;
		else
			return true;
	}
	
	private static void initStat(){
		ConfigManager.initConfig();
//		SourcePoolManager.startPool();
	}
}
