package com.ninima.triphelper.main;

import android.Manifest;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.ninima.triphelper.R;
import com.ninima.triphelper.model.Trip;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private MainViewModel viewModel;

    Toolbar toolbar;
    //TripAdapter mAdapter;
    //List<Trip> tripList = new List<>();
    ImageView backImg;
    private static final int PICK_FROM_CAMERA = 0000;
    private static final int PICK_FROM_ALBUM = 1111;
    private Uri photoUri;
    private String currentPhotoPath;//실제 사진 파일 경로
    String mImageCaptureName;//이미지 이름
    CollapsingToolbarLayout collapsingToolbarLayout;
    private final int NO_ALPHA_MASKING = 0xFF000000;
    private int BASIC = 123456;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //final TextView textView = findViewById(R.id.test);

        //액티비티와 생명주기를 함께하는 뷰모델 객체, 뷰모델은 액티비티가 끝나면 같이 끝나서 이미 finish 된 뷰에 ui 작업하는걸 걱정하지 않아도 됨
        viewModel = ViewModelProviders.of(this)
                .get(MainViewModel.class);

//        //뷰모델에 만들어 놓은 LiveData 를 observe(구독, 관찰?)함
//        //디비에 정보가 바뀔 때 마다 onChanged 호출
//        viewModel.trip.observe(this, new Observer<Trip>() {
//            @Override
//            public void onChanged(@Nullable Trip trip) {
//                //내부는 UI 쓰레드라서 ui 건드려도 상관없음(정확히 말하면 ui 작업하라고 있는거)
//                String result = "no result";
//                if(trip != null) {
//                    result = trip.getRegisterTime()+"";
//                }
//                textView.setText(result);
//            }
//        });
//
//        (findViewById(R.id.test)).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //백그라운드 쓰레드 호출은 뷰모델에 맡기고 밖에서는 호출만(코드 여기저기서 쓰레드만들면 보기 더러울거같아서)
//                viewModel.insertNewTrip(new Trip());
//            }
//        });


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);//트루면 백버튼이 생김

        collapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.collapsingToolbarLayout);
        changeBarColor();

        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                //Toast.makeText(MainActivity.this, "권한 허가", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                //Toast.makeText(MainActivity.this, "권한 거부\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }
        };
        TedPermission.with(this)
                .setPermissionListener(permissionlistener)
                .setRationaleMessage("카메라/앨범 사용 권한")
                .setDeniedMessage("왜 거부하셨어요...\n하지만 [설정] > [권한] 에서 권한을 허용할 수 있어요.")
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .check();

    }

    //메뉴바 적용한것
    @Override public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_toolbar, menu);
        return true;
    }

    //메뉴바 아이템이 클릭되었을때 동작
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add :
                show();
                return true ;
            default :
                return super.onOptionsItemSelected(item) ;
        }
    }

    //메뉴바에서 새 여행 추가할때 띄우는 다이얼로그
    void show() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        LayoutInflater inflater = getLayoutInflater();
//        View view = inflater.inflate(R.layout.main_dialog, null);
//        builder.setView(view);
//        final Button dialAddBtn = (Button) view.findViewById(R.id.add_btn);
//        final EditText title = (EditText) view.findViewById(R.id.et1);
//        final EditText place= (EditText) view.findViewById(R.id.et2);
//
//        final AlertDialog dialog = builder.create();
//        dialAddBtn.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//
//                Trip trips = new Trip(title.getText().toString(), place.getText().toString());
//                tripList.add(trips);
//                mAdapter.notifyDataSetChanged();
//                //Toast.makeText(getApplicationContext(), city1.getText().toString()+city2.getText().toString(),Toast.LENGTH_LONG).show();
//
//                dialog.dismiss();
//            }
//        });
//
//        dialog.show();

    }

    //전체 바 색 다르게하기
    private void changeBarColor(){
        try {
            Bitmap bitmap;
            if(BASIC != -1){
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.city);
            }else{
                bitmap = BitmapFactory.decodeFile(getRealPathFromURI(photoUri));
            }
            Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                @SuppressWarnings("ResourceType")
                @Override
                public void onGenerated(Palette palette) {
                    int vibrantColor = palette.getVibrantColor(R.color.sbDefault);
                    int vibrantLightColor = palette.getDarkVibrantColor(R.color.tbDefault);

                    if(getColorHashCode(vibrantColor).equals("#7f060091"))
                        Toast.makeText(MainActivity.this, "색 추출 실패", Toast.LENGTH_LONG).show();

                    collapsingToolbarLayout.setContentScrimColor(removeAlphaProperty(vibrantColor));
                    changeStatusBarColor(removeAlphaProperty(vibrantColor));
                }
            });

        } catch (Exception e) {
            Toast.makeText(MainActivity.this, "색 추출 실패", Toast.LENGTH_LONG).show();
            collapsingToolbarLayout.setContentScrimColor(
                    ContextCompat.getColor(this, R.color.tbDefault)
            );
            changeStatusBarColor(R.color.sbDefault);
        }
    }

    private String getColorHashCode(int color){
        return "#" + Integer.toHexString(color);
    }
    private int removeAlphaProperty(int color){
        return color | NO_ALPHA_MASKING;
    }

    private void changeStatusBarColor(int color){
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
        }
    }


    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(currentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
        Toast.makeText(this, "사진이 저장되었습니다", Toast.LENGTH_SHORT).show();
    }

    private void selectGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }

    private String getRealPathFromURI(Uri contentUri) {
        int column_index=0;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if(cursor.moveToFirst()){
            column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        }
        return cursor.getString(column_index);
    }

    private void sendPicture(Uri imgUri) {

        String imagePath = getRealPathFromURI(imgUri); // path 경로
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);//경로를 통해 비트맵으로 전환
        backImg.setImageBitmap(bitmap);
    }

    private File createImageFile() throws IOException {
        File dir = new File(Environment.getExternalStorageDirectory() + "/Pictures", "TestScrollView");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        mImageCaptureName = timeStamp + ".png";

        File storageDir = new File(dir, mImageCaptureName);
        currentPhotoPath = storageDir.getAbsolutePath();

        return storageDir;

    }

    private void selectCamera() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (intent.resolveActivity(getPackageManager()) != null) {
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {

                }
                if (photoFile != null) {
                    photoUri = FileProvider.getUriForFile(this, getPackageName(), photoFile);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                    startActivityForResult(intent, PICK_FROM_CAMERA);
                }
            }

        }
    }

    private void getPictureForPhoto() {
        Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath);
        backImg.setImageBitmap(bitmap);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //backImg.setImageURI(data.getData());
        Log.e("activity result", resultCode + "");
        if(resultCode != RESULT_OK) return;
        Log.e("activity result", requestCode + "");
        switch (requestCode){
            case PICK_FROM_ALBUM:
                Log.e("activity result", data.getData().toString());
                sendPicture(data.getData()); //갤러리에서 가져오기
                photoUri=data.getData();
                changeBarColor();
                break;
            case PICK_FROM_CAMERA:
                getPictureForPhoto(); //카메라에서 가져오기
                galleryAddPic();
                changeBarColor();
                break;
            default:
                break;
        }

    }
}