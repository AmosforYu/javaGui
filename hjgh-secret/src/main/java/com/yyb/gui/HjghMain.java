package com.yyb.gui;

import com.yyb.gui.box.Boxx;

/**
 * @author Yamos
 * @description Main
 * @date 2021/1/14 0014 15:04
 */
public class HjghMain {
    public static void main(String[] args) {
        System.out.println("THis is my first java GUI demo");
		
		try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
		
        Boxx boxx = new Boxx();
        boxx.run();
    }
}
