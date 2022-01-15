package view;

import javax.swing.*;

public class Window extends JFrame {

    Panel panel;

    public Window() {
        panel = new Panel();
        panel.setBounds(0, 0, 860, 750);
        add(panel);    //只有将画板添加到窗口才能画图

        //要使用线程才能实现动画效果
        Thread t = new Thread(panel);
        t.start();

        setLayout(null);        //画板要能调节大小，则窗口不能使用默认排版方式
        setBounds(400, 50, 860, 750);
        setVisible(true);
        validate();
        setDefaultCloseOperation(Window.EXIT_ON_CLOSE);
    }
}
