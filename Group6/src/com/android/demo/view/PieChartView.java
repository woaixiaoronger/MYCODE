package com.android.demo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Paint.FontMetrics;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;


/**
 * Բ��ͼ
 */
public class PieChartView extends View{

	private static final String tag = "PieChartView";

	/**����ɫ backcolor*/
    private int backColor = Color.TRANSPARENT;
    
    private int piePaddingLeft = 15;
    private int piePaddingTop = 15;
    private int piePaddingRight = 15;
    private int piePaddingBottom = 15;
    private int specialSpace = 10;
    
    private int rightSpace = 100;
    
    /**���� the data*/
    private float[] data = null;
    /**ÿ�����ݶ�Ӧ�ı��� the data title*/
    private String[] title = null;
    /**ȱʡ������ɫ*/
    private int defColor = Color.GREEN;
    /**���ݵ���ɫ data color*/
    private int[] color = null;
    /**���ݺ�*/
    private float sumData = 0;
    /**���ݸ���*/
    private int dataCount = 0;
    /**��ע��������*/
    private int specialIndex = -1;
    /**��ʼ���ƽǶ�*/
    private float startAngle = 30;
    
    private int barWidth = 15;
    
    private int textColor = 0xaa333333;
    
    public PieChartView(Context context){
        super(context);
    }
    
    public PieChartView(Context context, AttributeSet attrs){
        super(context,attrs);
    }
    
    public PieChartView(Context context, AttributeSet attrs, int defStyle){
        super(context,attrs,defStyle);
        
    }
    
    /**
     * ָ��һ�������Ƿ�Ҫ��ע
     * ��һ�����ݹ�עʱ��������������з��뿪��
     * @param index ���ݶ�Ӧ��index����0��ʼ
     */
    public void setSpecial(int index){
        if(data != null && dataCount > index){
            specialIndex = index;
        }
    }
    
    /**
     * �������ݸ���
     * @param count
     */
    public void setDataCount(int count){
        if(count > 0){
            data = new float[count];
            title = new String[count];
            dataCount = count;
            color = new int[count];
            for(int i = 0; i < count; i++){
                color[i] = defColor;
            }
        }
        Log.i(tag, "����:"+count);
    }
    
    /**
     * ��������
     * @param d
     */
    public void setData(float[] d){
    	Log.i(tag, "������");
        if(d != null && d.length == dataCount){
            for(int i = 0; i < dataCount; i++){
                sumData += d[i];
                Log.i(tag, "��Ϊ��"+d[i]);
            }
            data = d;
        }
    }
    
    /**
     * ����һ������
     * @param index ����index����0��ʼ
     * @param d
     */
    public void setData(int index,float d){
        if(data != null && dataCount > index){
            sumData -= data[index];
            data[index] = d;
            sumData += d;
        }
    }
    
    /**
     * �������ݱ���
     * @param desc
     */
    public void setDataTitle(String[] desc){
        if(desc != null && dataCount == desc.length){
            title = desc;
        }
    }
    
    /**
     * ����һ�����ݱ���
     * @param index ����index����0��ʼ
     * @param desc
     */
    public void setDataTitle(int index,String desc){
        if(title != null && dataCount > index){
            title[index] = desc;
        }
    }
    
    /**
     * ����������ɫ
     * @param c
     */
    public void setColor(int[] c){
        if(color != null && c.length == dataCount){
            color = c;
        }
        Log.i(tag, "��ɫ����"+c.length);
    }
    
    /**
     * ����һ��������ɫ
     * @param index ����index����0��ʼ
     * @param c
     */
    public void setColor(int index,int c){
        if(color != null & dataCount > index){
            color[index] = c;
        }
    }
    
    public void setBackgroundColor(int color){
        backColor = color;
    }
    
    protected void onDraw(Canvas canvas) {
    	Log.i(tag, "��ʼ����");
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();
        
        int height = getHeight() - paddingTop - paddingBottom;
        int width = getWidth() - paddingLeft - paddingRight ;
        
        if(data != null){
        	Log.i(tag, "��ȡ������");
            canvas.save();
            canvas.translate(paddingLeft, paddingTop);
            canvas.clipRect(0,0,width,height);
            
            canvas.drawColor(backColor);
            
            int w = width - piePaddingLeft - piePaddingRight - rightSpace;
            int h = height - piePaddingTop - piePaddingBottom;
            
            int r = w;
            if(w > h)
                r = h;
            
            RectF rf = new RectF(piePaddingLeft,piePaddingTop,piePaddingLeft + r,piePaddingTop + r);
            
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setStyle(Paint.Style.FILL);
            
            float ang = startAngle;
            
            float[] percent = new float[dataCount];
            
            for(int i = 0; i < data.length; i++){
                paint.setColor(color[i]);
                float tmp = data[i] / (sumData * 1.0f);
                percent[i] = tmp;
                tmp = tmp * 360;
                float toang = Math.round(tmp);
     
                if(specialIndex == i){
                    float ds = (ang + toang / 2) ;
                    float dy = (float)Math.abs((specialSpace * Math.sin(ds*0.01745)));
                    float dx = (float)Math.abs((specialSpace * Math.cos(ds*0.01745)));
                    if(ds > 0 && ds <= 90){
                        
                    }else if(ds > 90 && ds <=180){
                        dx = dx * (-1);
                    }else if(ds > 180 && ds <= 270){
                        dx = dx * (-1);
                        dy = dy * (-1);
                    }else if(ds > 270){
                        dy = dy * (-1);
                    }
                    RectF sf = new RectF(piePaddingLeft +dx,piePaddingTop + dy,piePaddingLeft +dx + r,piePaddingTop +r + dy );
                    canvas.drawArc(sf, ang,toang,true,paint);
                }else
                    canvas.drawArc(rf, ang,toang,true,paint);        
                ang += toang;
            }
            
            FontMetrics fm = paint.getFontMetrics();
            float texty = piePaddingTop  - fm.ascent;
            float textx = piePaddingLeft + r + 35;
            for(int i = 0; i < dataCount; i++){
                paint.setColor(color[i]);
                canvas.drawRect(textx, texty, textx +barWidth,texty+ barWidth, paint);
                paint.setColor(textColor);
                paint.setTextSize(20);
                canvas.drawText(String.format("%.1f%%", percent[i] * 100), textx + barWidth + 10, texty - fm.ascent, paint);
                
                texty += fm.descent - fm.ascent + 15;
            }
            
            canvas.restore();
        }else{
        	Log.i(tag, "û��");
        }
    }
    
    
    
}