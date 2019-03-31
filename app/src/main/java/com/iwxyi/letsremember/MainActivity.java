package com.iwxyi.letsremember;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.iwxyi.letsremember.Fragments.MineFragment;
import com.iwxyi.letsremember.Fragments.HomeFragment;
import com.iwxyi.letsremember.Fragments.TypeinFragment;
import com.iwxyi.letsremember.Globals.App;
import com.iwxyi.letsremember.Globals.Def;
import com.iwxyi.letsremember.Globals.User;
import com.iwxyi.letsremember.Users.LoginActivity;
import com.iwxyi.letsremember.Utils.DateTimeUtil;
import com.iwxyi.letsremember.Utils.FileUtil;

public class MainActivity extends AppCompatActivity {

    private HomeFragment homeFragment;
    private TypeinFragment typeinFragment;
    private MineFragment groupFragment;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            hideFragment(ft);
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    if (homeFragment == null) {
                        homeFragment = new HomeFragment();
                        ft.add(R.id.frame_layout, homeFragment, "home");
                    } else {
                        ft.show(homeFragment);
                    }
                    break;
                case R.id.navigation_typein:
                    if (typeinFragment == null) {
                        typeinFragment = new TypeinFragment();
                        ft.add(R.id.frame_layout, typeinFragment, "typein");
                    } else {
                        ft.show(typeinFragment);
                    }
                    break;
                case R.id.navigation_mine:
                    if (groupFragment == null) {
                        groupFragment = new MineFragment();
                        ft.add(R.id.frame_layout, groupFragment, "group");
                    } else {
                        ft.show(groupFragment);
                    }
                    break;
                default :
                    return false;
            }
            ft.commit();
            return true;
        }
    };

    private void hideFragment(FragmentTransaction ft) {
        if (homeFragment != null) {
            ft.hide(homeFragment);
        }
        if (typeinFragment != null) {
            ft.hide(typeinFragment);
        }
        if (groupFragment != null) {
            ft.hide(groupFragment);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initApplicationFirstUse();
    }

    private void initView() {
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        initFragment();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        if (!User.isLogin() && App.getInt("user_id") != 0) {
            startActivityForResult(new Intent(MainActivity.this, LoginActivity.class), Def.code_login);
        }

        if (App.getInt("firstOpen") == 0) {
            App.setVal("firstOpen", DateTimeUtil.getTimestamp());
        }
    }

    private void initFragment() {
        FragmentManager fm = getSupportFragmentManager();
        homeFragment = new HomeFragment();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.frame_layout, homeFragment, "home").commit();
    }


    private void initApplicationFirstUse() {
        if (!FileUtil.ensureFolder()) {
            App.deb("初始化数据失败");
            return;
        }
        if (FileUtil.exist(("material"))) // 已经初始化过了
            return ;
        FileUtil.ensureFolder("material");
        FileUtil.ensureFolder("material/index");
        FileUtil.ensureFile("material/index/index.txt");
        FileUtil.writeTextVals("material/index/index.txt", "<chapter>\n\t<positive>\n\t\t<content>\n\t\t\t内容（正面）\n\t\t</content>\n\t\t<description>\n\t\t\t描述\n\t\t</description>\n\t\t<hides>\n\t\t\t\n\t\t</hides>\n\t</positive>\n\t<reverse>\n\t\t<content>\n\t\t\t内容（反面）\n\t\t</content>\n\t\t<description>\n\t\t\t描述\n\t\t</description>\n\t\t<hides>\n\t\t\t\n\t\t</hides>\n\t</reverse>\n</chapter>");
    }
}
