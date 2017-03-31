package adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ToggleButton;

import java.util.List;

import bean.ImageItem;
import day713.test.phy.xianyu.R;
import de.greenrobot.event.EventBus;
import utils.BitmapCache;
import utils.StaticFinalNum;

/**
 * Created by Administrator on 2016/8/1.
 * 下方显示选中的图片的适配器
 */
public class SelectImageAdapter extends RecyclerView.Adapter<SelectImageAdapter.ItemViewHolder>{
    final String TAG = getClass().getSimpleName();
    private Context context;
    private List<ImageItem> imageItems_select;
    private ImageAdapter adapter;
    BitmapCache cache;
    String path = null;

    public SelectImageAdapter(Context context, List<ImageItem> imageItems_select,ImageAdapter adapter) {
        cache = new BitmapCache();
        this.context = context;
        this.imageItems_select = imageItems_select;
        this.adapter = adapter;
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
    public SelectImageAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.select_image_item,null);
        ItemViewHolder itemViewHolder = new ItemViewHolder(view);
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(SelectImageAdapter.ItemViewHolder holder, final int position) {
        //设置图片并处理缓存显示
        final ImageItem imageItem = imageItems_select.get(position);

        if (imageItems_select != null && imageItems_select.size() > position && imageItems_select.get(position).imagePath != null){//选中有item，且有图片地址
            final ImageItem item = imageItems_select.get(position);
            holder.img.setTag(item.imagePath);//设置标记
//            Log.v(TAG,"~~~~~~~~~~~~"+item.thumbnailPath);
            cache.displayBmp(holder.img, item.thumbnailPath, item.imagePath,
                    callback);
        }else if(imageItems_select != null && imageItems_select.size() > position && imageItems_select.get(position).imagePath == null
        &&imageItems_select.get(position).getBitmap() != null){//选中有item，没有图片地址
            holder.img.setImageBitmap(imageItems_select.get(position).getBitmap());
            Log.e(TAG, "~~~~~~~~~~~~~~~~~~2");
        }else{
            holder.img.setImageResource(R.mipmap.ic_launcher2);
            Log.e(TAG, "~~~~~~~~~~~~~~~~~~3");
        }


        //设置item上控件的点击事件
        //去掉选中图片图标的点击事件
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageItems_select.remove(position);//删除选中的图片
                notifyDataSetChanged();//更新选中列表UI
                EventBus.getDefault().post(StaticFinalNum.EVENTBUS_TAG1);
                EventBus.getDefault().post(StaticFinalNum.EVENTBUS_TAG2);
            }
        });
    }


    @Override
    public int getItemCount() {
        return imageItems_select.size();
    }

    //自定义控件携带者--找到item视图上相应控件
    public static class ItemViewHolder extends RecyclerView.ViewHolder{
        private ImageView img;
        private Button button;
        public ItemViewHolder(View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.img_selectPic);
            button = (Button) itemView.findViewById(R.id.btn_selectPic);
        }
    }


}
