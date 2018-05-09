package com.pt.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.pt.entity.CityInfo;
import com.pt.entity.WeatherInfo;
import com.pt.service.BitmapService;

import java.text.DecimalFormat;
import java.util.List;

/**
 * 自定义天气趋势View
 * Created by 韬 on 2016-05-26.
 */
public class WeatherTrendView extends View {

    private static final String TAG = "WeatherTrendView";

    private Context mContext;
    private CityInfo cityInfo;
    private Bitmap[] bitmaps;
    private Point[] points ;
    private String[] text;
    private int textColor = 0xffffffff;
    private Paint paint;
    private Paint mBitPaint;
    private Paint textPaint;
    public WeatherTrendView(Context context) {
        this(context,null);
    }

    public WeatherTrendView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public WeatherTrendView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    public CityInfo getCityInfo() {
        return cityInfo;
    }

    public void setCityInfo(CityInfo cityInfo) {
        this.cityInfo = cityInfo;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //判断是否有城市信息
        if(cityInfo == null)
            return;
        Log.i(TAG, "onDraw: " + cityInfo.toString());
        int height = getHeight();
        int width = getWidth();
        Log.i(TAG, "onDraw: height=" + height + ",width=" + width);

        int marginlr = dp2px(35);
        //绘制天气趋势区域的最高点
        int topY = dp2px(150);
        //绘制天气趋势区域最低点height - marginb
        int bottomY = height - dp2px(150);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.parseColor("#a67070"));
        paint.setStrokeWidth(2);
        paint.setStyle(Paint.Style.STROKE);

        textPaint = new Paint();
        textPaint.setAlpha(0x0000ff);
        textPaint.setStrokeWidth(3);
        textPaint.setTextSize(sp2px(14));
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setColor(textColor);

        mBitPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBitPaint.setFilterBitmap(true);
        mBitPaint.setDither(true);
        canvas.drawLine(0,0,width,0,paint);
        drawTextAboutToday(canvas);
//        canvas.drawLine(marginlr,bottomY,width-marginlr,bottomY,paint);
        Log.i(TAG, "onDraw: " );

        //每天Ave Tem 的坐标
        points = getPoints(marginlr,topY,bottomY);
        bitmaps = new Bitmap[5];
        text = new String[5];
        initBitmaps();
        //绘制点和直线
        drawPointsAndLine(points,canvas,paint);
        //绘制图片Bitmap
        drawBitmap(canvas,bottomY);

    }

    private void drawTextAboutToday(Canvas canvas) {
        String airQuality = cityInfo.getAirQuality();
        String tem = cityInfo.getTodayInfo().split("；")[0].split("：")[2].toString() ;
        String wind = cityInfo.getTodayInfo().split("；")[1].split("：")[1].toString();
        String time = cityInfo.getTime();
        Paint textPaint1 = new Paint();
        textPaint1.setAlpha(0xffffff);
        textPaint1.setStrokeWidth(3);
        textPaint1.setTextSize(sp2px(40));
        textPaint1.setTextAlign(Paint.Align.CENTER);
        textPaint1.setColor(textColor);
        canvas.drawText(tem,getWidth() / 3,dp2px(70),textPaint1);
        textPaint1.setTextSize(sp2px(14));
        canvas.drawText(airQuality,getWidth() / 3,dp2px(100),textPaint1 );
        canvas.drawText(wind,getWidth()/3 * 2 , dp2px(100),textPaint1);
        canvas.drawText(time,getWidth()/3 * 2 , dp2px(70),textPaint1);
    }

    private void initBitmaps() {
        List<WeatherInfo> weatherInfos = cityInfo.getWeatherInfos();

        for (int i = 0; i < weatherInfos.size(); i++) {
            bitmaps[i] = BitmapService.getBitmap(this.getResources(),weatherInfos.get(i).getPicId());
            text[i] = weatherInfos.get(i).getDate();
        }
    }

    /**
     * 绘制5天内对应天气地图片
     * @param canvas
     */
    private void drawBitmap(Canvas canvas,int bottomY) {


        Rect srcRect = new Rect(0,0,dp2px(70),dp2px(65));
        for (int i = 0; i < bitmaps.length; i++) {
            Log.i(TAG, "drawBitmap: " + bitmaps[i].toString());
            canvas.drawBitmap(bitmaps[i],srcRect,new Rect(points[i].x-dp2px(35),bottomY+dp2px(30) ,points[i].x+dp2px(35),bottomY + dp2px(95)),mBitPaint);
            canvas.drawText(text[i],points[i].x,bottomY + dp2px(120),textPaint);
            canvas.drawText(cityInfo.getWeatherInfos().get(i).getWeatherDes(),points[i].x,bottomY+dp2px(140),textPaint);
        }

    }

    /**
     * 绘制
     * @param points
     * @param canvas
     * @param paint
     */
    private void drawPointsAndLine(Point[] points, Canvas canvas, Paint paint) {
        //画笔
        paint.setColor(Color.parseColor("#fffc00d1"));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(8);
        Rect srcRect = new Rect(0,0,70,65);
        Point startP  ;
        Point endP ;
        for (int i = 0; i < points.length - 1; i++) {
            startP = points[i];
            endP = points [i + 1];
            canvas.drawLine(startP.x,startP.y,endP.x,endP.y,paint);
        }

        //画点
        paint.setColor(Color.parseColor("#28bbff"));
        paint.setStyle(Paint.Style.FILL);
        for (int i = 0; i < points.length; i++) {
            canvas.drawCircle(points[i].x,points[i].y,12,paint);
            String maxText = new DecimalFormat("0.#").format(cityInfo.getWeatherInfos().get(i).getMaxTem());
            String minText = new DecimalFormat("0.#").format(cityInfo.getWeatherInfos().get(i).getMinTem());

            canvas.drawText(maxText, points[i].x, points[i].y-dp2px(15),textPaint);
            canvas.drawText(minText, points[i].x, points[i].y+dp2px(22),textPaint);
        }
    }

    /**
     * 得到天气信息点的坐标
     *
     * @param marginlr
     * @param topY
     * @param bottomY
     * @return
     */
    private Point[] getPoints( int marginlr, int topY, int bottomY) {
        Point[] points = new Point[5];
        int realWidth = getWidth() - 2 * marginlr;
        int realHeight = bottomY - topY;
        //每天宽度
        int widthPerDay = realWidth / 4;
        //每摄氏度的高度
        int heightPerC = realHeight / (cityInfo.getAveMax() - cityInfo.getAveMin());
        //得到5天的天气信息集合
        List<WeatherInfo> weatherInfos = cityInfo.getWeatherInfos();
        //遍历当天且温度信息
        for (int i = 0; i < weatherInfos.size(); i++) {
            points[i] = new Point();
            points[i].x = marginlr + widthPerDay * i;
            //当天的平均气温
            int aveTem = (weatherInfos.get(i).getMaxTem() + weatherInfos.get(i).getMinTem()) / 2;
            //y坐标等于topY + 平均温差 * 每摄氏度高度
            points[i].y = topY + (cityInfo.getAveMax() - aveTem) * heightPerC;
            Log.i(TAG, "getPoints: " + points[i].x + "," + points[i].y);
        }
        return points;
    }


    /**
     * dp to px
     * @param dpVal
     * @return
     */
    private int dp2px(int dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dpVal,
                getResources().getDisplayMetrics());
    }

    /**
     * sp to px
     * @param spVal
     * @return
     */
    private int sp2px(int spVal){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spVal,
                getResources().getDisplayMetrics());
    }


}
