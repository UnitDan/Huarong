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

class Coordinate {
    int x;
    int y;

    Coordinate(int y, int x) {
        this.x = x;
        this.y = y;
    }

    Coordinate(Coordinate co) {
        this.x = co.x;
        this.y = co.y;
    }
}

class Position {
    Coordinate[] coordinates;
    int cur;

    Position() {
        this.coordinates = new Coordinate[10];
        this.cur = 0;
    }

    void addCoordinate(Coordinate index) {
        if (this.cur <= 9) {
            this.coordinates[cur] = index;
            this.cur += 1;
        }
    }

    Position(Position pos) {
        this.coordinates = new Coordinate[10];
        this.cur = 9;
        for (int i = 0; i < 10; i ++) {
            this.coordinates[i] = new Coordinate(pos.coordinates[i]);
        }
    }
}

public class gamectivity extends AppCompatActivity {
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
                        case MotionEvent.ACTION_UP:
                            int offsetX = x - lastX;
                            int offsetY = y - lastY;
                            Log.i("lastX1", String.valueOf(lastX));
                            Log.i("lastY1", String.valueOf(lastY));
                            Log.i("offsetX", String.valueOf(offsetX));
                            Log.i("offsetY", String.valueOf(offsetY));
                            if (offsetX > 3 * Math.abs(offsetY)) {
                                goRight((ImageView)v);
                            } else if (offsetX < -3 * Math.abs(offsetY)) {
                                goLeft((ImageView)v);
                            } else if (offsetY > 3 * Math.abs(offsetX)) {
                                goDown((ImageView)v);
                            } else if (offsetY < -3 * Math.abs(offsetX)) {
                                goUp((ImageView)v);
                            }
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
        switch (passId) {
            case "1":
                title.setText("第一关");
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
                break;
            case "2":
                title.setText("第二关");
                this.pass.addCoordinate(new Coordinate(3, 1));
                this.pass.addCoordinate(new Coordinate(3, 2));
                this.pass.addCoordinate(new Coordinate(4, 1));
                this.pass.addCoordinate(new Coordinate(4, 2));
                this.pass.addCoordinate(new Coordinate(0, 0));
                this.pass.addCoordinate(new Coordinate(2, 0));
                this.pass.addCoordinate(new Coordinate(0, 3));
                this.pass.addCoordinate(new Coordinate(2, 3));
                this.pass.addCoordinate(new Coordinate(2, 1));
                this.pass.addCoordinate(new Coordinate(0, 1));
                break;

            case "3":
                title.setText("第三关");
                this.pass.addCoordinate(new Coordinate(2, 0));
                this.pass.addCoordinate(new Coordinate(3, 1));
                this.pass.addCoordinate(new Coordinate(3, 2));
                this.pass.addCoordinate(new Coordinate(2, 3));
                this.pass.addCoordinate(new Coordinate(0, 0));
                this.pass.addCoordinate(new Coordinate(3, 0));
                this.pass.addCoordinate(new Coordinate(0, 3));
                this.pass.addCoordinate(new Coordinate(3, 3));
                this.pass.addCoordinate(new Coordinate(2, 1));
                this.pass.addCoordinate(new Coordinate(0, 1));
                break;

            case "4":
                title.setText("第四关");
                this.pass.addCoordinate(new Coordinate(2, 0));
                this.pass.addCoordinate(new Coordinate(2, 1));
                this.pass.addCoordinate(new Coordinate(2, 2));
                this.pass.addCoordinate(new Coordinate(2, 3));
                this.pass.addCoordinate(new Coordinate(0, 0));
                this.pass.addCoordinate(new Coordinate(3, 0));
                this.pass.addCoordinate(new Coordinate(0, 3));
                this.pass.addCoordinate(new Coordinate(3, 3));
                this.pass.addCoordinate(new Coordinate(3, 1));
                this.pass.addCoordinate(new Coordinate(0, 1));
                break;

            case "5":
                title.setText("第五关");
                this.pass.addCoordinate(new Coordinate(0, 0));
                this.pass.addCoordinate(new Coordinate(0, 3));
                this.pass.addCoordinate(new Coordinate(3, 1));
                this.pass.addCoordinate(new Coordinate(3, 2));
                this.pass.addCoordinate(new Coordinate(1, 0));
                this.pass.addCoordinate(new Coordinate(1, 3));
                this.pass.addCoordinate(new Coordinate(3, 0));
                this.pass.addCoordinate(new Coordinate(3, 3));
                this.pass.addCoordinate(new Coordinate(2, 1));
                this.pass.addCoordinate(new Coordinate(0, 1));
                break;

            case "6":
                title.setText("第六关");
                this.pass.addCoordinate(new Coordinate(3, 0));
                this.pass.addCoordinate(new Coordinate(3, 3));
                this.pass.addCoordinate(new Coordinate(4, 2));
                this.pass.addCoordinate(new Coordinate(4, 3));
                this.pass.addCoordinate(new Coordinate(1, 0));
                this.pass.addCoordinate(new Coordinate(2, 1));
                this.pass.addCoordinate(new Coordinate(2, 2));
                this.pass.addCoordinate(new Coordinate(1, 3));
                this.pass.addCoordinate(new Coordinate(4, 0));
                this.pass.addCoordinate(new Coordinate(0, 1));
                break;

            case "7":
                title.setText("第七关");
                this.pass.addCoordinate(new Coordinate(0, 0));
                this.pass.addCoordinate(new Coordinate(0, 3));
                this.pass.addCoordinate(new Coordinate(1, 0));
                this.pass.addCoordinate(new Coordinate(1, 3));
                this.pass.addCoordinate(new Coordinate(2, 0));
                this.pass.addCoordinate(new Coordinate(2, 1));
                this.pass.addCoordinate(new Coordinate(2, 2));
                this.pass.addCoordinate(new Coordinate(2, 3));
                this.pass.addCoordinate(new Coordinate(4, 1));
                this.pass.addCoordinate(new Coordinate(0, 1));
                break;

            case "8":
                title.setText("第八关");
                this.pass.addCoordinate(new Coordinate(0, 3));
                this.pass.addCoordinate(new Coordinate(1, 3));
                this.pass.addCoordinate(new Coordinate(2, 3));
                this.pass.addCoordinate(new Coordinate(3, 3));
                this.pass.addCoordinate(new Coordinate(0, 0));
                this.pass.addCoordinate(new Coordinate(2, 0));
                this.pass.addCoordinate(new Coordinate(2, 1));
                this.pass.addCoordinate(new Coordinate(2, 2));
                this.pass.addCoordinate(new Coordinate(4, 1));
                this.pass.addCoordinate(new Coordinate(0, 1));
                break;

            case "9":
                title.setText("第九关");
                this.pass.addCoordinate(new Coordinate(0, 3));
                this.pass.addCoordinate(new Coordinate(1, 3));
                this.pass.addCoordinate(new Coordinate(3, 1));
                this.pass.addCoordinate(new Coordinate(4, 1));
                this.pass.addCoordinate(new Coordinate(0, 0));
                this.pass.addCoordinate(new Coordinate(2, 0));
                this.pass.addCoordinate(new Coordinate(2, 3));
                this.pass.addCoordinate(new Coordinate(3, 2));
                this.pass.addCoordinate(new Coordinate(2, 1));
                this.pass.addCoordinate(new Coordinate(0, 1));
                break;

            case "10":
                title.setText("第十关");
                this.pass.addCoordinate(new Coordinate(0, 0));
                this.pass.addCoordinate(new Coordinate(0, 3));
                this.pass.addCoordinate(new Coordinate(1, 0));
                this.pass.addCoordinate(new Coordinate(1, 3));
                this.pass.addCoordinate(new Coordinate(2, 0));
                this.pass.addCoordinate(new Coordinate(2, 3));
                this.pass.addCoordinate(new Coordinate(3, 1));
                this.pass.addCoordinate(new Coordinate(3, 2));
                this.pass.addCoordinate(new Coordinate(2, 1));
                this.pass.addCoordinate(new Coordinate(0, 1));
                break;

            case "11":
                title.setText("第十一关");
                this.pass.addCoordinate(new Coordinate(0, 3));
                this.pass.addCoordinate(new Coordinate(1, 3));
                this.pass.addCoordinate(new Coordinate(4, 0));
                this.pass.addCoordinate(new Coordinate(4, 3));
                this.pass.addCoordinate(new Coordinate(0, 0));
                this.pass.addCoordinate(new Coordinate(2, 0));
                this.pass.addCoordinate(new Coordinate(2, 3));
                this.pass.addCoordinate(new Coordinate(3, 1));
                this.pass.addCoordinate(new Coordinate(2, 1));
                this.pass.addCoordinate(new Coordinate(0, 1));
                break;

            case "12":
                title.setText("第十二关");
                this.pass.addCoordinate(new Coordinate(2, 0));
                this.pass.addCoordinate(new Coordinate(3, 1));
                this.pass.addCoordinate(new Coordinate(3, 2));
                this.pass.addCoordinate(new Coordinate(2, 3));
                this.pass.addCoordinate(new Coordinate(0, 0));
                this.pass.addCoordinate(new Coordinate(0, 3));
                this.pass.addCoordinate(new Coordinate(3, 0));
                this.pass.addCoordinate(new Coordinate(3, 3));
                this.pass.addCoordinate(new Coordinate(2, 1));
                this.pass.addCoordinate(new Coordinate(0, 1));
                break;
        }
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

    public void goRight(ImageView v) {
        Log.i("goRight", "goRight");

        int index = -1;
        for (int i = 0; i < 10; i++) {
            if (blocks[i] == v) {
                index = i;
                break;
            }
        }
        Log.i("goRight index", String.valueOf(index));

        int rightX = curPos.coordinates[index].x + w[index] -1;
        Log.i("goLeft curPos",  "(" + (curPos.coordinates[index].x) + ", " + curPos.coordinates[index].y + ") ");
        int step = 1;
        if (h[index] == 1) {
            int Y = curPos.coordinates[index].y;
            if (isEmpty(rightX + step, Y)) {
                Log.i("goRight is empty",  "(" + (rightX + step) + ", " + Y + ") " + step + "\n");
            } else {
                Log.i("goRight is not empty",  "(" + (rightX + step) + ", " + Y + ") " + step + "\n");
                step  = 0;
                Log.i("goRight step ok",  "(" + (rightX + step) + ", " + Y + ") " + step + "\n");
            }
        } else if (h[index] == 2) {
            int Y1 = curPos.coordinates[index].y;
            int Y2 = curPos.coordinates[index].y + 1;
            if (isEmpty(rightX + step, Y1) && isEmpty(rightX + step, Y2)) {
                Log.i("goRight is empty",  "(" + (rightX + step) + ", " + Y1 + ") " + step + "\n");
            } else {
                Log.i("goRight is not empty",  "(" + (rightX + step) + ", " + Y1 + ") " + step + "\n");
                step = 0;
                Log.i("goRight step ok",  "(" + (rightX + step) + ", " + Y1 + ") " + step + "\n");
            }
        }

        curPos.coordinates[index].x += step;
        Log.i("goRight new x", curPos.coordinates[index].x + " + " + step);
        setLayout(blocks[index], curPos.coordinates[index].x * unit, curPos.coordinates[index].y * unit);
        checkWin();
    }

    public void goLeft(ImageView v) {
        Log.i("goLeft", "goLeft");

        int index = -1;
        for (int i = 0; i < 10; i++) {
            if (blocks[i] == v) {
                index = i;
                break;
            }
        }
        Log.i("goLeft index", String.valueOf(index));

        int leftX = curPos.coordinates[index].x;
        Log.i("goLeft curPos",  "(" + (leftX) + ", " + curPos.coordinates[index].y + ") ");
        int step = 1;
        if (h[index] == 1) {
            int Y = curPos.coordinates[index].y;
            if (isEmpty(leftX - step, Y)) {
                Log.i("goLeft is empty",  "(" + (leftX - step) + ", " + Y + ") " + step + "\n");
            } else {
                Log.i("goRight is not empty",  "(" + (leftX - step) + ", " + Y + ") " + step + "\n");
                step = 0;
                Log.i("goRight step ok",  "(" + (leftX - step) + ", " + Y + ") " + step + "\n");
            }
        } else if (h[index] == 2) {
            int Y1 = curPos.coordinates[index].y;
            int Y2 = curPos.coordinates[index].y + 1;
            if (isEmpty(leftX - step, Y1) && isEmpty(leftX - step, Y2)) {
                Log.i("goLeft is empty",  "(" + (leftX - step) + ", " + Y1 + ") " + step + "\n");
            } else {
                Log.i("goRight is not empty",  "(" + (leftX - step) + ", " + Y1 + ") " + step + "\n");
                step = 0;
                Log.i("goRight step ok",  "(" + (leftX - step) + ", " + Y1 + ") " + step + "\n");
            }
        }

        curPos.coordinates[index].x -= step;
        Log.i("goLeft new x", curPos.coordinates[index].x + " + " + step);
        setLayout(blocks[index], curPos.coordinates[index].x * unit, curPos.coordinates[index].y * unit);
        checkWin();
    }

    public void goUp(ImageView v) {
        Log.i("goUp", "goUp");

        int index = -1;
        for (int i = 0; i < 10; i++) {
            if (blocks[i] == v) {
                index = i;
                break;
            }
        }
        Log.i("goUp index", String.valueOf(index));

        int topY = curPos.coordinates[index].y;
        Log.i("goUp curPos",  "(" + (curPos.coordinates[index].x) + ", " + curPos.coordinates[index].y + ") ");
        int step = 1;
        if (w[index] == 1) {
            int X = curPos.coordinates[index].x;
            if (isEmpty(X, topY - step)) {
                Log.i("goUp is empty",  "(" + (X) + ", " + (topY - step) + ") " + step + "\n");
            } else {
                Log.i("goUp is not empty",  "(" + (X) + ", " + (topY - step) + ") " + step + "\n");
                step = 0;
                Log.i("goUp is ok",  "(" + (X) + ", " + (topY - step) + ") " + step + "\n");
            }
        } else if (w[index] == 2) {
            int X1 = curPos.coordinates[index].x;
            int X2 = curPos.coordinates[index].x + 1;
            if (isEmpty(X1, topY - step) && isEmpty(X2, topY - step)) {
                Log.i("goUp is empty",  "(" + (X1) + ", " + (topY - step) + ") " + step + "\n");
            } else {
                Log.i("goUp is not empty",  "(" + (X1) + ", " + (topY - step) + ") " + step + "\n");
                step = 0;
                Log.i("goUp is ok",  "(" + (X1) + ", " + (topY - step) + ") " + step + "\n");
            }
        }

        curPos.coordinates[index].y -= step;
        Log.i("goUp new y", curPos.coordinates[index].y + " + " + step);
        setLayout(blocks[index], curPos.coordinates[index].x * unit, curPos.coordinates[index].y * unit);
        checkWin();
    }

    public void goDown(ImageView v) {
        Log.i("goDown", "goDown");

        int index = -1;
        for (int i = 0; i < 10; i++) {
            if (blocks[i] == v) {
                index = i;
                break;
            }
        }
        Log.i("goDown index", String.valueOf(index));

        int bottomY = curPos.coordinates[index].y + h[index] - 1;
        Log.i("goDown curPos",  "(" + (curPos.coordinates[index].x) + ", " + curPos.coordinates[index].y + ") ");
        int step = 1;
        if (w[index] == 1) {
            int X = curPos.coordinates[index].x;
            if (isEmpty(X, bottomY + step)) {
                Log.i("goDown is empty",  "(" + (X) + ", " + (bottomY + step) + ") " + step + "\n");
            } else {
                Log.i("goDown is not empty",  "(" + (X) + ", " + (bottomY + step) + ") " + step + "\n");
                step = 0;
                Log.i("goDown is ok",  "(" + (X) + ", " + (bottomY + step) + ") " + step + "\n");
            }
        } else if (w[index] == 2) {
            int X1 = curPos.coordinates[index].x;
            int X2 = curPos.coordinates[index].x + 1;
            if (isEmpty(X1, bottomY + step) && isEmpty(X2, bottomY + step)) {
                Log.i("goDown is empty",  "(" + (X1) + ", " + (bottomY + step) + ") " + step + "\n");
            } else {
                Log.i("goDown is not empty",  "(" + (X1) + ", " + (bottomY + step) + ") " + step + "\n");
                step = 0;
                Log.i("goDown is ok",  "(" + (X1) + ", " + (bottomY + step) + ") " + step + "\n");
            }
        }

        curPos.coordinates[index].y += step;
        Log.i("goDown new y", curPos.coordinates[index].y + " + " + step);
        setLayout(blocks[index], curPos.coordinates[index].x * unit, curPos.coordinates[index].y * unit);
        checkWin();
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
                new AlertDialog.Builder(gamectivity.this);
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
