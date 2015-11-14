/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.aaron.desktop.view;

import com.aaron.desktop.main.Main;
import static com.aaron.desktop.main.Main.ManifestAttribute.*;

/**
 *
 * @author Aaron
 */
public final class AboutFrameView extends javax.swing.JFrame
{
    /**
     * Creates new form AboutFrameView
     */
    public AboutFrameView()
    {
        initComponents();
        setSize(330, 180);
        this.setLabels();
    }

    public void setLabels()
    {
        this.titleLabel.setText(Main.getInfo(Specification_Title));
        this.authorLabel.setText(Main.getInfo(Implementation_Vendor));
        this.versionLabel.setText(Main.getInfo(Specification_Version));
        this.buildVersionLabel.setText(Main.getInfo(Implementation_Version));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        title = new javax.swing.JLabel();
        developer = new javax.swing.JLabel();
        version = new javax.swing.JLabel();
        buildVersion = new javax.swing.JLabel();
        titleLabel = new javax.swing.JLabel();
        authorLabel = new javax.swing.JLabel();
        versionLabel = new javax.swing.JLabel();
        buildVersionLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("About");
        setResizable(false);
        getContentPane().setLayout(null);

        title.setText("Title: ");
        getContentPane().add(title);
        title.setBounds(20, 20, 60, 20);

        developer.setText("Developer: ");
        getContentPane().add(developer);
        developer.setBounds(20, 50, 80, 20);

        version.setText("Version: ");
        getContentPane().add(version);
        version.setBounds(20, 80, 90, 20);

        buildVersion.setText("Build Version: ");
        getContentPane().add(buildVersion);
        buildVersion.setBounds(20, 110, 90, 20);
        getContentPane().add(titleLabel);
        titleLabel.setBounds(130, 20, 210, 20);
        getContentPane().add(authorLabel);
        authorLabel.setBounds(130, 50, 210, 20);
        getContentPane().add(versionLabel);
        versionLabel.setBounds(130, 80, 210, 20);
        getContentPane().add(buildVersionLabel);
        buildVersionLabel.setBounds(130, 110, 210, 20);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel authorLabel;
    private javax.swing.JLabel buildVersion;
    private javax.swing.JLabel buildVersionLabel;
    private javax.swing.JLabel developer;
    private javax.swing.JLabel title;
    private javax.swing.JLabel titleLabel;
    private javax.swing.JLabel version;
    private javax.swing.JLabel versionLabel;
    // End of variables declaration//GEN-END:variables
}
