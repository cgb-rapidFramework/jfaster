package com.abocode.jfaster.core.platform.utils;

import com.abocode.jfaster.core.common.util.ConvertUtils;
import com.abocode.jfaster.core.platform.view.FunctionView;
import com.abocode.jfaster.system.entity.Function;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FunctionSortUtils
{
    public  static void sortView(List<FunctionView> functionViews){
        Collections.sort(functionViews, new NumberComparator(true));
    }
    public  static void sort(List<Function> functions){
        Collections.sort(functions, new FunctionComparator());
    }
}

class FunctionComparator implements Comparator {

    private String fun= "fun";

    /**
     * 菜单排序比较器
     */
    public int compare(Object o1, Object o2) {
        Function c1 = (Function) o1;
        Function c2 = (Function) o2;
        if (c1.getFunctionOrder() != null && c2.getFunctionOrder() != null) {
            int c1order = ConvertUtils.getInt(c1.getFunctionOrder().substring(c1.getFunctionOrder().indexOf(fun) + 3));
            int c2order = ConvertUtils.getInt(c2.getFunctionOrder().substring(c2.getFunctionOrder().indexOf(fun)) + 3);
            if (c1order > c2order) {
                return 1;
            } else {
                return -1;
            }
        } else {
            return 1;
        }

    }
}

class NumberComparator implements Comparator<Object>, Serializable {
    private boolean ignoreCase;
    public NumberComparator(boolean ignoreCase) {
        this.ignoreCase = ignoreCase;
    }

    public int compare(Object obj1, Object obj2) {
        String o1 = "";
        String o2 = "";
        if (ignoreCase) {
            FunctionView c1 = (FunctionView) obj1;
            FunctionView c2 = (FunctionView) obj2;
            o1 = c1.getFunctionOrder();
            o2 = c2.getFunctionOrder();
        }
        if (o1 != null && o2 != null) {
            for (int i = 0; i < o1.length(); i++) {
                if (i == o1.length() && i < o2.length()) {
                    return -1;
                } else if (i == o2.length() && i < o1.length()) {
                    return 1;
                }
                char ch1 = o1.charAt(i);
                char ch2 = o2.charAt(i);
                if (ch1 >= '0' && ch2 <= '9') {
                    int i1 = getNumber(o1.substring(i));
                    int i2 = getNumber(o2.substring(i));
                    if (i1 == i2) {
                        continue;
                    } else {
                        return i1 - i2;
                    }
                } else if (ch1 != ch2) {
                    return ch1 - ch2;
                }
            }
        }
        return 0;
    }

    private int getNumber(String str) {
        int num = Integer.MAX_VALUE;
        int bits = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) >= '0' && str.charAt(i) <= '9') {
                bits++;
            } else {
                break;
            }
        }
        if (bits > 0) {
            num = Integer.parseInt(str.substring(0, bits));
        }
        return num;
    }


}
