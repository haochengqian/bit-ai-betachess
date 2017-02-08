package alogrithm;

import java.util.Random;
/**
 * Created by Chris on 2017/2/8.
 */

/*
    神经网络
    input: value  b,s,x,m,j,p,z 分为位置、兵种 共11个输入，输出结果为红或黑的估值
    输入层11个节点，隐含层6个节点, 输出1个节点
*/

public class ANN {
    public static final int InNum = 11;      //输入层节点数
    public static final int HideNum = 6;     //隐含层节点数
    public static final int OutNum = 1;      //输出层节点数

    private double[] in_0;   //输入层输入数组
    private double[] in_1;   //隐含层输入数组
    private double in_2;     //输出层输入数组

    double[][] w;    //输入->隐含权值矩阵
    double[] v;     //隐含->输出权值矩阵

    private Random R = new Random();

    public ANN()
    {
        this.in_0 = new double[InNum];      //输入层11个节点
        this.in_1 = new double[HideNum];       //6个隐含层节点

        this.w = new double[InNum][HideNum];
        this.v = new double[HideNum];

        // 初始化权值矩阵
        // 每个权值均在[-1,1]
        for (int i = 0; i < InNum; i++)
            for (int j = 0; j < HideNum; j++)
                this.w[i][j] = R.nextDouble() * 2 - 1.0;

        for (int i = 0; i < HideNum; i++)
            this.v[i] = R.nextDouble() * 2 - 1.0;
    }

    /// 通过神经网络对棋局进行评估ß
    public double evaluate(int[] data)
    {
        //填充输入层神经元
        for (int i = 0; i < InNum; i++)
            in_0[i] = data[i];

        //计算隐含层值
        for (int j = 0; j < HideNum; j++)
        {
            in_1[j] = 0;
            for (int i = 0; i < InNum; i++)
                in_1[j] += w[i][j] * in_0[i];
        }


        //计算输出层值
        in_2 = 0;
        for (int j = 0; j < HideNum; j++)
            in_2 += v[j] * in_1[j];

        return in_2;
    }

    // 得到权值字符串
    public String getWeights()
    {
        StringBuilder stb = new StringBuilder();
        for (int i = 0; i < InNum; i++)
            for (int j = 0; j < HideNum; j++)
            {
                stb.append(w[i][j]);
                stb.append(" ");
            }
        stb.append('\n');
        for (int i = 0; i < HideNum; i++)
        {
            stb.append(v[i]);
            stb.append(' ');
        }
        return stb.toString();
    }
    public static ANN fromWeights(String str1, String str2)
    {
        int index = 0;
        ANN ann = new ANN();
        String[] strs1 = str1.split(" ");
        for (index = 0; index < strs1.length; index++)
            if (strs1[index] != "")
                ann.w[index / HideNum][index % HideNum] = Double.parseDouble(strs1[index]);

        String[] strs2 = str2.split(" ");
        for (index = 0; index < strs2.length; index++)
            if (strs2[index] != "")
                ann.v[index] = Double.parseDouble(strs2[index]);
        return ann;
    }
    public ANN copy()
    {
        ANN ann = new ANN();
        for (int i = 0; i < InNum; i++)
            for (int j = 0; j < HideNum; j++)
                ann.w[i][j] = w[i][j];

        for (int i = 0; i < HideNum; i++)
            ann.v[i] = v[i];
        return ann;
    }
}
