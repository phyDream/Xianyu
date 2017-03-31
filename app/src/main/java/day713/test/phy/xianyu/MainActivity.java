package day713.test.phy.xianyu;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fragment.FourFragment;
import fragment.OneFragment;
import fragment.ThreeFragment;
import fragment.TwoFragment;
import utils.PublicWay;

public class MainActivity extends AppCompatActivity {
    @Bind(R.id.replaceFragment)
    FrameLayout replaceFragment;
    @Bind(R.id.main_rb_xianyu)
    RadioButton mainRbXianyu;
    @Bind(R.id.main_rb_yutang)
    RadioButton mainRbYutang;
    @Bind(R.id.main_rb_msg)
    RadioButton mainRbMsg;
    @Bind(R.id.main_rb_mine)
    RadioButton mainRbMine;
    @Bind(R.id.main_img_fabu)
    ImageView mainImgFabu;
    @Bind(R.id.main_img_xian)
    ImageView mainImgXian;


    private FragmentManager fragmentManager;
    private OneFragment fragment1;
    private TwoFragment fragment2;
    private ThreeFragment fragment3;
    private FourFragment fragment4;
    private PopupWindow popupWindow;
    public static Bitmap bimap ;
    private static final String LOG_TAG = "MainActivity";

    //popupWindow内控件
    RelativeLayout relativeLayout;
    ImageView main_img_pic;
    ImageView main_img_show;
    ImageView main_img_oneKeySell;
    LinearLayout main_lin_oneKeySell;
    LinearLayout main_lin_pic;
    TextView tv1;
    TextView tv2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        PublicWay.activityList.add(this);
        mainImgXian.setAlpha(100);
        initView();

        fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        addFragmrnt(transaction);
        hideFragment(transaction);
        transaction.show(fragment1);
        transaction.commit();
        bimap = BitmapFactory.decodeResource(
                getResources(),
                R.mipmap.ic_launcher);
        //设置发布的点击事件-显示popupWindow
        mainImgFabu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopwindow();
                popupWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);
            }
        });
    }

    //初始化控件
    private void initView() {

    }


    @OnClick({R.id.main_rb_xianyu, R.id.main_rb_yutang, R.id.main_rb_msg, R.id.main_rb_mine})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_rb_xianyu:
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                hideFragment(transaction);
                transaction.show(fragment1);
                transaction.commit();
                break;
            case R.id.main_rb_yutang:
                FragmentTransaction transaction2 = fragmentManager.beginTransaction();
                hideFragment(transaction2);
                transaction2.show(fragment2);
                transaction2.commit();
                break;
            case R.id.main_rb_msg:
                FragmentTransaction transaction3 = fragmentManager.beginTransaction();
                hideFragment(transaction3);
                transaction3.show(fragment3);
                transaction3.commit();
                break;
            case R.id.main_rb_mine:
                FragmentTransaction transaction4 = fragmentManager.beginTransaction();
                hideFragment(transaction4);
                transaction4.show(fragment4);
                transaction4.commit();
                break;
        }
    }


    //隐藏所有fragment
    private void hideFragment(FragmentTransaction transaction) {
        transaction.hide(fragment1);
        transaction.hide(fragment2);
        transaction.hide(fragment3);
        transaction.hide(fragment4);
    }

    //添加所有fragment
    private void addFragmrnt(FragmentTransaction transaction) {
        if (fragment1 == null) {
            fragment1 = new OneFragment();
            transaction.add(R.id.replaceFragment, fragment1);
        } else {
            transaction.hide(fragment1);
        }

        if (fragment2 == null) {
            fragment2 = new TwoFragment();
            transaction.add(R.id.replaceFragment, fragment2);
        } else {
            transaction.hide(fragment2);
        }

        if (fragment3 == null) {
            fragment3 = new ThreeFragment();
            transaction.add(R.id.replaceFragment, fragment3);
        } else {
            transaction.hide(fragment3);
        }
        if (fragment4 == null) {
            fragment4 = new FourFragment();
            transaction.add(R.id.replaceFragment, fragment4);
        } else {
            transaction.hide(fragment4);
        }

    }

    //显示透明效果Popwindow
    private void showPopwindow() {
//        //参数：显示的activity、显示的风格
//        dialog = new Dialog(MainActivity.this, R.style.activity_translucent);
//        dialog.setContentView(R.layout.activity_publish);//设置布局
//        dialog.show();

        View contentView = getLayoutInflater().inflate(R.layout.activity_publish, null, false);
        popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        popupWindow.setAnimationStyle(R.style.dialog_animation);
        popupWindow.setFocusable(true);
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(false);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());// 响应返回键必须的语句
        backgroundAlpha(0.2f);//弹出时，修改背景色

        relativeLayout = (RelativeLayout) contentView.findViewById(R.id.rel);
        main_img_pic = (ImageView) contentView.findViewById(R.id.main_img_pic);
        main_img_show = (ImageView) contentView.findViewById(R.id.main_img_show);
        main_img_oneKeySell = (ImageView) contentView.findViewById(R.id.main_img_oneKeySell);
        main_lin_oneKeySell = (LinearLayout) contentView.findViewById(R.id.main_lin_oneKeySell);
        main_lin_pic = (LinearLayout) contentView.findViewById(R.id.main_lin_pic);
        tv1 = (TextView) contentView.findViewById(R.id.main_tv_pic);
        tv2 = (TextView) contentView.findViewById(R.id.main_tv_oneKeySell);

        //拍照的弹出显示动画
        TranslateAnimation mShowAction1 = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.2f,
                Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT,
                0.2f, Animation.RELATIVE_TO_PARENT, 0.0f);//向左上
        ScaleAnimation scaleanimation1 = new ScaleAnimation(0.5f, 1f, 0.5f, 1f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f);//放大
        AlphaAnimation alphaAnimation1 = new AlphaAnimation(0.0f, 1.0f);//创建透明度动画的对象
        alphaAnimation1.setDuration(1000);
        AnimationSet animationSet1 = new AnimationSet(true);
        animationSet1.setDuration(300);
        animationSet1.addAnimation(scaleanimation1);
        animationSet1.addAnimation(mShowAction1);
        main_lin_pic.startAnimation(animationSet1);
        main_lin_pic.setVisibility(View.VISIBLE);
        tv1.startAnimation(alphaAnimation1);//设置字体的渐变显示
        tv1.setVisibility(View.VISIBLE);

        //一键转卖的弹出显示动画
        TranslateAnimation mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, -0.2f,
                Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT,
                0.2f, Animation.RELATIVE_TO_PARENT, 0.0f);
        ScaleAnimation scaleanimation = new ScaleAnimation(0.5f, 1f, 0.5f, 1f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);//创建透明度动画的对象
        alphaAnimation.setDuration(1000);
        AnimationSet animationSet2 = new AnimationSet(true);
        animationSet2.setDuration(400);
        animationSet2.addAnimation(scaleanimation);
        animationSet2.addAnimation(mShowAction);
        main_lin_oneKeySell.startAnimation(animationSet2);
        main_lin_oneKeySell.setVisibility(View.VISIBLE);
        tv2.startAnimation(alphaAnimation);//设置字体的渐变显示
        tv2.setVisibility(View.VISIBLE);


        //popupWindow关闭时改回背景色
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                hideAnim();
                backgroundAlpha(1f);
            }
        });


        //给popupWindow根布局设置点击事件，点击就关闭弹窗
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                    popupWindow = null;
                }

            }
        });

        //返回键关闭popupWindow
        relativeLayout.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK) {
                    popupWindow.dismiss();
                }
                return false;
            }
        });

        //popupWindow里的控件1点击事件-跳转图片界面
        main_img_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PicActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fading_in, R.anim.fading_out);//跳转动画
                popupWindow.dismiss();
            }
        });

        //popupWindow里的控件2点击事件-跳转转卖界面
        main_img_oneKeySell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SaleActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fading_in, R.anim.fading_out);//跳转动画
                popupWindow.dismiss();
            }
        });

    }

    public void hideAnim() {
        //拍照控件的隐藏收回动画
        TranslateAnimation mShowAction1 = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.2f, Animation.RELATIVE_TO_PARENT,
                0.0f, Animation.RELATIVE_TO_PARENT, 0.2f);//向右下
        ScaleAnimation scaleanimation1 = new ScaleAnimation(1f, 0.5f, 1f, 0.5f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f);//放大
        AnimationSet animationSet1 = new AnimationSet(true);
        animationSet1.setDuration(300);
        animationSet1.addAnimation(scaleanimation1);
        animationSet1.addAnimation(mShowAction1);
        main_lin_pic.startAnimation(animationSet1);
        main_lin_pic.setVisibility(View.GONE);
    }


    //设置activity背景透明度
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }
}