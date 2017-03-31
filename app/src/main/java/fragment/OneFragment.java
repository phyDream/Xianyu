package fragment;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import adapter.OneViewPagerAdapter;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import day713.test.phy.xianyu.MainActivity;
import day713.test.phy.xianyu.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class OneFragment extends Fragment {


    @Bind(R.id.one_img_saoma)
    ImageView oneImgSaoma;
    @Bind(R.id.one_lin_search)
    LinearLayout oneLinSearch;
    @Bind(R.id.one_img_fenlei)
    ImageView oneImgFenlei;
    @Bind(R.id.one_viewPager)
    ViewPager oneViewPager;
    @Bind(R.id.one_lin_zhishidian)
    LinearLayout oneLinZhishidian;
    @Bind(R.id.img_selected1)
    ImageView imgSelected1;
    @Bind(R.id.img_selected2)
    ImageView imgSelected2;
    @Bind(R.id.img_selected3)
    ImageView imgSelected3;
    @Bind(R.id.img_selected4)
    ImageView imgSelected4;
    @Bind(R.id.img_selected5)
    ImageView imgSelected5;

    private List<Integer> pagers = new ArrayList<>();
    private MainActivity mainActivity;
    private OneViewPagerAdapter adpater;
    private static final String LOG_TAG = "OneFragment";
    private int pager = 1000000 / 2;
    private Thread thread;
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            int i = msg.what;
//            Log.v(LOG_TAG, "~~~~~~~~~~~~~~" + i);
            if(oneViewPager != null){
                oneViewPager.setCurrentItem(i);
            }
            return false;
        }
    });


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_one, container, false);
        ButterKnife.bind(this, view);
        mainActivity = (MainActivity) getActivity();
        setViewPager();//绑定填充viewPager展示页
        return view;
    }


    //绑定填充viewPager展示页
    private void setViewPager() {
        pagers.add(R.drawable.viewpager1);
        pagers.add(R.drawable.viewpager2);
        pagers.add(R.drawable.viewpager3);
        pagers.add(R.mipmap.ic_launcher2);
        pagers.add(R.mipmap.ic_launcher);

        adpater = new OneViewPagerAdapter(pagers, mainActivity);
        oneViewPager.setAdapter(adpater);
        oneViewPager.setCurrentItem(1000000 / 2);//设置初始页数很大，要为显示页数的整倍数
        oneViewPager.addOnPageChangeListener(new PagerChangeListener());
        initIcon(0);
        //自动翻页
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 500000; i++) {
                    try {
                        Thread.sleep(4000);
                        Message msg = new Message();
                        msg.what = ++pager % pagers.size();
                        handler.sendMessage(msg);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
        thread.start();
    }

    //设置展示页指示点
    public void initIcon(int position) {
        switch (position){
            case 0://先使所有点变黑，再使选中点变黄
                reset();
                imgSelected1.setImageResource(R.drawable.indicator_selected_home_pager);
                break;
            case 1:
                reset();
                imgSelected2.setImageResource(R.drawable.indicator_selected_home_pager);
                break;
            case 2:
                reset();
                imgSelected3.setImageResource(R.drawable.indicator_selected_home_pager);
                break;
            case 3:
                reset();
                imgSelected4.setImageResource(R.drawable.indicator_selected_home_pager);
                break;
            case 4:
                reset();
                imgSelected5.setImageResource(R.drawable.indicator_selected_home_pager);
                break;
        }
    }

    //重置所有指示点为黑点
    public void reset(){
        imgSelected1.setImageResource(R.drawable.indicator_unselected_home_pager);
        imgSelected2.setImageResource(R.drawable.indicator_unselected_home_pager);
        imgSelected3.setImageResource(R.drawable.indicator_unselected_home_pager);
        imgSelected4.setImageResource(R.drawable.indicator_unselected_home_pager);
        imgSelected5.setImageResource(R.drawable.indicator_unselected_home_pager);
    }

    //pager页滑动监听
    class PagerChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
//            Log.v(LOG_TAG,"~~~~~~~~~~~~~~"+position%pagers.size());
            initIcon(position % pagers.size());
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.one_img_saoma, R.id.one_lin_search, R.id.one_img_fenlei})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.one_img_saoma:
                break;
            case R.id.one_lin_search:
                break;
            case R.id.one_img_fenlei:
                break;
        }
    }
}
