package rpt.tool.mementobibere.java.userinfo;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.viewpager.widget.ViewPager;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;
import rpt.tool.mementobibere.MainActivity;
import rpt.tool.mementobibere.R;
import rpt.tool.mementobibere.java.userinfo.custom.NonSwipeableViewPager;
import rpt.tool.mementobibere.utils.managers.SharedPreferencesManager;
import rpt.tool.mementobibere.utils.view.custom.adapter.InitUserInfoPagerAdapter;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import androidx.core.content.ContextCompat;

public class InitUserInfoActivity extends MasterBaseAppCompatActivity {
    NonSwipeableViewPager viewPager;
    InitUserInfoPagerAdapter initUserInfoPagerAdapter;
    DotsIndicator dots_indicator;

    LinearLayout btn_back, btn_next;
    View space;
    AppCompatTextView lbl_next;

    int current_page_idx = 0;
    int max_page = 7;

    private static final int ALL_PERMISSION = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init_user_info);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(mContext.getResources().getColor(R.color.water_color));
        }

        FindViewById();
        Body();
    }

    public void FindViewById() {
        viewPager = findViewById(R.id.viewPager);
        dots_indicator = findViewById(R.id.dots_indicator);

        btn_back = findViewById(R.id.btn_back);
        btn_next = findViewById(R.id.btn_next);
        lbl_next = findViewById(R.id.lbl_next);
        space = findViewById(R.id.space);

        dots_indicator.setDotsClickable(false);
    }

    public void Body() {
        initUserInfoPagerAdapter = new InitUserInfoPagerAdapter(getSupportFragmentManager(), mContext);
        viewPager.setAdapter(initUserInfoPagerAdapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                current_page_idx = position;

                if (position == 0) {
                    btn_back.setVisibility(View.GONE);
                    space.setVisibility(View.GONE);
                } else {
                    btn_back.setVisibility(View.VISIBLE);
                    space.setVisibility(View.VISIBLE);
                }

                if (position == max_page - 1) {
                    lbl_next.setText(sh.get_string(R.string.get_started));
                } else {
                    lbl_next.setText(sh.get_string(R.string.str_next));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        viewPager.setOffscreenPageLimit(10);

        dots_indicator.setViewPager(viewPager);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (current_page_idx > 0) ;
                current_page_idx -= 1;

                viewPager.setCurrentItem(current_page_idx);
            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ah.customAlert(""+ph.getString(URLFactory.USER_NAME));

                if (current_page_idx == 0) {
                    if (sh.check_blank_data(SharedPreferencesManager.INSTANCE.getUserName())) {
                        ah.customAlert(sh.get_string(R.string.please_input_a_valid_name));
                        return;
                    }

                    if (SharedPreferencesManager.INSTANCE.getUserName().length() < 3) {
                        ah.customAlert(sh.get_string(R.string.please_input_a_valid_name_length));
                        return;
                    }
                }
                if (current_page_idx == 1) {
                    try {
                        if (sh.check_blank_data(SharedPreferencesManager.INSTANCE.getHeight())) {
                            ah.customAlert(sh.get_string(R.string.please_input_a_valid_height));
                            return;
                        }

                        if (sh.check_blank_data(String.valueOf(SharedPreferencesManager.INSTANCE.getWeightS()))) {
                            ah.customAlert(sh.get_string(R.string.please_input_your_weight));
                            return;
                        }
                        if(Float.parseFloat(SharedPreferencesManager.INSTANCE.getWeightS()) > getMaxWeight(SharedPreferencesManager.INSTANCE.getWeightUnit()) ||
                                Float.parseFloat(SharedPreferencesManager.INSTANCE.getWeightS()) < getMinWeight(SharedPreferencesManager.INSTANCE.getWeightUnit())){
                            ah.customAlert(sh.get_string(R.string.please_input_a_valid_weight));
                        }

                        float val = Float.parseFloat("" + SharedPreferencesManager.INSTANCE.getHeight());
                        if (val < 2) {
                            ah.customAlert(sh.get_string(R.string.please_input_a_valid_height));
                            return;
                        }

                        float val2 = Float.parseFloat("" + SharedPreferencesManager.INSTANCE.getWeightS());
                        if (val2 < 30) {
                            ah.customAlert(sh.get_string(R.string.please_input_a_valid_weight));
                            return;
                        }
                    } catch (Exception e) {
                    }
                }

                if (current_page_idx == 5) {
                    if (sh.check_blank_data(String.valueOf(SharedPreferencesManager.INSTANCE.getWakeUpTimeS()))
                            || sh.check_blank_data(String.valueOf(SharedPreferencesManager.INSTANCE.getSleepingTimeS()))) {
                        ah.customAlert(sh.get_string(R.string.please_input_a_valid_sleep_bed_time));
                        return;
                    } else if (SharedPreferencesManager.INSTANCE.getIgnoreNextStep()) {
                        ah.customAlert(sh.get_string(R.string.please_input_a_valid_sleep_bed_time));
                        return;
                    }
                }

                if (current_page_idx < max_page - 1) {
                    current_page_idx += 1;
                    viewPager.setCurrentItem(current_page_idx);
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        checkStoragePermissions();
                    } else {
                        gotoHomeScreen();
                    }
                }
            }
        });
    }

    public void checkStoragePermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(act, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(act, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, ALL_PERMISSION);
            } else {
                gotoHomeScreen();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case ALL_PERMISSION:
                /*if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                }
                else
                {
                }*/

                gotoHomeScreen();

                break;
        }
    }

    public void gotoHomeScreen() {
        SharedPreferencesManager.INSTANCE.setFirstRun(false);
        SharedPreferencesManager.INSTANCE.setStartTutorial(true);

        intent = new Intent(act, MainActivity.class);
        startActivity(intent);
        finish();

    }

    public int getMaxWeight(int weightUnit) {
        if(weightUnit == 0){
            return 200;
        }
        else{
            return 441;
        }
    }

    public int getMinWeight(int weightUnit) {
        if(weightUnit == 0){
            return 30;
        }
        else{
            return 66;
        }
    }

    @Override
    public void onBackPressed() {
        if (current_page_idx > 0) {
            current_page_idx -= 1;
            viewPager.setCurrentItem(current_page_idx);
        } else
            super.onBackPressed();
    }
}
