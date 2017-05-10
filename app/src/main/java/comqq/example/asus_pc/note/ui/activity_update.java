package comqq.example.asus_pc.note.ui;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.EditText;

import org.litepal.crud.DataSupport;

import comqq.example.asus_pc.note.R;
import comqq.example.asus_pc.note.db.Notepad;
import jp.wasabeef.richeditor.RichEditor;

import static android.graphics.BitmapFactory.decodeFile;


/**
 * Created by asus-pc on 2017/5/7.
 */

public class activity_update extends AppCompatActivity {
    private Toolbar toolbar;
    private EditText edt_title;
    private RichEditor richEditor;
    private Notepad notepad;
    private String str;
    private int screenWidth;
    private int screenHeight;
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
        getScreenSize();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_update, menu);
        return true;
    }


    private void initDate() {
        Intent intent = getIntent();
        notepad = (Notepad) intent.getSerializableExtra("notepad");
        String titlel = notepad.getTitle();
        str = notepad.getCentent();
        Log.e("AAA", "id:" + notepad.getId());
        edt_title.setText(titlel);
//        richEditor.
        richEditor.setHtml(str);
        richEditor.setOnTextChangeListener(new RichEditor.OnTextChangeListener() {
            @Override
            public void onTextChange(String text) {
                str = text;
            }
        });
    }

    private void initView() {
        edt_title = (EditText) findViewById(R.id.edt_title);
        richEditor = (RichEditor) findViewById(R.id.richeditor);
    }

    private void Update() {
        notepad.setCentent(str);
        notepad.setTitle(edt_title.getText().toString());
        notepad.update(notepad.getId());
        Intent intent = new Intent();
        intent.putExtra("state", 1);
        setResult(2, intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case android.R.id.home:
                Update();
                finish();
                break;
            case R.id.item_photo:
                intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1);
                break;
            case R.id.item_delete:
                DataSupport.delete(Notepad.class, notepad.getId());
                intent = new Intent();
                intent.putExtra("state", 1);
                setResult(2, intent);
                finish();
                break;
        }
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Update();
            finish();
        }
        return true;
    }
    private void getScreenSize() {
        // 获得WindowManager实例
        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        screenWidth = wm.getDefaultDisplay().getWidth();
        screenHeight = wm.getDefaultDisplay().getHeight();
        Log.e("main", "手机的长宽为 ： " + screenWidth + " - " + screenHeight);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && data != null && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            String[] str = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(uri, str, null, null, null);
            cursor.moveToFirst();
            int colnmnIndex = cursor.getColumnIndex(str[0]);
            final String picturePath = cursor.getString(colnmnIndex);
            uri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), getImageBitmap(picturePath), null, null));
            cursor = getContentResolver().query(uri, str, null, null, null);
            cursor.moveToFirst();
            colnmnIndex = cursor.getColumnIndex(str[0]);
            String path = cursor.getString(colnmnIndex);
            cursor.close();
            Log.e("AAA", path);
            richEditor.insertImage(path, "dachshund");
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private Bitmap getImageBitmap(String imgpath) {
        // 得到图片的长宽
        Bitmap bitmap = decodeFile(imgpath);
        int imageWidth = bitmap.getWidth();
        int imageHeight = bitmap.getHeight();
        Log.e("AAA","图片的宽高为 ： " + imageWidth + " - " + imageHeight);
        double scale = 1;
        // 计算长宽缩放比
        double scaleX = imageWidth / (screenWidth*1.0);
        double scaleY = imageHeight /(screenHeight*1.0);
        Log.e("AAA", "缩放比为:" + scale);
        // 那个比例大就用哪个比例
        if (scaleX > scaleY && scaleX > scale) {
            scale = (screenWidth*1.0)/imageHeight ;
            Log.e("AAA",""+1);
        }else if (scaleY > scaleX && scaleY > scale) {
            scale = (screenHeight*1.0)/imageWidth ;
            Log.e("AAA",""+2);
        }
        Log.e("AAA", "手机的长宽为 ： " + screenWidth + " - " + screenHeight);
        Log.e("AAA", "缩放比为:" + scale);

        bitmap = Bitmap.createScaledBitmap(bitmap, (int) (scale * imageWidth)/4, (int) (scale * imageHeight)/4, true);

        return bitmap;
    }
}
