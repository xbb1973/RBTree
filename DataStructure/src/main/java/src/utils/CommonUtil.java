package src.utils;

import src.RBTree.BST;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ：xbb
 * @date ：Created in 2020/5/11 1:30 下午
 * @description：通用Utils
 * @modifiedBy：
 * @version:
 */
public class CommonUtil {
    public static <T> List<T> castList(Object obj, Class<T> clazz)
    {
        List<T> result = new ArrayList<T>();
        if(obj instanceof List<?>)
        {
            for (Object o : (List<?>) obj)
            {
                result.add(clazz.cast(o));
            }
            return result;
        }
        return null;
    }

}
