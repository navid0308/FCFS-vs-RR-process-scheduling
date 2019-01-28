package fcfsnrr;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class FCFSSJB {
    private static final String times[]={"0ms","1ms","2ms","3ms","4ms","5ms", "6ms"};
    private static final String times2[]={"1ms","2ms","3ms","4ms","5ms","6ms"};
    private static final String inp[]={"1","2","3","4","5","6"};
    private static String GNATT_FCFS="GNATT: [ ";
    private static String GNATT_RR="GNATT: [ ";
    
    private static double sum_fcfs=0, sum_rr=0;
    private static int x;
    
    private static JFrame myFrame=new JFrame("FCFS & SJB");
    
    private static JComboBox [] cb;
    private static JComboBox boxy=new JComboBox(times2);
    private static JLabel [] label;
    private static JTextField [] tField;
    
    private static final JLabel q=new JLabel("Time quanta:");
    private static final JButton ok=new JButton("OK");
    private static final JButton cancel=new JButton("CANCEL");
    
    private static JLabel lb1=new JLabel("Loading....");
    private static JLabel lb2=new JLabel("Loading....");
    private static JLabel gnatt1=new JLabel("Loading....");
    private static JLabel gnatt2=new JLabel("Loading....");
    
    public static void sortS(String s[])
    {
        for(int i=0;i<x-1; i++)
        {
            if(s[i].charAt(0)>s[i+1].charAt(0))
            {
                String temp=s[i+1];
                s[i+1]=s[i];
                s[i]=temp;
                i=-1;
            }
        }
    }
    
    private static Thread FCFS=new Thread(){
        public void run()
        {
            String array[]=new String[x];
            
            for(int i=0; i<x;i++)
            {
                array[i]=(cb[2*i].getSelectedIndex())+" "+(cb[2*i+1].getSelectedIndex()+1+" "+(i+1));
            }
            sortS(array);
            
            int nums[][]=new int[x][3];
            
            for(int i=0; i<x; i++)
            {
                StringTokenizer str=new StringTokenizer(array[i], " ");
                nums[i][0]=Integer.parseInt((String) str.nextElement());//0=arrival
                nums[i][1]=Integer.parseInt((String) str.nextElement());//1=burst
                nums[i][2]=Integer.parseInt((String) str.nextElement());//2=process number
            }
            
            boolean done=false;
            int c=0, current=0;
            
            while(!done)
            {
                if(nums[c][0]>current)
                {
                    GNATT_FCFS=GNATT_FCFS+current+"ms...";
                    current=nums[c][0];
                }
                
                if(nums[c][1]>0)
                {
                    GNATT_FCFS=GNATT_FCFS+current+"ms (Process "+nums[c][2]+") ";
                    sum_fcfs=sum_fcfs+current-nums[c][0];
                    current=current+nums[c][1];
                    nums[c][1]=0;
                }
                
                c++;
                if(c==x)
                    c=0;
                
                done=true;
                for(int ab=0; ab<x; ab++)
                {
                    if(nums[ab][1]>0)
                        done=false;
                }
            }
            
            sum_fcfs=sum_fcfs/x;
            GNATT_FCFS=GNATT_FCFS+current+"ms ]";
            
            gnatt1.setText(GNATT_FCFS);
            lb1.setText("Average waiting time for first come first serve: "+sum_fcfs+"ms");
            myFrame.pack();
        }
    };
    
    private static Thread RR=new Thread(){
        public void run()
        {
            String array[]=new String[x];
            
            for(int i=0; i<x;i++)
            {
                array[i]=(cb[2*i].getSelectedIndex())+" "+(cb[2*i+1].getSelectedIndex()+1+" "+(i+1));
            }
            sortS(array);
            
            int nums[][]=new int[x][3];
            
            for(int i=0; i<x; i++)
            {
                StringTokenizer str=new StringTokenizer(array[i], " ");
                nums[i][0]=Integer.parseInt((String) str.nextElement());//0=arrival
                nums[i][1]=Integer.parseInt((String) str.nextElement());//1=burst
                nums[i][2]=Integer.parseInt((String) str.nextElement());//2=process number
            }
            
            boolean done=false;
            int waiter[]=new int[x];//calculate waiting time between 2 executions
            int time_quanta=boxy.getSelectedIndex()+1;
            int current=0, c=0, tracker=-1;
            
            while(!done)
            {
                if(waiter[c]==0)
                    waiter[c]=nums[c][0];
                
                if(nums[c][1]>0)
                {
                    if(tracker!=c)
                        GNATT_RR=GNATT_RR+current+"ms (Process "+nums[c][2]+") ";
                    
                    tracker=c;
                    
                    sum_rr=sum_rr+current-waiter[c];
                    
                    if(nums[c][1]>=time_quanta)
                    {
                        current=current+time_quanta;
                        nums[c][1]=nums[c][1]-time_quanta;
                    }
                    else
                    {
                        current=current+nums[c][1];
                        nums[c][1]=0;
                    }
                    waiter[c]=current;
                }
                
                if(c<(x-1))
                {
                    if((nums[c][1]==0 && nums[c+1][0]>current))
                    {
                        GNATT_RR=GNATT_RR+current+"ms...";
                        current=nums[c+1][0];
                    }
                    else if(nums[c][0]>current)
                    {
                        GNATT_RR=GNATT_RR+current+"ms...";
                        current=nums[c][0];
                    }
                }
                
                if(c==x || c==x-1)
                    c=0;
                else if(current>=nums[c+1][0])
                    c++;
                
                done=true;
                for(int ab=0; ab<x; ab++)
                {
                    if(nums[ab][1]>0)
                        done=false;
                }
            }
            
            sum_rr=sum_rr/x;
            GNATT_RR=GNATT_RR+current+"ms ]";
            
            gnatt2.setText(GNATT_RR);
            lb2.setText("Average waiting time for round robin: "+sum_rr+"ms");
            myFrame.pack();
        }
    };
    
    public static void out()
    {
        JPanel p=new JPanel();
        p.setLayout(new GridLayout(4,1,20,20));
        p.add(gnatt1);
        p.add(lb1);
        p.add(gnatt2);
        p.add(lb2);
        p.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        
        myFrame.add(p);
        
        myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        myFrame.pack();
        myFrame.setLocationByPlatform(true);
        myFrame.setResizable(false);
        myFrame.setVisible(true);
    }
    
    public static void starter()
    {
        label=new JLabel[x];
        for(int i=0; i<x; i++)
        {
            label[i]=new JLabel(Integer.toString(i+1));
        }
        
        cb=new JComboBox[2*x];
        for(int i=0; i<2*x-1; i=i+2)
        {
            cb[i]=new JComboBox(times);
            
            cb[i+1]=new JComboBox(times2);
        }
        
        JLabel l1=new JLabel("Process#");
        JLabel l2=new JLabel("Arrival time:");
        JLabel l3=new JLabel("Burst time:");
        
        JPanel myPanel=new JPanel(new GridLayout(x+1,3,10,10));
        JPanel tq=new JPanel(new GridLayout(1,2,10,10));
        JPanel buttons=new JPanel(new FlowLayout(FlowLayout.CENTER,10,10));
        JPanel input=new JPanel();
        input.setLayout(new BoxLayout(input, BoxLayout.Y_AXIS));
        
        myPanel.add(l1);
        myPanel.add(l2);
        myPanel.add(l3);
        
        for(int i=0; i<x; i++)
        {
            myPanel.add(label[i]);
            myPanel.add(cb[2*i]);
            myPanel.add(cb[2*i+1]);
        }
        
        buttons.add(ok);
        buttons.add(cancel);
        tq.add(q);
        tq.add(boxy);
        tq.setBorder(BorderFactory.createEmptyBorder(20,10,10,10));
        
        input.add(myPanel);
        input.add(tq);
        input.add(buttons);
        
        input.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        
        myFrame.add(input);
        
        myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        myFrame.pack();
        myFrame.setLocationByPlatform(true);
        myFrame.setResizable(false);
        myFrame.setVisible(true);
        
        cancel.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent cn)
            {
                myFrame.getContentPane().removeAll();
                myFrame.dispose();
                System.exit(0);
            }
        });
        
        ok.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent k)
            {
                FCFS.start();
                RR.start();
                
                myFrame.getContentPane().removeAll();
                myFrame.dispose();
            
                out();
            }
        }
        );
    }
    
    public static void main(String[] args) {
        
        JLabel abc=new JLabel("Number of processes:");
        final JComboBox bcd=new JComboBox(inp);
        bcd.setSelectedIndex(-1);
        JPanel cde=new JPanel(new GridLayout(1,2,10,10));
        cde.add(abc);
        cde.add(bcd);
        cde.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        myFrame.add(cde);
        
        myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        myFrame.pack();
        myFrame.setLocationByPlatform(true);
        myFrame.setResizable(false);
        myFrame.setVisible(true);
        
        bcd.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                x=bcd.getSelectedIndex()+1;
                myFrame.getContentPane().removeAll();
                myFrame.dispose();
                starter();
            }
        }
        );
    }
    
}