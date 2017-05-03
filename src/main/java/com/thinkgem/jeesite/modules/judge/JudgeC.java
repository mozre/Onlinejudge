package com.thinkgem.jeesite.modules.judge;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mozre on 17-4-30.
 */
public class JudgeC {


    // not any meaning
    private final static int ERR_UNABLE_TOUCH_NUM = 1;
    // fork fail
    private final static int ERR_SYSTEM_NUM = 2;
    // exec cmdline fail
    private final static int ERR_EXCMDLINE_NUM = 3;
    // out memory of array
    private final static int ERR_OUT_MEMORY_OF_ARRAY = 4;
    // compile erro
    private final static int ERR_COMPILE = 5;
    // code round trips
    private final static int ERR_EXEC_CIR = 7;
    // get a mistake result
    private final static int ERR_RESULT = 8;
    // success exec child process work
    private final static int SUCC_COMPLILE_NUM = 11;
    // success exec
    private final static int SUCC_EXEC_NUM = 10;
    // compile / exec -> rigth result
    private final static int SUCC_COMPILE_AND_EXEC_CHECKOUT = 9;


    public final static String COMPILE = "compile";
    public final static String TIME_OUT = "time_out";
    public final static String ANSWER = "answer";
    public final static String REMARKS = "remarks";


    private Map<String, Object> cases;
    private Map<String, String> results = new HashMap<String, String>();

    static {

        //TODO  绝对路径：改成你的工作目录 + /onlinejudge/libs/libjudge.so
//        System.load("/home/mozre/MS/CMake/libtest.so");
        System.load("/home/njzhenghao/WorkSpace/onlinejudge/libs/libjudge.so");
    }


    public JudgeC() {

    }

    public JudgeC(Map<String, Object> cases) {
        this.cases = cases;
        results.put(COMPILE, "1");
        results.put(TIME_OUT, "1");
        results.put(ANSWER, "1");
    }

    public native int startJudge(String path, String[] args, String result);


    public Map<String, String> startJudgeC() {


        System.load("/home/njzhenghao/WorkSpace/onlinejudge/libs/libjudge.so");
        String path = (String) cases.get("filePath");
        String[] args = (String[]) cases.get("inArgs");
        String result = (String) cases.get("outArgs");
        int rtn = startJudge(path, args, result);
        switch (rtn) {

            case ERR_COMPILE:

                String target = path.substring(0, path.lastIndexOf(".")) + ".txt";
                File file = new File(target);
                if (!file.exists()) {
                    results.put(REMARKS, "System error!");
                    break;
                }
                StringBuffer stringBuffer = new StringBuffer();

                try {
                    char[] temp = new char[1024];
                    FileInputStream fileInputStream = new FileInputStream(file);
                    InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
                    while (inputStreamReader.read(temp) != -1) {
                        stringBuffer.append(new String(temp));
                        temp = new char[1024];
                    }
                    fileInputStream.close();
                    inputStreamReader.close();

                    results.put(REMARKS, stringBuffer.toString());

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    file.delete();
                    File file1 = new File(path.substring(0, path.lastIndexOf(".")));
                    if (file1.exists()) {
                        file.delete();
                    }

                }

                break;

            case ERR_EXEC_CIR:
                results.put(COMPILE, "0");
                results.put(TIME_OUT, "1");
                break;

            case ERR_RESULT:

                results.put(COMPILE, "0");
                results.put(TIME_OUT, "0");
                break;

            case SUCC_COMPILE_AND_EXEC_CHECKOUT:
                results.put(COMPILE, "0");
                results.put(TIME_OUT, "0");
                results.put(ANSWER, "0");

                break;


        }

        return results;
    }

//    public static void main(String[] args) {
//
//        JudgeC j = new JudgeC();
//        String[] tmp = {
//                "a", "b", "c"
//        };
//        int result = j.startJudge("/home/mozre/MS/CMake/tt.c", tmp, "abc");
//
//        System.out.println("result = " + result);
//    }


}
