package day713.test.phy.xianyu;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.List;

import adapter.ImageAdapter;
import adapter.PicsAdapter;
import adapter.SelectImageAdapter;
import adapter.ShowPicPagerAdapter;
import bean.ImageBucket;
import bean.ImageItem;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;
import utils.AlbumHelper;
import utils.Bimp;
import utils.FileUtils;
import utils.StaticFinalNum;

public class PicActivity extends AppCompatActivity implements AdapterView.OnItemClickListener,ViewPager.OnPageChangeListener {

    @Bind(R.id.img_pic_back)
    ImageView imgPicBack;
    @Bind(R.id.tv_pic_choosePics)
    TextView tvPicChoosePics;
    @Bind(R.id.btn_pic_takeCamera)
    FloatingActionButton btnPicTakeCamera;
    @Bind(R.id.RecyclerView_select)
    android.support.v7.widget.RecyclerView RecyclerViewSelect;
    @Bind(R.id.btn_pic_confirm)
    Button btnPicConfirm;
    @Bind(R.id.btn_pic_count)
    Button btnPicCount;
    @Bind(R.id.lin_pic_confirm)
    RelativeLayout linPicConfirm;
    @Bind(R.id.listView_pic_choosePics)
    ListView listViewPicChoosePics;
    @Bind(R.id.frameLayout)
    FrameLayout frameLayout;
    @Bind(R.id.img_showPic_back)
    ImageView imgShowPicBack;
    @Bind(R.id.btn_showPic_confirm)
    ToggleButton btnShowPicConfirm;
    @Bind(R.id.viewPager_pic_show)
    ViewPager viewPagerPicShow;
    @Bind(R.id.lin_showPic)
    RelativeLayout linShowPic;
    @Bind(R.id.frameLayout_showPic)
    FrameLayout frameLayoutShowPic;
    private RecyclerView RecyclerView;
    private List<ImageBucket> imageBuckets = new ArrayList<>();//获得相册集合
    private List<ImageItem> imageItems = new ArrayList<>();//获得相片集合
    private List<ImageItem> imageItems_select = new ArrayList<>();//获得选中相片集合
    private ImageAdapter adapter;//图片展示适配器
    private SelectImageAdapter selectAdapter = new SelectImageAdapter(this, imageItems_select, adapter);
    private ShowPicPagerAdapter showBigPicAdapter;//显示大图的viewPager的适配器
    //选中图片展示适配器
    private PicsAdapter picsAdapter;//相册适配器
    private AlbumHelper helper = AlbumHelper.getHelper();
    final String TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic);
        ButterKnife.bind(this);
        //在接收者里面进行注册
        EventBus.getDefault().register(this);

        init();//图片显示初始化工作
        initSelect();//下面选中图片栏的初始化工作
        initPics();//初始化选择相册界面
        initPicShow();//初始化大图显示界面
    }


    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onReceive(Integer integer) {
        if (integer == StaticFinalNum.EVENTBUS_TAG1) {//收到来自选中图片列表的更改信息，更新图片展示列表的UI
            adapter.notifyDataSetChanged();
            //根据选中图片是否选中设置勾选按钮是否勾选
            if(imageItems_select.contains(imageItems.get(viewPagerPicShow.getCurrentItem()))){//
                btnShowPicConfirm.setChecked(true);
            }else {
                btnShowPicConfirm.setChecked(false);
            }
        } else if (integer == StaticFinalNum.EVENTBUS_TAG2) {//更新选中图片的数量信息
            if (imageItems_select.size() > 0) {
                btnPicCount.setVisibility(View.VISIBLE);
                btnPicCount.setText("" + imageItems_select.size());
            } else {
                btnPicCount.setVisibility(View.GONE);
            }

        }else if(integer >= 0){//显示大图界面
            showBigPicAdapter.notifyDataSetChanged();//每次点击后更新数据源
            viewPagerPicShow.setCurrentItem(integer,false);//获得当前选中位置信息，显示选中图片,并取消滑动效果
            //根据选中图片是否选中设置勾选按钮是否勾选
            if(imageItems_select.contains(imageItems.get(viewPagerPicShow.getCurrentItem()))){//
                btnShowPicConfirm.setChecked(true);
            }else {
                btnShowPicConfirm.setChecked(false);
            }
            //设置显示动画
            ScaleAnimation scaleanimation = new ScaleAnimation(0f, 1f, 0f, 1f,
                    ScaleAnimation.RELATIVE_TO_PARENT, 0.5f,
                    ScaleAnimation.RELATIVE_TO_PARENT, 0.5f);
            scaleanimation.setDuration(200);
            frameLayoutShowPic.startAnimation(scaleanimation);
            frameLayoutShowPic.setVisibility(View.VISIBLE);
        }

    }

    //全部图片显示初始化工作
    public void init() {
        RecyclerView = (android.support.v7.widget.RecyclerView) findViewById(R.id.RecyclerView);
        helper.init(this);//初始化
        imageBuckets = helper.getImagesBucketList(false);//得到相册集合
        for (int i = 0; i < imageBuckets.size(); i++) {
            imageItems.addAll(imageBuckets.get(i).imageList);//得到相片集合
        }
//        Log.v(TAG,"~~~~~~~~~~~~~"+imageBuckets.size()+"-----"+imageItems.size());
        //设置item放置模式
        GridLayoutManager manager = new GridLayoutManager(this, 4);
        RecyclerView.setLayoutManager(manager);
        adapter = new ImageAdapter(imageItems, PicActivity.this, imageItems_select, selectAdapter);
        RecyclerView.setAdapter(adapter);
        //设置间距
        ItemSpace space = new ItemSpace(8);
        RecyclerView.addItemDecoration(space);
    }

    //选中图片显示初始化显示工作
    private void initSelect() {
                Log.v(TAG,"~~~~~~~~~~~~~"+imageBuckets.size()+"-----"+imageItems.size());
        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);//设置横向滑动
        RecyclerViewSelect.setLayoutManager(linearLayoutManager);
        RecyclerViewSelect.setAdapter(selectAdapter);
    }

    //初始化选择相册界面
    private void initPics() {
        picsAdapter = new PicsAdapter(imageBuckets, this, imageItems);
        listViewPicChoosePics.setAdapter(picsAdapter);
        listViewPicChoosePics.setOnItemClickListener(this);//设置选项点击事件
    }

    //初始化大图显示界面
    private void initPicShow() {
        showBigPicAdapter = new ShowPicPagerAdapter(imageItems,this);
        viewPagerPicShow.setAdapter(showBigPicAdapter);
        viewPagerPicShow.setOnPageChangeListener(this);
        //返回键
        imgShowPicBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //设置隐藏动画
                ScaleAnimation scaleanimation = new ScaleAnimation(1f, 0f, 1f, 0f,
                        ScaleAnimation.RELATIVE_TO_PARENT, 0.5f,
                        ScaleAnimation.RELATIVE_TO_PARENT, 0.5f);
                scaleanimation.setDuration(200);
                frameLayoutShowPic.startAnimation(scaleanimation);
                frameLayoutShowPic.setVisibility(View.GONE);
            }
        });

        /**
         * 图片勾选键的点击事件
         */
        btnShowPicConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //如果点击已选中就取消选中
                if(imageItems_select.contains(imageItems.get(viewPagerPicShow.getCurrentItem())) ){
                    imageItems_select.remove(imageItems.get(viewPagerPicShow.getCurrentItem()));
                    selectAdapter.notifyDataSetChanged();//数据源变化，通知更新UI
                    adapter.notifyDataSetChanged();//数据源变化，通知更新UI
                    btnShowPicConfirm.setChecked(false);
                    EventBus.getDefault().post(StaticFinalNum.EVENTBUS_TAG2);
                }else {//如果点击未选中的图片
                    if(imageItems_select.size() < StaticFinalNum.MAX_SELECT){
                        imageItems_select.add(imageItems.get(viewPagerPicShow.getCurrentItem()));
                        selectAdapter.notifyDataSetChanged();//数据源变化，通知更新UI
                        adapter.notifyDataSetChanged();//数据源变化，通知更新UI
                        btnShowPicConfirm.setChecked(true);
                        EventBus.getDefault().post(StaticFinalNum.EVENTBUS_TAG2);
                    }else {
                        btnShowPicConfirm.setChecked(false);
                        Toast.makeText(PicActivity.this,"一次上传图片不能超过10张",Toast.LENGTH_LONG).show();
                        Log.v(TAG,"~~~~~~~~~~~~~~~一次上传图片不能超过10张");
                    }

                }
            }
        });
    }

    /**
     * 该页面主要控件点击事件
     */

    @OnClick({R.id.img_pic_back, R.id.tv_pic_choosePics, R.id.btn_pic_takeCamera, R.id.btn_pic_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_pic_back://返回键
                onBackPressed();
                break;
            case R.id.tv_pic_choosePics://选择图片
//                Log.v(TAG,frameLayout.getVisibility()+"~~~~~"+View.VISIBLE);
                if (frameLayout.getVisibility() == View.VISIBLE) {
                    //设置隐藏动画
                    ScaleAnimation scaleanimation = new ScaleAnimation(1f, 0f, 1f, 0f,
                            ScaleAnimation.RELATIVE_TO_PARENT, 0.9f,
                            ScaleAnimation.RELATIVE_TO_PARENT, 0f);
                    scaleanimation.setDuration(200);
                    frameLayout.startAnimation(scaleanimation);
                    frameLayout.setVisibility(View.GONE);
                } else if (frameLayout.getVisibility() == View.GONE) {
                    //设置显示动画
                    ScaleAnimation scaleanimation = new ScaleAnimation(0f, 1f, 0f, 1f,
                            ScaleAnimation.RELATIVE_TO_PARENT, 0.9f,
                            ScaleAnimation.RELATIVE_TO_PARENT, 0f);
                    scaleanimation.setDuration(200);
                    frameLayout.startAnimation(scaleanimation);
                    frameLayout.setVisibility(View.VISIBLE);
                }

                break;
            case R.id.btn_pic_takeCamera://调用相机
                if(imageItems_select.size() < 10){
                    photo();
                }else {
                    Toast.makeText(PicActivity.this,"一次上传图片不能超过10张",Toast.LENGTH_LONG).show();
                }

                break;
            case R.id.btn_pic_confirm://确认上传已选图片
                break;
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        imageItems.clear();//清空显示的图片
        imageItems.addAll(imageBuckets.get(position).imageList);//得到选中的相片集合
        adapter.notifyDataSetChanged();
        frameLayout.setVisibility(View.GONE);
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        //根据选中图片是否选中设置勾选按钮是否勾选
        if(imageItems_select.contains(imageItems.get(viewPagerPicShow.getCurrentItem()))){//
            btnShowPicConfirm.setChecked(true);
        }else {
            btnShowPicConfirm.setChecked(false);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    //设置item间隔
    class ItemSpace extends RecyclerView.ItemDecoration {
        private int space;

        public ItemSpace(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.bottom = space;
            outRect.left = space;
            outRect.right = space;
            if (parent.getChildAdapterPosition(view) == 0) {
                outRect.top = space;
            }
        }
    }

    private static final int TAKE_PICTURE = 0x000001;

    //调用相机
    public void photo() {
        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(openCameraIntent, TAKE_PICTURE);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PICTURE:
                if (imageItems_select.size() <= 10 && resultCode == RESULT_OK) {

                    String fileName = String.valueOf(System.currentTimeMillis());
                    Bitmap bm = (Bitmap) data.getExtras().get("data");
                    FileUtils.saveBitmap(bm, fileName);
                    ImageItem takePhoto = new ImageItem();
                    takePhoto.setBitmap(bm);
                    imageItems_select.add(takePhoto);
                    selectAdapter.notifyDataSetChanged();
                }
                break;
        }
    }

    //销毁activity
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        EventBus.getDefault().unregister(this);
    }

}
