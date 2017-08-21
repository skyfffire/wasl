package cn.optical_info.util;

/**
 * 该工具类用于判断服务端或本地的路灯是否已全部绑定
 * 
 * @author skyfffire@outlook.com
 */
public class Binding {
    /**
     * 判断本地的路灯是否全部被绑定
     * 
     * @return 是否被全部绑定
     */
    public static boolean isAllBindingInLocal(boolean[] notBindingInfo) {
        // 本地路灯全绑定验证
        for (int j = 0; j <= 3; j++) {            
            if (notBindingInfo[j]) {
                return false;
            }
        }
        
        return true;
    }
}
