package algorithm;

import chess.Board;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by haochengqian on 2017/2/10.
 */
public class PostApi {

    private static Map mapw;

    public static String sendGet(String url, String param) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url + param;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            return "nobestmov";
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                System.out.println("CLOSE INPUT出现异常！" + e2);
                return "nobestmov";
            }
        }
//        System.out.println(result);
        return result;
    }

    public static int[] processInf(String result){
        mapw = new HashMap<Character,Integer>();

        mapw.put('a',0);mapw.put('b',1);mapw.put('c',2);mapw.put('d',3);mapw.put('e',4);
        mapw.put('f',5);mapw.put('g',6);mapw.put('h',7);mapw.put('i',8);
        int ss = 9;
        for(char i='0';i<='9';i++,ss--)
            mapw.put(i,ss);

        String temp = result.substring(0,9);

        String[] word = new String[3];
        word = temp.split(":");
        if(word[0].equals("nobestmov")||word[0].equals("invalid b")) {
            int[] pos = new int[4];
            pos[0] = Integer.MAX_VALUE;
            return pos;
        }
        else{
            try{
                int length = word[1].length();
            }catch (Exception e2) {
                System.out.println("word[1]为空出现异常！" + e2);
                int[] pos = new int[4];
                pos[0] = Integer.MAX_VALUE;
                return pos;
            }
            int[] pos = new int[4];
            Integer s;
            if(word[1].length()!=4){
                System.out.println("word[1]异常！");
                pos = new int[4];
                pos[0] = Integer.MAX_VALUE;
                return pos;
            }

            for (int i = 0; i < 4; i++) {
                s = (Integer) mapw.get(word[1].charAt(i));
                try{
                    pos[i] = s.intValue();
                }catch (Exception e2) {
                    System.out.println("word[1]为空出现异常！" + e2);
                    pos = new int[4];
                    pos[0] = Integer.MAX_VALUE;
                    return pos;
                }
                pos[i] = s.intValue();
            }
            return pos;
        }
    }

}

