package view; /**
 * Created by haochengqian on 2017/2/13.
 */
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

public class LoopedStreams {
    private PipedOutputStream pipedOS=new PipedOutputStream();
    private boolean keepRunning=true;
    private ByteArrayOutputStream byteArrayOS=new ByteArrayOutputStream(){
        public void close() {
            System.out.println("byteArrayOS.close()");
            keepRunning=false;
            try {
                super.close();
                pipedOS.close();
            } catch (IOException e) {
                System.out.println("关闭byteArrayOS错误："+e.getMessage());
                System.exit(1);
            }
        }
    };
    private PipedInputStream pipedIS=new PipedInputStream(){
        public void close() {
            System.out.println("pipedIS.close()");
            keepRunning=false;
            try {
                super.close();
            } catch (IOException e) {
                System.out.println("关闭pipedIS错误："+e.getMessage());
                System.exit(1);
            }
        }
    };

    public LoopedStreams() throws IOException{
        // TODO 自动生成构造函数存根
        pipedOS.connect(pipedIS);
        startByteArrayReaderThread();
    }

    public OutputStream getOutputStream() {
        return byteArrayOS;
    }

    public InputStream getInputStream() {
        return pipedIS;
    }

    public void startByteArrayReaderThread() {
        new Thread(new Runnable(){
            public void run() {
                while (keepRunning) {
                    if (byteArrayOS.size()>0) { //检查流里面的字节
                        byte buffer[]=null;
                        synchronized (byteArrayOS) {
                            buffer=byteArrayOS.toByteArray();
                            byteArrayOS.reset(); //清空缓冲区
                        }
                        try {
                            pipedOS.write(buffer, 0, buffer.length); //把提取的流发送到pipedOS
                        } catch (IOException e) {
                            System.out.println(e.getMessage());
                            System.exit(1);
                        }
                    }else {  //没有数据可用，线程进入休眠状态
                        try {
                            Thread.sleep(1000); //休眠1秒
                        } catch (InterruptedException e) {
                        }

                    }
                }
            }
        }).start();
    }
}
