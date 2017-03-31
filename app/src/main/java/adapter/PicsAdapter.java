package adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import bean.ImageBucket;
import bean.ImageItem;
import day713.test.phy.xianyu.R;
import utils.BitmapCache;

/**
 * Created by Administrator on 2016/8/2.
 * 选择相册
 */
public class PicsAdapter extends BaseAdapter{
    private List<ImageBucket> pics;
    private List<ImageItem> imageItems;
    private Context context;
    final String TAG = getClass().getSimpleName();
    private List<ImageItem> first_pics = new ArrayList<>();//每个相册的首图集合
    BitmapCache cache;

    public PicsAdapter(List<ImageBucket> pics, Context context,List<ImageItem> imageItems) {
        cache = new BitmapCache();
        this.pics = pics;
        this.context = context;
        this.imageItems =imageItems;
        //遍历相册得到每个相册首图
        for(ImageBucket imageBucket : pics){
            first_pics.add(imageBucket.imageList.get(0));
        }
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
    public int getCount() {
        return pics.size();
    }

    @Override
    public Object getItem(int position) {
        return pics.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHoulder viewHoulder;
        if(convertView == null){
            viewHoulder = new ViewHoulder();
            convertView = LayoutInflater.from(context).inflate(R.layout.imagebucket_item,null);
            viewHoulder.img_pics_item = (ImageView) convertView.findViewById(R.id.img_pics_item);
            viewHoulder.tv_pics_item = (TextView) convertView.findViewById(R.id.tv_pics_item);
            convertView.setTag(viewHoulder);
        }else {
            viewHoulder = (ViewHoulder) convertView.getTag();
        }

//设置图片并处理缓存显示
        final ImageItem imageItem = first_pics.get(position);
        String path;
        if (first_pics != null && first_pics.size() > position)//选中有图片
            path = first_pics.get(position).imagePath;//得到对应位置的图片获取地址
        else//
            path = "camera_default";//没有图片地址设为默认显示地址
        //判断地址
        if (path.contains("camera_default")) {//没有图片就显示默认图片
            viewHoulder.img_pics_item.setImageResource(R.mipmap.ic_launcher);
        } else {
            final ImageItem item = first_pics.get(position);
            viewHoulder.img_pics_item.setTag(item.imagePath);//设置标记
//            Log.v(TAG,"~~~~~~~~~~~~"+item.thumbnailPath);
            cache.displayBmp(viewHoulder.img_pics_item, item.thumbnailPath, item.imagePath,
                    callback);
            viewHoulder.tv_pics_item.setText(pics.get(position).bucketName+" ("+pics.get(position).count+")");//设置相册名
        }

        return convertView;
    }

    class ViewHoulder{
        private ImageView img_pics_item;
        private TextView tv_pics_item;
    }
}
