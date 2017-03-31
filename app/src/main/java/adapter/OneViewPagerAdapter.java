package adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by Administrator on 2016/7/17.
 */
public class OneViewPagerAdapter extends PagerAdapter{

    private List<Integer> pagers;
    private Context context;

    public OneViewPagerAdapter(List<Integer> pagers, Context context) {
        this.pagers = pagers;
        this.context = context;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = new ImageView(context);
        imageView.setImageResource(pagers.get(position%pagers.size()));
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        container.addView(imageView);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((ImageView) object);
    }

    @Override
    public int getCount() {
        return 1000000;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
