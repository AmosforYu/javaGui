package com.yyb.gui.box;

import com.yyb.gui.utils.AppkeyEnum;
import com.yyb.gui.utils.CipherUtil;
import com.yyb.gui.utils.DESEncrypt;
import com.yyb.gui.utils.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Yamos
 * @description Boxx
 * @date 2021/1/14 0014 15:06
 */
public class Boxx extends Thread implements ActionListener, WindowListener {
    private JTextArea notEnOrDeStr, appkey;
    private JTextArea taLog;         //日志输出区域
    private JButton jbtEncode;       //加密按钮
    private JButton jbtDecode;       //解密按钮
    private JTextArea addKey, addSecret;
    private JButton jbtAddKey;       //增加密钥对按钮
    private static Map<String, String> keyMap = new HashMap<>();

    public void run() {
        //父窗体
        JFrame jFrame = new JFrame("和家固话加解密");
        // 创建按钮
        jbtEncode = new JButton("加密");
        jbtEncode.addActionListener(this);
        jbtDecode = new JButton("解密");
        jbtDecode.addActionListener(this);
        jbtAddKey = new JButton("增加密钥对");
        jbtAddKey.addActionListener(this);

        // 创建相关的文本域
        notEnOrDeStr = new JTextArea("");
        notEnOrDeStr.setLineWrap(true);
        notEnOrDeStr.setWrapStyleWord(true);
        appkey = new JTextArea("");
        appkey.setLineWrap(true);
        appkey.setWrapStyleWord(true);

        taLog = new JTextArea("");
        taLog.setLineWrap(true);
        taLog.setWrapStyleWord(true);
        taLog.setEditable(false);
        taLog.setBackground(Color.lightGray);

        // 创建相关的Label标签
        JLabel label1 = new JLabel("请输入待加/解密信息(无密钥加解密时，密钥框无需填写内容)");
        JLabel label2 = new JLabel("请输入密钥");
        JLabel labelLog = new JLabel("输出结果信息");

        // 待加/解密信息输入框 + appkey输入框
        JPanel operateText = new JPanel(new GridLayout(4, 1));
        operateText.add(label1);
        operateText.add(new JScrollPane(notEnOrDeStr));
        operateText.add(label2);
        operateText.add(new JScrollPane(appkey));

        //日志输出
        JPanel logPanel = new JPanel(new GridLayout(2, 1));
        logPanel.add(labelLog);
        logPanel.add(new JScrollPane(taLog));

        //本地增加密钥对
        JPanel addSecretPanel = new JPanel(new GridLayout(3, 2));
        JLabel label31 = new JLabel("appKey：");
        addSecretPanel.add(label31);
        JLabel label32 = new JLabel("appSecret：");
        addSecretPanel.add(label32);
        addKey = new JTextArea("");
        addSecret = new JTextArea("");
        addSecretPanel.add(new JScrollPane(addKey));
        addSecretPanel.add(new JScrollPane(addSecret));
        addSecretPanel.add(jbtAddKey);

        //开始按钮及左右信息
        MyPanel enOrDePanel = new MyPanel(new GridLayout(1, 2));
        enOrDePanel.add(jbtEncode);
        enOrDePanel.add(jbtDecode);

        //上级布局
        JPanel panel22 = new JPanel(new GridLayout(5, 1));
        panel22.add(operateText);
        panel22.add(logPanel);
        panel22.add(enOrDePanel);
        panel22.add(addSecretPanel);

        jFrame.setLayout(new GridLayout());
        jFrame.add(panel22, BorderLayout.CENTER);

        // 初始化JFrame窗口
        jFrame.setLocation(800, 300);
        jFrame.setSize(600, 600);
        jFrame.setBackground(Color.darkGray);
        jFrame.setResizable(true);
        jFrame.setVisible(true);

        panel22.setLayout(null);
        logPanel.setBounds(1, 95, 598, 300);
        enOrDePanel.setBounds(10, 390, 580, 80);
        addSecretPanel.setBounds(10, 460, 580, 80);

    }

    @Override
    public void actionPerformed(ActionEvent view) {
        try {

            if (view.getSource() == jbtEncode) {
                String toBeStr = notEnOrDeStr.getText();
                String appkeyStr = appkey.getText();

                if (StringUtils.isNotEmpty(toBeStr)) {
                    String encodedStr;
                    if (StringUtils.isEmpty(appkeyStr)) {
                        taLog.append("Enter encode With No Secret method");
                        taLog.append("\n");

                        encodedStr = encodeWithNoSecret(toBeStr);
                    } else {
                        taLog.append("Enter encode With Secret method");
                        taLog.append("\n");

                        String secret = getSecret(appkeyStr);
                        if (StringUtils.isEmpty(secret)) {
                            addKey.setText("请添加本地密钥对");
                            addSecret.setText("请添加本地密钥对");
                            encodedStr = "请添加本地密钥对";
                        } else {
                            encodedStr = encodeWithSecret(toBeStr, secret);
                        }
                    }

                    notEnOrDeStr.setText(encodedStr);
                }
            }

            if (view.getSource() == jbtDecode) {
                String toBeStr = notEnOrDeStr.getText();
                String appkeyStr = appkey.getText();

                if (StringUtils.isNotEmpty(toBeStr)) {
                    String encodedStr;
                    if (StringUtils.isEmpty(appkeyStr)) {
                        taLog.append("Enter decode With No Secret method");
                        taLog.append("\n");

                        encodedStr = decodeWithNoSecret(toBeStr);
                    } else {
                        taLog.append("Enter decode With Secret method");
                        taLog.append("\n");

                        String secret = getSecret(appkeyStr);
                        if (StringUtils.isEmpty(secret)) {
                            addKey.setText("请添加本地密钥对");
                            addSecret.setText("请添加本地密钥对");
                            encodedStr = "请添加本地密钥对";
                        } else {
                            encodedStr = decodeWithSecret(toBeStr, secret);
                        }
                    }
                    notEnOrDeStr.setText(encodedStr);
                }
            }

            if (view.getSource() == jbtAddKey) {
                taLog.append("Enter add key map method");
                taLog.append("\n");

                String addKeyStr = addKey.getText().trim();
                String addSecretStr = addSecret.getText().trim();

                if (StringUtils.isEmpty(addKeyStr)) {
                    addKey.setText("请填写正确的appkey");
                } else if (StringUtils.isEmpty(addSecretStr)) {
                    addSecret.setText("请填写正确的appSecret");
                } else if (StringUtils.isEmpty(addKeyStr) && StringUtils.isEmpty(addSecretStr)) {
                    addKey.setText("请填写正确的appkey");
                    addSecret.setText("请填写正确的appSecret");
                } else {
                    if (isRightSecret(addSecretStr)) {
                        keyMap.put(addKeyStr, addSecretStr);
                        taLog.append("add appkey map complete, appkey is : " + addKeyStr);
                        taLog.append("\n");
                        addKey.setText("appkey添加成功");
                        addSecret.setText("appSecret添加成功");
                    } else {
                        addSecret.setText("请填写正确的appSecret");
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            taLog.append("error: " + e);
            taLog.append("\n");
        }

    }

    private boolean isRightSecret(String secret) {
        double secretValue = Double.parseDouble(secret);
        boolean isRight = secret.length() <= 32 && secretValue % 16 == 0;
        return isRight;
    }

    private String encodeWithNoSecret(String toBeStr) {
        String encrypt = DESEncrypt.encrypt(toBeStr);
        taLog.append("encode complete, result is : " + encrypt);
        taLog.append("\n");
        return encrypt;
    }

    private String encodeWithSecret(String toBeStr, String secret) throws UnsupportedEncodingException {
        taLog.append("to be encode str is :" + toBeStr);
        taLog.append("\n");

        String encrypt = null;
        try {
            encrypt = CipherUtil.enCiper(secret, toBeStr);
        } catch (Exception e) {
            taLog.append("encode error, error is : " + e.getMessage());
            taLog.append("\n");
            encrypt = "待加密字符串异常";
        }

        taLog.append("encode complete, result is : " + encrypt);
        taLog.append("\n");
        return URLEncoder.encode(encrypt, "UTF-8");
    }

    private String getSecret(String appkey) {
        if (StringUtils.isNotEmpty(AppkeyEnum.getSecret(appkey))) {
            return AppkeyEnum.getSecret(appkey);
        }

        if (keyMap.isEmpty()) {
            return "";
        }

        return keyMap.get(appkey);
    }

    private String decodeWithNoSecret(String toBeStr) {
        String decrypt = DESEncrypt.decrypt(toBeStr);
        taLog.append("decode complete, result is : " + decrypt);
        taLog.append("\n");
        return decrypt;
    }

    private String decodeWithSecret(String toBeStr, String secretStr) {
        taLog.append("to be decode str is :" + toBeStr);
        taLog.append("\n");
        String decrypt = null;
        try {
            decrypt = CipherUtil.deCiper(secretStr, toBeStr);
        } catch (Exception e) {
            taLog.append("decode error, error is : " + e.getMessage());
            taLog.append("\n");
            decrypt = "待加密字符串异常";
        }
        taLog.append("encode complete, result is : " + decrypt);
        taLog.append("\n");
        return decrypt;
    }


    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {
        System.exit(0);
    }

    @Override
    public void windowClosed(WindowEvent e) {
        System.exit(0);
    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }
}
