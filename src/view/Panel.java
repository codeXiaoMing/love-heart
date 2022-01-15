package view;

import javax.swing.*;
import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.*;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

public class Panel extends JPanel implements Runnable {

    int R;     //三原色red
    int G;        //三原色green
    int B;        //三原色blue
    int tx;     //画图坐标
    int ty;    //画图坐标
    float y;   //循环画图行数
    boolean flag;      //画边框爱心还是实体爱心
    boolean increaseOrDecrease = false;        //G、B增大或减小
    boolean backRed = false;                    //从黑色变回红色
    File file = new File("music/My Lonely Soul.wav");    //背景音乐
    URL url = null;
    URI uri = null;
    AudioClip clip = null;

    public Panel() {
        try {
            uri = file.toURI();
            url = uri.toURL();
        } catch (MalformedURLException e1) {
            System.out.println(e1);
        }
        clip = Applet.newAudioClip(url);
        clip.loop();    //播放背景音乐
        R = 255;       //初始三原色为红色
        G = 0;
        B = 0;
        y = 1.5f;      //初始循环位置
        tx = 30;       //每一行画图的位置
        ty = 10;       //初始画图的列的位置
        flag = false;  //最开始画边框爱心
        setVisible(true);
    }

    public void paintComponent(Graphics g) {
        if (!flag) {     //画边框
            Color color = new Color(R, G, B);    //根据当前的RGB画相应颜色的图形
            g.setColor(color);
            g.fillRect(tx, ty, 13, 13);
            g.fillRect(tx, ty + 11, 13, 10);  //多往下画一点减小每行的间隔
        } else {          //画实体爱心
            super.paintComponent(g);        //将之前所有的边框先清空
            Color color = new Color(206, 40, 34);    //最终的颜色
            g.setColor(color);
            for (float i = 1.5f; i >= -1.5f; i -= 0.1f) {
                for (float x = -1.5f; x <= 1.5f; x += 0.05f) {
                    float a = x * x + i * i - 1;
                    if (a * a * a - x * x * i * i * i <= 0.0f) {
                        g.fillRect(tx, ty, 13, 13);
                        g.fillRect(tx, ty + 11, 13, 16);
                    }
                    tx += 13;
                }
                tx = 30;
                ty += 25;
            }
        }
    }

    public void run() {
        while (true) {
            try {
                Thread.sleep(70);
            } catch (Exception e) {
            }
            if (y >= -1.2f && !flag) {     //画边框爱心
                //根据公式(x^2+y^2-1)^3-x^2y^3=0画
                for (float x = -1.5f; x <= 1.5f; x += 0.05f) {
                    float a = x * x + y * y - 1;
                    if (a * a * a - x * x * y * y * y > 0.0f && y != 1.5f) {   //大于0是爱心外侧，小于0是内侧
                        this.repaint();
                        try {            //要把线程休眠一会才会轮到repaint()的线程
                            Thread.sleep(4);  //可自定义事件，事件越小画的速度越快但太小的话可能会漏画
                        } catch (InterruptedException e) {
                        }
                    }
                    tx += 13;  //每行往右走一点
                }
                tx = 30;   //画完一行后要回到最左边
                ty += 25;  //画下一行
                y -= 0.1f; //循环次数减少
            } else {         //画完一个边框后判断继续画下一个边框或画实体
                if (!increaseOrDecrease) {    //G、B数值增加
                    G += 4;
                    B += 4;
                } else {                        //G、B数值减小
                    G -= 4;
                    B -= 4;
                }
                if (G >= 70)                //G、B数值在0~70范围内
                {
                    increaseOrDecrease = true;
                }
                if (!backRed)                //红变黑
                {
                    R -= 10;
                } else                        //黑变红
                {
                    R += 10;
                }
                if (R <= 0) {                //到黑色了准备变回红色
                    R = 1;                    //重新初始化R
                    backRed = true;
                }
                if (G <= 0 && B <= 0) {
                    G = 1;
                    B = 1;
                    backRed = true;
                }
                if (R < 255)     //R没有再次变回255说明还在画边框
                {
                    y = 1.5f;
                } else {          //画实体爱心
                    flag = true;
                    try {
                        Thread.sleep(500);
                    } catch (Exception e) {
                    }
                    this.repaint();
                }
                tx = 30;   //每画完一次都要重新初始化画图坐标
                ty = 10;
            }
        }
    }
}
