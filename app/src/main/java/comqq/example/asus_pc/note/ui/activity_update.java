package comqq.example.asus_pc.note.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;

import comqq.example.asus_pc.note.R;


/**
 * Created by asus-pc on 2017/5/7.
 */

public class activity_update extends AppCompatActivity {
    private Toolbar toolbar;
    private EditText edt_title;
    private EditText edt_centent;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addnote);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("笔记");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.mipmap.ic_menu_back);
        }

        initView();
        initDate();

    }

    private void initDate() {
        Intent intent= getIntent();
        String titlel=intent.getStringExtra("title");
        String content=intent.getStringExtra("content");
        edt_title.setText(titlel);
        edt_centent.setText(content);

    }

    private void initView() {
        edt_title = (EditText) findViewById(R.id.edt_title);
        edt_centent = (EditText) findViewById(R.id.edt_centent);

    }
}
