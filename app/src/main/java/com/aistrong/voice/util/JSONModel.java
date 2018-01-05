package com.aistrong.voice.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;


public class JSONModel {
    //新版解析JOSN
    public String toJOSN(String msg){
        String jsonMsg = "";
        try {
            JSONObject jsonObject = JSONObject.parseObject(msg);
            jsonMsg = jsonObject.getString("info");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonMsg;
    }

    public String taskJOSN(String all_task) {
        return task1JOSN(all_task) + task2JOSN(all_task) + task3JOSN(all_task);
    }

    public String task1JOSN(String task) {
        String result1;
        String result = "";
        try {
            String taskType, mainDeptName, taskName, planEndDate, taskStatus;

            JSONObject jsonObject = JSON.parseObject(task);
            JSONArray jsonArray1 = jsonObject.getJSONArray("dataOne");

            for(int i = 0; i < jsonArray1.size(); i++){
                JSONObject jsonObjectArray = jsonArray1.getJSONObject(i);
                taskType = jsonObjectArray.getString("taskType");
                mainDeptName = jsonObjectArray.getString("mainDeptName");
                taskName = jsonObjectArray.getString("taskName");
                planEndDate = jsonObjectArray.getString("planEndDate");
                taskStatus = jsonObjectArray.getString("taskStatus");
                result1 = "一级任务" + "\n" + "任务名称：" + taskName + "\n"
                        + "任务类别：" + taskType + "\n"
                        + "牵头部门：" + mainDeptName + "\n"
                        + "完成日期：" + planEndDate + "\n"
                        + "任务状态：" + taskStatus + "\n"
                        + "------------------------------"+ "\n" ;
                result = result + result1;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;

    }

    public String task2JOSN(String task) {
        String result2;
        String result = "";
        try {
            String mainDeptName, taskName, planEndDate, taskStatus,
                    targetContent, milestoneContent;

            JSONObject jsonObject = JSON.parseObject(task);
            JSONArray jsonArray2 = jsonObject.getJSONArray("dataTwo");

            for(int i = 0; i < jsonArray2.size(); i++){
                JSONObject jsonObjectArray = jsonArray2.getJSONObject(i);
                targetContent = jsonObjectArray.getString("targetContent");
                mainDeptName = jsonObjectArray.getString("mainDeptName");
                taskName = jsonObjectArray.getString("taskName");
                planEndDate = jsonObjectArray.getString("planEndDate");
                taskStatus = jsonObjectArray.getString("taskStatus");
                milestoneContent = jsonObjectArray.getString("milestoneContent");
                result2 = "二级任务" + "\n" + "任务名称：" + taskName + "\n"
                        + "任务目标：" + targetContent + "\n"
                        + "里程碑：" + milestoneContent + "\n"
                        + "牵头部门：" + mainDeptName + "\n"
                        + "完成日期：" + planEndDate + "\n"
                        + "任务状态：" + taskStatus + "\n"
                        + "------------------------------"+ "\n" ;
                result = result + result2;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;

    }

    public String task3JOSN(String task) {
        String result3;
        String result = "";
        try {
            String mainDeptName, taskName, planEndDate, taskStatus,
                    workSource, leaderName,
                    masterName, taskRequire;

            JSONObject jsonObject = JSON.parseObject(task);
            JSONArray jsonArray3 = jsonObject.getJSONArray("dataThree");

            for(int i = 0; i < jsonArray3.size(); i++){
                JSONObject jsonObjectArray = jsonArray3.getJSONObject(i);
                mainDeptName = jsonObjectArray.getString("deptName");
                taskName = jsonObjectArray.getString("taskName");
                leaderName = jsonObjectArray.getString("leaderName");
                workSource = jsonObjectArray.getString("workSource");
                planEndDate = jsonObjectArray.getString("planEndDate");
                taskStatus = jsonObjectArray.getString("taskStatus");
                masterName = jsonObjectArray.getString("masterName");
                taskRequire = jsonObjectArray.getString("taskRequire");
                result3 = "三级任务" + "\n" + "任务名称：" + taskName + "\n"
                        + "任务来源：" + workSource + "\n"
                        + "牵头部门：" + mainDeptName + "\n"
                        + "牵头人：" + leaderName + "\n"
                        + "负责人：" + masterName + "\n"
                        + "任务要求：" + taskRequire+ "\n"
                        + "完成日期：" + planEndDate + "\n"
                        + "任务状态：" + taskStatus + "\n"
                        + "------------------------------"+ "\n" ;
                result = result + result3;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;

    }

}
