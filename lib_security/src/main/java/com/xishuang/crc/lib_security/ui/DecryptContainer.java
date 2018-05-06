package com.xishuang.crc.lib_security.ui;

import com.xishuang.crc.lib_security.util.AES;
import com.xishuang.crc.lib_security.util.Base64;
import com.xishuang.crc.lib_security.MainEntry;
import com.xishuang.crc.lib_security.util.RSA;
import com.xishuang.crc.lib_security.util.SignKey;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyPair;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.filechooser.FileSystemView;

/**
 * Author:xishuang
 * Date:2018.05.05
 * Des:文件解密界面
 */
public class DecryptContainer extends Container implements ActionListener {

    private JFileChooser mJFileChooser = new JFileChooser();
    private JButton mChooseFileBtn = new JButton("选择文件");
    private JButton mInputBtn = new JButton("导入密钥");
    private JButton mBuildBtn = new JButton("解密文件");
    /**
     * 文件选择路径
     */
    private JTextField mFilePath = new JTextField();
    /**
     * 密钥展示框
     */
    private JTextArea mKeyText = new JTextArea();

    private MainEntry mMainEntry;

    public DecryptContainer(MainEntry frame) {

        FileSystemView fsv = FileSystemView.getFileSystemView();
        File home = fsv.getHomeDirectory();
        mJFileChooser.setCurrentDirectory(home);
        // 设定只能选择到文件
        mJFileChooser.setFileSelectionMode(0);

        mMainEntry = frame;
        mChooseFileBtn.addActionListener(this);
        mBuildBtn.addActionListener(this);
        mInputBtn.addActionListener(this);

        JLabel jl1 = new JLabel("解密文件路径");
        JLabel jl2 = new JLabel("Base64文件密钥");

        mKeyText.setLineWrap(true);

        JScrollPane scrollPane = new JScrollPane(mKeyText);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        jl1.setBounds(30, 30, 80, 20);
        mFilePath.setBounds(110, 30, 200, 20);
        mChooseFileBtn.setBounds(320, 30, 90, 20);
        jl2.setBounds(30, 60, 120, 20);
        scrollPane.setBounds(30, 80, 370, 130);
        mInputBtn.setBounds(30, 232, 90, 20);
        mBuildBtn.setBounds(310, 232, 90, 20);


        add(mFilePath);
        add(mChooseFileBtn);
        add(jl1);
        add(jl2);
        add(scrollPane);
        add(mBuildBtn);
        add(mInputBtn);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(mChooseFileBtn)) {
            mJFileChooser.setDialogTitle("选择要解密的文件");
            //此句是打开文件选择器界面的触发语句
            int state = mJFileChooser.showOpenDialog(null);
            //撤销则返回
            if (state != 1) {
                // f为选择到的文件
                File f = mJFileChooser.getSelectedFile();
                mFilePath.setText(f.getAbsolutePath());
            }
        } else if (e.getSource().equals(mBuildBtn)) {
            decrypt();
        } else if (e.getSource().equals(mInputBtn)) {
            inputKey();
        }
    }

    /**
     * 导入已存在密钥
     */
    private void inputKey() {
        mJFileChooser.setDialogTitle("选择要导入的密钥文件");
        //设定只能选择到文件
        mJFileChooser.setFileSelectionMode(0);
        //此句是打开文件选择器界面的触发语句
        int state = mJFileChooser.showOpenDialog(null);
        //撤销则返回
        if (state != 1) {
            //f为选择到的文件
            File f = mJFileChooser.getSelectedFile();
            readKey(f);
        }
    }

    private void readKey(File file) {
        long size = file.length();
        byte[] buffer = new byte[(int) size];
        try {
            FileInputStream fis = new FileInputStream(file);
            fis.read(buffer);
            fis.close();
            String base64Str = Base64.encode(buffer);
            mKeyText.setText(base64Str);
        } catch (IOException e) {
            mMainEntry.showDialog(e.getMessage(), "错误");
        }
    }

    /**
     * 解密文件
     */
    private void decrypt() {
        try {
            String path = mFilePath.getText();
            if (path == null || path.equals("")) {
                mMainEntry.showDialog("请输入要解密的文件路径!", "路径文本框为空");
                return;
            }
            File fromFile = new File(path);
            if (!fromFile.exists()) {
                mMainEntry.showDialog("文件不存在!", "错误");
                return;
            }
            File toFile = new File(fromFile.getParentFile(), "decrypt_" + fromFile.getName());

            //获取被加密的密钥raw key
            String keyStr = mKeyText.getText();
            byte[] key = Base64.decode(keyStr);
            //获取应用签名密钥对，公钥解密raw key
            KeyPair keypair = SignKey.getSignKeyPair();
            byte[] rawkey = RSA.decrypt(key, keypair.getPublic());
            //用raw key去解密文件
            AES.decryptFile(rawkey, fromFile, toFile);
            mMainEntry.showDialog("成功生成解密文件，路径:" + toFile.getAbsolutePath(), "解密文件成功");
        } catch (Exception e) {
            e.printStackTrace();
            mMainEntry.showDialog(e.getMessage(), "错误");
        }
    }
}
