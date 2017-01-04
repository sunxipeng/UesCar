package activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.usecar.uescar.R;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import adapter.MyExpendableListAdapter;
import zxing.activity.CaptureActivity;

public class MainActivity extends BaseActivity implements View.OnClickListener{


    private TextView bt_left;
    private TextView bt_main;
    private DrawerLayout drawerLayout;
    private ExpandableListView expandlist;
    private List groupData;
    private List<String> childItems1;
    private List<String> childItems2;
    private List<String> childItems3;
    private Map<Integer, List<String>> childData;
    private MyExpendableListAdapter myAdapter;
    //private ImageView iv_twocode;
    private EditText et_opname, et_code, et_message, et_tool;
    private Button bt_scan;
    private TextView tv_add;
    private List<String> childitems4;
    private List<String> childitems5;
    private List<String> childitems6;
    private List<String> childitems7;
    private List<String> childitems8;
    private List<String> childitems9;
    private String str;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {

        bt_left = (TextView) findViewById(R.id.bt_left);
        bt_left.setOnClickListener(this);
        bt_main = (TextView) findViewById(R.id.bt_main);
        bt_main.setOnClickListener(this);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        /*iv_twocode = (ImageView) findViewById(R.id.iv_twocode);
        iv_twocode.setOnLongClickListener(this);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.test_erweima);
        iv_twocode.setImageBitmap(bitmap);*/
        initdata();
        initview();

    }


    private void initview() {
        et_opname = (EditText) findViewById(R.id.et_opname);
        et_code = (EditText) findViewById(R.id.et_code);
        et_message = (EditText) findViewById(R.id.et_message);
        et_tool = (EditText) findViewById(R.id.et_tool);

        bt_scan = (Button) findViewById(R.id.bt_scan);
        bt_scan.setOnClickListener(this);

        expandlist = (ExpandableListView) findViewById(R.id.expandlist);

        View view = LayoutInflater.from(this).inflate(R.layout.footview,null);
        tv_add = (TextView) view.findViewById(R.id.tv_add);
        tv_add.setOnClickListener(this);
        expandlist.addFooterView(view);
        expandlist.setAdapter(myAdapter);
        registerForContextMenu(expandlist);//给ExpandListView添加上下文菜单

        expandlist.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {

                if (childData.get(i).get(i1).equals("添加")) {

                    //弹出弹窗,让用户输入数据
                    showInsertLinkDialog(i);
                } else {

                    Toast.makeText(MainActivity.this, "我被点击了,我是父布局: "+i +"::" +childData.get(i).get(i1), Toast.LENGTH_SHORT).show();
                }


                return true;
            }
        });

    }

    private void initdata() {

        groupData = new ArrayList<String>();
        groupData.add("一级分类");
        groupData.add("二级分类");
        groupData.add("三级分类");

        childItems1 = new ArrayList<String>();
        childItems1.add("苹果");
        childItems1.add("苹果");
        childItems1.add("苹果");
        childItems1.add("添加");

        childItems2 = new ArrayList<String>();
        childItems2.add("苹果");
        childItems2.add("苹果");
        childItems2.add("苹果");
        childItems2.add("添加");

        childItems3 = new ArrayList<String>();
        childItems3.add("苹果");
        childItems3.add("苹果");
        childItems3.add("苹果");
        childItems3.add("添加");

        childData = new HashMap<Integer, List<String>>();
        childData.put(0, childItems1);
        childData.put(1, childItems2);
        childData.put(2, childItems3);
        myAdapter = new MyExpendableListAdapter(this, groupData, childData);
    }

    @Override
    protected void loadData() {

    }

    boolean flag1 = true;
    boolean flag2 = false;
    boolean flag3 = false;
    boolean flag4 = false;
    boolean flag5 = false;
    boolean flag6 = false;

    boolean flag_parent = false;
    boolean flag_sun = false;
    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.bt_left:
                drawerLayout.openDrawer(Gravity.LEFT);
                Toast.makeText(this, "left", Toast.LENGTH_SHORT).show();

                break;

            case R.id.bt_main:
                drawerLayout.closeDrawer(Gravity.LEFT);
                Toast.makeText(this, "main", Toast.LENGTH_SHORT).show();
                //drawerLayout.closeDrawer(Gravity.RIGHT);
                break;
            case R.id.bt_scan:

                //打开扫描界面扫描条形码或二维码
                Intent openCameraIntent = new Intent(MainActivity.this, CaptureActivity.class);
                startActivityForResult(openCameraIntent, 0);
                break;

            case R.id.tv_add:

                showInsertLinkDialog(10000);

                break;
        }

    }
    private AlertDialog linkDialog;
    private void showInsertLinkDialog(final int i) {

        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        linkDialog = adb.create();

        View view = LayoutInflater.from(this).inflate(R.layout.dialog_new, null);

        final EditText et_editor = (EditText) view.findViewById(R.id.et_editor);
        //点击确实的监听
        view.findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                str = et_editor.getText().toString().trim();
                if(TextUtils.isEmpty(str)){
                    Toast.makeText(MainActivity.this,"输入不能为空",Toast.LENGTH_SHORT).show();
                }else {
                    if(i==10000){
                        //点击的父主件
                        addparent();
                    }else {

                        addsun(i);
                    }

                    linkDialog.dismiss();
                }

            }
        });
        //点击取消的监听
        view.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linkDialog.dismiss();
            }
        });
        linkDialog.setCancelable(false);
        linkDialog.setView(view, 0, 0, 0, 0); // 设置 view
        linkDialog.show();
    }


    private void addparent(){

        if(flag1){

            childitems4 = new ArrayList<>();
            childitems4.add("添加");
            childData.put(groupData.size(), childitems4);
            groupData.add(str);
            myAdapter.notifyDataSetChanged();
            flag1 = false;
            flag2 = true;

            return;
        }
        if(flag2){

            childitems5 = new ArrayList<>();
            childitems5.add("添加");
            childData.put(groupData.size(), childitems5);
            groupData.add(str);
            myAdapter.notifyDataSetChanged();
            flag2 = false;
            flag3 = true;
           return;

        }
        if(flag3){
            childitems6 = new ArrayList<>();
            childitems6.add("添加");
            childData.put(groupData.size(), childitems6);
            groupData.add(str);
            myAdapter.notifyDataSetChanged();
            flag3 = false;
            flag4 = true;

            return;

        }if(flag4){

            childitems7 = new ArrayList<>();
            childitems7.add("添加");
            childData.put(groupData.size(), childitems7);
            groupData.add(str);
            myAdapter.notifyDataSetChanged();
            flag4 = false;
            flag5 = true;

            return;
        }if(flag5){

            childitems8 = new ArrayList<>();
            childitems8.add("添加");
            childData.put(groupData.size(), childitems8);
            groupData.add(str);
            myAdapter.notifyDataSetChanged();
            flag5 = false;
            flag6 = true;

            return;
        }if(flag6){

            childitems9 = new ArrayList<>();
            childitems9.add("添加");
            childData.put(groupData.size(), childitems9);
            groupData.add(str);
            myAdapter.notifyDataSetChanged();
            flag6 = false;

            return;
        }
    }

    private void addsun(int i){

        if(i==0){
            childItems1.add(childItems1.size()-1,str);
            myAdapter.notifyDataSetChanged();
        }else if(i==1){
            childItems2.add(childItems2.size()-1,str);
            myAdapter.notifyDataSetChanged();
        }
        else if(i==2){
            childItems3.add(childItems3.size()-1,str);
            myAdapter.notifyDataSetChanged();
        }else if(i==3){
            childitems4.add(childitems4.size()-1,str);
            myAdapter.notifyDataSetChanged();
        }else if(i==4){
            childitems5.add(childitems5.size()-1,str);
            myAdapter.notifyDataSetChanged();
        }else if(i==5){
            childitems6.add(childitems6.size()-1,str);
            myAdapter.notifyDataSetChanged();
        }else if(i==6){
            childitems7.add(childitems7.size()-1,str);
            myAdapter.notifyDataSetChanged();
        }else if(i==7){
            childitems8.add(childitems8.size()-1,str);
            myAdapter.notifyDataSetChanged();
        }else if(i==8){
            childitems9.add(childitems9.size()-1,str);
            myAdapter.notifyDataSetChanged();
        }
    }


   /* @Override
    public boolean onLongClick(View view) {
        switch (view.getId()) {

            case R.id.iv_twocode:
                //开始识别二维码
               // saveCurrentImage();
                break;
        }
        return true;
    }*/

    private String time;
    private File file = null;

   /* private void saveCurrentImage() {
        //获取当前屏幕的大小
        int width = getWindow().getDecorView().getRootView().getWidth();
        int height = getWindow().getDecorView().getRootView().getHeight();
        //生成相同大小的图片
        Bitmap temBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        //找到当前页面的根布局
        View view = getWindow().getDecorView().getRootView();
        //设置缓存
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        //从缓存中获取当前屏幕的图片,创建一个DrawingCache的拷贝，因为DrawingCache得到的位图在禁用后会被回收
        temBitmap = view.getDrawingCache();
        SimpleDateFormat df = new SimpleDateFormat("yyyymmddhhmmss");
        time = df.format(new Date());
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/screen", time + ".png");
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(file);
                temBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.flush();
                fos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/screen/" + time + ".png";
                    final Result result = parseQRcodeBitmap(path);
                    runOnUiThread(new Runnable() {
                        public void run() {
                            if (null != result) {

                                et_opname.setText(result.toString());
                                et_code.setText(result.toString());
                                et_message.setText(result.toString());
                                et_tool.setText(result.toString());
                                Toast.makeText(MainActivity.this, result.toString(), Toast.LENGTH_SHORT).show();
                                ///resultTextView.setText(result.toString());
                            } else {
                                Toast.makeText(MainActivity.this, "无法识别", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }).start();
            //禁用DrawingCahce否则会影响性能 ,而且不禁止会导致每次截图到保存的是缓存的位图
            view.setDrawingCacheEnabled(false);
        }
    }*/

    //解析二维码图片,返回结果封装在Result对象中
    private Result parseQRcodeBitmap(String bitmapPath) {
        //解析转换类型UTF-8
        Hashtable<DecodeHintType, String> hints = new Hashtable<DecodeHintType, String>();
        hints.put(DecodeHintType.CHARACTER_SET, "utf-8");
        //获取到待解析的图片
        BitmapFactory.Options options = new BitmapFactory.Options();
        //如果我们把inJustDecodeBounds设为true，那么BitmapFactory.decodeFile(String path, Options opt)
        //并不会真的返回一个Bitmap给你，它仅仅会把它的宽，高取回来给你
        options.inJustDecodeBounds = true;
        //此时的bitmap是null，这段代码之后，options.outWidth 和 options.outHeight就是我们想要的宽和高了
        Bitmap bitmap = BitmapFactory.decodeFile(bitmapPath, options);
        //我们现在想取出来的图片的边长（二维码图片是正方形的）设置为400像素
        /**
         options.outHeight = 400;
         options.outWidth = 400;
         options.inJustDecodeBounds = false;
         bitmap = BitmapFactory.decodeFile(bitmapPath, options);
         */
        //以上这种做法，虽然把bitmap限定到了我们要的大小，但是并没有节约内存，如果要节约内存，我们还需要使用inSimpleSize这个属性
        options.inSampleSize = options.outHeight / 400;
        if (options.inSampleSize <= 0) {
            options.inSampleSize = 1; //防止其值小于或等于0
        }
        /**
         * 辅助节约内存设置
         *
         * options.inPreferredConfig = Bitmap.Config.ARGB_4444;    // 默认是Bitmap.Config.ARGB_8888
         * options.inPurgeable = true;
         * options.inInputShareable = true;
         */
        options.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeFile(bitmapPath, options);
        //新建一个RGBLuminanceSource对象，将bitmap图片传给此对象
        RGBLuminanceSource rgbLuminanceSource = new RGBLuminanceSource(bitmap);
        //将图片转换成二进制图片
        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(rgbLuminanceSource));
        //初始化解析对象
        QRCodeReader reader = new QRCodeReader();
        //开始解析
        Result result = null;
        try {
            result = reader.decode(binaryBitmap, hints);
        } catch (Exception e) {
            // TODO: handle exception
        }

        return result;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //处理扫描结果（在界面上显示）
        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString("result");
            et_opname.setText(scanResult);
        }
    }
}
