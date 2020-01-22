package com.ando.download.base;

import android.graphics.Color;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ando.download.R;
import com.yanzhenjie.recyclerview.OnItemMenuClickListener;
import com.yanzhenjie.recyclerview.SwipeMenu;
import com.yanzhenjie.recyclerview.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.SwipeMenuItem;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;
import com.yanzhenjie.recyclerview.widget.DefaultItemDecoration;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Title: BaseSwipeItemActivity
 * <p>
 * Description:
 * </p>
 *
 * @author Changbao
 * @date 2020/1/22  15:39
 */
public abstract class BaseSwipeItemActivity extends AppCompatActivity {

    protected SwipeRecyclerView mRecyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        mRecyclerView = findViewById(R.id.recyclerViewSwipe);
        mRecyclerView.setLayoutManager(createLayoutManager());
        mRecyclerView.addItemDecoration(createItemDecoration());


        mRecyclerView.setSwipeMenuCreator(swipeMenuCreator);
        mRecyclerView.setOnItemMenuClickListener(mMenuItemClickListener);

        initViews(savedInstanceState);
    }

    protected abstract void initViews(Bundle savedInstanceState);

    protected abstract int getLayoutId();

    protected abstract void onDeleteClick(SwipeMenuBridge menuBridge, int position, int menuPosition);

    protected RecyclerView.LayoutManager createLayoutManager() {
        return new LinearLayoutManager(this);
    }

    protected RecyclerView.ItemDecoration createItemDecoration() {
        return new DefaultItemDecoration(ContextCompat.getColor(this, R.color.divider_color));
    }


    /**
     * 菜单创建器，在Item要创建菜单的时候调用。
     */
    protected SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {
        @Override
        public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int position) {
            int width = dp2px(90F);

            // 1. MATCH_PARENT 自适应高度，保持和Item一样高;
            // 2. 指定具体的高，比如80;
            // 3. WRAP_CONTENT，自身高度，不推荐;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;

            // 添加左侧的，如果不添加，则左侧不会出现菜单。
            {
//                SwipeMenuItem addItem = new SwipeMenuItem(BaseSwipeItemActivity.this).setBackground(R.drawable.selector_red)
//                        .setImage(R.drawable.ic_action_add)
//                        .setWidth(width)
//                        .setHeight(height);
//                swipeLeftMenu.addMenuItem(addItem); // 添加菜单到左侧。

//                SwipeMenuItem closeItem = new SwipeMenuItem(BaseSwipeItemActivity.this).setBackground(R.drawable.selector_red)
//                        .setImage(R.drawable.ic_action_close)
//                        .setWidth(width)
//                        .setHeight(height);
//                swipeLeftMenu.addMenuItem(closeItem); // 添加菜单到左侧。
            }

            // 添加右侧的，如果不添加，则右侧不会出现菜单。
            {
                SwipeMenuItem deleteItem = new SwipeMenuItem(BaseSwipeItemActivity.this).setBackground(R.drawable.selector_red)
                        .setImage(R.drawable.ic_action_delete)
                        .setText("删除")
                        .setTextColor(Color.WHITE)
                        .setWidth(width)
                        .setHeight(height);
                swipeRightMenu.addMenuItem(deleteItem);// 添加菜单到右侧。

//                SwipeMenuItem addItem = new SwipeMenuItem(BaseSwipeItemActivity.this).setBackground(R.drawable.selector_green)
//                        .setText("添加")
//                        .setTextColor(Color.WHITE)
//                        .setWidth(width)
//                        .setHeight(height);
//                swipeRightMenu.addMenuItem(addItem); // 添加菜单到右侧。
            }
        }
    };

    /**
     * RecyclerView的Item的Menu点击监听。
     */
    private OnItemMenuClickListener mMenuItemClickListener = new OnItemMenuClickListener() {
        @Override
        public void onItemClick(SwipeMenuBridge menuBridge, int position) {
            menuBridge.closeMenu();

            int direction = menuBridge.getDirection(); // 左侧还是右侧菜单
            int menuPosition = menuBridge.getPosition(); // 菜单在RecyclerView的Item中的Position

            if (direction == SwipeRecyclerView.RIGHT_DIRECTION) {
                Toast.makeText(BaseSwipeItemActivity.this, "list第" + position + "; 右侧菜单第" + menuPosition, Toast.LENGTH_SHORT).show();
                onDeleteClick(menuBridge, position, menuPosition);
            } else if (direction == SwipeRecyclerView.LEFT_DIRECTION) {
                Toast.makeText(BaseSwipeItemActivity.this, "list第" + position + "; 左侧菜单第" + menuPosition, Toast.LENGTH_SHORT).show();
            }
        }
    };

    private int dp2px(float dpValue) {
        float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}