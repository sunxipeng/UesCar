package activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.usecar.uescar.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import zxing.activity.CaptureActivity;
import zxing.encoding.EncodingHandler;

public class BarCodeTestActivity extends Activity {
   
	private TextView resultTextView;
	private EditText qrStrEditText;
	private ImageView qrImgImageView;
	private String time;
    private File file = null;	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);        
        resultTextView = (TextView) this.findViewById(R.id.tv_scan_result);
        qrStrEditText = (EditText) this.findViewById(R.id.et_qr_string);
        qrImgImageView = (ImageView) this.findViewById(R.id.iv_qr_image);
        
        Button scanBarCodeButton = (Button) this.findViewById(R.id.btn_scan_barcode);
        scanBarCodeButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//打开扫描界面扫描条形码或二维码
				Intent openCameraIntent = new Intent(BarCodeTestActivity.this,CaptureActivity.class);
				startActivityForResult(openCameraIntent, 0);
			}
		});
        
        qrImgImageView.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				// 长按识别二维码
				
			   saveCurrentImage();
				return true;
			}
		});
        
        

        
        Button generateQRCodeButton = (Button) this.findViewById(R.id.btn_add_qrcode);
        generateQRCodeButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try {
					String contentString = qrStrEditText.getText().toString();
					if (!contentString.equals("")) {
						//根据字符串生成二维码图片并显示在界面上，第二个参数为图片的大小（350*350）
						Bitmap qrCodeBitmap = EncodingHandler.createQRCode(contentString, 350);
						qrImgImageView.setImageBitmap(qrCodeBitmap);
					}else {
						//提示文本不能是空的
						Toast.makeText(BarCodeTestActivity.this, "Text can not be empty", Toast.LENGTH_SHORT).show();
					}
					
				} catch (WriterException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
    }
 
    //这种方法状态栏是空白，显示不了状态栏的信息
    private void saveCurrentImage()
    {
        //获取当前屏幕的大小
        int width = getWindow().getDecorView().getRootView().getWidth();
        int height = getWindow().getDecorView().getRootView().getHeight();
        //生成相同大小的图片
        Bitmap temBitmap = Bitmap.createBitmap( width, height, Bitmap.Config.ARGB_8888 );
        //找到当前页面的根布局
        View view =  getWindow().getDecorView().getRootView();
        //设置缓存
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        //从缓存中获取当前屏幕的图片,创建一个DrawingCache的拷贝，因为DrawingCache得到的位图在禁用后会被回收
        temBitmap = view.getDrawingCache();
        SimpleDateFormat df = new SimpleDateFormat("yyyymmddhhmmss");
        time = df.format(new Date());
        if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
            file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/screen",time + ".png");
            if(!file.exists()){
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
                        	if(null!=result){
                        	resultTextView.setText(result.toString());
                        	}else{
                            Toast.makeText(BarCodeTestActivity.this, "无法识别", Toast.LENGTH_LONG).show();
                        	}
                        	}
                    });
                }
            }).start();
            //禁用DrawingCahce否则会影响性能 ,而且不禁止会导致每次截图到保存的是缓存的位图
            view.setDrawingCacheEnabled(false);
        }
    }
    
    
    
    
    
    //解析二维码图片,返回结果封装在Result对象中  
    private Result  parseQRcodeBitmap(String bitmapPath){
        //解析转换类型UTF-8  
        Hashtable<DecodeHintType, String> hints = new Hashtable<DecodeHintType, String>();  
        hints.put(DecodeHintType.CHARACTER_SET, "utf-8");  
        //获取到待解析的图片  
        BitmapFactory.Options options = new BitmapFactory.Options();   
        //如果我们把inJustDecodeBounds设为true，那么BitmapFactory.decodeFile(String path, Options opt)  
        //并不会真的返回一个Bitmap给你，它仅仅会把它的宽，高取回来给你  
        options.inJustDecodeBounds = true;  
        //此时的bitmap是null，这段代码之后，options.outWidth 和 options.outHeight就是我们想要的宽和高了  
        Bitmap bitmap = BitmapFactory.decodeFile(bitmapPath,options);  
        //我们现在想取出来的图片的边长（二维码图片是正方形的）设置为400像素  
        /** 
            options.outHeight = 400; 
            options.outWidth = 400; 
            options.inJustDecodeBounds = false; 
            bitmap = BitmapFactory.decodeFile(bitmapPath, options); 
        */  
        //以上这种做法，虽然把bitmap限定到了我们要的大小，但是并没有节约内存，如果要节约内存，我们还需要使用inSimpleSize这个属性  
        options.inSampleSize = options.outHeight / 400;  
        if(options.inSampleSize <= 0){  
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
			resultTextView.setText(scanResult);
		}
	}
}