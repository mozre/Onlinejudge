package com.thinkgem.jeesite.modules.judge;

import java.io.*;
import java.util.*;

/**
 * Created by jbnlaptop on 2017/5/1.
 */
public class SimilarCheck {

    public final static int K = 7;//切片大小
    public final static int BaseHash = 3;//hash的基底，取质数3


    private List<Map<String, String>> list = new ArrayList<Map<String, String>>();

    public SimilarCheck(List<Map<String, String>> list) {
        this.list = list;
    }

    public List<Map<String, String>> startSimilar() {//单线程
        List<Map<String, String>> listReturn = new ArrayList<Map<String, String>>();
        for (Map<String, String> map : list) {
            listReturn.add(SimilarCheckMain(map));
        }
        return listReturn;
    }

    //TODO:K-gram切片
    public static String cutMain(String stringBeforeCut) {
        String stringAfterCut = ArrayToString(generateHashrolling(BaseHash, (generateKgram(stringBeforeCut, K)), K));
        return stringAfterCut;
    }

    //
    public static Map<String, String> SimilarCheckMain(Map<String, String> map) {
        String Code1 = "";
        String Code2 = "";
        Map<String, String> mapReturn = new HashMap<String, String>();
        mapReturn.put("uid1", map.get("uid1"));
        mapReturn.put("uid2", map.get("uid2"));
        mapReturn.put("similarRate", Double.toString(sim(similarMain(cutMain(removeSymbol(map.get("code1"))), cutMain(removeSymbol(map.get("code2"))))) * 100));
        //返回时存放数据的map
        return mapReturn;
    }

    //TODO:将ArrayList转换为String类型
    public static String ArrayToString(ArrayList line) {
        String stringResult = "";
        for (int i = 0; i < line.size(); i++) {
            stringResult += line.get(i);
        }
        return stringResult;
    }

    //TODO:将切片后的数据转换为Hash，基底为3
    public static ArrayList generateHashrolling(int Base, ArrayList kgram, int K) {//rolling
        ArrayList HashList = new ArrayList();
        int hash = 0;
        double q;
        String firstShingle = kgram.get(0).toString();
        for (int i = 0; i < 3; i++) {
            q = Math.pow(Base, (K - 1 - i));
            hash += Integer.valueOf(firstShingle.getBytes()[i]) * q;
        }//第一个框
        HashList.add(hash);

        String preshingle = "";//前置框
        String shingle = "";//当前框
        for (int i = 1; i < kgram.size(); i++) {
            preshingle = kgram.get(i - 1).toString();
            shingle = kgram.get(i).toString();
            q = Math.pow(Base, K);
            hash = (int) (hash * Base - Integer.valueOf(preshingle.getBytes()[0]) * q + Integer.valueOf(shingle.getBytes()[K - 1]));
            HashList.add(hash);
        }
        System.out.println(HashList);
        return HashList;
    }

    //TODO：切片模块主体
    public static ArrayList generateKgram(ArrayList line, int K) {//k为切片长度
        ArrayList kgram = new ArrayList();
        String q = "";
        for (int i = 0; i < line.size(); i++) {
            if (i + K > (line.size())) {
                break;
            }
            q = stringcut(line, i, i + K);
            kgram.add(q);
        }
        System.out.println(kgram);
        return kgram;
    }

    //TODO:切片,参数为字符串
    public static ArrayList generateKgram(String string, int K) {//k为切片长度
        ArrayList kgram = new ArrayList();
        String q = "";
        for (int i = 0; i < string.length(); i++) {
            if (i + K > (string.length())) {
                break;
            }
            q = string.substring(i, i + K);
            kgram.add(q);
        }
        System.out.println(kgram);
        return kgram;
    }

    //TODO:切片的具体实现
    public static String stringcut(ArrayList line, int start, int end) {
        String CutResult = "";
        for (int i = start; i < end; i++) {
            CutResult += line.get(start).toString();
            start++;
        }
        return CutResult;
    }

    //TODO:字符串放入MAP
    static Map<Character, int[]> similarMain(String string1, String string2) {
        Map<Character, int[]> vectorMap = new HashMap<Character, int[]>();
        int[] tempArray = null;
        for (Character character1 : string1.toCharArray()) {
            if (vectorMap.containsKey(character1)) {
                vectorMap.get(character1)[0]++;
            } else {
                tempArray = new int[2];
                tempArray[0] = 1;
                tempArray[1] = 0;
                vectorMap.put(character1, tempArray);
            }
        }
        for (Character character2 : string2.toCharArray()) {
            if (vectorMap.containsKey(character2)) {
                vectorMap.get(character2)[1]++;
            } else {
                tempArray = new int[2];
                tempArray[0] = 0;
                tempArray[1] = 1;
                vectorMap.put(character2, tempArray);
            }
        }
        return vectorMap;
    }

    //TODO:相似检测模块主体
    static double sim(Map<Character, int[]> vectorMap) {
        double result = 0;
        result = pointMulti(vectorMap) / sqrtMulti(vectorMap);
        return result;
    }

    //TODO:点乘（x1*y1+x2*y2）
    static double pointMulti(Map<Character, int[]> paramMap) {
        double result = 0;
        Set<Character> keySet = paramMap.keySet();
        for (Character character : keySet) {
            int temp[] = paramMap.get(character);
            result += (temp[0] * temp[1]);
        }
        return result;
    }

    //TODO:平方和开根
    static double sqrtMulti(Map<Character, int[]> paramMap) {
        double result = 0;
        result = squares(paramMap);
        result = Math.sqrt(result);
        return result;
    }

    //TODO：求平方和
    static double squares(Map<Character, int[]> paramMap) {
        double result1 = 0;
        double result2 = 0;
        Set<Character> keySet = paramMap.keySet();//键的集合
        for (Character character : keySet) {
            int temp[] = paramMap.get(character);
            result1 += (temp[0] * temp[0]);//平方1^2+
            result2 += (temp[1] * temp[1]);
        }
        return result1 * result2;
    }

    //TODO:list to map to String
    public static String L2M2S(List<Map<String, String>> list, String uid) {
        String resultString = "";
        resultString += list.get(0).get(uid);
        return resultString;
    }

    //TODO:去除多余符号
    public static String removeSymbol(String stringPre) {
        String stringAfter = "";
        stringAfter = stringPre.replace("\n", "").replace("\r", "").replace("\t", "");
        //去除格式符
        //stringAfter=stringAfter.replaceAll("char","c").replaceAll("int",'n').replaceAll()
        return removeCommentsWithQuoteAndDoubleEscape(stringAfter);
    }

    /**
     * 去除code注释
     * @param code
     * @return
     */
    public static String removeCommentsWithQuoteAndDoubleEscape(String code) {
        StringBuilder sb = new StringBuilder();
        int cnt = 0;
        boolean quoteFlag = false;
        for (int i = 0; i < code.length(); i++) {
            //如果没有开始双引号范围
            if (!quoteFlag) {
                //如果发现双引号开始
                if (code.charAt(i) == '\"') {
                    sb.append(code.charAt(i));
                    quoteFlag = true;
                    continue;
                }
                //处理双斜杠注释
                else if (i + 1 < code.length() && code.charAt(i) == '/' && code.charAt(i + 1) == '/') {
                    while (code.charAt(i) != '\n') {
                        i++;
                    }
                    continue;
                }
                //不在双引号范围内
                else {
                    //处理/**/注释段
                    if (cnt == 0) {
                        if (i + 1 < code.length() && code.charAt(i) == '/' && code.charAt(i + 1) == '*') {
                            cnt++;
                            i++;
                            continue;
                        }
                    } else {
                        //发现"*/"结尾
                        if (i + 1 < code.length() && code.charAt(i) == '*' && code.charAt(i + 1) == '/') {
                            cnt--;
                            i++;
                            continue;
                        }
                        //发现"/*"嵌套
                        if (i + 1 < code.length() && code.charAt(i) == '/' && code.charAt(i + 1) == '*') {
                            cnt++;
                            i++;
                            continue;
                        }
                    }
                    //如果没有发现/**/注释段或者已经处理完了嵌套的/**/注释段
                    if (cnt == 0) {
                        sb.append(code.charAt(i));
                        continue;
                    }
                }
            }
            //处理双引号注释段
            else {
                //如果发现双引号结束(非转义形式的双引号)
                if (code.charAt(i) == '\"' && code.charAt(i - 1) != '\\') {
                    sb.append(code.charAt(i));
                    quoteFlag = false;
                }
                //双引号开始了但是还没有结束
                else {
                    sb.append(code.charAt(i));
                }
            }
        }
        return sb.toString();
    }
}



