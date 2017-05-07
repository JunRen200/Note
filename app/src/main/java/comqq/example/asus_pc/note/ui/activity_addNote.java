package comqq.example.asus_pc.note.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Date;

import comqq.example.asus_pc.note.R;
import comqq.example.asus_pc.note.db.Notepad;

/**
 * Created by asus-pc on 2017/5/3.
 */

public class activity_addNote extends AppCompatActivity {
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
    }

    private void initView() {
        edt_title = (EditText) findViewById(R.id.edt_title);
        edt_centent = (EditText) findViewById(R.id.edt_centent);

    }

    public void saveData() {
        String title = edt_title.getText().toString();
        if (!title.equals("")) {
            String content = edt_centent.getText().toString();
            Date now = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
            String time = dateFormat.format(now);
            Notepad notepad = new Notepad();
            notepad.setTime(now);
            notepad.setTitle(title);
            notepad.setCentent(content);
            notepad.save();
            Intent intent = new Intent();
            intent.putExtra("state", 1);
            setResult(2, intent);
            Log.e("AAA", "12345678");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                saveData();
                finish();
                break;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_menu, menu);
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            saveData();
            finish();
        }
        return true;
    }
}
