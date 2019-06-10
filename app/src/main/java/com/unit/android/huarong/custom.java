package com.unit.android.huarong;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnDragListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.Toast;

public class custom extends AppCompatActivity {
    ImageView[] blocks;
    RelativeLayout relativeLayout;
    Button reset, back, next;

    Position pass;
    Position curPos;

    int unit;

    TextView title;
    String passId;

    int[] w = {1, 1, 1, 1, 1, 1, 1, 1, 2, 2};
    int[] h = {1, 1, 1, 1, 2, 2, 2, 2, 1, 2};

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gamectivity);

        reset = findViewById(R.id.reset);
        back = findViewById(R.id.back);
        next = findViewById(R.id.next);

        Intent intent = getIntent();
        passId = intent.getStringExtra("passId");

        Log.i("increate", this.passId);

        sizeInit();
        positionInit();
        curPos = new Position(pass);

        renderAll();

        for (int i = 0; i < 10; i++) {
            blocks[i].setOnTouchListener(new View.OnTouchListener() {
                int lastX = 0, lastY = 0;
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    int x = (int) event.getX();
                    int y = (int) event.getY();
                    Log.i("onTouch", "onTouch");
                    Log.i("X", String.valueOf(x));
                    Log.i("Y", String.valueOf(y));

                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            lastX = x;
                            lastY = y;
                            Log.i("lastX", String.valueOf(x));
                            Log.i("lastY", String.valueOf(y));
                            break;
                        case MotionEvent.ACTION_MOVE:
                            int offsetXm = x - lastX;
                            int offsetYm = y - lastY;
                            v.setAlpha(0.7f);
                            setLayout(v, x, y);
                            break;
                        case MotionEvent.ACTION_UP:
                            int offsetX = x - lastX;
                            int offsetY = y - lastY;
                            v.setAlpha(1f);
                            fixLayout(v, x, y);
                            break;
                    }
                    return true;
                }
            });
        }
    }

    public void sizeInit() {
        Log.i("insizeInit", "sizeInit");
        relativeLayout = findViewById(R.id.relativeLayout);
        relativeLayout.measure(0, 0);
        WindowManager wm = (WindowManager) this
                .getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();

        int totalWidth = width - 32;
        int totalHeight = height - 700;
//        int totalWidth = relativeLayout.getWidth();
//        int totalHeight = relativeLayout.getHeight();
        Log.d("size", totalWidth + " " + " " + totalHeight);
        unit = (totalHeight / 5) < (totalWidth / 4) ? (totalHeight / 5) : (totalWidth / 4);
        Log.d("unit", String.valueOf(unit));
        relativeLayout.getLayoutParams().height = 5 * unit;
        relativeLayout.getLayoutParams().width = 4 * unit;

        blocks = new ImageView[10];

        blocks[0] = findViewById(R.id.s1);
        blocks[0].getLayoutParams().height = unit;
        blocks[0].getLayoutParams().width = unit;

        blocks[1] = findViewById(R.id.s2);
        blocks[1].getLayoutParams().height = unit;
        blocks[1].getLayoutParams().width = unit;

        blocks[2] = findViewById(R.id.s3);
        blocks[2].getLayoutParams().height = unit;
        blocks[2].getLayoutParams().width = unit;

        blocks[3] = findViewById(R.id.s4);
        blocks[3].getLayoutParams().height = unit;
        blocks[3].getLayoutParams().width = unit;

        blocks[4] = findViewById(R.id.v1);
        blocks[4].getLayoutParams().height = 2 * unit;
        blocks[4].getLayoutParams().width = unit;

        blocks[5] = findViewById(R.id.v2);
        blocks[5].getLayoutParams().height = 2 * unit;
        blocks[5].getLayoutParams().width = unit;

        blocks[6] = findViewById(R.id.v3);
        blocks[6].getLayoutParams().height = 2 * unit;
        blocks[6].getLayoutParams().width = unit;

        blocks[7] = findViewById(R.id.v4);
        blocks[7].getLayoutParams().height = 2 * unit;
        blocks[7].getLayoutParams().width = unit;

        blocks[8] = findViewById(R.id.h);
        blocks[8].getLayoutParams().height = unit;
        blocks[8].getLayoutParams().width = 2 * unit;

        blocks[9] = findViewById(R.id.c);
        blocks[9].getLayoutParams().height = 2 * unit;
        blocks[9].getLayoutParams().width = 2 * unit;
    }

    public void positionInit() {
        Log.i("inpositionInit", "positionInit");
        pass = new Position();
        title = findViewById(R.id.pass_title);
        title.setText("自定义布局");
        this.pass.addCoordinate(new Coordinate(0, 0));
        this.pass.addCoordinate(new Coordinate(0, 1));
        this.pass.addCoordinate(new Coordinate(0, 2));
        this.pass.addCoordinate(new Coordinate(0, 3));
        this.pass.addCoordinate(new Coordinate(1, 0));
        this.pass.addCoordinate(new Coordinate(3, 0));
        this.pass.addCoordinate(new Coordinate(1, 3));
        this.pass.addCoordinate(new Coordinate(3, 3));
        this.pass.addCoordinate(new Coordinate(1, 1));
        this.pass.addCoordinate(new Coordinate(2, 1));
    }

    void renderAll() {
        for (int i = 0; i < 10; i++) {
            setLayout(blocks[i], curPos.coordinates[i].x * unit, curPos.coordinates[i].y * unit);
        }
    }

    public static void setLayout(View view,int x,int y) {
        MarginLayoutParams margin=new MarginLayoutParams(view.getLayoutParams());
        margin.setMargins(x, y, 0, 0);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(margin);
        view.setLayoutParams(layoutParams);
    }

    public void fixLayout(View view, int x, int y) {

    }

    boolean isEmpty(int x, int y) {
        if (x > 3 || x < 0 || y > 4 || y < 0) {
            Log.i("isEmpty", x + "," + y + " is wall");
            return false;
        }
        else {
            for (int i = 0; i < 10; i++) {
                if (x - curPos.coordinates[i].x < w[i] && x - curPos.coordinates[i].x >= 0 && y - curPos.coordinates[i].y < h[i] && y - curPos.coordinates[i].y >= 0) {
                    Log.i("isEmpty", x + "," + y + " is not empty with block " + i);
                    return false;
                }
            }
            Log.i("isEmpty", x + "," + y + " is empty");
            return true;
        }
    }

    boolean checkWin() {
        if (curPos.coordinates[9].x == 1 && curPos.coordinates[9].y == 3) {
            Log.d("succcess", "success");
            showCongratulateDialog();
            return true;
        } else {
            return false;
        }
    }

    private void showCongratulateDialog() {
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(custom.this);
//                normalDialog.setIcon(R.drawable.congradulation);
        normalDialog.setTitle("过关啦！");
        normalDialog.setMessage("点击进入下一关");
        normalDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                next(title);
            }
        });
        normalDialog.show();
    }

    public void reset(View view) {
        curPos = new Position(pass);
        renderAll();
    }

    public void previous(View view) {
        int pId = Integer.valueOf(passId);
        if (pId == 1) {
            Toast toast = Toast.makeText(getApplicationContext(), "已经是第一关啦！", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } else {
            passId = String.valueOf(pId - 1);
            positionInit();
            reset(title);
        }
    }

    public void next(View view) {
        int pId = Integer.valueOf(passId);
        if (pId == 12) {
            Toast toast = Toast.makeText(getApplicationContext(), "已经是最后一关啦！", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } else {
            passId = String.valueOf(pId + 1);
            positionInit();
            reset(title);
        }
    }
}
