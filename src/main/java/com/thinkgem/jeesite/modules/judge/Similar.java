package com.thinkgem.jeesite.modules.judge;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by jbnlaptop on 2017/5/1.
 */
public class Similar {

    public final static int K = 3;//切片大小
    public final static int BaseHash=3;//hash的基底，取质数3
    private String fileName1;
    private String fileName2;

    public Similar(String fileName1, String fileName2) {
        this.fileName1 = fileName1;
        this.fileName2 = fileName2;
    }

    public int startSimilar(){
        ArrayList line=readFileByChars(fileName1);
        ArrayList line2=readFileByChars(fileName2);
        //按字符读取文件

        String string1=ArrayToString(generateHashrolling(BaseHash,(generateKgram(line,K)),K));
        String string2=ArrayToString(generateHashrolling(BaseHash,(generateKgram(line2,K)),K));
        //把读取的字符串按 k-gram 语法 通过base阶乘的方式 转换成hash

        return (int)(sim(similarMain(string1,string2))*100);
    }
    //TODO:按字符读取，并去掉头文件以及格式符
    public static ArrayList readFileByChars(String fileName){
        File file = new File(fileName);
        ArrayList line=new ArrayList();
        Reader reader = null;
        try {//先读入reader
            reader = new InputStreamReader(new FileInputStream(file));
            int tempchar;
            while ((tempchar = reader.read()) != -1) {
                // 对于windows下，\r\n这两个字符在一起时，表示一个换行。
                // 但如果这两个字符分开显示时，会换两次行。
                // 因此，屏蔽掉\r，或者屏蔽\n。否则，将会多出很多空行。
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {//读取reader
            char[] tempchars = new char[30];
            int charread = 0;
            reader = new InputStreamReader(new FileInputStream(fileName));
            // 读入多个字符到字符数组中，charread为一次读取字符数
            while ((charread = reader.read(tempchars)) != -1) {
                // 同样屏蔽掉\r不显示

                for (int i = 0; i < charread; i++) {
                    if (tempchars[i] == '\r'||tempchars[i]=='\n'||tempchars[i]=='\t') {
                        continue;
                    }else if(tempchars[i]=='#'){
                        while(tempchars[i]!='>')
                        {i++;}
                    } else {
                        line.add(tempchars[i]);
                    }
                }
            }

        } catch (Exception e1) {
            e1.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        return line;
    }

    //TODO:将ArrayList转换为String类型
    public static String ArrayToString(ArrayList line){
        String stringResult="";
        for (int i=0;i<line.size();i++) {
            stringResult+=line.get(i);
        }
        return stringResult;
    }

    //TODO:将切片后的数据转换为Hash，基底为3
    public static ArrayList generateHashrolling(int Base,ArrayList kgram,int K){//rolling
        ArrayList HashList=new ArrayList();
        int hash =0;
        double q;
        String firstShingle = kgram.get(0).toString();
        for (int i=0;i<3;i++){
            q=Math.pow(Base,(K-1-i));
            hash+=Integer.valueOf(firstShingle.getBytes()[i])*q;
        }//第一个框
        HashList.add(hash);

        String preshingle="";//前置框
        String shingle="";//当前框
        for (int i=1;i<kgram.size();i++){
            preshingle = kgram.get(i-1).toString();
            shingle=kgram.get(i).toString();
            q=Math.pow(Base,K);
            hash=(int)(hash*Base-Integer.valueOf(preshingle.getBytes()[0])*q+Integer.valueOf(shingle.getBytes()[K-1]));
            HashList.add(hash);
        }
        System.out.println(HashList);
        return HashList;
    }

    //TODO：切片模块主体
    public static ArrayList generateKgram(ArrayList line,int K){//k为切片长度
        ArrayList kgram =new ArrayList();
        String q="";
        for(int i=0;i<line.size();i++){
            if (i+K>(line.size())){
                break;
            }
            q=stringcut(line,i ,i+K);
            kgram.add(q);
        }
        System.out.println(kgram);
        return kgram;
    }

    //TODO:切片的具体实现
    public static String stringcut(ArrayList line,int start,int end)
    {
        String CutResult="";
        for (int i=start;i<end;i++)
        {
            CutResult+=line.get(start).toString();
            start++;
        }
        return CutResult;
    }

    //TODO:字符串放入MAP
    static Map<Character, int[]> similarMain(String string1, String string2/*,Map<Character, int[]> vectorMap*/)
    {
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
}
