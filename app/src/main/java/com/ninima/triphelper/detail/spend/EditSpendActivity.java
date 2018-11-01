package com.ninima.triphelper.detail.spend;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.ninima.triphelper.R;
import com.ninima.triphelper.databinding.ActivityEditSpendBinding;
import com.ninima.triphelper.detail.spend.currency.CurrencyM;
import com.ninima.triphelper.model.CategoryM;
import com.ninima.triphelper.model.Spend;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class EditSpendActivity extends AppCompatActivity implements  DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener  {

    Context context = this;

    ActivityEditSpendBinding binding;
    Spend spend = new Spend();
    SpendViewModel viewModel;

    List<String> categoriesList;
    Spinner currencySpinner, categorySpinner;
    EditText categoryAddEt;
    Toolbar toolbar;

    Bitmap bitmap;
    ImageView img;
    private static final int PICK_FROM_CAMERA = 0000;
    private static final int PICK_FROM_ALBUM = 1111;
    private Uri photoUri=null;
    private String currentPhotoPath;//실제 사진 파일 경로
    String mImageCaptureName;//이미지 이름

    long tripId;
    int sid;
    boolean update = false;

    String title, place, detailM, category, currencyS;
    Float price;
    Date registerDate;
    int nYear, nMonth, nDay, nHour, nMinute;
    int  mYear, mMonth, mDay, mHour,mMinute, mSecond;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_edit_spend);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_spend);
        binding.setSpend(spend);

        categoryAddEt = (EditText)findViewById(R.id.categoryEt_editSpend);
        categorySpinner = (Spinner)findViewById(R.id.categoryS_editSpend);
        currencySpinner = (Spinner)findViewById(R.id.currencySS_editSpend);
        img = (ImageView)findViewById(R.id.img_editSpend);
        toolbar = (Toolbar) findViewById(R.id.htab_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(Color.GRAY);
        toolbar.setTitleTextColor(Color.WHITE);

        Intent intent = getIntent();
        update = intent.getBooleanExtra("isEdit", false);
        final long tid = intent.getLongExtra("tid",-1);
        tripId = tid;

        if(!update){
            SpendViewModel.SpendViewModelFactory factory = new SpendViewModel.SpendViewModelFactory(tid);
            viewModel = ViewModelProviders.of(this, factory)
                    .get(SpendViewModel.class);

            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle("지출 추가");
            }
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR); // current year
            mMonth = c.get(Calendar.MONTH); // current month
            mDay = c.get(Calendar.DAY_OF_MONTH); // current day
            mHour = c.get(Calendar.HOUR_OF_DAY) ;//current hour
            mMinute=c.get(Calendar.MINUTE);//current minute
            mSecond = c.get(Calendar.SECOND);
            registerDate = c.getTime();

            //mSpinner.setPrompt("통화 선택");

            viewModel.currencies.observe(this, new Observer<List<CurrencyM>>() {
                @Override
                public void onChanged(@Nullable List<CurrencyM> currencyMS) {
                    currencySpinnerSetting(currencyMS, false);
                }
            });

            viewModel.categoris.observe(this, new Observer<List<CategoryM>>() {
                @Override
                public void onChanged(@Nullable List<CategoryM> categoryMS) {
                    categorySpinnerSetting(categoryMS, false);
                }
            });

        }else{
            update = true;
            sid = intent.getIntExtra("sid", -1);

            SpendViewModel.SpendViewModelFactory2 factory = new SpendViewModel.SpendViewModelFactory2(sid, tid);
            viewModel = ViewModelProviders.of(this, factory)
                    .get(SpendViewModel.class);

            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle("지출 수정");
            }
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            viewModel.spend.observe(this, new Observer<Spend>() {
                @Override
                public void onChanged(@Nullable Spend spend) {
                    if(spend!=null){
                        updateSetting(spend);
                        currencyS = spend.getCurrencyS();
                        category = spend.getCategory();
                    }
                }
            });

            viewModel.currencies.observe(this, new Observer<List<CurrencyM>>() {
                @Override
                public void onChanged(@Nullable List<CurrencyM> currencyMS) {
                    currencySpinnerSetting(currencyMS, true);
                }
            });

            viewModel.categoris.observe(this, new Observer<List<CategoryM>>() {
                @Override
                public void onChanged(@Nullable List<CategoryM> categoryMS) {
                    categorySpinnerSetting(categoryMS, true);
                }
            });
        }


        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category = categorySpinner.getItemAtPosition(position).toString();
                if(category.equals("직접 추가")){
                    categoryAddEt.setVisibility(View.VISIBLE);
                    categoryAddEt.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            category = categoryAddEt.getText().toString();
                        }
                    });
                }else{
                    categoryAddEt.setVisibility(View.GONE);
                }
                if(category.equals("카테고리를 설정하세요")) category = null;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        currencySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currencyS = currencySpinner.getItemAtPosition(position).toString();
                if(currencyS.equals("기호를 설정하세요")) currencyS = null;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.dateEditSpend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                        EditSpendActivity.this , mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        binding.timeEditSpend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(context,
                        EditSpendActivity.this ,mHour, mMinute, true ); //시, 분, 24시간형식인지
                timePickerDialog.show();
            }
        });

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(EditSpendActivity.this);
                dialog.setMessage("추가할 사진을 선택하세요");
                dialog.setCancelable(true);
                dialog.setPositiveButton("앨범선택", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        img.setBackground(null);
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
                        img.setBackground(null);
                        selectCamera();
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        nYear = year;
        nMonth = month;
        nDay = dayOfMonth;
        registerDate = getDate(nYear,nMonth,nDay,nHour,nMinute);
        SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd");
        String to = transFormat.format(registerDate);
        binding.dateEditSpend.setText(to);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        nHour = hourOfDay;
        nMinute = minute;
        registerDate = getDate(nYear,nMonth,nDay,nHour,nMinute);
        SimpleDateFormat transFormat = new SimpleDateFormat("HH:mm");
        String to = transFormat.format(registerDate);
        binding.timeEditSpend.setText(to);
    }

    public Date getDate(int year, int month, int day, int hour, int minute) {
        Calendar cal = Calendar.getInstance();
        int sec = cal.get(Calendar.SECOND);
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minute);
        cal.set(Calendar.SECOND, sec);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public void updateSetting(Spend s){

        tripId = s.getTripId();
        registerDate = s.getRegisterDate();
        binding.priceEtEditSpend.setText(Float.toString(s.getPrice()));
        binding.titleEtEditSpend.setText(s.getTitle());
        binding.placeEtEditSpend.setText(s.getPlace());
        binding.detailEtEditSpend.setText(s.getDetail());
        category = s.getCategory();
        currencyS = s.getCurrencyS();

        SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = transFormat.format(registerDate);
        SimpleDateFormat transFormat2 = new SimpleDateFormat("HH:mm");
        String time = transFormat2.format(registerDate);
        binding.dateEditSpend.setText(date);
        binding.timeEditSpend.setText(time);

        String bi = s.getPicUri();
        if(!TextUtils.isEmpty(bi)){
            photoUri = Uri.parse(bi);
            img.setImageURI(photoUri);
        }

    }

    public void currencySpinnerSetting(List<CurrencyM> clist, boolean b){
        List<String> list = new ArrayList<String>();
        if(!b) list.add("기호를 설정하세요");

        int i=0;
        for(i=0; i<clist.size(); i++){
            list.add(clist.get(i).getTag());
        }

        ArrayAdapter spinnerAdapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, list);
        currencySpinner.setAdapter(spinnerAdapter);

        if(b){
            int pos = 0;
            for(pos=0; pos<list.size(); pos++){
                if(list.get(pos).equals(currencyS)){
                    currencySpinner.setSelection(pos);
                    return;
                }
            }
            if(pos == list.size()){
                Toast.makeText(context, "삭제된 통화, 원화로 기본설정됩니다.", Toast.LENGTH_SHORT).show();
                currencyS = "₩";
                currencySpinner.setSelection(0);
            }
        }
    }

    public void categorySpinnerSetting(List<CategoryM> clist, boolean b){
        List<String> list = new ArrayList<String>();
        if(!b) list.add("카테고리를 설정하세요");
        list.add("직접 추가");
        int i=0;
        for(i=0; i<clist.size(); i++){
            list.add(clist.get(i).getCategory());
        }

        if(!b) {
            categoriesList = list.subList(2, list.size());
        }else{
            categoriesList = list.subList(1, list.size());
        }

        ArrayAdapter spinnerAdapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, list);
        categorySpinner.setAdapter(spinnerAdapter);

        if(b){
            int pos = 0;
            for(pos=0; pos<list.size(); pos++){
                if(list.get(pos).equals(category)){
                    categorySpinner.setSelection(pos);
                    return;
                }
            }
            if(pos == list.size()){
                Toast.makeText(context, "삭제된 카테고리입니다.", Toast.LENGTH_SHORT).show();
                category = "";
                categorySpinner.setSelection(0);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.detail_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Toast.makeText(this, "지출 내역이 저장되지 않습니다", Toast.LENGTH_SHORT).show();
                finish();
                return true;
            case R.id.action_save:
                title = binding.titleEtEditSpend.getText().toString();
                price = Float.parseFloat(binding.priceEtEditSpend.getText().toString());
                place = binding.placeEtEditSpend.getText().toString();
                detailM = binding.detailEtEditSpend.getText().toString();
                spend.setTripId(tripId);
                spend.setTitle(title);
                spend.setPrice(price);
                spend.setCurrencyS(currencyS);
                spend.setCategory(category);
                if(registerDate != null) spend.setRegisterDate(registerDate);
                if(TextUtils.isEmpty(place)) spend.setPlace(place);
                if(TextUtils.isEmpty(detailM)) spend.setDetail(detailM);

                if(!update){
                    if(TextUtils.isEmpty(title) || TextUtils.isEmpty(category)
                            || TextUtils.isEmpty(Float.toHexString(price)) || TextUtils.isEmpty(currencyS)){
                        Toast.makeText(this, "필수항목 미기입으로 지출 내역이 저장되지 않습니다.", Toast.LENGTH_SHORT).show();
                    }else {
                        if(!categoriesList.contains(category)){
                            CategoryM categoryM = new CategoryM();
                            categoryM.setCategory(category);
                            categoryM.setTid(tripId);
                            viewModel.insertNewCategory(categoryM);
                        }
                        viewModel.insertNewSpend(spend);
                        Toast.makeText(this, "지출 내역이 저장되었습니다.", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                }else{
                    if(TextUtils.isEmpty(title) || TextUtils.isEmpty(category)
                            || TextUtils.isEmpty(Float.toHexString(price)) || TextUtils.isEmpty(currencyS)){
                        Toast.makeText(this, "필수항목 미기입으로 지출 내역이 수정되지 않습니다.", Toast.LENGTH_SHORT).show();
                    }else {
                        if(!categoriesList.contains(category)){
                            CategoryM categoryM = new CategoryM();
                            categoryM.setCategory(category);
                            categoryM.setTid(tripId);
                            viewModel.insertNewCategory(categoryM);
                        }
                        spend.setSid(sid);
                        viewModel.updateSpend(spend);
                        Toast.makeText(this, "지출 내역이 수정되었습니다.", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }

                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "지출 내역이 저장되지 않습니다.", Toast.LENGTH_SHORT).show();
        super.onBackPressed();
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
        img.setImageBitmap(bitmap);
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
        img.setImageBitmap(bitmap);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != RESULT_OK) return;
        switch (requestCode){
            case PICK_FROM_ALBUM:
                sendPicture(data.getData()); //갤러리에서 가져오기
                photoUri=data.getData();
                spend.setPicUri(photoUri.toString());
                photoUri=null;
                break;
            case PICK_FROM_CAMERA:
                getPictureForPhoto(); //카메라에서 가져오기
                galleryAddPic();
                spend.setPicUri(photoUri.toString());
                photoUri=null;
                break;
            default:
                break;
        }

    }
}
