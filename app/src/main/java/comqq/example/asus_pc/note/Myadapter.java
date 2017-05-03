package comqq.example.asus_pc.note;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by asus-pc on 2017/5/3.
 */

public class Myadapter extends RecyclerView.Adapter<Myadapter.ViewHolder> {
    List<Notepad> list;
    Myadapter(List<Notepad> list){
        this.list=list;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recyle_item,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Notepad notepad=list.get(position);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        String time = dateFormat.format(notepad.getTime());
        holder.txt_title.setText(notepad.getTitle());
        holder.txt_content.setText(notepad.getCentent());
        holder.txt_time.setText(time);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txt_title;
        private TextView txt_content;
        private TextView txt_time;
        public ViewHolder(View itemView) {
            super(itemView);
            txt_title= (TextView) itemView.findViewById(R.id.txt_title);
            txt_content= (TextView) itemView.findViewById(R.id.txt_content);
            txt_time= (TextView) itemView.findViewById(R.id.txt_time);
        }
    }
    public void setlist(List<Notepad> list){
        this.list=list;
        notifyDataSetChanged();
    }
}
