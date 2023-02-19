package org.example.helpers;

import java.util.Arrays;
import java.util.Map;

public class DemoHelper {
    public static boolean demoExistValidation(
            Map<Integer, Map<String, Object>> originalMap,
            Integer id
    ) {
        Map<String, Object> existed = originalMap.get(id);
        if (existed != null) {
            return false;
        }
        return true;
    }

    public static boolean demoExistValidationV2(
            Map<Integer, Map<String, Object>> originalMap,
            Integer id
    ) {
        Integer[] ids = originalMap.keySet().toArray(new Integer[0]);
        // 下面的操作是对原有的数据进行排序
        Arrays.sort(ids);

        // 查找数据
        for (Integer i : ids) {
            if (i.equals(id)) {
                return true;
            }
        }

        return false;
    }

    public static void updateExistedUser(Map<String, Object> existedUser, Map<String, Object> newUser ){
        for(Map.Entry<String, Object> entry: existedUser.entrySet()){
            String key = entry.getKey();
            Object value = entry.getValue();
            if(newUser.containsKey(key)){
                Object newValue = newUser.get(key);
                existedUser.put(key, newValue);
            }
        }
    }
}
