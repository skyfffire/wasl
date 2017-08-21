package cn.optical_info.util;

import cn.optical_info.com.CommIO;
import cn.optical_info.session.UserSession;

public class StartCommIO {
    public static void start() {
        if (UserSession.getComm() != null) {
            try {
                new InitWithComm();
                
                // 设置串口IO到Session中
                UserSession.setCommIO(new CommIO());
                
                CommIO.SerialReader reader = 
                        UserSession.getCommIO().
                        new SerialReader(UserSession.getSerialPort().getInputStream());
                CommIO.SerialWriter writer = 
                        UserSession.getCommIO().
                        new SerialWriter(UserSession.getSerialPort().getOutputStream());
                for (int j = 1; j <= 3; j++) {
                    System.out.println((4 - j) + "秒后开启双向通讯:");
                    
                    Thread.sleep(1000);
                }
                
                System.out.println("开启双向通讯:");
                reader.start();
                writer.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
