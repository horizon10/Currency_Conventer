import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DovizUygulamasi  {
    public static void main(String[] args) {
        JFrame jFrame=new JFrame();
        jFrame.setBounds(100,100,500,500);
        jFrame.setResizable(false);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Font f1=new Font("Arial",Font.BOLD,15);
        jFrame.getContentPane().setBackground(new Color(32, 80, 12));

        //miktar label'ı ve miktarı yazmak için textField
        JLabel label0=new JLabel("miktar:");
        label0.setBounds(10,10,150,100);
        label0.setForeground(new Color(161, 161, 8));
        jFrame.getContentPane().add(label0);

        JTextArea textArea=new JTextArea();
        textArea.setBounds(65,50,50,15);
        textArea.setFont(f1);
        jFrame.getContentPane().add(textArea);


        //çevrilmek istenen para birimi
        String araba[]={" ","USD","EUR","GBP","JPY","CNY","KWD","TRY"};
        JLabel label1=new JLabel("hangi para birimi:");
        label1.setBounds(10,80,150,100);
        label1.setForeground(new Color(161, 161, 8));
        jFrame.getContentPane().add(label1);

        JComboBox comboBox0=new JComboBox<>(araba);
        comboBox0.setBounds(120,115,100,30);
        jFrame.getContentPane().add(comboBox0);

        //çevrilecek para birimi
        JLabel label2=new JLabel("çevirilecek para birimi:");
        label2.setBounds(10,150,150,100);
        label2.setForeground(new Color(161, 161, 8));
        jFrame.getContentPane().add(label2);

        JComboBox comboBox1=new JComboBox<>(araba);
        comboBox1.setBounds(150,185,100,30);
        jFrame.getContentPane().add(comboBox1);

        //çevirilen miktar
        JLabel label3=new JLabel("çevilen miktar:");
        label3.setBounds(10,230,150,100);
        label3.setForeground(new Color(161, 161, 8));
        jFrame.getContentPane().add(label3);

        JLabel label=new JLabel("");
        label.setBounds(100,230,150,100);
        label.setForeground(new Color(255, 255, 255));
        jFrame.getContentPane().add(label);

        //kur değeri
        JLabel label4=new JLabel("kur: ");
        label4.setBounds(10,200,150,100);
        label4.setForeground(new Color(161, 161, 8));
        jFrame.getContentPane().add(label4);

        JLabel label5=new JLabel("");
        label5.setBounds(40,200,150,100);
        label5.setForeground(new Color(255, 255, 255));
        jFrame.getContentPane().add(label5);

        //çevirmek için basılan buton
        JButton button=new JButton("çevir");
        button.setBounds(270,185,100,30);
        jFrame.getContentPane().add(button);

        //buton aksiyonları
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //herhangi biri seçilmezse hata döndür
                if(!textArea.getText().isEmpty() &&comboBox0.getSelectedIndex()!=0&&comboBox1.getSelectedIndex()!=0){
                String ifade=textArea.getText();
                    if (ifade.contains(",")) {
                        ifade = ifade.replace(",", ".");
                    }
                    //geçersiz miktar girilirse
                    try {
                        float amount = Float.parseFloat(ifade);
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Geçersiz miktar formatı. Lütfen sayısal bir değer girin.");
                    }
                    //kullanıcıdan alınan veriler
                    float amount=Float.parseFloat(ifade);

                    String temp1= (String) comboBox0.getSelectedItem();

                    String temp2= (String) comboBox1.getSelectedItem();

                    try {
                        sendhttpsRequest(temp1,temp2,amount,jFrame,label,label5);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
                else{
                    JOptionPane.showMessageDialog(null, "miktar veya birim girilmemiş!");
                }

            }

        });


        jFrame.setLayout(null);
        jFrame.setVisible(true);



    }
    //istek gönderme
    private static void sendhttpsRequest(String temp1, String temp2, float amount, JFrame jFrame, JLabel label,JLabel label5) throws IOException {
        //apinin key kısmı size ait
        String GET_URL="https://v6.exchangerate-api.com/v6/KEY/pair/"+temp1+"/"+temp2+"/"+amount;
        URL url=new URL(GET_URL);
        HttpURLConnection httpURLConnection= (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod("GET");
        int responseCode= httpURLConnection.getResponseCode();

        if (responseCode==HttpURLConnection.HTTP_OK){
            BufferedReader in=new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            String inpuline;
            StringBuffer response=new StringBuffer();

            while ((inpuline=in.readLine())!=null){
                response.append(inpuline);
            }in.close();
            JSONObject object=new JSONObject(response.toString());
            Double exchangeRate= object.getDouble("conversion_rate");
            Double conversionResult= object.getDouble("conversion_result");
            ;
            label5.setText(String.valueOf(exchangeRate) +" "+temp2);
            label.setText(String.valueOf(conversionResult) +" "+  temp2);
            jFrame.revalidate();
            jFrame.repaint();
        }
        else {
            JOptionPane.showMessageDialog(null, "istek alma başarısız oldu");
        }
    }
}