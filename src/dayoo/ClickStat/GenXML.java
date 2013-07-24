package dayoo.ClickStat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import dayoo.ClickStat.config.Config;
import dayoo.ClickStat.config.ConfigManager;

public class GenXML implements Runnable {

	Log log = LogFactory.getLog(GenXML.class);
	private static Object obj = new Object();
	private File datafile = new File(ConfigManager.getDataFile());
	public void run() {
		FileOutputStream fos = null;
		OutputStreamWriter osw = null;
		while(true){
			//写xml文件
			try {
				Thread.sleep(Config.interval*1000);
				HashMap sd = StatData.getStatData();
				Document doc = DocumentHelper.createDocument();
				Element root = doc.addElement("click-stat");
				synchronized(obj){
					Iterator it = sd.entrySet().iterator();
					while(it.hasNext()){
						Map.Entry entry = (Map.Entry)it.next();
						String id = (String)entry.getKey();
						String num = (String)entry.getValue();
						Element ele = root.addElement("stat");
						Element idele = ele.addElement("id");
						idele.setText(id);
						Element numele = ele.addElement("num");
						numele.setText(num);
					}
				}
				if(!datafile.exists())
					datafile.createNewFile();
				else
					datafile.delete();
				fos = new FileOutputStream(datafile);
				osw = new OutputStreamWriter(fos);
				doc.write(osw);
				log.info("写入数据文件成功"+new Date(System.currentTimeMillis()));
				osw.close();
				fos.close();
//				Thread.sleep(3000);
			} catch (InterruptedException e) {
				log.fatal("写xml线程出现异常  ",e);
			} catch (FileNotFoundException e) {
				log.error("输出文件未找到"+ConfigManager.getDataFile(),e);
			} catch (IOException e) {
				log.error("输出文件写入失败，请检查是否有其它程序独占",e);
			} finally{
				try{
					osw.close();
					fos.close();
				}catch (Exception e) {
				}
			}
			
			
			//检测是否需要更新配置
			try{
				File cshome = new File(Config.getCSHome());
				File[] files = cshome.listFiles();
				for(int i=0;i<files.length;i++){
					File file = files[i];
					if("updateConfig".equalsIgnoreCase(file.getName())){
						ConfigManager.updateConfig();
						file.delete();
						break;
					}
				}
			}catch (Exception e) {
				log.error("更新配置文件失败");
			}
			
			//清理内存IP信息，查是否有last超过太久的
		}
	}
	
	public static void main(String[] args) {
		new GenXML().run();
	}

}
