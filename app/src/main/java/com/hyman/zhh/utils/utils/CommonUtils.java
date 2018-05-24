package com.hyman.zhh.utils.utils;

import android.graphics.Paint;
import android.text.TextUtils;
import android.widget.TextView;


/**
 * common utils
 * Created by zhanghao on 2016/11/29.
 */

public class CommonUtils {

    public static final String TAG = "CommonUtils";

    /**
     * 实现 android:ellipsize="end" 的效果(一行空间占满之后才会换行)<br/>
     * Example:  separateStr(textView,text,580,2,50);
     *
     * @param textView view
     * @param str      待显示的文字
     * @param maxWidth 最大宽度
     * @param maxLine  最大行数
     * @param fontSize 字体大小
     */
    public static void separateStr(TextView textView, String str, int maxWidth, int maxLine, int fontSize) {
        if (textView == null || maxWidth <= 0 || maxLine <= 0 || fontSize <= 0) {
            PrintLog.d(TAG, "text view is null or param is 0");
            return;
        }
        if (TextUtils.isEmpty(str)) {
            PrintLog.d(TAG, "str is empty");
            textView.setText("");
            return;
        }
        String endStr = "...";

        Paint paint = new Paint();
        paint.setTextSize(fontSize);

        float securityWidth = paint.measureText(endStr);
        float endLineWidth = paint.measureText("\n");
        PrintLog.d(TAG, "securityWidth = " + securityWidth + ", \n width = " + paint.measureText("\n"));

        maxWidth -= endLineWidth;   //减去换行\n所占宽度

        int currentLine = 1;    //第几行，从1开始
        float totalWidth = 0f;  //当前统计的总宽度,当换行之后重置为0

        int startIndex = 0;

        int size = str.length();

        StringBuilder stringBuilder = new StringBuilder();
        String line;

        for (int i = 0; i < size; i++) {
            totalWidth += paint.measureText(str, i, i + 1);
            //PrintLog.i(TAG, "i = " + i + ",totalWidth =" + totalWidth);
            if (totalWidth > maxWidth) {
                // 该换行了
                if (currentLine < maxLine) {
                    // 不是最后一行
                    line = str.substring(startIndex, i) + "\n"; //截取[startIndex，i）,不包含i
                    PrintLog.i(TAG, "line = " + line);
                    stringBuilder.append(line);
                    currentLine++;
                    startIndex = i;
                    i--;    //i下标位置，并没有记录，所以需要重新从i统计
                    totalWidth = 0;
                } else {
                    // 最后一行，特殊处理
                    int flag = i;
                    // 查找最后一行除了"..."之外其他可以显示的部分，当退出循环后，flag指向的位置，需要包含
                    while (totalWidth >= maxWidth - securityWidth) {
                        //PrintLog.i(TAG, "totalWidth =" + totalWidth);
                        totalWidth -= paint.measureText(str, flag, flag + 1);
                        flag--;
                    }
                    line = str.substring(startIndex, flag + 1) + endStr;
                    PrintLog.i(TAG, "line = " + line);
                    stringBuilder.append(line);
                    totalWidth = 0;
                    break;
                }

            }
        }

        if (totalWidth != 0) {
            stringBuilder.append(str.substring(startIndex));
        }

        PrintLog.i(TAG, "result = " + stringBuilder.toString());
        textView.setText(stringBuilder.toString());
    }

}
