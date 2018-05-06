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
import java.io.FileOutputStream;
import java.security.KeyPair;
import java.util.UUID;

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
 * Des:文件加密界面
 */
public class EncryptContainer extends Container implements ActionListener {

    private JButton mChooseFileBtn = new JButton("选择文件");
    private JButton mOutputBtn = new JButton("导出密钥");
    private JButton mRandomBtn = new JButton("随机生成密钥");
    private JButton mImportBtn = new JButton("导入密钥");
    private JButton mEncryptFileBtn = new JButton("加密文件");
    /**
     * 文件选择路径
     */
    private JTextField mFilePath = new JTextField();
    /**
     * 密钥展示框
     */
    private JTextArea mKeyText = new JTextArea();

    private MainEntry mMainEntry;

    private JFileChooser mJFileChooser;

    public EncryptContainer(MainEntry frame) {
        mJFileChooser = new JFileChooser();
        FileSystemView fsv = FileSystemView.getFileSystemView();
        File home = fsv.getHomeDirectory();
        mJFileChooser.setCurrentDirectory(home);
        // 设定只能选择到文件
        mJFileChooser.setFileSelectionMode(0);

        mMainEntry = frame;
        mChooseFileBtn.addActionListener(this);
        mImportBtn.addActionListener(this);
        mRandomBtn.addActionListener(this);
        mEncryptFileBtn.addActionListener(this);
        mOutputBtn.addActionListener(this);

        JLabel jl1 = new JLabel("加密文件路径");
        JLabel jl2 = new JLabel("Base64文件密钥");

        mKeyText.setLineWrap(true);

        JScrollPane scrollPane = new JScrollPane(mKeyText);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        //xz坐标，y坐标，宽度，高度
        jl1.setBounds(30, 30, 80, 20);
        mFilePath.setBounds(110, 30, 200, 20);
        mChooseFileBtn.setBounds(320, 30, 90, 20);
        jl2.setBounds(30, 60, 120, 20);
        scrollPane.setBounds(30, 80, 370, 130);

        mRandomBtn.setBounds(25, 232, 120, 20);
        mImportBtn.setBounds(140, 232, 90, 20);
        mOutputBtn.setBounds(230, 232, 90, 20);
        mEncryptFileBtn.setBounds(310, 232, 90, 20);


        add(mFilePath);
        add(mChooseFileBtn);
        add(jl2);
        add(jl1);
        add(scrollPane);
        add(mEncryptFileBtn);
        add(mOutputBtn);
        add(mImportBtn);
        add(mRandomBtn);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(mChooseFileBtn)) {
            mJFileChooser.setDialogTitle("选择要加密的文件");
            // 此句是打开文件选择器界面的触发语句
            int state = mJFileChooser.showOpenDialog(null);
            // 撤销则返回
            if (state == 1) {
                return;
            } else {
                // f为选择到的文件
                File f = mJFileChooser.getSelectedFile();
                mFilePath.setText(f.getAbsolutePath());
            }
        } else if (e.getSource().equals(mEncryptFileBtn)) {
            encrypt();
        } else if (e.getSource().equals(mOutputBtn)) {
            outputKey();
        } else if (e.getSource().equals(mRandomBtn)) {
            randomKey();
        } else if (e.getSource().equals(mImportBtn)) {
            importKey();
        }
    }

    /**
     * 加密文件
     */
    private void encrypt() {
        try {
            String path = mFilePath.getText();
            if (path == null || path.equals("")) {
                mMainEntry.showDialog("请设置密钥导出的目录!", "路径文本框为空");
                return;
            }
            File fromFile = new File(path);
            if (!fromFile.exists()) {
                mMainEntry.showDialog("文件不存在!", "错误");
                return;
            }

            String base64Key = mKeyText.getText();

            if (base64Key == null || base64Key.equals("")) {
                mMainEntry.showDialog("请先随机生成密钥或导入密钥！", "密钥为空");
                return;
            }
            KeyPair pair = SignKey.getSignKeyPair();

            byte[] data = Base64.decode(base64Key);

            byte[] rawkey = RSA.decrypt(data, pair.getPublic());

            File toFile = new File(fromFile.getParentFile(), "encrypt_" + fromFile.getName());
            AES.encryptFile(rawkey, fromFile, toFile);
            mMainEntry.showDialog("成功生成加密文件，路径:" + toFile.getAbsolutePath(), "加密文件成功");
        } catch (Exception e) {
            mMainEntry.showDialog(e.getMessage(), "错误");
        }
    }

    /**
     * 导入已存在的密钥
     */
    private void importKey() {
        mJFileChooser.setDialogTitle("选择已存在的密钥文件");
        // 此句是打开文件选择器界面的触发语句
        int state = mJFileChooser.showOpenDialog(null);
        // 撤销则返回
        if (state != 1) {
            // f为选择到的文件
            File f = mJFileChooser.getSelectedFile();

            long size = f.length();
            byte[] buffer = new byte[(int) size];
            try {
                FileInputStream fis = new FileInputStream(f);
                fis.read(buffer);
                fis.close();
                String base64Str = Base64.encode(buffer);
                mKeyText.setText(base64Str);
            } catch (Exception e) {
                mMainEntry.showDialog(e.getMessage(), "错误");
            }
        }
    }

    /**
     * 导出密钥，这个密钥是被加密后的
     */
    private void outputKey() {
        try {
            String path = mFilePath.getText();
            if (path == null || path.equals("")) {
                mMainEntry.showDialog("请设置密钥导出的目录!", "路径文本框为空");
                return;
            }
            File dir = new File(path).getParentFile();
            if (!dir.exists()) {
                mMainEntry.showDialog("导出目录不存在!", "导出错误");
                return;
            }

            String base64Key = mKeyText.getText();
            if (base64Key == null || base64Key.equals("")) {
                mMainEntry.showDialog("请先生成密钥再导出!", "密钥文本框为空");
                return;
            }
            byte[] key = Base64.decode(base64Key);
            if (key == null) {
                mMainEntry.showDialog("Base64转换失败", "key == null");
                return;
            }
            //将raw key输出
            File keyFile = new File(dir, "testkey.dat");
            FileOutputStream fos = new FileOutputStream(keyFile);
            fos.write(key);
            fos.flush();
            fos.close();
            mMainEntry.showDialog("导出密钥成功！路径:" + keyFile.getAbsolutePath(), "导出密钥成功");
        } catch (Exception e) {
            mMainEntry.showDialog(e.getMessage(), "错误");
        }
    }

    /**
     * 生成随机密钥
     */
    private void randomKey() {
        try {
            //生成raw key
            String uuid = UUID.randomUUID().toString();
            byte[] seed = uuid.getBytes("UTF-8");
            byte[] rawkey = AES.getRawKey(seed);
            //获取应用签名的密钥对
            KeyPair pair = SignKey.getSignKeyPair();
            //将raw key加密
            byte[] key = RSA.encrypt(rawkey, pair.getPrivate());
            String base64Key = Base64.encode(key);
            mKeyText.setText(base64Key);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
