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

import java.text.SimpleDateFormat;
import java.util.Date;

import comqq.example.asus_pc.note.R;
import comqq.example.asus_pc.note.db.Notepad;
import jp.wasabeef.richeditor.RichEditor;

import static android.graphics.BitmapFactory.decodeFile;

/**
 * Created by asus-pc on 2017/5/3.
 */

public class activity_addNote extends AppCompatActivity {
    private Toolbar toolbar;
    private EditText edt_title;
    private RichEditor richEditor;
    private String str = "";
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
        getScreenSize();
    }

    private void getScreenSize() {
        // 获得WindowManager实例
        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        screenWidth = wm.getDefaultDisplay().getWidth();
        screenHeight = wm.getDefaultDisplay().getHeight();
        Log.e("main", "手机的长宽为 ： " + screenWidth + " - " + screenHeight);
    }

    private void initView() {
        edt_title = (EditText) findViewById(R.id.edt_title);
        richEditor = (RichEditor) findViewById(R.id.richeditor);
        richEditor.setEditorFontSize(22);
        richEditor.setOnTextChangeListener(new RichEditor.OnTextChangeListener() {
            @Override
            public void onTextChange(String text) {
                str = text;
                Log.e("AAA", str);
            }
        });
    }

    public void saveData() {
        String title = edt_title.getText().toString();
        if (!title.equals("") || !str.equals("")) {
            Date now = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
            String time = dateFormat.format(now);
            Notepad notepad = new Notepad();
            notepad.setTime(now);
            notepad.setTitle(title);
            notepad.setCentent(str);
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
            case R.id.item_photo:
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1);
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
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            saveData();
            finish();
        }
        return true;
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
        Log.e("AAA", "图片的宽高为 ： " + imageWidth + " - " + imageHeight);
        double scale = 1;
        // 计算长宽缩放比
        double scaleX = imageWidth / (screenWidth * 1.0);
        double scaleY = imageHeight / (screenHeight * 1.0);
        Log.e("AAA", "缩放比为:" + scale);
        // 那个比例大就用哪个比例
        if (scaleX > scaleY && scaleX > scale) {
            scale = (screenWidth * 1.0) / imageHeight;
            Log.e("AAA", "" + 1);
        } else if (scaleY > scaleX && scaleY > scale) {
            scale = (screenHeight * 1.0) / imageWidth;
            Log.e("AAA", "" + 2);
        }
        Log.e("AAA", "手机的长宽为 ： " + screenWidth + " - " + screenHeight);
        Log.e("AAA", "缩放比为:" + scale);

        bitmap = Bitmap.createScaledBitmap(bitmap, (int) (scale * imageWidth) / 4, (int) (scale * imageHeight) / 4, true);

        return bitmap;
    }

}
