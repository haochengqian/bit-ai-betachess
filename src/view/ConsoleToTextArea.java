package view;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.Document;
/**
 * Created by haochengqian on 2017/2/13.
 */
public class ConsoleToTextArea extends JTextArea{
    //转发所有从各个数组元素读取的数据到JTextArea
        public ConsoleToTextArea(InputStream[] inStreams) {
            for(int i = 0; i < inStreams.length; i++){
                startConsoleReaderThread(inStreams[i]);
            }
        }

        //捕获和显示所有写入到控制台流的数据
        public ConsoleToTextArea() throws IOException{
            final LoopedStreams loopedStreams=new LoopedStreams();

            //重定向System.out和System.err
            PrintStream printStream=new PrintStream(loopedStreams.getOutputStream());
            System.setOut(printStream);
            System.setErr(printStream);
            startConsoleReaderThread(loopedStreams.getInputStream());
        }

        private void startConsoleReaderThread(InputStream inStream){
            final BufferedReader bufferedReader=
                    new BufferedReader(new InputStreamReader(inStream));

            new Thread(new Runnable(){
                public void run() {
                    StringBuffer stringBuffer=new StringBuffer();
                    try {
                        String s;
                        Document document=getDocument();
                        while ((s=bufferedReader.readLine())!=null) {
                            boolean caretAtEnd=false;
                            caretAtEnd=getCaretPosition()==document.getLength()?true:false;
                            stringBuffer.setLength(0);
                            append(stringBuffer.append(s).append("\n").toString());
                            if (caretAtEnd) {
                                setCaretPosition(document.getLength());
                            }
                        }
                    } catch (IOException e) {
                        JOptionPane.showMessageDialog(null, "从bufferedReader中读取错误："+e.getMessage());
                        System.exit(1);
                    }
                }
            }).start();
        }

}
