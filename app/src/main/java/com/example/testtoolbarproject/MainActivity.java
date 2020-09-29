package com.example.testtoolbarproject;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {

    ActionBarDrawerToggle mDrawerToggle;
    androidx.appcompat.widget.Toolbar toolbar;
    ImageView toolbar_button;
    NestedScrollView scrollableLayout;
    String key = "blabla";
    String toConvert = "90fc708a-5cdf-4776-9945-14cb7bcd4298";


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        scrollableLayout = (NestedScrollView) findViewById(R.id.scrollable_layout);
        toolbar = findViewById(R.id.anim_toolbar);
        final DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        toolbar_button = findViewById(R.id.iv_my_event_image);
        final ActionBar actionBar = getSupportActionBar();

        toolbar_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.open();
            }
        });

//        if (actionBar != null) {
//            actionBar.setDisplayHomeAsUpEnabled(true);
        mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {

            public void onDrawerClosed(View view) {
                supportInvalidateOptionsMenu();
                //drawerOpened = false;
            }

            public void onDrawerOpened(View drawerView) {
                supportInvalidateOptionsMenu();
                //drawerOpened = true;
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                if ("en_US".equals(Resources.getSystem().getConfiguration().locale.toString())) {
                    scrollableLayout.setTranslationX((slideOffset * drawerView.getWidth()));
                } else {
                    scrollableLayout.setTranslationX((slideOffset * drawerView.getWidth()) * -1);
                }
                drawerLayout.bringChildToFront(drawerView);
                drawerLayout.requestLayout();
            }
        };
        drawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
//        }

        final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = true;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    Log.e("ch", "changed1");

                    collapsingToolbarLayout.setTitle("The Loopers");
                    isShow = true;
                } else if (isShow) {
                    Log.e("ch", "changed2");

                    collapsingToolbarLayout.setTitle(" ");//careful there should a space between double quote otherwise it wont work
                    isShow = false;
                }
            }
        });

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        BlankFragment fragment = new BlankFragment();
        fragmentTransaction.add(R.id.fl_home_container, fragment);
        fragmentTransaction.commitNow();

        drawSomething();
    }

    public void drawSomething() {

        Log.e("1", toConvert + " length: " + toConvert.length());

        String afterEncryption = encodeHashing(toConvert, key);
        String afterDec = decodeHashing(afterEncryption);

        Log.e("3",  afterEncryption + " length: " + afterEncryption.length());
        Log.e("5", afterDec  + " length: " + afterDec.length());

//        String encryptedValue = EncryptionUtils.encrypt(this, key);
//        Log.e(" Encrypted Value ", encryptedValue);
//
//        String decryptedValue = EncryptionUtils.decrypt(this, encryptedValue);
//        Log.e(" Decrypted Value ", decryptedValue);
    }

        public ArrayList<Integer> generateFibo(int countMax){
            int n1=0,n2=1,n3,count = countMax;
            ArrayList<Integer> array = new ArrayList<>();
            System.out.print(n1+" "+n2);//printing 0 and 1
            array.add(n1);
            array.add(n2);
            for(int i=2; i < count; ++i) {
                n3=n1+n2;
                array.add(n3);
                n1=n2;
                n2=n3;
            }

            return array;
        }

        public String encodeHashing(String toConvert, String key) {
            //first step - encode with xor
            String firstConversion = encode(toConvert, key);

            Log.e("2",  firstConversion + " length: " + firstConversion.length());

            ArrayList<Integer> fibonacciArray = generateFibo(firstConversion.length());
            ArrayList<Character> charsArray = new ArrayList<>();

            // second and third steps of encryption
            for (int i = 0; i < firstConversion.length(); i++) {
                Character currentChar = firstConversion.charAt(i);
                Integer charAsInt = (int) currentChar;
                int rotatedChar = Integer.rotateLeft(charAsInt, 1);
                char finalChar = (char) (rotatedChar + fibonacciArray.get(i));

                charsArray.add(finalChar);
            }



            return charsArray.toString().replaceAll(", |\\[|\\]", "");

        }

         public String decodeHashing(String toDecrypt) {

             ArrayList<Character> afterDecoding = new ArrayList<>();
             ArrayList<Integer> fibonacciArray = generateFibo(toDecrypt.length());
                //first 2 steps of decoding(fibonacci and rotating)
             for (int i = 0; i < toDecrypt.length(); i++) {
                 Character currentChar = toDecrypt.charAt(i);

                 Integer charAsInt = (int) currentChar;
                 char chari = (char) (charAsInt - fibonacciArray.get(i));

                 int rotatedChar = Integer.rotateRight(chari, 1);
//                 Log.e("check", "current char: " + currentChar + "\ncharAsInt: " + charAsInt + "\nchari: " + chari + "\nrotatedChar: " + rotatedChar  + "\nfinal: " + (char) rotatedChar);
                 afterDecoding.add((char) rotatedChar);
             }

             String after2steps = afterDecoding.toString().replaceAll(", |\\[|\\]", "");
                //last step - decode with xor
        return decode(after2steps, key );
    }

        public String decode(String s, String key) {
            return new String(xorWithKey(base64Decode(s), key.getBytes()));
        }

        public String encode(String s, String key) {
            return base64Encode(xorWithKey(s.getBytes(), key.getBytes()));
        }

        private byte[] xorWithKey(byte[] a, byte[] key) {
            byte[] out = new byte[a.length];
            for (int i = 0; i < a.length; i++) {
                out[i] = (byte) (a[i] ^ key[i%key.length]);
            }
            return out;
        }

        private byte[] base64Decode(String s) {
            return Base64.decode(s,Base64.DEFAULT);
        }

        private String base64Encode(byte[] bytes) {
            return new String(Base64.encode(bytes,Base64.DEFAULT));
        }

}