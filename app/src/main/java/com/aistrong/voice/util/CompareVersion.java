package com.aistrong.voice.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class CompareVersion {

    //获取本地版本号
    public String getLocalVersion(Context context) {
        String versionName = "";
        try {
            // ---get the package info---
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
            if (versionName == null || versionName.length() <= 0) {
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionName;
    }

    //获取网络版本号
    public String getNetVersion() {
        URL url;
        int responseCode;
        HttpURLConnection urlConnection;
        BufferedReader reader;
        String netVersion = "";
        try {
            url = new URL("https://coding.net/u/Joshua-Zheng/p/Voice/git/raw/master/update");
            //打开URL
            urlConnection = (HttpURLConnection) url.openConnection();
            //获取服务器响应代码
            responseCode = urlConnection.getResponseCode();
            if (responseCode == 200) {
                //得到输入流，即获得了网页的内容
                int lineCount = 0;
                reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));
                while (reader.readLine() != null) {
                    lineCount++;
                    if (lineCount == 1) {
                        netVersion = reader.readLine();
                        break;
                    }
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return netVersion;
    }
}
