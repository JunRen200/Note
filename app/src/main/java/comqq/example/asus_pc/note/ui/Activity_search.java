package comqq.example.asus_pc.note.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import org.litepal.crud.DataSupport;

import java.util.List;

import comqq.example.asus_pc.note.R;
import comqq.example.asus_pc.note.adapter.Myadapter_search;
import comqq.example.asus_pc.note.db.Notepad;

/**
 * Created by asus-pc on 2017/5/3.
 */

public class Activity_search extends AppCompatActivity implements Myadapter_search.ToolbarSet{
    private ImageView img_back;
    private EditText edt_search;
    private RecyclerView mrecyclerView;
    private Myadapter_search myadapter;
    private String str;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initView();
    }

    private void initView() {
        img_back = (ImageView) findViewById(R.id.img_back);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.putExtra("state", 1);
                setResult(2, intent);
                finish();
            }
        });
        edt_search= (EditText) findViewById(R.id.edt_search);
        mrecyclerView= (RecyclerView) findViewById(R.id.recycler);
        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().equals("")) {
                    mrecyclerView.setVisibility(View.VISIBLE);
                    Log.e("AAA", s.toString());
                    str=s.toString();
                    List<Notepad> list =  DataSupport.where("title like ?", "%" + s.toString() + "%").find(Notepad.class);
                    if (myadapter == null) {
                        myadapter = new Myadapter_search(list, Activity_search.this);
                        mrecyclerView.setAdapter(myadapter);
                        mrecyclerView.setLayoutManager (new LinearLayoutManager(Activity_search.this, LinearLayoutManager.VERTICAL, false));
                    }
                    myadapter.setlist(list);
                }else {
                    mrecyclerView.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @Override
    public void openUpdate(Notepad notepad) {
        Intent intent=new Intent(Activity_search.this,activity_update.class);
        Bundle bundle=new Bundle();
        bundle.putSerializable("notepad",notepad);
        intent.putExtras(bundle);
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                int state=0;
                if(data!=null) {
                    state = data.getIntExtra("state", 5);
                }
                Log.e("AAA", state + "");
                if (state == 1) {
                    List<Notepad> list =  DataSupport.where("title like ?", "%" +str+ "%").find(Notepad.class);
                    myadapter.setlist(list);
                }
                break;
        }
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent=new Intent();
            intent.putExtra("state", 1);
            setResult(2, intent);
            finish();
        }
        return true;
    }
}
