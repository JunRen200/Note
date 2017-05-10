package comqq.example.asus_pc.note.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import comqq.example.asus_pc.note.R;
import comqq.example.asus_pc.note.db.Notepad;

/**
 * Created by asus-pc on 2017/5/9.
 */

public class Myadapter_search extends RecyclerView.Adapter<Myadapter_search.ViewHolder> {
    public interface ToolbarSet{
        public void openUpdate(Notepad notepad);
    }
    private List<Notepad> list;
    private ToolbarSet toolbarSet;
    public Myadapter_search(List<Notepad> list, Myadapter_search.ToolbarSet toolbarSet) {
        this.list = list;
        this.toolbarSet=toolbarSet;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recyle_item,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Notepad notepad = list.get(position);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        String time = dateFormat.format(notepad.getTime());
        holder.txt_title.setText(notepad.getTitle());
        holder.txt_content.setText(notepad.getCentent());
        holder.txt_time.setText(time);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toolbarSet.openUpdate(list.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private CardView cardView;
        private TextView txt_title;
        private TextView txt_content;
        private TextView txt_time;
        public ViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.carview);
            txt_title = (TextView) itemView.findViewById(R.id.txt_title);
            txt_content = (TextView) itemView.findViewById(R.id.txt_content);
            txt_time = (TextView) itemView.findViewById(R.id.txt_time);
        }
    }
    public void setlist(List<Notepad> list) {
        this.list = list;
        notifyDataSetChanged();
    }
}
