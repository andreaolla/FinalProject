package com.example.andre.finalproject;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button btn1 = findViewById(R.id.muscle1);
        final Button btn2 = findViewById(R.id.muscle2);
        final Button btn3 = findViewById(R.id.muscle3);

        //costruisco l'array contenente i campioni
        String[]arr1 = new String[0];
        String[]arr2 = new String[0];
        String[]arr3 = new String[0];
        try {
            arr1 = SecondColumn("emg_healthy1.txt");
            arr2 = SecondColumn("emg_healthy2.txt");
            arr3 = arr2;
        } catch (IOException e) {   e.printStackTrace();    }


        final String[] finalArr1 = arr1; // per poterla utilizare dentro il Listener
        final String[] finalArr2 = arr2;
        final String[] finalArr3 = arr3;
        MyHandler1 handler1 = new MyHandler1();
        MyHandler2 handler2 = new MyHandler2();
        MyHandler3 handler3 = new MyHandler3();
        final Printer P1 = new Printer(handler1, finalArr1);
        final Printer P2 = new Printer(handler2, finalArr2);
        final Printer P3 = new Printer(handler3, finalArr3);


        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                //btn1.setActivated(!btn1.isActivated());
                if (P1.getState() == Thread.State.NEW) {P1.start();}
                else {    P1.suspendresume();     }
            }
        });


        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                //btn2.setActivated(!btn2.isActivated());
                if (P2.getState() == Thread.State.NEW) {P2.start();}
                else {    P2.suspendresume();     }
            }
        });


        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                //btn2.setActivated(!btn2.isActivated());
                if (P3.getState() == Thread.State.NEW) {P3.start();}
                else {    P3.suspendresume();     }
            }
        });

    }

    private class MyHandler1 extends Handler {
        @Override
        public void handleMessage(Message msg) {
            //System.out.println("DEBUG: Handler(Main): - " +msg);

            Bundle bundle = msg.getData(); //costruisco il bundle
            if (bundle.containsKey("refresh")) { // controllo che sia stato inserito il campione
                String value = bundle.getString("refresh"); //ne pesco il valore
                Button btn = findViewById(R.id.muscle1);
                System.out.println("DEBUG: Handler1(Main): - " + value);
                btn.setText(value + " mV"); //setto il testo che ho messo nel layout che ho creato
                if (Float.valueOf(value) < 0) {    btn.setActivated(false);    } //cambia colore se il valore è > o < di 0
                else {  btn.setActivated(true);    }                             // tramite selector_round_pressed.xml: rosso se < 0 e verde se >0
            }
        }
    }

    private class MyHandler2 extends Handler {
        @Override
        public void handleMessage(Message msg) {
            //System.out.println("DEBUG: Handler(Main): - " +msg);

            Bundle bundle = msg.getData(); //costruisco il bundle
            if (bundle.containsKey("refresh")) { // controllo che sia stato inserito il campione
                String value = bundle.getString("refresh"); //ne pesco il valore
                Button btn = findViewById(R.id.muscle2);
                System.out.println("DEBUG: Handler2(Main): - " +value);
                btn.setText(value + " mV"); //setto il testo che ho messo nel layout che ho creato
                float V =Float.valueOf(value);
                if (    V < -3/10   ) {    btn.setEnabled(true);    btn.setActivated(false);    }
                else {  if (V > 0)    {btn.setEnabled(false); btn.setActivated(true);}
                else {btn.setEnabled(true); btn.setActivated(true);}}                    //cambia colore
            }
        }
    }

    private class MyHandler3 extends Handler {
        @Override
        public void handleMessage(Message msg) {
            //System.out.println("DEBUG: Handler(Main): - " +msg);

            Bundle bundle = msg.getData(); //costruisco il bundle
            if (bundle.containsKey("refresh")) { // controllo che sia stato inserito il campione
                String value = bundle.getString("refresh"); //ne pesco il valore
                Button btn = findViewById(R.id.muscle3);
                System.out.println("DEBUG: Handler2(Main): - " +value);
                btn.setText(value + " mV"); //setto il testo che ho messo nel layout che ho creato
                if (Float.valueOf(value) < 0) {    btn.setActivated(false);    } //cambia colore se il valore è > o < di 0
                else {  btn.setActivated(true);    }                             // tramite selector_round_pressed.xml: rosso se < 0 e verde se >0
            }
        }
    }
    //Metodo fatto da me che presa in ingresso il nome di un file txt produce un array di stringhe contentente solo la seconda colonna del file
    private String[] SecondColumn(String directory) throws IOException {


        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(getAssets().open(directory)));
        } catch (IOException e) {   e.printStackTrace();    }

        String line = "a";
        String[] arr = new String[0];
        int i = 0;
        while (!(line == null)) {

            assert reader != null;
            line = reader.readLine();
            if (line != null) {
                String[] columns = line.split(" ");
                //System.out.println("COLONNA1 = " + columns[0b1]);


                int aLen = arr.length;
                int bLen = columns.length;
                String[] c = new String[aLen + bLen-1];
                while (i < aLen) {
                    c[i] = arr[i];
                    i++;
                }
                c[i] = columns[1];
                i = 0;

                arr = c;
            }
        } System.out.println("FUNZIONA" + arr.length);
        return arr;
    }
}
