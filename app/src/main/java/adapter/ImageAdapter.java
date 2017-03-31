package adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.List;

import bean.ImageItem;
import day713.test.phy.xianyu.R;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;
import utils.BitmapCache;
import utils.MyToast;
import utils.StaticFinalNum;

/**
 * Created by Administrator on 2016/7/28.
 */
public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ItemViewHolder>{
    final String TAG = getClass().getSimpleName();
    private List<ImageItem> imageItems;
    private Context context;
    private List<ImageItem> imageItems_select;
    private SelectImageAdapter selectImageAdapter;
    BitmapCache cache;

    public ImageAdapter(List<ImageItem> imageItems, Context context,List<ImageItem> imageItems_select
                        ,SelectImageAdapter selectImageAdapter) {
        cache = new BitmapCache();
        this.imageItems = imageItems;
        this.context = context;
        this.imageItems_select = imageItems_select;
        this.selectImageAdapter = selectImageAdapter;
    }

    //设置图片
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
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_item,null);
        ItemViewHolder itemViewHolder = new ItemViewHolder(view);
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, final int position) {

        //设置图片并处理缓存显示
        final ImageItem imageItem = imageItems.get(position);
        String path;
        if (imageItems != null && imageItems.size() > position)//相册有图片
            path = imageItems.get(position).imagePath;//得到对应位置的图片获取地址
        else//
            path = "camera_default";//没有图片地址设为默认显示地址
        //判断地址
        if (path.contains("camera_default")) {//没有图片就显示默认图片
            holder.img.setImageResource(R.mipmap.ic_launcher);
        } else {
            final ImageItem item = imageItems.get(position);
            holder.img.setTag(item.imagePath);//设置标记
//            Log.v(TAG,"~~~~~~~~~~~~"+item.thumbnailPath);
            cache.displayBmp(holder.img, item.thumbnailPath, item.imagePath,
                    callback);

            //设置控件时，该位置没有被选中就设置未选中样式
            if(!imageItems_select.contains(imageItems.get(position))){
                holder.button.setChecked(false);
                holder.toggleButton.setChecked(false);
            }else {
                holder.button.setChecked(true);
                holder.toggleButton.setChecked(true);
            }
        }

        //item的勾选图片的点击事件，选中的图片加入选中的集合
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                  //如果点击已选中就取消选中
                  if(imageItems_select.contains(imageItems.get(position)) ){
                      imageItems_select.remove(imageItems.get(position));
                      selectImageAdapter.notifyDataSetChanged();
                      holder.button.setChecked(false);
                      holder.toggleButton.setChecked(false);
                      EventBus.getDefault().post(StaticFinalNum.EVENTBUS_TAG2);
                  }else {//如果点击未选中的图片
                      if(imageItems_select.size() < StaticFinalNum.MAX_SELECT){
                          imageItems_select.add(imageItems.get(position));
                          selectImageAdapter.notifyDataSetChanged();
                          holder.button.setChecked(true);
                          holder.toggleButton.setChecked(true);
                          EventBus.getDefault().post(StaticFinalNum.EVENTBUS_TAG2);
                      }else {
                          holder.button.setChecked(false);
                          holder.toggleButton.setChecked(false);
                          Toast.makeText(context,"一次上传图片不能超过10张",Toast.LENGTH_LONG).show();
                          Log.v(TAG,"~~~~~~~~~~~~~~~一次上传图片不能超过10张");
                      }

                  }

            }
        });

        //item的点击事件
        holder.toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EventBus.getDefault().post(position);//点击显示大图界面
                //如果点击已选中就取消选中
                if(imageItems_select.contains(imageItems.get(position)) ){
                    holder.toggleButton.setChecked(true);
                }else {//如果点击未选中的图片
                        holder.toggleButton.setChecked(false);
                }
            }
        });

    }


    @Override
    public int getItemCount() {
        return imageItems.size();
    }

    //自定义控件携带者--找到item视图上相应控件
    public static class ItemViewHolder extends RecyclerView.ViewHolder{
        private ImageView img;
        private ToggleButton toggleButton;
        private ToggleButton button;
        public ItemViewHolder(View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.imageItem);
            toggleButton = (ToggleButton) itemView.findViewById(R.id.toggle_button);
            button = (ToggleButton) itemView.findViewById(R.id.choosedbt);
        }
    }
}
