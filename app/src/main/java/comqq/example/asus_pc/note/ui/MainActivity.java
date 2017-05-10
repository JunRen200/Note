package comqq.example.asus_pc.note.ui;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import org.litepal.crud.DataSupport;

import java.util.List;

import comqq.example.asus_pc.note.FabScrollListener;
import comqq.example.asus_pc.note.R;
import comqq.example.asus_pc.note.adapter.Myadapter;
import comqq.example.asus_pc.note.db.Notepad;

public class MainActivity extends AppCompatActivity implements FabScrollListener.HideScrollListener ,Myadapter.ToolbarSet {
    private Toolbar toolbar;
    private DrawerLayout mDrawerLayout;
    private RecyclerView recyclerView;
    private Myadapter myadapter;
    private List<Notepad> list;
    private NavigationView navigationView;
    private FloatingActionButton fab_add;
    final int ON_DELETE=1;
    final int NO_DELETE=0;
    private int state=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("笔记本");
        setSupportActionBar(toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.draw_layout);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.mipmap.ic_menu_sort_by_size);
        }
        initData();
        initView();
    }

    private void initData() {
        list = DataSupport.findAll(Notepad.class);
    }

    private void initView() {
        fab_add = (FloatingActionButton) findViewById(R.id.fab);
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        myadapter = new Myadapter(list,this);
        recyclerView.setAdapter(myadapter);
        LinearLayoutManager linear = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linear);
//        recyclerView.addItemDecoration(new DividerItemDecoration(MainActivity.this,DividerItemDecoration.VERTICAL));
        recyclerView.addOnScrollListener(new FabScrollListener(MainActivity.this));
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.nva_a);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                mDrawerLayout.closeDrawers();
                return true;
            }
        });
        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, activity_addNote.class);
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if(state==ON_DELETE) {
            menu.clear();
            getMenuInflater().inflate(R.menu.delete_menu, menu);
        }else if(state==NO_DELETE){
            menu.clear();
            getMenuInflater().inflate(R.menu.menu,menu);
        }
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if(state==NO_DELETE) {
                    mDrawerLayout.openDrawer(GravityCompat.START);
                }else if(state==ON_DELETE){
                    myadapter.setChoose_state();
                    ActionBar actionBar = getSupportActionBar();
                    if (actionBar != null) {
                        actionBar.setDisplayHomeAsUpEnabled(true);
                        actionBar.setHomeAsUpIndicator(R.mipmap.ic_menu_sort_by_size);
                    }
                    state=NO_DELETE;
                    supportInvalidateOptionsMenu();
                }
                break;
            case R.id.item_search:
                Intent intent = new Intent(MainActivity.this, Activity_search.class);
                startActivityForResult(intent,1);
                break;
            case R.id.item_delete:
                myadapter.deleteDate();
                break;
            case R.id.item_list:
                LinearLayoutManager linear = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);
                recyclerView.setLayoutManager(linear);
                break;
            case  R.id.item_thumb:
                GridLayoutManager grid=new GridLayoutManager(MainActivity.this,2);
                recyclerView.setLayoutManager(grid);
                break;
        }

        return true;
    }

    public void onHide() {
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) fab_add.getLayoutParams();
        ViewCompat.animate(fab_add).translationY(fab_add.getHeight() + layoutParams.bottomMargin + layoutParams.topMargin).setInterpolator(new AccelerateInterpolator(3));
    }

    public void onShow() {
        ViewCompat.animate(fab_add).translationY(0).setInterpolator(new DecelerateInterpolator(3));
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
                    initData();
                    myadapter.setlist(list);
                }
                break;
        }
    }


    private void No_DeleteState(){
        myadapter.setChoose_state();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.mipmap.ic_menu_sort_by_size);
        }
        state=NO_DELETE;
        supportInvalidateOptionsMenu();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK&&myadapter.getchoose_state()==Myadapter.ON_CHOOSE){
            No_DeleteState();
        }else if(keyCode==KeyEvent.KEYCODE_BACK){
            return super.onKeyDown(keyCode, event);
        }
        return true;
    }

    @Override
    public void ToolbarDelete() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.mipmap.ic_menu_back);
        }
        state=ON_DELETE;
        supportInvalidateOptionsMenu();
    }

    @Override
    public void openUpdate(Notepad notepad) {
        Intent intent=new Intent(MainActivity.this,activity_update.class);
        Bundle bundle=new Bundle();
        bundle.putSerializable("notepad",notepad);
        intent.putExtras(bundle);
        startActivityForResult(intent,1);
    }
}
