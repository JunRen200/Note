package comqq.example.asus_pc.note.adapter;

import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import comqq.example.asus_pc.note.R;
import comqq.example.asus_pc.note.db.Notepad;

/**
 * Created by asus-pc on 2017/5/3.
 */

public class Myadapter extends RecyclerView.Adapter<Myadapter.ViewHolder> {
    public interface ToolbarSet{
        public void ToolbarDelete();
        public void openUpdate(Notepad notepad);
    }
    private ToolbarSet toolbarSet;
    private List<Notepad> list;
    public static int NO_CHOOSE = 0;
    public static int ON_CHOOSE = 1;
    private int[] indexlist;
    private int choose_state = NO_CHOOSE;


    public Myadapter(List<Notepad> list,ToolbarSet toolbarSet) {
        this.list = list;
        this.toolbarSet=toolbarSet;
        indexlist = new int[this.list.size()];
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.e("AAA", "创建");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyle_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Log.e("AAA", "绑定" + position);
        final Notepad notepad = list.get(position);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        String time = dateFormat.format(notepad.getTime());
        holder.txt_title.setText(notepad.getTitle());
        holder.txt_content.setText(notepad.getCentent());
        holder.txt_time.setText(time);
        if (choose_state == NO_CHOOSE) {
            holder.img_change.setVisibility(View.GONE);
        } else if (choose_state == ON_CHOOSE && indexlist[position] == 1) {
            holder.img_change.setVisibility(View.VISIBLE);
            holder.img_change.setImageResource(R.mipmap.check2);
            holder.cardView.setBackgroundColor(Color.parseColor("#d8ede7"));
        } else if (choose_state == ON_CHOOSE) {
            holder.img_change.setVisibility(View.VISIBLE);
            holder.img_change.setImageResource(R.mipmap.check1);
            holder.cardView.setBackgroundColor(Color.parseColor("#ffffff"));
        }
        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (choose_state == NO_CHOOSE) {
                    choose_state = ON_CHOOSE;
                    indexlist[position] = 1;
                    notifyItemRangeChanged(0, list.size());
                    toolbarSet.ToolbarDelete();
                }
                return true;
            }
        });
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (choose_state == ON_CHOOSE) {
                    if (indexlist[position] == 0) {
                        indexlist[position] = 1;
                        holder.img_change.setImageResource(R.mipmap.check2);
                        holder.cardView.setBackgroundColor(Color.parseColor("#d8ede7"));
                    } else if (indexlist[position] == 1) {
                        indexlist[position] = 0;
                        holder.img_change.setImageResource(R.mipmap.check1);
                        holder.cardView.setBackgroundColor(Color.parseColor("#ffffff"));
                    }
                }else if(choose_state==NO_CHOOSE){
                    toolbarSet.openUpdate(notepad);
                }
            }
        });
    }

    private void delete(int position) {
        list.get(position).delete();
        notifyItemRemoved(position);
        if (position != list.size()) {
            notifyItemRangeChanged(position, list.size() - position);
        }
        list.remove(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView img_change;
        private CardView cardView;
        private TextView txt_title;
        private TextView txt_content;
        private TextView txt_time;

        public ViewHolder(View itemView) {
            super(itemView);
            img_change = (ImageView) itemView.findViewById(R.id.img_change);
            cardView = (CardView) itemView.findViewById(R.id.carview);
            txt_title = (TextView) itemView.findViewById(R.id.txt_title);
            txt_content = (TextView) itemView.findViewById(R.id.txt_content);
            txt_time = (TextView) itemView.findViewById(R.id.txt_time);
        }
    }

    public void setlist(List<Notepad> list) {
        this.list = list;
        indexlist = new int[this.list.size()];
        notifyDataSetChanged();
    }

    public int getchoose_state() {
        return choose_state;
    }

    public void setChoose_state() {
        if (choose_state == ON_CHOOSE) {
            choose_state = NO_CHOOSE;
            indexlist = new int[this.list.size()];
            notifyItemRangeChanged(0, list.size());
        }
    }
    public void deleteDate(){
        for(int i=0;i<indexlist.length;i++){
            if(indexlist[i]==1){
                list.get(i).delete();
            }
        }
        for(int i=0;i<indexlist.length;i++){
            if(indexlist[i]==1){
                list.remove(i);
            }
        }
        indexlist=new int[list.size()];
        notifyDataSetChanged();
    }
}
