package com.thinkgem.jeesite.modules.judge;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class Judge {

    private final static String COMPLIER_COMMAND_PRE = "clang  ";
    private final static String COMPLIER_COMMAND_MID = " -o ";
    private final static String COMPLIER_COMMAND_END = "a.o";
    private final static String SPACE = " ";
    private final static String COMPILER_TARGET_DIR = "/tmp";
    public final static String ERR_MSG = "err_msg";
    public final static String RESULT_TYPE = "result_type";
    public final static String ERR_SYS = "err_sys";
    public final static String SUCCESS_RESULT = "sucess_result";
    public final static String ERR_RESULT = "err_result";
    public final static String ERR_TYPE_COMPILE = "compile_err";
    public final static String ERR_TYPE_EXEC_CIR = "exec_err_cir";
    public final static String ERR_TYPE_EXEC = "exec_err_exec";
    public final static String ERR_TYPE_EXEC_RES = "exec_err_exec_res";


    public final static String COMPILE = "compile";
    public final static String TIME_OUT = "time_out";
    public final static String ANSWER = "answer";
    public final static String REMARKS = "remarks";

    private Map<String, Object> cases;
    //    private ConcurrentHashMap<String, String> results = new ConcurrentHashMap<String, String>();
    private Map<String, String> results = new HashMap<String, String>();
    private Object lock = new Object();
    private Timer timer = new Timer();
    private String target;

    public Judge(Map<String, Object> cases) {
        this.cases = cases;
        results.put(COMPILE, "1");
        results.put(TIME_OUT, "1");
        results.put(ANSWER, "1");
    }

    public Map<String, String> startJude() {


        String path = (String) cases.get("filePath");
//        results.put(RESULT_TYPE, ERR_SYS);
        if (compile(path)) {
            exec( target, (String[]) cases.get("inArgs"));
        }

        return results;
    }


    private boolean exec(String dirPath, String[] inArgs) {

        StringBuffer buffer = new StringBuffer().append(target);
        for (String str : inArgs) {
            buffer.append(SPACE).append(str);
        }
        try {
            Process process = Runtime.getRuntime().exec(buffer.toString());
            monitor(process);
            process.waitFor();
            if (process.exitValue() == 0) {
                results.put(TIME_OUT, "0");
                StringBuffer resStr = new StringBuffer();
                InputStreamReader ir1 = new InputStreamReader(process.getInputStream());
                LineNumberReader input1 = new LineNumberReader(ir1);
                String line;
                while ((line = input1.readLine()) != null) {
                    resStr.append(line);
                }
                String result = resStr.toString();
                result.replace(" ", "");
                if (result.equals((String) cases.get("outArgs"))) {
                    //TODO 编译运行正确
//                    results.put(RESULT_TYPE, SUCCESS_RESULT);
                    results.put(ANSWER, "0");


                } else {
                    // TODO 编译正确运行结果对比错误
//                    results.put(RESULT_TYPE, ERR_RESULT);
                }

            } else {

                //TODO 编译正确运行错误
                StringBuffer errStr = new StringBuffer();
                InputStreamReader ir1 = new InputStreamReader(process.getErrorStream());
                LineNumberReader input1 = new LineNumberReader(ir1);
                String line;
                while ((line = input1.readLine()) != null) {
                    errStr.append(line);
                }


//                results.put(RESULT_TYPE, ERR_TYPE_EXEC);
//                results.put(ERR_MSG, errStr.toString());

            }
            // if (inputStream.available() == 0) { // // 此处向数据库写入正确结果 // }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return true;
    }

    public void monitor(final Process process) {
        TimerTask task = new TimerTask() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                if (!process.isAlive()) {
                    return;
                }
                process.destroy();

                //TODO 死循环
//                results.put(RESULT_TYPE, ERR_TYPE_EXEC_CIR);

            }
        };

        timer.schedule(task, 2000);

    }

    private boolean compile(String path) {
        target = path.substring(0, path.lastIndexOf(".") - 1);

        int len = path.length();
        int tmp = path.lastIndexOf(".");


        StringBuffer buffer = new StringBuffer()
                .append(COMPLIER_COMMAND_PRE).append(path)
                .append(COMPLIER_COMMAND_MID)
                .append(target);
        try {
            final Process process = Runtime.getRuntime().exec(buffer.toString());
            process.waitFor();
            if (process.exitValue() == 0) {
                results.put(COMPILE, "0");
                return true;
            } else {
                StringBuffer errStr = new StringBuffer();
                InputStreamReader ir1 = new InputStreamReader(process.getErrorStream());
                LineNumberReader input1 = new LineNumberReader(ir1);
                String line;
                while ((line = input1.readLine()) != null) {
                    errStr.append(line);
                }
                //TODO 编译错误
                results.put(REMARKS, errStr.toString());
//                results.put(ERR_MSG, errStr.toString());
//                results.put(RESULT_TYPE, ERR_TYPE_COMPILE);

                return false;

            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InterruptedException e) {
            // TODO: handle exception
            e.printStackTrace();
        }

        results.put(COMPILE, "0");

        return true;
    }

    private void writeData(BufferedInputStream in) {
        // TODO Auto-generated method stub 向数据库写入结果

    }

    private boolean isExistPath(String path) {

        File file = new File(path);

        return file.exists();
    }


}
