package com.pt.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.pt.dao.WeatherDao;
import com.pt.entity.CityInfo;
import com.pt.service.NetService;
import com.pt.service.WeatherService;
import com.pt.view.WeatherTrendView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    private static final int LOADED_PROVINCE = 1;
    private static final int LOADED_CITY = 2;
    private static final int LOADED_WEATHER = 3;
    private static final  int LOADED_AUTO = 4;
    private static final int LOADED_FAILURE = 0;

    private boolean isdb_province= false;
    private boolean isdb_city = false;
    private boolean isdb_weather = false;
    //判断
    private ConnectivityManager connManager;
    private WeatherTrendView trendView ;

    private ImageButton ib_location;
    private TextView city_name;
    private ImageButton ib_setting;

    private Dialog dialog;
    private RadioGroup radioGroup;

    private RadioButton rb_auto;
    private RadioButton rb_manual;

    private Spinner spinner_province;
    private Spinner spinner_city;

    private Button btn_cancel;
    private Button btn_ok;

    private List<String> province_datas;
    private List<String> city_datas;

    private ArrayAdapter<String> province_adapter;
    private ArrayAdapter<String> city_adapter;

    private WeatherDao weatherDao;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case LOADED_FAILURE:
                    Toast.makeText(MainActivity.this,R.string.network_error,Toast.LENGTH_SHORT).show();
                    break;
                case LOADED_CITY:       //加载城市
                    Log.i(TAG, "handleMessage: " + city_datas.toString());
                    final int position = msg.arg1;
                    if(!isdb_city && city_datas != null && city_datas.size() != 0 ) {
                        Log.i(TAG, "handleMessage: isdb_ctiy false");
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                weatherDao.deleteCityByProvince(province_datas.get(position));
                                weatherDao.addcityName(province_datas.get(position), city_datas);
                                List<String> city_test = weatherDao.showCityByProvince(province_datas.get(position));
                                for (int i = 0; i < city_test.size(); i++) {
                                    Log.i(TAG, "run: " + city_test.get(i));
                                }
                            }
                        }).start();
                    }
                    spinner_city.setAdapter(city_adapter);
                    break;
                case LOADED_PROVINCE:   //加载省份
                    Log.i(TAG, "handleMessage: " + province_datas.toString());
                    if(!isdb_province && province_datas != null && province_datas.size() != 0) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                weatherDao.deleteProvince();
                                weatherDao.addProvince(province_datas);
                                List<String> province_test = weatherDao.showProvince();
                                for (int i = 0; i < province_test.size(); i++) {
                                    Log.i(TAG, "run: " + province_test.get(i));
                                }
                             }
                        }).start();
                     }
                    spinner_province.setAdapter(province_adapter);
                    break;
                case LOADED_WEATHER:    //加载天气
                    final CityInfo cityInfo = (CityInfo) msg.obj;
                    if(!isdb_weather && cityInfo.getCityName() != null) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                //删除已存在的信息，添加新的加载数据
                                weatherDao.deleteDataByCityName(cityInfo.getCityName());
                                weatherDao.addCityInfo(cityInfo);
                            }
                        }).start();
                    }
                    trendView.setCityInfo(cityInfo);
                    trendView.invalidate();
                    city_name.setText(spinner_province.getSelectedItem().toString() + "  " + cityInfo.getCityName());
                    break;
                case LOADED_AUTO:       //自动定位

                    CityInfo auto_cityInfo = (CityInfo) msg.obj;
                    trendView.setCityInfo(auto_cityInfo);
                    trendView.invalidate();

                    Bundle bundle = msg.getData();
                    city_name.setText(bundle.getString("city"));
                    dialog.dismiss();
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        province_datas = new ArrayList<>();
        trendView = (WeatherTrendView) findViewById(R.id.trend_view);
        ib_location = (ImageButton) findViewById(R.id.ib_location);
        city_name = (TextView) findViewById(R.id.city_name);
        ib_setting = (ImageButton) findViewById(R.id.ib_setting);
        ib_location.setOnClickListener(this);
        ib_setting.setOnClickListener(this);
        weatherDao = new WeatherDao(this);
        //开启数据
        weatherDao.open();
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_location:
                startDialog();
                break;
            case R.id.ib_setting:
                Toast.makeText(MainActivity.this,R.string.setting,Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_cancel:
                dialog.dismiss();
                break;
            case R.id.btn_ok:
                loadWeatherByCity();
                dialog.dismiss();
                break;
        }

    }

    private void loadWeatherByCity() {
        if (rb_auto.isChecked()) {  //自动定位
            //开启线程访问网络得到城市名
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String city = NetService.getCity();
                    if(city == null) {
                        handler.sendEmptyMessage(LOADED_FAILURE);
                    }else{
                        CityInfo cityInfo = WeatherService.getWeather(city.split(" ")[1].toString());
                        if(cityInfo != null) {
                            Message msg = new Message();
                            Bundle bundle = new Bundle();
                            bundle.putString("city", city);
                            msg.setData(bundle);
                            msg.obj = cityInfo;
                            msg.what = LOADED_AUTO;
                            handler.sendMessage(msg);
                        }
                    }

                }
            }).start();
        } else if (rb_manual.isChecked()) {//手动选择
            if (spinner_city.getSelectedItem() == null) {
                    Toast.makeText(MainActivity.this, R.string.waitTotry, Toast.LENGTH_SHORT).show();
                return;
            }
            final String cityName = spinner_city.getSelectedItem().toString();
            final String province = spinner_province.getSelectedItem().toString();
            if (cityName == null || "".equals(cityName)) {
                Toast.makeText(MainActivity.this, R.string.error, Toast.LENGTH_SHORT).show();
            } else {

                Log.i(TAG, "onClick: " + cityName);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final CityInfo cityInfo;
                        if(!isNetWorkAvailable()) {
                            Log.i(TAG, "run: " + province);
                            cityInfo = weatherDao.showCityInfo(cityName);
                            isdb_weather = true;
                        }else {
                            cityInfo = WeatherService.getWeather(cityName);
                            isdb_weather = false;
                        }

                        if (cityInfo == null ) {
                            handler.sendEmptyMessage(LOADED_FAILURE);
                        }else{
                            Message msg = new Message();
                            msg.obj = cityInfo;
                            msg.what = LOADED_WEATHER;
                            handler.sendMessage(msg);
                        }
                    }
                }).start();

            }
        }
    }


    /**
     * 开启对话框效果
     */
    private void startDialog() {

        dialog = new Dialog(this);
        dialog.setTitle("城市选择");
        dialog.setCancelable(false);
        //对话框布局设置
        dialog.setContentView(R.layout.layout_dialog);
        dialog.show();

        initView(dialog);
        //默认手动设置为false，即不开启spinner效果
        spinner_province.setEnabled(false);
        spinner_city.setEnabled(false);
        //当选中变化时
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.rb_auto){
                    spinner_province.setEnabled(false);
                    spinner_city.setEnabled(false);
                }else if(checkedId == R.id.rb_manual){
                    spinner_city.setEnabled(true);
                    spinner_province.setEnabled(true);
                }
            }
        });
        loadProvince();
        //第五步：为下拉列表设置各种事件的响应，这个事响应菜单被选中
        spinner_province.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {
                Log.i(TAG, "onItemSelected: " + province_datas.get(position));
                loadCity(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btn_ok.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
    }

    /**
     * 加载城市
     * @param position
     */
    private void loadCity(final int position) {

        //加载对应省份的城市信息
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "run: start");
                if(!isNetWorkAvailable()){  //数据库中获取
                    Log.i(TAG, "run: " + "!isNetWorkAvailable()");
                    city_datas = weatherDao.showCityByProvince(province_datas.get(position));
                    isdb_city = true;
                }else {//从网络中获取并存入数据库中
                    Log.i(TAG, "run: " + "isNetWorkAvailable");
                    city_datas = WeatherService.getCityList(province_datas.get(position));
                    isdb_city = false;
                }
                if(city_datas == null||city_datas.size() == 0){
                    handler.sendEmptyMessage(LOADED_FAILURE);
                }else{
                    Log.i(TAG, "run: " + city_datas.toString());
                    city_adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, city_datas);
                    Message msg = new Message();
                    msg.arg1 = position;
                    msg.what = LOADED_CITY;
                    handler.sendMessage(msg);
                }
            }
        }).start();

    }

    /**
     * 加载省份
     */
    private void loadProvince() {
        //开启线程操作
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(isNetWorkAvailable()){   //如果有网络则从网络中获取
                    Log.i(TAG, "run: 1" );
                    province_datas = WeatherService.getProvinceList();
                    isdb_province = false;
                }else{      //没有则从数据库中获取
                    province_datas = weatherDao.showProvince();
                    isdb_province = true;
                }

                //判断是否有数据
                if(province_datas == null || province_datas.size() == 0){
                    handler.sendEmptyMessage(LOADED_FAILURE);
                }else{//有则利用handler更新UI
                    Log.i(TAG, "run: 4");
                    province_adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_item, province_datas);
                    //第三步：为适配器设置下拉列表下拉时的菜单样式。
                    province_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    handler.sendEmptyMessage(LOADED_PROVINCE);
                    Log.i(TAG, "run: 5" );
                }
            }
        }).start();

    }

    /**
     * 初始对话框中的View
     * @param dialog
     */
    private void initView(Dialog dialog) {
        radioGroup = (RadioGroup) dialog.findViewById(R.id.radio_group);
        rb_auto = (RadioButton) dialog.findViewById(R.id.rb_auto);
        rb_manual = (RadioButton) dialog.findViewById(R.id.rb_manual);
        spinner_province = (Spinner) dialog.findViewById(R.id.spinner_province);
        spinner_city = (Spinner) dialog.findViewById(R.id.spinner_city);
        btn_cancel = (Button) dialog.findViewById(R.id.btn_cancel);
        btn_ok = (Button) dialog.findViewById(R.id.btn_ok);
    }

    /**
     * 判断当前网络是否可用
     *
     * @return
     */
    public boolean isNetWorkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager != null) {
            NetworkInfo info = manager.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        weatherDao.close();
    }

    /**
     * 根据天气判断背景图片的显示
     */
    private void loadBackground() {
    }
}
