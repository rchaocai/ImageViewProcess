package com.xishuang.crc.lib_security;

import com.xishuang.crc.lib_security.ui.DecryptContainer;
import com.xishuang.crc.lib_security.ui.EncryptContainer;

import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;

/**
 * Author:xishuang
 * Date:2018.05.05
 * Des:加解密主入口
 */
public class MainEntry {
    /**
     * 界面对象
     */
    private JFrame mSurface;

    private MainEntry(JFrame mainFrame) {
        mSurface = mainFrame;
    }

    public void showDialog (String message, String title) {
        JOptionPane.showMessageDialog(mSurface, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        JFrame mainFrame = new JFrame();
        MainEntry mainEntry = new MainEntry(mainFrame);

        mainFrame.setTitle("加解密面板");
        mainFrame.setSize(450, 350);
        mainFrame.setLocationRelativeTo(null);
        // 创建选项卡面板
        JTabbedPane tabPane = new JTabbedPane();
        Container tabEncrypt = new EncryptContainer(mainEntry);
        Container tabDecrypt = new DecryptContainer(mainEntry);
        tabPane.add(tabEncrypt, "文件加密");
        tabPane.add(tabDecrypt, "文件解密");
        mainFrame.setContentPane(tabPane);
        //显示窗口
        mainFrame.setVisible(true);
    }
}
