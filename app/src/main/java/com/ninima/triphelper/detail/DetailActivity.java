package com.ninima.triphelper.detail;

import android.app.DatePickerDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ninima.triphelper.R;
import com.ninima.triphelper.databinding.ActivityDetailBinding;
import com.ninima.triphelper.detail.memo.EditMemoActivity;
import com.ninima.triphelper.detail.memo.MemoFragment;
import com.ninima.triphelper.detail.spend.EditSpendActivity;
import com.ninima.triphelper.detail.spend.SpendFragment;
import com.ninima.triphelper.model.Trip;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    Context context = this;
    public DetailViewModel viewModel;
    Trip trip;

    ActivityDetailBinding binding;

    TextView dateNotice;
    LinearLayout dateLayout;
    long tid;

    Bitmap bitmap;
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
        setContentView(R.layout.activity_detail);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail);
        binding.setTrip(trip);

        Intent intent = getIntent();
        tid = intent.getLongExtra("tid",-1);
        String bartitle = intent.getStringExtra("trip_title");

        DetailViewModel.DetailViewModelFactory factory = new DetailViewModel.DetailViewModelFactory(tid);
        viewModel = ViewModelProviders.of(this, factory)
                .get(DetailViewModel.class);

        viewModel.trip.observe(this, new Observer<Trip>() {
            @Override
            public void onChanged(@Nullable Trip trip) {
                setupTripDetail(trip);
            }
        });


        backImg = (ImageView)findViewById(R.id.htab_header);
        dateNotice = (TextView)findViewById(R.id.notice_tv);
        dateLayout = (LinearLayout)findViewById(R.id.l_date);
        collapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.htab_collapse_toolbar);
        changeBarColor();

        final Toolbar toolbar = (Toolbar) findViewById(R.id.htab_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(bartitle);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.htab_viewpager);
        setupViewPager(viewPager);
        final TabLayout tabLayout = (TabLayout) findViewById(R.id.htab_tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                switch (tab.getPosition()) {
                    case 0:
                        // TODO: 31/03/17
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        binding.commentTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                dialog.setTitle("여행상세 정보");
                dialog.setMessage("여행에 대한 간략한 설명을 남겨주세요.");
                dialog.setCancelable(false);
                final EditText name = new EditText(context);
                if(trip.getComment()==null){
                    name.setHint("동반자, 컨셉 등");
                }else{
                    name.setText(trip.getComment());
                }
                dialog.setView(name);
                dialog.setPositiveButton("수정", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        binding.commentTv.setText(name.getText().toString());
                        trip.setComment(name.getText().toString());
                        viewModel.updateTrip(trip);
                        dialog.dismiss();
                    }
                });
                dialog.setNegativeButton("취소",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        binding.dateTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                if(trip.getStartDate()!=null) c.setTime(trip.getStartDate());
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day

                DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                binding.dateTv.setText(year + "/" + (monthOfYear + 1) + "/" + dayOfMonth);
                                c.set(Calendar.YEAR, year);
                                c.set(Calendar.MONTH, monthOfYear+1);
                                c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                trip.setStartDate(c.getTime());
                                viewModel.updateTrip(trip);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();

            }
        });

        binding.date2Tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                if(trip.getEndDate()!=null) c.setTime(trip.getEndDate());
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day

                DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                binding.date2Tv.setText(year + "/" + (monthOfYear + 1) + "/" + dayOfMonth);
                                c.set(Calendar.YEAR, year);
                                c.set(Calendar.MONTH, monthOfYear+1);
                                c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                trip.setEndDate(c.getTime());
                                viewModel.updateTrip(trip);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        binding.placeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                dialog.setTitle("여행상세 정보");
                dialog.setMessage("방문 지역을 입력해주세요.");
                dialog.setCancelable(false);
                final EditText name = new EditText(context);
                if(trip.getPlace()==null){
                    name.setHint("한국");
                }else{
                    name.setHint(trip.getPlace());
                }
                dialog.setView(name);
                dialog.setPositiveButton("수정", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        binding.placeTv.setText(name.getText().toString());
                        trip.setPlace(name.getText().toString());
                        viewModel.updateTrip(trip);
                        dialog.dismiss();
                    }
                });
                dialog.setNegativeButton("취소",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });


        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(DetailActivity.this);
                dialog.setMessage("변경할 배경을 선택하세요");
                dialog.setCancelable(true);
                dialog.setPositiveButton("앨범선택", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectGallery();
                        dialog.dismiss();
                    }
                });
                dialog.setNeutralButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.setNegativeButton("사진촬영", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectCamera();
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        dateNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                if(trip.getStartDate()!=null) c.setTime(trip.getStartDate());
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day

                DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                c.set(Calendar.YEAR, year);
                                c.set(Calendar.MONTH, monthOfYear+1);
                                c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                trip.setStartDate(c.getTime());
                                viewModel.updateTrip(trip);
                                binding.dateTv.setText(year + "/" + (monthOfYear + 1) + "/" + dayOfMonth);
                                dateLayout.setVisibility(View.VISIBLE);
                                dateNotice.setText("");
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();

                final Calendar cal = Calendar.getInstance();
                if(trip.getEndDate()!=null) cal.setTime(trip.getEndDate());
                int nYear = cal.get(Calendar.YEAR); // current year
                int nMonth = cal.get(Calendar.MONTH); // current month
                int nDay = cal.get(Calendar.DAY_OF_MONTH); // current day

                DatePickerDialog datePickerDialog2 = new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                cal.set(Calendar.YEAR, year);
                                cal.set(Calendar.MONTH, monthOfYear+1);
                                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                trip.setEndDate(cal.getTime());
                                viewModel.updateTrip(trip);
                                binding.date2Tv.setText(year + "/" + (monthOfYear + 1) + "/" + dayOfMonth);
                                dateLayout.setVisibility(View.VISIBLE);
                                dateNotice.setText("");
                            }
                        }, nYear, nMonth, nDay);
                datePickerDialog2.show();
            }
        });

    }

    private void setupTripDetail(Trip t){

        trip = t;
        binding.setTrip(t);

        if(TextUtils.isEmpty(trip.getComment())){//trip.getComment()==null || trip.getComment().isEmpty()){
            binding.commentTv.setText("여행에 대한 간략한 설명을 남겨주세요.");
        }else{
            binding.commentTv.setText(trip.getComment());
        }

        if(TextUtils.isEmpty(trip.getPlace())){
            binding.placeTv.setText("방문하는 지역을 남겨주세요.");
        }else{
            binding.placeTv.setText(trip.getPlace());
        }

        if(trip.getStartDate()==null){
            dateLayout.setVisibility(View.INVISIBLE);
            dateNotice.setText("여행 기간을 입력해 주세요.");
        }else{
            dateLayout.setVisibility(View.VISIBLE);
            dateNotice.setText("");
        }

        String bi = trip.getPicUri();
        if(!TextUtils.isEmpty(bi)){
            BASIC=-1;
            photoUri = Uri.parse(bi);
            backImg.setImageURI(photoUri);
            changeBarColor();
        }


    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new SpendFragment(tid), "지출상세");
        adapter.addFrag(new StatisticsFragment(), "지출통계");
        adapter.addFrag(new MemoFragment(), "메모");
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_add:
                AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                dialog.setTitle("기록 추가");
                dialog.setMessage("메모와 지출 중 하나를 선택해 주세요.");
                dialog.setCancelable(true);
                dialog.setPositiveButton("지출", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Intent intent = new Intent(context, EditSpendActivity.class);
                        intent.putExtra("tid", trip.getRegisterTime());
                        context.startActivity(intent);
                        dialog.dismiss();
                    }
                });
                dialog.setNegativeButton("메모",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Intent intent = new Intent(context, EditMemoActivity.class);
                        intent.putExtra("tid", trip.getRegisterTime());
                        context.startActivity(intent);
                        dialog.dismiss();
                    }
                });
                dialog.show();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    private static class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


    //전체 바 색 다르게하기
    private void changeBarColor(){
        try {
            if(BASIC != -1){
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sky);
            }else{
                bitmap = ((BitmapDrawable)backImg.getDrawable()).getBitmap();
            }
            Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                @SuppressWarnings("ResourceType")
                @Override
                public void onGenerated(Palette palette) {
                    int vibrantColor = palette.getVibrantColor(R.color.sbDefault);
                    if(getColorHashCode(vibrantColor).equals("#7f05007a"))
                        Toast.makeText(DetailActivity.this, "색 추출 실패", Toast.LENGTH_LONG).show();
                    collapsingToolbarLayout.setContentScrimColor(removeAlphaProperty(vibrantColor));
                    changeStatusBarColor(removeAlphaProperty(vibrantColor));
                }
            });

        } catch (Exception e) {
            Toast.makeText(DetailActivity.this, "색 추출 실패", Toast.LENGTH_LONG).show();
            collapsingToolbarLayout.setContentScrimColor(
                    ContextCompat.getColor(this, R.color.hihihi)
            );
            changeStatusBarColor(R.color.hihihi);
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
        photoUri = contentUri;
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
        File dir = new File(Environment.getExternalStorageDirectory() + "/Pictures", "TripHelper");
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
        if(resultCode != RESULT_OK) return;
        switch (requestCode){
            case PICK_FROM_ALBUM:
                Log.e("activity result", data.getData().toString());
                sendPicture(data.getData()); //갤러리에서 가져오기
                photoUri=data.getData();
                BASIC=-1;
                changeBarColor();
                trip.setPicUri(photoUri.toString());
                viewModel.updateTrip(trip);
                break;
            case PICK_FROM_CAMERA:
                getPictureForPhoto(); //카메라에서 가져오기
                galleryAddPic();
                BASIC=-1;
                changeBarColor();
                trip.setPicUri(photoUri.toString());
                viewModel.updateTrip(trip);
                break;
            default:
                break;
        }

    }

}
