package com.ni.newrecycler.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ni.newrecycler.R;
import com.ni.newrecycler.bean.Bean;
import com.ni.newrecycler.utils.ImageLoaderUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by dell on 2017/4/5.
 */

public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener, View.OnLongClickListener {
    private Context context;
    private List<Bean.ApkBeanHeadline> list;
    private OnChildClickListener listener;
    private RecyclerView recyclerView;
    private final DisplayImageOptions option;

    public void setOnChildClickListener(OnChildClickListener listener) {
        this.listener = listener;
    }

    public MyAdapter(Context context, List<Bean.ApkBeanHeadline> list) {
        this.context = context;
        this.list = list;
        //设置图片成功失败 加载的情况
        option = ImageLoaderUtil.getOption
                (R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.
                        ic_launcher, new FadeInBitmapDisplayer(100));
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        view.setOnClickListener(this);
        view.setOnLongClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        //进行赋值
        ((ViewHolder) holder).nameTv.setText(list.get(position).getName());
        //取MB
        String apkSize = list.get(position).getApkSize();
        String formatSize = getFormatSize(Double.parseDouble(apkSize));

        ((ViewHolder) holder).sizeTv.setText(formatSize+"/"+list.get(position).getDownloadTimes().substring(0,2)+"万");
        ImageLoader.getInstance().displayImage(list.get(position).getIconUrl(),((ViewHolder) holder).imageView,option);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    //绑定
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    //解绑
    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        this.recyclerView = null;
    }

    /**
     * 点击
     */
    @Override
    public void onClick(View view) {
        if (recyclerView != null && listener != null) {
            int position = recyclerView.getChildAdapterPosition(view);
            listener.onChildClick(position);
        }
    }
    /**
     *   删除的方法
     */
    public void remove(int positon){
        list.remove(positon);
        notifyItemRemoved(positon);
    }
    /**
     * 长按
     */
    @Override
    public boolean onLongClick(View v) {
        if (recyclerView != null && listener != null) {
            int position = recyclerView.getChildAdapterPosition(v);
            listener.onChildLongClick(position);
        }
        return true;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView nameTv, sizeTv;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.item_image);
            nameTv = (TextView) itemView.findViewById(R.id.item_name);
            sizeTv = (TextView) itemView.findViewById(R.id.item_size);
        }
    }

    //定义一个接口
    public interface OnChildClickListener {
        void onChildClick(int position);

        void onChildLongClick(int position);
    }

    /**
     * 格式化单位
     */
    public static String getFormatSize(double size) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            return size + "Byte";
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "KB";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "MB";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
                + "TB";
    }
}
