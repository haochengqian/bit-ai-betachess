//package alogrithm;
//
//import java.util.Collections;
//import java.util.Random;
//
///**
// * Created by Chris on 2017/2/8.
// */
//public class EProgramming {
//    private class Group implements Comparable<Group>
//    {
//        public ANN ann = null;
//        public int Rate = 0;
//        public int WinTimes = 0;
//        public Group(ANN ann)
//        {
//            this.ann = ann;
//            Rate = 0;
//            WinTimes = 0;
//        }
//
//        @Override
//        public int compareTo(Group o) {
//            // 先按 WinTimes 排序
//            if (this.WinTimes != o.WinTimes) {
//                return (this.WinTimes - o.WinTimes);
//            }
//            // 按 Rate 排序
//            else {
//                return (this.Rate - o.Rate);
//            }
//        }
//    }
//    private int group_num = 50;                             // 种群大小
//    private Group[] group;                                  // 种群
//    private int times = 10;                                 // 世代数
//    private int etimes = 1;                                 // 评价时测试次数
//    private int q = 10;                                     // q竞争法 -->竞争q次
//    private IProgress<double> progress;
//
//    private EProgramming(int groupsize, int times, IProgress<double> progress)
//    {
//        this.progress = progress;
//        group_num = groupsize;
//        this.times = times;
//        group = new Group[group_num * 2];
//        for (int i = 0; i < group_num * 2; i++)
//            group[i] = new Group(new ANN());
//    }
//
//    private EProgramming(ANN[] anns, int times, IProgress<double> progress)
//    {
//        this.progress = progress;
//        group_num = anns.length;
//        this.times = times;
//        group = new Group[group_num * 2];
//        for (int i = 0; i < group_num; i++)
//            group[i] = new Group(anns[i]);
//        for (int i = 0; i < group_num; i++)
//            group[i + group_num] = new Group(new ANN());
//    }
//
//    public static double getNormalDistributionValue()
//    {
//        Random rand = new Random();
//        double u1, u2, v1 = 0, v2 = 0, s = 0, z1 = 0, z2 = 0;
//        while (s > 1 || s == 0)
//        {
//            u1 = rand.nextDouble();
//            u2 = rand.nextDouble();
//            v1 = 2 * u1 - 1;
//            v2 = 2 * u2 - 1;
//            s = v1 * v1 + v2 * v2;
//        }
//        z1 = Math.sqrt(-2 * Math.log(s) / s) * v1;
//        z2 = Math.sqrt(-2 * Math.log(s) / s) * v2;
//        return z1;
//    }
//
//    // 产生后代
//    private void Breed()
//    {
//        for (int i = 0; i < group_num; i++)
//        {
//            if (group[i].Rate == 0)
//            {
//                for (int j = 0; j < ANN.InNum; j++)
//                    for (int k = 0; k < ANN.HideNum; k++)
//                        group[group_num + i].ann.w[j][k] = group[i].ann.w[j][k] + getNormalDistributionValue();
//
//                for (int j = 0; j < ANN.HideNum; j++)
//                    group[group_num + i].ann.v[j] = group[i].ann.v[j] + getNormalDistributionValue();
//            }
//            else
//            {
//                for (int j = 0; j < ANN.InNum; j++)
//                    for (int k = 0; k < ANN.HideNum; k++)
//                        group[group_num + i].ann.w[j][k] = group[i].ann.w[j][k] + Math.sqrt(group[i].Rate) * getNormalDistributionValue(); // TODO: 数字大小不一致
//
//                for (int j = 0; j < ANN.HideNum; j++)
//                    group[group_num + i].ann.v[j] = group[i].ann.v[j] + Math.sqrt(group[i].Rate) * getNormalDistributionValue();
//            }
//        }
//    }
//
//    // 评价个体
//    private Task<int> EvaluateAsync()
//    {
//        TaskCompletionSource<int> tcs = new TaskCompletionSource<int>();
//        progress.Report(0);
//        completecount = 0;
//        // 每个个体
//        Task<int>[] tsks = (from item in @group select EvaluateSingleAsync(item)).ToArray();
//        Task.Factory.ContinueWhenAll(tsks, (tks) =>
//                {
//                        progress.Report(1);
//        tcs.SetResult(tks.Count());
//
//        });
//        return tcs.Task;
//    }
//    private int completecount = 0;
//    private object locker = new object();
//
//    private Task<int> EvaluateSingleAsync(Group item)
//    {
//        return Task.Factory.StartNew((arg) =>
//                {
//                        Group gpitem = arg as Group;
//        WinnerType winner;
//        int times = new Simmulate.Simmulator().Simmluate(new CommonBoard.Board(15, 15), gpitem.ANN, out winner);
//        if (winner == WinnerType.Black)
//            times = 400 - times;
//        gpitem.Rate = times;
//        lock (locker)
//        {
//            completecount++;
//            progress.Report(completecount / 1.0 / group_num / 2);
//        }
//        return times;
//        }, item);
//    }
//
//    // 采用Q竞争选择下一代群体
//    private void Select()
//    {
//        Random rand = new Random();
//        for (Group item: group)
//        {
//            item.WinTimes = 0;
//            for (int i = 0; i < q; i++)
//            {
//                int n = rand.nextInt(group_num * 2 - 1);
//                if (item.Rate >= group[n].Rate)
//                    item.WinTimes++;
//            }
//        }
//        // 排序
//        Collections.sort(group);
//        group = (from item in @group orderby item.WinTimes descending, item.Rate descending select new Group(item.ANN)).ToArray();
//    }
//
//
//    public async Task<ANN[]> RunAsync()
//    {
//        for (int i = 0; i < times; i++)
//        {
//            System.out.println("第"+ i + "代");
//            Breed();
//            await EvaluateAsync();
//            Select();
//        }
//        int index = 0;
//        int maxrate = group[0].Rate;
//        for (int i = 1; i < group.length; i++)
//            if (maxrate < group[i].Rate)
//            {
//                maxrate = group[i].Rate;
//                index = i;
//            }
//        return (from item in @group.Take(group_num) orderby item.Rate select item.ANN).ToArray();
//    }
//    public static ANN[] StaticRun(int groupsize, int times, IProgress<double> progress)
//    {
//        Task<ANN[]> task = new EProgramming(groupsize, times, progress).RunAsync();
//        task.Wait();
//        return task.Result;
//    }
//    public static ANN[] StaticRun(ANN[] anns, int times, IProgress<double> progress)
//    {
//        Task<ANN[]> task = new EProgramming(anns, times, progress).RunAsync();
//        task.Wait();
//        return task.Result;
//    }
//}
