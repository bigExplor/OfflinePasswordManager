package com.example.accountmanager.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.MimeTypeMap;

import androidx.core.content.FileProvider;

import com.example.accountmanager.BuildConfig;
import com.example.accountmanager.base.BaseApplication;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class FileUtil {
    public static String getFilePath(Context context, String fileName) throws IOException {
        String filePath = context.getFilesDir() + File.separator + fileName;
        File file = new File(filePath);
        if (!file.exists()) {
            file.createNewFile();
        }
        return filePath;
    }

    public static boolean writeToFile(String path, String str, boolean append) throws IOException {
        File file = new File(path);
        if (!file.exists()) return false;
        FileWriter writer = new FileWriter(file, append);
        writer.append(str);
        writer.flush();
        writer.close();
        return true;
    }

    public static String share(Context context, String path) {
        String mimeType = "";
        if(TextUtils.isEmpty(path)) return "文件不存在";
        File file = new File(path);
        mimeType = getMimeType(file.getAbsolutePath());
        Log.d("shaetag", "share: " + mimeType);
        Uri uri = null;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            String authority = BuildConfig.APPLICATION_ID + ".fileprovider";
            uri = FileProvider.getUriForFile(context, authority, file);
        } else {
            uri = Uri.fromFile(file);
        }
        Intent intent = new Intent(Intent.ACTION_SEND);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }
        intent.setType(mimeType);
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        if (hasApplication(context, intent)) {
            context.startActivity(intent);
            return "文件已生成，选择分享方式";
        }
        return "获取分享方式失败";
    }

    // 调用系统功能获取文件的mimeType
    private static String getMimeType(String path) {
        String extension = MimeTypeMap.getFileExtensionFromUrl(path);
        String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        if (TextUtils.isEmpty(mimeType)) {
            return "*/*";
        }
        return mimeType;
    }

    // 判断是否有应用支持分享该类型的文件
    private static boolean hasApplication(Context context, Intent intent) {
        PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> activities = packageManager.queryIntentActivities(intent, 0);
        return activities.size() > 0;
    }
}
