package cn.optical_info.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import cn.skyfffire.other.SuperUUID;

/**
 * 该工具类用于生成 三：串口通讯工具的 ID
 * 
 * @author skyfffire@outlook.com
 */
public class IDTool {    
    /**
     * 获取这个工具的ID, 如果没有, 就生成一个
     * 
     * @return 新生成的或读取的工具ID
     */
    public static String getToolID() {
        String toolID = null;                                                      
        boolean isCreateFile = false;
        String filePath = "ID.conn";                                            // ID文件所在路径
        
        while (!isCreateFile) {
            try {
                InputStreamReader isr = 
                        new InputStreamReader(new FileInputStream(filePath));
                char[] bfr = new char[64];                                      // 临时寄存点
                
                isr.read(bfr);                                                  // 读取文件中的ID
                isr.close();
                toolID = new String(bfr);                                       // 将读取的内容放入ID
                
                // 如果没有ID，就重新生成一个
                if (!isSuccessID(bfr)) {
                    toolID = SuperUUID.getUUID() + SuperUUID.getUUID();         // 构造一个UUID
                    
                    // 向文件中写入构造的ID
                    OutputStreamWriter out = 
                            new OutputStreamWriter(
                                    new FileOutputStream(filePath));            // 程序打算将新生成的
                                                                                // UUID放到这个文件中
                    
                    out.write(toolID + "\n\n\n\n# Please do not "
                            + "change any content "
                            + "in this file. #"
                            + "");                                              // 存放ID以及提示信息
                    out.close();
                }
                
                isCreateFile = true;
            } catch (IOException e) {
                e.printStackTrace();
                
                // 没有文件就创建一个
                System.err.println("创建文件:");
                File newFile = new File(filePath);
                try {
                    newFile.createNewFile();
                } catch (Exception createException) {
                    createException.printStackTrace();
                }
                System.err.println("Com_Connection已自动创建文件");
                System.err.println("生成ID:");
                getToolID();
            }
        }
        
        return toolID;
    }
    
    /**
     * 用于判断是否生成了正确的ID
     * @param bfr   ID缓冲
     * @return      是否是正确的
     */
    public static boolean isSuccessID(char[] bfr) {
        for (char now : bfr) {
            if (now != 0) {
                return true;
            }
        }
        
        return false;
    }
}
