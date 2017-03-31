package adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import bean.ImageItem;
import day713.test.phy.xianyu.R;
import uk.co.senab.photoview.PhotoView;
import utils.BitmapCache;

/**
 * Created by Administrator on 2016/8/3.
 * 显示大图的ViewPager的适配器
 */
public class ShowPicPagerAdapter extends PagerAdapter{
    final String TAG = getClass().getSimpleName();
    private List<ImageItem> imageItems;
    private Context context;
    BitmapCache cache;//图片处理对象

    public ShowPicPagerAdapter(List<ImageItem> imageItems, Context context) {
        cache = new BitmapCache();
        this.imageItems = imageItems;
        this.context = context;
    }

    //设置图片的接口对象
    BitmapCache.ImageCallback callback = new BitmapCache.ImageCallback() {
        @Override
        public void imageLoad(ImageView imageView, Bitmap bitmap,
                              Object... params) {
            if (imageView != null && bitmap != null) {
                String url = (String) params[0];
                if (url != null && url.equals((String) imageView.getTag())) {
                    ((ImageView) imageView).setImageBitmap(bitmap);
                } else {
                    Log.e(TAG, "callback, bmp not match");
                }
            } else {
                Log.e(TAG, "callback, bmp null");
            }
        }
    };

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        PhotoView photoView = new PhotoView(context);
        photoView.setScaleType(PhotoView.ScaleType.FIT_CENTER);
        //设置图片并处理缓存显示
        String path;
        if (imageItems != null && imageItems.size() > position)//相册有图片
            path = imageItems.get(position).imagePath;//得到对应位置的图片获取地址
        else//
            path = "camera_default";//没有图片地址设为默认显示地址
        //判断地址
        if (path.contains("camera_default")) {//没有图片就显示默认图片
            photoView.setImageResource(R.mipmap.ic_launcher2);
        } else {
            final ImageItem item = imageItems.get(position);
            photoView.setImageBitmap(imageItems.get(position).getBitmap());
        }
        container.addView(photoView);
        return photoView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((PhotoView)object);
    }

    @Override
    public int getCount() {
        return imageItems.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
