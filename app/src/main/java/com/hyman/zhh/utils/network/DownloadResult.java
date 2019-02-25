package com.hyman.zhh.utils.network;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class DownloadResult {

    public static final int REASON_REQUEST_FAILED = 100;
    public static final int REASON_BODY_IS_NULL = 101;
    public static final int REASON_BODY_LENGTH_IS_ZERO = 102;
    public static final int REASON_SAVE_FILE_FAILED = 103;

    private boolean success;

    private float downloadSpeed;

    private int reasonCode;

    private DownloadResult(boolean success, float downloadSpeed, int reasonCode) {
        this.success = success;
        this.downloadSpeed = downloadSpeed;
        this.reasonCode = reasonCode;
    }

    public boolean isSuccess() {
        return success;
    }

    public float getDownloadSpeed() {
        return downloadSpeed;
    }

    public int getReasonCode() {
        return reasonCode;
    }

    @Override
    public String toString() {
        return "DownloadResult{" +
                "success=" + success +
                ", downloadSpeed=" + downloadSpeed +
                ", reasonCode=" + reasonCode +
                '}';
    }

    public static DownloadResult getSuccessResult(float downloadSpeed) {
        return new DownloadResult(true, downloadSpeed, 0);
    }

    public static DownloadResult getFailureResult(int reasonCode) {
        return new DownloadResult(false, 0, reasonCode);
    }

    private static final String EMPTY_RESULT = "|||||||";
    private static final String SPLIT = "|";
    private static final String SPLIT_FIELD = "*";

    public static String getStatisticsResult(List<DownloadResult> downloadResults) {
        // 格式：|测试总次数|成功次数|下载速度1*下载速度2...|下载平均速度|失败次数|失败原因1*失败原因2...|
        if (downloadResults == null || downloadResults.size() <= 0) {
            return EMPTY_RESULT;
        }

        ArrayList<DownloadResult> successList = new ArrayList<>();
        ArrayList<DownloadResult> failureList = new ArrayList<>();
        for (DownloadResult result : downloadResults) {
            if (result.isSuccess()) {
                successList.add(result);
            } else {
                failureList.add(result);
            }
        }

        StringBuilder builder = new StringBuilder(SPLIT);
        builder.append(downloadResults.size());
        builder.append(SPLIT);

        addSuccessStr(successList, builder);
        addFailureStr(failureList, builder);
        return builder.toString();
    }

    private static void addSuccessStr(ArrayList<DownloadResult> successList, StringBuilder builder) {
        // 成功次数|下载速度1*下载速度2...|下载平均速度|
        int successSize = successList.size();
        builder.append(successSize);
        builder.append(SPLIT);

        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        if (successSize > 1) {
            float totalSpeed = 0f;
            for (int i = 0; i < successSize; i++) {
                float downloadSpeed = successList.get(i).getDownloadSpeed();
                totalSpeed += downloadSpeed;
                if (i != 0) {
                    builder.append(SPLIT_FIELD);
                }
                builder.append(downloadSpeed);
            }
            builder.append(SPLIT);
            builder.append(decimalFormat.format(totalSpeed / successSize));
            builder.append(SPLIT);
        } else if (successSize == 1) {
            float downloadSpeed = successList.get(0).getDownloadSpeed();
            builder.append(downloadSpeed);
            builder.append(SPLIT);
            builder.append(decimalFormat.format(downloadSpeed));
            builder.append(SPLIT);
        } else {
            builder.append(SPLIT);
            builder.append(SPLIT);
        }
    }

    private static void addFailureStr(ArrayList<DownloadResult> failureList, StringBuilder builder) {
        // 失败次数|失败原因1*失败原因2...|
        int failureSize = failureList.size();
        builder.append(failureSize);
        builder.append(SPLIT);
        if (failureSize > 1) {
            for (int i = 0; i < failureSize; i++) {
                if (i != 0) {
                    builder.append(SPLIT_FIELD);
                }
                builder.append(failureList.get(i).getReasonCode());
            }
            builder.append(SPLIT);
        } else if (failureSize == 1) {
            builder.append(failureList.get(0).getReasonCode());
            builder.append(SPLIT);
        } else {
            builder.append(SPLIT);
        }
    }
}
