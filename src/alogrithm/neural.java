package alogrithm;

import java.util.Random;
/**
 * Created by ch2en on 2017/2/6.
 */

/*
    神经网络
    input: value  b,s,x,m,j,p,z 分为位置、兵种 共11个输入，输出结果为红或黑的估值
    11个输入，隐含层5个 输出一个
*/
public class neural {
    public double[][] layer;
                public double[][][] layer_weight;
                private int length=3;
                public int[] layer_num={11,5,1};
                public neural(){
                    layer=new double [length][];
                    layer_weight=new double [length-1][][];
                    Random random=new Random();

                    if(true){ //之后应该改成算好了的话就直接从文件中读取权值
                        //随机初始化
                        for(int i=0;i<length;i++){
                            layer[i]=new double[layer_num[i]];
                            if(i+1<length){
                                layer_weight[i]=new double [layer_num[i]+1][layer_num[i+1]];
                                for(int j=0;j<layer_num[i]+1;j++){
                                    for(int k=0;k<layer_num[i+1];k++){
                                        layer_weight[i][j][k]=random.nextDouble()/1000000;
                                        //System.out.println(layer_weight[i][j][k]);
                                    }
                                }
                            }
            }
        }
        else{

        }
    }

    //计算输出
    public double computeOut(int[] data){
        for(int i=0;i<layer_num[0];i++){
            layer[0][i]=(double)data[i];
            System.out.printf("%f,",layer[0][i]);
        }
        System.out.printf("\n");
        for(int i=1;i<length;i++) {
            for (int j = 0; j < layer_num[i]; j++) {
                double z = layer_weight[i - 1][layer_num[i - 1]][j];
                for (int k = 0; k < layer_num[i - 1]; k++) {
                    z += layer_weight[i - 1][k][j] * layer[i - 1][k];
                }
                layer[i][j] = 1 / (1 + Math.exp(-z));
            }
        }
        return layer[2][0];

        //return (int)(layer[2][0]*1e16);
    }
}
