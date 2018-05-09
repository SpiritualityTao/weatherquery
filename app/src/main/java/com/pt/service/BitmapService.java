package com.pt.service;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.pt.activity.R;

/**
 * Created by 韬 on 2016-05-28.
 */
public class BitmapService {

    public static Bitmap getBitmap(Resources resources, String picId){
        Bitmap bitmap = null;
        if("0.gif".equals(picId)){
            bitmap = BitmapFactory.decodeResource(resources,R.drawable.a_0);
        }else  if("1.gif".equals(picId)){
            bitmap = BitmapFactory.decodeResource(resources,R.drawable.a_1);
        }else if("2.gif".equals(picId)){
            bitmap = BitmapFactory.decodeResource(resources,R.drawable.a_2);
        }else if("3.gif".equals(picId)){
            bitmap = BitmapFactory.decodeResource(resources,R.drawable.a_3);
        }else if("4.gif".equals(picId)){
            bitmap = BitmapFactory.decodeResource(resources,R.drawable.a_4);
        }else if("5.gif".equals(picId)){
            bitmap = BitmapFactory.decodeResource(resources,R.drawable.a_5);
        }else if("6.gif".equals(picId)){
            bitmap = BitmapFactory.decodeResource(resources,R.drawable.a_6);
        }else if("7.gif".equals(picId)){
            bitmap = BitmapFactory.decodeResource(resources,R.drawable.a_7);
        }else if("8.gif".equals(picId)){
            bitmap = BitmapFactory.decodeResource(resources,R.drawable.a_8);
        }else if("9.gif".equals(picId)){
            bitmap = BitmapFactory.decodeResource(resources,R.drawable.a_9);
        }else if("10.gif".equals(picId)){
            bitmap = BitmapFactory.decodeResource(resources,R.drawable.a_10);
        }else if("11.gif".equals(picId)){
            bitmap = BitmapFactory.decodeResource(resources,R.drawable.a_11);
        }else if("12.gif".equals(picId)){
            bitmap = BitmapFactory.decodeResource(resources,R.drawable.a_12);
        }else if("13.gif".equals(picId)){
            bitmap = BitmapFactory.decodeResource(resources,R.drawable.a_13);
        }else if("14.gif".equals(picId)){
            bitmap = BitmapFactory.decodeResource(resources,R.drawable.a_14);
        }else if("15.gif".equals(picId)){
            bitmap = BitmapFactory.decodeResource(resources,R.drawable.a_15);
        }else if("16.gif".equals(picId)){
            bitmap = BitmapFactory.decodeResource(resources,R.drawable.a_16);
        }else if("17.gif".equals(picId)){
            bitmap = BitmapFactory.decodeResource(resources,R.drawable.a_17);
        }else if("18.gif".equals(picId)){
            bitmap = BitmapFactory.decodeResource(resources,R.drawable.a_18);
        }else if("19.gif".equals(picId)){
            bitmap = BitmapFactory.decodeResource(resources,R.drawable.a_19);
        }else if("20.gif".equals(picId)){
            bitmap = BitmapFactory.decodeResource(resources,R.drawable.a_20);
        }else if("21.gif".equals(picId)){
            bitmap = BitmapFactory.decodeResource(resources,R.drawable.a_21);
        }else if("22.gif".equals(picId)){
            bitmap = BitmapFactory.decodeResource(resources,R.drawable.a_22);
        }else if("23.gif".equals(picId)){
            bitmap = BitmapFactory.decodeResource(resources,R.drawable.a_23);
        }else if("24.gif".equals(picId)){
            bitmap = BitmapFactory.decodeResource(resources,R.drawable.a_24);
        }else if("25.gif".equals(picId)){
            bitmap = BitmapFactory.decodeResource(resources,R.drawable.a_25);
        }else if("26.gif".equals(picId)){
            bitmap = BitmapFactory.decodeResource(resources,R.drawable.a_26);
        }else if("27.gif".equals(picId)){
            bitmap = BitmapFactory.decodeResource(resources,R.drawable.a_27);
        }else if("28.gif".equals(picId)){
            bitmap = BitmapFactory.decodeResource(resources,R.drawable.a_28);
        }else if("29.gif".equals(picId)){
            bitmap = BitmapFactory.decodeResource(resources,R.drawable.a_29);
        }else if("30.gif".equals(picId)){
            bitmap = BitmapFactory.decodeResource(resources,R.drawable.a_30);
        } if("31.gif".equals(picId)){
            bitmap = BitmapFactory.decodeResource(resources,R.drawable.a_31);
        }
        return bitmap;
    }
}
