package cn.optical_info.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;
import java.util.regex.Pattern;

import cn.optical_info.session.UserSession;

/**
 * 用于向指定手机发送短信的API
 * 
 * @author skyfffire@outlook.com
 */
public class SendMsg {
    private static String illegalString = null;
    
    /**
     * 发送短信
     * 
     * @param phone     目标手机
     * @param content   发送内容
     */
    public static void sendMsg(String phone, String content) {
        // 校验短信内容是否可发送
        if (checkIllegal(content)) {
            try {
                String utf8URL = "http://utf8.sms.webchinese.cn?Uid=Hummingbird"
                    + "&Key=5f4086e8f923140a3578"
                    + "&smsMob=" + phone
                    + "&smsText=" + content;
                URLConnection conn = new URL(utf8URL).openConnection();
                
                // 设置通用的请求属性
                conn.setRequestProperty("accept", "*/*");
                conn.setRequestProperty("connection", "Keep-Alive");
                conn.setRequestProperty("user-agent",
                        "Mozilla/4.0 (compatible; MSIE 6.0; "
                        + "Windows NT 5.1;SV1)");
                // 发送POST请求必须设置如下两行
                conn.setDoOutput(true);
                conn.setDoInput(true);
                // 发送请求
                PrintWriter out = new PrintWriter(conn.getOutputStream());
                out.print("");
                
                System.err.println("已发送短信通知");
                
                // 获取响应
                Scanner s = new Scanner(conn.getInputStream());
                
                System.out.println(s.nextLine());
                
                out.close();
                s.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            // 判断是否为图形界面模式
            if (UserSession.getMsgPanel() != null) {
                ShowMsg.showErrorMessage("存在相关屏蔽词'" 
                        + illegalString + "', 请查阅Illegal文件", 
                        "短信发送提示");
            } else {
                System.out.println("存在相关屏蔽词'" 
                        + illegalString + "', 请查阅Illegal文件");
            }
        }
    }
    
    /**
     * 校验短信内容中是否对于中国网建的屏蔽词库违规
     * 
     * @param content
     * @return 代表
     */
    public static boolean checkIllegal(String content) {
        String path = "Illegal";
        Scanner input = null;
        
        try {
            input = new Scanner(new FileInputStream(path));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 读取屏蔽词文件中的相关内容
        String kString = input.nextLine();
        input.close();
        
        // 切割出每一个屏蔽词，正则校验短信内容是否可行
        for (String now : kString.split("/")) {
            if (now.length() != 0) {                
                Pattern p = Pattern.compile(".*" + now + ".*");
                
                // 一旦匹配到相关屏蔽词，代表短信内容不可发送
                if (p.matcher(content).find()) {
                    illegalString = now;
                    
                    return false;
                }
            }
        }
        
        return true;
    }
}